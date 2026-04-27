/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.action;

import java.util.List;

/**
 * <pre>
 * opt-cloud-dc - BaseDeviceProvider
 * </pre>
 * Device Provider
 * <pre>
 * date              time          author
 * ───────────────────────────────────────
 * 2024-10-19 0019   16:02:34      LiHua
 * Copyright (c) 2024, H-OPTEL All Rights Reserved.
 * </pre>
 *
 * @author LiHua
 * @version V2.0.0
 */
public class BaseDeviceProvider {

    /**
     * 服务器注册器
     */
    protected final IDeviceRpcProviderService deviceRpcProviderService;
    /**
     * 当前  YangType.OTN_CTC 产品线 的所有实现
     */
    protected final List<IDeviceService> deviceServices;
    protected final YangType yangType;

    public BaseDeviceProvider(final IDeviceRpcProviderService deviceRpcProviderService, YangType yangType, List<IDeviceService> deviceServices) {
        this.deviceRpcProviderService = deviceRpcProviderService;
        this.deviceServices = deviceServices;
        this.yangType = yangType;
    }

    /**
     * Method called when the blueprint container is created.
     */
    public void init() {
        deviceRpcProviderService.registerDeviceRpcProvider(this.deviceServices);
    }

    public void close() {
        deviceRpcProviderService.unregisterDeviceRpcProvider(this.yangType);
    }
}