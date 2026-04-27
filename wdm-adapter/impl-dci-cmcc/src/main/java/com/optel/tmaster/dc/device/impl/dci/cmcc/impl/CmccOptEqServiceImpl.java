/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciEqServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.config.DciEqServiceTransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.util.DciUtils;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.types.rev220208.FAN;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.eq.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.anchors.top.Fan;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.Components;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.components.Component;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.components.ComponentKey;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.MiniotnRpcService;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.Collections;


/**
 * CTC 板卡属性设置
 * 2022/3/10 16:54
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class CmccOptEqServiceImpl extends BaseDciEqServiceImpl implements IDeviceServiceWdmCmcc {

    @Override
    public ListenableFuture<RpcResult<UpdateFanEqOutput>> updateFanEq(UpdateFanEqInput input) {
        @NonNull InstanceIdentifier<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.fan.rev230426.Config1> iid =
                create(Components.class)
                        .child(Component.class, new ComponentKey(input.getName()))
                        .child(Fan.class)
                        .child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.anchors.top.fan.Config.class)
                        .augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.fan.rev230426.Config1.class);
        MountTools.doMergeToConfig(input.getNeId(), iid, new DciEqServiceTransform().apiUpdateFanEqToDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetFanEqOutput>> getFanEq(GetFanEqInput input) {
        Components components = filter(input.getNeId(), null, Collections.singleton(FAN.class));
        return RpcResultUtil.success(new DciEqServiceTransform().devGetFanEqOutputToApi(components));
    }

    @Override
    public ListenableFuture<RpcResult<SwitchMcuOutput>> switchMcu(SwitchMcuInput input) {

        MiniotnRpcService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnRpcService.class);
        DciEqServiceTransform transform = new DciEqServiceTransform();
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.SwitchMcuOutput>> rpcResult
                = rpcService.switchMcu(transform.apiSwitchMcuInputToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResult, transform::devSwitchMcuOutputToDevToApi,
                r -> DciUtils.getOcRpcResult(r.getResult(), r.getMsg()));

    }

    @Override
    public ListenableFuture<RpcResult<GetPtmEqOutput>> getPtmEq(GetPtmEqInput input) {
        @NonNull InstanceIdentifier<Components> iid = create(Components.class);
        Components components = getComponents(input.getNeId(), null);
        return RpcResultUtil.success(new DciEqServiceTransform().devGetPtmEqOutputToApi(components));
    }

    @Override
    public ListenableFuture<RpcResult<UpdatePtmEqOutput>> updatePtmEq(UpdatePtmEqInput input) {
        //极简OTN没有PTM
//        @NonNull InstanceIdentifier<Config1> iid = create(Components.class)
//                .child(Component.class, new ComponentKey(input.getName())).child(Ptm.class)
//                .child(Config.class).augmentation(Config1.class);
//        MountTools.doMergeToConfig(input.getNeId(),iid,new DciEqServiceTransform().apiUpdatePtmEqToDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetMcuEqOutput>> getMcuEq(GetMcuEqInput input) {
        @NonNull InstanceIdentifier<Components> iid = create(Components.class);
        Components components = getComponents(input.getNeId(), null);
        return RpcResultUtil.success(new DciEqServiceTransform().devGetMcuEqOutputToApi(components));
    }

    @Override
    public ListenableFuture<RpcResult<GetPsuEqOutput>> getPsuEq(GetPsuEqInput input) {
        @NonNull InstanceIdentifier<Components> iid = create(Components.class);
        Components components = getComponents(input.getNeId(), null);
        return RpcResultUtil.success(new DciEqServiceTransform().devGetPsuEqOutputToApi(components));
    }
}
