/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.nc.nccore;

import com.optel.tmaster.dc.common.OptelDcException;
import com.optel.tmaster.dc.general.base.action.YangMode;
import com.optel.tmaster.dc.general.base.exception.device.DeviceCommicationException;
import com.optel.tmaster.dc.general.base.exception.device.DeviceOperaFailException;
import com.optel.tmaster.dc.general.base.util.MdsalUtil;
import org.opendaylight.mdsal.binding.api.*;
import org.opendaylight.mdsal.binding.dom.codec.impl.DataObjectStreamer;
import org.opendaylight.mdsal.common.api.LogicalDatastoreType;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.netconf.notification._1._0.rev080714.CreateSubscriptionInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.netconf.notification._1._0.rev080714.NotificationsService;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.netconf.notification._1._0.rev080714.StreamNameType;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.netconf.node.connection.status.available.capabilities.AvailableCapability;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.NotificationListener;
import org.opendaylight.yangtools.yang.binding.RpcService;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * ClassName: MountTools
 * <ul>
 * <li>(通过netconf查询/配置设备工具类)</li>
 * </ul>
 *
 * @author LWX 2019年9月24日下午3:30:20
 */
public class MountTools {

    private static final String FILTER_LOCK_RPC = "filter_lock_rpc";
    private static final Logger LOG = LoggerFactory.getLogger(MountTools.class);
    private static MountPointService mountPointService;

    /**
     * 需要全字段输出的YangMode集合，配置后对集合中的设备类型自动启用全字段输出
     */
    private static final Set<YangMode> EMIT_ALL_FIELDS_MODES = Collections.synchronizedSet(new HashSet<>());

    /**
     * 需要递归空字段输出的YangMode集合，配置后对集合中的设备类型自动启用递归空字段输出
     * 当容器对象为null时，递归输出该容器下所有子字段的空标签
     */
    private static final Set<YangMode> EMIT_RECURSIVE_FIELDS_MODES = Collections.synchronizedSet(new HashSet<>());

    /**
     * 添加需要全字段输出的YangMode
     */
    public static void addEmitAllFieldsMode(YangMode yangMode) {
        EMIT_ALL_FIELDS_MODES.add(yangMode);
    }

    /**
     * 移除全字段输出的YangMode
     */
    public static void removeEmitAllFieldsMode(YangMode yangMode) {
        EMIT_ALL_FIELDS_MODES.remove(yangMode);
    }

    /**
     * 添加需要递归空字段输出的YangMode
     * 注意：递归空字段输出依赖于全字段输出，此方法会自动将YangMode也添加到全字段输出集合中
     */
    public static void addEmitRecursiveFieldsMode(YangMode yangMode) {
        EMIT_ALL_FIELDS_MODES.add(yangMode);
        EMIT_RECURSIVE_FIELDS_MODES.add(yangMode);
    }

    /**
     * 移除递归空字段输出的YangMode
     */
    public static void removeEmitRecursiveFieldsMode(YangMode yangMode) {
        EMIT_RECURSIVE_FIELDS_MODES.remove(yangMode);
    }

    /**
     * 判断该设备是否需要全字段输出
     */
    private static boolean needEmitAllFields(String netconfId) {
        if (EMIT_ALL_FIELDS_MODES.isEmpty()) {
            return false;
        }
        YangMode yangMode = NcTools.getYangMode(netconfId);
        return yangMode != null && EMIT_ALL_FIELDS_MODES.contains(yangMode);
    }

    /**
     * 判断该设备是否需要递归空字段输出
     */
    private static boolean needEmitRecursiveFields(String netconfId) {
        if (EMIT_RECURSIVE_FIELDS_MODES.isEmpty()) {
            return false;
        }
        YangMode yangMode = NcTools.getYangMode(netconfId);
        return yangMode != null && EMIT_RECURSIVE_FIELDS_MODES.contains(yangMode);
    }

    public static void init(final MountPointService mountService) {
        MountTools.mountPointService = mountService;
    }

    /**
     * 得到设备挂载点
     *
     * @param nodeId netconfNodeId
     * @return MountPoint
     */
    private static MountPoint getMountPoint(final String nodeId) {
        final Optional<MountPoint> mountPointOptional = mountPointService.getMountPoint(NcConstant.netconfNodeIid(nodeId));
        if (mountPointOptional.isEmpty()) {
            //通信异常
            throw new DeviceCommicationException(nodeId, DeviceCommicationException.ConnectType.NETCONF);
        }
        return mountPointOptional.get();
    }

    /**
     * 根据netconfNodeId 得到对应设备的 DataBroker
     *
     * @param netconfNodeId String
     * @return DataBroker
     */
    private static DataBroker getNodeDataBroker(final String netconfNodeId) {
        MountPoint mountPoint = getMountPoint(netconfNodeId);
        return mountPoint.getService(DataBroker.class).get();
    }

    /**
     * 从config库查询设备数据
     *
     * @param netconfId String
     * @param iid       查询的InstanceIdentifier
     * @param <D>       查询的数据对象类型
     * @return 查询结果
     */
    public static <D extends DataObject> D queryFromConfig(String netconfId, InstanceIdentifier<D> iid) {
        return query(netconfId, iid, LogicalDatastoreType.CONFIGURATION);
    }

    /**
     * 从operational库查询
     *
     * @param netconfId String 网元id
     * @param iid       查询的InstanceIdentifier
     * @param <D>       查询的数据对象类型
     * @return 查询结果
     */
    public static <D extends DataObject> D queryFromOperational(String netconfId, InstanceIdentifier<D> iid) {
        return query(netconfId, iid, LogicalDatastoreType.OPERATIONAL);
    }

    /**
     * 查询
     */
    public static <D extends DataObject> D query(String netconfId, InstanceIdentifier<D> iid, LogicalDatastoreType configuration) {
        DataBroker ncDataBroker = getNodeDataBroker(netconfId);
        return MdsalUtil.read(ncDataBroker, iid, configuration);
    }

    /**
     * 得到设备的RPC服务
     *
     * @param nodeId     设备netconfId
     * @param rpcService RPC服务类
     * @return RPC 服务
     */
    public static <T extends RpcService> T getRpcService(String nodeId, Class<T> rpcService) {
        MountPoint mountPoint = getMountPoint(nodeId);
        RpcConsumerRegistry rpcConsumerRegistry = mountPoint.getService(RpcConsumerRegistry.class).get();
        return rpcConsumerRegistry.getRpcService(rpcService);
    }

    /**
     * 注册通知实现类
     *
     * @param nodeId    设备netconfId
     * @param listeners 需要注册的通知Listener 实现类
     * @return ListenerRegistration  instance that should be used to unregister the listener
     */
    public static <T extends NotificationListener> List<ListenerRegistration<T>> registerNotificationListener(final String nodeId, List<T> listeners) {
        List<ListenerRegistration<T>> result = new ArrayList<>();
        if (listeners == null) {
            return result;
        }
        Optional<NotificationService> serviceOptional = getMountPoint(nodeId).getService(NotificationService.class);
        if (serviceOptional.isEmpty()) {
            LOG.warn("NotificationService is no present, can not registerNotificationListener.nodeId:{}", nodeId);
            return result;
        }
        NotificationService notificationService = serviceOptional.get();
        for (T listener : listeners) {
            ListenerRegistration<T> accessRegistration = notificationService.registerNotificationListener(listener);
            result.add(accessRegistration);
        }
        return result;
    }

    /**
     * 向设备订阅通知
     *
     * @param nodeId 设备netconfId
     */
    public static void createSubscription(String nodeId) {
        createSubscription(nodeId, "Netconf");
    }

    public static void createSubscription(String nodeId, String streamName) {
        Optional<RpcConsumerRegistry> registryOptional = getMountPoint(nodeId).getService(RpcConsumerRegistry.class);
        if (registryOptional.isEmpty()) {
            LOG.warn("RpcConsumerRegistry is no present, can not createSubscription.nodeId: {}", nodeId);
            return;
        }
        NotificationsService rpcService = registryOptional.get().getRpcService(NotificationsService.class);
        CreateSubscriptionInputBuilder createSubscriptionInputBuilder = new CreateSubscriptionInputBuilder();
        if (streamName != null) {
            createSubscriptionInputBuilder.setStream(new StreamNameType(streamName));
        }
        LOG.info("Triggering notification stream {} for node {}", streamName, nodeId);
        rpcService.createSubscription(createSubscriptionInputBuilder.build());
    }

    /**
     * 得到设备支持的能力集
     *
     * @param netconfNode 设备ID
     * @return 设备支持的能力集
     */
    public static List<String> getAvailableCapability(NetconfNode netconfNode) {
        if (netconfNode == null || netconfNode.getAvailableCapabilities() == null || netconfNode.getAvailableCapabilities().getAvailableCapability() == null) {
            return new ArrayList<>();
        }
        return netconfNode.getAvailableCapabilities().getAvailableCapability().stream().map(AvailableCapability::getCapability).collect(Collectors.toList());
    }


    /**
     * 修改设备config库配置
     *
     * @param netconfId    设备netconfId
     * @param resourcePath 操作路径
     * @param resource     修改数据
     */
    public static <D extends DataObject> void doMergeToConfig(String netconfId, InstanceIdentifier<D> resourcePath, D resource) {
        boolean emitAll = needEmitAllFields(netconfId);
        boolean emitRecursive = emitAll && needEmitRecursiveFields(netconfId);
        try {
            if (emitAll) {
                DataObjectStreamer.setEmitAllFields(true);
            }
            if (emitRecursive) {
                DataObjectStreamer.setEmitRecursiveFields(true);
            }
            DataBroker nodeDataBroker = getNodeDataBroker(netconfId);
            MdsalUtil.doMerge(nodeDataBroker, resourcePath, resource, LogicalDatastoreType.CONFIGURATION);
        } catch (OptelDcException e) {
            throw buildDeviceFailException(e);
        } finally {
            if (emitRecursive) {
                DataObjectStreamer.clearEmitRecursiveFields();
            }
            if (emitAll) {
                DataObjectStreamer.clearEmitAllFields();
            }
        }
    }

    /**
     * 修改设备config配置
     *
     * @param netconfId    设备netconfId
     * @param resourcePath 修改数据路径
     * @param resource     修改数据
     */
    public static <D extends DataObject> void putConfig(String netconfId, InstanceIdentifier<D> resourcePath, D resource) {
        boolean emitAll = needEmitAllFields(netconfId);
        boolean emitRecursive = emitAll && needEmitRecursiveFields(netconfId);
        try {
            if (emitAll) {
                DataObjectStreamer.setEmitAllFields(true);
            }
            if (emitRecursive) {
                DataObjectStreamer.setEmitRecursiveFields(true);
            }
            DataBroker nodeDataBroker = getNodeDataBroker(netconfId);
            MdsalUtil.put(nodeDataBroker, resourcePath, resource, LogicalDatastoreType.CONFIGURATION);
        } catch (OptelDcException e) {
            throw buildDeviceFailException(e);
        } finally {
            if (emitRecursive) {
                DataObjectStreamer.clearEmitRecursiveFields();
            }
            if (emitAll) {
                DataObjectStreamer.clearEmitAllFields();
            }
        }
    }


    /**
     * 从config库设备删除数据
     *
     * @param netconfId    设备netconfId
     * @param resourcePath 删除的数据 路径
     * @param <D>          数据类型
     */
    public static <D extends DataObject> void deleteFromConfig(String netconfId, InstanceIdentifier<D> resourcePath) {
        DataBroker nodeDataBroker = getNodeDataBroker(netconfId);
        try {
            MdsalUtil.delete(nodeDataBroker, resourcePath, LogicalDatastoreType.CONFIGURATION);
        } catch (OptelDcException e) {
            throw buildDeviceFailException(e);
        }
    }


    private static DeviceOperaFailException buildDeviceFailException(OptelDcException e) {
        return new DeviceOperaFailException(e.getMessage(), e.getCause(), e.getRpcErrors().toArray(new RpcError[0]));
    }

    /**
     * 修改设备config库配置
     *
     * @param netconfId    设备netconfId
     * @param resourcePath 操作路径
     * @param resource     修改数据
     */
    public static <D extends DataObject> void doMergeToConfigIgnoreLock(String netconfId, InstanceIdentifier<D> resourcePath, D resource) {
        try {
            System.setProperty(FILTER_LOCK_RPC, "true");
            doMergeToConfig(netconfId, resourcePath, resource);
        } finally {
            System.setProperty(FILTER_LOCK_RPC, "false");
        }
    }

    /**
     * 从config库设备删除数据
     *
     * @param netconfId    设备netconfId
     * @param resourcePath 删除的数据 路径
     * @param <D>          数据类型
     */
    public static <D extends DataObject> void deleteFromConfigIgnoreLock(String netconfId, InstanceIdentifier<D> resourcePath) {
        try {
            System.setProperty(FILTER_LOCK_RPC, "true");
            deleteFromConfig(netconfId, resourcePath);
        } finally {
            System.setProperty(FILTER_LOCK_RPC, "false");
        }
    }
}
