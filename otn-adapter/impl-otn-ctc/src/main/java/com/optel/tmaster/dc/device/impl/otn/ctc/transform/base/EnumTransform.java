/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform.base;

import com.optel.tmaster.dc.device.impl.base.transform.ITransform;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.AlarmState;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.PerceivedSeverity;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.PllState;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.AccessAction;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.ConnectionDirection;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.VlanType;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.eth.rev210927.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.file.rev210927.ActiveStatus;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.file.rev210927.DownloadStatus;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.file.rev210927.FailReason;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.file.rev210927.FileType;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.osu.rev210927.TcmModeType;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.rev210927.GHaoStatus;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.rev210927.ModifyAction;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.rev210927.OduPosition;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.rev210927.OduflexAdjust;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.types.rev210927.AdaptationType;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.types.rev210927.ClientSignalType;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.types.rev210927.SwitchType;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.pdh.rev210927.E1FrameType;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.pdh.rev210927.E1Opcode;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.pdh.rev210927.E1PhyType;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ProtectionType;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchDirection;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.Granularity;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.ThresholdType;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.*;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.OduSwitchType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.SdhSwitchType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.SignalType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.Clk2mType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.QL;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.SsmMode;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.MeStatus;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ResetType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionDirection;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ReversionMode;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SdTrigger;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchReason;

import java.util.*;

/**
 * EnumTransform
 * 数据转换
 * date       time        author
 * ─────────────────────────────
 * 2021/10/8   10:48      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public interface EnumTransform extends ITransform {

    default Set<SwitchType> devSwitchTypeToApiList(Set<SdhSwitchType> list){
        if(list==null || list.isEmpty()){
            return null;
        }
        Set<SwitchType> resultList = new HashSet<>();
        for(SdhSwitchType e:list){
            resultList.add(devSwitchTypeToApi(e));
        }
        return resultList;
    }

    /**
     * 将 dev identity switch-type 转换为 api identity switch-type
     *
     * @param switchType dev switch-type
     * @return api switch-type
     */
    default SwitchType devSwitchTypeToApi(SdhSwitchType switchType) {
        if (switchType == null) {
            return null;
        }
        switch (switchType) {
            case VC3:
                return SwitchType.VC3;
            case VC4:
                return SwitchType.VC4;
            case VC12:
                return SwitchType.VC12;
            default:
                throw getNoMatchEnumValueException(switchType);
        }
    }

    /**
     * SwitchType dev to api
     * @param list dev
     * @return api
     */
    default Set<SwitchType> devOduSwitchTypeToApiList(
            Set<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.OduSwitchType> list){
        if(list==null || list.isEmpty()){
            return null;
        }
        Set<SwitchType> resultList = new HashSet<>();
        for(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.OduSwitchType e:list){
            resultList.add(devSwitchTypeToApi(e));
        }
        return resultList;
    }

    /**
     * 将 dev ODU switch type转换为 api switch type
     *
     * @param switchType DEV switchType
     * @return api switchType
     */
    default SwitchType devSwitchTypeToApi(OduSwitchType switchType) {
        if (switchType == null) {
            return null;
        }
        switch (switchType) {
            case ODU0:
                return SwitchType.ODU0;
            case ODU1:
                return SwitchType.ODU1;
            case ODU2:
                return SwitchType.ODU2;
            case ODU2e:
                return SwitchType.ODU2e;
            case ODU3:
                return SwitchType.ODU3;
            case ODU4:
                return SwitchType.ODU4;
            case ODUflexCBR:
                return SwitchType.ODUflexCBR;
            case ODUflexGFP:
                return SwitchType.ODUflexGFP;
            case OSU:
                return SwitchType.OSU;
            default:
                throw getNoMatchEnumValueException(switchType);
        }
    }

    /**
     * 将 api switch-type 转换为 dev sdh switch-type
     *
     * @param switchType api switch-type
     * @return dev switch-type
     */
    default SdhSwitchType apiSdhSwitchTypeToDev(SwitchType switchType) {
        if (switchType == null) {
            return null;
        }
        switch (switchType) {
            case VC3:
                return SdhSwitchType.VC3;
            case VC4:
                return SdhSwitchType.VC4;
            case VC12:
                return SdhSwitchType.VC12;
            default:
                throw getNoMatchEnumValueException(switchType);
        }
    }

    /**
     * 将 api switch-type  转换为dev ODU switch-type
     *
     * @param switchType api switch-type
     * @return dev switch-type
     */
    default OduSwitchType apiSwitchTypeToDev(SwitchType switchType) {
        if (switchType == null) {
            return null;
        }
        switch (switchType) {
            case ODU0:
                return OduSwitchType.ODU0;
            case ODU1:
                return OduSwitchType.ODU1;
            case ODU2:
                return OduSwitchType.ODU2;
            case ODU2e:
                return OduSwitchType.ODU2e;
            case ODU3:
                return OduSwitchType.ODU3;
            case ODU4:
                return OduSwitchType.ODU4;
            case ODUflexCBR:
                return OduSwitchType.ODUflexCBR;
            case ODUflexGFP:
                return OduSwitchType.ODUflexGFP;
            case OSU:
                return OduSwitchType.OSU;
            default:
                throw getNoMatchEnumValueException(switchType);
        }
    }

    /**
     * 将adaptationType由 dev格式转换为api格式
     *
     * @param adaptationType adaptationType api
     * @return adaptationType dev
     */
    default AdaptationType devAdaptationTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType adaptationType) {
        if (adaptationType == null) {
            return null;
        }
        switch (adaptationType) {
            case CBRAMP:
                return AdaptationType.CBRAMP;
            case CBRBMP:
                return AdaptationType.CBRBMP;
            case CBRx:
                return AdaptationType.CBRx;
            case GFPF:
                return AdaptationType.GFPF;
            case GFPT:
                return AdaptationType.GFPT;
            case GMP:
                return AdaptationType.GMP;
            case NULL:
                return AdaptationType.NULL;
            case ODUij:
                return AdaptationType.ODUij;
            case ODUj21:
                return AdaptationType.ODUj21;
            case ODUk:
                return AdaptationType.ODUk;
            case PRBS:
                return AdaptationType.PRBS;
            case IMP:
                return AdaptationType.IMP;
            case GMPOSU:
                return AdaptationType.GMPOSU;
            case IMPOSU:
                return AdaptationType.IMPOSU;
            default:
                throw getNoMatchEnumValueException(adaptationType);
        }
    }


    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType apiAdaptationTypeToDev(AdaptationType adaptationType) {
        if (adaptationType == null) {
            return null;
        }
        switch (adaptationType) {
            case CBRAMP:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType.CBRAMP;
            case CBRBMP:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType.CBRBMP;
            case CBRx:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType.CBRx;
            case GFPF:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType.GFPF;
            case GFPT:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType.GFPT;
            case GMP:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType.GMP;
            case NULL:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType.NULL;
            case ODUij:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType.ODUij;
            case ODUj21:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType.ODUj21;
            case ODUk:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType.ODUk;
            case PRBS:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType.PRBS;
            case IMP:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType.IMP;
            case GMPOSU:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType.GMPOSU;
            case IMPOSU:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType.IMPOSU;

            default:
                throw getNoMatchEnumValueException(adaptationType);
        }
    }

    /**
     * 将 dev信号类型转换为api信号类型
     *
     * @param clientSignalType 信号类型 dev
     * @return 信号类型 api
     */
    default ClientSignalType devClientSignalTypeToApi(SignalType clientSignalType) {
        if (clientSignalType == null) {
            return null;
        }
        switch (clientSignalType) {
            case E1:
                return ClientSignalType.E1;
            case FE:
                return ClientSignalType.FE;
            case GE:
                return ClientSignalType.GE;
            case _10GE:
                return ClientSignalType.LAN10GE;
            case ODU0:
                return ClientSignalType.OTU0;
            case ODU1:
                return ClientSignalType.OTU1;
            case ODU2:
                return ClientSignalType.OTU2;
            case ODU2e:
                return ClientSignalType.OTU2e;
            case ODU4:
                return ClientSignalType.OTU4;
            case STM1:
                return ClientSignalType.STM1;
            case STM4:
                return ClientSignalType.STM4;
            case STM16:
                return ClientSignalType.STM16;
            case STM64:
                return ClientSignalType.STM64;
            case STM256:
                return ClientSignalType.STM256;
            case NULL:
                return ClientSignalType.NoSignal;
            case ETH:
                return ClientSignalType.ETH;
            default:
                throw getNoMatchEnumValueException(clientSignalType);
        }
    }

    /**
     * ClientSignalType api to dev
     *
     * @param clientSignalType api signalType
     * @return dev signalType
     */
    default SignalType apiClientSignalTypeToDev(
            ClientSignalType clientSignalType) {
        if (clientSignalType == null) {
            return null;
        }
        switch (clientSignalType) {
            case E1:
                return SignalType.E1;
            case FE:
                return SignalType.FE;
            case GE:
                return SignalType.GE;
            case LAN10GE:
                return SignalType._10GE;
            case OTU0:
                return SignalType.ODU0;
            case OTU1:
                return SignalType.ODU1;
            case OTU2:
                return SignalType.ODU2;
            case OTU2e:
                return SignalType.ODU2e;
            case OTU4:
                return SignalType.ODU4;
            case STM1:
                return SignalType.STM1;
            case STM4:
                return SignalType.STM4;
            case STM16:
                return SignalType.STM16;
            case STM64:
                return SignalType.STM64;
            case STM256:
                return SignalType.STM256;
            case NoSignal:
                return SignalType.NULL;
            case ETH:
                return SignalType.ETH;
            default:
                throw getNoMatchEnumValueException(clientSignalType);
        }
    }


    /**
     * 设备类型 dev转为api
     *
     * @param deviceType 设备类型 dev
     * @return 设备类型 api
     */
    default DeviceType devDeviceTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.DeviceType deviceType) {
        if (deviceType == null) {
            return null;
        }
        switch (deviceType) {
            case MoBox:
                return DeviceType.MoBox;
            case PoBox:
                return DeviceType.PoBox;
            case MoPnp:
                return DeviceType.MoPnp;
            case PoPnp:
                return DeviceType.PoPnp;
            case OtmBox:
                return DeviceType.OtmBox;
            case OtmPnp:
                return DeviceType.OtmPnp;
            default:
                throw getNoMatchEnumValueException(deviceType);
        }
    }

    /**
     * 端口角色 dev转api
     *
     * @param portRole dev
     * @return api
     */
    default PortRole devPortRoleToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.PortRole portRole) {
        if (portRole == null) {
            return null;
        }
        switch (portRole) {
            case LeafNode:
                return PortRole.Leaf;
            case Root:
                return PortRole.Root;
            case Symmetric:
                return PortRole.Symmetric;
            default:
                throw getNoMatchEnumValueException(portRole);
        }
    }

    /**
     * 端口角色 api转dev
     *
     * @param portRole api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.PortRole apiPortRoleToDev(PortRole portRole) {
        if (portRole == null) {
            return null;
        }
        switch (portRole) {
            case Leaf:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.PortRole.LeafNode;
            case Root:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.PortRole.Root;
            case Symmetric:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.PortRole.Symmetric;
            default:
                throw getNoMatchEnumValueException(portRole);
        }
    }

    /**
     * 端口保护角色 dev转api
     *
     * @param protectRole 端口保护角色
     * @return 端口保护角色 api
     */
    default ProtectRole devProtectRoleToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ProtectRole protectRole) {
        if (protectRole == null) {
            return null;
        }
        switch (protectRole) {
            case Null:
                return ProtectRole.Null;
            case Primary:
                return ProtectRole.Primary;
            case Protected:
                return ProtectRole.Protected;
            case Secondary:
                return ProtectRole.Secondary;
            default:
                throw getNoMatchEnumValueException(protectRole);
        }
    }

    /**
     * G HAO状态 dev转api
     *
     * @param gHaoStatus dev
     * @return api
     */
    default GHaoStatus devGHaoStatusToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.GHaoStatus gHaoStatus) {
        if (gHaoStatus == null) {
            return null;
        }
        switch (gHaoStatus) {
            case Idle:
                return GHaoStatus.Idle;
            case BwrDec:
                return GHaoStatus.BwrDec;
            case BwrInc:
                return GHaoStatus.BwrInc;
            case LcrDec:
                return GHaoStatus.LcrDec;
            case LcrInc:
                return GHaoStatus.LcrInc;
            case LcrHandshake:
                return GHaoStatus.LcrHandshake;
            default:
                throw getNoMatchEnumValueException(gHaoStatus);
        }
    }

    /**
     * OAM间隔 dev转api
     *
     * @param oamTransmitInterval dev
     * @return api
     */
    default OamTransmitInterval devOamTransmitIntervalToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.OamTransmitInterval oamTransmitInterval) {
        if (oamTransmitInterval == null) {
            return null;
        }
        switch (oamTransmitInterval) {
            case Interval3ms33:
                return OamTransmitInterval.Interval3ms33;
            case Interval10ms:
                return OamTransmitInterval.Interval10ms;
            case Interval100ms:
                return OamTransmitInterval.Interval100ms;
            case Interval1s:
                return OamTransmitInterval.Interval1s;
            case Interval10s:
                return OamTransmitInterval.Interval10s;
            case Interval1min:
                return OamTransmitInterval.Interval1min;
            default:
                throw getNoMatchEnumValueException(oamTransmitInterval);
        }
    }

    /**
     * 源端宿端模式 dev转api
     *
     * @param activeMode 模式 dev
     * @return api
     */
    default ActiveMode devActiveModeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ActiveMode activeMode) {
        if (activeMode == null) {
            return null;
        }
        switch (activeMode) {
            case Monitor:
                return ActiveMode.Monitor;
            case Operation:
                return ActiveMode.Operation;
            case Transparency:
                return ActiveMode.Transparency;
            default:
                throw getNoMatchEnumValueException(activeMode);
        }
    }

    default TcmModeType devTcmModeTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.TcmModeType tcmModeType){
        if (tcmModeType == null){
            return null;
        }
        switch (tcmModeType){
            case Operational:
                return TcmModeType.Operational;
            case Monitor:
                return TcmModeType.Monitor;
            case Transparent:
                return TcmModeType.Transparent;
            default:
                throw  getNoMatchEnumValueException(tcmModeType);
        }
    }

    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.TcmModeType apiTcmModeTypeToDev(TcmModeType tcmModeType){
        if (tcmModeType == null){
            return null;
        }
        switch (tcmModeType){
            case Operational:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.TcmModeType.Operational;
            case Monitor:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.TcmModeType.Monitor;
            case Transparent:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.TcmModeType.Transparent;
            default:
                throw  getNoMatchEnumValueException(tcmModeType);
        }
    }

    /**
     * 源端宿端模式 api转dev
     *
     * @param activeMode 模式 api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ActiveMode apiActiveModeToDev(ActiveMode activeMode) {
        if (activeMode == null) {
            return null;
        }
        switch (activeMode) {
            case Monitor:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ActiveMode.Monitor;
            case Operation:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ActiveMode.Operation;
            case Transparency:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ActiveMode.Transparency;
            default:
                throw getNoMatchEnumValueException(activeMode);
        }
    }

    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.OduflexAdjust apiOduflexAdjustToDev(OduflexAdjust oduflexAdjust) {
        if (oduflexAdjust == null) {
            return null;
        }
        switch (oduflexAdjust) {
            case Hit:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.OduflexAdjust.Hit;
            case Hitless:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.OduflexAdjust.Hitless;
            default:
                throw getNoMatchEnumValueException(oduflexAdjust);
        }
    }

    /**
     * EthMappingType dev to api
     *
     * @param ethMappingType dev
     * @return api
     */
    default EthMappingType devEthMappingTypeToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.EthMappingType ethMappingType) {
        if (ethMappingType == null) {
            return null;
        }
        switch (ethMappingType) {
            case EoO:
                return EthMappingType.EoO;
            case EoS:
                return EthMappingType.EoS;
            case EoOSU:
                return EthMappingType.EoOSU;
            default:
                throw getNoMatchEnumValueException(ethMappingType);
        }
    }

    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.EthMappingType apiEthMappingTypeToDev(
            EthMappingType ethMappingType) {
        if (ethMappingType == null) {
            return null;
        }
        switch (ethMappingType) {
            case EoO:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.EthMappingType.EoO;
            case EoS:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.EthMappingType.EoS;
            case EoOSU:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.EthMappingType.EoOSU;
            default:
                throw getNoMatchEnumValueException(ethMappingType);
        }
    }

    default Set<EthMappingType> devEthMappingTypeToApiList(
            Set<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.EthMappingType> list) {
        if (list == null || list.isEmpty()) {
            return null;
        }
        Set<EthMappingType> resultList = new HashSet<>();
        for (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.EthMappingType e : list) {
            resultList.add(devEthMappingTypeToApi(e));
        }
        return resultList;
    }


    /**
     * Tim模式 dev转api
     *
     * @param timMode tim模式 dev
     * @return api
     */
    default TimMode devTimModeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.TimMode timMode) {
        if (timMode == null) {
            return null;
        }
        switch (timMode) {
            case DAPI:
                return TimMode.DAPI;
            case SAPI:
                return TimMode.SAPI;
            case SAPInDAPI:
                return TimMode.SAPInDAPI;
            case NoCheck:
                return TimMode.NoCheck;
            default:
                throw getNoMatchEnumValueException(timMode);
        }
    }

    /**
     * Tim模式 api转dev
     *
     * @param timMode tim模式 api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.TimMode apiTimModeToDev(TimMode timMode) {
        if (timMode == null) {
            return null;
        }
        switch (timMode) {
            case DAPI:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.TimMode.DAPI;
            case SAPI:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.TimMode.SAPI;
            case SAPInDAPI:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.TimMode.SAPInDAPI;
            case NoCheck:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.TimMode.NoCheck;
            default:
                throw getNoMatchEnumValueException(timMode);
        }
    }

    /**
     * Accessaction 动作dev转api
     *
     * @param accessAction dev
     * @return api
     */
    default AccessAction devAccessactionToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.AccessAction accessAction) {
        if (accessAction == null) {
            return null;
        }
        switch (accessAction) {
            case Keep:
                return AccessAction.Keep;
            case PushPop:
                return AccessAction.PushPop;
            case Exchange:
                return AccessAction.Exchange;
            case PopPush:
                return AccessAction.PopPush;
            default:
                throw getNoMatchEnumValueException(accessAction);
        }
    }

    /**
     * Accessaction 动作api转dev
     *
     * @param accessAction api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.AccessAction apiAccessActionToDev(AccessAction accessAction) {
        if (accessAction == null) {
            return null;
        }
        switch (accessAction) {
            case Keep:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.AccessAction.Keep;
            case PushPop:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.AccessAction.PushPop;
            case Exchange:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.AccessAction.Exchange;
            case PopPush:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.AccessAction.PopPush;
            default:
                throw getNoMatchEnumValueException(accessAction);
        }
    }

    /**
     * VlanType dev转api
     *
     * @param vlanType dev
     * @return api
     */
    default VlanType devVlanTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.VlanType vlanType) {
        if (vlanType == null) {
            return null;
        }
        switch (vlanType) {
            case CTag:
                return VlanType.CTag;
            case STag:
                return VlanType.STag;
            case UnTag:
                return VlanType.UnTag;
            default:
                throw getNoMatchEnumValueException(vlanType);
        }

    }

    /**
     * VlanType api转dev
     *
     * @param vlanType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.VlanType apiVlanTypeToDev(VlanType vlanType) {
        if (vlanType == null) {
            return null;
        }
        switch (vlanType) {
            case CTag:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.VlanType.CTag;
            case STag:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.VlanType.STag;
            case UnTag:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.VlanType.UnTag;
            default:
                throw getNoMatchEnumValueException(vlanType);
        }

    }


    /**
     * ql  api转dev
     *
     * @param ql type api
     * @return type dev
     */
    default QL apiQlToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL ql) {
        if (ql == null) {
            return null;
        }
        switch (ql) {
            case QLDNU:
                return QL.QLDNU;
            case QLPRC:
                return QL.QLPRC;
            case QLSEC:
                return QL.QLSEC;
            case QLSSUA:
                return QL.QLSSUA;
            case QLSSUB:
                return QL.QLSSUB;
            case QLEEC2:
                return QL.QLEEC2;
            case QLEEEC:
                return QL.QLEEEC;
            case QLNONE:
                return QL.QLNONE;
            case QLPRTC:
                return QL.QLPRTC;
            case QLEPRTC:
                return QL.QLEPRTC;
            default:
                throw getNoMatchEnumValueException(ql);

        }
    }

    /**
     * ql  dev转api
     *
     * @param ql type api
     * @return type dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL devQlToApi(QL ql) {
        if (ql == null) {
            return null;
        }
        switch (ql) {
            case QLDNU:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLDNU;
            case QLPRC:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLPRC;
            case QLSEC:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLSEC;
            case QLSSUA:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLSSUA;
            case QLSSUB:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLSSUB;
            case QLEEC2:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLEEC2;
            case QLEPRTC:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLEPRTC;
            case QLPRTC:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLPRTC;
            case QLNONE:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLNONE;
            case QLEEEC:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLEEEC;
            default:
                throw getNoMatchEnumValueException(ql);

        }
    }

    /**
     * ssmMode  api转dev
     *
     * @param ssmMode type api
     * @return type dev
     */
    default SsmMode apiSsmModeToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.SsmMode ssmMode) {
        if (ssmMode == null) {
            return null;

        }
        switch (ssmMode) {
            case Auto:
                return SsmMode.Auto;
            case Force:
                return SsmMode.Force;
            default:
                throw getNoMatchEnumValueException(ssmMode);
        }
    }

    /**
     * ssmMode  api转dev
     *
     * @param ssmMode type api
     * @return type dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.SsmMode devSsmModeToApi(SsmMode ssmMode) {
        if (ssmMode == null) {
            return null;

        }
        switch (ssmMode) {
            case Auto:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.SsmMode.Auto;
            case Force:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.SsmMode.Force;
            default:
                throw getNoMatchEnumValueException(ssmMode);
        }
    }

    /**
     * pllState  dev转api
     *
     * @param pllState type dev
     * @return type api
     */
    default PllState devPllStateApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.PllState pllState) {
        if (pllState == null) {
            return null;
        }
        switch (pllState) {
            case Lock:
                return PllState.Lock;
            case Accquire:
                return PllState.Accquire;
            case Holdover:
                return PllState.Holdover;
            case FreeRunning:
                return PllState.FreeRunning;
            default:
                throw getNoMatchEnumValueException(pllState);
        }
    }

    /**
     * Clk2mType  api转dev
     *
     * @param clk2mType type dev
     * @return type api
     */
    default Clk2mType apiClk2mTypeToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.Clk2mType clk2mType) {
        if (clk2mType == null) {
            return null;
        }
        switch (clk2mType) {
            case _2MHz:
                return Clk2mType._2MHz;
            case _2Mbits:
                return Clk2mType._2Mbits;
            default:
                throw getNoMatchEnumValueException(clk2mType);
        }
    }

    /**
     * Clk2mType  dev转api
     *
     * @param clk2mType type dev
     * @return type api
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.Clk2mType devClk2mTypeToApi(Clk2mType clk2mType) {
        if (clk2mType == null) {
            return null;
        }
        switch (clk2mType) {
            case _2MHz:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.Clk2mType._2MHz;
            case _2Mbits:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.Clk2mType._2Mbits;
            default:
                throw getNoMatchEnumValueException(clk2mType);
        }
    }

    /**
     * 业务时延测量类型 api转dev
     *
     * @param measureType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.MeasureType apiServiceDelayMeasureTypeToDev(MeasureType measureType) {
        if (measureType == null) {
            return null;
        }
        switch (measureType) {
            case TCM:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.MeasureType.TCM;
            case PM:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.MeasureType.PM;
            default:
                throw getNoMatchEnumValueException(measureType);
        }
    }

    /**
     * 业务时延测量类型 api转dev
     *
     * @param measureType dev
     * @return api
     */
    default MeasureType devServiceDelayMeasureTypeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.MeasureType measureType) {
        if (measureType == null) {
            return null;
        }
        switch (measureType) {
            case TCM:
                return MeasureType.TCM;
            case PM:
                return MeasureType.PM;
            default:
                throw getNoMatchEnumValueException(measureType);
        }
    }

    /**
     * 单板复位类型 api转dev
     *
     * @param resetType api
     * @return dev
     */
    default ResetType apiResetTypeToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ResetType resetType) {
        if (resetType == null) {
            return null;
        }
        switch (resetType) {
            case SoftReset:
                return ResetType.SoftReset;
            case HardReset:
                return ResetType.HardReset;
            default:
                throw getNoMatchEnumValueException(resetType);
        }
    }

    /**
     * lm工作类型 dev转api
     *
     * @param workingMode api
     * @return dev
     */
    default EthLmWorkingMode devEthLmWorkingModeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthLmWorkingMode workingMode) {
        if (workingMode == null) {
            return null;
        }
        switch (workingMode) {
            case DualEnded:
                return EthLmWorkingMode.DualEnded;
            case SingleEnded:
                return EthLmWorkingMode.SingleEnded;
            default:
                throw getNoMatchEnumValueException(workingMode);
        }
    }

    /**
     * lm工作类型 api转dev
     *
     * @param workingMode dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthLmWorkingMode apiEthLmWorkingModeToDev(EthLmWorkingMode workingMode) {
        if (workingMode == null) {
            return null;
        }
        return switch (workingMode) {
            case DualEnded ->
                    org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthLmWorkingMode.DualEnded;
            case SingleEnded ->
                    org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthLmWorkingMode.SingleEnded;
        };

    }

    /**
     * oam config api转dev
     *
     * @param oamTransmitInterval api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.OamTransmitInterval apiOamTransmitIntervalToDev(OamTransmitInterval oamTransmitInterval) {
        if (oamTransmitInterval == null) {
            return null;
        }
        switch (oamTransmitInterval) {
            case Interval1s:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.OamTransmitInterval.Interval1s;
            case Interval10s:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.OamTransmitInterval.Interval10s;
            case Interval1min:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.OamTransmitInterval.Interval1min;
            case Interval10ms:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.OamTransmitInterval.Interval10ms;
            case Interval3ms33:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.OamTransmitInterval.Interval3ms33;
            case Interval100ms:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.OamTransmitInterval.Interval100ms;
            default:
                throw getNoMatchEnumValueException(oamTransmitInterval);
        }
    }

    /**
     * TpidDifinition  dev转api
     *
     * @param tpidDifinition type dev
     * @return type api
     */
    default TpidDifinition devTpidDifinitionToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.TpidDifinition tpidDifinition) {
        if (tpidDifinition == null) {
            return null;
        }
        switch (tpidDifinition) {
            case _0x88a8:
                return TpidDifinition._0x88a8;
            case _0x8100:
                return TpidDifinition._0x8100;
            case _0x9100:
                return TpidDifinition._0x9100;
            case _0x9200:
                return TpidDifinition._0x9200;
            default:
                throw getNoMatchEnumValueException(tpidDifinition);
        }
    }

    /**
     * TpidDifinition  api转dev
     *
     * @param tpidDifinition type api
     * @return type dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.TpidDifinition apiTpidDifinitionToDev(TpidDifinition tpidDifinition) {
        if (tpidDifinition == null) {
            return null;
        }
        switch (tpidDifinition) {
            case _0x88a8:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.TpidDifinition._0x88a8;
            case _0x8100:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.TpidDifinition._0x8100;
            case _0x9100:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.TpidDifinition._0x9100;
            case _0x9200:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.TpidDifinition._0x9200;
            default:
                throw getNoMatchEnumValueException(tpidDifinition);
        }
    }

    /**
     * eqTypeList dev 转 api
     *
     * @param eqTypeList dev
     * @return SetTypeResult api
     */
    default List<EqType> devEqTypeToApiList(List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.EqType> eqTypeList) {
        if (eqTypeList == null) {
            return null;
        }
        List<EqType> result = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.EqType eqType : eqTypeList) {
            result.add(devEqTypeToApi(eqType));
        }
        return result;
    }

    /**
     * eqType dev 转 api
     *
     * @param eqType dev
     * @return SetTypeResult api
     */
    default EqType devEqTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.EqType eqType) {
        if (eqType == null) {
            return null;
        }
        switch (eqType) {
            case Fan:
                return EqType.Fan;
            case Clock:
                return EqType.Clock;
            case Power:
                return EqType.Power;
            case Service:
                return EqType.Service;
            case CrossConnect:
                return EqType.CrossConnect;
            case SystemControl:
                return EqType.SystemControl;
            case Eos2eoosu:
                return EqType.Eos2eoosu;
            default:
                throw getNoMatchEnumValueException(eqType);
        }
    }
    /**
     * WorkingState dev 转 api
     * @param workingState dev
     * @return SetTypeResult api
     */
    default WorkingState devWorkingStateToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.WorkingState workingState){
        if(workingState == null){
            return null;
        }
        switch (workingState){
            case Normal:
                return WorkingState.Normal;
            case Backup:
                return WorkingState.Backup;
            default:
                throw getNoMatchEnumValueException(workingState);
        }
    }

    default RemoteState devRemoteStateToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.RemoteState remoteState){
        if(remoteState == null){
            return null;
        }
        switch (remoteState){
            case Online:
                return RemoteState.Online;
            case Offline:
                return RemoteState.Offline;
            default:
                throw getNoMatchEnumValueException(remoteState);
        }
    }

    /**
     * LptMode dev to api
     * @param lptMode dev
     * @return api
     */
    default LptMode devLptModeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LptMode lptMode){
        if(lptMode==null){
            return null;
        }
        switch (lptMode){
            case TOL:
                return LptMode.TOL;
            case ICSD:
                return LptMode.ICSD;
            default:
                throw getNoMatchEnumValueException(lptMode);
        }
    }

    /**
     * LptMode api to dev
     * @param lptMode api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LptMode apiLptModeToDev(LptMode lptMode){
        if(lptMode==null){
            return null;
        }
        switch (lptMode){
            case TOL:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LptMode.TOL;
            case ICSD:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LptMode.ICSD;
            default:
                throw getNoMatchEnumValueException(lptMode);
        }
    }


    default OperationalState devOperationalStateToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.OperationalState operationalState){
        if(operationalState == null){
            return null;
        }
        switch (operationalState){
            case Up:
                return OperationalState.Up;
            case Down:
                return OperationalState.Down;
            default:
                throw  getNoMatchEnumValueException(operationalState);
        }
    }

    /**
     * AdminState dev to api
     * @param adminState dev
     * @return api
     */
    default AdminState devAdminStateToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.AdminState adminState){
        if(adminState==null){
            return null;
        }
        switch (adminState){
            case Enabled:
                return AdminState.Enabled;
            case Disabled:
                return AdminState.Disabled;
            default:
                throw getNoMatchEnumValueException(adminState);
        }
    }

    /**
     * AdminState api to dev
     * @param adminState api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.AdminState apiAdminStateToDev(AdminState adminState){
        if(adminState==null){
            return null;
        }
        switch (adminState){
            case Enabled:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.AdminState.Enabled;
            case Disabled:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.AdminState.Disabled;
            default:
                throw getNoMatchEnumValueException(adminState);
        }
    }

    /**
     * EthPortMode dev to api
     * @param ethPortMode dev
     * @return api
     */
    default EthPortMode devEthPortModeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthPortMode ethPortMode){
        if(ethPortMode==null){
            return null;
        }
        switch (ethPortMode) {
            case Trunk:
                return EthPortMode.Trunk;
            case Access:
                return EthPortMode.Access;
            case Hybrid:
                return EthPortMode.Hybrid;
            case Transport:
                return EthPortMode.Transport;
            default:
                throw getNoMatchEnumValueException(ethPortMode);
        }
    }

    /**
     * EthPortMode api to dev
     * @param ethPortMode api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthPortMode apiEthPortModeToDev(EthPortMode ethPortMode){
        if(ethPortMode==null){
            return null;
        }
        switch (ethPortMode) {
            case Trunk:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthPortMode.Trunk;
            case Access:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthPortMode.Access;
            case Hybrid:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthPortMode.Hybrid;
            case Transport:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthPortMode.Transport;
            default:
                throw getNoMatchEnumValueException(ethPortMode);
        }
    }

    /**
     * meStatus  api转dev
     *
     * @param meStatus type api
     * @return type dev
     */
    default MeStatus apiMeStatusToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.MeStatus meStatus) {
        if (meStatus == null) {
            return null;
        }
        switch (meStatus) {
            case Running:
                return MeStatus.Running;
            case Installing:
                return MeStatus.Installing;
            case Maintenance:
                return MeStatus.Maintenance;
            default:
                throw getNoMatchEnumValueException(meStatus);
        }
    }

    /**
     * 网元状态 dev转api
     * @param meStatus 网元状态 dev
     * @return 网元状态 api
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.MeStatus devMeStatusToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.MeStatus meStatus){
        if(meStatus == null){
            return null;
        }
        switch (meStatus){
            case Installing:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.MeStatus.Installing;
            case Running:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.MeStatus.Running;
            case Maintenance:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.MeStatus.Maintenance;
            default:
                throw getNoMatchEnumValueException(meStatus);
        }
    }

    /**
     * NTP 服务器状态 dev转api
     * @param ntpState NTP 服务器状态
     * @return NTP 服务器状态
     */
    default NtpState devNtpStateToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.NtpState ntpState){
        if(ntpState == null){
            return null;
        }
        switch (ntpState){
            case FSET:
                return NtpState.FSET;
            case FREQ:
                return NtpState.FREQ;
            case NSET:
                return NtpState.NSET;
            case SPIK:
                return NtpState.SPIK;
            case SYNC:
                return NtpState.SYNC;
            default:
                throw getNoMatchEnumValueException(ntpState);
        }
    }

    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.ModifyAction apiModifyActionToDev(ModifyAction modifyAction){
        if(modifyAction == null){
            return null;
        }
        switch (modifyAction){
            case AddAction:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.ModifyAction.AddAction;
            case AbortAction:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.ModifyAction.AbortAction;
            case DeleteAction:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.ModifyAction.DeleteAction;
            default:
                throw getNoMatchEnumValueException(modifyAction);
        }
    }

    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.OduPosition apiOduPositionToDev(OduPosition oduPosition){
        if(oduPosition == null){
            return null;
        }
        switch (oduPosition){
            case LineOdu:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.OduPosition.LineOdu;
            case ClientOdu:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.OduPosition.ClientOdu;
            default:
                throw getNoMatchEnumValueException(oduPosition);
        }
    }
    /**
     * SetTypeResult dev 转 api
     * @param setTypeResult dev
     * @return SetTypeResult api
     */
    default SetTypeResult devSetTypeResultToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.SetTypeResult setTypeResult) {
        if (setTypeResult == null) {
            return null;
        }
        switch (setTypeResult) {
            case Fail:
                return SetTypeResult.Fail;
            case Success:
                return SetTypeResult.Success;
            default:
                throw getNoMatchEnumValueException(setTypeResult);
        }
    }

    /**
     * 告警级别 dev to api
     * @param perceivedSeverity dev
     * @return api
     */
    default PerceivedSeverity devPerceivedSeverityToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.PerceivedSeverity perceivedSeverity){
        if(perceivedSeverity==null){
            return null;
        }
        switch (perceivedSeverity){
            case Major:
                return PerceivedSeverity.Major;
            case Minor:
                return PerceivedSeverity.Minor;
            case Warning:
                return PerceivedSeverity.Warning;
            case Critical:
                return PerceivedSeverity.Critical;
            default:
                throw getNoMatchEnumValueException(perceivedSeverity);
        }
    }

    /**
     * 告警级别 api to dev
     * @param perceivedSeverity api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.PerceivedSeverity apiPerceivedSeverityToDev(
            PerceivedSeverity perceivedSeverity){
        if(perceivedSeverity==null){
            return null;
        }
        switch (perceivedSeverity){
            case Major:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.PerceivedSeverity.Major;
            case Minor:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.PerceivedSeverity.Minor;
            case Warning:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.PerceivedSeverity.Warning;
            case Critical:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.PerceivedSeverity.Critical;
            default:
                throw getNoMatchEnumValueException(perceivedSeverity);
        }
    }

    /**
     * 告警状态 dev to api
     * @param alarmState dev
     * @return api
     */
    default AlarmState devAlarmStateToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.AlarmState alarmState){
        if(alarmState==null){
            return null;
        }
        switch (alarmState){
            case End:
                return AlarmState.End;
            case Start:
                return AlarmState.Start;
            default:
                throw getNoMatchEnumValueException(alarmState);
        }
    }

    /**
     * 告警状态 api to dev
     * @param alarmState api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.AlarmState apiAlarmStateToDev(AlarmState alarmState){
        if(alarmState==null){
            return null;
        }
        switch (alarmState){
            case End:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.AlarmState.End;
            case Start:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.AlarmState.Start;
            default:
                throw getNoMatchEnumValueException(alarmState);
        }
    }


    /**
     * 业务类型 dev转api
     * @param serviceType dev业务类型
     * @return api业务类型
     */
    default ServiceType devServiceTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ServiceType serviceType){
        if(serviceType == null){
            return null;
        }
        switch (serviceType){
            case SDH:
                return ServiceType.SDH;
            case EPL:
                return ServiceType.EPL;
            case EVPL:
                return ServiceType.EVPL;
            case ODU:
                return ServiceType.ODU;
            case OSU:
                return ServiceType.OSU;
            case ELAN:
                return ServiceType.ELAN;
            case ETREE:
                return ServiceType.ETREE;
            case EEPL:
                return ServiceType.EEPL;
            case EEVPL:
                return ServiceType.EEVPL;
            default:
                throw getNoMatchEnumValueException(serviceType);
        }
    }

    /**
     * 业务类型api转dev
     * @param serviceType 业务类型 api
     * @return 业务类型 dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ServiceType apiServiceTypeToDev(ServiceType serviceType){
        if(serviceType == null){
            return null;
        }
        switch (serviceType){
            case SDH:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ServiceType.SDH;
            case EPL:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ServiceType.EPL;
            case EVPL:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ServiceType.EVPL;
            case ODU:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ServiceType.ODU;
            case OSU:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ServiceType.OSU;
            case ELAN:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ServiceType.ELAN;
            case ETREE:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ServiceType.ETREE;
            case EEPL:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ServiceType.EEPL;
            case EEVPL:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ServiceType.EEVPL;
            default:
                throw getNoMatchEnumValueException(serviceType);
        }
    }

    /**
     * 连接方向 api转dev
     *
     * @param connectionDirection 连接方向 api
     * @return 连接方向 dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.ConnectionDirection apiConnectionDirectionToDev(ConnectionDirection connectionDirection) {
        if (connectionDirection == null) {
            return null;
        }
        switch (connectionDirection) {
            case Up:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.ConnectionDirection.Up;
            case Down:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.ConnectionDirection.Down;
            default:
                throw getNoMatchEnumValueException(connectionDirection);
        }
    }

    /**
     * 保护类型 api转换为dev
     *
     * @param protectionType 保护类型 api
     * @return 保护类型 dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType apiProtectionTypeToDev(ProtectionType protectionType) {
        if (protectionType == null) {
            return null;
        }
        switch (protectionType) {
            case LagLoadBlancing:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.LagLoadBlancing;
            case VcSncp:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.VcSncp;
            case Lag1To1:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.Lag;
            case OduSncpI:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.OduSncpI;
            case OduSncpN:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.OduSncpN;
            case OduSncpS:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.OduSncpS;
            case Msp1Plus1:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.Msp1Plus1;
            case Och1Plus1:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.Och1Plus1;
            case NoProtection:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.NoProtection;
            case OsuSncpS:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.OsuSncpS;
            case OsuSncpN:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.OsuSncpN;
            case OsuSncpI:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.OsuSncpI;
            default:
                throw getNoMatchEnumValueException(protectionType);
        }
    }
    /**
     * 保护类型 api转换为dev
     *
     * @param protectionType 保护类型 api
     * @return 保护类型 dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType apiProtectionTyperev240702ToDev(ProtectionType protectionType) {
        if (protectionType == null) {
            return null;
        }
        switch (protectionType) {
            case LagLoadBlancing:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.LagLoadBlancing;
            case VcSncp:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.VcSncp;
            case Lag1To1:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.Lag;
            case OduSncpI:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.OduSncpI;
            case OduSncpN:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.OduSncpN;
            case OduSncpS:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.OduSncpS;
            case Msp1Plus1:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.Msp1Plus1;
            case Och1Plus1:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.Och1Plus1;
            case NoProtection:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.NoProtection;
            case OsuSncpS:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.OsuSncpS;
            case OsuSncpN:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.OsuSncpN;
            case OsuSncpI:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType.OsuSncpI;
            default:
                throw getNoMatchEnumValueException(protectionType);
        }
    }

    /**
     * Granularity dev to api
     *
     * @param granularity dev
     * @return api
     */
    default Granularity devGranularityToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.Granularity granularity) {
        if (granularity == null) {
            return null;
        }
        switch (granularity) {
            case _24h:
                return Granularity._24h;
            case _15min:
                return Granularity._15min;
            default:
                throw getNoMatchEnumValueException(granularity);
        }
    }

    /**
     * Granularity api to dev
     *
     * @param granularity api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.Granularity apiGranularityToDev(
            Granularity granularity) {
        if (granularity == null) {
            return null;
        }
        switch (granularity) {
            case _24h:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.Granularity._24h;
            case _15min:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.Granularity._15min;
            default:
                throw getNoMatchEnumValueException(granularity);
        }
    }

    /**
     * Threshold dev to api
     *
     * @param thresholdType dev
     * @return api
     */
    default ThresholdType devThresholdTypeToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.ThresholdType thresholdType) {
        if (thresholdType == null) {
            return null;
        }
        switch (thresholdType) {
            case Low:
                return ThresholdType.Low;
            case High:
                return ThresholdType.High;
            default:
                throw getNoMatchEnumValueException(thresholdType);
        }
    }

    /**
     * Threshold api to dev
     *
     * @param thresholdType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.ThresholdType apiThresholdTypeToDev(ThresholdType thresholdType) {
        if (thresholdType == null) {
            return null;
        }
        switch (thresholdType) {
            case Low:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.ThresholdType.Low;
            case High:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.ThresholdType.High;
            default:
                throw getNoMatchEnumValueException(thresholdType);
        }
    }


    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LearningMode apiLearningModeToDev(LearningMode learningMode) {
        if (learningMode == null) {
            return null;
        }
        switch (learningMode) {
            case IVL:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LearningMode.IVL;
            case SVL:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LearningMode.SVL;
            default:
                throw getNoMatchEnumValueException(learningMode);
        }
    }

    default LearningMode devLearningModeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LearningMode learningMode) {
        if (learningMode == null) {
            return null;
        }
        switch (learningMode) {
            case IVL:
                return LearningMode.IVL;
            case SVL:
                return LearningMode.SVL;
            default:
                throw getNoMatchEnumValueException(learningMode);
        }
    }

    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.UnknownFrameProcess apiUnknownFrameProcessToDev(UnknownFrameProcess frameProcess) {
        if (frameProcess == null) {
            return null;
        }
        switch (frameProcess) {
            case Discard:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.UnknownFrameProcess.Discard;
            case Transmit:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.UnknownFrameProcess.Transmit;
            default:
                throw getNoMatchEnumValueException(frameProcess);
        }
    }

    default UnknownFrameProcess devUnknownFrameProcessToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.UnknownFrameProcess frameProcess) {
        if (frameProcess == null) {
            return null;
        }
        switch (frameProcess) {
            case Discard:
                return UnknownFrameProcess.Discard;
            case Transmit:
                return UnknownFrameProcess.Transmit;
            default:
                throw getNoMatchEnumValueException(frameProcess);
        }
    }

    /**
     * SdTrigger  api to dev
     *
     * @param sdTrigger api
     * @return dev
     */
    default SdTrigger apiSdTriggerToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SdTrigger sdTrigger) {
        if (sdTrigger == null) {
            return null;
        }
        switch (sdTrigger) {
            case Enabled:
                return SdTrigger.Enabled;
            case Disabled:
                return SdTrigger.Disabled;
            default:
                throw getNoMatchEnumValueException(sdTrigger);

        }
    }
    /**
     * SdTrigger  api to dev
     *
     * @param sdTrigger api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SdTrigger apiSdTriggerrev240702ToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SdTrigger sdTrigger) {
        if (sdTrigger == null) {
            return null;
        }
        switch (sdTrigger) {
            case Enabled:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SdTrigger.Enabled;
            case Disabled:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SdTrigger.Disabled;
            default:
                throw getNoMatchEnumValueException(sdTrigger);

        }
    }
    /**
     * switchType api to dev
     *
     * @param switchDirection api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchType apiSwitchDirectionToDev(SwitchDirection switchDirection) {
        if (switchDirection == null) {
            return null;
        }
        switch (switchDirection) {
            case UniSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchType.UniSwitch;
            case BiSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchType.BiSwitch;
            default:
                throw getNoMatchEnumValueException(switchDirection);
        }
    }

    /**
     * switchType api to dev
     *
     * @param switchDirection api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchType apiSwitchDirectionrev240702ToDev(SwitchDirection switchDirection) {
        if (switchDirection == null) {
            return null;
        }
        switch (switchDirection) {
            case UniSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchType.UniSwitch;
            case BiSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchType.BiSwitch;
            default:
                throw getNoMatchEnumValueException(switchDirection);
        }
    }

    /**
     * ReversionMode api to dev
     *
     * @param reversionMode api
     * @return dev
     */
    default ReversionMode apiReversionModeToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ReversionMode reversionMode) {
        if (reversionMode == null) {
            return null;
        }
        switch (reversionMode) {
            case Revertive:
                return ReversionMode.Revertive;
            case NonRevertive:
                return ReversionMode.NonRevertive;
            default:
                throw getNoMatchEnumValueException(reversionMode);
        }
    }
    /**
     * ReversionMode api to dev
     *
     * @param reversionMode api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ReversionMode apiReversionModerev240702ToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ReversionMode reversionMode) {
        if (reversionMode == null) {
            return null;
        }
        switch (reversionMode) {
            case Revertive:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ReversionMode.Revertive;
            case NonRevertive:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ReversionMode.NonRevertive;
            default:
                throw getNoMatchEnumValueException(reversionMode);
        }
    }
    /**
     * 保护类型 dev转换为api
     *
     * @param protectionType 保护类型 dev
     * @return 保护类型 api
     */
    default ProtectionTypeEqPg devProtectionTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.ProtectionTypeEqPg protectionType) {
        if (protectionType == null) {
            return null;
        }
        switch (protectionType) {
            case ControlCrossConnect1Plus1:
                return ProtectionTypeEqPg.ControlCrossConnect1Plus1;
            case Power1Plus1:
                return ProtectionTypeEqPg.Power1Plus1;
            case Control1Plus1:
                return ProtectionTypeEqPg.Control1Plus1;
            case CrossConnect1Plus1:
                return ProtectionTypeEqPg.CrossConnect1Plus1;
            default:
                throw getNoMatchEnumValueException(protectionType);
        }
    }
    /**
     * 保护类型 dev转换为api
     *
     * @param protectionType 保护类型 dev
     * @return 保护类型 api
     */
    default ProtectionTypeEqPg devProtectionTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionTypeEqPg protectionType) {
        if (protectionType == null) {
            return null;
        }
        switch (protectionType) {
            case ControlCrossConnect1Plus1:
                return ProtectionTypeEqPg.ControlCrossConnect1Plus1;
            case Power1Plus1:
                return ProtectionTypeEqPg.Power1Plus1;
            case Control1Plus1:
                return ProtectionTypeEqPg.Control1Plus1;
            case CrossConnect1Plus1:
                return ProtectionTypeEqPg.CrossConnect1Plus1;
            default:
                throw getNoMatchEnumValueException(protectionType);
        }
    }
    /**
     * 保护类型 dev转换为api
     *
     * @param protectionType 保护类型 dev
     * @return 保护类型 api
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ProtectionType devProtectionTypeToApi( org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType protectionType) {
        if (protectionType == null) {
            return null;
        }
        switch (protectionType) {
            case LagLoadBlancing:
                return ProtectionType.LagLoadBlancing;
            case NoProtection:
                return ProtectionType.NoProtection;
            case Och1Plus1:
                return ProtectionType.Och1Plus1;
            case Msp1Plus1:
                return ProtectionType.Msp1Plus1;
            case OduSncpS:
                return ProtectionType.OduSncpS;
            case OduSncpN:
                return ProtectionType.OduSncpN;
            case OduSncpI:
                return ProtectionType.OduSncpI;
            case VcSncp:
                return ProtectionType.VcSncp;
            case OsuSncpI:
                return ProtectionType.OsuSncpI;
            case OsuSncpN:
                return ProtectionType.OsuSncpN;
            case OsuSncpS:
                return ProtectionType.OsuSncpS;
            case Lag:
                return ProtectionType.Lag1To1;
            default:
                throw getNoMatchEnumValueException(protectionType);
        }
    }
    /**
     * 保护类型 dev转换为api
     *
     * @param protectionType 保护类型 dev
     * @return 保护类型 api
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ProtectionType devProtectionTypeToApi( org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.ProtectionType protectionType) {
        if (protectionType == null) {
            return null;
        }
        switch (protectionType) {
            case LagLoadBlancing:
                return ProtectionType.LagLoadBlancing;
            case NoProtection:
                return ProtectionType.NoProtection;
            case Och1Plus1:
                return ProtectionType.Och1Plus1;
            case Msp1Plus1:
                return ProtectionType.Msp1Plus1;
            case OduSncpS:
                return ProtectionType.OduSncpS;
            case OduSncpN:
                return ProtectionType.OduSncpN;
            case OduSncpI:
                return ProtectionType.OduSncpI;
            case VcSncp:
                return ProtectionType.VcSncp;
            case OsuSncpI:
                return ProtectionType.OsuSncpI;
            case OsuSncpN:
                return ProtectionType.OsuSncpN;
            case OsuSncpS:
                return ProtectionType.OsuSncpS;
            case Lag:
                return ProtectionType.Lag1To1;
            default:
                throw getNoMatchEnumValueException(protectionType);
        }
    }
    /**
     * SdTrigger  api to dev
     *
     * @param sdTrigger api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SdTrigger devSdTriggerToApi(SdTrigger sdTrigger) {
        if (sdTrigger == null) {
            return null;
        }
        switch (sdTrigger) {
            case Enabled:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SdTrigger.Enabled;
            case Disabled:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SdTrigger.Disabled;
            default:
                throw getNoMatchEnumValueException(sdTrigger);

        }
    }
    /**
     * SdTrigger  api to dev
     *
     * @param sdTrigger api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SdTrigger devSdTriggerToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.SdTrigger sdTrigger) {
        if (sdTrigger == null) {
            return null;
        }
        switch (sdTrigger) {
            case Enabled:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SdTrigger.Enabled;
            case Disabled:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SdTrigger.Disabled;
            default:
                throw getNoMatchEnumValueException(sdTrigger);

        }
    }
    /**
     * switchType dev to api
     *
     * @param switchDirection dev
     * @return api
     */
    default SwitchDirection devSwitchDirectionToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchType switchDirection) {
        if (switchDirection == null) {
            return null;
        }
        switch (switchDirection) {
            case UniSwitch:
                return SwitchDirection.UniSwitch;
            case BiSwitch:
                return SwitchDirection.BiSwitch;
            default:
                throw getNoMatchEnumValueException(switchDirection);
        }
    }
    /**
     * switchType dev to api
     *
     * @param switchDirection dev
     * @return api
     */
    default SwitchDirection devSwitchDirectionrev240702ToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchType switchDirection) {
        if (switchDirection == null) {
            return null;
        }
        switch (switchDirection) {
            case UniSwitch:
                return SwitchDirection.UniSwitch;
            case BiSwitch:
                return SwitchDirection.BiSwitch;
            default:
                throw getNoMatchEnumValueException(switchDirection);
        }
    }
    /**
     * ReversionMode dev to api
     *
     * @param reversionMode dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ReversionMode devReversionModeToApi(ReversionMode reversionMode) {
        if (reversionMode == null) {
            return null;
        }
        switch (reversionMode) {
            case Revertive:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ReversionMode.Revertive;
            case NonRevertive:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ReversionMode.NonRevertive;
            default:
                throw getNoMatchEnumValueException(reversionMode);
        }
    }
    /**
     * ReversionMode dev to api
     *
     * @param reversionMode dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ReversionMode devReversionModeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.ReversionMode reversionMode) {
        if (reversionMode == null) {
            return null;
        }
        switch (reversionMode) {
            case Revertive:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ReversionMode.Revertive;
            case NonRevertive:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ReversionMode.NonRevertive;
            default:
                throw getNoMatchEnumValueException(reversionMode);
        }
    }

    /**
     * SwitchReason api to dev
     *
     * @param switchReason api
     * @return dev
     */
    default SwitchReason apiSwitchReasonToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason switchReason) {
        if (switchReason == null) {
            return null;
        }
        switch (switchReason) {
            case ManualSwitch:
                return SwitchReason.ManualSwitch;
            case ForceSwitch:
                return SwitchReason.ForceSwitch;
            case NoSwitch:
                return SwitchReason.NoSwitch;
            case Lockout:
                return SwitchReason.Lockout;
            case Cleared:
                return SwitchReason.Cleared;
            case SdSwitch:
                return SwitchReason.SdSwitch;
            case SfSwitch:
                return SwitchReason.SfSwitch;
            case WaitForRestore:
                return SwitchReason.WaitForRestore;
            default:
                throw getNoMatchEnumValueException(switchReason);
        }
    }
    /**
     * SwitchReason api to dev
     *
     * @param switchReason api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchReason apiSwitchReasonrev240702ToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason switchReason) {
        if (switchReason == null) {
            return null;
        }
        switch (switchReason) {
            case ManualSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchReason.ManualSwitch;
            case ForceSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchReason.ForceSwitch;
            case NoSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchReason.NoSwitch;
            case Lockout:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchReason.Lockout;
            case Cleared:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchReason.Cleared;
            case SdSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchReason.SdSwitch;
            case SfSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchReason.SfSwitch;
            case WaitForRestore:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchReason.WaitForRestore;
            default:
                throw getNoMatchEnumValueException(switchReason);
        }
    }
    /**
     * SwitchReason api to dev
     *
     * @param switchReason api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.SwitchReason apiSwitchReasonRev210924ToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason switchReason) {
        if (switchReason == null) {
            return null;
        }
        switch (switchReason) {
            case ManualSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.SwitchReason.ManualSwitch;
            case ForceSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.SwitchReason.ForceSwitch;
            case NoSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.SwitchReason.NoSwitch;
            case Lockout:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.SwitchReason.Lockout;
            case Cleared:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.SwitchReason.Cleared;
            case SdSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.SwitchReason.SdSwitch;
            case SfSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.SwitchReason.SfSwitch;
            case WaitForRestore:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.SwitchReason.WaitForRestore;
            default:
                throw getNoMatchEnumValueException(switchReason);
        }
    }
    /**
     * SwitchReason api to dev
     *
     * @param switchReason api
     * @return dev
     */
    default SwitchReason apiSwitchReasonToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason switchReason) {
        if (switchReason == null) {
            return null;
        }
        switch (switchReason) {
            case ManualSwitch:
                return SwitchReason.ManualSwitch;
            case ForceSwitch:
                return SwitchReason.ForceSwitch;
            case NoSwitch:
                return SwitchReason.NoSwitch;
            case Lockout:
                return SwitchReason.Lockout;
            case Cleared:
                return SwitchReason.Cleared;
            case SdSwitch:
                return SwitchReason.SdSwitch;
            case SfSwitch:
                return SwitchReason.SfSwitch;
            case WaitForRestore:
                return SwitchReason.WaitForRestore;
            default:
                throw getNoMatchEnumValueException(switchReason);
        }
    }
    /**
     * SwitchReason api to dev
     *
     * @param switchReason api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason devSwitchReasonServicePgToApi(SwitchReason switchReason) {
        if (switchReason == null) {
            return null;
        }
        switch (switchReason) {
            case ManualSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.ManualSwitch;
            case ForceSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.ForceSwitch;
            case NoSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.NoSwitch;
            case Lockout:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.Lockout;
            case Cleared:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.Cleared;
            case SdSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.SdSwitch;
            case SfSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.SfSwitch;
            case WaitForRestore:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.WaitForRestore;
            default:
                throw getNoMatchEnumValueException(switchReason);
        }
    }
    /**
     * SwitchReason api to dev
     *
     * @param switchReason api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason devSwitchReasonServicePgrev240702ToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchReason switchReason) {
        if (switchReason == null) {
            return null;
        }
        switch (switchReason) {
            case ManualSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.ManualSwitch;
            case ForceSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.ForceSwitch;
            case NoSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.NoSwitch;
            case Lockout:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.Lockout;
            case Cleared:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.Cleared;
            case SdSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.SdSwitch;
            case SfSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.SfSwitch;
            case WaitForRestore:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason.WaitForRestore;
            default:
                throw getNoMatchEnumValueException(switchReason);
        }
    }
    /**
     * SwitchReason api to dev
     *
     * @param switchReason api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason devSwitchReasonToApi(SwitchReason switchReason) {
        if (switchReason == null) {
            return null;
        }
        switch (switchReason) {
            case ManualSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.ManualSwitch;
            case ForceSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.ForceSwitch;
            case NoSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.NoSwitch;
            case Lockout:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.Lockout;
            case Cleared:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.Cleared;
            case SdSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.SdSwitch;
            case SfSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.SfSwitch;
            case WaitForRestore:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.WaitForRestore;
            default:
                throw getNoMatchEnumValueException(switchReason);
        }
    }
    /**
     * SwitchReason api to dev
     *
     * @param switchReason api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason devSwitchReasonToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.SwitchReason switchReason) {
        if (switchReason == null) {
            return null;
        }
        switch (switchReason) {
            case ManualSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.ManualSwitch;
            case ForceSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.ForceSwitch;
            case NoSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.NoSwitch;
            case Lockout:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.Lockout;
            case Cleared:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.Cleared;
            case SdSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.SdSwitch;
            case SfSwitch:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.SfSwitch;
            case WaitForRestore:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason.WaitForRestore;
            default:
                throw getNoMatchEnumValueException(switchReason);
        }
    }
    /**
     * ProtectionDirection dev to api
     *
     * @param direction dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ProtectionDirection devProtectionDirectionServicePgToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionDirection direction) {
        if (direction == null) {
            return null;
        }
        switch (direction) {
            case ToPrimary:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ProtectionDirection.ToPrimary;
            case ToSecondary:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ProtectionDirection.ToSecondary;
            default:
                throw getNoMatchEnumValueException(direction);
        }
    }
    /**
     * ProtectionDirection dev to api
     *
     * @param direction dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ProtectionDirection devProtectionDirectionServicePgrev240702ToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionDirection direction) {
        if (direction == null) {
            return null;
        }
        switch (direction) {
            case ToPrimary:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ProtectionDirection.ToPrimary;
            case ToSecondary:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ProtectionDirection.ToSecondary;
            default:
                throw getNoMatchEnumValueException(direction);
        }
    }
    /**
     * ProtectionDirection dev to api
     *
     * @param direction dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ProtectionDirection devProtectionDirectionToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionDirection direction) {
        if (direction == null) {
            return null;
        }
        switch (direction) {
            case ToPrimary:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ProtectionDirection.ToPrimary;
            case ToSecondary:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ProtectionDirection.ToSecondary;
            default:
                throw getNoMatchEnumValueException(direction);
        }
    }
    /**
     * ProtectionDirection dev to api
     *
     * @param direction dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ProtectionDirection devProtectionDirectionToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.ProtectionDirection direction) {
        if (direction == null) {
            return null;
        }
        switch (direction) {
            case ToPrimary:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ProtectionDirection.ToPrimary;
            case ToSecondary:
                return org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ProtectionDirection.ToSecondary;
            default:
                throw getNoMatchEnumValueException(direction);
        }
    }

    /**
     * fileType api to dev
     * @param fileType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.FileType
            apiFileTypeToDev(FileType fileType){
        if(fileType==null){
            return null;
        }
        switch (fileType){
            case Data:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.FileType.Data;
            case Upgrade:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.FileType.Upgrade;
            default:
                throw getNoMatchEnumValueException(fileType);
        }
    }

    /**
     * ActiveStatus dev to api
     * @param activeStatus dev
     * @return api
     */
    default ActiveStatus devActiveStatusToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.ActiveStatus activeStatus){
        if (activeStatus==null){
            return null;
        }
        switch (activeStatus){
            case Fail:
                return ActiveStatus.Fail;
            case Success:
                return ActiveStatus.Success;
            case Activing:
                return ActiveStatus.Activing;
            default:
                throw getNoMatchEnumValueException(activeStatus);
        }
    }

    /**
     * FailReason dev to api
     * @param failReason dev
     * @return api
     */
    default FailReason devFailReasonToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.FailReason failReason){
        if(failReason==null){
            return null;
        }
        switch (failReason){
            case Timeout:
                return FailReason.Timeout;
            case AuthFail:
                return FailReason.AuthFail;
            case SocketFail:
                return FailReason.SocketFail;
            case FileNotExist:
                return FailReason.FileNotExist;
            case ValidateFail:
                return FailReason.ValidateFail;
            case DataInGenerating:
                return FailReason.DataInGenerating;
            case FileTypeMismatch:
                return FailReason.FileTypeMismatch;
            case UpgradeInProgress:
                return FailReason.UpgradeInProgress;
            default:
                throw getNoMatchEnumValueException(failReason);
        }
    }

    /**
     * fileType dev to api
     * @param fileType dev
     * @return api
     */
    default FileType devFileTypeToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.FileType fileType){
        if(fileType==null){
            return null;
        }
        switch (fileType){
            case Data:
                return FileType.Data;
            case Upgrade:
                return FileType.Upgrade;
            default:
                throw getNoMatchEnumValueException(fileType);
        }
    }

    default DownloadStatus devDownloadStatusToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.DownloadStatus downloadStatus){
        if(downloadStatus==null){
            return null;
        }
        switch (downloadStatus){
            case Fail:
                return DownloadStatus.Fail;
            case Validated:
                return DownloadStatus.Validated;
            case Downloaded:
                return DownloadStatus.Downloaded;
            case Validating:
                return DownloadStatus.Validating;
            case Downloading:
                return DownloadStatus.Downloading;
            default:
                throw getNoMatchEnumValueException(downloadStatus);
        }
    }

    /**
     * 将设备LaserStatus 转换成api
     * @param laserStatus 设备数据结构
     * @return api
     */
    default LaserStatus devLaserStatusToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.LaserStatus laserStatus){
        if(laserStatus==null){
            return null;
        }
        switch (laserStatus) {
            case NoOpticalModule:
                return LaserStatus.NoOpticalModule;
            case LaserOff:
                return LaserStatus.LaserOff;
            case LaserOn:
                return LaserStatus.LaserOn;
            default:
                throw getNoMatchEnumValueException(laserStatus);
        }
    }

    /**
     * InterfaceType dev to api
     * @param interfaceType dev
     * @return api
     */
    default InterfaceType devInterfaceTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.InterfaceType interfaceType){
        if(interfaceType==null){
            return null;
        }
        switch (interfaceType) {
            case NNI:
                return InterfaceType.NNI;
            case UNI:
                return InterfaceType.UNI;
            default:
                throw getNoMatchEnumValueException(interfaceType);
        }
    }

    /**
     * LoopbackType acc to otn
     * @param loopbackType acc
     * @return otn
     */
    default LoopbackType devLoopbackTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.LoopbackType loopbackType) {
        if(loopbackType == null){
            return null;
        }
        switch (loopbackType) {
            case NonLoopback:
                return LoopbackType.NonLoopback;
            case FacilityLoopback:
                return LoopbackType.FacilityLoopback;
            case TerminalLoopback:
                return LoopbackType.TerminalLoopback;
            default:
                throw getNoMatchEnumValueException(loopbackType);
        }
    }

    /**
     * WorkingMode dev to api
     * @param list dev
     * @return api
     */
    default Set<WorkingMode> devWorkingModeToApiList(
            Set<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.WorkingMode> list){
        if(list==null || list.isEmpty()){
            return null;
        }
        Set<WorkingMode> resultList = new HashSet<>();
        for(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.WorkingMode workingMode:list){
            resultList.add(devWorkingModeToApi(workingMode));
        }
        return resultList;
    }

    /**
     * workingMode dev to api
     * @param workingMode dev
     * @return api
     */
    default WorkingMode devWorkingModeToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.WorkingMode workingMode){
        if(workingMode==null){
            return null;
        }
        switch (workingMode) {
            case _1000MFullDuplex:
                return WorkingMode._1000MFullDuplex;
            case _100MFullDuplex:
                return WorkingMode._100MFullDuplex;
            case _10GEFullDuplex:
                return WorkingMode._10GEFullDuplex;
            case Auto:
                return WorkingMode.Auto;
            default:
                throw getNoMatchEnumValueException(workingMode);
        }
    }

    /**
     * PortType dev to api
     * @param portType dev
     * @return api
     */
    default PortType devPortTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.PortType portType){
        if(portType==null){
            return null;
        }
        switch (portType){
            case Optical:
                return PortType.Optical;
            case Electrical:
                return PortType.Electrical;
            default:
                throw getNoMatchEnumValueException(portType);
        }
    }

    /**
     * AdaptationType dev to api
     * @param list dev
     * @return api
     */
    default Set<AdaptationType> devAdaptationTypeToApiList(
            Set<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType> list){
        if(list==null || list.isEmpty()){
            return null;
        }
        Set<AdaptationType> resultList = new HashSet<>();
        for(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType e:list){
            resultList.add(devAdaptationTypeToApi(e));
        }
        return resultList;
    }

    /**
     * ClientSignalType dev to api
     * @param list dev
     * @return api
     */
    default Set<ClientSignalType> devClientSignalTypeToApiList(
            Set<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.SignalType> list){
        if(list==null || list.isEmpty()){
            return null;
        }
        Set<ClientSignalType> resultList = new HashSet<>();
        for(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.SignalType e : list){
            resultList.add(devClientSignalTypeToApi(e));
        }
        return resultList;
    }

    /**
     * E1FrameType dev to api
     * @param e1FrameType dev
     * @return api
     */
    default E1FrameType devE1FrameTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.E1FrameType e1FrameType){
        if(e1FrameType==null){
            return null;
        }
        switch (e1FrameType){
            case Unframe:
                return E1FrameType.Unframe;
            case PCM30:
                return E1FrameType.PCM30;
            case PCM30C:
                return E1FrameType.PCM30C;
            case PCM31:
                return E1FrameType.PCM31;
            case PCM31C:
                return E1FrameType.PCM31C;
            default:
                throw getNoMatchEnumValueException(e1FrameType);
        }
    }

    /**
     * E1Opcode dev to api
     * @param e1Opcode dev
     * @return api
     */
    default E1Opcode devE1OpcodeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.E1Opcode e1Opcode){
        if(e1Opcode==null){
            return null;
        }
        switch (e1Opcode){
            case AMI:
                return E1Opcode.AMI;
            case HDB3:
                return E1Opcode.HDB3;
            default:
                throw getNoMatchEnumValueException(e1Opcode);
        }
    }

    /**
     * E1PhyType dev to api
     * @param e1PhyType dev
     * @return api
     */
    default E1PhyType devE1PhyTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.E1PhyType e1PhyType){
        if(e1PhyType==null){
            return null;
        }
        switch (e1PhyType){
            case _75o:
                return E1PhyType._75o;
            case _120o:
                return E1PhyType._120o;
            default:
                throw getNoMatchEnumValueException(e1PhyType);
        }
    }

    /**
     * InsertType api to dev
     * @param insertType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.InsertType apiInsertTypeToDev(
            InsertType insertType){
        if(insertType==null){
            return null;
        }
        switch (insertType){
            case Null:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.InsertType.Null;
            case PN11:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.InsertType.PN11;
            case MSAIS:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.InsertType.MSAIS;
            case DelayInsert:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.InsertType.DelayInsert;
            case DirectInsert:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.InsertType.DirectInsert;
            default:
                throw getNoMatchEnumValueException(insertType);
        }
    }

    /**
     * WorkingMode api to dev
     * @param workingMode api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.WorkingMode apiWorkingModeToDev(
            WorkingMode workingMode){
        if(workingMode==null){
            return null;
        }
        switch (workingMode){
            case Auto:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.WorkingMode.Auto;
            case _10GEFullDuplex:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.WorkingMode._10GEFullDuplex;
            case _100MFullDuplex:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.WorkingMode._100MFullDuplex;
            case _1000MFullDuplex:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.WorkingMode._1000MFullDuplex;
            default:
                throw getNoMatchEnumValueException(workingMode);
        }

    }

    /**
     * PortType api to dev
     * @param portType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.PortType apiPortTypeToDev(PortType portType){
        if(portType==null){
            return null;
        }
        switch (portType){
            case Optical:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.PortType.Optical;
            case Electrical:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.PortType.Electrical;
            default:
                throw getNoMatchEnumValueException(portType);
        }
    }

    /**
     * InsertType dev to api
     * @param insertType dev
     * @return api
     */
    default InsertType devInsertTypeToApi(
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.InsertType insertType){
        if(insertType==null){
            return null;
        }
        switch (insertType){
            case Null:
                return InsertType.Null;
            case PN11:
                return InsertType.PN11;
            case MSAIS:
                return InsertType.MSAIS;
            case DelayInsert:
                return InsertType.DelayInsert;
            case DirectInsert:
                return InsertType.DirectInsert;
            default:
                throw getNoMatchEnumValueException(insertType);
        }
    }

    /**
     * E1FrameType api to dev
     * @param e1FrameType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.E1FrameType apiE1FrameTypeToDev(E1FrameType e1FrameType){
        if(e1FrameType==null){
            return null;
        }
        switch (e1FrameType){
            case Unframe:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.E1FrameType.Unframe;
            case PCM30:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.E1FrameType.PCM30;
            case PCM30C:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.E1FrameType.PCM30C;
            case PCM31:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.E1FrameType.PCM31;
            case PCM31C:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.E1FrameType.PCM31C;
            default:
                throw getNoMatchEnumValueException(e1FrameType);
        }
    }

    /**
     * E1Opcode api to dev
     * @param e1Opcode api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.E1Opcode apiE1OpcodeToDev(E1Opcode e1Opcode){
        if(e1Opcode==null){
            return null;
        }
        switch (e1Opcode){
            case AMI:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.E1Opcode.AMI;
            case HDB3:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.E1Opcode.HDB3;
            default:
                throw getNoMatchEnumValueException(e1Opcode);
        }
    }

    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.InterfaceType apiInterfaceToDev(InterfaceType interfaceType) {
        if (interfaceType == null) {
            return null;
        }
        switch (interfaceType) {
            case UNI:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.InterfaceType.UNI;
            case NNI:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.InterfaceType.NNI;
            default:
                throw getNoMatchEnumValueException(interfaceType);

        }
    }

    /**
     * 端口环回类型 api转dev
     *
     * @param loopbackType dev 端口环回类型
     * @return api 端口环回类型
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.LoopbackType apiLoopBackTypeToDev(LoopbackType loopbackType) {
        if (loopbackType == null) {
            return null;
        }
        switch (loopbackType) {
            case NonLoopback:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.LoopbackType.NonLoopback;
            case FacilityLoopback:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.LoopbackType.FacilityLoopback;
            case TerminalLoopback:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.LoopbackType.TerminalLoopback;
            default:
                throw getNoMatchEnumValueException(loopbackType);
        }
    }

    /**
     * LaserStatus api to dev
     * @param laserStatus api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.LaserStatus apiLaserStatusToDev(LaserStatus laserStatus){
        if(laserStatus==null){
            return null;
        }
        switch (laserStatus){
            case LaserOn:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.LaserStatus.LaserOn;
            case LaserOff:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.LaserStatus.LaserOff;
            case NoOpticalModule:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.LaserStatus.NoOpticalModule;
            default:
                throw getNoMatchEnumValueException(laserStatus);
        }
    }
    /**
     * ProtectionDirection api to dev
     *
     * @param direction api
     * @return dev
     */
    default ProtectionDirection apiProtectionDirectionToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ProtectionDirection direction) {
        if (direction == null) {
            return null;
        }
        switch (direction) {
            case ToPrimary:
                return ProtectionDirection.ToPrimary;
            case ToSecondary:
                return ProtectionDirection.ToSecondary;
            default:
                throw getNoMatchEnumValueException(direction);
        }
    }

    /**
     * ProtectionDirection api to dev
     *
     * @param direction api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.ProtectionDirection apiProtectionDirectionRev210924ToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ProtectionDirection direction) {
        if (direction == null) {
            return null;
        }
        switch (direction) {
            case ToPrimary:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.ProtectionDirection.ToPrimary;
            case ToSecondary:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev210924.ProtectionDirection.ToSecondary;
            default:
                throw getNoMatchEnumValueException(direction);
        }
    }
    /**
     * ProtectionDirection api to dev
     *
     * @param direction api
     * @return dev
     */
    default ProtectionDirection apiProtectionDirectionToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ProtectionDirection direction) {
        if (direction == null) {
            return null;
        }
        switch (direction) {
            case ToPrimary:
                return ProtectionDirection.ToPrimary;
            case ToSecondary:
                return ProtectionDirection.ToSecondary;
            default:
                throw getNoMatchEnumValueException(direction);
        }
    }
    /**
     * ProtectionDirection api to dev
     *
     * @param direction api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionDirection apiProtectionDirectionrev240702ToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ProtectionDirection direction) {
        if (direction == null) {
            return null;
        }
        switch (direction) {
            case ToPrimary:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionDirection.ToPrimary;
            case ToSecondary:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionDirection.ToSecondary;
            default:
                throw getNoMatchEnumValueException(direction);
        }
    }
    /**
     * PortMemberRole api to dev
     *
     * @param role api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.PortMemberRole apiPortMemberRoleToDev(PortMemberRole role) {
        if (role == null) {
            return null;
        }
        switch (role) {
            case Slave:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.PortMemberRole.Slave;
            case Master:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.PortMemberRole.Master;
            default:
                throw getNoMatchEnumValueException(role);
        }
    }

    /**
     * LoadAlgorithm api to dev
     *
     * @param loadAlgorithm api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LoadAlgorithm apiLoadAlgorithmToDev(LoadAlgorithm loadAlgorithm) {
        if (loadAlgorithm == null) {
            return null;
        }
        switch (loadAlgorithm) {
            case DIP:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LoadAlgorithm.DIP;
            case SIP:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LoadAlgorithm.SIP;
            case DMAC:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LoadAlgorithm.DMAC;
            case SDIP:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LoadAlgorithm.SDIP;
            case SMAC:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LoadAlgorithm.SMAC;
            case SDMAC:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LoadAlgorithm.SDMAC;
            default:
                throw getNoMatchEnumValueException(loadAlgorithm);
        }
    }
    /**
     * AggregationMode api to dev
     * @param mode api
     * @return dev
     * */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.AggregationMode apiAggregationModeToDev(AggregationMode mode) {
        if (mode == null) {
            return null;
        }
        switch (mode) {
            case Lacp:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.AggregationMode.Lacp;
            case Manual:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.AggregationMode.Manual;
            default:
                throw getNoMatchEnumValueException(mode);
        }
    }

    /**
     * PortMemberRole dev to api
     *
     * @param role dev
     * @return api
     */
    default PortMemberRole devPortMemberRoleToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.PortMemberRole role) {
        if (role == null) {
            return null;
        }
        switch (role) {
            case Slave:
                return PortMemberRole.Slave;
            case Master:
                return PortMemberRole.Master;
            default:
                throw getNoMatchEnumValueException(role);
        }
    }

    /**
     * LoadAlgorithm dev to api
     *
     * @param loadAlgorithm dev
     * @return api
     */
    default LoadAlgorithm devLoadAlgorithmToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LoadAlgorithm loadAlgorithm) {
        if (loadAlgorithm == null) {
            return null;
        }
        switch (loadAlgorithm) {
            case DIP:
                return LoadAlgorithm.DIP;
            case SIP:
                return LoadAlgorithm.SIP;
            case DMAC:
                return LoadAlgorithm.DMAC;
            case SDIP:
                return LoadAlgorithm.SDIP;
            case SMAC:
                return LoadAlgorithm.SMAC;
            case SDMAC:
                return LoadAlgorithm.SDMAC;
            default:
                throw getNoMatchEnumValueException(loadAlgorithm);
        }
    }
    /**
     * AggregationMode dev to api
     * @param mode dev
     * @return api
     * */
    default AggregationMode devAggregationModeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.AggregationMode mode) {
        if (mode == null) {
            return null;
        }
        switch (mode) {
            case Lacp:
                return AggregationMode.Lacp;
            case Manual:
                return AggregationMode.Manual;
            default:
                throw getNoMatchEnumValueException(mode);
        }
    }

    /**
     * InterfaceType dev to api
     * @param interfaceType dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.InterfaceType apiInterfaceTypeToDev(InterfaceType interfaceType){
        if(interfaceType==null){
            return null;
        }
        switch (interfaceType){
            case UNI:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.InterfaceType.UNI;
            case NNI:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.InterfaceType.NNI;
            default:
                throw getNoMatchEnumValueException(interfaceType);
        }
    }
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.ModifyAction apiOsuModifyActionToDev(ModifyAction modifyAction){
        if(modifyAction==null){
            return null;
        }
        switch (modifyAction){
            case DeleteAction:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.ModifyAction.Delete;
            case AbortAction:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.ModifyAction.Rollback;
            case AddAction:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.ModifyAction.Add;
            default:
                 throw getNoMatchEnumValueException(modifyAction);
        }
    }
    /**
     * PrbsMeasureState dev to ap
     * */
    default PrbsMeasureState devToPrbsMeasureStateApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.PrbsMeasureState state){
        if(state==null){
            return null;
        }
        switch (state){
            case Finished:
                return PrbsMeasureState.Finished;
            case Measuring:
                return PrbsMeasureState.Measuring;
            default:
                throw getNoMatchEnumValueException(state);
        }
    }
    /**
    * PrbsResultType dev to ap
    * */
    default PrbsResultType devToPrbsResultTypeApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.PrbsResultType resultType){
        if(resultType==null){
            return null;
        }
        switch (resultType){
            case LineBreak:
                return PrbsResultType.LineBreak;
            case LineNormal:
                return PrbsResultType.LineNormal;
            case LineDegrade:
                return PrbsResultType.LineDegrade;
            default:
                throw getNoMatchEnumValueException(resultType);
        }
    }
}
