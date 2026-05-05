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
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptOtnProtectionGroupServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.OtnServicePgGroupImpl;
import com.optel.tmaster.dc.general.base.action.YangMode;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.protection.rev200425.*;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.AccProtectionGroupService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.Pgs;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.PreCreatePgOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.Pg;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.PgBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.PgKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.Future;

/**
 * 2020/4/30 10:27
 *
 * @author lxy
 * @version V1.0.0
 */
public class CtcOptOtnProtectionGroupServiceImpl extends BaseOptOtnProtectionGroupServiceImpl implements IDeviceServiceOtnCtc {

    /**
     * 当前服务支持的Yang Mode类型
     *
     * @return 支持的类型
     */
    @Override
    public Collection<YangMode> supportDevice() {
        return CollUtil.newArrayList(YangMode.OTN_CTC_STANDARD_MODE, YangMode.OTN_CTC_MODE);
    }

    @Override
    public ListenableFuture<RpcResult<ModifyProtectionGroupOutput>> modifyProtectionGroup(ModifyProtectionGroupInput input) {
        PgKey key = new PgKey(input.getPgId());
        InstanceIdentifier<Pg> iid = create(Pgs.class).child(Pg.class, key);
        PgBuilder pgBuilder = new OtnServicePgGroupImpl().apiPgToDev(input);
        MountTools.doMergeToConfig(input.getNeId(), iid, pgBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<PreCreateProtectionGroupOutput>> preCreateProtectionGroup(PreCreateProtectionGroupInput input) {
        AccProtectionGroupService accProtectionGroupService = MountTools.getRpcService(input.getNeId(), AccProtectionGroupService.class);
        Future<RpcResult<PreCreatePgOutput>> rpcResultFuture = accProtectionGroupService.preCreatePg(new OtnServicePgGroupImpl().apiPreCreatePgInputToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResultFuture, r -> new OtnServicePgGroupImpl().devPreCreateProtectionOutputToDev(r));
    }

    @Override
    public ListenableFuture<RpcResult<CreateProtectionGroupOutput>> createProtectionGroup(CreateProtectionGroupInput input) {
        PgKey key = new PgKey(input.getPgId());
        InstanceIdentifier<Pg> iid = create(Pgs.class).child(Pg.class, key);
        PgBuilder pgBuilder = new OtnServicePgGroupImpl().apiPgToDev(input);
        MountTools.putConfig(input.getNeId(), iid, pgBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryProtectionGroupOutput>> queryProtectionGroup(QueryProtectionGroupInput input) {
        List<Pg> pgList = new ArrayList<>();
        if (input.getPgId() != null) {
            //只查一条
            InstanceIdentifier<Pg> iid = create(Pgs.class).child(Pg.class, new PgKey(input.getPgId()));
            Pg pg = MountTools.queryFromOperational(input.getNeId(), iid);
            if (pg != null) {
                pgList.add(pg);
            }
        } else {
            InstanceIdentifier<Pgs> iid = create(Pgs.class);
            //从设备处获取数据
            Pgs pgs = MountTools.queryFromOperational(input.getNeId(), iid);
            if (pgs != null && pgs.getPg() != null) {
                pgList = new ArrayList<>(pgs.getPg().values());
            }
        }
        //组装数据
        QueryProtectionGroupOutputBuilder builder = new OtnServicePgGroupImpl().devQueryProtectionGroupOutputToApi(pgList);

        return RpcResultUtil.success(builder.build());
    }

    @Override
    public ListenableFuture<RpcResult<RemoveProtectionGroupOutput>> removeProtectionGroup(RemoveProtectionGroupInput input) {
        PgKey key = new PgKey(input.getPgId());
        InstanceIdentifier<Pg> iid = create(Pgs.class).child(Pg.class, key);
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<PerformProtectionCommandOutput>> performProtectionCommand(PerformProtectionCommandInput input) {
        AccProtectionGroupService service = MountTools.getRpcService(input.getNeId(), AccProtectionGroupService.class);

        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.PerformProtectionCommandInputBuilder performProtectionCommandInputBuilder =
                new OtnServicePgGroupImpl().apiPerformProtectionCommandToDev(input);

        return RpcResultUtil.buildFutureResult(service.performProtectionCommand(performProtectionCommandInputBuilder.build()));
    }
}
