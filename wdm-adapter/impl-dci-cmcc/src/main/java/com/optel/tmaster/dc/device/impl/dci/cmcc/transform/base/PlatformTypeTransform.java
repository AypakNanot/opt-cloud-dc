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
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.lldp.types.rev220208.ChassisIdType;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.lldp.types.rev220208.PortIdType;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.types.rev220208.*;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.rpc.rev220208.RebootInputGrouping;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.TerminalEthernetProtocolConfig;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.TerminalLogicalChanAssignmentConfig;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.TerminalLogicalChannelState;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.transport.types.rev220208.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.chassis.rev200630.ChassisConfig;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.me.rev230426.MeState;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.RebootInput;

/**
 * PlatFormTypeTransform
 * platform-types yang中的数据类型转换
 * date       time        author
 * ─────────────────────────────
 * 2022/2/9   10:47      liwenxue
 * Copyright (c) 2022, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public interface PlatformTypeTransform extends ITransform,AlarmTypeTransform{

    /**
     * 设备名称转换
     * @param partNo 设备名称
     * @return 网管名称
     */
    default String devPartNoToApi(String partNo){
        if(partNo==null || partNo.isEmpty()){
            return partNo;
        }
        if("DCI2.0".equals(partNo)){
            return "MssEdge 25-D11";
        }
        if("DCI3.0".equals(partNo)){
            return "MssEdge 25-D12";
        }
        if("OPC4".equals(partNo)){
            return "MssEdge 25-D31";
        }
        if("119-0015-S0801-A02".equals(partNo)){
            return "MssEdge 25-D2";
        }
        if("119-0015-S0801-A01".equals(partNo)){
            return "MssEdge 25-D2";
        }
        if("119-0015-S0801-A03".equals(partNo)){
            return "MssEdge 25-D2";
        }
        if("119-0015-S0801-A04".equals(partNo)){
            return "MssEdge 25-D2";
        }
        if ("119-0015-S0801-C02".equals(partNo)) {
            return "MssEdge 25";
        }
        return partNo;
    }

    /**
     * direction dev to api
     * @param direction dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.chassis.rev220208.ChassisConfig.Direction devDirectionToApi(ChassisConfig.Direction direction){
        if(direction==null){
            return null;
        }
        switch (direction){
            case EAST:
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.chassis.rev220208.ChassisConfig.Direction.EAST;
            case WEST:
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.chassis.rev220208.ChassisConfig.Direction.WEST;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(direction);
        }
    }

    /**
     * type dev to api
     * @param type dev
     * @return api
     */
    default OPENCONFIGCOMPONENT devTypeToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.OPENCONFIGCOMPONENT type){
        if(type==null){
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.OPENCONFIGCOMPONENT> lnc = type.implementedInterface();
        //极简OTN 新增
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.ME.class.equals(lnc)){
            return ME.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.CHASSIS.class.equals(lnc)){
            return CHASSIS.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.POWERSUPPLY.class.equals(lnc)){
            return POWERSUPPLY.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.FAN.class.equals(lnc)){
            return FAN.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.CONTROLLERCARD.class.equals(lnc)){
            return CONTROLLERCARD.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.LINECARD.class.equals(lnc)){
            return LINECARD.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.OLP.class.equals(lnc)){
            return OLP.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.OTDRCARD.class.equals(lnc)){
            return OTDRCARD.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.OCMCARD.class.equals(lnc)){
            return OCMCARD.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.OA.class.equals(lnc)){
            return OA.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.OLA.class.equals(lnc)){
            return OLA.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.MUX.class.equals(lnc)){
            return MUX.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.TFF.class.equals(lnc)){
            return TFF.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.WSS.class.equals(lnc)){
            return WSS.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.PTME.class.equals(lnc)){
            return PTME.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.PTMW.class.equals(lnc)){
            return PTMW.VALUE;
        }

        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.SLOT.class.equals(lnc)){
            return SLOT.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.APS.class.equals(lnc)){
            return APS.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.EDFA.class.equals(lnc)){
            return EDFA.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.OCM.class.equals(lnc)){
            return OCM.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.OTDR.class.equals(lnc)){
            return OTDR.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.PTM.class.equals(lnc)){
            return PTM.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.TRANSCEIVER.class.equals(lnc)){
            return TRANSCEIVER.VALUE;
        }
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.PORT.class.equals(lnc)){
            return PORT.VALUE;
        }
       if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.OPTICALCHANNEL.class.equals(lnc)){
           return OPTICALCHANNEL.VALUE;
       }
        throw NoMatchEnumValueException.getNoMatchEnumValueException(type);
    }

    /**
     * OperStatus dev to api
     *
     * @param operStatus dev
     * @return api
     */
    default COMPONENTOPERSTATUS devOperStatusToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.COMPONENTOPERSTATUS operStatus) {
        if (operStatus == null) {
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.COMPONENTOPERSTATUS> lnc = operStatus.implementedInterface();
        if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.ACTIVE.class.equals(lnc)) {
            return ACTIVE.VALUE;
        }
        if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.INACTIVE.class.equals(lnc)) {
            return INACTIVE.VALUE;
        }
        if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.DISABLED.class.equals(lnc)) {
            return DISABLED.VALUE;
        }
        throw NoMatchEnumValueException.getNoMatchEnumValueException(operStatus);

    }

    /**
     * adminStateType dev to api
     *
     * @param adminStateType dev
     * @return api
     */
    default AdminStateType devAdminStateTypeToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.AdminStateType adminStateType) {
        if (adminStateType == null) {
            return null;
        }
        switch (adminStateType) {
            case MAINT:
                return AdminStateType.MAINT;
            case ENABLED:
                return AdminStateType.ENABLED;
            case DISABLED:
                return AdminStateType.DISABLED;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(adminStateType);
        }
    }

    /**
     * 此方法特别地兼容component下的adminState，由于 V1设备有时会上报非法空字符串，不在admin-state枚举值范围内
     * 为了兼容，将其类型改为了字符串
     *  adminStateType dev to api
     * @param adminStateType dev
     * @return api
     */
    default AdminStateType devAdminStateTypeToApi(String adminStateType) {
        if (adminStateType == null || adminStateType.isEmpty()) {
            return null;
        }
        if(AdminStateType.MAINT.getName().equals(adminStateType)){
            return AdminStateType.MAINT;
        }
        if(AdminStateType.ENABLED.getName().equals(adminStateType)){
            return AdminStateType.ENABLED;
        }
        if(AdminStateType.DISABLED.getName().equals(adminStateType)){
            return AdminStateType.DISABLED;
        }
        throw NoMatchEnumValueException.getNoMatchEnumValueException(adminStateType);
    }

    /**
     * adminStateType api to dev
     *
     * @param adminStateType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.AdminStateType
    apiAdminStateTypeToDev(AdminStateType adminStateType) {
        if (adminStateType == null) {
            return null;
        }
        switch (adminStateType) {
            case DISABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.AdminStateType.DISABLED;
            case ENABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.AdminStateType.ENABLED;
            case MAINT:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.AdminStateType.MAINT;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(adminStateType);
        }
    }

    /**
     * 此方法特别地兼容component下的adminState，由于 V1设备有时会上报非法空字符串，不在admin-state枚举值范围内
     * 为了兼容，将其类型改为了字符串
     *  adminStateType api to dev
     * @param adminStateType api
     * @return dev String
     */
    default String apiAdminStateTypeToDevString(AdminStateType adminStateType){
        if (adminStateType == null) {
            return null;
        }
        switch (adminStateType) {
            case DISABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.AdminStateType.DISABLED.getName();
            case ENABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.AdminStateType.ENABLED.getName();
            case MAINT:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.AdminStateType.MAINT.getName();
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(adminStateType);
        }
    }
    default ComponentChassisType devDeviceTypeToApi(MeState.DeviceType type) {
        if (type == null) {
            return null;
        }
        switch (type){
            case E:
                return ComponentChassisType.E;
            case O:
                return ComponentChassisType.O;
            case EO:
                return ComponentChassisType.EO;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(type);
        }
    }

    default ComponentChassisType devComponentChassisTypeToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.ComponentChassisType componentChassisType) {
        if (componentChassisType == null) {
            return null;
        }
        switch (componentChassisType) {
            case E:
                return ComponentChassisType.E;
            case O:
                return ComponentChassisType.O;
            case EO:
                return ComponentChassisType.EO;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(componentChassisType);
        }
    }

    default ChannelIntervalType devChannelIntervalTypeToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.ChannelIntervalType channelIntervalType) {
        if (channelIntervalType == null) {
            return null;
        }
        switch (channelIntervalType) {
            case CHANNEL50G:
                return ChannelIntervalType.CHANNEL50G;
            case CHANNEL75G:
                return ChannelIntervalType.CHANNEL75G;
            case CHANNEL100G:
                return ChannelIntervalType.CHANNEL100G;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(channelIntervalType);
        }
    }

    /**
     * 光通路间隔 api to dev
     *
     * @param channelIntervalType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.ChannelIntervalType apiChannelIntervalTypeToDev(ChannelIntervalType channelIntervalType) {
        if (channelIntervalType == null) {
            return null;
        }
        switch (channelIntervalType) {
            case CHANNEL50G:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.ChannelIntervalType.CHANNEL50G;
            case CHANNEL75G:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.ChannelIntervalType.CHANNEL75G;
            case CHANNEL100G:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.ChannelIntervalType.CHANNEL100G;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(channelIntervalType);
        }
    }

    /**
     * 复位类型 api to dev
     *
     * @param rebootType api
     * @return dev
     */
    default RebootInput.RebootType apiToRebootTypeDev(RebootInputGrouping.RebootType rebootType) {
        if (rebootType == null) {
            return null;
        }
        switch (rebootType) {
            case Cold:
                return RebootInput.RebootType.Cold;
            case Warm:
                return RebootInput.RebootType.Warm;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(rebootType);
        }
    }

    /**
     * 东西向 api to dev
     *
     * @param direction api
     * @return dev
     */
    default ChassisConfig.Direction apiToDirectionDev(org.opendaylight.yang.gen.v1.com.optel.dci.yang.platform.chassis.rev220208.ChassisConfig.Direction direction) {
        if (direction == null) {
            return null;
        }
        switch (direction) {
            case EAST:
                return ChassisConfig.Direction.EAST;
            case WEST:
                return ChassisConfig.Direction.WEST;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(direction);
        }
    }

    /**
     * 光通路间隔 dev to api
     *
     * @param type dev
     * @return api
     */
    default ChannelIntervalType devToChannelIntervalTypeApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.platform.types.rev230426.ChannelIntervalType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case CHANNEL100G:
                return ChannelIntervalType.CHANNEL100G;
            case CHANNEL75G:
                return ChannelIntervalType.CHANNEL75G;
            case CHANNEL50G:
                return ChannelIntervalType.CHANNEL50G;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(type);
        }

    }

    /**
     * 时延模式 dev to api
     *
     * @param type dev
     * @return api
     */
    default DelayTestModeType devToDelayTestModeApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.DelayTestModeType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case FAR:
                return DelayTestModeType.FAR;
            case NEAR:
                return DelayTestModeType.NEAR;
            case DISABLED:
                return DelayTestModeType.DISABLED;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(type);

        }
    }

    default DelayTestModeType devToDelayTestModeApi(String type) {
        if (type == null || "".equals(type)) {
            return null;
        }
        if (DelayTestModeType.FAR.getName().equals(type)) {
            return DelayTestModeType.FAR;
        }
        if (DelayTestModeType.NEAR.getName().equals(type)) {
            return DelayTestModeType.NEAR;
        }
        if (DelayTestModeType.DISABLED.getName().equals(type)) {
            return DelayTestModeType.DISABLED;
        }
        throw NoMatchEnumValueException.getNoMatchEnumValueException(type);
    }

    /**
     * 时延模式 api to dev
     *
     * @param type api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.DelayTestModeType apiToDelayTestModeDev(DelayTestModeType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case FAR:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.DelayTestModeType.FAR;
            case NEAR:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.DelayTestModeType.NEAR;
            case DISABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.DelayTestModeType.DISABLED;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(type);

        }
    }

    default String apiToDelayTestModeDevString(DelayTestModeType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case FAR:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.DelayTestModeType.FAR.getName();
            case NEAR:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.DelayTestModeType.NEAR.getName();
            case DISABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.DelayTestModeType.DISABLED.getName();
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(type);
        }
    }
    /**
     * 链路状态 dev to api
     *
     * @param state dev
     * @return api
     */
    default TerminalLogicalChannelState.LinkState devToLinkStateApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.TerminalLogicalChannelState.LinkState state) {
        if (state == null) {
            return null;
        }
        switch (state) {
            case UP:
                return TerminalLogicalChannelState.LinkState.UP;
            case DOWN:
                return TerminalLogicalChannelState.LinkState.DOWN;
            case TESTING:
                return TerminalLogicalChannelState.LinkState.TESTING;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(state);
        }

    }
    /**
     * 通道层协议 api to dev
     *
     * @param clazz api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.LOGICALELEMENTPROTOCOLTYPE apiToLogicalChannelTypeDev(LOGICALELEMENTPROTOCOLTYPE clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends LOGICALELEMENTPROTOCOLTYPE> lnc = clazz.implementedInterface();
        if (PROTETHERNET.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTETHERNET.VALUE;
        } else if (PROTOTU.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOTU.VALUE;
        } else if (PROTODU.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTODU.VALUE;
        } else if (PROTSDH.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTSDH.VALUE;
        } else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(clazz);
        }

    }
    /**
     * 通道层协议 dev to api
     *
     * @param clazz dev
     * @return api
     */
    default LOGICALELEMENTPROTOCOLTYPE devToLogicalChannelTypeApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.LOGICALELEMENTPROTOCOLTYPE clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.LOGICALELEMENTPROTOCOLTYPE> lnc = clazz.implementedInterface();
        if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTETHERNET.class.equals(lnc)) {
            return PROTETHERNET.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOTU.class.equals(lnc)) {
            return PROTOTU.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTODU.class.equals(lnc)) {
            return PROTODU.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTSDH.class.equals(lnc)) {
            return PROTSDH.VALUE;
        } else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(clazz);
        }

    }

    /**
     * 环回模式 dev to api
     *
     * @param type dev
     * @return api
     */
    default LoopbackModeType devToLoopbackModeTypeApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.LoopbackModeType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case NONE:
                return LoopbackModeType.NONE;
            case FACILITY:
                return LoopbackModeType.FACILITY;
            case TERMINAL:
                return LoopbackModeType.TERMINAL;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(type);
        }
    }
    /**
     * 管理状态 api to dev
     *
     * @param type api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.AdminStateType apiToAdminStateDev(AdminStateType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case ENABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.AdminStateType.ENABLED;
            case DISABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.AdminStateType.DISABLED;
            case MAINT:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.AdminStateType.MAINT;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(type);
        }
    }
    /**
     * 环回模式 api to dev
     *
     * @param type api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.LoopbackModeType apiToLoopbackModeTypeDev(LoopbackModeType type) {
        if (type == null) {
            return null;
        }
        switch (type) {
            case NONE:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.LoopbackModeType.NONE;
            case FACILITY:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.LoopbackModeType.FACILITY;
            case TERMINAL:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.LoopbackModeType.TERMINAL;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(type);
        }
    }
    /**
     * RateClass api to dev
     *
     * @param clazz api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBUTARYRATECLASSTYPE apiToRateClassDev(TRIBUTARYRATECLASSTYPE clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends TRIBUTARYRATECLASSTYPE> lnc = clazz.implementedInterface();
        if (TRIBRATE1G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE1G.VALUE;
        } else if (TRIBRATE10G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE10G.VALUE;
        } else if (TRIBRATE25G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE25G.VALUE;
        } else if (TRIBRATE40G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE40G.VALUE;
        } else if (TRIBRATE100G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE100G.VALUE;
        } else if (TRIBRATE150G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE150G.VALUE;
        } else if (TRIBRATE200G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE200G.VALUE;
        } else if (TRIBRATE250G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE250G.VALUE;
        } else if (TRIBRATE300G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE300G.VALUE;
        } else if (TRIBRATE350G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE350G.VALUE;
        } else if (TRIBRATE400G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE400G.VALUE;
        } else if (TRIBRATE450G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE450G.VALUE;
        } else if (TRIBRATE500G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE500G.VALUE;
        } else if (TRIBRATE550G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE550G.VALUE;
        } else if (TRIBRATE600G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE600G.VALUE;
        } else if (TRIBRATE650G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE650G.VALUE;
        } else if (TRIBRATE700G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE700G.VALUE;
        } else if (TRIBRATE750G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE750G.VALUE;
        } else if (TRIBRATE800G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE800G.VALUE;
        } else if (TRIBRATE850G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE850G.VALUE;
        } else if (TRIBRATE900G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE900G.VALUE;
        } else if (TRIBRATE950G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE950G.VALUE;
        } else if (TRIBRATE1000G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE1000G.VALUE;
        } else if (TRIBRATE1050G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE1050G.VALUE;
        } else if (TRIBRATE1100G.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE1100G.VALUE;
        } else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(clazz);
        }
    }
    /**
     * RateClass dev to api
     *
     * @param clazz dev
     * @return api
     */
    default TRIBUTARYRATECLASSTYPE devToRateClassApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBUTARYRATECLASSTYPE clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBUTARYRATECLASSTYPE> lnc = clazz.implementedInterface();
        if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE1G.class.equals(lnc)) {
            return TRIBRATE1G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE10G.class.equals(lnc)) {
            return TRIBRATE10G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE25G.class.equals(lnc)) {
            return TRIBRATE25G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE40G.class.equals(lnc)) {
            return TRIBRATE40G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE100G.class.equals(lnc)) {
            return TRIBRATE100G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE150G.class.equals(lnc)) {
            return TRIBRATE150G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE200G.class.equals(lnc)) {
            return TRIBRATE200G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE250G.class.equals(lnc)) {
            return TRIBRATE250G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE300G.class.equals(lnc)) {
            return TRIBRATE300G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE350G.class.equals(lnc)) {
            return TRIBRATE350G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE400G.class.equals(lnc)) {
            return TRIBRATE400G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE450G.class.equals(lnc)) {
            return TRIBRATE450G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE500G.class.equals(lnc)) {
            return TRIBRATE500G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE550G.class.equals(lnc)) {
            return TRIBRATE550G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE600G.class.equals(lnc)) {
            return TRIBRATE600G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE650G.class.equals(lnc)) {
            return TRIBRATE650G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE700G.class.equals(lnc)) {
            return TRIBRATE700G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE750G.class.equals(lnc)) {
            return TRIBRATE750G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE800G.class.equals(lnc)) {
            return TRIBRATE800G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE850G.class.equals(lnc)) {
            return TRIBRATE850G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE900G.class.equals(lnc)) {
            return TRIBRATE900G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE950G.class.equals(lnc)) {
            return TRIBRATE950G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE1000G.class.equals(lnc)) {
            return TRIBRATE1000G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE1050G.class.equals(lnc)) {
            return TRIBRATE1050G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBRATE1100G.class.equals(lnc)) {
            return TRIBRATE1100G.VALUE;
        } else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(clazz);
        }
    }
    /**
     * TribProtocol api to dev
     *
     * @param clazz api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBUTARYPROTOCOLTYPE apiToTribProtocolDev(TRIBUTARYPROTOCOLTYPE clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends TRIBUTARYPROTOCOLTYPE> lnc = clazz.implementedInterface();
        if (PROT1GE.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROT1GE.VALUE;
        } else if (PROTOC48.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOC48.VALUE;
        } else if (PROTSTM16.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTSTM16.VALUE;
        } else if (PROT10GELAN.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROT10GELAN.VALUE;
        } else if (PROT10GEWAN.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROT10GEWAN.VALUE;
        } else if (PROTOC192.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOC192.VALUE;
        } else if (PROTSTM64.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTSTM64.VALUE;
        } else if (PROTOTU2.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOTU2.VALUE;
        } else if (PROTOTU2E.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOTU2E.VALUE;
        } else if (PROTOTU1E.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOTU1E.VALUE;
        } else if (PROTODU2.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTODU2.VALUE;
        } else if (PROTODU2E.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTODU2E.VALUE;
        } else if (PROT40GE.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROT40GE.VALUE;
        } else if (PROTOC768.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOC768.VALUE;
        } else if (PROTSTM256.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTSTM256.VALUE;
        } else if (PROTOTU3.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOTU3.VALUE;
        } else if (PROTODU3.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTODU3.VALUE;
        } else if (PROT100GE.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROT100GE.VALUE;
        } else if (PROT100GMLG.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROT100GMLG.VALUE;
        } else if (PROTOTU4.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOTU4.VALUE;
        } else if (PROTOTUCN.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOTUCN.VALUE;
        } else if (PROTODUCN.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTODUCN.VALUE;
        } else if (PROTODU4.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTODU4.VALUE;
        } else if (PROT400GE.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROT400GE.VALUE;
        } else if (PROTNA.class.equals(lnc)) {
            return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTNA.VALUE;
        } else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(clazz);
        }
    }
    /**
     * TribProtocol dev to api
     *
     * @param clazz dev
     * @return api
     */
    default TRIBUTARYPROTOCOLTYPE devToTribProtocolApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBUTARYPROTOCOLTYPE clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBUTARYPROTOCOLTYPE> lnc = clazz.implementedInterface();
        if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROT1GE.class.equals(lnc)) {
            return PROT1GE.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOC48.class.equals(lnc)) {
            return PROTOC48.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTSTM16.class.equals(lnc)) {
            return PROTSTM16.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROT10GELAN.class.equals(lnc)) {
            return PROT10GELAN.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROT10GEWAN.class.equals(lnc)) {
            return PROT10GEWAN.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOC192.class.equals(lnc)) {
            return PROTOC192.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTSTM64.class.equals(lnc)) {
            return PROTSTM64.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOTU2.class.equals(lnc)) {
            return PROTOTU2.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOTU2E.class.equals(lnc)) {
            return PROTOTU2E.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOTU1E.class.equals(lnc)) {
            return PROTOTU1E.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTODU2.class.equals(lnc)) {
            return PROTODU2.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTODU2E.class.equals(lnc)) {
            return PROTODU2E.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROT40GE.class.equals(lnc)) {
            return PROT40GE.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOC768.class.equals(lnc)) {
            return PROTOC768.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTSTM256.class.equals(lnc)) {
            return PROTSTM256.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOTU3.class.equals(lnc)) {
            return PROTOTU3.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTODU3.class.equals(lnc)) {
            return PROTODU3.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROT100GE.class.equals(lnc)) {
            return PROT100GE.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROT100GMLG.class.equals(lnc)) {
            return PROT100GMLG.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOTU4.class.equals(lnc)) {
            return PROTOTU4.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTOTUCN.class.equals(lnc)) {
            return PROTOTUCN.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTODUCN.class.equals(lnc)) {
            return PROTODUCN.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTODU4.class.equals(lnc)) {
            return PROTODU4.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROT400GE.class.equals(lnc)) {
            return PROT400GE.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTNA.class.equals(lnc)) {
            return PROTNA.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.PROTUNASSIGN.class.equals(lnc)) {
            return PROTUNASSIGN.VALUE;
        } else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(clazz);
        }
    }

    /**
     * clientAls dev to api
     *
     * @param clientAls dev
     * @return api
     */
    default TerminalEthernetProtocolConfig.ClientAls devToClientAlsApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.TerminalEthernetProtocolConfig.ClientAls clientAls) {
        if (clientAls == null) {
            return null;
        }
        switch (clientAls) {
            case NONE:
                return TerminalEthernetProtocolConfig.ClientAls.NONE;
            case ETHERNET:
                return TerminalEthernetProtocolConfig.ClientAls.ETHERNET;
            case LASERSHUTDOWN:
                return TerminalEthernetProtocolConfig.ClientAls.LASERSHUTDOWN;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(clientAls);
        }
    }

    /**
     * clientFec dev to api
     *
     * @param clientFec dev
     * @return api
     */
    default TerminalEthernetProtocolConfig.ClientFec devToClientFecApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.TerminalEthernetProtocolConfig.ClientFec clientFec) {
        if (clientFec == null) {
            return null;
        }
        switch (clientFec) {
            case DISABLED:
                return TerminalEthernetProtocolConfig.ClientFec.DISABLED;
            case ENABLED:
                return TerminalEthernetProtocolConfig.ClientFec.ENABLED;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(clientFec);
        }
    }

    /**
     * chassisIdType dev to api
     *
     * @param chassisIdType dev
     * @return api
     */
    default ChassisIdType devToChassisIdTypeApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.types.rev181121.ChassisIdType chassisIdType) {
        if (chassisIdType == null) {
            return null;
        }
        switch (chassisIdType) {
            case LOCAL:
                return ChassisIdType.LOCAL;
            case MACADDRESS:
                return ChassisIdType.MACADDRESS;
            case INTERFACENAME:
                return ChassisIdType.INTERFACENAME;
            case PORTCOMPONENT:
                return ChassisIdType.PORTCOMPONENT;
            case INTERFACEALIAS:
                return ChassisIdType.INTERFACEALIAS;
            case NETWORKADDRESS:
                return ChassisIdType.NETWORKADDRESS;
            case CHASSISCOMPONENT:
                return ChassisIdType.CHASSISCOMPONENT;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(chassisIdType);
        }

    }

    /**
     * portIdType dev to api
     *
     * @param portIdType dev
     * @return api
     */
    default PortIdType devToPortIdTypeApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.lldp.types.rev181121.PortIdType portIdType) {
        if (portIdType == null) {
            return null;
        }
        switch (portIdType) {
            case NETWORKADDRESS:
                return PortIdType.NETWORKADDRESS;
            case INTERFACEALIAS:
                return PortIdType.INTERFACEALIAS;
            case PORTCOMPONENT:
                return PortIdType.PORTCOMPONENT;
            case INTERFACENAME:
                return PortIdType.INTERFACENAME;
            case MACADDRESS:
                return PortIdType.MACADDRESS;
            case LOCAL:
                return PortIdType.LOCAL;
            case AGENTCIRCUITID:
                return PortIdType.AGENTCIRCUITID;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(portIdType);
        }
    }

    /**
     * TributarySlotGranularity dev to api
     *
     * @param clazz dev
     * @return api
     */
    default TRIBUTARYSLOTGRANULARITY devToTributarySlotGranularityApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBUTARYSLOTGRANULARITY clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBUTARYSLOTGRANULARITY> lnc = clazz.implementedInterface();
        if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBSLOT125G.class.equals(lnc)) {
            return TRIBSLOT125G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBSLOT25G.class.equals(lnc)) {
            return TRIBSLOT25G.VALUE;
        } else if (org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.TRIBSLOT5G.class.equals(lnc)) {
            return TRIBSLOT5G.VALUE;
        } else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(lnc);
        }
    }

    /**
     * AssignmentType dev to api
     *
     * @param assignmentType dev
     * @return api
     */
    default TerminalLogicalChanAssignmentConfig.AssignmentType devToAssignmentTypeApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev230426.TerminalLogicalChanAssignmentConfig.AssignmentType assignmentType) {
        if (assignmentType == null) {
            return null;
        }
        switch (assignmentType) {
            case LOGICALCHANNEL:
                return TerminalLogicalChanAssignmentConfig.AssignmentType.LOGICALCHANNEL;
            case OPTICALCHANNEL:
                return TerminalLogicalChanAssignmentConfig.AssignmentType.OPTICALCHANNEL;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(assignmentType);
        }
    }
    /**
     * oMapping dev to api
     *
     * @param clazz dev
     * @return api
     */
    default FRAMEMAPPINGPROTOCOL devToMappingApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.FRAMEMAPPINGPROTOCOL clazz){
        if(clazz==null){
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.FRAMEMAPPINGPROTOCOL> lnc = clazz.implementedInterface();
        if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.AMP.class.equals(lnc)){
            return AMP.VALUE;
        }else if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.GMP.class.equals(lnc)){
            return GMP.VALUE;
        }else if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.BMP.class.equals(lnc)){
            return BMP.VALUE;
        }else if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.CBR.class.equals(lnc)){
            return CBR.VALUE;
        }else if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.GFPT.class.equals(lnc)){
            return GFPT.VALUE;
        }else if(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.transport.types.rev230426.GFPF.class.equals(lnc)){
            return GFPF.VALUE;
        }else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(clazz);
        }
    }

}
