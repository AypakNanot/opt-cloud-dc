/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.exception.device;

import com.optel.tmaster.dc.common.OptelDcException;
import com.optel.tmaster.dc.general.base.exception.ExceptionApplicationTag;

/**
 * <ul>
 * <li>(设备不支持)</li>
 * </ul>
 *
 * @author LWX 2020/7/24 11:17
 */
public class DeviceNotSupportException extends OptelDcException {

    public DeviceNotSupportException(String nodeId, String message){
        super(message == null?"nodeId:"+nodeId:message+"nodeId:"+nodeId);
    }

    @Override
    public String getApplicationTag(){
        return ExceptionApplicationTag.DEVICE_NOT_SUPPORTED;
    }

}
