/*
 * Copyright (c) 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.netconf.sal.connect.netconf.sal;

import com.google.common.util.concurrent.*;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.mdsal.dom.api.*;
import org.opendaylight.netconf.sal.connect.api.RemoteDeviceHandler;
import org.opendaylight.netconf.sal.connect.netconf.listener.NetconfDeviceCommunicator;
import org.opendaylight.netconf.sal.connect.netconf.listener.NetconfSessionPreferences;
import org.opendaylight.netconf.sal.connect.netconf.util.NetconfMessageTransformUtil;
import org.opendaylight.netconf.sal.connect.util.RemoteDeviceId;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.rfc8528.data.api.MountPointContext;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.opendaylight.yangtools.yang.data.api.schema.ContainerNode;
import org.opendaylight.yangtools.yang.data.api.schema.NormalizedNode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

import static com.google.common.base.Preconditions.checkState;
import static java.util.Objects.requireNonNull;
import static org.opendaylight.netconf.sal.connect.netconf.util.NetconfBaseOps.getSourceNode;
import static org.opendaylight.netconf.sal.connect.netconf.util.NetconfMessageTransformUtil.*;

/**
 * SalFacade proxy that invokes keepalive RPCs to prevent session shutdown from remote device
 * and to detect incorrect session drops (netconf session is inactive, but TCP/SSH connection is still present).
 * The keepalive RPC is a get-config with empty filter.
 */
public final class KeepaliveSalFacade implements RemoteDeviceHandler<NetconfSessionPreferences> {
    private static final Logger LOG = LoggerFactory.getLogger(KeepaliveSalFacade.class);

    // 2 minutes keepalive delay by default
    private static final long DEFAULT_DELAY = TimeUnit.MINUTES.toSeconds(2);

    // 1 minute transaction timeout by default
    private static final long DEFAULT_TRANSACTION_TIMEOUT_MILLI = TimeUnit.MILLISECONDS.toMillis(60000);

    private final KeepaliveTask keepaliveTask = new KeepaliveTask();
    private final RemoteDeviceHandler<NetconfSessionPreferences> salFacade;
    private final ScheduledExecutorService executor;

    private final long keepaliveDelaySeconds;
    private final long timeoutNanos;
    private final long delayNanos;

    //触发重连次数
    private final int reConnTimes;

    //判断定时任务只执行一次
    private boolean judgeHeart;
    //心跳超时时间
    private final long heartTimeout;
    private final RemoteDeviceId id;
    private final long maxMsgTime;
    private volatile NetconfDeviceCommunicator listener;
    private volatile DOMRpcService currentDeviceRpc;
    private final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

    public KeepaliveSalFacade(final RemoteDeviceId id, final RemoteDeviceHandler<NetconfSessionPreferences> salFacade,
                              final ScheduledExecutorService executor, final long keepaliveDelaySeconds,
                              final long requestTimeoutMillis) {
        long timeoutNanos1;
        long delayNanos1;
        int reConnTimes1;
        long heartTimeout1;
        this.id = id;
        this.salFacade = salFacade;
        this.executor = requireNonNull(executor);
        this.keepaliveDelaySeconds = keepaliveDelaySeconds;
        this.judgeHeart = false;
        try {
            String customHeartDelay = System.getProperty("custom_heart_delay_seconds", String.valueOf(keepaliveDelaySeconds));
            delayNanos1 = TimeUnit.SECONDS.toNanos(Long.parseLong(customHeartDelay));
        } catch (Exception x) {
            delayNanos1 = TimeUnit.SECONDS.toNanos(keepaliveDelaySeconds);
            LOG.warn("{}: setting task delay,{} s", id, delayNanos1);
        }
        delayNanos = delayNanos1;
        try {
            String reqTimeout = System.getProperty("custom_request_timeout_delay_millis", String.valueOf(requestTimeoutMillis));
            timeoutNanos1 = TimeUnit.MILLISECONDS.toNanos(Long.parseLong(reqTimeout));
        } catch (Exception x) {
            timeoutNanos1 = TimeUnit.MILLISECONDS.toNanos(requestTimeoutMillis);
            LOG.warn("{}: setting request timeout delay,{} nanos", id, delayNanos1);
        }

        timeoutNanos = timeoutNanos1;

        try {
            reConnTimes1 = Integer.parseInt(System.getProperty("re_conn_times", "3"));
        } catch (NumberFormatException e) {
            reConnTimes1 = 3;
            LOG.warn("{}: setting re_conn_times ,{} times", id, reConnTimes1);
        }

        this.reConnTimes = reConnTimes1;
        this.maxMsgTime = this.reConnTimes * TimeUnit.NANOSECONDS.toMillis(delayNanos);
        try {
            String reqTimeout = System.getProperty("custom_heart_timeout_delay_millis", "10000");
            heartTimeout1 = TimeUnit.MILLISECONDS.toNanos(Long.parseLong(reqTimeout));
        } catch (Exception x) {
            heartTimeout1 = TimeUnit.MILLISECONDS.toNanos(10000);
            LOG.warn("{}: setting heart timeout delay,{} nanos", id, heartTimeout1);
        }
        this.heartTimeout = heartTimeout1;
        LOG.info("{}: init KeepaliveSalFacade,keepaliveDelaySeconds:{}s,delayNanos:{} Nanos,timeoutNanos:{} Nanos,reConnTimes:{},maxMsgTime:{},heartTimeout:{}", id, keepaliveDelaySeconds, delayNanos, timeoutNanos, reConnTimes, maxMsgTime, heartTimeout);
    }

    public KeepaliveSalFacade(final RemoteDeviceId id, final RemoteDeviceHandler<NetconfSessionPreferences> salFacade,
                              final ScheduledExecutorService executor) {
        this(id, salFacade, executor, DEFAULT_DELAY, DEFAULT_TRANSACTION_TIMEOUT_MILLI);
    }

    /**
     * Set the netconf session listener whenever ready.
     *
     * @param listener netconf session listener
     */
    public void setListener(final NetconfDeviceCommunicator listener) {
        this.listener = listener;
    }

    /**
     * Cancel current keepalive and also reset current deviceRpc.
     */
    private synchronized void stopKeepalives() {

        currentDeviceRpc = null;
    }

    //失败计数 超过次数后重连 只有该处触发重连
    void checkReConnTime() {
        long lastSucMsgTime = listener.getLastSucMsgTime();
        String tf = sdf.format(new Date(lastSucMsgTime));
        LOG.warn("{} heart keepalive fail.,netconf last res msg time:{}", id, tf);
        long now = System.currentTimeMillis() - lastSucMsgTime;
        if (now > this.maxMsgTime) {
            LOG.error("{} heart keepalive fail will reconnect,max time out :{} ms,netconf last res msg time:{}", id, this.maxMsgTime, tf);
            reconnect();
        } else {
            keepaliveTask.enableKeepalive();
        }
    }

    void reconnect() {
        checkState(listener != null, "%s: Unable to reconnect, session listener is missing", id);
        stopKeepalives();
        LOG.info("{}: Reconnecting inactive netconf session", id);
        listener.disconnect();
    }

    @Override
    public void onDeviceConnected(final MountPointContext remoteSchemaContext,
                                  final NetconfSessionPreferences netconfSessionPreferences, final DOMRpcService deviceRpc) {
        onDeviceConnected(remoteSchemaContext, netconfSessionPreferences, deviceRpc, null);
    }

    @Override
    public void onDeviceConnected(final MountPointContext remoteSchemaContext,
                                  final NetconfSessionPreferences netconfSessionPreferences, final DOMRpcService deviceRpc,
                                  final DOMActionService deviceAction) {
        this.currentDeviceRpc = requireNonNull(deviceRpc);
        salFacade.onDeviceConnected(remoteSchemaContext, netconfSessionPreferences,
                new KeepaliveDOMRpcService(deviceRpc), deviceAction);

        LOG.debug("{}: Netconf session initiated, starting keepalives", id);
        LOG.trace("{}: Scheduling keepalives every {}", id, keepaliveDelaySeconds);
        LOG.info("{}: is connected ", id);
        //执行一次 不再执行
        if (!judgeHeart) {
            keepaliveTask.enableKeepalive();
            judgeHeart = true;
        }


    }

    @Override
    public void onDeviceDisconnected() {
        stopKeepalives();
        salFacade.onDeviceDisconnected();
        LOG.info("{}: is disconnected ", id);
        this.judgeHeart = false;
    }

    @Override
    public void onDeviceFailed(final Throwable throwable) {
        stopKeepalives();
        salFacade.onDeviceFailed(throwable);
        LOG.info("{}: is deviceFailed ", id);
        this.judgeHeart = false;
    }

    @Override
    public void onNotification(final DOMNotification domNotification) {
        salFacade.onNotification(domNotification);
    }

    @Override
    public void close() {
        stopKeepalives();
        salFacade.close();
        this.judgeHeart = false;
    }

    // Keepalive RPC static resources
    private static final @NonNull ContainerNode KEEPALIVE_PAYLOAD =
            NetconfMessageTransformUtil.wrap(NETCONF_GET_CONFIG_NODEID,
                    getSourceNode(NETCONF_RUNNING_QNAME), NetconfMessageTransformUtil.EMPTY_FILTER);

    /**
     * Invoke keepalive RPC and check the response. In case of any received response the keepalive
     * is considered successful and schedules next keepalive with a fixed delay. If the response is unsuccessful (no
     * response received, or the rcp could not even be sent) immediate reconnect is triggered as netconf session
     * is considered inactive/failed.
     */
    private final class KeepaliveTask implements Runnable {

        private volatile long lastActivity = System.nanoTime();

        KeepaliveTask() {

        }

        @Override
        public void run() {
            final long local = lastActivity;
            final long now = System.nanoTime();
            final long inFutureNanos = local + delayNanos - now;
            if (inFutureNanos < 0) {
                sendKeepalive(now);
            }
        }


        private synchronized void sendKeepalive(final long now) {

            final DOMRpcService deviceRpc = currentDeviceRpc;
            if (deviceRpc == null) {
                // deviceRpc is null, which means we hit the reconnect window and attempted to send keepalive while
                // we were reconnecting. Next keepalive will be scheduled after reconnect so no action necessary here.
                LOG.debug("{}: Skipping keepalive while reconnecting", id);
                return;
            }

            LOG.trace("{}: Invoking keepalive RPC", id);
            LOG.info("{}: send heart pack,{}", id, now);

            final ListenableFuture<? extends DOMRpcResult> deviceFuture =
                    currentDeviceRpc.invokeRpc(NETCONF_GET_CONFIG_QNAME, KEEPALIVE_PAYLOAD);

            final HeartRequestTimeoutTask timeout = new HeartRequestTimeoutTask(deviceFuture);

            final ScheduledFuture<?> timeoutFuture = executor.schedule(timeout, heartTimeout, TimeUnit.NANOSECONDS);

            lastActivity = now;
            deviceFuture.addListener(() -> {
                timeoutFuture.cancel(false);
                LOG.debug("{}: heart ok. cancel timeout task.", id);
            }, MoreExecutors.directExecutor());
        }

        synchronized void enableKeepalive() {
            executor.schedule(this, delayNanos, TimeUnit.NANOSECONDS);
        }
    }

    /*
     * Request timeout task is called once the requestTimeoutMillis is reached. At that moment, if the request is not
     * yet finished, we cancel it.
     */
    private final class RequestTimeoutTask implements FutureCallback<DOMRpcResult>, Runnable {
        private final @NonNull SettableFuture<DOMRpcResult> userFuture = SettableFuture.create();
        private final @NonNull ListenableFuture<? extends DOMRpcResult> deviceFuture;

        RequestTimeoutTask(final ListenableFuture<? extends DOMRpcResult> rpcResultFuture) {
            this.deviceFuture = requireNonNull(rpcResultFuture);
            Futures.addCallback(deviceFuture, this, MoreExecutors.directExecutor());
        }

        @Override
        public void run() {
            deviceFuture.cancel(true);
            userFuture.cancel(false);

        }

        @Override
        public void onSuccess(final DOMRpcResult result) {
            // No matter what response we got,
            // rpc-reply or rpc-error, we got it from device so the netconf session is OK.
            userFuture.set(result);
        }

        @Override
        public void onFailure(final Throwable throwable) {
            // User/Application RPC failed (The RPC did not reach the remote device or ...)
            // FIXME: what other reasons could cause this ?)
            LOG.warn("{}: Rpc failure detected. Reconnecting netconf session", id, throwable);
            userFuture.setException(throwable);
        }
    }

    /**
     * 心跳超时任务
     */
    private final class HeartRequestTimeoutTask implements FutureCallback<DOMRpcResult>, Runnable {
        private final @NonNull SettableFuture<DOMRpcResult> userFuture = SettableFuture.create();
        private final @NonNull ListenableFuture<? extends DOMRpcResult> deviceFuture;

        HeartRequestTimeoutTask(final ListenableFuture<? extends DOMRpcResult> rpcResultFuture) {
            this.deviceFuture = requireNonNull(rpcResultFuture);
            Futures.addCallback(deviceFuture, this, MoreExecutors.directExecutor());
        }

        @Override
        public void run() {
            deviceFuture.cancel(true);
            userFuture.cancel(false);

        }

        @Override
        public void onSuccess(final DOMRpcResult result) {
            // No matter what response we got,
            // rpc-reply or rpc-error, we got it from device so the netconf session is OK.
            userFuture.set(result);
            if (result == null) {
                LOG.warn("{} heart result is null", id);
                checkReConnTime();
                return;
            }
            if (result.getResult() == null) {
                Collection<? extends RpcError> errors = result.getErrors();
                if (!errors.isEmpty()) {
                    LOG.warn("{} heart keepalive failed with error:{}", id, errors);
                    checkReConnTime();
                } else {
                    keepaliveTask.enableKeepalive();
                }
            } else {
                //结果不为null时
                keepaliveTask.enableKeepalive();
            }

        }

        @Override
        public void onFailure(final Throwable throwable) {
            // User/Application RPC failed (The RPC did not reach the remote device or ...)
            // FIXME: what other reasons could cause this ?)
            LOG.warn("{}:heart Rpc failure detected.", id);
            userFuture.setException(throwable);
            checkReConnTime();

        }
    }

    /**
     * DOMRpcService proxy that attaches reset-keepalive-task and schedule
     * request-timeout-task to each RPC invocation.
     */
    public final class KeepaliveDOMRpcService implements DOMRpcService {
        private final @NonNull DOMRpcService deviceRpc;

        KeepaliveDOMRpcService(final DOMRpcService deviceRpc) {
            this.deviceRpc = requireNonNull(deviceRpc);
        }

        public @NonNull DOMRpcService getDeviceRpc() {
            return deviceRpc;
        }

        @Override
        public ListenableFuture<? extends DOMRpcResult> invokeRpc(final QName type, final NormalizedNode input) {

            final ListenableFuture<? extends DOMRpcResult> deviceFuture = deviceRpc.invokeRpc(type, input);

            final RequestTimeoutTask timeout = new RequestTimeoutTask(deviceFuture);
            final ScheduledFuture<?> timeoutFuture = executor.schedule(timeout, timeoutNanos, TimeUnit.NANOSECONDS);
            deviceFuture.addListener(() -> {
                timeoutFuture.cancel(false);
                LOG.debug("{}: req ok, cancel timeout task.", id);
            }, MoreExecutors.directExecutor());

            return timeout.userFuture;
        }

        @Override
        public <T extends DOMRpcAvailabilityListener> ListenerRegistration<T> registerRpcListener(final T rpcListener) {
            // There is no real communication with the device (yet), hence recordActivity() or anything
            return deviceRpc.registerRpcListener(rpcListener);
        }
    }
}
