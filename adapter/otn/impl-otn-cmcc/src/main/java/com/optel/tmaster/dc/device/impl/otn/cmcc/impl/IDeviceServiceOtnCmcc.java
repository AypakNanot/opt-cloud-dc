/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.impl;

import cn.hutool.core.collection.CollUtil;
import com.optel.tmaster.dc.device.impl.base.otn.IDeviceServiceOtn;
import com.optel.tmaster.dc.general.base.action.YangMode;
import com.optel.tmaster.dc.general.base.action.YangType;

import java.util.Collection;

/**
 * <pre>
 * opt-cloud-dc - IDeviceServiceOtnCmcc
 * </pre>
 * OTN CMCC 设备服务
 * <pre>
 * date              time          author
 * ───────────────────────────────────────
 * 2024-10-19 0019   9:55:57      LiHua
 * Copyright (c) 2024, H-OPTEL All Rights Reserved.
 * </pre>
 *
 * @author LiHua
 * @version V2.0.0
 */
public interface IDeviceServiceOtnCmcc extends IDeviceServiceOtn {
    /**
     * 当前服务支持的Yang Mode类型
     *
     * @return 支持的类型
     */
    @Override
    default Collection<YangMode> supportDevice() {
        return CollUtil.newArrayList(YangMode.typeOf(YangType.OTN_CMCC));
    }
}