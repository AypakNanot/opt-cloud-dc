/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.transform.common.resource;

import cn.hutool.core.collection.CollUtil;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.base.CommonTypeTransform;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.base.EqTypeTransform;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.base.PlatformTypeTransform;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.base.PortAndTransceiverAndOtdrAndOpticalChannelTypeTransform;
import com.optel.tmaster.dc.general.base.exception.manage.NoMatchEnumValueException;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.AvgMinMaxInstantStatsPrecision2DBm;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.AvgMinMaxInstantStatsPrecision2MA;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.StatInterval;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.Timeticks64;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.ChassisBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.FanBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.McuBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.MuxBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.PowerSupplyBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.PtmBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.memory.state.Memory;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.memory.state.MemoryBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.temp.state.TemperatureBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.component.LineCard;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.component.LineCardBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.component.Port;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.component.StateBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.transceiver.rev220208.optical.power.state.LaserBiasCurrentBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.transceiver.rev220208.optical.power.state.TecCurrentBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.transceiver.rev220208.physical.channel.top.PhysicalChannelsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.transceiver.rev220208.physical.channel.top.physical.channels.ChannelBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.transceiver.rev220208.port.transceiver.top.Transceiver;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.transceiver.rev220208.port.transceiver.top.TransceiverBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.types.rev220208.ComponentPowerType;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.optical.channel.top.OpticalChannel;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.terminal.optical.channel.top.OpticalChannelBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.line.common.rev220208.transport.line.common.port.state.InputPowerBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.line.common.rev220208.transport.line.common.port.state.OutputPowerBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.line.common.rev220208.transport.line.common.port.top.OpticalPortBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.FrequencyType;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.TRIBUTARYPROTOCOLTYPE;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.yang.rev220208.Date;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.com.device.rev200210.GetCommonResourceOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.com.device.rev200210.GetCommonResourceOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.com.device.rev200210.UpdateCommonResourceInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.fan.rev200630.State1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.linecard.rev200630.Component1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.linecard.rev200630.Component1Builder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.linecard.rev200630.linecard.top.Linecard;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.linecard.rev200630.linecard.top.LinecardBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.port.rev200630.Port1;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.Chassis;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.Fan;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.Mcu;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.Mux;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.PowerSupply;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.Ptm;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.temp.state.Temperature;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.Components;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.Component;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.ComponentKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.component.Config;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.component.ConfigBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.component.State;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev200630.physical.channel.top.physical.channels.Channel;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev200630.physical.channel.top.physical.channels.ChannelKey;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;

/**
 * 设备通用资源 类型转换器
 * 2022/3/3 16:45
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class CommonResourceTransformImpl implements PlatformTypeTransform, EqTypeTransform, PortAndTransceiverAndOtdrAndOpticalChannelTypeTransform {
    private static final Logger LOG = LoggerFactory.getLogger(CommonResourceTransformImpl.class);
    public Config apiUpdateConfigToDev(UpdateCommonResourceInput input){
        ConfigBuilder configBuilder = new ConfigBuilder();
        configBuilder.setName(input.getName());
        configBuilder.setDescription(input.getDescription());
        configBuilder.setVendorTypePreconf(input.getVendorTypePreconf());
        configBuilder.setAdminState(apiAdminStateTypeToDevString(input.getAdminState()));
        return configBuilder.build();
    }

    public Component1 apiUpdateLineCardToDev(UpdateCommonResourceInput input){
        Component1Builder builder = new Component1Builder();
        builder.setLinecard(apiUpdateLineCardToDev(input.getPowerAdminState()));
        return builder.build();
    }


    public Linecard apiUpdateLineCardToDev(ComponentPowerType type){
        LinecardBuilder builder = new LinecardBuilder();
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.linecard.rev200630.linecard.top.linecard.ConfigBuilder configBuilder =
                new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.linecard.rev200630.linecard.top.linecard.ConfigBuilder();
        configBuilder.setPowerAdminState(apiUpdateLineCardTypeToDev(type));
        builder.setConfig(configBuilder.build());
        return builder.build();
    }

    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev200630.ComponentPowerType
        apiUpdateLineCardTypeToDev(ComponentPowerType type){
        if(type==null){
            return null;
        }
        switch (type){
            case POWERDISABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev200630.ComponentPowerType.POWERDISABLED;
            case POWERENABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev200630.ComponentPowerType.POWERENABLED;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(type);
        }
    }

    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.Component apiUpdateCommonResourceToDev(UpdateCommonResourceInput input) {
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.ComponentBuilder builder
                = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.top.components.ComponentBuilder();
        ConfigBuilder configBuilder = new ConfigBuilder();
        configBuilder.setName(input.getName());
        configBuilder.setDescription(input.getDescription());
        configBuilder.setVendorTypePreconf(input.getVendorTypePreconf());
        configBuilder.setAdminState(apiAdminStateTypeToDevString(input.getAdminState()));
        builder.setConfig(configBuilder.build());
        return builder.build();
    }

    /**
     * temperature dev to api
     *
     * @param temperature dev
     * @return api
     */
    private org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.temp.state.Temperature devTemperatureToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.temp.state.Temperature temperature) {
        if (temperature == null) {
            return null;
        }
        TemperatureBuilder resultBuilder = new TemperatureBuilder();
        resultBuilder.setAlarmSeverity(devSeverityToApi(temperature.getAlarmSeverity()));
        resultBuilder.setAlarmStatus(temperature.getAlarmStatus());
        resultBuilder.setAlarmThreshold(temperature.getAlarmThreshold());
        resultBuilder.setAvg(temperature.getAvg());
        resultBuilder.setInstant(temperature.getInstant());
        if (temperature.getInterval() != null) {
            resultBuilder.setInterval(new StatInterval(temperature.getInterval().getValue()));
        }
        resultBuilder.setMax(temperature.getMax());
        if (temperature.getMaxTime() != null) {
            resultBuilder.setMaxTime(new Timeticks64(temperature.getMaxTime().getValue()));
        }
        resultBuilder.setMin(temperature.getMin());
        if (temperature.getMinTime() != null) {
            resultBuilder.setMinTime(new Timeticks64(temperature.getMinTime().getValue()));
        }
        return resultBuilder.build();
    }

    private Memory devMemoryToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.component.memory.state.Memory memory) {
        if (memory == null) {
            return null;
        }
        MemoryBuilder resultBuilder = new MemoryBuilder();
        resultBuilder.setAvailable(memory.getAvailable());
        resultBuilder.setUtilized(memory.getUtilized());
        return resultBuilder.build();
    }

    /**
     * state dev to api
     *
     * @param state dev
     * @return api
     */
    private org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.component.State devStateToApi(State state) {
        if (state != null) {
            StateBuilder stateBuilder = new StateBuilder();
            stateBuilder.setAdminState(devAdminStateTypeToApi(state.getAdminState()));
            stateBuilder.setDescription(state.getDescription());
            //TODO 将其他厂商的设备名称 修改成optel
            // if(state.getMfgName()!=null && !state.getMfgName().isEmpty()){
            //     stateBuilder.setMfgName("optel");
            // }
            stateBuilder.setMfgName(state.getMfgName());
            if (state.getMfgDate() != null) {
                stateBuilder.setMfgDate(new Date(state.getMfgDate().getValue()));
            }
            stateBuilder.setHardwareVersion(state.getHardwareVersion());
            stateBuilder.setFirmwareVersion(state.getFirmwareVersion());
            stateBuilder.setSoftwareVersion(state.getSoftwareVersion());
            stateBuilder.setSerialNo(state.getSerialNo());
            stateBuilder.setProductionNum(state.getPartNo());
            stateBuilder.setPartNo(devPartNoToApi(state.getPartNo()));
            stateBuilder.setOperStatus(devOperStatusToApi(state.getOperStatus()));
            stateBuilder.setRemovable(state.getRemovable());
            stateBuilder.setEmpty(state.getEmpty());
            stateBuilder.setParent(state.getParent());
            stateBuilder.setPanelDescription(state.getPanelDescription());
            //温度
            Temperature temperature = state.getTemperature();
            if (temperature != null) {
                stateBuilder.setTemperature(devTemperatureToApi(temperature));
            }
            stateBuilder.setRatedPower(state.getRatedPower());
            stateBuilder.setUsedPower(state.getUsedPower());
            //板卡安装相关
            if (state.getSupportedVendorTypes() != null) {
                stateBuilder.setSupportedVendorType(state.getSupportedVendorTypes().getSupportedVendorType());
            }
            stateBuilder.setActualVendorType(state.getActualVendorType());
            stateBuilder.setVendorTypePreconf(state.getVendorTypePreconf());
            stateBuilder.setAllocatedPower(state.getAllocatedPower());
            stateBuilder.setId(state.getId());
            stateBuilder.setLocation(state.getLocation());
            stateBuilder.setMemory(devMemoryToApi(state.getMemory()));
            stateBuilder.setSwitchAbility(state.getSwitchAbility());
            stateBuilder.setType(devTypeToApi(state.getType()));
            return stateBuilder.build();
        }
        return null;
    }

    private org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.PowerSupply devPsusBuilderToApi(PowerSupply powerSupply) {
        if (powerSupply == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.power.supply.State state
                = powerSupply.getState();
        if (state == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.psu.rev200630.State1 psuState
                = state.augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.psu.rev200630.State1.class);
        if (psuState != null) {
            PowerSupplyBuilder resultBuilder = new PowerSupplyBuilder();
            resultBuilder.setEnabled(psuState.getEnabled());
            resultBuilder.setSupportedPowerSupplyMode(devSupportedPowerSupplyModesToApi(psuState.getSupportedPowerSupplyModes()));
            resultBuilder.setUsedPowerSupplyMode(devPsuModeTypeToApi(psuState.getUsedPowerSupplyMode()));
            resultBuilder.setCapacity(psuState.getCapacity());
            resultBuilder.setInputCurrent(psuState.getInputCurrent());
            resultBuilder.setInputVoltage(psuState.getInputVoltage());
            resultBuilder.setOutputCurrent(psuState.getOutputCurrent());
            resultBuilder.setOutputVoltage(psuState.getOutputVoltage());
            return resultBuilder.build();
        }
        return null;
    }

    private org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.Mcu devMcuToApi(Mcu mcu) {
        if (mcu == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.mcu.State state
                = mcu.getState();
        if (state == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mcu.rev200630.@Nullable State1 mcuState
                = state.augmentation(
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mcu.rev200630.State1.class);
        if (mcuState == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.McuBuilder mcuBuilder
                = new McuBuilder();
        mcuBuilder.setMcuTotalMemory(mcuState.getMcuTotalMemory());
        mcuBuilder.setMcuAvailableMemory(mcuState.getMcuAvailableMemory());
        mcuBuilder.setMcuCpuUtilization(mcuState.getMcuCpuUtilization());
        mcuBuilder.setActive(mcuState.getActive());
        return mcuBuilder.build();
    }

    private org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.Ptm devPtmToApi(Ptm ptm) {
        if (ptm == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.ptm.State state = ptm.getState();
        if (state == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.ptm.rev200630.@Nullable State1 state1 =
                state.augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.ptm.rev200630.State1.class);
        if (state1 == null) {
            return null;
        }
        PtmBuilder ptmBuilder = new PtmBuilder();
        ptmBuilder.setWorkState(devPtmWorkTypeToApi(state1.getWorkState()));
        return ptmBuilder.build();
    }

    private org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.Fan devFanToApi(Fan fan) {
        if (fan == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.fan.State state = fan.getState();
        if (state == null) {
            return null;
        }
        @Nullable State1 s = state.augmentation(State1.class);
        if (s == null) {
            return null;
        }
        FanBuilder fanBuilder = new FanBuilder();
        fanBuilder.setFanMode(devFanModeTypeToApi(s.getFanMode()));
        fanBuilder.setFanSpeed(devFanSpeedTypeToApi(s.getFanSpeed()));
        return fanBuilder.build();
    }

    private org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.Chassis devChassisToApi(Chassis chassis,String partNo) {
        if (chassis == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.chassis.State state
                = chassis.getState();
        if (state == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.chassis.rev200630.@Nullable State1 state1
                = state.augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.chassis.rev200630.State1.class);
        ChassisBuilder builder = new ChassisBuilder();
        if(state1!=null){
            builder.setChassisType(devComponentChassisTypeToApi(state1.getChassisType(), partNo));
            builder.setDirection(devDirectionToApi(state1.getDirection()));
            builder.setLcd(state1.getLcd());
        }
        return builder.build();
    }

    private Port devToPortApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.Port port) {
        if (port == null) {
            return null;
        }
        @org.eclipse.jdt.annotation.Nullable Port1 portAuamentation = port.augmentation(Port1.class);
        org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.component.PortBuilder portBuilder = new org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.component.PortBuilder();
        if (portAuamentation != null && portAuamentation.getBreakoutMode() != null) {
            if (portAuamentation.getBreakoutMode().getState() != null) {
                portBuilder.setChannelSpeed(devToChannelSpeedApi(portAuamentation.getBreakoutMode().getState().getChannelSpeed()));
                portBuilder.setNumChannels(portAuamentation.getBreakoutMode().getState().getNumChannels());
            }
        }
        if (port.getState() != null) {
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.rev200630.platform.anchors.top.port.State state = port.getState();
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.port.rev200630.@Nullable State2 stateAuamentation = state.augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.port.rev200630.State2.class);
            if (stateAuamentation != null) {
                portBuilder.setLayerProtocolName(devToTribProtocolApi(stateAuamentation.getLayerProtocolName()));
                portBuilder.setReverseMode(devToReverseModeApi(stateAuamentation.getReverseMode()));
                if (stateAuamentation.getSupportedLayerProtocolNames() != null
                        && CollUtil.isNotEmpty(stateAuamentation.getSupportedLayerProtocolNames().getSupportedLayerProtocolName())) {
                    Set<TRIBUTARYPROTOCOLTYPE> layerNames = new HashSet<>();
                    for(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev200630.TRIBUTARYPROTOCOLTYPE cla:
                            stateAuamentation.getSupportedLayerProtocolNames().getSupportedLayerProtocolName()){
                        layerNames.add(devToTribProtocolApi(cla));
                    }
                    portBuilder.setSupportedLayerProtocolName(layerNames);
                }
            }
        }
        //todo 光层端口配置
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.line.common.rev200630.@Nullable Port1 opticalPort = port.augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.line.common.rev200630.Port1.class);
        if (opticalPort != null && opticalPort.getOpticalPort() != null) {
            OpticalPortBuilder opticalPortBuilder = new OpticalPortBuilder();
            if (opticalPort.getOpticalPort().getState() != null) {
                opticalPortBuilder.setAdminState(devAdminStateTypeToApi(opticalPort.getOpticalPort().getState().getAdminState()));
                opticalPortBuilder.setMonObject(opticalPort.getOpticalPort().getState().getMonObject());
                opticalPortBuilder.setMonType(devToMonTypeApi(opticalPort.getOpticalPort().getState().getMonType()));
                opticalPortBuilder.setOpticalPortType(devToOpticalPortType(opticalPort.getOpticalPort().getState().getOpticalPortType()));
                opticalPortBuilder.setPowerOffset(opticalPort.getOpticalPort().getState().getPowerOffset());
                opticalPortBuilder.setTilt(opticalPort.getOpticalPort().getState().getTilt());
                AvgMinMaxInstantStatsPrecision2DBm inputPower = devToAvgMinMaxInstantStatsPrecisionApi(opticalPort.getOpticalPort().getState().getInputPower());
                if (inputPower != null) {
                    opticalPortBuilder.setInputPower(new InputPowerBuilder(inputPower).build());
                }
                AvgMinMaxInstantStatsPrecision2DBm outputPower = devToAvgMinMaxInstantStatsPrecisionApi(opticalPort.getOpticalPort().getState().getOutputPower());
                if (outputPower != null) {
                    opticalPortBuilder.setOutputPower(new OutputPowerBuilder(outputPower).build());
                }
            } else if (opticalPort.getOpticalPort().getConfig() != null) {
                opticalPortBuilder.setPowerOffset(opticalPort.getOpticalPort().getConfig().getPowerOffset());
                opticalPortBuilder.setAdminState(devAdminStateTypeToApi(opticalPort.getOpticalPort().getConfig().getAdminState()));
            }
            portBuilder.setOpticalPort(opticalPortBuilder.build());
        }
        return portBuilder.build();
    }

    private Transceiver devToTransceiverApi(Component component) {
        if (component == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev200630.Component1 transceiver
                = component.augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev200630.Component1.class);
        if (transceiver == null || transceiver.getTransceiver() == null) {
            return null;
        }
        TransceiverBuilder builder = new TransceiverBuilder();
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev200630.port.transceiver.top.transceiver.State state = transceiver.getTransceiver().getState();
        if (state != null) {
            builder.setEnabled(state.getEnabled());
            builder.setUsedServicePortTypePreconf(devToUsedServicePortTypeApi(state.getUsedServicePortTypePreconf()));
            builder.setUsedServicePortType(devToUsedServicePortTypeApi(state.getUsedServicePortType()));
            builder.setFormFactor(devToFormFactorApi(state.getFormFactor()));
            builder.setComplianceCode(state.getComplianceCode());
            builder.setConnectorType(devToConnectorTypeApi(state.getConnectorType()));
            builder.setVendor(state.getVendor());
            builder.setTransmissionDistance(state.getTransmissionDistance());
            builder.setSerialNo(state.getSerialNo());
            builder.setDateCode(new CommonTypeTransform(){}.stringToDateTime(state.getDateCode()));
            if(state.getSupportedServicePortTypes()!=null){
                builder.setSupportedServicePortType(devToUsedServicePortTypeApiList(state.getSupportedServicePortTypes().getSupportedServicePortType()));
            }
            builder.setFaultCondition(state.getFaultCondition());
            builder.setOutputPower(devToOutputPowerApi(state.getOutputPower()));
            builder.setInputPower(devToInputPowerApi(state.getInputPower()));

        }
        if (transceiver.getTransceiver().getPhysicalChannels() != null) {
            @Nullable Map<ChannelKey, Channel> channels = transceiver.getTransceiver().getPhysicalChannels().getChannel();
            PhysicalChannelsBuilder physicalChannels = new PhysicalChannelsBuilder();
            Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.transceiver.rev220208.physical.channel.top.physical.channels.ChannelKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.transceiver.rev220208.physical.channel.top.physical.channels.Channel> values = new HashMap<>(channels.size());
            for (Channel channel : channels.values()) {
                ChannelBuilder channelBuilder = new ChannelBuilder();
                channelBuilder.setIndex(channel.getIndex());
                if (channel.getState() != null) {
                    channelBuilder.setDescription(channel.getState().getDescription());
                    channelBuilder.setOutputFrequency(new FrequencyType(channel.getState().getOutputFrequency().getValue()));
                    channelBuilder.setTargetOutputPower(channel.getState().getTargetOutputPower());
                    AvgMinMaxInstantStatsPrecision2MA laserBiasCurrent = devToAvgMinMaxInstantStatsPrecisionApi(channel.getState().getLaserBiasCurrent());
                    if (laserBiasCurrent != null) {
                        channelBuilder.setLaserBiasCurrent(new LaserBiasCurrentBuilder(laserBiasCurrent).build());
                    }
                    AvgMinMaxInstantStatsPrecision2MA tecCurrent = devToAvgMinMaxInstantStatsPrecisionApi(channel.getState().getTecCurrent());
                    if (tecCurrent != null) {
                        channelBuilder.setTecCurrent(new TecCurrentBuilder(tecCurrent).build());
                    }
                    channelBuilder.setInputPower(devToInputPowerApi(channel.getState().getInputPower()));
                    channelBuilder.setOutputPower(devToOutputPowerApi(channel.getState().getOutputPower()));
                    channelBuilder.setTxLaser(channel.getState().getTxLaser());
                }
                channelBuilder.withKey(new org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.transceiver.rev220208.physical.channel.top.physical.channels.ChannelKey(channel.getIndex()));
                values.put(channelBuilder.key(), channelBuilder.build());
            }
            physicalChannels.setChannel(values);
            builder.setPhysicalChannels(physicalChannels.build());
        }
        return builder.build();
    }
    private OpticalChannel devToOpticalChannel(Component component){
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.@Nullable Component1 component1 = component.augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.Component1.class);
        if(component1==null||component1.getOpticalChannel()==null){
            return null;
        }
        OpticalChannelBuilder builder=new OpticalChannelBuilder();
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.terminal.optical.channel.top.optical.channel.State state = component1.getOpticalChannel().getState();
        if(state!=null){
            builder.setFrequency(new FrequencyType(state.getFrequency().getValue()));
            builder.setGroupId(state.getGroupId());
            builder.setTargetOutputPower(state.getTargetOutputPower());
            builder.setOperationalMode(state.getOperationalMode());
            builder.setLinePort(state.getLinePort());
            builder.setInputPower(devToInputPowerApi(state.getInputPower()));
            builder.setOutputPower(devToOutputPowerApi(state.getOutputPower()));
        }
        return builder.build();
    }

    public Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.ComponentKey
            , org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.Component>
    devToComponentsToApi(Components components) {
        if (components == null || components.getComponent() == null) {
            return null;
        }
        @Nullable Map<ComponentKey, Component> componentMap = components.getComponent();
        Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.ComponentKey
                , org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.Component>
                resultMap = new HashMap<>(componentMap.size());
        //开始转换 data:componentMap result:resultMap
        if (!componentMap.isEmpty()) {
            for (Component component : componentMap.values()) {
                if (component != null) {
                    org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.ComponentBuilder
                            componentBuilder = new org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform
                            .rev220208.platform.component.top.ComponentBuilder();
                    componentBuilder.setName(component.getName());
                    //注入通用属性
                    org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.component.top.component.State
                            state = devStateToApi(component.getState());
                    if (state != null) {
                        componentBuilder.setState(state);
                    }
                    if(Objects.isNull(state)){
                        LOG.info(component.getName()+"  state is null!");
                    }else{
                        //注入机框属性
                        componentBuilder.setChassis(devChassisToApi(component.getChassis(),state.getPartNo()));
                    }
                    //注入PsuEq
                    componentBuilder.setPowerSupply(devPsusBuilderToApi(component.getPowerSupply()));
                    //注入mcu eq
                    componentBuilder.setMcu(devMcuToApi(component.getMcu()));
                    //注入ptm eq
                    componentBuilder.setPtm(devPtmToApi(component.getPtm()));
                    //注入fan eq
                    componentBuilder.setFan(devFanToApi(component.getFan()));
                    // 注入port
                    componentBuilder.setPort(devToPortApi(component.getPort()));
                    //注入 line card
                    componentBuilder.setLineCard(devLineCardToApi(component.augmentation(Component1.class)));
                    //注入 Transceiver
                    componentBuilder.setTransceiver(devToTransceiverApi(component));
                    //注入 OpticalChannel
                    componentBuilder.setOpticalChannel(devToOpticalChannel(component));
                    //注入 MUX
                    componentBuilder.setMux(devMuxToApi(component.getMux()));
                    //TFF
                    componentBuilder.setTff(new TffWssTransformImpl().devTffToApi(component.getTff()));
                    //WSS
                    componentBuilder.setWss(new TffWssTransformImpl().devWssToApi(component.getWss()));
                    resultMap.put(
                            new org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform
                                    .rev220208.platform.component.top.ComponentKey(component.getName()), componentBuilder.build());
                }

            }
        }
        return resultMap;
    }




    public org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.rev220208.platform.anchors.top.Mux devMuxToApi(Mux mux) {
        if (mux == null || mux.getState() == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mux.rev200630.State1 muxState
                = mux.getState().augmentation(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.mux.rev200630.State1.class);
        if(muxState == null){
            return null;
        }
        MuxBuilder muxBuilder = new MuxBuilder();
        //有mux-state的数据才是需要返回的数据
        muxBuilder.setChannelNum(muxState.getChannelNum());
        muxBuilder.setChannelInterval(devChannelIntervalTypeToApi(muxState.getChannelInterval()));
        muxBuilder.setSupportFlexGrid(muxState.getSupportFlexGrid());
        return muxBuilder.build();
    }

    /**
     * Line card dev to api
     *
     * @param component dev
     * @return api
     */
    public LineCard devLineCardToApi(Component1 component) {
        if (component != null) {
            Linecard lineCard = component.getLinecard();
            if (lineCard != null) {
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.linecard.rev200630.linecard.top.linecard.State state1 = lineCard.getState();
                if (state1 != null) {
                    LineCardBuilder lineCardBuilder = new LineCardBuilder();
                    lineCardBuilder.setPowerAdminState(devComponentPowerTypeToApi(state1.getPowerAdminState()));
                    return lineCardBuilder.build();
                }
            }
        }
        return null;
    }

    public GetCommonResourceOutput devCommonResourceToApi(Components components) {
        GetCommonResourceOutputBuilder builder = new GetCommonResourceOutputBuilder();
        builder.setComponent(devToComponentsToApi(components));
        return builder.build();
    }

}
