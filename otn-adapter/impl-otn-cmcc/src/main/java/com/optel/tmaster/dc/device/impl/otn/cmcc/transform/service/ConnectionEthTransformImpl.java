/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform.service;

import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEthConnectionNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEthConnectionNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEthConnectionNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.Connection;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eth.rev210927.create.eth.connection.in.grouping.EthNni;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eth.rev210927.create.eth.connection.out.grouping.ConnectionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eth.rev210927.create.eth.connection.out.grouping.CreateComponentBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.VlanSpec;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.CreateEthConnectionInput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.CreateEthConnectionInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.CreateEthConnectionOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.EthNniBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.EthUniBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.PrimaryNni2Builder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.PrimaryNniBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.SecondaryNni2Builder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.SecondaryNniBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.eth.nni.FtpClientVlanSpecBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.eth.nni.FtpVlanSpecBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.eth.uni.ClientVlanSpecBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.eth.uni.UniVlanSpecBuilder;

/**
 * ConnectionEthTransformImpl
 * 以太网专线业务
 * date       time        author
 * ─────────────────────────────
 * 2021/10/12   11:10      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class ConnectionEthTransformImpl extends ConnectionTransformImpl {

    /**
     * 创建以太网专线 ETH连接 输入参数 api转dev
     * @param input 创建以太网专线输入参数 api
     * @return 创建以太网专线业务 输入参数 dev
     */
    public CreateEthConnectionInput apiCreateEthConnectionInputToDev(CreateEthConnectionNeInput input){
        if(input == null){
            return null;
        }
        CreateEthConnectionInputBuilder builder = new CreateEthConnectionInputBuilder(apiCreateConnectionToDev(input));
        if(input.getEthNni() != null){
            EthNni ethNni = input.getEthNni();
            EthNniBuilder ethNniBuilder = new EthNniBuilder();
            VlanSpec ftpClientVlanSpec = apiVlanSpecToDev(ethNni.getFtpClientVlanSpec());
            if(ftpClientVlanSpec != null){
                ethNniBuilder.setFtpClientVlanSpec(new FtpClientVlanSpecBuilder(ftpClientVlanSpec).build());
            }
            VlanSpec ftpVlanSpec = apiVlanSpecToDev(ethNni.getFtpVlanSpec());
            if(ftpVlanSpec != null){
                ethNniBuilder.setFtpVlanSpec(new FtpVlanSpecBuilder(ftpVlanSpec).build());
            }
            ethNniBuilder.setNniPtpName(ethNni.getNniPtpName());
            ethNniBuilder.setSecondNniPtpName(ethNni.getSecondNniPtpName());
            builder.setEthNni(ethNniBuilder.build());
        }

        if(input.getEthUni() != null){
            EthUniBuilder ethUniBuilder = new EthUniBuilder();
            VlanSpec clientVlanSpec = apiVlanSpecToDev(input.getEthUni().getClientVlanSpec());
            if (clientVlanSpec != null) {
                ethUniBuilder.setClientVlanSpec(new ClientVlanSpecBuilder(clientVlanSpec).build());
            }
            ethUniBuilder.setSecondUniPtpName(input.getEthUni().getSecondUniPtpName());
            ethUniBuilder.setUniPtpName(input.getEthUni().getUniPtpName());
            VlanSpec vlanSpec = apiVlanSpecToDev(input.getEthUni().getUniVlanSpec());
            if(vlanSpec != null){
                ethUniBuilder.setUniVlanSpec(new UniVlanSpecBuilder(vlanSpec).build());
            }
            builder.setEthUni(ethUniBuilder.build());
        }
        builder.setNni2ProtectionType(apiProtectionTypeToDev(input.getNni2ProtectionType()));
        builder.setNni2Tcm(input.getNni2Tcm());
        builder.setNniProtectionType(apiProtectionTypeToDev(input.getNniProtectionType()));
        builder.setNniTcm(input.getNniTcm());
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Nni primaryNni = apiNniToDev(input.getPrimaryNni());
        if(input.getPrimaryNni() != null && primaryNni != null){
            PrimaryNniBuilder primaryNniBuilder = new PrimaryNniBuilder(primaryNni);
            VlanSpec ftpClientVlanSpec = apiVlanSpecToDev(input.getPrimaryNni().getFtpClientVlanSpec());
            if (ftpClientVlanSpec != null) {
                primaryNniBuilder.setFtpClientVlanSpec(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.primary.nni.FtpClientVlanSpecBuilder(ftpClientVlanSpec).build());
            }
            VlanSpec ftpVlanSpec = apiVlanSpecToDev(input.getPrimaryNni().getFtpVlanSpec());
            if(ftpVlanSpec != null){
                primaryNniBuilder.setFtpVlanSpec(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.primary.nni.FtpVlanSpecBuilder(ftpVlanSpec).build());
            }
            primaryNniBuilder.setTpid(apiTpidDifinitionToDev(input.getPrimaryNni().getTpid()));
            builder.setPrimaryNni(primaryNniBuilder.build());
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Nni primaryNni2 = apiNniToDev(input.getPrimaryNni2());
        if(input.getPrimaryNni2() != null && primaryNni2 != null){
            PrimaryNni2Builder primaryNni2Builder = new PrimaryNni2Builder(primaryNni2);
            VlanSpec ftpVlanSpec = apiVlanSpecToDev(input.getPrimaryNni2().getFtpVlanSpec());
            if(ftpVlanSpec != null){
                primaryNni2Builder.setFtpVlanSpec(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.primary.nni2.FtpVlanSpecBuilder(ftpVlanSpec).build());
                primaryNni2Builder.setTpid(apiTpidDifinitionToDev(input.getPrimaryNni2().getTpid()));
            }
            builder.setPrimaryNni2(primaryNni2Builder.build());
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.Capacity requestedCapacity = apiCapacityToDev(input.getRequestedCapacity());
        if(requestedCapacity != null){
            builder.setRequestedCapacity(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.create.connection.RequestedCapacityBuilder(requestedCapacity).build());
        }
        if(input.getSecondaryNni() != null){
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Nni secondaryNni = apiNniToDev(input.getSecondaryNni());
            if(secondaryNni != null){
                SecondaryNniBuilder secondaryNniBuilder = new SecondaryNniBuilder(secondaryNni);
                VlanSpec ftpClientVlanSpec = apiVlanSpecToDev(input.getSecondaryNni().getFtpClientVlanSpec());
                if(ftpClientVlanSpec != null){
                    secondaryNniBuilder.setFtpClientVlanSpec(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.secondary.nni.FtpClientVlanSpecBuilder(ftpClientVlanSpec).build());
                }
                VlanSpec ftpVlanSpec = apiVlanSpecToDev(input.getSecondaryNni().getFtpVlanSpec());
                if(ftpVlanSpec != null){
                    secondaryNniBuilder.setFtpVlanSpec(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.secondary.nni.FtpVlanSpecBuilder(ftpVlanSpec).build());
                }
                builder.setSecondaryNni(secondaryNniBuilder.build());
            }
        }
        if(input.getSecondaryNni2() != null){
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Nni secondaryNni2 = apiNniToDev(input.getSecondaryNni2());
            if(secondaryNni2 != null){
                SecondaryNni2Builder secondaryNni2Builder = new SecondaryNni2Builder(secondaryNni2);
                VlanSpec ftpVlanSpec = apiVlanSpecToDev(input.getSecondaryNni2().getFtpVlanSpec());
                if(ftpVlanSpec != null){
                    secondaryNni2Builder.setFtpVlanSpec(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.create.eth.connection.input.secondary.nni2.FtpVlanSpecBuilder(ftpVlanSpec).build());
                }
                builder.setSecondaryNni2(secondaryNni2Builder.build());
            }
        }
        builder.setServiceMappingMode(apiAdaptationTypeToDev(input.getServiceMappingMode()));
        builder.setUniProtectionType(apiProtectionTypeToDev(input.getUniProtectionType()));
        return builder.build();
    }


    public CreateEthConnectionNeOutput devCreateEthConnectionOutputToApi(CreateEthConnectionOutput createEthConnectionOutput){
        if(createEthConnectionOutput == null){
            return null;
        }
        CreateEthConnectionNeOutputBuilder createEthConnectionNeOutputBuilder = new CreateEthConnectionNeOutputBuilder();
        Connection connection = devConnectionToApi(createEthConnectionOutput.getConnection());
        if(connection != null){
            createEthConnectionNeOutputBuilder.setConnection(new ConnectionBuilder(connection).build());
        }
        if(createEthConnectionOutput.getCreateComponent() != null){
            CreateComponentBuilder createComponentBuilder = new CreateComponentBuilder()
                    .setCtpName(createEthConnectionOutput.getCreateComponent().getCtpName())
                    .setFtpName(createEthConnectionOutput.getCreateComponent().getFtpName());
            createEthConnectionNeOutputBuilder.setCreateComponent(createComponentBuilder.build());
        }
        return createEthConnectionNeOutputBuilder.build();
    }

}
