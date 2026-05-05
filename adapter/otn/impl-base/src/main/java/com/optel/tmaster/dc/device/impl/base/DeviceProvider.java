/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.base;

import com.google.common.collect.Maps;
import com.optel.tmaster.dc.device.impl.base.listener.UdpChannel;
import com.optel.tmaster.dc.device.impl.base.meta.NodeRetryMeta;
import com.optel.tmaster.dc.device.impl.base.task.NetconfConnectRetryTask;
import com.optel.tmaster.dc.device.impl.base.task.NetconfConnectStateMonitorTask;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * ClassName: DevconfProvider
 * <ul>
 * <li>(blueprint初始化加载模块)</li>
 * </ul>
 *
 * @author LWX 2019年9月25日上午11:14:29
 */
public class DeviceProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DeviceProvider.class);

    private final DataBroker dataBroker;
    private ScheduledThreadPoolExecutor scheduledThreadPoolExecutor = null;
    private ScheduledThreadPoolExecutor retryThreadPoolExecutor = null;
    private final Map<String, NodeRetryMeta> nodeRetryMetaMap = Maps.newConcurrentMap();

    public DeviceProvider(final DataBroker dataBroker) {
        this.dataBroker = dataBroker;
    }

    /**
     * Method called when the blueprint container is created.
     */
    public void init() {
        // 开启一个监听udp的线程
        new Thread(new UdpChannel()).start();
        // 进行监听设备连接.每两分钟监测一次（这个是因为某些设备没有重连机制，所以进行这样操作）
        scheduledThreadPoolExecutor = new ScheduledThreadPoolExecutor(2, new DefaultThreadFactory("NetconfConnectStatus"));
        retryThreadPoolExecutor = new ScheduledThreadPoolExecutor(2, new DefaultThreadFactory("NetconfConnectStatus_retry"));
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new NetconfConnectStateMonitorTask(dataBroker, nodeRetryMetaMap), 120, 120, TimeUnit.SECONDS);
        scheduledThreadPoolExecutor.scheduleAtFixedRate(new NetconfConnectRetryTask(dataBroker, nodeRetryMetaMap, retryThreadPoolExecutor), 130, 1, TimeUnit.SECONDS);
        LOG.info("DeviceProvider Session Initiated");
    }

    /**
     * Method called when the blueprint container is destroyed.
     */
    public void close() {
        if (retryThreadPoolExecutor != null) {
            retryThreadPoolExecutor.shutdown();
        }
        if (scheduledThreadPoolExecutor != null) {
            scheduledThreadPoolExecutor.shutdown();
        }
        LOG.info("DeviceProvider Closed");
    }
}