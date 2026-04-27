/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.notification;

import cn.hutool.core.collection.CollUtil;
import com.optel.api.otn.in.notification.OamDmNotification;
import com.optel.api.otn.in.notification.OamLbNotification;
import com.optel.api.otn.in.notification.OamLmNotification;
import com.optel.api.otn.in.notification.OamLtNotification;
import com.optel.api.otn.in.notification.RemoteChangeNotificationDTO;
import com.optel.api.otn.service.notification.OtnExtensionNotification;
import com.optel.api.otn.service.notification.oam.EthOamNotification;
import com.optel.tmaster.dc.device.impl.base.transform.ITransform;
import com.optel.tmaster.dc.general.base.util.CommonUtil;
import com.optel.tmaster.dc.general.nc.nccore.notification.BaseNotificationListenerImpl;
import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.DmNotification;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LbNotification;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LmNotification;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LtNotification;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.OptOtnExtensionListener;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.RemoteChangeNotification;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.dm.notification.grouping.DmNotificationBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.lb.summary.grouping.Summary;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.lb.notification.grouping.LbNotificationBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.lm.notification.grouping.LmNotificationBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.lt.notification.grouping.LtNotificationBuilder;

import java.math.BigDecimal;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * <pre>
 *    o o o o o o     p p p p p p   t t t t t t t    e e e e e e    l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p p p p p        t t         e e e e e e    l l
 *    o o     o o     p p                t t         e e            l l
 *    o o     o o     p p                t t         e e            l l
 *    o o     o o     p p                t t         e e            l l
 *    o o o o o o     p p                t t         e e e e e e    l l l l l l
 *
 *              LiHua       佛主保佑       永无BUG
 * </pre>
 * <p>
 * 2020/8/14 15:37:29
 * 扩展通知处理
 *
 * @author LiH
 * @version V1.0.0
 */
public class OptOtnExtensionListenerImpl extends BaseNotificationListenerImpl implements OptOtnExtensionListener, ITransform {
    public OptOtnExtensionListenerImpl(String nodeId) {
        super(nodeId);
    }

    @Override
    public void onLbNotification(LbNotification notification) {
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.lb.notification.grouping.LbNotification lb =
                new LbNotificationBuilder(notification.getLbNotification()).build();
        List<OamLbNotification.EthLb> ethLbs = lb.getEthLb().values().stream().map(e ->
                new OamLbNotification.EthLb()
                        .setBytes(CommonUtil.getIntegerValue(e.getBytes()))
                        .setSequence(e.getSequence())
                        .setTime(e.getTime())).collect(Collectors.toList());
        Summary summary = lb.getSummary();
        OamLbNotification notify = new OamLbNotification()
                .setCtpName(lb.getCtpName());
        if (Objects.nonNull(summary)) {
            notify.setSummary(new OamLbNotification.Summary()
                    .setAverageTime(summary.getAverageTime())
                    .setMaximumTime(summary.getMaximumTime())
                    .setMinimumTime(summary.getMinimumTime()));
        }
        notify.setEthLb(ethLbs);
        notify.setDcNeId(super.nodeId);
        RpcRfcUtil.callInvoke(EthOamNotification.class.getName()
                , "onLbNotification"
                , new String[]{OamLbNotification.class.getName()}
                , new Object[]{notify});
    }

    @Override
    public void onLtNotification(LtNotification notification) {
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.lt.notification.grouping.LtNotification lt =
                new LtNotificationBuilder(notification.getLtNotification()).build();
        OamLtNotification notify = new OamLtNotification();
        notify.setCtpName(lt.getCtpName());
        List<OamLtNotification.EthLt> ethLts = lt.getEthLt().values().stream().map(e -> new OamLtNotification.EthLt()
                        .setHops(CommonUtil.getIntegerValue(e.getHops())).setMpMac(e.getMpMac()).setReplyAction(e.getReplyAction()))
                .collect(Collectors.toList());
        notify.setEthLt(ethLts);
        notify.setDcNeId(super.nodeId);
        RpcRfcUtil.callInvoke(EthOamNotification.class.getName()
                , "onLtNotification"
                , new String[]{OamLtNotification.class.getName()}
                , new Object[]{notify});
    }

    @Override
    public void onLmNotification(LmNotification notification) {
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.lm.notification.grouping.LmNotification lm =
                new LmNotificationBuilder(notification.getLmNotification()).build();
        OamLmNotification notify = new OamLmNotification().setCtpName(lm.getCtpName()).setName(lm.getName());
        if (Objects.nonNull(lm.getEthLm())) {
            List<OamLmNotification.EthLm> ethLms = lm.getEthLm().stream().map(e -> new OamLmNotification.EthLm()
                    .setIndex(CommonUtil.getLongValue(e.getIndex()))
                    .setLocalLostPackRatio(BigDecimal.valueOf(e.getLocalLostPackRatio().doubleValue()))
                    .setLocalReceiveCount(CommonUtil.getLongValue(e.getLocalReceiveCount()))
                    .setLocalSendCount(CommonUtil.getLongValue(e.getLocalSendCount()))
                    .setRemoteLostPackRatio(BigDecimal.valueOf(e.getRemoteLostPackRatio().doubleValue()))
                    .setRemoteReceiveCount(CommonUtil.getLongValue(e.getRemoteReceiveCount()))
                    .setRemoteSendCount(CommonUtil.getLongValue(e.getRemoteSendCount()))
            ).collect(Collectors.toList());
            notify.setEthLm(ethLms);
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.lm.summary.grouping.Summary summary = lm.getSummary();
        if (Objects.nonNull(summary)) {
            OamLmNotification.Summary summary1 = new OamLmNotification.Summary()
                    .setLocalLoss(CommonUtil.getLongValue(summary.getLocalLoss()))
                    .setLocalLossRatio(BigDecimal.valueOf(summary.getLocalLossRatio().doubleValue()))
                    .setLocalTxPkts(CommonUtil.getLongValue(summary.getLocalTxPkts()))
                    .setRemoteLoss(CommonUtil.getLongValue(summary.getRemoteLoss()))
                    .setRemoteLossRatio(BigDecimal.valueOf(summary.getRemoteLossRatio().doubleValue()))
                    .setRemoteTxPkts(CommonUtil.getLongValue(summary.getRemoteTxPkts()));
            notify.setSummary(summary1);
        }
        notify.setDcNeId(super.nodeId);
        RpcRfcUtil.callInvoke(EthOamNotification.class.getName()
                , "onLmNotification"
                , new String[]{OamLmNotification.class.getName()}
                , new Object[]{notify});
    }

    @Override
    public void onDmNotification(DmNotification notification) {
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.dm.notification.grouping.DmNotification dm =
                new DmNotificationBuilder(notification.getDmNotification()).build();
        OamDmNotification notify = new OamDmNotification().setCtpName(dm.getCtpName()).setName(dm.getName());
        if (Objects.nonNull(dm.getEthDm())) {
            List<OamDmNotification.EthDm> ethDms = dm.getEthDm().stream().map(e -> new OamDmNotification.EthDm()
                    .setIndex(CommonUtil.getLongValue(e.getIndex()))
                    .setDelay(BigDecimal.valueOf(e.getDelay().doubleValue()))
                    .setDelayVariation(BigDecimal.valueOf(e.getDelayVariation().doubleValue()))
            ).collect(Collectors.toList());
            notify.setEthDm(ethDms);
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.dm.summary.grouping.Summary summary = dm.getSummary();
        if (Objects.nonNull(summary)) {
            OamDmNotification.Summary summary1 = new OamDmNotification.Summary()
                    .setAverageDelay(BigDecimal.valueOf(summary.getAverageDelay().doubleValue()))
                    .setAverageDelayVariation(BigDecimal.valueOf(summary.getAverageDelayVariation().doubleValue()))
                    .setMaximumDelay(BigDecimal.valueOf(summary.getMaximumDelay().doubleValue()))
                    .setMaximumDelayVariation(BigDecimal.valueOf(summary.getMaximumDelayVariation().doubleValue()))
                    .setMinimumDelay(BigDecimal.valueOf(summary.getMinimumDelay().doubleValue()))
                    .setMinimumDelayVariation(BigDecimal.valueOf(summary.getMinimumDelayVariation().doubleValue()));
            notify.setSummary(summary1);
        }

        notify.setDcNeId(super.nodeId);
        RpcRfcUtil.callInvoke(EthOamNotification.class.getName()
                , "onDmNotification"
                , new String[]{OamDmNotification.class.getName()}
                , new Object[]{notify});
    }

    /**
     * 远端网元上线/下线通知
     *
     * @param notification 通知数据
     */
    @Override
    public void onRemoteChangeNotification(RemoteChangeNotification notification) {
        if (notification == null) {
            return;
        }
        RemoteChangeNotificationDTO remoteChangeNotificationDto = new RemoteChangeNotificationDTO();
        remoteChangeNotificationDto.setPtpName(notification.getPtpName());
        remoteChangeNotificationDto.setChangingReason(notification.getChangingReason().getName());
        remoteChangeNotificationDto.setRemoteModuleName(notification.getRemoteModuleName());
        if (notification.getRemoteModuleMac() != null) {
            remoteChangeNotificationDto.setRemoteModuleMac(notification.getRemoteModuleMac().getValue());
        }
        remoteChangeNotificationDto.setRemoteModuleUuid(notification.getRemoteModuleUuid());
        remoteChangeNotificationDto.setVlanId(notification.getVlanId().intValue());
        remoteChangeNotificationDto.setRemotePtpName(CollUtil.newArrayList(notification.getRemotePtpName()));
        remoteChangeNotificationDto.setDcNeId(this.nodeId);

        RpcRfcUtil.callInvoke(OtnExtensionNotification.class.getName()
                , "onRemoteChangeNotification"
                , new String[]{RemoteChangeNotificationDTO.class.getName()}
                , new Object[]{remoteChangeNotificationDto});
    }
}
