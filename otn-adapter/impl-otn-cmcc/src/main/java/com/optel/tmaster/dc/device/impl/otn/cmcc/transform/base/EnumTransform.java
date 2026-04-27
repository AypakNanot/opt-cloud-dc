/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base;

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
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.rev210927.GHaoStatus;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.rev210927.ModifyAction;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.rev210927.OduPosition;
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
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.Clk2mType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.QL;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.clock.rev210820.SsmMode;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.MeStatus;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ResetType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.*;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.ProtectionDirection;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.ReversionMode;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.SdTrigger;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.SwitchReason;
import org.opendaylight.yangtools.yang.binding.BaseIdentity;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.Set;

/**
 * EnumTransform
 * 数据转换
 * date       time        author
 * ─────────────────────────────
 * 2021/10/8   10:51      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public interface EnumTransform extends ITransform {

    default <T> T d2e(BaseIdentity st, Class<T> cls) {
        if (st == null) {
            return null;
        }
        String localName = st.implementedInterface().getSimpleName();
        try {
            Method forName = cls.getDeclaredMethod("forName", String.class);
            return (T) forName.invoke(null, localName);
        } catch (InvocationTargetException | NoSuchMethodException | IllegalAccessException e) {
            throw getNoMatchEnumValueException(st);
        }
    }

    /**
     * SwitchType dev to api
     *
     * @param list dev
     * @return api
     */
    default Set<SwitchType> devSwitchTypeToApiList(
            Collection<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.SwitchType> list) {
        return cts(list, this::devSwitchTypeToApi);
    }

    /**
     * 将 dev identity switch-type 转换为 api identity switch-type
     *
     * @param st dev switch-type
     * @return api switch-type
     */
    default SwitchType devSwitchTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.SwitchType st) {
        return d2e(st, SwitchType.class);
    }


    /**
     * SwitchType api to dev
     *
     * @param sts api
     * @return dev
     */
    default Set<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.SwitchType> apiSwitchTypeToDevList(
            Collection<SwitchType> sts) {
        return cts(sts, this::apiSwitchTypeToDev);
    }

    /**
     * 将 api identity switch-type  转换为dev identity switch-type
     *
     * @param switchType api switch-type
     * @return dev switch-type
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.SwitchType apiSwitchTypeToDev(
            SwitchType switchType) {
        if (switchType == null) {
            return null;
        }
        return switch (switchType) {
            case ODU0 -> ODU0.VALUE;
            case ODU1 -> ODU1.VALUE;
            case ODU2 -> ODU2.VALUE;
            case ODU2e -> ODU2e.VALUE;
            case ODU3 -> ODU3.VALUE;
            case ODU4 -> ODU4.VALUE;
            case ODUflexCBR -> ODUflexCBR.VALUE;
            case ODUflexGFP -> ODUflexGFP.VALUE;
            case VC12 -> VC12.VALUE;
            case VC3 -> VC3.VALUE;
            case VC4 -> VC4.VALUE;
            default -> throw getNoMatchEnumValueException(switchType);
        };
    }

    /**
     * AdaptationType dev to api
     *
     * @param list dev
     * @return api
     */
    default Set<AdaptationType> devAdaptationTypeToApiList(Collection<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.AdaptationType> list) {
        return cts(list, this::devAdaptationTypeToApi);
    }

    /**
     * dev to api
     *
     * @param at dev
     * @return api
     */
    default AdaptationType devAdaptationTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.AdaptationType at) {
        return d2e(at,AdaptationType.class);
    }


    /**
     * adaptationType api转dev
     *
     * @param adaptationType api adaptationType
     * @return dev adaptationType
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.AdaptationType apiAdaptationTypeToDev(
            AdaptationType adaptationType) {
        if (adaptationType == null) {
            return null;
        }
        return switch (adaptationType) {
            case CBRAMP -> CBRAMP.VALUE;
            case CBRBMP -> CBRBMP.VALUE;
            case CBRx -> CBRx.VALUE;
            case GFPF -> GFPF.VALUE;
            case GFPT -> GFPT.VALUE;
            case GMP -> GMP.VALUE;
            case NULL -> NULL.VALUE;
            case ODUij -> ODUij.VALUE;
            case ODUj21 -> ODUj21.VALUE;
            case ODUk -> ODUk.VALUE;
            case PRBS -> PRBS.VALUE;
            default -> throw getNoMatchEnumValueException(adaptationType);
        };
    }


    /**
     * ClientSignalType dev to api
     *
     * @param list dev
     * @return api
     */
    default Set<ClientSignalType> devClientSignalTypeToApiList(Collection<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.ClientSignalType> list) {
        return cts(list, this::devClientSignalTypeToApi);
    }


    /**
     * ClientSignalType dev to api
     *
     * @param cst dev ClientSignalType
     * @return api ClientSignalType
     */
    default ClientSignalType devClientSignalTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.ClientSignalType cst) {
        if (cst == null) {
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.ClientSignalType> clientSignalType = cst.implementedInterface();
        if (clientSignalType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.E1.class)) {
            return ClientSignalType.E1;
        }
        if (clientSignalType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.FE.class)) {
            return ClientSignalType.FE;
        }
        if (clientSignalType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.GE.class)) {
            return ClientSignalType.GE;
        }
        if (clientSignalType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.LAN10GE.class)) {
            return ClientSignalType.LAN10GE;
        }
        if (clientSignalType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.NoSignal.class)) {
            return ClientSignalType.NoSignal;
        }
        if (clientSignalType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.OTU0.class)) {
            return ClientSignalType.OTU0;
        }
        if (clientSignalType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.OTU1.class)) {
            return ClientSignalType.OTU1;
        }
        if (clientSignalType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.OTU2.class)) {
            return ClientSignalType.OTU2;
        }
        if (clientSignalType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.OTU2e.class)) {
            return ClientSignalType.OTU2e;
        }
        if (clientSignalType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.OTU4.class)) {
            return ClientSignalType.OTU4;
        }
        if (clientSignalType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.STM1.class)) {
            return ClientSignalType.STM1;
        }
        if (clientSignalType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.STM4.class)) {
            return ClientSignalType.STM4;
        }
        if (clientSignalType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.STM16.class)) {
            return ClientSignalType.STM16;
        }
        if (clientSignalType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.STM64.class)) {
            return ClientSignalType.STM64;
        }
        throw getNoMatchEnumValueException(cst);
    }

    /**
     * ClientSignalType api to dev
     *
     * @param clientSignalType api signalType
     * @return dev signalType
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.ClientSignalType apiClientSignalTypeToDev(
            ClientSignalType clientSignalType) {
        if (clientSignalType == null) {
            return null;
        }
        return switch (clientSignalType) {
            case E1 -> E1.VALUE;
            case FE -> FE.VALUE;
            case GE -> GE.VALUE;
            case LAN10GE -> LAN10GE.VALUE;
            case NoSignal -> NoSignal.VALUE;
            case OTU0 -> OTU0.VALUE;
            case OTU1 -> OTU1.VALUE;
            case OTU2 -> OTU2.VALUE;
            case OTU2e -> OTU2e.VALUE;
            case OTU4 -> OTU4.VALUE;
            case STM1 -> STM1.VALUE;
            case STM4 -> STM4.VALUE;
            case STM16 -> STM16.VALUE;
            case STM64 -> STM64.VALUE;
            default -> throw getNoMatchEnumValueException(clientSignalType);
        };
    }

    /**
     * 设备类型 dev转为api
     *
     * @param deviceType 设备类型 dev
     * @return 设备类型 api
     */
    default DeviceType devDeviceTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.DeviceType deviceType) {
        if (deviceType == null) {
            return null;
        }
        switch (deviceType) {
            case OtmPnp:
                return DeviceType.OtmPnp;
            case OtmBox:
                return DeviceType.OtmBox;
            case PoPnp:
                return DeviceType.PoPnp;
            case PoBox:
                return DeviceType.PoBox;
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
    default PortRole devPortRoleToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.PortRole portRole) {
        if (portRole == null) {
            return null;
        }
        switch (portRole) {
            case Leaf:
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
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.PortRole apiPortRoleToDev(PortRole portRole) {
        if (portRole == null) {
            return null;
        }
        switch (portRole) {
            case Leaf:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.PortRole.Leaf;
            case Root:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.PortRole.Root;
            case Symmetric:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.PortRole.Symmetric;
            default:
                throw getNoMatchEnumValueException(portRole);
        }
    }

    /**
     * 端口环回类型 api转dev
     *
     * @param loopbackType dev 端口环回类型
     * @return api 端口环回类型
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LoopbackType apiLoopBackTypeToDev(LoopbackType loopbackType) {
        if (loopbackType == null) {
            return null;
        }
        switch (loopbackType) {
            case NonLoopback:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LoopbackType.NonLoopback;
            case FacilityLoopback:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LoopbackType.FacilityLoopback;
            case TerminalLoopback:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LoopbackType.TerminalLoopback;
            default:
                throw getNoMatchEnumValueException(loopbackType);
        }
    }

    /**
     * 端口环回类型 dev转api
     *
     * @param loopbackType dev 端口环回类型
     * @return api 端口环回类型
     */
    default LoopbackType devLoopBackTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LoopbackType loopbackType) {
        if (loopbackType == null) {
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
     * 端口保护角色 dev转api
     *
     * @param protectRole 端口保护角色
     * @return 端口保护角色 api
     */
    default ProtectRole devProtectRoleToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ProtectRole protectRole) {
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
    default GHaoStatus devGHaoStatusToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.GHaoStatus gHaoStatus) {
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
    default OamTransmitInterval devOamTransmitIntervalToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.OamTransmitInterval oamTransmitInterval) {
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
     * PortType dev to api
     *
     * @param portType dev
     * @return api
     */
    default PortType devPortTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.PortType portType) {
        if (portType == null) {
            return null;
        }
        if (portType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.PortType.Electrical)) {
            return PortType.Electrical;
        }
        if (portType.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.PortType.Optical)) {
            return PortType.Optical;
        }
        throw getNoMatchEnumValueException(portType);
    }

    /**
     * PortType api to dev
     *
     * @param portType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.PortType apiPortTypeToDev(PortType portType) {
        if (portType == null) {
            return null;
        }
        switch (portType) {
            case Optical:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.PortType.Optical;
            case Electrical:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.PortType.Electrical;
            default:
                throw getNoMatchEnumValueException(portType);
        }
    }

    /**
     * workingMode dev to api
     *
     * @param workingMode dev
     * @return api
     */
    default WorkingMode devWorkingModeToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.WorkingMode workingMode) {
        if (workingMode == null) {
            return null;
        }
        if (workingMode.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.WorkingMode.Auto)) {
            return WorkingMode.Auto;
        }
        if (workingMode.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.WorkingMode._100MFullDuplex)) {
            return WorkingMode._100MFullDuplex;
        }
        if (workingMode.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.WorkingMode._1000MFullDuplex)) {
            return WorkingMode._1000MFullDuplex;
        }
        if (workingMode.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.WorkingMode._10GEFullDuplex)) {
            return WorkingMode._10GEFullDuplex;
        }
        throw getNoMatchEnumValueException(workingMode);
    }

    /**
     * InterfaceType dev to api
     *
     * @param interfaceType dev
     * @return api
     */
    default InterfaceType devInterfaceTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.InterfaceType interfaceType) {
        if (interfaceType == null) {
            return null;
        }
        switch (interfaceType) {
            case UNI:
                return InterfaceType.UNI;
            case NNI:
                return InterfaceType.NNI;
            default:
                throw getNoMatchEnumValueException(interfaceType);
        }
    }

    /**
     * InterfaceType dev to api
     *
     * @param interfaceType dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.InterfaceType apiInterfaceTypeToDev(InterfaceType interfaceType) {
        if (interfaceType == null) {
            return null;
        }
        switch (interfaceType) {
            case UNI:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.InterfaceType.UNI;
            case NNI:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.InterfaceType.NNI;
            default:
                throw getNoMatchEnumValueException(interfaceType);
        }
    }

    /**
     * 将设备LaserStatus 转换成api
     *
     * @param laserStatus 设备数据结构
     * @return api
     */
    default LaserStatus devLaserStatusToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LaserStatus laserStatus) {
        if (laserStatus == null) {
            return null;
        }
        if (laserStatus.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LaserStatus.LaserOn)) {
            return LaserStatus.LaserOn;
        }
        if (laserStatus.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LaserStatus.LaserOff)) {
            return LaserStatus.LaserOff;
        }
        if (laserStatus.equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LaserStatus.NoOpticalModule)) {
            return LaserStatus.NoOpticalModule;
        }
        throw getNoMatchEnumValueException(laserStatus);
    }

    /**
     * LaserStatus api to dev
     *
     * @param laserStatus api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LaserStatus apiLaserStatusToDev(LaserStatus laserStatus) {
        if (laserStatus == null) {
            return null;
        }
        switch (laserStatus) {
            case LaserOn:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LaserStatus.LaserOn;
            case LaserOff:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LaserStatus.LaserOff;
            case NoOpticalModule:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LaserStatus.NoOpticalModule;
            default:
                throw getNoMatchEnumValueException(laserStatus);
        }
    }

    /**
     * LoopbackType acc to otn
     *
     * @param loopbackType acc
     * @return otn
     */
    default LoopbackType devLoopbackTypeToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LoopbackType loopbackType) {
        if (loopbackType == null) {
            return null;
        }
        if (loopbackType.equals(
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LoopbackType.FacilityLoopback)) {
            return LoopbackType.FacilityLoopback;
        }
        if (loopbackType.equals(
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LoopbackType.TerminalLoopback)) {
            return LoopbackType.TerminalLoopback;
        }
        if (loopbackType.equals(
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.LoopbackType.NonLoopback)) {
            return LoopbackType.NonLoopback;
        }
        throw getNoMatchEnumValueException(loopbackType);
    }

    /**
     * WorkingMode dev to api
     *
     * @param list dev
     * @return api
     */
    default Set<WorkingMode> devWorkingModeToApiList(
            Collection<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.WorkingMode> list) {
        return cts(list, this::devWorkingModeToApi);
    }

    /**
     * E1FrameType dev to api
     *
     * @param e1FrameType dev
     * @return api
     */
    default E1FrameType devE1FrameTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.E1FrameType e1FrameType) {
        if (e1FrameType == null) {
            return null;
        }
        switch (e1FrameType) {
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
     * E1FrameType api to dev
     *
     * @param e1FrameType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.E1FrameType apiE1FrameTypeToDev(E1FrameType e1FrameType) {
        if (e1FrameType == null) {
            return null;
        }
        switch (e1FrameType) {
            case Unframe:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.E1FrameType.Unframe;
            case PCM30:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.E1FrameType.PCM30;
            case PCM30C:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.E1FrameType.PCM30C;
            case PCM31:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.E1FrameType.PCM31;
            case PCM31C:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.E1FrameType.PCM31C;
            default:
                throw getNoMatchEnumValueException(e1FrameType);
        }
    }


    /**
     * E1Opcode dev to api
     *
     * @param e1Opcode dev
     * @return api
     */
    default E1Opcode devE1OpcodeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.E1Opcode e1Opcode) {
        if (e1Opcode == null) {
            return null;
        }
        switch (e1Opcode) {
            case AMI:
                return E1Opcode.AMI;
            case HDB3:
                return E1Opcode.HDB3;
            default:
                throw getNoMatchEnumValueException(e1Opcode);
        }
    }

    /**
     * E1Opcode api to dev
     *
     * @param e1Opcode api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.E1Opcode apiE1OpcodeToDev(E1Opcode e1Opcode) {
        if (e1Opcode == null) {
            return null;
        }
        switch (e1Opcode) {
            case AMI:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.E1Opcode.AMI;
            case HDB3:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.E1Opcode.HDB3;
            default:
                throw getNoMatchEnumValueException(e1Opcode);
        }
    }

    /**
     * E1PhyType dev to api
     *
     * @param e1PhyType dev
     * @return api
     */
    default E1PhyType devE1PhyTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.E1PhyType e1PhyType) {
        if (e1PhyType == null) {
            return null;
        }
        switch (e1PhyType) {
            case _75o:
                return E1PhyType._75o;
            case _120o:
                return E1PhyType._120o;
            default:
                throw getNoMatchEnumValueException(e1PhyType);
        }
    }

    /**
     * EthMappingType dev to api
     *
     * @param ethMappingType dev
     * @return api
     */
    default EthMappingType devEthMappingTypeToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.EthMappingType ethMappingType) {
        if (ethMappingType == null) {
            return null;
        }
        switch (ethMappingType) {
            case EoO:
                return EthMappingType.EoO;
            case EoS:
                return EthMappingType.EoS;
            default:
                throw getNoMatchEnumValueException(ethMappingType);
        }
    }

    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.EthMappingType apiEthMappingTypeToDev(
            EthMappingType ethMappingType) {
        if (ethMappingType == null) {
            return null;
        }
        switch (ethMappingType) {
            case EoO:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.EthMappingType.EoO;
            case EoS:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.EthMappingType.EoS;
            default:
                throw getNoMatchEnumValueException(ethMappingType);
        }
    }

    default Set<EthMappingType> devEthMappingTypeToApiList(
            Collection<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.EthMappingType> list) {
        return cts(list, this::devEthMappingTypeToApi);
    }


    /**
     * 源端宿端模式 dev转api
     *
     * @param activeMode 模式 dev
     * @return api
     */
    default ActiveMode devActiveModeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ActiveMode activeMode) {
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


    /**
     * 源端宿端模式 api转dev
     *
     * @param activeMode 模式 api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ActiveMode apiActiveModeToDev(ActiveMode activeMode) {
        if (activeMode == null) {
            return null;
        }
        switch (activeMode) {
            case Monitor:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ActiveMode.Monitor;
            case Operation:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ActiveMode.Operation;
            case Transparency:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ActiveMode.Transparency;
            default:
                throw getNoMatchEnumValueException(activeMode);
        }
    }


    /**
     * Tim模式 dev转api
     *
     * @param timMode tim模式 dev
     * @return api
     */
    default TimMode devTimModeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.TimMode timMode) {
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
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.TimMode apiTimModeToDev(TimMode timMode) {
        if (timMode == null) {
            return null;
        }
        switch (timMode) {
            case DAPI:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.TimMode.DAPI;
            case SAPI:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.TimMode.SAPI;
            case SAPInDAPI:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.TimMode.SAPInDAPI;
            case NoCheck:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.TimMode.NoCheck;
            default:
                throw getNoMatchEnumValueException(timMode);
        }
    }

    /**
     * lm工作类型 dev转api
     *
     * @param workingMode api
     * @return dev
     */
    default EthLmWorkingMode devEthLmWorkingModeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.EthLmWorkingMode workingMode) {
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
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.EthLmWorkingMode apiEthLmWorkingModeToDev(EthLmWorkingMode workingMode) {
        if (workingMode == null) {
            return null;
        }
        switch (workingMode) {
            case DualEnded:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.EthLmWorkingMode.DualEnded;
            case SingleEnded:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.EthLmWorkingMode.SingleEnded;
            default:
                throw getNoMatchEnumValueException(workingMode);
        }
    }

    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.InterfaceType apiInterfaceToDev(InterfaceType interfaceType) {
        if (interfaceType == null) {
            return null;
        }
        switch (interfaceType) {
            case UNI:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.InterfaceType.UNI;
            case NNI:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.InterfaceType.NNI;
            default:
                throw getNoMatchEnumValueException(interfaceType);

        }
    }

    /**
     * 业务时延测量类型 api转dev
     *
     * @param measureType dev
     * @return api
     */
    default MeasureType devServiceDelayMeasureTypeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.MeasureType measureType) {
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
     * 业务时延测量类型 api转dev
     *
     * @param measureType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.MeasureType apiServiceDelayMeasureTypeToDev(MeasureType measureType) {
        if (measureType == null) {
            return null;
        }
        switch (measureType) {
            case TCM:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.MeasureType.TCM;
            case PM:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.MeasureType.PM;
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
     * Accessaction 动作dev转api
     *
     * @param accessAction dev
     * @return api
     */
    default AccessAction devAccessactionToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.AccessAction accessAction) {
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
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.AccessAction apiAccessActionToDev(AccessAction accessAction) {
        if (accessAction == null) {
            return null;
        }
        switch (accessAction) {
            case Keep:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.AccessAction.Keep;
            case PushPop:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.AccessAction.PushPop;
            case Exchange:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.AccessAction.Exchange;
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
    default VlanType devVlanTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.VlanType vlanType) {
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
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.VlanType apiVlanTypeToDev(VlanType vlanType) {
        if (vlanType == null) {
            return null;
        }
        switch (vlanType) {
            case CTag:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.VlanType.CTag;
            case STag:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.VlanType.STag;
            case UnTag:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.VlanType.UnTag;
            default:
                throw getNoMatchEnumValueException(vlanType);
        }

    }

    default Set<EqType> devEqTypeToApiList(Collection<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.EqType> list) {
        return cts(list, this::devEqTypeToApi);
    }

    default EqType devEqTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.EqType eqType) {
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
            default:
                throw getNoMatchEnumValueException(eqType);
        }
    }

    default WorkingState devWorkingStateToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.WorkingState workingState) {
        if (workingState == null) {
            return null;
        }
        switch (workingState) {
            case Normal:
                return WorkingState.Normal;
            case Backup:
                return WorkingState.Backup;
            default:
                throw getNoMatchEnumValueException(workingState);
        }
    }

    default RemoteState devRemoteStateToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.RemoteState remoteState) {
        if (remoteState == null) {
            return null;
        }
        switch (remoteState) {
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
     *
     * @param lptMode dev
     * @return api
     */
    default LptMode devLptModeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LptMode lptMode) {
        if (lptMode == null) {
            return null;
        }
        switch (lptMode) {
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
     *
     * @param lptMode api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LptMode apiLptModeToDev(LptMode lptMode) {
        if (lptMode == null) {
            return null;
        }
        return switch (lptMode) {
            case TOL -> org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LptMode.TOL;
            case ICSD -> org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LptMode.ICSD;
            default -> throw getNoMatchEnumValueException(lptMode);
        };
    }


    default OperationalState devOperationalStateToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.OperationalState operationalState) {
        if (operationalState == null) {
            return null;
        }
        return switch (operationalState) {
            case Up -> OperationalState.Up;
            case Down -> OperationalState.Down;
            default -> throw getNoMatchEnumValueException(operationalState);
        };
    }

    /**
     * AdminState dev to api
     *
     * @param adminState dev
     * @return api
     */
    default AdminState devAdminStateToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.AdminState adminState) {
        if (adminState == null) {
            return null;
        }
        return switch (adminState) {
            case Enabled -> AdminState.Enabled;
            case Disabled -> AdminState.Disabled;
            default -> throw getNoMatchEnumValueException(adminState);
        };
    }

    /**
     * AdminState api to dev
     *
     * @param adminState api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.AdminState apiAdminStateToDev(AdminState adminState) {
        if (adminState == null) {
            return null;
        }
        return switch (adminState) {
            case Enabled -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.AdminState.Enabled;
            case Disabled -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.AdminState.Disabled;
            default -> throw getNoMatchEnumValueException(adminState);
        };
    }

    /**
     * EthPortMode dev to api
     *
     * @param ethPortMode dev
     * @return api
     */
    default EthPortMode devEthPortModeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.EthPortMode ethPortMode) {
        if (ethPortMode == null) {
            return null;
        }
        return switch (ethPortMode) {
            case Trunk -> EthPortMode.Trunk;
            case Access -> EthPortMode.Access;
            case Hybrid -> EthPortMode.Hybrid;
            case Transport -> EthPortMode.Transport;
            default -> throw getNoMatchEnumValueException(ethPortMode);
        };
    }

    /**
     * EthPortMode api to dev
     *
     * @param ethPortMode api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.EthPortMode apiEthPortModeToDev(EthPortMode ethPortMode) {
        if (ethPortMode == null) {
            return null;
        }
        return switch (ethPortMode) {
            case Trunk -> org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.EthPortMode.Trunk;
            case Access -> org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.EthPortMode.Access;
            case Hybrid -> org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.EthPortMode.Hybrid;
            case Transport -> org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.EthPortMode.Transport;
            default -> throw getNoMatchEnumValueException(ethPortMode);
        };
    }

    /**
     * oam config api转dev
     *
     * @param oamTransmitInterval api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.OamTransmitInterval apiOamTransmitIntervalToDev(OamTransmitInterval oamTransmitInterval) {
        if (oamTransmitInterval == null) {
            return null;
        }
        return switch (oamTransmitInterval) {
            case Interval1s ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.OamTransmitInterval.Interval1s;
            case Interval10s ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.OamTransmitInterval.Interval10s;
            case Interval1min ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.OamTransmitInterval.Interval1min;
            case Interval10ms ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.OamTransmitInterval.Interval10ms;
            case Interval3ms33 ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.OamTransmitInterval.Interval3ms33;
            case Interval100ms ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.OamTransmitInterval.Interval100ms;
            default -> throw getNoMatchEnumValueException(oamTransmitInterval);
        };
    }

    /**
     * InsertType dev to api
     *
     * @param insertType dev
     * @return api
     */
    default InsertType devInsertTypeToApi(
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.InsertType insertType) {
        if (insertType == null) {
            return null;
        }
        return switch (insertType) {
            case Null -> InsertType.Null;
            case PN11 -> InsertType.PN11;
            case MSAIS -> InsertType.MSAIS;
            case DelayInsert -> InsertType.DelayInsert;
            case DirectInsert -> InsertType.DirectInsert;
            default -> throw getNoMatchEnumValueException(insertType);
        };
    }

    /**
     * InsertType api to dev
     *
     * @param insertType api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.InsertType apiInsertTypeToDev(
            InsertType insertType) {
        if (insertType == null) {
            return null;
        }
        return switch (insertType) {
            case Null -> org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.InsertType.Null;
            case PN11 -> org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.InsertType.PN11;
            case MSAIS -> org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.InsertType.MSAIS;
            case DelayInsert ->
                    org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.InsertType.DelayInsert;
            case DirectInsert ->
                    org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.InsertType.DirectInsert;
            default -> throw getNoMatchEnumValueException(insertType);
        };
    }

    /**
     * 业务类型 dev转api
     *
     * @param serviceType dev业务类型
     * @return api业务类型
     */
    default ServiceType devServiceTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ServiceType serviceType) {
        if (serviceType == null) {
            return null;
        }
        return switch (serviceType) {
            case SDH -> ServiceType.SDH;
            case EPL -> ServiceType.EPL;
            case EVPL -> ServiceType.EVPL;
            case ODU -> ServiceType.ODU;
            case ELAN -> ServiceType.ELAN;
            case ETREE -> ServiceType.ETREE;
            case EEPL -> ServiceType.EEPL;
            case EEVPL -> ServiceType.EEVPL;
            default -> throw getNoMatchEnumValueException(serviceType);
        };
    }

    /**
     * 业务类型api转dev
     *
     * @param serviceType 业务类型 api
     * @return 业务类型 dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ServiceType apiServiceTypeToDev(ServiceType serviceType) {
        if (serviceType == null) {
            return null;
        }
        return switch (serviceType) {
            case SDH -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ServiceType.SDH;
            case EPL -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ServiceType.EPL;
            case EVPL -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ServiceType.EVPL;
            case ODU -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ServiceType.ODU;
            case ELAN -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ServiceType.ELAN;
            case ETREE -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ServiceType.ETREE;
            case EEPL -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ServiceType.EEPL;
            case EEVPL -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ServiceType.EEVPL;
            default -> throw getNoMatchEnumValueException(serviceType);
        };
    }


    /**
     * 连接方向 api转dev
     *
     * @param connectionDirection 连接方向 api
     * @return 连接方向 dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.ConnectionDirection apiConnectionDirectionToDev(ConnectionDirection connectionDirection) {
        if (connectionDirection == null) {
            return null;
        }
        return switch (connectionDirection) {
            case Up -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.ConnectionDirection.Up;
            case Down -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.ConnectionDirection.Down;
            default -> throw getNoMatchEnumValueException(connectionDirection);
        };
    }

    /**
     * 保护类型 api转换为dev
     *
     * @param protectionType 保护类型 api
     * @return 保护类型 dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.ProtectionType apiProtectionTypeToDev(ProtectionType protectionType) {
        if (protectionType == null) {
            return null;
        }
        return switch (protectionType) {
            case LagLoadBlancing ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.ProtectionType.LagLoadBlancing;
            case VcSncp ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.ProtectionType.VcSncp;
            case Lag1To1 ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.ProtectionType.Lag1To1;
            case OduSncpI ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.ProtectionType.OduSncpI;
            case OduSncpN ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.ProtectionType.OduSncpN;
            case OduSncpS ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.ProtectionType.OduSncpS;
            case Msp1Plus1 ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.ProtectionType.Msp1Plus1;
            case Och1Plus1 ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.ProtectionType.Och1Plus1;
            case NoProtection ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.ProtectionType.NoProtection;
            default -> throw getNoMatchEnumValueException(protectionType);
        };
    }

    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.TpidDifinition apiTpidDifinitionToDev(TpidDifinition tpidDifinition) {
        if (tpidDifinition == null) {
            return null;
        }
        return switch (tpidDifinition) {
            case _0x88a8 -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.TpidDifinition._0x88a8;
            case _0x8100 -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.TpidDifinition._0x8100;
            case _0x9100 -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.TpidDifinition._0x9100;
            case _0x9200 -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.TpidDifinition._0x9200;
            default -> throw getNoMatchEnumValueException(tpidDifinition);
        };
    }

    /**
     * TpidDifinition  dev转api
     *
     * @param tpidDifinition type dev
     * @return type api
     */
    default TpidDifinition devTpidDifinitionToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.TpidDifinition tpidDifinition) {
        if (tpidDifinition == null) {
            return null;
        }
        return switch (tpidDifinition) {
            case _0x88a8 -> TpidDifinition._0x88a8;
            case _0x8100 -> TpidDifinition._0x8100;
            case _0x9100 -> TpidDifinition._0x9100;
            case _0x9200 -> TpidDifinition._0x9200;
            default -> throw getNoMatchEnumValueException(tpidDifinition);
        };
    }

    /**
     * WorkingMode api to dev
     *
     * @param workingMode api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.WorkingMode apiWorkingModeToDev(
            WorkingMode workingMode) {
        if (workingMode == null) {
            return null;
        }
        return switch (workingMode) {
            case Auto -> org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.WorkingMode.Auto;
            case _10GEFullDuplex ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.WorkingMode._10GEFullDuplex;
            case _100MFullDuplex ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.WorkingMode._100MFullDuplex;
            case _1000MFullDuplex ->
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.WorkingMode._1000MFullDuplex;
            default -> throw getNoMatchEnumValueException(workingMode);
        };

    }

    /**
     * 倒换操作类型 api转dev
     *
     * @param switchType type api
     * @return type dev
     */
    default SwitchReason apiSwitchReasonToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.SwitchReason switchType) {
        if (switchType == null) {
            return null;
        }
        return switch (switchType) {
            case Lockout -> SwitchReason.Lockout;
            case NoSwitch -> SwitchReason.NoSwitch;
            case ForceSwitch -> SwitchReason.ForceSwitch;
            case ManualSwitch -> SwitchReason.ManualSwitch;
            case WaitForRestore -> SwitchReason.WaitForRestore;
            case SfSwitch -> SwitchReason.SfSwitch;
            case SdSwitch -> SwitchReason.SdSwitch;
            case Cleared -> SwitchReason.Cleared;
            default -> throw getNoMatchEnumValueException(switchType);
        };
    }

    /**
     * 倒换操作类型 api转dev
     *
     * @param switchType type api
     * @return type dev
     */
    default SwitchReason apiSwitchReasonToDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.SwitchReason switchType) {
        if (switchType == null) {
            return null;
        }
        return switch (switchType) {
            case Lockout -> SwitchReason.Lockout;
            case NoSwitch -> SwitchReason.NoSwitch;
            case ForceSwitch -> SwitchReason.ForceSwitch;
            case ManualSwitch -> SwitchReason.ManualSwitch;
            case WaitForRestore -> SwitchReason.WaitForRestore;
            case SfSwitch -> SwitchReason.SfSwitch;
            case SdSwitch -> SwitchReason.SdSwitch;
            case Cleared -> SwitchReason.Cleared;
            default -> throw getNoMatchEnumValueException(switchType);
        };
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
        return switch (ql) {
            case QLDNU -> QL.QLDNU;
            case QLPRC -> QL.QLPRC;
            case QLSEC -> QL.QLSEC;
            case QLSSUA -> QL.QLSSUA;
            case QLSSUB -> QL.QLSSUB;
            case QLEEC2 -> QL.QLEEC2;
            default -> throw getNoMatchEnumValueException(ql);
        };
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
        return switch (ql) {
            case QLDNU -> org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLDNU;
            case QLPRC -> org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLPRC;
            case QLSEC -> org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLSEC;
            case QLSSUA -> org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLSSUA;
            case QLSSUB -> org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLSSUB;
            case QLEEC2 -> org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.QL.QLEEC2;
            default -> throw getNoMatchEnumValueException(ql);
        };
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
        return switch (ssmMode) {
            case Auto -> SsmMode.Auto;
            case Force -> SsmMode.Force;
            default -> throw getNoMatchEnumValueException(ssmMode);
        };
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
        return switch (ssmMode) {
            case Auto -> org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.SsmMode.Auto;
            case Force -> org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.SsmMode.Force;
            default -> throw getNoMatchEnumValueException(ssmMode);
        };
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
        return switch (pllState) {
            case Lock -> PllState.Lock;
            case Accquire -> PllState.Accquire;
            case Holdover -> PllState.Holdover;
            case FreeRunning -> PllState.FreeRunning;
            default -> throw getNoMatchEnumValueException(pllState);
        };
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
        return switch (clk2mType) {
            case _2MHz -> Clk2mType._2MHz;
            case _2Mbits -> Clk2mType._2Mbits;
            default -> throw getNoMatchEnumValueException(clk2mType);
        };
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
        return switch (clk2mType) {
            case _2MHz -> org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.Clk2mType._2MHz;
            case _2Mbits -> org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.clock.rev210927.Clk2mType._2Mbits;
            default -> throw getNoMatchEnumValueException(clk2mType);
        };
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
        return switch (meStatus) {
            case Running -> MeStatus.Running;
            case Installing -> MeStatus.Installing;
            case Maintenance -> MeStatus.Maintenance;
            default -> throw getNoMatchEnumValueException(meStatus);
        };
    }

    /**
     * 网元状态 dev转api
     *
     * @param meStatus 网元状态 dev
     * @return 网元状态 api
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.MeStatus devMeStatusToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.MeStatus meStatus) {
        if (meStatus == null) {
            return null;
        }
        return switch (meStatus) {
            case Installing -> org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.MeStatus.Installing;
            case Running -> org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.MeStatus.Running;
            case Maintenance ->
                    org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.MeStatus.Maintenance;
            default -> throw getNoMatchEnumValueException(meStatus);
        };
    }

    /**
     * NTP 服务器状态 dev转api
     *
     * @param ntpState NTP 服务器状态
     * @return NTP 服务器状态
     */
    default NtpState devNtpStateToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.NtpState ntpState) {
        if (ntpState == null) {
            return null;
        }
        switch (ntpState) {
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

    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.ModifyAction apiModifyActionToDev(ModifyAction modifyAction) {
        if (modifyAction == null) {
            return null;
        }
        switch (modifyAction) {
            case AddAction:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.ModifyAction.AddAction;
            case AbortAction:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.ModifyAction.AbortAction;
            case DeleteAction:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.ModifyAction.DeleteAction;
            default:
                throw getNoMatchEnumValueException(modifyAction);
        }
    }

    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.OduPosition apiOduPositionToDev(OduPosition oduPosition) {
        if (oduPosition == null) {
            return null;
        }
        switch (oduPosition) {
            case LineOdu:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.OduPosition.LineOdu;
            case ClientOdu:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.OduPosition.ClientOdu;
            default:
                throw getNoMatchEnumValueException(oduPosition);
        }
    }

    /**
     * SetTypeResult dev 转 api
     *
     * @param setTypeResult dev
     * @return SetTypeResult api
     */
    default SetTypeResult devSetTypeResultToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.SetTypeResult setTypeResult) {
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
     *
     * @param perceivedSeverity dev
     * @return api
     */
    default PerceivedSeverity devPerceivedSeverityToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.PerceivedSeverity perceivedSeverity) {
        if (perceivedSeverity == null) {
            return null;
        }
        switch (perceivedSeverity) {
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
     *
     * @param perceivedSeverity api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.PerceivedSeverity apiPerceivedSeverityToDev(
            PerceivedSeverity perceivedSeverity) {
        if (perceivedSeverity == null) {
            return null;
        }
        switch (perceivedSeverity) {
            case Major:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.PerceivedSeverity.Major;
            case Minor:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.PerceivedSeverity.Minor;
            case Warning:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.PerceivedSeverity.Warning;
            case Critical:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.PerceivedSeverity.Critical;
            default:
                throw getNoMatchEnumValueException(perceivedSeverity);
        }
    }

    /**
     * 告警状态 dev to api
     *
     * @param alarmState dev
     * @return api
     */
    default AlarmState devAlarmStateToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.AlarmState alarmState) {
        if (alarmState == null) {
            return null;
        }
        switch (alarmState) {
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
     *
     * @param alarmState api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.AlarmState apiAlarmStateToDev(AlarmState alarmState) {
        if (alarmState == null) {
            return null;
        }
        switch (alarmState) {
            case End:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.AlarmState.End;
            case Start:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev190213.AlarmState.Start;
            default:
                throw getNoMatchEnumValueException(alarmState);
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
            case _1min:
                return Granularity._1min;
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

    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LearningMode apiLearningModeToDev(LearningMode learningMode) {
        if (learningMode == null) {
            return null;
        }
        switch (learningMode) {
            case IVL:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LearningMode.IVL;
            case SVL:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LearningMode.SVL;
            default:
                throw getNoMatchEnumValueException(learningMode);
        }
    }

    default LearningMode devLearningModeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LearningMode learningMode) {
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

    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.UnknownFrameProcess apiUnknownFrameProcessToDev(UnknownFrameProcess frameProcess) {
        if (frameProcess == null) {
            return null;
        }
        switch (frameProcess) {
            case Discard:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.UnknownFrameProcess.Discard;
            case Transmit:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.UnknownFrameProcess.Transmit;
            default:
                throw getNoMatchEnumValueException(frameProcess);
        }
    }

    default UnknownFrameProcess devUnknownFrameProcessToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.UnknownFrameProcess frameProcess) {
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


    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.FileType
    apiFileTypeToDev(FileType fileType) {
        if (fileType == null) {
            return null;
        }
        switch (fileType) {
            case Data:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.FileType.Data;
            case Upgrade:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.FileType.Upgrade;
            default:
                throw getNoMatchEnumValueException(fileType);
        }
    }

    default FileType devFileTypeToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.FileType fileType) {
        if (fileType == null) {
            return null;
        }
        switch (fileType) {
            case Data:
                return FileType.Data;
            case Upgrade:
                return FileType.Upgrade;
            default:
                throw getNoMatchEnumValueException(fileType);
        }
    }

    default DownloadStatus devDownloadStatusToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.DownloadStatus downloadStatus) {
        if (downloadStatus == null) {
            return null;
        }
        switch (downloadStatus) {
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

    default FailReason devFailReasonToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.FailReason failReason) {
        if (failReason == null) {
            return null;
        }
        switch (failReason) {
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

    default ActiveStatus devActiveStatusToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.ActiveStatus activeStatus) {
        if (activeStatus == null) {
            return null;
        }
        switch (activeStatus) {
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
     * switchType api to dev
     *
     * @param switchDirection api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.SwitchDirection apiSwitchDirectionToDev(SwitchDirection switchDirection) {
        if (switchDirection == null) {
            return null;
        }
        switch (switchDirection) {
            case UniSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.SwitchDirection.UniSwitch;
            case BiSwitch:
                return org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.SwitchDirection.BiSwitch;
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
     * 保护类型 dev转换为api
     *
     * @param protectionType 保护类型 dev
     * @return 保护类型 api
     */
    default ProtectionType devProtectionTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.ProtectionType protectionType) {
        if (protectionType == null) {
            return null;
        }
        switch (protectionType) {
            case VcSncp:
                return ProtectionType.VcSncp;
            case Lag1To1:
                return ProtectionType.Lag1To1;
            case OduSncpI:
                return ProtectionType.OduSncpI;
            case OduSncpN:
                return ProtectionType.OduSncpN;
            case OduSncpS:
                return ProtectionType.OduSncpS;
            case Msp1Plus1:
                return ProtectionType.Msp1Plus1;
            case Och1Plus1:
                return ProtectionType.Och1Plus1;
            case NoProtection:
                return ProtectionType.NoProtection;
            case LagLoadBlancing:
                return ProtectionType.LagLoadBlancing;

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
    default ProtectionTypeEqPg devProtectionTypeToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.ProtectionTypeEqPg protectionType) {
        if (protectionType == null) {
            return null;
        }
        switch (protectionType) {
            case Power1Plus1:
                return ProtectionTypeEqPg.Power1Plus1;
            case Control1Plus1:
                return ProtectionTypeEqPg.Control1Plus1;
            case CrossConnect1Plus1:
                return ProtectionTypeEqPg.CrossConnect1Plus1;
            case ControlCrossConnect1Plus1:
                return ProtectionTypeEqPg.ControlCrossConnect1Plus1;

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
     * switchType dev to api
     *
     * @param switchDirection dev
     * @return api
     */
    default SwitchDirection devSwitchDirectionToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.SwitchDirection switchDirection) {
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
     * ProtectionDirection dev to api
     *
     * @param direction dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.protection.group.rev210927.ProtectionDirection devProtectionDirectionServicePgToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev200708.ProtectionDirection direction) {
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
    default org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ProtectionDirection devProtectionDirectionToApi(ProtectionDirection direction) {
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
     * LoadAlgorithm dev to api
     *
     * @param loadAlgorithm dev
     * @return api
     */
    default LoadAlgorithm devLoadAlgorithmToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LoadAlgorithm loadAlgorithm) {
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
     *
     * @param mode dev
     * @return api
     */
    default AggregationMode devAggregationModeToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.AggregationMode mode) {
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
     * LoadAlgorithm api to dev
     *
     * @param loadAlgorithm api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LoadAlgorithm apiLoadAlgorithmToDev(LoadAlgorithm loadAlgorithm) {
        if (loadAlgorithm == null) {
            return null;
        }
        switch (loadAlgorithm) {
            case DIP:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LoadAlgorithm.DIP;
            case SIP:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LoadAlgorithm.SIP;
            case DMAC:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LoadAlgorithm.DMAC;
            case SDIP:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LoadAlgorithm.SDIP;
            case SMAC:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LoadAlgorithm.SMAC;
            case SDMAC:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.LoadAlgorithm.SDMAC;
            default:
                throw getNoMatchEnumValueException(loadAlgorithm);
        }
    }

    /**
     * AggregationMode api to dev
     *
     * @param mode api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.AggregationMode apiAggregationModeToDev(AggregationMode mode) {
        if (mode == null) {
            return null;
        }
        switch (mode) {
            case Lacp:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.AggregationMode.Lacp;
            case Manual:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.AggregationMode.Manual;
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
    default PortMemberRole devPortMemberRoleToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.PortMemberRole role) {
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
     * PortMemberRole api to dev
     *
     * @param role api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.PortMemberRole apiPortMemberRoleToDev(PortMemberRole role) {
        if (role == null) {
            return null;
        }
        switch (role) {
            case Slave:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.PortMemberRole.Slave;
            case Master:
                return org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.PortMemberRole.Master;
            default:
                throw getNoMatchEnumValueException(role);
        }
    }
}
