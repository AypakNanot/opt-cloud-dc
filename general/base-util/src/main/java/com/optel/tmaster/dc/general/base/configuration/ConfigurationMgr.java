/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.configuration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

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
 *
 *          l       i    h     h    u     u       a a
 *          l            h     h    u     u     a     a
 *          l       i    h h h h    u     u     a     a
 *          l       i    h     h    u     u     a     a
 *          l l l   i    h     h      u u  u      a a   a
 *
 *              LiHua       佛主保佑       永无bBUG
 * </pre>
 * 配置缓存管理
 * 2019/9/9 16:30:24
 * @author LiH
 */
public final class ConfigurationMgr {
    private static final Logger LOG = LoggerFactory.getLogger("ConfigurationMgr");

    private ConfigurationMgr(){}

    /**
     * 缓存所有的配置文件信息
     */
    private static final ConcurrentHashMap<Class<?>,IConfiguration> CONFIG_CACHES = new ConcurrentHashMap<>();

    /**
     * 添加或者修改配置信息
     * @param configuration 配置信息
     */
    protected static void addAndUpdate(IConfiguration configuration){
        Class<? extends IConfiguration> classes = configuration.getClass();
        CONFIG_CACHES.put(classes,configuration);
        LOG.info("ConfigurationMgr load :{} succ.",classes.getTypeName());
    }

    /**
     * 获取指定配置信息
     * @param classes 需要获取配置信息的class
     * @return 配置信息
     */
    protected static IConfiguration getConfig(Class<?> classes,boolean iscache){
        IConfiguration config = CONFIG_CACHES.get(classes);
        if(Objects.isNull(config)){
            try {
                Object obj = classes.newInstance();
                if(obj instanceof IPropertiesConfiguration){
                    IPropertiesConfiguration cf = (IPropertiesConfiguration) obj;
                    PropertiesLoader.loadProperties(cf,iscache);
                    config = cf;
                }
            } catch (InstantiationException | IllegalAccessException e) {
                LOG.error("Load Configuation is error.",e);
            }
        }
        return config;
    }

    protected static void close(){
        CONFIG_CACHES.clear();
        LOG.info("ConfigurationMgr close.");
    }

    /**
     * 删除缓存
     * @param classes key
     */
    protected static void removeConfig(Class<?> classes) {
        CONFIG_CACHES.remove(classes);
    }
}
