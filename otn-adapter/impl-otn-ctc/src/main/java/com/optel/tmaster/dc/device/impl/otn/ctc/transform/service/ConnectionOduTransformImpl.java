/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform.service;

import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateOduConnectionNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateOduConnectionNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateOduConnectionNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.rev210927.create.odu.connection.out.grouping.ConnectionBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.CreateOduConnectionInput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.CreateOduConnectionInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.CreateOduConnectionOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.create.odu.connection.input.ClientSideNniBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.create.odu.connection.input.PrimaryNni2Builder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.create.odu.connection.input.PrimaryNniBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.create.odu.connection.input.SecondaryNni2Builder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.create.odu.connection.input.SecondaryNniBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.create.odu.connection.output.err.or.ok.OperOk;

/**
 * ConnectionOtnTransformImpl
 * ODU 业务
 * date       time        author
 * ─────────────────────────────
 * 2021/10/13   11:30      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class ConnectionOduTransformImpl extends ConnectionTransformImpl{

    public CreateOduConnectionInput apiCreateOduConnectionInputToDev(CreateOduConnectionNeInput input){
        if(input == null){
            return null;
        }
        CreateOduConnectionInputBuilder builder = new CreateOduConnectionInputBuilder(apiCreateConnectionToDev(input));
        builder.setNniProtectionType(apiProtectionTypeToDev(input.getNniProtectionType()));
        builder.setNniTcm(input.getNniTcm());
        builder.setNni2ProtectionType(apiProtectionTypeToDev(input.getNni2ProtectionType()));
        builder.setNni2Tcm(input.getNni2Tcm());
        if(input.getClientSideNni() != null){
            builder.setClientSideNni(new ClientSideNniBuilder(apiNniToDev(input.getClientSideNni())).build());
        }
        if(input.getPrimaryNni() != null){
            builder.setPrimaryNni(new PrimaryNniBuilder(apiNniToDev(input.getPrimaryNni())).build());
        }
        if(input.getSecondaryNni() != null){
            builder.setSecondaryNni(new SecondaryNniBuilder(apiNniToDev(input.getSecondaryNni())).build());
        }
        if(input.getPrimaryNni2() != null){
            builder.setPrimaryNni2(new PrimaryNni2Builder(apiNniToDev(input.getPrimaryNni2())).build());
        }
        if(input.getSecondaryNni2() != null){
            builder.setSecondaryNni2(new SecondaryNni2Builder(apiNniToDev(input.getSecondaryNni2())).build());
        }
        return builder.build();
    }

    /**
     * 创建ODU业务 输出参数 dev转api
     * @param output 输出参数 dev
     * @return 输出参数 api
     */
    public CreateOduConnectionNeOutput devCreateOduConnectionOutputToApi(CreateOduConnectionOutput output){
        if(output == null){
            return null;
        }
        if (output.getErrOrOk().implementedInterface().equals(OperOk.class)) {
            OperOk operOk = (OperOk) output.getErrOrOk();
            CreateOduConnectionNeOutputBuilder outputBuilder = new CreateOduConnectionNeOutputBuilder();
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.Connection connection = devConnectionToApi(operOk.getConnection());
            if(connection != null){
                outputBuilder.setConnection(new ConnectionBuilder(connection).build());
            }
            return outputBuilder.build();
        } else {
            // 这种情况不会发生
            return null;
        }
    }

}
