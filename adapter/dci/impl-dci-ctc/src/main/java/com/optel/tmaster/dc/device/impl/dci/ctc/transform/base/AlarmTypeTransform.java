/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.transform.base;

import com.optel.tmaster.dc.dci.impl.base.transform.ITransform;
import com.optel.tmaster.dc.general.base.exception.manage.NoMatchEnumValueException;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.mask.types.rev220208.AlarmObjectType;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.rev220208.AlarmState;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.CRITICAL;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.MAJOR;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.MINOR;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.OPENCONFIGALARMSEVERITY;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.UNKNOWN;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.WARNING;

/**
 * 告警类型转换器
 * 2022/3/14 9:48
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public interface AlarmTypeTransform extends ITransform {

    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.types.rev200617.AlarmObjectType apiAlarmObjectTypeToDev(AlarmObjectType alarmObjectType){
        if(alarmObjectType==null){
            return null;
        }
        switch (alarmObjectType){
            case CARD:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.types.rev200617.AlarmObjectType.CARD;
            case PORT:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.types.rev200617.AlarmObjectType.PORT;
            case CHASSIS:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.types.rev200617.AlarmObjectType.CHASSIS;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(alarmObjectType);
        }
    }

    /**
     * alarm service dev to api
     * @param service dev
     * @return api
     */
    default OPENCONFIGALARMSEVERITY devSeverityToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.types.rev200630.OPENCONFIGALARMSEVERITY service){
        if(service==null){
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.types.rev200630.OPENCONFIGALARMSEVERITY> lnc = service.implementedInterface();
        if(lnc.getSimpleName().equals(CRITICAL.class.getSimpleName())){
            return CRITICAL.VALUE;
        }
        if(MAJOR.class.getSimpleName().equals(lnc.getSimpleName())){
            return MAJOR.VALUE;
        }
        if(WARNING.class.getSimpleName().equals(lnc.getSimpleName())){
            return WARNING.VALUE;
        }
        if(MINOR.class.getSimpleName().equals(lnc.getSimpleName())){
            return MINOR.VALUE;
        }
        if(UNKNOWN.class.getSimpleName().equals(lnc.getSimpleName())){
            return UNKNOWN.VALUE;
        }
        throw NoMatchEnumValueException.getNoMatchEnumValueException(service);
    }

    /**
     * TypeId dev to api
     * @param typeId dev
     * @return api
     */
    default AlarmState.TypeId devTypeIdToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.rev200630.AlarmState.TypeId typeId){
        if(typeId==null){
            return null;
        }
        return new AlarmState.TypeId(typeId.getString());
    }

    /**
     * AlarmObjectType dev to api
     * @param alarmObjectType dev
     * @return api
     */
    default AlarmObjectType devAlarmObjectTypeToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.types.rev200617.AlarmObjectType alarmObjectType){
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
