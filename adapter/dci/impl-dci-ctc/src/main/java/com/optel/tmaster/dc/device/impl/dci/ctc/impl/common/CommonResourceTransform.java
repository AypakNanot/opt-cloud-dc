/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.impl.common;

import com.optel.tmaster.dc.dci.impl.base.transform.ITransform;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.common.resource.CommonResourceTransformImpl;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.types.rev220208.OPENCONFIGCOMPONENT;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.Interfaces;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.InterfacesBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.interfaces.Interface;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.interfaces.top.interfaces.InterfaceKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.Components;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.ComponentsBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.Component;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.ComponentKey;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.interfaces.rev180220.InterfaceType;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 公共通用资源 工具
 * 2022/3/16 9:34
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public interface CommonResourceTransform extends ITransform {
    /**
     * 获取到通用资源节点
     *
     * @param neId 网元id
     * @return 节点
     */
    default Components getComponents(String neId) {
        @NonNull InstanceIdentifier<Components> iid = create(Components.class);
        return MountTools.queryFromOperational(neId, iid);
    }

    /**
     * 获取到通用资源节点
     *
     * @param neId     网元id
     * @param nameList name list 可为空，为空时，表示查所有
     * @return Components
     */
    default Components getComponents(String neId, Set<String> nameList) {
        if (nameList == null || nameList.isEmpty()) {
            return getComponents(neId);
        }
        ComponentsBuilder resultBuilder = new ComponentsBuilder();
        if (nameList.size() == 1) {
            //只有一条name数据作为主键
            KeyedInstanceIdentifier<Component, ComponentKey> iid =
                    create(Components.class).child(Component.class, new ComponentKey(nameList.iterator().next()));
            Component component = MountTools.queryFromOperational(neId, iid);
            if (component != null) {
                Map<ComponentKey, Component> resultMap = new HashMap<>(nameList.size());
                resultMap.put(new ComponentKey(component.getName()), component);
                resultBuilder.setComponent(resultMap);
            }
        } else {
            resultBuilder.setComponent(filterByName(getComponents(neId).getComponent(), nameList));
        }
        return resultBuilder.build();
    }

    /**
     * 根据name过滤Components
     *
     * @param dataMap  待过滤数据
     * @param nameList 需要过滤出的name,若为空，返回所有数据
     * @return 过滤结果
     */
    private Map<ComponentKey, Component> filterByName(Map<ComponentKey, Component> dataMap, Set<String> nameList) {
        if (dataMap == null) {
            return null;
        }
        if (nameList == null || nameList.isEmpty()) {
            return dataMap;
        }
        Map<ComponentKey, Component> resultMap = new HashMap<>(nameList.size());
        for (String name : nameList) {
            ComponentKey key = new ComponentKey(name);
            if (dataMap.containsKey(key)) {
                resultMap.put(key, dataMap.get(key));
            }
        }
        return resultMap;
    }

    default Components filter(String neId, Set<String> nameList, Set<Class<? extends OPENCONFIGCOMPONENT>> typeList) {
        Components components = getComponents(neId, nameList);
        return filter(components, typeList);
    }

    default Components filter(Components components, Set<Class<? extends OPENCONFIGCOMPONENT>> typeList) {
        if (components == null) {
            return null;
        }
        if (typeList == null || typeList.isEmpty()) {
            return components;
        }
        @Nullable Map<ComponentKey, Component> map = components.getComponent();
        @Nullable Map<ComponentKey, Component> resultMap = new HashMap<>(map.size());
        ComponentsBuilder componentsBuilder = new ComponentsBuilder();
        for (Component e : map.values()) {
            if (e.getState() != null) {
                if (typeList.contains(new CommonResourceTransformImpl().devTypeToApi(e.getState().getType()).implementedInterface())) {
                    resultMap.put(new ComponentKey(e.getName()), e);
                }
            }
        }
        componentsBuilder.setComponent(resultMap);
        return componentsBuilder.build();
    }

    /**
     * 获取Interfaces
     *
     * @param neId     网元neid
     * @param nameList 筛选name
     * @return interfaces
     */
    default Interfaces getInterfaces(String neId, Set<String> nameList) {
        InterfacesBuilder builder = new InterfacesBuilder();
        InstanceIdentifier<Interfaces> iid = create(Interfaces.class);
        Interfaces interfaces = MountTools.queryFromOperational(neId, iid);
        if (interfaces != null) {
            if (nameList != null) {
                Map<InterfaceKey, Interface> values = new HashMap<>(nameList.size());
                List<Interface> collect = interfaces.getInterface().values().stream().filter(e -> nameList.contains(e.getName())).collect(Collectors.toList());
                for (Interface item : collect) {
                    values.put(item.key(), item);
                }
                builder.setInterface(values);
                return builder.build();
            }
            return interfaces;
        }
        return null;
    }

    /**
     * 根据type筛选
     *
     * @param interfaces interfaces
     * @param type       type
     * @return interfaces
     */
    default Interfaces filterInterfaceByType(Interfaces interfaces, Set<InterfaceType> type) {
        if (interfaces == null || interfaces.getInterface() == null) {
            return null;
        }
        if (type == null || type.isEmpty()) {
            return interfaces;
        }
        InterfacesBuilder interfacesBuilder = new InterfacesBuilder();
        Map<InterfaceKey, Interface> values = new HashMap<>(interfaces.getInterface().size());
        for (Interface item : interfaces.getInterface().values()) {
            if (item.getState() != null && type.contains(item.getState().getType())) {
                values.put(item.key(), item);
            }
        }
        interfacesBuilder.setInterface(values);
        return interfacesBuilder.build();
    }
}
