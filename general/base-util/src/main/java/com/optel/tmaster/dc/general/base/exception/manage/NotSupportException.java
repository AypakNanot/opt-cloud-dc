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
 * <li>(未支持)</li>
 * </ul>
 *
 * @author LWX 2020/6/19 16:20
 */
public class NotSupportException extends OptelDcException {

    public NotSupportException() {
        super("Not Support Yet.");
    }

    @Override
    public String getApplicationTag(){
        return ExceptionApplicationTag.NOT_SUPPORTED;
    }

}
