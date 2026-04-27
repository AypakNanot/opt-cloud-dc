/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptNeTimeServiceImpl;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.time.rev200427.GetObjectTimeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.time.rev200427.GetObjectTimeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.time.rev200427.GetObjectTimeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.time.rev200427.SetManagedElementTimeNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.time.rev200427.SetManagedElementTimeNeOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.AccDevmService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.GetManagedElementTimeInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.GetManagedElementTimeOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.SetManagedElementTimeInputBuilder;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.concurrent.Future;

/**
 * dc-aggregator - CtcOptNeTimeServiceImpl
 * 网元时间配置
 * date       time        author
 * ─────────────────────────────
 * 2021/10/7   9:59      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class CtcOptNeTimeServiceImpl extends BaseOptNeTimeServiceImpl implements IDeviceServiceOtnCtc {
    @Override
    public ListenableFuture<RpcResult<GetObjectTimeOutput>> getObjectTime(GetObjectTimeInput input) {
        AccDevmService accDevmService = MountTools.getRpcService(input.getNeId(), AccDevmService.class);
        Future<RpcResult<GetManagedElementTimeOutput>> rpcResultFuture = accDevmService.getManagedElementTime(new GetManagedElementTimeInputBuilder().build());
        return RpcResultUtil.buildFutureResult(rpcResultFuture, (result -> new GetObjectTimeOutputBuilder().setObjectTime(result.getDateTime()).build()));
    }

    /**
     * 网元时间设置
     *
     * @param input 网元基本属性，时间
     * @return 操作结果
     */
    @Override
    public ListenableFuture<RpcResult<SetManagedElementTimeNeOutput>> setManagedElementTimeNe(SetManagedElementTimeNeInput input) {
        AccDevmService accDevmService = MountTools.getRpcService(input.getNeId(), AccDevmService.class);
        SetManagedElementTimeInputBuilder setManagedElementTimeInputBuilder = new SetManagedElementTimeInputBuilder()
                .setNewTime(input.getNewTime());
        return RpcResultUtil.buildFutureResult(accDevmService.setManagedElementTime(setManagedElementTimeInputBuilder.build()));
    }
}
