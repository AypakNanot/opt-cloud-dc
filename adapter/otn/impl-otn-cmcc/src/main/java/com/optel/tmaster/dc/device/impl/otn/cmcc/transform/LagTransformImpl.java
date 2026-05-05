/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform;

import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.EnumTransform;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.ServiceTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.AddOrUpdateLagParameterInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.lag.parameters.grouping.LagParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lag.parameter.grouping.PortMembers;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lag.parameter.grouping.PortMembersBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lag.parameters.grouping.LagParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lag.parameters.grouping.LagParameterKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.port.member.grouping.PortMember;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.port.member.grouping.PortMemberBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.port.member.grouping.PortMemberKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * lag 转换
 *
 * @author Quan Jingyuan
 * @since 2021/10/16
 **/
public class LagTransformImpl implements CommonTransform, EnumTransform, ServiceTransform {
    public LagParameterBuilder apiLagParameterToDev(AddOrUpdateLagParameterInput input) {
        LagParameterBuilder builder = new LagParameterBuilder();
        if (input == null) {
            return builder;
        }
        builder.setLagId(input.getLagId());
        builder.withKey(new LagParameterKey(input.getLagId()));
        builder.setSelectedPorts(input.getSelectedPorts());
        builder.setSysPriority(input.getSysPriority());
        if (input.getAggregationMode() != null) {
            builder.setAggregationMode(apiAggregationModeToDev(input.getAggregationMode()));
        }
        if (input.getLoadAlgorithm() != null) {
            builder.setLoadAlgorithm(apiLoadAlgorithmToDev(input.getLoadAlgorithm()));
        }
        if (input.getPortMembers() != null) {
            builder.setPortMembers(apiPortMembersToDev(input.getPortMembers()));
        }
        if (input.getWorkingMode() != null) {
            builder.setWorkingMode(apiProtectionTypeToDev(input.getWorkingMode()));
        }
        return builder;
    }

    public List<LagParameter> devLagParameterToApi(Collection<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lag.parameters.grouping.LagParameter> parameters) {
        List<LagParameter> lagParameters = new ArrayList<>();
        if (parameters == null) {
            return lagParameters;
        }
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lag.parameters.grouping.LagParameter lagParameter : parameters) {
            org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.lag.parameters.grouping.LagParameterBuilder builder = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.lag.parameters.grouping.LagParameterBuilder();
            builder.setLagId(lagParameter.getLagId());
            builder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.lag.parameters.grouping.LagParameterKey(lagParameter.getLagId()));
            builder.setSelectedPorts(lagParameter.getSelectedPorts());
            builder.setSysPriority(lagParameter.getSysPriority());
            if (lagParameter.getAggregationMode() != null) {
                builder.setAggregationMode(devAggregationModeToApi(lagParameter.getAggregationMode()));
            }
            if (lagParameter.getLoadAlgorithm() != null) {
                builder.setLoadAlgorithm(devLoadAlgorithmToApi(lagParameter.getLoadAlgorithm()));
            }
            if (lagParameter.getPortMembers() != null) {
                builder.setPortMembers(devPortMembersToApi(lagParameter.getPortMembers()));
            }
            if (lagParameter.getWorkingMode() != null) {
                builder.setWorkingMode(devProtectionTypeToApi(lagParameter.getWorkingMode()));
            }
            lagParameters.add(builder.build());
        }
        return lagParameters;
    }

    private org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.lag.parameter.grouping.PortMembers devPortMembersToApi(PortMembers portMembers) {
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.lag.parameter.grouping.PortMembersBuilder builder = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.lag.parameter.grouping.PortMembersBuilder();
        if (portMembers == null || portMembers.getPortMember() == null) {
            return null;
        }

        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.port.member.grouping.PortMember> portMemberList = new ArrayList<>();
        for (PortMember item : portMembers.getPortMember().values()) {
            org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.port.member.grouping.PortMemberBuilder portMemberBuilder = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.port.member.grouping.PortMemberBuilder();
            portMemberBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.port.member.grouping.PortMemberKey(item.getName()));
            portMemberBuilder.setName(item.getName());
            portMemberBuilder.setPriority(item.getPriority());
            if (item.getRole() != null) {
                portMemberBuilder.setRole(devPortMemberRoleToApi(item.getRole()));
            }
            portMemberList.add(portMemberBuilder.build());
        }

        builder.setPortMember(ltm(portMemberList));
        return builder.build();
    }

    private PortMembers apiPortMembersToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.lag.parameter.grouping.PortMembers portMembers) {
        PortMembersBuilder builder = new PortMembersBuilder();
        if (portMembers == null || portMembers.getPortMember() == null) {
            return null;
        }

        List<PortMember> portMemberList = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.port.member.grouping.PortMember item : portMembers.getPortMember().values()) {
            PortMemberBuilder portMemberBuilder = new PortMemberBuilder();
            portMemberBuilder.withKey(new PortMemberKey(item.getName()));
            portMemberBuilder.setName(item.getName());
            portMemberBuilder.setPriority(item.getPriority());
            if (item.getRole() != null) {
                portMemberBuilder.setRole(apiPortMemberRoleToDev(item.getRole()));
            }
            portMemberList.add(portMemberBuilder.build());
        }

        builder.setPortMember(ltm(portMemberList));
        return builder.build();
    }
}
