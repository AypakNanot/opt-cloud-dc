/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptObjInstallationServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.ObjInstallTransformImpl;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.obj.installation.rev210116.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eh.installation.capabilitys.grouping.EhInstallationCapability;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eh.installation.capabilitys.grouping.EhInstallationCapabilityKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eq.installation.capabilitys.grouping.EqInstallationCapability;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eq.installation.capabilitys.grouping.EqInstallationCapabilityKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Future;

/**
 * dc-aggregator - CtcOptObjInstallationServiceImpl
 * 板卡/端口安装
 * date       time        author
 * ─────────────────────────────
 * 2021/10/7   10:01      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class CtcOptObjInstallationServiceImpl extends BaseOptObjInstallationServiceImpl implements IDeviceServiceOtnCtc {
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
                setEhInstallationCapability(ltm(new ObjInstallTransformImpl().devEhInstallationToApi(ehInstallationCapabilityList))).build());
    }

    @Override
    public ListenableFuture<RpcResult<InstallEqTypeOutput>> installEqType(InstallEqTypeInput input) {
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        ObjInstallTransformImpl objInstallTransform = new ObjInstallTransformImpl();
        Future<RpcResult<SetEqTypeOutput>> resultFuture = optOtnExtensionService.setEqType(objInstallTransform.apiSetEqTypeInputToDev(input));
        return RpcResultUtil.buildFutureResult(resultFuture, result -> new InstallEqTypeOutputBuilder(objInstallTransform.devEqTypeOutputToApi(result).build()).build());
    }

    @Override
    public ListenableFuture<RpcResult<QueryEqInstallationOutput>> queryEqInstallation(QueryEqInstallationInput input) {
        List<EqInstallationCapability> result = new ArrayList<>();
        if (StrUtil.isEmpty(input.getEqName())) {
            InstanceIdentifier<EqInstallationCapabilitys> iid = create(EqInstallationCapabilitys.class);
            EqInstallationCapabilitys eqInstallationCapabilitys = MountTools.queryFromOperational(input.getNeId(), iid);
            if (eqInstallationCapabilitys != null) {
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
        ObjInstallTransformImpl objInstallTransform = new ObjInstallTransformImpl();
        return RpcResultUtil.success(objInstallTransform.devQueryEqInstallationOutputToApi(result).build());
    }

    @Override
    public ListenableFuture<RpcResult<InstallPtpTypeOutput>> installPtpType(InstallPtpTypeInput input) {
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        ObjInstallTransformImpl objInstallTransform = new ObjInstallTransformImpl();
        SetPtpTypeInputBuilder inputBuilder = objInstallTransform.apiSetPtpTypeInputToDev(input);
        Future<RpcResult<SetPtpTypeOutput>> rpcResultFuture = optOtnExtensionService.setPtpType(inputBuilder.build());
        return RpcResultUtil.buildFutureResult(rpcResultFuture, result -> new InstallPtpTypeOutputBuilder(objInstallTransform.devSetPtpTypeOutputToApi(result).build()).build());
    }
}
