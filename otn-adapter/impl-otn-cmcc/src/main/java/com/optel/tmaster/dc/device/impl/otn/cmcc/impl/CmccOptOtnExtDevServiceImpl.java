/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.impl;

import cn.hutool.core.collection.CollUtil;
import com.google.common.util.concurrent.ListenableFuture;
import cn.hutool.core.map.MapUtil;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptOtnExtDevServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.*;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.service.VsiTransformImpl;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import com.optel.tmaster.dc.general.nc.nccore.NcTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ctp.extension.grouping.CtpExtension;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ctp.extension.grouping.CtpExtensionKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.extension.CtpExtensions;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.extension.LagParameters;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.extension.PtpTypePacs;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lag.parameters.grouping.LagParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lag.parameters.grouping.LagParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lag.parameters.grouping.LagParameterKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.remote.module.grouping.RemoteModule;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.remote.module.grouping.RemoteModuleBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.remote.module.grouping.RemoteModuleKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.tpid.grouping.Tpid;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.tpid.grouping.TpidBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.tpid.grouping.TpidKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * dc-aggregator - CmccOptOtnExtDevServiceImpl
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
public class CmccOptOtnExtDevServiceImpl extends BaseOptOtnExtDevServiceImpl implements IDeviceServiceOtnCmcc {
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
            TpIdTransformImpl tpIdTransform = new TpIdTransformImpl();
            if (input.getName() != null && input.getName().size() == 1) {
                InstanceIdentifier<Tpid> identifier = create(Tpids.class).child(Tpid.class, new TpidKey(CollUtil.getFirst(input.getName())));
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
        TpIdTransformImpl tpIdTransform = new TpIdTransformImpl();
        TpidBuilder tpidBuilder = tpIdTransform.apiTpidToDev(input);
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
                    .child(RemoteModule.class, new RemoteModuleKey(CollUtil.getFirst(input.getPtpName())));
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
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<GetLicenseInfoOutput>> getLicenseInfo(GetLicenseInfoInput input) {
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<PrbsBitErrorValueCleanOutput>> prbsBitErrorValueClean(PrbsBitErrorValueCleanInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<GetUnknownTpnOutput>> getUnknownTpn(GetUnknownTpnInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<SetPrbsParametersOutput>> setPrbsParameters(SetPrbsParametersInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<GetPrbsParameterOutput>> getPrbsParameter(GetPrbsParameterInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<DeleteServiceMemberNeOutput>> deleteServiceMemberNe(DeleteServiceMemberNeInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<CreateServiceNeOutput>> createServiceNe(CreateServiceNeInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<AddServiceMemberNeOutput>> addServiceMemberNe(AddServiceMemberNeInput input) {
        return null;
    }
}
