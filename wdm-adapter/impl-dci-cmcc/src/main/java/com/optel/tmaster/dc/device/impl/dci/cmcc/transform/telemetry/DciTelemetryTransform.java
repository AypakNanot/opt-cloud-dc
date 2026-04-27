/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.telemetry;

import cn.hutool.core.collection.CollUtil;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.*;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.destination.groups.DestinationBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.SensorProfilesBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.SensorProfilesKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.sensor.group.*;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.telemetry.rev230804.GetTelemetryOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.telemetry.rev230804.GetTelemetryOutputBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.TelemetrySystem;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.SensorGroups;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.Subscriptions;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.destination.groups.DestinationGroup;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.destination.groups.DestinationGroupBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.destination.groups.DestinationGroupKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.destination.groups.destination.group.Destinations;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.destination.groups.destination.group.DestinationsBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.destination.groups.destination.group.destinations.Destination;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.destination.groups.destination.group.destinations.DestinationKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.includes.Include;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.includes.IncludeBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.includes.IncludeKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.sensor.paths.SensorPath;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.sensor.paths.SensorPathBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.sensor.paths.SensorPathKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.sensor.paths.sensor.path.ConfigBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.PersistentSubscriptions;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.SensorProfiles;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.State;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.sensor.profiles.SensorProfile;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.sensor.profiles.SensorProfileBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.sensor.profiles.SensorProfileKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.types.rev181121.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.IpAddress;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.Ipv4Address;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.Ipv6Address;
import org.opendaylight.yangtools.yang.common.Uint64;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

/**
 * DciTelemetryTransform
 * 极简Otn telemetry 转换类
 * date time author
 * ─────────────────────────────
 * 2023/8/7 14:19 Dengzhiming
 * Copyright (c) 2023, H-OPTEL All Rights Reserved.
 *
 * @author Dengzhiming
 * @version V1.8.0
 */
public class DciTelemetryTransform {

    public GetTelemetryOutput devTelemetrySystemToApi(TelemetrySystem telemetrySystem) {
        if (telemetrySystem == null) {
            return null;
        }
        GetTelemetryOutputBuilder outputBuilder = new GetTelemetryOutputBuilder();
        devSensorGroupsToApi(telemetrySystem.getSensorGroups(), outputBuilder);
        devDestinationGroupsToApi(telemetrySystem.getDestinationGroups(), outputBuilder);
        devSubscriptionsToApi(telemetrySystem.getSubscriptions(), outputBuilder);
        return outputBuilder.build();
    }

    private void devSensorGroupsToApi(SensorGroups sensorGroups, GetTelemetryOutputBuilder outputBuilder) {
        if (sensorGroups == null || outputBuilder == null) {
            return;
        }
        Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.SensorGroupKey, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.SensorGroup> sensorGroup = sensorGroups.getSensorGroup();
        if (CollUtil.isEmpty(sensorGroup)) {
            return;
        }
        Map<SensorGroupKey, SensorGroup> sensorGroupMap = new HashMap<>();
        for (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.SensorGroup sensorGroupValue : sensorGroup.values()) {
            SensorGroupBuilder sensorGroupBuilder = new SensorGroupBuilder();
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.SensorPaths sensorPathObject = sensorGroupValue.getSensorPaths();
            if (null != sensorPathObject && CollUtil.isNotEmpty(sensorPathObject.getSensorPath())) {
                Map<SensorPathsKey, SensorPaths> pathsMap = new HashMap<>();
                Map<SensorPathKey, SensorPath> sensorPaths = sensorPathObject.getSensorPath();
                for (SensorPath value : sensorPaths.values()) {
                    SensorPathsBuilder builder = new SensorPathsBuilder();
                    builder.setPath(value.getPath());
                    pathsMap.put(new SensorPathsKey(value.getPath()), builder.build());
                }
                sensorGroupBuilder.setSensorPaths(pathsMap);
            }
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.Includes includesObject = sensorGroupValue.getIncludes();
            if (null != includesObject && CollUtil.isNotEmpty(includesObject.getInclude())) {
                Map<IncludesKey, Includes> includesMap = new HashMap<>();
                Map<IncludeKey, Include> includes = includesObject.getInclude();
                for (Include value : includes.values()) {
                    IncludesBuilder builder = new IncludesBuilder();
                    builder.setObjectName(value.getObjectName());
                    includesMap.put(new IncludesKey(value.getObjectName()), builder.build());
                }
                sensorGroupBuilder.setIncludes(includesMap);
            }
            sensorGroupBuilder.setSensorGroupId(sensorGroupValue.getSensorGroupId());
            sensorGroupMap.put(new SensorGroupKey(sensorGroupValue.getSensorGroupId()), sensorGroupBuilder.build());
        }
        outputBuilder.setSensorGroup(sensorGroupMap);
    }

    private void devDestinationGroupsToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.DestinationGroups destinationGroups, GetTelemetryOutputBuilder outputBuilder) {
        if (destinationGroups == null || outputBuilder == null) {
            return;
        }
        Map<DestinationGroupKey, DestinationGroup> destinationGroup = destinationGroups.nonnullDestinationGroup();
        if (CollUtil.isEmpty(destinationGroup)) {
            return;
        }
        Map<DestinationGroupsKey, DestinationGroups> destinationGroupsMap = new HashMap<>();
        for (DestinationGroup groupValue : destinationGroup.values()) {
            DestinationGroupsBuilder groupsBuilder = new DestinationGroupsBuilder();
            groupsBuilder.setGroupId(groupValue.getGroupId());
            Destinations destinations = groupValue.getDestinations();
            if (destinations == null) {
                continue;
            }
            Map<DestinationKey, Destination> destination = destinations.getDestination();
            if (CollUtil.isEmpty(destination)) {
                continue;
            }
            Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.destination.groups.DestinationKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.destination.groups.Destination> destinationMap = new HashMap<>();
            for (Destination value : destination.values()) {
                DestinationBuilder builder = new DestinationBuilder();
                builder.setDestinationAddress(value.getDestinationAddress().stringValue());
                builder.setDestinationPort(value.getDestinationPort());
                destinationMap.put(new org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.destination.groups.DestinationKey(value.getDestinationAddress().stringValue(), value.getDestinationPort()), builder.build());
            }
            groupsBuilder.setDestination(destinationMap);
            destinationGroupsMap.put(new DestinationGroupsKey(groupValue.getGroupId()), groupsBuilder.build());
        }
        outputBuilder.setDestinationGroups(destinationGroupsMap);
    }

    @SuppressWarnings("java:S3776")
    private void devSubscriptionsToApi(Subscriptions subscriptions, GetTelemetryOutputBuilder outputBuilder) {
        if (subscriptions == null || outputBuilder == null || subscriptions.getPersistentSubscriptions() == null || CollUtil.isEmpty(subscriptions.getPersistentSubscriptions().getPersistentSubscription())) {
            return;
        }
        PersistentSubscriptions persistentSubscriptions = subscriptions.getPersistentSubscriptions();
        Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscriptionKey, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscription> persistentSubscription = persistentSubscriptions.getPersistentSubscription();
        Map<PersistentSubscriptionKey, PersistentSubscription> subscriptionMap = new HashMap<>();
        for (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscription subscriptionValue : persistentSubscription.values()) {
            PersistentSubscriptionBuilder subscriptionBuilder = new PersistentSubscriptionBuilder();
            State subscriptionValueState = subscriptionValue.getState();
            if (subscriptionValueState != null) {
                if (subscriptionValueState.getLocalSourceAddress() != null) {
                    subscriptionBuilder.setLocalSourceAddress(subscriptionValueState.getLocalSourceAddress().stringValue());
                }
                subscriptionBuilder.setLocalSourcePort(subscriptionValueState.getLocalSourcePort());
                if (subscriptionValueState.getProtocol() != null) {
                    subscriptionBuilder.setProtocol(subscriptionValueState.getProtocol().implementedInterface().getSimpleName());
                }
                if (subscriptionValueState.getEncoding() != null) {
                    subscriptionBuilder.setEncoding(subscriptionValueState.getEncoding().implementedInterface().getSimpleName());
                }
                subscriptionBuilder.setName(subscriptionValueState.getName());
            }
            SensorProfiles sensorProfiles = subscriptionValue.getSensorProfiles();
            if (sensorProfiles != null && CollUtil.isNotEmpty(sensorProfiles.getSensorProfile())) {
                Map<SensorProfileKey, SensorProfile> sensorProfile = sensorProfiles.getSensorProfile();
                Map<SensorProfilesKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.SensorProfiles> profilesMap = new HashMap<>();
                for (SensorProfile value : sensorProfile.values()) {
                    SensorProfilesBuilder builder = new SensorProfilesBuilder();
                    org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.sensor.profiles.sensor.profile.State state = value.getState();
                    if (state != null) {
                        builder.setSensorGroup(state.getSensorGroup());
                        // 极简otn间隔单位为s，网管侧统一使用ms，此处转化为ms
                        Uint64 sampleInterval = state.getSampleInterval();
                        Uint64 uint64 = Uint64.valueOf(sampleInterval.longValue() * 1000);
                        builder.setSampleInterval(uint64);
                        builder.setHeartbeatInterval(state.getHeartbeatInterval());
                        builder.setSuppressRedundant(state.getSuppressRedundant());
                    }
                    profilesMap.put(new SensorProfilesKey(value.getSensorGroup()), builder.build());
                }
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.DestinationGroups destinationGroups = subscriptionValue.getDestinationGroups();
                if (destinationGroups != null && CollUtil.isNotEmpty(destinationGroups.getDestinationGroup())) {
                    Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.destination.groups.DestinationGroupKey, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.destination.groups.DestinationGroup> destinationGroup = destinationGroups.getDestinationGroup();
                    Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.DestinationGroupsKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.DestinationGroups> map = new HashMap<>();
                    for (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.destination.groups.DestinationGroup value : destinationGroup.values()) {
                        org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.DestinationGroupsBuilder builder = new org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.DestinationGroupsBuilder();
                        builder.setGroupId(value.getGroupId());
                        map.put(new org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.DestinationGroupsKey(value.getGroupId()), builder.build());
                    }
                    subscriptionBuilder.setDestinationGroups(map);
                }
                subscriptionBuilder.setSensorProfiles(profilesMap);
            }
            subscriptionMap.put(new PersistentSubscriptionKey(subscriptionValue.getName()), subscriptionBuilder.build());
        }
        outputBuilder.setPersistentSubscription(subscriptionMap);
    }

    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.SensorGroup apiSensorGroupToDev(
            SensorGroup sensorGroup) {
        if (null == sensorGroup) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.SensorGroupBuilder groupBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.SensorGroupBuilder();
        groupBuilder.setSensorGroupId(sensorGroup.getSensorGroupId());
        Map<SensorPathsKey, SensorPaths> sensorPaths = sensorGroup.getSensorPaths();
        if (CollUtil.isNotEmpty(sensorPaths)) {
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.SensorPathsBuilder sensorPathsBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.SensorPathsBuilder();
            HashMap<SensorPathKey, SensorPath> map = new HashMap<>();
            for (SensorPaths value : sensorPaths.values()) {
                SensorPathBuilder builder = new SensorPathBuilder();
                ConfigBuilder configBuilder = new ConfigBuilder();
                configBuilder.setPath(value.getPath());
                builder.setConfig(configBuilder.build());
                builder.setPath(value.getPath());
                map.put(new SensorPathKey(value.getPath()), builder.build());
            }
            sensorPathsBuilder.setSensorPath(map);
            groupBuilder.setSensorPaths(sensorPathsBuilder.build());
        }
        Map<IncludesKey, Includes> includes = sensorGroup.getIncludes();
        if (CollUtil.isNotEmpty(includes)) {
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.IncludesBuilder includesBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.IncludesBuilder();
            Map<IncludeKey, Include> map = new HashMap<>();
            for (Includes value : includes.values()) {
                IncludeBuilder builder = new IncludeBuilder();
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.includes.include.ConfigBuilder configBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.includes.include.ConfigBuilder();
                configBuilder.setObjectName(value.getObjectName());
                builder.setConfig(configBuilder.build());
                builder.setObjectName(value.getObjectName());
                map.put(new IncludeKey(value.getObjectName()), builder.build());
            }
            includesBuilder.setInclude(map);
            groupBuilder.setIncludes(includesBuilder.build());
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.ConfigBuilder configBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.sensor.groups.sensor.group.ConfigBuilder();
        configBuilder.setSensorGroupId(sensorGroup.getSensorGroupId());
        groupBuilder.setConfig(configBuilder.build());
        return groupBuilder.build();
    }

    public DestinationGroup apiDestinationGroupsToDev(
            DestinationGroups destinationGroups) {
        if (null == destinationGroups) {
            return null;
        }
        Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.destination.groups.DestinationKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.destination.groups.Destination> destination = destinationGroups.getDestination();
        if (CollUtil.isEmpty(destination)) {
            return null;
        }
        DestinationGroupBuilder groupBuilder = new DestinationGroupBuilder();
        groupBuilder.setGroupId(destinationGroups.getGroupId());
        DestinationsBuilder destinationsBuilder = new DestinationsBuilder();
        Map<DestinationKey, Destination> map = new HashMap<>();
        for (org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.destination.groups.Destination value : destination.values()) {
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.destination.groups.destination.group.destinations.DestinationBuilder builder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.destination.groups.destination.group.destinations.DestinationBuilder();
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.destination.groups.destination.group.destinations.destination.ConfigBuilder configBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.destination.groups.destination.group.destinations.destination.ConfigBuilder();
            IpAddress ipAddress = isIpv4(value.getDestinationAddress()) ? new IpAddress(new Ipv4Address(value.getDestinationAddress())) : new IpAddress(new Ipv6Address(value.getDestinationAddress()));
            configBuilder.setDestinationAddress(ipAddress);
            configBuilder.setDestinationPort(value.getDestinationPort());
            builder.setConfig(configBuilder.build());
            builder.setDestinationAddress(ipAddress);
            builder.setDestinationPort(value.getDestinationPort());
            map.put(new DestinationKey(ipAddress, value.getDestinationPort()), builder.build());
        }
        destinationsBuilder.setDestination(map);
        groupBuilder.setDestinations(destinationsBuilder.build());
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.destination.groups.destination.group.ConfigBuilder configBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.destination.groups.destination.group.ConfigBuilder();
        configBuilder.setGroupId(destinationGroups.getGroupId());
        groupBuilder.setConfig(configBuilder.build());
        return groupBuilder.build();
    }

    private boolean isIpv4(String ip) {
        if (StrUtil.isBlank(ip)) {
            return false;
        }
        try {
            return InetAddress.getByName(ip).getHostAddress().equals(ip);
        } catch (UnknownHostException e) {
            return false;
        }
    }

    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscription apiPersistentSubscriptionToDev(
            PersistentSubscription subscription) {
        if (null == subscription) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscriptionBuilder subscriptionBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.PersistentSubscriptionBuilder();
        subscriptionBuilder.setName(subscription.getName());
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.ConfigBuilder subscriptionConfigBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.ConfigBuilder();
        subscriptionConfigBuilder.setName(subscription.getName());
        if (StrUtil.isNotBlank(subscription.getLocalSourceAddress())) {
            subscriptionConfigBuilder.setLocalSourceAddress(isIpv4(subscription.getLocalSourceAddress()) ? new IpAddress(new Ipv4Address(subscription.getLocalSourceAddress())) : new IpAddress(new Ipv6Address(subscription.getLocalSourceAddress())));
        }
        subscriptionConfigBuilder.setLocalSourcePort(subscription.getLocalSourcePort());
        subscriptionConfigBuilder.setProtocol(apiProtocolToDev(subscription.getProtocol()));
        subscriptionConfigBuilder.setEncoding(apiEncodingToDev(subscription.getEncoding()));
        subscriptionBuilder.setConfig(subscriptionConfigBuilder.build());
        Map<SensorProfilesKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.SensorProfiles> sensorProfiles = subscription.getSensorProfiles();
        if (CollUtil.isNotEmpty(sensorProfiles)) {
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.SensorProfilesBuilder sensorProfilesBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.SensorProfilesBuilder();
            Map<SensorProfileKey, SensorProfile> map = new HashMap<>();
            for (org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.SensorProfiles value : sensorProfiles.values()) {
                SensorProfileBuilder builder = new SensorProfileBuilder();
                builder.setSensorGroup(value.getSensorGroup());
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.sensor.profiles.sensor.profile.ConfigBuilder configBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.sensor.profiles.sensor.profile.ConfigBuilder();
                // 极简otn间隔单位为s，网管侧统一使用ms，此处转化为s
                Uint64 sampleInterval = value.getSampleInterval();
                Uint64 uint64 = Uint64.valueOf(sampleInterval.longValue() / 1000);
                configBuilder.setSampleInterval(uint64);
                configBuilder.setHeartbeatInterval(value.getHeartbeatInterval());
                configBuilder.setSuppressRedundant(value.getSuppressRedundant());
                configBuilder.setSensorGroup(value.getSensorGroup());
                builder.setConfig(configBuilder.build());
                map.put(new SensorProfileKey(value.getSensorGroup()), builder.build());
            }
            sensorProfilesBuilder.setSensorProfile(map);
            subscriptionBuilder.setSensorProfiles(sensorProfilesBuilder.build());
        }
        Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.DestinationGroupsKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.DestinationGroups> destinationGroups = subscription.getDestinationGroups();
        if (CollUtil.isNotEmpty(destinationGroups)) {
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.DestinationGroupsBuilder destinationGroupsBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.DestinationGroupsBuilder();
            Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.destination.groups.DestinationGroupKey, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.destination.groups.DestinationGroup> map = new HashMap<>();
            for (org.opendaylight.yang.gen.v1.com.optel.dci.yang.telemetry.rev220208.telemetry.data.persistent.subscription.DestinationGroups value : destinationGroups.values()) {
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.destination.groups.DestinationGroupBuilder builder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.destination.groups.DestinationGroupBuilder();
                builder.setGroupId(value.getGroupId());
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.destination.groups.destination.group.ConfigBuilder configBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.destination.groups.destination.group.ConfigBuilder();
                configBuilder.setGroupId(value.getGroupId());
                builder.setConfig(configBuilder.build());
                map.put(new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.telemetry.rev230426.telemetry.top.telemetry.system.subscriptions.persistent.subscriptions.persistent.subscription.destination.groups.DestinationGroupKey(value.getGroupId()), builder.build());
            }
            destinationGroupsBuilder.setDestinationGroup(map);
            subscriptionBuilder.setDestinationGroups(destinationGroupsBuilder.build());
        }
        return subscriptionBuilder.build();
    }

    private STREAMPROTOCOL apiProtocolToDev(String protocol) {
        if (StrUtil.isBlank(protocol)) {
            return STREAMGRPC.VALUE;
        }
        switch (protocol) {
            case "STREAM_SSH":
                return STREAMSSH.VALUE;
            case "STREAM_JSON_RPC":
                return STREAMJSONRPC.VALUE;
            case "STREAM_THRIFT_RPC":
                return STREAMTHRIFTRPC.VALUE;
            case "STREAM_WEBSOCKET_RPC":
                return STREAMWEBSOCKETRPC.VALUE;
            default:
                return STREAMGRPC.VALUE;
        }
    }

    private DATAENCODINGMETHOD apiEncodingToDev(String encoding) {
        if (StrUtil.isBlank(encoding)) {
            return ENCPROTO3.VALUE;
        }
        switch (encoding) {
            case "ENC_XML":
                return ENCXML.VALUE;
            case "ENC_JSON_IETF":
                return ENCJSONIETF.VALUE;
            default:
                return ENCPROTO3.VALUE;
        }
    }

}