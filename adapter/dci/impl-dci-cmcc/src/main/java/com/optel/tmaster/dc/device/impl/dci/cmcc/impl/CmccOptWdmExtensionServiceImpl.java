/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.impl;


import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciWdmExtensionServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.extension.ExtensionDataTransformImpl;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.wdm.extension.rev241211.GetDeviceModeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.wdm.extension.rev241211.GetDeviceModeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.wdm.extension.rev241211.ModifyDeviceModeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.wdm.extension.rev241211.ModifyDeviceModeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.wdm.extension.rev241211.OperatePortServerInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.wdm.extension.rev241211.OperatePortServerOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.wdm.extension.rev241211.OptWdmExtensionService;
import org.opendaylight.yang.gen.v1.com.optel.yang.wdm.extension.rev241211.extension.top.DeviceMode;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

/**
 * CMCC 极简OTN扩展服务接口
 * 2024/12/17
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class CmccOptWdmExtensionServiceImpl extends BaseDciWdmExtensionServiceImpl implements IDeviceServiceWdmCmcc {
    private final ExtensionDataTransformImpl extensionDataTransform = new ExtensionDataTransformImpl();

    @Override
    public ListenableFuture<RpcResult<GetDeviceModeOutput>> getDeviceMode(GetDeviceModeInput input) {
        InstanceIdentifier<DeviceMode> iid = create(DeviceMode.class);
        DeviceMode deviceMode = MountTools.queryFromOperational(input.getNeId(), iid);
        return RpcResultUtil.success(extensionDataTransform.devDeviceModeToApi(deviceMode));
    }

    @Override
    public ListenableFuture<RpcResult<ModifyDeviceModeOutput>> modifyDeviceMode(ModifyDeviceModeInput input) {
        InstanceIdentifier<DeviceMode> iid = create(DeviceMode.class);
        MountTools.doMergeToConfig(input.getNeId(), iid, extensionDataTransform.apiDeviceModeToDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<OperatePortServerOutput>> operatePortServer(OperatePortServerInput input) {
        OptWdmExtensionService optWdmExtensionService = MountTools.getRpcService(input.getNeId(), OptWdmExtensionService.class);
        return RpcResultUtil.buildFutureResult(optWdmExtensionService.operatePortServer(extensionDataTransform.apiOperatePortServerInputToDev(input)));
    }
}
