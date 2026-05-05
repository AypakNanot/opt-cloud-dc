/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.optel.tmaster.dc.device.impl.dci.cmcc.impl;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciTelemetryServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.telemetry.DciTelemetryTransform;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.*;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.DestinationGroupsKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.SensorProfiles;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.SensorProfilesBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.SensorProfilesKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.sensor.group.*;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.telemetry.rev230804.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.TelemetrySystem;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.SensorGroups;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.Subscriptions;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.destination.groups.DestinationGroup;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.destination.groups.DestinationGroupKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.PersistentSubscriptions;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.Uint16;
import org.opendaylight.yangtools.yang.common.Uint64;

import java.util.HashMap;
import java.util.Map;

/**
 * CmccOptTelemetryServiceImpl
 * telemetry服务 实现类
 * date time author
 * ─────────────────────────────
 * 2023/8/7 13:44 Dengzhiming
 * Copyright (c) 2023, H-OPTEL All Rights Reserved.
 *
 * @author Dengzhiming
 * @version V1.8.0
 */
public class CmccOptTelemetryServiceImpl extends BaseDciTelemetryServiceImpl implements IDeviceServiceWdmCmcc {

    @Override
    public ListenableFuture<RpcResult<GetTelemetryOutput>> getTelemetry(GetTelemetryInput input) {
        if (input == null) {
            return RpcResultUtil.failed();
        }
        GetTelemetryOutput output = null;
        String property = System.getProperty("isOptTest", "false");
        if (Boolean.TRUE.equals(Boolean.valueOf(property))) {
            GetTelemetryOutputBuilder outputBuilder = new GetTelemetryOutputBuilder();
            Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.DestinationGroupsKey, DestinationGroups> destinationGroupsMap = new HashMap<>();
            Map<SensorGroupKey, SensorGroup> sensorGroupMap = new HashMap<>();
            Map<PersistentSubscriptionKey, PersistentSubscription> subscriptionMap = new HashMap<>();
            DestinationGroupsBuilder destinationGroupsBuilder = new DestinationGroupsBuilder();
            destinationGroupsBuilder.setDestination(Maps.newHashMap());
            destinationGroupsBuilder.setGroupId("1");
            destinationGroupsMap.put(new org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.DestinationGroupsKey("1"), destinationGroupsBuilder.build());
            SensorGroupBuilder sensorGroupBuilder = new SensorGroupBuilder();
            sensorGroupBuilder.setSensorGroupId("1");
            Map<SensorPathsKey, SensorPaths> pathsMap = new HashMap<>();
            SensorPathsBuilder pathsBuilder = new SensorPathsBuilder();
            pathsBuilder.setPath("/openconfig-platform:components/component/ openconfig-platform-transceiver:transceiver/state");
            pathsMap.put(new SensorPathsKey("/openconfig-platform:components/component/ openconfig-platform-transceiver:transceiver/state"), pathsBuilder.build());
            sensorGroupBuilder.setSensorPaths(pathsMap);
            Map<IncludesKey, Includes> includesMap = new HashMap<>();
            IncludesBuilder includesBuilder = new IncludesBuilder();
            includesBuilder.setObjectName("PORT-1-1");
            includesMap.put(new IncludesKey("PORT-1-1"), includesBuilder.build());
            sensorGroupBuilder.setIncludes(includesMap);
            sensorGroupMap.put(new SensorGroupKey("1"), sensorGroupBuilder.build());
            PersistentSubscriptionBuilder subscriptionBuilder = new PersistentSubscriptionBuilder();
            subscriptionBuilder.setEncoding("ENC_PROTO3");
            subscriptionBuilder.setLocalSourceAddress("127.0.0.1");
            subscriptionBuilder.setLocalSourcePort(Uint16.ONE);
            subscriptionBuilder.setName("1");
            subscriptionBuilder.setProtocol("STREAM_GRPC");
            Map<DestinationGroupsKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.DestinationGroups> destinationGroupsHashMap = new HashMap<>();
            org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.DestinationGroupsBuilder childGroupsBuilder = new org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.DestinationGroupsBuilder();
            childGroupsBuilder.setGroupId("1");
            destinationGroupsHashMap.put(new DestinationGroupsKey("1"), childGroupsBuilder.build());
            subscriptionBuilder.setDestinationGroups(destinationGroupsHashMap);
            Map<SensorProfilesKey, SensorProfiles> profilesHashMap = new HashMap<>();
            SensorProfilesBuilder profilesBuilder = new SensorProfilesBuilder();
            profilesBuilder.setHeartbeatInterval(Uint64.ONE);
            profilesBuilder.setSampleInterval(Uint64.ONE);
            profilesBuilder.setSensorGroup("1");
            profilesBuilder.setSuppressRedundant(false);
            profilesHashMap.put(new SensorProfilesKey("1"), profilesBuilder.build());
            subscriptionBuilder.setSensorProfiles(profilesHashMap);
            subscriptionMap.put(new PersistentSubscriptionKey("1"), subscriptionBuilder.build());
            outputBuilder.setDestinationGroups(destinationGroupsMap);
            outputBuilder.setPersistentSubscription(subscriptionMap);
            outputBuilder.setSensorGroup(sensorGroupMap);
            output = outputBuilder.build();
        } else {
            @NonNull InstanceIdentifier<TelemetrySystem> iid = create(TelemetrySystem.class);
            TelemetrySystem telemetrySystem = MountTools.queryFromOperational(input.getNeId(), iid);
            output = new DciTelemetryTransform().devTelemetrySystemToApi(telemetrySystem);
        }
        return RpcResultUtil.success(output);
    }

    @Override
    public ListenableFuture<RpcResult<SetTelemetryOutput>> setTelemetry(SetTelemetryInput input) {
        if (input == null) {
            return RpcResultUtil.failed();
        }
        Map<SensorGroupKey, SensorGroup> sensorGroup = input.getSensorGroup();
        Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.DestinationGroupsKey, DestinationGroups> destinationGroups = input.getDestinationGroups();
        Map<PersistentSubscriptionKey, PersistentSubscription> persistentSubscription = input.getPersistentSubscription();
        if (CollUtil.isNotEmpty(sensorGroup)) {
            for (SensorGroup groupValue : sensorGroup.values()) {
                @NonNull KeyedInstanceIdentifier<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.SensorGroup, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.SensorGroupKey> iid = create(TelemetrySystem.class).child(SensorGroups.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.SensorGroup.class, new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.SensorGroupKey(groupValue.getSensorGroupId()));
                MountTools.doMergeToConfig(input.getNeId(), iid, new DciTelemetryTransform().apiSensorGroupToDev(groupValue));
            }
        }
        if (CollUtil.isNotEmpty(destinationGroups)) {
            for (DestinationGroups groupValue : destinationGroups.values()) {
                @NonNull KeyedInstanceIdentifier<DestinationGroup, DestinationGroupKey> iid = create(TelemetrySystem.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.DestinationGroups.class).child(DestinationGroup.class, new DestinationGroupKey(groupValue.getGroupId()));
                MountTools.doMergeToConfig(input.getNeId(), iid, new DciTelemetryTransform().apiDestinationGroupsToDev(groupValue));
            }
        }
        if (CollUtil.isNotEmpty(persistentSubscription)) {
            for (PersistentSubscription value : persistentSubscription.values()) {
                @NonNull KeyedInstanceIdentifier<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscription, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscriptionKey> iid = create(TelemetrySystem.class).child(Subscriptions.class).child(PersistentSubscriptions.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscription.class, new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscriptionKey(value.getName()));
                MountTools.doMergeToConfig(input.getNeId(), iid, new DciTelemetryTransform().apiPersistentSubscriptionToDev(value));
            }
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<DeleteTelemetryOutput>> deleteTelemetry(DeleteTelemetryInput input) {
        if (null == input) {
            return RpcResultUtil.failed();
        }
        if (null != input.getName()) {
            @NonNull KeyedInstanceIdentifier<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscription, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscriptionKey> iid = create(TelemetrySystem.class).child(Subscriptions.class).child(PersistentSubscriptions.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscription.class, new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscriptionKey(input.getName()));
            MountTools.deleteFromConfig(input.getNeId(), iid);
        }
        if (null != input.getSensorGroupId()) {
            @NonNull KeyedInstanceIdentifier<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.SensorGroup, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.SensorGroupKey> iid = create(TelemetrySystem.class).child(SensorGroups.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.SensorGroup.class, new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.SensorGroupKey(input.getSensorGroupId()));
            MountTools.deleteFromConfig(input.getNeId(), iid);
        }
        if (null != input.getGroupId()) {
            @NonNull KeyedInstanceIdentifier<DestinationGroup, DestinationGroupKey> iid = create(TelemetrySystem.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.DestinationGroups.class).child(DestinationGroup.class, new DestinationGroupKey(input.getGroupId()));
            MountTools.deleteFromConfig(input.getNeId(), iid);
        }
        return RpcResultUtil.success();
    }
}