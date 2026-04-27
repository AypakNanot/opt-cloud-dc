/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptOtnClockServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.ClockTransformImpl;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.clock.rev201119.*;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.*;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.ext._2m.clocks.Ext2mClock;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.ext._2m.clocks.Ext2mClockBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.ext._2m.clocks.Ext2mClockKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.sync.clock.sources.SyncClockSource;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.sync.clock.sources.SyncClockSourceBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.sync.clock.sources.SyncClockSourceKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;


/**
 * 时钟接口
 *
 * @author Quan Jingyuan
 * @since 2021/9/7
 **/
public class CmccOptOtnClockServiceImpl extends BaseOptOtnClockServiceImpl implements IDeviceServiceOtnCmcc {

    @Override
    public ListenableFuture<RpcResult<ModifySyncSourceClockOutput>> modifySyncSourceClock(ModifySyncSourceClockInput input) {
        KeyedInstanceIdentifier<SyncClockSource, SyncClockSourceKey> child = create(SyncClockSources.class).child(SyncClockSource.class, new SyncClockSourceKey(input.getName()));
        SyncClockSourceBuilder sourceBuilder = getSyncClockSourceBuilder(input);
        MountTools.doMergeToConfig(input.getNeId(), child, sourceBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<AddSyncSourceClockOutput>> addSyncSourceClock(AddSyncSourceClockInput input) {
        KeyedInstanceIdentifier<SyncClockSource, SyncClockSourceKey> child = create(SyncClockSources.class).child(SyncClockSource.class, new SyncClockSourceKey(input.getName()));
        MountTools.doMergeToConfig(input.getNeId(), child, getSyncClockSourceBuilder(input).build());
        return RpcResultUtil.success();
    }

    private SyncClockSourceBuilder getSyncClockSourceBuilder(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.SyncClockSource input) {
        ClockTransformImpl clockTransform = new ClockTransformImpl();
        return clockTransform.apiSyncClockSourceToDev(input);
    }

    @Override
    public ListenableFuture<RpcResult<SetGlobalSsmOutput>> setGlobalSsm(SetGlobalSsmInput input) {
        InstanceIdentifier<GlobalSsm> identifier = create(GlobalSsm.class);
        ClockTransformImpl clockTransform = new ClockTransformImpl();
        GlobalSsmBuilder builder = clockTransform.apiGlobalSsmToDev(input);
        MountTools.doMergeToConfig(input.getNeId(), identifier, builder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetGlobalSsmOutput>> getGlobalSsm(GetGlobalSsmInput input) {
        InstanceIdentifier<GlobalSsm> iid = create(GlobalSsm.class);
        GlobalSsm globalSsm = MountTools.queryFromOperational(input.getNeId(), iid);
        if (globalSsm == null) {
            return RpcResultUtil.success();
        }
        ClockTransformImpl clockTransform = new ClockTransformImpl();
        GetGlobalSsmOutputBuilder builder = clockTransform.devGlobalSsmToApi(globalSsm);
        return RpcResultUtil.success(builder.build());
    }

    @Override
    public ListenableFuture<RpcResult<DeleteSyncSourceClockOutput>> deleteSyncSourceClock(DeleteSyncSourceClockInput input) {
        InstanceIdentifier<SyncClockSource> instanceIdentifier = create(SyncClockSources.class).child(SyncClockSource.class, new SyncClockSourceKey(input.getName()));
        MountTools.deleteFromConfig(input.getNeId(), instanceIdentifier);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetSyncSourceClockOutput>> getSyncSourceClock(GetSyncSourceClockInput input) {
        InstanceIdentifier<SyncClockSources> iid = create(SyncClockSources.class);

        SyncClockSources syncClockSources = MountTools.queryFromOperational(input.getNeId(), iid);
        if (syncClockSources == null) {
            return RpcResultUtil.success();
        }
        ClockTransformImpl clockTransform = new ClockTransformImpl();
        GetSyncSourceClockOutputBuilder builder = clockTransform.devSyncSourceClockToApi(syncClockSources);
        return RpcResultUtil.success(builder.build());
    }

    @Override
    public ListenableFuture<RpcResult<GetCurrentClockSourceOutput>> getCurrentClockSource(GetCurrentClockSourceInput input) {
        InstanceIdentifier<CurrentSyncClock> iid = create(CurrentSyncClock.class);
        CurrentSyncClock currentSyncClocks = MountTools.queryFromOperational(input.getNeId(), iid);
        if (currentSyncClocks == null) {
            return RpcResultUtil.success();
        }
        ClockTransformImpl clockTransform = new ClockTransformImpl();
        GetCurrentClockSourceOutputBuilder builder = clockTransform.devCurrentClockSourceApi(currentSyncClocks);
        return RpcResultUtil.success(builder.build());
    }

    @Override
    public ListenableFuture<RpcResult<Set2mExtClockOutput>> set2mExtClock(Set2mExtClockInput input) {
        KeyedInstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.ext._2m.clocks.Ext2mClock, Ext2mClockKey> child = create(Ext2mClocks.class).child(Ext2mClock.class, new Ext2mClockKey(input.getName()));
        ClockTransformImpl clockTransform = new ClockTransformImpl();
        Ext2mClockBuilder extClock2mBuilder = clockTransform.apiExt2mClockToDev(input);
        MountTools.doMergeToConfig(input.getNeId(), child, extClock2mBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<Get2mExtClockOutput>> get2mExtClock(Get2mExtClockInput input) {
        InstanceIdentifier<Ext2mClocks> identifier = create(Ext2mClocks.class);
        Ext2mClocks extClock2ms = null;
        if (input.getName() == null || "".equals(input.getName())) {
            extClock2ms = MountTools.queryFromOperational(input.getNeId(), identifier);
        } else {
            extClock2ms = MountTools.queryFromOperational(input.getName(), identifier);
        }
        if (extClock2ms == null) {
            return RpcResultUtil.success();
        }
        ClockTransformImpl clockTransform = new ClockTransformImpl();
        Get2mExtClockOutputBuilder builder = clockTransform.devGet2mExtClockOutputToApi(extClock2ms);
        return RpcResultUtil.success(builder.build());
    }
}
