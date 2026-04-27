/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.universal.impl.client;

/**
 * @author dzm
 * @since 2025/1/8
 */
public interface Client {

    /**
     * 判断连接是否正常
     *
     * @return true/false
     */
    boolean isAvailable();

    /**
     * 下发命令
     *
     * @param command 命令
     * @param mode    下发模式，0：普通下发，1：tab补全
     */
    void sendCommand(String command, Integer mode);

    /**
     * 断开连接，释放线程池
     */
    void disconnect();

    /**
     * 判断是否长时间没有操作, 设置超过10分钟则认为长时间为未操作
     *
     * @return true/false
     */
    boolean isTimeOut(long now);

}
