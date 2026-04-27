/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc;

import com.optel.tmaster.dc.general.base.action.BaseDeviceProvider;
import com.optel.tmaster.dc.general.base.action.IDeviceRpcProviderService;
import com.optel.tmaster.dc.general.base.action.IDeviceService;
import com.optel.tmaster.dc.general.base.action.YangType;

import java.util.List;


/**
 * ClassName: CtcWdmProvider
 * <ul>
 * <li>(blueprint初始化加载模块)</li>
 * </ul>
 *
 * @author LWX 2022年2月8日上午11:14:29
 */
public class CtcWdmProvider extends BaseDeviceProvider {

    public CtcWdmProvider(final IDeviceRpcProviderService deviceRpcProviderService, List<IDeviceService> deviceServices) {
        super(deviceRpcProviderService, YangType.WDM_CTC, deviceServices);
    }
}