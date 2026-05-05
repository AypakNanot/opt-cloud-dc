/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.notification;

import com.optel.api.otn.in.notification.OtnPgSwitchNotificationDTO;
import com.optel.api.otn.service.notification.AccOtnPgSwitchNotification;
import com.optel.tmaster.dc.general.base.util.CommonUtil;
import com.optel.tmaster.dc.general.nc.nccore.notification.BaseNotificationListenerImpl;
import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.AccProtectionGroupListener;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionSwitchNotification;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.protection._switch.notification.PgOrEqpg;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.protection._switch.notification.pg.or.eqpg.Eqpg;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.protection._switch.notification.pg.or.eqpg.eqpg.EqPg;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.protection._switch.notification.pg.or.eqpg.pg.Pg;
import org.slf4j.LoggerFactory;


/**
 * <ul>
 * <li>( 接收和处理 acc-protection-group.yang 中定义的通知)</li>
 * </ul>
 *
 * @author LWX 2020/5/4 14:56
 */
public class AccProtectionGroupListenerImpl extends BaseNotificationListenerImpl implements AccProtectionGroupListener {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AccProtectionGroupListenerImpl.class);

    public AccProtectionGroupListenerImpl(String nodeId) {
        super(nodeId);
    }

    /**
     * 收到 保护倒换通知
     *
     * @param notification 通知消息内容
     */
    @Override
    public void onProtectionSwitchNotification(ProtectionSwitchNotification notification) {
        OtnPgSwitchNotificationDTO otnPgSwitchNotification = null;
        PgOrEqpg pgOrEqPg = notification.getPgOrEqpg();
        if (pgOrEqPg == null) {
            return;
        }
        if(pgOrEqPg instanceof org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.protection._switch.notification.pg.or.eqpg.Pg){
            otnPgSwitchNotification = new OtnPgSwitchNotificationDTO();
            Pg pg =   ((org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.protection._switch.notification.pg.or.eqpg.Pg) pgOrEqPg).getPg();
            otnPgSwitchNotification.setPgSwitchType("pg");
            otnPgSwitchNotification.setDcNeId(this.nodeId);
            otnPgSwitchNotification.setProtectionSwitchSerialNo(CommonUtil.getLongValue(notification.getProtectionSwitchSerialNo()));
            otnPgSwitchNotification.setPgId(CommonUtil.getIntegerValue(pg.getPgId()));
            otnPgSwitchNotification.setEventTime(System.currentTimeMillis());
            otnPgSwitchNotification.setSelectedPort(pg.getSelectedPort());
            if (pg.getSwitchReason() != null) {
                otnPgSwitchNotification.setSwitchReason(pg.getSwitchReason().getName());
            }
            if (pg.getProtectionDirection() != null) {
                otnPgSwitchNotification.setProtectionDirection(pg.getProtectionDirection().getName());
            }
        }else if(pgOrEqPg instanceof Eqpg){
            otnPgSwitchNotification = new OtnPgSwitchNotificationDTO();
            EqPg pg = ((Eqpg) pgOrEqPg).getEqPg();
            otnPgSwitchNotification.setPgSwitchType("eq-pg");
            otnPgSwitchNotification.setDcNeId(this.nodeId);
            otnPgSwitchNotification.setProtectionSwitchSerialNo(CommonUtil.getLongValue(notification.getProtectionSwitchSerialNo()));
            otnPgSwitchNotification.setPgId(CommonUtil.getIntegerValue(pg.getPgId()));
            otnPgSwitchNotification.setEventTime(System.currentTimeMillis());
            otnPgSwitchNotification.setSelectedPort(pg.getSelectedEq());
            if (pg.getSwitchReason() != null) {
                otnPgSwitchNotification.setSwitchReason(pg.getSwitchReason().getName());
            }
            if (pg.getProtectionDirection() != null) {
                otnPgSwitchNotification.setProtectionDirection(pg.getProtectionDirection().getName());
            }
        }
        if(otnPgSwitchNotification != null){
            LOG.debug("receive ProtectionSwitchNotification.nodeId:{}",this.nodeId);
            //发送通知至OTN-Server
            RpcRfcUtil.callInvoke(AccOtnPgSwitchNotification.class.getName()
                    ,"onOtnPgSwitchNotification"
                    ,new String[]{OtnPgSwitchNotificationDTO.class.getName()}
                    ,new Object[]{otnPgSwitchNotification});
        }
    }
}
