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
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.ServiceTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.alarm.rev200427.ModifyAlarmAttrNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.alarm.rev200427.SetAlarmMaskBatchInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.alarm.rev200427.SetAlarmMaskStatusInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.alarm.rev200427.set.alarm.mask.status.input.SetAlarmsMaskStatus;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.alarms.grouping.Alarm;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.alarms.grouping.AlarmBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.alarms.grouping.AlarmKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.alarms.mask.state.grouping.AlarmMaskState;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.alarms.mask.state.grouping.AlarmMaskStateKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.tcas.grouping.TcaBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.tcas.grouping.TcaKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.alarm.attrs.grouping.AlarmAttr;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.alarm.attrs.grouping.AlarmAttrBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.alarm.attrs.grouping.AlarmAttrKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.alarms.mask.state.grouping.AlarmMaskStateBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.SetAlarmAttrInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.AlarmMaskStatesBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.tcas.grouping.Tca;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.tca.TcaParameterBuilder;

import java.util.LinkedList;
import java.util.List;

/**
 * alarm 转换器
 * 2021/10/14 13:51
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class AlarmTransformImpl extends AbstractCmccTransformer implements CommonTransform, EnumTransform, ServiceTransform {
    public List<AlarmAttr> devAlarmAttrToApiList(List<
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.alarm.attrs.grouping.AlarmAttr> list){
        if(list==null || list.isEmpty()){
            return null;
        }
        List<AlarmAttr> resultList = new LinkedList<>();
        for(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.alarm.attrs.grouping.AlarmAttr e : list){
            resultList.add(devAlarmAttrToApi(e).build());
        }
        return resultList;
    }

    public AlarmAttrBuilder devAlarmAttrToApi(
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.alarm.attrs.grouping.AlarmAttr alarmAttr){
        if(alarmAttr==null){
            return null;
        }
        AlarmAttrBuilder alarmAttrBuilder = new AlarmAttrBuilder();
        alarmAttrBuilder.withKey(new AlarmAttrKey(alarmAttr.getAlarmCode(),alarmAttr.getObjectName()))
                .setObjectName(alarmAttr.getObjectName())
                .setAlarmCode(alarmAttr.getAlarmCode())
                .setAppearDelayTime(alarmAttr.getAppearDelayTime())
                .setDisappearDelayTime(alarmAttr.getDisappearDelayTime())
                .setObjectType(alarmAttr.getObjectType())
                .setPerceivedSeverity(devPerceivedSeverityToApi(alarmAttr.getPerceivedSeverity()));
        return alarmAttrBuilder;
    }

    public List<AlarmMaskState> devAlarmMaskStateToApi(
            List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.alarm.mask.states.AlarmMaskState> list){
        if(list==null || list.isEmpty()){
            return null;
        }
        List<AlarmMaskState> resultList = new LinkedList<>();
        for(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.alarm.mask.states.AlarmMaskState e : list){
            resultList.add(devAlarmMaskStateToApi(e).build());
        }
        return resultList;
    }

    public AlarmMaskStateBuilder devAlarmMaskStateToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.alarm.mask.states.AlarmMaskState alarmMaskState){
        if(alarmMaskState==null){
            return null;
        }
        AlarmMaskStateBuilder alarmMaskStateBuilder = new AlarmMaskStateBuilder();
        alarmMaskStateBuilder.withKey(new AlarmMaskStateKey(alarmMaskState.getAlarmCode(),alarmMaskState.getObjectName()))
                .setObjectName(alarmMaskState.getObjectName())
                .setObjectType(alarmMaskState.getObjectType())
                .setAlarmCode(alarmMaskState.getAlarmCode())
                .setMaskState(alarmMaskState.getMaskState());
        return alarmMaskStateBuilder;
    }

    public AlarmMaskStatesBuilder apiAlarmMaskStatesToDev(SetAlarmMaskStatusInput input){
        if(input==null){
            return null;
        }
        if(input.getSetAlarmsMaskStatus()==null || input.getSetAlarmsMaskStatus().isEmpty()){
            return null;
        }

        AlarmMaskStatesBuilder builders = new AlarmMaskStatesBuilder();
        List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.alarm.mask.states.AlarmMaskState> list = new LinkedList<>();

        for(SetAlarmsMaskStatus e:input.getSetAlarmsMaskStatus().values()){
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.alarm.mask.states.AlarmMaskStateBuilder builder
                    = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.alarm.mask.states.AlarmMaskStateBuilder();
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.alarm.mask.states.AlarmMaskStateKey key
                    = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.alarm.mask.states.AlarmMaskStateKey(
                            e.getAlarmCode(),e.getObjectName());
            builder.withKey(key)
                    .setAlarmCode(e.getAlarmCode())
                    .setObjectName(e.getObjectName())
                    .setObjectType(e.getObjectType())
                    .setMaskState(e.getMaskState());
            list.add(builder.build());
        }
        builders.setAlarmMaskState(ltm(list));
        return builders;
    }

    public List<Tca> devTcaToApiList(List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.tcas.grouping.Tca> list){
        if(list==null || list.isEmpty()){
            return null;
        }
        List<Tca> resultList = new LinkedList<>();
        for(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.tcas.grouping.Tca e : list){
            resultList.add(devTcaToApi(e).build());
        }
        return resultList;
    }

    public TcaBuilder devTcaToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.tcas.grouping.Tca tca){
        if(tca==null){
            return null;
        }
        TcaBuilder tcaBuilder = new TcaBuilder();
        tcaBuilder.withKey(new TcaKey(tca.getTcaSerialNo()))
                .setCurrentValue(devRealToApi(tca.getCurrentValue()))
                .setEndTime(tca.getEndTime())
                .setStartTime(tca.getStartTime())
                .setTcaParameter(devHistoryTcaParameterBuilder(tca.getTcaParameter()).build())
                .setTcaSerialNo(tca.getTcaSerialNo())
                .setTcaState(devAlarmStateToApi(tca.getTcaState()));
        return tcaBuilder;
    }

    public TcaParameterBuilder devHistoryTcaParameterBuilder(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.tca.TcaParameter tcaParameter){
        if(tcaParameter==null){
            return null;
        }
        TcaParameterBuilder tcaParameterBuilder = new TcaParameterBuilder();
        tcaParameterBuilder.setGranularity(devGranularityToApi(tcaParameter.getGranularity()))
                .setObjectName(tcaParameter.getObjectName())
                .setObjectType(tcaParameter.getObjectType())
                .setPmParameterName(tcaParameter.getPmParameterName())
                .setThresholdValue(devRealToApi(tcaParameter.getThresholdValue()))
                .setThresholdType(devThresholdTypeToApi(tcaParameter.getThresholdType()));
        return tcaParameterBuilder;
    }

    public SetAlarmAttrInputBuilder apiAlarmAttrToDev(ModifyAlarmAttrNeInput input){
        if(input==null){
            return null;
        }
        SetAlarmAttrInputBuilder builder = new SetAlarmAttrInputBuilder();
        builder.setObjectName(
                input.getObjectName()).setAlarmCode(input.getAlarmCode())
                .setPerceivedSeverity(apiPerceivedSeverityToDev(input.getPerceivedSeverity()))
                .setAppearDelayTime(input.getAppearDelayTime())
                .setDisappearDelayTime(input.getDisappearDelayTime());
        return builder;
    }

    public List<Alarm> devAlarmToApiList(List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.alarms.grouping.Alarm> list){
        if(list==null || list.isEmpty()){
            return null;
        }
        List<Alarm> resultList = new LinkedList<>();
        for(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.alarms.grouping.Alarm e : list){
            resultList.add(devAlarmToApi(e).build());
        }
        return resultList;
    }

    public AlarmBuilder devAlarmToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.alarms.grouping.Alarm alarm){
        if(alarm==null){
            return null;
        }
        AlarmBuilder alarmBuilder = new AlarmBuilder();
        alarmBuilder.withKey(new AlarmKey(alarm.getAlarmSerialNo()))
                .setAdditionalText(alarm.getAdditionalText())
                .setAlarmCode(alarm.getAlarmCode())
                .setAlarmSerialNo(alarm.getAlarmSerialNo())
                .setAlarmState(devAlarmStateToApi(alarm.getAlarmState()))
                .setEndTime(alarm.getEndTime())
                .setObjectName(alarm.getObjectName())
                .setObjectType(alarm.getObjectType())
                .setPerceivedSeverity(devPerceivedSeverityToApi(alarm.getPerceivedSeverity()))
                .setStartTime(alarm.getStartTime());
        return alarmBuilder;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn
            .extension.rev200805.SetAlarmMaskBatchInput apiAlarmMaskToDev(SetAlarmMaskBatchInput input){
        if(input==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.SetAlarmMaskBatchInputBuilder builder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.SetAlarmMaskBatchInputBuilder();
        builder.setAlarmCode(input.getAlarmCode())
                .setMaskState(input.getMaskState())
                .setObjectName(input.getObjectName())
                .setPerceivedSeverity(apiPerceivedSeverityToDev(input.getPerceivedSeverity()))
                .setObjectType(input.getObjectType());
        return builder.build();
    }
}
