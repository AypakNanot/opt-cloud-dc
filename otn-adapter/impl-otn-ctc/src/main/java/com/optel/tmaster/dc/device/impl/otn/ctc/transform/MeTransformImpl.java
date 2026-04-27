/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform;

import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.AbstractCtcTransformer;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.EnumTransform;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.GetOpticalModuleInfoOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.GetOpticalModuleInfoOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.QueryMeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.QueryMeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.get.optical.module.info.output.OpticalModuleInfosBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.get.optical.module.info.output.optical.module.infos.OpticalModuleInfo;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.get.optical.module.info.output.optical.module.infos.OpticalModuleInfoBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.get.optical.module.info.output.optical.module.infos.OpticalModuleInfoKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.me.grouping.NtpServers;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.me.grouping.NtpServersBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ntp.servers.grouping.NtpServer;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ntp.servers.grouping.NtpServerBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ntp.servers.grouping.NtpServerKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.GetOpticalModuleInfoInput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.GetOpticalModuleInfoInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.Me;

import java.util.*;

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
public class MeTransformImpl extends AbstractCtcTransformer implements CommonTransform, EnumTransform{

    public QueryMeOutput devMeToApiOut(Me me){
        if(me == null){
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
        queryMeOutputBuilder.setSerialNumber(me.getSerialNumber());

        return queryMeOutputBuilder.build();
    }


    public NtpServers devNtpServersToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.me.NtpServers ntpServers){
        if(ntpServers == null){
            return null;
        }
        NtpServersBuilder ntpServersBuilder = new NtpServersBuilder();
        if(ntpServers.getNtpServer() != null){
            List<NtpServer> ntpServerList = new ArrayList<>();
            for(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.me.ntp.servers.NtpServer ntpServer:ntpServers.getNtpServer().values()){
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
    public GetOpticalModuleInfoInput apiToGetOpticalModuleInfoInputDev(org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.GetOpticalModuleInfoInput infoInput){
        GetOpticalModuleInfoInputBuilder builder=new GetOpticalModuleInfoInputBuilder();
        builder.setPtpName(infoInput.getPtpName());
        return builder.build();
    }

    public GetOpticalModuleInfoOutput decToGetOpticalModuleInfoOutputApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.GetOpticalModuleInfoOutput opticalModuleInfoOutput){
        GetOpticalModuleInfoOutputBuilder builder =new GetOpticalModuleInfoOutputBuilder();

        if(Objects.isNull(opticalModuleInfoOutput)||Objects.isNull(opticalModuleInfoOutput.getOpticalModuleInfos())||Objects.isNull(opticalModuleInfoOutput.getOpticalModuleInfos().getOpticalModuleInfo())){
            return builder.build();
        }
        if(opticalModuleInfoOutput.getOpticalModuleInfos().getOpticalModuleInfo().size()==0){
            return builder.build();
        }
        OpticalModuleInfosBuilder opticalModuleInfosBuilder=new OpticalModuleInfosBuilder();

        @Nullable Map<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.get.optical.module.info.output.optical.module.infos.OpticalModuleInfoKey, org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.get.optical.module.info.output.optical.module.infos.OpticalModuleInfo> opticalModuleInfo =
        opticalModuleInfoOutput.getOpticalModuleInfos().getOpticalModuleInfo();


        Map<OpticalModuleInfoKey, OpticalModuleInfo> values =new HashMap<>(opticalModuleInfo.size());
       for(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.OpticalModuleInfo moduleInfo:opticalModuleInfo.values()){
           OpticalModuleInfoBuilder opticalModuleInfoBuilder=new OpticalModuleInfoBuilder();
           opticalModuleInfoBuilder.withKey(new OpticalModuleInfoKey(moduleInfo.getPtpName()));
           opticalModuleInfoBuilder.setAdditional(moduleInfo.getAdditional());
           opticalModuleInfoBuilder.setCur(moduleInfo.getCur());
           opticalModuleInfoBuilder.setInputPow(moduleInfo.getInputPow());
           opticalModuleInfoBuilder.setOutputPow(moduleInfo.getOutputPow());
           opticalModuleInfoBuilder.setPn(moduleInfo.getPn());
           opticalModuleInfoBuilder.setPtpName(moduleInfo.getPtpName());
           opticalModuleInfoBuilder.setSn(moduleInfo.getSn());
           opticalModuleInfoBuilder.setTemperature(moduleInfo.getTemperature());
           opticalModuleInfoBuilder.setVendor(moduleInfo.getVendor());
           opticalModuleInfoBuilder.setVer(moduleInfo.getVer());
           opticalModuleInfoBuilder.setVoltage(moduleInfo.getVoltage());
           values.put(opticalModuleInfoBuilder.key(),opticalModuleInfoBuilder.build());
       }
        opticalModuleInfosBuilder.setOpticalModuleInfo(values);
        builder.setOpticalModuleInfos(opticalModuleInfosBuilder.build());
        return builder.build();
    }
}
