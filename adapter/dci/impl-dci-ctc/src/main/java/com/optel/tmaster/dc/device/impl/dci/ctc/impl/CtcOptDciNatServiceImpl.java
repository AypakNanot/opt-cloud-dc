/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciNatServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.config.DciNatServiceTransform;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.communication.rev220928.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.nat.rev220627.nats.top.Nats;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.nat.rev220627.nats.top.nats.Nat;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.nat.rev220627.nats.top.nats.NatKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

/**
 * @author Quan Jingyuan
 * @since 2022/9/28
 **/
public class CtcOptDciNatServiceImpl extends BaseDciNatServiceImpl implements IDeviceServiceWdmCtc {
    /**
     * Invoke {@code get-nat} RPC.
     *
     * @param input of {@code get-nat}
     * @return output of {@code get-nat}
     */
    @Override
    public ListenableFuture<RpcResult<GetNatOutput>> getNat(GetNatInput input) {
        InstanceIdentifier<Nats> iid = create(Nats.class);
        Nats nats = MountTools.queryFromOperational(input.getNeId(), iid);
        return RpcResultUtil.success(new DciNatServiceTransform().devToNatApi(nats));
    }

    /**
     * Invoke {@code set-nat} RPC.
     *
     * @param input of {@code set-nat}
     * @return output of {@code set-nat}
     */
    @Override
    public ListenableFuture<RpcResult<SetNatOutput>> setNat(SetNatInput input) {
        @NonNull KeyedInstanceIdentifier<Nat, NatKey> child = create(Nats.class).child(Nat.class, new NatKey(input.getIndex()));
        MountTools.doMergeToConfig(input.getNeId(), child, new DciNatServiceTransform().apiToNatDev(input));
        return RpcResultUtil.success();
    }

    /**
     * Invoke {@code delete-nat} RPC.
     *
     * @param input of {@code delete-nat}
     * @return output of {@code delete-nat}
     */
    @Override
    public ListenableFuture<RpcResult<DeleteNatOutput>> deleteNat(DeleteNatInput input) {
        @NonNull KeyedInstanceIdentifier<Nat, NatKey> child = create(Nats.class).child(Nat.class, new NatKey(input.getIndex()));
        MountTools.deleteFromConfig(input.getNeId(), child);
        return RpcResultUtil.success();
    }
}
