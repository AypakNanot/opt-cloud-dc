/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.transform.common.resource;

import com.optel.tmaster.dc.device.impl.dci.ctc.transform.base.PlatformTypeTransform;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.mux.rev220208.frequency.state.FrequencyChannel;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.mux.rev220208.frequency.state.FrequencyChannelBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.mux.rev220208.frequency.state.FrequencyChannelKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.Tff;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.TffBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.Wss;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.WssBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.tff.rev220208.passthrough.adddrop.channels.state.AddDropChannelsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.tff.rev220208.passthrough.adddrop.channels.state.PassThroughChannelsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.FrequencyType;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.tff.rev200630.passthrough.adddrop.channels.state.AddDropChannels;

import java.util.HashMap;
import java.util.Map;

/**
 * TffTransformImpl
 *
 * date       time        author
 * ─────────────────────────────
 * 2022/4/27   16:31      liwenxue
 * Copyright (c) 2022, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class TffWssTransformImpl implements PlatformTypeTransform {

    public Tff devTffToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.Tff tff ){
        if(tff == null || tff.getState() == null){
            return null;
        }
        TffBuilder tffBuilder = new TffBuilder();
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.tff.rev200630.@Nullable State1 state1 =
                tff.getState().augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.tff.rev200630.State1.class);
        if(state1 == null){
            return null;
        }
        tffBuilder.setActualVoaAttenuation(state1.getActualVoaAttenuation());
        tffBuilder.setAddDropChannelNum(state1.getAddDropChannelNum());
        tffBuilder.setTargetVoaAttenuation(state1.getTargetVoaAttenuation());
        tffBuilder.setChannelInterval(devToChannelIntervalTypeApi(state1.getChannelInterval()));
        if (state1.getAddDropChannels() != null && state1.getAddDropChannels().getFrequencyChannel() != null) {
            AddDropChannelsBuilder addDropChannelsBuilder = new AddDropChannelsBuilder();
            addDropChannelsBuilder.setFrequencyChannel(devToFrequencyChannelMapApi(state1.getAddDropChannels().getFrequencyChannel()));
            tffBuilder.setAddDropChannels(addDropChannelsBuilder.build());
        }
        if (state1.getPassThroughChannels() != null && state1.getPassThroughChannels().getFrequencyChannel() != null) {
            PassThroughChannelsBuilder passThroughChannelsBuilder = new PassThroughChannelsBuilder();
            passThroughChannelsBuilder.setFrequencyChannel(devToFrequencyChannelMapApi(state1.getPassThroughChannels().getFrequencyChannel()));
            tffBuilder.setPassThroughChannels(passThroughChannelsBuilder.build());
        }
        return tffBuilder.build();
    }

    private Map<FrequencyChannelKey, FrequencyChannel> devToFrequencyChannelMapApi(Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mux.rev200630.frequency.state.FrequencyChannelKey,
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mux.rev200630.frequency.state.FrequencyChannel> channels) {
        if(channels == null){
            return null;
        }
        Map<FrequencyChannelKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.mux.rev220208.frequency.state.FrequencyChannel> channelMap = new HashMap<>(channels.size());
        for (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mux.rev200630.frequency.state.FrequencyChannel channel : channels.values()) {
            FrequencyChannelBuilder channelBuilder = new FrequencyChannelBuilder();
            channelBuilder.setIndex(channel.getIndex());
            channelBuilder.setActualAddVoaAttenuation(channel.getActualAddVoaAttenuation());
            channelBuilder.setActualDropVoaAttenuation(channel.getActualDropVoaAttenuation());
            channelBuilder.setAdPort(channel.getAdPort());
            channelBuilder.setLinePort(channel.getLinePort());
            channelBuilder.setMaxEdgeFreq(channel.getMaxEdgeFreq());
            channelBuilder.setMinEdgeFreq(channel.getMinEdgeFreq());
            channelBuilder.setTargetAddVoaAttenuation(channel.getTargetAddVoaAttenuation());
            channelBuilder.setTargetAddVoaAttenuation(channel.getTargetAddVoaAttenuation());
            channelBuilder.setTargetDropVoaAttenuation(channel.getTargetDropVoaAttenuation());
            channelMap.put(new FrequencyChannelKey(channel.getIndex()), channelBuilder.build());
        }
        return channelMap;
    }

    public Wss devWssToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.Wss wss){
        if(wss == null || wss.getState() == null){
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.wss.rev200630.@Nullable State1 state1 = wss.getState().augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.wss.rev200630.State1.class);
        if(state1 == null){
            return null;
        }
        WssBuilder wssBuilder = new WssBuilder();
        wssBuilder.setFrequencyChannel(devToFrequencyChannelMapApi(state1.getFrequencyChannel()));
        if(state1.getLowerFrequency() != null){
            wssBuilder.setLowerFrequency(new FrequencyType(state1.getLowerFrequency().getValue()));
        }
        wssBuilder.setMaxDegree(state1.getMaxDegree());
        wssBuilder.setMinFlexibleGrid(state1.getMinFlexibleGrid());
        if(state1.getUpperFrequency() != null){
            wssBuilder.setUpperFrequency(new FrequencyType(state1.getUpperFrequency().getValue()));
        }
        return wssBuilder.build();
    }

}
