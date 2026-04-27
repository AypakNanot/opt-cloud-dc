/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.notification;

import com.optel.api.otn.in.notification.DeviceEvent;
import com.optel.api.otn.in.notification.PeerChange;
import com.optel.api.otn.service.notification.AccDevmNotification;
import com.optel.api.otn.service.notification.DeviceEventNotification;
import com.optel.tmaster.dc.general.nc.nccore.notification.BaseNotificationListenerImpl;
import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.AccDevmListener;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.FtrChangeNotification;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.PeerChangeNotification;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.notifications.rev210924.EventType;

/**
 * <ul>
 * <li>(处理 acc-devm 中定义的通知)</li>
 * </ul>
 *
 * @author LWX 2020/8/12 11:01
 */
public class AccDevmListenerImpl extends BaseNotificationListenerImpl implements AccDevmListener {

    public AccDevmListenerImpl(String nodeId) {
        super(nodeId);
    }

    @Override
    public void onFtrChangeNotification(FtrChangeNotification notification) {
        DeviceEvent e = new DeviceEvent();
        e.setDcNeId(this.nodeId);
        e.setEventTime(System.currentTimeMillis());
        e.setObjectType("PTP");
        e.setObjectName(notification.getPtpName());
        e.setEventType(EventType.AttributeValueChange.getName());
        e.setAttributeName("远端模块");
        if(StrUtil.isEmpty(notification.getFTR()) || StrUtil.isEmpty(notification.getRemotePtp())){
            e.setNewValue("断开");
        }else{
            e.setNewValue("远端模块:"+notification.getRemotePtp()+" FTR:"+notification.getFTR()+"连接");
        }
        e.setOldValue("");
        RpcRfcUtil.callInvoke(DeviceEventNotification.class.getName()
                ,"uploadEvent"
                ,new String[]{DeviceEvent.class.getName()}
                ,new Object[]{e});
    }

    /**
     * 链路对端信息改变通知
     * @param notification 对端信息
     */
    @Override
    public void onPeerChangeNotification(PeerChangeNotification notification) {
        PeerChange peerChange = new PeerChange();
        peerChange.setDcNeId(this.nodeId);
        peerChange.setPtpName(notification.getPtpName());
        peerChange.setPeerIpAddress(notification.getPeerIpAddress().getValue());
        peerChange.setPeerPtpTcpId(notification.getPeerPtpTcpId());
        RpcRfcUtil.callInvoke(AccDevmNotification.class.getName(), "onPeerChangeNotification",new String[]{PeerChange.class.getName()},  new Object[]{peerChange});
    }
}
