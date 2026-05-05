/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */

package com.optel.tmaster.dc.device.impl.dci.ctc.impl;

import cn.hutool.core.collection.CollUtil;
import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciTelemetryServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.telemetry.DciTelemetryTransform;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.*;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.telemetry.rev230804.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.TelemetrySystem;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.SensorGroups;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.Subscriptions;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.destination.groups.DestinationGroup;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.destination.groups.DestinationGroupKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.subscriptions.PersistentSubscriptions;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.Map;

/**
 * CtcOptTelemetryServiceImpl
 * 电信telemetry服务实现类
 * date time author
 * ─────────────────────────────
 * 2023/8/10 18:19 Dengzhiming
 * Copyright (c) 2023, H-OPTEL All Rights Reserved.
 *
 * @author Dengzhiming
 * @version V1.8.0
 */
public class CtcOptTelemetryServiceImpl extends BaseDciTelemetryServiceImpl implements IDeviceServiceWdmCtc {

    @Override
    public ListenableFuture<RpcResult<GetTelemetryOutput>> getTelemetry(GetTelemetryInput input) {
        if (input == null) {
            return RpcResultUtil.failed();
        }
        @NonNull InstanceIdentifier<TelemetrySystem> iid = create(TelemetrySystem.class);
        TelemetrySystem telemetrySystem = MountTools.queryFromOperational(input.getNeId(), iid);
        return RpcResultUtil.success(new DciTelemetryTransform().devTelemetrySystemToApi(telemetrySystem));
    }

    @Override
    public ListenableFuture<RpcResult<SetTelemetryOutput>> setTelemetry(SetTelemetryInput input) {
        if (input == null) {
            return RpcResultUtil.failed();
        }
        Map<SensorGroupKey, SensorGroup> sensorGroup = input.getSensorGroup();
        Map<DestinationGroupsKey, DestinationGroups> destinationGroups = input.getDestinationGroups();
        Map<PersistentSubscriptionKey, PersistentSubscription> persistentSubscription = input.getPersistentSubscription();
        if (CollUtil.isNotEmpty(sensorGroup)) {
            for (SensorGroup groupValue : sensorGroup.values()) {
                @NonNull KeyedInstanceIdentifier<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.sensor.groups.SensorGroup, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.sensor.groups.SensorGroupKey> iid = create(TelemetrySystem.class).child(SensorGroups.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.sensor.groups.SensorGroup.class, new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.sensor.groups.SensorGroupKey(groupValue.getSensorGroupId()));
                MountTools.doMergeToConfig(input.getNeId(), iid, new DciTelemetryTransform().apiSensorGroupToDev(groupValue));
            }
        }
        if (CollUtil.isNotEmpty(destinationGroups)) {
            for (DestinationGroups groupValue : destinationGroups.values()) {
                @NonNull KeyedInstanceIdentifier<DestinationGroup, DestinationGroupKey> iid = create(TelemetrySystem.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.DestinationGroups.class).child(DestinationGroup.class, new DestinationGroupKey(groupValue.getGroupId()));
                MountTools.doMergeToConfig(input.getNeId(), iid, new DciTelemetryTransform().apiDestinationGroupsToDev(groupValue));
            }
        }
        if (CollUtil.isNotEmpty(persistentSubscription)) {
            for (PersistentSubscription value : persistentSubscription.values()) {
                @NonNull KeyedInstanceIdentifier<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscription, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscriptionKey> iid = create(TelemetrySystem.class).child(Subscriptions.class).child(PersistentSubscriptions.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscription.class, new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscriptionKey(value.getName()));
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
            @NonNull KeyedInstanceIdentifier<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscription, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscriptionKey> iid = create(TelemetrySystem.class).child(Subscriptions.class).child(PersistentSubscriptions.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscription.class, new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscriptionKey(input.getName()));
            MountTools.deleteFromConfig(input.getNeId(), iid);
        }
        if (null != input.getSensorGroupId()) {
            @NonNull KeyedInstanceIdentifier<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.sensor.groups.SensorGroup, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.sensor.groups.SensorGroupKey> iid =
                    create(TelemetrySystem.class).child(SensorGroups.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.sensor.groups.SensorGroup.class, new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.sensor.groups.SensorGroupKey(input.getSensorGroupId()));
            MountTools.deleteFromConfig(input.getNeId(), iid);
        }
        if (null != input.getGroupId()) {
            @NonNull KeyedInstanceIdentifier<DestinationGroup, DestinationGroupKey> iid = create(TelemetrySystem.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev200630.telemetry.top.telemetry.system.DestinationGroups.class).child(DestinationGroup.class, new DestinationGroupKey(input.getGroupId()));
            MountTools.deleteFromConfig(input.getNeId(), iid);
        }
        return RpcResultUtil.success();
    }


}