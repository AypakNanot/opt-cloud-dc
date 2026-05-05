/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform;

import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.AbstractCmccTransformer;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.EnumTransform;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.tcm.parameters.grouping.TcmParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.tcm.parameters.grouping.TcmParameterKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.tcm.parameters.grouping.TcmParameter;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: TcmTransformImpl
 * <ul>
 * <li>Tcm转换类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2021/10/9 14:37
 */
public class TcmTransformImpl extends AbstractCmccTransformer implements CommonTransform, EnumTransform {

    public List<TcmParameter> devTcmToApiList (List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.tcm.parameters.grouping.TcmParameter> tcmParameters){
        if(tcmParameters == null){
            return null;
        }
        List<TcmParameter> res = new ArrayList<>();
        for(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.tcm.parameters.grouping.TcmParameter tcmParameter: tcmParameters){
            res.add(devTcmToApi(tcmParameter).build());
        }
        return res;
    }

    public TcmParameterBuilder devTcmToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.tcm.parameters.grouping.TcmParameter tcmParameter){
        if(tcmParameter == null){
            return null;
        }
        TcmParameterBuilder tcmParameterBuilder = new TcmParameterBuilder();
        tcmParameterBuilder.setDestMode(devActiveModeToApi(tcmParameter.getDestMode()));
        tcmParameterBuilder.withKey(new TcmParameterKey(tcmParameter.getName(),tcmParameter.getTcmGrade()));
        tcmParameterBuilder.setName(tcmParameter.getName());
        tcmParameterBuilder.setSourceMode(devActiveModeToApi(tcmParameter.getSourceMode()));
        tcmParameterBuilder.setTcmGrade(tcmParameter.getTcmGrade());
        tcmParameterBuilder.setTimMode(devTimModeToApi(tcmParameter.getTimMode()));
        tcmParameterBuilder.setTtiActualRx(tcmParameter.getTtiActualRx());
        tcmParameterBuilder.setTtiActualTx(tcmParameter.getTtiActualTx());
        tcmParameterBuilder.setTtiExpectedRx(tcmParameter.getTtiExpectedRx());
        tcmParameterBuilder.setLckInsert(tcmParameter.getLckInsert());
        tcmParameterBuilder.setLtcAction(tcmParameter.getLtcAction());
        tcmParameterBuilder.setTimAction(tcmParameter.getTimAction());
        return tcmParameterBuilder;
    }
}
