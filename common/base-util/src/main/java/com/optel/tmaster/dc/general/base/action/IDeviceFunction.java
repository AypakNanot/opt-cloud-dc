/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.action;

import org.opendaylight.yangtools.yang.binding.RpcService;

/**
 * <pre>
 * opt-cloud-dc - IDeviceFunction
 * </pre>
 * 设备服务管理
 * <pre>
 * date              time          author
 * ───────────────────────────────────────
 * 2024-10-19 0019   14:14:05      LiHua
 * Copyright (c) 2024, H-OPTEL All Rights Reserved.
 * </pre>
 *
 * @author LiHua
 * @version V2.0.0
 */
public interface IDeviceFunction {

    /**
     * 添加实现到缓存
     *
     * @param yangMode   mode
     * @param rpcService impl
     */
    void add(YangMode yangMode, RpcService rpcService);

    /**
     * 删除 缓存集合
     *
     * @param yangMode mode
     */
    void del(YangMode yangMode);
}