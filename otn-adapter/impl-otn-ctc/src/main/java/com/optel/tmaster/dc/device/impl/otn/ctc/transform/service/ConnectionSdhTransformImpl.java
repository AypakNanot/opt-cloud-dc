/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform.service;

import cn.hutool.core.util.StrUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.optel.commons.tools.collection.SetUtil;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateSdhConnectionNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateSdhConnectionNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateSdhConnectionNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.SdhFtp;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.create.sdh.connection.in.grouping.PrimaryNni;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.create.sdh.connection.in.grouping.PrimaryNni2;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.create.sdh.connection.in.grouping.SdhNni;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.create.sdh.connection.in.grouping.SdhUni;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.create.sdh.connection.in.grouping.SecondSdhNni;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.create.sdh.connection.in.grouping.SecondSdhUni;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.create.sdh.connection.in.grouping.SecondaryNni;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.create.sdh.connection.in.grouping.SecondaryNni2;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.create.sdh.connection.out.grouping.Connection;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.create.sdh.connection.out.grouping.ConnectionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.create.sdh.connection.out.grouping.ConnectionKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.sdh.rev210927.create.sdh.connection.out.grouping.CreateComponentBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.SdhSwitchType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.SignalType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Nni;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.CreateSdhConnectionInput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.CreateSdhConnectionInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.CreateSdhConnectionOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.PrimaryNni2Builder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.PrimaryNniBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.SdhNniBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.SdhUniBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.SecondSdhNniBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.SecondSdhUniBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.SecondaryNni2Builder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.SecondaryNniBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.primary.nni.SdhFtpBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.output.err.or.ok.OperOk;

import java.util.*;

/**
 * ConnectionSdhTransformImpl
 * SDH业务
 * date       time        author
 * ─────────────────────────────
 * 2021/10/13   10:29      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class ConnectionSdhTransformImpl extends ConnectionTransformImpl {

    public CreateSdhConnectionInput apiCreateSdhInputToDev(CreateSdhConnectionNeInput input) {
        if (input == null) {
            return null;
        }
        CreateSdhConnectionInputBuilder builder = new CreateSdhConnectionInputBuilder(apiCreateConnectionToDev(input));
        builder.setServiceMappingMode(apiAdaptationTypeToDev(input.getServiceMappingMode()));
        builder.setUniProtectionType(apiProtectionTypeToDev(input.getUniProtectionType()));
        builder.setNniProtectionType(apiProtectionTypeToDev(input.getNniProtectionType()));
        builder.setNniTcm(input.getNniTcm());
        builder.setNni2ProtectionType(apiProtectionTypeToDev(input.getNni2ProtectionType()));
        builder.setNni2Tcm(input.getNni2Tcm());
        builder.setSdhUni(apiSdhUniToDev(input.getSdhUni()));
        builder.setSecondSdhUni(apiSecondSdhUniToDev(input.getSecondSdhUni()));
        builder.setSdhNni(apiSdhNniToDev(input.getSdhNni()));
        builder.setSecondSdhNni(apiSecondSdhNniToDev(input.getSecondSdhNni()));
        builder.setPrimaryNni(apiPrimaryNniToDev(input.getPrimaryNni()));
        builder.setSecondaryNni(apiSecondaryNniToDev(input.getSecondaryNni()));
        builder.setPrimaryNni2(apiPrimaryNni2ToDev(input.getPrimaryNni2()));
        builder.setSecondaryNni2(apiSecondaryNni2ToDev(input.getSecondaryNni2()));
        return builder.build();
    }

    private org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.SdhUni apiSdhUniToDev(SdhUni sdhUni) {
        if (sdhUni == null) {
            return null;
        }
        SdhUniBuilder sdhUniBuilder = new SdhUniBuilder();
        sdhUniBuilder.setMappingPath(sdhUni.getMappingPath());
        sdhUniBuilder.setTimeSlot(sdhUni.getTimeSlot());
        sdhUniBuilder.setUniPtpName(sdhUni.getUniPtpName());
        sdhUniBuilder.setVcType(apiSdhSwitchTypeToDev(sdhUni.getVcType()));
        return sdhUniBuilder.build();
    }

    private org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.SecondSdhUni apiSecondSdhUniToDev(SecondSdhUni secondSdhUni) {
        if (secondSdhUni == null) {
            return null;
        }
        SecondSdhUniBuilder secondSdhUniBuilder = new SecondSdhUniBuilder();
        secondSdhUniBuilder.setMappingPath(secondSdhUni.getMappingPath());
        secondSdhUniBuilder.setTimeSlot(secondSdhUni.getTimeSlot());
        secondSdhUniBuilder.setUniPtpName(secondSdhUni.getUniPtpName());
        secondSdhUniBuilder.setVcType(apiSdhSwitchTypeToDev(secondSdhUni.getVcType()));
        return secondSdhUniBuilder.build();
    }

    private org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.SdhNni apiSdhNniToDev(SdhNni sdhNni) {
        if (sdhNni == null) {
            return null;
        }
        SdhNniBuilder sdhNniBuilder = new SdhNniBuilder();
        sdhNniBuilder.setMappingPath(sdhNni.getMappingPath());
        sdhNniBuilder.setTimeSlot(sdhNni.getTimeSlot());
        sdhNniBuilder.setNniPtpName(sdhNni.getNniPtpName());
        sdhNniBuilder.setVcType(apiSdhSwitchTypeToDev(sdhNni.getVcType()));
        return sdhNniBuilder.build();
    }

    private org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.SecondSdhNni apiSecondSdhNniToDev(SecondSdhNni secondSdhNni) {
        if (secondSdhNni == null) {
            return null;
        }
        SecondSdhNniBuilder secondSdhNniBuilder = new SecondSdhNniBuilder();
        secondSdhNniBuilder.setMappingPath(secondSdhNni.getMappingPath());
        secondSdhNniBuilder.setTimeSlot(secondSdhNni.getTimeSlot());
        secondSdhNniBuilder.setNniPtpName(secondSdhNni.getNniPtpName());
        secondSdhNniBuilder.setVcType(apiSdhSwitchTypeToDev(secondSdhNni.getVcType()));
        return secondSdhNniBuilder.build();
    }

    private org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.PrimaryNni apiPrimaryNniToDev(PrimaryNni primaryNni) {
        if (primaryNni == null) {
            return null;
        }
        PrimaryNniBuilder primaryNniBuilder = new PrimaryNniBuilder(apiNniToDev(primaryNni));
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.SdhFtp sdhFtp = apiSdhFtpToDev(primaryNni.getSdhFtp());
        if (sdhFtp != null) {
            primaryNniBuilder.setSdhFtp(new SdhFtpBuilder(sdhFtp).build());
        }
        return primaryNniBuilder.build();
    }

    private org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.SdhFtp apiSdhFtpToDev(SdhFtp sdhFtp) {
        if (sdhFtp == null) {
            return null;
        }
        return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.SdhFtp() {
            @Override
            public Class<? extends org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.SdhFtp> implementedInterface() {
                return null;
            }

            @Override
            public SignalType getSdhType() {
                return apiClientSignalTypeToDev(sdhFtp.getSdhType());
            }

            @Override
            public SdhSwitchType getVcType() {
                return apiSdhSwitchTypeToDev(sdhFtp.getVcType());
            }

            @Override
            public Set<String> getMappingPath() {
                String mappingPath = sdhFtp.getMappingPath();
                if (StrUtil.isNotBlank(mappingPath)) {
                    String[] sf = mappingPath.split(",");
                    return SetUtil.newHashSet(sf);
                }
                return SetUtil.newHashSet();
            }

            @Override
            public String getTimeSlot() {
                return sdhFtp.getTimeSlot();
            }

        };
    }


    private org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.SecondaryNni apiSecondaryNniToDev(SecondaryNni secondaryNni) {
        if (secondaryNni == null) {
            return null;
        }
        Nni nni = apiNniToDev(secondaryNni);
        if (nni == null) {
            return null;
        }
        SecondaryNniBuilder secondaryNniBuilder = new SecondaryNniBuilder(nni);
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.SdhFtp sdhFtp = apiSdhFtpToDev(secondaryNni.getSdhFtp());
        if (sdhFtp != null) {
            secondaryNniBuilder.setSdhFtp(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.secondary.nni.SdhFtpBuilder(sdhFtp).build());
        }
        return secondaryNniBuilder.build();
    }

    private org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.PrimaryNni2 apiPrimaryNni2ToDev(PrimaryNni2 primaryNni2) {
        if (primaryNni2 == null) {
            return null;
        }
        Nni nni = apiNniToDev(primaryNni2);
        if (nni == null) {
            return null;
        }
        PrimaryNni2Builder primaryNni2Builder = new PrimaryNni2Builder(nni);
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.SdhFtp sdhFtp = apiSdhFtpToDev(primaryNni2.getSdhFtp());
        if (sdhFtp != null) {
            primaryNni2Builder.setSdhFtp(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.primary.nni2.SdhFtpBuilder(sdhFtp).build());
        }
        return primaryNni2Builder.build();
    }

    private org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.SecondaryNni2 apiSecondaryNni2ToDev(SecondaryNni2 secondaryNni2) {
        if (secondaryNni2 == null) {
            return null;
        }
        Nni nni = apiNniToDev(secondaryNni2);
        if (nni == null) {
            return null;
        }
        SecondaryNni2Builder secondaryNni2Builder = new SecondaryNni2Builder(nni);
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.SdhFtp sdhFtp = apiSdhFtpToDev(secondaryNni2.getSdhFtp());
        if (sdhFtp != null) {
            secondaryNni2Builder.setSdhFtp(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.input.secondary.nni2.SdhFtpBuilder(sdhFtp).build());
        }
        return secondaryNni2Builder.build();
    }


    public CreateSdhConnectionNeOutput devCreateSdhConnectionOutputToApi(CreateSdhConnectionOutput output) {
        if (output == null) {
            return null;
        }
        if (output.getErrOrOk().implementedInterface().equals(OperOk.class)) {
            OperOk operOk = (OperOk) output.getErrOrOk();
            if (Objects.isNull(operOk.getConnection())) {
                return null;
            }
            CreateSdhConnectionNeOutputBuilder createSdhConnectionNeOutputBuilder = new CreateSdhConnectionNeOutputBuilder();
            Collection<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.output.err.or.ok.oper.ok.Connection> connections = operOk.getConnection().values();
            if (CollectionUtil.isNotEmpty(connections)) {
                List<Connection> result = new ArrayList<>();
                for (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.create.sdh.connection.output.err.or.ok.oper.ok.Connection connection : connections) {
                    ConnectionBuilder connectionBuilder = new ConnectionBuilder(devConnectionToApi(connection)).withKey(
                            new ConnectionKey(connection.key().getName()));
                    result.add(connectionBuilder.build());
                }
                createSdhConnectionNeOutputBuilder.setConnection(ltm(result));
            }
            if (operOk.getCreateComponent() != null) {
                CreateComponentBuilder createComponentBuilder = new CreateComponentBuilder()
                        .setCtpName(operOk.getCreateComponent().getCtpName())
                        .setFtpName(operOk.getCreateComponent().getFtpName());
                createSdhConnectionNeOutputBuilder.setCreateComponent(createComponentBuilder.build());
            }
            return createSdhConnectionNeOutputBuilder.build();
        } else {
            // 这种情况不会发生
            return null;
        }
    }

}
