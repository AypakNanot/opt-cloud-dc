/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.impl;

import cn.hutool.core.collection.CollUtil;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciAlarmServiceImpl;
import com.optel.tmaster.dc.dci.impl.base.transform.ITransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.alarm.AlarmServiceTransform;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.*;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.create.alarm.mask.input.MaskAlarms;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.create.alarm.mask.input.MaskAlarmsKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.ObjectType;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.mask.rev230426.alarms.mask.top.AlarmsMask;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.mask.rev230426.alarms.mask.top.AlarmsMaskBuilder;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.mask.rev230426.alarms.mask.top.alarms.mask.AlarmMask;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.mask.rev230426.alarms.mask.top.alarms.mask.AlarmMaskBuilder;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.mask.rev230426.alarms.mask.top.alarms.mask.AlarmMaskKey;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.mask.rev230426.alarms.mask.top.alarms.mask.alarm.mask.Config;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.mask.rev230426.alarms.mask.top.alarms.mask.alarm.mask.StateBuilder;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.mask.types.rev230426.AlarmMaskObjectType;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.rev230426.AlarmsBuilder;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.rev230426.alarms.top.Alarm;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.rev230426.alarms.top.AlarmBuilder;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.rev230426.alarms.top.AlarmKey;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.GetHistoryAlarmsOutput;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.MiniotnRpcService;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.get.history.alarms.output.Alarms;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.get.history.alarms.output.Tcas;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.TcaTop;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tca.top.TcasBuilder;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tcas.top.Tca;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tcas.top.TcaBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.Uint64;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * dci ctc 告警实现类
 * 2022/3/14 9:00
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class CmccOptAlarmServiceImpl extends BaseDciAlarmServiceImpl implements ITransform, IDeviceServiceWdmCmcc {
    private static final String IS_OPT_TEST="IS_OPT_TEST";
    private static final String DATE_TIME = "2023-07-30T00:00:00+08:00";
    private static final String PORT_APSP = "PORT-1-1-2-APSP";

    @Override
    public ListenableFuture<RpcResult<GetAlarmMaskOutput>> getAlarmMask(GetAlarmMaskInput input) {
        @NonNull InstanceIdentifier<AlarmsMask> iid = create(AlarmsMask.class);
        AlarmsMask alarmsMask;
        String property = System.getProperty(IS_OPT_TEST, Boolean.FALSE.toString());
        if (Boolean.TRUE.equals(Boolean.valueOf(property))) {
            AlarmsMaskBuilder alarmsMaskBuilder = new AlarmsMaskBuilder();
            Map<AlarmMaskKey, AlarmMask> map = new HashMap<>(8);
            AlarmMaskBuilder builder = new AlarmMaskBuilder();
            builder.setIndex("1");
            StateBuilder stateBuilder = new StateBuilder();
            stateBuilder.setIndex("1");
            stateBuilder.setObjectName("CHASSIS-1");
            stateBuilder.setObjectRange(AlarmMaskObjectType.PORT);
            builder.setState(stateBuilder.build());
            map.put(new AlarmMaskKey("1"), builder.build());
            alarmsMaskBuilder.setAlarmMask(map);
            alarmsMask = alarmsMaskBuilder.build();
        } else {
            alarmsMask = MountTools.queryFromOperational(input.getNeId(), iid);
        }
        return RpcResultUtil.success(new AlarmServiceTransform().devGetAlarmMaskOutputToApi(alarmsMask));
    }

    @Override
    public ListenableFuture<RpcResult<DeleteAlarmMaskOutput>> deleteAlarmMask(DeleteAlarmMaskInput input) {
        Set<String> indexList = input.getIndex();
        if (indexList != null && !indexList.isEmpty()) {
            for (String index : indexList) {
                @NonNull KeyedInstanceIdentifier<AlarmMask, AlarmMaskKey> iid =
                        create(AlarmsMask.class).child(AlarmMask.class, new AlarmMaskKey(index));
                MountTools.deleteFromConfig(input.getNeId(), iid);
            }
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<CreateAlarmMaskOutput>> createAlarmMask(CreateAlarmMaskInput input) {
        Map<MaskAlarmsKey, MaskAlarms> maskAlarmMap = input.getMaskAlarms();
        if (maskAlarmMap != null && !maskAlarmMap.isEmpty()) {
            for (Map.Entry<MaskAlarmsKey, MaskAlarms> entry : maskAlarmMap.entrySet()) {
                try {
                    MaskAlarms maskAlarm = entry.getValue();
                    @NonNull InstanceIdentifier<Config> iid
                            = create(AlarmsMask.class)
                            .child(AlarmMask.class, new AlarmMaskKey(entry.getKey().getIndex()))
                            .child(Config.class);
                    MountTools.doMergeToConfig(input.getNeId(), iid, new AlarmServiceTransform().apiCreateAlarmMaskToDev(maskAlarm));
                } catch (Exception e) {
                    //Ignored
                }
            }
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetAlarmCurrentOutput>> getAlarmCurrent(GetAlarmCurrentInput input) {
        GetAlarmCurrentOutput output = null;
        String property = System.getProperty(IS_OPT_TEST, Boolean.FALSE.toString());
        if (Boolean.TRUE.equals(Boolean.valueOf(property))) {
            AlarmsBuilder alarmsBuilder = new AlarmsBuilder();
            AlarmBuilder alarmBuilder = new AlarmBuilder();
            alarmBuilder.setAdditionalText("15 min input power is lower than threshold");
            alarmBuilder.setAlarmCode("LANE_IN_PWR_L_15MIN");
            alarmBuilder.setAlarmSerialNo(Uint64.valueOf(2717947810L));
            alarmBuilder.setAlarmState(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.types.rev181121.AlarmState.Start);
            alarmBuilder.setEndTime(new org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime(DATE_TIME));
            alarmBuilder.setObjectName(PORT_APSP);
            alarmBuilder.setObjectType(ObjectType.PORT);
//            alarmBuilder.setPerceivedSeverity(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.types.rev181121.MAJOR.class);
            alarmBuilder.setStartTime(new org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime(DATE_TIME));
            Map<AlarmKey, Alarm> map = new HashMap<>();
            map.put(new AlarmKey(Uint64.valueOf(1)), alarmBuilder.build());
            alarmsBuilder.setAlarm(map);
            output = new AlarmServiceTransform().devGetAlarmCurrentOutputToApi(alarmsBuilder.build());
        } else {
            @NonNull InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.rev230426.Alarms> iid = create(org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.rev230426.Alarms.class);
            org.opendaylight.yang.gen.v1.urn.cmcc.yang.alarms.rev230426.Alarms alarms = MountTools.queryFromOperational(input.getNeId(), iid);
            output = new AlarmServiceTransform().devGetAlarmCurrentOutputToApi(alarms);
        }
        return RpcResultUtil.success(output);
    }

    @Override
    public ListenableFuture<RpcResult<QueryTcasOutput>> queryTcas(QueryTcasInput input) {
        QueryTcasOutput output = null;
        String property = System.getProperty(IS_OPT_TEST, Boolean.FALSE.toString());
        if (Boolean.TRUE.equals(Boolean.valueOf(property))) {
            TcasBuilder tcasBuilder = new TcasBuilder();
            Map<org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tcas.top.TcaKey, Tca> map = new HashMap<>();
            TcaBuilder tcaBuilder = new TcaBuilder();
            tcaBuilder.setTcaSerialNo("1");
            tcaBuilder.setTcaState(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.types.rev181121.AlarmState.Start);
//            tcaBuilder.setCurrentValue(new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.Real(BigDecimal.ONE));
            tcaBuilder.setGranularity(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.Granularity._15min);
            tcaBuilder.setObjectName(PORT_APSP);
            tcaBuilder.setObjectType(ObjectType.TRANSCEIVER);
            tcaBuilder.setPmParameterName("TCA_OPT_INPUT_POWER");
            tcaBuilder.setStartTime(new org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime(DATE_TIME));
            tcaBuilder.setThresholdType(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.ThresholdType.Low);
//            tcaBuilder.setThresholdValue(new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.Real(BigDecimal.ONE));
            map.put(new org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tcas.top.TcaKey("1"), tcaBuilder.build());
            tcasBuilder.setTca(map);
            output = new AlarmServiceTransform().devCurrentTcaAlarmToApi(tcasBuilder.build());
        } else {
            @NonNull InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tca.top.Tcas> iid = create(TcaTop.class).child(org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tca.top.Tcas.class);
            org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tca.top.Tcas tcas = MountTools.queryFromOperational(input.getNeId(), iid);
            output = new AlarmServiceTransform().devCurrentTcaAlarmToApi(tcas);
        }

        return RpcResultUtil.success(output);
    }

    @Override
    public ListenableFuture<RpcResult<QueryAlarmHistoryOutput>> queryAlarmHistory(QueryAlarmHistoryInput input) {
        Alarms alarms = null;
        String property = System.getProperty(IS_OPT_TEST, Boolean.FALSE.toString());
        if (Boolean.TRUE.equals(Boolean.valueOf(property))) {
            org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.get.history.alarms.output.AlarmsBuilder alarmsBuilder = new org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.get.history.alarms.output.AlarmsBuilder();
            alarmsBuilder.setAlarm(Maps.newHashMap());
            HashMap<AlarmKey, Alarm> map = new HashMap<>();
            AlarmBuilder alarmBuilder = new AlarmBuilder();
            alarmBuilder.setAdditionalText("15 min input power is lower than threshold");
            alarmBuilder.setAlarmCode("LANE_IN_PWR_L_15MIN");
            alarmBuilder.setAlarmSerialNo(Uint64.valueOf(2717947810L));
            alarmBuilder.setAlarmState(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.types.rev181121.AlarmState.Start);
            alarmBuilder.setEndTime(new org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime(DATE_TIME));
            alarmBuilder.setObjectName(PORT_APSP);
            alarmBuilder.setObjectType(ObjectType.PORT);
//            alarmBuilder.setPerceivedSeverity(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.types.rev181121.MAJOR.class);
            alarmBuilder.setStartTime(new org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime(DATE_TIME));
            map.put(new AlarmKey(Uint64.ONE), alarmBuilder.build());
            alarmsBuilder.setAlarm(map);
            alarms = alarmsBuilder.build();
        } else {
            MiniotnRpcService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnRpcService.class);
            ListenableFuture<RpcResult<GetHistoryAlarmsOutput>> historyAlarms = rpcService.getHistoryAlarms(new AlarmServiceTransform().apiToGetHistoryAlarmsInput(input));
            try {
                RpcResult<GetHistoryAlarmsOutput> rpcResult = historyAlarms.get();
                if (null != rpcResult && rpcResult.isSuccessful() && null != rpcResult.getResult() && null != rpcResult.getResult().getAlarms()) {
                    alarms = rpcResult.getResult().getAlarms();
                }
            } catch (Exception e) {
                //Ignored
            }
        }
        QueryAlarmHistoryOutput output = new AlarmServiceTransform().devToQueryAlarmHistoryOutput(alarms);
        return RpcResultUtil.success(output);
    }

    @Override
    public ListenableFuture<RpcResult<QueryTcaHistoryOutput>> queryTcaHistory(QueryTcaHistoryInput input) {
        Tcas tcas = null;
        String property = System.getProperty(IS_OPT_TEST, Boolean.FALSE.toString());
        if (Boolean.TRUE.equals(Boolean.valueOf(property))) {
            org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.get.history.alarms.output.TcasBuilder tcasBuilder = new org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.get.history.alarms.output.TcasBuilder();
            HashMap<org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tcas.top.TcaKey, Tca> map = new HashMap<>();
            TcaBuilder tcaBuilder = new TcaBuilder();
            tcaBuilder.setTcaSerialNo("1");
            tcaBuilder.setTcaState(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.types.rev181121.AlarmState.Start);
//            tcaBuilder.setCurrentValue(new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.Real(BigDecimal.ONE));
            tcaBuilder.setGranularity(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.Granularity._15min);
            tcaBuilder.setObjectName(PORT_APSP);
            tcaBuilder.setObjectType(ObjectType.TRANSCEIVER);
            tcaBuilder.setPmParameterName("TCA_OPT_INPUT_POWER");
            tcaBuilder.setStartTime(new org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime(DATE_TIME));
            tcaBuilder.setEndTime(new org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime(DATE_TIME));
            tcaBuilder.setThresholdType(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.ThresholdType.Low);
//            tcaBuilder.setThresholdValue(new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.Real(BigDecimal.ONE));
            map.put(new org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tcas.top.TcaKey("1"), tcaBuilder.build());
            tcasBuilder.setTca(map);
            tcas = tcasBuilder.build();
        } else {
            MiniotnRpcService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnRpcService.class);
            ListenableFuture<RpcResult<GetHistoryAlarmsOutput>> historyAlarms = rpcService.getHistoryAlarms(new AlarmServiceTransform().apiToGetHistoryAlarmsInput(input));
            try {
                RpcResult<GetHistoryAlarmsOutput> rpcResult = historyAlarms.get();
                if (null != rpcResult && rpcResult.isSuccessful() && null != rpcResult.getResult() && null != rpcResult.getResult().getTcas()) {
                    tcas = rpcResult.getResult().getTcas();
                }

            } catch (Exception e) {
                //Ignored
            }

        }
        QueryTcaHistoryOutput output = new AlarmServiceTransform().devToQueryAlarmHistoryOutput(tcas);
        return RpcResultUtil.success(output);
    }

    private void filterTime(QueryTcaHistoryOutput output, org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.yang.rev220208.DateAndTime startTime, org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.yang.rev220208.DateAndTime endTime) {
        if (output == null) {
            return;
        }
        Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.rev220208.tcas.top.TcaKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.rev220208.tcas.top.Tca> tca = output.getTca();
        if (CollUtil.isEmpty(tca)) {
            return;
        }
        for (Map.Entry<org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.rev220208.tcas.top.TcaKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.alarms.rev220208.tcas.top.Tca> entry : tca.entrySet()) {
            if (entry.getValue().getStartTime() != null && startTime != null) {
                Long devStart = new AlarmServiceTransform().dateAndTimeToLong(new org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime(entry.getValue().getStartTime().getValue()));
                Long inputStart = new AlarmServiceTransform().dateAndTimeToLong(new org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime(startTime.getValue()));
                if (inputStart > devStart) {
                    tca.remove(entry.getKey());
                    continue;
                }
            }
            if (entry.getValue().getEndTime() != null && endTime != null) {
                Long devEnd = new AlarmServiceTransform().dateAndTimeToLong(new org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime(entry.getValue().getEndTime().getValue()));
                Long inputEnd = new AlarmServiceTransform().dateAndTimeToLong(new org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime(endTime.getValue()));
                if (inputEnd < devEnd) {
                    tca.remove(entry.getKey());
                }
            }
        }

    }

}
