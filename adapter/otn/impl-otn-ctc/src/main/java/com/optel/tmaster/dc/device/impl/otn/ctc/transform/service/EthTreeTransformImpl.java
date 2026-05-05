/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform.service;

import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEthTreeServiceNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEthTreeServiceNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEthTreeServiceNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.create.eth.tree.service.in.grouping.Nnis;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.create.eth.tree.service.in.grouping.Unis;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.BasicParamGrouping;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.CreateEthTreeServiceInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.CreateEthTreeServiceInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.CreateEthTreeServiceOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.create.eth.tree.service.in.grouping.NnisBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.create.eth.tree.service.in.grouping.NnisKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.create.eth.tree.service.in.grouping.UnisBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.create.eth.tree.service.in.grouping.UnisKey;

import java.util.ArrayList;
import java.util.List;

/**
 * EthTreeTransformImpl
 * 以太网汇聚业务
 * date       time        author
 * ─────────────────────────────
 * 2021/10/15   9:24      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class EthTreeTransformImpl extends VsiTransformImpl {

    /**
     * 创建以太网汇聚业务输入参数 api转dev
     * @param input 创建以太网汇聚业务输入参数
     * @return 创建以太网汇聚业务输入参数
     */
    public CreateEthTreeServiceInput apiCreateTreeInputToDev(CreateEthTreeServiceNeInput input){
        if(input == null){
            return null;
        }
        BasicParamGrouping basicParamGrouping = apiBasicParamGroupingToDev(input);
        if(basicParamGrouping == null){
            return null;
        }
        CreateEthTreeServiceInputBuilder builder = new CreateEthTreeServiceInputBuilder(basicParamGrouping);
        //unis
        if(input.getUnis() != null){
            List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.create.eth.tree.service.in.grouping.Unis> devUnis = new ArrayList<>();
            for(Unis unis:input.getUnis().values()){
                UnisBuilder unisBuilder = new UnisBuilder(apiUniGroupingToDev(unis));
                unisBuilder.withKey(new UnisKey(unis.key().getUniPtpName()));
                unisBuilder.setPortRole(apiPortRoleToDev(unis.getPortRole()));
                devUnis.add(unisBuilder.build());
            }
            builder.setUnis(ltm(devUnis));
        }
        //nnis
        if(input.getNnis() != null){
            List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.create.eth.tree.service.in.grouping.Nnis> devNnis = new ArrayList<>();
            for(Nnis nnis:input.getNnis().values()){
                NnisBuilder nnisBuilder = new NnisBuilder(apiNniGroupingToDev(nnis));
                nnisBuilder.withKey(new NnisKey(nnis.key().getNniKey()));
                devNnis.add(nnisBuilder.build());
            }
            builder.setNnis(ltm(devNnis));
        }
        return builder.build();
    }


    public CreateEthTreeServiceNeOutput devCreateEthTreeServiceToApi(CreateEthTreeServiceOutput output){
        if(output == null){
            return null;
        }
        CreateEthTreeServiceNeOutputBuilder builder = new CreateEthTreeServiceNeOutputBuilder(new EthLanTransformImpl().devCreateEthLanServiceOutputToApi(output));
        return builder.build();
    }
}
