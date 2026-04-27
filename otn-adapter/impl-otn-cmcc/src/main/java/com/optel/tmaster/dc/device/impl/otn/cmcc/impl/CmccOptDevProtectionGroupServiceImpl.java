/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.impl;

import cn.hutool.core.collection.CollUtil;
import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptDevProtectionGroupServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.DevProtectionGroupTransformImpl;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import com.optel.tmaster.dc.general.nc.nccore.NcTools;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.dev.protection.rev200901.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.SfPersistencyTimers;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.sf.persistency.timers.grouping.SfPersistencyTimer;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.sf.persistency.timers.grouping.SfPersistencyTimerKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.AccProtectionGroupService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.EqPgs;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.PerformEqProtectionCommandInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.eq.pgs.EqPg;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.eq.pgs.EqPgKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.Uint16;

import java.util.*;

/**
 * <ul>
 * <li>(板卡保护组)</li>
 * </ul>
 *
 * @author liwenxue 2020/9/1 13:44
 */
public class CmccOptDevProtectionGroupServiceImpl extends BaseOptDevProtectionGroupServiceImpl implements IDeviceServiceOtnCmcc {

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
            @Nullable Set<Uint16> pgIdList = input.getPgId();
            if (pgIdList == null) {
                InstanceIdentifier<SfPersistencyTimers> iid = create(SfPersistencyTimers.class);
                SfPersistencyTimers sfPersistencyTimers = MountTools.queryFromOperational(input.getNeId(), iid);
                if (sfPersistencyTimers != null && sfPersistencyTimers.getSfPersistencyTimer() != null) {
                    outputBuilder.setSfPersistencyTimer(ltm(
                            new DevProtectionGroupTransformImpl().devSfPersistencyTimerToApi(
                                    new ArrayList<>(sfPersistencyTimers.getSfPersistencyTimer().values()))));
                }
            } else if (pgIdList.size() == 1) {
                InstanceIdentifier<SfPersistencyTimer> iid = create(SfPersistencyTimers.class)
                        .child(SfPersistencyTimer.class, new SfPersistencyTimerKey(CollUtil.getFirst(pgIdList)));
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
                    outputBuilder.setSfPersistencyTimer(ltm(
                            new DevProtectionGroupTransformImpl().devSfPersistencyTimerToApi(
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
        List<EqPg> pgs = new ArrayList<>();
        if (input.getDevPgId() != null) {
            InstanceIdentifier<EqPg> iid =
                    create(EqPgs.class).child(EqPg.class, new EqPgKey(input.getDevPgId()));
            EqPg devPg = MountTools.queryFromOperational(input.getNeId(), iid);
            if (devPg != null) {
                pgs.add(devPg);
            }
        } else {
            InstanceIdentifier<EqPgs> iid = create(EqPgs.class);
            EqPgs eqPgs = MountTools.queryFromOperational(input.getNeId(), iid);
            if (eqPgs != null && eqPgs.getEqPg() != null) {
                pgs = new ArrayList<>(eqPgs.getEqPg().values());
            }
        }
        QueryDevProtectionGroupOutputBuilder queryDevProtectionGroupOutputBuilder = new DevProtectionGroupTransformImpl().devProtectionGroupToApi(pgs);
        return RpcResultUtil.success(queryDevProtectionGroupOutputBuilder.build());

    }

    @Override
    public ListenableFuture<RpcResult<DevPgSwitchOutput>> devPgSwitch(DevPgSwitchInput input) {
        AccProtectionGroupService service = MountTools.getRpcService(input.getNeId(), AccProtectionGroupService.class);
        PerformEqProtectionCommandInputBuilder inputBuilder = new DevProtectionGroupTransformImpl().apiDevPgSwitchInputToDev(input);
        return RpcResultUtil.buildFutureResult(service.performEqProtectionCommand(inputBuilder.build()));
    }
}
