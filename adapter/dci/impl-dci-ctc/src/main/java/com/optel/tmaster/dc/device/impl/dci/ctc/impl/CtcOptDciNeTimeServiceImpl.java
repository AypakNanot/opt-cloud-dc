/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciNeTimeServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.config.DciNeTimeTransform;
import com.optel.tmaster.dc.device.impl.dci.ctc.util.DciUtils;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.ne.time.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.OpenconfigRpcService;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.SetDatetimeInputBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.SetDatetimeOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.ntp.server.top.Servers;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.ntp.server.top.servers.Server;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.ntp.server.top.servers.ServerKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.ntp.top.Ntp;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.top.System;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.top.system.State;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

/**
 * ClassName: CtcOptDciNeTimeServiceImpl
 * <ul>
 * <li>中电信 DCI 网元时间服务实现类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2022/3/11 11:07
 */
public class CtcOptDciNeTimeServiceImpl extends BaseDciNeTimeServiceImpl implements IDeviceServiceWdmCtc {
    /**
     * Invoke {@code query-me-ntp} RPC.
     *
     * @param input of {@code query-me-ntp}
     * @return output of {@code query-me-ntp}
     */
    @Override
    public ListenableFuture<RpcResult<QueryMeNtpOutput>> queryMeNtp(QueryMeNtpInput input) {
        InstanceIdentifier<Ntp> iid = create(System.class).child(Ntp.class);
        Ntp ntp = MountTools.queryFromOperational(input.getNeId(), iid);
        return RpcResultUtil.success(new DciNeTimeTransform().devQueryMeNtpOutputToApi(ntp));
    }

    /**
     * Invoke {@code add-modify-me-ntp} RPC.
     *
     * @param input of {@code add-modify-me-ntp}
     * @return output of {@code add-modify-me-ntp}
     */
    @Override
    public ListenableFuture<RpcResult<AddModifyMeNtpOutput>> addModifyMeNtp(AddModifyMeNtpInput input) {
        ServerKey serverKey = new ServerKey(new DciNeTimeTransform().apiHostToDev(input.getAddress()));
        InstanceIdentifier<Server> iid = create(System.class).child(Ntp.class).child(Servers.class).child(Server.class, serverKey);
        MountTools.doMergeToConfig(input.getNeId(), iid, new DciNeTimeTransform().apiAddModifyMeNtpInputToDev(input));
        return RpcResultUtil.success();
    }

    /**
     * Invoke {@code delete-me-ntp} RPC.
     *
     * @param input of {@code delete-me-ntp}
     * @return output of {@code delete-me-ntp}
     */
    @Override
    public ListenableFuture<RpcResult<DeleteMeNtpOutput>> deleteMeNtp(DeleteMeNtpInput input) {
        ServerKey serverKey = new ServerKey(new DciNeTimeTransform().apiDeleteMeNtpInputToDev(input));
        InstanceIdentifier<Server> iid = create(System.class).child(Ntp.class).child(Servers.class).child(Server.class, serverKey);
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    /**
     * Invoke {@code get-ne-time} RPC.
     *
     * @param input of {@code get-ne-time}
     * @return output of {@code get-ne-time}
     */
    @Override
    public ListenableFuture<RpcResult<GetNeTimeOutput>> getNeTime(GetNeTimeInput input) {
        InstanceIdentifier<State> iid = create(System.class).child(State.class);
        State state = MountTools.queryFromOperational(input.getNeId(), iid);
        return RpcResultUtil.success(new DciNeTimeTransform().devGetNeTimeOutputToApi(state));
    }

    /**
     * Invoke {@code set-ne-time} RPC.
     *
     * @param input of {@code set-ne-time}
     * @return output of {@code set-ne-time}
     */
    @Override
    public ListenableFuture<RpcResult<SetNeTimeOutput>> setNeTime(SetNeTimeInput input) {
        OpenconfigRpcService openconfigRpcService = MountTools.getRpcService(input.getNeId(), OpenconfigRpcService.class);
        ListenableFuture<RpcResult<SetDatetimeOutput>> rpcResultListenableFuture = openconfigRpcService.setDatetime(new SetDatetimeInputBuilder().setDatetime(input.getDatetime()).build());
        return RpcResultUtil.buildFutureResult(rpcResultListenableFuture, null, e -> DciUtils.getOcRpcResult(e.getSetDatetimeResult()));
    }
}
