/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.action;

import java.util.Collection;

/**
 * <pre>
 * opt-cloud-dc - IDeviceService
 * </pre>
 * 设备服务，所有跟设备相关的服务多实现此接口
 * <pre>
 * date              time          author
 * ───────────────────────────────────────
 * 2024-10-19 0019   9:43:26      LiHua
 * Copyright (c) 2024, H-OPTEL All Rights Reserved.
 * </pre>
 *
 * @author LiHua
 * @version V2.0.0
 */
public interface IDeviceService {

    /**
     * 当前服务支持的Yang Mode类型
     *
     * @return 支持的类型
     */
    Collection<YangMode> supportDevice();

}