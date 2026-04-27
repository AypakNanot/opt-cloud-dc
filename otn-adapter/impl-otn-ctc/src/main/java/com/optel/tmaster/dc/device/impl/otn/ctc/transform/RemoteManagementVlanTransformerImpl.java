/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform;

import cn.hutool.core.map.MapUtil;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.AbstractCtcTransformer;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.EnumTransform;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.ServiceTransform;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ftr.extension.grouping.FtrExtension;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ftr.extension.grouping.FtrExtensionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ftr.extension.grouping.FtrExtensionKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.FtrExtensions;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.RemoteModules;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.remote.module.grouping.RemoteModule;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.remote.module.grouping.RemoteModuleKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.Ftrs;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ftrs.Ftr;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ftrs.FtrKey;

import java.util.*;

/**
 * <pre>
 * TMaster2000V9.00H - RemoteManagementVlanTransformerImpl
 * </pre>
 * 远端管理 vlan 配置
 * <pre>
 * date              time          author
 * ───────────────────────────────────────
 * 2025/3/18   13:22      LiHua
 * Copyright (c) 2025, H-OPTEL All Rights Reserved.
 * </pre>
 *
 * @author LiHua
 * @version V2.0.0
 */
public class RemoteManagementVlanTransformerImpl extends AbstractCtcTransformer implements CommonTransform, EnumTransform, ServiceTransform {

    public void devFtrExtToApi(FtrExtensions ftrExtensions, Map<FtrExtensionKey, FtrExtension> ftrExtensionKeyFtrExtensionMap) {
        if (Objects.isNull(ftrExtensions)) {
            return;
        }
        var ftrExtension = ftrExtensions.getFtrExtension();
        if (Objects.isNull(ftrExtension) || ftrExtension.isEmpty()) {
            return;
        }
        ftrExtension.forEach((key, value) -> {
            FtrExtensionKey k = new FtrExtensionKey(key.getLocalName(), key.getVlanId());
            FtrExtension existFtr = ftrExtensionKeyFtrExtensionMap.get(k);
            FtrExtension v = new FtrExtensionBuilder()
                    .setLocalName(value.getLocalName())
                    .setUuid(value.getUuid())
                    .setVlanId(value.getVlanId())
                    .setName(value.getName())
                    .setSerialNumber(value.getSerialNumber())
                    .setDeviceType(value.getDeviceType().getName())
                    .setModelType(value.getModelType().getName())
                    .setManufacturer(value.getManufacturer())
                    .setProductName(value.getProductName())
                    .setSoftwareVersion(value.getSoftwareVersion())
                    .setHardwareVersion(value.getHardwareVersion())
                    .setIpAddress(value.getIpAddress())
                    .setState(value.getState().getName())
                    .setMonitoringEnable(existFtr != null && existFtr.getMonitoringEnable())
                    .build();
            ftrExtensionKeyFtrExtensionMap.put(k, v);
        });
    }

    /**
     * 组合 remote module 数据和 标准ftr 的数据
     * 旧版本的数据和业务模式下的数据，是采用 remote module + 标准的ftr数据进行上报的
     *
     * @param ftrs          ftr
     * @param remoteModules remote module
     * @return 组合后的数据
     */
    public Map<FtrExtensionKey, FtrExtension> devRemoteModuleToApi(Ftrs ftrs, RemoteModules remoteModules) {
        if (Objects.isNull(remoteModules)) {
            return Collections.emptyMap();
        }
        final Collection<Ftr> ftrList = new ArrayList<>();
        if (Objects.nonNull(ftrs) && MapUtil.isNotEmpty(ftrs.getFtr())) {
            Map<FtrKey, Ftr> ftrMap = ftrs.getFtr();
            if (ftrMap != null && !ftrMap.isEmpty()) {
                ftrList.addAll(ftrMap.values());
            }
        }

        Map<RemoteModuleKey, RemoteModule> remoteModule = remoteModules.getRemoteModule();
        if (remoteModule == null || remoteModule.isEmpty()) {
            return Collections.emptyMap();
        }
        Map<FtrExtensionKey, FtrExtension> ftrExtensionHashMap = new HashMap<>();
        remoteModule.forEach((key, value) -> {
            FtrExtensionKey k = new FtrExtensionKey(value.getName(), value.getVlanId());
            FtrExtensionBuilder ftrExtensionBuilder = new FtrExtensionBuilder()
                    .setLocalName(k.getLocalName())
                    .setUuid(value.getRemoteModuleUuid())
                    .setVlanId(k.getVlanId())
                    .setName(value.getRemoteModuleName())
                    .setIpAddress(null)
                    .setState(value.getRemoteState().getName())
                    .setMonitoringEnable(value.getMonitoringEnable());
            Ftr ftr = getFtr(value, ftrList);
            if (Objects.nonNull(ftr)) {
                ftrExtensionBuilder
                        .setSerialNumber(ftr.getSerialNumber())
                        .setDeviceType(ftr.getDeviceType() == null ? "" : ftr.getDeviceType().getName())
                        .setModelType(ftr.getModelType() == null ? "" : ftr.getModelType().getName())
                        .setManufacturer(ftr.getManufacturer())
                        .setProductName(ftr.getProductName())
                        .setSoftwareVersion(ftr.getSoftwareVersion())
                        .setHardwareVersion(ftr.getHardwareVersion())
                ;
            }
            ftrExtensionHashMap.put(k, ftrExtensionBuilder.build());
        });
        return ftrExtensionHashMap;
    }

    /**
     * 获取标准ftr 信息
     *
     * @param rm   rm
     * @param ftrs 所有ftr
     * @return ftr
     */
    private Ftr getFtr(RemoteModule rm, Collection<Ftr> ftrs) {
        String uuid = rm.getRemoteModuleUuid();
        if (Objects.isNull(uuid)) {
            return null;
        }
        return ftrs.stream().filter(e -> e.getUuid().equalsIgnoreCase(uuid)).findAny().orElse(null);
    }

}