/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.AbstractCmccTransformer;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.EnumTransform;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.ServiceTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.cfm.oam.rev200814.GetEthDmInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.cfm.oam.rev200814.GetEthLbInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.cfm.oam.rev200814.GetEthLmInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.cfm.oam.rev200814.StartEthDmInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.cfm.oam.rev200814.StartEthLbInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.cfm.oam.rev200814.StartEthLmInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.cfm.oam.rev200814.StartEthLtInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.cfm.oam.rev200814.StopEthDmInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.cfm.oam.rev200814.StopEthLbInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.cfm.oam.rev200814.StopEthLmInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.cfm.oam.rev200814.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eth.rev210927.EthOamConfigGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eth.rev210927.EthOamStatePacGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.configs.grouping.EthDmConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.configs.grouping.EthDmConfigBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.configs.grouping.EthDmConfigKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.grouping.EthDm;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.grouping.EthDmBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lb.grouping.EthLbBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lb.grouping.EthLbKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lm.configs.grouping.EthLmConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lm.configs.grouping.EthLmConfigBuilder;
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
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.GetEthDmInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.GetEthLbInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.GetEthLmInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StartEthDmInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StartEthLbInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StartEthLmInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StartEthLtInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StopEthDmInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StopEthLbInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StopEthLmInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.lb.grouping.EthLb;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.oam.mip.configs.grouping.EthOamMipConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.oam.mip.configs.grouping.EthOamMipConfigBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ctps.ctp.eth.ctp.pac.OamConfigBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ctps.ctp.eth.ctp.pac.OamStatePacBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * oam 转换
 *
 * @author Quan Jingyuan
 * @since 2021/10/9
 **/
public class OamTransformImpl extends AbstractCmccTransformer implements CommonTransform, EnumTransform, ServiceTransform {

    public EthLms devEthLmsToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.get.eth.lm.output.EthLms ethLm) {
        EthLmsBuilder ethLmsBuilder = new EthLmsBuilder();
        ethLmsBuilder.setEthLm(devEthLmToApiList(ethLm.getEthLm()));
        return ethLmsBuilder.build();
    }

    public EthDms devEthDmsToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.get.eth.dm.output.EthDms ethDm) {
        EthDmsBuilder ethDmsBuilder = new EthDmsBuilder();
        ethDmsBuilder.setEthDm(devEthDmToApiList(ethDm.getEthDm()));
        return ethDmsBuilder.build();
    }

    private EthLm devEthLmToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.lm.grouping.EthLm ethLm) {
        if (ethLm == null) {
            return null;
        }
        EthLmBuilder ethLmBuilder = new EthLmBuilder();
        if (ethLm.getIndex() != null) {
            ethLmBuilder.setIndex(ethLm.getIndex());
        }
        if (ethLm.getLocalLostPackRatio() != null) {
            ethLmBuilder.setLocalLostPackRatio(ethLm.getLocalLostPackRatio());
        }
        if (ethLm.getLocalReceiveCount() != null) {
            ethLmBuilder.setLocalReceiveCount(ethLm.getLocalReceiveCount());
        }
        if (ethLm.getLocalSendCount() != null) {
            ethLmBuilder.setLocalSendCount(ethLm.getLocalSendCount());
        }
        if (ethLm.getRemoteLostPackRatio() != null) {
            ethLmBuilder.setRemoteLostPackRatio(ethLm.getRemoteLostPackRatio());
        }
        if (ethLm.getRemoteReceiveCount() != null) {
            ethLmBuilder.setRemoteReceiveCount(ethLm.getRemoteReceiveCount());
        }
        if (ethLm.getRemoteSendCount() != null) {
            ethLmBuilder.setRemoteSendCount(ethLm.getRemoteSendCount());
        }
        return ethLmBuilder.build();
    }

    private EthDm devEthDmToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.dm.grouping.EthDm ethDm) {
        if (ethDm == null) {
            return null;
        }
        EthDmBuilder ethDmBuilder = new EthDmBuilder();
        if (ethDm.getIndex() != null) {
            ethDmBuilder.setIndex(ethDm.getIndex());
        }
        if (ethDm.getDelay() != null) {
            ethDmBuilder.setDelay(ethDm.getDelay());
        }
        if (ethDm.getDelayVariation() != null) {
            ethDmBuilder.setDelayVariation(ethDm.getDelayVariation());
        }
        return ethDmBuilder.build();
    }

    private List<EthLm> devEthLmToApiList(List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.lm.grouping.EthLm> ethLm) {
        if (ethLm == null) {
            return CollUtil.newArrayList();
        }
        List<EthLm> ethLms = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.lm.grouping.EthLm item : ethLm) {
            ethLms.add(devEthLmToApi(item));
        }
        return ethLms;
    }

    private List<EthDm> devEthDmToApiList(List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.dm.grouping.EthDm> ethDm) {
        if (ethDm == null) {
            return CollUtil.newArrayList();
        }
        List<EthDm> ethDms = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.dm.grouping.EthDm item : ethDm) {
            ethDms.add(devEthDmToApi(item));
        }
        return ethDms;
    }

    public GetEthLmInputBuilder apiGetEthLmInputToDev(GetEthLmInput input) {
        GetEthLmInputBuilder builder = new GetEthLmInputBuilder();
        builder.setCtpName(input.getCtpName());
        builder.setName(input.getName());
        return builder;
    }

    public GetEthDmInputBuilder apiGetEthDmInputToDev(GetEthDmInput input) {
        GetEthDmInputBuilder builder = new GetEthDmInputBuilder();
        builder.setCtpName(input.getCtpName());
        builder.setName(input.getName());
        return builder;
    }

    public StopEthLmInputBuilder apiStopEthLmToDev(StopEthLmInput input) {
        if (input == null) {
            return null;
        }
        StopEthLmInputBuilder stopEthLmInputBuilder = new StopEthLmInputBuilder();
        stopEthLmInputBuilder.setCtpName(input.getCtpName());
        stopEthLmInputBuilder.setName(input.getName());
        return stopEthLmInputBuilder;
    }

    public StopEthDmInputBuilder apiStopEthDmToDev(StopEthDmInput input) {
        if (input == null) {
            return null;
        }
        StopEthDmInputBuilder stopEthDmInputBuilder = new StopEthDmInputBuilder();
        stopEthDmInputBuilder.setCtpName(input.getCtpName());
        stopEthDmInputBuilder.setName(input.getName());
        return stopEthDmInputBuilder;
    }

    public StartEthLmInputBuilder apiStartEthLmToDev(StartEthLmInput input) {
        if (input == null) {
            return null;
        }
        StartEthLmInputBuilder startEthLmInputBuilder = new StartEthLmInputBuilder();
        startEthLmInputBuilder.setCtpName(input.getCtpName());
        startEthLmInputBuilder.setName(input.getName());
        return startEthLmInputBuilder;
    }

    public StartEthDmInputBuilder apiStartEthDmToDev(StartEthDmInput input) {
        if (input == null) {
            return null;
        }
        StartEthDmInputBuilder startEthDmInputBuilder = new StartEthDmInputBuilder();
        startEthDmInputBuilder.setCtpName(input.getCtpName());
        startEthDmInputBuilder.setName(input.getName());
        return startEthDmInputBuilder;
    }

    public GetEthLmConfigOutputBuilder devLmConfigToApi(OamEthConfig oamEthConfig) {
        GetEthLmConfigOutputBuilder configOutputBuilder = new GetEthLmConfigOutputBuilder();
        configOutputBuilder.setEthLmConfig(devLmConfigToApiList(oamEthConfig.getEthLmConfigs()));
        return configOutputBuilder;
    }

    public GetEthDmConfigOutputBuilder devDmConfigToApi(OamEthConfig oamEthConfig) {
        GetEthDmConfigOutputBuilder configOutputBuilder = new GetEthDmConfigOutputBuilder();
        configOutputBuilder.setEthDmConfig(devDmConfigToApiList(oamEthConfig.getEthDmConfigs()));
        return configOutputBuilder;
    }

    private org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.configs.grouping.EthDmConfigBuilder devDmConfigToApi(EthDmConfigGrouping ethDmConfig) {
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.configs.grouping.EthDmConfigBuilder ethDmConfigBuilder = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.dm.configs.grouping.EthDmConfigBuilder();
        if (ethDmConfig == null) {
            return ethDmConfigBuilder;
        }
        if (ethDmConfig.getDuration() != null) {
            ethDmConfigBuilder.setDuration(ethDmConfig.getDuration());
        }
        if (ethDmConfig.getCtpName() != null) {
            ethDmConfigBuilder.setCtpName(ethDmConfig.getCtpName());
        }
        if (ethDmConfig.getName() != null) {
            ethDmConfigBuilder.withKey(new EthDmConfigKey(ethDmConfig.getName()));
            ethDmConfigBuilder.setName(ethDmConfig.getName());
        }
        if (ethDmConfig.getPriority() != null) {
            ethDmConfigBuilder.setPriority(ethDmConfig.getPriority());
        }
        if (ethDmConfig.getTimes() != null) {
            ethDmConfigBuilder.setTimes(ethDmConfig.getTimes());
        }

        return ethDmConfigBuilder;

    }

    private org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lm.configs.grouping.EthLmConfigBuilder devLmConfigToApi(EthLmConfigGrouping ethLmConfig) {
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lm.configs.grouping.EthLmConfigBuilder ethLmConfigBuilder = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lm.configs.grouping.EthLmConfigBuilder();
        if (ethLmConfig == null) {
            return ethLmConfigBuilder;
        }
        if (ethLmConfig.getDuration() != null) {
            ethLmConfigBuilder.setDuration(ethLmConfig.getDuration());
        }
        if (ethLmConfig.getCtpName() != null) {
            ethLmConfigBuilder.setCtpName(ethLmConfig.getCtpName());
        }
        if (ethLmConfig.getName() != null) {
            ethLmConfigBuilder.withKey(new EthLmConfigKey(ethLmConfig.getName()));
            ethLmConfigBuilder.setName(ethLmConfig.getName());
        }
        if (ethLmConfig.getPriority() != null) {
            ethLmConfigBuilder.setPriority(ethLmConfig.getPriority());
        }
        if (ethLmConfig.getTimes() != null) {
            ethLmConfigBuilder.setTimes(ethLmConfig.getTimes());
        }
        if (ethLmConfig.getWorkingMode() != null) {
            ethLmConfigBuilder.setWorkingMode(devEthLmWorkingModeToApi(ethLmConfig.getWorkingMode()));
        }
        return ethLmConfigBuilder;

    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.dm.configs.grouping.EthDmConfig apiDmConfigToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.EthDmConfigGrouping ethDmConfig) {
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.dm.configs.grouping.EthDmConfigBuilder ethDmConfigBuilder = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.dm.configs.grouping.EthDmConfigBuilder();
        if (ethDmConfig == null) {
            return ethDmConfigBuilder.build();
        }
        if (ethDmConfig.getDuration() != null) {
            ethDmConfigBuilder.setDuration(ethDmConfig.getDuration());
        }
        if (ethDmConfig.getCtpName() != null) {
            ethDmConfigBuilder.setCtpName(ethDmConfig.getCtpName());
        }
        if (ethDmConfig.getName() != null) {
            ethDmConfigBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.dm.configs.grouping.EthDmConfigKey(ethDmConfig.getName()));
            ethDmConfigBuilder.setName(ethDmConfig.getName());
        }
        if (ethDmConfig.getPriority() != null) {
            ethDmConfigBuilder.setPriority(ethDmConfig.getPriority());
        }
        if (ethDmConfig.getTimes() != null) {
            ethDmConfigBuilder.setTimes(ethDmConfig.getTimes());
        }
        return ethDmConfigBuilder.build();

    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.lm.configs.grouping.EthLmConfig apiLmConfigToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.EthLmConfigGrouping ethLmConfig) {
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.lm.configs.grouping.EthLmConfigBuilder ethLmConfigBuilder = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.lm.configs.grouping.EthLmConfigBuilder();
        if (ethLmConfig == null) {
            return ethLmConfigBuilder.build();
        }
        if (ethLmConfig.getDuration() != null) {
            ethLmConfigBuilder.setDuration(ethLmConfig.getDuration());
        }
        if (ethLmConfig.getCtpName() != null) {
            ethLmConfigBuilder.setCtpName(ethLmConfig.getCtpName());
        }
        if (ethLmConfig.getName() != null) {
            ethLmConfigBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.lm.configs.grouping.EthLmConfigKey(ethLmConfig.getName()));
            ethLmConfigBuilder.setName(ethLmConfig.getName());
        }
        if (ethLmConfig.getPriority() != null) {
            ethLmConfigBuilder.setPriority(ethLmConfig.getPriority());
        }
        if (ethLmConfig.getTimes() != null) {
            ethLmConfigBuilder.setTimes(ethLmConfig.getTimes());
        }
        if (ethLmConfig.getWorkingMode() != null) {
            ethLmConfigBuilder.setWorkingMode(apiEthLmWorkingModeToDev(ethLmConfig.getWorkingMode()));
        }
        return ethLmConfigBuilder.build();

    }

    private Map<EthDmConfigKey, EthDmConfig> devDmConfigToApiList(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.oam.eth.config.EthDmConfigs ethDmConfigs) {
        Map<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.dm.configs.grouping.EthDmConfigKey, org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.dm.configs.grouping.EthDmConfig> ethDmConfig = ethDmConfigs.getEthDmConfig();
        if (MapUtil.isEmpty(ethDmConfig)) {
            return MapUtil.empty();
        }
        return ctm(ethDmConfig.values(), this::devDmConfigToApi, EthDmConfigBuilder::key, EthDmConfigBuilder::build);
    }

    private Map<EthLmConfigKey, EthLmConfig> devLmConfigToApiList(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.oam.eth.config.EthLmConfigs ethLmConfigs) {
        Map<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.lm.configs.grouping.EthLmConfigKey, org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.lm.configs.grouping.EthLmConfig> ethLmConfig = ethLmConfigs.getEthLmConfig();
        if (MapUtil.isEmpty(ethLmConfig)) {
            return MapUtil.empty();
        }
        return ctm(ethLmConfig.values(), this::devLmConfigToApi, EthLmConfigBuilder::key, EthLmConfigBuilder::build);
    }

    public StopEthLbInputBuilder apiStopEtrLbToDev(StopEthLbInput input) {
        if (input == null) {
            return new StopEthLbInputBuilder();
        }
        StopEthLbInputBuilder stopEthLbInputBuilder = new StopEthLbInputBuilder();
        stopEthLbInputBuilder.setCtpName(input.getCtpName());
        return stopEthLbInputBuilder;
    }

    public StartEthLbInputBuilder apiStartEtrLbToDev(StartEthLbInput input) {
        if (input == null) {
            return new StartEthLbInputBuilder();
        }
        StartEthLbInputBuilder startEthLbInputBuilder = new StartEthLbInputBuilder();
        if (input.getCtpName() != null) {
            startEthLbInputBuilder.setCtpName(input.getCtpName());
        }
        if (input.getDuration() != null) {
            startEthLbInputBuilder.setDuration(input.getDuration());
        }
        if (input.getLength() != null) {
            startEthLbInputBuilder.setLength(input.getLength());
        }
        if (input.getMac() != null) {
            startEthLbInputBuilder.setMac(input.getMac());
        }
        if (input.getMpId() != null) {
            startEthLbInputBuilder.setMpId(input.getMpId());
        }
        if (input.getPeriod() != null) {
            startEthLbInputBuilder.setPeriod(input.getPeriod());
        }
        if (input.getPriority() != null) {
            startEthLbInputBuilder.setPriority(input.getPriority());
        }
        if (input.getTimes() != null) {
            startEthLbInputBuilder.setTimes(input.getTimes());
        }
        return startEthLbInputBuilder;
    }

    public StartEthLtInputBuilder apiStartEthLtToDev(StartEthLtInput input) {
        if (input == null) {
            return new StartEthLtInputBuilder();
        }

        StartEthLtInputBuilder startEthLtInputBuilder = new StartEthLtInputBuilder();
        if (input.getCtpName() != null) {
            startEthLtInputBuilder.setCtpName(input.getCtpName());
        }

        if (input.getTtl() != null) {
            startEthLtInputBuilder.setTtl(input.getTtl());
        }
        if (input.getMac() != null) {
            startEthLtInputBuilder.setMac(input.getMac());
        }
        if (input.getMpId() != null) {
            startEthLtInputBuilder.setMpId(input.getMpId());
        }
        if (input.getPriority() != null) {
            startEthLtInputBuilder.setPriority(input.getPriority());
        }
        return startEthLtInputBuilder;

    }

    public OamConfigBuilder apiEthOamConfigToDev(EthOamConfigGrouping config) {
        if (config == null) {
            return new OamConfigBuilder();
        }
        OamConfigBuilder builder = new OamConfigBuilder();
        if (config.getMdl() != null) {
            builder.setMdl(config.getMdl());
        }
        if (config.getCcInterval() != null) {
            builder.setCcInterval(apiOamTransmitIntervalToDev(config.getCcInterval()));
        }
        if (config.getDmInterval() != null) {
            builder.setDmInterval(apiOamTransmitIntervalToDev(config.getDmInterval()));
        }
        if (config.getLmInterval() != null) {
            builder.setLmInterval(apiOamTransmitIntervalToDev(config.getLmInterval()));
        }
        if (config.getMdName() != null) {
            builder.setMdName(config.getMdName());
        }
        if (config.getMegId() != null) {
            builder.setMegId(config.getMegId());
        }
        if (config.getMel() != null) {
            builder.setMel(config.getMel());
        }
        if (config.getMepId() != null) {
            builder.setMepId(config.getMepId());
        }
        if (config.getRemoteMepId() != null) {
            builder.setRemoteMepId(config.getRemoteMepId());
        }
        return builder;
    }

    public OamStatePacBuilder apiOamStatePacToDev(EthOamStatePacGrouping config) {
        if (config == null) {
            return new OamStatePacBuilder();
        }
        OamStatePacBuilder statePacBuilder = new OamStatePacBuilder();
        statePacBuilder.setCcState(config.getCcState());
        statePacBuilder.setDmState(config.getDmState());
        statePacBuilder.setLmState(config.getLmState());
        statePacBuilder.setTmState(config.getTmState());
        return statePacBuilder;
    }

    public GetEthOamMipConfigOutputBuilder devGetEthOamMipConfigOutputToApi(Collection<EthOamMipConfig> ethOamMipConfigs) {
        if (ethOamMipConfigs == null) {
            return new GetEthOamMipConfigOutputBuilder();
        }
        GetEthOamMipConfigOutputBuilder builder = new GetEthOamMipConfigOutputBuilder();
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.oam.mip.configs.grouping.EthOamMipConfigBuilder> list = new ArrayList<>();
        for (EthOamMipConfig item : ethOamMipConfigs) {
            org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.oam.mip.configs.grouping.EthOamMipConfigBuilder ethOamMipConfig = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.oam.mip.configs.grouping.EthOamMipConfigBuilder();
            ethOamMipConfig.setCtpName(item.getCtpName());
            ethOamMipConfig.withKey(new EthOamMipConfigKey(item.key().getCtpName()));
            ethOamMipConfig.setMdName(item.getMdName());
            ethOamMipConfig.setMegId(item.getMegId());
            ethOamMipConfig.setMel(item.getMel());
            ethOamMipConfig.setMipId(item.getMipId());
            list.add(ethOamMipConfig);
        }
        if (!list.isEmpty()) {
            builder.setEthOamMipConfig(ctm(list,
                    org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.oam.mip.configs.grouping.EthOamMipConfigBuilder::key,
                    org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.oam.mip.configs.grouping.EthOamMipConfigBuilder::build));
        }
        return builder;
    }

    public EthOamMipConfigBuilder apiEthOamMipConfigToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.EthOamMipConfigGrouping configGrouping) {
        if (configGrouping == null) {
            return new EthOamMipConfigBuilder();
        }
        EthOamMipConfigBuilder builder = new EthOamMipConfigBuilder();
        builder.setCtpName(configGrouping.getCtpName());
        builder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.oam.mip.configs.grouping.EthOamMipConfigKey(configGrouping.getCtpName()));
        builder.setMdName(configGrouping.getMdName());
        builder.setMegId(configGrouping.getMegId());
        builder.setMipId(configGrouping.getMipId());
        builder.setMel(configGrouping.getMel());
        return builder;
    }

    public GetEthLbInputBuilder apiGetEthLbInputToDev(GetEthLbInput ethLbInput) {
        if (ethLbInput == null) {
            return new GetEthLbInputBuilder();
        }
        GetEthLbInputBuilder builder = new GetEthLbInputBuilder();
        builder.setCtpName(ethLbInput.getCtpName());

        return builder;
    }

    public EthLbs devEthLbsToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.get.eth.lb.output.EthLbs ethLbs) {
        if (ethLbs == null || ethLbs.getEthLb() == null) {
            return null;
        }
        EthLbsBuilder ethLbsBuilder = new EthLbsBuilder();
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lb.grouping.EthLbBuilder> ethLbList = new ArrayList<>();
        for (EthLb ethLb : ethLbs.getEthLb().values()) {
            EthLbBuilder ethLbBuilder = new EthLbBuilder();
            ethLbBuilder.setBytes(ethLb.getBytes());
            ethLbBuilder.withKey(new EthLbKey(ethLb.key().getSequence()));
            ethLbBuilder.setSequence(ethLb.getSequence());
            ethLbBuilder.setTime(ethLb.getTime());
            ethLbList.add(ethLbBuilder);
        }
        ethLbsBuilder.setEthLb(ctm(ethLbList,
                org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lb.grouping.EthLbBuilder::key,
                org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.lb.grouping.EthLbBuilder::build));
        return ethLbsBuilder.build();
    }
}
