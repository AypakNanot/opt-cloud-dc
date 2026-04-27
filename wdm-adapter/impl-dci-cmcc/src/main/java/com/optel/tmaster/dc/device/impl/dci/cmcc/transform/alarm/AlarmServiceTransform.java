/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.alarm;

import cn.hutool.core.collection.CollUtil;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base.AlarmTypeTransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base.CommonTransform;
import org.apache.commons.lang3.StrUtil;
import org.eclipse.jdt.annotation.NonNull;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.rev220208.AlarmState.TypeId;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.rev220208.tcas.top.TcaBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.Granularity;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ThresholdType;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.Timeticks64;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.*;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.get.alarm.mask.output.MaskAlarms;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.get.alarm.mask.output.MaskAlarmsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.get.alarm.mask.output.MaskAlarmsKey;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.query.alarm.history.output.AlarmsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.query.alarm.history.output.AlarmsKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.types.rev181121.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.ObjectType;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.mask.rev230426.alarms.mask.top.AlarmsMask;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.mask.rev230426.alarms.mask.top.alarms.mask.AlarmMask;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.mask.rev230426.alarms.mask.top.alarms.mask.AlarmMaskKey;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.mask.rev230426.alarms.mask.top.alarms.mask.alarm.mask.Config;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.mask.rev230426.alarms.mask.top.alarms.mask.alarm.mask.ConfigBuilder;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.mask.rev230426.alarms.mask.top.alarms.mask.alarm.mask.State;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.rev230426.Alarms;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.rev230426.alarms.top.Alarm;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.rev230426.alarms.top.AlarmKey;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.GetHistoryAlarmsInput;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.GetHistoryAlarmsInputBuilder;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tca.top.Tcas;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tcas.top.Tca;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tcas.top.TcaKey;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime;
import org.opendaylight.yangtools.yang.common.Uint64;

import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

/**
 * AlarmServiceTransform 实现类
 * 2022/3/14 9:02
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class
AlarmServiceTransform implements AlarmTypeTransform, CommonTransform {

    public QueryAlarmHistoryOutput devToQueryAlarmHistoryOutput(org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.get.history.alarms.output.Alarms alarms) {
        if (null == alarms) {
            return null;
        }
        Map<AlarmKey, Alarm> alarmMap = alarms.getAlarm();
        if (CollUtil.isEmpty(alarmMap)) {
            return null;
        }
        QueryAlarmHistoryOutputBuilder outputBuilder = new QueryAlarmHistoryOutputBuilder();
        Map<AlarmsKey, org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.query.alarm.history.output.Alarms> map = new HashMap<>();
        for (Alarm alarm : alarmMap.values()) {
            AlarmsBuilder alarmsBuilder = new AlarmsBuilder();
            alarmsBuilder.setAlarmAbbreviate(alarm.getAlarmCode());
            alarmsBuilder.setId(alarm.getAlarmSerialNo().toString());
            alarmsBuilder.setResource(alarm.getObjectName());
            alarmsBuilder.setSeverity(devPerceivedSeverityToApi(alarm.getPerceivedSeverity()));
            alarmsBuilder.setText(alarm.getAdditionalText());
            alarmsBuilder.setTimeCreated(devTimeToApi(alarm.getStartTime()));
            alarmsBuilder.setEndTime(devTimeToApi(alarm.getEndTime()));
            alarmsBuilder.setTypeId(new TypeId(alarm.getObjectType().getName()));
            map.put(new AlarmsKey(alarm.getAlarmSerialNo().toString()), alarmsBuilder.build());
        }
        outputBuilder.setAlarms(map);
        return outputBuilder.build();
    }

    public QueryTcaHistoryOutput devToQueryAlarmHistoryOutput(org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.get.history.alarms.output.Tcas tcas) {
        if (null == tcas) {
            return null;
        }
        Map<TcaKey, Tca> tcaMap = tcas.getTca();
        if (CollUtil.isEmpty(tcaMap)) {
            return null;
        }
        QueryTcaHistoryOutputBuilder outputBuilder = new QueryTcaHistoryOutputBuilder();
        Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.rev220208.tcas.top.TcaKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.rev220208.tcas.top.Tca> map = new HashMap<>();
        for (Tca tca : tcaMap.values()) {
            TcaBuilder tcaBuilder = new TcaBuilder();
            tcaBuilder.setAlarmSerialNo(tca.getTcaSerialNo());
            tcaBuilder.setAlarmState(devTcaAlarmStateToApi(tca.getTcaState()));
            tcaBuilder.setCurrentValue(devTcaRealToApi(tca.getCurrentValue()));
            tcaBuilder.setEndTime(devTcaDateTimeToApi(tca.getEndTime()));
            tcaBuilder.setGranularity(devTcaGranularityToApi(tca.getGranularity()));
            tcaBuilder.setObjectName(tca.getObjectName());
            tcaBuilder.setObjectType(devTcaObjectTypeToApi(tca.getObjectType()));
            tcaBuilder.setPmParameterName(tca.getPmParameterName());
            tcaBuilder.setStartTime(devTcaDateTimeToApi(tca.getStartTime()));
            tcaBuilder.setThresholdType(devTcaThresholdTypeToApi(tca.getThresholdType()));
            tcaBuilder.setThresholdValue(devTcaRealToApi(tca.getThresholdValue()));
            map.put(new org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.rev220208.tcas.top.TcaKey(tca.getTcaSerialNo()), tcaBuilder.build());
        }
        outputBuilder.setTca(map);
        return outputBuilder.build();
    }

    public GetHistoryAlarmsInput apiToGetHistoryAlarmsInput(QueryAlarmHistoryInput input) {
        if (null == input) {
            return null;
        }
        GetHistoryAlarmsInputBuilder getHistoryAlarmsInputBuilder = new GetHistoryAlarmsInputBuilder();
        if (null != input.getEndTime()) {
            getHistoryAlarmsInputBuilder.setEndTime(new DateAndTime(input.getEndTime().getValue()));
        }
        if (null != input.getStartTime()) {
            getHistoryAlarmsInputBuilder.setStartTime(new DateAndTime(input.getStartTime().getValue()));
        }
        return getHistoryAlarmsInputBuilder.build();
    }

    public GetHistoryAlarmsInput apiToGetHistoryAlarmsInput(QueryTcaHistoryInput input) {
        if (null == input) {
            return null;
        }
        GetHistoryAlarmsInputBuilder getHistoryAlarmsInputBuilder = new GetHistoryAlarmsInputBuilder();
        if (null != input.getEndTime()) {
            getHistoryAlarmsInputBuilder.setEndTime(new DateAndTime(input.getEndTime().getValue()));
        }
        if (null != input.getStartTime()) {
            getHistoryAlarmsInputBuilder.setStartTime(new DateAndTime(input.getStartTime().getValue()));
        }
        return getHistoryAlarmsInputBuilder.build();
    }

    /**
     * AlarmsMaskTop dev to api
     *
     * @param alarmsMask dev
     * @return api
     */
    public GetAlarmMaskOutput devGetAlarmMaskOutputToApi(AlarmsMask alarmsMask) {
        if (alarmsMask == null) {
            return null;
        }
        @Nullable Map<AlarmMaskKey, AlarmMask> alarmMaskMap = alarmsMask.getAlarmMask();
        if (alarmMaskMap == null || alarmMaskMap.isEmpty()) {
            return null;
        }
        Map<MaskAlarmsKey, MaskAlarms> map = new HashMap<>(alarmMaskMap.size());
        for (AlarmMask alarmMask : alarmMaskMap.values()) {
            MaskAlarmsBuilder builder = new MaskAlarmsBuilder();
            State state = alarmMask.getState();
            if (state != null) {
                builder.setIndex(alarmMask.getIndex());
                builder.setObjectName(state.getObjectName());
                builder.setObjectRange(devAlarmObjectTypeToApi(state.getObjectRange()));
                map.put(new MaskAlarmsKey(alarmMask.getIndex()), builder.build());
            } else {
                Config config = alarmMask.getConfig();
                if (config != null) {
                    builder.setIndex(alarmMask.getIndex());
                    builder.setObjectName(config.getObjectName());
                    builder.setObjectRange(devAlarmObjectTypeToApi(config.getObjectRange()));
                    map.put(new MaskAlarmsKey(alarmMask.getIndex()), builder.build());
                }
            }
        }
        GetAlarmMaskOutputBuilder builder = new GetAlarmMaskOutputBuilder();
        builder.setMaskAlarms(map);
        return builder.build();

    }

    /**
     * Create Alarm Mask
     *
     * @param maskAlarm api
     * @return dev
     */
    public Config apiCreateAlarmMaskToDev(org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.create.alarm.mask.input.MaskAlarms maskAlarm) {
        if (maskAlarm == null) {
            return null;
        }
        ConfigBuilder builder = new ConfigBuilder();
        builder.setIndex(maskAlarm.getIndex());
        builder.setObjectName(maskAlarm.getObjectName());
        builder.setObjectRange(apiAlarmObjectTypeToDev(maskAlarm.getObjectRange()));
        return builder.build();
    }


    public GetAlarmCurrentOutput devGetAlarmCurrentOutputToApi(Alarms alarms) {
        if (alarms == null) {
            return null;
        }
        @NonNull Map<AlarmKey, Alarm> alarmMap = alarms.getAlarm();
        if (alarmMap == null || alarmMap.isEmpty()) {
            return null;
        }
        GetAlarmCurrentOutputBuilder builder = new GetAlarmCurrentOutputBuilder();
        Map<org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.get.alarm.current.output.AlarmsKey, org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.get.alarm.current.output.Alarms> map = new HashMap<>(alarmMap.size());
        for (Alarm alarm : alarmMap.values()) {
            AlarmState state = alarm.getAlarmState();
            if (state == null) {
                continue;
            }
            org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.get.alarm.current.output.AlarmsBuilder alarmsBuilder = new org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.get.alarm.current.output.AlarmsBuilder();
            alarmsBuilder.setAlarmAbbreviate(alarm.getAlarmCode());
            alarmsBuilder.setId(alarm.getAlarmSerialNo().toString());
            alarmsBuilder.setResource(alarm.getObjectName());
            alarmsBuilder.setSeverity(devPerceivedSeverityToApi(alarm.getPerceivedSeverity()));
            alarmsBuilder.setText(alarm.getAdditionalText());
            alarmsBuilder.setTimeCreated(devTimeToApi(alarm.getStartTime()));
            alarmsBuilder.setTypeId(devObjectTypeToApi(alarm.getObjectType()));
            map.put(new org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.get.alarm.current.output.AlarmsKey(alarm.getAlarmSerialNo().toString()), alarmsBuilder.build());
        }
        builder.setAlarms(map);
        return builder.build();
    }

    Timeticks64 devTimeToApi(DateAndTime dateAndTime) {
        if (!(dateAndTime != null && StrUtil.isNotEmpty(dateAndTime.getValue()))) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(dateAndTime.getValue(), formatter);
        long second = localDateTime.toEpochSecond(ZoneOffset.ofHours(8)) * 1000000000;
        return new Timeticks64(Uint64.valueOf(second));
    }

    public Long dateAndTimeToLong(DateAndTime dateAndTime) {
        if (dateAndTime == null) {
            return null;
        }
        DateTimeFormatter formatter = DateTimeFormatter.ISO_ZONED_DATE_TIME;
        LocalDateTime localDateTime = LocalDateTime.parse(dateAndTime.getValue(), formatter);
        return localDateTime.toEpochSecond(ZoneOffset.ofHours(8)) * 1000000000;
    }


    /**
     * TypeId dev to api
     *
     * @param objectType dev
     * @return api
     */
    private TypeId devObjectTypeToApi(
            ObjectType objectType) {
        if (objectType == null) {
            return null;
        }
        return new TypeId(objectType.getName());
    }


    /**
     * 告警级别 dev to api
     *
     * @param perceivedSeverity dev
     * @return api
     */
    public org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.OPENCONFIGALARMSEVERITY devPerceivedSeverityToApi(
            OPENCONFIGALARMSEVERITY perceivedSeverity) {
        if (perceivedSeverity == null) {
            return null;
        }
        Class<? extends OPENCONFIGALARMSEVERITY> lnc = perceivedSeverity.implementedInterface();
        if (lnc.equals(CRITICAL.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.CRITICAL.VALUE;
        }
        if (lnc.equals(MAJOR.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.MAJOR.VALUE;
        }
        if (lnc.equals(MINOR.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.MINOR.VALUE;
        }
        if (lnc.equals(WARNING.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.WARNING.VALUE;
        }
        if (lnc.equals(UNKNOWN.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.UNKNOWN.VALUE;
        }
        throw getNoMatchEnumValueException(perceivedSeverity);
    }


    public QueryTcasOutput devCurrentTcaAlarmToApi(Tcas tcas) {
        if (tcas == null || CollUtil.isEmpty(tcas.getTca())) {
            return null;
        }
        Map<TcaKey, Tca> tcaMap = tcas.getTca();
        QueryTcasOutputBuilder outputBuilder = new QueryTcasOutputBuilder();
        Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.rev220208.tcas.top.TcaKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.rev220208.tcas.top.Tca> map = new HashMap<>();
        for (Tca tca : tcaMap.values()) {
            TcaBuilder tcaBuilder = new TcaBuilder();
            tcaBuilder.setAlarmSerialNo(tca.getTcaSerialNo());
            tcaBuilder.setAlarmState(devTcaAlarmStateToApi(tca.getTcaState()));
            tcaBuilder.setCurrentValue(devTcaRealToApi(tca.getCurrentValue()));
            tcaBuilder.setEndTime(devTcaDateTimeToApi(tca.getEndTime()));
            tcaBuilder.setGranularity(devTcaGranularityToApi(tca.getGranularity()));
            tcaBuilder.setObjectName(tca.getObjectName());
            tcaBuilder.setObjectType(devTcaObjectTypeToApi(tca.getObjectType()));
            tcaBuilder.setPmParameterName(tca.getPmParameterName());
            tcaBuilder.setStartTime(devTcaDateTimeToApi(tca.getStartTime()));
            tcaBuilder.setThresholdType(devTcaThresholdTypeToApi(tca.getThresholdType()));
            tcaBuilder.setThresholdValue(devTcaRealToApi(tca.getThresholdValue()));
            map.put(new org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.rev220208.tcas.top.TcaKey(tca.getTcaSerialNo()), tcaBuilder.build());
        }
        return outputBuilder.setTca(map).build();
    }

    private ThresholdType devTcaThresholdTypeToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.ThresholdType thresholdType) {
        if (thresholdType == null) {
            return null;
        }
        switch (thresholdType.getName()) {
            case "high":
                return ThresholdType.High;
            case "low":
                return ThresholdType.Low;
            default:
                return null;
        }
    }


    private org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType devTcaObjectTypeToApi(ObjectType objectType) {
        if (objectType == null) {
            return null;
        }
        switch (objectType.getName()) {
            case "ME":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.ME;
            case "CHASSIS":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.CHASSIS;
            case "SLOT":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.SLOT;
            case "CARD":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.CARD;
            case "PORT":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.PORT;
            case "TRANSCEIVER":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.TRANSCEIVER;
            case "OPTICAL-CHANNEL":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.OPTICALCHANNEL;
            case "APS":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.APS;
            case "EDFA":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.EDFA;
            case "OTU":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.OTU;
            case "ODU":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.ODU;
            case "ETH":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.ETH;
            case "SDH":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.SDH;
            case "OTDR":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.OTDR;
            case "OCM":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.OCM;
            case "INTERFACE":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.INTERFACE;
            case "OADM":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.OADM;
            case "WSS":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.WSS;
            case "FREQUENCY-CHANNEL":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType.FREQUENCYCHANNEL;
            default:
                return null;
        }
    }

    public ObjectType apiTcaObjectTypeToDev(org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.ObjectType objectType) {
        if (objectType == null) {
            return null;
        }
        switch (objectType.getName()) {
            case "ME":
                return ObjectType.ME;
            case "CHASSIS":
                return ObjectType.CHASSIS;
            case "SLOT":
                return ObjectType.SLOT;
            case "CARD":
                return ObjectType.CARD;
            case "PORT":
                return ObjectType.PORT;
            case "TRANSCEIVER":
                return ObjectType.TRANSCEIVER;
            case "OPTICAL-CHANNEL":
                return ObjectType.OPTICALCHANNEL;
            case "APS":
                return ObjectType.APS;
            case "EDFA":
                return ObjectType.EDFA;
            case "OTU":
                return ObjectType.OTU;
            case "ODU":
                return ObjectType.ODU;
            case "ETH":
                return ObjectType.ETH;
            case "SDH":
                return ObjectType.SDH;
            case "OTDR":
                return ObjectType.OTDR;
            case "OCM":
                return ObjectType.OCM;
            case "INTERFACE":
                return ObjectType.INTERFACE;
            case "OADM":
                return ObjectType.OADM;
            case "WSS":
                return ObjectType.WSS;
            case "FREQUENCY-CHANNEL":
                return ObjectType.FREQUENCYCHANNEL;
            default:
                return null;
        }
    }


    private Granularity devTcaGranularityToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.Granularity granularity) {
        if (granularity == null) {
            return null;
        }
        switch (granularity.getName()) {
            case "15min":
                return Granularity._15min;
            case "24h":
                return Granularity._24h;
            default:
                return null;
        }
    }


    private org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.yang.rev220208.DateAndTime devTcaDateTimeToApi(DateAndTime time) {
        if (time == null) {
            return null;
        }
        return new org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.yang.rev220208.DateAndTime(time.getValue());
    }


    private org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.Real devTcaRealToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.Real real) {
        if (real == null) {
            return null;
        }
        return new org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.Real(real.getDecimal64());
    }


    private org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.AlarmState devTcaAlarmStateToApi(AlarmState alarmState) {
        if (alarmState == null) {
            return null;
        }
        switch (alarmState.getName()) {
            case "start":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.AlarmState.Start;
            case "end":
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.AlarmState.End;
            default:
                return null;
        }
    }

    public AlarmState apiTcaAlarmStateToDev(org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.types.rev220208.AlarmState alarmState) {
        if (alarmState == null) {
            return null;
        }
        switch (alarmState.getName()) {
            case "start":
                return AlarmState.Start;
            case "end":
                return AlarmState.End;
            default:
                return null;
        }
    }

}
