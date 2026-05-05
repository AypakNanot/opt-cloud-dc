/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.configuration;

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
 * 所有properties类型的超接口
 * 2019/9/9 16:30:24
 * @author LiH
 */
public interface IPropertiesConfiguration extends IConfiguration{

    /**
     * 清空
     */
    void clear();

    /**
     * 删除某个属性
     * @param key key
     */
    void clearPropertyDirect(String key);

    /**
     * 添加属性
     * @param key key
     * @param value value
     */
    void addPropertyDirect(String key, Object value);

    /**
     * 获取属性值
     * @param key key
     * @return value
     */
    Object getProperty(String key);

    /**
     * 设置属性
     * @param key key
     * @param value value
     */
    void setProperty(String key, Object value);

    /**
     * 是否为空
     * @return true：空，false：不为空
     */
    boolean isEmpty();

    /**
     * 是否包含某个key的
     * @param key key
     * @return true：包含，false：未包含
     */
    boolean containsKey(String key);

}
