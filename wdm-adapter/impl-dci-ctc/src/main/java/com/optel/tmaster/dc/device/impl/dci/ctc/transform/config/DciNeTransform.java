/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.transform.config;

import com.optel.tmaster.dc.device.impl.dci.ctc.transform.base.PlatformTypeTransform;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.yang.rev220208.Date;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.ne.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.top.Lldp;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.top.LldpBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.top.lldp.Config;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.chassis.rev200630.State1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.chassis.rev200630.State1Builder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.Chassis;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.Components;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.Component;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.ComponentKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.component.State;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.RebootInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.RebootInputBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.ntp.top.Ntp;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.ntp.top.NtpBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.ntp.top.ntp.ConfigBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.top.System;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.top.SystemBuilder;

import java.util.Map;

/**
 * DciNeTransform
 *
 * date       time        author
 * ─────────────────────────────
 * 2022/2/9   10:41      liwenxue
 * Copyright (c) 2022, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class DciNeTransform implements PlatformTypeTransform {

    public System apiNtpToDev(UpdateNeInput input){
        SystemBuilder systemBuilder = new SystemBuilder();
        NtpBuilder ntpBuilder = new NtpBuilder();
        ConfigBuilder configBuilder = new ConfigBuilder();
        configBuilder.setEnabled(input.getNtpEnabled());
        ntpBuilder.setConfig(configBuilder.build());
        systemBuilder.setNtp(ntpBuilder.build());
        return systemBuilder.build();
    }

    public Lldp apiLldpToDev(UpdateNeInput input){
        LldpBuilder builder = new LldpBuilder();
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.top.lldp.ConfigBuilder configBuilder
                = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.top.lldp.ConfigBuilder();
        configBuilder.setEnabled(input.getLldpEnabled());
        builder.setConfig(configBuilder.build());
        return builder.build();
    }

    @SuppressWarnings("all")
    public GetNeInfoOutputBuilder devGetNeInfoOutputBuilderToApi(Components components,
                                                     Ntp ntp, Lldp lldp){
        GetNeInfoOutputBuilder outputBuilder = new GetNeInfoOutputBuilder();
        //机框版本
        if(components!=null && components.getComponent()!=null){
            @Nullable Map<ComponentKey, Component> componentMap = components.getComponent();
            for(Component component : componentMap.values()){
                State state = component.getState();
                if(state!=null){
                    outputBuilder.setFirmwareVersion(state.getFirmwareVersion());
                    outputBuilder.setHardwareVersion(state.getHardwareVersion());
                    outputBuilder.setSoftwareVersion(state.getSoftwareVersion());
                    outputBuilder.setAdminState(devAdminStateTypeToApi(state.getAdminState()));
                    //将其他厂商的设备名称 修改成optel
                    // if(state.getMfgName()!=null && !state.getMfgName().isEmpty()){
                    //     outputBuilder.setMfgName("optel");
                    // }
                    /**
                     * 允许所有厂商上报他们自己的设备名称
                     */
                    outputBuilder.setMfgName(state.getMfgName());
                    if(state.getMfgDate()!=null){
                        outputBuilder.setMfgDate(new Date(state.getMfgDate().getValue()));
                    }
                    outputBuilder.setDescription(state.getDescription());
                    outputBuilder.setSerialNo(state.getSerialNo());
                    outputBuilder.setProductionNum(state.getPartNo());
                    outputBuilder.setPartNo(devPartNoToApi(state.getPartNo()));
                    outputBuilder.setRatedPower(state.getRatedPower());
                }
                // 注入机框属性
                Chassis chassis = component.getChassis();
                if(chassis!=null){
                    org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.chassis.State state1 = chassis.getState();
                    if(state1!=null){
                        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.chassis.rev200630.@Nullable State1 state2
                                = state1.augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.chassis.rev200630.State1.class);
                        if(state2!=null){
                            outputBuilder.setChassisType(devComponentChassisTypeToApi(state2.getChassisType(),state.getPartNo()));
                        }
                    }
                }
            }
        }
        //ntp 使能情况
        if(ntp!=null){
            if(ntp.getState()!=null){
                outputBuilder.setNtpEnabled(ntp.getState().getEnabled());
            }else if(ntp.getConfig()!=null){
                outputBuilder.setNtpEnabled(ntp.getConfig().getEnabled());
            }
        }
        if(lldp!=null ){
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.top.lldp.State state = lldp.getState();
            if(state!=null){
                outputBuilder.setLldpEnabled(state.getEnabled());
            }else if(lldp.getConfig()!=null){
                Config config = lldp.getConfig();
                outputBuilder.setLldpEnabled(config.getEnabled());
            }
        }
        return outputBuilder;
    }

    public GetNeInfoOutput devGetNeInfoOutputToApi(Components components, Ntp ntp, Lldp lldp){
        GetNeInfoOutputBuilder builder = devGetNeInfoOutputBuilderToApi(components, ntp, lldp);
        if(builder!=null){
            return builder.build();
        }
        return null;

    }

    public GetNeInfoOutput devGetNeInfoOutputToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetNeInfoOutput output, Components components,
            Ntp ntp, Lldp lldp){
        GetNeInfoOutputBuilder outputBuilder = devGetNeInfoOutputBuilderToApi(components,ntp,lldp);
        outputBuilder.setNeType(output.getNeType());

        // outputBuilder.setChassisType(devComponentChassisTypeToApi(output.getChassisType()));
        outputBuilder.setOla(output.getOla());
        // //机框版本
        // if(components!=null && components.getComponent()!=null){
        //     @Nullable Map<ComponentKey, Component> componentMap = components.getComponent();
        //     Component component = componentMap.get(0);
        //     State state = component.getState();
        //     if(state!=null){
        //         outputBuilder.setFirmwareVersion(state.getFirmwareVersion());
        //         outputBuilder.setHardwareVersion(state.getHardwareVersion());
        //         outputBuilder.setSoftwareVersion(state.getSoftwareVersion());
        //     }
        // }
        // //ntp 使能情况
        // if(system!=null){
        //     Ntp ntp = system.getNtp();
        //     if(ntp!=null && ntp.getState()!=null){
        //         outputBuilder.setNtpEnabled(ntp.getState().getEnabled());
        //     }
        // }
        // if(lldp!=null ){
        //     org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.top.lldp.State state = lldp.getState();
        //     if(state!=null){
        //         outputBuilder.setLldpEnabled(state.getEnabled());
        //     }
        // }
        return outputBuilder.build();
    }

    public RebootInput apiToRebootInputDev(org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.ne.rev200210.RebootInput input){
        RebootInputBuilder builder=new RebootInputBuilder();
        builder.setComponentName(input.getComponentName());
        builder.setRebootType(apiToRebootTypeDev(input.getRebootType()));
        return builder.build();
    }


    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.SetDatetimeInput apiToSetDatetimeInputDev(SetDatetimeInput input){
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.SetDatetimeInputBuilder builder=new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.SetDatetimeInputBuilder();
        builder.setDatetime(input.getDatetime());
        return builder.build();
    }


    public GetChassisConfigOutput devToGetChassisConfigOutputApi(Components components){
        GetChassisConfigOutputBuilder builder=new GetChassisConfigOutputBuilder();
        return builder.build();
    }
    public State1 apiToState1Dev(SetChassisConfigInput input){
        State1Builder builder=new State1Builder();
        builder.setLcd(input.getLcd());
        builder.setDirection(apiToDirectionDev(input.getDirection()));
        return builder.build();
    }
}
