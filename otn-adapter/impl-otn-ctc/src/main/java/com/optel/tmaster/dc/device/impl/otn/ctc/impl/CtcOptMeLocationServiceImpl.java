/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptMeLocationServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.util.OptelLocationUtil;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.me.location.rev200911.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.MeLocations;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.PortLightsState;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 首页子架图实现接口
 *
 * @author Quan Jingyuan 2020/9/11
 **/
public class CtcOptMeLocationServiceImpl extends BaseOptMeLocationServiceImpl implements IDeviceServiceOtnCtc {
    private static final Logger LOG = LoggerFactory.getLogger(OptMeLocationService.class);


    @Override
    public ListenableFuture<RpcResult<GetDevCapabilitySetOutput>> getDevCapabilitySet(GetDevCapabilitySetInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<GetPortStateOutput>> getPortState(GetPortStateInput input) {
        InstanceIdentifier<org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.PortLightsState> iid = create(org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.PortLightsState.class);
        PortLightsState portLightsState = null;

//        try {
//            portLightsState = MountTools.queryFromOperational(input.getNeId(), iid);
//        } catch (Exception e) {
//            portLightsState = null;
//        }
        GetPortStateOutputBuilder getPortStateOutputBuilder = new GetPortStateOutputBuilder();
        if (portLightsState != null) {
            getPortStateOutputBuilder.setEq(portLightsState.getEq());
        }

        return RpcResultUtil.success(getPortStateOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<EqLocationsOutput>> eqLocations(EqLocationsInput input) {
        InstanceIdentifier<MeLocations> iid = create(org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.MeLocations.class);
        org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.MeLocations meLocations = null;

//        try {
//            meLocations = MountTools.queryFromOperational(input.getNeId(), iid);
//        } catch (Exception e) {
//            meLocations = OptelLocationUtil.getLocations(input.getDeviceType().intValue(), input.getProductName(), input.getPowerPanelType());
//        }
        meLocations = OptelLocationUtil.getLocations(input.getDeviceType().intValue(), input.getProductName(), input.getPowerPanelType());
        EqLocationsOutputBuilder locationsOutputBuilder = new EqLocationsOutputBuilder();
        if (meLocations == null) {
            meLocations = OptelLocationUtil.getLocations(input.getDeviceType().intValue(), input.getProductName(), input.getPowerPanelType());
        }
        locationsOutputBuilder.setEqObject(meLocations.getEqObject());
        locationsOutputBuilder.setMeHeight(meLocations.getMeHeight());
        locationsOutputBuilder.setMeWidth(meLocations.getMeWidth());
        return RpcResultUtil.success(locationsOutputBuilder.build());
    }
}
