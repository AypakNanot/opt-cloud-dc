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
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.AvgMinMaxInstantStatsPrecision2DB;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.AvgMinMaxInstantStatsPrecision2DBm;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.AvgMinMaxInstantStatsPrecision2MA;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.optical.amplifier.state.ActualGainBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.optical.amplifier.state.ActualGainTiltBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.optical.amplifier.state.InputPowerBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.optical.amplifier.state.LaserBiasCurrentBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.optical.amplifier.state.OpticalReturnLossBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.optical.amplifier.state.OutputPowerBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.optical.amplifier.top.OpticalAmplifier;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.optical.amplifier.top.OpticalAmplifierBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.optical.amplifier.top.OpticalAmplifierKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.optical.amplifier.top.SupervisoryChannel;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.optical.amplifier.top.SupervisoryChannelBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.optical.amplifier.top.SupervisoryChannelKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.FrequencyType;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.edfa.rev200210.GetEdfaConfigOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.edfa.rev200210.GetEdfaConfigOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.edfa.rev200210.SetEdfaConfigInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev230426.optical.amplifier.top.optical.amplifier.amplifiers.amplifier.Config;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev230426.optical.amplifier.top.optical.amplifier.amplifiers.amplifier.ConfigBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.AprEnableType;

import java.util.*;

/**
 * 放大器转换类
 *
 * @author Quan Jingyuan
 * @since 2022/3/8
 **/
public class DciEdfaTransform implements PlatformTypeTransform, PortAndTransceiverAndOtdrAndOpticalChannelTypeTransform {

    public GetEdfaConfigOutput devToGetEdfaConfigOutputApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev230426.optical.amplifier.top.OpticalAmplifier opticalAmplifier, Set<String> names) {
        if (opticalAmplifier == null) {
            return null;
        }

        GetEdfaConfigOutputBuilder builder = new GetEdfaConfigOutputBuilder();
        if (opticalAmplifier.getAmplifiers() != null && opticalAmplifier.getAmplifiers().getAmplifier() != null) {
            Map<OpticalAmplifierKey, OpticalAmplifier> values = new HashMap<>(opticalAmplifier.getAmplifiers().getAmplifier().size());
            for (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev230426.optical.amplifier.top.optical.amplifier.amplifiers.Amplifier amplifier : opticalAmplifier.getAmplifiers().getAmplifier().values()) {
                boolean judge = amplifier.getState() == null || (names != null && !names.contains(amplifier.getName()));
                if(judge){
                    continue;
                }
                OpticalAmplifierBuilder amplifierBuilder = new OpticalAmplifierBuilder();
                amplifierBuilder.setName(amplifier.getName());
                amplifierBuilder.setType(devToOpticalAmpTypeApi(amplifier.getState().getType()));
                amplifierBuilder.setTargetGain(amplifier.getState().getTargetGain());
                amplifierBuilder.setTargetGainTilt(amplifier.getState().getTargetGainTilt());
                amplifierBuilder.setAmpMode(devToOpticalAmpLifierModeApi(amplifier.getState().getAmpMode()));
                amplifierBuilder.setTargetOutputPower(amplifier.getState().getTargetOutputPower());
                amplifierBuilder.setEnabled(amplifier.getState().getEnabled());
                amplifierBuilder.setTargetVoaAttenuation(amplifier.getState().getTargetVoaAttenuation());
                amplifierBuilder.setAprEnable(devToAprEnableTypeApi(amplifier.getState().getAprEnable()));
                amplifierBuilder.setIngressPort(amplifier.getState().getIngressPort());
                amplifierBuilder.setEgressPort(amplifier.getState().getEgressPort());
                amplifierBuilder.setActualVoaAttenuation(amplifier.getState().getActualVoaAttenuation());
                AvgMinMaxInstantStatsPrecision2DB actualGain = devToAvgMinMaxInstantStatsPrecisionApi(amplifier.getState().getActualGain());
                if (actualGain != null) {
                    amplifierBuilder.setActualGain(new ActualGainBuilder(actualGain).build());
                }
                AvgMinMaxInstantStatsPrecision2DB actualGainTilt = devToAvgMinMaxInstantStatsPrecisionApi(amplifier.getState().getActualGainTilt());
                if (actualGainTilt != null) {
                    amplifierBuilder.setActualGainTilt(new ActualGainTiltBuilder(actualGainTilt).build());
                }
                AvgMinMaxInstantStatsPrecision2DBm output = devToAvgMinMaxInstantStatsPrecisionApi(amplifier.getState().getOutputPower());
                if (output != null) {
                    amplifierBuilder.setOutputPower(new OutputPowerBuilder(output).build());
                }
                AvgMinMaxInstantStatsPrecision2DBm input = devToAvgMinMaxInstantStatsPrecisionApi(amplifier.getState().getInputPower());
                if (input != null) {
                    amplifierBuilder.setInputPower(new InputPowerBuilder(input).build());
                }
                AvgMinMaxInstantStatsPrecision2MA laserBiasCurrent = devToAvgMinMaxInstantStatsPrecisionApi(amplifier.getState().getLaserBiasCurrent());
                if (laserBiasCurrent != null) {
                    amplifierBuilder.setLaserBiasCurrent(new LaserBiasCurrentBuilder(laserBiasCurrent).build());
                }
                AvgMinMaxInstantStatsPrecision2DBm opticalReturnLoss = devToAvgMinMaxInstantStatsPrecisionApi(amplifier.getState().getOpticalReturnLoss());
                if (opticalReturnLoss != null) {
                    amplifierBuilder.setOpticalReturnLoss(new OpticalReturnLossBuilder(opticalReturnLoss).build());
                }
                amplifierBuilder.withKey(new OpticalAmplifierKey(amplifier.getName()));
                values.put(amplifierBuilder.key(), amplifierBuilder.build());
            }
            builder.setOpticalAmplifier(values);
        }
        if (opticalAmplifier.getSupervisoryChannels() != null && opticalAmplifier.getSupervisoryChannels().getSupervisoryChannel() != null) {
            Map<SupervisoryChannelKey, SupervisoryChannel> values =new HashMap<>(opticalAmplifier.getSupervisoryChannels().getSupervisoryChannel().size());

            for(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev230426.optical.amplifier.top.optical.amplifier.supervisory.channels.SupervisoryChannel supervisoryChannel:opticalAmplifier.getSupervisoryChannels().getSupervisoryChannel().values()){
                if(supervisoryChannel.getState()==null){
                    continue;
                }
                SupervisoryChannelBuilder supervisoryChannelBuilder=new SupervisoryChannelBuilder();
                supervisoryChannelBuilder.setEs(supervisoryChannel.getState().getEs());
                supervisoryChannelBuilder.setFees(supervisoryChannel.getState().getFees());
                supervisoryChannelBuilder.setFeses(supervisoryChannel.getState().getFeses());
                supervisoryChannelBuilder.setInterfaceName(supervisoryChannel.getState().getInterface());
                if(supervisoryChannel.getState().getOutputFrequency()!=null){
                    supervisoryChannelBuilder.setOutputFrequency(new FrequencyType(supervisoryChannel.getState().getOutputFrequency().getValue()));
                }
                supervisoryChannelBuilder.setRxDropMessageNum(supervisoryChannel.getState().getRxDropMessageNum());
                supervisoryChannelBuilder.setRxMessageNum(supervisoryChannel.getState().getRxMessageNum());
                supervisoryChannelBuilder.setTxMessageNum(supervisoryChannel.getState().getTxMessageNum());
                supervisoryChannelBuilder.setSes(supervisoryChannel.getState().getSes());
                supervisoryChannelBuilder.setUas(supervisoryChannel.getState().getUas());
                AvgMinMaxInstantStatsPrecision2DBm output = devToAvgMinMaxInstantStatsPrecisionApi(supervisoryChannel.getState().getOutputPower());
                if (output != null) {
                    supervisoryChannelBuilder.setOutputPower(new org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.line.common.rev220208.optical.osc.state.OutputPowerBuilder(output).build());
                }
                AvgMinMaxInstantStatsPrecision2DBm input = devToAvgMinMaxInstantStatsPrecisionApi(supervisoryChannel.getState().getInputPower());
                if (input != null) {
                    supervisoryChannelBuilder.setInputPower(new org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.line.common.rev220208.optical.osc.state.InputPowerBuilder(input).build());
                }
                AvgMinMaxInstantStatsPrecision2MA laserBiasCurrent = devToAvgMinMaxInstantStatsPrecisionApi(supervisoryChannel.getState().getLaserBiasCurrent());
                if (laserBiasCurrent != null) {
                    supervisoryChannelBuilder.setLaserBiasCurrent(new org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.line.common.rev220208.optical.osc.state.LaserBiasCurrentBuilder(laserBiasCurrent).build());
                }
                values.put(new SupervisoryChannelKey(supervisoryChannel.getInterface()),supervisoryChannelBuilder.build());
            }
            builder.setSupervisoryChannel(values);
        }
        return builder.build();
    }


    public Config apiToConfigDev(SetEdfaConfigInput input){
        var configBuilder = new ConfigBuilder();
        configBuilder.setTargetGain(input.getTargetGain());
        configBuilder.setTargetGainTilt(input.getTargetGainTilt());
        configBuilder.setAmpMode( apiToOpticalAmpTypeDev(input.getAmpMode()));
        configBuilder.setTargetOutputPower(input.getTargetOutputPower());
        var aprEnable = apiToAprEnableTypeDev(input.getAprEnable());
        if (Objects.isNull(aprEnable)) {
            configBuilder.setEnabled(input.getEnabled());
        } else {
            if (Objects.equals(AprEnableType.DISABLE.getIntValue(), aprEnable.getIntValue())) {
                configBuilder.setEnabled(input.getEnabled());
            }
        }
        configBuilder.setTargetVoaAttenuation(input.getTargetVoaAttenuation());
        configBuilder.setAprEnable(apiToAprEnableTypeDev(input.getAprEnable()));
        return configBuilder.build();
    }

}
