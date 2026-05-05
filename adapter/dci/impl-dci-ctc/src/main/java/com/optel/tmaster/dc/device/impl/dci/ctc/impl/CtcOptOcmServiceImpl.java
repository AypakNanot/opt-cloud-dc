/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciOcmImpl;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.config.DciOcmTransform;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.ocm.rev200210.GetOcmConfigInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.ocm.rev200210.GetOcmConfigOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.ocm.rev200210.SetOcmConfigInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.ocm.rev200210.SetOcmConfigOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.channel.monitor.rev200630.channel.monitor.top.ChannelMonitors;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.channel.monitor.rev200630.channel.monitor.top.channel.monitors.ChannelMonitor;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.channel.monitor.rev200630.channel.monitor.top.channel.monitors.ChannelMonitorKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.channel.monitor.rev200630.channel.monitor.top.channel.monitors.channel.monitor.Config;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

/**
 * 光监控配置实现类
 *
 * @author Quan Jingyuan
 * @since 2022/3/8
 **/
public class CtcOptOcmServiceImpl extends BaseDciOcmImpl implements IDeviceServiceWdmCtc {
    @Override
    public ListenableFuture<RpcResult<GetOcmConfigOutput>> getOcmConfig(GetOcmConfigInput input) {
        @NonNull InstanceIdentifier<ChannelMonitors> identifier = create(ChannelMonitors.class);
        ChannelMonitors channelMonitors = MountTools.queryFromOperational(input.getNeId(), identifier);
        return RpcResultUtil.success(new DciOcmTransform().devToGetOchConfigOutputApi(channelMonitors, input.getName()));

    }

    @Override
    public ListenableFuture<RpcResult<SetOcmConfigOutput>> setOcmConfig(SetOcmConfigInput input) {
        @NonNull InstanceIdentifier<Config> child = create(ChannelMonitors.class)
                .child(ChannelMonitor.class, new ChannelMonitorKey(input.getName()))
                .child(Config.class);
        MountTools.doMergeToConfig(input.getNeId(), child, new DciOcmTransform().apiToConfigDev(input));
        return RpcResultUtil.success();
    }


}
