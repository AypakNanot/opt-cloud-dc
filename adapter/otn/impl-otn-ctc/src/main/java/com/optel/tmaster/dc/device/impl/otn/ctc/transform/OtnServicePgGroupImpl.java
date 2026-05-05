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
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.ServiceTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.protection.rev200425.PerformProtectionCommandInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.protection.rev200425.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.*;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.PerformProtectionCommandInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.PreCreatePgInput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.PreCreatePgInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.PreCreatePgOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.PgBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.PgKey;
import org.opendaylight.yangtools.yang.common.Uint16;
import org.opendaylight.yangtools.yang.common.Uint32;
import org.opendaylight.yangtools.yang.common.Uint64;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 业务保护组 转换
 *
 * @author Quan Jingyuan
 * @since 2021/10/15
 **/
public class OtnServicePgGroupImpl extends AbstractCtcTransformer implements CommonTransform, EnumTransform, ServiceTransform {

    public PgBuilder apiPgToDev(ModifyProtectionGroupInput input) {
        PgBuilder pgBuilder = new PgBuilder();
        if (input == null) {
            return pgBuilder;
        }
        pgBuilder.withKey(new PgKey(input.getPgId())).setPgId(input.getPgId()).setWaitToRestoreTime(input.getWaitToRestoreTime()).setHoldOff(input.getHoldOff());
        if (input.getSdTrigger() != null) {
            pgBuilder.setSdTrigger(apiSdTriggerToDev(input.getSdTrigger()));
        }
        if (input.getSwitchDirection() != null) {
            pgBuilder.setSwitchType(apiSwitchDirectionToDev(input.getSwitchDirection()));
        }
        if (input.getReversionMode() != null) {
            pgBuilder.setReversionMode(apiReversionModeToDev(input.getReversionMode()));
        }
        return pgBuilder;
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.PgBuilder apiPgrev240702ToDev(ModifyProtectionGroupInput input) {
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.PgBuilder pgBuilder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.PgBuilder();
        if (input == null) {
            return pgBuilder;
        }
        pgBuilder.withKey(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.PgKey(input.getPgId())).setPgId(input.getPgId()).setWaitToRestoreTime(input.getWaitToRestoreTime()).setHoldOff(input.getHoldOff());
        if (input.getSdTrigger() != null) {
            pgBuilder.setSdTrigger(apiSdTriggerrev240702ToDev(input.getSdTrigger()));
        }
        if (input.getSwitchDirection() != null) {
            pgBuilder.setSwitchType(apiSwitchDirectionrev240702ToDev(input.getSwitchDirection()));
        }
        if (input.getReversionMode() != null) {
            pgBuilder.setReversionMode(apiReversionModerev240702ToDev(input.getReversionMode()));
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
            pgBuilder.setSwitchType(apiSwitchDirectionToDev(input.getSwitchDirection()));
        }
        if (input.getReversionMode() != null) {
            pgBuilder.setReversionMode(apiReversionModeToDev(input.getReversionMode()));
        }
        return pgBuilder;
    }
    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.PgBuilder apiPgrev240702ToDev(CreateProtectionGroupInput input) {
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.PgBuilder pgBuilder = new  org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.PgBuilder();
        if (input == null) {
            return pgBuilder;
        }
        pgBuilder.withKey(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.PgKey(input.getPgId())).setPgId(input.getPgId())
                .setTcm(input.getTcm())
                .setHoldOff(input.getHoldOff())
                .setWaitToRestoreTime(input.getWaitToRestoreTime())
                .setPrimaryPort(input.getPrimaryPort()).setSecondaryPort(input.getSecondaryPort());
        if (input.getSdTrigger() != null) {
            pgBuilder.setSdTrigger(apiSdTriggerrev240702ToDev(input.getSdTrigger()));
        }
        if (input.getProtectionType() != null) {
            pgBuilder.setProtectionType(apiProtectionTyperev240702ToDev(input.getProtectionType()));
        }

        if (input.getSwitchDirection() != null) {
            pgBuilder.setSwitchType(apiSwitchDirectionrev240702ToDev(input.getSwitchDirection()));
        }
        if (input.getReversionMode() != null) {
            pgBuilder.setReversionMode(apiReversionModerev240702ToDev(input.getReversionMode()));
        }
        return pgBuilder;
    }
    public QueryProtectionGroupOutputBuilder devQueryProtectionGroupOutputToApi(List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.Pg> pgs) {
        QueryProtectionGroupOutputBuilder builder = new QueryProtectionGroupOutputBuilder();
        if (pgs == null) {
            return builder;
        }
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pgs.grouping.Pg> pgList = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.Pg pg : pgs) {
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pgs.grouping.PgBuilder pgBuilder =
                    new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pgs.grouping.PgBuilder(devPgToApi(pg));
            pgBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pgs.grouping.PgKey(pg.getPgId()));
            pgList.add(pgBuilder.build());
        }
        builder.setPg(ltm(pgList));
        return builder;
    }
    public QueryProtectionGroupOutputBuilder devQueryProtectionGroupOutputrev240702ToApi(List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.Pg> pgs) {
        QueryProtectionGroupOutputBuilder builder = new QueryProtectionGroupOutputBuilder();
        if (pgs == null) {
            return builder;
        }

        Map<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pgs.grouping.PgKey, org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pgs.grouping.Pg> map=new HashMap<>(pgs.size());
        for (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.Pg pg : pgs) {
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pgs.grouping.PgBuilder pgBuilder=new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pgs.grouping.PgBuilder();
            pgBuilder.setSwitchReason(devSwitchReasonServicePgrev240702ToApi(pg.getSwitchReason()));
            pgBuilder.setSwitchDirection(devSwitchDirectionrev240702ToApi(pg.getSwitchType()));
            pgBuilder.setProtectionDirection(devProtectionDirectionServicePgrev240702ToApi(pg.getProtectionDirection()));
            pgBuilder.setTcm(pg.getTcm());
            pgBuilder.setPgId(pg.getPgId());
            pgBuilder.setSelectedPort(pg.getSelectedPort());
            pgBuilder.setPrimaryPort(pg.getPrimaryPort());
            pgBuilder.setSecondaryPort(pg.getSecondaryPort());
            pgBuilder.setHoldOff(pg.getHoldOff());
            pgBuilder.setWaitToRestoreTime(pg.getWaitToRestoreTime());
            pgBuilder.setSdTrigger(devSdTriggerToApi(pg.getSdTrigger()));
            pgBuilder.setReversionMode(devReversionModeToApi(pg.getReversionMode()));
            pgBuilder.setProtectionType(devProtectionTypeToApi(pg.getProtectionType()));
            map.put(new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pgs.grouping.PgKey(pg.getPgId()),pgBuilder.build());
        }
        builder.setPg(map);
        return builder;
    }
    public Pg devPgToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.Pg pg){
        if(pg == null){
            return null;
        }
        return new Pg() {
            @Override
            public Class<? extends Pg> implementedInterface() {
                return null;
            }

            @Override
            public Uint16 getPgId() {
                return pg.getPgId();
            }

            @Override
            public Uint16 getCreateType() {
                return pg.getCreateType();
            }

            @Override
            public Boolean getDeleteCascade() {
                return pg.getDeleteCascade();
            }

            @Override
            public ProtectionType getProtectionType() {
                return devProtectionTypeToApi(pg.getProtectionType());
            }

            @Override
            public ReversionMode getReversionMode() {
                return devReversionModeToApi(pg.getReversionMode());
            }

            @Override
            public Uint16 getTcm() {
                return pg.getTcm();
            }

            @Override
            public SwitchDirection getSwitchDirection() {
                return devSwitchDirectionToApi(pg.getSwitchType());
            }

            @Override
            public SwitchReason getSwitchReason() {
                return devSwitchReasonServicePgToApi(pg.getSwitchReason());
            }

            @Override
            public ProtectionDirection getProtectionDirection() {
                return devProtectionDirectionServicePgToApi(pg.getProtectionDirection());
            }

            @Override
            public Uint32 getHoldOff() {
                return pg.getHoldOff();
            }

            @Override
            public Uint64 getWaitToRestoreTime() {
                return pg.getWaitToRestoreTime();
            }

            @Override
            public SdTrigger getSdTrigger() {
                return devSdTriggerToApi(pg.getSdTrigger());
            }

            @Override
            public String getPrimaryPort() {
                return pg.getPrimaryPort();
            }

            @Override
            public String getSecondaryPort() {
                return pg.getSecondaryPort();
            }

            @Override
            public String getSelectedPort() {
                return pg.getSelectedPort();
            }
        };
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.PerformProtectionCommandInputBuilder apiPerformProtectionrev240702CommandToDev(PerformProtectionCommandInput input){
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.PerformProtectionCommandInputBuilder builder=new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.PerformProtectionCommandInputBuilder();
        if(input==null){
            return builder;
        }
        builder.setPgId(input.getPgId());
        if(input.getProtectionCommand()!=null){
            builder.setProtectionCommand(apiSwitchReasonrev240702ToDev(input.getProtectionCommand()));
        }
        if(input.getProtectionDirection()!=null){
            builder.setProtectionDirection(apiProtectionDirectionrev240702ToDev(input.getProtectionDirection()));
        }
        return builder;
    }
    public PerformProtectionCommandInputBuilder apiPerformProtectionCommandToDev(PerformProtectionCommandInput input){
        PerformProtectionCommandInputBuilder builder=new PerformProtectionCommandInputBuilder();
        if(input==null){
            return builder;
        }
        builder.setPgId(input.getPgId());
        if(input.getProtectionCommand()!=null){
            builder.setProtectionCommand(apiSwitchReasonToDev(input.getProtectionCommand()));
        }
        if(input.getProtectionDirection()!=null){
            builder.setProtectionDirection(apiProtectionDirectionToDev(input.getProtectionDirection()));
        }
        return builder;
    }

    public PreCreatePgInput apiPreCreatePgInputToDev(PreCreateProtectionGroupInput input){
        PreCreatePgInputBuilder builder = new PreCreatePgInputBuilder();
        builder.setAdaptationType(apiAdaptationTypeToDev(input.getAdaptationType()));
        builder.setHoldOff(input.getHoldOff());
        builder.setOduSignalType(apiClientSignalTypeToDev(input.getOduSignalType()));
        builder.setPrimaryPort(input.getPrimaryPort());
        builder.setProtectionType(input.getProtectionType());
        builder.setReversionMode(apiReversionModeToDev(input.getReversionMode()));
        builder.setSdhSignalType(apiClientSignalTypeToDev(input.getSdhSignalType()));
        builder.setSecondaryPort(input.getSecondaryPort());
        builder.setServiceMappingMode(apiAdaptationTypeToDev(input.getServiceMappingMode()));
        builder.setSwitchCapability(apiSwitchTypeToDev(input.getSwitchCapability()));
        builder.setSwitchType(apiSwitchDirectionToDev(input.getSwitchDirection()));
        builder.setTsDetail(input.getTsDetail());
        builder.setWaitToRestoreTime(input.getWaitToRestoreTime());
        builder.setDeleteCascade(input.getDeleteCascade());
        return builder.build();
    }


    public PreCreateProtectionGroupOutput devPreCreateProtectionOutputToDev(PreCreatePgOutput output){
        if(output == null || output.getPg() == null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pre.create.pg.output.grouping.PgBuilder pgBuilder =
                new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.pre.create.pg.output.grouping.PgBuilder(devPgToApi(output.getPg()));
        return new PreCreateProtectionGroupOutputBuilder().setPg(pgBuilder.build()).build();
    }
}
