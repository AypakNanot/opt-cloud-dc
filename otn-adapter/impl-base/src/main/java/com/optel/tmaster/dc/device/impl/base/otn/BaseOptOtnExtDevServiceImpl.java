/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.base.otn;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.*;
import org.opendaylight.yangtools.yang.common.RpcResult;

/**
 * dc-aggregator - BaseOptOtnExtDevServiceImpl
 * 扩展功能
 * date       time        author
 * ─────────────────────────────
 * 2021/10/7   9:52      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public abstract class BaseOptOtnExtDevServiceImpl implements IDeviceServiceOtn, OptOtnExtDevService {
    @Override
    public ListenableFuture<RpcResult<GetRemoteFtrOutput>> getRemoteFtr(GetRemoteFtrInput input) {
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyRemoteFtrOutput>> modifyRemoteFtr(ModifyRemoteFtrInput input) {
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<DeleteRemoteFtrOutput>> deleteRemoteFtr(DeleteRemoteFtrInput input) {
        return RpcResultUtil.success();
    }
}
