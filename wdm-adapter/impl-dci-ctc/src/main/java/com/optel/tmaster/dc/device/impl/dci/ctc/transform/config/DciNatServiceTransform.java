/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.transform.config;

import com.optel.tmaster.dc.general.base.exception.manage.NoMatchEnumValueException;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.nat.rev220627.nats.top.Nat;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.nat.rev220627.nats.top.NatBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.nat.rev220627.nats.top.NatKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.Ipv4Address;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.communication.rev220928.GetNatOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.communication.rev220928.GetNatOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.communication.rev220928.SetNatInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.nat.rev220627.nats.top.Nats;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.nat.rev220627.nats.top.nats.nat.ConfigBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.nat.types.rev220627.ProtocolType;

import java.util.HashMap;
import java.util.Map;

/**
 * NAT转换
 *
 * @author Quan Jingyuan
 * @since 2022/9/28
 **/
public class DciNatServiceTransform {

    public GetNatOutput devToNatApi(Nats nats) {
        if (nats == null || nats.getNat() == null) {
            return null;
        }
        GetNatOutputBuilder builder = new GetNatOutputBuilder();
        Map<NatKey, Nat> values = new HashMap<>(nats.getNat().size());
        for (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.nat.rev220627.nats.top.nats.Nat nat : nats.getNat().values()) {
            NatBuilder natBuilder = new NatBuilder();
            if (nat != null && nat.getState() != null) {
                natBuilder.setIndex(nat.getIndex());
                if (nat.getState().getDstIp() != null) {
                    natBuilder.setDstIp(new Ipv4Address(nat.getState().getDstIp().getValue()));
                }
                natBuilder.setDstPort(nat.getState().getDstPort());
                if (nat.getState().getSrcIp() != null) {
                    natBuilder.setSrcIp(new Ipv4Address(nat.getState().getSrcIp().getValue()));
                }
                natBuilder.setSrcPort(nat.getState().getSrcPort());
                natBuilder.setProtocol(devToApiProtocolType(nat.getState().getProtocol()));
                natBuilder.withKey(new NatKey(nat.getIndex()));
                values.put(natBuilder.key(), natBuilder.build());
            }
        }
        builder.setNat(values);
        return builder.build();
    }

    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.nat.rev220627.nats.top.nats.Nat apiToNatDev(SetNatInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.nat.rev220627.nats.top.nats.NatBuilder builder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.nat.rev220627.nats.top.nats.NatBuilder();
        builder.setIndex(input.getIndex());
        ConfigBuilder configBuilder = new ConfigBuilder();
        configBuilder.setIndex(input.getIndex());
        if (input.getDstIp() != null) {
            configBuilder.setDstIp(new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.Ipv4Address(input.getDstIp().getValue()));
        }
        configBuilder.setDstPort(input.getDstPort());
        if (input.getSrcIp() != null) {
            configBuilder.setSrcIp(new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.Ipv4Address(input.getSrcIp().getValue()));
        }
        configBuilder.setSrcPort(input.getSrcPort());
        if (input.getProtocol() != null) {
            configBuilder.setProtocol(apiToDevProtocolType(input.getProtocol()));
        }
        builder.setConfig(configBuilder.build());
        return builder.build();
    }

    private ProtocolType apiToDevProtocolType(org.opendaylight.yang.gen.v1.com.optel.dci.yang.nat.types.rev220627.ProtocolType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case TCP:
                return ProtocolType.TCP;
            case UDP:
                return ProtocolType.UDP;
            case ALL:
                return ProtocolType.ALL;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(type);
        }
    }

    private org.opendaylight.yang.gen.v1.com.optel.dci.yang.nat.types.rev220627.ProtocolType devToApiProtocolType(ProtocolType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case TCP:
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.nat.types.rev220627.ProtocolType.TCP;
            case UDP:
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.nat.types.rev220627.ProtocolType.UDP;
            case ALL:
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.nat.types.rev220627.ProtocolType.ALL;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(type);
        }
    }
}
