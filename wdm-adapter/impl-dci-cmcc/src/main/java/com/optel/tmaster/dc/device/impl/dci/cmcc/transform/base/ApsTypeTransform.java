/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base;

import com.optel.tmaster.dc.general.base.exception.manage.NoMatchEnumValueException;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optical.transport.line.protection.rev220208.PortType;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.rpc.rev220208.SwitchOlpInputGrouping;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.APSPATHS;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.PRIMARY;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.SECONDARY;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.SwitchOlpInput;

/**
 * @author Quan Jingyuan
 * @since 2022/3/25
 **/
public interface ApsTypeTransform {

    /**
     * ActivePath dev to api
     *
     * @param clazz dev
     * @return api
     */
    default PortType devToActivePathApi(APSPATHS clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends APSPATHS> lnc = clazz.implementedInterface();
        if (PRIMARY.class.equals(lnc)) {
            return PortType.PRIMARY;
        } else if (SECONDARY.class.equals(lnc)) {
            return PortType.SECONDARY;
        } else {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(clazz);
        }
    }

    /**
     * forceToPort dev to api
     *
     * @param forceToPort dev
     * @return api
     */
    default PortType devToForceToPortApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.ApsConfig.ForceToPort forceToPort) {
        if (forceToPort == null) {
            return null;
        }
        switch (forceToPort) {
            case NONE:
                return PortType.NONE;
            case PRIMARY:
                return PortType.PRIMARY;
            case SECONDARY:
                return PortType.SECONDARY;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(forceToPort);
        }
    }

    /**
     * forceToPort api to dev
     *
     * @param forceToPort api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.ApsConfig.ForceToPort apiToForceToPortDev(PortType forceToPort) {
        if (forceToPort == null) {
            return null;
        }
        switch (forceToPort) {
            case NONE:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.ApsConfig.ForceToPort.NONE;
            case PRIMARY:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.ApsConfig.ForceToPort.PRIMARY;
            case SECONDARY:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.ApsConfig.ForceToPort.SECONDARY;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(forceToPort);
        }
    }
    /**
     * switchToPort api to dev
     *
     * @param switchToPort api
     * @return dev
     */
    default SwitchOlpInput.SwitchToPort apiToSwitchPortDev(SwitchOlpInputGrouping.SwitchToPort switchToPort) {
        if (switchToPort == null) {
            return null;
        }
        switch (switchToPort) {
            case SECONDARY:
                return SwitchOlpInput.SwitchToPort.SECONDARY;
            case PRIMARY:
                return SwitchOlpInput.SwitchToPort.PRIMARY;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(switchToPort);
        }
    }
    /**
    * Status转换
     * @param status dev
     * @return api
    * */
    //极简OTN没有ApsStatus
//    default ApsState.ApsStatus devToApsStatusApi(ApsSta status){
//        if(status==null){
//            return null;
//        }
//        switch (status){
//            case UNKNOWN:
//                return ApsState.ApsStatus.UNKNOWN;
//            case NR:
//                return ApsState.ApsStatus.NR;
//            case DNR:
//                return ApsState.ApsStatus.DNR;
//            case FSP:
//                return ApsState.ApsStatus.FSP;
//            case FSW:
//                return ApsState.ApsStatus.FSW;
//            case LOP:
//                return ApsState.ApsStatus.LOP;
//            case MSP:
//                return ApsState.ApsStatus.MSP;
//            case MSW:
//                return ApsState.ApsStatus.MSW;
//            case SDP:
//                return ApsState.ApsStatus.SDP;
//            case SDW:
//                return ApsState.ApsStatus.SDW;
//            case SFP:
//                return ApsState.ApsStatus.SFP;
//            case SFW:
//                return ApsState.ApsStatus.SFW;
//            case WTR:
//                return ApsState.ApsStatus.WTR;
//                default:
//                    throw NoMatchEnumValueException.getNoMatchEnumValueException(status);
//
//        }
//    }
}
