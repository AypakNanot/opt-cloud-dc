/*
 * Copyright © 2023 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.base.meta;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * NodeRetryMeta
 * date       time        author
 * ─────────────────────────────
 * 2024/1/29   14:55      tanghaiqing
 * Copyright (c) 2024, H-OPTEL All Rights Reserved.
 *
 * @author tanghaiqing
 * @version V1.0.0
 */
public class NodeRetryMeta {
    private String nodeId;
    private BigDecimal sleepFactor;
    private long keepaliveDelay;
    private long maxRetryTime;
    private AtomicInteger times;
    /**
     * 开启重配置触发重连的机制，这个状态量只要一开启，就不会置为 false
     */
    private volatile boolean running;
    /**
     * 当 running 为 true 时，taskFuture 表示下一次的延迟任务
     */
    private ScheduledFuture<?> taskFuture = null;

    private NodeRetryMeta() {
    }

    public NodeRetryMeta(String nodeId, BigDecimal sleepFactor, long keepaliveDelay) {
        this.nodeId = nodeId;
        this.sleepFactor = sleepFactor;
        this.keepaliveDelay = keepaliveDelay;
        this.maxRetryTime = 60 * 10L;
        this.times = new AtomicInteger(1);
        this.running = false;
    }

    public synchronized boolean tryTask() {
        boolean tryResult = false;
        if (Boolean.FALSE.equals(running)) {
            synchronized (this) {
                if (Boolean.FALSE.equals(running)) {
                    this.running = true;
                    tryResult = true;
                }
            }
        }
        return tryResult;
    }

    public String getNodeId() {
        return nodeId;
    }

    public Integer getTimes() {
        return times.get();
    }

    public void incrementTimes() {
        times.incrementAndGet();
    }

    @SuppressWarnings("squid:S1452")
    public ScheduledFuture<?> getTaskFuture() {
        return taskFuture;
    }

    public void setTaskFuture(ScheduledFuture<?> taskFuture) {
        this.taskFuture = taskFuture;
    }

    /**
     * 通过当前的 meta 数据获取下一次延迟时间
     * 以 120 心跳间隔为例
     * 1: 立即，2: 120, 3: 120 + 1.5 * 120 = 300,..., n: 60 * 10 = 600
     *
     * @return 下一次延迟时间
     */
    public Long getNextTime() {
        double result = Objects.equals(times.get(), 1) ? 0 : keepaliveDelay;
        for (int i = 1; i < times.get() - 1; i++) {
            result += keepaliveDelay * sleepFactor.doubleValue();
        }
        return Math.min(Math.round(result), maxRetryTime);
    }
}
