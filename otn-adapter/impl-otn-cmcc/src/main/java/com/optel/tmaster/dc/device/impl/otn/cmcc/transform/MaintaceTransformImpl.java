/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform;

import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.AbstractCmccTransformer;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.EnumTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.ResetNeInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.service.delay.measure.configs.grouping.ServiceDelayMeasureConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.service.delay.measure.configs.grouping.ServiceDelayMeasureConfigBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.service.delay.measure.configs.grouping.ServiceDelayMeasureConfigKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ServiceDelayMeasureConfigs;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ResetInputBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * 维护功能转换
 * @author Quan Jingyuan
 * @since 2021/10/9
 **/
public class MaintaceTransformImpl  extends AbstractCmccTransformer implements CommonTransform , EnumTransform {

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.service.delay.measure.configs.grouping.ServiceDelayMeasureConfig apiServiceDelayConfigToDev(ServiceDelayMeasureConfig serviceDelayMeasureConfig){
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.service.delay.measure.configs.grouping.ServiceDelayMeasureConfigBuilder measureConfigBuilder=new  org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.service.delay.measure.configs.grouping.ServiceDelayMeasureConfigBuilder();
        measureConfigBuilder.setEnable(serviceDelayMeasureConfig.getEnable());
        measureConfigBuilder.setConnectionName(serviceDelayMeasureConfig.getConnectionName());
        measureConfigBuilder.setMeasureName(serviceDelayMeasureConfig.getMeasureName());
        measureConfigBuilder.setMeasureType(apiServiceDelayMeasureTypeToDev(serviceDelayMeasureConfig.getMeasureType()));
        measureConfigBuilder.setPtpName(serviceDelayMeasureConfig.getPtpName());
        measureConfigBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.service.delay.measure.configs.grouping.ServiceDelayMeasureConfigKey(serviceDelayMeasureConfig.key().getPtpName()));
        measureConfigBuilder.setTcm(serviceDelayMeasureConfig.getTcm());
        return measureConfigBuilder.build();
    }
    public List<ServiceDelayMeasureConfig> devServiceDelayConfigToApi(ServiceDelayMeasureConfigs serviceDelayMeasureConfigs){
        List<ServiceDelayMeasureConfig> configList=new ArrayList<>();
        if(serviceDelayMeasureConfigs == null || serviceDelayMeasureConfigs.getServiceDelayMeasureConfig() == null){
            return configList;
        }
        for(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.service.delay.measure.configs.grouping.ServiceDelayMeasureConfig item:serviceDelayMeasureConfigs.getServiceDelayMeasureConfig().values()){
            ServiceDelayMeasureConfigBuilder measureConfigBuilder=new ServiceDelayMeasureConfigBuilder();
            measureConfigBuilder.setConnectionName(item.getConnectionName());
            measureConfigBuilder.setMeasureName(item.getMeasureName());
            measureConfigBuilder.setMeasureType(devServiceDelayMeasureTypeToApi(item.getMeasureType()));
            measureConfigBuilder.setPtpName(item.getPtpName());
            measureConfigBuilder.withKey(new ServiceDelayMeasureConfigKey(item.key().getPtpName()));
            measureConfigBuilder.setTcm(item.getTcm());
            configList.add(measureConfigBuilder.build());
        }
        return configList;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.GetServiceDelayMeasureResultOutput devServiceDelayResultToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.GetServiceDelayMeasureResultOutput result){
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.GetServiceDelayMeasureResultOutputBuilder outPutBuilder = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.GetServiceDelayMeasureResultOutputBuilder();
        outPutBuilder.setLinkDelay(result.getLinkDelay());
        outPutBuilder.setServiceDelay(result.getServiceDelay());
        return outPutBuilder.build();
    }

    public ResetInputBuilder apiResetToDev(ResetNeInput input){
        ResetInputBuilder resetInputBuilder = new ResetInputBuilder();
        resetInputBuilder.setEqName(input.getEqName());
        resetInputBuilder.setResetType(apiResetTypeToDev(input.getResetType()));
        return resetInputBuilder;
    }

}
