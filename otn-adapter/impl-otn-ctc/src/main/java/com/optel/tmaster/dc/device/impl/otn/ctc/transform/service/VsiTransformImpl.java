/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform.service;

import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.*;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.AddVcgPathNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.AddVcgPathNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.AddVcgPathNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.BasicParamGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.NniGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.UniGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.VsiGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.add.vsi.nni.member.out.grouping.CreateComponent;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.add.vsi.nni.member.out.grouping.CreateComponentBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.get.vsi.member.attr.out.grouping.VsiMemberAttrKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.vsi.grouping.StatePac;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.vsis.grouping.Vsi;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.vsis.grouping.VsiKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.get.vsi.member.attr.out.grouping.VsiMemberAttr;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.uni.grouping.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.vsis.grouping.VsiBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.Capacity;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.VlanSpec;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.AdminState;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ServiceType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.nni.grouping.*;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.EthMappingType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType;
import org.opendaylight.yangtools.yang.common.Uint16;
import org.opendaylight.yangtools.yang.common.Uint32;

import java.util.*;

/**
 * VsiTransformImpl
 * Vsi
 * date       time        author
 * ─────────────────────────────
 * 2021/10/14   16:47      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class VsiTransformImpl  extends ConnectionEosTransformImpl  {

    /**
     * Vsi数据 dev转api
     * @param vsis vsi数据 dev
     * @return 查询vsi数据结果 api
     */
    public QueryVsiOutput devQueryVsiToApi(Vsis vsis){
        if(vsis == null){
            return null;
        }
        QueryVsiOutputBuilder queryVsiOutputBuilder = new QueryVsiOutputBuilder();
        if(vsis.getVsi() != null){
            List<Vsi> apiVsiList = new ArrayList<>();
            for(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.vsis.grouping.Vsi vsi:vsis.getVsi().values()){
                org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.vsis.grouping.VsiBuilder vsiBuilder
                        = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.vsis.grouping.VsiBuilder(devVsiToApi(vsi));
                vsiBuilder.withKey(new VsiKey(vsi.key().getName()));
                apiVsiList.add(vsiBuilder.build());
            }
            queryVsiOutputBuilder.setVsi(ltm(apiVsiList));
        }
        return queryVsiOutputBuilder.build();
    }


    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.vsis.grouping.Vsi apiModifyVsiInputToDev(ModifyVsiInput input){
        VsiBuilder vsiBuilder = new VsiBuilder();
        vsiBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.vsis.grouping.VsiKey(input.getName()));
        vsiBuilder.setName(input.getName()).setMtu(input.getMtu());
        vsiBuilder.setBroadcastProc(apiUnknownFrameProcessToDev(input.getBroadcastProc()))
                .setUnknownMulticastProc(apiUnknownFrameProcessToDev(input.getUnknownMulticastProc()))
                .setUnknownUnicastProc(apiUnknownFrameProcessToDev(input.getUnknownUnicastProc()))
                .setMacLearningMode(apiLearningModeToDev(input.getMacLearningMode()))
                .setMacAutoLearningCapacity(apiAdminStateToDev(input.getMacAutoLearningCapacity()));
        return vsiBuilder.build();
    }

/**
     * VSI 添加Vsi成员端口输入参数 api转dev
     * @param input 添加vsi成员端口输入参数 api
     * @return 添加vsi成员端口输入参数 dev
     */
    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.AddVsiNniMemberInput
    apiAddVsiNniMemberToDev(AddVsiNniMemberNeInput input){
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.NniGrouping nniGrouping = apiNniGroupingToDev(input);
        if(nniGrouping == null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.AddVsiNniMemberInputBuilder builder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.AddVsiNniMemberInputBuilder(nniGrouping);
        builder.setName(input.getName());
        return builder.build();
    }

    public AddVsiNniMemberNeOutput
    devAddVsiNniMemberOutputToDev(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.AddVsiNniMemberOutput output){
        if(output == null){
            return null;
        }
        AddVsiNniMemberNeOutputBuilder builder = new AddVsiNniMemberNeOutputBuilder();
        builder.setCreateComponent(devCreateComponentToApi(output.getCreateComponent()));
        builder.setNniCtp(output.getNniCtp());
        builder.setPgId(output.getPgId());
        return builder.build();
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.DeleteVsiMemberInput apiDeleteVsiMemberInput(DeleteVsiMemberNeInput input){
        if(input == null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.DeleteVsiMemberInputBuilder builder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.DeleteVsiMemberInputBuilder();
        builder.setMemberName(input.getMemberName())
                .setVsiName(input.getVsiName())
                .setInterfaceType(apiInterfaceTypeToDev(input.getInterfaceType()));
        return builder.build();
    }

    /**
     * 添加EOS业务虚级联路径 输入参数 api转dev
     * @param input 添加EOS业务虚级联路径 输入参数 api
     * @return 添加EOS业务虚级联路径 输入参数 dev
     */
    public AddVcgPathInput apiAddVcgPathInputToDev(AddVcgPathNeInput input){
        if(input == null){
            return null;
        }
        AddVcgPathInputBuilder builder = new AddVcgPathInputBuilder(apiNniToDev(input));
        builder.setEthFtpName(input.getEthFtpName());
        builder.setSdhSignalType(apiClientSignalTypeToDev(input.getSdhSignalType()));
        builder.setVcType(apiSdhSwitchTypeToDev(input.getVcType()));
        builder.setMappingPath(input.getMappingPath());
        return builder.build();
    }

    /**
     *  添加EOS业务虚级联路径 输出参数 dev转api
     * @param output  添加EOS业务虚级联路径 输出参数 dev
     * @return  添加EOS业务虚级联路径 输出参数 api
     */
    public AddVcgPathNeOutput apiAddVcgPathOutputToDev(AddVcgPathOutput output){
        if(output == null){
            return null;
        }
        AddVcgPathNeOutputBuilder builder = new AddVcgPathNeOutputBuilder();
        builder.setCtpName(output.getCtpName());
        builder.setFtpName(output.getFtpName());
        builder.setVcConnectionName(output.getVcConnectionName());
        return builder.build();
    }

    protected org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.NniGrouping apiNniGroupingToDev(NniGrouping nniGrouping){
        if(nniGrouping == null){
            return null;
        }
        return new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.NniGrouping() {

            @Override
            public ProtectionType getNniProtectionType() {
                return apiProtectionTypeToDev(nniGrouping.getNniProtectionType());
            }

            @Override
            public Uint16 getNniTcm() {
                return nniGrouping.getNniTcm();
            }

            @Override
            public PrimaryNni getPrimaryNni() {
                return apiPrimaryNniToDev(nniGrouping.getPrimaryNni());
            }

            @Override
            public SecondaryNni getSecondaryNni() {
                return apiSecondaryNniToDev(nniGrouping.getSecondaryNni());
            }

            @Override
            public EosPac getEosPac() {
                return apiEosPacToDev(nniGrouping.getEosPac());
            }

            @Override
            public MultiNniPac getMultiNniPac() {
                return null;
            }

            @Override
            public MultiSecondaryNni getMultiSecondaryNni() {
                return null;
            }

            @Override
            public LcasAttr getLcasAttr() {
                return apiLcasAttrToDev(nniGrouping.getLcasAttr());
            }

            @Override
            public MultiSvlanAttr getMultiSvlanAttr() {
                return apiMultiSvlanAttrToDev(nniGrouping.getMultiSvlanAttr());
            }

            @Override
            public Class<? extends org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.NniGrouping> implementedInterface() {
                return null;
            }

            @Override
            public EthMappingType getMappingType() {
                return apiEthMappingTypeToDev(nniGrouping.getMappingType());
            }

            @Override
            public Uint16 getPartitionId() {
                return nniGrouping.getPartitionId();
            }
        };
    }

    protected org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.UniGrouping apiUniGroupingToDev(UniGrouping uniGrouping){
        if(uniGrouping == null){
            return null;
        }
        return new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.UniGrouping() {
            @Override
            public Class<? extends org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.UniGrouping> implementedInterface() {
                return null;
            }

            @Override
            public String getUniPtpName() {
                return uniGrouping.getUniPtpName();
            }

            @Override
            public IngressCapacity getIngressCapacity() {
                Capacity capacity = apiCapacityToDev(uniGrouping.getIngressCapacity());
                if(capacity != null){
                    return new IngressCapacityBuilder(capacity).build();
                }
                return null;
            }

            @Override
            public EgressCapacity getEgressCapacity() {
                Capacity capacity = apiCapacityToDev(uniGrouping.getEgressCapacity());
                if(capacity != null){
                    return new EgressCapacityBuilder(capacity).build();
                }
                return null;
            }


            @Override
            public ClientVlanSpec getClientVlanSpec() {
                VlanSpec clientVlanSpec = apiVlanSpecToDev(uniGrouping.getClientVlanSpec());
                if(clientVlanSpec == null){
                    return null;
                }
                return new ClientVlanSpecBuilder(clientVlanSpec).build();
            }

            @Override
            public UniVlanSpec getUniVlanSpec() {
                VlanSpec uniVlanSpec = apiVlanSpecToDev(uniGrouping.getUniVlanSpec());
                if(uniVlanSpec == null){
                    return null;
                }
                return new UniVlanSpecBuilder(uniVlanSpec).build();
            }

            @Override
            public Uint16 getPartitionId() {
                return null;
            }

            @Override
            public Uint32 getBroadcastSuppress() {
                return uniGrouping.getBroadcastSuppress();
            }

            @Override
            public Uint32 getUnknownUnicastSuppress() {
                return uniGrouping.getUnknownUnicastSuppress();
            }

            @Override
            public Uint32 getUnknownMulticastSuppress() {
                return uniGrouping.getUnknownMulticastSuppress();
            }
        };
    }

    protected org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.BasicParamGrouping apiBasicParamGroupingToDev(BasicParamGrouping basicParamGrouping){
        if(basicParamGrouping == null){
            return null;
        }
        return new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.BasicParamGrouping() {
            @Override
            public Class<? extends org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.BasicParamGrouping> implementedInterface() {
                return null;
            }

            @Override
            public String getLabel() {
                return basicParamGrouping.getLabel();
            }

            @Override
            public ServiceType getServiceType() {
                return apiServiceTypeToDev(basicParamGrouping.getServiceType());
            }

            @Override
            public Uint16 getMtu() {
                return basicParamGrouping.getMtu();
            }

            @Override
            public AdminState getMacAutoLearningCapacity() {
                return apiAdminStateToDev(basicParamGrouping.getMacAutoLearningCapacity());
            }

            @Override
            public LearningMode getMacLearningMode() {
                return apiLearningModeToDev(basicParamGrouping.getMacLearningMode());
            }

            @Override
            public UnknownFrameProcess getUnknownUnicastProc() {
                return apiUnknownFrameProcessToDev(basicParamGrouping.getUnknownUnicastProc());
            }

            @Override
            public UnknownFrameProcess getUnknownMulticastProc() {
                return apiUnknownFrameProcessToDev(basicParamGrouping.getUnknownMulticastProc());
            }

            @Override
            public UnknownFrameProcess getBroadcastProc() {
                return apiUnknownFrameProcessToDev(basicParamGrouping.getBroadcastProc());
            }
        };
    }

    public VsiGrouping devVsiToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.VsiGrouping vsi){
        if(vsi == null){
            return null;
        }
        return new VsiGrouping() {
            @Override
            public Class<? extends VsiGrouping> implementedInterface() {
                return null;
            }

            @Override
            public String getName() {
                return vsi.getName();
            }

            @Override
            public String getLabel() {
                return vsi.getLabel();
            }

            @Override
            public Uint16 getMtu() {
                return vsi.getMtu();
            }


            @Override
            public org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ServiceType getServiceType() {
                return devServiceTypeToApi(vsi.getServiceType());
            }

            @Override
            public Set<String> getUniCtp() {
                return vsi.getUniCtp();
            }

            @Override
            public Set<String> getNniCtp() {
                return vsi.getNniCtp();
            }

            @Override
            public @org.eclipse.jdt.annotation.Nullable Set<Uint16> getPgId() {
                return vsi.getPgId();
            }

            @Override
            public StatePac getStatePac() {
                return null;
            }

            @Override
            public org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.AdminState getMacAutoLearningCapacity() {
                return devAdminStateToApi(vsi.getMacAutoLearningCapacity());
            }

            @Override
            public org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.LearningMode getMacLearningMode() {
                return devLearningModeToApi(vsi.getMacLearningMode());
            }

            @Override
            public org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.UnknownFrameProcess getUnknownUnicastProc() {
                return devUnknownFrameProcessToApi(vsi.getUnknownUnicastProc());
            }

            @Override
            public org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.UnknownFrameProcess getUnknownMulticastProc() {
                return devUnknownFrameProcessToApi(vsi.getUnknownMulticastProc());
            }

            @Override
            public org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.UnknownFrameProcess getBroadcastProc() {
                return devUnknownFrameProcessToApi(vsi.getBroadcastProc());
            }
        };
    }

    public CreateComponent devCreateComponentToApi (
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.add.vsi.nni.member.out.grouping.CreateComponent createComponent){
        if(createComponent==null){
            return null;
        }
        CreateComponentBuilder builder = new CreateComponentBuilder();
        builder.setCtpName(createComponent.getCtpName());
        builder.setFtpName(createComponent.getFtpName());
        builder.setVcConnectionName(createComponent.getVcConnectionName());
        return builder.build();
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.AddVsiUniMemberInput
    apiAddVsiUniMemberToDev(AddVsiUniMemberNeInput input){
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.UniGrouping uniGrouping
                = apiUniGroupingToDev(input);
        if(uniGrouping==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.AddVsiUniMemberInputBuilder builder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.AddVsiUniMemberInputBuilder(uniGrouping);
        builder.setName(input.getName());
        builder.setPartitionId(input.getPartitionId());
        return builder.build();
    }

    public AddVsiUniMemberNeOutput
    devAddVsiUniMemberToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.AddVsiUniMemberOutput output){
        if(output==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.AddVsiUniMemberNeOutputBuilder builder
                = new org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.AddVsiUniMemberNeOutputBuilder();
        builder.setUniCtpName(output.getUniCtpName());
        builder.setNniCtpName(output.getNniCtpName());
        builder.setPgId(output.getPgId());
        return builder.build();
    }

    public org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.GetVsiMemberAttrNeOutput
    devGetVsiMemberAttrToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetVsiMemberAttrOutput output){
        if(output==null){
            return null;
        }
        GetVsiMemberAttrNeOutputBuilder builder = new GetVsiMemberAttrNeOutputBuilder();
        builder.setVsiMemberAttr(ltm(devVsiMemberAttrToApiList(output.getVsiMemberAttr())));
        return builder.build();
    }

    public List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.get.vsi.member.attr.out.grouping.VsiMemberAttr>
    devVsiMemberAttrToApiList(Map<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.get.vsi.member.attr.out.grouping.VsiMemberAttrKey, VsiMemberAttr> list){
        if(list==null || list.isEmpty()){
            return null;
        }
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.get.vsi.member.attr.out.grouping.VsiMemberAttr> resultList = new LinkedList<>();
        for(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.get.vsi.member.attr.out.grouping.VsiMemberAttr e : list.values()){
            resultList.add(devVsiMemberAttrToApi(e));
        }
        return resultList;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.get.vsi.member.attr.out.grouping.VsiMemberAttr
    devVsiMemberAttrToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.get.vsi.member.attr.out.grouping.VsiMemberAttr e){
        if(e==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.get.vsi.member.attr.out.grouping.VsiMemberAttrBuilder builder =
                new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.get.vsi.member.attr.out.grouping.VsiMemberAttrBuilder();
        builder.withKey(new VsiMemberAttrKey(e.getCtpName()));
        builder.setCtpName(e.getCtpName());
        org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.Capacity egressCapacity
                = devCapacityToApi(e.getEgressCapacity());
        if(egressCapacity!=null){
            builder.setEgressCapacity(
                    new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension
                            .rev210927.vsi.member.attr.grouping.EgressCapacityBuilder(egressCapacity).build());
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.Capacity ingressCapacity
                = devCapacityToApi(e.getIngressCapacity());
        if(ingressCapacity!=null){
            builder.setIngressCapacity(
                    new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927
                            .vsi.member.attr.grouping.IngressCapacityBuilder(ingressCapacity).build());
        }
        builder.setPartitionId(e.getPartitionId());
        builder.setBroadcastSuppress(e.getBroadcastSuppress());
        builder.setUnknownMulticastSuppress(e.getUnknownMulticastSuppress());
        builder.setUnknownUnicastSuppress(e.getUnknownUnicastSuppress());
        return builder.build();
    }

}
