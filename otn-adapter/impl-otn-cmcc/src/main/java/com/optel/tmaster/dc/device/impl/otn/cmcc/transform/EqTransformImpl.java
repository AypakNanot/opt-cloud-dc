/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform;

import cn.hutool.core.collection.CollectionUtil;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.AbstractCmccTransformer;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.EnumTransform;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.eqs.grouping.Eq;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.eqs.grouping.EqBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.eqs.grouping.EqKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * EqTransformImpl
 * 单板
 * date       time        author
 * ─────────────────────────────
 * 2021/10/9   17:47      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class EqTransformImpl extends AbstractCmccTransformer implements EnumTransform {

    public List<Eq> devEqToApiList(Collection<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.eqs.Eq> eqList){
        if(CollectionUtil.isEmpty(eqList)){
            return null;
        }
        List<Eq> result = new ArrayList<>();
        for(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.eqs.Eq eq:eqList){
            result.add(devEqToApi(eq));
        }
        return result;
    }

    public Eq devEqToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.eqs.Eq eq){
        if(eq == null){
            return null;
        }
        EqBuilder eqBuilder = new EqBuilder();
        eqBuilder.withKey(new EqKey(eq.key().getName()));
        eqBuilder.setName(eq.getName());
        eqBuilder.setFtp(eq.getFtp());
        eqBuilder.setHardwareVersion(eq.getHardwareVersion());
        eqBuilder.setPtp(eq.getPtp());
        eqBuilder.setSoftwareVersion(eq.getSoftwareVersion());
        eqBuilder.setPlugState(eq.getPlugState());
        eqBuilder.setXcCapability(eq.getXcCapability());
        if(eq.getEqType()!=null){
            eqBuilder.setEqType(devEqTypeToApiList(eq.getEqType()));
        }
        if(eq.getWorkingState()!=null){
            eqBuilder.setWorkingState(devWorkingStateToApi(eq.getWorkingState()));

        }
        return eqBuilder.build();
    }
}
