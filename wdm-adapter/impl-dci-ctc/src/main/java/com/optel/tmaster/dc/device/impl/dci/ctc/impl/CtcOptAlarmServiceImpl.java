/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciAlarmServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.alarm.AlarmServiceTransform;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.*;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.create.alarm.mask.input.MaskAlarms;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.alarm.rev200210.create.alarm.mask.input.MaskAlarmsKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.rev200630.alarms.mask.top.AlarmsMask;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.rev200630.alarms.mask.top.alarms.mask.AlarmMask;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.rev200630.alarms.mask.top.alarms.mask.AlarmMaskKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.mask.rev200630.alarms.mask.top.alarms.mask.alarm.mask.Config;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.alarms.rev200630.alarms.top.Alarms;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.top.System;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.Map;
import java.util.Set;

/**
 * dci ctc 告警实现类
 * 2022/3/14 9:00
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class CtcOptAlarmServiceImpl extends BaseDciAlarmServiceImpl implements IDeviceServiceWdmCtc {
    @Override
    public ListenableFuture<RpcResult<GetAlarmMaskOutput>> getAlarmMask(GetAlarmMaskInput input) {
        @NonNull InstanceIdentifier<AlarmsMask> iid = create(AlarmsMask.class);
        AlarmsMask alarmsMask = MountTools.queryFromOperational(input.getNeId(), iid);
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
            for (MaskAlarmsKey key : maskAlarmMap.keySet()) {
                try {
                    MaskAlarms maskAlarm = maskAlarmMap.get(key);
                    @NonNull InstanceIdentifier<Config> iid
                            = create(AlarmsMask.class)
                            .child(AlarmMask.class, new AlarmMaskKey(key.getIndex()))
                            .child(Config.class);
                    MountTools.doMergeToConfig(input.getNeId(), iid, new AlarmServiceTransform().apiCreateAlarmMaskToDev(maskAlarm));
                } catch (Exception e) {
                    //todo 当有部分失败时，需要记录操作结果
                }

            }
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetAlarmCurrentOutput>> getAlarmCurrent(GetAlarmCurrentInput input) {
        InstanceIdentifier<System> root = create(System.class);
        @NonNull InstanceIdentifier<Alarms> iid = root.child(Alarms.class);
        Alarms alarms = MountTools.queryFromOperational(input.getNeId(), iid);
        return RpcResultUtil.success(new AlarmServiceTransform().devGetAlarmCurrentOutputToApi(alarms));
    }

    @Override
    public ListenableFuture<RpcResult<QueryTcasOutput>> queryTcas(QueryTcasInput input) {
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryAlarmHistoryOutput>> queryAlarmHistory(QueryAlarmHistoryInput input) {
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryTcaHistoryOutput>> queryTcaHistory(QueryTcaHistoryInput input) {
        return RpcResultUtil.success();
    }
}
