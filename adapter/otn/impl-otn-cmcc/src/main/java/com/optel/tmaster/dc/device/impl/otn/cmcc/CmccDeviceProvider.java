/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc;

import com.optel.tmaster.dc.general.base.action.BaseDeviceProvider;
import com.optel.tmaster.dc.general.base.action.IDeviceRpcProviderService;
import com.optel.tmaster.dc.general.base.action.IDeviceService;
import com.optel.tmaster.dc.general.base.action.YangType;

import java.util.List;


/**
 * <pre>
 * opt-cloud-dc - CmccDeviceProvider
 * </pre>
 * OTN CMCC DeviceProvider
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
public class CmccDeviceProvider extends BaseDeviceProvider {

    public CmccDeviceProvider(final IDeviceRpcProviderService deviceRpcProviderService, List<IDeviceService> deviceServices) {
        super(deviceRpcProviderService, YangType.OTN_CMCC, deviceServices);
    }

}