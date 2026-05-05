/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.otn.rest.api.services;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import org.opendaylight.mdsal.binding.api.RpcProviderService;
import org.opendaylight.yangtools.concepts.ObjectRegistration;
import org.opendaylight.yangtools.yang.binding.RpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Objects;

/**
 * <pre>
 * opt-cloud-dc - ProxyServiceGenerator
 * </pre>
 * REST API 实现类 代理服务生成器, 自动根据接口生成实现类，并注册到 RPC 中供REST 远程调用
 * <pre>
 * date              time          author
 * ───────────────────────────────────────
 * 2024-10-17 0017   10:23:43      LiHua
 * Copyright (c) 2024, H-OPTEL All Rights Reserved.
 * </pre>
 *
 * @author LiHua
 * @version V2.0.0
 */
public class ProxyServiceGenerator implements AutoCloseable {
    private static final Logger LOG = LoggerFactory.getLogger(ProxyServiceGenerator.class);
    /**
     * RPC Rpc服务器注册器
     */
    private final RpcProviderService rpcProviderService;
    private List<ObjectRegistration> ors;

    public ProxyServiceGenerator(RpcProviderService rpcProviderService) {
        this.rpcProviderService = rpcProviderService;
    }

    public void init() {
        LOG.info("ProxyServiceGenerator scan all rest services proxy Implementation start...");
        List<String> services = ProxyServiceUtil.getAllProxyService(this.getClass());
        LOG.info("scan rpc service count:{}", services.size());
        ors = services.stream()
                .map(e -> ProxyServiceUtil.getProxyRpcImpl(this.getClass(), e)).
                filter(Objects::nonNull)
                .map(d -> {
                    if (MapUtil.isEmpty(d)) {
                        return null;
                    }
                    try {
                        Class cls = (Class) d.get("type");
                        RpcService o = (RpcService) d.get("implementation");
                        ObjectRegistration objectRegistration = rpcProviderService.registerRpcImplementation(cls, o);
                        LOG.info("registerRpcImplementation {} ok.", d);
                        return objectRegistration;
                    } catch (Exception e) {
                        LOG.info("registerRpcImplementation {} fail.", d, e);
                    }
                    return null;
                })
                .filter(Objects::nonNull)
                .toList();
        LOG.info("ProxyServiceGenerator scan all rest services proxy Implementation end...,services count:{} .", ors.size());
    }

    /**
     * Closes this resource, relinquishing any underlying resources.
     * This method is invoked automatically on objects managed by the
     * {@code try}-with-resources statement.
     */
    @Override
    public void close() throws Exception {
        if (CollUtil.isNotEmpty(ors)) {
            for (ObjectRegistration or : ors) {
                or.close();
            }
        }
    }
}