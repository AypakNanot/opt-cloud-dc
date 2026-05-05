/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciInterfaceImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.config.DciInterfaceTransform;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci._interface.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ip.rev230426.Subinterface1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ip.rev230426.ipv4.top.Ipv4;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ip.rev230426.ipv4.top.ipv4.Addresses;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ip.rev230426.ipv4.top.ipv4.addresses.Address;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ip.rev230426.ipv4.top.ipv4.addresses.AddressKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.Interfaces;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.interfaces.Interface;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.interfaces.InterfaceKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.subinterfaces.top.Subinterfaces;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.subinterfaces.top.subinterfaces.Subinterface;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.Ipv4Address;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;


/**
 * ClassName: CtcOptDciInterfaceServiceImpl
 * <ul>
 * <li>中电信 DCI 数据接口 实现类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2022/3/9 15:10
 */
public class CmccOptDciInterfaceServiceImpl extends BaseDciInterfaceImpl implements IDeviceServiceWdmCmcc {
    private static final String IS_OPT_TEST = "isOptTest";

    /**
     * Invoke {@code get-client-eth-interface} RPC.
     *
     * <pre>
     *     <code>
     *         查询客户侧以太网接口
     *     </code>
     * </pre>
     *
     * @param input of {@code get-client-eth-interface}
     * @return output of {@code get-client-eth-interface}
     */
    @Override
    public ListenableFuture<RpcResult<GetInterfaceOutput>> getInterface(GetInterfaceInput input) {
        Interfaces interfaces = null;
        String property = System.getProperty(IS_OPT_TEST, Boolean.FALSE.toString());
        if (Boolean.valueOf(property)) {
//            interfaces =ComponentsTestUtil.getAllInterfaces();
        } else {
            interfaces = getInterfaces(input.getNeId(), input.getName());
        }
        Interfaces filterInterfaces = filterInterfaceByType(interfaces, input.getType());
        return RpcResultUtil.success(new DciInterfaceTransform().devGetInterfaceOutputToApi(filterInterfaces));

    }


    /**
     * Invoke {@code set-client-eth-interface} RPC.
     *
     * <pre>
     *     <code>
     *         设置客户侧以太网接口
     *     </code>
     * </pre>
     *
     * @param input of {@code set-client-eth-interface}
     * @return output of {@code set-client-eth-interface}
     */
    @Override
    public ListenableFuture<RpcResult<SetClientEthInterfaceOutput>> setClientEthInterface(SetClientEthInterfaceInput input) {
        @NonNull KeyedInstanceIdentifier<Interface, InterfaceKey> child = create(Interfaces.class)
                .child(Interface.class, new InterfaceKey(input.getName()));
//                .augmentation(Interface1.class)
//                .child(Ethernet.class)
//                .child(Config.class)
//                .augmentation(Config1.class);
        MountTools.doMergeToConfig(input.getNeId(), child, new DciInterfaceTransform().apiSetClientEthInterfaceInputToDev(input));
        return RpcResultUtil.success();
    }

    /**
     * Invoke {@code set-dcn-interface} RPC.
     *
     * <pre>
     *     <code>
     *         DCN配置（配置NMInterface，OSCInterface）
     *     </code>
     * </pre>
     *
     * @param input of {@code set-dcn-interface}
     * @return output of {@code set-dcn-interface}
     */
    @Override
    public ListenableFuture<RpcResult<SetDcnInterfaceOutput>> setDcnInterface(SetDcnInterfaceInput input) {
        @NonNull InstanceIdentifier<Address> child = create(Interfaces.class)
                .child(Interface.class, new InterfaceKey(input.getName()))
                .child(Subinterfaces.class)
                .child(Subinterface.class)
                .augmentation(Subinterface1.class)
                .child(Ipv4.class)
                .child(Addresses.class)
                .child(Address.class, new AddressKey(new Ipv4Address(input.getIp().getValue())));
        @NonNull InstanceIdentifier<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.interfaces._interface.Config> configIid = create(Interfaces.class)
                .child(Interface.class, new InterfaceKey(input.getName()))
                .child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.interfaces._interface.Config.class);
        DciInterfaceTransform dciInterfaceTransform = new DciInterfaceTransform();
        if (input.getOspfEnabled() != null || input.getDhcpEnabled() != null) {
            MountTools.doMergeToConfig(input.getNeId(), configIid, dciInterfaceTransform.apiToConfigDev(input));
        }
        MountTools.doMergeToConfig(input.getNeId(), child, dciInterfaceTransform.apiToAddressDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<SetNeighborIpOutput>> setNeighborIp(SetNeighborIpInput input) {
        //极简OTN没有 邻居IP
//        OpenconfigRpcService rpcService = MountTools.getRpcService(input.getNeId(), OpenconfigRpcService.class);
//        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.SetNeighborIpOutput>> rpcResultListenableFuture = rpcService.setNeighborIp(new DciInterfaceTransform().apiToSetNeighborIpInputDev(input));
//        return RpcResultUtil.buildFutureResult(rpcResultListenableFuture,null,e -> DciUtils.getOcRpcResult(e.getResult()));
        return null;
    }
}
