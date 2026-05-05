/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.notification;

import com.optel.api.common.base.Constant;
import com.optel.api.wdm.service.notification.CtcDciEventNotification;
import com.optel.tmaster.dc.general.nc.nccore.notification.BaseNotificationListenerImpl;
import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.notification.rev230426.EventNotification;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.notification.rev230426.OpenconfigEventListener;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.notification.rev230426.event.Avc;

/**
 * 事件通知处理
 * 2022/3/14 10:56
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class CmccOpenConfigEventListenerImpl extends BaseNotificationListenerImpl implements OpenconfigEventListener {

    public CmccOpenConfigEventListenerImpl(String nodeId) {
        super(nodeId);
    }

    @Override
    public void onEventNotification(EventNotification notification) {
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.notification.rev230426.event.notification.Event event
                = notification.getEvent();
        com.optel.api.wdm.in.notification.EventNotification eventNotification = switchEvent(event);
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


    }

    private com.optel.api.wdm.in.notification.EventNotification
    switchEvent(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.notification.rev230426.event.notification.Event event) {
        if (event == null) {
            return null;
        }
        com.optel.api.wdm.in.notification.EventNotification result = new com.optel.api.wdm.in.notification.EventNotification();
        result.setId(String.valueOf(event.getEventSerialNo()));
        result.setTypeId(event.getEventType().getName());
        result.setResource(event.getObjectName());
        result.setVendorType(event.getObjectType().getName());
        Avc avc = event.getAvc();
        if (avc != null) {
            result.setText(avc.getAttributeName() + Constant.COLON + avc.getOldAttributeValue() + " -> " + avc.getNewAttributeValue());
        }
        result.setEventAbbreviate(event.getEventAbbr());
        result.setTimeCreated(event.getTimeCreated().getValue().longValue());
        result.setDcNeId(this.nodeId);
        result.setSeverity("MINOR");
        return result;
    }
}
