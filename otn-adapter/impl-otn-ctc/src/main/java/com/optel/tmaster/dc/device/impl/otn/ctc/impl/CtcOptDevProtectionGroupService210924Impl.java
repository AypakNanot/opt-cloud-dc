/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.impl;

import cn.hutool.core.collection.CollUtil;
import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptDevProtectionGroupServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.DevProtectionGroupTransformImpl;
import com.optel.tmaster.dc.general.base.action.YangMode;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import com.optel.tmaster.dc.general.nc.nccore.NcTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.dev.protection.rev200901.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.SfPersistencyTimers;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.sf.persistency.timers.grouping.SfPersistencyTimer;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.sf.persistency.timers.grouping.SfPersistencyTimerKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.AccProtectionGroupService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.EqPgs;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.PerformEqProtectionCommandInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.eq.pgs.EqPgKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.Uint16;

import java.util.*;

/**
 * CtcOptDevProtectionGroupServiceImpl
 * 业务保护组配置 电信老版本兼容
 * date       time        author
 * ─────────────────────────────
 * 2023/2/14   10:56      QJY
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author QJY
 * @version V1.0.0
 */
public class CtcOptDevProtectionGroupService210924Impl extends BaseOptDevProtectionGroupServiceImpl implements IDeviceServiceOtnCtc {
    /**
     * 当前服务支持的Yang Mode类型
     *
     * @return 支持的类型
     */
    @Override
    public Collection<YangMode> supportDevice() {
        return CollUtil.newArrayList(YangMode.OTN_CTC_OLD_MODE);
    }

    @Override
    public ListenableFuture<RpcResult<ModifySfPersistencyTimerOutput>> modifySfPersistencyTimer(ModifySfPersistencyTimerInput input) {
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            SfPersistencyTimerKey key = new SfPersistencyTimerKey(input.getPgId());
            InstanceIdentifier<SfPersistencyTimer> iid = create(SfPersistencyTimers.class)
                    .child(SfPersistencyTimer.class, key);
            MountTools.doMergeToConfig(input.getNeId(), iid
                    , new DevProtectionGroupTransformImpl().apiSfPersistencyTimerToDev(input).build());
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QuerySfPersistencyTimersOutput>> querySfPersistencyTimers(QuerySfPersistencyTimersInput input) {
        QuerySfPersistencyTimersOutputBuilder outputBuilder = new QuerySfPersistencyTimersOutputBuilder();
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            Set<Uint16> pgIdList = input.getPgId();
            if (pgIdList == null) {
                InstanceIdentifier<SfPersistencyTimers> iid = create(SfPersistencyTimers.class);
                SfPersistencyTimers sfPersistencyTimers = MountTools.queryFromOperational(input.getNeId(), iid);
                if (sfPersistencyTimers != null && sfPersistencyTimers.getSfPersistencyTimer() != null) {
                    outputBuilder.setSfPersistencyTimer(
                            ltm(new DevProtectionGroupTransformImpl().devSfPersistencyTimerToApi(
                                    sfPersistencyTimers.getSfPersistencyTimer().values())));
                }
            } else if (pgIdList.size() == 1) {
                InstanceIdentifier<SfPersistencyTimer> iid = create(SfPersistencyTimers.class)
                        .child(SfPersistencyTimer.class, new SfPersistencyTimerKey(pgIdList.iterator().next()));
                SfPersistencyTimer sfPersistencyTimer = MountTools.queryFromOperational(input.getNeId(), iid);
                if (sfPersistencyTimer != null) {
                    List<SfPersistencyTimer> list = new LinkedList<>();
                    list.add(sfPersistencyTimer);
                    outputBuilder.setSfPersistencyTimer(ltm(new DevProtectionGroupTransformImpl().devSfPersistencyTimerToApi(list)));
                }
            } else {
                InstanceIdentifier<SfPersistencyTimers> iid = create(SfPersistencyTimers.class);
                SfPersistencyTimers sfPersistencyTimers = MountTools.queryFromOperational(input.getNeId(), iid);
                if (sfPersistencyTimers != null) {
                    outputBuilder.setSfPersistencyTimer(
                            ltm(new DevProtectionGroupTransformImpl().devSfPersistencyTimerToApi(
                                    filterSfPersistencyTimer(sfPersistencyTimers.getSfPersistencyTimer(), pgIdList))));
                }
            }
        }
        return RpcResultUtil.success(outputBuilder.build());
    }

    /**
     * 根据pgId过滤SfPersistencyTimer
     *
     * @param sfPersistencyTimerList sfPersistencyTimerList 待过滤数据
     * @param pgIdList               pgIdList pgId List
     * @return 过滤后的数据
     */
    private List<SfPersistencyTimer> filterSfPersistencyTimer(Map<SfPersistencyTimerKey, SfPersistencyTimer> sfPersistencyTimerList, Set<Uint16> pgIdList) {
        List<SfPersistencyTimer> list = new ArrayList<>();
        if (CollUtil.isNotEmpty(pgIdList) && sfPersistencyTimerList != null) {
            for (Uint16 pgId : pgIdList) {
                SfPersistencyTimer sfPersistencyTimer = sfPersistencyTimerList.get(new SfPersistencyTimerKey(pgId));
                if (sfPersistencyTimer != null) {
                    list.add(sfPersistencyTimer);
                }
            }
        }
        return list;
    }

    @Override
    public ListenableFuture<RpcResult<QueryDevProtectionGroupOutput>> queryDevProtectionGroup(QueryDevProtectionGroupInput input) {
        List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.eq.pgs.EqPg> pgs = new ArrayList<>();
        if (input.getDevPgId() != null) {
            InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.eq.pgs.EqPg> iid =
                    create(EqPgs.class).child(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.eq.pgs.EqPg.class, new EqPgKey(input.getDevPgId()));
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.eq.pgs.EqPg eqPg = MountTools.queryFromOperational(input.getNeId(), iid);
            if (eqPg != null) {
                pgs.add(eqPg);
            }
        } else {
            InstanceIdentifier<EqPgs> iid = create(EqPgs.class);
            EqPgs eqPgs = MountTools.queryFromOperational(input.getNeId(), iid);
            if (eqPgs != null) {
                pgs = new ArrayList<>(eqPgs.getEqPg().values());
            }
        }
        QueryDevProtectionGroupOutputBuilder queryDevProtectionGroupOutputBuilder = new DevProtectionGroupTransformImpl().devProtectionGroupRev210924ToApi(pgs);

        return RpcResultUtil.success(queryDevProtectionGroupOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<DevPgSwitchOutput>> devPgSwitch(DevPgSwitchInput input) {
        AccProtectionGroupService service = MountTools.getRpcService(input.getNeId(), AccProtectionGroupService.class);
        PerformEqProtectionCommandInputBuilder inputBuilder = new DevProtectionGroupTransformImpl().apiDevPgSwitchInputRev210924ToDev(input);
        return RpcResultUtil.buildFutureResult(service.performEqProtectionCommand(inputBuilder.build()));
    }
}
