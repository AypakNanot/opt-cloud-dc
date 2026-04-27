/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.notification;

import com.optel.api.otn.in.notification.DeviceEvent;
import com.optel.api.otn.service.notification.DeviceEventNotification;
import com.optel.tmaster.dc.general.nc.nccore.notification.BaseNotificationListenerImpl;
import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.notifications.rev210924.AccNotificationsListener;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.notifications.rev210924.EmsOperNotification;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.notifications.rev210924.AttributeValueChangeNotification;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.notifications.rev210924.CommonNotification;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.notifications.rev210924.EventType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.notifications.rev210924.attribute.value.change.notification.AttributeValueChange;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.notifications.rev210924.attribute.value.change.notification.Event;
import org.slf4j.LoggerFactory;


/**
 * <ul>
 * <li>(接收和处理acc-notifications.yang中定义的通知。通知由设备上报)</li>
 * </ul>
 *
 * @author LWX 2020/5/4 14:45
 */
public class AccNotificationListenerImpl extends BaseNotificationListenerImpl implements AccNotificationsListener {

    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AccNotificationListenerImpl.class);

    public AccNotificationListenerImpl(String nodeId) {
        super(nodeId);
    }

    /**
     * 收到设备的通用通知消息
     * @param notification 通知内容
     */
    @Override
    public void onCommonNotification(CommonNotification notification) {
        DeviceEvent e = notificationToDeviceEvent(notification.getEvent());
        RpcRfcUtil.callInvoke(DeviceEventNotification.class.getName()
                ,"uploadEvent"
                ,new String[]{DeviceEvent.class.getName()}
                ,new Object[]{e});
    }

    /**
     *收到设备的 对象属性修改上报 通知
     * @param notification 对象属性修改上报通知内容
     */
    @Override
    public void onAttributeValueChangeNotification(AttributeValueChangeNotification notification) {
        DeviceEvent e = notificationToDeviceEvent(notification.getAttributeValueChange(), notification.getEvent());
        RpcRfcUtil.callInvoke(DeviceEventNotification.class.getName()
                ,"uploadEvent"
                ,new String[]{DeviceEvent.class.getName()}
                ,new Object[]{e});
    }

    @Override
    public void  onEmsOperNotification(EmsOperNotification notification){
        // 不实现该通知接口 属性改变通知由onAttributeValueChangeNotification接收
    }

    private DeviceEvent notificationToDeviceEvent(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.notifications.rev210924.common.notification.Event event){
        DeviceEvent e = new DeviceEvent();
        e.setObjectName(event.getObjectName());
        e.setDcNeId(this.nodeId);
        if(event.getEventType()!=null){
            e.setEventType(event.getEventType().getName());
        }
        e.setUserName(event.getUserName());
        if(event.getEventSerialNo()!=null){
            e.setEventSerialNo(event.getEventSerialNo().longValue());
        }
        e.setObjectType(event.getObjectType());
        e.setEventTime(System.currentTimeMillis());
        return e;
    }

    private DeviceEvent notificationToDeviceEvent(AttributeValueChange attributeValueChange,Event event){
        DeviceEvent e = new DeviceEvent();
        if(event==null){
            return null;
        }
        e.setDcNeId(this.nodeId);
        e.setEventSerialNo(event.getEventSerialNo().longValue());
        e.setEventType(event.getEventType().getName());
        e.setObjectName(event.getObjectName());
        e.setObjectType(event.getObjectType());
        if(event.getEventType()== EventType.AttributeValueChange ){
            e.setAttributeName(attributeValueChange.getAttributeName());
            e.setNewValue(attributeValueChange.getNewValue());
            e.setOldValue(attributeValueChange.getOldValue());
        }
        e.setEventTime(System.currentTimeMillis());
        return e;
    }

}
