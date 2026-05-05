/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.notification;

import com.optel.api.wdm.service.notification.CtcDciAlarmNotification;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.alarm.AlarmServiceTransform;
import com.optel.tmaster.dc.general.nc.nccore.notification.BaseNotificationListenerImpl;
import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.rev230426.AlarmNotification;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.rev230426.MiniotnAlarmsListener;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.rev230426.alarm.notification.Alarm;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * 极简otn告警上报通知
 * 2022/3/14 11:23
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class CmccOpenConfigSystemListenerImpl extends BaseNotificationListenerImpl implements MiniotnAlarmsListener {

    private static final Logger LOG = LoggerFactory.getLogger(CmccOpenConfigSystemListenerImpl.class);

    public CmccOpenConfigSystemListenerImpl(String nodeId) {
        super(nodeId);
    }

    private static final BlockingQueue<com.optel.api.wdm.in.notification.AlarmNotification> QUEUE = new LinkedBlockingQueue<>();

    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();

    static {
        EXECUTOR.scheduleAtFixedRate(() -> {
                    List<com.optel.api.wdm.in.notification.AlarmNotification> list = new ArrayList<>();
                    QUEUE.drainTo(list);
                    if (list.isEmpty()) {
                        return;
                    }
                    try {
                        RpcRfcUtil.callInvoke(CtcDciAlarmNotification.class.getName()
                                , "alarmNotification"
                                , new String[]{List.class.getName()}
                                , new Object[]{list});
                        LOG.info("Send alarm notification to wdm server count:{} ",list.size());
                    } catch (Exception e) {
                        LOG.error("Send alarm notification to wdm server error", e);
                    }
                },
                1,
                1,
                TimeUnit.SECONDS);
    }

    @Override
    public void onAlarmNotification(AlarmNotification notification) {
        Alarm alarm = notification.getAlarm();
        if (alarm == null || alarm.getAlarmState() == null) {
            return;
        }
        switch (alarm.getAlarmState().getName()) {
            case "end":
                //消失的告警
                QUEUE.add(switchAlarm(alarm, true));
                break;
            case "start":
                //产生的告警
                QUEUE.add(switchAlarm(alarm, false));
                break;
            default:
        }

    }

    private com.optel.api.wdm.in.notification.AlarmNotification switchAlarm(Alarm alarm, boolean isDisappear) {
        if (alarm == null) {
            return null;
        }
        com.optel.api.wdm.in.notification.AlarmNotification notification = new com.optel.api.wdm.in.notification.AlarmNotification();
        notification.setDcNeId(this.nodeId);
        notification.setId(alarm.getAlarmSerialNo().toString());
        notification.setAlarmCode(alarm.getAlarmCode());
        notification.setAlarmType(alarm.getObjectType().getName());
        notification.setObjectName(alarm.getObjectName());
        notification.setSeverity(alarm.getPerceivedSeverity().implementedInterface().getSimpleName());
        notification.setText(alarm.getAdditionalText());
        notification.setAlarmState(isDisappear ? 1 : 0);
        if (isDisappear) {
            notification.setEndTime(new AlarmServiceTransform().dateAndTimeToLong(alarm.getEndTime()));
        } else {
            notification.setStartTime(new AlarmServiceTransform().dateAndTimeToLong(alarm.getStartTime()));
        }
        return notification;
    }

}
