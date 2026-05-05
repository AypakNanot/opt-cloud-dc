///*
// * Copyright © 2019 optel and others.  All rights reserved.
// *
// * This program and the accompanying materials are made available under the
// * terms of the Eclipse Public License v1.0 which accompanies this distribution,
// * and is available at http://www.eclipse.org/legal/epl-v10.html
// */
//package com.optel.tmaster.dc.device.impl.dci.ctc.notification;
//
//import cn.hutool.core.collection.CollUtil;
//import com.optel.api.wdm.in.notification.AlarmNotification;
//import com.optel.api.wdm.service.notification.CtcDciAlarmNotification;
//import com.optel.tmaster.dc.general.base.util.CommonUtil;
//import com.optel.tmaster.dc.general.nc.nccore.notification.BaseNotificationListenerImpl;
//import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
//import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.rev200630.alarms.top.alarms.Alarm;
//import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.rev200630.alarms.top.alarms.AlarmKey;
//import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.rev200630.alarms.top.alarms.alarm.State;
//import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.AlarmsNotification;
//import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.OpenconfigSystemListener;
//import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.alarms.notification.Delete;
//import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.alarms.notification.Update;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//import java.util.*;
//import java.util.concurrent.*;
//import java.util.stream.Collectors;
//
///**
// * 告警上报通知
// * 2022/3/14 11:23
// *
// * @author LongXianYong
// * @version V1.0.0
// */
//public class CtcOpenConfigSystemListenerImpl extends BaseNotificationListenerImpl implements OpenconfigSystemListener {
//
//    private static final Logger LOG = LoggerFactory.getLogger(CtcOpenConfigSystemListenerImpl.class);
//
//    public CtcOpenConfigSystemListenerImpl(String nodeId) {
//        super(nodeId);
//    }
//
//    private static final BlockingQueue<AlarmNotification> QUEUE = new LinkedBlockingQueue<>();
//
//    private static final ScheduledExecutorService EXECUTOR = Executors.newSingleThreadScheduledExecutor();
//
//    static {
//        EXECUTOR.scheduleAtFixedRate(() -> {
//                    List<AlarmNotification> list = new ArrayList<>();
//                    QUEUE.drainTo(list);
//                    if (list.isEmpty()) {
//                        return;
//                    }
//                    try {
//                        RpcRfcUtil.callInvoke(CtcDciAlarmNotification.class.getName()
//                                , "alarmNotification"
//                                , new String[]{List.class.getName()}
//                                , new Object[]{list});
//                        LOG.info("Send alarm notification to wdm server count: " + list.size());
//                    } catch (Exception e) {
//                        LOG.error("Send alarm notification to wdm server error", e);
//                    }
//                },
//                1,
//                1,
//                TimeUnit.SECONDS);
//    }
//
//    private AlarmNotification switchAlarm(Alarm alarm, boolean flag) {
//        if (alarm == null) {
//            return null;
//        }
//        State state = alarm.getState();
//        AlarmNotification notification = new AlarmNotification();
//        notification.setDcNeId(this.nodeId);
//        notification.setId(state.getId());
//        notification.setAlarmCode(state.getAlarmAbbreviate());
//        notification.setAlarmType(state.getTypeId().getString());
//        notification.setObjectName(state.getResource());
//        notification.setSeverity(state.getSeverity().implementedInterface().getSimpleName());
//        notification.setText(state.getText());
//        notification.setAlarmState(flag ? 0 : 1);
//        if (state.getTimeCreated() != null) {
//            if (flag) {
//                notification.setStartTime(CommonUtil.getLongValue(state.getTimeCreated().getValue()));
//            } else {
//                notification.setEndTime(CommonUtil.getLongValue(state.getTimeCreated().getValue()));
//            }
//        }
//        return notification;
//
//    }
//
//    private List<AlarmNotification> switchAlarm(Map<AlarmKey, Alarm> map, boolean state) {
//        if (CollUtil.isEmpty(map)) {
//            return Collections.emptyList();
//        }
//        return map.values().stream().map(item -> switchAlarm(item, state)).collect(Collectors.toList());
//    }
//
//    @Override
//    public void onAlarmsNotification(AlarmsNotification notification) {
//        Delete delete = notification.getDelete();
//        Update update = notification.getUpdate();
//        if (delete != null && delete.getAlarms() != null) {
//            //消失的告警
//            List<AlarmNotification> alarmNotifications = switchAlarm(delete.getAlarms().getAlarm(), false);
//            QUEUE.addAll(alarmNotifications);
//        }
//        if (update != null && update.getAlarms() != null) {
//            //产生的告警
//            List<AlarmNotification> alarmNotifications = switchAlarm(update.getAlarms().getAlarm(), true);
//            QUEUE.addAll(alarmNotifications);
//        }
//    }
//
//}
