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
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.msi.auto.parameter.grouping.MsiAutoParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.msi.auto.parameter.grouping.MsiAutoParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.msi.auto.parameter.grouping.MsiAutoParameterKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.msi.parameter.grouping.MsiParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.msi.parameter.grouping.MsiParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.msi.parameter.grouping.MsiParameterKey;

import java.util.ArrayList;
import java.util.List;

/**
 * ClassName: MsiTransformImpl
 * <ul>
 * <li>Msi转换类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2021/10/9 14:37
 */
public class MsiTransformImpl extends AbstractCtcTransformer implements CommonTransform, EnumTransform {

    public List<MsiAutoParameter> devMsiAutoToApiList (List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.msi.auto.parameter.grouping.MsiAutoParameter> msiAutoParameters){
        if(msiAutoParameters == null){
            return null;
        }
        List<MsiAutoParameter> res = new ArrayList<>();
        for(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.msi.auto.parameter.grouping.MsiAutoParameter msiAutoParameter: msiAutoParameters){
            res.add(devMsiAutoToApi(msiAutoParameter).build());
        }
        return res;
    }

    public MsiAutoParameterBuilder devMsiAutoToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.msi.auto.parameter.grouping.MsiAutoParameter msiAutoParameter){
        if(msiAutoParameter == null){
            return null;
        }
        MsiAutoParameterBuilder msiAutoParameterBuilder = new MsiAutoParameterBuilder();
        msiAutoParameterBuilder.setAuto(msiAutoParameter.getAuto());
        msiAutoParameterBuilder.withKey(new MsiAutoParameterKey(msiAutoParameter.getName()));
        msiAutoParameterBuilder.setName(msiAutoParameter.getName());
        return msiAutoParameterBuilder;
    }

    public List<MsiParameter> devMsiParameterToApiList (List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.msi.parameter.grouping.MsiParameter> msiParameters){
        if(msiParameters == null){
            return null;
        }
        List<MsiParameter> res = new ArrayList<>();
        for(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.msi.parameter.grouping.MsiParameter msiParameter : msiParameters){
            res.add(devMsiParameterToApi(msiParameter).build());
        }
        return res;
    }

    public MsiParameterBuilder devMsiParameterToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.msi.parameter.grouping.MsiParameter msiParameter){
        if(msiParameter == null){
            return null;
        }
        MsiParameterBuilder msiParameterBuilder = new MsiParameterBuilder();
        msiParameterBuilder.withKey(new MsiParameterKey(msiParameter.getName(),msiParameter.getTributaryPortNum()));
        msiParameterBuilder.setName(msiParameter.getName());
        msiParameterBuilder.setOdukLevel(devSwitchTypeToApi(msiParameter.getOdukLevel()));
        msiParameterBuilder.setTributaryPortNum(msiParameter.getTributaryPortNum());
        msiParameterBuilder.setTsNum(msiParameter.getTsNum());
        msiParameterBuilder.setTsActualRx(msiParameter.getTsActualRx());
        msiParameterBuilder.setTsOccupiedRx(msiParameter.getTsOccupiedRx());
        msiParameterBuilder.setTsOccupiedTx(msiParameter.getTsOccupiedTx());
        return msiParameterBuilder;
    }
}
