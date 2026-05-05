/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.base.task;

import com.google.common.collect.Lists;
import com.optel.tmaster.dc.device.impl.base.meta.NodeRetryMeta;
import com.optel.tmaster.dc.general.base.util.MdsalUtil;
import com.optel.tmaster.dc.general.nc.nccore.NcConstant;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * dc-aggregator - NetconfConnectStateMonitorTask
 * Netconf 连接状态监测任务
 * date       time        author
 * ─────────────────────────────
 * 2020/12/24   13:30      liwenxue
 * Copyright (c) 2020, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class NetconfConnectStateMonitorTask implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(NetconfConnectStateMonitorTask.class);
    private final DataBroker dataBroker;
    private final Map<String, NodeRetryMeta> nodeRetryMetaMap;

    public NetconfConnectStateMonitorTask(DataBroker dataBroker, Map<String, NodeRetryMeta> nodeRetryMetaMap) {
        this.dataBroker = dataBroker;
        this.nodeRetryMetaMap = nodeRetryMetaMap;
    }

    @Override
    public void run() {
        LOG.debug("NetconfConnectStateMonitorTask run");
        List<String> nodeIds = Lists.newLinkedList();
        for (Node node : MdsalUtil.readFromOperational(dataBroker, NcConstant.NETCONF_TOPOLOGY_IID).nonnullNode().values()) {
            NetconfNode ncNode = node.augmentation(NetconfNode.class);
            String nodeId = node.key().getNodeId().getValue();
            nodeIds.add(nodeId);
            if (ncNode != null) {
                NetconfNodeConnectionStatus.ConnectionStatus status = ncNode.getConnectionStatus();
                if (NetconfNodeConnectionStatus.ConnectionStatus.Connecting == status) {
                    // 如果重试数据源里没有才加入
                    nodeRetryMetaMap.putIfAbsent(nodeId, new NodeRetryMeta(nodeId, ncNode.getSleepFactor().decimalValue(), ncNode.getKeepaliveDelay().toJava()));
                }
            }
        }
        // 将重试数据源中多的数据进行删除
        nodeRetryMetaMap.entrySet().removeIf(entry -> {
            boolean needRemove = !nodeIds.contains(entry.getKey());
            if (needRemove) {
                Optional.ofNullable(entry.getValue().getTaskFuture()).ifPresent(future -> future.cancel(false));
            }
            return needRemove;
        });
    }
}
