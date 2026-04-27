///*
// * Copyright © 2019 optel and others.  All rights reserved.
// *
// * This program and the accompanying materials are made available under the
// * terms of the Eclipse Public License v1.0 which accompanies this distribution,
// * and is available at http://www.eclipse.org/legal/epl-v10.html
// */
//package com.optel.tmaster.dc.device.impl.otn.cmcc.notification;
//
//import com.optel.api.otn.in.notification.PeerChange;
//import com.optel.api.otn.service.notification.AccDevmNotification;
//import com.optel.tmaster.dc.general.nc.nccore.notification.BaseNotificationListenerImpl;
//import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
//import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.AccDevmListener;
//import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.FtrChangeNotification;
//import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.PeerChangeNotification;
//
///**
// * <ul>
// * <li>(处理 acc-devm 中定义的通知)</li>
// * </ul>
// *
// * @author LWX 2020/8/12 11:01
// */
//public class AccDevmListenerImpl extends BaseNotificationListenerImpl implements AccDevmListener {
//
//    public AccDevmListenerImpl(String nodeId) {
//        super(nodeId);
//    }
//    /**
//     * 链路对端信息改变通知
//     * @param notification 对端信息
//     */
//    @Override
//    public void onPeerChangeNotification(PeerChangeNotification notification) {
//        PeerChange peerChange = new PeerChange();
//        peerChange.setDcNeId(this.nodeId);
//        peerChange.setPtpName(notification.getPtpName());
//        peerChange.setPeerIpAddress(notification.getPeerIpAddress());
//        peerChange.setPeerPtpTcpId(notification.getPeerPtpTcpId());
//        RpcRfcUtil.callInvoke(AccDevmNotification.class.getName(), "onPeerChangeNotification",new String[]{PeerChange.class.getName()},  new Object[]{peerChange});
//    }
//
//    @Override
//    public void onFtrChangeNotification(FtrChangeNotification notification) {
//
//    }
//
//}
