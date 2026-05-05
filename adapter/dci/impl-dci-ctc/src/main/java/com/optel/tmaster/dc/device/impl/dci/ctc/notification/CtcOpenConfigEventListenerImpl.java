///*
// * Copyright © 2019 optel and others.  All rights reserved.
// *
// * This program and the accompanying materials are made available under the
// * terms of the Eclipse Public License v1.0 which accompanies this distribution,
// * and is available at http://www.eclipse.org/legal/epl-v10.html
// */
//package com.optel.tmaster.dc.device.impl.dci.ctc.notification;
//
//import com.optel.api.wdm.service.notification.CtcDciEventNotification;
//import com.optel.tmaster.dc.general.nc.nccore.notification.BaseNotificationListenerImpl;
//import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
//import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.event.rev200630.EventNotification;
//import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.event.rev200630.OpenconfigEventListener;
//import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.event.rev200630.event.notification.Events;
//
///**
// * 事件通知处理
// * 2022/3/14 10:56
// *
// * @author LongXianYong
// * @version V1.0.0
// */
//public class CtcOpenConfigEventListenerImpl extends BaseNotificationListenerImpl implements OpenconfigEventListener {
//
//    public CtcOpenConfigEventListenerImpl(String nodeId) {
//        super(nodeId);
//    }
//
//    @Override
//    public void onEventNotification(EventNotification notification) {
//        com.optel.api.wdm.in.notification.EventNotification event = switchEventDevToApi(notification.getEvents());
//        if(event!=null){
//            try {
//                RpcRfcUtil.callInvoke
//                        (CtcDciEventNotification.class.getName()
//                        ,"uploadEvent"
//                        ,new String[]{com.optel.api.wdm.in.notification.EventNotification.class.getName()}
//                        ,new Object[]{event});
//            }catch (Exception e){
//                // System.out.println("dc:event:"+event);
//            }
//        }
//
//
//    }
//
//    /**
//     * event dev to api
//     * @param event dev
//     * @return api
//     */
//    private com.optel.api.wdm.in.notification.EventNotification switchEventDevToApi(Events event){
//        if(event==null){
//            return null;
//        }
//        com.optel.api.wdm.in.notification.EventNotification e = new com.optel.api.wdm.in.notification.EventNotification();
//        e.setDcNeId(this.nodeId);
//        e.setEventAbbreviate(event.getEventAbbreviate());
//        if(event.getHostname()!=null){
//            e.setHostname(event.getHostname().getValue());
//        }
//        e.setId(event.getId());
//        e.setResource(event.getResource());
//        if(event.getSeverity()!=null){
//            e.setSeverity(event.getSeverity().implementedInterface().getSimpleName());
//        }
//        e.setText(event.getText());
//        if(event.getTimeCreated()!=null){
//            e.setTimeCreated(event.getTimeCreated().getValue().longValue());
//        }
//        if(event.getTypeId()!=null){
//            e.setTypeId(event.getTypeId().getString());
//        }
//        e.setVendorType(event.getVendorType());
//        return e;
//    }
//}
