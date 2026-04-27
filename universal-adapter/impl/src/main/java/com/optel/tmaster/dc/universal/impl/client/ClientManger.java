/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.universal.impl.client;

import cn.hutool.core.text.CharSequenceUtil;
import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import org.opendaylight.yang.gen.v1.com.optel.device.dev.universal.cli.rev230103.*;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.Instant;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TMaster-V8.00 - ClientManger
 * CLI实现类
 * date       time        author
 * ─────────────────────────────
 * 2023/1/3   10:43      dengzhiming
 * Copyright (c) 2023, H-OPTEL All Rights Reserved.
 *
 * @author dengzhiming
 * @version V1.8.0
 */
public class ClientManger implements CliService {
    private static final Logger log = LoggerFactory.getLogger(ClientManger.class);

    /**
     * key为roomId，value为telnet客户端对象
     */
    private final ConcurrentHashMap<String, Client> clientMap = new ConcurrentHashMap<>();

//    private final String[] types = new String[]{CliMessage.class.getName()};

    /**
     * 定时任务线程池，检测健康状态
     */
    private final ThreadPoolExecutor executor = new ThreadPoolExecutor(3, CONN_LIMIT + 1, 0L, TimeUnit.MINUTES, new SynchronousQueue<>(), new ThreadPoolExecutor.AbortPolicy());

    private volatile boolean isRun;
    private static final int CONN_LIMIT = 20;

    @Override
    public ListenableFuture<RpcResult<CliConnectOutput>> cliConnect(CliConnectInput input) {
        runTimerIfAbsent();
        var outputBuilder = new CliConnectOutputBuilder();
        String roomId = input.getRoomId();
        var origin = clientMap.get(roomId);
        if (origin != null) {
            origin.disconnect();
        } else if (clientMap.size() >= CONN_LIMIT) {
            this.releaseResource();
            if (clientMap.size() >= CONN_LIMIT) {
                return RpcResultUtil.success(outputBuilder.setResult(false).setMessage("limit").build());
            }
        }
        if (CharSequenceUtil.isNotBlank(input.getUsername()) && CharSequenceUtil.isNotBlank(input.getPassword())) {
            var sshClient = new SshClient(input.getAddress(), input.getPort().intValue(), roomId, executor, input.getUsername(), input.getPassword());
            clientMap.put(input.getRoomId(), sshClient);
            outputBuilder.setResult(sshClient.getResult()).setMessage(sshClient.getMessage());
        } else {
            var telnet = new TelnetClient(input.getAddress(), input.getPort().intValue(), roomId, executor);
            clientMap.put(input.getRoomId(), telnet);
            outputBuilder.setResult(telnet.isAvailable());
        }
        return RpcResultUtil.success(outputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<CliDisconnectOutput>> cliDisconnect(CliDisconnectInput input) {
        var client = clientMap.remove(input.getRoomId());
        if (client != null) {
            try {
                client.disconnect();
            } catch (Exception e) {
                log.error("Cli disconnect error", e);
            }
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<CliCommandOutput>> cliCommand(CliCommandInput input) {
        var telnet = clientMap.get(input.getRoomId());
        if (telnet == null) {
//            RpcRfcUtil.callInvoke(ICliSocketService.class.getName(), "onCliMessage", types, new Object[]{new CliMessage(input.getRoomId(), "Connect is not in room")});
        } else {
            telnet.sendCommand(input.getCommand(), input.getMode().intValue());
        }
        return RpcResultUtil.success();
    }

    /**
     * 释放不使用的资源
     */
    private void releaseResource() {
        long now = Instant.now().getEpochSecond();
        clientMap.forEach((k, v) -> {
            if (!v.isTimeOut(now)) {
                return;
            }
            try {
                v.disconnect();
            } catch (Exception e) {
                log.warn("Release resource error", e);
            }
            clientMap.remove(k);
        });
    }

    /**
     * 启动定时线程，检测健康状态
     */
    private void runTimerIfAbsent() {
        if (!isRun) {
            synchronized (ClientManger.class) {
                if (!isRun) {
                    executor.submit(() -> {
                        while (true) {
                            this.releaseResource();
                            try {
                                TimeUnit.MINUTES.sleep(10L);
                            } catch (InterruptedException e) {
                                Thread.currentThread().interrupt();
                            }
                        }
                    });
                    isRun = true;
                }
            }
        }
    }

}
