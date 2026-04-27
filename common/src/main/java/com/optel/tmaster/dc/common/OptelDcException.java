/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.common;

import com.google.common.collect.ImmutableList;
import org.opendaylight.yangtools.yang.common.ErrorType;
import org.opendaylight.yangtools.yang.common.OperationFailedException;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 
 * ClassName: OptelDcException
 * <ul>
 * <li>(DC异常基类。用于DC中抛出的异常)</li>
 * </ul>
 * @author LWX 2019年9月24日下午1:51:36
 *
 */
public class OptelDcException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private List<RpcError> rpcErrors = new ArrayList<>();


    public OptelDcException(String message){
        this(message, null, (RpcError[]) null);
    }

    public OptelDcException( String message, RpcError... errors) {
        this(message, null, errors);
    }

    public OptelDcException( String message, Throwable cause, RpcError... errors){
        super(message, cause);

        if (errors != null && errors.length > 0) {
            rpcErrors = ImmutableList.copyOf( Arrays.asList( errors ) );
        } else {
            // Add a default RpcError.
            if(cause != null){
                OperationFailedException exception = null;
                if(cause instanceof OperationFailedException){
                    exception = (OperationFailedException) cause;
                }
                else if(cause.getCause() != null){
                    if(cause.getCause() instanceof OperationFailedException){
                        exception = (OperationFailedException) cause.getCause() ;
                    }
                }
                if(exception != null){
                    rpcErrors = exception.getErrorList();
                }
            }

            if(rpcErrors.isEmpty()){
                rpcErrors = ImmutableList.of(RpcResultBuilder.newError(ErrorType.APPLICATION, null,
                        getMessage(), getApplicationTag(), null, getCause()));
            }
        }
    }

    public List<RpcError> getRpcErrors() {
        return rpcErrors;
    }

    public String getApplicationTag(){
        return null;
    }
}
