/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform;

import cn.hutool.core.map.MapUtil;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.AbstractCmccTransformer;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.EnumTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.obj.installation.rev210116.InstallPtpTypeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.obj.installation.rev210116.QueryEqInstallationOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SetEqTypeInParameterGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eh.installation.capabilitys.grouping.EhInstallationCapability;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eh.installation.capabilitys.grouping.EhInstallationCapabilityBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eh.installation.capabilitys.grouping.EhInstallationCapabilityKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eq.installation.capability.grouping.PtpCapability;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eq.installation.capability.grouping.PtpCapabilityBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eq.installation.capability.grouping.PtpCapabilityKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eq.installation.capabilitys.grouping.EqInstallationCapabilityBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eq.installation.capabilitys.grouping.EqInstallationCapabilityKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.set.eq.type.out.parameter.grouping.SetEqTypeOutParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.set.eq.type.out.parameter.grouping.SetEqTypeOutParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.set.eq.type.out.parameter.grouping.SetEqTypeOutParameterKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.set.ptp.type.out.parameter.grouping.SetPtpTypeOutParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.set.ptp.type.out.parameter.grouping.SetPtpTypeOutParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.set.ptp.type.out.parameter.grouping.SetPtpTypeOutParameterKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.SetEqTypeInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.SetEqTypeInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.SetEqTypeOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.SetPtpTypeInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.SetPtpTypeOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eq.installation.capabilitys.grouping.EqInstallationCapability;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.set.eq.type.in.parameter.grouping.SetEqTypeInParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.set.eq.type.in.parameter.grouping.SetEqTypeInParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.set.eq.type.in.parameter.grouping.SetEqTypeInParameterKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.set.ptp.type.in.parameter.grouping.SetPtpTypeInParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.set.ptp.type.in.parameter.grouping.SetPtpTypeInParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.set.ptp.type.in.parameter.grouping.SetPtpTypeInParameterKey;

import java.util.*;

/**
 * 端口安装 转换
 *
 * @author Quan Jingyuan
 * @since 2021/10/13
 **/
public class ObjInstallTransformImpl extends AbstractCmccTransformer implements CommonTransform, EnumTransform {

    public Map<EhInstallationCapabilityKey, EhInstallationCapability> apiEhInstallationToDev(List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eh.installation.capabilitys.grouping.EhInstallationCapability> capabilities) {
        if (capabilities == null) {
            return MapUtil.empty();
        }
        Map<EhInstallationCapabilityKey, EhInstallationCapability> ehInstallationCapabilities = new HashMap<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eh.installation.capabilitys.grouping.EhInstallationCapability item : capabilities) {
            EhInstallationCapabilityKey key = new EhInstallationCapabilityKey(item.key().getName());
            EhInstallationCapabilityBuilder builder = new EhInstallationCapabilityBuilder();
            builder.setActualEqType(item.getActualEqType());
            builder.setEqType(item.getEqType());
            builder.withKey(key);
            builder.setName(item.getName());
            builder.setSupportEqType(item.getSupportEqType());
            ehInstallationCapabilities.put(key, builder.build());
        }
        return ehInstallationCapabilities;
    }

    public SetEqTypeInput apiSetEqTypeInputToDev(SetEqTypeInParameterGrouping grouping) {
        if (grouping == null) {
            return null;
        }
        SetEqTypeInputBuilder builder = new SetEqTypeInputBuilder();
        if (grouping.getSetEqTypeInParameter() != null) {
            List<SetEqTypeInParameter> eqTypeInParameters = new ArrayList<>();
            for (org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.set.eq.type.in.parameter.grouping.SetEqTypeInParameter item : grouping.getSetEqTypeInParameter().values()) {
                SetEqTypeInParameterBuilder parameter = new SetEqTypeInParameterBuilder();
                parameter.setEhName(item.getEhName());
                parameter.setEqType(item.getEqType());
                parameter.withKey(new SetEqTypeInParameterKey(item.key().getEhName()));
                eqTypeInParameters.add(parameter.build());
            }
            builder.setSetEqTypeInParameter(ltm(eqTypeInParameters));
        }
        return builder.build();
    }

    public SetPtpTypeInputBuilder apiSetPtpTypeInputToDev(InstallPtpTypeInput input) {
        SetPtpTypeInputBuilder builder = new SetPtpTypeInputBuilder();
        if (input == null || input.getSetPtpTypeInParameter() == null) {
            return builder;
        }
        List<SetPtpTypeInParameter> parameterList = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.set.ptp.type.in.parameter.grouping.SetPtpTypeInParameter parameter : input.getSetPtpTypeInParameter().values()) {
            SetPtpTypeInParameterBuilder parameterBuilder = new SetPtpTypeInParameterBuilder();
            parameterBuilder.setEqName(parameter.getEqName());
            parameterBuilder.withKey(new SetPtpTypeInParameterKey(parameter.getEqName(), parameter.getPortId()));
            parameterBuilder.setPortId(parameter.getPortId());
            parameterBuilder.setPtpType(parameter.getPtpType());
            parameterList.add(parameterBuilder.build());
        }
        builder.setSetPtpTypeInParameter(ltm(parameterList));
        return builder;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SetEqTypeOutputBuilder devEqTypeOutputToApi(SetEqTypeOutput grouping) {
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SetEqTypeOutputBuilder builder = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SetEqTypeOutputBuilder();
        if (grouping == null || grouping.getSetEqTypeOutParameter() == null) {
            return builder;
        }
        List<SetEqTypeOutParameter> setEqTypeOutParameters = new ArrayList<>();

        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.set.eq.type.out.parameter.grouping.SetEqTypeOutParameter parameter : grouping.getSetEqTypeOutParameter().values()) {
            SetEqTypeOutParameterBuilder parameterBuilder = new SetEqTypeOutParameterBuilder();
            parameterBuilder.setEhName(parameter.getEhName());
            parameterBuilder.setEqName(parameter.getEqName());
            parameterBuilder.withKey(new SetEqTypeOutParameterKey(parameter.key().getEhName()));
            if (parameter.getResult() != null) {
                parameterBuilder.setResult(devSetTypeResultToApi(parameter.getResult()));
            }
            setEqTypeOutParameters.add(parameterBuilder.build());
        }
        builder.setSetEqTypeOutParameter(ltm(setEqTypeOutParameters));

        return builder;
    }

    public QueryEqInstallationOutputBuilder devQueryEqInstallationOutputToApi(List<EqInstallationCapability> list) {
        QueryEqInstallationOutputBuilder builder = new QueryEqInstallationOutputBuilder();
        if (list == null) {
            return builder;
        }
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eq.installation.capabilitys.grouping.EqInstallationCapability> capabilities = new ArrayList<>();
        for (EqInstallationCapability item : list) {
            org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eq.installation.capabilitys.grouping.EqInstallationCapabilityBuilder capabilityBuilder = new EqInstallationCapabilityBuilder();
            capabilityBuilder.setEqName(item.getEqName());
            capabilityBuilder.withKey(new EqInstallationCapabilityKey(item.key().getEqName()));
            capabilityBuilder.setPtpCapability(ltm(devPtpCapabilityToApi(item.nonnullPtpCapability().values())));
            capabilities.add(capabilityBuilder.build());
        }
        builder.setEqInstallationCapability(ltm(capabilities));
        return builder;
    }

    private List<PtpCapability> devPtpCapabilityToApi(Collection<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eq.installation.capability.grouping.PtpCapability> ptpCapabilities) {
        if (ptpCapabilities == null) {
            return new ArrayList<>();
        }
        List<PtpCapability> capabilityList = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eq.installation.capability.grouping.PtpCapability item : ptpCapabilities) {
            PtpCapabilityBuilder ptpCapabilityBuilder = new PtpCapabilityBuilder();

            ptpCapabilityBuilder.setActualPtpType(item.getActualPtpType());
            ptpCapabilityBuilder.withKey(new PtpCapabilityKey(item.key().getPortId()));
            ptpCapabilityBuilder.setPortId(item.getPortId());
            ptpCapabilityBuilder.setPtpType(item.getPtpType());
            ptpCapabilityBuilder.setSupportPtpType(item.getSupportPtpType());
            capabilityList.add(ptpCapabilityBuilder.build());
        }
        return capabilityList;

    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SetPtpTypeOutputBuilder devSetPtpTypeOutputToApi(SetPtpTypeOutput output) {
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SetPtpTypeOutputBuilder builder = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SetPtpTypeOutputBuilder();
        if (output == null || output.getSetPtpTypeOutParameter() == null) {
            return builder;
        }
        List<SetPtpTypeOutParameter> ptpTypeOutParameters = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.set.ptp.type.out.parameter.grouping.SetPtpTypeOutParameter item : output.getSetPtpTypeOutParameter().values()) {
            SetPtpTypeOutParameterBuilder parameterBuilder = new SetPtpTypeOutParameterBuilder();
            parameterBuilder.setEqName(item.getEqName());
            parameterBuilder.withKey(new SetPtpTypeOutParameterKey(item.getEqName(), item.getPortId()));
            parameterBuilder.setPortId(item.getPortId());
            parameterBuilder.setPtpName(item.getPtpName());
            if (item.getResult() != null) {
                parameterBuilder.setResult(devSetTypeResultToApi(item.getResult()));

            }
            ptpTypeOutParameters.add(parameterBuilder.build());
        }
        builder.setSetPtpTypeOutParameter(ltm(ptpTypeOutParameters));
        return builder;

    }
}
