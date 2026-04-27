/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform;

import cn.hutool.core.map.MapUtil;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.AbstractCtcTransformer;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.PerformanceTransform;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.performance.rev210927.performances.grouping.Performance;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.performance.rev210927.performances.grouping.PerformanceBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.performance.rev210927.performances.grouping.PerformanceKey;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: impl
 * <ul>
 * <li>当前性能转换类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2021/10/12 15:02
 */
public class PerformanceTransformImpl extends AbstractCtcTransformer implements PerformanceTransform {

    public Map<PerformanceKey, Performance> devPerformanceToApi(Map<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.PerformanceKey,
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.Performance> performances){
        if(MapUtil.isEmpty(performances)){
            return null;
        }
        Map<PerformanceKey,Performance> res = new HashMap<>(performances.size());
        for(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.Performance performance: performances.values()){
            Performance performance1 = devPerformanceToApi(performance);
            res.put(performance1.key(),performance1);
        }
        return res;
    }

    /**
     * dev to performance api
     * @param performance dev
     * @return api
     */
    public Performance devPerformanceToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.Performance performance){
        if(performance == null){
            return null;
        }
        PerformanceBuilder performanceBuilder = new PerformanceBuilder();
        PerformanceKey performanceKey = new PerformanceKey(devGranularityToApi(performance.getGranularity()), performance.getObjectName(), performance.getPmParameterName(), performance.getStartTime());
        performanceBuilder.withKey(performanceKey);
        performanceBuilder.setDigitalOrAnalog(devDigitalOrAnalogToApi(performance.getDigitalOrAnalog()));
        performanceBuilder.setGranularity(devGranularityToApi(performance.getGranularity()));
        performanceBuilder.setObjectName(performance.getObjectName());
        performanceBuilder.setObjectType(performance.getObjectType());
        performanceBuilder.setPmParameterName(performance.getPmParameterName());
        performanceBuilder.setStartTime(performance.getStartTime());
        return performanceBuilder.build();
    }
}
