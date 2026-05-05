/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.config;

import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base.PortAndTransceiverAndOtdrAndOpticalChannelTypeTransform;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.transceiver.rev200210.SetTransceiverChannelInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.transceiver.rev200210.SetTransceiverInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev230426.port.transceiver.top.transceiver.Config;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev230426.port.transceiver.top.transceiver.ConfigBuilder;

/**
 * 模块转换类
 *
 * @author Quan Jingyuan
 * @since 2022/3/4
 **/
public class DciTransceiverTransform implements PortAndTransceiverAndOtdrAndOpticalChannelTypeTransform {


    public Config apiToConfigDev(SetTransceiverInput input) {
        ConfigBuilder builder=new ConfigBuilder();
        builder.setEnabled(input.getEnabled());
        builder.setUsedServicePortTypePreconf(apiToUsedServicePortTypeDev(input.getUsedServicePortTypePreconf()));
        return builder.build();
    }
    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev230426.physical.channel.top.physical.channels.channel.Config apiToChannelDev(SetTransceiverChannelInput input){

        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev230426.physical.channel.top.physical.channels.channel.ConfigBuilder configBuilder=new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev230426.physical.channel.top.physical.channels.channel.ConfigBuilder();
//极简OTN无 物理通道修改
                configBuilder.setDescription(input.getDescription());
        configBuilder.setIndex(input.getIndex());
        configBuilder.setTxLaser(input.getTxLaser());
        return configBuilder.build();
    }


}
