/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.action;

import org.opendaylight.yangtools.yang.binding.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <pre>
 * opt-cloud-dc - DeviceRpcProviderServiceImpl
 * </pre>
 * 设置rpc调用实现 注册接受器
 * <pre>
 * date              time          author
 * ───────────────────────────────────────
 * 2024-10-19 0019   9:39:41      LiHua
 * Copyright (c) 2024, H-OPTEL All Rights Reserved.
 * </pre>
 *
 * @author LiHua
 * @version V2.0.0
 */
public class DeviceRpcProviderServiceImpl implements IDeviceRpcProviderService {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceRpcProviderServiceImpl.class);
    private final DeviceFunction deviceFunction;

    public DeviceRpcProviderServiceImpl(DeviceFunction deviceFunction) {
        this.deviceFunction = deviceFunction;
    }

    @Override
    public void registerDeviceRpcProvider(List<IDeviceService> deviceServices) {
        for (IDeviceService deviceService : deviceServices) {
            Collection<YangMode> yangModes = deviceService.supportDevice();
            Class<? extends IDeviceService> deviceCls = deviceService.getClass();
            if (!(deviceService instanceof RpcService)) {
                LOG.warn("unregister name:{}, because not instanceof RpcService.", deviceCls.getName());
                continue;
            }
            yangModes.forEach(e -> {
                deviceFunction.add(e, (RpcService) deviceService);
                LOG.info("registerDeviceRpcProvider YANG_MODE:{}, impl:{}", e, deviceService.getClass());
            });
        }
    }

    /**
     * 取消注册
     *
     * @param yangType 产品线 模式
     */
    @Override
    public void unregisterDeviceRpcProvider(YangType yangType) {
        Collection<YangMode> yangModes = YangMode.typeOf(yangType);
        yangModes.forEach(deviceFunction::del);
    }
}