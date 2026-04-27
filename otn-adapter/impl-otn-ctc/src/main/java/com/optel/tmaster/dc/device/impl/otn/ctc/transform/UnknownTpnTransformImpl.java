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
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetUnknownTpnOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetUnknownTpnOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.get.unknown.tpn.output.UnknownTpns;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.get.unknown.tpn.output.UnknownTpnsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.unknown.tpn.grouping.UnknownTpn;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.unknown.tpn.grouping.UnknownTpnBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.unknown.tpn.grouping.UnknownTpnKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetUnknownTpnInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetUnknownTpnInputBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Quan Jingyuan
 * @since 2022/1/7
 **/
public class UnknownTpnTransformImpl extends AbstractCtcTransformer implements CommonTransform, EnumTransform {

    public GetUnknownTpnInput apiToGetUnknownTpnInputDev(org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetUnknownTpnInput input){
        GetUnknownTpnInputBuilder builder=new GetUnknownTpnInputBuilder();
        builder.setCtpName(input.getCtpName());
       return builder.build();
    }
    public GetUnknownTpnOutput devToGetUnknownTpnOutputApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetUnknownTpnOutput output){
        GetUnknownTpnOutputBuilder builder=new GetUnknownTpnOutputBuilder();
        builder.setUnknownTpns(devToUnknownTpnsApi(output.getUnknownTpns()));
        return builder.build();
    }


    private UnknownTpns devToUnknownTpnsApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.get.unknown.tpn.output.UnknownTpns tpns){
        UnknownTpnsBuilder builder=new UnknownTpnsBuilder();
        List<UnknownTpn> tpnList=new ArrayList<>();
        if(tpns ==null || tpns.getUnknownTpn()==null){
            return builder.build();
        }
        for(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.unknown.tpn.grouping.UnknownTpn tpn:tpns.getUnknownTpn().values()){
            UnknownTpnBuilder tpnBuilder=new UnknownTpnBuilder();
            tpnBuilder.setCtpName(tpn.getCtpName());
            tpnBuilder.withKey(new UnknownTpnKey(tpn.key().getCtpName()));
            tpnBuilder.setTpn(tpn.getTpn());
            tpnList.add(tpnBuilder.build());
        }
        builder.setUnknownTpn(ltm(tpnList));
        return builder.build();
    }
}
