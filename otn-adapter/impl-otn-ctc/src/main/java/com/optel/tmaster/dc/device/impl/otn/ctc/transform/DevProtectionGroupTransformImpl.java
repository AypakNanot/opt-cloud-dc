/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform;

import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.AbstractCtcTransformer;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.EnumTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.dev.protection.rev200901.DevPgSwitchInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.dev.protection.rev200901.ModifySfPersistencyTimerInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.dev.protection.rev200901.QueryDevProtectionGroupOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.dev.pgs.grouping.EqPg;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.dev.pgs.grouping.EqPgKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.sf.persistency.timers.grouping.SfPersistencyTimer;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.sf.persistency.timers.grouping.SfPersistencyTimerKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.PerformEqProtectionCommandInputBuilder;
import org.opendaylight.yangtools.yang.common.Uint64;

import java.util.*;

/**
 * 保护组
 * 2021/10/18 9:16
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class DevProtectionGroupTransformImpl extends AbstractCtcTransformer implements CommonTransform, EnumTransform {
    public QueryDevProtectionGroupOutputBuilder devProtectionGroupToApi(List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.eq.pgs.EqPg> devPgs) {
        QueryDevProtectionGroupOutputBuilder builder = new QueryDevProtectionGroupOutputBuilder();
        if (devPgs == null) {
            return builder;
        }
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.dev.pgs.grouping.EqPg> eqPgs=new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.eq.pgs.EqPg item:devPgs){
            org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.dev.pgs.grouping.EqPgBuilder eqPgBuilder=new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.dev.pgs.grouping.EqPgBuilder();
            eqPgBuilder.withKey(new EqPgKey(item.key().getPgId()));
            eqPgBuilder.setPgId(item.getPgId());
            eqPgBuilder.setPrimaryEq(item.getPrimaryEq());
            eqPgBuilder.setSecondaryEq(item.getSecondaryEq());
            eqPgBuilder.setSelectedEq(item.getSelectedEq());
            if(item.getProtectionDirection()!=null){
                eqPgBuilder.setProtectionDirection(devProtectionDirectionToApi(item.getProtectionDirection()));
            }
            if(item.getProtectionType()!=null){
                eqPgBuilder.setProtectionType(devProtectionTypeToApi(item.getProtectionType()));
            }
            if(item.getSwitchReason()!=null){
                eqPgBuilder.setSwitchReason(devSwitchReasonToApi(item.getSwitchReason()));
            }
            eqPgs.add(eqPgBuilder.build());
        }
        builder.setEqPg(ltm(eqPgs));
        return builder;
    }

    public QueryDevProtectionGroupOutputBuilder devProtectionGroupRev210924ToApi(List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.eq.pgs.EqPg> devPgs) {
        QueryDevProtectionGroupOutputBuilder builder = new QueryDevProtectionGroupOutputBuilder();
        if (devPgs == null) {
            return builder;
        }
        Map<EqPgKey, EqPg> values=new HashMap<>(devPgs.size());
        for (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.eq.pgs.EqPg item:devPgs){
            org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.dev.pgs.grouping.EqPgBuilder eqPgBuilder=new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.dev.pgs.grouping.EqPgBuilder();
            eqPgBuilder.withKey(new EqPgKey(item.key().getPgId()));
            eqPgBuilder.setPgId(item.getPgId());
            eqPgBuilder.setPrimaryEq(item.getPrimaryEq());
            eqPgBuilder.setSecondaryEq(item.getSecondaryEq());
            eqPgBuilder.setSelectedEq(item.getSelectedEq());
            if(item.getProtectionDirection()!=null){
                eqPgBuilder.setProtectionDirection(devProtectionDirectionToApi(item.getProtectionDirection()));
            }
            if(item.getProtectionType()!=null){
                eqPgBuilder.setProtectionType(devProtectionTypeToApi(item.getProtectionType()));
            }
            if(item.getSwitchReason()!=null){
                eqPgBuilder.setSwitchReason(devSwitchReasonToApi(item.getSwitchReason()));
            }
            values.put(new EqPgKey(item.getPgId()),eqPgBuilder.build());
        }
        builder.setEqPg(values);
        return builder;
    }
    public PerformEqProtectionCommandInputBuilder apiDevPgSwitchInputToDev(DevPgSwitchInput input){
        PerformEqProtectionCommandInputBuilder builder=new PerformEqProtectionCommandInputBuilder();
        builder.setPgId(Uint64.valueOf(input.getDevPgId().longValue()));
        if(input.getProtectionCommand()!=null){
            builder.setProtectionCommand(apiSwitchReasonToDev(input.getProtectionCommand()));
        }
        if(input.getProtectionDirection()!=null){
            builder.setProtectionDirection(apiProtectionDirectionToDev(input.getProtectionDirection()));
        }
        return builder;
    }
    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.PerformEqProtectionCommandInputBuilder apiDevPgSwitchInputRev210924ToDev(DevPgSwitchInput input){
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.PerformEqProtectionCommandInputBuilder builder=new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.PerformEqProtectionCommandInputBuilder();
      builder.setPgId(input.getDevPgId());
        if(input.getProtectionCommand()!=null){
            builder.setProtectionCommand(apiSwitchReasonRev210924ToDev(input.getProtectionCommand()));
        }
        if(input.getProtectionDirection()!=null){
            builder.setProtectionDirection(apiProtectionDirectionRev210924ToDev(input.getProtectionDirection()));
        }
        return builder;
    }
    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.sf.persistency.timers.grouping.SfPersistencyTimerBuilder
            apiSfPersistencyTimerToDev(ModifySfPersistencyTimerInput input){
        if(input==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.sf.persistency.timers.grouping.SfPersistencyTimerBuilder
                sfPersistencyTimerBuilder
                    = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.sf.persistency.timers.grouping.SfPersistencyTimerBuilder();

        sfPersistencyTimerBuilder.withKey(new SfPersistencyTimerKey(input.getPgId()))
                .setPgId(input.getPgId())
                .setPersistencyTime(input.getPersistencyTime());

        return sfPersistencyTimerBuilder;
    }

    public Set<SfPersistencyTimer>
            devSfPersistencyTimerToApi(
            Collection<org.opendaylight.yang.gen.v1.com.optel.yang.otn
                                .extension.rev210329.sf.persistency.timers.grouping.SfPersistencyTimer> list){
        if(list==null || list.isEmpty()){
            return null;
        }
        Set<SfPersistencyTimer> resultList = new HashSet<>();
        for(org.opendaylight.yang.gen.v1.com.optel.yang.otn
                .extension.rev210329.sf.persistency.timers.grouping.SfPersistencyTimer e:list){
            resultList.add(devSfPersistencyTimerToApi(e).build());
        }
        return resultList;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.sf.persistency.timers.grouping.SfPersistencyTimerBuilder
            devSfPersistencyTimerToApi(
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.sf.persistency
                    .timers.grouping.SfPersistencyTimer sfPersistencyTimer){
        if(sfPersistencyTimer==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.sf.persistency.timers.grouping.SfPersistencyTimerBuilder sfPersistencyTimerBuilder = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.sf.persistency.timers.grouping.SfPersistencyTimerBuilder();
        sfPersistencyTimerBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.sf.persistency.timers.grouping.SfPersistencyTimerKey(sfPersistencyTimer.getPgId()))
                .setPgId(sfPersistencyTimer.getPgId())
                .setPersistencyTime(sfPersistencyTimer.getPersistencyTime());
        return sfPersistencyTimerBuilder;
    }

}
