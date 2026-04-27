/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.api.wdm.in.notification.EventNotification;
import com.optel.api.wdm.service.notification.CtcDciEventNotification;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseOptDciTestServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.notification.CmccOpenConfigEventListenerImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.notification.CmccOpenConfigSystemListenerImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.notification.CmccOpenConfigTcaListenerImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.alarm.AlarmServiceTransform;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
import org.apache.commons.lang3.StrUtil;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.test.rev230605.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.notification.rev230426.EventNotificationBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.notification.rev230426.EventType;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.notification.rev230426.event.AvcBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.notification.rev230426.event.notification.EventBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.Granularity;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.ObjectType;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.ThresholdType;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.Timeticks64;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.rev230426.AlarmNotificationBuilder;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.rev230426.alarm.notification.AlarmBuilder;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.TcaNotificationBuilder;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tca.notification.TcaBuilder;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.Uint64;

/**
 * CtcOptDciTestServiceImpl
 * 测试服务 实现类
 * date time author
 * ─────────────────────────────
 * 2023/6/5 11:15 Dengzhiming
 * Copyright (c) 2023, H-OPTEL All Rights Reserved.
 *
 * @author Dengzhiming
 * @version V1.8.0
 */
public class CmccOptDciTestServiceImpl extends BaseOptDciTestServiceImpl implements IDeviceServiceWdmCmcc {

    @Override
    public ListenableFuture<RpcResult<GenerateDciEventOutput>> generateDciEvent(GenerateDciEventInput input) {
        EventNotification notification = new EventNotification();
        notification.setTypeId(input.getTypeId());
        notification.setVendorType(input.getVendorType());
        notification.setText(input.getText());
        notification.setId(input.getId());
        notification.setResource(input.getResource());
        notification.setHostname(input.getHostname());
        notification.setEventAbbreviate(input.getEventAbbreviate());
        notification.setTimeCreated(input.getTimeCreated());
        notification.setDcNeId(input.getNeId());
        notification.setSeverity(input.getSeverity());
        try {
            RpcRfcUtil.callInvoke
                    (CtcDciEventNotification.class.getName()
                            , "uploadEvent"
                            , new String[]{EventNotification.class.getName()}
                            , new Object[]{notification});
        } catch (Exception e) {
            // Ignored
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ReportMiniotnEventOutput>> reportMiniotnEvent(ReportMiniotnEventInput input) {
        if (input == null) {
            return null;
        }
        EventNotificationBuilder eventNotificationBuilder
                = new EventNotificationBuilder();
        EventBuilder eventBuilder = new EventBuilder();
        AvcBuilder avcBuilder = new AvcBuilder();
        avcBuilder.setAttributeName(input.getAttributeName());
        avcBuilder.setNewAttributeValue(input.getNewValue());
        avcBuilder.setOldAttributeValue(input.getOldValue());
        eventBuilder.setAvc(avcBuilder.build());
        eventBuilder.setEventSerialNo(Uint64.valueOf(input.getEventSerialNo()));
        eventBuilder.setEventType(EventType.AttributeValueChange);
        eventBuilder.setObjectName(input.getObjectName());
        eventBuilder.setObjectType(ObjectType.valueOf(input.getObjectType()));
        eventBuilder.setEventAbbr(input.getEventAbbr());
        eventBuilder.setTimeCreated(new Timeticks64(Uint64.valueOf(input.getTimeCreated())));
        eventNotificationBuilder.setEvent(eventBuilder.build());
        new CmccOpenConfigEventListenerImpl(input.getNeId()).onEventNotification(eventNotificationBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ReportMiniotnAlarmOutput>> reportMiniotnAlarm(ReportMiniotnAlarmInput input) {
        if (input == null) {
            return null;
        }
        AlarmNotificationBuilder alarmNotificationBuilder = new AlarmNotificationBuilder();
        AlarmBuilder alarmBuilder = new AlarmBuilder();
        alarmBuilder.setAdditionalText(input.getAdditionalText());
        alarmBuilder.setAlarmCode(input.getAlarmCode());
        alarmBuilder.setAlarmSerialNo(Uint64.valueOf(input.getAlarmSerialNo()));
        alarmBuilder.setAlarmState(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.types.rev181121.AlarmState.Start);
        if (StrUtil.isNotEmpty(input.getEndTime())) {
            alarmBuilder.setEndTime(new DateAndTime(input.getEndTime()));
        }
        alarmBuilder.setObjectName(input.getObjectName());
        alarmBuilder.setObjectType(ObjectType.ME);
//        alarmBuilder.setPerceivedSeverity(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.types.rev181121.MAJOR.class);
        alarmBuilder.setStartTime(new DateAndTime(input.getStartTime()));
        alarmNotificationBuilder.setAlarm(alarmBuilder.build());
        new CmccOpenConfigSystemListenerImpl(input.getNeId()).onAlarmNotification(alarmNotificationBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ReportMiniotnTcaOutput>> reportMiniotnTca(ReportMiniotnTcaInput input) {
        if (input == null) {
            return null;
        }
        TcaNotificationBuilder tcaNotificationBuilder = new TcaNotificationBuilder();
        TcaBuilder tcaBuilder = new TcaBuilder();
        tcaBuilder.setTcaSerialNo(input.getAlarmSerialNo());
        tcaBuilder.setTcaState(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.types.rev181121.AlarmState.Start);
//        tcaBuilder.setCurrentValue(new Real(BigDecimal.ONE));
        if (StrUtil.isNotEmpty(input.getEndTime())) {
            tcaBuilder.setEndTime(new DateAndTime(input.getEndTime()));
        }
        tcaBuilder.setGranularity(Granularity._15min);
        tcaBuilder.setObjectName(input.getObjectName());
        tcaBuilder.setObjectType(new AlarmServiceTransform().apiTcaObjectTypeToDev(input.getObjectType()));
        tcaBuilder.setPmParameterName(input.getPmParameterName());
        tcaBuilder.setStartTime(new DateAndTime(input.getStartTime()));
        tcaBuilder.setThresholdType(ThresholdType.Low);
//        tcaBuilder.setThresholdValue(new Real(BigDecimal.ONE));
        tcaNotificationBuilder.setTca(tcaBuilder.build());
        new CmccOpenConfigTcaListenerImpl(input.getNeId()).onTcaNotification(tcaNotificationBuilder.build());
        return RpcResultUtil.success();
    }
}