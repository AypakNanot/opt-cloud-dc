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
import org.opendaylight.yangtools.yang.common.RpcError;

/**
 * <ul>
 * <li>(操作数据库异常)</li>
 * </ul>
 *
 * @author LWX 2020/1/19 10:38
 */
public class DataStoreOperateException extends OptelDcException{

    public DataStoreOperateException(String message, RpcError... errors) {
        super(message, errors);
    }

    public DataStoreOperateException(String message, Throwable cause, RpcError... errors){
        super(message, cause, errors);
    }

    @Override
    public String getApplicationTag() {
        return ExceptionApplicationTag.OPERATE_DATASTORE_ERROR;
    }
}
