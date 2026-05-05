/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.notification;

import com.optel.api.common.base.Constant;
import com.optel.api.wdm.in.notification.AlarmNotification;
import com.optel.api.wdm.in.notification.EventNotification;
import com.optel.api.wdm.service.notification.CtcDciAlarmNotification;
import com.optel.api.wdm.service.notification.CtcDciEventNotification;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.alarm.AlarmServiceTransform;
import com.optel.tmaster.dc.general.nc.nccore.notification.BaseNotificationListenerImpl;
import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.types.rev181121.AlarmState;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.notification.rev230426.EventType;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.notification.rev230426.event.Avc;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.Granularity;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.Real;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.ThresholdType;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.otdr.result.event.events.Event;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.MiniotnTcasListener;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.TcaNotification;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tca.notification.Tca;

import java.util.ArrayList;
import java.util.List;

/**
 * 事件通知处理
 * 2022/3/14 10:56
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class CmccOpenConfigTcaListenerImpl extends BaseNotificationListenerImpl implements MiniotnTcasListener {

    public CmccOpenConfigTcaListenerImpl(String nodeId) {
        super(nodeId);
    }

    @Override
    public void onTcaNotification(TcaNotification notification) {
        if(notification==null){
            return ;
        }
        com.optel.api.wdm.in.notification.EventNotification eventNotification = switchEvent(notification.getTca());
        if (eventNotification != null) {
            try {
                RpcRfcUtil.callInvoke
                        (CtcDciEventNotification.class.getName()
                                , "uploadEvent"
                                , new String[]{com.optel.api.wdm.in.notification.EventNotification.class.getName()}
                                , new Object[]{eventNotification});
            } catch (Exception e) {
                //Ignore
            }
        }

//        AlarmNotification tca = switchTcaToAlarm(notification.getTca());
//        List<AlarmNotification> list = new ArrayList<>();
//        list.add(tca);
//        try {
//            RpcRfcUtil.callInvoke(CtcDciAlarmNotification.class.getName()
//                    , "alarmNotification"
//                    , new String[]{List.class.getName()}
//                    , new Object[]{list});
//        } catch (Exception e) {
//            //Ignored
//        }
    }

    private EventNotification switchEvent(Tca event) {
        if (event == null) {
            return null;
        }
        com.optel.api.wdm.in.notification.EventNotification result = new com.optel.api.wdm.in.notification.EventNotification();
        result.setId(String.valueOf(event.getTcaSerialNo()));
        result.setTypeId(EventType.AttributeValueChange.getName());
        result.setResource(event.getObjectName());
        result.setVendorType(event.getObjectType().getName());
        Real currentValue = event.getCurrentValue();
        Real thresholdValue = event.getThresholdValue();
        String pmParameterName = event.getPmParameterName();
        if (Granularity._15min.equals(event.getGranularity())) {
            pmParameterName += "(15min)";
        } else if (Granularity._24h.equals(event.getGranularity())) {
            pmParameterName += "(24h)";
        }
        if (currentValue != null && thresholdValue != null) {
            ThresholdType thresholdType = event.getThresholdType();
            if (ThresholdType.High.equals(thresholdType)) {
                result.setText("性能参数名称" + Constant.COLON + pmParameterName + ", 高门限值" + Constant.COLON + thresholdValue.getDecimal64() + ", 当前性能值" + Constant.COLON + currentValue.getDecimal64());
            } else if (ThresholdType.Low.equals(thresholdType)) {
                result.setText("性能参数名称" + Constant.COLON + pmParameterName + ", 低门限值" + Constant.COLON + thresholdValue.getDecimal64() + ", 当前性能值" + Constant.COLON + currentValue.getDecimal64());
            }
        }
        result.setEventAbbreviate("TCA Event");
        if (AlarmState.Start.equals(event.getTcaState())) {
            result.setText(result.getText() + ", 产生");
            result.setTimeCreated(new AlarmServiceTransform().dateAndTimeToLong(event.getStartTime()));
        } else {
            result.setText(result.getText() + ", 消失");
            result.setTimeCreated(new AlarmServiceTransform().dateAndTimeToLong(event.getEndTime()));
        }
        result.setDcNeId(this.nodeId);
        result.setSeverity("MINOR");
        return result;
    }

    /**
     * 将极简OTN的Tca转换成网管告警数据上报
     * @param tca 设备上报的tca数据
     * @return 告警数据结构
     */
    private AlarmNotification switchTcaToAlarm(Tca tca){
        AlarmNotification alarmNotification = new AlarmNotification();
        alarmNotification.setDcNeId(this.nodeId);
        alarmNotification.setId(tca.getTcaSerialNo());
        // 将 性能名称 高低类型 时间周期 组合成tca 告警名称
        // 做一个兼容当性能值名称不带TCA时，在这里补充上，这样可以兼容告警的模板
        StringBuilder alarmCode = new StringBuilder();
        if(!tca.getPmParameterName().contains("TCA")){
            alarmCode.append("TCA_");
        }
        alarmCode.append(tca.getPmParameterName());
        if(tca.getPmParameterName().contains("_L") || tca.getPmParameterName().contains("_H")){
        }else{
            if(ThresholdType.High.equals(tca.getThresholdType())){
                alarmCode.append("_H");
            }else{
                alarmCode.append("_L");
            }
            if(Granularity._15min.equals(tca.getGranularity())){
                alarmCode.append("_15MIN");
            }else{
                alarmCode.append("_24H");
            }
        }
        alarmNotification.setAlarmCode(alarmCode.toString());
        alarmNotification.setObjectName(tca.getObjectName());
        alarmNotification.setAlarmType(tca.getObjectType().getName());
        // 在此处 默认使用次要告警这个告警等级
        alarmNotification.setSeverity("MINOR");
        // 将tca的越限值以及当前性能越限值 填在这个地方
        alarmNotification.setText("Current Value:"+tca.getCurrentValue().stringValue()
                +Constant.COMMA+"Threshold Value:"+tca.getThresholdValue().stringValue());
        switch (tca.getTcaState().getName()) {
            case "end":
                alarmNotification.setEndTime(new AlarmServiceTransform().dateAndTimeToLong(tca.getEndTime()));
                alarmNotification.setAlarmState(1);
                break;
            case "start":
                alarmNotification.setStartTime(new AlarmServiceTransform().dateAndTimeToLong(tca.getStartTime()));
                alarmNotification.setAlarmState(0);
                break;
            default:
        }
        return alarmNotification;

    }
}
