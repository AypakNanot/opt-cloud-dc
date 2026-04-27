/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.transform.config;

import com.optel.tmaster.dc.device.impl.dci.ctc.transform.base.PlatformTypeTransform;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.base.PortAndTransceiverAndOtdrAndOpticalChannelTypeTransform;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.channel.monitor.rev220208.channel.monitor.top.ChannelMonitor;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.channel.monitor.rev220208.channel.monitor.top.ChannelMonitorBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.channel.monitor.rev220208.channel.monitor.top.ChannelMonitorKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.channel.monitor.rev220208.media.channel.spectrum.power.top.Channel;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.channel.monitor.rev220208.media.channel.spectrum.power.top.ChannelBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.channel.monitor.rev220208.media.channel.spectrum.power.top.ChannelKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.FrequencyType;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.ocm.rev200210.GetOcmConfigOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.ocm.rev200210.GetOcmConfigOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.ocm.rev200210.SetOcmConfigInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.channel.monitor.rev200630.channel.monitor.top.ChannelMonitors;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.channel.monitor.rev200630.channel.monitor.top.channel.monitors.channel.monitor.Config;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.channel.monitor.rev200630.channel.monitor.top.channel.monitors.channel.monitor.ConfigBuilder;

import java.util.*;

/**
 * @author Quan Jingyuan
 * @since 2022/3/8
 **/
public class DciOcmTransform implements PortAndTransceiverAndOtdrAndOpticalChannelTypeTransform, PlatformTypeTransform {
    public GetOcmConfigOutput devToGetOchConfigOutputApi(ChannelMonitors monitors, Set<String> names) {
        if (monitors == null || monitors.getChannelMonitor() == null) {
            return null;
        }
        GetOcmConfigOutputBuilder builder = new GetOcmConfigOutputBuilder();
        Map<ChannelMonitorKey, ChannelMonitor> values = new HashMap<>(monitors.getChannelMonitor().size());
        for (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.channel.monitor.rev200630.channel.monitor.top.channel.monitors.ChannelMonitor monitor : monitors.getChannelMonitor().values()) {
            if(names!=null&&names.size()>0&&!names.contains(monitor.getName())){
                continue;
            }
            ChannelMonitorBuilder channelMonitorBuilder = new ChannelMonitorBuilder();
            channelMonitorBuilder.setName(monitor.getName());

            if (monitor.getChannels() != null && monitor.getChannels().getChannel().size() != 0) {
                Map<ChannelKey, Channel> channelMap = new HashMap<>(monitor.getChannels().getChannel().size());
                for (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.channel.monitor.rev200630.media.channel.spectrum.power.top.channels.Channel channel : monitor.getChannels().getChannel().values()) {
                    org.opendaylight.yang.gen.v1.com.optel.dci.yang.channel.monitor.rev220208.media.channel.spectrum.power.top.ChannelBuilder channelBuilder = new ChannelBuilder();
                    FrequencyType lower = new FrequencyType(channel.getLowerFrequency().getValue());
                    FrequencyType upper = new FrequencyType(channel.getUpperFrequency().getValue());
                    channelBuilder.setLowerFrequency(lower);
                    channelBuilder.setUpperFrequency(upper);
                    if(channel.getState()!=null){
                        channelBuilder.setOsnr(channel.getState().getOsnr());
                        channelBuilder.setPower(channel.getState().getPower());
                    }
                    channelMap.put(new ChannelKey(lower, upper), channelBuilder.build());
                }
                channelMonitorBuilder.setChannel(channelMap);
            }
            if(monitor.getState()!=null){
                channelMonitorBuilder.setActiveLocalPort(monitor.getState().getActiveLocalPort());
                channelMonitorBuilder.setMonitorPort(monitor.getState().getMonitorPort());
                if(monitor.getState().getLowerFrequency()!=null){
                    channelMonitorBuilder.setLowerFrequency(new FrequencyType(monitor.getState().getLowerFrequency().getValue()));
                }
                if(monitor.getState().getUpperFrequency()!=null){
                    channelMonitorBuilder.setUpperFrequency(new FrequencyType(monitor.getState().getUpperFrequency().getValue()));
                }
                channelMonitorBuilder.setChannelInterval(devChannelIntervalTypeToApi(monitor.getState().getChannelInterval()));
                //有state才添加
                values.put(new ChannelMonitorKey(monitor.getName()), channelMonitorBuilder.build());
            }
        }
        builder.setChannelMonitor(values);
        return builder.build();

    }


    public Config apiToConfigDev(SetOcmConfigInput input){
        ConfigBuilder configBuilder=new ConfigBuilder();
        configBuilder.setActiveLocalPort(input.getActiveLocalPort());
        configBuilder.setChannelInterval(apiChannelIntervalTypeToDev(input.getChannelInterval()));
        if(Objects.nonNull(input.getLowerFrequency())&&Objects.nonNull(input.getLowerFrequency().getValue())){
            configBuilder.setLowerFrequency(new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev200630.FrequencyType(input.getLowerFrequency().getValue()));
        }
        configBuilder.setMonitorPort(input.getMonitorPort());
        configBuilder.setName(input.getName());
        if(Objects.nonNull(input.getUpperFrequency())&&Objects.nonNull(input.getUpperFrequency().getValue())){
            configBuilder.setUpperFrequency(new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev200630.FrequencyType(input.getUpperFrequency().getValue()));
        }
        return configBuilder.build();
    }
}
