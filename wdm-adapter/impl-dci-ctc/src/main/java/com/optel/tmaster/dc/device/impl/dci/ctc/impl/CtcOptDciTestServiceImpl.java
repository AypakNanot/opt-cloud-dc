///*
// * Copyright © 2019 optel and others.  All rights reserved.
// *
// * This program and the accompanying materials are made available under the
// * terms of the Eclipse Public License v1.0 which accompanies this distribution,
// * and is available at http://www.eclipse.org/legal/epl-v10.html
// */
//package com.optel.tmaster.dc.device.impl.dci.ctc.impl;
//
//import com.google.common.util.concurrent.ListenableFuture;
//import com.optel.api.wdm.in.notification.EventNotification;
//import com.optel.api.wdm.service.notification.CtcDciEventNotification;
//import com.optel.tmaster.dc.dci.impl.base.dci.BaseOptDciTestServiceImpl;
//import com.optel.tmaster.dc.device.impl.dci.ctc.impl.task.AlarmReportTestThread;
//import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
//import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
//import org.eclipse.jdt.annotation.Nullable;
//import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.test.rev230605.*;
//import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.test.rev230605.report.miniotn.alarm.input.DataList;
//import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.test.rev230605.report.miniotn.alarm.input.DataListKey;
//import org.opendaylight.yangtools.yang.common.RpcResult;
//
//import java.util.Collection;
//import java.util.Map;
//
///**
// * CtcOptDciTestServiceImpl
// * 测试服务 实现类
// * date time author
// * ─────────────────────────────
// * 2023/6/5 11:15 Dengzhiming
// * Copyright (c) 2023, H-OPTEL All Rights Reserved.
// *
// * @author Dengzhiming
// * @version V1.8.0
// */
//public class CtcOptDciTestServiceImpl extends BaseOptDciTestServiceImpl implements IDeviceServiceWdmCtc {
//
//    @Override
//    public ListenableFuture<RpcResult<GenerateDciEventOutput>> generateDciEvent(GenerateDciEventInput input) {
//        EventNotification notification = new EventNotification();
//        notification.setTypeId(input.getTypeId());
//        notification.setVendorType(input.getVendorType());
//        notification.setText(input.getText());
//        notification.setId(input.getId());
//        notification.setResource(input.getResource());
//        notification.setHostname(input.getHostname());
//        notification.setEventAbbreviate(input.getEventAbbreviate());
//        notification.setTimeCreated(input.getTimeCreated());
//        notification.setDcNeId(input.getNeId());
//        notification.setSeverity(input.getSeverity());
//        try {
//            RpcRfcUtil.callInvoke
//                    (CtcDciEventNotification.class.getName()
//                            , "uploadEvent"
//                            , new String[]{com.optel.api.wdm.in.notification.EventNotification.class.getName()}
//                            , new Object[]{notification});
//        } catch (Exception e) {
//            // Ignored
//        }
//        return RpcResultUtil.success();
//    }
//
//    @Override
//    public ListenableFuture<RpcResult<ReportMiniotnEventOutput>> reportMiniotnEvent(ReportMiniotnEventInput input) {
//        return null;
//    }
//
//    @Override
//    public ListenableFuture<RpcResult<ReportMiniotnAlarmOutput>> reportMiniotnAlarm(ReportMiniotnAlarmInput input) {
//        if (input == null) {
//            return null;
//        }
//        @Nullable Map<DataListKey, DataList> map = input.getDataList();
//        Collection<DataList> dataList = map.values();
//        Thread thread = new Thread(
//                new AlarmReportTestThread(
//                        dataList, input.getAlarmSize(), input.getAlarmTime(), input.getNeId()));
//        thread.start();
//        return RpcResultUtil.success();
//    }
//
//    @Override
//    public ListenableFuture<RpcResult<ReportMiniotnTcaOutput>> reportMiniotnTca(ReportMiniotnTcaInput input) {
//        return null;
//    }
//}