/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptCfmOamServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.OamTransformImpl;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import com.optel.tmaster.dc.general.nc.nccore.NcTools;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.cfm.oam.rev200814.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.OamEthConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.OptOtnExtensionService;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.dm.configs.grouping.EthDmConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.dm.configs.grouping.EthDmConfigKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.lm.configs.grouping.EthLmConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.lm.configs.grouping.EthLmConfigKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.oam.mip.configs.grouping.EthOamMipConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.oam.mip.configs.grouping.EthOamMipConfigBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.oam.mip.configs.grouping.EthOamMipConfigKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.oam.eth.config.EthDmConfigs;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.oam.eth.config.EthLmConfigs;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.oam.eth.config.EthMipConfigs;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.Ctps;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ctps.Ctp;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ctps.CtpKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.Ctp1;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.EthCtpPac;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.OamConfig;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.OamConfigBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.OamStatePac;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.OamStatePacBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.Map;
import java.util.Objects;

/**
 * dc-aggregator - CtcOptCfmOamServiceImpl
 * OAM 配置
 * date       time        author
 * ─────────────────────────────
 * 2021/10/7   9:58      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class CtcOptCfmOamServiceImpl extends BaseOptCfmOamServiceImpl implements IDeviceServiceOtnCtc {
    @Override
    public ListenableFuture<RpcResult<StartEthLbOutput>> startEthLb(StartEthLbInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthLbInputBuilder lb = new OamTransformImpl().apiStartEtrLbToDev(input);
        return RpcResultUtil.buildFutureResult(service.startEthLb(lb.build()));
    }

    @Override
    public ListenableFuture<RpcResult<GetEthLbOutput>> getEthLb(GetEthLbInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetEthLbInputBuilder lb = oamTransform.apiGetEthLbInputToDev(input);

        return RpcResultUtil.buildFutureResult(service.getEthLb(lb.build()), c -> new GetEthLbOutputBuilder(oamTransform.devEthLbsToApi(c.getEthLbs())).build());
    }


    @Override
    public ListenableFuture<RpcResult<StopEthLbOutput>> stopEthLb(StopEthLbInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StopEthLbInputBuilder lb = new OamTransformImpl().apiStopEtrLbToDev(input);

        return RpcResultUtil.buildFutureResult(service.stopEthLb(lb.build()));
    }

    /**
     * LT
     *
     * @return eth
     */
    @Override
    public ListenableFuture<RpcResult<StartEthLtOutput>> startEthLt(StartEthLtInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthLtInputBuilder lt = new OamTransformImpl().apiStartEthLtToDev(input);

        return RpcResultUtil.buildFutureResult(service.startEthLt(lt.build()));
    }

    /**
     * Dual Ended LM
     *
     * @return lm
     */
    @Override
    public ListenableFuture<RpcResult<GetEthLmOutput>> getEthLm(GetEthLmInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetEthLmInputBuilder lm = oamTransform.apiGetEthLmInputToDev(input);

        return RpcResultUtil.buildFutureResult(service.getEthLm(lm.build()), c -> new GetEthLmOutputBuilder(oamTransform.devEthLmsToApi(c.getEthLms())).build());
    }

    @Override
    public ListenableFuture<RpcResult<StartEthLmOutput>> startEthLm(StartEthLmInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthLmInputBuilder lm = new OamTransformImpl().apiStartEthLmToDev(input);

        return RpcResultUtil.buildFutureResult(service.startEthLm(lm.build()));
    }

    @Override
    public ListenableFuture<RpcResult<StopEthLmOutput>> stopEthLm(StopEthLmInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StopEthLmInputBuilder lm = new OamTransformImpl().apiStopEthLmToDev(input);

        return RpcResultUtil.buildFutureResult(service.stopEthLm(lm.build()));
    }

    /**
     * DM
     *
     * @return 结果
     */
    @Override
    public ListenableFuture<RpcResult<GetEthDmOutput>> getEthDm(GetEthDmInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        OamTransformImpl oamTransform = new OamTransformImpl();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetEthDmInputBuilder dm = oamTransform.apiGetEthDmInputToDev(input);
        return RpcResultUtil.buildFutureResult(service.getEthDm(dm.build()), c -> new GetEthDmOutputBuilder(oamTransform.devEthDmsToApi(c.getEthDms())).build());
    }

    @Override
    public ListenableFuture<RpcResult<StartEthDmOutput>> startEthDm(StartEthDmInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StartEthDmInputBuilder dm = new OamTransformImpl().apiStartEthDmToDev(input);

        return RpcResultUtil.buildFutureResult(service.startEthDm(dm.build()));
    }

    @Override
    public ListenableFuture<RpcResult<StopEthDmOutput>> stopEthDm(StopEthDmInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.StopEthDmInputBuilder dm = new OamTransformImpl().apiStopEthDmToDev(input);

        return RpcResultUtil.buildFutureResult(service.stopEthDm(dm.build()));
    }

    /**
     * 修改ETH OAM测量状态对象
     *
     * @param input 输入参数
     * @return 返回结果
     */
    @Override
    public ListenableFuture<RpcResult<ModifyEthOamStatePacOutput>> modifyEthOamStatePac(ModifyEthOamStatePacInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<OamStatePac> iid = getIidByOamStatePac(input.getCtpName());
        OamStatePacBuilder builder = new OamTransformImpl().apiOamStatePacToDev(input);
        MountTools.doMergeToConfig(input.getNeId(), iid, builder.build());
        return RpcResultUtil.success();
    }

    /**
     * 获取OAM 状态 路径
     *
     * @param ctpName ctp name
     * @return 路径
     */
    private InstanceIdentifier<OamStatePac> getIidByOamStatePac(String ctpName) {
        return create(Ctps.class)
                .child(Ctp.class, new CtpKey(ctpName))
                .augmentation(Ctp1.class)
                .child(EthCtpPac.class)
                .child(OamStatePac.class);
    }

    @Override
    public ListenableFuture<RpcResult<AddEthOamConfigOutput>> addEthOamConfig(AddEthOamConfigInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<OamConfig> iid = getIidByOamConfig(input.getCtpName());
        OamConfigBuilder oamConfigBuilder = new OamTransformImpl().apiEthOamConfigToDev(input);
        MountTools.putConfig(input.getNeId(), iid, oamConfigBuilder.build());
        return RpcResultUtil.success();
    }

    /**
     * 获取OAM 配置 路径
     *
     * @param ctpName ctp
     * @return 路径
     */
    private InstanceIdentifier<OamConfig> getIidByOamConfig(String ctpName) {
        return create(Ctps.class)
                .child(Ctp.class, new CtpKey(ctpName))
                .augmentation(Ctp1.class)
                .child(EthCtpPac.class)
                .child(OamConfig.class);
    }


    /**
     * 修改ETH OAM配置对象
     *
     * @param input 输入参数
     * @return 返回结果
     */
    @Override
    public ListenableFuture<RpcResult<ModifyEthOamConfigOutput>> modifyEthOamConfig(ModifyEthOamConfigInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<OamConfig> iid = getIidByOamConfig(input.getCtpName());
        OamConfigBuilder oamConfigBuilder = new OamTransformImpl().apiEthOamConfigToDev(input);
        MountTools.doMergeToConfig(input.getNeId(), iid, oamConfigBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<DeleteEthOamConfigOutput>> deleteEthOamConfig(DeleteEthOamConfigInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<OamConfig> iid = getIidByOamConfig(input.getCtpName());
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetEthDmConfigOutput>> getEthDmConfig(GetEthDmConfigInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
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
        GetEthDmConfigOutputBuilder configOutputBuilder = new OamTransformImpl().devDmConfigToApi(oamEthConfig);
        return RpcResultUtil.success(configOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<GetEthLmConfigOutput>> getEthLmConfig(GetEthLmConfigInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
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
        GetEthLmConfigOutputBuilder configOutputBuilder = new OamTransformImpl().devLmConfigToApi(oamEthConfig);
        return RpcResultUtil.success(configOutputBuilder.build());
    }

    /**
     * 获取OAM Lm配置 路径
     *
     * @param name 测量名称
     * @return 路径
     */
    private InstanceIdentifier<EthLmConfig> getIidByOamLmConfig(String name) {
        return create(OamEthConfig.class)
                .child(EthLmConfigs.class).child(EthLmConfig.class, new EthLmConfigKey(name));
    }

    /**
     * 获取OAM Lm配置 路径
     *
     * @param name 测量名称
     * @return 路径
     */
    private InstanceIdentifier<EthDmConfig> getIidByOamDmConfig(String name) {
        return create(OamEthConfig.class)
                .child(EthDmConfigs.class).child(EthDmConfig.class, new EthDmConfigKey(name));
    }

    @Override
    public ListenableFuture<RpcResult<DeleteEthLmConfigOutput>> deleteEthLmConfig(DeleteEthLmConfigInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<EthLmConfig> iid = getIidByOamLmConfig(input.getName());
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<AddEthDmConfigOutput>> addEthDmConfig(AddEthDmConfigInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<EthDmConfig> iid = getIidByOamDmConfig(input.getName());
        MountTools.putConfig(input.getNeId(), iid, new OamTransformImpl().apiDmConfigToDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<DeleteEthDmConfigOutput>> deleteEthDmConfig(DeleteEthDmConfigInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<EthDmConfig> iid = getIidByOamDmConfig(input.getName());
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<AddEthLmConfigOutput>> addEthLmConfig(AddEthLmConfigInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<EthLmConfig> iid = getIidByOamLmConfig(input.getName());
        MountTools.putConfig(input.getNeId(), iid, new OamTransformImpl().apiLmConfigToDev(input));
        return RpcResultUtil.success();
    }

    /**
     * 获取OAM Mip 路径
     *
     * @param name ctp name
     * @return 路径
     */
    private InstanceIdentifier<EthOamMipConfig> getIidByOamMipConfig(String name) {
        return create(OamEthConfig.class)
                .child(EthMipConfigs.class).child(EthOamMipConfig.class, new EthOamMipConfigKey(name));
    }

    @Override
    public ListenableFuture<RpcResult<AddEthOamMipConfigOutput>> addEthOamMipConfig(AddEthOamMipConfigInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        final InstanceIdentifier<EthOamMipConfig> iid = getIidByOamMipConfig(input.getCtpName());
        final EthOamMipConfigBuilder builder = new OamTransformImpl().apiEthOamMipConfigToDev(input);
        MountTools.putConfig(input.getNeId(), iid, builder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<DeleteEthOamMipConfigOutput>> deleteEthOamMipConfig(DeleteEthOamMipConfigInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        MountTools.deleteFromConfig(input.getNeId(), getIidByOamMipConfig(input.getCtpName()));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetEthOamMipConfigOutput>> getEthOamMipConfig(GetEthOamMipConfigInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
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
        GetEthOamMipConfigOutputBuilder configOutputBuilder = new OamTransformImpl().devGetEthOamMipConfigOutputToApi(ethOamMipConfig.values());
        return RpcResultUtil.success(configOutputBuilder.build());
    }
}
