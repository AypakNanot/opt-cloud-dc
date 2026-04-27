/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.base.listener;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;
import java.util.Date;

/**
 * 设备掉电而提供的UDP
 * 2020/11/3 11:33
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class UdpChannel implements Runnable{
    private static final Logger LOG = LoggerFactory.getLogger(UdpChannel.class);
    private static final int DEFAULT_PORT = 5001;
    private static final int RECEIVE_BUFFER_SIZE = 2048;
    private static final String DEFAULT_CTC_ALARM_CODE="PWR_NO_INPUT#";
    private static final String DEFAULT_CMCC_ALARM_CODE="PNI";
    private static final String ALARM_CODE="PWR_NO_INPUT";


/*    private AlarmReport buildAlarm(String uuid){
        AlarmReport alarmReport = new AlarmReport();
        alarmReport.setDcNeId(uuid);
        alarmReport.setAdditionalText(ALARM_CODE);
        alarmReport.setAlarmCode(ALARM_CODE);
        alarmReport.setAlarmSerialNo(101101L);
        alarmReport.setAlarmState(0);
        alarmReport.setStartTime(currentTime());
        alarmReport.setObjectType("ME");
        alarmReport.setPerceivedSeverity(0);
        return alarmReport;
    }*/

    /**
     * 获取一个当前时间戳
     * @return time
     */
    private long currentTime() {
        Date date = new Date(System.currentTimeMillis());
        return date.getTime()/1000;
    }

    /*private void reportAlarm(byte[] data,String defaultCode){
        String s = new String(data,0,data.length);
        s= s.trim();
        //构造掉电告警通知
        LOG.info("=============================udp info======================="+s);
        if(s.contains(defaultCode)){
            //填充 uuid
            int index = s.indexOf(defaultCode) + defaultCode.length();
            if(index<s.length()){
                String uuid = s.substring(index);
                //直接构建AlarmReport
                AlarmReport alarmReport = buildAlarm(uuid);
                RpcRfcUtil.callInvoke(AccAlarmNotification.class.getName()
                        ,"reportAlarmNotification"
                        ,new String[]{AlarmReport.class.getName()}
                        ,new Object[]{alarmReport});
            }
        }
    }*/

    @Override
    @SuppressWarnings("squid:S2189")
    public void run() {
        try {
            DatagramSocket socket = new DatagramSocket(DEFAULT_PORT);
            //端口暴露 5001
            LOG.debug("udp channel is start");
            while (true){
                try {
                    byte[] arr = new byte[RECEIVE_BUFFER_SIZE];
                    DatagramPacket dp = new DatagramPacket(arr,0,arr.length);
                    socket.receive(dp);
                    byte[] data = dp.getData();
//                    reportAlarm(data,DEFAULT_CTC_ALARM_CODE);
//                    reportAlarm(data,DEFAULT_CMCC_ALARM_CODE);
                }catch (Exception e){
                    // 防止出现异常后 监听中断
                    e.printStackTrace();
                }
            }
        } catch (SocketException e) {
            LOG.error(this+"udp socket is error");
        }
    }
}
