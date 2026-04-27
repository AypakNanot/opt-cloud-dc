/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciTransceiverImpl;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.common.resource.CommonResourceTransformImpl;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.config.DciTransceiverTransform;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.types.rev220208.TRANSCEIVER;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.transceiver.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.Components;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.Component;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.ComponentKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev200630.Component1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev200630.physical.channel.top.PhysicalChannels;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev200630.physical.channel.top.physical.channels.Channel;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev200630.physical.channel.top.physical.channels.ChannelKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev200630.port.transceiver.top.Transceiver;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev200630.port.transceiver.top.transceiver.Config;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Quan Jingyuan
 * @since 2022/3/4
 **/
public class CtcOptTransceiverServiceImpl extends BaseDciTransceiverImpl implements IDeviceServiceWdmCtc {
    @Override
    public ListenableFuture<RpcResult<GetTransceiversOutput>> getTransceivers(GetTransceiversInput input) {
        return RpcResultUtil.success(new GetTransceiversOutputBuilder().setComponent(getComponentForTransceiver(input.getNeId(), input.getName())).build());
    }

    @Override
    public ListenableFuture<RpcResult<GetTransceiversChannelOutput>> getTransceiversChannel(GetTransceiversChannelInput input) {
        return RpcResultUtil.success(new GetTransceiversChannelOutputBuilder().setComponent(getComponentForTransceiver(input.getNeId(), input.getName())).build());

    }

    @Override
    public ListenableFuture<RpcResult<SetTransceiverOutput>> setTransceiver(SetTransceiverInput input) {
        @NonNull InstanceIdentifier<Config> child = create(Components.class).child(Component.class, new ComponentKey(input.getName())).augmentation(Component1.class).child(Transceiver.class).child(Config.class);
        MountTools.doMergeToConfig(input.getNeId(), child, new DciTransceiverTransform().apiToConfigDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<SetTransceiverChannelOutput>> setTransceiverChannel(SetTransceiverChannelInput input) {
        @NonNull InstanceIdentifier<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev200630.physical.channel.top.physical.channels.channel.Config> child = create(Components.class).child(Component.class, new ComponentKey(input.getName())).augmentation(Component1.class).child(Transceiver.class).child(PhysicalChannels.class).child(Channel.class, new ChannelKey(input.getIndex())).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev200630.physical.channel.top.physical.channels.channel.Config.class);
        MountTools.doMergeToConfig(input.getNeId(), child, new DciTransceiverTransform().apiToChannelDev(input));
        return RpcResultUtil.success();
    }

    private Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.ComponentKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.Component> getComponentForTransceiver(String neId, Set<String> names) {
        Components components = getComponents(neId, names);
        Components filter = filter(components, Collections.singleton(TRANSCEIVER.class));
        return new CommonResourceTransformImpl().devToComponentsToApi(filter);
    }
}
