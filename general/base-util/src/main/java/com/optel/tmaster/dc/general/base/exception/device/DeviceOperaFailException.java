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
import org.opendaylight.yangtools.yang.common.RpcError;

/**
 * <ul>
 * <li>(下发设备失败)</li>
 * </ul>
 *
 * @author LWX 2020/6/19 15:43
 */
public class DeviceOperaFailException  extends OptelDcException {

    public DeviceOperaFailException(){
        super("Device Failed");
    }

    public DeviceOperaFailException(Throwable cause){
        super("Device Failed", cause);
    }

    public DeviceOperaFailException( String message, Throwable cause, RpcError... errors){
        super(message, cause, errors);
    }

    @Override
    public String getApplicationTag(){
        return ExceptionApplicationTag.DEVICE_OPERATION_FAILED;
    }

}
