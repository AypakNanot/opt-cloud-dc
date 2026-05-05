/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.impl;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.commons.collections.CollUtils;
import cn.hutool.core.map.MapUtil;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptOtnExtDevServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.*;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.service.NeServiceTransformImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.service.VsiTransformImpl;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import com.optel.tmaster.dc.general.nc.nccore.NcTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.DeleteRemoteFtrInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.DeleteRemoteFtrOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetLicenseInfoInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetLicenseInfoOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetPrbsParameterInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetPrbsParameterOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetPrbsResultInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetPrbsResultOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetUnknownTpnInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetUnknownTpnOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.PrbsBitErrorValueCleanInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.PrbsBitErrorValueCleanOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.SetPrbsParametersInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.SetPrbsParametersOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.DeleteRemoteFtrInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ctp.extension.grouping.CtpExtension;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ctp.extension.grouping.CtpExtensionKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.extension.CtpExtensions;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.extension.LagParameters;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.extension.PtpTypePacs;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ftr.extension.grouping.FtrExtension;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ftr.extension.grouping.FtrExtensionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ftr.extension.grouping.FtrExtensionKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.lag.parameters.grouping.LagParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.lag.parameters.grouping.LagParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.lag.parameters.grouping.LagParameterKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.remote.module.grouping.RemoteModule;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.remote.module.grouping.RemoteModuleBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.remote.module.grouping.RemoteModuleKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tpid.grouping.Tpid;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tpid.grouping.TpidBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tpid.grouping.TpidKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.AddServiceMemberOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.CreateServiceOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.DeleteServiceMemberOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.OptCreateServiceService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.Ftrs;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * 扩展配置
 * date       time        author
 * ─────────────────────────────
 * 2021/10/7   10:04      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
@SuppressWarnings("all")
public class CtcOptOtnExtDevServiceImpl extends BaseOptOtnExtDevServiceImpl implements IDeviceServiceOtnCtc {

    private final static Logger LOG = LoggerFactory.getLogger(CtcOptOtnExtDevServiceImpl.class);
    private final RemoteManagementVlanTransformerImpl remoteManagementVlanTransformer = new RemoteManagementVlanTransformerImpl();

    @Override
    public ListenableFuture<RpcResult<DeleteLagParameterOutput>> deleteLagParameter(DeleteLagParameterInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<LagParameter> iid = create(Extension.class).child(LagParameters.class)
                .child(LagParameter.class, new LagParameterKey(input.getLagId()));
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<AddOrUpdateLagParameterOutput>> addOrUpdateLagParameter(AddOrUpdateLagParameterInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<LagParameter> iid = create(Extension.class).child(LagParameters.class)
                .child(LagParameter.class, new LagParameterKey(input.getLagId()));
        LagParameterBuilder lagParameterBuilder = new LagTransformImpl().apiLagParameterToDev(input);
        lagParameterBuilder.withKey(new LagParameterKey(input.getLagId()));
        MountTools.doMergeToConfig(input.getNeId(), iid, lagParameterBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<SelectTpidsOutput>> selectTpids(SelectTpidsInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        SelectTpidsOutputBuilder outputBuilder = new SelectTpidsOutputBuilder();
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            TpidTransformImpl tpIdTransform = new TpidTransformImpl();
            if (input.getName() != null && input.getName().size() == 1) {
                InstanceIdentifier<Tpid> identifier = create(Tpids.class).child(Tpid.class, new TpidKey(input.getName().iterator().next()));
                Tpid tpid = MountTools.queryFromOperational(input.getNeId(), identifier);
                if (tpid == null) {
                    outputBuilder.setTpid(new HashMap<>());
                } else {
                    outputBuilder.setTpid(ltm(tpIdTransform.devSelectTpidsOutputToApi(Collections.singletonList(tpid))));
                }
            } else {
                InstanceIdentifier<Tpids> identifier = create(Tpids.class);
                Tpids tpids = MountTools.queryFromOperational(input.getNeId(), identifier);
                if (tpids == null || MapUtil.isEmpty(tpids.getTpid())) {
                    outputBuilder.setTpid(new HashMap<>());
                } else {
                    if (CollUtil.isEmpty(input.getName())) {
                        outputBuilder.setTpid(ltm(tpIdTransform.devSelectTpidsOutputToApi(tpids.getTpid().values())));
                    } else {
                        List<Tpid> tpidList = tpids.getTpid().values().stream().filter(e -> input.getName().contains(e.getName())).collect(Collectors.toList());
                        outputBuilder.setTpid(ltm(tpIdTransform.devSelectTpidsOutputToApi(tpidList)));
                    }
                }
            }
        }
        return RpcResultUtil.success(outputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<ModifyTpidsOutput>> modifyTpids(ModifyTpidsInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        TpidKey tpidKey = new TpidKey(input.getName());
        InstanceIdentifier<Tpid> identifiable = create(Tpids.class).child(Tpid.class, tpidKey);
        TpidBuilder tpidBuilder = new TpidTransformImpl().apiTpidToDev(input);
        MountTools.putConfig(input.getNeId(), identifiable, tpidBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryRemoteModuleOutput>> queryRemoteModule(QueryRemoteModuleInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        List<RemoteModule> remoteModuleList = new ArrayList<>();
        if (CollUtil.isNotEmpty(input.getPtpName()) && input.getPtpName().size() == 1) {
            InstanceIdentifier<RemoteModule> iid = create(RemoteModules.class)
                    .child(RemoteModule.class, new RemoteModuleKey(input.getPtpName().iterator().next()));
            RemoteModule remoteModule = MountTools.queryFromOperational(input.getNeId(), iid);
            if (remoteModule != null) {
                remoteModuleList.add(remoteModule);
            }
        } else {
            InstanceIdentifier<RemoteModules> iid = create(RemoteModules.class);
            RemoteModules remoteModules = MountTools.queryFromOperational(input.getNeId(), iid);
            if (remoteModules != null && remoteModules.getRemoteModule() != null) {
                remoteModuleList.addAll(remoteModules.getRemoteModule().values());
                if (CollUtil.isNotEmpty(input.getPtpName()) && CollUtil.isNotEmpty(remoteModuleList)) {
                    //根据ptpName进行过滤
                    remoteModuleList = remoteModuleList.stream().filter(e -> input.getPtpName().contains(e.getName())).collect(Collectors.toList());
                }
            }
        }
        QueryRemoteModuleOutputBuilder queryRemoteModuleOutputBuilder = new QueryRemoteModuleOutputBuilder();
        queryRemoteModuleOutputBuilder.setRemoteModule(ltm(new RemoteModuleTransformImpl().devRemoteModuleToApiList(remoteModuleList)));
        return RpcResultUtil.success(queryRemoteModuleOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<QueryPortTypeOutput>> queryPortType(QueryPortTypeInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<PtpTypePacs> iid = create(Extension.class).child(PtpTypePacs.class);
        PtpTypePacs ptpType = MountTools.queryFromOperational(input.getNeId(), iid);
        QueryPortTypeOutputBuilder builder = new QueryPortTypeOutputBuilder();
        if (ptpType != null && ptpType.getPtpType() != null) {
            builder.setPtpType(ltm(new PtpTransformImpl().devPortTypeToApiList(ptpType.getPtpType().values())));
        }
        return RpcResultUtil.success(builder.build());
    }

    @Override
    public ListenableFuture<RpcResult<ModifyRemoteModuleOutput>> modifyRemoteModule(ModifyRemoteModuleInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<RemoteModule> iid = create(RemoteModules.class).child(RemoteModule.class, new RemoteModuleKey(input.getName()));
        RemoteModuleBuilder remoteModuleBuilder = new RemoteModuleBuilder().withKey(new RemoteModuleKey(input.getName()));
        remoteModuleBuilder.setName(input.getName());
        remoteModuleBuilder.setMonitoringEnable(input.getMonitoringEnable());
        remoteModuleBuilder.setVlanId(input.getVlanId());
        remoteModuleBuilder.setRemoteModuleName(input.getRemoteModuleName());
        MountTools.doMergeToConfig(input.getNeId(), iid, remoteModuleBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryLagParameterOutput>> queryLagParameter(QueryLagParameterInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        QueryLagParameterOutputBuilder builder = new QueryLagParameterOutputBuilder();
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            InstanceIdentifier<LagParameters> iid = create(Extension.class).child(LagParameters.class);
            LagParameters lag = MountTools.queryFromOperational(input.getNeId(), iid);
            if (lag != null && lag.getLagParameter() != null) {
                builder.setLagParameter(ltm(new LagTransformImpl().devLagParameterToApi(lag.getLagParameter().values())));
            }
        }
        return RpcResultUtil.success(builder.build());
    }

    @Override
    public ListenableFuture<RpcResult<AddVcgPathNeOutput>> addVcgPathNe(AddVcgPathNeInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        VsiTransformImpl vsiTransform = new VsiTransformImpl();
        Future<RpcResult<AddVcgPathOutput>> rpcResultFuture = optOtnExtensionService.addVcgPath(vsiTransform.apiAddVcgPathInputToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResultFuture, vsiTransform::apiAddVcgPathOutputToDev);
    }

    @Override
    public ListenableFuture<RpcResult<DeleteRemoteModuleOutput>> deleteRemoteModule(DeleteRemoteModuleInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        DeleteRemotePtpsInputBuilder deleteRemotePtpsInputBuilder = new DeleteRemotePtpsInputBuilder().setName(input.getPtpName());
        return RpcResultUtil.buildFutureResult(optOtnExtensionService.deleteRemotePtps(deleteRemotePtpsInputBuilder.build()));
    }

    @Override
    public ListenableFuture<RpcResult<ModifyEthCtpOutput>> modifyEthCtp(ModifyEthCtpInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<CtpExtension> iid =
                create(Extension.class)
                        .child(CtpExtensions.class)
                        .child(CtpExtension.class, new CtpExtensionKey(input.getCtpName()));
        //下发设备
        MountTools.doMergeToConfig(input.getNeId(), iid, new CtpExtensionTransformImpl().apiCtpExtensionToDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetPrbsResultOutput>> getPrbsResult(GetPrbsResultInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        PrbsTransformImpl prbsTransform = new PrbsTransformImpl();
        Future<RpcResult<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetPrbsResultOutput>> prbsResult = optOtnExtensionService.getPrbsResult(prbsTransform.apiToGetPrbsResultInputDev(input));
        return RpcResultUtil.buildFutureResult(prbsResult, prbsTransform::devToGetPrbsResultOutputApi);
    }

    @Override
    public ListenableFuture<RpcResult<GetLicenseInfoOutput>> getLicenseInfo(GetLicenseInfoInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.OptOtnExtensionService rpc = MountTools.getRpcService(input.getNeId(), org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.OptOtnExtensionService.class);
        var inputDev = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetLicenseInfoInputBuilder().build();
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetLicenseInfoOutput>> licenseInfo = rpc.getLicenseInfo(inputDev);
        GetLicenseInfoOutput output = null;
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetLicenseInfoOutput rpcResult = null;
        try {
            rpcResult = licenseInfo.get().getResult();
            output = new LicenseTransformImpl().devToGetLicenseInfoOutput(rpcResult);
        } catch (Exception e) {
            LOG.error("license转换出错：" + e.getMessage());
        }
        return RpcResultUtil.success(output);
    }

    @Override
    public ListenableFuture<RpcResult<PrbsBitErrorValueCleanOutput>> prbsBitErrorValueClean(PrbsBitErrorValueCleanInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        return RpcResultUtil.buildFutureResult(optOtnExtensionService.prbsBitErrorValueClean(new PrbsTransformImpl().apiToPrbsBitErrorValueCleanInputDev(input)));
    }

    @Override
    public ListenableFuture<RpcResult<GetUnknownTpnOutput>> getUnknownTpn(GetUnknownTpnInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        UnknownTpnTransformImpl tpnTransform = new UnknownTpnTransformImpl();
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetUnknownTpnInput getUnknownTpnInput = tpnTransform.apiToGetUnknownTpnInputDev(input);
        Future<RpcResult<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetUnknownTpnOutput>> unknownTpn = optOtnExtensionService.getUnknownTpn(getUnknownTpnInput);
        return RpcResultUtil.buildFutureResult(unknownTpn, tpnTransform::devToGetUnknownTpnOutputApi);
    }

    @Override
    public ListenableFuture<RpcResult<SetPrbsParametersOutput>> setPrbsParameters(SetPrbsParametersInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        return RpcResultUtil.buildFutureResult(optOtnExtensionService.setPrbsParameters(new PrbsTransformImpl().apiToSetPrbsParametersInputDev(input)));
    }

    @Override
    public ListenableFuture<RpcResult<GetPrbsParameterOutput>> getPrbsParameter(GetPrbsParameterInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        PrbsTransformImpl prbsTransform = new PrbsTransformImpl();
        Future<RpcResult<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetPrbsParameterOutput>> prbsParameter = optOtnExtensionService.getPrbsParameter(prbsTransform.apiToGetPrbsParameterInputDev(input));
        return RpcResultUtil.buildFutureResult(prbsParameter, prbsTransform::devToGetPrbsParameterOutputApi);
    }

    @Override
    public ListenableFuture<RpcResult<DeleteServiceMemberNeOutput>> deleteServiceMemberNe(DeleteServiceMemberNeInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptCreateServiceService service = MountTools.getRpcService(input.getNeId(), OptCreateServiceService.class);
        NeServiceTransformImpl serviceTransform = new NeServiceTransformImpl();
        Future<RpcResult<DeleteServiceMemberOutput>> rpcResultFuture = service.deleteServiceMember(serviceTransform.apiToDeleteServiceMemberInputDev(input));
        return RpcResultUtil.buildFutureResult(rpcResultFuture, serviceTransform::devToDeleteServiceMemberNeOutputApi);
    }

    @Override
    public ListenableFuture<RpcResult<CreateServiceNeOutput>> createServiceNe(CreateServiceNeInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptCreateServiceService service = MountTools.getRpcService(input.getNeId(), OptCreateServiceService.class);
        NeServiceTransformImpl serviceTransform = new NeServiceTransformImpl();
        Future<RpcResult<CreateServiceOutput>> rpcResultFuture = service.createService(serviceTransform.apiToCreateServiceInputDev(input));
        return RpcResultUtil.buildFutureResult(rpcResultFuture, serviceTransform::devToCreateServiceNeOutputApi);
    }

    @Override
    public ListenableFuture<RpcResult<AddServiceMemberNeOutput>> addServiceMemberNe(AddServiceMemberNeInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptCreateServiceService service = MountTools.getRpcService(input.getNeId(), OptCreateServiceService.class);
        NeServiceTransformImpl serviceTransform = new NeServiceTransformImpl();
        Future<RpcResult<AddServiceMemberOutput>> rpcResultFuture = service.addServiceMember(serviceTransform.apiToAddServiceMemberInputDev(input));
        return RpcResultUtil.buildFutureResult(rpcResultFuture, serviceTransform::devToAddServiceMemberNeOutputApi);
    }

    @Override
    public ListenableFuture<RpcResult<GetRemoteFtrOutput>> getRemoteFtr(GetRemoteFtrInput input) {
        Map<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ftr.extension.grouping.FtrExtensionKey, org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ftr.extension.grouping.FtrExtension> ftrExtensionKeyFtrExtensionMap = Maps.newHashMap();
        try {
            // 自定义 远端模块数据
            InstanceIdentifier<RemoteModules> iid = create(RemoteModules.class);
            RemoteModules remoteModules = MountTools.queryFromOperational(input.getNeId(), iid);
            // 尝试 旧 ftr数据，将数据组合到一起
            InstanceIdentifier<Ftrs> iidOld = create(Ftrs.class);
            Ftrs devDatas = MountTools.queryFromOperational(input.getNeId(), iidOld);
            ftrExtensionKeyFtrExtensionMap.putAll(remoteManagementVlanTransformer.devRemoteModuleToApi(devDatas, remoteModules));
            FtrExtensions ftrExtensions = getFtrExtensions(input.getNeId());
            if (Objects.nonNull(ftrExtensions)) {
                remoteManagementVlanTransformer.devFtrExtToApi(ftrExtensions, ftrExtensionKeyFtrExtensionMap);
                // 将业务模式 + 管理模式的数据合并到一起
                GetRemoteFtrOutput remoteFtrOutput = new GetRemoteFtrOutputBuilder()
                        .setFtrExtension(ftrExtensionKeyFtrExtensionMap).build();
                return RpcResultUtil.success(remoteFtrOutput);
            }
        } catch (Exception e) {
            LOG.error("ignored query remote ftr error.", e);
        }

        GetRemoteFtrOutput remoteFtrOutput = new GetRemoteFtrOutputBuilder()
                .setFtrExtension(ftrExtensionKeyFtrExtensionMap).build();
        return RpcResultUtil.success(remoteFtrOutput);
    }

    private FtrExtensions getFtrExtensions(String neId) {
        InstanceIdentifier<FtrExtensions> iid = create(FtrExtensions.class);
        FtrExtensions ftrExtensions = MountTools.queryFromOperational(neId, iid);
        return ftrExtensions;
    }

    @Override
    public ListenableFuture<RpcResult<ModifyRemoteFtrOutput>> modifyRemoteFtr(ModifyRemoteFtrInput input) {
        FtrExtensions ftrExtensions = getFtrExtensions(input.getNeId());
        if (Objects.nonNull(ftrExtensions)) {
            FtrExtensionKey key = new FtrExtensionKey(input.getLocalName(), input.getVlanId());
            InstanceIdentifier<FtrExtension> iid = create(FtrExtensions.class).child(FtrExtension.class, key);
            FtrExtension ftrExtension = new FtrExtensionBuilder().setName(input.getName()).withKey(key).build();
            MountTools.doMergeToConfig(input.getNeId(), iid, ftrExtension);
            return RpcResultUtil.success();
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<DeleteRemoteFtrOutput>> deleteRemoteFtr(DeleteRemoteFtrInput input) {
        FtrExtensions ftrExtensions = getFtrExtensions(input.getNeId());
        if (Objects.nonNull(ftrExtensions)) {
            OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
            var ftrInput = new DeleteRemoteFtrInputBuilder()
                    .setLocalName(input.getLocalName())
                    .setVlanId(input.getVlanId())
                    .build();
            var rpcResultListenableFuture = service.deleteRemoteFtr(ftrInput);
            return RpcResultUtil.buildFutureResult(rpcResultListenableFuture);
        }
        return RpcResultUtil.success();
    }
}
