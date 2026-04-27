/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciOtdrImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.config.DciOtdrTransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.util.DciUtils;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.otdr.rev200210.*;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.ActionResult;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.MiniotnOtdrService;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.otdr.result.top.Results;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.otdr.result.top.results.Result;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.otdr.result.top.results.ResultKey;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.otdr.top.Otdrs;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.otdr.top.otdrs.Otdr;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.otdr.top.otdrs.OtdrKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

/**
 * @author Quan Jingyuan
 * @since 2022/3/7
 **/
public class CmccOptOtdrServiceImpl extends BaseDciOtdrImpl implements IDeviceServiceWdmCmcc {
    private final static String IS_OPT_TEST = "isOptTest";

    @Override
    public ListenableFuture<RpcResult<StartOtdrOutput>> startOtdr(StartOtdrInput input) {
        MiniotnOtdrService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnOtdrService.class);
        DciOtdrTransform dciOtdrTransform = new DciOtdrTransform();
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.StartOtdrOutput>> rpcResultListenableFuture = rpcService.startOtdr(dciOtdrTransform.apiToStartOtdrInputDev(input));
        return RpcResultUtil.buildFutureResult(rpcResultListenableFuture, dciOtdrTransform::devToStartOtdrOutputApi, r -> DciUtils.getOcRpcResult(r.getStartOtdrResult() == ActionResult.SUCCESS));

    }

    @Override
    public ListenableFuture<RpcResult<StopOtdrOutput>> stopOtdr(StopOtdrInput input) {
        MiniotnOtdrService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnOtdrService.class);
        DciOtdrTransform dciOtdrTransform = new DciOtdrTransform();
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.StopOtdrOutput>> rpcResultListenableFuture = rpcService.stopOtdr(dciOtdrTransform.apiToStopOtdrOutputDev(input));
        return RpcResultUtil.buildFutureResult(rpcResultListenableFuture, null, r -> DciUtils.getOcRpcResult(r.getStopOtdrResult() == ActionResult.SUCCESS));
    }

    @Override
    public ListenableFuture<RpcResult<GetOtdrConfigOutput>> getOtdrConfig(GetOtdrConfigInput input) {
        @NonNull InstanceIdentifier<Otdrs> otdrsInstanceIdentifier = create(Otdrs.class);
        String property = System.getProperty(IS_OPT_TEST, Boolean.FALSE.toString());
        Otdrs otdrs = null;
        if (Boolean.valueOf(property)) {
//            otdrs=new TestData().getOtdr();
        } else {
            otdrs = MountTools.queryFromOperational(input.getNeId(), otdrsInstanceIdentifier);
        }
        return RpcResultUtil.success(new DciOtdrTransform().devToGetOtdrConfigOutputApi(otdrs));
    }

    @Override
    public ListenableFuture<RpcResult<GetOtdrResultOutput>> getOtdrResult(GetOtdrResultInput input) {
        @NonNull KeyedInstanceIdentifier<Otdr, OtdrKey> child = create(Otdrs.class).child(Otdr.class, new OtdrKey(input.getName()));
        Otdr otdr = null;
        String property = System.getProperty(IS_OPT_TEST, Boolean.FALSE.toString());
        if (Boolean.valueOf(property)) {
//            otdr= new TestData().getOtdr().getOtdr().values().iterator().next();
        } else {
            otdr = MountTools.queryFromOperational(input.getNeId(), child);
        }

        return RpcResultUtil.success(new DciOtdrTransform().devToGetOtdrResultOutputApi(otdr));
    }

    @Override
    public ListenableFuture<RpcResult<GetOtdrResultEventOutput>> getOtdrResultEvent(GetOtdrResultEventInput input) {
        @NonNull KeyedInstanceIdentifier<Result, ResultKey> child = create(Otdrs.class).child(Otdr.class, new OtdrKey(input.getName())).child(Results.class).child(Result.class, new ResultKey(input.getIndex(), input.getMonitorPort()));
        Result result = null;
        String property = System.getProperty(IS_OPT_TEST, Boolean.FALSE.toString());
        if (Boolean.valueOf(property)) {
//            result= new TestData().getOtdr().getOtdr().values().iterator().next().getResults().getResult().values().iterator().next();
        } else {
            result = MountTools.queryFromOperational(input.getNeId(), child);
        }
        return RpcResultUtil.success(new DciOtdrTransform().devToGetOtdrResultEventOutputApi(result));
    }

    @Override
    public ListenableFuture<RpcResult<SetOtdrConfigOutput>> setOtdrConfig(SetOtdrConfigInput input) {
        @NonNull InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.otdr.top.otdrs.otdr.Config> child = create(Otdrs.class).child(Otdr.class, new OtdrKey(input.getName())).child(org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.otdr.top.otdrs.otdr.Config.class);
        MountTools.doMergeToConfig(input.getNeId(), child, new DciOtdrTransform().apiToOtdrDev(input));
        return RpcResultUtil.success();
    }
}
