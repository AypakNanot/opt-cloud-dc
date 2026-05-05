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
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.dev.protection.rev200901.DevPgSwitchInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.dev.protection.rev200901.ModifySfPersistencyTimerInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.dev.protection.rev200901.QueryDevProtectionGroupOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.dev.pgs.grouping.EqPgBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.dev.pgs.grouping.EqPgKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.sf.persistency.timers.grouping.SfPersistencyTimer;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.PerformEqProtectionCommandInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.eq.pgs.EqPg;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * 设备保护组 转换
 *
 * @author Quan Jingyuan
 * @since 2021/10/12
 **/
public class DevProtectionGroupTransformImpl extends AbstractCmccTransformer implements CommonTransform, EnumTransform {
    public QueryDevProtectionGroupOutputBuilder devProtectionGroupToApi(List<EqPg> devPgs) {
        QueryDevProtectionGroupOutputBuilder builder = new QueryDevProtectionGroupOutputBuilder();
        if (devPgs == null) {
            return builder;
        }
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.dev.pgs.grouping.EqPg> eqPgs = new ArrayList<>();
        for (EqPg item : devPgs) {
            EqPgBuilder eqPgBuilder = new EqPgBuilder();
            eqPgBuilder.withKey(new EqPgKey(item.key().getPgId()));
            eqPgBuilder.setPgId(item.getPgId());
            eqPgBuilder.setPrimaryEq(item.getPrimaryEq());
            eqPgBuilder.setSecondaryEq(item.getSecondaryEq());
            eqPgBuilder.setSelectedEq(item.getSelectedEq());

            if (item.getProtectionDirection() != null) {
                eqPgBuilder.setProtectionDirection(devProtectionDirectionToApi(item.getProtectionDirection()));
            }
            if (item.getProtectionType() != null) {
                eqPgBuilder.setProtectionType(devProtectionTypeToApi(item.getProtectionType()));
            }
            if (item.getSwitchReason() != null) {
                eqPgBuilder.setSwitchReason(devSwitchReasonToApi(item.getSwitchReason()));
            }
            eqPgs.add(eqPgBuilder.build());
        }
        builder.setEqPg(ltm(eqPgs));
        return builder;
    }

    public PerformEqProtectionCommandInputBuilder apiDevPgSwitchInputToDev(DevPgSwitchInput input) {
        PerformEqProtectionCommandInputBuilder builder = new PerformEqProtectionCommandInputBuilder();
        builder.setPgId(input.getDevPgId());
        if (input.getProtectionCommand() != null) {
            builder.setProtectionCommand(apiSwitchReasonToDev(input.getProtectionCommand()));
        }
        if (input.getProtectionDirection() != null) {
            builder.setProtectionDirection(apiProtectionDirectionToDev(input.getProtectionDirection()));
        }
        return builder;
    }

    public List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.sf.persistency.timers.grouping.SfPersistencyTimer>
    devSfPersistencyTimerToApi(List<SfPersistencyTimer> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.sf.persistency.timers.grouping.SfPersistencyTimer> resultList = new LinkedList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn
                .extension.rev200805.sf.persistency.timers.grouping.SfPersistencyTimer e : list) {
            resultList.add(devSfPersistencyTimerToApi(e).build());
        }
        return resultList;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.sf.persistency.timers.grouping.SfPersistencyTimerBuilder
    devSfPersistencyTimerToApi(
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.sf.persistency
                    .timers.grouping.SfPersistencyTimer sfPersistencyTimer) {
        if (sfPersistencyTimer == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.sf.persistency.timers.grouping.SfPersistencyTimerBuilder sfPersistencyTimerBuilder = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.sf.persistency.timers.grouping.SfPersistencyTimerBuilder();
        sfPersistencyTimerBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.sf.persistency.timers.grouping.SfPersistencyTimerKey(sfPersistencyTimer.getPgId()))
                .setPgId(sfPersistencyTimer.getPgId())
                .setPersistencyTime(sfPersistencyTimer.getPersistencyTime());
        return sfPersistencyTimerBuilder;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.sf.persistency.timers.grouping.SfPersistencyTimerBuilder
    apiSfPersistencyTimerToDev(ModifySfPersistencyTimerInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.sf.persistency.timers.grouping
                .SfPersistencyTimerBuilder sfPersistencyTimerBuilder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.sf.persistency.timers
                .grouping.SfPersistencyTimerBuilder();
        sfPersistencyTimerBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805
                        .sf.persistency.timers.grouping.SfPersistencyTimerKey(input.getPgId()))
                .setPgId(input.getPgId())
                .setPersistencyTime(input.getPersistencyTime());
        return sfPersistencyTimerBuilder;
    }
}
