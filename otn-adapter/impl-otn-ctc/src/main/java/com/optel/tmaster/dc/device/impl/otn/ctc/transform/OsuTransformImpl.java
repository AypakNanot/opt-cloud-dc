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
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.OsuTwoWayDmOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.OsuTwoWayDmOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.Capacity;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.ModifyOsuConnectionCapacityInput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.ModifyOsuConnectionCapacityInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.OsuTwoWayDmInput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.OsuTwoWayDmInputBuilder;

/**
 * osu 转换
 * @author Quan Jingyuan
 * @since 2021/11/9
 **/
public class OsuTransformImpl  extends AbstractCtcTransformer implements CommonTransform, EnumTransform, ServiceTransform {
    public OsuTwoWayDmInput apiOsuTwoWayDelayToDev(org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.OsuTwoWayDmInput input){
        OsuTwoWayDmInputBuilder builder=new OsuTwoWayDmInputBuilder();
        builder.setCtpName(input.getCtpName());
        builder.setDmType(input.getDmType());
        return builder.build();
    }
    public OsuTwoWayDmOutput devOsuTwoWayDelayToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.OsuTwoWayDmOutput osuTwoWayDmOutput){
        OsuTwoWayDmOutputBuilder builder=new OsuTwoWayDmOutputBuilder();
        builder.setOsuDelay(osuTwoWayDmOutput.getOsuDelay());
        return builder.build();
    }

    public ModifyOsuConnectionCapacityInput apiToOsuConnectionCapacityDev(org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.ModifyOsuConnectionCapacityInput input){
        Capacity requestCapacity = apiCapacityToDev(input);
        ModifyOsuConnectionCapacityInputBuilder builder=new ModifyOsuConnectionCapacityInputBuilder();
        builder.setCtpName(input.getCtpName());
        builder.setCirOrTotalsize(requestCapacity.getCirOrTotalsize());
        builder.setTimer(input.getTimer());
        if(input.getAction()!=null){
            builder.setAction(apiOsuModifyActionToDev(input.getAction()));
        }
        return builder.build();
    }
}
