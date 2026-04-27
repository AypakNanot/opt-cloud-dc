/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.exception.manage;

/**
 * <ul>
 * <li>(参数错误信息类)</li>
 * </ul>
 *
 * @author LWX 2020/6/20 11:29
 */
public class ParameterError {
    public enum ParameterErrorType{
        /**
         * 参数格式错误
         */
        FORMATERROR,
        /**
         * 参数已被使用
         */
        INUSE,
        /**
         * 对象不存在
         */
        NOTEXIST,
        /**
         * 参数无效
         */
        INVALID

    }

    /**
     * 错误类型
     */
    private ParameterErrorType parameterErrorType;
    /**
     * 参数名称
     */
    private String parameter;

    public ParameterError(String parameter, ParameterErrorType parameterErrorType){
        this.parameter = parameter;
        this.parameterErrorType = parameterErrorType;
    }

    public String getParameter() {
        return parameter;
    }

    public ParameterErrorType getParameterErrorType() {
        return parameterErrorType;
    }

}
