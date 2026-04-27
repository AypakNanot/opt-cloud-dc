/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.service.util;

/**
 * <pre>
 * TMaster2000V8.22 - RpcParameter
 * </pre>
 * 参数
 * <pre>
 * date              time          author
 * ───────────────────────────────────────
 * 2023-12-19 0019   15:21:40      LiHua
 * Copyright (c) 2023, H-OPTEL All Rights Reserved.
 * </pre>
 *
 * @author LiHua
 * @version V2.0.0
 */
public class RpcParameter {

    /**
     * 接口名称
     */
    private String interfaceName;
    /**
     * 方法
     */
    private String method;
    /**
     * 参数类型
     */
    private String[] parameterTypes;
    /**
     * 参数值
     */
    private Object[] args;

    public RpcParameter() {
    }

    public RpcParameter(String interfaceName, String method, String[] parameterTypes, Object[] args) {
        this.interfaceName = interfaceName;
        this.method = method;
        this.parameterTypes = parameterTypes;
        this.args = args;
    }

    public String getInterfaceName() {
        return interfaceName;
    }

    public void setInterfaceName(String interfaceName) {
        this.interfaceName = interfaceName;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String[] getParameterTypes() {
        return parameterTypes;
    }

    public void setParameterTypes(String[] parameterTypes) {
        this.parameterTypes = parameterTypes;
    }

    public Object[] getArgs() {
        return args;
    }

    public void setArgs(Object[] args) {
        this.args = args;
    }
}