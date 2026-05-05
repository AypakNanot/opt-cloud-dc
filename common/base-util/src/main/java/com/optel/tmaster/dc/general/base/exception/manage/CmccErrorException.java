/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.exception.manage;

import com.optel.tmaster.dc.common.OptelDcException;
import com.optel.tmaster.dc.general.base.exception.ErrorMessage;
import com.optel.tmaster.dc.general.base.exception.ExceptionApplicationTag;
import com.optel.tmaster.dc.general.base.util.RpcErrorUtil;
import org.opendaylight.yangtools.yang.common.RpcError;

/**
 * <ul>
 * <li>(中移动定义的错误)</li>
 * </ul>
 *
 * @author LWX 2020/6/19 15:44
 */
public class CmccErrorException extends OptelDcException {

    public CmccErrorException(ErrorMessage errorMessage){
        this(RpcErrorUtil.getRpcError(errorMessage.errorMsg, errorMessage.errorDesc, ExceptionApplicationTag.CMCC_ERROR));
    }

    private CmccErrorException(RpcError error){
        super("CmccError",error);
    }

    @Override
    public String getApplicationTag(){
        return ExceptionApplicationTag.CMCC_ERROR;
    }

    public static CmccErrorException blankFieldErrorException(String field){
        String errorMsg = "The %s field value cannot be blank";
        errorMsg = String.format(errorMsg, field);
        String errorDesc = "%s 字段为空";
        errorDesc = String.format(errorDesc, field);
        return new CmccErrorException(RpcErrorUtil.getRpcError(errorMsg, errorDesc, ExceptionApplicationTag.CMCC_ERROR));
    }

}
