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
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetPrbsResultOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetPrbsResultOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.set.prbs.parameters.input.PrbsParameters;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.prbs.parameters.grouping.PrbsParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.prbs.parameters.grouping.PrbsParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.prbs.parameters.grouping.PrbsParameterKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.set.prbs.parameters.input.PrbsParametersBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Quan Jingyuan
 * @since 2022/1/8
 **/
public class PrbsTransformImpl extends AbstractCtcTransformer implements CommonTransform, EnumTransform, ServiceTransform {

    public PrbsBitErrorValueCleanInput apiToPrbsBitErrorValueCleanInputDev(org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.PrbsBitErrorValueCleanInput input) {
        PrbsBitErrorValueCleanInputBuilder builder = new PrbsBitErrorValueCleanInputBuilder();
        builder.setCtpName(input.getCtpName());
        return builder.build();
    }

    public GetPrbsResultInput apiToGetPrbsResultInputDev(org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetPrbsResultInput input) {
        GetPrbsResultInputBuilder builder = new GetPrbsResultInputBuilder();
        builder.setCtpName(input.getCtpName());
        return builder.build();
    }

    public GetPrbsResultOutput devToGetPrbsResultOutputApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetPrbsResultOutput output){
        GetPrbsResultOutputBuilder builder=new GetPrbsResultOutputBuilder();
        builder.setBitErrorValue(output.getBitErrorValue());
        if(output.getBitErrorValue()!=null){
            builder.setMeasureState(devToPrbsMeasureStateApi(output.getMeasureState()));
        }
        if(output.getMeasureState()!=null){
            builder.setResult(devToPrbsResultTypeApi(output.getResult()));
        }
        return builder.build();
    }
    public SetPrbsParametersInput apiToSetPrbsParametersInputDev(org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.SetPrbsParametersInput input){
        SetPrbsParametersInputBuilder builder=new SetPrbsParametersInputBuilder();
        PrbsParameters prbsParameters = input.getPrbsParameters();
        List<PrbsParameter> parameters=new ArrayList<>();
        if(prbsParameters!=null&&prbsParameters.getPrbsParameter()!=null){
            for (org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.prbs.parameters.grouping.PrbsParameter item:prbsParameters.getPrbsParameter().values()){
                org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.prbs.parameters.grouping.PrbsParameterBuilder prbsParameterBuilder=new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.prbs.parameters.grouping.PrbsParameterBuilder();
                prbsParameterBuilder.setCtpName(item.getCtpName());
                prbsParameterBuilder.withKey(new PrbsParameterKey(item.key().getCtpName()));
                prbsParameterBuilder.setPrbsMonitorEnable(item.getPrbsMonitorEnable());
                prbsParameterBuilder.setPrbsSendEnable(item.getPrbsSendEnable());
                parameters.add(prbsParameterBuilder.build());
            }
        }
        PrbsParametersBuilder parametersBuilder=new PrbsParametersBuilder();
        parametersBuilder.setPrbsParameter(ltm(parameters));
        builder.setPrbsParameters(parametersBuilder.build());
        return builder.build();
    }
    public GetPrbsParameterInput apiToGetPrbsParameterInputDev(org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetPrbsParameterInput input){
        GetPrbsParameterInputBuilder builder=new GetPrbsParameterInputBuilder();
        builder.setCtpName(input.getCtpName());
        return builder.build();
    }
    public org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetPrbsParameterOutput devToGetPrbsParameterOutputApi(GetPrbsParameterOutput output){
        org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetPrbsParameterOutputBuilder builder=new org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetPrbsParameterOutputBuilder();
        List<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.prbs.parameters.grouping.PrbsParameter> parameterList=new ArrayList<>();
        if(output.getPrbsParameters()!=null&&output.getPrbsParameters().getPrbsParameter()!=null){
            for(PrbsParameter item:output.getPrbsParameters().getPrbsParameter().values()){
                PrbsParameterBuilder parameterBuilder=new PrbsParameterBuilder();
                parameterBuilder.setCtpName(item.getCtpName());
                parameterBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.prbs.parameters.grouping.PrbsParameterKey(item.key().getCtpName()));
                parameterBuilder.setPrbsSendEnable(item.getPrbsSendEnable());
                parameterBuilder.setPrbsMonitorEnable(item.getPrbsMonitorEnable());
                parameterList.add(parameterBuilder.build());
            }
        }
        org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.get.prbs.parameter.output.PrbsParametersBuilder prbsParametersBuilder=new org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.get.prbs.parameter.output.PrbsParametersBuilder();
        prbsParametersBuilder.setPrbsParameter(ltm(parameterList));
        builder.setPrbsParameters(prbsParametersBuilder.build());
        return builder.build();
    }
}
