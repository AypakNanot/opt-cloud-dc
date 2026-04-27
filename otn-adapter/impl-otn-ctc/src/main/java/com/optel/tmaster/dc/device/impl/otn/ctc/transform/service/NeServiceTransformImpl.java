/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform.service;

import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.AbstractCtcTransformer;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.ServiceTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.AddServiceMemberNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.AddServiceMemberNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.AddServiceMemberNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.CreateServiceNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.CreateServiceNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.CreateServiceNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.DeleteServiceMemberNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.DeleteServiceMemberNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.DeleteServiceMemberNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.PortParamGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.add.service.member.output.grouping.Results;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.add.service.member.output.grouping.ResultsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.add.service.member.output.grouping.ResultsKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.create.service.output.grouping.Connection;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.create.service.output.grouping.ConnectionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.create.service.output.grouping.ConnectionKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.create.service.output.grouping.CreateComponent;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.create.service.output.grouping.CreateComponentBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.create.service.output.grouping.FailService;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.create.service.output.grouping.FailServiceBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.create.service.output.grouping.FailServiceKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.create.service.output.grouping.Vsi;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.create.service.output.grouping.VsiBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.create.service.output.grouping.VsiKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.delete.service.member.output.grouping.FailData;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.delete.service.member.output.grouping.FailDataBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.delete.service.member.output.grouping.FailDataKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.AddServiceMemberInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.AddServiceMemberInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.AddServiceMemberOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.CreateServiceInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.CreateServiceInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.CreateServiceOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.DeleteServiceMemberInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.DeleteServiceMemberInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.DeleteServiceMemberOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.add.service.member.input.AddMember;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.add.service.member.input.AddMemberBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.add.service.member.input.AddMemberKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.add.service.member.input.add.member.AzParams;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.add.service.member.input.add.member.AzParamsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.create.service.input.ServiceParam;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.create.service.input.ServiceParamBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.create.service.input.ServiceParamKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.create.service.input.service.param.AParamsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.create.service.input.service.param.ZParamsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.delete.service.member.input.DeleteMember;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.delete.service.member.input.DeleteMemberBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.delete.service.member.input.DeleteMemberKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.eth.attr.pac.grouping.SwitchVlanSpecBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.eth.attr.pac.grouping.VlanSpecBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.port.param.grouping.EthAttrPacBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.port.param.grouping.LcasAttrPacBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.port.param.grouping.OduAttrPacBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.port.param.grouping.OsuAttrPacBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.port.param.grouping.SdhAttrPacBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.service.basic.param.grouping.VsiParam;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.service.basic.param.grouping.VsiParamBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.service.param.grouping.PointAttrs;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.service.param.grouping.PointAttrsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.service.param.grouping.PointAttrsKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.service.param.grouping.point.attrs.PrimaryParamBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.service.param.grouping.point.attrs.RequestedCapacityBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.service.param.grouping.point.attrs.SecondaryParamBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.Capacity;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.VlanSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * 网元创建业务 添加/删除成员 转换
 *
 * @author Quan Jingyuan
 * @since 2022/3/14
 **/
public class NeServiceTransformImpl extends AbstractCtcTransformer implements ServiceTransform, CommonTransform {
    public DeleteServiceMemberInput apiToDeleteServiceMemberInputDev(DeleteServiceMemberNeInput input) {
        if (input == null || input.getDeleteMember() == null) {
            return null;
        }
        DeleteServiceMemberInputBuilder builder = new DeleteServiceMemberInputBuilder();
        List<DeleteMember> deleteMembers = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.delete.service.member.input.grouping.DeleteMember member : input.getDeleteMember().values()) {
            DeleteMemberBuilder deleteMemberBuilder = new DeleteMemberBuilder();
            deleteMemberBuilder.setAzType(member.getAzType());
            deleteMemberBuilder.setName(member.getName());
            deleteMemberBuilder.setPortName(member.getPortName());
            deleteMemberBuilder.withKey(new DeleteMemberKey(member.key().getName()));
            deleteMembers.add(deleteMemberBuilder.build());
        }
        builder.setDeleteMember(ltm(deleteMembers));
        return builder.build();
    }

    public DeleteServiceMemberNeOutput devToDeleteServiceMemberNeOutputApi(DeleteServiceMemberOutput output) {
        if (output == null || output.getFailData() == null) {
            return null;
        }
        DeleteServiceMemberNeOutputBuilder builder = new DeleteServiceMemberNeOutputBuilder();
        builder.setFailData(ltm(devToFailDataList(output.getFailData())));
        return builder.build();
    }

    public CreateServiceNeOutput devToCreateServiceNeOutputApi(CreateServiceOutput output) {
        if (output == null) {
            return null;
        }
        CreateServiceNeOutputBuilder builder = new CreateServiceNeOutputBuilder();
        builder.setCreateComponent(devToCreateComponentApi(output.getCreateComponent()));
        builder.setConnection(ltm(devToConnectionList(output.getConnection())));
        builder.setFailService(ltm(devToFailServiceList(output.getFailService())));
        builder.setVsi(ltm(devToVsiDevList(output.getVsi())));
        return builder.build();
    }

    public CreateServiceInput apiToCreateServiceInputDev(CreateServiceNeInput input) {
        if (input == null || input.getServiceParam() == null) {
            return null;
        }
        CreateServiceInputBuilder builder = new CreateServiceInputBuilder();
        List<ServiceParam> serviceParams = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.create.service.input.grouping.ServiceParam serviceParam : input.getServiceParam().values()) {
            ServiceParamBuilder serviceParamBuilder = new ServiceParamBuilder();
            serviceParamBuilder.withKey(new ServiceParamKey(serviceParam.key().getServiceKey()));
            serviceParamBuilder.setLabel(serviceParam.getLabel());
            serviceParamBuilder.setDirection(apiToServiceDirectionDev(serviceParam.getDirection()));
            serviceParamBuilder.setLayerProtocolName(apiLayerNameToDev(serviceParam.getLayerProtocolName()));
            serviceParamBuilder.setServiceKey(serviceParam.getServiceKey());
            serviceParamBuilder.setServiceMappingMode(apiAdaptationTypeToDev(serviceParam.getServiceMappingMode()));
            serviceParamBuilder.setServiceType(apiServiceTypeToDev(serviceParam.getServiceType()));
            serviceParamBuilder.setVsiParam(apiToVsiParamDev(serviceParam.getVsiParam()));
            if (serviceParam.getZParams() != null && serviceParam.getZParams().getPointAttrs() != null) {
                ZParamsBuilder aParamsBuilder = new ZParamsBuilder();
                aParamsBuilder.setPointAttrs(ltm(apiToPointAttrDevList(serviceParam.getZParams().getPointAttrs())));
                serviceParamBuilder.setZParams(aParamsBuilder.build());
            }
            if (serviceParam.getAParams() != null && serviceParam.getAParams().getPointAttrs() != null) {
                AParamsBuilder aParamsBuilder = new AParamsBuilder();
                aParamsBuilder.setPointAttrs(ltm(apiToPointAttrDevList(serviceParam.getAParams().getPointAttrs())));
                serviceParamBuilder.setAParams(aParamsBuilder.build());
            }
            serviceParams.add(serviceParamBuilder.build());
        }
        builder.setServiceParam(ltm(serviceParams));
        return builder.build();
    }

    public AddServiceMemberInput apiToAddServiceMemberInputDev(AddServiceMemberNeInput input) {
        if (input == null) {
            return null;
        }
        AddServiceMemberInputBuilder builder = new AddServiceMemberInputBuilder();
        builder.setAddMember(ltm(apiToAddMemberDev(input.getAddMember())));
        return builder.build();
    }

    public AddServiceMemberNeOutput devToAddServiceMemberNeOutputApi(AddServiceMemberOutput output) {
        if (output == null) {
            return null;
        }
        AddServiceMemberNeOutputBuilder builder = new AddServiceMemberNeOutputBuilder();
        builder.setFailData(ltm(devToOutputFailDataList(output.getFailData())));
        builder.setResults(ltm(devToResultsApi(output.getResults())));
        return builder.build();
    }

    private List<Results> devToResultsApi(Map<org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.add.service.member.output.ResultsKey,
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.add.service.member.output.Results> results) {
        if (results == null) {
            return null;
        }
        List<Results> resultsList = new ArrayList<>();

        for(org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.add.service.member.output.Results result:results.values()){
            ResultsBuilder builder=new ResultsBuilder();
            builder.withKey(new ResultsKey(result.key().getName()));
            builder.setName(result.getName());
            builder.setPgId(result.getPgId());
            builder.setPortName(result.getPortName());
            resultsList.add(builder.build());
        }
        return resultsList;
    }

    private List<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.add.service.member.output.grouping.FailData> devToOutputFailDataList(
            Map<org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.add.service.member.output.FailDataKey,
                    org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.add.service.member.output.FailData> failData) {
        if(failData == null){
            return null;
        }
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.add.service.member.output.grouping.FailData> failDataList = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.add.service.member.output.FailData item : failData.values()) {
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.add.service.member.output.grouping.FailDataBuilder failDataBuilder = new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.add.service.member.output.grouping.FailDataBuilder();
            failDataBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.add.service.member.output.grouping.FailDataKey(item.key().getName()));
            failDataBuilder.setMessage(item.getMessage());
            failDataBuilder.setName(item.getName());
            failDataBuilder.setPointKey(item.getPointKey());
            failDataList.add(failDataBuilder.build());
        }
        return failDataList;
    }

    private List<FailData> devToFailDataList(Map<org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.delete.service.member.output.FailDataKey,
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.delete.service.member.output.FailData>  failData) {
        if(failData==null){
            return null;
        }
        List<FailData> failDataList = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.delete.service.member.output.FailData item : failData.values()) {
            FailDataBuilder failDataBuilder = new FailDataBuilder();
            failDataBuilder.withKey(new FailDataKey(item.key().getName()));
            failDataBuilder.setMessage(item.getMessage());
            failDataBuilder.setName(item.getName());
            failDataBuilder.setPortName(item.getPortName());
            failDataList.add(failDataBuilder.build());
        }
        return failDataList;
    }

    private List<AddMember> apiToAddMemberDev( Map<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.add.service.member.input.grouping.AddMemberKey,
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.add.service.member.input.grouping.AddMember> addMembers) {
        if (addMembers == null) {
            return null;
        }
        List<AddMember> addMemberList = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.add.service.member.input.grouping.AddMember addMember : addMembers.values()) {
            AddMemberBuilder builder = new AddMemberBuilder();
            builder.setName(addMember.getName());
            builder.withKey(new AddMemberKey(addMember.key().getName()));
            builder.setAzParams(apiToAzParamsDev(addMember.getAzParams()));
            addMemberList.add(builder.build());
        }
        return addMemberList;
    }

    private AzParams apiToAzParamsDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.add.service.member.input.grouping.add.member.AzParams azParams) {
        if (azParams == null) {
            return null;
        }
        AzParamsBuilder builder = new AzParamsBuilder();
        builder.setAzType(azParams.getAzType());
        builder.setPointAttrs(ltm(apiToPointAttrDevList(azParams.getPointAttrs())));
        return builder.build();
    }

    private List<Vsi> devToVsiDevList(Map<org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.create.service.output.VsiKey,
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.create.service.output.Vsi> vsis) {
        if (vsis == null) {
            return null;
        }
        List<Vsi> vsiList = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.create.service.output.Vsi vsi : vsis.values()) {
            VsiBuilder builder = new VsiBuilder(new VsiTransformImpl().devVsiToApi(vsi));
            builder.withKey(new VsiKey(vsi.key().getName()));
            vsiList.add(builder.build());
        }
        return vsiList;
    }

    private List<FailService> devToFailServiceList(Map<org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.create.service.output.FailServiceKey,
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.create.service.output.FailService> failServices) {
        if (failServices == null) {
            return null;
        }
        List<FailService> failServiceList = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.create.service.output.FailService service : failServices.values()) {
            FailServiceBuilder builder = new FailServiceBuilder();
            builder.withKey(new FailServiceKey(service.key().getServiceKey()));
            builder.setMessage(service.getMessage());
            builder.setServiceKey(service.getServiceKey());
            failServiceList.add(builder.build());
        }
        return failServiceList;
    }

    private List<Connection> devToConnectionList(Map<org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.create.service.output.ConnectionKey,
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.create.service.output.Connection> connections) {
        if (connections == null) {
            return null;
        }
        List<Connection> connectionList = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.create.service.output.Connection connection : connections.values()) {
            ConnectionBuilder connectionBuilder = new ConnectionBuilder(new ConnectionTransformImpl().devConnectionToApi(connection));
            connectionBuilder.withKey(new ConnectionKey(connection.key().getName()));
            connectionList.add(connectionBuilder.build());
        }
        return connectionList;
    }

    private CreateComponent devToCreateComponentApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.create.service.output.CreateComponent createComponent) {
        if (createComponent == null) {
            return null;
        }
        CreateComponentBuilder builder = new CreateComponentBuilder();
        builder.setConnectionName(createComponent.getConnectionName());
        builder.setPortName(createComponent.getPortName());
        return builder.build();
    }

    private VsiParam apiToVsiParamDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.service.basic.param.grouping.VsiParam vsiParam) {
        if (vsiParam == null) {
            return null;
        }
        VsiParamBuilder builder = new VsiParamBuilder();
        builder.setBroadcastProc(apiUnknownFrameProcessToDev(vsiParam.getBroadcastProc()));
        builder.setUnknownMulticastProc(apiUnknownFrameProcessToDev(vsiParam.getUnknownMulticastProc()));
        builder.setUnknownUnicastProc(apiUnknownFrameProcessToDev(vsiParam.getUnknownUnicastProc()));
        builder.setMacAutoLearningCapacity(apiAdminStateToDev(vsiParam.getMacAutoLearningCapacity()));
        builder.setMacLearningMode(apiLearningModeToDev(vsiParam.getMacLearningMode()));
        builder.setMtu(vsiParam.getMtu());
        return builder.build();
    }

    private List<PointAttrs> apiToPointAttrDevList(Map<org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.service.param.grouping.PointAttrsKey,
            org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.service.param.grouping.PointAttrs> attrsList) {
        if(attrsList==null){
            return null;
        }
        List<PointAttrs> attrs = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.service.param.grouping.PointAttrs attr : attrsList.values()) {
            PointAttrsBuilder attrsBuilder = new PointAttrsBuilder();
            attrsBuilder.withKey(new PointAttrsKey(attr.key().getPointKey()));
            attrsBuilder.setPointKey(attr.getPointKey());
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.PortParamGrouping primary = apiToPortParamGroupingDev(attr.getPrimaryParam());
            if(primary != null){
                attrsBuilder.setPrimaryParam(new PrimaryParamBuilder(primary).build());
            }
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.PortParamGrouping secondary = apiToPortParamGroupingDev(attr.getSecondaryParam());
            if(secondary!=null){
                attrsBuilder.setSecondaryParam(new SecondaryParamBuilder(secondary).build());
            }
            attrsBuilder.setTcm(attr.getTcm());
            attrsBuilder.setProtectionType(apiProtectionTypeToDev(attr.getProtectionType()));
            Capacity requestCapacity = apiCapacityToDev(attr.getRequestedCapacity());
            if (requestCapacity != null) {
                attrsBuilder.setRequestedCapacity(new RequestedCapacityBuilder(requestCapacity).build());
            }
            attrs.add(attrsBuilder.build());
        }
        return attrs;
    }

    private org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.PortParamGrouping apiToPortParamGroupingDev(PortParamGrouping grouping) {
        if(grouping==null){
            return null;
        }
        PrimaryParamBuilder builder = new PrimaryParamBuilder();
        if (grouping.getEthAttrPac() != null) {
            EthAttrPacBuilder ethAttrPacBuilder = new EthAttrPacBuilder();
            ethAttrPacBuilder.setBroadcastSuppress(grouping.getEthAttrPac().getBroadcastSuppress());
            ethAttrPacBuilder.setPartitionId(grouping.getEthAttrPac().getPartitionId());
            VlanSpec switchVlanSpec = apiVlanSpecToDev(grouping.getEthAttrPac().getSwitchVlanSpec());
            if(switchVlanSpec!=null){
                ethAttrPacBuilder.setSwitchVlanSpec(new SwitchVlanSpecBuilder(switchVlanSpec).build());
            }
            ethAttrPacBuilder.setUnknownMulticastSuppress(grouping.getEthAttrPac().getUnknownMulticastSuppress());
            ethAttrPacBuilder.setUnknownUnicastSuppress(grouping.getEthAttrPac().getUnknownUnicastSuppress());
            VlanSpec vlanSpec = apiVlanSpecToDev(grouping.getEthAttrPac().getVlanSpec());
            if(vlanSpec!=null){
                ethAttrPacBuilder.setVlanSpec(new VlanSpecBuilder(vlanSpec).build());
            }
            builder.setEthAttrPac(ethAttrPacBuilder.build());
        }
        if (grouping.getLcasAttrPac() != null) {
            LcasAttrPacBuilder lcasAttrPacBuilder = new LcasAttrPacBuilder();
            lcasAttrPacBuilder.setHoldOff(grouping.getLcasAttrPac().getHoldOff());
            lcasAttrPacBuilder.setLcas(grouping.getLcasAttrPac().getLcas());
            lcasAttrPacBuilder.setTsd(grouping.getLcasAttrPac().getTsd());
            lcasAttrPacBuilder.setWtr(grouping.getLcasAttrPac().getWtr());
            builder.setLcasAttrPac(lcasAttrPacBuilder.build());
        }
        if (grouping.getOduAttrPac() != null) {
            OduAttrPacBuilder oduAttrPacBuilder = new OduAttrPacBuilder();
            oduAttrPacBuilder.setAdaptationType(apiAdaptationTypeToDev(grouping.getOduAttrPac().getAdaptationType()));
            oduAttrPacBuilder.setSignalType(apiClientSignalTypeToDev(grouping.getOduAttrPac().getSignalType()));
            oduAttrPacBuilder.setSwitchCapability(apiSwitchTypeToDev(grouping.getOduAttrPac().getSwitchCapability()));
            oduAttrPacBuilder.setTsDetail(grouping.getOduAttrPac().getTsDetail());
            builder.setOduAttrPac(oduAttrPacBuilder.build());
        }
        if (grouping.getOsuAttrPac() != null) {
            OsuAttrPacBuilder osuAttrPacBuilder = new OsuAttrPacBuilder();
            osuAttrPacBuilder.setTpn(grouping.getOsuAttrPac().getTpn());
            builder.setOsuAttrPac(osuAttrPacBuilder.build());
        }
        if (grouping.getSdhAttrPac() != null) {
            SdhAttrPacBuilder sdhAttrPacBuilder = new SdhAttrPacBuilder();
            sdhAttrPacBuilder.setMappingPath(grouping.getSdhAttrPac().getMappingPath());
            sdhAttrPacBuilder.setSignalType(apiClientSignalTypeToDev(grouping.getSdhAttrPac().getSignalType()));
            sdhAttrPacBuilder.setTimeSlot(grouping.getSdhAttrPac().getTimeSlot());
            sdhAttrPacBuilder.setVcType(apiSdhSwitchTypeToDev(grouping.getSdhAttrPac().getVcType()));
            builder.setSdhAttrPac(sdhAttrPacBuilder.build());
        }
        builder.setPtpName(grouping.getPtpName());
        return builder.build();
    }
}
