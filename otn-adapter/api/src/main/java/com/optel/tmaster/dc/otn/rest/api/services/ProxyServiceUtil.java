/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.otn.rest.api.services;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import com.optel.tmaster.dc.general.base.action.DeviceClassFactory;
import javassist.ClassClassPath;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yangtools.yang.binding.RpcService;
import org.osgi.framework.Bundle;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Predicate;

/**
 * <pre>
 * opt-cloud-dc - ProxyServiceUtil
 * </pre>
 * 代理服务工具类
 * <pre>
 * date              time          author
 * ───────────────────────────────────────
 * 2024-10-17 0017   10:04:47      LiHua
 * Copyright (c) 2024, H-OPTEL All Rights Reserved.
 * </pre>
 *
 * @author LiHua
 * @version V2.0.0
 */
public class ProxyServiceUtil {
    private static final Logger LOG = LoggerFactory.getLogger(ProxyServiceUtil.class);

    private ProxyServiceUtil() {
    }

    private static final ClassPool CLASS_POOL = ClassPool.getDefault();

    static {
        CLASS_POOL.appendClassPath(new ClassClassPath(ProxyServiceUtil.class));
        CLASS_POOL.appendClassPath(new ClassClassPath(DeviceClassFactory.class));
    }

    /**
     * 根据cls 获取 当前class的bundle中代理服务的接口集合。
     *
     * @param cls cls
     * @return 代理接口集合，这里返回的只是经过了部分过滤
     */
    public static List<String> getAllProxyService(@NonNull final Class<?> cls, @NonNull final String packagePrefix, @NonNull final Predicate<String> filter) {
        Bundle bundle = FrameworkUtil.getBundle(cls);
        BundleWiring bundleWiring = bundle.adapt(BundleWiring.class);
        LOG.debug("scan service by package prefix:{}", packagePrefix);
        LOG.debug("class:{} bundle id:{}", cls.getName(), bundle.getBundleId());
        Collection<String> clss = bundleWiring.listResources("", "*Service.class", BundleWiring.LISTRESOURCES_RECURSE);
        return clss.stream()
                .filter(e -> e.startsWith(packagePrefix))
                .filter(filter)
                .map(e -> e.replace("/", "."))
                .toList();
    }

    public static List<String> getAllProxyService(@NonNull Class<?> cls) {
        return getAllProxyService(cls, ex -> (ex.contains("/optel/devconf/opt/") || ex.contains("/optel/device/opt/")));
    }

    public static List<String> getAllProxyService(@NonNull Class<?> cls, @NonNull Predicate<String> filter) {
        return getAllProxyService(cls, "org/opendaylight/yang/gen/v1/com/optel", filter);
    }

    public static Dict getProxyRpcImpl(@NonNull Class<?> bundleCls, String serverClassName) {
        Dict ins = Dict.create();
        try {
            if (!serverClassName.endsWith(".class")) {
                serverClassName = serverClassName + ".class";
            }
            // 获取类 name，不包含后缀 .class
            String name = serverClassName.substring(0, serverClassName.lastIndexOf("."));
            // 获取类 simpleName 名称
            String simpleName = name.substring(name.lastIndexOf(".") + 1);
            // 所有生成代理实现类加后缀
            String proxyImplName = simpleName + "ProxyImpl";
            CLASS_POOL.appendClassPath(name);
            LOG.debug("Class pool append class path:{}", name);
            String serverImplName = "com.optel.tmaster.dc.otn.rest.api.services." + proxyImplName;
            CtClass serverImpl = CLASS_POOL.makeClass(serverImplName);
            LOG.debug("Generator service impl class name:{}", serverImplName);
            CtClass ctClass = CLASS_POOL.getCtClass(name);
            serverImpl.addInterface(ctClass);
            LOG.debug("Service impl :{}, add interface:{}", serverImplName, name);
            Class<?> interfaceCls = Class.forName(name);
            List<String> methodBody = getMethodBody(interfaceCls);
            for (String bo : methodBody) {
                CtMethod ctMethod = CtMethod.make(bo, serverImpl);
                serverImpl.addMethod(ctMethod);
                LOG.debug("Service impl :{}, add method name:{}", serverImplName, ctMethod.getName());
            }
            Class<?> serverCls = serverImpl.toClass(bundleCls);
            if (!ClassUtil.isAssignable(RpcService.class, serverCls)) {
                LOG.warn("Service :{} is not RpcService son. lost this service.", serverCls.getName());
                ctClass.detach();
                serverImpl.detach();
                return ins;
            }
            RpcService obj = (RpcService) serverCls.getDeclaredConstructor().newInstance();
            LOG.info("Generator service impl :{} ,interface :{} ok.", serverImplName, name);
            ins.set("type", interfaceCls);
            ins.set("implementation", obj);
        } catch (Exception e) {
            LOG.error("Generator Service {} Impl is fail.", serverClassName, e);
        }
        return ins;
    }

    /**
     * 根据cls 接口生成 接口下所有方法的方法体内容。
     *
     * @param interfaceCls 接口，必须实现了RpcService
     * @return 接口实现所有方法体
     */
    public static List<String> getMethodBody(Class<?> interfaceCls) {
        List<String> bs = new ArrayList<>();
        Method[] publicMethods = ClassUtil.getPublicMethods(interfaceCls);
        for (Method method : publicMethods) {
            String methodName = method.getName();
            Parameter[] parameters = method.getParameters();
            String inputTypeName = parameters[0].getType().getName();
            String interfaceClsName = interfaceCls.getName();
            Dict vs = Dict.create()
                    .set("methodName", methodName)
                    .set("inputTypeName", inputTypeName)
                    .set("interfaceCls", interfaceClsName);
            LOG.debug("create method info:{}", vs);
            String ms = StrUtil.format("""

                    public com.google.common.util.concurrent.ListenableFuture {methodName}({inputTypeName} input){
                        return (({interfaceCls})com.optel.tmaster.dc.general.base.action.DeviceClassFactory.getDeviceExecutor(input.getYangMode(), {interfaceCls}.class)).{methodName}(input);
                    }
                    """, vs);
            bs.add(ms);
            LOG.trace("Generator interface name:{} method name:{},method body:\n{}", interfaceClsName, methodName, ms);
        }
        return bs;
    }

}