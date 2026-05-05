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
 * opt-cloud-dc - IDeviceRpcProvider
 * </pre>
 * 设备远程服务实现注册器
 * <pre>
 * date              time          author
 * ───────────────────────────────────────
 * 2024-10-19 0019   9:20:55      LiHua
 * Copyright (c) 2024, H-OPTEL All Rights Reserved.
 * </pre>
 *
 * @author LiHua
 * @version V2.0.0
 */
public interface IDeviceRpcProviderService {

    /**
     * 注册服务实现类
     *
     * @param deviceServices 实现类
     */
    void registerDeviceRpcProvider(List<IDeviceService> deviceServices);

    /**
     * 取消注册
     *
     * @param yangType 产品线 模式
     */
    void unregisterDeviceRpcProvider(YangType yangType);
}