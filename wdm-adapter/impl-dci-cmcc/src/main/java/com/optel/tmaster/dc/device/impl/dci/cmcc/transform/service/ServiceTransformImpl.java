/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.service;

import cn.hutool.core.collection.CollUtil;
import com.alibaba.fastjson.JSON;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base.CommonTypeTransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base.PlatformTypeTransform;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.lldp.rev220208.lldp._interface.state.CountersBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.lldp.rev220208.lldp.custom.tlv.top.CustomTlv;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.lldp.rev220208.lldp.custom.tlv.top.CustomTlvBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.lldp.rev220208.lldp.custom.tlv.top.CustomTlvKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.mux.rev220208.FrequencyChannelsConfig;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.rpc.rev220208.get.oduk.delay.output.grouping.DelayDataValue;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.rpc.rev220208.get.oduk.delay.output.grouping.DelayDataValueBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.lldp.logical.channel.neighbor.top.Neighbors;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.lldp.logical.channel.neighbor.top.NeighborsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.lldp.logical.channel.neighbor.top.neighbors.Neighbor;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.lldp.logical.channel.neighbor.top.neighbors.NeighborBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.lldp.logical.channel.neighbor.top.neighbors.NeighborKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.lldp.logical.channel.top.LldpBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.sdh.j0.top.SdhJ0;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.logical.chan.assignment.top.LogicalChannelAssignments;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.logical.chan.assignment.top.LogicalChannelAssignmentsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.logical.chan.assignment.top.LogicalChannelAssignmentsKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.logical.channel.ingress.top.Ingress;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.logical.channel.ingress.top.IngressBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.logical.channel.top.LogicalChannel;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.logical.channel.top.LogicalChannelBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.logical.channel.top.LogicalChannelKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.operational.mode.top.OperationalModes;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.operational.mode.top.OperationalModesBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.operational.mode.top.OperationalModesKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.otn.protocol.multi.stats.*;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.otn.protocol.top.Otn;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.otn.protocol.top.OtnBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.service.rev200210.*;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.service.rev200210.batch.set.logical.channel.input.LogicalChannelConfig;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.custom.tlv.top.custom.tlvs.Tlv;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.custom.tlv.top.custom.tlvs.TlvBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.rev181121.lldp.custom.tlv.top.custom.tlvs.TlvKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.sdh.j0.top.SdhJ0Builder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.sdh.j0.top.sdh.j0.SdhJ0ConfigBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.device.top.TerminalDevice;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.logical.channel.top.LogicalChannels;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.logical.channel.top.LogicalChannelsBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.logical.channel.top.logical.channels.Channel;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.logical.channel.top.logical.channels.ChannelBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.logical.channel.top.logical.channels.ChannelKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.otn.protocol.top.otn.ConfigBuilder;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.Counter64;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime;
import org.opendaylight.yangtools.yang.common.Uint16;
import org.opendaylight.yangtools.yang.common.Uint32;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.stream.Collectors;

/**
 * ServiceTransformImpl
 * date       time        author
 * ─────────────────────────────
 * 2022/2/14   17:32      liwenxue
 * Copyright (c) 2022, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class ServiceTransformImpl implements PlatformTypeTransform, CommonTypeTransform {
    private static final Logger log = LoggerFactory.getLogger(ServiceTransformImpl.class);

   //极简OTN 没有TFF
//    public Config1 apiToConfig1Dev(SetTffConfigInput input) {
//        Config1Builder builder = new Config1Builder();
//        builder.setTargetVoaAttenuation(input.getTargetVoaAttenuation());
//        return builder.build();
//    }

    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mux.rev230426.frequency.config.FrequencyChannel apiToFrequencyChannelDev(FrequencyChannelsConfig input) {
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mux.rev230426.frequency.config.FrequencyChannelBuilder builder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mux.rev230426.frequency.config.FrequencyChannelBuilder();
        builder.setAdPort(input.getAdPort());
        builder.setIndex(input.getIndex());
        builder.setLinePort(input.getLinePort());
        builder.setMaxEdgeFreq(input.getMaxEdgeFreq());
        builder.setMinEdgeFreq(input.getMinEdgeFreq());
        builder.setTargetAddVoaAttenuation(input.getTargetAddVoaAttenuation());
        builder.setTargetDropVoaAttenuation(input.getTargetDropVoaAttenuation());
        return builder.build();
    }
// 极简OTN没有WSS
//    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.wss.rev230426.Config1 apiToFrequencyChannelsDev(BatchSetWssFrequencyChannelsInput input) {
//        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.wss.rev230426.Config1Builder builder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.wss.rev230426.Config1Builder();
//        Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mux.rev230426.frequency.config.FrequencyChannelKey, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mux.rev230426.frequency.config.FrequencyChannel> values = new HashMap<>();
//        Objects.requireNonNull(input.getFrequencyChannel()).forEach((key, frequencyChannel) -> {
//            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mux.rev230426.frequency.config.FrequencyChannelBuilder frequencyChannelBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mux.rev230426.frequency.config.FrequencyChannelBuilder();
//            frequencyChannelBuilder.setIndex(frequencyChannel.getIndex());
//            frequencyChannelBuilder.setAdPort(frequencyChannel.getAdPort());
//            frequencyChannelBuilder.setLinePort(frequencyChannel.getLinePort());
//            frequencyChannelBuilder.setMaxEdgeFreq(frequencyChannel.getMaxEdgeFreq());
//            frequencyChannelBuilder.setMinEdgeFreq(frequencyChannel.getMinEdgeFreq());
//            frequencyChannelBuilder.setTargetAddVoaAttenuation(frequencyChannel.getTargetAddVoaAttenuation());
//            frequencyChannelBuilder.setTargetDropVoaAttenuation(frequencyChannel.getTargetDropVoaAttenuation());
//            frequencyChannelBuilder.withKey(new FrequencyChannelKey(frequencyChannel.getIndex()));
//            values.put(frequencyChannelBuilder.key(), frequencyChannelBuilder.build());
//        });
//        builder.setFrequencyChannel(values);
//        return builder.build();
//    }

    public GetTerminalDeviceOutput devTerminalDeviceToApi(TerminalDevice terminalDevice){
        if(terminalDevice == null){
            return null;
        }
        GetTerminalDeviceOutputBuilder outputBuilder = new GetTerminalDeviceOutputBuilder();
        outputBuilder.setOperationalModes(devToOperationalModesApi(terminalDevice.getOperationalModes(), null));
        if(terminalDevice.getLogicalChannels() != null && terminalDevice.getLogicalChannels().getChannel()!= null){
            outputBuilder.setLogicalChannel(devToChannelsApiMap(terminalDevice.getLogicalChannels().getChannel().values()));
        }
        try {
            log.info("cmcc devTerminalDeviceToApi ServiceTransformImpl, {}", JSON.toJSONString(outputBuilder));
        } catch (Exception e) {
            log.error("print cmcc devTerminalDeviceToApi ServiceTransformImpl log error", e);
        }
        return outputBuilder.build();
    }


    public GetLogicalChannelsOutput devToGetLogicalChannelsOutputApi(LogicalChannels logicalChannels, Set<Uint32> indexes) {
        GetLogicalChannelsOutputBuilder builder = new GetLogicalChannelsOutputBuilder();
        if (logicalChannels != null && logicalChannels.getChannel() != null) {
            if(CollUtil.isNotEmpty(indexes)){
                List<Channel> collect = logicalChannels.getChannel().values().stream().filter(e -> indexes.contains(e.getIndex())).collect(Collectors.toList());
                builder.setLogicalChannel(devToChannelsApiMap(collect));
            }else{
                builder.setLogicalChannel(devToChannelsApiMap(logicalChannels.getChannel().values()));
            }
        }
        return builder.build();
    }

    public GetOperationalModesOutput devToGetOperationalModesOutputApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.operational.mode.top.OperationalModes operationalModes, Set<Uint16> modeIds){
        if(operationalModes == null){
            return null;
        }
        GetOperationalModesOutputBuilder outputBuilder = new GetOperationalModesOutputBuilder();
        outputBuilder.setOperationalModes(devToOperationalModesApi(operationalModes, modeIds));
        return outputBuilder.build();
    }

    public Map<OperationalModesKey, OperationalModes> devToOperationalModesApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.operational.mode.top.OperationalModes operationalModes, Set<Uint16> modeIds) {
        if(operationalModes == null || operationalModes.getMode() == null){
            return null;
        }
        Map<OperationalModesKey, OperationalModes> result = new HashMap<>(operationalModes.getMode().size());
        for (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.operational.mode.top.operational.modes.Mode mode : operationalModes.getMode().values()) {
            if(CollUtil.isEmpty(modeIds) || modeIds.contains(mode.getModeId())){
                OperationalModes operationalMode = devToModeApi(mode);
                if(operationalMode != null){
                    result.put(operationalMode.key(), operationalMode);
                }
            }
        }
        return result;
    }

    private OperationalModes devToModeApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.operational.mode.top.operational.modes.Mode mode) {
        if (mode == null || mode.getState() == null) {
            return null;
        }
        OperationalModesBuilder builder = new OperationalModesBuilder();
        builder.setDescription(mode.getState().getDescription());
        builder.setModeId(mode.getModeId());
        builder.setVendorId(mode.getState().getVendorId());
        builder.withKey(new OperationalModesKey(mode.getModeId()));
        return builder.build();
    }

    public Ingress devChannelIngressToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.logical.channel.ingress.top.Ingress ingress) {
        if (ingress == null || ingress.getState() == null) {
            return null;
        }
        IngressBuilder builder = new IngressBuilder();
        builder.setIngressPort(ingress.getState().getIngressPort());
        builder.setPhysicalChannel(ingress.getState().getPhysicalChannel());
        return builder.build();
    }
    public Channel apiToChannelDev(SetLogicalChannelInput input){
        ChannelBuilder channelBuilder = new ChannelBuilder();

        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.logical.channel.top.logical.channels.channel.ConfigBuilder channelConfigBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.logical.channel.top.logical.channels.channel.ConfigBuilder();
        channelConfigBuilder.setAdminState(apiAdminStateTypeToDev(input.getAdminState()));
        channelConfigBuilder.setDelayTestMode(apiToDelayTestModeDev(input.getDelayTestMode()));
        channelConfigBuilder.setDescription(input.getDescription());
        channelConfigBuilder.setIndex(input.getIndex());
        channelConfigBuilder.setLogicalChannelType(apiToLogicalChannelTypeDev(input.getLogicalChannelType()));
        channelConfigBuilder.setLoopbackMode(apiToLoopbackModeTypeDev(input.getLoopbackMode()));
        channelConfigBuilder.setRateClass(apiToRateClassDev(input.getRateClass()));
        channelConfigBuilder.setTestSignal(input.getTestSignal());
        channelConfigBuilder.setTribProtocol(apiToTribProtocolDev(input.getTribProtocol()));
        channelBuilder.setIndex(input.getIndex());
        channelBuilder.setConfig(channelConfigBuilder.build());

        Otn otn = input.getOtn();
        if (Objects.nonNull(otn)) {
            // 电层OTU业务通道TTI开销配置
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.otn.protocol.top.OtnBuilder otnBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.otn.protocol.top.OtnBuilder();
            ConfigBuilder otnConfigBuilder = new ConfigBuilder();
            otnConfigBuilder.setTtiMsgTransmit(input.getOtn().getTtiMsgTransmit());
            otnConfigBuilder.setTtiMsgExpected(input.getOtn().getTtiMsgExpected());
            otnBuilder.setConfig(otnConfigBuilder.build());
            channelBuilder.setOtn(otnBuilder.build());
        }

        SdhJ0 sdhJ0 = input.getSdhJ0();
        if (Objects.nonNull(sdhJ0)) {
            // SDH 逻辑通道 j0 配置
            SdhJ0Builder sdhJ0Builder = new SdhJ0Builder();
            SdhJ0ConfigBuilder sdhJ0ConfigBuilder = new SdhJ0ConfigBuilder();
            sdhJ0ConfigBuilder.setJ0ExpectedRx(sdhJ0.getJ0ExpectedRx());
            sdhJ0Builder.setSdhJ0Config(sdhJ0ConfigBuilder.build());
            channelBuilder.setSdhJ0(sdhJ0Builder.build());
        }
        return channelBuilder.build();
    }
    private Map<LogicalChannelAssignmentsKey, LogicalChannelAssignments> devLogicalAssignmentToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.logical.chan.assignment.top.LogicalChannelAssignments assignments) {
        if (assignments == null || assignments.getAssignment() == null) {
            return null;
        }
        Map<LogicalChannelAssignmentsKey, LogicalChannelAssignments>values = new HashMap<>(assignments.getAssignment().size());
        for (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.logical.chan.assignment.top.logical.channel.assignments.Assignment assignment : assignments.getAssignment().values()) {
            if (assignment.getState() != null) {
                LogicalChannelAssignmentsBuilder builder = new LogicalChannelAssignmentsBuilder();
                builder.withKey(new LogicalChannelAssignmentsKey(assignment.getIndex()));
                builder.setAllocation(assignment.getState().getAllocation());
                builder.setAssignmentType(devToAssignmentTypeApi(assignment.getState().getAssignmentType()));
                builder.setDescription(assignment.getState().getDescription());
                builder.setIndex(assignment.getState().getIndex());
                builder.setLogicalChannel(assignment.getState().getLogicalChannel());
                builder.setMapping(devToMappingApi(assignment.getState().getMapping()));
                builder.setOpticalChannel(assignment.getState().getOpticalChannel());
                builder.setTributarySlotIndex(assignment.getState().getTributarySlotIndex());
                values.put(builder.key(), builder.build());
            }
        }
        return values;
    }

    public GetOtnProtocolOutput devToGetOtnProtocolOutputApi(Channel channel) {
        if (channel == null || channel.getOtn() == null || channel.getOtn().getState() == null) {
            return null;
        }
        GetOtnProtocolOutputBuilder builder = new GetOtnProtocolOutputBuilder();
        if(channel.getOtn().getState().getBbe()!=null){
            builder.setBbe(new Counter64(channel.getOtn().getState().getBbe().getValue()));
        }
        if(channel.getOtn().getState().getBber()!=null){
            builder.setBber(new Counter64(channel.getOtn().getState().getBber().getValue()));
        }
        if(channel.getOtn().getState().getEs()!=null){
            builder.setEs(new Counter64(channel.getOtn().getState().getEs().getValue()));

        }
        builder.setEsnr(devToEsnrApi(channel.getOtn().getState().getEsnr()));
        if(channel.getOtn().getState().getFebbe()!=null){
            builder.setFebbe(new Counter64(channel.getOtn().getState().getFebbe().getValue()));

        }
        if(channel.getOtn().getState().getFebber()!=null){
            builder.setFebber(new Counter64(channel.getOtn().getState().getFebber().getValue()));

        }
        if(channel.getOtn().getState().getFees()!=null){
            builder.setFees(new Counter64(channel.getOtn().getState().getFees().getValue()));

        }
        if(channel.getOtn().getState().getFeses()!=null){
            builder.setFeses(new Counter64(channel.getOtn().getState().getFeses().getValue()));

        }
        if(channel.getOtn().getState().getFesesr()!=null){
            builder.setFesesr(new Counter64(channel.getOtn().getState().getFesesr().getValue()));

        }
        if(channel.getOtn().getState().getFeuas()!=null){
            builder.setFeuas(new Counter64(channel.getOtn().getState().getFeuas().getValue()));

        }
        builder.setPostFecBer(devToPostFecBerApi(channel.getOtn().getState().getPostFecBer()));
        builder.setPreFecBer(devToPreFecBerApi(channel.getOtn().getState().getPreFecBer()));
        builder.setOpticalSignalToNoiseRatio(devToOpticalSignalToNoiseRatioToApi(channel.getOtn().getState().getOpticalSignalToNoiseRatio()));
        builder.setQValue(devToQValueApi(channel.getOtn().getState().getQValue()));
        builder.setRdiMsg(channel.getOtn().getState().getRdiMsg());
        if(channel.getOtn().getState().getSes()!=null){
            builder.setSes(new Counter64(channel.getOtn().getState().getSes().getValue()));

        }
        if(channel.getOtn().getState().getSesr()!=null){

            builder.setSesr(new Counter64(channel.getOtn().getState().getSesr().getValue()));
        }

        builder.setTributarySlotGranularity(devToTributarySlotGranularityApi(channel.getOtn().getState().getTributarySlotGranularity()));
        builder.setTtiMsgAuto(channel.getOtn().getState().getTtiMsgAuto());
        builder.setTtiMsgExpected(channel.getOtn().getState().getTtiMsgExpected());
        builder.setTtiMsgRecv(channel.getOtn().getState().getTtiMsgRecv());
        builder.setTtiMsgTransmit(channel.getOtn().getState().getTtiMsgTransmit());
        if(channel.getOtn().getState().getUas()!=null){
            builder.setUas(new Counter64(channel.getOtn().getState().getUas().getValue()));
        }

        builder.setTtiMsgTransmit(channel.getOtn().getState().getTtiMsgTransmit());
        builder.setTtiMsgExpected(channel.getOtn().getState().getTtiMsgExpected());
        builder.setTtiMsgRecv(channel.getOtn().getState().getTtiMsgRecv());
        return builder.build();
    }

    private QValue devToQValueApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.otn.protocol.multi.stats.QValue qValue) {
        if(qValue == null){
            return null;
        }
        QValueBuilder builder = new QValueBuilder();
        builder.setInterval(devStatIntervalToApi(qValue.getInterval()));
        builder.setMax(qValue.getMax());
        builder.setMinTime(devTimeticks64ToApi(qValue.getMinTime()));
        builder.setAvg(qValue.getAvg());
        builder.setInstant(qValue.getInstant());
        builder.setMaxTime(devTimeticks64ToApi(qValue.getMaxTime()));
        builder.setMin(qValue.getMin());
        return builder.build();

    }

    private PreFecBer devToPreFecBerApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.otn.protocol.multi.stats.PreFecBer preFecBer) {
        if(preFecBer == null){
            return null;
        }
        PreFecBerBuilder builder = new PreFecBerBuilder();
        builder.setMinTime(devTimeticks64ToApi(preFecBer.getMinTime()));
        builder.setAvg(preFecBer.getAvg());
        builder.setInstant(preFecBer.getInstant());
        builder.setMaxTime(devTimeticks64ToApi(preFecBer.getMaxTime()));
        builder.setMin(preFecBer.getMin());
        builder.setInterval(devStatIntervalToApi(preFecBer.getInterval()));
        builder.setMax(preFecBer.getMax());
        return builder.build();
    }

    private PostFecBer devToPostFecBerApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.otn.protocol.multi.stats.PostFecBer postFecBer) {
        if(postFecBer == null){
            return null;
        }
        PostFecBerBuilder builder = new PostFecBerBuilder();
        builder.setMaxTime(devTimeticks64ToApi(postFecBer.getMaxTime()));
        builder.setMin(postFecBer.getMin());
        builder.setMinTime(devTimeticks64ToApi(postFecBer.getMinTime()));
        builder.setAvg(postFecBer.getAvg());
        builder.setInstant(postFecBer.getInstant());
        builder.setInterval(devStatIntervalToApi(postFecBer.getInterval()));
        builder.setMax(postFecBer.getMax());
        return builder.build();
    }

    private Esnr devToEsnrApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.otn.protocol.multi.stats.Esnr esnr) {
        if(esnr == null){
            return null;
        }
        EsnrBuilder builder = new EsnrBuilder();
        builder.setAvg(esnr.getAvg());
        builder.setInstant(esnr.getInstant());
        builder.setInterval(devStatIntervalToApi(esnr.getInterval()));
        builder.setMax(esnr.getMax());
        builder.setMaxTime(devTimeticks64ToApi(esnr.getMaxTime()));
        builder.setMin(esnr.getMin());
        builder.setMinTime(devTimeticks64ToApi(esnr.getMinTime()));
        return builder.build();
    }

    public GetEthernetProtocolOutput devToGetEthernetProtocolOutputApi(Channel channel) {
        if (channel == null || channel.getEthernet() == null || channel.getEthernet().getState() == null) {
            return null;
        }
        GetEthernetProtocolOutputBuilder builder = new GetEthernetProtocolOutputBuilder();
//        builder.setAlsDelay(channel.getEthernet().getState().getAlsDelay());
        builder.setClientAls(devToClientAlsApi(channel.getEthernet().getState().getClientAls()));
        builder.setClientFec(devToClientFecApi(channel.getEthernet().getState().getClientFec()));
        builder.setInPcsBipErrors(devCounter64ToApi(channel.getEthernet().getState().getInPcsBipErrors()));
        builder.setInPcsErroredSeconds(devCounter64ToApi(channel.getEthernet().getState().getInPcsErroredSeconds()));
        builder.setInPcsSeverelyErroredSeconds(devCounter64ToApi(channel.getEthernet().getState().getInPcsSeverelyErroredSeconds()));
        builder.setInPcsUnavailableSeconds(devCounter64ToApi(channel.getEthernet().getState().getInPcsUnavailableSeconds()));
        builder.setOutBlockErrors(devCounter64ToApi(channel.getEthernet().getState().getOutBlockErrors()));
        builder.setOutCrcErrors(devCounter64ToApi(channel.getEthernet().getState().getOutCrcErrors()));
        builder.setOutPcsBipErrors(devCounter64ToApi(channel.getEthernet().getState().getOutPcsBipErrors()));
        builder.setRxBytes(devCounter64ToApi(channel.getEthernet().getState().getRxBytes()));
        builder.setRxCrcErrSum(devCounter64ToApi(channel.getEthernet().getState().getRxCrcErrSum()));
        builder.setRxPkts(devCounter64ToApi(channel.getEthernet().getState().getRxPkts()));
        builder.setRxPkts64Octets(devCounter64ToApi(channel.getEthernet().getState().getRxPkts64Octets()));
        builder.setRxPkts65to127Octets(devCounter64ToApi(channel.getEthernet().getState().getRxPkts65to127Octets()));
        builder.setRxPkts128to255Octets(devCounter64ToApi(channel.getEthernet().getState().getRxPkts128to255Octets()));
        builder.setRxPkts256to511Octets(devCounter64ToApi(channel.getEthernet().getState().getRxPkts256to511Octets()));
        builder.setRxPkts512to1023Octets(devCounter64ToApi(channel.getEthernet().getState().getRxPkts512to1023Octets()));
        builder.setRxPkts1024to1518Octets(devCounter64ToApi(channel.getEthernet().getState().getRxPkts1024to1518Octets()));
        builder.setRxStatOsz(devCounter64ToApi(channel.getEthernet().getState().getRxStatOsz()));
        builder.setRxStatUsz(devCounter64ToApi(channel.getEthernet().getState().getRxStatUsz()));
        builder.setTxBytes(devCounter64ToApi(channel.getEthernet().getState().getTxBytes()));
        builder.setTxCrcErrSum(devCounter64ToApi(channel.getEthernet().getState().getTxCrcErrSum()));
        builder.setTxPkts(devCounter64ToApi(channel.getEthernet().getState().getTxPkts()));
        builder.setTxPkts64Octets(devCounter64ToApi(channel.getEthernet().getState().getTxPkts64Octets()));
        builder.setTxPkts65to127Octets(devCounter64ToApi(channel.getEthernet().getState().getTxPkts65to127Octets()));
        builder.setTxPkts128to255Octets(devCounter64ToApi(channel.getEthernet().getState().getTxPkts128to255Octets()));
        builder.setTxPkts256to511Octets(devCounter64ToApi(channel.getEthernet().getState().getTxPkts256to511Octets()));
        builder.setTxPkts512to1023Octets(devCounter64ToApi(channel.getEthernet().getState().getTxPkts512to1023Octets()));
        builder.setTxPkts1024to1518Octets(devCounter64ToApi(channel.getEthernet().getState().getTxPkts1024to1518Octets()));
        builder.setTxStatOsz(devCounter64ToApi(channel.getEthernet().getState().getTxStatOsz()));
        builder.setTxStatUsz(devCounter64ToApi(channel.getEthernet().getState().getTxStatUsz()));
        if (channel.getEthernet().getLldp() != null) {
            LldpBuilder lldpBuilder = new LldpBuilder();
            if (channel.getEthernet().getLldp().getState() != null) {
                if (channel.getEthernet().getLldp().getState().getCounters() != null) {
                    CountersBuilder countersBuilder = new CountersBuilder();
                    if(channel.getEthernet().getLldp().getState().getCounters().getFrameDiscard()!=null){
                        countersBuilder.setFrameDiscard(new Counter64(channel.getEthernet().getLldp().getState().getCounters().getFrameDiscard().getValue()));
                    }
                    if(channel.getEthernet().getLldp().getState().getCounters().getFrameErrorIn()!=null){
                        countersBuilder.setFrameErrorIn(new Counter64(channel.getEthernet().getLldp().getState().getCounters().getFrameErrorIn().getValue()));
                    }
                    if(channel.getEthernet().getLldp().getState().getCounters().getFrameErrorOut()!=null){
                        countersBuilder.setFrameErrorOut(new Counter64(channel.getEthernet().getLldp().getState().getCounters().getFrameErrorOut().getValue()));
                    }
                    if(channel.getEthernet().getLldp().getState().getCounters().getFrameIn()!=null){
                        countersBuilder.setFrameIn(new Counter64(channel.getEthernet().getLldp().getState().getCounters().getFrameIn().getValue()));
                    }
                    if(channel.getEthernet().getLldp().getState().getCounters().getFrameOut()!=null){
                        countersBuilder.setFrameOut(new Counter64(channel.getEthernet().getLldp().getState().getCounters().getFrameOut().getValue()));
                    }
                    if(channel.getEthernet().getLldp().getState().getCounters().getLastClear()!=null){
                        countersBuilder.setLastClear(new DateAndTime(channel.getEthernet().getLldp().getState().getCounters().getLastClear().getValue()));
                    }
                    if(channel.getEthernet().getLldp().getState().getCounters().getTlvDiscard()!=null){
                        countersBuilder.setTlvDiscard(new Counter64(channel.getEthernet().getLldp().getState().getCounters().getTlvDiscard().getValue()));
                    }
                    if(channel.getEthernet().getLldp().getState().getCounters().getTlvUnknown()!=null){
                        countersBuilder.setTlvUnknown(new Counter64(channel.getEthernet().getLldp().getState().getCounters().getTlvUnknown().getValue()));
                    }

                    lldpBuilder.setCounters(countersBuilder.build());
                }
                lldpBuilder.setEnabled(channel.getEthernet().getLldp().getState().getEnabled());
                lldpBuilder.setSnooping(channel.getEthernet().getLldp().getState().getSnooping());
            }
            if (channel.getEthernet().getLldp().getNeighbors() != null) {
                lldpBuilder.setNeighbors(devToNeighborsApi(channel.getEthernet().getLldp().getNeighbors()));
            }
            builder.setLldp(lldpBuilder.build());
        }
        return builder.build();
    }
    public org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.GetOdukDelayInput apiToGetOdukDelayInputDev(GetOdukDelayInput input){
        if(input==null){
            return null;
        }
       org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.GetOdukDelayInputBuilder builder=new org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.GetOdukDelayInputBuilder();
        builder.setIndex(input.getIndex());
        builder.setDelayTestTimes(input.getDelayTestTimes());
        return builder.build();
    }
    public GetOdukDelayOutput devToGetOdukDelayOutputApi(org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.GetOdukDelayOutput odukDelayOutput){
        if(odukDelayOutput==null){
            return null;
        }
        GetOdukDelayOutputBuilder builder=new GetOdukDelayOutputBuilder();
        builder.setDelayDataValue(devToDelayDataValueApi(odukDelayOutput.getDelayDataValue()));
        return builder.build();
    }
    private DelayDataValue devToDelayDataValueApi(org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.get.oduk.delay.output.DelayDataValue dataValue){
        if(dataValue==null){
            return null;
        }
        DelayDataValueBuilder builder=new DelayDataValueBuilder();
        builder.setAvg(dataValue.getAvg());
        builder.setInstant(dataValue.getInstant());
        builder.setMax(dataValue.getMax());
        builder.setMin(dataValue.getMin());
        return builder.build();
    }
    private Neighbors devToNeighborsApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.lldp.logical.channel.neighbor.top.Neighbors neighbors) {
        if(neighbors == null){
            return null;
        }
        NeighborsBuilder neighborsBuilder = new NeighborsBuilder();
        Map<NeighborKey, Neighbor> values = new HashMap<>(neighbors.getNeighbor().size());
        for (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.lldp.logical.channel.neighbor.top.neighbors.Neighbor neighbor : neighbors.getNeighbor().values()) {
            NeighborBuilder neighborBuilder = new NeighborBuilder();
            if (neighbor.getState() != null) {
                org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.lldp.logical.channel.neighbor.top.neighbors.neighbor.StateBuilder stateBuilder = new org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.lldp.logical.channel.neighbor.top.neighbors.neighbor.StateBuilder();
                stateBuilder.setAge(neighbor.getState().getAge());
                stateBuilder.setChassisId(neighbor.getState().getChassisId());
                stateBuilder.setChassisIdType(devToChassisIdTypeApi(neighbor.getState().getChassisIdType()));
                stateBuilder.setId(neighbor.getState().getId());
                stateBuilder.setLastUpdate(neighbor.getState().getLastUpdate());
                stateBuilder.setManagementAddress(neighbor.getState().getManagementAddress());
                stateBuilder.setManagementAddressType(neighbor.getState().getManagementAddressType());
                stateBuilder.setPortDescription(neighbor.getState().getPortDescription());
                stateBuilder.setPortId(neighbor.getState().getPortId());
                stateBuilder.setPortIdType(devToPortIdTypeApi(neighbor.getState().getPortIdType()));
                stateBuilder.setSystemDescription(neighbor.getState().getSystemDescription());
                stateBuilder.setSystemName(neighbor.getState().getSystemName());
                stateBuilder.setTtl(neighbor.getState().getTtl());
                neighborBuilder.setState(stateBuilder.build());
            }
            if (neighbor.getCustomTlvs() != null) {
                if (neighbor.getCustomTlvs().getTlv() != null) {
                    // CustomTlvsBuilder customTlvsBuilder = new CustomTlvsBuilder();
                    Map<TlvKey, Tlv> valuesTlv = new HashMap<>(neighbor.getCustomTlvs().getTlv().size());
                    Map<CustomTlvKey, CustomTlv> map = new HashMap<>(neighbor.getCustomTlvs().getTlv().size());
                    for (Tlv tlv : neighbor.getCustomTlvs().getTlv().values()) {
                        CustomTlvBuilder customTlvBuilder = new CustomTlvBuilder();
                        TlvBuilder tlvBuilder = new TlvBuilder();
                        if (tlv.getState() != null) {
                            customTlvBuilder.setOui(tlv.getState().getOui());
                            customTlvBuilder.setOuiSubtype(tlv.getState().getOuiSubtype());
                            customTlvBuilder.setType(tlv.getState().getType());
                            customTlvBuilder.setValue(tlv.getState().getValue());
                            customTlvBuilder.withKey(new CustomTlvKey(customTlvBuilder.getOui(),customTlvBuilder.getOuiSubtype(),customTlvBuilder.getType()));
                        }
                        tlvBuilder.setOui(tlv.getOui());
                        tlvBuilder.setOuiSubtype(tlv.getOuiSubtype());
                        tlvBuilder.setType(tlv.getType());
                        tlvBuilder.withKey(new TlvKey(tlv.getOui(), tlv.getOuiSubtype(), tlv.getType()));
                        valuesTlv.put(tlvBuilder.key(), tlvBuilder.build());

                        map.put(customTlvBuilder.key(),customTlvBuilder.build());

                    }
                    neighborBuilder.setCustomTlv(map);
                    // customTlvsBuilder.setTlv(valuesTlv);
                    // neighborBuilder.setCustomTlvs(customTlvsBuilder.build());
                }
            }
            neighborBuilder.setId(neighbor.getId());
            values.put(new NeighborKey(neighbor.getId()), neighborBuilder.build());
        }
        neighborsBuilder.setNeighbor(values);
        return neighborsBuilder.build();
    }


    private  Map<LogicalChannelKey, LogicalChannel> devToChannelsApiMap(Collection<Channel> channels) {
        if (channels == null) {
            return null;
        }
        Map<LogicalChannelKey, LogicalChannel> values = new HashMap<>(channels.size());
        for (Channel channel : channels) {
            LogicalChannel channelApi = devToChannelApi(channel);
            if(channelApi != null){
                values.put(channelApi.key(), channelApi);
            }
        }
        return values;
    }

    private LogicalChannel devToChannelApi(Channel channel) {
        if (channel == null || channel.getState() == null) {
            return null;
        }
        LogicalChannelBuilder channelBuilder = new LogicalChannelBuilder();
        channelBuilder.setAdminState(devAdminStateTypeToApi(channel.getState().getAdminState()));
        channelBuilder.setDelayTestMode(devToDelayTestModeApi(channel.getState().getDelayTestMode()));
        channelBuilder.setDescription(channel.getState().getDescription());
        channelBuilder.setIndex(channel.getIndex());
        channelBuilder.setLinkState(devToLinkStateApi(channel.getState().getLinkState()));
        channelBuilder.setLogicalChannelType(devToLogicalChannelTypeApi(channel.getState().getLogicalChannelType()));
        channelBuilder.setLoopbackMode(devToLoopbackModeTypeApi(channel.getState().getLoopbackMode()));
        channelBuilder.setRateClass(devToRateClassApi(channel.getState().getRateClass()));
        channelBuilder.setTestSignal(channel.getState().getTestSignal());
        channelBuilder.setTribProtocol(devToTribProtocolApi(channel.getState().getTribProtocol()));
        channelBuilder.withKey(new LogicalChannelKey(channel.getIndex()));
        channelBuilder.setIngress(devChannelIngressToApi(channel.getIngress()));
        channelBuilder.setLogicalChannelAssignments(devLogicalAssignmentToApi(channel.getLogicalChannelAssignments()));
        // otn 赋值
        OtnBuilder otnBuilder = new OtnBuilder();
        if (channel.getOtn() != null && channel.getOtn().getState() != null) {
            otnBuilder.setTtiMsgTransmit(channel.getOtn().getState().getTtiMsgTransmit());
            otnBuilder.setTtiMsgExpected(channel.getOtn().getState().getTtiMsgExpected());
            otnBuilder.setTtiMsgRecv(channel.getOtn().getState().getTtiMsgRecv());
        }
        channelBuilder.setOtn(otnBuilder.build());
        // sdh 赋值
        org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.sdh.j0.top.SdhJ0Builder sdhJ0Builder = new org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.sdh.j0.top.SdhJ0Builder();
        if (channel.getSdhJ0() != null && (channel.getSdhJ0().getSdhJ0State() != null || channel.getSdhJ0().getSdhJ0Config() != null)) {
            if (channel.getSdhJ0().getSdhJ0State() != null) {
                sdhJ0Builder.setJ0ExpectedRx(channel.getSdhJ0().getSdhJ0State().getJ0ExpectedRx());
                sdhJ0Builder.setJ0ActualRx(channel.getSdhJ0().getSdhJ0State().getJ0ActualRx());
                sdhJ0Builder.setJ0ActualTx(channel.getSdhJ0().getSdhJ0State().getJ0ActualTx());
            }
            if (channel.getSdhJ0().getSdhJ0Config() != null) {
                if (Objects.isNull(sdhJ0Builder.getJ0ExpectedRx())) {
                    sdhJ0Builder.setJ0ExpectedRx(channel.getSdhJ0().getSdhJ0Config().getJ0ExpectedRx());
                }
                if (Objects.isNull(sdhJ0Builder.getJ0ActualTx())) {
                    sdhJ0Builder.setJ0ActualTx(channel.getSdhJ0().getSdhJ0Config().getJ0ActualTx());
                }
            }
        }
        channelBuilder.setSdhJ0(sdhJ0Builder.build());
        return channelBuilder.build();
    }

   public LogicalChannels apiToLogicalChannels(BatchSetLogicalChannelInput input){
       LogicalChannelsBuilder builder=new LogicalChannelsBuilder();
       Map<ChannelKey, Channel> values =new HashMap<>(input.getLogicalChannelConfig().size());
       for(LogicalChannelConfig channelConfig:input.getLogicalChannelConfig()){
           ChannelBuilder channelBuilder=new ChannelBuilder();
           channelBuilder.setIndex(channelConfig.getIndex());
           org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.logical.channel.top.logical.channels.channel.ConfigBuilder configBuilder=new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.logical.channel.top.logical.channels.channel.ConfigBuilder();
           configBuilder.setIndex(channelConfig.getIndex());
           if (Objects.nonNull(channelConfig.getAdminState())) {
               configBuilder.setAdminState(apiToAdminStateDev(channelConfig.getAdminState()));
           }
           configBuilder.setLoopbackMode(apiToLoopbackModeTypeDev(channelConfig.getLoopbackMode()));
           if (Objects.nonNull(configBuilder.getDelayTestMode())) {
               configBuilder.setDelayTestMode(configBuilder.getDelayTestMode());
           }
           channelBuilder.setConfig(configBuilder.build());
           channelBuilder.withKey(new ChannelKey(channelConfig.getIndex()));
           values.put(channelBuilder.key(),channelBuilder.build());
       }
       builder.setChannel(values);
       return builder.build();
   }

    /**
     * OpticalSignalToNoiseRatio dev to api
     *
     * @param opticalSignalToNoiseRatio dev
     * @return api
     */
    public OpticalSignalToNoiseRatio devToOpticalSignalToNoiseRatioToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.terminal.otn.protocol.multi.stats.OpticalSignalToNoiseRatio opticalSignalToNoiseRatio) {
        if (opticalSignalToNoiseRatio == null) {
            return null;
        }
        OpticalSignalToNoiseRatioBuilder builder = new OpticalSignalToNoiseRatioBuilder();
        builder.setAvg(opticalSignalToNoiseRatio.getAvg());
        builder.setInstant(opticalSignalToNoiseRatio.getInstant());
        builder.setInterval(devStatIntervalToApi(opticalSignalToNoiseRatio.getInterval()));
        builder.setMax(opticalSignalToNoiseRatio.getMax());
        builder.setMaxTime(devTimeticks64ToApi(opticalSignalToNoiseRatio.getMaxTime()));
        builder.setMin(opticalSignalToNoiseRatio.getMin());
        builder.setMinTime(devTimeticks64ToApi(opticalSignalToNoiseRatio.getMinTime()));
        return builder.build();
    }
}
