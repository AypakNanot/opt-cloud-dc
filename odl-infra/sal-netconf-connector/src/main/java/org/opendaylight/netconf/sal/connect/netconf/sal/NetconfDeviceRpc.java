/*
 * Copyright (c) 2014 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.netconf.sal.connect.netconf.sal;

import com.google.common.collect.Collections2;
import com.google.common.util.concurrent.*;
//import org.apache.logging.log4j.ThreadContext;
import org.opendaylight.mdsal.dom.api.*;
import org.opendaylight.mdsal.dom.spi.DefaultDOMRpcResult;
import org.opendaylight.netconf.api.NetconfMessage;
import org.opendaylight.netconf.sal.connect.api.MessageTransformer;
import org.opendaylight.netconf.sal.connect.api.RemoteDeviceCommunicator;
import org.opendaylight.netconf.sal.connect.netconf.listener.NetconfDeviceCommunicator;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.concepts.NoOpListenerRegistration;
import org.opendaylight.yangtools.yang.common.QName;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.data.api.schema.NormalizedNode;
import org.opendaylight.yangtools.yang.model.api.SchemaContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;

import static java.util.Objects.requireNonNull;

/**
 * Invokes RPC by sending netconf message via listener. Also transforms result from NetconfMessage to CompositeNode.
 */
public final class NetconfDeviceRpc implements DOMRpcService {
    private final RemoteDeviceCommunicator<NetconfMessage> communicator;
    private final MessageTransformer<NetconfMessage> transformer;
    private final SchemaContext schemaContext;
    private static final Logger LOG = LoggerFactory.getLogger(NetconfDeviceRpc.class);

    private static final Logger ROOT_LOG = LoggerFactory.getLogger(Logger.ROOT_LOGGER_NAME);

    private final int MAX_STR_LEN=400;


//    public void logDeviceData(String ip, int port) {
//        try {
//            String deviceKey = ip + "-" + port;
//            ThreadContext.put("deviceKey", deviceKey);
//        } finally {
//            ThreadContext.remove("deviceKey");
//        }
//    }

    public NetconfDeviceRpc(final SchemaContext schemaContext, final RemoteDeviceCommunicator<NetconfMessage> communicator, final MessageTransformer<NetconfMessage> transformer) {
        this.communicator = communicator;
        this.transformer = transformer;
        this.schemaContext = requireNonNull(schemaContext);
    }

    @Override
    @SuppressWarnings("checkstyle:IllegalCatch")
    public ListenableFuture<DOMRpcResult> invokeRpc(final QName type, final NormalizedNode input) {
        final SettableFuture<DOMRpcResult> ret = SettableFuture.create();
        if (filterLockRpc(type, input)) {
            ret.set(new DefaultDOMRpcResult());
            return ret;
        }
        NetconfMessage netconfMessage = transformer.toRpcRequest(type, input);
        final ListenableFuture<RpcResult<NetconfMessage>> delegateFuture = communicator.sendRequest(netconfMessage, type);
        String openLog = System.getProperty("openLog", "true");
        if (Objects.nonNull(openLog) && LOG.isInfoEnabled()) {
            LOG.info("send xml {}:{} {}", ((NetconfDeviceCommunicator)this.communicator).getId(),System.lineSeparator(), netconfMessage);
        }
        if (Objects.nonNull(openLog) && ROOT_LOG.isInfoEnabled()) {
            ROOT_LOG.info("send xml {}:{} {}", ((NetconfDeviceCommunicator)this.communicator).getId(),System.lineSeparator(), netconfMessage);
        }
        Futures.addCallback(delegateFuture, new FutureCallback<RpcResult<NetconfMessage>>() {
            @Override
            public void onSuccess(final RpcResult<NetconfMessage> result) {
                try {
                    String openLog = System.getProperty("openResultLog", "true");
                    if (Objects.nonNull(openLog) &&Objects.nonNull(result)&&Objects.nonNull(result.getResult())) {
                        String string = result.getResult().toString();
                        if (LOG.isDebugEnabled()) {
                            if(isGarbledCode(string)){
                                LOG.error("xml garbled code error,device:{},rpc result:{},xml:{}",((NetconfDeviceCommunicator)communicator).getId(),result.isSuccessful(),string);
                            }else{
                                LOG.debug("receive xml from device:{},rpc result:{},xml {}",((NetconfDeviceCommunicator)communicator).getId(),result.isSuccessful(),string);
                            }
                        } else if (LOG.isInfoEnabled()) {
                            if(string.length()>MAX_STR_LEN){
                                //大于400 的报文只打印前缀 后缀
                                if(isGarbledCode(string)){
                                    LOG.error("xml garbled code error,device:{},rpc result:{},prefix:{},suffix:{}",((NetconfDeviceCommunicator)communicator).getId(),result.isSuccessful(),string.substring(0,MAX_STR_LEN/2),string.substring(string.length()-MAX_STR_LEN/2));
                                } else{
                                    LOG.info("receive xml from device:{},rpc result:{}:,prefix:{},suffix:{}",((NetconfDeviceCommunicator)communicator).getId(),result.isSuccessful(),string.substring(0,MAX_STR_LEN/2),string.substring(string.length()-MAX_STR_LEN/2));
                                }
                            }else {
                                if(isGarbledCode(string)){
                                    LOG.error("xml garbled code error,device:{},rpc result:{},xml:{}",((NetconfDeviceCommunicator)communicator).getId(),result.isSuccessful(),string);
                                }else{
                                    LOG.info("receive xml from device:{},rpc result:{},xml {}",((NetconfDeviceCommunicator)communicator).getId(),result.isSuccessful(),string);
                                }
                            }
                        }

                        if (ROOT_LOG.isDebugEnabled()) {
                            if(isGarbledCode(string)){
                                ROOT_LOG.error("xml garbled code error,device:{},rpc result:{},xml:{}",((NetconfDeviceCommunicator)communicator).getId(),result.isSuccessful(),string);
                            }else{
                                ROOT_LOG.debug("receive xml from device:{},rpc result:{},xml {}",((NetconfDeviceCommunicator)communicator).getId(),result.isSuccessful(),string);
                            }
                        } else if (ROOT_LOG.isInfoEnabled()) {
                            if(string.length()>MAX_STR_LEN){
                                //大于400 的报文只打印前缀 后缀
                                if(isGarbledCode(string)){
                                    ROOT_LOG.error("xml garbled code error,device:{},rpc result:{},prefix:{},suffix:{}",((NetconfDeviceCommunicator)communicator).getId(),result.isSuccessful(),string.substring(0,MAX_STR_LEN/2),string.substring(string.length()-MAX_STR_LEN/2));
                                } else{
                                    ROOT_LOG.info("receive xml from device:{},rpc result:{}:,prefix:{},suffix:{}",((NetconfDeviceCommunicator)communicator).getId(),result.isSuccessful(),string.substring(0,MAX_STR_LEN/2),string.substring(string.length()-MAX_STR_LEN/2));
                                }
                            }else {
                                if(isGarbledCode(string)){
                                    ROOT_LOG.error("xml garbled code error,device:{},rpc result:{},xml:{}",((NetconfDeviceCommunicator)communicator).getId(),result.isSuccessful(),string);
                                }else{
                                    ROOT_LOG.info("receive xml from device:{},rpc result:{},xml {}",((NetconfDeviceCommunicator)communicator).getId(),result.isSuccessful(),string);
                                }
                            }
                        }
                    }
                    ret.set(result.isSuccessful() ? transformer.toRpcResult(result.getResult(), type) : new DefaultDOMRpcResult(result.getErrors()));
                } catch (Exception cause) {
                    ret.setException(new DefaultDOMRpcException("Unable to parse rpc reply. type: " + type + " input: " + input, cause));
                }
            }

            @Override
            public void onFailure(final Throwable cause) {
                ret.setException(new DOMRpcImplementationNotAvailableException(cause, "Unable to invoke rpc %s", type));
            }

        }, MoreExecutors.directExecutor());
        return ret;
    }
    /**
    * 校验字符串中的字符是否在 ASKII 可显示范围内
     * @param str 检测字符串
     * @return true 乱码 false 正常
    * */
    private boolean isGarbledCode(String str){
        for(int i=0;i<str.length();i++){
            char c=str.charAt(i);
            if((c<32||c>126)&&!Character.isWhitespace(c)){
                //非ASKII的可显示字符
                return  true;
            }
        }
        return false;
    }
    /**
     * 过滤掉 lock
     *
     * @param type  type
     * @param input input
     * @return true: filter ,false: lock dev
     */
    private boolean filterLockRpc(QName type, NormalizedNode input) {
        String localName = type.getLocalName();
        if ("lock".equalsIgnoreCase(localName) || "unlock".equalsIgnoreCase(localName)) {
            String flr = System.getProperty("filter_lock_rpc", "false");
            if ("true".equalsIgnoreCase(flr)) {
                ROOT_LOG.info("ignore {} rpc invoke.", localName);
                return true;
            }
        }
        return false;
    }

    @Override
    public <T extends DOMRpcAvailabilityListener> ListenerRegistration<T> registerRpcListener(final T listener) {
        listener.onRpcAvailable(Collections2.transform(schemaContext.getOperations(), input -> DOMRpcIdentifier.create(input.getQName())));

        // NOOP, no rpcs appear and disappear in this implementation
        return NoOpListenerRegistration.of(listener);
    }
}
