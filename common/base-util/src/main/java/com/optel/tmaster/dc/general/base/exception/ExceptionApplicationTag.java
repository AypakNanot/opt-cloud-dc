/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.exception;

/**
 * <ul>
 * <li>(定义错误码)</li>
 * </ul>
 *
 * @author LWX 2020/6/19 18:10
 */
public interface ExceptionApplicationTag {

    /**
     * 操作dataStore异常
     */
     String OPERATE_DATASTORE_ERROR = "1";
    /**
     * 设备通信异常
     */
    String DEVICE_COMMICATION_ERROR = "2";
    /**
     * 下发设备失败
     */
     String DEVICE_OPERATION_FAILED = "3";
    /**
     * 中移动定义的错误
     */
    String CMCC_ERROR = "4";
    /**
     * 暂不支持
     */
     String NOT_SUPPORTED = "5";
    /**
     * 设备对象不存在
     */
    String NE_OBJECT_NOT_EXIST = "6";
    /**
     * 参数错误
     */
     String PARAMETER_ERROR = "7";
    /**
     * 未找到实现类
     */
    String IMPL_NOT_FIND = "8";

    /**
     * 设备不支持的功能
     */
    String DEVICE_NOT_SUPPORTED = "9";

    /**
     * 枚举值转换错误
     */
    String NO_MATCH_ENUM_VALUE = "10";


}
