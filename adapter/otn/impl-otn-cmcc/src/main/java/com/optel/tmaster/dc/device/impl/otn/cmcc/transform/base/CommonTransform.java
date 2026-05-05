/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base;

import cn.hutool.core.collection.CollUtil;
import com.optel.tmaster.dc.device.impl.base.transform.ITransform;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.AdminState;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.OperationalState;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.StatePac;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ptps.grouping.ptp.OpticalPowerPac;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.Real;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ETH;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.ODU;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.SDH;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.LayerProtocolName;
import org.opendaylight.yangtools.yang.binding.Augmentation;

import java.util.*;
import java.util.stream.Collectors;

/**
 * CommonTransform
 * 公共的对象转换
 * date       time        author
 * ─────────────────────────────
 * 2021/9/30   17:22      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public interface CommonTransform extends ITransform, EnumTransform {

    /**
     * 将CMCC层协议转换为API对应的层协议
     *
     * @param layerNameList CMCC 层协议
     * @return api层协议
     */
    default Set<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.LayerProtocolName>
    devLayerNameToApi(Collection<LayerProtocolName> layerNameList) {
        return cts(layerNameList,this::devLayerNameToApi);
    }

    /**
     * 将CMCC层协议转换为APi层协议
     *
     * @param layerName CMCC转换的层协议
     * @return api层协议
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.LayerProtocolName devLayerNameToApi(
            LayerProtocolName layerName) {
        Class<? extends LayerProtocolName> lnc = layerName.implementedInterface();
        if (lnc.equals(ETH.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eth.rev210927.ETH.VALUE;
        } else if (lnc.equals(SDH.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.SDH.VALUE;
        } else if (lnc.equals(ODU.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.rev210927.ODU.VALUE;
        }
        throw getNoMatchEnumValueException(layerName);
    }

    /**
     * 将CMCC层协议转换为API对应的层协议
     *
     * @param layerNameList api 层协议
     * @return CMCC层协议
     */
    default Set<LayerProtocolName> apiLayerNameToDev(List<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.LayerProtocolName> layerNameList) {
        if (CollUtil.isEmpty(layerNameList)) {
            return Collections.emptySet();
        }
        return layerNameList.stream().map(this::apiLayerNameToDev).collect(Collectors.toSet());
    }

    /**
     * 将api层协议转换为CMCC dev层协议
     *
     * @param layerName api格式的层协议
     * @return CMCC层协议
     */
    default LayerProtocolName apiLayerNameToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.LayerProtocolName layerName) {
        if (layerName == null) {
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.LayerProtocolName> lmc = layerName.implementedInterface();
        if (lmc.equals(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eth.rev210927.ETH.class)) {
            return ETH.VALUE;
        } else if (lmc.equals(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.SDH.class)) {
            return SDH.VALUE;
        } else if (lmc.equals(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.rev210927.ODU.class)) {
            return ODU.VALUE;
        }
        throw getNoMatchEnumValueException(layerName);
    }


    /**
     * 通用状态属性 dev转api
     *
     * @param statePac 通用状态属性 dev
     * @return 通用状态属性 api
     */
    default StatePac devStatePacToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.StatePac statePac) {
        if (statePac == null) {
            return null;
        }
        return new StatePac() {

            @Override
            public OperationalState getOperationalState() {
                return devOperationalStateToApi(statePac.getOperationalState());
            }

            @Override
            public AdminState getAdminState() {
                return devAdminStateToApi(statePac.getAdminState());
            }

            @Override
            public Class<? extends StatePac> implementedInterface() {
                return null;
            }
        };
    }

    /**
     * OpticalPowerPac acc to otn
     *
     * @param opticalPowerPac acc
     * @return otn
     */
    default OpticalPowerPac devOpticalPowerPacToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ptps.ptp.OpticalPowerPac opticalPowerPac) {
        if (opticalPowerPac == null) {
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
     * real acc to otn
     *
     * @param real acc
     * @return otn
     */
    default Real devRealToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.Real real) {
        if (real == null) {
            return null;
        }
        return new Real(real.getDecimal64());
    }

    /**
     * real api to dev
     *
     * @param real api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.Real apiRealToDev(Real real) {
        if (real == null) {
            return null;
        }
        return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.Real(real.getDecimal64());

    }

}
