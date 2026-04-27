/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseOptDciLldpServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.config.DciLldpTransform;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.lldp.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp._interface.top.Interfaces;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp._interface.top.interfaces.Interface;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp._interface.top.interfaces.InterfaceKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.top.Lldp;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.top.lldp.Config;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.OpenconfigRpcService;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

/**
 * ClassName: CtcOptDciLldpServiceImpl
 * <ul>
 * <li>中电信 LLDP服务 实现类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2022/3/11 14:48
 */
public class CtcOptDciLldpServiceImpl extends BaseOptDciLldpServiceImpl implements IDeviceServiceWdmCtc {
    /**
     * Invoke {@code get-lldp} RPC.
     *
     * @param input of {@code get-lldp}
     * @return output of {@code get-lldp}
     */
    @Override
    public ListenableFuture<RpcResult<GetLldpOutput>> getLldp(GetLldpInput input) {
        if (input == null) {
            return null;
        }
        @NonNull InstanceIdentifier<Lldp> iid = create(Lldp.class);
        Lldp lldp = MountTools.queryFromOperational(input.getNeId(), iid);
        return RpcResultUtil.success(new DciLldpTransform().devLldpInterfaceToApi(lldp));
    }

    /**
     * Invoke {@code set-lldp-if-enable} RPC.
     *
     * @param input of {@code set-lldp-if-enable}
     * @return output of {@code set-lldp-if-enable}
     */
    @Override
    public ListenableFuture<RpcResult<SetLldpIfEnableOutput>> setLldpIfEnable(SetLldpIfEnableInput input) {
        InterfaceKey interfaceKey = new InterfaceKey(input.getName());
        InstanceIdentifier<Interface> iid = create(Lldp.class).child(Interfaces.class).child(Interface.class, interfaceKey);
        MountTools.doMergeToConfig(input.getNeId(), iid, new DciLldpTransform().apiSetLldpIfEnableInputToDev(input));
        return RpcResultUtil.success();
    }

    /**
     * Invoke {@code set-neighbour-ip} RPC.
     *
     * @param input of {@code set-neighbour-ip}
     * @return output of {@code set-neighbour-ip}
     */
    @Override
    public ListenableFuture<RpcResult<SetNeighbourIpOutput>> setNeighbourIp(SetNeighbourIpInput input) {
        OpenconfigRpcService openconfigRpcService = MountTools.getRpcService(input.getNeId(), OpenconfigRpcService.class);
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.SetNeighborIpOutput>> rpcResultListenableFuture
                = openconfigRpcService.setNeighborIp(new DciLldpTransform().apiSetNeighbourIpInputToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResultListenableFuture);
    }

    /**
     * Invoke {@code set-lldp} RPC.
     *
     * @param input of {@code set-lldp}
     * @return output of {@code set-lldp}
     */
    @Override
    public ListenableFuture<RpcResult<SetLldpOutput>> setLldp(SetLldpInput input) {
        @NonNull InstanceIdentifier<Config> iid = create(Lldp.class).child(Config.class);
        MountTools.doMergeToConfig(input.getNeId(), iid, new DciLldpTransform().apiSetLldpEnableInputToDev(input));
        return RpcResultUtil.success();
    }

}
