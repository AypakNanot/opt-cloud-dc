///*
// * Copyright © 2019 optel and others.  All rights reserved.
// *
// * This program and the accompanying materials are made available under the
// * terms of the Eclipse Public License v1.0 which accompanies this distribution,
// * and is available at http://www.eclipse.org/legal/epl-v10.html
// */
//package com.optel.tmaster.dc.device.impl.otn.ctc.notification;
//
//import com.optel.api.otn.in.notification.AlarmReport;
//import com.optel.api.otn.service.notification.AccAlarmNotification;
//import com.optel.tmaster.dc.general.nc.nccore.notification.BaseNotificationListenerImpl;
//import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
//import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.AccAlarmsListener;
//import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.AlarmNotification;
//import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.TcaNotification;
//import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.alarm.notification.Alarm;
//import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tca.TcaParameter;
//import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tca.notification.Tca;
//import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//
///**
// *
// * 2020/5/5 11:29
// *
// * @author lxy
// * @version V1.0.0
// */
//public class AccAlarmListenerImpl extends BaseNotificationListenerImpl implements AccAlarmsListener {
//    public AccAlarmListenerImpl(String nodeId) {
//        super(nodeId);
//    }
//
//    /**
//     * 接收性能越限通知
//     * @param notification 性能越限告警tca
//     */
//    @Override
//    public void onTcaNotification(TcaNotification notification) {
//        //2021.08.20 暂关闭tca往告警通道上报告警 避免对系统性能消耗过大
//        //  2022.12.26 I2 Otn 北向接口需要，打开Tca
//         Tca tca = notification.getTca();
//         AlarmReport alarmReport =switchTcaToReport(tca);
//         alarmReport.setDcNeId(this.nodeId);
//         RpcRfcUtil.callInvoke(AccAlarmNotification.class.getName()
//                 ,"reportAlarmNotification"
//                 ,new String[]{AlarmReport.class.getName()}
//                 ,new Object[]{alarmReport});
//    }
//
//    public AlarmReport switchTcaToReport(Tca tca){
//        AlarmReport alarmReport = new AlarmReport();
//        if(tca==null){
//            return alarmReport;
//        }
//
//        TcaParameter tcaParameter = tca.getTcaParameter();
//
//        alarmReport.setAlarmSerialNo(tca.getTcaSerialNo().longValue());
//        alarmReport.setAlarmCode("TCA");
//        alarmReport.setObjectType(tcaParameter.getObjectType());
//        // 20221221,ghl,构造TCA告警码与WDM设备统一方便I2北向上报，如：TCA_OPT_INPUT_POWER_H_15MIN
//        String codeStr = "TCA" + "_" +
//                tcaParameter.getPmParameterName() +
//                "_" +
//                (tcaParameter.getThresholdType().getIntValue() == 0 ? "L" : "H") +
//                "_" +
//                tcaParameter.getGranularity().getName();
////        alarmReport.setAdditionalText("性能越限告警");
//        alarmReport.setPerceivedSeverity(1);
//        alarmReport.setAlarmState(tca.getTcaState().getIntValue());
//        alarmReport.setObjectName(tcaParameter.getObjectName());
//        //处理告警时间
//        if(tca.getEndTime()!=null){
//            codeStr = codeStr +":end";
//            alarmReport.setStartTime(switchDateAndTimeToLong(tca.getEndTime()));
//            alarmReport.setEndTime(switchDateAndTimeToLong(tca.getEndTime())+1);
//        }else{
//            codeStr = codeStr +":start";
//            alarmReport.setStartTime(switchDateAndTimeToLong(tca.getStartTime()));
//        }
//        alarmReport.setAdditionalText(codeStr.toUpperCase());
//        return alarmReport;
//
//    }
//
//    /**
//     * 接收设备告警上报信息
//     * @param notification 设备告警信息
//     */
//    @Override
//    public void onAlarmNotification(AlarmNotification notification) {
//        Alarm alarm = notification.getAlarm();
//        AlarmReport alarmReport =switchAlarmToReport(alarm);
//        alarmReport.setDcNeId(this.nodeId);
//        // RpcRfcUtil.getRfc(AccAlarmNotification.class).reportAlarmNotification(alarmReport)
//        RpcRfcUtil.callInvoke(AccAlarmNotification.class.getName()
//                ,"reportAlarmNotification"
//                ,new String[]{AlarmReport.class.getName()}
//                ,new Object[]{alarmReport});
//    }
//
//    /**
//     * 转换器 将dc的告警数据转换成ods的信息格式
//     * @param alarm 待转告警
//     * @return 上报告警信息
//     */
//    private AlarmReport switchAlarmToReport(Alarm alarm){
//        AlarmReport alarmReport = new AlarmReport();
//        if(alarm==null){
//            return alarmReport;
//        }
//        alarmReport.setAlarmSerialNo(alarm.getAlarmSerialNo().longValue());
//        alarmReport.setAlarmCode(alarm.getAlarmCode());
//        alarmReport.setAdditionalText(alarm.getAdditionalText());
//        alarmReport.setObjectType(alarm.getObjectType());
//        alarmReport.setPerceivedSeverity(alarm.getPerceivedSeverity().getIntValue());
//        alarmReport.setAlarmState(alarm.getAlarmState().getIntValue());
//        alarmReport.setObjectName(alarm.getObjectName());
//        //处理告警时间
//        alarmReport.setStartTime(switchDateAndTimeToLong(alarm.getStartTime()));
//        if(alarm.getEndTime()!=null){
//            alarmReport.setEndTime(switchDateAndTimeToLong(alarm.getEndTime()));
//        }
//        return alarmReport;
//    }
//
//    /**
//     * DateAndTime转long
//     * @param dateAndTime 待转换的数据
//     * @return long数据
//     */
//    private long switchDateAndTimeToLong(DateAndTime dateAndTime) {
//        String formatter = "yyyy-MM-dd'T'HH:mm:ss";
//        SimpleDateFormat sdf = new SimpleDateFormat(formatter);
//        Date dt = new Date();
//        try {
//            dt = sdf.parse(dateAndTime.getValue());
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
//        return dt.getTime()/1000;
//    }
//}
