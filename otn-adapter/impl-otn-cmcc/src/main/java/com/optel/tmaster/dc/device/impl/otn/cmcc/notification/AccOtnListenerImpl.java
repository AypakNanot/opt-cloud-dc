///*
// * Copyright © 2019 optel and others.  All rights reserved.
// *
// * This program and the accompanying materials are made available under the
// * terms of the Eclipse Public License v1.0 which accompanies this distribution,
// * and is available at http://www.eclipse.org/legal/epl-v10.html
// */
//package com.optel.tmaster.dc.device.impl.otn.cmcc.notification;
//
//import com.optel.api.otn.in.notification.AccOtnOduflexNotification;
//import com.optel.api.otn.service.notification.AccOtnNotification;
//import com.optel.tmaster.dc.general.nc.nccore.notification.BaseNotificationListenerImpl;
//import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
//import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.AccOtnListener;
//import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.OduflexNotification;
//
///**
// * <ul>
// * <li>(接收和处理 acc-otn 中定义的通知)</li>
// * </ul>
// *
// * @author LWX 2020/5/4 15:10
// */
//public class AccOtnListenerImpl extends BaseNotificationListenerImpl implements AccOtnListener {
//    public AccOtnListenerImpl(String nodeId) {
//        super(nodeId);
//    }
//
//    /**
//     * DOU flex带宽调整通知
//     */
//    @Override
//    public void onOduflexNotification(OduflexNotification notification) {
//        if(notification == null || notification.getCtpName() == null || notification.getCtpName().isEmpty()){
//            return;
//        }
//        AccOtnOduflexNotification accOtnOduflexNotification = new AccOtnOduflexNotification();
//        accOtnOduflexNotification.setDcNeId(this.nodeId);
//        accOtnOduflexNotification.setCtpName(notification.getCtpName());
//        accOtnOduflexNotification.setModifyResult(notification.getModifyResult().getName());
//        accOtnOduflexNotification.setOduflexAdjustSerialNo(notification.getOduflexAdjustSerialNo().longValue());
//        //发送通知至OTN-Server
//        RpcRfcUtil.callInvoke(AccOtnNotification.class.getName()
//                ,"onOduflexNotification"
//                ,new String[]{AccOtnOduflexNotification.class.getName()}
//                ,new Object[]{accOtnOduflexNotification});
//    }
//
//}
