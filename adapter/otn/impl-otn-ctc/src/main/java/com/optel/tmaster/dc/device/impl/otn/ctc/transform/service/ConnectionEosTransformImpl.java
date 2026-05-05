/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform.service;

import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEosConnectionNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEosConnectionNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEosConnectionNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.Connection;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eos.rev210927.create.eos.connection.in.grouping.EthUni;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eos.rev210927.create.eos.connection.out.grouping.ConnectionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eos.rev210927.create.eos.connection.out.grouping.CreateComponentBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eos.rev210927.nni.grouping.EosPac;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eos.rev210927.nni.grouping.LcasAttr;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eos.rev210927.nni.grouping.MultiSvlanAttr;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eos.rev210927.nni.grouping.PrimaryNni;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eos.rev210927.nni.grouping.SecondaryNni;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.VlanSpec;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.CreateEosConnectionInput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.CreateEosConnectionInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.CreateEosConnectionOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.create.eos.connection.input.EthUniBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.create.eos.connection.input.eth.uni.ClientVlanSpecBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.create.eos.connection.input.eth.uni.UniVlanSpecBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.create.eos.connection.output.err.or.ok.CreateOk;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.EosPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.LcasAttrBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.MultiSvlanAttrBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.PrimaryNniBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.SecondaryNniBuilder;

/**
 * ConnectionEosTransformImpl
 * EOS业务
 * date       time        author
 * ─────────────────────────────
 * 2021/10/12   14:51      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class ConnectionEosTransformImpl extends ConnectionTransformImpl {

    public CreateEosConnectionInput apiCreateEosConnectionInputToDev(CreateEosConnectionNeInput input){
        if(input == null){
            return null;
        }
        CreateEosConnectionInputBuilder builder = new CreateEosConnectionInputBuilder(apiCreateConnectionToDev(input));
        builder.setUniProtectionType(apiProtectionTypeToDev(input.getUniProtectionType()));
        builder.setServiceMappingMode(apiAdaptationTypeToDev(input.getServiceMappingMode()));
        builder.setEthUni(apiEthUniToDev(input.getEthUni()));
        builder.setNniProtectionType(apiProtectionTypeToDev(input.getNniProtectionType()));
        builder.setNniTcm(input.getNniTcm());
        builder.setPrimaryNni(apiPrimaryNniToDev(input.getPrimaryNni()));
        builder.setSecondaryNni(apiSecondaryNniToDev(input.getSecondaryNni()));
        builder.setEosPac(apiEosPacToDev(input.getEosPac()));
        // builder.setMultiNniPac();
        // builder.setMultiSecondaryNni();
        builder.setMultiSvlanAttr(apiMultiSvlanAttrToDev(input.getMultiSvlanAttr()));
        builder.setLcasAttr(apiLcasAttrToDev(input.getLcasAttr()));
        return builder.build();
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.SecondaryNni apiSecondaryNniToDev(SecondaryNni secondaryNni){
        if(secondaryNni == null){
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Nni devSecondaryNni = apiNniToDev(secondaryNni);
        if(devSecondaryNni == null){
            return null;
        }
        SecondaryNniBuilder secondaryNniBuilder = new SecondaryNniBuilder(devSecondaryNni);
        VlanSpec ftpClientVlanSpec = apiVlanSpecToDev(secondaryNni.getFtpClientVlanSpec());
        if(ftpClientVlanSpec != null){
            secondaryNniBuilder.setFtpClientVlanSpec(
                    new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.secondary.nni.FtpClientVlanSpecBuilder(ftpClientVlanSpec).build());
        }
        VlanSpec ftpVlanSpec = apiVlanSpecToDev(secondaryNni.getFtpVlanSpec());
        if(ftpVlanSpec != null){
            secondaryNniBuilder.setFtpVlanSpec(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.secondary.nni.FtpVlanSpecBuilder(ftpVlanSpec).build());
        }
        return secondaryNniBuilder.build();
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.EosPac apiEosPacToDev(EosPac eosPac){
        if(eosPac == null){
            return null;
        }
        EosPacBuilder eosPacBuilder = new EosPacBuilder();
        eosPacBuilder.setHoldOff(eosPac.getHoldOff());
        eosPacBuilder.setMappingPath(eosPac.getMappingPath());
        eosPacBuilder.setMappingPathProtect(eosPac.getMappingPathProtect());
        eosPacBuilder.setSdhSignalType(apiClientSignalTypeToDev(eosPac.getSdhSignalType()));
        eosPacBuilder.setSdhSignalTypeProtect(apiClientSignalTypeToDev(eosPac.getSdhSignalTypeProtect()));
        eosPacBuilder.setVcType(apiSdhSwitchTypeToDev(eosPac.getVcType()));
        eosPacBuilder.setWtr(eosPac.getWtr());
        eosPacBuilder.setLcas(eosPac.getLcas());
        eosPacBuilder.setTsd(eosPac.getTsd());
        return eosPacBuilder.build();
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.PrimaryNni apiPrimaryNniToDev(PrimaryNni primaryNni){
        if(primaryNni == null){
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Nni nni = apiNniToDev(primaryNni);
        if( nni != null){
            PrimaryNniBuilder primaryNniBuilder = new PrimaryNniBuilder(nni);
            VlanSpec ftpClientVlanSpec = apiVlanSpecToDev(primaryNni.getFtpClientVlanSpec());
            if (ftpClientVlanSpec != null) {
                primaryNniBuilder.setFtpClientVlanSpec(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.primary.nni.FtpClientVlanSpecBuilder(ftpClientVlanSpec).build());
            }
            VlanSpec ftpVlanSpec = apiVlanSpecToDev(primaryNni.getFtpVlanSpec());
            if(ftpVlanSpec != null){
                primaryNniBuilder.setFtpVlanSpec(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.primary.nni.FtpVlanSpecBuilder(ftpVlanSpec).build());
            }
            primaryNniBuilder.setTpid(apiTpidDifinitionToDev(primaryNni.getTpid()));
            return primaryNniBuilder.build();
        }
        return null;
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.MultiSvlanAttr apiMultiSvlanAttrToDev(MultiSvlanAttr svlan){
        if(svlan == null){
            return null;
        }
        MultiSvlanAttrBuilder ethFtpAttrBuilder = new MultiSvlanAttrBuilder();
        VlanSpec ftpClientVlanSpec = apiVlanSpecToDev(svlan.getFtpClientVlanSpec());
        if (ftpClientVlanSpec != null) {
            ethFtpAttrBuilder.setFtpClientVlanSpec(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.multi.svlan.attr.FtpClientVlanSpecBuilder(ftpClientVlanSpec).build());
        }
        VlanSpec ftpVlanSpec = apiVlanSpecToDev(svlan.getFtpVlanSpec());
        if(ftpVlanSpec != null){
            ethFtpAttrBuilder.setFtpVlanSpec(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.multi.svlan.attr.FtpVlanSpecBuilder(ftpVlanSpec).build());
        }
        ethFtpAttrBuilder.setTpid(apiTpidDifinitionToDev(svlan.getTpid()));
        return ethFtpAttrBuilder.build();
    }

    private org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.create.eos.connection.input.EthUni apiEthUniToDev(EthUni ethUni){
        if(ethUni == null){
            return null;
        }
        EthUniBuilder ethUniBuilder = new EthUniBuilder();
        VlanSpec clientVlanSpec = apiVlanSpecToDev(ethUni.getClientVlanSpec());
        if (clientVlanSpec != null) {
            ethUniBuilder.setClientVlanSpec(new ClientVlanSpecBuilder(clientVlanSpec).build());
        }
        ethUniBuilder.setUniPtpName(ethUni.getUniPtpName());
        ethUniBuilder.setSecondUniPtpName(ethUni.getSecondUniPtpName());
        VlanSpec vlanSpec = apiVlanSpecToDev(ethUni.getUniVlanSpec());
        if(vlanSpec != null){
            ethUniBuilder.setUniVlanSpec(new UniVlanSpecBuilder(vlanSpec).build());
        }
        return ethUniBuilder.build();
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.LcasAttr apiLcasAttrToDev(LcasAttr lcasAttr){
        if(lcasAttr == null){
            return null;
        }
        LcasAttrBuilder lcasAttrBuilder = new LcasAttrBuilder();
        lcasAttrBuilder.setHoldOff(lcasAttr.getHoldOff());
        lcasAttrBuilder.setLcas(lcasAttr.getLcas());
        lcasAttrBuilder.setTsd(lcasAttr.getTsd());
        lcasAttrBuilder.setWtr(lcasAttr.getWtr());
        return lcasAttrBuilder.build();
    }

    /**
     * 创建EOS业务输出参数转换
     * @param output dev返回的输出参数
     * @return api 输出
     */
    public CreateEosConnectionNeOutput devCreateEosConnectionOutputToApi(CreateEosConnectionOutput output){
        if(output == null){
            return null;
        }
        if (output.getErrOrOk().implementedInterface().equals(CreateOk.class)) {
            CreateOk createOk = (CreateOk) output.getErrOrOk();
            CreateEosConnectionNeOutputBuilder createEthConnectionNeOutputBuilder = new CreateEosConnectionNeOutputBuilder();
            Connection connection = devConnectionToApi(createOk.getConnection());
            if(connection != null){
                createEthConnectionNeOutputBuilder.setConnection(new ConnectionBuilder(connection).build());
            }
            if(createOk.getCreateComponent() != null){
                CreateComponentBuilder createComponentBuilder = new CreateComponentBuilder()
                        .setFtpName(createOk.getCreateComponent().getFtpName())
                        .setCtpName(createOk.getCreateComponent().getCtpName())
                        .setVcConnectionName(createOk.getCreateComponent().getVcConnectionName());
                createEthConnectionNeOutputBuilder.setCreateComponent(createComponentBuilder.build());
            }
            return createEthConnectionNeOutputBuilder.build();
        } else {
            // 这种情况不会发生
            return null;
        }
    }

}
