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
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.PerformanceTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.performance.rev200428.SetPmStateInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.performance.rev210927.pm.states.PmState;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.performance.rev210927.pm.states.PmStateBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.performance.rev210927.pm.states.PmStateKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev190213.SetPmStateInputBuilder;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ClassName: PmStateTransformImpl
 * <ul>
 * <li>性能监控状态 转换</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2021/10/13 14:31
 */
public class PmStateTransformImpl extends AbstractCmccTransformer implements PerformanceTransform {

    public List<PmState> devPmStateToApiList(Collection<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev190213.pm.states.PmState> pmStates){
        if(CollectionUtil.isEmpty(pmStates)){
            return null;
        }
        List<PmState> pmStateList = new ArrayList<>();
        for(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev190213.pm.states.PmState pmState:pmStates){
            pmStateList.add(devPmStateToApi(pmState).build());
        }
        return pmStateList;
    }

    /**
     * dev pmState to api
     * @param pmState dev
     * @return api
     */
    public PmStateBuilder devPmStateToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev190213.pm.states.PmState pmState){
        if(pmState == null){
            return null;
        }
        PmStateBuilder pmStateBuilder = new PmStateBuilder();
        PmStateKey pmStateKey = new PmStateKey(devGranularityToApi(pmState.getGranularity()),pmState.getObjectName(),pmState.getPmParameterName());
        pmStateBuilder.withKey(pmStateKey);
        pmStateBuilder.setObjectName(pmState.getObjectName());
        pmStateBuilder.setGranularity(devGranularityToApi(pmState.getGranularity()));
        pmStateBuilder.setObjectType(pmState.getObjectType());
        pmStateBuilder.setPmParameterName(pmState.getPmParameterName());
        pmStateBuilder.setPmState(pmState.getPmState());
        return pmStateBuilder;
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev190213.SetPmStateInput apiSetPmStateInputDev(SetPmStateInput setPmStateInput){
        if (setPmStateInput == null){
            return null;
        }
        SetPmStateInputBuilder setPmStateInputBuilder = new SetPmStateInputBuilder();
        setPmStateInputBuilder.setPmState(setPmStateInput.getPmState());
        setPmStateInputBuilder.setGranularity(apiGranularityToDev(setPmStateInput.getGranularity()));
        setPmStateInputBuilder.setIncludeSubObj(setPmStateInput.getIncludeSubObj());
        setPmStateInputBuilder.setObjectName(setPmStateInput.getObjectName());
        setPmStateInputBuilder.setObjectType(setPmStateInput.getObjectType());
        setPmStateInputBuilder.setPmParameterName(setPmStateInput.getPmParameterName());
        return setPmStateInputBuilder.build();
    }
}
