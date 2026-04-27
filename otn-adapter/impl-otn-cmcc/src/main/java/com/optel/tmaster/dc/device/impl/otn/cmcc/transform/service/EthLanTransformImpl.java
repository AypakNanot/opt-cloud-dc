/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform.service;

import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEthLanServiceNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEthLanServiceNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEthLanServiceNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.VsiGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.create.eth.lan.service.in.grouping.Nnis;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.create.eth.lan.service.in.grouping.Unis;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.create.service.out.grouping.CreateComponentBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.create.service.out.grouping.VsiBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.CreateEthLanServiceInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.CreateEthLanServiceInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.CreateServiceOutGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.create.eth.lan.service.in.grouping.NnisBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.create.eth.lan.service.in.grouping.NnisKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.create.eth.lan.service.in.grouping.UnisBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.create.eth.lan.service.in.grouping.UnisKey;

import java.util.ArrayList;
import java.util.List;

/**
 * EthLanTransformImpl
 * 以太网专网业务
 * date       time        author
 * ─────────────────────────────
 * 2021/10/13   16:14      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class EthLanTransformImpl extends VsiTransformImpl {

    /**
     * 创建以太网专网业务 输入参数 api转dev
     * @param input 创建以太网专网业务 输入参数 api
     * @return 创建以太网专网业务 输入参数 dev
     */
    public CreateEthLanServiceInput apiCreateEthLanServiceInputToDev(CreateEthLanServiceNeInput input){
        if(input == null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.BasicParamGrouping basicParamGrouping = apiBasicParamGroupingToDev(input);
        CreateEthLanServiceInputBuilder builder = new CreateEthLanServiceInputBuilder(basicParamGrouping);
        if(input.getUnis() != null){
            List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.create.eth.lan.service.in.grouping.Unis> unisList = new ArrayList<>();
            for(Unis unis:input.getUnis().values()){
                UnisBuilder unisBuilder = new UnisBuilder(apiUniGroupingToDev(unis));
                unisBuilder.withKey(new UnisKey(unis.key().getUniPtpName()));
                unisList.add(unisBuilder.build());
            }
            builder.setUnis(ltm(unisList));
        }
        if(input.getNnis() != null){
            List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.create.eth.lan.service.in.grouping.Nnis> nniList = new ArrayList<>();
            for(Nnis nnis:input.getNnis().values()){
                NnisBuilder nnisBuilder = new NnisBuilder(apiNniGroupingToDev(nnis));
                nnisBuilder.withKey(new NnisKey(nnis.key().getNniKey()));
                nniList.add(nnisBuilder.build());
            }
            builder.setNnis(ltm(nniList));
        }
        return builder.build();
    }



    /**
     * 创建以太网专网业务输出参数 dev转api
     * @param output 创建以太网专网业务输出参数 dev
     * @return 创建以太网专网业务输出参数 api
     */
    public CreateEthLanServiceNeOutput devCreateEthLanServiceOutputToApi(CreateServiceOutGrouping output){
        if(output == null){
            return null;
        }
        CreateEthLanServiceNeOutputBuilder builder = new CreateEthLanServiceNeOutputBuilder();
        if(output.getCreateComponent() != null){
            CreateComponentBuilder createComponentBuilder = new CreateComponentBuilder();
            createComponentBuilder.setCtpName(output.getCreateComponent().getCtpName());
            createComponentBuilder.setFtpName(output.getCreateComponent().getFtpName());
            createComponentBuilder.setVcConnectionName(output.getCreateComponent().getVcConnectionName());
            builder.setCreateComponent(createComponentBuilder.build());
        }
        VsiGrouping vsiGrouping = devVsiToApi(output.getVsi());
        if(vsiGrouping != null){
            VsiBuilder vsiBuilder = new VsiBuilder(vsiGrouping);
            builder.setVsi(vsiBuilder.build());
        }
        return builder.build();
    }


}
