/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.base.transform;


import com.optel.tmaster.dc.common.ITf;
import com.optel.tmaster.dc.general.base.exception.manage.NoMatchEnumValueException;

/**
 * ITransform
 * 转换基础
 * date       time        author
 * ─────────────────────────────
 * 2021/10/8   15:09      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public interface ITransform extends ITf {
    /**
     * 转换时无匹配的枚举值异常信息
     * @param data 数据信息
     * @return 异常
     */
    default NoMatchEnumValueException getNoMatchEnumValueException(Object data){
        return NoMatchEnumValueException.getNoMatchEnumValueException(data);
    }
}
