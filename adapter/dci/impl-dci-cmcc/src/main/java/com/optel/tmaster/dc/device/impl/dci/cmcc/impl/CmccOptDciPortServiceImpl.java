/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciPortImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.impl.common.CommonResourceTransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.common.resource.CommonResourceTransformImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.config.DciPortTransform;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.types.rev220208.PORT;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.port.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.port.rev230426.Config1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.anchors.top.Port;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.anchors.top.port.Config;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.Components;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.components.Component;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.components.ComponentKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.line.common.rev230426.Port1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.line.common.rev230426.transport.line.common.port.top.OpticalPort;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.Collections;
import java.util.Map;
import java.util.Set;

/**
 * @author Quan Jingyuan
 * @since 2022/3/3
 **/
public class CmccOptDciPortServiceImpl extends BaseDciPortImpl implements IDeviceServiceWdmCmcc {
    @Override
    public ListenableFuture<RpcResult<GetElectricPortOutput>> getElectricPort(GetElectricPortInput input) {
        return RpcResultUtil.success(new GetElectricPortOutputBuilder().setComponent(this.getComponentForPort(input.getNeId(), input.getName())).build());
    }

    @Override
    public ListenableFuture<RpcResult<SetElectricPortOutput>> setElectricPort(SetElectricPortInput input) {
        @NonNull InstanceIdentifier<Config1> iid = create(Components.class).child(Component.class, new ComponentKey(input.getName())).child(Port.class).child(Config.class).augmentation(Config1.class);
        MountTools.doMergeToConfig(input.getNeId(), iid, new DciPortTransform().apiToConfig1Dev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<SetOpticalPortOutput>> setOpticalPort(SetOpticalPortInput input) {
        @NonNull InstanceIdentifier<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.line.common.rev230426.transport.line.common.port.top.optical.port.Config> child = create(Components.class).child(Component.class, new ComponentKey(input.getName())).child(Port.class).augmentation(Port1.class).child(OpticalPort.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.line.common.rev230426.transport.line.common.port.top.optical.port.Config.class);
        MountTools.doMergeToConfig(input.getNeId(), child, new DciPortTransform().apiToOpticalPortConfigDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetOpticalPortOutput>> getOpticalPort(GetOpticalPortInput input) {
        return RpcResultUtil.success(new GetOpticalPortOutputBuilder().setComponent(this.getComponentForPort(input.getNeId(), input.getName())).build());
    }

    private Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.ComponentKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.Component> getComponentForPort(String neId, Set<String> names) {
        Components components = getComponents(neId, names);
        Components filter = filter(components, Collections.singleton(PORT.class));
        return new CommonResourceTransformImpl().devToComponentsToApi(filter);
    }
}
