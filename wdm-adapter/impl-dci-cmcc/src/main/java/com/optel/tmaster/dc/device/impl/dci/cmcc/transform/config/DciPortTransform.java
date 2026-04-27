/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.config;

import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base.PlatformTypeTransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base.PortAndTransceiverAndOtdrAndOpticalChannelTypeTransform;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.port.rev200210.SetElectricPortInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.port.rev200210.SetOpticalPortInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.port.rev230426.Config1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.port.rev230426.Config1Builder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.line.common.rev230426.transport.line.common.port.top.optical.port.ConfigBuilder;

/**
 * 端口转换类
 *
 * @author Quan Jingyuan
 * @since 2022/3/4
 **/
public class DciPortTransform implements PortAndTransceiverAndOtdrAndOpticalChannelTypeTransform, PlatformTypeTransform {


    public Config1 apiToConfig1Dev(SetElectricPortInput input) {
        Config1Builder builder = new Config1Builder();
        //极简OTN 无该字段
//        builder.setReverseMode(apiToReverseModeDev(input.getReverseMode()));
        builder.setLayerProtocolName(apiToTribProtocolDev(input.getLayerProtocolName()));
        return builder.build();
    }
    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.line.common.rev230426.transport.line.common.port.top.optical.port.Config apiToOpticalPortConfigDev(SetOpticalPortInput input){
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.line.common.rev230426.transport.line.common.port.top.optical.port.ConfigBuilder builder=new ConfigBuilder();
        builder.setPowerOffset(input.getPowerOffset());
        return builder.build();
    }
}
