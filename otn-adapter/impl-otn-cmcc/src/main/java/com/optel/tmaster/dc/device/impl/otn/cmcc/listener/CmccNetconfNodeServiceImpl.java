/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.listener;

import cn.hutool.core.collection.CollUtil;
import com.optel.tmaster.dc.device.impl.otn.cmcc.impl.IDeviceServiceOtnCmcc;
import com.optel.tmaster.dc.device.impl.otn.cmcc.notification.*;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import com.optel.tmaster.dc.general.nc.nccore.common.CommonConstant;
import com.optel.tmaster.dc.general.nc.nccore.listener.NetconfNodeService;
import com.optel.tmaster.dc.general.nc.nccore.notification.NotificationUtil;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNode;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.opendaylight.yangtools.yang.binding.NotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * dc-aggregator - CmccNetconfNodeServiceImpl
 * Netconf 连接
 * date       time        author
 * ─────────────────────────────
 * 2021/10/7   10:38      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class CmccNetconfNodeServiceImpl implements NetconfNodeService, IDeviceServiceOtnCmcc {

    private static final Logger LOG = LoggerFactory.getLogger(CmccNetconfNodeServiceImpl.class);

    @Override
    public void netconfConnected(Node nodeData) {
        // 连接成功，进行注册通知
        /*NetconfNode ncNode = nodeData.augmentation(NetconfNode.class);
        List<String> availableCapability = MountTools.getAvailableCapability(ncNode);
        if (availableCapability.contains(CommonConstant.ODL_CAPABILITY)) {
            //包含DC模块，表示为ODL的controller-config，不进行注册处理
            return;
        }
        boolean isSubscription = NotificationUtil.isSupportSubscribe(availableCapability);
        String nodeId = nodeData.getNodeId().getValue();
        if (!isSubscription) {
            LOG.warn("Device is not supported NOTIFICATION_CAPABILITY.nodeId:" + nodeId);
            return;
        }
        List<NotificationListener> listeners = new ArrayList<>();
        //注册 acc-notifications 中的通知
        if (availableCapability.contains(NotificationConstant.ACC_NOTIFICATION_CAPABILITY)) {
//            listeners.add(new AccNotificationListenerImpl(nodeId));
        }
        //注册 acc-protection-group 中的通知
        if (availableCapability.contains(NotificationConstant.ACC_PROTECTION_GROUP_CAPABILITY)) {
//            listeners.add(new AccProtectionGroupListenerImpl(nodeId));
        }
        //注册 acc-otn 中的通知
        if (availableCapability.contains(NotificationConstant.ACC_OTN_YANG_CAPABILITY)) {
//            listeners.add(new AccOtnListenerImpl(nodeId));
        }
        //注册acc-alarm中的通知
        if (availableCapability.contains(NotificationConstant.ACC_ALARM_CAPABILITY)) {
//            listeners.add(new AccAlarmListenerImpl(nodeId));
        }
        //注册 acc-devm中的通知
        if (availableCapability.contains(NotificationConstant.ACC_DEVM__CAPABILITY)) {
//            listeners.add(new AccDevmListenerImpl(nodeId));
        }
        //注册 opt-otn-extension 中的通知
        if (availableCapability.contains(NotificationConstant.OPT_OTN_EXTENSION_CAPABILITY)) {
//            listeners.add(new OptOtnExtensionListenerImpl(nodeId));
        }
        if (CollUtil.isNotEmpty(listeners)) {
            MountTools.registerNotificationListener(nodeId, listeners);
            MountTools.createSubscription(nodeId);
        }*/
    }

    @Override
    public void netconfDisconnected(Node nodeData) {

    }

}
