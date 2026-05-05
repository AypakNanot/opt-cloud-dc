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
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ftp.rev200425.EthFtpPacExtBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ftp.rev200425.ModifyEthFtpPacInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ftp.rev200425.ModifyEthVcgInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ftp.rev200425.ModifySdhFtpPacInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ftp.rev200425.SdhFtpPacExtBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ftp.rev200425.eth.ftp.pac.ext.EthFtpPacOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ftp.rev200425.eth.ftp.pac.ext.eth.ftp.pac.output.VcgParametersOutBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ftp.rev200425.sdh.ftp.pac.ext.SdhFtpPacOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.StatePac;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ftp.StatePacBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ftps.grouping.Ftp;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ftps.grouping.FtpBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ftps.grouping.FtpKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.gfpf.parameter.grouping.GfpfParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.gfpf.parameter.grouping.GfpfParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.gfpf.parameter.grouping.GfpfParameterKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev190213.ftps.ftp.eth.ftp.pac.VcgParameters;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ftps.ftp.EthFtpPac;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.ftps.ftp.SdhFtpPac;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * ftp 转换器
 * 2021/10/13 8:50
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class FtpTransformImpl extends AbstractCmccTransformer implements CommonTransform, EnumTransform, ServiceTransform {

    public List<GfpfParameter> devGfpfParameterToApiList(Map<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.gfpf.parameter.grouping.GfpfParameterKey,
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.gfpf.parameter.grouping.GfpfParameter> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<GfpfParameter> resultList = new LinkedList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.gfpf.parameter.grouping.GfpfParameter e : list.values()) {
            resultList.add(devGfpfParameterToApi(e));
        }
        return resultList;
    }

    public GfpfParameter devGfpfParameterToApi(
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.gfpf.parameter.grouping.GfpfParameter gfpfParameter) {
        if (gfpfParameter == null) {
            return null;
        }
        GfpfParameterBuilder gfpfParameterBuilder = new GfpfParameterBuilder();
        gfpfParameterBuilder.withKey(new GfpfParameterKey(gfpfParameter.getEthFtp()))
                .setEthFtp(gfpfParameter.getEthFtp())
                .setPayloadFcs(gfpfParameter.getPayloadFcs())
                .setScrambler(gfpfParameter.getScrambler());
        return gfpfParameterBuilder.build();
    }

    public List<Ftp> devFtpToApiList(List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ftps.Ftp> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<Ftp> resultList = new LinkedList<>();
        for (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ftps.Ftp e : list) {
            resultList.add(devFtpToApi(e).build());
        }
        return resultList;
    }

    public FtpBuilder devFtpToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ftps.Ftp ftp) {
        if (ftp == null) {
            return null;
        }
        FtpBuilder ftpBuilder = new FtpBuilder();
        ftpBuilder.withKey(new FtpKey(ftp.getName()));
        ftpBuilder.setName(ftp.getName());
        ftpBuilder.setServerCtp(ftp.getServerCtp());
        StatePac statePac = devStatePacToApi(ftp.getStatePac());
        if (statePac != null) {
            ftpBuilder.setStatePac(new StatePacBuilder(statePac).build());
        }
        ftpBuilder.setLayerProtocolName(devLayerNameToApi(ftp.getLayerProtocolName()));
        ftpBuilder.setClientCtp(ftp.getClientCtp());
        //对ftp中有扩展内容进行转化
        //eth ftp
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ftp1 ethFtp
                = ftp.augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ftp1.class);
        if (ethFtp != null && ethFtp.getEthFtpPac() != null) {
            EthFtpPac ethFtpPac = ethFtp.getEthFtpPac();
            EthFtpPacOutputBuilder ethPtpPacOutputBuilder = new EthFtpPacOutputBuilder();
            ethPtpPacOutputBuilder.setCurrentMtu(ethFtpPac.getCurrentMtu());
            ethPtpPacOutputBuilder.setMappingType(devEthMappingTypeToApi(ethFtpPac.getMappingType()));
            ethPtpPacOutputBuilder.setServiceMappingMode(devAdaptationTypeToApi(ethFtpPac.getServiceMappingMode()));
            ethPtpPacOutputBuilder.setSupportedMtu(ethFtpPac.getSupportedMtu());
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev190213.EthFtpPac1 vcgParameter
                    = ethFtpPac.augmentation(
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev190213.EthFtpPac1.class);
            //vcg
            if (vcgParameter != null && vcgParameter.getVcgParameters() != null) {
                VcgParameters vcgParameters = vcgParameter.getVcgParameters();
                VcgParametersOutBuilder vcgParametersOutBuilder = new VcgParametersOutBuilder();
                vcgParametersOutBuilder.setHoldOff(vcgParameters.getHoldOff())
                        .setRxNumber(vcgParameters.getRxNumber())
                        .setTxNumber(vcgParameters.getTxNumber())
                        .setVcType(devSwitchTypeToApi(vcgParameters.getVcType()))
                        .setWtr(vcgParameters.getWtr())
                        .setLcas(vcgParameters.getLcas())
                        .setSoHandshakeState(vcgParameters.getSoHandshakeState())
                        .setTsd(vcgParameters.getTsd());
                ethPtpPacOutputBuilder.setVcgParametersOut(vcgParametersOutBuilder.build());
            }
            ftpBuilder.addAugmentation( new EthFtpPacExtBuilder().setEthFtpPacOutput(ethPtpPacOutputBuilder.build()).build());
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ftp1 sdhFtp
                = ftp.augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ftp1.class);
        if (sdhFtp != null && sdhFtp.getSdhFtpPac() != null) {
            SdhFtpPac sdhFtpPac = sdhFtp.getSdhFtpPac();
            SdhFtpPacOutputBuilder sdhFtpPacOutputBuilder = new SdhFtpPacOutputBuilder();
            sdhFtpPacOutputBuilder.setJ0ActualRx(sdhFtpPac.getJ0ActualRx())
                    .setJ0ActualTx(sdhFtpPac.getJ0ActualTx())
                    .setJ0ExpectedRx(sdhFtpPac.getJ0ExpectedRx())
                    .setSdhSignalType(devClientSignalTypeToApi(sdhFtpPac.getSdhSignalType()))
                    .setServiceMappingMode(devAdaptationTypeToApi(sdhFtpPac.getServiceMappingMode()))
                    .setSwitchCapability(devSwitchTypeToApiList(sdhFtpPac.getSwitchCapability()));
            ftpBuilder.addAugmentation(new SdhFtpPacExtBuilder().setSdhFtpPacOutput(sdhFtpPacOutputBuilder.build()).build());
        }
        return ftpBuilder;
    }


    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ftp1Builder
    apiEthFtpToDev(ModifyEthFtpPacInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ftp1Builder ftp1Builder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ftp1Builder();
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ftps.ftp.EthFtpPacBuilder ethFtpPacBuilder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ftps.ftp.EthFtpPacBuilder();
        ethFtpPacBuilder.setCurrentMtu(input.getCurrentMtu());
        ethFtpPacBuilder.setServiceMappingMode(apiAdaptationTypeToDev(input.getServiceMappingMode()));
        ethFtpPacBuilder.setMappingType(apiEthMappingTypeToDev(input.getMappingType()));
        ftp1Builder.setEthFtpPac(ethFtpPacBuilder.build());
        return ftp1Builder;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.gfpf.parameter.grouping.GfpfParameterBuilder
    apiGfpfParameterToDev(ModifyEthFtpPacInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.gfpf.parameter.grouping.GfpfParameterBuilder gfpfParameterBuilder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.gfpf.parameter.grouping.GfpfParameterBuilder();
        gfpfParameterBuilder
                .withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.gfpf.parameter.grouping.GfpfParameterKey(input.getFtpName()))
                .setEthFtp(input.getFtpName())
                .setPayloadFcs(input.getPayloadFcs())
                .setScrambler(input.getScrambler());
        return gfpfParameterBuilder;
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev190213.EthFtpPac1Builder apiEthVcgToApi(ModifyEthVcgInput input){
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev190213.EthFtpPac1Builder ethFtpPac1Builder =
                new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev190213.EthFtpPac1Builder();
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev190213.ftps.ftp.eth.ftp.pac.VcgParametersBuilder vcgParametersBuilder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev190213.ftps.ftp.eth.ftp.pac.VcgParametersBuilder();
        vcgParametersBuilder.setLcas(input.getLcas()).setWtr(input.getWtr())
                .setTsd(input.getTsd()).setHoldOff(input.getHoldOff());
        ethFtpPac1Builder.setVcgParameters(vcgParametersBuilder.build());
        return ethFtpPac1Builder;
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ftp1Builder apiSdhFtpToDev(ModifySdhFtpPacInput input){
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ftp1Builder ftp1Builder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ftp1Builder();
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.ftps.ftp.SdhFtpPacBuilder sdhFtpPacBuilder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.ftps.ftp.SdhFtpPacBuilder();
        sdhFtpPacBuilder.setSdhSignalType(apiClientSignalTypeToDev(input.getSdhSignalType()))
                .setJ0ExpectedRx(input.getJ0ExpectedRx()).setJ0ActualTx(input.getJ0ActualTx());
        ftp1Builder.setSdhFtpPac(sdhFtpPacBuilder.build());
        return ftp1Builder;
    }

}
