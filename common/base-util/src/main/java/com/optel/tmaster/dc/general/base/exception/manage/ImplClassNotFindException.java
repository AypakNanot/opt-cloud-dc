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
 * <li>(未找到实现类)</li>
 * </ul>
 *
 * @author LWX 2020/6/20 13:55
 */
public class ImplClassNotFindException extends OptelDcException {

    public ImplClassNotFindException(String yangMode, String cla){
        super("yangMode:"+yangMode+";Class:"+cla);
    }


    public ImplClassNotFindException(String message){
        super(message);
    }

    @Override
    public String getApplicationTag(){
        return ExceptionApplicationTag.IMPL_NOT_FIND;
    }

}
