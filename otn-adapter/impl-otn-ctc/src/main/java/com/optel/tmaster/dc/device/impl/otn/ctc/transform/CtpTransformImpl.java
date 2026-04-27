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
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.ServiceTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.*;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.eth.ctp.pac.ext.EthCtpPacOutBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.odu.ctp.pac.ext.OduCtpPacOutBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.osu.ctp.pac.ext.OsuCtpPacOutBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.vc.ctp.pac.ext.VcCtpPacOutBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.VlanSpec;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.StatePac;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ctp.StatePacBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ctps.grouping.CtpBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ctps.grouping.CtpKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eth.rev210927.eth.ctp.pac.grouping.*;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ctps.Ctp;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.Ctp1;

import java.util.ArrayList;
import java.util.List;

/**
 * CtpTransformImpl
 * CTP
 * date       time        author
 * ─────────────────────────────
 * 2021/10/7   11:23      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class CtpTransformImpl extends AbstractCtcTransformer implements CommonTransform, EnumTransform, ServiceTransform {

    public List<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ctps.grouping.Ctp> devCtpToApi(List<Ctp> ctpList) {
        if (ctpList == null) {
            return null;
        }
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ctps.grouping.Ctp> result = new ArrayList<>();
        for (Ctp ctp : ctpList) {
            result.add(devCtpToApi(ctp).build());
        }
        return result;
    }


    public CtpBuilder devCtpToApi(Ctp ctp) {
        if (ctp == null) {
            return null;
        }
        CtpBuilder ctpBuilder = new CtpBuilder();
        ctpBuilder.withKey(new CtpKey(ctp.getName()));
        ctpBuilder.setName(ctp.getName());
        ctpBuilder.setServerTp(ctp.getServerTp());
        ctpBuilder.setLayerProtocolName(devLayerNameToApi(ctp.getLayerProtocolName()));
        ctpBuilder.setPortRole(devPortRoleToApi(ctp.getPortRole()));
        StatePac statePac = devStatePacToApi(ctp.getStatePac());
        if (statePac != null) {
            ctpBuilder.setStatePac(new StatePacBuilder(statePac).build());
        }
        ctpBuilder.setLoopBack(devLoopbackTypeToApi(ctp.getLoopBack()));
        ctpBuilder.setProtectRole(devProtectRoleToApi(ctp.getProtectRole()));
        //转化成对应输出的扩展
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ctp1 sdhCtpPac = ctp.augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ctp1.class);
        if (sdhCtpPac != null && sdhCtpPac.getVcCtpPac() != null) {
            VcCtpPacOutBuilder vcCtpPacOutBuilder = new VcCtpPacOutBuilder();
            // J1 改为 RPC 获取，此处代码将废弃
            vcCtpPacOutBuilder.setJ1ActualRx(sdhCtpPac.getVcCtpPac().getJ1ActualRx());
            vcCtpPacOutBuilder.setJ1ActualTx(sdhCtpPac.getVcCtpPac().getJ1ActualTx());
            vcCtpPacOutBuilder.setJ1ExpectedRx(sdhCtpPac.getVcCtpPac().getJ1ExpectedRx());

            vcCtpPacOutBuilder.setMappingPath(sdhCtpPac.getVcCtpPac().getMappingPath());
            vcCtpPacOutBuilder.setVcType(devSwitchTypeToApi(sdhCtpPac.getVcCtpPac().getVcType()));
            ctpBuilder.addAugmentation(new VcCtpPacExtBuilder().setVcCtpPacOut(vcCtpPacOutBuilder.build()).build());
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Ctp1 oduPac = ctp.augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Ctp1.class);
        if (oduPac != null && oduPac.getOduCtpPac() != null) {
            OduCtpPacOutBuilder oduCtpPacOutBuilder = new OduCtpPacOutBuilder();
            oduCtpPacOutBuilder.setAdaptationType(devAdaptationTypeToApi(oduPac.getOduCtpPac().getAdaptationType()));
            oduCtpPacOutBuilder.setOduSignalType(devClientSignalTypeToApi(oduPac.getOduCtpPac().getOduSignalType()));
            oduCtpPacOutBuilder.setSwitchCapability(devSwitchTypeToApi(oduPac.getOduCtpPac().getSwitchCapability()));
            oduCtpPacOutBuilder.setCurrentNumberOfTributarySlots(oduPac.getOduCtpPac().getCurrentNumberOfTributarySlots());
            oduCtpPacOutBuilder.setGHaoStatus(devGHaoStatusToApi(oduPac.getOduCtpPac().getGHaoStatus()));
            oduCtpPacOutBuilder.setTsDetail(oduPac.getOduCtpPac().getTsDetail());
            oduCtpPacOutBuilder.setPmtrailTraceActualRx(oduPac.getOduCtpPac().getPmtrailTraceActualRx());
            oduCtpPacOutBuilder.setPmtrailTraceActualTx(oduPac.getOduCtpPac().getPmtrailTraceActualTx());
            oduCtpPacOutBuilder.setPmtrailTraceExpectedRx(oduPac.getOduCtpPac().getPmtrailTraceExpectedRx());
            ctpBuilder.addAugmentation(new OduCtpPacExtBuilder().setOduCtpPacOut(oduCtpPacOutBuilder.build()).build());
        }

        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.Ctp1 osuPac = ctp.augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.Ctp1.class);
        if (osuPac != null && osuPac.getOsuCtpPac() != null) {
            OsuCtpPacOutBuilder osuCtpPacOutBuilder = new OsuCtpPacOutBuilder();
            osuCtpPacOutBuilder.setAdaptationType(devAdaptationTypeToApi(osuPac.getOsuCtpPac().getAdaptationType()));
            osuCtpPacOutBuilder.setOsuSignalType(devClientSignalTypeToApi(osuPac.getOsuCtpPac().getOsuSignalType()));
            osuCtpPacOutBuilder.setPmTrailTraceActualRx(osuPac.getOsuCtpPac().getPmTrailTraceActualRx());
            osuCtpPacOutBuilder.setPmTrailTraceActualTx(osuPac.getOsuCtpPac().getPmTrailTraceActualTx());
            osuCtpPacOutBuilder.setPmTrailTraceExpectedRx(osuPac.getOsuCtpPac().getPmTrailTraceExpectedRx());
            osuCtpPacOutBuilder.setTpn(osuPac.getOsuCtpPac().getTpn());
            osuCtpPacOutBuilder.setBandwidth(osuPac.getOsuCtpPac().getBandwidth());
            osuCtpPacOutBuilder.setTcm1Mode(devTcmModeTypeToApi(osuPac.getOsuCtpPac().getTcm1Mode()));
            osuCtpPacOutBuilder.setTcm2Mode(devTcmModeTypeToApi(osuPac.getOsuCtpPac().getTcm2Mode()));
            ctpBuilder.addAugmentation(new OsuCtpPacExtBuilder().setOsuCtpPacOut(osuCtpPacOutBuilder.build()).build());
        }
        Ctp1 ethCtpPac = ctp.augmentation(Ctp1.class);
        if (ethCtpPac != null && ethCtpPac.getEthCtpPac() != null) {
            EthCtpPacOutBuilder ethCtpPacOutBuilder = new EthCtpPacOutBuilder();
            VlanSpec vlanSpec = devVlanSpecToApi(ethCtpPac.getEthCtpPac().getVlanSpec());
            if (vlanSpec != null) {
                ethCtpPacOutBuilder.setVlanSpec(new VlanSpecBuilder(vlanSpec).build());
            }
            VlanSpec clientVlanSpec = devVlanSpecToApi(ethCtpPac.getEthCtpPac().getClientVlanSpec());
            if (clientVlanSpec != null) {
                ethCtpPacOutBuilder.setClientVlanSpec(new ClientVlanSpecBuilder(clientVlanSpec).build());
            }
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.OamConfig oamConfig = ethCtpPac.getEthCtpPac().getOamConfig();
            if (oamConfig != null) {
                OamConfigBuilder oamConfigBuilder = new OamConfigBuilder();
                oamConfigBuilder.setCcInterval(devOamTransmitIntervalToApi(oamConfig.getCcInterval()));
                oamConfigBuilder.setDmInterval(devOamTransmitIntervalToApi(oamConfig.getDmInterval()));
                oamConfigBuilder.setLmInterval(devOamTransmitIntervalToApi(oamConfig.getLmInterval()));
                oamConfigBuilder.setMdName(oamConfig.getMdName());
                oamConfigBuilder.setMdl(oamConfig.getMdl());
                oamConfigBuilder.setMegId(oamConfig.getMegId());
                oamConfigBuilder.setMel(oamConfig.getMel());
                oamConfigBuilder.setMepId(oamConfig.getMepId());
                oamConfigBuilder.setRemoteMepId(oamConfig.getRemoteMepId());
                ethCtpPacOutBuilder.setOamConfig(oamConfigBuilder.build());
            }
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.OamStatePac oamStatePac = ethCtpPac.getEthCtpPac().getOamStatePac();
            if (oamStatePac != null) {
                OamStatePacBuilder oamStatePacBuilder = new OamStatePacBuilder();
                oamStatePacBuilder.setCcState(oamStatePac.getCcState());
                oamStatePacBuilder.setDmState(oamStatePac.getDmState());
                oamStatePacBuilder.setLmState(oamStatePac.getLmState());
                oamStatePacBuilder.setTmState(oamStatePac.getTmState());
                ethCtpPacOutBuilder.setOamStatePac(oamStatePacBuilder.build());
            }
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.Performance performance = ethCtpPac.getEthCtpPac().getPerformance();
            if (performance != null) {
                PerformanceBuilder performanceBuilder = new PerformanceBuilder();
                performanceBuilder.setDelay(performance.getDelay());
                performanceBuilder.setRxBytes(performance.getRxBytes());
                performanceBuilder.setTxBytes(performance.getTxBytes());
                performanceBuilder.setFarPacketLossRate(devRealToApi(performance.getFarPacketLossRate()));
                performanceBuilder.setNearPacketLossRate(devRealToApi(performance.getNearPacketLossRate()));
                ethCtpPacOutBuilder.setPerformance(performanceBuilder.build());
            }
            ctpBuilder.addAugmentation(new EthCtpPacExtBuilder().setEthCtpPacOut(ethCtpPacOutBuilder.build()).build());
        }
        return ctpBuilder;
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ctps.CtpBuilder apiCtpToDev(ModifyCtpInput input) {
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ctps.CtpBuilder ctpBuilder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ctps.CtpBuilder();
        ctpBuilder.withKey(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ctps.CtpKey(input.getCtpName()))
                .setName(input.getCtpName())
                .setLayerProtocolName(apiLayerNameToDev(input.getLayerProtocolName()))
                .setLoopBack(apiLoopBackTypeToDev(input.getLoopBack()))
                .setServerTp(input.getServerTp());
        return ctpBuilder;
    }

}
