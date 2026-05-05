/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptOtnDhcpServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.DhcpTransformImpl;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.dhcp.rev210104.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.DhcpRelay;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.DhcpRelayBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.DhcpRelayInterfaces;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.dhcp.relay.interfaces.DhcpRelayInterface;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.dhcp.relay.interfaces.DhcpRelayInterfaceBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dhcp.relay.grouping.dhcp.relay.interfaces.DhcpRelayInterfaceKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

/**
 * dc-aggregator - CmccOptOtnDhcpServiceImpl
 * DHCP 配置
 * date       time        author
 * ─────────────────────────────
 * 2021/10/7   10:03      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class CmccOptOtnDhcpServiceImpl extends BaseOptOtnDhcpServiceImpl implements IDeviceServiceOtnCmcc {
    @Override
    public ListenableFuture<RpcResult<QueryDhcpRelayOutput>> queryDhcpRelay(QueryDhcpRelayInput input) {
        InstanceIdentifier<DhcpRelay> iid = create(DhcpRelay.class);
        DhcpRelay dhcpRelay = MountTools.queryFromConfig(input.getNeId(), iid);
        if (dhcpRelay != null) {
            QueryDhcpRelayOutputBuilder dhcpRelayOutput = new DhcpTransformImpl().devQueryDhcpRelayOutputToApi(dhcpRelay);
            return RpcResultUtil.success(dhcpRelayOutput.build());
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ConfigDhcpRelayInterfacesOutput>> configDhcpRelayInterfaces(ConfigDhcpRelayInterfacesInput input) {
        DhcpRelayInterfaceKey dhcpRelayInterfaceKey = new DhcpRelayInterfaceKey(input.getName());
        InstanceIdentifier<DhcpRelayInterface> iid = create(DhcpRelay.class).child(DhcpRelayInterfaces.class)
                .child(DhcpRelayInterface.class, dhcpRelayInterfaceKey);
        DhcpRelayInterfaceBuilder dhcpRelayInterfaceBuilder = new DhcpTransformImpl().apiDhcpRelayInterfaceToDev(input);
        dhcpRelayInterfaceBuilder.withKey(dhcpRelayInterfaceKey);
        MountTools.doMergeToConfig(input.getNeId(), iid, dhcpRelayInterfaceBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<DeleteDhcpRelayInterfacesOutput>> deleteDhcpRelayInterfaces(DeleteDhcpRelayInterfacesInput input) {
        InstanceIdentifier<DhcpRelayInterface> iid = create(DhcpRelay.class).child(DhcpRelayInterfaces.class)
                .child(DhcpRelayInterface.class, new DhcpRelayInterfaceKey(input.getName()));
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<SetDhcpRelayEnableOutput>> setDhcpRelayEnable(SetDhcpRelayEnableInput input) {
        InstanceIdentifier<DhcpRelay> iid = create(DhcpRelay.class);
        DhcpRelayBuilder dhcpRelayBuilder = new DhcpTransformImpl().apiDhcpRelayToDev(input);
        MountTools.putConfig(input.getNeId(), iid, dhcpRelayBuilder.build());
        return RpcResultUtil.success();
    }

}
