/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.impl;

import cn.hutool.core.collection.CollUtil;
import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciCommonResourceServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.common.resource.CommonResourceTransformImpl;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.types.rev220208.OPENCONFIGCOMPONENT;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.com.device.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.linecard.rev230426.Component1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.psu.rev230426.Config1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.psu.rev230426.Config1Builder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.anchors.top.PowerSupply;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.Components;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.components.Component;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.components.ComponentKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.component.top.components.component.Config;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.stream.Collectors;

import static cn.hutool.core.text.CharSequenceUtil.isNotBlank;

/**
 * CTC 通用属性实现类
 * 2022/3/3 14:48
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class CmccOptCommonResourceServiceImpl extends BaseDciCommonResourceServiceImpl implements IDeviceServiceWdmCmcc {

    @Override
    public ListenableFuture<RpcResult<UpdateCommonResourceOutput>> updateCommonResource(UpdateCommonResourceInput input) {
        String vendorTypePreconf = input.getVendorTypePreconf();
        boolean uninstall = "un_slot".equalsIgnoreCase(vendorTypePreconf);
        if (input.getPowerAdminState() == null) {
            if (uninstall) {
                @NonNull KeyedInstanceIdentifier<Component, ComponentKey> iid = create(Components.class).child(Component.class, new ComponentKey(input.getName()));
                MountTools.deleteFromConfigIgnoreLock(input.getNeId(), iid);
            } else {
                @NonNull InstanceIdentifier<Config> iid
                        = create(Components.class).child(Component.class, new ComponentKey(input.getName())).child(Config.class);
                if (isNotBlank(vendorTypePreconf)) {
                    MountTools.doMergeToConfigIgnoreLock(input.getNeId(), iid, new CommonResourceTransformImpl().apiUpdateConfigToDev(input));
                } else {
                    MountTools.doMergeToConfig(input.getNeId(), iid, new CommonResourceTransformImpl().apiUpdateConfigToDev(input));
                }
            }
        } else {
            @NonNull InstanceIdentifier<Component1> iid = create(Components.class)
                    .child(Component.class, new ComponentKey(input.getName()))
                    .augmentation(Component1.class);
            MountTools.doMergeToConfig(input.getNeId(), iid, new CommonResourceTransformImpl().apiUpdateLineCardToDev(input));
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetCommonResourceOutput>> getCommonResource(GetCommonResourceInput input) {
        Components result = filter(input.getNeId(), input.getName(), CollUtil.isEmpty(input.getType()) ? null : input.getType().stream().map(OPENCONFIGCOMPONENT::implementedInterface).collect(Collectors.toSet()));
        return RpcResultUtil.success(new CommonResourceTransformImpl().devCommonResourceToApi(result));
    }

    /**
     * Invoke {@code delete-common-resource} RPC.
     *
     * <pre>
     *     <code>
     *         删除component
     *     </code>
     * </pre>
     *
     * @param input of {@code delete-common-resource}
     * @return output of {@code delete-common-resource}
     */
    @Override
    public ListenableFuture<RpcResult<DeleteCommonResourceOutput>> deleteCommonResource(DeleteCommonResourceInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<ModifyPsuOutput>> modifyPsu(ModifyPsuInput input) {
        InstanceIdentifier<Config1> iid = create(Components.class)
                .child(Component.class, new ComponentKey(input.getName()))
                .child(PowerSupply.class)
                .child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.anchors.top.power.supply.Config.class)
                .augmentation(Config1.class);
        var configBuilder = new Config1Builder();
        configBuilder.setPowerLoadHighThreshold(input.getPowerLoadHighThreshold());
        MountTools.doMergeToConfig(input.getNeId(), iid, configBuilder.build());
        return RpcResultUtil.success();
    }
}
