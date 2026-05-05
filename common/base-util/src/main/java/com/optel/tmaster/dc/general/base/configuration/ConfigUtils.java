/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.configuration;

import org.apache.commons.beanutils.BeanUtils;

import java.lang.reflect.InvocationTargetException;

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
 * 提供方便快捷的配置获取
 * 2019/9/9 16:30:24
 * @author LiH
 */
public final class ConfigUtils {

    /**
     * 工具类，不让外部进行创建
     */
    private ConfigUtils(){}

    /**
     * 根据class获取配置信息,支持修改，修改后全局则修改
     * @param classes 对象class
     * @return 配置信息
     */
    public static IConfiguration getConfig(Class<?> classes){
        return getConfig(classes,true);
    }

    /**
     * 加载bundle中资源文件,这个为临时加载不会缓存
     * @param clz 需要使用临时资源文件的class
     * @param path 文件相对resouces路径
     * @return 数据
     */
    public static Properties getResources(Class<?> clz, String path){
        return PropertiesLoader.loadProperties(clz,path);
    }

    /**
     * 根据class获取配置信息,支持修改，修改后全局则修改
     * @param classes 对象class
     * @param iscache 是否需要缓存
     * @return 配置信息
     */
    public static IConfiguration getConfig(Class<?> classes,boolean iscache){
        return ConfigurationMgr.getConfig(classes,iscache);
    }

    /**
     * 根据class获取配置信息，修改对全局无效。
     * @param classes 对象class
     * @return 配置信息
     */
    public static IConfiguration getConfigClone(Class<?> classes){
        IConfiguration config = getConfig(classes,false);
        IConfiguration cf = null;
        try {
            cf = (IConfiguration) BeanUtils.cloneBean(config);
        } catch (IllegalAccessException | InstantiationException | InvocationTargetException | NoSuchMethodException e) {
            e.printStackTrace();
        }
        return cf;
    }

    /**
     * 移除缓存
     * @param classes key
     */
    public static void removeConfig(Class<?> classes){
        ConfigurationMgr.removeConfig(classes);
    }

}
