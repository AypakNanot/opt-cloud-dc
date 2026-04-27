/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform;

import org.apache.commons.collections.CollUtils;
import cn.hutool.core.map.MapUtil;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.AbstractCtcTransformer;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.PerformanceTransform;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.tca.parameters.grouping.TcaParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.tca.parameters.grouping.TcaParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.tca.parameters.grouping.TcaParameterKey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * ClassName: impl
 * <ul>
 * <li>性能门限转换类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2021/10/12 15:02
 */
public class TcaTransformImpl extends AbstractCtcTransformer implements PerformanceTransform {

    /**
     * dev tca to api List
     * @param tcaParameters dev
     * @return api
     */
    public List<TcaParameter> devTcaToApiList(List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.TcaParameter> tcaParameters){
        if(CollUtil.isEmpty(tcaParameters)){
            return null;
        }
        List<TcaParameter> res = new ArrayList<>();
        for(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.TcaParameter tcaParameter: tcaParameters){
            res.add(devTcaToApi(tcaParameter).build());
        }
        return res;
    }

    /**
     * dev to Tca api
     * @param tcaParameter dev
     * @return api
     */
    public TcaParameterBuilder devTcaToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.TcaParameter tcaParameter){
        if(tcaParameter == null){
            return null;
        }
        TcaParameterBuilder tcaParameterBuilder = new TcaParameterBuilder();
        TcaParameterKey tcaParameterKey = new TcaParameterKey(devGranularityToApi(tcaParameter.getGranularity()), tcaParameter.getObjectName(), tcaParameter.getPmParameterName(), devThresholdTypeToApi(tcaParameter.getThresholdType()));
        tcaParameterBuilder.withKey(tcaParameterKey);
        tcaParameterBuilder.setGranularity(devGranularityToApi(tcaParameter.getGranularity()));
        tcaParameterBuilder.setObjectName(tcaParameter.getObjectName());
        tcaParameterBuilder.setObjectType(tcaParameter.getObjectType());
        tcaParameterBuilder.setPmParameterName(tcaParameter.getPmParameterName());
        tcaParameterBuilder.setThresholdType(devThresholdTypeToApi(tcaParameter.getThresholdType()));
        tcaParameterBuilder.setThresholdValue(devRealToApi(tcaParameter.getThresholdValue()));
        return tcaParameterBuilder;
    }

    /**
     * dev tca to api List
     * @param tcaParameters dev
     * @return api
     */
    public List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.tca.parameters.grouping.TcaParameter> devTcaExtensionToApiList(
            Map<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tca.parameters.grouping.TcaParameterKey, org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tca.parameters.grouping.TcaParameter> tcaParameters){
        if(MapUtil.isEmpty(tcaParameters)){
            return null;
        }
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.tca.parameters.grouping.TcaParameter> res = new ArrayList<>();
        for(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tca.parameters.grouping.TcaParameter tcaParameter: tcaParameters.values()){
            res.add(devTcaExtensionToApi(tcaParameter));
        }
        return res;
    }

    /**
     * dev to Tca api
     * @param tcaParameter dev
     * @return api
     */
    public org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.tca.parameters.grouping.TcaParameter devTcaExtensionToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tca.parameters.grouping.TcaParameter tcaParameter){
        if(tcaParameter == null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.tca.parameters.grouping.TcaParameterBuilder tcaParameterBuilder = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.tca.parameters.grouping.TcaParameterBuilder();
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.tca.parameters.grouping.TcaParameterKey tcaParameterKey = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.tca.parameters.grouping.TcaParameterKey(devGranularityToApi(tcaParameter.getGranularity()), tcaParameter.getObjectName(), tcaParameter.getPmParameterName(), devThresholdTypeToApi(tcaParameter.getThresholdType()));
        tcaParameterBuilder.withKey(tcaParameterKey);
        tcaParameterBuilder.setGranularity(devGranularityToApi(tcaParameter.getGranularity()));
        tcaParameterBuilder.setObjectName(tcaParameter.getObjectName());
        tcaParameterBuilder.setObjectType(tcaParameter.getObjectType());
        tcaParameterBuilder.setPmParameterName(tcaParameter.getPmParameterName());
        tcaParameterBuilder.setThresholdType(devThresholdTypeToApi(tcaParameter.getThresholdType()));
        tcaParameterBuilder.setThresholdValue(devRealToApi(tcaParameter.getThresholdValue()));
        return tcaParameterBuilder.build();
    }

}
