/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.universal.impl.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * TMaster-V8.00 - TelnetClient
 * CLI Telnet Client自定义
 * date       time        author
 * ─────────────────────────────
 * 2023/1/3   11:26      dengzhiming
 * Copyright (c) 2023, H-OPTEL All Rights Reserved.
 *
 * @author dengzhiming
 * @version V1.8.0
 */
public class TelnetClient implements Client {
    private static final Logger log = LoggerFactory.getLogger(TelnetClient.class);
    private final org.apache.commons.net.telnet.TelnetClient telnet;
    private InputStream is;
    private OutputStream os;
    private volatile boolean execute = true;
    private volatile long time;
//    private static final String[] types = new String[]{CliMessage.class.getName()};
//    private static final String SOCKET_NAME = ICliSocketService.class.getName();
    private final String roomId;

    public TelnetClient(String server, int port, String roomId, ThreadPoolExecutor executor) {
        this.roomId = roomId;
        telnet = new org.apache.commons.net.telnet.TelnetClient();
        telnet.setConnectTimeout(3000);
        try {
            telnet.connect(server, port);
            telnet.setKeepAlive(true);
            is = telnet.getInputStream();
            os = telnet.getOutputStream();
            time = Instant.now().getEpochSecond();
            executor.submit(() -> {
                while (execute) {
                    try {
                        int temp = is.available();
                        if (temp > 0) {
                            var sb = new StringBuilder();
                            var buffer = new byte[512];
                            do {
                                int len = is.read(buffer, 0, Math.min(temp, 512));
                                sb.append(new String(buffer, 0, len));
                            } while ((temp = is.available()) > 0);
//                            RpcRfcUtil.callInvoke(SOCKET_NAME, "onCliMessage", types, new Object[]{new CliMessage(roomId, sb.toString())});
                        }
                        TimeUnit.MILLISECONDS.sleep(50L);
                    } catch (InterruptedException | IOException e) {
                        log.error("Telnet read error", e);
                        Thread.currentThread().interrupt();
                    }
                }
            });
        } catch (Exception e) {
            log.error("Telnet connect error", e);
        }
    }

    /**
     * 下发命令
     *
     * @param command 命令
     * @param mode    下发模式，0：普通下发，1：tab补全
     */
    @Override
    public void sendCommand(String command, Integer mode) {
        time = Instant.now().getEpochSecond();
        try {
            if (mode == 0) {
                os.write((command + System.lineSeparator()).getBytes(StandardCharsets.UTF_8));
            } else {
                os.write(command.getBytes(StandardCharsets.UTF_8));
            }
            os.flush();
        } catch (IOException e) {
//            RpcRfcUtil.callInvoke(SOCKET_NAME, "onCliMessage", types, new Object[]{new CliMessage(roomId, "Connect is not available")});
        }
    }

    /**
     * 断开连接，释放线程池
     */
    @Override
    public void disconnect() {
        execute = false;
        if (os != null) {
            try {
                os.close();
            } catch (IOException e) {
                log.error("output stream disconnect error", e);
            }
        }
        if (is != null) {
            try {
                is.close();
            } catch (IOException e) {
                log.error("input stream disconnect error", e);
            }
        }
        if (telnet != null) {
            try {
                telnet.disconnect();
            } catch (Exception e) {
                log.error("telnet disconnect error", e);
            }
        }
    }

    /**
     * 判断是否长时间没有操作, 设置超过10分钟则认为长时间为未操作
     *
     * @return true/false
     */
    @Override
    public boolean isTimeOut(long now) {
        return now - time > 600;
    }

    /**
     * 判断连接是否正常
     *
     * @return true/false
     */
    @Override
    public boolean isAvailable() {
        return telnet.isConnected() && telnet.isAvailable();
    }

}
