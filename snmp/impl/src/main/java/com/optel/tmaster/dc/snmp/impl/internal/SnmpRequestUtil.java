/*
 * Copyright (c) 2014, 2015 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.snmp.impl.internal;

import org.opendaylight.yang.gen.v1.com.optel.params.yang.snmp.config.rev211113.SnmpRequestConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.CommunityTarget;
import org.snmp4j.Target;
import org.snmp4j.mp.SnmpConstants;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.UdpAddress;

import java.net.Inet4Address;
import java.net.UnknownHostException;

/**
 * SNMP请求
 * @author liwenxue
 */
public class SnmpRequestUtil {
    private static final Logger LOG = LoggerFactory.getLogger(SnmpRequestUtil.class);
    static final String DEFAULT_COMMUNITY = "public";
    static final Integer DEFAULT_SNMP_LISTEN_PORT = 161;
    static final int RETRIES = 5;
    static final int TIMEOUT = 1000;
    static final int MAXREPETITIONS = 10000;

    static Target getTargetForIp(SnmpRequestConfig config) {
        Address addr;
        try {
            int port = DEFAULT_SNMP_LISTEN_PORT;
            if(config.getPort() != null){
                port = config.getPort().intValue();
            }
            addr = new UdpAddress(Inet4Address.getByName(config.getIpAddress().getValue()), port);
        } catch (UnknownHostException e) {
            LOG.warn("Failed to create UDP Address", e);
            return null;
        }
        CommunityTarget communityTarget = new CommunityTarget();
        String community = config.getCommunity();
        if (community == null || "".equals(community)) {
            community = DEFAULT_COMMUNITY;
        }
        communityTarget.setCommunity(new OctetString(community));
        communityTarget.setAddress(addr);
        communityTarget.setRetries(RETRIES);
        communityTarget.setTimeout(TIMEOUT);
        communityTarget.setVersion(SnmpConstants.version2c);
        return communityTarget;
    }


}
