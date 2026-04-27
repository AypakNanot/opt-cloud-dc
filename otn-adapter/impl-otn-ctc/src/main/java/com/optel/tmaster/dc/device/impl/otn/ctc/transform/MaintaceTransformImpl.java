/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform;

import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.AbstractCtcTransformer;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.EnumTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.ResetNeInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.service.delay.measure.configs.grouping.ServiceDelayMeasureConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetServiceDelayMeasureResultOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ServiceDelayMeasureConfigs;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.service.delay.measure.configs.grouping.ServiceDelayMeasureConfigBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.service.delay.measure.configs.grouping.ServiceDelayMeasureConfigKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ResetInputBuilder;

import java.util.ArrayList;
import java.util.List;

/**
* 维护功能转换
* @author Quan Jingyuan
* @since 2021/10/14
**/
public class MaintaceTransformImpl  extends AbstractCtcTransformer implements CommonTransform, EnumTransform {

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.service.delay.measure.configs.grouping.ServiceDelayMeasureConfig apiServiceDelayConfigToDev(ServiceDelayMeasureConfig serviceDelayMeasureConfig){
        ServiceDelayMeasureConfigBuilder measureConfigBuilder=new  ServiceDelayMeasureConfigBuilder();
        measureConfigBuilder.setEnable(serviceDelayMeasureConfig.getEnable());
        measureConfigBuilder.setConnectionName(serviceDelayMeasureConfig.getConnectionName());
        measureConfigBuilder.setMeasureName(serviceDelayMeasureConfig.getMeasureName());
        measureConfigBuilder.setMeasureType(apiServiceDelayMeasureTypeToDev(serviceDelayMeasureConfig.getMeasureType()));
        measureConfigBuilder.setPtpName(serviceDelayMeasureConfig.getPtpName());
        measureConfigBuilder.withKey(new ServiceDelayMeasureConfigKey(serviceDelayMeasureConfig.key().getPtpName()));
        measureConfigBuilder.setTcm(serviceDelayMeasureConfig.getTcm());
        return measureConfigBuilder.build();
    }
    public List<ServiceDelayMeasureConfig> devServiceDelayConfigToApi(ServiceDelayMeasureConfigs serviceDelayMeasureConfigs){
        if(serviceDelayMeasureConfigs == null){
            return null;
        }List<ServiceDelayMeasureConfig> configList=new ArrayList<>();
        if(serviceDelayMeasureConfigs.getServiceDelayMeasureConfig() != null){
            for(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.service.delay.measure.configs.grouping.ServiceDelayMeasureConfig item:serviceDelayMeasureConfigs.getServiceDelayMeasureConfig().values()){
                org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.service.delay.measure.configs.grouping.ServiceDelayMeasureConfigBuilder measureConfigBuilder=new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.service.delay.measure.configs.grouping.ServiceDelayMeasureConfigBuilder();
                measureConfigBuilder.setConnectionName(item.getConnectionName());
                measureConfigBuilder.setMeasureName(item.getMeasureName());
                measureConfigBuilder.setMeasureType(devServiceDelayMeasureTypeToApi(item.getMeasureType()));
                measureConfigBuilder.setPtpName(item.getPtpName());
                measureConfigBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.service.delay.measure.configs.grouping.ServiceDelayMeasureConfigKey(item.key().getPtpName()));
                measureConfigBuilder.setTcm(item.getTcm());
                configList.add(measureConfigBuilder.build());
            }
        }
        return configList;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.GetServiceDelayMeasureResultOutput devServiceDelayResultToApi(GetServiceDelayMeasureResultOutput result){
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

