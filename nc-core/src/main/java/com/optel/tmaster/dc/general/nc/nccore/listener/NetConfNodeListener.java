/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.nc.nccore.listener;

import com.optel.tmaster.dc.general.base.action.DeviceClassFactory;
import com.optel.tmaster.dc.general.base.action.YangMode;
import com.optel.tmaster.dc.general.base.util.DcFileUtil;
import com.optel.tmaster.dc.general.base.util.MdsalUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import com.optel.tmaster.dc.general.nc.nccore.NcConstant;
import com.optel.tmaster.dc.general.nc.nccore.NcTools;
import com.optel.tmaster.dc.general.nc.nccore.common.CommonConstant;
import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.mdsal.binding.api.DataObjectModification;
import org.opendaylight.mdsal.binding.api.DataTreeChangeListener;
import org.opendaylight.mdsal.binding.api.DataTreeModification;
import org.opendaylight.netconf.console.api.NetconfCommands;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.Uri;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeBuilder;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus.ConnectionStatus;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.NotificationListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * ClassName: NetConfNodeListener
 * <ul>
 * <li>(监听设备netconf的连接状态)</li>
 * </ul>
 *
 * @author LWX 2019年10月9日上午9:51:22
 */
@SuppressWarnings("all")
public class NetConfNodeListener implements DataTreeChangeListener<Node> {

    private static final Logger LOG = LoggerFactory.getLogger(NetConfNodeListener.class);
    private DataBroker dataBroker;
    private NetconfCommands netconfCommands;
    private List<ListenerRegistration<NotificationListener>> listenerRegistrations = null;
    public NetConfNodeListener(DataBroker dataBroker, NetconfCommands netconfCommands) {
        this.dataBroker = dataBroker;
        this.netconfCommands = netconfCommands;
    }

    /**
     * node数据变化触发改变
     *
     * @param changes 数据变化集合，包含node的所有数据
     */
    @Override
    public void onDataTreeChanged(Collection<DataTreeModification<Node>> changes) {
        // 遍历数据变化集合
        for (final DataTreeModification<Node> change : changes) {
            LOG.info("custom log of NetConfNodeListener begin");
            // 根据变换的数据获取node数据，包含变化之前和变换之后的数据
            final DataObjectModification<Node> rootNode = change.getRootNode();
            // 根据对象的改变状态来进行分别处理
            switch (rootNode.getModificationType()) {
                case SUBTREE_MODIFIED:
                case WRITE:
                    this.onWriteOrSubtreeModifiedHandler(rootNode);
                    break;
                case DELETE:
                    this.onDeleteHandler(rootNode);
                    break;
                default:
                    break;
            }
            LOG.info("custom log of NetConfNodeListener end");
        }
    }

    private void onWriteOrSubtreeModifiedHandler(DataObjectModification<Node> rootNode) {
        // 状态为改变或者修改进行同一处理
        // 获取变换之后的数据
        Node nodeData = rootNode.getDataAfter();
        if (nodeData == null) {
            LOG.info("custom log of NetConfNodeListener empty nodeId, type: {}", rootNode.getModificationType());
            return;
        }
        String neId = nodeData.getNodeId().getValue();
        // 从对象中获取所需数据
        NetconfNode ncNode = nodeData.augmentation(NetconfNode.class);
        if (ncNode != null) {
            ConnectionStatus status = ncNode.getConnectionStatus();
            if (status == null) {
                LOG.info("custom log of NetConfNodeListener empty status, current nodeId: {}, type: {}", neId, rootNode.getModificationType());
                return;
            }
            LOG.info("custom log of NetConfNodeListener current nodeId: {}, type: {}, status: {}", neId, rootNode.getModificationType(), status);
            switch (status) {
                case Connected:
                    // 连接成功进行的处理
                    LOG.info("netconf node,nodeId: {}  connected.", nodeData.getNodeId().getValue());
                    var netconfNode = getNetconfNode(neId);
                    if (netconfNode != null && !DeviceType.getSchemaCacheDirectory().contains(netconfNode.getSchemaCacheDirectory())) {
                        Optional<YangMode> nodeYangMode = NcTools.getNodeYangMode(nodeData);
                        if (nodeYangMode.isPresent()) {
                            var deviceType = DeviceType.getDeviceTypeByType(nodeYangMode.get().getMode());
                            if (deviceType == null) {
                                break;
                            }
                            updateNode(netconfNode, neId, deviceType.getDir());
                        }
                    } else {
                        List<String> availableCapability = MountTools.getAvailableCapability(ncNode);
                        if (availableCapability.contains(CommonConstant.ODL_CAPABILITY)) {
                            // 包含DC模块，表示为ODL的controller-config，不进行处理
                            return;
                        }
                        Optional<YangMode> nodeYangMode = NcTools.getNodeYangMode(nodeData);
                        if (nodeYangMode.isPresent()) {
                            YangMode yangMode = nodeYangMode.get();
                            NetconfNodeService netconfNodeService = DeviceClassFactory.getDeviceExecutor(yangMode, NetconfNodeService.class);
                            netconfNodeService.netconfConnected(nodeData);
                        }
                        // 通知 flex-server，设备netconf连接成功。
                        RpcRfcUtil.callInvoke("com.optel.api.generic.service.DeviceConnect", "netconfConnected", new String[]{"java.lang.String"}, new Object[]{neId});
                    }
                    break;
                case Connecting:
                    // 正在连接进行的处理
                    if (isNormal(neId)) {
                        netconfDisconnected(nodeData);
                    }
                    LOG.info("netconf node,nodeId: {}  connecting.", nodeData.getNodeId().getValue());
                    break;
                case UnableToConnect:
                    // 不能连接进行的处理
                    netconfDisconnected(nodeData);
                    LOG.info("netconf node,nodeId: {}  UnableToConnect.", nodeData.getNodeId().getValue());
                    break;
                default:
                    break;
            }
        }
    }

    private void onDeleteHandler(DataObjectModification<Node> rootNode) {
        // 数据删除所做处理
        Node dataBefore = rootNode.getDataBefore();
        if (Objects.nonNull(dataBefore)) {
            String nodeId = Optional.ofNullable(dataBefore.getNodeId()).map(Uri::getValue).orElse(null);
            LOG.info("custom log of NetConfNodeListener current nodeId: {}, type: {}", nodeId, rootNode.getModificationType());
            if (isNormal(nodeId)) {
                netconfDisconnected(dataBefore);
            }
            DcFileUtil.deleteCacheTemp(nodeId);
        } else {
            LOG.info("custom log of NetConfNodeListener empty nodeId, type: {}", rootNode.getModificationType());
        }
    }

    /**
     * 断开连接
     *
     * @param nodeData 网元 NODE
     */
    private void netconfDisconnected(Node nodeData) {
        //通知 otn-server，设备netconf断开连接。
        RpcRfcUtil.callInvoke("com.optel.api.generic.service.DeviceConnect", "netconfDisConnected", new String[]{"java.lang.String"}, new Object[]{nodeData.getNodeId().getValue()});
    }


    /**
     * 关闭
     */
    public void close() {
        if (listenerRegistrations != null) {
            for (ListenerRegistration<NotificationListener> listener : listenerRegistrations) {
                listener.close();
            }
        }
    }

    private NetconfNode getNetconfNode(String neId) {
        InstanceIdentifier<Node> path = NcConstant.netconfNodeIid(neId);
        var node = MdsalUtil.readFromConfig(dataBroker, path);
        if (node == null) {
            return null;
        }
        return node.augmentation(NetconfNode.class);
    }

    public void updateNode(NetconfNode ncNode, String neId, String schemaCacheDirectory) {
        netconfCommands.disconnectDevice(neId);
        var builder = new NetconfNodeBuilder(ncNode);
        builder.setSchemaCacheDirectory(schemaCacheDirectory);
        netconfCommands.connectDevice(builder.build(), neId);
    }

    public boolean isNormal(String neId) {
        var netconfNode = getNetconfNode(neId);
        if (netconfNode == null) {
            return false;
        }
        return DeviceType.getSchemaCacheDirectory().contains(netconfNode.getSchemaCacheDirectory());
    }

}