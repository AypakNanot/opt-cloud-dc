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
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.mask.types.rev220208.AlarmObjectType;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.rev220208.AlarmState;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.*;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.mask.types.rev230426.AlarmMaskObjectType;

/**
 * 告警类型转换器
 * 2022/3/14 9:48
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public interface AlarmTypeTransform extends ITransform {

    default AlarmMaskObjectType apiAlarmObjectTypeToDev(AlarmObjectType alarmObjectType){
        if(alarmObjectType==null){
            return null;
        }
        switch (alarmObjectType){
            case CARD:
                return AlarmMaskObjectType.CARD;
            case PORT:
                return AlarmMaskObjectType.PORT;
            case CHASSIS:
                return AlarmMaskObjectType.CHASSIS;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(alarmObjectType);
        }
    }
////
////    /**
////     * alarm service dev to api
////     * @param service dev
////     * @return api
////     */
////    default Class<? extends OPENCONFIGALARMSEVERITY> devSeverityToApi(
////            Class<? extends org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.types.rev200630.OPENCONFIGALARMSEVERITY> service){
////        if(service==null){
////            return null;
////        }
////        if(service.getSimpleName().equals(CRITICAL.class.getSimpleName())){
////            return CRITICAL.class;
////        }
////        if(MAJOR.class.getSimpleName().equals(service.getSimpleName())){
////            return MAJOR.class;
////        }
////        if(WARNING.class.getSimpleName().equals(service.getSimpleName())){
////            return WARNING.class;
////        }
////        if(MINOR.class.getSimpleName().equals(service.getSimpleName())){
////            return MINOR.class;
////        }
////        if(UNKNOWN.class.getSimpleName().equals(service.getSimpleName())){
////            return UNKNOWN.class;
////        }
////        throw NoMatchEnumValueException.getNoMatchEnumValueException(service);
////    }
////
////    /**
////     * TypeId dev to api
////     * @param typeId dev
////     * @return api
////     */
////    default AlarmState.TypeId devTypeIdToApi(
////            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.rev200630.AlarmState.TypeId typeId){
////        if(typeId==null){
////            return null;
////        }
////        return new AlarmState.TypeId(typeId.getString());
////    }
////
    /**
     * AlarmObjectType dev to api
     * @param alarmObjectType dev
     * @return api
     */
    default AlarmObjectType devAlarmObjectTypeToApi(
            AlarmMaskObjectType alarmObjectType){
        if(alarmObjectType==null){
            return null;
        }
        switch (alarmObjectType){
            case CHASSIS:
                return AlarmObjectType.CHASSIS;
            case PORT:
                return AlarmObjectType.PORT;
            case CARD:
                return AlarmObjectType.CARD;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(alarmObjectType);
        }
    }
}
