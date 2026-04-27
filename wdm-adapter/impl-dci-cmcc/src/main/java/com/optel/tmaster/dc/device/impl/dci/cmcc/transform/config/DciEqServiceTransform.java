/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.config;

import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base.EqTypeTransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.common.resource.CommonResourceTransformImpl;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.eq.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.Components;

/**
 * TODO
 * 2022/3/11 13:55
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class DciEqServiceTransform extends CommonResourceTransformImpl implements EqTypeTransform {

//    public Config1 apiUpdatePtmEqToDev(UpdatePtmEqInput input){
//        if(input==null){
//            return null;
//        }
//        Config1Builder builder = new Config1Builder();
//        builder.setWorkState(apiPtmWorkTypeToDev(input.getWorkState()));
//        return builder.build();
//    }

    public org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.SwitchMcuInput apiSwitchMcuInputToDev(org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.eq.rev200210.SwitchMcuInput input){
        if(input==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.SwitchMcuInputBuilder builder = new org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.SwitchMcuInputBuilder();
        builder.setSwitchTarget(input.getName());
        return builder.build();
    }


    public SwitchMcuOutput devSwitchMcuOutputToDevToApi(org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.SwitchMcuOutput output){
        if(output==null){
            return null;
        }
        SwitchMcuOutputBuilder builder = new SwitchMcuOutputBuilder();

        return builder.build();
    }

    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.fan.rev230426.Config1 apiUpdateFanEqToDev(UpdateFanEqInput input){
        if(input==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.fan.rev230426.Config1Builder builder =
                new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.fan.rev230426.Config1Builder();
        builder.setFanMode(apiFanModeTypeToDev(input.getFanMode()));
        builder.setFanSpeed(apiFanSpeedTypeToDev(input.getFanSpeed()));
        if(input.getFanSpeedHighThreshold()!=null){
            builder.setFanSpeedHighThreshold(input.getFanSpeedHighThreshold());
        }
        return builder.build();
    }

    public GetPsuEqOutput devGetPsuEqOutputToApi(Components components){
        if (components == null || components.getComponent() == null) {
            return null;
        }
        GetPsuEqOutputBuilder builder = new GetPsuEqOutputBuilder();
        builder.setComponent(devToComponentsToApi(components));
        return builder.build();
    }

    public GetMcuEqOutput devGetMcuEqOutputToApi(Components components){
        if (components == null || components.getComponent() == null) {
            return null;
        }

        GetMcuEqOutputBuilder builder = new GetMcuEqOutputBuilder();
        builder.setComponent(devToComponentsToApi(components));
        return builder.build();
        // @Nullable Map<ComponentKey, Component> componentMap = components.getComponent();
        // Map<McusKey, Mcus> map = new HashMap<>(componentMap.size());
        // for(Component e:componentMap.values()){
        //     Mcu mcu = e.getMcu();
        //     org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.mcu.State state
        //             = mcu.getState();
        //     org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mcu.rev200630.@Nullable State1 mcuState
        //             = state.augmentation(
        //                     org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mcu.rev200630.State1.class);
        //     if(mcuState==null){
        //         continue;
        //     }
        //     McusBuilder mcusBuilder = new McusBuilder();
        //     mcusBuilder.setName(e.getName());
        //     mcusBuilder.setMcuTotalMemory(mcuState.getMcuTotalMemory());
        //     mcusBuilder.setMcuAvailableMemory(mcuState.getMcuAvailableMemory());
        //     mcusBuilder.setMcuCpuUtilization(mcuState.getMcuCpuUtilization());
        //     mcusBuilder.setActive(mcuState.getActive());
        //     map.put(new McusKey(e.getName()),mcusBuilder.build());
        // }
        // builder.setMcus(map);
        // return builder.build();
    }

    public GetPtmEqOutput devGetPtmEqOutputToApi(Components components){
        if (components == null || components.getComponent() == null) {
            return null;
        }
        GetPtmEqOutputBuilder builder = new GetPtmEqOutputBuilder();
        builder.setComponent(devToComponentsToApi(components));
        return builder.build();
    }

    public GetFanEqOutput devGetFanEqOutputToApi(Components components){
        if (components == null || components.getComponent() == null) {
            return null;
        }
        GetFanEqOutputBuilder builder = new GetFanEqOutputBuilder();
        builder.setComponent(devToComponentsToApi(components));
        return builder.build();
    }
}
