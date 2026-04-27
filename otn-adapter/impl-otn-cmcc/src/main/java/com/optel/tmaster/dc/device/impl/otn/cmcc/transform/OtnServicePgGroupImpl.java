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
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.protection.rev200425.CreateProtectionGroupInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.protection.rev200425.ModifyProtectionGroupInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.protection.rev200425.PerformProtectionCommandInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.protection.rev200425.QueryProtectionGroupOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.PerformProtectionCommandInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.pgs.PgBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.pgs.PgKey;

import java.util.ArrayList;
import java.util.List;

/**
 * 业务保护组 转换
 *
 * @author Quan Jingyuan
 * @since 2021/10/16
 **/
public class OtnServicePgGroupImpl extends AbstractCmccTransformer implements CommonTransform, EnumTransform {
    public PgBuilder apiPgToDev(ModifyProtectionGroupInput input) {
        PgBuilder pgBuilder = new PgBuilder();
        if (input == null) {
            return pgBuilder;
        }
        pgBuilder.withKey(new PgKey(input.getPgId())).setPgId(input.getPgId())
                .setWaitToRestoreTime(input.getWaitToRestoreTime())
                .setHoldOff(input.getHoldOff());
        if (input.getSdTrigger() != null) {
            pgBuilder.setSdTrigger(apiSdTriggerToDev(input.getSdTrigger()));
        }
        if (input.getSwitchDirection() != null) {
            pgBuilder.setSwitchDirection(apiSwitchDirectionToDev(input.getSwitchDirection()));
        }
        if (input.getReversionMode() != null) {
            pgBuilder.setReversionMode(apiReversionModeToDev(input.getReversionMode()));
        }
        return pgBuilder;
    }

    public PgBuilder apiPgToDev(CreateProtectionGroupInput input) {
        PgBuilder pgBuilder = new PgBuilder();
        if (input == null) {
            return pgBuilder;
        }
        pgBuilder.withKey(new PgKey(input.getPgId())).setPgId(input.getPgId())
                .setTcm(input.getTcm())
                .setHoldOff(input.getHoldOff())
                .setWaitToRestoreTime(input.getWaitToRestoreTime())
                .setPrimaryPort(input.getPrimaryPort()).setSecondaryPort(input.getSecondaryPort());
        if (input.getSdTrigger() != null) {
            pgBuilder.setSdTrigger(apiSdTriggerToDev(input.getSdTrigger()));
        }
        if (input.getProtectionType() != null) {
            pgBuilder.setProtectionType(apiProtectionTypeToDev(input.getProtectionType()));
        }

        if (input.getSwitchDirection() != null) {
            pgBuilder.setSwitchDirection(apiSwitchDirectionToDev(input.getSwitchDirection()));
        }
        if (input.getReversionMode() != null) {
            pgBuilder.setReversionMode(apiReversionModeToDev(input.getReversionMode()));
        }
        return pgBuilder;
    }

    public QueryProtectionGroupOutputBuilder devQueryProtectionGroupOutputToApi(List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.pgs.Pg> pgs) {
        QueryProtectionGroupOutputBuilder builder = new QueryProtectionGroupOutputBuilder();
        if (pgs == null) {
            return builder;
        }
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pgs.grouping.PgBuilder> pgList = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.pgs.Pg pg : pgs) {
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pgs.grouping.PgBuilder pgBuilder = new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pgs.grouping.PgBuilder();
            pgBuilder.setHoldOff(pg.getHoldOff());
            pgBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pgs.grouping.PgKey(pg.getPgId()));
            pgBuilder.setPgId(pg.getPgId());
            pgBuilder.setPrimaryPort(pg.getPrimaryPort());
            pgBuilder.setSecondaryPort(pg.getSecondaryPort());
            pgBuilder.setSelectedPort(pg.getSelectedPort());
            pgBuilder.setWaitToRestoreTime(pg.getWaitToRestoreTime());
            pgBuilder.setTcm(pg.getTcm());

            if (pg.getProtectionDirection() != null) {
                pgBuilder.setProtectionDirection(devProtectionDirectionServicePgToApi(pg.getProtectionDirection()));
            }
            if (pg.getSwitchDirection() != null) {
                pgBuilder.setSwitchDirection(devSwitchDirectionToApi(pg.getSwitchDirection()));
            }
            if (pg.getSdTrigger() != null) {
                pgBuilder.setSdTrigger(devSdTriggerToApi(pg.getSdTrigger()));
            }
            if (pg.getProtectionType() != null) {
                pgBuilder.setProtectionType(devProtectionTypeToApi(pg.getProtectionType()));
            }
            if (pg.getReversionMode() != null) {
                pgBuilder.setReversionMode(devReversionModeToApi(pg.getReversionMode()));
            }
            if (pg.getSwitchReason() != null) {
                pgBuilder.setSwitchReason(devSwitchReasonServicePgToApi(pg.getSwitchReason()));
            }
            pgList.add(pgBuilder);
        }
        builder.setPg(ctm(pgList,
                org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pgs.grouping.PgBuilder::key,
                org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pgs.grouping.PgBuilder::build));
        return builder;
    }

    public PerformProtectionCommandInputBuilder apiPerformProtectionCommandToDev(PerformProtectionCommandInput input) {
        PerformProtectionCommandInputBuilder builder = new PerformProtectionCommandInputBuilder();
        if (input == null) {
            return builder;
        }
        builder.setPgId(input.getPgId());
        if (input.getProtectionCommand() != null) {
            builder.setProtectionCommand(apiSwitchReasonToDev(input.getProtectionCommand()));
        }
        if (input.getProtectionDirection() != null) {
            builder.setProtectionDirection(apiProtectionDirectionToDev(input.getProtectionDirection()));
        }
        return builder;
    }
}
