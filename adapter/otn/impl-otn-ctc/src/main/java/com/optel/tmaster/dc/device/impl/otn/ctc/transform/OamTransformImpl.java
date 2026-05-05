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
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.cfm.oam.rev200814.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eth.rev210927.EthOamConfigGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eth.rev210927.EthOamStatePacGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.configs.grouping.EthDmConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.configs.grouping.EthDmConfigKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.grouping.EthDm;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.grouping.EthDmBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lb.grouping.EthLbBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lb.grouping.EthLbKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lm.configs.grouping.EthLmConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lm.configs.grouping.EthLmConfigKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lm.grouping.EthLm;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lm.grouping.EthLmBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.oam.mip.configs.grouping.EthOamMipConfigKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.get.eth.dm.output.EthDms;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.get.eth.dm.output.EthDmsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.get.eth.lb.output.EthLbs;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.get.eth.lb.output.EthLbsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.get.eth.lm.output.EthLms;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.get.eth.lm.output.EthLmsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthDmConfigGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthLmConfigGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.OamEthConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.dm.configs.grouping.EthDmConfigBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.lb.grouping.EthLb;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.lm.configs.grouping.EthLmConfigBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.oam.mip.configs.grouping.EthOamMipConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.oam.mip.configs.grouping.EthOamMipConfigBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.oam.eth.config.EthDmConfigs;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.oam.eth.config.EthLmConfigs;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.OamConfigBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.OamStatePacBuilder;

import java.util.*;

/**
 * @author Quan Jingyuan
 * @since 2021/10/14
 **/

public class OamTransformImpl extends AbstractCtcTransformer implements CommonTransform, EnumTransform, ServiceTransform {

    public EthLms devEthLmsToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.get.eth.lm.output.EthLms ethLm){
        EthLmsBuilder ethLmsBuilder=new EthLmsBuilder();
        ethLmsBuilder.setEthLm(devEthLmToApiList(ethLm.getEthLm()));
        return ethLmsBuilder.build();
    }
    public EthDms devEthDmsToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.get.eth.dm.output.EthDms ethDm){
        EthDmsBuilder ethDmsBuilder=new EthDmsBuilder();
        ethDmsBuilder.setEthDm(devEthDmToApiList(ethDm.getEthDm()));
        return ethDmsBuilder.build();
    }
    private EthLm devEthLmToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.lm.grouping.EthLm ethLm){
        if(ethLm==null){
            return null;
        }
        EthLmBuilder ethLmBuilder=new EthLmBuilder();
        if(ethLm.getIndex()!=null){
            ethLmBuilder.setIndex(ethLm.getIndex());
        }
        if(ethLm.getLocalLostPackRatio()!=null){
            ethLmBuilder.setLocalLostPackRatio(ethLm.getLocalLostPackRatio());
        }
        if(ethLm.getLocalReceiveCount()!=null){
            ethLmBuilder.setLocalReceiveCount(ethLm.getLocalReceiveCount());
        }
        if(ethLm.getLocalSendCount()!=null){
            ethLmBuilder.setLocalSendCount(ethLm.getLocalSendCount());
        }
        if(ethLm.getRemoteLostPackRatio()!=null){
            ethLmBuilder.setRemoteLostPackRatio(ethLm.getRemoteLostPackRatio());
        }
        if(ethLm.getRemoteReceiveCount()!=null){
            ethLmBuilder.setRemoteReceiveCount(ethLm.getRemoteReceiveCount());
        }
        if(ethLm.getRemoteSendCount()!=null){
            ethLmBuilder.setRemoteSendCount(ethLm.getRemoteSendCount());
        }
        return ethLmBuilder.build();
    }
    private EthDm devEthDmToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.dm.grouping.EthDm ethDm){
        if(ethDm==null){
            return null;
        }
        EthDmBuilder ethDmBuilder=new EthDmBuilder();
        if(ethDm.getIndex()!=null){
            ethDmBuilder.setIndex(ethDm.getIndex());
        }
        if(ethDm.getDelay()!=null){
            ethDmBuilder.setDelay(ethDm.getDelay());
        }
        if(ethDm.getDelayVariation()!=null){
            ethDmBuilder.setDelayVariation(ethDm.getDelayVariation());
        }
        return ethDmBuilder.build();
    }
    private List<EthLm> devEthLmToApiList(List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.lm.grouping.EthLm> ethLm){
        if(ethLm==null){
            return null;
        }
        List<EthLm> ethLms=new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.lm.grouping.EthLm item:ethLm){
            ethLms.add(devEthLmToApi(item));
        }
        return ethLms;
    }

    private List<EthDm> devEthDmToApiList(List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.dm.grouping.EthDm> ethDm){
        if(ethDm==null){
            return null;
        }
        List<EthDm> ethDms=new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.dm.grouping.EthDm item:ethDm){
            ethDms.add(devEthDmToApi(item));
        }
        return ethDms;
    }
    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetEthLmInputBuilder apiGetEthLmInputToDev(GetEthLmInput input){
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetEthLmInputBuilder builder=new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetEthLmInputBuilder();
        builder.setCtpName(input.getCtpName());
        builder.setName(input.getName());
        return builder;
    }
    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetEthDmInputBuilder apiGetEthDmInputToDev(GetEthDmInput input){
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetEthDmInputBuilder builder=new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetEthDmInputBuilder();
        builder.setCtpName(input.getCtpName());
        builder.setName(input.getName());
        return builder;
    }
    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StopEthLmInputBuilder apiStopEthLmToDev(StopEthLmInput input){
        if (input==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StopEthLmInputBuilder stopEthLmInputBuilder=new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StopEthLmInputBuilder();
        stopEthLmInputBuilder.setCtpName(input.getCtpName());
        stopEthLmInputBuilder.setName(input.getName());
        return stopEthLmInputBuilder;
    }
    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StopEthDmInputBuilder apiStopEthDmToDev(StopEthDmInput input){
        if (input==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StopEthDmInputBuilder stopEthDmInputBuilder=new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StopEthDmInputBuilder();
        stopEthDmInputBuilder.setCtpName(input.getCtpName());
        stopEthDmInputBuilder.setName(input.getName());
        return stopEthDmInputBuilder;
    }
    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthLmInputBuilder apiStartEthLmToDev(StartEthLmInput input){
        if (input==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthLmInputBuilder startEthLmInputBuilder=new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthLmInputBuilder();
        startEthLmInputBuilder.setCtpName(input.getCtpName());
        startEthLmInputBuilder.setName(input.getName());
        return startEthLmInputBuilder;
    }
    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthDmInputBuilder apiStartEthDmToDev(StartEthDmInput input){
        if (input==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthDmInputBuilder startEthDmInputBuilder=new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthDmInputBuilder();
        startEthDmInputBuilder.setCtpName(input.getCtpName());
        startEthDmInputBuilder.setName(input.getName());
        return startEthDmInputBuilder;
    }
    public GetEthLmConfigOutputBuilder devLmConfigToApi(OamEthConfig oamEthConfig) {
        GetEthLmConfigOutputBuilder configOutputBuilder = new GetEthLmConfigOutputBuilder();
        configOutputBuilder.setEthLmConfig(ltm(devLmConfigToApiList(oamEthConfig.getEthLmConfigs())));
        return configOutputBuilder;
    }
    public GetEthDmConfigOutputBuilder devDmConfigToApi(OamEthConfig oamEthConfig) {
        GetEthDmConfigOutputBuilder configOutputBuilder = new GetEthDmConfigOutputBuilder();
        configOutputBuilder.setEthDmConfig(ltm(devDmConfigToApiList(oamEthConfig.getEthDmConfigs())));
        return configOutputBuilder;
    }
    private EthDmConfig devDmConfigToApi(EthDmConfigGrouping ethDmConfig) {
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.configs.grouping.EthDmConfigBuilder ethDmConfigBuilder = new  org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.configs.grouping.EthDmConfigBuilder();
        if (ethDmConfig == null) {
            return ethDmConfigBuilder.build();
        }
        if (ethDmConfig.getDuration() != null) {
            ethDmConfigBuilder.setDuration(ethDmConfig.getDuration());
        }
        if (ethDmConfig.getCtpName() != null) {
            ethDmConfigBuilder.setCtpName(ethDmConfig.getCtpName());
        }
        if(ethDmConfig.getName()!=null){
            ethDmConfigBuilder.withKey(new EthDmConfigKey(ethDmConfig.getName()));
            ethDmConfigBuilder.setName(ethDmConfig.getName());
        }
        if(ethDmConfig.getPriority()!=null){
            ethDmConfigBuilder.setPriority(ethDmConfig.getPriority());
        }
        if(ethDmConfig.getTimes()!=null){
            ethDmConfigBuilder.setTimes(ethDmConfig.getTimes());
        }

        return ethDmConfigBuilder.build();

    }
    private EthLmConfig devLmConfigToApi(EthLmConfigGrouping ethLmConfig) {
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lm.configs.grouping.EthLmConfigBuilder ethLmConfigBuilder = new  org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lm.configs.grouping.EthLmConfigBuilder();
        if (ethLmConfig == null) {
            return ethLmConfigBuilder.build();
        }
        if (ethLmConfig.getDuration() != null) {
            ethLmConfigBuilder.setDuration(ethLmConfig.getDuration());
        }
        if (ethLmConfig.getCtpName() != null) {
            ethLmConfigBuilder.setCtpName(ethLmConfig.getCtpName());
        }
        if(ethLmConfig.getName()!=null){
            ethLmConfigBuilder.withKey(new EthLmConfigKey(ethLmConfig.getName()));
            ethLmConfigBuilder.setName(ethLmConfig.getName());
        }
        if(ethLmConfig.getPriority()!=null){
            ethLmConfigBuilder.setPriority(ethLmConfig.getPriority());
        }
        if(ethLmConfig.getTimes()!=null){
            ethLmConfigBuilder.setTimes(ethLmConfig.getTimes());
        }
        if(ethLmConfig.getWorkingMode()!=null){
            ethLmConfigBuilder.setWorkingMode(devEthLmWorkingModeToApi(ethLmConfig.getWorkingMode()));
        }
        return ethLmConfigBuilder.build();

    }
    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.dm.configs.grouping.EthDmConfig apiDmConfigToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.EthDmConfigGrouping ethDmConfig) {
        EthDmConfigBuilder ethDmConfigBuilder = new  EthDmConfigBuilder();
        if (ethDmConfig == null) {
            return ethDmConfigBuilder.build();
        }
        if (ethDmConfig.getDuration() != null) {
            ethDmConfigBuilder.setDuration(ethDmConfig.getDuration());
        }
        if (ethDmConfig.getCtpName() != null) {
            ethDmConfigBuilder.setCtpName(ethDmConfig.getCtpName());
        }
        if(ethDmConfig.getName()!=null){
            ethDmConfigBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.dm.configs.grouping.EthDmConfigKey(ethDmConfig.getName()));
            ethDmConfigBuilder.setName(ethDmConfig.getName());
        }
        if(ethDmConfig.getPriority()!=null){
            ethDmConfigBuilder.setPriority(ethDmConfig.getPriority());
        }
        if(ethDmConfig.getTimes()!=null){
            ethDmConfigBuilder.setTimes(ethDmConfig.getTimes());
        }
        return ethDmConfigBuilder.build();

    }
    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.lm.configs.grouping.EthLmConfig apiLmConfigToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.EthLmConfigGrouping ethLmConfig) {
        EthLmConfigBuilder ethLmConfigBuilder = new  EthLmConfigBuilder();
        if (ethLmConfig == null) {
            return ethLmConfigBuilder.build();
        }
        if (ethLmConfig.getDuration() != null) {
            ethLmConfigBuilder.setDuration(ethLmConfig.getDuration());
        }
        if (ethLmConfig.getCtpName() != null) {
            ethLmConfigBuilder.setCtpName(ethLmConfig.getCtpName());
        }
        if(ethLmConfig.getName()!=null){
            ethLmConfigBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.lm.configs.grouping.EthLmConfigKey(ethLmConfig.getName()));
            ethLmConfigBuilder.setName(ethLmConfig.getName());
        }
        if(ethLmConfig.getPriority()!=null){
            ethLmConfigBuilder.setPriority(ethLmConfig.getPriority());
        }
        if(ethLmConfig.getTimes()!=null){
            ethLmConfigBuilder.setTimes(ethLmConfig.getTimes());
        }
        if(ethLmConfig.getWorkingMode()!=null){
            ethLmConfigBuilder.setWorkingMode(apiEthLmWorkingModeToDev(ethLmConfig.getWorkingMode()));
        }
        return ethLmConfigBuilder.build();

    }
    private List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.configs.grouping.EthDmConfig> devDmConfigToApiList(EthDmConfigs ethDmConfigs) {
        if (ethDmConfigs == null || ethDmConfigs.getEthDmConfig() == null) {
            return new ArrayList<>();
        }
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.configs.grouping.EthDmConfig> configs = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.dm.configs.grouping.EthDmConfig ethDmConfig : ethDmConfigs.getEthDmConfig().values()) {
            configs.add(devDmConfigToApi(ethDmConfig));
        }
        return configs;
    }
    private Set<EthLmConfig> devLmConfigToApiList(EthLmConfigs ethLmConfigs) {
        if (ethLmConfigs == null || ethLmConfigs.getEthLmConfig() == null) {
            return new HashSet<>();
        }
        Set<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lm.configs.grouping.EthLmConfig> configs = new HashSet<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.lm.configs.grouping.EthLmConfig ethLmConfig : ethLmConfigs.getEthLmConfig().values()) {
            configs.add(devLmConfigToApi(ethLmConfig));
        }
        return configs;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StopEthLbInputBuilder apiStopEtrLbToDev(StopEthLbInput input){
        if(input==null){
            return new  org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StopEthLbInputBuilder();
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StopEthLbInputBuilder stopEthLbInputBuilder=new  org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StopEthLbInputBuilder();
        stopEthLbInputBuilder.setCtpName(input.getCtpName());
        return  stopEthLbInputBuilder;
    }
    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthLbInputBuilder apiStartEtrLbToDev(StartEthLbInput input){
        if(input==null){
            return new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthLbInputBuilder();
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthLbInputBuilder startEthLbInputBuilder=new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthLbInputBuilder();
        if(input.getCtpName()!=null){
            startEthLbInputBuilder.setCtpName(input.getCtpName());
        }
        if(input.getDuration()!=null){
            startEthLbInputBuilder.setDuration(input.getDuration());
        }
        if(input.getLength()!=null){
            startEthLbInputBuilder.setLength(input.getLength());
        }
        if(input.getMac()!=null){
            startEthLbInputBuilder.setMac(input.getMac());
        }
        if(input.getMpId()!=null){
            startEthLbInputBuilder.setMpId(input.getMpId());
        }
        if(input.getPeriod()!=null){
            startEthLbInputBuilder.setPeriod(input.getPeriod());
        }
        if(input.getPriority()!=null){
            startEthLbInputBuilder.setPriority(input.getPriority());
        }
        if(input.getTimes()!=null){
            startEthLbInputBuilder.setTimes(input.getTimes());
        }
        return  startEthLbInputBuilder;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthLtInputBuilder apiStartEthLtToDev(StartEthLtInput input) {
        if(input==null){
            return  new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthLtInputBuilder();
        }

        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthLtInputBuilder startEthLtInputBuilder=new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthLtInputBuilder();
        if(input.getCtpName()!=null){
            startEthLtInputBuilder.setCtpName(input.getCtpName());
        }

        if(input.getTtl()!=null){
            startEthLtInputBuilder.setTtl(input.getTtl());
        }
        if(input.getMac()!=null){
            startEthLtInputBuilder.setMac(input.getMac());
        }
        if(input.getMpId()!=null){
            startEthLtInputBuilder.setMpId(input.getMpId());
        }
        if(input.getPriority()!=null){
            startEthLtInputBuilder.setPriority(input.getPriority());
        }
        return  startEthLtInputBuilder;

    }
    public OamConfigBuilder apiEthOamConfigToDev(EthOamConfigGrouping config){
        if(config==null){
            return  new OamConfigBuilder();
        }
        OamConfigBuilder builder=new OamConfigBuilder();
        if(config.getMdl()!=null){
            builder.setMdl(config.getMdl());
        }
        if(config.getCcInterval()!=null){
            builder.setCcInterval(apiOamTransmitIntervalToDev(config.getCcInterval()));
        }
        if(config.getDmInterval()!=null){
            builder.setDmInterval(apiOamTransmitIntervalToDev(config.getDmInterval()));
        }
        if(config.getLmInterval()!=null){
            builder.setLmInterval(apiOamTransmitIntervalToDev(config.getLmInterval()));
        }
        if(config.getMdName()!=null){
            builder.setMdName(config.getMdName());
        }
        if(config.getMegId()!=null){
            builder.setMegId(config.getMegId());
        }
        if(config.getMel()!=null){
            builder.setMel(config.getMel());
        }
        if(config.getMepId()!=null){
            builder.setMepId(config.getMepId());
        }
        if(config.getRemoteMepId()!=null){
            builder.setRemoteMepId(config.getRemoteMepId());
        }
        return builder;
    }

    public OamStatePacBuilder apiOamStatePacToDev(EthOamStatePacGrouping config){
        if(config==null){
            return new OamStatePacBuilder();
        }
        OamStatePacBuilder statePacBuilder=new OamStatePacBuilder();
        statePacBuilder.setCcState(config.getCcState());
        statePacBuilder.setDmState(config.getDmState());
        statePacBuilder.setLmState(config.getLmState());
        statePacBuilder.setTmState(config.getTmState());
        return  statePacBuilder;
    }
    public GetEthOamMipConfigOutputBuilder devGetEthOamMipConfigOutputToApi(Collection<EthOamMipConfig> ethOamMipConfigs){
        if(ethOamMipConfigs==null){
            return new GetEthOamMipConfigOutputBuilder();
        }
        GetEthOamMipConfigOutputBuilder builder = new GetEthOamMipConfigOutputBuilder();
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.oam.mip.configs.grouping.EthOamMipConfig> list=new ArrayList<>();
        for(EthOamMipConfig item:ethOamMipConfigs){
            org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.oam.mip.configs.grouping.EthOamMipConfigBuilder ethOamMipConfig=new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.oam.mip.configs.grouping.EthOamMipConfigBuilder();
            ethOamMipConfig.setCtpName(item.getCtpName());
            ethOamMipConfig.withKey(new EthOamMipConfigKey(item.key().getCtpName()));
            ethOamMipConfig.setMdName(item.getMdName());
            ethOamMipConfig.setMegId(item.getMegId());
            ethOamMipConfig.setMel(item.getMel());
            ethOamMipConfig.setMipId(item.getMipId());
            list.add(ethOamMipConfig.build());
        }
        if(list.size()!=0){
            builder.setEthOamMipConfig(ltm(list));
        }
        return builder;
    }

    public EthOamMipConfigBuilder apiEthOamMipConfigToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.EthOamMipConfigGrouping  configGrouping){
        if(configGrouping==null){
            return new EthOamMipConfigBuilder();
        }
        EthOamMipConfigBuilder builder = new EthOamMipConfigBuilder();
        builder.setCtpName(configGrouping.getCtpName());
        builder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.oam.mip.configs.grouping.EthOamMipConfigKey(configGrouping.getCtpName()));
        builder.setMdName(configGrouping.getMdName());
        builder.setMegId(configGrouping.getMegId());
        builder.setMipId(configGrouping.getMipId());
        builder.setMel(configGrouping.getMel());
        return builder;
    }
    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetEthLbInputBuilder apiGetEthLbInputToDev(GetEthLbInput ethLbInput){
        if(ethLbInput==null){
            return new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetEthLbInputBuilder();
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetEthLbInputBuilder builder = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetEthLbInputBuilder();
        builder.setCtpName(ethLbInput.getCtpName());

        return builder;
    }
    public EthLbs devEthLbsToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.get.eth.lb.output.EthLbs ethLbs){
        if(ethLbs==null || ethLbs.getEthLb() == null){
            return null;
        }
        EthLbsBuilder ethLbsBuilder=new EthLbsBuilder();
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lb.grouping.EthLb> ethLbList=new ArrayList<>();
        for (EthLb ethLb:ethLbs.getEthLb().values()){
            EthLbBuilder ethLbBuilder=new EthLbBuilder();
            ethLbBuilder.setBytes(ethLb.getBytes());
            ethLbBuilder.withKey(new EthLbKey(ethLb.key().getSequence()));
            ethLbBuilder.setSequence(ethLb.getSequence());
            ethLbBuilder.setTime(ethLb.getTime());
            ethLbList.add(ethLbBuilder.build());
        }
        ethLbsBuilder.setEthLb(ltm(ethLbList));
        return ethLbsBuilder.build();
    }
}
