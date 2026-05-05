/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.common;

import org.opendaylight.yangtools.yang.binding.*;

import java.util.*;
import java.util.function.Function;
import java.util.stream.Collector;
import java.util.stream.Collectors;

/**
 * <pre>
 * TMaster2000V8.25 - ITf
 * </pre>
 * 基础转换，主要是 DEV <==> DC
 * <pre>
 * date              time          author
 * ───────────────────────────────────────
 * 2024-08-28 0028   15:29:24      LiHua
 * Copyright (c) 2024, H-OPTEL All Rights Reserved.
 * </pre>
 *
 * @author LiHua
 * @version V2.0.0
 */
public interface ITf {
    /**
     * 集合转换为 set
     * 应对 odl 升级 所需
     *
     * @param collection 被转换的设备集合
     * @param f          将集合转换成单个的方法
     * @param <T>        集合 类型
     * @param <R>        单对象返回类型
     * @return Set集合
     */
    default <T, R> Set<R> cts(Collection<T> collection, Function<T, R> f) {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptySet();
        }
        return collection.stream().map(f).collect(Collectors.toSet());
    }

    /**
     * 集合转换为 List
     * 应对 odl 升级 所需
     *
     * @param collection 被转换的设备集合
     * @param f          将集合转换成单个的方法
     * @param <T>        集合 类型
     * @param <R>        单对象返回类型
     * @return List集合
     */
    default <T, R> List<R> ctl(Collection<T> collection, Function<T, R> f) {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptyList();
        }
        return collection.stream().map(f).collect(Collectors.toList());
    }

    /**
     * 集合转map
     * 应对 odl 升级 所需
     *
     * @param collection  被转换的设备集合
     * @param f           将集合转换成单个的方法
     * @param keyMapper   单对象 转换成map的key
     * @param valueMapper 单对象转换成map的value
     * @param <T>         集合 类型
     * @param <R>         单对象返回类型
     * @param <K>         转换后map的key 类型
     * @param <U>         转换后map的value 类型
     * @return map
     */
    default <T, R, K, U> Map<K, U> ctm(Collection<T> collection,
                                       Function<T, R> f,
                                       Function<? super R, ? extends K> keyMapper,
                                       Function<? super R, ? extends U> valueMapper) {
        Set<R> dta = cts(collection, f);
        if (collection == null || collection.isEmpty()) {
            return Collections.emptyMap();
        }
        Collector<R, ?, Map<K, U>> map = Collectors.toMap(keyMapper, valueMapper);
        return dta.stream().collect(map);
    }

    /**
     * 集合 转 map
     * 应对 odl 升级 所需
     *
     * @param collection  被转换的设备集合
     * @param keyMapper   单对象 转换成map的key
     * @param valueMapper 单对象转换成map的value
     * @param <T>         单对象返回类型
     * @param <K>         转换后map的key 类型
     * @param <V>         转换后map的value 类型
     * @return map
     */
    default <T, K, V> Map<K, V> ctm(Collection<T> collection,
                                    Function<? super T, ? extends K> keyMapper,
                                    Function<? super T, ? extends V> valueMapper) {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptyMap();
        }
        Collector<T, ?, Map<K, V>> map = Collectors.toMap(keyMapper, valueMapper);
        return collection.stream().collect(map);
    }

    /**
     * 将list转map
     * 应对 odl 升级 所需
     *
     * @param collection 集合
     * @param <K>        单个对象的 key
     * @param <V>        单对象本身
     * @return map
     */
    default <K extends Identifier<? extends Identifiable<K>>, V extends org.opendaylight.yangtools.yang.binding.Identifiable<K>>
    Map<K, V> ltm(Collection<V> collection) {
        if (collection == null || collection.isEmpty()) {
            return Collections.emptyMap();
        }
        return collection.stream().collect(Collectors.toMap(org.opendaylight.yangtools.yang.binding.Identifiable::key, item -> item));
    }

    /**
     * 根据class 获取 identifier
     *
     * @param cls cls  cls extends DataObject & DataRoot
     * @return identifier
     */
    @SuppressWarnings("unchecked")
    default InstanceIdentifier create(Class cls) {
        // DataRoot
        if (DataRoot.class.isAssignableFrom(cls)) {
            return InstanceIdentifier.create(cls);
        }
        // DateObject
        if (DataObject.class.isAssignableFrom(cls)) {
            InstanceIdentifier.Item<?> item = InstanceIdentifier.Item.of(cls);
            return InstanceIdentifier.unsafeOf(List.of(item));
        }
        throw new OptelDcException("Not support searching for identifiers ,cls:" + cls.getName() + ", support class type : impl DataRoot or DataObject.");
    }

}