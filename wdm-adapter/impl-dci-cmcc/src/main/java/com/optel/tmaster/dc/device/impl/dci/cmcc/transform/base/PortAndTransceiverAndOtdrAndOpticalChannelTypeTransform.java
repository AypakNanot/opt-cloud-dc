/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base;

import com.optel.tmaster.dc.dci.impl.base.transform.ITransform;
import com.optel.tmaster.dc.general.base.exception.manage.NoMatchEnumValueException;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.ETHERNETSPEED;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.*;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.OPTICALAMPLIFIERMODE;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.OPTICALAMPLIFIERTYPE;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.optical.amplifier.state.ActualGainBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.ActionResult;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.OtdrFileFormatType;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.OtdrState;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.StartOtdrInputGrouping;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.otdr.result.event.Event;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.transceiver.rev220208.optical.power.state.*;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.line.common.rev220208.TransportLineCommonPortState;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.TRANSCEIVERPORTTYPE;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ethernet.rev230426.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev230426.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.*;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.StartOtdrInput;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.otdr.result.top.results.Result;

import java.util.HashSet;
import java.util.Set;

/**
 * @author Quan Jingyuan
 * @since 2022/3/7
 **/
public interface PortAndTransceiverAndOtdrAndOpticalChannelTypeTransform extends ITransform {


    /**
     * 通道速率dev to api
     *
     * @param clazz dev速率
     * @return api速率
     */
    default ETHERNETSPEED devToChannelSpeedApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ethernet.rev230426.ETHERNETSPEED clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ethernet.rev230426.ETHERNETSPEED> lnc = clazz.implementedInterface();
        if (lnc.equals(SPEED10GB.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEED10GB.VALUE;
        } else if (lnc.equals(SPEED25GB.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEED25GB.VALUE;
        } else if (lnc.equals(SPEED40GB.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEED40GB.VALUE;
        } else if (lnc.equals(SPEED50GB.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEED50GB.VALUE;
        } else if (lnc.equals(SPEED100GB.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEED100GB.VALUE;
        } else if (lnc.equals(SPEED200GB.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEED200GB.VALUE;
        } else if (lnc.equals(SPEED400GB.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEED400GB.VALUE;
        } else {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEEDUNKNOWN.VALUE;
        }
    }


    /**
     * 反转类型api to dev
     *
     * @param reverseMode api反转类型
     * @return dev反转类型
     */
//    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.port.rev230426.PortConfig.ReverseMode apiToReverseModeDev(PortConfig.ReverseMode reverseMode) {
//        if (reverseMode == null) {
//            return null;
//        }
//        switch (reverseMode) {
//            case AUTO:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.port.rev230426.PortConfig.ReverseMode.AUTO;
//            case NONE:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.port.rev230426.PortConfig.ReverseMode.NONE;
//            case MANUAL:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.port.rev230426.PortConfig.ReverseMode.MANUAL;
//            default:
//                throw NoMatchEnumValueException.getNoMatchEnumValueException(reverseMode);
//        }
//    }

    /**
     * 反转类型dev to api
     *
     * @param reverseMode dev反转类型
     * @return api反转类型
     */
    //极简OTN没有反转模式
//    default PortConfig.ReverseMode devToReverseModeApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.port.rev230426.PortConfig.ReverseMode reverseMode) {
//        if (reverseMode == null) {
//            return null;
//        }
//        switch (reverseMode) {
//            case AUTO:
//                return PortConfig.ReverseMode.AUTO;
//            case NONE:
//                return PortConfig.ReverseMode.NONE;
//            case MANUAL:
//                return PortConfig.ReverseMode.MANUAL;
//            default:
//                throw NoMatchEnumValueException.getNoMatchEnumValueException(reverseMode);
//        }
//    }

    /**
     * 端口安装类型Dev to api list
     *
     * @param classList dev
     * @return api
     */
    default Set<TRANSCEIVERPORTTYPE> devToUsedServicePortTypeApiList(Set<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRANSCEIVERPORTTYPE> classList) {
        if (classList == null || classList.size() == 0) {
            return null;
        }
        Set<TRANSCEIVERPORTTYPE> list = new HashSet<>();
        for (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRANSCEIVERPORTTYPE clazz : classList) {
            list.add(devToUsedServicePortTypeApi(clazz));
        }
        return list;
    }

    /**
     * 端口安装类型 api to Dev
     *
     * @param clazz api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRANSCEIVERPORTTYPE apiToUsedServicePortTypeDev(TRANSCEIVERPORTTYPE clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends TRANSCEIVERPORTTYPE> lnc = clazz.implementedInterface();
        if (lnc.equals(org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PTOTUC4.class)) {
            return PTOTUC4.VALUE;
        } else if (lnc.equals(org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PTOTUC2.class)) {
            return PTOTUC2.VALUE;
        } else if (lnc.equals(org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PTOTU4.class)) {
            return PTOTU4.VALUE;
        } else if (lnc.equals(org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PTOTU2.class)) {
            return PTOTU2.VALUE;
        } else if (lnc.equals(org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PTSTM64.class)) {
            return PTSTM64.VALUE;
        } else if (lnc.equals(org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT600G.class)) {
            return PT600G.VALUE;
        } else if (lnc.equals(org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT400G.class)) {
            return PT400G.VALUE;
        } else if (lnc.equals(org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT200G.class)) {
            return PT200G.VALUE;
        } else if (lnc.equals(org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT100GBASE.class)) {
            return PT100GBASE.VALUE;
        } else if (lnc.equals(org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT10GBASE.class)) {
            return PT10GBASE.VALUE;
        } else if (lnc.equals(org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT1000M.class)) {
            return PT1000M.VALUE;
        } else if (lnc.equals(org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT100M.class)) {
            return PT100M.VALUE;
        } else if (lnc.equals(org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT2M.class)) {
            return PT2M.VALUE;
        } else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(clazz);
        }
    }

    /**
     * 端口安装类型Dev to api
     *
     * @param clazz dev
     * @return api
     */
    default TRANSCEIVERPORTTYPE devToUsedServicePortTypeApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRANSCEIVERPORTTYPE clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRANSCEIVERPORTTYPE> lnc = clazz.implementedInterface();
        if (lnc.equals(PTOTUC4.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PTOTUC4.VALUE;
        } else if (lnc.equals(PTOTUC2.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PTOTUC2.VALUE;
        } else if (lnc.equals(PTOTU4.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PTOTU4.VALUE;
        } else if (lnc.equals(PTOTU2.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PTOTU2.VALUE;
        } else if (lnc.equals(PTSTM64.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PTSTM64.VALUE;
        } else if (lnc.equals(PT600G.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT600G.VALUE;
        } else if (lnc.equals(PT400G.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT400G.VALUE;
        } else if (lnc.equals(PT200G.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT200G.VALUE;
        } else if (lnc.equals(PT100GBASE.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT100GBASE.VALUE;
        } else if (lnc.equals(PT10GBASE.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT10GBASE.VALUE;
        } else if (lnc.equals(PT1000M.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT1000M.VALUE;
        } else if (lnc.equals(PT100M.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT100M.VALUE;
        } else if (lnc.equals(PT2M.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.PT2M.VALUE;
        } else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(lnc);
        }
    }


    /**
     * 实际插入模块的封装形式 dev to api
     *
     * @param clazz dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.TRANSCEIVERFORMFACTORTYPE devToFormFactorApi(TRANSCEIVERFORMFACTORTYPE clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends TRANSCEIVERFORMFACTORTYPE> lnc = clazz.implementedInterface();
        if (lnc.equals(CFP.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.CFP.VALUE;
        } else if (lnc.equals(CFP2.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.CFP2.VALUE;
        } else if (lnc.equals(CFP2ACO.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.CFP2ACO.VALUE;
        } else if (lnc.equals(CFP2DCO.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.CFP2DCO.VALUE;
        } else if (lnc.equals(CFP4.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.CFP4.VALUE;
        } else if (lnc.equals(QSFP.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.QSFP.VALUE;
        } else if (lnc.equals(QSFPPLUS.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.QSFPPLUS.VALUE;
        } else if (lnc.equals(QSFP28.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.QSFP28.VALUE;
        } else if (lnc.equals(SFP.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.SFP.VALUE;
        } else if (lnc.equals(SFPPLUS.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.SFPPLUS.VALUE;
        } else if (lnc.equals(XFP.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.XFP.VALUE;
        } else if (lnc.equals(NONPLUGGABLE.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.NONPLUGGABLE.VALUE;
        } else if (lnc.equals(OTHER.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.OTHER.VALUE;
        } else if (lnc.equals(QSFP56.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.QSFP56.VALUE;
        } else if (lnc.equals(QSFPDD.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.QSFPDD.VALUE;
        } else if(lnc.equals(TRANSCEIVERFORMFACTORTYPE.class)){
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.TRANSCEIVERFORMFACTORTYPE.VALUE;
        }else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(lnc);
        }
    }

    /**
     * 光纤接头类型 dev to api
     *
     * @param clazz dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.FIBERCONNECTORTYPE devToConnectorTypeApi(FIBERCONNECTORTYPE clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends FIBERCONNECTORTYPE> lnc = clazz.implementedInterface();
        if (lnc.equals(SCCONNECTOR.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.SCCONNECTOR.VALUE;
        } else if (lnc.equals(LCCONNECTOR.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.LCCONNECTOR.VALUE;
        } else if (lnc.equals(MPOCONNECTOR.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.MPOCONNECTOR.VALUE;
        } else if (lnc.equals(MTPCONNECTOR.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.MTPCONNECTOR.VALUE;
        } else if(lnc.equals(AOCCONNECTOR.class)){
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.AOCCONNECTOR.VALUE;
        } else if(lnc.equals(DACCONNECTOR.class)){
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.DACCONNECTOR.VALUE;
        } else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(lnc);
        }
    }


    /**
     * 测量模式 api to dev
     *
     * @param measureMode api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.otdr.result.top.Result.MeasureMode devToMeasureModeApi(Result.MeasureMode measureMode) {
        if (measureMode == null) {
            return null;
        }
        switch (measureMode) {
            case MANUAL:
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.otdr.result.top.Result.MeasureMode.MANUAL;
            case AUTO:
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.otdr.result.top.Result.MeasureMode.AUTO;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(measureMode);
        }
    }

    /**
     * 测量模式 api to dev
     *
     * @param measureMode api
     * @return dev
     */
    default StartOtdrInput.MeasureMode apiToMeasureModeDev(StartOtdrInputGrouping.MeasureMode measureMode) {
        if (measureMode == null) {
            return null;
        }
        switch (measureMode) {
            case MANUAL:
                return StartOtdrInput.MeasureMode.MANUAL;
            case AUTO:
                return StartOtdrInput.MeasureMode.AUTO;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(measureMode);
        }
    }

    /**
     * 文件类型 dev to api
     *
     * @param type dev
     * @return api
     */
    default OtdrFileFormatType devToOtdrFileFormatTypeApi(org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.OtdrFileFormatType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case GR196:
                return OtdrFileFormatType.GR196;
            case OTHER:
                return OtdrFileFormatType.OTHER;
            case SR4731:
                return OtdrFileFormatType.SR4731;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(type);
        }
    }

    /**
     * rpc结果 dev to api
     *
     * @param result dev
     * @return api
     */
    default ActionResult devToActionResultApi(org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.ActionResult result) {
        if (result == null) {
            return null;
        }
        switch (result) {
            case FAIL:
                return ActionResult.FAIL;
            case SUCCESS:
                return ActionResult.SUCCESS;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(result);
        }
    }

    /**
     * otdr测量类型 dev to api
     *
     * @param state dev
     * @return api
     */
    default OtdrState.MeasuringState devToMeasuringStateApi(org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.OtdrState.MeasuringState state) {
        if (state == null) {
            return null;
        }
        switch (state) {
            case FAIL:
                return OtdrState.MeasuringState.FAIL;
            case RUNNING:
                return OtdrState.MeasuringState.RUNNING;
            case FINISHED:
                return OtdrState.MeasuringState.FINISHED;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(state);
        }
    }

    /**
     * 输出功率 dev to api
     *
     * @param outputPower dev
     * @return api
     */
    default OutputPower devToOutputPowerApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev230426.optical.power.state.OutputPower outputPower) {
        if (outputPower == null) {
            return null;
        }
        OutputPowerBuilder builder = new OutputPowerBuilder();
        builder.setAvg(outputPower.getAvg());
        builder.setInstant(outputPower.getInstant());
        builder.setMax(outputPower.getMax());
        builder.setMin(outputPower.getMin());
        if (outputPower.getMinTime() != null) {
            builder.setMinTime(new Timeticks64(outputPower.getMinTime().getValue()));
        }
        if (outputPower.getMaxTime() != null) {
            builder.setMaxTime(new Timeticks64(outputPower.getMaxTime().getValue()));
        }
        if (outputPower.getInterval() != null) {
            builder.setInterval(new StatInterval(outputPower.getInterval().getValue()));
        }

        return builder.build();
    }

    /**
     * 输入功率 api to dev
     *
     * @param inputPower api
     * @return dev
     */
    default InputPower devToInputPowerApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.transceiver.rev230426.optical.power.state.InputPower inputPower) {
        if (inputPower == null) {
            return null;
        }
        InputPowerBuilder builder = new InputPowerBuilder();
        builder.setMax(inputPower.getMax());
        builder.setAvg(inputPower.getAvg());
        builder.setMin(inputPower.getMin());
        builder.setInstant(inputPower.getInstant());
        if (inputPower.getMinTime() != null) {
            builder.setMinTime(new Timeticks64(inputPower.getMinTime().getValue()));
        }
        if (inputPower.getInterval() != null) {
            builder.setInterval(new StatInterval(inputPower.getInterval().getValue()));
        }
        if (inputPower.getMaxTime() != null) {
            builder.setMaxTime(new Timeticks64(inputPower.getMaxTime().getValue()));
        }

        return builder.build();
    }

    /**
     * 放大器类型 dev to api
     *
     * @param clazz dev
     * @return api
     */
    default OPTICALAMPLIFIERTYPE devToOpticalAmpTypeApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev230426.OPTICALAMPLIFIERTYPE clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev230426.OPTICALAMPLIFIERTYPE> lnc = clazz.implementedInterface();
        if (lnc.equals(EDFA.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.EDFA.VALUE;
        } else if (lnc.equals(FORWARDRAMAN.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.FORWARDRAMAN.VALUE;
        } else if (lnc.equals(BACKWARDRAMAN.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.BACKWARDRAMAN.VALUE;
        } else if (lnc.equals(HYBRID.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.HYBRID.VALUE;
        } else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(clazz);
        }
    }

    /**
     * 放大模式 api to dev
     *
     * @param clazz api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev230426.OPTICALAMPLIFIERMODE apiToOpticalAmpTypeDev(OPTICALAMPLIFIERMODE clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends OPTICALAMPLIFIERMODE> lnc = clazz.implementedInterface();
        if (lnc.equals(org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.CONSTANTPOWER.class)) {
            return CONSTANTPOWER.VALUE;
        } else if (lnc.equals(org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.CONSTANTGAIN.class)) {
            return CONSTANTGAIN.VALUE;
        } else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(clazz);
        }
    }

    /**
     * 放大模式 dev to api
     *
     * @param clazz dev
     * @return api
     */
    default OPTICALAMPLIFIERMODE devToOpticalAmpLifierModeApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev230426.OPTICALAMPLIFIERMODE clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.amplifier.rev230426.OPTICALAMPLIFIERMODE> lnc = clazz.implementedInterface();
        if (lnc.equals(CONSTANTGAIN.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.CONSTANTGAIN.VALUE;
        } else if (lnc.equals(CONSTANTPOWER.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.amplifier.rev220208.CONSTANTPOWER.VALUE;
        } else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(clazz);
        }
    }

    /**
     * apr使能 dev to api
     *
     * @param aprEnableType dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.AprEnableType devToAprEnableTypeApi(AprEnableType aprEnableType) {
        if (aprEnableType == null) {
            return null;
        }
        switch (aprEnableType) {
            case ENABLE:
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.AprEnableType.ENABLE;
            case DISABLE:
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.AprEnableType.DISABLE;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(aprEnableType);
        }
    }

    /**
     * APR使能 api to dev
     *
     * @param type api
     * @return dev
     */
    default AprEnableType apiToAprEnableTypeDev(org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.AprEnableType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case DISABLE:
                return AprEnableType.DISABLE;
            case ENABLE:
                return AprEnableType.ENABLE;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(type);
        }
    }

    /**
     * MonType dev to api
     *
     * @param monType dev
     * @return api
     */
    default TransportLineCommonPortState.MonType devToMonTypeApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.line.common.rev230426.TransportLineCommonPortState.MonType monType) {
        if (monType == null) {
            return null;
        }
        switch (monType) {
            case OCM:
                return TransportLineCommonPortState.MonType.OCM;
            case OTDR:
                return TransportLineCommonPortState.MonType.OTDR;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(monType);
        }
    }

    /**
     * 端口类型 dev to api
     *
     * @param clazz dev
     * @return api
     */
    /**
     * 端口类型 dev to api
     *
     * @param clazz dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.OPTICALPORTTYPE devToOpticalPortType(OPTICALPORTTYPE clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends OPTICALPORTTYPE> lnc = clazz.implementedInterface();
        if (BI.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.BI.VALUE;
        } else if (INGRESS.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.INGRESS.VALUE;
        } else if (EGRESS.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.EGRESS.VALUE;
        } else if (ADD.class.equals(lnc) ) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.ADD.VALUE;
        } else if (DROP.class.equals(lnc) ) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.DROP.VALUE;
        } else if (MONITOR.class.equals(lnc) ) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.MONITOR.VALUE;
        } else if (TERMINALCLIENT.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.TERMINALCLIENT.VALUE;
        } else if (TERMINALLINE.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.TERMINALLINE.VALUE;
        } else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(lnc);
        }
    }

    /**
     * AvgMinMaxInstantStatsPrecision2DBm dev to api
     *
     * @param data dev
     * @return api
     */
    default AvgMinMaxInstantStatsPrecision2DBm devToAvgMinMaxInstantStatsPrecisionApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.AvgMinMaxInstantStatsPrecision2DBm data) {
        if (data == null) {
            return null;
        }
        InputPowerBuilder builder = new InputPowerBuilder();
        builder.setMin(data.getMin());
        builder.setInstant(data.getInstant());
        builder.setMax(data.getMax());
        builder.setAvg(data.getAvg());
        if (data.getMaxTime() != null) {
            builder.setMaxTime(new Timeticks64(data.getMaxTime().getValue()));
        }
        if (data.getMinTime() != null) {
            builder.setMinTime(new Timeticks64(data.getMinTime().getValue()));
        }
        if (data.getInterval() != null) {
            builder.setInterval(new StatInterval(data.getInterval().getValue()));
        }
        return builder.build();
    }

    /**
     * AvgMinMaxInstantStatsPrecision2MA dev to api
     *
     * @param data dev
     * @return api
     */
    default AvgMinMaxInstantStatsPrecision2MA devToAvgMinMaxInstantStatsPrecisionApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.AvgMinMaxInstantStatsPrecision2MA data) {
        if (data == null) {
            return null;
        }
        TecCurrentBuilder builder = new TecCurrentBuilder();
        builder.setAvg(data.getAvg());
        builder.setInstant(data.getInstant());
        builder.setMax(data.getMax());
        builder.setMin(data.getMin());
        if (data.getMaxTime() != null) {
            builder.setMaxTime(new Timeticks64(data.getMaxTime().getValue()));
        }
        if (data.getInterval() != null) {
            builder.setInterval(new StatInterval(data.getInterval().getValue()));
        }
        if (data.getMinTime() != null) {
            builder.setMinTime(new Timeticks64(data.getMinTime().getValue()));
        }
        return builder.build();
    }

    /**
     * AvgMinMaxInstantStatsPrecision2DB dev to api
     *
     * @param data dev
     * @return api
     */
    default AvgMinMaxInstantStatsPrecision2DB devToAvgMinMaxInstantStatsPrecisionApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.AvgMinMaxInstantStatsPrecision2DB data) {
        if (data == null) {
            return null;
        }
        ActualGainBuilder builder = new ActualGainBuilder();
        builder.setAvg(data.getAvg());
        builder.setInstant(data.getInstant());
        builder.setMax(data.getMax());
        builder.setMin(data.getMin());
        if (data.getInterval() != null) {
            builder.setInterval(new StatInterval(data.getInterval().getValue()));
        }
        if (data.getMinTime() != null) {
            builder.setMinTime(new Timeticks64(data.getMinTime().getValue()));
        }
        if (data.getMaxTime() != null) {
            builder.setMaxTime(new Timeticks64(data.getMaxTime().getValue()));
        }
        return builder.build();
    }
    /**
     * EventType dev to api
     *
     * @param eventType dev
     * @return api
     */
    default Event.EventType devToEventTypeApi(org.opendaylight.yang.gen.v1.urn.cmcc.yang.otdr.rev230426.otdr.result.event.events.Event.EventType eventType) {
        if (eventType == null) {
            return null;
        }
        switch (eventType) {
            case ENDOFFIBER:
                return Event.EventType.ENDOFFIBER;
            case REFLECTION:
                return Event.EventType.REFLECTION;
            case BENDINGLOSS:
                return Event.EventType.BENDINGLOSS;
            case NEGATIVESPLICELOSS:
                return Event.EventType.NEGATIVESPLICELOSS;
            case POSITIVESPLICELOSS:
                return Event.EventType.POSITIVESPLICELOSS;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(eventType);
        }
    }
}
