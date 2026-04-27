/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.exception.manage;

import com.optel.tmaster.dc.common.OptelDcException;
import com.optel.tmaster.dc.general.base.exception.ExceptionApplicationTag;
import com.optel.tmaster.dc.general.base.util.RpcErrorUtil;
import org.opendaylight.yangtools.yang.common.RpcError;

import java.util.List;

/**
 * <ul>
 * <li>(参数错误异常)</li>
 * </ul>
 *
 * @author LWX 2020/6/19 15:44
 */
public class ParameterException extends OptelDcException {

    public ParameterException(ParameterError errors) {
        super("ParameterError", getRpcErrors(errors));
    }

    public ParameterException(List<ParameterError> errors) {
        super("ParameterError", getRpcErrors(errors.toArray(new ParameterError[0])));
    }

    private static RpcError[]  getRpcErrors(ParameterError ... errors){
        if(errors == null){
            return null;
        }
        RpcError[] rpcErrors = new RpcError[errors.length];
        for(int i=0; i<errors.length; i++){
            rpcErrors[i] = RpcErrorUtil.getRpcError(errors[i].getParameter(), errors[i].getParameterErrorType().toString(), ExceptionApplicationTag.PARAMETER_ERROR);
        }
        return rpcErrors;
    }

    @Override
    public String getApplicationTag(){
        return ExceptionApplicationTag.PARAMETER_ERROR;
    }

}
