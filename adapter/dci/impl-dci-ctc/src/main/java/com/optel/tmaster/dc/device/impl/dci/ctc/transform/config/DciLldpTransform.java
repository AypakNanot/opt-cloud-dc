/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.transform.config;

import cn.hutool.core.map.MapUtil;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.base.GeneralUtilTransform;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.lldp.rev220208.lldp._interface.top.LldpInterface;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.lldp.rev220208.lldp._interface.top.LldpInterfaceBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.lldp.rev220208.lldp._interface.top.LldpInterfaceKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.lldp.rev220208.lldp.neighbor.top.NeighborBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.lldp.rev220208.lldp.neighbor.top.NeighborKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.lldp.rev220208.lldp.top.LldpBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.lldp.rev200210.GetLldpOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.lldp.rev200210.GetLldpOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.lldp.rev200210.SetLldpIfEnableInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.lldp.rev200210.SetLldpInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.lldp.rev200210.SetNeighbourIpInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp._interface.top.Interfaces;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp._interface.top.interfaces.Interface;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp._interface.top.interfaces.InterfaceBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp._interface.top.interfaces._interface.ConfigBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.neighbor.top.Neighbors;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.neighbor.top.neighbors.Neighbor;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.neighbor.top.neighbors.neighbor.State;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.top.Lldp;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.top.lldp.Config;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.SetNeighborIpInputBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: DciLldpTransform
 * <ul>
 * <li>LLDP 转换类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2022/3/11 14:47
 */
public class DciLldpTransform implements GeneralUtilTransform {

    public GetLldpOutput devLldpInterfaceToApi(Lldp lldp) {
        if (null == lldp){
            return null;
        }
        GetLldpOutputBuilder getLldpOutputBuilder = new GetLldpOutputBuilder();
        LldpBuilder lldpBuilder = new LldpBuilder();
        LldpInterfaceBuilder lldpInterfaceBuilder = new LldpInterfaceBuilder();
        Config config = lldp.getConfig();
        Interfaces interfaces = lldp.getInterfaces();
        if (config != null){
            lldpBuilder.setEnabled(config.getEnabled());
        }
        if (interfaces != null && MapUtil.isNotEmpty(interfaces.getInterface())) {
            HashMap<LldpInterfaceKey, LldpInterface> hashMap = new HashMap<>(interfaces.getInterface().size());
            @Nullable Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp._interface.top.interfaces.InterfaceKey, Interface> devInterfaceMap = interfaces.getInterface();
            for (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp._interface.top.interfaces.InterfaceKey interfaceKey : devInterfaceMap.keySet()) {
                Interface devInterface = devInterfaceMap.get(interfaceKey);
                LldpInterfaceKey lldpInterfaceKey = new LldpInterfaceKey(interfaceKey.getName());
                lldpInterfaceBuilder.setName(devInterface.getName());
                if (devInterface.getConfig() != null) {
                    lldpInterfaceBuilder.setEnabled(devInterface.getConfig().getEnabled());
                }
                lldpInterfaceBuilder.setNeighbor(devNeighborToApi(devInterface.getNeighbors()));
                hashMap.put(lldpInterfaceKey,lldpInterfaceBuilder.build());
            }
            lldpBuilder.setLldpInterface(hashMap);
        }
        getLldpOutputBuilder.setLldp(lldpBuilder.build());
        return getLldpOutputBuilder.build();
    }

    public  Map<NeighborKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.lldp.rev220208.lldp.neighbor.top.Neighbor>
    devNeighborToApi(Neighbors neighbors){
        if(neighbors == null){
            return null;
        }
        if (neighbors.getNeighbor() == null){
            return null;
        }
        @Nullable Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.neighbor.top.neighbors.NeighborKey, Neighbor> neighborMap = neighbors.getNeighbor();
        Map<NeighborKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.lldp.rev220208.lldp.neighbor.top.Neighbor> outNeighborMap = new HashMap<>(neighborMap.size());
        for (Neighbor  neighbor: neighborMap.values()) {
            NeighborKey neighborKey = new NeighborKey(neighbor.getId());
            NeighborBuilder neighborBuilder = new NeighborBuilder();
            neighborBuilder.setId(neighbor.getId());
            State state = neighbor.getState();
            if (state != null) {
                neighborBuilder.setId(state.getId());
                neighborBuilder.setChassisId(state.getChassisId());
                neighborBuilder.setManagementAddress(state.getManagementAddress());
                neighborBuilder.setPortId(state.getPortId());
                neighborBuilder.setPortDescription(state.getPortDescription());
                neighborBuilder.setSystemName(state.getSystemName());
                neighborBuilder.setSystemDescription(state.getSystemDescription());
                neighborBuilder.setTtl(state.getTtl());
            }
            outNeighborMap.put(neighborKey, neighborBuilder.build());
        }
        return outNeighborMap;
    }

    public Interface apiSetLldpIfEnableInputToDev(SetLldpIfEnableInput input) {
        InterfaceBuilder interfaceBuilder = new InterfaceBuilder();
        if(input != null){
            interfaceBuilder.setName(input.getName());
            ConfigBuilder configBuilder = new ConfigBuilder();
            configBuilder.setName(input.getName());
            configBuilder.setEnabled(input.getEnabled());
            interfaceBuilder.setConfig(configBuilder.build());
        }
        return interfaceBuilder.build();
    }

    public Config apiSetLldpEnableInputToDev(SetLldpInput input) {
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.top.lldp.ConfigBuilder configBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.top.lldp.ConfigBuilder();
        if (input != null){
            configBuilder.setEnabled(input.getEnabled());
        }
        return configBuilder.build();
    }

        public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.SetNeighborIpInput apiSetNeighbourIpInputToDev(SetNeighbourIpInput ipInput) {
        SetNeighborIpInputBuilder setNeighbourIpInputBuilder = new SetNeighborIpInputBuilder();
        if(ipInput != null){
            setNeighbourIpInputBuilder.setLocalPort(ipInput.getLocalPort());
            setNeighbourIpInputBuilder.setNeighborIp(apiIpv4AddressToDev(ipInput.getNeighborIp()));
            setNeighbourIpInputBuilder.setNeighborGateway(apiIpv4AddressToDev(ipInput.getNeighborGateway()));
            setNeighbourIpInputBuilder.setNeighborIpType(apiSetNeighbourIpInputToDev(ipInput.getNeighborIpType()));
            setNeighbourIpInputBuilder.setNeighborId(ipInput.getNeighborId());
            setNeighbourIpInputBuilder.setNeighborPortName(ipInput.getNeighborPortName());
            setNeighbourIpInputBuilder.setNeighborPrefixLength(ipInput.getNeighborPrefixLength());
        }
        return setNeighbourIpInputBuilder.build();
    }
}
