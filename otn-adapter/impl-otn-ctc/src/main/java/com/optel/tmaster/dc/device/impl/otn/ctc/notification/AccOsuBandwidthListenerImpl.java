/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.notification;

import com.optel.api.otn.in.notification.OsuAdjustmentNotificationDTO;
import com.optel.tmaster.dc.general.base.util.CommonUtil;
import com.optel.tmaster.dc.general.nc.nccore.notification.BaseNotificationListenerImpl;
import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.capacity.CirOrTotalsize;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.capacity.cir.or.totalsize.ForCir;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.capacity.cir.or.totalsize.ForTotalsize;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.AccOsuListener;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.OsuAdjustmentNotification;
import org.slf4j.LoggerFactory;

/**
 * @author Quan Jingyuan
 * @since 2021/11/9
 **/
public class AccOsuBandwidthListenerImpl extends BaseNotificationListenerImpl implements AccOsuListener {
    private static final org.slf4j.Logger LOG = LoggerFactory.getLogger(AccOsuBandwidthListenerImpl.class);

    public AccOsuBandwidthListenerImpl(String nodeId) {
        super(nodeId);
    }

    @Override
    public void onOsuAdjustmentNotification(OsuAdjustmentNotification notification) {
        OsuAdjustmentNotificationDTO dto = new OsuAdjustmentNotificationDTO();
        if (notification.getRequestedCapacity() == null) {
            return;
        }
        dto.setCtpName(notification.getCtpName());
        if(notification.getFailReason()!=null){
            dto.setFailReason(notification.getFailReason().getName());
        }
        dto.setModifyResult(notification.getModifyResult());
        dto.setOsuAdjustmentSerialNo(CommonUtil.getLongValue(notification.getOsuAdjustSerialNo()));
        dto.setDcNeId(this.nodeId);
        CirOrTotalsize cirOrTotalsize = notification.getRequestedCapacity().getCirOrTotalsize();
        if (cirOrTotalsize instanceof ForTotalsize) {
            Integer totalSize = CommonUtil.getIntegerValue(((ForTotalsize) cirOrTotalsize).getTotalSize());
            dto.setTotalSize(totalSize);
        } else if (cirOrTotalsize instanceof ForCir) {
            ForCir forCir = (ForCir) cirOrTotalsize;
            dto.setCir(CommonUtil.getIntegerValue(forCir.getCir()));
            dto.setCbs(CommonUtil.getIntegerValue(forCir.getCbs()));
            dto.setPbs(CommonUtil.getIntegerValue(forCir.getPbs()));
            dto.setPir(CommonUtil.getIntegerValue(forCir.getPir()));
        }

        LOG.debug("receive OsuAdjustmentNotification.nodeId:{}", this.nodeId);
        //发送通知至OTN-Server
        RpcRfcUtil.callInvoke(com.optel.api.otn.service.notification.OsuAdjustmentNotification.class.getName()
                , "onOsuBandwidthAdjustNotification"
                , new String[]{OsuAdjustmentNotificationDTO.class.getName()}
                , new Object[]{dto});


    }
}
