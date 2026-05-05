/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.impl;

import com.alibaba.fastjson.JSON;
import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciServiceServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.service.ServiceTransformImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.util.DciUtils;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.service.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.device.top.TerminalDevice;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.logical.channel.top.LogicalChannels;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.logical.channel.top.logical.channels.Channel;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.logical.channel.top.logical.channels.ChannelKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.operational.mode.top.OperationalModes;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.MiniotnRpcService;
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
public class CmccOptDciServiceServiceImpl extends BaseDciServiceServiceImpl implements IDeviceServiceWdmCmcc {
    private static final Logger log = LoggerFactory.getLogger(CmccOptDciServiceServiceImpl.class);

    private final static String IS_OPT_TEST = "isOptTest";
    private final static String FALSE_STR = "false";

    @Override
    public ListenableFuture<RpcResult<SetTffConfigOutput>> setTffConfig(SetTffConfigInput input) {
        //极简OTN 没有TFF
//        @NonNull InstanceIdentifier<Config1> identifier = create(Components.class).child(Component.class, new ComponentKey(input.getName())).child(Tff.class).child(Config.class).augmentation(Config1.class);
//        MountTools.doMergeToConfig(input.getNeId(),identifier,new ServiceTransformImpl().apiToConfig1Dev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<SetPassThroughChannelOutput>> setPassThroughChannel(SetPassThroughChannelInput input) {
        //极简OTN 没有TFF
//        @NonNull KeyedInstanceIdentifier<FrequencyChannel, FrequencyChannelKey> child = create(Components.class)
//                .child(Component.class, new ComponentKey(input.getName()))
//                .child(Tff.class).child(Config.class).augmentation(Config1.class)
//                .child(PassThroughChannels.class)
//                .child(FrequencyChannel.class, new FrequencyChannelKey(input.getIndex()));
//        MountTools.doMergeToConfig(input.getNeId(),child,new ServiceTransformImpl().apiToFrequencyChannelDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<SetAddDropChannelOutput>> setAddDropChannel(SetAddDropChannelInput input) {
        //极简OTN 没有TFF
//        @NonNull KeyedInstanceIdentifier<FrequencyChannel, FrequencyChannelKey> child = create(Components.class)
//                .child(Component.class, new ComponentKey(input.getName()))
//                .child(Tff.class).child(Config.class).augmentation(Config1.class)
//                .child(AddDropChannels.class)
//                .child(FrequencyChannel.class, new FrequencyChannelKey(input.getIndex()));
//        MountTools.doMergeToConfig(input.getNeId(),child,new ServiceTransformImpl().apiToFrequencyChannelDev(input));
        return RpcResultUtil.success();
    }


    @Override
    public ListenableFuture<RpcResult<SetWssFrequencyChannelsOutput>> setWssFrequencyChannels(SetWssFrequencyChannelsInput input) {
        //极简OTN 没有 wss
        //        @NonNull KeyedInstanceIdentifier<FrequencyChannel, FrequencyChannelKey> child = create(Components.class)
//                .child(Component.class, new ComponentKey(input.getName()))
//                .child(Wss.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev230426.platform.anchors.top.wss.Config.class).augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.wss.rev200630.Config1.class)
//                .child(FrequencyChannel.class, new FrequencyChannelKey(input.getIndex()));
//        MountTools.doMergeToConfig(input.getNeId(),child,new ServiceTransformImpl().apiToFrequencyChannelDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<BatchSetWssFrequencyChannelsOutput>> batchSetWssFrequencyChannels(BatchSetWssFrequencyChannelsInput input) {
        //极简OTN 没有 wss
        //        @NonNull InstanceIdentifier<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.wss.rev200630.Config1> child = create(Components.class)
//                .child(Component.class, new ComponentKey(input.getName()))
//                .child(Wss.class)
//                .child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.wss.Config.class)
//                .augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.wss.rev200630.Config1.class);
//
//        MountTools.doMergeToConfig(input.getNeId(), child, new ServiceTransformImpl().apiToFrequencyChannelsDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<DeleteWssFrequencyChannelsOutput>> deleteWssFrequencyChannels(DeleteWssFrequencyChannelsInput input) {
        //极简OTN 没有 wss
        //        @NonNull KeyedInstanceIdentifier<FrequencyChannel, FrequencyChannelKey> child = create(Components.class)
//                .child(Component.class, new ComponentKey(input.getName()))
//                .child(Wss.class).child(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.wss.Config.class).augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.wss.rev200630.Config1.class)
//                .child(FrequencyChannel.class, new FrequencyChannelKey(input.getIndex()));
//        MountTools.deleteFromConfig(input.getNeId(), child);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetOdukDelayOutput>> getOdukDelay(GetOdukDelayInput input) {
        MiniotnRpcService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnRpcService.class);
        ServiceTransformImpl serviceTransform = new ServiceTransformImpl();
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.GetOdukDelayOutput>> odukDelay = rpcService.getOdukDelay(serviceTransform.apiToGetOdukDelayInputDev(input));
        return RpcResultUtil.buildFutureResult(odukDelay, serviceTransform::devToGetOdukDelayOutputApi, e -> DciUtils.getOcRpcResult(e.getGetDelayResult()));

    }

    @Override
    public ListenableFuture<RpcResult<GetTerminalDeviceOutput>> getTerminalDevice(GetTerminalDeviceInput input) {
        String property = System.getProperty(IS_OPT_TEST, FALSE_STR);
        TerminalDevice terminalDevice = null;
        if (Boolean.valueOf(property)) {
//            terminalDevice= ComponentsTestUtil.getTerminalDevice();
        } else {
            InstanceIdentifier<TerminalDevice> identifier = create(TerminalDevice.class);
            terminalDevice = MountTools.queryFromOperational(input.getNeId(), identifier);
        }
        try {
            log.info("device CmccOptDciServiceServiceImpl, {}", JSON.toJSONString(terminalDevice));
        } catch (Exception e) {
            log.error("print device CmccOptDciServiceServiceImpl log error", e);
        }
        return RpcResultUtil.success(new ServiceTransformImpl().devTerminalDeviceToApi(terminalDevice));
    }

    @Override
    public ListenableFuture<RpcResult<GetLogicalChannelsOutput>> getLogicalChannels(GetLogicalChannelsInput input) {
        @NonNull InstanceIdentifier<LogicalChannels> identifier = create(TerminalDevice.class).child(LogicalChannels.class);
        String property = System.getProperty(IS_OPT_TEST, FALSE_STR);
        LogicalChannels logicalChannels = null;
        if (Boolean.valueOf(property)) {
//            logicalChannels= ComponentsTestUtil.getLogicalChannels();
        } else {
            logicalChannels = MountTools.queryFromOperational(input.getNeId(), identifier);
        }
        return RpcResultUtil.success(new ServiceTransformImpl().devToGetLogicalChannelsOutputApi(logicalChannels, input.getIndex()));
    }

    @Override
    public ListenableFuture<RpcResult<SetLogicalChannelOutput>> setLogicalChannel(SetLogicalChannelInput input) {
        @NonNull InstanceIdentifier<Channel> child = create(TerminalDevice.class)
                .child(LogicalChannels.class)
                .child(Channel.class, new ChannelKey(input.getIndex()));
        MountTools.doMergeToConfig(input.getNeId(), child, new ServiceTransformImpl().apiToChannelDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetOperationalModesOutput>> getOperationalModes(GetOperationalModesInput input) {
        @NonNull InstanceIdentifier<OperationalModes> identifier = create(TerminalDevice.class).child(OperationalModes.class);
        String property = System.getProperty(IS_OPT_TEST, FALSE_STR);
        OperationalModes operationalModes = null;
        if (Boolean.valueOf(property)) {
//            operationalModes= new TestData().getOperationalModes();
        } else {
            operationalModes = MountTools.queryFromOperational(input.getNeId(), identifier);
        }
        return RpcResultUtil.success(new ServiceTransformImpl().devToGetOperationalModesOutputApi(operationalModes, input.getModeId()));
    }

    @Override
    public ListenableFuture<RpcResult<GetEthernetProtocolOutput>> getEthernetProtocol(GetEthernetProtocolInput input) {
        @NonNull KeyedInstanceIdentifier<Channel, ChannelKey> child = create(TerminalDevice.class).child(LogicalChannels.class).child(Channel.class, new ChannelKey(input.getIndex()));

        String property = System.getProperty(IS_OPT_TEST, FALSE_STR);
        Channel channel = null;
        if (Boolean.valueOf(property)) {
//            channel= ComponentsTestUtil.getLogicalChannels().getChannel().values().iterator().next();
        } else {
            channel = MountTools.queryFromOperational(input.getNeId(), child);
        }

        return RpcResultUtil.success(new ServiceTransformImpl().devToGetEthernetProtocolOutputApi(channel));
    }

    @Override
    public ListenableFuture<RpcResult<GetOtnProtocolOutput>> getOtnProtocol(GetOtnProtocolInput input) {
        @NonNull KeyedInstanceIdentifier<Channel, ChannelKey> child = create(TerminalDevice.class).child(LogicalChannels.class).child(Channel.class, new ChannelKey(input.getIndex()));
        String property = System.getProperty(IS_OPT_TEST, FALSE_STR);
        Channel channel = null;
        if (Boolean.valueOf(property)) {
//            channel= ComponentsTestUtil.getLogicalChannels().getChannel().values().iterator().next();
        } else {
            channel = MountTools.queryFromOperational(input.getNeId(), child);
        }
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
