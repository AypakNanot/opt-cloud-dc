///*
// * Copyright © 2019 optel and others.  All rights reserved.
// *
// * This program and the accompanying materials are made available under the
// * terms of the Eclipse Public License v1.0 which accompanies this distribution,
// * and is available at http://www.eclipse.org/legal/epl-v10.html
// */
//package com.optel.tmaster.dc.device.impl.otn.cmcc.notification;
//
//import cn.hutool.core.collection.CollUtil;
//import com.optel.api.otn.in.notification.*;
//import com.optel.api.otn.service.notification.AccOtnPgSwitchNotification;
//import com.optel.api.otn.service.notification.OtnExtensionNotification;
//import com.optel.api.otn.service.notification.oam.EthOamNotification;
//import com.optel.tmaster.dc.general.base.util.CommonUtil;
//import com.optel.tmaster.dc.general.nc.nccore.notification.BaseNotificationListenerImpl;
//import com.optel.tmaster.dc.general.service.util.RpcRfcUtil;
//import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.*;
//import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dm.notification.grouping.DmNotificationBuilder;
//import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.lb.summary.grouping.Summary;
//import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lm.notification.grouping.LmNotificationBuilder;
//import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lt.notification.grouping.LtNotificationBuilder;
//import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.protection._switch.notification.EqPg;
//
//import java.util.List;
//import java.util.Objects;
//import java.util.stream.Collectors;
//
///**
// * <pre>
// *    o o o o o o     p p p p p p   t t t t t t t    e e e e e e    l l
// *    o o     o o     p p       p        t t         e e            l l
// *    o o     o o     p p       p        t t         e e            l l
// *    o o     o o     p p       p        t t         e e            l l
// *    o o     o o     p p       p        t t         e e            l l
// *    o o     o o     p p p p p p        t t         e e e e e e    l l
// *    o o     o o     p p                t t         e e            l l
// *    o o     o o     p p                t t         e e            l l
// *    o o     o o     p p                t t         e e            l l
// *    o o o o o o     p p                t t         e e e e e e    l l l l l l
// *
// *              LiHua       佛主保佑       永无BUG
// * </pre>
// * <p>
// * 2020/8/14 15:37:29
// * 扩展通知处理
// *
// * @author LiH
// * @version V1.0.0
// */
//public class OptOtnExtensionListenerImpl extends BaseNotificationListenerImpl implements OptOtnExtensionListener {
//    public OptOtnExtensionListenerImpl(String nodeId) {
//        super(nodeId);
//    }
//
//    @Override
//    public void onLbNotification(LbNotification notification) {
//        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lb.notification.grouping.LbNotification lb =
//                notification.getLbNotification();
//        OamLbNotification notify = new OamLbNotification()
//                .setCtpName(lb.getCtpName());
//        if(lb.getEthLb() != null){
//            List<OamLbNotification.EthLb> ethLbs = lb.getEthLb().values().stream().map(e ->
//                    new OamLbNotification.EthLb()
//                            .setBytes(CommonUtil.getIntegerValue(e.getBytes()))
//                            .setSequence(e.getSequence())
//                            .setTime(e.getTime())).collect(Collectors.toList());
//            notify.setEthLb(ethLbs);
//        }
//        Summary summary = lb.getSummary();
//        if (Objects.nonNull(summary)) {
//            notify.setSummary(new OamLbNotification.Summary()
//                    .setAverageTime(summary.getAverageTime())
//                    .setMaximumTime(summary.getMaximumTime())
//                    .setMinimumTime(summary.getMinimumTime()));
//        }
//        notify.setDcNeId(super.nodeId);
//        RpcRfcUtil.callInvoke(EthOamNotification.class.getName()
//                ,"onLbNotification"
//                ,new String[]{OamLbNotification.class.getName()}
//                ,new Object[]{notify});
//    }
//
//    @Override
//    public void onLtNotification(LtNotification notification) {
//        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lt.notification.grouping.LtNotification lt =
//                new LtNotificationBuilder(notification.getLtNotification()).build();
//        OamLtNotification notify = new OamLtNotification();
//        notify.setCtpName(lt.getCtpName());
//        List<OamLtNotification.EthLt> ethLts = lt.getEthLt().values().stream().map(e -> new OamLtNotification.EthLt()
//                .setHops(CommonUtil.getIntegerValue(e.getHops())).setMpMac(e.getMpMac()).setReplyAction(e.getReplyAction()))
//                .collect(Collectors.toList());
//        notify.setEthLt(ethLts);
//        notify.setDcNeId(super.nodeId);
//        RpcRfcUtil.callInvoke(EthOamNotification.class.getName()
//                ,"onLtNotification"
//                ,new String[]{OamLtNotification.class.getName()}
//                ,new Object[]{notify});
//    }
//
//    @Override
//    public void onLmNotification(LmNotification notification) {
//        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lm.notification.grouping.LmNotification lm =
//                new LmNotificationBuilder(notification.getLmNotification()).build();
//        OamLmNotification notify = new OamLmNotification().setCtpName(lm.getCtpName()).setName(lm.getName());
//        if(Objects.nonNull(lm.getEthLm())){
//            List<OamLmNotification.EthLm> ethLms = lm.getEthLm().stream().map(e -> new OamLmNotification.EthLm()
//                    .setIndex(CommonUtil.getLongValue(e.getIndex()))
//                    .setLocalLostPackRatio(e.getLocalLostPackRatio().decimalValue())
//                    .setLocalReceiveCount(CommonUtil.getLongValue(e.getLocalReceiveCount()))
//                    .setLocalSendCount(CommonUtil.getLongValue(e.getLocalSendCount()))
//                    .setRemoteLostPackRatio(e.getRemoteLostPackRatio().decimalValue())
//                    .setRemoteReceiveCount(CommonUtil.getLongValue(e.getRemoteReceiveCount()))
//                    .setRemoteSendCount(CommonUtil.getLongValue(e.getRemoteSendCount()))
//            ).collect(Collectors.toList());
//            notify.setEthLm(ethLms);
//        }
//        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.lm.summary.grouping.Summary summary = lm.getSummary();
//        if (Objects.nonNull(summary)) {
//            OamLmNotification.Summary summary1 = new OamLmNotification.Summary()
//                    .setLocalLoss(CommonUtil.getLongValue(summary.getLocalLoss()))
//                    .setLocalLossRatio(summary.getLocalLossRatio().decimalValue())
//                    .setLocalTxPkts(CommonUtil.getLongValue(summary.getLocalTxPkts()))
//                    .setRemoteLoss(CommonUtil.getLongValue(summary.getRemoteLoss()))
//                    .setRemoteLossRatio(summary.getRemoteLossRatio().decimalValue())
//                    .setRemoteTxPkts(CommonUtil.getLongValue(summary.getRemoteTxPkts()));
//            notify.setSummary(summary1);
//        }
//        notify.setDcNeId(super.nodeId);
//        RpcRfcUtil.callInvoke(EthOamNotification.class.getName()
//                ,"onLmNotification"
//                ,new String[]{OamLmNotification.class.getName()}
//                ,new Object[]{notify});
//    }
//
//
//    @Override
//    public void onDmNotification(DmNotification notification) {
//        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.dm.notification.grouping.DmNotification dm =
//                new DmNotificationBuilder(notification.getDmNotification()).build();
//        OamDmNotification notify = new OamDmNotification().setCtpName(dm.getCtpName()).setName(dm.getName());
//        if(Objects.nonNull(dm.getEthDm())){
//            List<OamDmNotification.EthDm> ethDms = dm.getEthDm().stream().map(e -> new OamDmNotification.EthDm()
//                    .setIndex(CommonUtil.getLongValue(e.getIndex()))
//                    .setDelay(e.getDelay().decimalValue())
//                    .setDelayVariation(e.getDelayVariation().decimalValue())
//            ).collect(Collectors.toList());
//            notify.setEthDm(ethDms);
//        }
//        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.dm.summary.grouping.Summary summary = dm.getSummary();
//        if (Objects.nonNull(summary)) {
//            OamDmNotification.Summary summary1 = new OamDmNotification.Summary()
//                    .setAverageDelay(summary.getAverageDelay().decimalValue())
//                    .setAverageDelayVariation(summary.getAverageDelayVariation().decimalValue())
//                    .setMaximumDelay(summary.getMaximumDelay().decimalValue())
//                    .setMaximumDelayVariation(summary.getMaximumDelayVariation().decimalValue())
//                    .setMinimumDelay(summary.getMinimumDelay().decimalValue())
//                    .setMinimumDelayVariation(summary.getMinimumDelayVariation().decimalValue());
//            notify.setSummary(summary1);
//        }
//
//        notify.setDcNeId(super.nodeId);
//        RpcRfcUtil.callInvoke(EthOamNotification.class.getName()
//                ,"onDmNotification"
//                ,new String[]{OamDmNotification.class.getName()}
//                ,new Object[]{notify});
//    }
//
//    @Override
//    public void onProtectionSwitchNotification(ProtectionSwitchNotification notification) {
//        OtnPgSwitchNotificationDTO otnPgSwitchNotification = new OtnPgSwitchNotificationDTO();
//        EqPg eqpg = notification.getEqPg();
//        otnPgSwitchNotification.setPgSwitchType("eq-pg");
//        otnPgSwitchNotification.setDcNeId(this.nodeId);
//        otnPgSwitchNotification.setProtectionSwitchSerialNo(CommonUtil.getLongValue(notification.getProtectionSwitchSerialNo()));
//        otnPgSwitchNotification.setPgId(CommonUtil.getIntegerValue(eqpg.getPgId()));
//        otnPgSwitchNotification.setEventTime(System.currentTimeMillis());
//        otnPgSwitchNotification.setSelectedPort(eqpg.getSelectedEq());
//        if (eqpg.getSwitchReason() != null) {
//            otnPgSwitchNotification.setSwitchReason(eqpg.getSwitchReason().getName());
//        }
//        if (eqpg.getProtectionDirection() != null) {
//            otnPgSwitchNotification.setProtectionDirection(eqpg.getProtectionDirection().getName());
//        }
//        //发送通知至OTN-Server
//        RpcRfcUtil.callInvoke(AccOtnPgSwitchNotification.class.getName()
//                ,"onOtnPgSwitchNotification"
//                ,new String[]{OtnPgSwitchNotificationDTO.class.getName()}
//                ,new Object[]{otnPgSwitchNotification});
//    }
//
//
//    /**
//     * 远端网元上线/下线通知
//     * @param notification 通知数据
//     */
//    @Override
//    public void onRemoteChangeNotification(RemoteChangeNotification notification) {
//        if(notification == null){
//            return;
//        }
//        RemoteChangeNotificationDTO remoteChangeNotificationDto = new RemoteChangeNotificationDTO();
//        remoteChangeNotificationDto.setPtpName(notification.getPtpName());
//        remoteChangeNotificationDto.setChangingReason(notification.getChangingReason().getName());
//        remoteChangeNotificationDto.setRemoteModuleName(notification.getRemoteModuleName());
//        if(notification.getRemoteModuleMac() != null){
//            remoteChangeNotificationDto.setRemoteModuleMac(notification.getRemoteModuleMac().getValue());
//        }
//        remoteChangeNotificationDto.setRemoteModuleUuid(notification.getRemoteModuleUuid());
//        remoteChangeNotificationDto.setRemotePtpName(CollUtil.newArrayList(notification.getRemotePtpName()));
//        remoteChangeNotificationDto.setDcNeId(this.nodeId);
//
//        RpcRfcUtil.callInvoke(OtnExtensionNotification.class.getName()
//                ,"onRemoteChangeNotification"
//                ,new String[]{RemoteChangeNotificationDTO.class.getName()}
//                ,new Object[]{remoteChangeNotificationDto});
//    }
//}
