/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.transform.alarm;

import com.optel.tmaster.dc.device.impl.dci.ctc.transform.base.AlarmTypeTransform;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.Timeticks64;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.*;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.get.alarm.current.output.AlarmsKey;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.get.alarm.mask.output.MaskAlarms;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.get.alarm.mask.output.MaskAlarmsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.get.alarm.mask.output.MaskAlarmsKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.rev200630.alarms.mask.top.AlarmsMask;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.rev200630.alarms.mask.top.alarms.mask.AlarmMask;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.rev200630.alarms.mask.top.alarms.mask.AlarmMaskBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.rev200630.alarms.mask.top.alarms.mask.AlarmMaskKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.rev200630.alarms.mask.top.alarms.mask.alarm.mask.Config;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.rev200630.alarms.mask.top.alarms.mask.alarm.mask.ConfigBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.rev200630.alarms.top.Alarms;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.rev200630.alarms.top.alarms.Alarm;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.rev200630.alarms.top.alarms.AlarmKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.rev200630.alarms.top.alarms.alarm.State;

import java.util.*;

/**
 * AlarmServiceTransform 实现类
 * 2022/3/14 9:02
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class AlarmServiceTransform implements AlarmTypeTransform {

    public List<AlarmMask> apiAlarmMaskToDev(DeleteAlarmMaskInput input){
        if(input==null){
            return null;
        }
        @Nullable Set<String> indexList = input.getIndex();
        if(indexList==null || indexList.isEmpty()){
            return null;
        }
        List<AlarmMask> resultList = new LinkedList<>();
        for(String index :indexList){
            AlarmMaskBuilder builder = new AlarmMaskBuilder();
            builder.setIndex(index);
            resultList.add(builder.build());
        }

        return resultList;
    }

    /**
     * AlarmsMaskTop dev to api
     * @param alarmsMask dev
     * @return api
     */
    public GetAlarmMaskOutput devGetAlarmMaskOutputToApi(AlarmsMask alarmsMask){
        if(alarmsMask==null){
            return null;
        }
        @Nullable Map<AlarmMaskKey, AlarmMask> alarmMaskMap = alarmsMask.getAlarmMask();
        if(alarmMaskMap==null || alarmMaskMap.isEmpty()){
            return null;
        }
        Map<MaskAlarmsKey, MaskAlarms> map = new HashMap<>(alarmMaskMap.size());
        for(AlarmMask alarmMask:alarmMaskMap.values()){
            MaskAlarmsBuilder builder = new MaskAlarmsBuilder();
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.rev200630.alarms.mask.top.alarms.mask.alarm.mask.State state
                    = alarmMask.getState();
            if(state!=null){
                builder.setIndex(alarmMask.getIndex());
                builder.setObjectName(state.getObjectName());
                builder.setObjectRange(devAlarmObjectTypeToApi(state.getObjectRange()));
                map.put(new MaskAlarmsKey(alarmMask.getIndex()),builder.build());
            }else{
                Config config = alarmMask.getConfig();
                if(config!=null){
                    builder.setIndex(alarmMask.getIndex());
                    builder.setObjectName(config.getObjectName());
                    builder.setObjectRange(devAlarmObjectTypeToApi(config.getObjectRange()));
                    map.put(new MaskAlarmsKey(alarmMask.getIndex()),builder.build());
                }
            }


        }
        GetAlarmMaskOutputBuilder builder = new GetAlarmMaskOutputBuilder();
        builder.setMaskAlarms(map);
        return builder.build();

    }

    /**
     * Create Alarm Mask
     * @param maskAlarm api
     * @return dev
     */
    public Config apiCreateAlarmMaskToDev(org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.create.alarm.mask.input.MaskAlarms maskAlarm){
        if(maskAlarm==null){
            return null;
        }
        ConfigBuilder builder = new ConfigBuilder();
        builder.setIndex(maskAlarm.getIndex());
        builder.setObjectName(maskAlarm.getObjectName());
        builder.setObjectRange(apiAlarmObjectTypeToDev(maskAlarm.getObjectRange()));
        return builder.build();
    }

    /**
     * systemTop dev to api
     * @param alarms dev
     * @return api
     */
    public GetAlarmCurrentOutput devGetAlarmCurrentOutputToApi(Alarms alarms){
        if(alarms==null){
            return null;
        }
        @Nullable Map<AlarmKey, Alarm> alarmMap = alarms.getAlarm();
        if(alarmMap==null || alarmMap.isEmpty()){
            return null;
        }
        GetAlarmCurrentOutputBuilder builder = new GetAlarmCurrentOutputBuilder();
        Map<AlarmsKey
                , org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.get.alarm.current.output.Alarms> map
                = new HashMap<>(alarmMap.size());
        for(Alarm alarm :alarmMap.values()){
            State state = alarm.getState();
            if(state==null){
                continue;
            }
            org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.get.alarm.current.output.AlarmsBuilder alarmsBuilder
                    = new org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.get.alarm.current.output.AlarmsBuilder();
            alarmsBuilder.setId(alarm.getId());
            alarmsBuilder.setResource(state.getResource());
            alarmsBuilder.setText(state.getText());
            alarmsBuilder.setTimeCreated(new Timeticks64(state.getTimeCreated().getValue()));
            alarmsBuilder.setSeverity(devSeverityToApi(state.getSeverity()));
            alarmsBuilder.setTypeId(devTypeIdToApi(state.getTypeId()));
            alarmsBuilder.setAlarmAbbreviate(state.getAlarmAbbreviate());
            map.put(new AlarmsKey(alarm.getId()),alarmsBuilder.build());
        }
        builder.setAlarms(map);
        return builder.build();

    }
}
