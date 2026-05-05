/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform;

import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.AbstractCtcTransformer;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.CommonTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.ModifyUserPasswordNeInput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ModifyUserPasswordInputBuilder;

/**
 * 设备用户管理 转换
 * @author Quan Jingyuan
 * @since 2021/10/14
 **/
public class DeviceUserManageTransformImpl extends AbstractCtcTransformer implements CommonTransform {
    public ModifyUserPasswordInputBuilder apiModifyUserPasswordToDev(ModifyUserPasswordNeInput input) {
        ModifyUserPasswordInputBuilder inputBuilder = new ModifyUserPasswordInputBuilder();
        if(input==null){
            return inputBuilder;
        }
        inputBuilder.setNewPassword(input.getNewPassword());
        inputBuilder.setOldPassword(input.getOldPassword());
        inputBuilder.setUserName(input.getUserName());
        return inputBuilder;
    }
}
