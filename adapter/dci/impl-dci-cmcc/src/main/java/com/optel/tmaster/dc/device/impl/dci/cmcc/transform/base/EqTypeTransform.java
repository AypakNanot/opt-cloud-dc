/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base;

import com.optel.tmaster.dc.dci.impl.base.transform.ITransform;
import com.optel.tmaster.dc.general.base.exception.manage.NoMatchEnumValueException;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.types.rev220208.*;

import java.util.HashSet;
import java.util.Set;

/**
 * TODO
 * 2022/3/11 14:54
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public interface EqTypeTransform extends ITransform {

    /**
     * ComponentPowerType dev to api
     * @param componentPowerType
     * @return
     */
    default ComponentPowerType devComponentPowerTypeToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.ComponentPowerType componentPowerType){
        if(componentPowerType==null){
            return null;
        }
        switch (componentPowerType){
            case POWERENABLED:
                return ComponentPowerType.POWERENABLED;
            case POWERDISABLED:
                return ComponentPowerType.POWERDISABLED;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(componentPowerType);
        }
    }

    /**
     * FanSpeedType api to dev
     * @param fanSpeedType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.FanSpeedType apiFanSpeedTypeToDev(FanSpeedType fanSpeedType){
        if(fanSpeedType==null){
            return null;
        }
        switch (fanSpeedType){
            case MIDDLE:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.FanSpeedType.MIDDLE;
            case HIGH:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.FanSpeedType.HIGH;
            case LOW:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.FanSpeedType.LOW;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(fanSpeedType);
        }
    }

    /**
     * FanModeType api to dev
     * @param fanModeType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.FanModeType apiFanModeTypeToDev(FanModeType fanModeType){
        if(fanModeType==null){
            return null;
        }
        switch (fanModeType){
            case MANUAL:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.FanModeType.MANUAL;
            case AUTO:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.FanModeType.AUTO;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(fanModeType);
        }
    }

    /**
     * api to dev
     * @param ptmWorkType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.PtmWorkType
            apiPtmWorkTypeToDev(PtmWorkType ptmWorkType){
        if(ptmWorkType==null){
            return null;
        }
        switch (ptmWorkType){
            case UpDown:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.PtmWorkType.UpDown;
            case RELAY:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.PtmWorkType.RELAY;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(ptmWorkType);
        }
    }

    /**
     * devSupportedPowerSupplyModesToApi
     * @param supportedPowerSupplyModes dev
     * @return api
     */
    default Set<PsuModeType> devSupportedPowerSupplyModesToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.psu.rev230426.psu.state.SupportedPowerSupplyModes supportedPowerSupplyModes){

        if(supportedPowerSupplyModes==null){
            return null;
        }
        Set<PsuModeType> list = new HashSet<>();
        @Nullable Set<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.PsuModeType> supportedPowerSupplyModeList
                = supportedPowerSupplyModes.getSupportedPowerSupplyMode();
        for(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.PsuModeType e:supportedPowerSupplyModeList){
            list.add(devPsuModeTypeToApi(e));
        }
        return list;
    }

    /**
     * devPsuModeTypeToApi
     * @param psuModeType dev
     * @return api
     */
    default PsuModeType devPsuModeTypeToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.PsuModeType psuModeType){
        if(psuModeType==null){
            return null;
        }
        switch (psuModeType){
            case AC:
                return PsuModeType.AC;
            case DC:
                return PsuModeType.DC;
            case HVDC:
                return PsuModeType.HVDC;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(psuModeType);
        }
    }

    /**
     *  dev fan_mode to api
     * @param fanModeType dev
     * @return api
     */
    default FanModeType devFanModeTypeToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.FanModeType fanModeType){
        if(fanModeType==null){
            return null;
        }
        switch (fanModeType){
            case AUTO:
                return FanModeType.AUTO;
            case MANUAL:
                return FanModeType.MANUAL;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(fanModeType);
        }
    }

    /**
     * dev ptmWorkType to api
     * @param ptmWorkType dev
     * @return api
     */
    default PtmWorkType devPtmWorkTypeToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.PtmWorkType ptmWorkType){
        if(ptmWorkType==null){
            return null;
        }
        switch (ptmWorkType){
            case RELAY:
                return PtmWorkType.RELAY;
            case UpDown:
                return PtmWorkType.UpDown;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(ptmWorkType);
        }
    }

    /**
     * dev FanSpeedType To Api
     * @param fanSpeedType dev
     * @return api
     */
    default FanSpeedType devFanSpeedTypeToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.FanSpeedType fanSpeedType){
        if(fanSpeedType==null){
            return null;
        }
        switch (fanSpeedType){
            case LOW:
                return FanSpeedType.LOW;
            case HIGH:
                return FanSpeedType.HIGH;
            case MIDDLE:
                return FanSpeedType.MIDDLE;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(fanSpeedType);
        }
    }
}
