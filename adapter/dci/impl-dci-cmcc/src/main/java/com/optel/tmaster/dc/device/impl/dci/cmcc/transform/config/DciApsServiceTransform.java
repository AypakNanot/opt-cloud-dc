/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.config;

import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base.ApsTypeTransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base.PlatformTypeTransform;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.aps.rev200210.GetApsConfigsOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.aps.rev200210.GetApsConfigsOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.aps.rev200210.SetApsConfigInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.aps.rev200210.SwitchOlpInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.aps.rev200210.get.aps.configs.output.ApsConfigs;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.aps.rev200210.get.aps.configs.output.ApsConfigsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.aps.rev200210.get.aps.configs.output.ApsConfigsKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.automatic.protection._switch.top.ApsModules;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.automatic.protection._switch.top.aps.modules.ApsModule;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.automatic.protection._switch.top.aps.modules.aps.module.Config;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.automatic.protection._switch.top.aps.modules.aps.module.ConfigBuilder;

import java.util.*;
import java.util.stream.Collectors;

/**
 * aps光保护转换类
 *
 * @author Quan Jingyuan
 * @since 2022/3/25
 **/
public class DciApsServiceTransform implements ApsTypeTransform, PlatformTypeTransform {
    public GetApsConfigsOutput devToGetApsConfigsOutputApi(ApsModules apsModules, Set<String> names) {
        if (apsModules == null || apsModules.getApsModule() == null) {
            return null;
        }
        GetApsConfigsOutputBuilder builder = new GetApsConfigsOutputBuilder();
        if (names == null) {
            builder.setApsConfigs(devToApsConfigsApiMap(apsModules.getApsModule().values()));
        } else {
            Collection<ApsModule> values = apsModules.getApsModule().values();
            builder.setApsConfigs(devToApsConfigsApiMap(values.stream().filter(e -> names.contains(e.getName())).collect(Collectors.toList())));
        }
        return builder.build();
    }
    public Config apiToApsModuleDev(SetApsConfigInput input){
        if(input==null){
            return null;
        }
        ConfigBuilder builder=new ConfigBuilder();
        builder.setForceToPort(apiToForceToPortDev(input.getForceToPort()));
        builder.setHoldOffTime(input.getHoldOffTime());
        builder.setName(input.getName());
        builder.setPrimarySwitchHysteresis(input.getPrimarySwitchHysteresis());
        builder.setPrimarySwitchThreshold(input.getPrimarySwitchThreshold());
        builder.setProtectGroupName(input.getProtectGroupName());
        builder.setRelativeSwitchThreshold(input.getRelativeSwitchThreshold());
        builder.setRelativeSwitchThresholdOffset(input.getRelativeSwitchThresholdOffset());
        builder.setRevertive(input.getRevertive());
        builder.setSecondarySwitchThreshold(input.getSecondarySwitchThreshold());
        builder.setWaitToRestoreTime(input.getWaitToRestoreTime());
        return builder.build();
    }
    public org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.SwitchOlpInput apiToSwitchOlpInputDev(SwitchOlpInput input){
        if(input==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.SwitchOlpInputBuilder builder=new org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.SwitchOlpInputBuilder();
        builder.setName(input.getName());
        builder.setSwitchToPort(apiToSwitchPortDev(input.getSwitchToPort()));
        return builder.build();
    }

    private Map<ApsConfigsKey, ApsConfigs> devToApsConfigsApiMap(Collection<ApsModule> apsModules) {
        Map<ApsConfigsKey, ApsConfigs> values = new HashMap<>(apsModules.size());
        for (ApsModule apsModule : apsModules) {
            if (apsModule.getState() != null) {
                ApsConfigsBuilder apsConfigsBuilder = new ApsConfigsBuilder();
                apsConfigsBuilder.withKey(new ApsConfigsKey(apsModule.getName()));
                apsConfigsBuilder.setActivePath(devToActivePathApi(apsModule.getState().getActivePath()));
                apsConfigsBuilder.setForceToPort(devToForceToPortApi(apsModule.getState().getForceToPort()));
                apsConfigsBuilder.setHoldOffTime(apsModule.getState().getHoldOffTime());
                apsConfigsBuilder.setName(apsModule.getState().getName());
                apsConfigsBuilder.setPrimarySwitchHysteresis(apsModule.getState().getPrimarySwitchHysteresis());
                apsConfigsBuilder.setPrimarySwitchThreshold(apsModule.getState().getPrimarySwitchThreshold());
                apsConfigsBuilder.setProtectGroupName(apsModule.getState().getProtectGroupName());
                apsConfigsBuilder.setRelativeSwitchThreshold(apsModule.getState().getRelativeSwitchThreshold());
                apsConfigsBuilder.setRelativeSwitchThresholdOffset(apsModule.getState().getRelativeSwitchThresholdOffset());
                apsConfigsBuilder.setRevertive(apsModule.getState().getRevertive());
                apsConfigsBuilder.setSecondarySwitchThreshold(apsModule.getState().getSecondarySwitchThreshold());
                apsConfigsBuilder.setWaitToRestoreTime(apsModule.getState().getWaitToRestoreTime());
                values.put(apsConfigsBuilder.key(), apsConfigsBuilder.build());
            }
        }
        return values;
    }
}
