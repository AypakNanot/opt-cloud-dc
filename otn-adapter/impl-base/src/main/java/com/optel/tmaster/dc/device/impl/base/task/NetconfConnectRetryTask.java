/*
 * Copyright © 2023 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.base.task;

import com.optel.tmaster.dc.device.impl.base.meta.NodeRetryMeta;
import com.optel.tmaster.dc.general.base.util.MdsalUtil;
import com.optel.tmaster.dc.general.nc.nccore.NcConstant;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.mdsal.common.api.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.Topology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * NetconfConnectRetryTask
 * date       time        author
 * ─────────────────────────────
 * 2024/1/29   14:48      tanghaiqing
 * Copyright (c) 2024, H-OPTEL All Rights Reserved.
 *
 * @author tanghaiqing
 * @version V1.0.0
 */
public class NetconfConnectRetryTask implements Runnable {
    private static final Logger LOG = LoggerFactory.getLogger(NetconfConnectRetryTask.class);
    private final DataBroker dataBroker;
    private final Map<String, NodeRetryMeta> nodeRetryMetaMap;
    private final ScheduledThreadPoolExecutor retryThreadPoolExecutor;

    public NetconfConnectRetryTask(DataBroker dataBroker, Map<String, NodeRetryMeta> nodeRetryMetaMap, ScheduledThreadPoolExecutor retryThreadPoolExecutor) {
        this.dataBroker = dataBroker;
        this.nodeRetryMetaMap = nodeRetryMetaMap;
        this.retryThreadPoolExecutor = retryThreadPoolExecutor;
    }

    @Override
    public void run() {
        nodeRetryMetaMap.forEach((nodeId, nodeRetryMeta) -> {
            if (nodeRetryMeta.tryTask()) {
                // 拿到标记量就开启重试子任务成功，先执行一次，子任务是按状态进行递归重试，标记量只有在一开始能拿到，所以开启子任务时只会在第一次执行
                taskRun(nodeRetryMeta).run();
            }
        });
    }

    private Runnable taskRun(NodeRetryMeta nodeRetryMeta) {
        return () -> {
            // 把之前的任务取消（在其它需要删除重试数据源的时候也需要写）
            Optional.ofNullable(nodeRetryMeta.getTaskFuture()).ifPresent(future -> future.cancel(false));
            getNode(nodeRetryMeta.getNodeId()).ifPresent(node -> getNetconfNode(node).ifPresent(netconfNode -> {
                // 前面的 Optional 只是为了非空判断，这里是一定会进入的
                if (Objects.equals(NetconfNodeConnectionStatus.ConnectionStatus.Connecting, netconfNode.getConnectionStatus())) {
                    LOG.warn("Netconf status is connecting, reconfigure, nodeId: {}, times: {}", node.getNodeId(), nodeRetryMeta.getTimes());
                    // 当前状态是 connecting，通过重新配置 topo 节点以触发 netconf 重连机制
                    InstanceIdentifier<Node> nodeIid = NcConstant.netconfNodeIid(node);
                    Node updateNode = MdsalUtil.readFromConfig(dataBroker, nodeIid);
                    MdsalUtil.put(dataBroker, nodeIid, updateNode, LogicalDatastoreType.CONFIGURATION);
                    // 将重试次数递增
                    nodeRetryMeta.incrementTimes();
                    // 如果此次任务还是检测在 connecting，那么放到下一个延迟任务中
                    LOG.warn("Netconf status is connecting, will try reconfigure after {} seconds, nodeId: {}", nodeRetryMeta.getNextTime(), node.getNodeId());
                    // 放入延迟任务中，下一次执行
                    nodeRetryMeta.setTaskFuture(retryThreadPoolExecutor.schedule(taskRun(nodeRetryMeta), nodeRetryMeta.getNextTime(), TimeUnit.SECONDS));
                } else {
                    LOG.warn("Netconf status is not connecting, remove retry connect mode, nodeId: {}", node.getNodeId());
                    // 如果已经不是 connecting，那么就可以删除它的重试源数据了
                    nodeRetryMetaMap.remove(nodeRetryMeta.getNodeId());
                }
            }));
        };
    }

    private Optional<Node> getNode(String nodeId) {
        Topology topology = MdsalUtil.readFromOperational(dataBroker, NcConstant.NETCONF_TOPOLOGY_IID);
        return topology.nonnullNode().values().stream().filter(e -> Objects.equals(e.getNodeId().getValue(), nodeId)).findAny();
    }

    private Optional<NetconfNode> getNetconfNode(Node node) {
        return Optional.ofNullable(node.augmentation(NetconfNode.class));
    }
}
