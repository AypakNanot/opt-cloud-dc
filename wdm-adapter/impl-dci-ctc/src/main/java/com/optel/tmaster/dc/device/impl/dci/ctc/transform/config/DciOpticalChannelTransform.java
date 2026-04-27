/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.transform.config;

import com.optel.tmaster.dc.device.impl.dci.ctc.transform.base.PortAndTransceiverAndOtdrAndOpticalChannelTypeTransform;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.optical.channel.rev200210.SetOpticalChannelInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.terminal.optical.channel.top.optical.channel.Config;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.terminal.optical.channel.top.optical.channel.ConfigBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev200630.FrequencyType;

/**
 * 光通道转换类
 * @author Quan Jingyuan
 * @since 2022/3/7
 **/
public class DciOpticalChannelTransform implements PortAndTransceiverAndOtdrAndOpticalChannelTypeTransform {


    public Config apiToConfigDev(SetOpticalChannelInput input){
        ConfigBuilder configBuilder=new ConfigBuilder();
        configBuilder.setTargetOutputPower(input.getTargetOutputPower());
        configBuilder.setOperationalMode(input.getOperationalMode());
        configBuilder.setLinePort(input.getLinePort());
        configBuilder.setFrequency(new FrequencyType(input.getFrequency().getValue()));
        return configBuilder.build();
    }

}
