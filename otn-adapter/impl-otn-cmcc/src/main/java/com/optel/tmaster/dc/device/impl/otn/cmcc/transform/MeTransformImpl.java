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
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.QueryMeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.QueryMeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.me.grouping.NtpServers;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.me.grouping.NtpServersBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ntp.servers.grouping.NtpServer;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ntp.servers.grouping.NtpServerBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ntp.servers.grouping.NtpServerKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.Me;

import java.util.ArrayList;
import java.util.List;

/**
 * dc-aggregator - MeTransformImpl
 * 网元属性转换
 * date       time        author
 * ─────────────────────────────
 * 2021/9/30   17:28      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class MeTransformImpl extends AbstractCmccTransformer implements CommonTransform, EnumTransform {

    public QueryMeOutput devMeToApiOut(Me me) {
        if (me == null) {
            return null;
        }
        QueryMeOutputBuilder queryMeOutputBuilder = new QueryMeOutputBuilder();
        queryMeOutputBuilder.setName(me.getName());
        queryMeOutputBuilder.setDeviceType(devDeviceTypeToApi(me.getDeviceType()));
        queryMeOutputBuilder.setLayerProtocolName(devLayerNameToApi(me.getLayerProtocolName()));
        queryMeOutputBuilder.setUuid(me.getUuid());
        queryMeOutputBuilder.setEq(me.getEq());
        queryMeOutputBuilder.setHardwareVersion(me.getHardwareVersion());
        queryMeOutputBuilder.setSoftwareVersion(me.getSoftwareVersion());
        queryMeOutputBuilder.setIpAddress(me.getIpAddress());
        queryMeOutputBuilder.setGateWay1(me.getGateWay1());
        queryMeOutputBuilder.setGateWay2(me.getGateWay2());
        queryMeOutputBuilder.setManufacturer(me.getManufacturer());
        queryMeOutputBuilder.setMask(me.getMask());
        queryMeOutputBuilder.setMcPort(me.getMcPort());
        queryMeOutputBuilder.setProductName(me.getProductName());
        queryMeOutputBuilder.setStatus(devMeStatusToApi(me.getStatus()));
        queryMeOutputBuilder.setNtpEnable(me.getNtpEnable());
        queryMeOutputBuilder.setNtpState(devNtpStateToApi(me.getNtpState()));
        queryMeOutputBuilder.setNtpServers(devNtpServersToApi(me.getNtpServers()));
        return queryMeOutputBuilder.build();
    }


    public NtpServers devNtpServersToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.me.NtpServers ntpServers) {
        if (ntpServers == null) {
            return null;
        }
        NtpServersBuilder ntpServersBuilder = new NtpServersBuilder();
        if (ntpServers.getNtpServer() != null) {
            List<NtpServer> ntpServerList = new ArrayList<>();
            for (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.me.ntp.servers.NtpServer ntpServer : ntpServers.getNtpServer().values()) {
                NtpServerBuilder ntpServerBuilder = new NtpServerBuilder();
                ntpServerBuilder.setIpAddress(ntpServer.getIpAddress());
                ntpServerBuilder.withKey(new NtpServerKey(ntpServer.key().getName()));
                ntpServerBuilder.setName(ntpServer.getName());
                ntpServerBuilder.setNtpVersion(ntpServer.getNtpVersion());
                ntpServerBuilder.setPort(ntpServer.getPort());
                ntpServerList.add(ntpServerBuilder.build());
            }
            ntpServersBuilder.setNtpServer(ltm(ntpServerList));
        }

        return ntpServersBuilder.build();
    }

}
