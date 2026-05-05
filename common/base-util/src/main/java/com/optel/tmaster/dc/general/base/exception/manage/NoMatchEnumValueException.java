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
 * NoMatchEnumValueException
 * 未转换出匹配的枚举值异常
 * date       time        author
 * ─────────────────────────────
 * 2021/10/11   13:45      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class NoMatchEnumValueException extends OptelDcException {

    public NoMatchEnumValueException(String message) {
        super(message);
    }

    public NoMatchEnumValueException(String className, String value) {
        super(String.format("EnumType: %s; Invalid value: %s", className, value));
    }

    /**
     * 转换时无匹配的枚举值异常信息
     *
     * @param data 枚举数据信息
     * @return 异常
     */
    public static NoMatchEnumValueException getNoMatchEnumValueException(Object data) {
        if (data == null) {
            return new NoMatchEnumValueException("data is null");
        }
        if (data instanceof Enum) {
            return new NoMatchEnumValueException(data.getClass().getSimpleName(), ((Enum<?>) data).name());
        }
        return new NoMatchEnumValueException(data.getClass().getSimpleName(), data.toString());
    }

    @Override
    public String getApplicationTag() {
        return ExceptionApplicationTag.NO_MATCH_ENUM_VALUE;
    }

}
