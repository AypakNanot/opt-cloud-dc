/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform.service;

import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEoOsuConnectionNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEos2eoosuConnectionNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateOsuConnectionNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateOsuConnectionNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateOsuConnectionNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.Connection;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.VlanSpec;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.input.primary.nni.FtpClientVlanSpecBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.input.secondary.nni.FtpVlanSpecBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.input.uni.ClientVlanSpecBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.output.err.or.ok.oper.ok.CreateComponent;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.output.err.or.ok.oper.ok.PortsWithRole;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Nni;

/**
 * Osu Connection 数据转换
 * 2021/11/15 14:35
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class ConnectionOsuTransformImpl extends ConnectionTransformImpl{

    /**
     * Create Eo Osu Connection api to dev
     * @param input api
     * @return dev
     */
    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.CreateEoOsuConnectionInput
            apiCreateEoOsuConnectionToDev(CreateEoOsuConnectionNeInput input){
        if(input==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.CreateEoOsuConnectionInputBuilder createEoOsuConnectionInputBuilder =
                new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.CreateEoOsuConnectionInputBuilder(apiCreateConnectionToDev(input));
        createEoOsuConnectionInputBuilder.setNniProtectionType(apiProtectionTypeToDev(input.getNniProtectionType()));
        Nni primaryNni = apiNniToDev(input.getPrimaryNni());
        if(primaryNni!=null){
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.input.PrimaryNniBuilder primaryNniBuilder
                    = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.input.PrimaryNniBuilder(primaryNni);
            VlanSpec ftpClientVlanSpec = apiVlanSpecToDev(input.getPrimaryNni().getFtpClientVlanSpec());
            if(ftpClientVlanSpec != null){
                primaryNniBuilder.setFtpClientVlanSpec(new FtpClientVlanSpecBuilder(ftpClientVlanSpec).build());
            }
            VlanSpec ftpVlanSpec = apiVlanSpecToDev(input.getPrimaryNni().getFtpVlanSpec());
            if(ftpVlanSpec != null){
                primaryNniBuilder.setFtpVlanSpec(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.input.primary.nni.FtpVlanSpecBuilder(ftpVlanSpec).build());
            }
            primaryNniBuilder.setTpn(input.getPrimaryNni().getTpn());
            primaryNniBuilder.setTpid(apiTpidDifinitionToDev(input.getPrimaryNni().getTpid()));
            createEoOsuConnectionInputBuilder.setPrimaryNni(primaryNniBuilder.build());
        }
        Nni secondaryNni = apiNniToDev(input.getSecondaryNni());
        if(secondaryNni!=null){
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.input.SecondaryNniBuilder secondaryNniBuilder
                    = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.input.SecondaryNniBuilder(secondaryNni);
            VlanSpec ftpClientVlanSpec = apiVlanSpecToDev(input.getSecondaryNni().getFtpClientVlanSpec());
            if(ftpClientVlanSpec != null){
                secondaryNniBuilder.setFtpClientVlanSpec(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702
                        .create.eo.osu.connection.input.secondary.nni.FtpClientVlanSpecBuilder(ftpClientVlanSpec).build());
            }
            VlanSpec ftpVlanSpec = apiVlanSpecToDev(input.getSecondaryNni().getFtpVlanSpec());
            if(ftpVlanSpec != null){
                secondaryNniBuilder.setFtpVlanSpec(new FtpVlanSpecBuilder(ftpVlanSpec).build());
            }
            secondaryNniBuilder.setTpn(input.getSecondaryNni().getTpn());
            secondaryNniBuilder.setTpid(apiTpidDifinitionToDev(input.getSecondaryNni().getTpid()));
            createEoOsuConnectionInputBuilder.setSecondaryNni(secondaryNniBuilder.build());
        }
        createEoOsuConnectionInputBuilder.setUni(apiEoOsuUniToDev(input.getUni()));
        createEoOsuConnectionInputBuilder.setUniProtectionType(apiProtectionTypeToDev(input.getUniProtectionType()));
        return createEoOsuConnectionInputBuilder.build();
    }

    /**
     * EoOsuUni api to dev
     * @param uni api
     * @return dev
     */
    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.input.Uni apiEoOsuUniToDev(
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.eo.osu.connection.in.grouping.Uni uni){
        if(uni==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.input.UniBuilder uniBuilder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.input.UniBuilder();
        VlanSpec clientVlanSpec = apiVlanSpecToDev(uni.getClientVlanSpec());
        if(clientVlanSpec != null){
            uniBuilder.setClientVlanSpec(new ClientVlanSpecBuilder(clientVlanSpec).build());
        }
        uniBuilder.setSecondUniPtpName(uni.getSecondUniPtpName());
        uniBuilder.setUniPtpName(uni.getUniPtpName());
        VlanSpec uniVlanSpec = apiVlanSpecToDev(uni.getUniVlanSpec());
        if(uniVlanSpec!=null){
            uniBuilder.setUniVlanSpec(
                    new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu
                            .rev240702.create.eo.osu.connection.input.uni.UniVlanSpecBuilder(uniVlanSpec).build());
        }
        return uniBuilder.build();
    }

    /**
     * Create Eo Osu Connection dev to api
     * @param createEoOsuConnectionOutput dev
     * @return api
     */
    public org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEoOsuConnectionNeOutput
        devCreateEoOsuConnectionToApi(
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.CreateEoOsuConnectionOutput createEoOsuConnectionOutput){
        if(createEoOsuConnectionOutput==null){
            return null;
        }
        if (createEoOsuConnectionOutput.getErrOrOk().implementedInterface().equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.output.err.or.ok.OperOk.class)) {
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.output.err.or.ok.OperOk operOk = (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.output.err.or.ok.OperOk) createEoOsuConnectionOutput.getErrOrOk();
            org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEoOsuConnectionNeOutputBuilder createEoOsuConnectionNeOutputBuilder
                    = new org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEoOsuConnectionNeOutputBuilder();
            Connection connection = devConnectionToApi(operOk.getConnection());
            if(connection!=null){
                createEoOsuConnectionNeOutputBuilder.setConnection(
                        new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu
                                .rev210927.create.eo.osu.connection.out.grouping.ConnectionBuilder(connection).build()
                );
            }
            CreateComponent createComponent = operOk.getCreateComponent();
            if(createComponent!=null){
                org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.eo.osu.connection.out.grouping.CreateComponentBuilder createComponentBuilder =
                        new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.eo.osu.connection.out.grouping.CreateComponentBuilder();
                createComponentBuilder.setOsuConnectionName(createComponent.getOsuConnectionName());
                createComponentBuilder.setFtpName(createComponent.getFtpName());
                createComponentBuilder.setCtpName(createComponent.getCtpName());
                createEoOsuConnectionNeOutputBuilder.setCreateComponent(createComponentBuilder.build());
            }
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eo.osu.connection.output.err.or.ok.oper.ok.PortsWithRole portsWithRole
                    = operOk.getPortsWithRole();
            if(portsWithRole!=null){
                org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.eo.osu.connection.out.grouping.PortsWithRoleBuilder portsWithRoleBuilder =
                        new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.eo.osu.connection.out.grouping.PortsWithRoleBuilder();
                portsWithRoleBuilder.setEthFtp(portsWithRole.getEthFtp());
                portsWithRoleBuilder.setEthFtpCtp(portsWithRole.getEthFtpCtp());
                portsWithRoleBuilder.setEthFtpOsuCtp(portsWithRole.getEthFtpOsuCtp());
                portsWithRoleBuilder.setEthPtpCtp(portsWithRole.getEthPtpCtp());
                portsWithRoleBuilder.setOsuFtp(portsWithRole.getOsuFtp());
                portsWithRoleBuilder.setOsuFtpCtp(portsWithRole.getOsuFtpCtp());
                portsWithRoleBuilder.setOsuFtpCtpP(portsWithRole.getOsuFtpCtpP());
                portsWithRoleBuilder.setOsuFtpP(portsWithRole.getOsuFtpP());
                portsWithRoleBuilder.setUlOduCtp(portsWithRole.getUlOduCtp());
                portsWithRoleBuilder.setUlOduCtpP(portsWithRole.getUlOduCtpP());
                createEoOsuConnectionNeOutputBuilder.setPortsWithRole(portsWithRoleBuilder.build());
            }
            return createEoOsuConnectionNeOutputBuilder.build();
        } else {
            // 这种情况不会发生
            return null;
        }
    }

    /**
     * 创建 Osu Connection 输入参数 api to dev
     * @param input api 输入参数
     * @return dev
     */
    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.CreateOsuConnectionInput
            apiCreateOsuConnectionToDev(CreateOsuConnectionNeInput input){
        if(input==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.CreateOsuConnectionInputBuilder createOsuConnectionInputBuilder =
                new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.CreateOsuConnectionInputBuilder(apiCreateConnectionToDev(input));
        createOsuConnectionInputBuilder.setNni2ProtectionType(apiProtectionTypeToDev(input.getNni2ProtectionType()));
        createOsuConnectionInputBuilder.setNniProtectionType(apiProtectionTypeToDev(input.getNniProtectionType()));
        Nni primaryNni = apiNniToDev(input.getPrimaryNni());
        if(primaryNni!=null){
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.input.PrimaryNniBuilder primaryNniBuilder
                    = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.input.PrimaryNniBuilder(primaryNni);
            primaryNniBuilder.setTpn(input.getPrimaryNni().getTpn());
            createOsuConnectionInputBuilder.setPrimaryNni(primaryNniBuilder.build());
        }
        Nni primaryNni2 = apiNniToDev(input.getPrimaryNni2());
        if(primaryNni2!=null){
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.input.PrimaryNni2Builder primaryNni2Builder
                    = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.input.PrimaryNni2Builder(primaryNni2);
            primaryNni2Builder.setTpn(input.getPrimaryNni2().getTpn());
            createOsuConnectionInputBuilder.setPrimaryNni2(primaryNni2Builder.build());
        }
        Nni secondaryNni = apiNniToDev(input.getSecondaryNni());
        if(secondaryNni!=null){
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.input.SecondaryNniBuilder secondaryNniBuilder
                    = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.input.SecondaryNniBuilder(secondaryNni);
            secondaryNniBuilder.setTpn(input.getSecondaryNni().getTpn());
            createOsuConnectionInputBuilder.setSecondaryNni(secondaryNniBuilder.build());
        }
        Nni secondaryNni2 = apiNniToDev(input.getSecondaryNni2());
        if(secondaryNni2!=null){
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.input.SecondaryNni2Builder secondaryNni2Builder
                    = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.input.SecondaryNni2Builder(secondaryNni2);
            secondaryNni2Builder.setTpn(input.getSecondaryNni2().getTpn());
            createOsuConnectionInputBuilder.setSecondaryNni2(secondaryNni2Builder.build());
        }
        createOsuConnectionInputBuilder.setUni(apiOsuUniToDev(input.getUni()));
        createOsuConnectionInputBuilder.setUniProtectionType(apiProtectionTypeToDev(input.getUniProtectionType()));
        return createOsuConnectionInputBuilder.build();
    }

    /**
     * ous connection 数据转换
     * @param uni api
     * @return dev
     */
    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.input.Uni apiOsuUniToDev(
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.osu.connection.in.grouping.Uni uni){
        if(uni==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.input.UniBuilder uniBuilder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.input.UniBuilder();
        uniBuilder.setClientSignalType(apiClientSignalTypeToDev(uni.getClientSignalType()));
        uniBuilder.setSecondUniPtpName(uni.getSecondUniPtpName());
        uniBuilder.setUniPtpName(uni.getUniPtpName());
        return uniBuilder.build();
    }

    /**
     * Create Osu Connection dev to api
     * @param createOsuConnectionOutput dev
     * @return api
     */
    public CreateOsuConnectionNeOutput devCreateOsuConnectionToApi(
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu
                            .rev240702.CreateOsuConnectionOutput createOsuConnectionOutput){
        if(createOsuConnectionOutput==null){
            return null;
        }
        if (createOsuConnectionOutput.getErrOrOk().implementedInterface().equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.output.err.or.ok.OperOk.class)) {
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.output.err.or.ok.OperOk operOk = (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.output.err.or.ok.OperOk) createOsuConnectionOutput.getErrOrOk();
            CreateOsuConnectionNeOutputBuilder createOsuConnectionNeOutputBuilder = new CreateOsuConnectionNeOutputBuilder();
            Connection connection = devConnectionToApi(operOk.getConnection());
            if(connection!=null){
                createOsuConnectionNeOutputBuilder.setConnection(
                        new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu
                                .rev210927.create.osu.connection.out.grouping.ConnectionBuilder(connection).build());
            }
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.osu.connection.output.err.or.ok.oper.ok.CreateComponent createComponent
                    = operOk.getCreateComponent();
            if(createComponent!=null){
                org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.osu.connection.out.grouping.CreateComponentBuilder createComponentBuilder =
                        new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.osu.connection.out.grouping.CreateComponentBuilder();
                createComponentBuilder.setCtpName(createComponent.getCtpName());
                createComponentBuilder.setFtpName(createComponent.getFtpName());
                createOsuConnectionNeOutputBuilder.setCreateComponent(createComponentBuilder.build());
            }
            PortsWithRole portsWithRole = operOk.getPortsWithRole();
            if(portsWithRole!=null){
                org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.osu.connection.out.grouping.PortsWithRoleBuilder  portsWithRoleBuilder=
                        new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.osu.connection.out.grouping.PortsWithRoleBuilder();
                portsWithRoleBuilder.setDlOduCtp(portsWithRole.getDlOduCtp());
                portsWithRoleBuilder.setDlOduCtpP(portsWithRole.getDlOduCtpP());
                portsWithRoleBuilder.setDlOsuFtp(portsWithRole.getDlOsuFtp());
                portsWithRoleBuilder.setDlOsuFtpP(portsWithRole.getDlOsuFtpP());
                portsWithRoleBuilder.setDlOsuFtpCtp(portsWithRole.getDlOsuFtpCtp());
                portsWithRoleBuilder.setDlOsuFtpCtpP(portsWithRole.getDlOsuFtpCtpP());
                portsWithRoleBuilder.setOsuPtpCtp(portsWithRole.getOsuPtpCtp());
                portsWithRoleBuilder.setUlOduCtp(portsWithRole.getUlOduCtp());
                portsWithRoleBuilder.setUlOduCtpP(portsWithRole.getUlOduCtpP());
                portsWithRoleBuilder.setUlOsuFtp(portsWithRole.getUlOsuFtp());
                portsWithRoleBuilder.setUlOsuFtpCtp(portsWithRole.getUlOsuFtpCtp());
                portsWithRoleBuilder.setUlOsuFtpCtpP(portsWithRole.getUlOsuFtpCtpP());
                portsWithRoleBuilder.setUlOsuFtpP(portsWithRole.getUlOsuFtpP());
                createOsuConnectionNeOutputBuilder.setPortsWithRole(portsWithRoleBuilder.build());
            }
            return createOsuConnectionNeOutputBuilder.build();
        } else {
            // 这种情况不会发生
            return null;
        }
    }

    /**
     * Create Eos 2eo osu Connection api to dev
     * @param input api
     * @return dev
     */
    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu
            .rev240702.CreateEos2eoosuConnectionInput apiCreateEos2eoosuConnectionToDev(
                    CreateEos2eoosuConnectionNeInput input){
        if(input==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.CreateEos2eoosuConnectionInputBuilder createEos2eoosuConnectionInputBuilder =
                new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.CreateEos2eoosuConnectionInputBuilder(apiCreateConnectionToDev(input));
        createEos2eoosuConnectionInputBuilder.setEosPac(devEosPacToApi(input.getEosPac()));
        createEos2eoosuConnectionInputBuilder.setNniProtectionType(apiProtectionTypeToDev(input.getNniProtectionType()));
        Nni primaryNni = apiNniToDev(input.getPrimaryNni());
        if(primaryNni!=null){
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.input.PrimaryNniBuilder primaryNniBuilder =
                    new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.input.PrimaryNniBuilder(primaryNni);
            primaryNniBuilder.setTpn(input.getPrimaryNni().getTpn());
            createEos2eoosuConnectionInputBuilder.setPrimaryNni(primaryNniBuilder.build());
        }
        Nni secondaryNni = apiNniToDev(input.getSecondaryNni());
        if(secondaryNni!=null){
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.input.SecondaryNniBuilder secondaryNniBuilder =
                    new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.input.SecondaryNniBuilder(secondaryNni);
            secondaryNniBuilder.setTpn(input.getSecondaryNni().getTpn());
            createEos2eoosuConnectionInputBuilder.setSecondaryNni(secondaryNniBuilder.build());
        }
        createEos2eoosuConnectionInputBuilder.setUni(apiEos2eoOsuToDev(input.getUni()));
        createEos2eoosuConnectionInputBuilder.setUniProtectionType(apiProtectionTypeToDev(input.getUniProtectionType()));
        return createEos2eoosuConnectionInputBuilder.build();
    }

    /**
     * Eos 2eo Osu api to dev
     * @param uni api
     * @return dev
     */
    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.input.Uni apiEos2eoOsuToDev(
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.eos2eoosu.connection.in.grouping.Uni uni){
        if(uni==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.input.UniBuilder uniBuilder =
                new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.input.UniBuilder();
        uniBuilder.setMappingPath(uni.getMappingPath());
        uniBuilder.setSecondUniPtpName(uni.getSecondUniPtpName());
        uniBuilder.setUniPtpName(uni.getUniPtpName());
        uniBuilder.setVcType(apiSdhSwitchTypeToDev(uni.getVcType()));
        return uniBuilder.build();
    }

    /**
     * EosPac dev to api
     * @param eosPac dev
     * @return api
     */
    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.input.EosPac devEosPacToApi(
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.eos2eoosu.connection.in.grouping.EosPac eosPac){
        if(eosPac==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.input.EosPacBuilder eosPacBuilder =
                new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.input.EosPacBuilder();
        eosPacBuilder.setHoldOff(eosPac.getHoldOff());
        eosPacBuilder.setLcas(eosPac.getLcas());
        eosPacBuilder.setTsd(eosPac.getTsd());
        eosPacBuilder.setWtr(eosPac.getWtr());
        return eosPacBuilder.build();
    }

    /**
     * Create Eos 2eo osu Connection dev to api
     * @param output dev
     * @return api
     */
    public org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection
            .rev200427.CreateEos2eoosuConnectionNeOutput devCreateEos2eoosuConnectionToApi(
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.CreateEos2eoosuConnectionOutput output){
        if(output==null){
            return null;
        }
        if (output.getErrOrOk().implementedInterface().equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.output.err.or.ok.OperOk.class)) {
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.output.err.or.ok.OperOk operOk = (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.output.err.or.ok.OperOk) output.getErrOrOk();
            org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEos2eoosuConnectionNeOutputBuilder createEos2eoosuConnectionNeOutputBuilder =
                    new org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEos2eoosuConnectionNeOutputBuilder();
            Connection connection = devConnectionToApi(operOk.getConnection());
            if(connection!=null){
                createEos2eoosuConnectionNeOutputBuilder.setConnection(
                        new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu
                                .rev210927.create.eos2eoosu.connection.out.grouping.ConnectionBuilder(connection).build());
            }
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.output.err.or.ok.oper.ok.CreateComponent createComponent
                    = operOk.getCreateComponent();
            if(createComponent!=null){
                org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.eos2eoosu.connection.out.grouping.CreateComponentBuilder createComponentBuilder =
                        new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.eos2eoosu.connection.out.grouping.CreateComponentBuilder();
                createComponentBuilder.setCtpName(createComponent.getCtpName());
                createComponentBuilder.setFtpName(createComponent.getFtpName());
                createComponentBuilder.setOsuConnectionName(createComponent.getOsuConnectionName());
                createEos2eoosuConnectionNeOutputBuilder.setCreateComponent(createComponentBuilder.build());
            }
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.create.eos2eoosu.connection.output.err.or.ok.oper.ok.PortsWithRole portsWithRole
                    = operOk.getPortsWithRole();
            if(portsWithRole!=null){
                org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.eos2eoosu.connection.out.grouping.PortsWithRoleBuilder portsWithRoleBuilder =
                        new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.create.eos2eoosu.connection.out.grouping.PortsWithRoleBuilder();
                portsWithRoleBuilder.setDlEthFtp(portsWithRole.getDlEthFtp());
                portsWithRoleBuilder.setDlEthFtpCtp(portsWithRole.getDlEthFtpCtp());
                portsWithRoleBuilder.setOsuFtp(portsWithRole.getOsuFtp());
                portsWithRoleBuilder.setOsuFtpCtp(portsWithRole.getOsuFtpCtp());
                portsWithRoleBuilder.setOsuFtpCtpP(portsWithRole.getOsuFtpCtpP());
                portsWithRoleBuilder.setOsuFtpP(portsWithRole.getOsuFtpP());
                portsWithRoleBuilder.setUlEthFtp(portsWithRole.getUlEthFtp());
                portsWithRoleBuilder.setUlEthFtpCtp(portsWithRole.getUlEthFtpCtp());
                portsWithRoleBuilder.setUlEthFtpOsuCtp(portsWithRole.getUlEthFtpOsuCtp());
                portsWithRoleBuilder.setUlOduCtp(portsWithRole.getUlOduCtp());
                portsWithRoleBuilder.setUlOduCtpP(portsWithRole.getUlOduCtpP());
                portsWithRoleBuilder.setVcCtp(portsWithRole.getVcCtp());
                createEos2eoosuConnectionNeOutputBuilder.setPortsWithRole(portsWithRoleBuilder.build());
            }
            return createEos2eoosuConnectionNeOutputBuilder.build();
        } else {
            // 这种情况不会发生
            return null;
        }
    }



}
