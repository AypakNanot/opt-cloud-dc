/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciNeServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.config.DciNeTransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.util.DciUtils;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.ne.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.top.Lldp;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.chassis.rev200630.State1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.anchors.top.Chassis;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.anchors.top.chassis.State;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.Components;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.components.Component;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.components.ComponentKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev230426.system.ntp.top.Ntp;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev230426.system.top.System;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.MiniotnRpcService;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.Collections;

/**
 * CtcOptDciNeServiceImpl
 * 实现类
 * date       time        author
 * ─────────────────────────────
 * 2022/2/8   16:31      liwenxue
 * Copyright (c) 2022, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class CmccOptDciNeServiceImpl extends BaseDciNeServiceImpl implements IDeviceServiceWdmCmcc {
    private static final String IS_OPT_TEST = "isOptTest";

    @Override
    public ListenableFuture<RpcResult<UpdateNeOutput>> updateNe(UpdateNeInput input) {
        //ntp
        @NonNull InstanceIdentifier<System> iid = create(System.class);
        MountTools.doMergeToConfig(input.getNeId(), iid, new DciNeTransform().apiNtpToDev(input));
        @NonNull InstanceIdentifier<Lldp> lldpIid = create(Lldp.class);
        MountTools.doMergeToConfig(input.getNeId(), lldpIid, new DciNeTransform().apiLldpToDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetNeInfoOutput>> getNeInfo(GetNeInfoInput input) {
        // OpenconfigRpcService rpcService = MountTools.getRpcService(input.getNeId(), OpenconfigRpcService.class);
        // ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetNeInfoOutput>> resultListenableFuture =
        //         rpcService.getNeInfo(new GetNeInfoInputBuilder().build());
        //ntp
        InstanceIdentifier<Ntp> ntpIid = create(System.class).child(Ntp.class);
//        @NonNull InstanceIdentifier<Ntp> ntpIid = create(System.class).child(Ntp.class);
        //lldp
        @NonNull InstanceIdentifier<Lldp> lldpIid = create(Lldp.class);
//         @NonNull InstanceIdentifier<System> systemIid = create(System.class).child(Ntp.class);

        Lldp lldp = null;
        Ntp ntp = null;
        String property = java.lang.System.getProperty(IS_OPT_TEST, Boolean.FALSE.toString());
        if (Boolean.valueOf(property)) {
//            TestData testData = new TestData();
//            lldp =testData.getLLdp();
//            ntp=testData.getSystem().getNtp();
        } else {
            lldp = MountTools.queryFromOperational(input.getNeId(), lldpIid);
            ntp = MountTools.queryFromOperational(input.getNeId(), ntpIid);
        }
        //设备机框上的数据
        Components result = filter(input.getNeId(), null, Collections.singleton(org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.types.rev220208.ME.class));
        return RpcResultUtil.success(new DciNeTransform().devGetNeInfoOutputToApi(result, ntp, lldp));
        // return RpcResultUtil.buildFutureResult(resultListenableFuture, r->new DciNeTransform().devGetNeInfoOutputToApi(r,result,system,lldp),
        //         r-> DciUitls.getOcRpcResult(r.getResult(), r.getMsg()));
    }

    @Override
    public ListenableFuture<RpcResult<QueryComponentOutput>> queryComponent(QueryComponentInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<RebootOutput>> reboot(RebootInput input) {
        MiniotnRpcService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnRpcService.class);
        DciNeTransform dciNeTransform = new DciNeTransform();
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.RebootOutput>> reboot = rpcService.reboot(dciNeTransform.apiToRebootInputDev(input));
        return RpcResultUtil.buildFutureResult(reboot, null, e -> DciUtils.getOcRpcResult(e.getRebootResult()));
    }

    @Override
    public ListenableFuture<RpcResult<SetDatetimeOutput>> setDatetime(SetDatetimeInput input) {

        MiniotnRpcService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnRpcService.class);
        DciNeTransform dciNeTransform = new DciNeTransform();
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.SetDatetimeOutput>> rpcResultListenableFuture = rpcService.setDatetime(dciNeTransform.apiToSetDatetimeInputDev(input));
        return RpcResultUtil.buildFutureResult(rpcResultListenableFuture, null, r -> DciUtils.getOcRpcResult(r.getSetDatetimeResult()));

    }

    @Override
    public ListenableFuture<RpcResult<GetChassisConfigOutput>> getChassisConfig(GetChassisConfigInput input) {
        @NonNull InstanceIdentifier<Components> identifier = create(Components.class);
        DciNeTransform dciNeTransform = new DciNeTransform();
        Components components = MountTools.queryFromOperational(input.getNeId(), identifier);
        return RpcResultUtil.success(dciNeTransform.devToGetChassisConfigOutputApi(components));
    }

    @Override
    public ListenableFuture<RpcResult<SetChassisConfigOutput>> setChassisConfig(SetChassisConfigInput input) {
        @NonNull InstanceIdentifier<State1> identifier = create(Components.class).child(Component.class, new ComponentKey(input.getName())).child(Chassis.class).child(State.class).augmentation(State1.class);
        MountTools.doMergeToConfig(input.getNeId(), identifier, new DciNeTransform().apiToState1Dev(input));
        return RpcResultUtil.success();
    }
}
