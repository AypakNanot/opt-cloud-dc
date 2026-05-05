/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.universal.impl.client;

import com.jcraft.jsch.Channel;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;
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
 * @author dzm
 * @since 2025/1/8
 */
public class SshClient implements Client {
    private static final Logger log = LoggerFactory.getLogger(SshClient.class);
    private InputStream is;
    private OutputStream os;
    private Session session;
    private Channel channel;
    private volatile long time;
//    private static final String[] types = new String[]{CliMessage.class.getName()};
//    private static final String SOCKET_NAME = ICliSocketService.class.getName();
    private final String roomId;
    private boolean result = true;
    private String message = "";
    private volatile boolean execute = true;


    public boolean getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public SshClient(String server, int port, String roomId, ThreadPoolExecutor executor, String username, String password) {
        this.roomId = roomId;
        // 创建 JSch 对象
        var jsch = new JSch();
        try {
            // 创建会话
            session = jsch.getSession(username, server, port);
            session.setPassword(password);
            session.setTimeout(3000);
            // 禁用主机密钥检查（根据需要修改）
            session.setConfig("StrictHostKeyChecking", "no");
            // 连接到服务器
            session.connect();
            // 创建交互式通道
            channel = session.openChannel("shell");
            // 获取通道的输入和输出流
            is = channel.getInputStream();
            os = channel.getOutputStream();
            // 启动通道
            channel.connect();
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
                        log.error("SSH read error", e);
                        Thread.currentThread().interrupt();
                    }
                }
            });
        } catch (Exception e) {
            result = false;
            if ("Auth fail".equals(e.getMessage())) {
                message = "ssh auth";
            } else {
                message = "common error";
            }
            log.info("SSH connect error", e);
        }
    }

    /**
     * 判断连接是否正常
     *
     * @return true/false
     */
    @Override
    public boolean isAvailable() {
        return session.isConnected();
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
                os.write((command + "\n").getBytes(StandardCharsets.UTF_8));
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
        if (channel != null) {
            channel.disconnect();
        }
        if (session != null) {
            session.disconnect();
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
}
