/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.action;


import org.opendaylight.yangtools.yang.binding.RpcService;

/**
 * <ul>
 * <li>(工厂类)</li>
 * </ul>
 *
 * @author LWX 2020/1/21 14:36
 */
public class DeviceClassFactory {
    private DeviceClassFactory() {
    }

    /**
     * 根据基本设备信息得到具体实现类
     * 注意本方法在 自动化调用处进行调用 com.optel.tmaster.dc.otn.rest.api.services.ProxyServiceUtil
     *
     * @param yangMode Yang Mode
     * @param cls      功能定义 接口类
     * @param <T>      功能实现类
     * @return 功能实现类对象, 当无时，抛异常
     */
    public static <T extends RpcService> T getDeviceExecutor(String yangMode, Class<T> cls) {
        //yang文件类型
        return DeviceFunction.getService(yangMode, cls);
    }

    public static <T extends RpcService> T getDeviceExecutor(YangMode yangMode, Class<T> cls) {
        //yang文件类型
        return DeviceFunction.getService(yangMode, cls);
    }
}

