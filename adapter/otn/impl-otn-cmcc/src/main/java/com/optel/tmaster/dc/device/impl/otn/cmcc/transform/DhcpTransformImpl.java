/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform;

import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.AbstractCmccTransformer;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.EnumTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.dhcp.rev210104.ConfigDhcpRelayInterfacesInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.dhcp.rev210104.QueryDhcpRelayOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.dhcp.rev210104.SetDhcpRelayEnableInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.dhcp.relay.grouping.DhcpRelayInterfaces;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.dhcp.relay.grouping.DhcpRelayInterfacesBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.dhcp.relay.grouping.dhcp.relay.interfaces.DhcpRelayInterface;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.dhcp.relay.grouping.dhcp.relay.interfaces.DhcpRelayInterfaceBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.dhcp.relay.grouping.dhcp.relay.interfaces.DhcpRelayInterfaceKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.DhcpRelay;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.DhcpRelayBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * dhcp relay 转换
 * @author Quan Jingyuan
 * @since 2021/10/13
 **/
public class DhcpTransformImpl extends AbstractCmccTransformer implements CommonTransform, EnumTransform {
    public QueryDhcpRelayOutputBuilder devQueryDhcpRelayOutputToApi(DhcpRelay dhcpRelay) {
        QueryDhcpRelayOutputBuilder builder = new QueryDhcpRelayOutputBuilder();
        if (dhcpRelay == null) {
            return builder;
        }
        builder.setEnable(dhcpRelay.getEnable());
        builder.setDhcpRelayInterfaces(devDhcpRelayToApi(dhcpRelay.getDhcpRelayInterfaces()));

        return builder;
    }

    private DhcpRelayInterfaces devDhcpRelayToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.DhcpRelayInterfaces interfaces) {
        DhcpRelayInterfacesBuilder builder = new DhcpRelayInterfacesBuilder();
        if (interfaces == null || interfaces.getDhcpRelayInterface() == null) {
            return builder.build();
        }
        List<DhcpRelayInterface> dhcpRelayInterfaces = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.dhcp.relay.interfaces.DhcpRelayInterface item : interfaces.getDhcpRelayInterface().values()) {
            DhcpRelayInterfaceBuilder relayInterfaceBuilder = new DhcpRelayInterfaceBuilder();
            relayInterfaceBuilder.setName(item.getName());
            relayInterfaceBuilder.withKey(new DhcpRelayInterfaceKey(item.getName()));
            relayInterfaceBuilder.setDhcpServiceIp(item.getDhcpServiceIp());
            dhcpRelayInterfaces.add(relayInterfaceBuilder.build());
        }
        builder.setDhcpRelayInterface(ltm(dhcpRelayInterfaces));
        return builder.build();

    }
    private org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.DhcpRelayInterfaces apiDhcpRelayToDev(DhcpRelayInterfaces interfaces) {
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.DhcpRelayInterfacesBuilder builder = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.DhcpRelayInterfacesBuilder();
        if (interfaces == null || interfaces.getDhcpRelayInterface() == null) {
            return builder.build();
        }
        List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.dhcp.relay.interfaces.DhcpRelayInterface> dhcpRelayInterfaces = new ArrayList<>();
        for (DhcpRelayInterface item : interfaces.getDhcpRelayInterface().values()) {
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.dhcp.relay.interfaces.DhcpRelayInterfaceBuilder relayInterfaceBuilder = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.dhcp.relay.interfaces.DhcpRelayInterfaceBuilder();
            relayInterfaceBuilder.setName(item.getName());
            relayInterfaceBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.dhcp.relay.interfaces.DhcpRelayInterfaceKey(item.getName()));
            relayInterfaceBuilder.setDhcpServiceIp(item.getDhcpServiceIp());
            dhcpRelayInterfaces.add(relayInterfaceBuilder.build());
        }
        builder.setDhcpRelayInterface(ltm(dhcpRelayInterfaces));
        return builder.build();

    }
    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.dhcp.relay.interfaces.DhcpRelayInterfaceBuilder apiDhcpRelayInterfaceToDev(ConfigDhcpRelayInterfacesInput input) {
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.dhcp.relay.interfaces.DhcpRelayInterfaceBuilder builder = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.dhcp.relay.interfaces.DhcpRelayInterfaceBuilder();
        if (input == null) {
            return builder;
        }
        builder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.dhcp.relay.interfaces.DhcpRelayInterfaceKey(input.getName()));
        builder.setName(input.getName());
        builder.setDhcpServiceIp(input.getDhcpServiceIp());
        return builder;
    }

    public DhcpRelayBuilder apiDhcpRelayToDev(SetDhcpRelayEnableInput input){
        DhcpRelayBuilder builder=new DhcpRelayBuilder();
        if(input==null){
            return builder;
        }
        builder.setEnable(input.getEnable());
        builder.setDhcpRelayInterfaces(apiDhcpRelayToDev(input.getDhcpRelayInterfaces()));

        return builder;
    }
}
