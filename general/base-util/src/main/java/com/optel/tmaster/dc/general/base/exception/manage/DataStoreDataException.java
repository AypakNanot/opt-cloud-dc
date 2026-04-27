/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.exception.manage;

import com.optel.tmaster.dc.common.OptelDcException;

/**
 * <ul>
 * <li>(数据库数据异常
 *      对于一些数据出错，比如关联数据不存在等，抛出此异常)</li>
 * </ul>
 *
 * @author LWX 2020/6/20 14:19
 */
public class DataStoreDataException extends OptelDcException {

    public DataStoreDataException() {
        super("Data Error");
    }

}
