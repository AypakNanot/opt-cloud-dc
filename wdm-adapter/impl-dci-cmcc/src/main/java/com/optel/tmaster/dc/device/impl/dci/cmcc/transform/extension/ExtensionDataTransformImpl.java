/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.extension;

import cn.hutool.core.util.ObjectUtil;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base.ExtensionDataTransform;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.wdm.extension.rev241211.GetDeviceModeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.wdm.extension.rev241211.GetDeviceModeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.wdm.extension.rev241211.ModifyDeviceModeInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.wdm.extension.rev241211.OperatePortServerInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.wdm.extension.rev241211.OperatePortServerInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.wdm.extension.rev241211.extension.top.DeviceMode;
import org.opendaylight.yang.gen.v1.com.optel.yang.wdm.extension.rev241211.extension.top.DeviceModeBuilder;

/**
 * 极简OTN数据转换器
 * 2024/12/17 9:48
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class ExtensionDataTransformImpl implements ExtensionDataTransform {

    public GetDeviceModeOutput devDeviceModeToApi(DeviceMode deviceMode) {
        GetDeviceModeOutputBuilder builder = new GetDeviceModeOutputBuilder();
        if (ObjectUtil.isNotEmpty(deviceMode)) {
            builder.setAutoConfigMode(deviceMode.getAutoConfigMode());
            builder.setGradedEnergySavingMode(deviceMode.getGradedEnergySavingMode());
        }
        return builder.build();
    }

    public DeviceMode apiDeviceModeToDev(ModifyDeviceModeInput input) {
        DeviceModeBuilder builder = new DeviceModeBuilder();
        if (ObjectUtil.isNotEmpty(input)) {
            builder.setAutoConfigMode(input.getAutoConfigMode());
            builder.setGradedEnergySavingMode(input.getGradedEnergySavingMode());
        }
        return builder.build();
    }

    public OperatePortServerInput apiOperatePortServerInputToDev(org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.wdm.extension.rev241211.OperatePortServerInput input) {
        OperatePortServerInputBuilder operatePortServerInputBuilder = new OperatePortServerInputBuilder();
        operatePortServerInputBuilder.setObjName(input.getObjName());
        operatePortServerInputBuilder.setOperate(input.getOperate());
        return operatePortServerInputBuilder.build();
    }
}
