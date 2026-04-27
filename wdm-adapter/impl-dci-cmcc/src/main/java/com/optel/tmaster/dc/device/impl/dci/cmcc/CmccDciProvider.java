/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc;

import com.optel.tmaster.dc.general.base.action.BaseDeviceProvider;
import com.optel.tmaster.dc.general.base.action.IDeviceRpcProviderService;
import com.optel.tmaster.dc.general.base.action.IDeviceService;
import com.optel.tmaster.dc.general.base.action.YangType;

import java.util.List;


/**
 * blueprint初始化加载模块
 *
 * @author SunYu 2023/7/25
 */
public class CmccDciProvider extends BaseDeviceProvider {

    public CmccDciProvider(final IDeviceRpcProviderService deviceRpcProviderService, List<IDeviceService> deviceServices) {
        super(deviceRpcProviderService, YangType.WDM_CMCC, deviceServices);
    }

}