/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.impl.task;

import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.test.rev230605.report.miniotn.alarm.input.DataList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;

/**
 * 告警上报测试线程
 * 2023/11/6 16:30
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class AlarmReportTestThread extends Thread {

    private static final Logger LOG = LoggerFactory.getLogger(AlarmReportTestThread.class);

    //模板数据
    Collection<DataList> dataList;
    //每秒发送数量
    Integer size;
    //持续发送时间
    Long alarmTime;

    String neId;

    public AlarmReportTestThread(Collection<DataList> dataList, Integer size, Long alarmTime, String neId){
        this.dataList = dataList;
        this.size = size;
        this.alarmTime = alarmTime;
        this.neId = neId;
    }

    @Override
    public void run() {
        super.run();
        /*List<AlarmNotification> list = new LinkedList<>();
        boolean flag = true;
        long time = (System.currentTimeMillis()*1000000)-1000000000*60*60*24;
        for(DataList data : dataList){
            AlarmNotification alarmNotification = new AlarmNotification();
            alarmNotification.setId(data.getId());
            alarmNotification.setDcNeId(neId);
            alarmNotification.setAlarmType(data.getTypeId());
            alarmNotification.setAlarmCode(data.getAlarmAbbreviate());
            alarmNotification.setText(data.getText());
            alarmNotification.setSeverity(data.getSeverity());
            alarmNotification.setStartTime(time);
            alarmNotification.setAlarmState(0);
            alarmNotification.setObjectName(data.getResource());
            list.add(alarmNotification);
        }
        for(int i=0;i<alarmTime;i++){
            try {
                long t1 = System.currentTimeMillis();
                if(i!=0){
                    //反转一次产生消失
                    for(AlarmNotification notification:list){
                        if(notification.getAlarmState()==0){
                            notification.setAlarmState(1);
                            notification.setStartTime(notification.getStartTime());
                            notification.setEndTime(notification.getStartTime()+i*1000000000);
                        }else{
                            notification.setAlarmState(0);
                            notification.setStartTime(notification.getEndTime()+i*1000000000);
                            notification.setEndTime(0);
                        }
                    }
                }
                RpcRfcUtil.callInvoke(CtcDciAlarmNotification.class.getName()
                        ,"alarmNotification"
                        ,new String[]{List.class.getName()}
                        ,new Object[]{list});
                long t2 = System.currentTimeMillis();
                long t = t2-t1;
                if(t<1000){
                    Thread.sleep(1000 - t);
                }
            }catch (Exception e){
                //
                LOG.error("auto test fail:"+list);
            }
        }
*/
    }
}
