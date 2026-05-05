/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciEdfaImpl;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.config.DciEdfaTransform;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.edfa.rev200210.GetEdfaConfigInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.edfa.rev200210.GetEdfaConfigOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.edfa.rev200210.SetEdfaConfigInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.edfa.rev200210.SetEdfaConfigOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev200630.optical.amplifier.top.OpticalAmplifier;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev200630.optical.amplifier.top.optical.amplifier.Amplifiers;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev200630.optical.amplifier.top.optical.amplifier.amplifiers.Amplifier;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev200630.optical.amplifier.top.optical.amplifier.amplifiers.AmplifierKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev200630.optical.amplifier.top.optical.amplifier.amplifiers.amplifier.Config;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

/**
 * 光放大器配置实现类
 *
 * @author Quan Jingyuan
 * @since 2022/3/8
 **/
public class CtcOptEdfaServiceImpl extends BaseDciEdfaImpl implements IDeviceServiceWdmCtc {
    @Override
    public ListenableFuture<RpcResult<GetEdfaConfigOutput>> getEdfaConfig(GetEdfaConfigInput input) {
        @NonNull InstanceIdentifier<OpticalAmplifier> identifier = create(OpticalAmplifier.class);
        OpticalAmplifier opticalAmplifier = MountTools.queryFromOperational(input.getNeId(), identifier);
        return RpcResultUtil.success(new DciEdfaTransform().devToGetEdfaConfigOutputApi(opticalAmplifier, input.getName()));
    }

    @Override
    public ListenableFuture<RpcResult<SetEdfaConfigOutput>> setEdfaConfig(SetEdfaConfigInput input) {
        @NonNull InstanceIdentifier<Config> child = create(OpticalAmplifier.class).child(Amplifiers.class).child(Amplifier.class, new AmplifierKey(input.getName())).child(Config.class);
        MountTools.doMergeToConfig(input.getNeId(), child, new DciEdfaTransform().apiToConfigDev(input));
        return RpcResultUtil.success();
    }
}
