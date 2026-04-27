/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.action;

import cn.hutool.core.util.ClassUtil;
import com.optel.tmaster.dc.general.base.exception.manage.ImplClassNotFindException;
import org.opendaylight.yangtools.yang.binding.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * <ul>
 * <li>(设备功能集 -- 根据yang模式，对应其实现功能集
 *          在每个bundle进行初始化时，向功能集中加入对应数据)</li>
 * </ul>
 *
 * @author LWX 2020/1/21 16:25
 */
public class DeviceFunction implements IDeviceFunction, AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(DeviceFunction.class);

    public DeviceFunction() {
        // nothing to do
    }

    /**
     * 南向yang模式与功能集对应关系 key--yang mode; value--功能实现类
     */
    private static final Map<YangMode, Collection<RpcService>> DEVICE_PROVIDER = new HashMap<>();

    @Override
    public void add(YangMode yangMode, RpcService rpcService) {
        Collection<RpcService> rpcServices = DEVICE_PROVIDER.get(yangMode);
        if (Objects.isNull(rpcServices)) {
            rpcServices = new ArrayList<>();
            DEVICE_PROVIDER.put(yangMode, rpcServices);
        }
        rpcServices.add(rpcService);
    }

    @Override
    public void del(YangMode yangMode) {
        DEVICE_PROVIDER.remove(yangMode);
    }

    /**
     * 根据yang mode 获取实现类
     *
     * @param yangMode yang mode
     * @param cls      实现类接口 ，必选是RpcService 子类
     * @param <T>      类型
     * @return 实现类
     */
    public static <T extends RpcService> T getService(String yangMode, Class<T> cls) {
        YangMode yang = YangMode.from(yangMode).orElse(null);
        return getService(yang, cls);
    }

    /**
     * 根据yang mode 获取实现类
     *
     * @param yangMode yang mode
     * @param cls      实现类接口 ，必选是RpcService 子类
     * @param <T>      类型
     * @return 实现类
     */
    public static <T extends RpcService> T getService(YangMode yangMode, Class<T> cls) {
        if (Objects.isNull(yangMode)) {
            String errorMsg = String.format("yangMode is null, class:%s", cls.getName());
            LOG.error("can not find implement;{}", errorMsg);
            throw new ImplClassNotFindException("", errorMsg);
        }
        Collection<RpcService> rpcServices = DEVICE_PROVIDER.get(yangMode);
        Optional<RpcService> rpcServiceOptional = rpcServices.stream().filter(e -> ClassUtil.isAssignable(cls, e.getClass())).findAny();
        if (rpcServiceOptional.isEmpty()) {
            String errorMsg = String.format("yangMode:%s, class:%s", yangMode.getMode(), cls.getName());
            LOG.error("can not find implement;{}", errorMsg);
            throw new ImplClassNotFindException(yangMode.getMode(), errorMsg);
        }
        return (T) rpcServiceOptional.get();
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     */
    @Override
    public void close() throws Exception {
        DEVICE_PROVIDER.clear();
    }

}
