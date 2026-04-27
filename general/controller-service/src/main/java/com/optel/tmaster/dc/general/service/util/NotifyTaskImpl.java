/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.service.util;

import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.thread.ExecutorBuilder;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.json.JSONUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Function;

/**
 * <pre>
 * TMaster2000V8.22 - NotifyTaskImpl
 * </pre>
 * 后台任务发送器
 * <pre>
 * date              time          author
 * ───────────────────────────────────────
 * 2023-12-19 0019   15:02:26      LiHua
 * Copyright (c) 2023, H-OPTEL All Rights Reserved.
 * </pre>
 *
 * @author LiHua
 * @version V2.0.0
 */
public class NotifyTaskImpl implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(NotifyTaskImpl.class);
    /**
     * 通知队列
     */
    private static final LinkedBlockingQueue<RpcParameter> NOTIFY_QUEUE = new LinkedBlockingQueue<>(100_000);
    private static final AtomicBoolean FLAG = new AtomicBoolean(false);
    /**
     * 这里没有对告警做处理，只能是单线程操作，防止后产生的告警，先到达网管，一个线程来放置任务，另外一个线程来执行任务
     * LiHua 在蜂拥的时候，会导致任务加不进去，先将队列扩大到10w,正确的做法应该是 还加一个线层 一直去检测 对端服务器是否正常，如果不成功，则不进行尝试
     */
    private static final ThreadPoolExecutor EXECUTOR = ExecutorBuilder.create().setCorePoolSize(2).setMaxPoolSize(2)
            .setWorkQueue(new LinkedBlockingQueue<>(100_000)).setKeepAliveTime(0).build();

    public void put(RpcParameter notify) {
        NOTIFY_QUEUE.add(notify);
    }

    private RpcParameter get() {
        try {
            return NOTIFY_QUEUE.poll(1, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            FLAG.set(false);
            Thread.currentThread().interrupt();
        }
        return null;
    }

    /**
     * Method called when the blueprint container is created.
     */
    public void init() {
        FLAG.set(true);
        EXECUTOR.submit(this);
    }


    /**
     * Method called when the blueprint container is destroyed.
     */
    public void close() {
        FLAG.set(false);
    }

    private Runnable newTask(RpcParameter rpc) {
        return () -> {
            final String corePath = System.getProperty("system.core.path", "127.0.0.1:38551");
            final String reqUrl = "http://" + corePath + "/sys-api/rpc/dc-any";
            final HttpRequest req = HttpRequest.post(reqUrl)
                    // 8 分钟超时
                    .timeout(1000 * 60 * 8).bearerAuth("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoicm9vdCIsImV4cCI6MTY0MzA3NjUxNX0.UBDJYkldh-31NXB_jdPvHA8n94101ypTkSBwZVaqJiA");
            String p = JSONUtil.toJsonStr(rpc);
            LOG.debug("dc->notify, Parameter:{}", p);
            req.body(p);
            try (HttpResponse response = req.execute()) {
                // 2xx 成功状态码
                int status = response.getStatus();
                LOG.debug("notify ok.status:{},url:{}", status, reqUrl);
            } catch (Exception ex) {
                if (ex.getCause() instanceof java.net.ConnectException) {
                    LOG.error("remote server not start...url:{}", reqUrl);
                    return;
                }
                if (ex.getCause() instanceof IORuntimeException) {
                    LOG.error("IO Exception,{}", ex.getMessage());
                    return;
                }
                LOG.error("with error.", ex);
            }
        };
    }

    public <V> V runTask(RpcParameter rpc, Function<String, V> transform) {
        final String reqUrl = "http://" + System.getProperty("system.core.path", "127.0.0.1:38551") + "/sys-api/rpc/dc-any";
        final HttpRequest req = HttpRequest.post(reqUrl)
                .timeout(1000 * 60 * 8)
                .body(JSONUtil.toJsonStr(rpc))
                .bearerAuth("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJuYW1lIjoicm9vdCIsImV4cCI6MTY0MzA3NjUxNX0.UBDJYkldh-31NXB_jdPvHA8n94101ypTkSBwZVaqJiA");
        try (HttpResponse response = req.execute()) {
            return transform.apply(response.body());
        } catch (Exception ex) {
            if (ex.getCause() instanceof java.net.ConnectException) {
                LOG.error("remote server not start...url:{}", reqUrl);
            } else if (ex.getCause() instanceof IORuntimeException) {
                LOG.error("IO Exception,{}", ex.getMessage());
            } else {
                LOG.error("with error.", ex);
            }
            return null;
        }
    }

    @Override
    public void run() {
        while (FLAG.get()) {
            try {
                RpcParameter rpc = this.get();
                if (Objects.nonNull(rpc)) {
                    EXECUTOR.submit(this.newTask(rpc));
                }
            } catch (Exception e) {
                LOG.error("submit notify error.", e);
            }
        }
    }
}