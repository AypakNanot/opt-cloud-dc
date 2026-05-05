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
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.ServiceTransform;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.*;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.eth.mapping.pac.ext.EthMappingPacOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.eth.ptp.pac.ext.EthPtpPacOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.odu.ptp.pac.ext.OduPtpPacOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.pdh.ptp.pac.ext.PdhPtpPacOutBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.sdh.mapping.pac.ext.SdhMappingPacOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.sdh.ptp.pac.ext.SdhPtpPacOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.Capacity;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.StatePac;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.mcs.groping.McPort;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.mcs.groping.McPortBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.mcs.groping.McPortKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ptps.grouping.Ptp;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ptps.grouping.PtpBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ptps.grouping.PtpKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ptps.grouping.ptp.StatePacBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.types.rev210927.ClientSignalType;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.alarm.delay.inserts.grouping.AlarmDelayInsert;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.alarm.delay.inserts.grouping.AlarmDelayInsertBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.alarm.delay.inserts.grouping.AlarmDelayInsertKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.ptp.band.width.grouping.EthPtpBandWidth;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.ptp.band.width.grouping.EthPtpBandWidthBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.ptp.band.width.grouping.EthPtpBandWidthKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.ptp.band.width.grouping.eth.ptp.band.width.EgressCapacityBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.ptp.band.width.grouping.eth.ptp.band.width.IngressCapacityBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.ptp.frame.spaces.grouping.EthPtpFrameSpace;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.ptp.frame.spaces.grouping.EthPtpFrameSpaceBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.ptp.frame.spaces.grouping.EthPtpFrameSpaceKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.lpt.grouping.Lpt;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.lpt.grouping.LptBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.lpt.grouping.LptKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ptp.extension.grouping.PtpExtension;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ptp.extension.grouping.PtpExtensionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ptp.extension.grouping.PtpExtensionKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ptp.extension.parameter.grouping.EthPtpExtension;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ptp.extension.parameter.grouping.EthPtpExtensionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ptp.extension.parameter.grouping.OduPtpExtension;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ptp.extension.parameter.grouping.OduPtpExtensionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ptp.type.grouping.PtpType;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ptp.type.grouping.PtpTypeBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ptp.type.grouping.PtpTypeKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.SetOduTerminationPointTypeInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.SetOduTerminationPointTypeInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.extension.PtpExtensions;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ptps.ptp.EthMappingPac;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ptps.ptp.EthPtpPac;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.ptps.ptp.OduPtpPac;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.ptps.ptp.PdhPtpPac;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.ptps.ptp.SdhMappingPac;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.ptps.ptp.SdhPtpPac;

import java.util.*;

/**
 * PTP 数据转换器
 * 2021/10/9 10:23
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class PtpTransformImpl extends AbstractCmccTransformer implements CommonTransform, EnumTransform, ServiceTransform {

    public List<Ptp>
    devPtpToApiList(List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ptps.Ptp> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<Ptp> resultList
                = new LinkedList<>();
        for (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ptps.Ptp ptp : list) {
            resultList.add(devPtpToApi(ptp).build());
        }
        return resultList;
    }

    /**
     * PTP dev to api
     *
     * @param ptp dev
     * @return api
     */
    public PtpBuilder devPtpToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ptps.Ptp ptp) {
        if (ptp == null) {
            return null;
        }
        PtpBuilder ptpBuilder = new PtpBuilder();
        ptpBuilder.setCtp(ptp.getCtp());
        StatePac statePac = devStatePacToApi(ptp.getStatePac());
        if (statePac != null) {
            ptpBuilder.setStatePac(new StatePacBuilder(statePac).build());
        }
        ptpBuilder.setInterfaceType(devInterfaceTypeToApi(ptp.getInterfaceType()));
        ptpBuilder.setSupportedLayerProtocolName(devLayerNameToApi(ptp.getSupportedLayerProtocolName()));
        ptpBuilder.withKey(new PtpKey(ptp.getName()));
        ptpBuilder.setLaserStatus(devLaserStatusToApi(ptp.getLaserStatus()));
        ptpBuilder.setLayerProtocolName(devLayerNameToApi(ptp.getLayerProtocolName()));
        ptpBuilder.setLocalTcpId(ptp.getLocalTcpId());
        ptpBuilder.setLoopBack(devLoopbackTypeToApi(ptp.getLoopBack()));
        ptpBuilder.setName(ptp.getName());
        ptpBuilder.setOpticalPowerPac(devOpticalPowerPacToApi(ptp.getOpticalPowerPac()));
        ptpBuilder.setPeerIpAddress(ptp.getPeerIpAddress());
        ptpBuilder.setPeerTcpId(ptp.getPeerTcpId());
        ptpBuilder.setRemotePtp(ptp.getRemotePtp());
        //转化成对应输出的扩展
        //eth
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ptp2 ethPtp =
                ptp.augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ptp2.class);
        if (ethPtp != null && ethPtp.getEthPtpPac() != null) {
            EthPtpPacOutputBuilder ethPtpPacOutputBuilder = new EthPtpPacOutputBuilder();
            EthPtpPac ethPtpPac = ethPtp.getEthPtpPac();
            ethPtpPacOutputBuilder.setCurrentMtu(ethPtpPac.getCurrentMtu());
            ethPtpPacOutputBuilder.setCurrentWorkingMode(devWorkingModeToApi(ethPtpPac.getCurrentWorkingMode()));
            ethPtpPacOutputBuilder.setLldpPeerChassisId(ethPtpPac.getLldpPeerChassisId());
            ethPtpPacOutputBuilder.setLldpPeerPortId(ethPtpPac.getLldpPeerPortId());
            ethPtpPacOutputBuilder.setLldpPeerSystemName(ethPtpPac.getLldpPeerSystemName());
            ethPtpPacOutputBuilder.setMacAddress(ethPtpPac.getMacAddress());
            ethPtpPacOutputBuilder.setPortType(devPortTypeToApi(ethPtpPac.getPortType()));
            ethPtpPacOutputBuilder.setSupportedMtu(ethPtpPac.getSupportedMtu());
            ethPtpPacOutputBuilder.setSupportedWorkingMode(devWorkingModeToApiList(ethPtpPac.getSupportedWorkingMode()));
            ethPtpPacOutputBuilder.setLldpEnable(ethPtpPac.getLldpEnable());
            ethPtpPacOutputBuilder.setPauseControl(ethPtpPac.getPauseControl());
            ptpBuilder.addAugmentation(new EthPtpPacExtBuilder().setEthPtpPacOutput(ethPtpPacOutputBuilder.build()).build());
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ptp1 ethMapping
                = ptp.augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ptp1.class);
        if (ethMapping != null && ethMapping.getEthMappingPac() != null) {
            EthMappingPac ethMappingPac = ethMapping.getEthMappingPac();
            EthMappingPacOutputBuilder ethMappingPacOutputBuilder = new EthMappingPacOutputBuilder();
            ethMappingPacOutputBuilder.setSupportedMappingType(devEthMappingTypeToApiList(ethMappingPac.getSupportedMappingType()));
            ethMappingPacOutputBuilder.setSupportedOduServerSwitchCapability(
                    devSwitchTypeToApiList(ethMappingPac.getSupportedOduServerSwitchCapability()));
            ethMappingPacOutputBuilder.setSupportedSdhServerSwitchCapability(
                    devSwitchTypeToApiList(ethMappingPac.getSupportedSdhServerSwitchCapability()));
            ptpBuilder.addAugmentation(new EthMappingPacExtBuilder().setEthMappingPacOutput(ethMappingPacOutputBuilder.build()).build());
        }
        //odu
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Ptp1 oduPtp
                = ptp.augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Ptp1.class);
        if (oduPtp != null && oduPtp.getOduPtpPac() != null) {
            OduPtpPacOutputBuilder oduPtpPacOutputBuilder = new OduPtpPacOutputBuilder();
            OduPtpPac oduPtpPac = oduPtp.getOduPtpPac();
            oduPtpPacOutputBuilder.setAdaptationType(devAdaptationTypeToApiList(oduPtpPac.getAdaptationType()));
            oduPtpPacOutputBuilder.setOduCapacity(oduPtpPac.getOduCapacity());
            oduPtpPacOutputBuilder.setOduSignalType(devClientSignalTypeToApiList(oduPtpPac.getOduSignalType()));
            oduPtpPacOutputBuilder.setPmtrailTraceActualRx(oduPtpPac.getPmtrailTraceActualRx());
            oduPtpPacOutputBuilder.setPmtrailTraceActualTx(oduPtpPac.getPmtrailTraceActualTx());
            oduPtpPacOutputBuilder.setPmtrailTraceExpectedRx(oduPtpPac.getPmtrailTraceExpectedRx());
            oduPtpPacOutputBuilder.setSmtrailTraceActualRx(oduPtpPac.getSmtrailTraceActualRx());
            oduPtpPacOutputBuilder.setSmtrailTraceActualTx(oduPtpPac.getSmtrailTraceActualTx());
            oduPtpPacOutputBuilder.setSmtrailTraceExpectedRx(oduPtpPac.getSmtrailTraceExpectedRx());
            oduPtpPacOutputBuilder.setSwitchCapability(devSwitchTypeToApiList(oduPtpPac.getSwitchCapability()));
            oduPtpPacOutputBuilder.setTsDetail(oduPtpPac.getTsDetail());
            ptpBuilder.addAugmentation(new OduPtpPacExtBuilder().setOduPtpPacOutput(oduPtpPacOutputBuilder.build()).build());
        }
        //sdh
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ptp2 sdhPtp
                = ptp.augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ptp2.class);
        if (sdhPtp != null && sdhPtp.getSdhPtpPac() != null) {
            SdhPtpPacOutputBuilder sdhPtpPacOutputBuilder = new SdhPtpPacOutputBuilder();
            SdhPtpPac sdhPtpPac = sdhPtp.getSdhPtpPac();
            ClientSignalType currentSdhSignalType = devClientSignalTypeToApi(sdhPtpPac.getCurrentSdhSignalType());
            sdhPtpPacOutputBuilder.setCurrentSdhSignalType(Optional.ofNullable(currentSdhSignalType).orElseGet(() -> devClientSignalTypeToApi(sdhPtpPac.getCurrentSignalType())));
            sdhPtpPacOutputBuilder.setJ0ActualRx(sdhPtpPac.getJ0ActualRx());
            sdhPtpPacOutputBuilder.setJ0ActualTx(sdhPtpPac.getJ0ActualTx());
            sdhPtpPacOutputBuilder.setJ0ExpectedRx(sdhPtpPac.getJ0ExpectedRx());
            Set<ClientSignalType> supportedSdhSignalTypes = devClientSignalTypeToApiList(sdhPtpPac.getSupportedSdhSignalTypes());
            sdhPtpPacOutputBuilder.setSupportedSdhSignalTypes(Optional.ofNullable(supportedSdhSignalTypes).orElseGet(() -> devClientSignalTypeToApiList(sdhPtpPac.getSupportedSignalTypes())));
            sdhPtpPacOutputBuilder.setSwitchCapability(devSwitchTypeToApiList(sdhPtpPac.getSwitchCapability()));
            ptpBuilder.addAugmentation(new SdhPtpPacExtBuilder().setSdhPtpPacOutput(sdhPtpPacOutputBuilder.build()).build());
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ptp1 sdhMapping
                = ptp.augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ptp1.class);
        if (sdhMapping != null && sdhMapping.getSdhMappingPac() != null) {
            SdhMappingPac sdhMappingPac = sdhMapping.getSdhMappingPac();
            SdhMappingPacOutputBuilder sdhMappingPacOutputBuilder = new SdhMappingPacOutputBuilder();
            sdhMappingPacOutputBuilder.setSupportedSdhSignalTypes(
                    devClientSignalTypeToApiList(sdhMappingPac.getSupportedSdhSignalTypes()));
            sdhMappingPacOutputBuilder.setSwitchCapability(devSwitchTypeToApiList(sdhMappingPac.getSwitchCapability()));
            ptpBuilder.addAugmentation(new SdhMappingPacExtBuilder().setSdhMappingPacOutput(
                    sdhMappingPacOutputBuilder.build()).build());
        }
        //pdh
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.Ptp1 pdhPtp
                = ptp.augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.Ptp1.class);
        if (pdhPtp != null && pdhPtp.getPdhPtpPac() != null) {
            PdhPtpPac pdhPtpPac = pdhPtp.getPdhPtpPac();
            PdhPtpPacOutBuilder pdhPtpPacOutBuilder = new PdhPtpPacOutBuilder();
            pdhPtpPacOutBuilder.setE1FrameType(devE1FrameTypeToApi(pdhPtpPac.getE1FrameType()));
            pdhPtpPacOutBuilder.setE1Opcode(devE1OpcodeToApi(pdhPtpPac.getE1Opcode()));
            pdhPtpPacOutBuilder.setE1PhyType(devE1PhyTypeToApi(pdhPtpPac.getE1PhyType()));
            ptpBuilder.addAugmentation(new PdhPtpPacExtBuilder().setPdhPtpPacOut(pdhPtpPacOutBuilder.build()).build());
        }
        return ptpBuilder;
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ptps.PtpBuilder apiModifyPtpToDev(ModifyPtpInput input) {
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ptps.PtpBuilder ptpBuilder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ptps.PtpBuilder();
        ptpBuilder.withKey(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ptps.PtpKey(input.getPtpName()))
                .setName(input.getPtpName()).setInterfaceType(apiInterfaceToDev(input.getInterfaceType()))
                .setLoopBack(apiLoopBackTypeToDev(input.getLoopBack()))
                .setLayerProtocolName(apiLayerNameToDev(input.getLayerProtocolName()));
        return ptpBuilder;
    }

    public List<EthPtpBandWidth> devEthPtpsBandWidthToApiList(
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.EthPtpsBandWidth ethPtpsBandWidth) {
        if (ethPtpsBandWidth == null) {
            return null;
        }
        @Nullable Map<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.band.width.grouping.EthPtpBandWidthKey, org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.band.width.grouping.EthPtpBandWidth> list
                = ethPtpsBandWidth.getEthPtpBandWidth();
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<EthPtpBandWidth> resultList = new LinkedList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.band.width.grouping.EthPtpBandWidth e : list.values()) {
            resultList.add(devEthPtpsBandWidthToApi(e).build());
        }
        return resultList;
    }

    /**
     * EthPtpsBandWidthBuilder dev to api
     *
     * @param ethPtpBandWidth dev
     * @return api
     */
    public EthPtpBandWidthBuilder devEthPtpsBandWidthToApi(
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.band.width.grouping.EthPtpBandWidth ethPtpBandWidth) {
        if (ethPtpBandWidth == null) {
            return null;
        }
        EthPtpBandWidthBuilder ethPtpBandWidthBuilder = new EthPtpBandWidthBuilder();
        ethPtpBandWidthBuilder.setName(ethPtpBandWidth.getName()).withKey(new EthPtpBandWidthKey(ethPtpBandWidth.getName()));
        Capacity ingressCapacity = devCapacityToApi(ethPtpBandWidth.getIngressCapacity());
        if (ingressCapacity != null) {
            ethPtpBandWidthBuilder.setIngressCapacity(new IngressCapacityBuilder(ingressCapacity).build());
        }
        Capacity egressCapacity = devCapacityToApi(ethPtpBandWidth.getEgressCapacity());
        if (egressCapacity != null) {
            ethPtpBandWidthBuilder.setEgressCapacity(new EgressCapacityBuilder(egressCapacity).build());
        }
        return ethPtpBandWidthBuilder;
    }


    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ptps.PtpBuilder apiOpticalPowerToDev(ModifyPtpOpticalPowerPacInput input) {
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ptps.PtpBuilder
                ptpBuilder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ptps.PtpBuilder();
        ptpBuilder.setName(input.getPtpName()).setLaserStatus(apiLaserStatusToDev(input.getLaserStatus()));
        if (input.getOutputPower() != null) {
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ptps.ptp.OpticalPowerPacBuilder opticalPowerPacBuilder
                    = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ptps.ptp.OpticalPowerPacBuilder();
            opticalPowerPacBuilder.setOutputPower(apiRealToDev(input.getOutputPower()));
            ptpBuilder.setOpticalPowerPac(opticalPowerPacBuilder.build());
        }
        return ptpBuilder;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lpt.grouping.LptBuilder apiLptToDev(ModifyLptInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lpt.grouping.LptBuilder builder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lpt.grouping.LptBuilder();
        builder.withKey(
                        new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lpt.grouping.LptKey(input.getPtpName()))
                .setName(input.getPtpName())
                .setAdminState(apiAdminStateToDev(input.getAdminState()))
                .setLptMode(apiLptModeToDev(input.getLptMode()));
        return builder;

    }

    public List<Lpt> devLptToApiList(
            List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lpt.grouping.Lpt> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<Lpt> resultList = new LinkedList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lpt.grouping.Lpt e : list) {
            resultList.add(devLptToApi(e).build());
        }
        return resultList;
    }

    public LptBuilder devLptToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lpt.grouping.Lpt lpt) {
        if (lpt == null) {
            return null;
        }
        LptBuilder lptBuilder = new LptBuilder();
        lptBuilder.withKey(new LptKey(lpt.getName()))
                .setName(lpt.getName())
                .setLptMode(devLptModeToApi(lpt.getLptMode()))
                .setAdminState(devAdminStateToApi(lpt.getAdminState()));
        return lptBuilder;
    }

    public List<PtpExtension> devPtpExtensionToApiList(
            Collection<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtension> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<PtpExtension> resultList = new LinkedList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtension e : list) {
            resultList.add(devPtpExtensionToApi(e).build());
        }
        return resultList;
    }

    public PtpExtensionBuilder devPtpExtensionToApi(
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtension ptpExtension) {
        if (ptpExtension == null) {
            return null;
        }
        PtpExtensionBuilder ptpExtensionBuilder = new PtpExtensionBuilder();
        ptpExtensionBuilder.withKey(new PtpExtensionKey(ptpExtension.getName()))
                .setName(ptpExtension.getName())
                .setLaserAutoEnable(ptpExtension.getLaserAutoEnable())
                .setEthPtpExtension(devEthPtpExtensionToApi(ptpExtension.getEthPtpExtension()))
                .setOduPtpExtension(devOduPtpExtensionToApi(ptpExtension.getOduPtpExtension()));
        return ptpExtensionBuilder;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtensionBuilder apiPtpExtensionToDev(ModifyOduPtpExtensionInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtensionBuilder ptpExtensionBuilder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtensionBuilder();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.parameter.grouping.OduPtpExtensionBuilder oduPtpExtensionBuilder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.parameter.grouping.OduPtpExtensionBuilder();
        oduPtpExtensionBuilder.setPmTimAction(input.getPmTimAction())
                .setPmTimMode(apiTimModeToDev(input.getPmTimMode()))
                .setSmTimAction(input.getSmTimAction())
                .setSmTimMode(apiTimModeToDev(input.getSmTimMode()));
        ptpExtensionBuilder.withKey(
                        new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtensionKey(input.getPtpName()))
                .setOduPtpExtension(oduPtpExtensionBuilder.build());
        return ptpExtensionBuilder;
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ptp2Builder apiSdhPtpToDev(ModifySdhPtpPacInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.ptps.ptp.SdhPtpPacBuilder ptpPacBuilder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.ptps.ptp.SdhPtpPacBuilder();
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.ClientSignalType currentSdhSignalType = apiClientSignalTypeToDev(input.getCurrentSdhSignalType());
        ptpPacBuilder.setCurrentSdhSignalType(currentSdhSignalType)
                .setCurrentSignalType(currentSdhSignalType)
                .setJ0ActualTx(input.getJ0ActualTx())
                .setJ0ExpectedRx(input.getJ0ExpectedRx());
        return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ptp2Builder().setSdhPtpPac(ptpPacBuilder.build());
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.Ptp1Builder apiPdhPtpToDev(ModifyPdhPtpInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.ptps.ptp.PdhPtpPacBuilder pdh
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.ptps.ptp.PdhPtpPacBuilder();
        pdh.setE1FrameType(apiE1FrameTypeToDev(input.getE1FrameType()))
                .setE1Opcode(apiE1OpcodeToDev(input.getE1Opcode()));
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.Ptp1Builder ptp1Builder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.Ptp1Builder();
        ptp1Builder.setPdhPtpPac(pdh.build());
        return ptp1Builder;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtensionBuilder
    apiPtpExtensionToDev(ModifyLaserAutoEnableInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtensionBuilder ptpExtensionBuilder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtensionBuilder();
        ptpExtensionBuilder.withKey(
                        new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtensionKey(input.getPtpName()))
                .setLaserAutoEnable(input.getLaserAutoEnable());
        return ptpExtensionBuilder;
    }

    /**
     * EthPtpExtension dev to api
     *
     * @param ethPtpExtension dev
     * @return api
     */
    public EthPtpExtension devEthPtpExtensionToApi(
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.parameter.grouping.EthPtpExtension ethPtpExtension) {
        if (ethPtpExtension == null) {
            return null;
        }
        EthPtpExtensionBuilder ethPtpExtensionBuilder = new EthPtpExtensionBuilder();
        ethPtpExtensionBuilder.setPortMode(devEthPortModeToApi(ethPtpExtension.getPortMode()))
                .setPvid(ethPtpExtension.getPvid())
                .setQinqEnable(ethPtpExtension.getQinqEnable());
        return ethPtpExtensionBuilder.build();
    }

    public OduPtpExtension devOduPtpExtensionToApi(
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.parameter.grouping.OduPtpExtension oduPtpExtension) {
        if (oduPtpExtension == null) {
            return null;
        }
        OduPtpExtensionBuilder oduPtpExtensionBuilder = new OduPtpExtensionBuilder();
        oduPtpExtensionBuilder.setPmTimAction(oduPtpExtension.getPmTimAction())
                .setPmTimMode(devTimModeToApi(oduPtpExtension.getPmTimMode()))
                .setSmTimAction(oduPtpExtension.getSmTimAction())
                .setSmTimMode(devTimModeToApi(oduPtpExtension.getSmTimMode()));
        return oduPtpExtensionBuilder.build();
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Ptp1Builder apiOduPtpToApi(ModifyOduPtpPacInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.ptps.ptp.OduPtpPacBuilder ptpPacBuilder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.ptps.ptp.OduPtpPacBuilder();
        ptpPacBuilder.setSmtrailTraceActualTx(input.getSmtrailTraceActualTx())
                .setSmtrailTraceExpectedRx(input.getSmtrailTraceExpectedRx())
                .setPmtrailTraceActualTx(input.getPmtrailTraceActualTx())
                .setPmtrailTraceExpectedRx(input.getPmtrailTraceExpectedRx());
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Ptp1Builder ptp1Builder =
                new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Ptp1Builder().setOduPtpPac(ptpPacBuilder.build());
        return ptp1Builder;
    }

    public List<McPort> devMcPortToApiList(Collection<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.mc.ports.McPort> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<McPort> resultList = new LinkedList<>();
        for (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.mc.ports.McPort e : list) {
            resultList.add(devMcPortToApi(e).build());
        }
        return resultList;
    }

    public McPortBuilder devMcPortToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.mc.ports.McPort mcPort) {
        if (mcPort == null) {
            return null;
        }
        McPortBuilder mcPortBuilder = new McPortBuilder();
        mcPortBuilder.withKey(new McPortKey(mcPort.getName()))
                .setName(mcPort.getName())
                .setAdminState(devAdminStateToApi(mcPort.getAdminState()))
                .setOperationalState(devOperationalStateToApi(mcPort.getOperationalState()))
                .setMacAddress(mcPort.getMacAddress());
        return mcPortBuilder;
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.mc.ports.McPortBuilder apiMcPortToDev(ModifyMcPortInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.mc.ports.McPortBuilder mcPortBuilder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.mc.ports.McPortBuilder();
        mcPortBuilder.withKey(
                        new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.mc.ports.McPortKey(input.getName()))
                .setName(input.getName())
                .setAdminState(apiAdminStateToDev(input.getAdminState()));
        return mcPortBuilder;
    }

    public List<AlarmDelayInsert> devAlarmDelayInsertToApiList(
            Collection<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.alarm.delay.inserts.grouping.AlarmDelayInsert> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<AlarmDelayInsert> resultList = new LinkedList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.alarm.delay.inserts.grouping.AlarmDelayInsert e : list) {
            resultList.add(devAlarmDelayInsertToApi(e).build());
        }
        return resultList;
    }

    public AlarmDelayInsertBuilder devAlarmDelayInsertToApi(
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.alarm.delay.inserts.grouping.AlarmDelayInsert alarmDelayInsert) {
        AlarmDelayInsertBuilder alarmDelayInsertBuilder = new AlarmDelayInsertBuilder();
        alarmDelayInsertBuilder.withKey(new AlarmDelayInsertKey(alarmDelayInsert.getName()))
                .setName(alarmDelayInsert.getName())
                .setDelayTime(alarmDelayInsert.getDelayTime())
                .setInsertType(devInsertTypeToApi(alarmDelayInsert.getInsertType()));
        return alarmDelayInsertBuilder;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.alarm.delay.inserts.grouping.AlarmDelayInsertBuilder
    apiAlarmDelayInsertToDev(ModifyAlarmDelayInsertInput input) {
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.alarm.delay.inserts.grouping.AlarmDelayInsertBuilder alarmDelayInsertBuilder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.alarm.delay.inserts.grouping.AlarmDelayInsertBuilder();
        alarmDelayInsertBuilder
                .withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.alarm.delay.inserts.grouping.AlarmDelayInsertKey(input.getName()))
                .setName(input.getName())
                .setDelayTime(input.getDelayTime())
                .setInsertType(apiInsertTypeToDev(input.getInsertType()));
        return alarmDelayInsertBuilder;
    }


    public SetOduTerminationPointTypeInput apiModifyOduTypeInputToDev(ModifyOduPtpTypeInput input) {
        if (input == null) {
            return null;
        }
        SetOduTerminationPointTypeInputBuilder setOduTerminationPointTypeInputBuilder = new SetOduTerminationPointTypeInputBuilder();
        setOduTerminationPointTypeInputBuilder.setOduPtpName(input.getOduPtpName());
        setOduTerminationPointTypeInputBuilder.setSignalType(apiClientSignalTypeToDev(input.getSignalType()));
        return setOduTerminationPointTypeInputBuilder.build();
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ptp2Builder apiEthPtpToDev(ModifyEthPtpPacInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ptp2Builder ptp2Builder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ptp2Builder();
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ptps.ptp.EthPtpPacBuilder ptpPacBuilder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ptps.ptp.EthPtpPacBuilder();

        ptpPacBuilder
                .setCurrentWorkingMode(apiWorkingModeToDev(input.getCurrentWorkingMode()))
                .setCurrentMtu(input.getCurrentMtu())
                .setPauseControl(input.getPauseControl())
                .setMacAddress(input.getMacAddress())
                .setLldpEnable(input.getLldpEnable());
        ptp2Builder.setEthPtpPac(ptpPacBuilder.build());
        return ptp2Builder;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.band.width.grouping.EthPtpBandWidthBuilder
    apiEthPtpBandWidthToDev(ModifyEthPtpPacInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.band.width.grouping.EthPtpBandWidthBuilder bandWidthBuilder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.band.width.grouping.EthPtpBandWidthBuilder();
        if (input.getIngressCapacity() != null || input.getEgressCapacity() != null) {
            bandWidthBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn
                    .extension.rev200805.eth.ptp.band.width.grouping.EthPtpBandWidthKey(input.getPtpName()));
            if (input.getIngressCapacity() != null) {
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.Capacity ingressCapacity
                        = apiCapacityToDev(input.getIngressCapacity());
                if (ingressCapacity != null) {
                    bandWidthBuilder.setIngressCapacity(new org.opendaylight.yang.gen.v1.com.optel.yang.otn
                            .extension.rev200805.eth.ptp.band.width.grouping.eth.ptp.band.width
                            .IngressCapacityBuilder(ingressCapacity).build());
                }
            }
            if (input.getEgressCapacity() != null) {
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.Capacity egressCapacity
                        = apiCapacityToDev(input.getEgressCapacity());
                if (egressCapacity != null) {
                    bandWidthBuilder.setEgressCapacity(
                            new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth
                                    .ptp.band.width.grouping.eth.ptp.band.width.EgressCapacityBuilder(egressCapacity).build());
                }

            }
        }
        return bandWidthBuilder;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtensionBuilder
    apiEthPtpExtensionToDev(ModifyEthPtpPacInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtensionBuilder ptpExtensionBuilder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtensionBuilder();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.parameter.grouping.EthPtpExtensionBuilder ethPtpExtensionBuilder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.parameter.grouping.EthPtpExtensionBuilder();
        ethPtpExtensionBuilder
                .setPvid(input.getPvid())
                .setPortMode(apiEthPortModeToDev(input.getPortMode()))
                .setQinqEnable(input.getQinqEnable());
        ptpExtensionBuilder
                .withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtensionKey(input.getPtpName()))
                .setName(input.getPtpName())
                .setEthPtpExtension(ethPtpExtensionBuilder.build());
        return ptpExtensionBuilder;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.SetEthPtpOpticalElectricalInput apiPtpPortTypeToDev(ModifyEthPtpPacInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.SetEthPtpOpticalElectricalInputBuilder setEthPtpOpticalElectricalInputBuilder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.SetEthPtpOpticalElectricalInputBuilder();
        setEthPtpOpticalElectricalInputBuilder.
                setEthPtpName(input.getPtpName()).setPortType(apiPortTypeToDev(input.getPortType()));
        return setEthPtpOpticalElectricalInputBuilder.build();
    }

    public List<PtpType> devPortTypeToApiList(Collection<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.type.grouping.PtpType> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<PtpType> resultList = new LinkedList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.type.grouping.PtpType e : list) {
            resultList.add(devPortTypeToApi(e).build());
        }
        return resultList;
    }

    public PtpTypeBuilder devPortTypeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.type.grouping.PtpType ptpType) {
        if (ptpType == null) {
            return null;
        }
        PtpTypeBuilder ptpTypeBuilder = new PtpTypeBuilder();
        ptpTypeBuilder.withKey(new PtpTypeKey(ptpType.getName()))
                .setName(ptpType.getName())
                .setPtpType(devClientSignalTypeToApi(ptpType.getPtpType()));
        return ptpTypeBuilder;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.frame.spaces.grouping.EthPtpFrameSpaceBuilder apiEthPtpFrameSpaceToDev(ModifyEthPtpPacInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.frame.spaces.grouping.EthPtpFrameSpaceBuilder ethPtpFrameSpaceBuilder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.frame.spaces.grouping.EthPtpFrameSpaceBuilder();
        ethPtpFrameSpaceBuilder
                .withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.frame.spaces.grouping.EthPtpFrameSpaceKey(input.getPtpName()))
                .setFrameSpace(input.getFrameSpace());
        return ethPtpFrameSpaceBuilder;
    }

    public List<EthPtpFrameSpace> devEthPtpFrameSpaceToApiList(Collection<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.frame.spaces.grouping.EthPtpFrameSpace> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<EthPtpFrameSpace> resultList = new LinkedList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.frame.spaces.grouping.EthPtpFrameSpace e : list) {
            resultList.add(devEthPtpFrameSpaceToApi(e).build());
        }
        return resultList;
    }

    public EthPtpFrameSpaceBuilder devEthPtpFrameSpaceToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.frame.spaces.grouping.EthPtpFrameSpace ethPtpFrameSpace) {
        if (ethPtpFrameSpace == null) {
            return null;
        }
        EthPtpFrameSpaceBuilder ethPtpFrameSpaceBuilder = new EthPtpFrameSpaceBuilder();
        ethPtpFrameSpaceBuilder.withKey(new EthPtpFrameSpaceKey(ethPtpFrameSpace.getName()))
                .setName(ethPtpFrameSpace.getName())
                .setFrameSpace(ethPtpFrameSpace.getFrameSpace());
        return ethPtpFrameSpaceBuilder;
    }

    public List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtension> getPtpExtensionList(PtpExtensions ptpExtensionsData) {
        List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtension> listDate = new LinkedList<>();
        if (ptpExtensionsData != null && ptpExtensionsData.getPtpExtension() != null) {
            Collection<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtension> ptpExtensionList = ptpExtensionsData.getPtpExtension().values();
            for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtension e : ptpExtensionList) {
                if (e.getEthPtpExtension() != null) {
                    listDate.add(e);
                }
            }
        }
        return listDate;
    }

}
