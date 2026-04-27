/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.impl;

import cn.hutool.core.util.StrUtil;
import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptObjInstallationServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.ObjInstallTransformImpl;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.obj.installation.rev210116.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eh.installation.capabilitys.grouping.EhInstallationCapability;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eh.installation.capabilitys.grouping.EhInstallationCapabilityKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eq.installation.capabilitys.grouping.EqInstallationCapability;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eq.installation.capabilitys.grouping.EqInstallationCapabilityKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * dc-aggregator - CmccOptObjInstallationServiceImpl
 * 板卡/端口安装
 * date       time        author
 * ─────────────────────────────
 * 2021/10/7   10:01      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class CmccOptObjInstallationServiceImpl extends BaseOptObjInstallationServiceImpl implements IDeviceServiceOtnCmcc {
    @Override
    public ListenableFuture<RpcResult<QueryEhInstallationOutput>> queryEhInstallation(QueryEhInstallationInput input) {
        List<EhInstallationCapability> ehInstallationCapabilityList = new ArrayList<>();
        if (StrUtil.isEmpty(input.getName())) {
            InstanceIdentifier<EhInstallationCapabilitys> iid = create(EhInstallationCapabilitys.class);
            EhInstallationCapabilitys ehInstallationCapabilitys = MountTools.queryFromOperational(input.getNeId(), iid);
            if (ehInstallationCapabilitys != null && ehInstallationCapabilitys.getEhInstallationCapability() != null) {
                ehInstallationCapabilityList = new ArrayList<>(ehInstallationCapabilitys.getEhInstallationCapability().values());
            }
        } else {
            InstanceIdentifier<EhInstallationCapability> iid = create(EhInstallationCapabilitys.class)
                    .child(EhInstallationCapability.class, new EhInstallationCapabilityKey(input.getName()));
            EhInstallationCapability ehInstallationCapability = MountTools.queryFromOperational(input.getNeId(), iid);
            if (ehInstallationCapability != null) {
                ehInstallationCapabilityList.add(ehInstallationCapability);
            }
        }
        return RpcResultUtil.success(new QueryEhInstallationOutputBuilder().
                setEhInstallationCapability(new ObjInstallTransformImpl().apiEhInstallationToDev(ehInstallationCapabilityList)).build());
    }

    @Override
    public ListenableFuture<RpcResult<InstallEqTypeOutput>> installEqType(InstallEqTypeInput input) {
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        ObjInstallTransformImpl transform = new ObjInstallTransformImpl();
        Future<RpcResult<SetEqTypeOutput>> resultFuture = optOtnExtensionService.setEqType(transform.apiSetEqTypeInputToDev(input));
        return RpcResultUtil.buildFutureResult(resultFuture, result -> new InstallEqTypeOutputBuilder(transform.devEqTypeOutputToApi(result).build()).build());
    }

    @Override
    public ListenableFuture<RpcResult<QueryEqInstallationOutput>> queryEqInstallation(QueryEqInstallationInput input) {
        List<EqInstallationCapability> result = new ArrayList<>();
        if (StrUtil.isEmpty(input.getEqName())) {
            InstanceIdentifier<EqInstallationCapabilitys> iid = create(EqInstallationCapabilitys.class);
            EqInstallationCapabilitys eqInstallationCapabilitys = MountTools.queryFromOperational(input.getNeId(), iid);
            if (eqInstallationCapabilitys != null && eqInstallationCapabilitys.getEqInstallationCapability() != null) {
                result = new ArrayList<>(eqInstallationCapabilitys.getEqInstallationCapability().values());
            }
        } else {
            InstanceIdentifier<EqInstallationCapability> iid = create(EqInstallationCapabilitys.class)
                    .child(EqInstallationCapability.class, new EqInstallationCapabilityKey(input.getEqName()));
            EqInstallationCapability eqInstallationCapability = MountTools.queryFromOperational(input.getNeId(), iid);
            if (eqInstallationCapability != null) {
                result.add(eqInstallationCapability);
            }
        }
        return RpcResultUtil.success(new ObjInstallTransformImpl().devQueryEqInstallationOutputToApi(result).build());
    }

    @Override
    public ListenableFuture<RpcResult<InstallPtpTypeOutput>> installPtpType(InstallPtpTypeInput input) {
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        SetPtpTypeInputBuilder inputBuilder = new ObjInstallTransformImpl().apiSetPtpTypeInputToDev(input);
        Future<RpcResult<SetPtpTypeOutput>> rpcResultFuture = optOtnExtensionService.setPtpType(inputBuilder.build());
        return RpcResultUtil.buildFutureResult(rpcResultFuture, result -> new InstallPtpTypeOutputBuilder(new ObjInstallTransformImpl().devSetPtpTypeOutputToApi(result).build()).build());
    }

}
