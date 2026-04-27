/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.util;

import java.io.File;
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
 *
 *          l       i    h     h    u     u       a a
 *          l            h     h    u     u     a     a
 *          l       i    h h h h    u     u     a     a
 *          l       i    h     h    u     u     a     a
 *          l l l   i    h     h      u u  u      a a   a
 *
 *              LiHua       佛主保佑       永无bBUG
 * </pre>
 * 设置一些全局变量
 * 2019/9/9 16:30:24
 *
 * @author LiH
 */
public class GlobalConfig {

    /**
     * 获取全局运行根目录。KARAF_HOME在karaf启动的时候，在karaf文件中已经进行了设置。
     *
     * @return 运行根目录
     */
    public static String getKarafHome() {
        String karafHome = System.getProperty("karaf.home");
        if (karafHome == null || "".equals(karafHome)) {
            final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
            //获取开发环境中的运行目录,去掉文件前面独特的'/'
            karafHome = new File(Objects.requireNonNull(classLoader.getResource("")).getFile()).getAbsolutePath();
        }
        return karafHome;
    }

    /**
     * 获取系统编码格式
     *
     * @return 编码格式
     */
    public static String getSystemEncoding() {
        String systemEncoding = System.getProperty("SYSTEM_ENCODING");
        if (systemEncoding == null || "".equals(systemEncoding)) {
            systemEncoding = "UTF-8";
        }
        return systemEncoding;
    }

}
