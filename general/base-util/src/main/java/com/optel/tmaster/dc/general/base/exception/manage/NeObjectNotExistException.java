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

/**
 * <ul>
 * <li>(设备对象不存在)</li>
 * </ul>
 *
 * @author LWX 2020/6/19 15:41
 */
public class NeObjectNotExistException extends OptelDcException {

    public NeObjectNotExistException(String neId){
        super(neId);
    }

    @Override
    public String getApplicationTag(){
        return ExceptionApplicationTag.NE_OBJECT_NOT_EXIST;
    }

}
