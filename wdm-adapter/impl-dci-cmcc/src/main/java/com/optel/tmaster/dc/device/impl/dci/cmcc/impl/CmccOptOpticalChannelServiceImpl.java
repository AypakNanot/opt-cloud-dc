/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciOpticalChannelImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.impl.common.CommonResourceTransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.common.resource.CommonResourceTransformImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.config.DciOpticalChannelTransform;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.OPTICALCHANNEL;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.optical.channel.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.Components;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.components.Component;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.components.ComponentKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.Component1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.optical.channel.top.OpticalChannel;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.optical.channel.top.optical.channel.Config;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Quan Jingyuan
 * @since 2022/3/7
 **/
public class CmccOptOpticalChannelServiceImpl extends BaseDciOpticalChannelImpl implements IDeviceServiceWdmCmcc {
    @Override
    public ListenableFuture<RpcResult<SetOpticalChannelOutput>> setOpticalChannel(SetOpticalChannelInput input) {
        @NonNull InstanceIdentifier<Config> child = create(Components.class).child(Component.class, new ComponentKey(input.getName())).augmentation(Component1.class).child(OpticalChannel.class).child(Config.class);
        MountTools.doMergeToConfig(input.getNeId(), child, new DciOpticalChannelTransform().apiToConfigDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetOpticalChannelOutput>> getOpticalChannel(GetOpticalChannelInput input) {
        Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.ComponentKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.Component> componentForOpticalChannel = getComponentForOpticalChannel(input.getNeId(), input.getName());
        return RpcResultUtil.success(new GetOpticalChannelOutputBuilder().setComponent(componentForOpticalChannel).build());
    }

    private Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.ComponentKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.Component> getComponentForOpticalChannel(String neId, Set<String> names) {
        Components components = getComponents(neId, names);
        Components filter = filter(components, Collections.singleton(OPTICALCHANNEL.class));
        return new CommonResourceTransformImpl().devToComponentsToApi(filter);
    }
}
