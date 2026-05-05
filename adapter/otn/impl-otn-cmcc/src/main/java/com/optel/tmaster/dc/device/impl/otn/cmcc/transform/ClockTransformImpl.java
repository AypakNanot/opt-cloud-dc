/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform;

import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.AbstractCmccTransformer;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.EnumTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.clock.rev201119.Get2mExtClockOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.clock.rev201119.GetCurrentClockSourceOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.clock.rev201119.GetGlobalSsmOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.clock.rev201119.GetSyncSourceClockOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.Ext2mClock;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.GlobalSsmGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.SyncClockSource;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.*;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.ext._2m.clocks.Ext2mClockBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.ext._2m.clocks.Ext2mClockKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.sync.clock.sources.SyncClockSourceBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.sync.clock.sources.SyncClockSourceKey;

import java.util.ArrayList;
import java.util.List;

/**
 * 时钟 转换
 *
 * @author Quan Jingyuan
 * @since 2021/10/12
 **/
public class ClockTransformImpl extends AbstractCmccTransformer implements CommonTransform, EnumTransform {
    public SyncClockSourceBuilder apiSyncClockSourceToDev(SyncClockSource syncClockSource) {
        SyncClockSourceBuilder builder = new SyncClockSourceBuilder();
        if (syncClockSource == null) {
            return builder;
        }
        builder.setName(syncClockSource.getName());
        builder.withKey(new SyncClockSourceKey(syncClockSource.getName()));
        builder.setPriority(syncClockSource.getPriority());
        if (syncClockSource.getRxQl() != null) {
            builder.setRxQl(apiQlToDev(syncClockSource.getRxQl()));

        }
        if (syncClockSource.getSsmForceQl() != null) {
            builder.setSsmForceQl(apiQlToDev(syncClockSource.getSsmForceQl()));

        }
        if (syncClockSource.getSsmMode() != null) {
            builder.setSsmMode(apiSsmModeToDev(syncClockSource.getSsmMode()));
        }
        return builder;
    }

    public GetSyncSourceClockOutputBuilder devSyncSourceClockToApi(SyncClockSources syncClockSources) {
        GetSyncSourceClockOutputBuilder builder = new GetSyncSourceClockOutputBuilder();
        if (syncClockSources == null || syncClockSources.getSyncClockSource() == null) {
            return builder;
        }
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.sync.clock.sources.group.SyncClockSourceBuilder> clockSources = new ArrayList<>();

        for (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.SyncClockSource syncClockSource : syncClockSources.getSyncClockSource().values()) {
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.sync.clock.sources.group.SyncClockSourceBuilder clockSource = new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.sync.clock.sources.group.SyncClockSourceBuilder();
            clockSource.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.sync.clock.sources.group.SyncClockSourceKey(syncClockSource.getName()));
            clockSource.setName(syncClockSource.getName());
            clockSource.setPriority(syncClockSource.getPriority());
            if (syncClockSource.getSsmForceQl() != null) {
                clockSource.setSsmForceQl(devQlToApi(syncClockSource.getSsmForceQl()));
            }
            if (syncClockSource.getRxQl() != null) {
                clockSource.setRxQl(devQlToApi(syncClockSource.getRxQl()));
            }
            if (syncClockSource.getSsmMode() != null) {
                clockSource.setSsmMode(devSsmModeToApi(syncClockSource.getSsmMode()));

            }
            clockSources.add(clockSource);
        }
        builder.setSyncClockSource(ctm(clockSources,
                org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.sync.clock.sources.group.SyncClockSourceBuilder::key,
                org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.sync.clock.sources.group.SyncClockSourceBuilder::build));
        return builder;
    }

    public GetCurrentClockSourceOutputBuilder devCurrentClockSourceApi(CurrentSyncClock currentSyncClock) {
        GetCurrentClockSourceOutputBuilder builder = new GetCurrentClockSourceOutputBuilder();
        if (currentSyncClock == null) {
            return builder;
        }
        if (currentSyncClock.getCurrentQl() != null) {
            builder.setCurrentQl(devQlToApi(currentSyncClock.getCurrentQl()));
        }
        builder.setName(currentSyncClock.getName());
        if (currentSyncClock.getPllState() != null) {
            builder.setPllState(devPllStateApi(currentSyncClock.getPllState()));
        }
        return builder;
    }

    public GetGlobalSsmOutputBuilder devGlobalSsmToApi(GlobalSsm globalSsm) {
        GetGlobalSsmOutputBuilder builder = new GetGlobalSsmOutputBuilder();
        if (globalSsm == null) {
            return builder;
        }
        builder.setSsmEnable(globalSsm.getSsmEnable());
        builder.setWtr(globalSsm.getWtr());
        return builder;
    }

    public Ext2mClockBuilder apiExt2mClockToDev(Ext2mClock clock) {
        Ext2mClockBuilder builder = new Ext2mClockBuilder();
        if (clock == null) {
            return null;
        }
        builder.setName(clock.getName());
        builder.withKey(new Ext2mClockKey(clock.getName()));
        builder.setSaRxBit(clock.getSaRxBit());
        builder.setSaTxBit(clock.getSaTxBit());
        if (clock.getQlLimit() != null) {
            builder.setQlLimit(apiQlToDev(clock.getQlLimit()));
        }
        if (clock.getClk2mType() != null) {
            builder.setClk2mType(apiClk2mTypeToDev(clock.getClk2mType()));
        }
        return builder;
    }

    public Get2mExtClockOutputBuilder devGet2mExtClockOutputToApi(Ext2mClocks ext2mClocks) {
        Get2mExtClockOutputBuilder builder = new Get2mExtClockOutputBuilder();
        if (ext2mClocks == null || ext2mClocks.getExt2mClock() == null) {
            return builder;
        }
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.ext.clock._2m.grouping.Ext2mClockBuilder> ext2mClockList = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.Ext2mClock ext2mClock : ext2mClocks.getExt2mClock().values()) {
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.ext.clock._2m.grouping.Ext2mClockBuilder clock = new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.ext.clock._2m.grouping.Ext2mClockBuilder();
            clock.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.ext.clock._2m.grouping.Ext2mClockKey(ext2mClock.getName()));
            clock.setName(ext2mClock.getName());
            clock.setSaRxBit(ext2mClock.getSaRxBit());
            clock.setSaTxBit(ext2mClock.getSaTxBit());
            if (ext2mClock.getQlLimit() != null) {
                clock.setQlLimit(devQlToApi(ext2mClock.getQlLimit()));

            }
            if (ext2mClock.getClk2mType() != null) {
                clock.setClk2mType(devClk2mTypeToApi(ext2mClock.getClk2mType()));
            }
            ext2mClockList.add(clock);
        }
        builder.setExt2mClock(ctm(ext2mClockList,
                org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.ext.clock._2m.grouping.Ext2mClockBuilder::key,
                org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.ext.clock._2m.grouping.Ext2mClockBuilder::build));
        return builder;
    }

    public GlobalSsmBuilder apiGlobalSsmToDev(GlobalSsmGrouping ssmGrouping) {
        GlobalSsmBuilder builder = new GlobalSsmBuilder();
        if (ssmGrouping == null) {
            return null;
        }
        builder.setSsmEnable(ssmGrouping.getSsmEnable());
        builder.setWtr(ssmGrouping.getWtr());
        return builder;
    }
}
