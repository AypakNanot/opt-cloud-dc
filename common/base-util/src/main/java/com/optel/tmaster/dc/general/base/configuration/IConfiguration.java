/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.configuration;

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
 * 所有配置文件的超接口，只有实现此接口才会进行配置更新
 * 2019/9/9 16:30:24
 * @author LiH
 */
public interface IConfiguration extends Cloneable{

    /**
     * 文件相对于src\main\resources\configuration的路径
     * @return 文件路径
     */
    String getRelativizePath();

    /**
     * 配置属性前缀，比如配置文件中为omc.rmuid.zone,那么前缀就为'omc.rmuid'
     * @return 前缀
     */
    default String getPrefix() {
        return "";
    }

    /**
     * 配置默分隔，比如配置文件中为omc.rmuid.zone,那么前缀就为'.'
     * @return 分隔符
     */
    default char getDelimiter(){
        return '.';
    }

    /**
     * 初始化
     */
    void init();

    /**
     * 销毁
     */
    void close();
}
