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
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

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
 * 所有Properties类型的配置抽象实现类
 * 2019/9/9 16:30:24
 * @author LiH
 */
public abstract class AbstractPropertiesConfiguration extends AbstractConfiguration implements IPropertiesConfiguration{

    /** stores the configuration key-value pairs */
    public Map<String, Object> store = new LinkedHashMap<>();

    /**
     * Adds a key/value pair to the map.  This routine does no magic morphing.
     * It ensures the keylist is maintained
     *
     * @param key key to use for mapping
     * @param value object to store
     */
    @Override
    public void addPropertyDirect(String key, Object value) {
        Object previousValue = getProperty(key);

        if (previousValue == null)
        {
            store.put(key, value);
        }
        else if (previousValue instanceof List)
        {
            // safe to case because we have created the lists ourselves
            @SuppressWarnings("unchecked")
            List<Object> valueList = (List<Object>) previousValue;
            // the value is added to the existing list
            valueList.add(value);
        }
        else
        {
            // the previous value is replaced by a list containing the previous value and the new value
            List<Object> list = new ArrayList<>();
            list.add(previousValue);
            list.add(value);

            store.put(key, list);
        }
        try {
            BeanUtils.setProperty(this,key,value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            //NOTHING
        }
    }

    /**
     * Read property from underlying map.
     *
     * @param key key to use for mapping
     *
     * @return object associated with the given configuration key.
     */
    @Override
    public Object getProperty(String key)
    {
        return store.get(key);
    }

    @Override
    public void setProperty(String key, Object value){
        addPropertyDirect(key,value);
    }

    /**
     * Check if the configuration is empty
     *
     * @return {@code true} if Configuration is empty,
     * {@code false} otherwise.
     */
    @Override
    public boolean isEmpty()
    {
        return store.isEmpty();
    }

    /**
     * check if the configuration contains the key
     *
     * @param key the configuration key
     *
     * @return {@code true} if Configuration contain given key,
     * {@code false} otherwise.
     */
    @Override
    public boolean containsKey(String key)
    {
        return store.containsKey(key);
    }

    /**
     * Clear a property in the configuration.
     *
     * @param key the key to remove along with corresponding value.
     */
    @Override
    public void clearPropertyDirect(String key)
    {
        if (containsKey(key))
        {
            store.remove(key);
        }
    }

    @Override
    public void clear()
    {
        store.clear();
    }

    /**
     * Get the list of the keys contained in the configuration
     * repository.
     *
     * @return An Iterator.
     */
    public Iterator<String> getKeys()
    {
        return store.keySet().iterator();
    }

}
