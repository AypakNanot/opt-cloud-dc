/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.util;

/**
 * ClassName: CommomErrorCodeUtil
 * <ul>
 * <li>(公共工具类)</li>
 * </ul>
 * @author LWX 2019年9月30日下午3:21:42
 *
 */
public class CommonUtil {

    public static Integer getIntegerValue(Number value){
        if(value == null){
            return null;
        }
        return value.intValue();
    }

    public static Long getLongValue(Number value){
        if(value == null){
            return null;
        }
        return value.longValue();
    }
}
