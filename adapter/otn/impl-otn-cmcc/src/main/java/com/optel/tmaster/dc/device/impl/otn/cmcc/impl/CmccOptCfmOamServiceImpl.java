/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptCfmOamServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.OamTransformImpl;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.cfm.oam.rev200814.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.OamEthConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.OptOtnExtensionService;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.dm.configs.grouping.EthDmConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.dm.configs.grouping.EthDmConfigBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.dm.configs.grouping.EthDmConfigKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.lm.configs.grouping.EthLmConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.lm.configs.grouping.EthLmConfigKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.oam.mip.configs.grouping.EthOamMipConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.oam.mip.configs.grouping.EthOamMipConfigBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.oam.mip.configs.grouping.EthOamMipConfigKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.oam.eth.config.EthDmConfigs;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.oam.eth.config.EthLmConfigs;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.oam.eth.config.EthMipConfigs;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.Ctps;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ctps.Ctp;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ctps.CtpKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ctp1;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ctps.ctp.EthCtpPac;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ctps.ctp.eth.ctp.pac.OamConfig;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ctps.ctp.eth.ctp.pac.OamConfigBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ctps.ctp.eth.ctp.pac.OamStatePac;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ctps.ctp.eth.ctp.pac.OamStatePacBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.Map;
import java.util.Objects;

/**
 * dc-aggregator - CmccOptCfmOamServiceImpl
 * OAM 配置
 * date       time        author
 * ─────────────────────────────
 * 2021/10/7   9:58      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class CmccOptCfmOamServiceImpl extends BaseOptCfmOamServiceImpl implements IDeviceServiceOtnCmcc {
    @Override
    public ListenableFuture<RpcResult<GetEthOamMipConfigOutput>> getEthOamMipConfig(GetEthOamMipConfigInput input) {
        InstanceIdentifier<OamEthConfig> iid = create(OamEthConfig.class);
        OamEthConfig oamEthConfig = MountTools.queryFromOperational(input.getNeId(), iid);
        if (Objects.isNull(oamEthConfig)) {
            return RpcResultUtil.success();
        }
        if (Objects.isNull(oamEthConfig.getEthMipConfigs())) {
            return RpcResultUtil.success();
        }
        final @Nullable Map<EthOamMipConfigKey, EthOamMipConfig> ethOamMipConfig = oamEthConfig.getEthMipConfigs().getEthOamMipConfig();
        if (Objects.isNull(ethOamMipConfig)) {
            return RpcResultUtil.success();
        }
        OamTransformImpl oamTransform = new OamTransformImpl();
        GetEthOamMipConfigOutputBuilder configOutputBuilder = oamTransform.devGetEthOamMipConfigOutputToApi(ethOamMipConfig.values());
        return RpcResultUtil.success(configOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<GetEthDmConfigOutput>> getEthDmConfig(GetEthDmConfigInput input) {
        InstanceIdentifier<OamEthConfig> iid = create(OamEthConfig.class);
        OamEthConfig oamEthConfig = MountTools.queryFromOperational(input.getNeId(), iid);
        if (Objects.isNull(oamEthConfig)) {
            return RpcResultUtil.success();
        }
        if (Objects.isNull(oamEthConfig.getEthDmConfigs())) {
            return RpcResultUtil.success();
        }
        if (Objects.isNull(oamEthConfig.getEthDmConfigs().getEthDmConfig())) {
            return RpcResultUtil.success();
        }
        OamTransformImpl oamTransform = new OamTransformImpl();
        return RpcResultUtil.success(oamTransform.devDmConfigToApi(oamEthConfig).build());
    }

    @Override
    public ListenableFuture<RpcResult<AddEthOamConfigOutput>> addEthOamConfig(AddEthOamConfigInput input) {
        InstanceIdentifier<OamConfig> iid = create(Ctps.class)
                .child(Ctp.class, new CtpKey(input.getCtpName()))
                .augmentation(Ctp1.class)
                .child(EthCtpPac.class)
                .child(OamConfig.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        OamConfigBuilder oamConfigBuilder = oamTransform.apiEthOamConfigToDev(input);
        MountTools.putConfig(input.getNeId(), iid, oamConfigBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<DeleteEthOamConfigOutput>> deleteEthOamConfig(DeleteEthOamConfigInput input) {
        InstanceIdentifier<OamConfig> iid = create(Ctps.class)
                .child(Ctp.class, new CtpKey(input.getCtpName()))
                .augmentation(Ctp1.class)
                .child(EthCtpPac.class)
                .child(OamConfig.class);
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<DeleteEthLmConfigOutput>> deleteEthLmConfig(DeleteEthLmConfigInput input) {
        InstanceIdentifier<EthLmConfig> iid = create(OamEthConfig.class)
                .child(EthLmConfigs.class).child(EthLmConfig.class, new EthLmConfigKey(input.getName()));
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<AddEthOamMipConfigOutput>> addEthOamMipConfig(AddEthOamMipConfigInput input) {
        final InstanceIdentifier<EthOamMipConfig> iid = create(OamEthConfig.class)
                .child(EthMipConfigs.class).child(EthOamMipConfig.class, new EthOamMipConfigKey(input.getCtpName()));
        OamTransformImpl oamTransform = new OamTransformImpl();
        final EthOamMipConfigBuilder builder = oamTransform.apiEthOamMipConfigToDev(input);
        MountTools.putConfig(input.getNeId(), iid, builder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<AddEthDmConfigOutput>> addEthDmConfig(AddEthDmConfigInput input) {
        InstanceIdentifier<EthDmConfig> iid = create(OamEthConfig.class)
                .child(EthDmConfigs.class).child(EthDmConfig.class, new EthDmConfigKey(input.getName()));
        OamTransformImpl oamTransform = new OamTransformImpl();
        EthDmConfigBuilder oamConfigBuilder = new EthDmConfigBuilder(oamTransform.apiDmConfigToDev(input));
        MountTools.putConfig(input.getNeId(), iid, oamConfigBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetEthLbOutput>> getEthLb(GetEthLbInput input) {
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.GetEthLbInputBuilder lb = oamTransform.apiGetEthLbInputToDev(input);

        return RpcResultUtil.buildFutureResult(service.getEthLb(lb.build()), (c) -> new GetEthLbOutputBuilder(oamTransform.devEthLbsToApi(c.getEthLbs())).build());
    }

    @Override
    public ListenableFuture<RpcResult<GetEthLmOutput>> getEthLm(GetEthLmInput input) {
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.GetEthLmInputBuilder lm = oamTransform.apiGetEthLmInputToDev(input);
        return RpcResultUtil.buildFutureResult(service.getEthLm(lm.build()), (c) -> new GetEthLmOutputBuilder(oamTransform.devEthLmsToApi(c.getEthLms())).build());
    }

    @Override
    public ListenableFuture<RpcResult<GetEthDmOutput>> getEthDm(GetEthDmInput input) {
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.GetEthDmInputBuilder dm = oamTransform.apiGetEthDmInputToDev(input);
        return RpcResultUtil.buildFutureResult(service.getEthDm(dm.build()), (c) -> new GetEthDmOutputBuilder(oamTransform.devEthDmsToApi(c.getEthDms())).build());
    }

    @Override
    public ListenableFuture<RpcResult<DeleteEthOamMipConfigOutput>> deleteEthOamMipConfig(DeleteEthOamMipConfigInput input) {
        MountTools.deleteFromConfig(input.getNeId(), create(OamEthConfig.class)
                .child(EthMipConfigs.class).child(EthOamMipConfig.class, new EthOamMipConfigKey(input.getCtpName())));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetEthLmConfigOutput>> getEthLmConfig(GetEthLmConfigInput input) {
        InstanceIdentifier<OamEthConfig> iid = create(OamEthConfig.class);
        OamEthConfig oamEthConfig = MountTools.queryFromOperational(input.getNeId(), iid);
        if (Objects.isNull(oamEthConfig)) {
            return RpcResultUtil.success();
        }
        if (Objects.isNull(oamEthConfig.getEthLmConfigs())) {
            return RpcResultUtil.success();
        }
        if (Objects.isNull(oamEthConfig.getEthLmConfigs().getEthLmConfig())) {
            return RpcResultUtil.success();
        }
        OamTransformImpl oamTransform = new OamTransformImpl();

        return RpcResultUtil.success(oamTransform.devLmConfigToApi(oamEthConfig).build());
    }

    @Override
    public ListenableFuture<RpcResult<StartEthLbOutput>> startEthLb(StartEthLbInput input) {
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StartEthLbInputBuilder lb = oamTransform.apiStartEtrLbToDev(input);
        return RpcResultUtil.buildFutureResult(service.startEthLb(lb.build()));
    }

    @Override
    public ListenableFuture<RpcResult<StartEthLtOutput>> startEthLt(StartEthLtInput input) {
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StartEthLtInputBuilder lt = oamTransform.apiStartEthLtToDev(input);
        return RpcResultUtil.buildFutureResult(service.startEthLt(lt.build()));
    }

    @Override
    public ListenableFuture<RpcResult<StartEthLmOutput>> startEthLm(StartEthLmInput input) {
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StartEthLmInputBuilder lm = oamTransform.apiStartEthLmToDev(input);

        return RpcResultUtil.buildFutureResult(service.startEthLm(lm.build()));
    }

    @Override
    public ListenableFuture<RpcResult<StartEthDmOutput>> startEthDm(StartEthDmInput input) {
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StartEthDmInputBuilder dm =
                new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StartEthDmInputBuilder(oamTransform.apiStartEthDmToDev(input).build());
        return RpcResultUtil.buildFutureResult(service.startEthDm(dm.build()));
    }

    @Override
    public ListenableFuture<RpcResult<DeleteEthDmConfigOutput>> deleteEthDmConfig(DeleteEthDmConfigInput input) {
        InstanceIdentifier<EthDmConfig> iid = create(OamEthConfig.class)
                .child(EthDmConfigs.class).child(EthDmConfig.class, new EthDmConfigKey(input.getName()));
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<AddEthLmConfigOutput>> addEthLmConfig(AddEthLmConfigInput input) {
        InstanceIdentifier<EthLmConfig> iid =
                create(OamEthConfig.class).
                child(EthLmConfigs.class).
                child(EthLmConfig.class, new EthLmConfigKey(input.getName()));
        OamTransformImpl oamTransform = new OamTransformImpl();
        MountTools.putConfig(input.getNeId(), iid, oamTransform.apiLmConfigToDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyEthOamStatePacOutput>> modifyEthOamStatePac(ModifyEthOamStatePacInput input) {
        InstanceIdentifier<OamStatePac> iid = create(Ctps.class)
                .child(Ctp.class, new CtpKey(input.getCtpName()))
                .augmentation(Ctp1.class)
                .child(EthCtpPac.class)
                .child(OamStatePac.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        OamStatePacBuilder builder = oamTransform.apiOamStatePacToDev(input);
        MountTools.doMergeToConfig(input.getNeId(), iid, builder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyEthOamConfigOutput>> modifyEthOamConfig(ModifyEthOamConfigInput input) {
        InstanceIdentifier<OamConfig> iid = create(Ctps.class)
                .child(Ctp.class, new CtpKey(input.getCtpName()))
                .augmentation(Ctp1.class)
                .child(EthCtpPac.class)
                .child(OamConfig.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        OamConfigBuilder oamConfigBuilder = oamTransform.apiEthOamConfigToDev(input);
        MountTools.doMergeToConfig(input.getNeId(), iid, oamConfigBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<StopEthLbOutput>> stopEthLb(StopEthLbInput input) {
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StopEthLbInputBuilder lb = oamTransform.apiStopEtrLbToDev(input);
        return RpcResultUtil.buildFutureResult(service.stopEthLb(lb.build()));
    }

    @Override
    public ListenableFuture<RpcResult<StopEthLmOutput>> stopEthLm(StopEthLmInput input) {
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StopEthLmInputBuilder lm = oamTransform.apiStopEthLmToDev(input);
        return RpcResultUtil.buildFutureResult(service.stopEthLm(lm.build()));
    }

    @Override
    public ListenableFuture<RpcResult<StopEthDmOutput>> stopEthDm(StopEthDmInput input) {
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StopEthDmInputBuilder dm =
                new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.StopEthDmInputBuilder(oamTransform.apiStopEthDmToDev(input).build());
        return RpcResultUtil.buildFutureResult(service.stopEthDm(dm.build()));
    }
}
