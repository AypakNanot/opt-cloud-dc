/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.collection.CollectionUtil;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.AbstractCtcTransformer;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.CommonTransform;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.eqs.grouping.Eq;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.eqs.grouping.EqBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.eqs.grouping.EqKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 板卡属性 转换
 * @author Quan Jingyuan
 * @since 2021/10/14
 **/
public class EqTransformImpl extends AbstractCtcTransformer implements CommonTransform {

    public List<Eq> devEqToApiList(Collection<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.eqs.Eq> eqList){
        if(CollectionUtil.isEmpty(eqList)){
            return null;
        }
        List<Eq> result = new ArrayList<>();
        for(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.eqs.Eq eq:eqList){
            result.add(devEqToApi(eq));
        }
        return result;
    }

    public Eq devEqToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.eqs.Eq eq){
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
        eqBuilder.setSerialNumber(eq.getSerialNumber());
        if(eq.getEqType()!=null){
            eqBuilder.setEqType(CollUtil.newHashSet(devEqTypeToApiList(CollUtil.newArrayList(eq.getEqType()))));
        }
        if(eq.getWorkingState()!=null){
            eqBuilder.setWorkingState(devWorkingStateToApi(eq.getWorkingState()));

        }
        return eqBuilder.build();
    }
}
