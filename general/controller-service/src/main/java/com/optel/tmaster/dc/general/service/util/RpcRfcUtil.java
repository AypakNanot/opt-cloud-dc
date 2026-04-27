/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.service.util;


import java.io.Serializable;
import java.util.Objects;

/**
 * <pre>
 *    o o o o o o     p p p p p p   t t t t t t t    e e e e e e    l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p p p p p        t t         e e e e e e    l l
 *    o o     o o     p p                t t         e e            l l
 *    o o     o o     p p                t t         e e            l l
 *    o o     o o     p p                t t         e e            l l
 *    o o o o o o     p p                t t         e e e e e e    l l l l l l
 *
 *              LiHua       佛主保佑       永无BUG
 * </pre>
 * <p>
 * 2020/5/5 14:02:33
 *
 * @author LiH
 * @version V1.0.0
 */
public class RpcRfcUtil {
    /**
     * 错误的设计 , 无奈的办法，兼容之前
     */
    private static NotifyTaskImpl notifyTaskImpl = null;

    /**
     * 不应该把下面的callInvoke 写成 static 的, 错误的设计. 2023-12-19 LiHua 使用的地方应该采用 ref 引用的方式进行操作
     *
     * @param notifyTaskImpl notify
     */
    public RpcRfcUtil(final NotifyTaskImpl notifyTaskImpl) {
        RpcRfcUtil.notifyTaskImpl = notifyTaskImpl;
    }

    /**
     * 进行调用, 代理调用到 rpc服务中
     *
     * @param interfaceName  调用的接口全路径
     * @param method         调用的方法名称
     * @param parameterTypes 输入参数类型
     * @param args           输入参数
     */
    public static void callInvoke(String interfaceName, String method, String[] parameterTypes, Object[] args) {
        RpcParameter rpcParameter = new RpcParameter(interfaceName, method, parameterTypes, args);
        notifyTaskImpl.put(rpcParameter);
    }

    /**
     * 进行调用, 代理调用到 rpc服务中, 并返回结果
     *
     * @param interfaceName  调用的接口全路径
     * @param method         调用的方法名称
     * @param parameterTypes 输入参数类型
     * @param args           输入参数
     * @param clazz          结果的类型
     * @param <T>            泛型
     * @return               结果
     */
    public static <T extends Serializable> T callInvoke(String interfaceName, String method, String[] parameterTypes, Object[] args, Class<T> clazz) {
        return notifyTaskImpl.runTask(new RpcParameter(interfaceName, method, parameterTypes, args), res -> {
            if (Objects.equals(clazz, String.class)) {
                return (T) res;
            }
            return null;
        });
    }

    /**
     * 消息推送
     *
     * @param interfaceName  调用的接口全路径
     * @param method         调用的方法名称
     * @param parameterTypes 输入参数类型
     * @param args           输入参数
     */
    public void notify(String interfaceName, String method, String[] parameterTypes, Object[] args) {
        RpcParameter rpcParameter = new RpcParameter(interfaceName, method, parameterTypes, args);
        notify(rpcParameter);
    }

    /**
     * 消息推送
     *
     * @param rpc rpc 参数
     */
    public void notify(RpcParameter rpc) {
        notifyTaskImpl.put(rpc);
    }
}
