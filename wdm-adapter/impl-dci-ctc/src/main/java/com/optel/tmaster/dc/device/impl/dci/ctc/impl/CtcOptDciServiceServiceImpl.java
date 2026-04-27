/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciServiceServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.service.ServiceTransformImpl;
import com.optel.tmaster.dc.device.impl.dci.ctc.util.DciUtils;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.service.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mux.rev200630.frequency.config.FrequencyChannel;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mux.rev200630.frequency.config.FrequencyChannelKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.Tff;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.Wss;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.tff.Config;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.Components;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.Component;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.ComponentKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.tff.rev200630.Config1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.tff.rev200630.passthrough.adddrop.channels.config.AddDropChannels;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.tff.rev200630.passthrough.adddrop.channels.config.PassThroughChannels;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.OpenconfigRpcService;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.terminal.device.top.TerminalDevice;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.terminal.logical.channel.top.LogicalChannels;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.terminal.logical.channel.top.logical.channels.Channel;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.terminal.logical.channel.top.logical.channels.ChannelKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.terminal.operational.mode.top.OperationalModes;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * CtcOptDciServiceServiceImpl
 * DCI 业务配置
 * date       time        author
 * ─────────────────────────────
 * 2022/2/14   17:18      liwenxue
 * Copyright (c) 2022, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class CtcOptDciServiceServiceImpl extends BaseDciServiceServiceImpl implements IDeviceServiceWdmCtc {
    private static final Logger log = LoggerFactory.getLogger(CtcOptDciServiceServiceImpl.class);

    @Override
    public ListenableFuture<RpcResult<SetTffConfigOutput>> setTffConfig(SetTffConfigInput input) {
        @NonNull InstanceIdentifier<Config1> identifier = create(Components.class).child(Component.class, new ComponentKey(input.getName())).child(Tff.class).child(Config.class).augmentation(Config1.class);
        MountTools.doMergeToConfig(input.getNeId(), identifier, new ServiceTransformImpl().apiToConfig1Dev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<SetPassThroughChannelOutput>> setPassThroughChannel(SetPassThroughChannelInput input) {
        @NonNull KeyedInstanceIdentifier<FrequencyChannel, FrequencyChannelKey> child = create(Components.class)
                .child(Component.class, new ComponentKey(input.getName()))
                .child(Tff.class).child(Config.class).augmentation(Config1.class)
                .child(PassThroughChannels.class)
                .child(FrequencyChannel.class, new FrequencyChannelKey(input.getIndex()));
        MountTools.doMergeToConfig(input.getNeId(), child, new ServiceTransformImpl().apiToFrequencyChannelDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<SetAddDropChannelOutput>> setAddDropChannel(SetAddDropChannelInput input) {
        @NonNull KeyedInstanceIdentifier<FrequencyChannel, FrequencyChannelKey> child = create(Components.class)
                .child(Component.class, new ComponentKey(input.getName()))
                .child(Tff.class).child(Config.class).augmentation(Config1.class)
                .child(AddDropChannels.class)
                .child(FrequencyChannel.class, new FrequencyChannelKey(input.getIndex()));
        MountTools.doMergeToConfig(input.getNeId(), child, new ServiceTransformImpl().apiToFrequencyChannelDev(input));
        return RpcResultUtil.success();
    }


    @Override
    public ListenableFuture<RpcResult<SetWssFrequencyChannelsOutput>> setWssFrequencyChannels(SetWssFrequencyChannelsInput input) {
        @NonNull KeyedInstanceIdentifier<FrequencyChannel, FrequencyChannelKey> child = create(Components.class)
                .child(Component.class, new ComponentKey(input.getName()))
                .child(Wss.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.wss.Config.class).augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.wss.rev200630.Config1.class)
                .child(FrequencyChannel.class, new FrequencyChannelKey(input.getIndex()));
        MountTools.doMergeToConfig(input.getNeId(), child, new ServiceTransformImpl().apiToFrequencyChannelDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<BatchSetWssFrequencyChannelsOutput>> batchSetWssFrequencyChannels(BatchSetWssFrequencyChannelsInput input) {
        @NonNull InstanceIdentifier<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.wss.rev200630.Config1> child = create(Components.class)
                .child(Component.class, new ComponentKey(input.getName()))
                .child(Wss.class)
                .child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.wss.Config.class)
                .augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.wss.rev200630.Config1.class);

        MountTools.doMergeToConfig(input.getNeId(), child, new ServiceTransformImpl().apiToFrequencyChannelsDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<DeleteWssFrequencyChannelsOutput>> deleteWssFrequencyChannels(DeleteWssFrequencyChannelsInput input) {
        @NonNull KeyedInstanceIdentifier<FrequencyChannel, FrequencyChannelKey> child = create(Components.class)
                .child(Component.class, new ComponentKey(input.getName()))
                .child(Wss.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.wss.Config.class).augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.wss.rev200630.Config1.class)
                .child(FrequencyChannel.class, new FrequencyChannelKey(input.getIndex()));
        MountTools.deleteFromConfig(input.getNeId(), child);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetOdukDelayOutput>> getOdukDelay(GetOdukDelayInput input) {
        OpenconfigRpcService rpcService = MountTools.getRpcService(input.getNeId(), OpenconfigRpcService.class);
        ServiceTransformImpl serviceTransform = new ServiceTransformImpl();
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetOdukDelayOutput>> odukDelay = rpcService.getOdukDelay(serviceTransform.apiToGetOdukDelayInputDev(input));
        return RpcResultUtil.buildFutureResult(odukDelay, serviceTransform::devToGetOdukDelayOutputApi, e -> DciUtils.getOcRpcResult(e.getGetDelayResult()));
    }

    @Override
    public ListenableFuture<RpcResult<GetTerminalDeviceOutput>> getTerminalDevice(GetTerminalDeviceInput input) {
        InstanceIdentifier<TerminalDevice> identifier = create(TerminalDevice.class);
        TerminalDevice terminalDevice = MountTools.queryFromOperational(input.getNeId(), identifier);
        try {
//            log.info("device CtcOptDciServiceServiceImpl, {}", JSON.toJSONString(terminalDevice));
        } catch (Exception e) {
            log.error("print device CtcOptDciServiceServiceImpl log error", e);
        }
        return RpcResultUtil.success(new ServiceTransformImpl().devTerminalDeviceToApi(terminalDevice));
    }

    @Override
    public ListenableFuture<RpcResult<GetLogicalChannelsOutput>> getLogicalChannels(GetLogicalChannelsInput input) {
        @NonNull InstanceIdentifier<LogicalChannels> identifier = create(TerminalDevice.class).child(LogicalChannels.class);
        LogicalChannels logicalChannels = MountTools.queryFromOperational(input.getNeId(), identifier);
        return RpcResultUtil.success(new ServiceTransformImpl().devToGetLogicalChannelsOutputApi(logicalChannels, input.getIndex()));
    }

    @Override
    public ListenableFuture<RpcResult<SetLogicalChannelOutput>> setLogicalChannel(SetLogicalChannelInput input) {
        @NonNull InstanceIdentifier<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.terminal.logical.channel.top.logical.channels.Channel> child = create(TerminalDevice.class)
                .child(LogicalChannels.class)
                .child(Channel.class, new ChannelKey(input.getIndex()));
        MountTools.doMergeToConfig(input.getNeId(), child, new ServiceTransformImpl().apiToChannelDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetOperationalModesOutput>> getOperationalModes(GetOperationalModesInput input) {
        @NonNull InstanceIdentifier<OperationalModes> identifier = create(TerminalDevice.class).child(OperationalModes.class);
        OperationalModes operationalModes = MountTools.queryFromOperational(input.getNeId(), identifier);
        return RpcResultUtil.success(new ServiceTransformImpl().devToGetOperationalModesOutputApi(operationalModes, input.getModeId()));
    }

    @Override
    public ListenableFuture<RpcResult<GetEthernetProtocolOutput>> getEthernetProtocol(GetEthernetProtocolInput input) {
        @NonNull KeyedInstanceIdentifier<Channel, ChannelKey> child = create(TerminalDevice.class).child(LogicalChannels.class).child(Channel.class, new ChannelKey(input.getIndex()));
        Channel channel = MountTools.queryFromOperational(input.getNeId(), child);
        return RpcResultUtil.success(new ServiceTransformImpl().devToGetEthernetProtocolOutputApi(channel));
    }

    @Override
    public ListenableFuture<RpcResult<GetOtnProtocolOutput>> getOtnProtocol(GetOtnProtocolInput input) {
        @NonNull KeyedInstanceIdentifier<Channel, ChannelKey> child = create(TerminalDevice.class).child(LogicalChannels.class).child(Channel.class, new ChannelKey(input.getIndex()));
        Channel channel = MountTools.queryFromOperational(input.getNeId(), child);
        return RpcResultUtil.success(new ServiceTransformImpl().devToGetOtnProtocolOutputApi(channel));
    }

    /**
     * Invoke {@code batch-set-logical-channel} RPC.
     *
     * @param input of {@code batch-set-logical-channel}
     * @return output of {@code batch-set-logical-channel}
     */
    @Override
    public ListenableFuture<RpcResult<BatchSetLogicalChannelOutput>> batchSetLogicalChannel(BatchSetLogicalChannelInput input) {
        @NonNull InstanceIdentifier<LogicalChannels> child = create(TerminalDevice.class).child(LogicalChannels.class);
        MountTools.doMergeToConfig(input.getNeId(), child, new ServiceTransformImpl().apiToLogicalChannels(input));
        return RpcResultUtil.success();
    }
}
