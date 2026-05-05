/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.config;

import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base.EthTypeTransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base.GeneralUtilTransform;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.netconf.sal.connect.netconf.schema.mapping.NetconfMessageTransformer;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.TerminalEthernetProtocolConfig;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.ethernet.top.Ethernet;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.ethernet.top.EthernetBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.interfaces.top.Interface;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.interfaces.top.InterfaceBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.interfaces.top.InterfaceKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.ipv4.top.Ipv4Builder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.ipv4.top.ipv4.AddressesBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.otn.top.OtnBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.sdh.top.SdhBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.subinterfaces.top.SubinterfaceBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci._interface.rev200210.GetInterfaceOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci._interface.rev200210.GetInterfaceOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci._interface.rev200210.SetClientEthInterfaceInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci._interface.rev200210.SetDcnInterfaceInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ethernet.rev230426.Interface1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ethernet.rev230426.Interface1Builder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ip.rev230426.Subinterface1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ip.rev230426.ipv4.top.ipv4.addresses.Address;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ip.rev230426.ipv4.top.ipv4.addresses.AddressBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ip.rev230426.ipv4.top.ipv4.addresses.AddressKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.Interfaces;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.interfaces._interface.ConfigBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.interfaces._interface.State;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.otn.top.Otn;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.sdh.top.Sdh;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.subinterfaces.top.subinterfaces.Subinterface;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev230426.State1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.Config1Builder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.Ipv4Address;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: DciInterfaceTransform
 * <ul>
 * <li>数据接口转换类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2022/3/9 15:20
 */
public class DciInterfaceTransform implements EthTypeTransform, GeneralUtilTransform {
    /**
     * 转换接口  查询以太网客户侧接口
     *
     * @param interfaces 接口
     * @return 查询输入
     */
    public GetInterfaceOutput devGetInterfaceOutputToApi(Interfaces interfaces) {
        if (interfaces == null || interfaces.getInterface() == null || interfaces.getInterface().isEmpty()) {
            return null;
        }
        GetInterfaceOutputBuilder getClientEthInterfaceOutputBuilder = new GetInterfaceOutputBuilder();
        Map<InterfaceKey, Interface> apiIfMap = new HashMap<>(interfaces.getInterface().size());
        for (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.interfaces.Interface item : interfaces.getInterface().values()) {
            InterfaceBuilder interfaceBuilder = new InterfaceBuilder();
            interfaceBuilder.withKey(new InterfaceKey(item.key().getName()));
            interfaceBuilder.setName(item.getName());
            if (item.getState() != null) {
                State state = item.getState();
                interfaceBuilder.setType(state.getType());
                State1 transceiverState = state.augmentation(State1.class);
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.port.rev230426.State1 portState = state.augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.port.rev230426.State1.class);
                if (transceiverState != null) {
                    interfaceBuilder.setTransceiver((String) transceiverState.getTransceiver());
                }
                if (portState != null) {
                    interfaceBuilder.setHardwarePort(portState.getHardwarePort());
                }
                interfaceBuilder.setOspfEnabled(item.getState().getOspfEnabled());
            }
            if (item.getSubinterfaces() != null && item.getSubinterfaces().getSubinterface() != null) {
                interfaceBuilder.setSubinterface(getSubInterface(item.getSubinterfaces().getSubinterface()));
            }
            Interface1 ethInterface = item.augmentation(Interface1.class);
            if (ethInterface != null) {
                interfaceBuilder.setEthernet(getEthernet(ethInterface.getEthernet(), item.getName()));
            }
            Otn otn = item.getOtn();
            if (otn != null && otn.getState() != null) {
                OtnBuilder otnBuilder = new OtnBuilder();
                otnBuilder.setClientAls(devOtnClientAlsToApi(otn.getState().getClientAls()));
                interfaceBuilder.setOtn(otnBuilder.build());
            }
            Sdh sdh = item.getSdh();
            if (sdh != null && sdh.getState() != null) {
                SdhBuilder sdhBuilder = new SdhBuilder();
                sdhBuilder.setClientAls(devSdhClientAlsToApi(sdh.getState().getClientAls()));
                interfaceBuilder.setSdh(sdhBuilder.build());
            }
            apiIfMap.put(interfaceBuilder.key(), interfaceBuilder.build());
        }
        getClientEthInterfaceOutputBuilder.setInterface(apiIfMap);
        return getClientEthInterfaceOutputBuilder.build();
    }

    private Ethernet getEthernet(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ethernet.rev230426.ethernet.top.Ethernet ethernet, String name) {
        if (ethernet == null) {
            return null;
        }
        EthernetBuilder ethernetBuilder = new EthernetBuilder();
        if (ethernet.getState() != null) {
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.@Nullable State1 state1 = ethernet.getState().augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.State1.class);

            ethernetBuilder.setMacAddress(devMacAddressToApi(ethernet.getState().getMacAddress()));
            ethernetBuilder.setDuplexMode(devDuplexModeToApi(ethernet.getState().getDuplexMode()));
            ethernetBuilder.setPortSpeed(devEthernetSpeedToApi(ethernet.getState().getPortSpeed()));
            if (state1 != null) {
                ethernetBuilder.setClientAls(devClientAlsToApi(state1.getClientAls()));
                ethernetBuilder.setClientFec(devClientFecToApi(state1.getClientFec()));
            } else {
                if (NetconfMessageTransformer.clientAlsMap.containsKey(name)) {
                    ethernetBuilder.setClientAls(TerminalEthernetProtocolConfig.ClientAls.forName(NetconfMessageTransformer.clientAlsMap.get(name)));
                }
                if (NetconfMessageTransformer.clientFecMap.containsKey(name)) {
                    ethernetBuilder.setClientFec(TerminalEthernetProtocolConfig.ClientFec.forName(NetconfMessageTransformer.clientFecMap.get(name)));
                }
            }

        }
        return ethernetBuilder.build();
    }

    private org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.subinterfaces.top.Subinterface getSubInterface(Subinterface subinterface) {
        if (subinterface == null) {
            return null;
        }
        SubinterfaceBuilder subinterfaceBuilder = new SubinterfaceBuilder();
        if (subinterface.getState() != null) {
            subinterfaceBuilder.setName(subinterface.getState().getName());
            subinterfaceBuilder.setIndex(subinterface.getIndex());
            @Nullable Subinterface1 ipv4 = subinterface.augmentation(Subinterface1.class);
            if (ipv4 != null && ipv4.getIpv4() != null && ipv4.getIpv4().getAddresses() != null) {
                Ipv4Builder ipv4Builder = new Ipv4Builder();
                AddressesBuilder addressesBuilder = new AddressesBuilder();
                @Nullable Map<AddressKey, Address> address = ipv4.getIpv4().getAddresses().getAddress();
                if (address != null) {
                    Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.ipv4.top.ipv4.addresses.AddressKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.ipv4.top.ipv4.addresses.Address> values = new HashMap<>(address.size());
                    for (Address ip : address.values()) {
                        if (ip.getState() != null) {
                            org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.ipv4.top.ipv4.addresses.AddressBuilder addressBuilder = new org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.ipv4.top.ipv4.addresses.AddressBuilder();
                            addressBuilder.setIp(devIpv4AddressToApi(ip.getState().getIp()));
                            addressBuilder.setGateway(devIpv4AddressToApi(ip.getState().getGateway()));
                            addressBuilder.setPrefixLength(ip.getState().getPrefixLength());
                            addressBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.ipv4.top.ipv4.addresses.AddressKey(addressBuilder.getIp()));
                            values.put(addressBuilder.key(), addressBuilder.build());
                        }
                    }
                    addressesBuilder.setAddress(values);
                }
                ipv4Builder.setAddresses(addressesBuilder.build());
                subinterfaceBuilder.setIpv4(ipv4Builder.build());
            }
        }
        return subinterfaceBuilder.build();
    }

    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.interfaces._interface.Config apiToConfigDev(SetDcnInterfaceInput input) {
        ConfigBuilder builder = new ConfigBuilder();
        builder.setOspfEnabled(input.getOspfEnabled());
        builder.setDhcpEnabled(input.getDhcpEnabled());
        return builder.build();
    }

    /**
     * 转换接口，设置以太网客户侧接口
     *
     * @param input 设置输入
     * @return 下发接口
     */
    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.interfaces.Interface apiSetClientEthInterfaceInputToDev(SetClientEthInterfaceInput input) {

        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.interfaces.InterfaceBuilder interfaceBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.interfaces.InterfaceBuilder();
        interfaceBuilder.setName(input.getName());
        Interface1Builder interface1Builder = new Interface1Builder();
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ethernet.rev230426.ethernet.top.EthernetBuilder ethernetBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ethernet.rev230426.ethernet.top.EthernetBuilder();
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ethernet.rev230426.ethernet.top.ethernet.ConfigBuilder configBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ethernet.rev230426.ethernet.top.ethernet.ConfigBuilder();
        Config1Builder builder = new Config1Builder();
        builder.setClientAls(apiClientAlsToDev(input.getClientAls()));
        builder.setClientFec(apiClientFecToDev(input.getClientFec()));
        configBuilder.addAugmentation(builder.build());
        ethernetBuilder.setConfig(configBuilder.build());
        interface1Builder.setEthernet(ethernetBuilder.build());
        interfaceBuilder.addAugmentation(interface1Builder.build());
        if (input.getOtn() != null) {
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.otn.top.OtnBuilder otnBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.otn.top.OtnBuilder();
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.otn.top.otn.ConfigBuilder otnConfig = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.otn.top.otn.ConfigBuilder();
            otnConfig.setClientAls(apiOtnClientAlsToDev(input.getOtn().getClientAls()));
            otnBuilder.setConfig(otnConfig.build());
            interfaceBuilder.setOtn(otnBuilder.build());
        }
        if (input.getSdh() != null) {
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.sdh.top.SdhBuilder sdhBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.sdh.top.SdhBuilder();
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.sdh.top.sdh.ConfigBuilder sdhConfig = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.sdh.top.sdh.ConfigBuilder();
            sdhConfig.setClientAls(apiSdhClientAlsToDev(input.getSdh().getClientAls()));
            sdhBuilder.setConfig(sdhConfig.build());
            interfaceBuilder.setSdh(sdhBuilder.build());
        }
        return interfaceBuilder.build();
    }

    public Address apiToAddressDev(SetDcnInterfaceInput interfaceInput) {
        AddressBuilder builder = new AddressBuilder();
        Ipv4Address ipv4Address = apiIpv4AddressToDev(interfaceInput.getIp());
        builder.withKey(new AddressKey(ipv4Address));
        builder.setIp(ipv4Address);
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ip.rev230426.ipv4.top.ipv4.addresses.address.ConfigBuilder configBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ip.rev230426.ipv4.top.ipv4.addresses.address.ConfigBuilder();
        configBuilder.setIp(ipv4Address);
        configBuilder.setGateway(apiIpv4AddressToDev(interfaceInput.getGateway()));
        configBuilder.setPrefixLength(interfaceInput.getPrefixLength());
        builder.setConfig(configBuilder.build());
        return builder.build();
    }
//极简OTN没有邻居IP
//    public SetNeighborIpInput apiToSetNeighborIpInputDev(org.opendaylight.yang.gen.v1.com.optel.device.opt.dci._interface.rev200210.SetNeighborIpInput input) {
//        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.SetNeighborIpInputBuilder builder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.SetNeighborIpInputBuilder();
//        builder.setLocalPort(input.getLocalPort());
//        builder.setNeighborGateway(apiIpv4AddressToDev(input.getNeighborGateway()));
//        builder.setNeighborId(input.getNeighborId());
//        builder.setNeighborIp(apiIpv4AddressToDev(input.getNeighborIp()));
//        builder.setNeighborIpType(apiSetNeighbourIpInputToDev(input.getNeighborIpType()));
//        builder.setNeighborPortName(input.getNeighborPortName());
//        builder.setNeighborPrefixLength(input.getNeighborPrefixLength());
//        return builder.build();
//    }
}
