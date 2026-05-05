/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform.base;

import cn.hutool.core.collection.CollUtil;
import com.optel.tmaster.dc.device.impl.base.transform.ITransform;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.AdminState;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.OperationalState;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.StatePac;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ptps.grouping.ptp.OpticalPowerPac;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.Real;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ETH;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.OSU;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.ODU;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.SDH;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.LayerProtocolName;
import org.opendaylight.yangtools.yang.binding.Augmentation;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 *  CommonTransform
 * 公共的对象转换
 * date       time        author
 * ─────────────────────────────
 * 2021/9/30   17:22      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public interface CommonTransform extends ITransform,EnumTransform {

    /**
     * 将CTC层协议转换为API对应的层协议
     * @param layerNameList CTC 层协议
     * @return api层协议
     */
    default Set<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.LayerProtocolName>
    devLayerNameToApi(Set<LayerProtocolName> layerNameList){
        if(CollUtil.isEmpty(layerNameList)){
            return null;
        }
        Set<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.LayerProtocolName> result = new HashSet<>();
        for(LayerProtocolName layerName:layerNameList){
            result.add(devLayerNameToApi(layerName));
        }
        return result;
    }

    /**
     *  OpticalPowerPac acc to otn
     * @param opticalPowerPac acc
     * @return otn
     */
    default OpticalPowerPac devOpticalPowerPacToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ptps.grouping.ptp.OpticalPowerPac opticalPowerPac){
        if(opticalPowerPac==null){
            return null;
        }
        return new OpticalPowerPac() {
            @Override
            public @NonNull Map<Class<? extends Augmentation<OpticalPowerPac>>, Augmentation<OpticalPowerPac>> augmentations() {
                return new HashMap<>(0);
            }

            @Override
            public Real getInputPower() {
                return devRealToApi(opticalPowerPac.getInputPower());
            }

            @Override
            public Real getOutputPower() {
                return devRealToApi(opticalPowerPac.getOutputPower());
            }

            @Override
            public Real getInputPowerUpperThreshold() {
                return devRealToApi(opticalPowerPac.getInputPowerUpperThreshold());
            }

            @Override
            public Real getInputPowerLowerThreshold() {
                return devRealToApi(opticalPowerPac.getInputPowerLowerThreshold());
            }
        };
    }

    /**
     * 将CTC层协议转换为APi层协议
     * @param layerName CTC转换的层协议
     * @return api层协议
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.LayerProtocolName devLayerNameToApi(
            LayerProtocolName layerName){
        if(layerName == null){
            return null;
        }
        Class<? extends LayerProtocolName> lnc = layerName.implementedInterface();
        if(ETH.class.equals(lnc)){
            return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eth.rev210927.ETH.VALUE;
        }else if(SDH.class.equals(lnc)){
            return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.SDH.VALUE;
        }else if(ODU.class.equals(lnc)){
            return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.rev210927.ODU.VALUE;
        }else if (OSU.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.OSU.VALUE;
        }
        throw getNoMatchEnumValueException(layerName);
    }

    /**
     * 将CTC层协议转换为API对应的层协议
     * @param layerNameList api 层协议
     * @return CTC层协议
     */
    default Set<LayerProtocolName> apiLayerNameToDev(Set<
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.LayerProtocolName> layerNameList){
        if(CollUtil.isEmpty(layerNameList)){
            return null;
        }
        Set<LayerProtocolName> result = new HashSet<>();
        for(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.LayerProtocolName layerName:layerNameList){
            result.add(apiLayerNameToDev(layerName));
        }
        return result;
    }

    /**
     * 将api层协议转换为CTC dev层协议
     * @param layerName api格式的层协议
     * @return CTC层协议
     */
    default LayerProtocolName apiLayerNameToDev(
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.LayerProtocolName layerName){
        if(layerName == null){
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.LayerProtocolName> lnc = layerName.implementedInterface();
        if(lnc.equals(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eth.rev210927.ETH.class)){
            return ETH.VALUE;
        }else if(lnc.equals(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.SDH.class)){
            return SDH.VALUE;
        }else if(lnc.equals(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.rev210927.ODU.class)){
            return ODU.VALUE;
        }else if(lnc.equals(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.OSU.class)){
            return OSU.VALUE;
        }
        throw getNoMatchEnumValueException(layerName);
    }

    /**
     * 通用状态属性 dev转api
     * @param statePac 通用状态属性 dev
     * @return 通用状态属性 api
     */
    default StatePac devStatePacToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.StatePac statePac){
        if(statePac == null){
            return null;
        }
        return new StatePac() {
            @Override
            public Class<? extends StatePac> implementedInterface() {
                return null;
            }

            @Override
            public OperationalState getOperationalState() {
                if(statePac.getOperationalState() == null){
                    return null;
                }
                return OperationalState.forValue(statePac.getOperationalState().getIntValue());
            }

            @Override
            public AdminState getAdminState() {
                if(statePac.getAdminState() == null){
                    return null;
                }
                return AdminState.forValue(statePac.getAdminState().getIntValue());
            }

        };
    }

    /**
     * Real类型数据 从dev转换为api
     * @param real dev real数据
     * @return api real 数据
     */
    default Real devRealToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.Real real){
        if(real == null){
            return null;
        }
        return new Real(real.getDecimal64());
    }
    /**
     * real api to dev
     * @param real api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.Real apiRealToDev(Real real){
        if(real==null){
            return null;
        }
        return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.Real(real.getDecimal64());

    }


}
