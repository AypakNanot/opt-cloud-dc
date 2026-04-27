/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.transform.base;

import com.optel.tmaster.dc.dci.impl.base.transform.ITransform;
import com.optel.tmaster.dc.general.base.exception.manage.NoMatchEnumValueException;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.EthernetInterfaceConfig;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.OtnConfig;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.rev220208.SdhConfig;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.terminal.device.rev220208.TerminalEthernetProtocolConfig;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ethernet.rev200630.*;

/**
 * InterfaceName: EthTypeTransform
 * <ul>
 * <li>以太网相关字段转换类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2022/3/9 19:03
 */
public interface EthTypeTransform extends ITransform {
    /**
     * 工作模式 dev to api
     * @param duplexMode dev
     * @return api
     */
    default EthernetInterfaceConfig.DuplexMode devDuplexModeToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ethernet.rev200630.EthernetInterfaceConfig.DuplexMode duplexMode){
        if(duplexMode == null){
            return null;
        }
        switch (duplexMode){
            case FULL:
                return EthernetInterfaceConfig.DuplexMode.FULL;
            case HALF:
                return EthernetInterfaceConfig.DuplexMode.HALF;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(duplexMode);
        }
    }
    /**
     * 以太网端口速率dev to api
     *
     * @param clazz dev速率
     * @return api速率
     */
    default org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.ETHERNETSPEED devEthernetSpeedToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ethernet.rev200630.ETHERNETSPEED clazz) {
        if (clazz == null) {
            return null;
        }
        Class<? extends org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.ethernet.rev200630.ETHERNETSPEED> lnc = clazz.implementedInterface();
        if(lnc.equals(SPEED10MB.class)){
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEED10MB.VALUE;
        }else if(lnc.equals(SPEED100MB.class)){
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEED100MB.VALUE;
        }else if (lnc.equals(SPEED1GB.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEED1GB.VALUE;
        }  else if (lnc.equals(SPEED2500MB.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEED2500MB.VALUE;
        }  else if (lnc.equals(SPEED5GB.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEED5GB.VALUE;
        }   else if (lnc.equals(SPEED10GB.class)) {
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
        }  else if (lnc.equals(SPEED600GB.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEED600GB.VALUE;
        }  else if (lnc.equals(SPEED800GB.class)) {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEED800GB.VALUE;
        }  else {
            return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.SPEEDUNKNOWN.VALUE;
        }
    }

    /**
     * ClientAls转换
     * @param clientAls dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.TerminalEthernetProtocolConfig.ClientAls devClientAlsToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.TerminalEthernetProtocolConfig.ClientAls clientAls){
        if(clientAls == null){
            return null;
        }
        switch (clientAls){
            case ETHERNET:
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.TerminalEthernetProtocolConfig.ClientAls.ETHERNET;
            case LASERSHUTDOWN:
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.TerminalEthernetProtocolConfig.ClientAls.LASERSHUTDOWN;
            case NONE:
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.TerminalEthernetProtocolConfig.ClientAls.NONE;
            default :
                throw NoMatchEnumValueException.getNoMatchEnumValueException(clientAls);
        }
    }

    /**
     * ClientFec转换
     * @param clientFec dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.TerminalEthernetProtocolConfig.ClientFec devClientFecToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.TerminalEthernetProtocolConfig.ClientFec clientFec){
        if(clientFec == null){
            return null;
        }
        switch (clientFec){
            case ENABLED:
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.TerminalEthernetProtocolConfig.ClientFec.ENABLED;
            case DISABLED:
                return org.opendaylight.yang.gen.v1.com.optel.dci.yang.interfaces.ethernet.rev220208.TerminalEthernetProtocolConfig.ClientFec.DISABLED;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(clientFec);
        }
    }


    /**
     * ClientAls转换
     * @param clientAls api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.TerminalEthernetProtocolConfig.ClientAls  apiClientAlsToDev(TerminalEthernetProtocolConfig.ClientAls clientAls){
        if(clientAls == null){
            return null;
        }
        switch (clientAls){
            case ETHERNET:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.TerminalEthernetProtocolConfig.ClientAls.ETHERNET;
            case LASERSHUTDOWN:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.TerminalEthernetProtocolConfig.ClientAls.LASERSHUTDOWN;
            case NONE:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.TerminalEthernetProtocolConfig.ClientAls.NONE;
            default :
                throw NoMatchEnumValueException.getNoMatchEnumValueException(clientAls);
        }
    }

    /**
     * ClientFec转换
     * @param clientFec api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.TerminalEthernetProtocolConfig.ClientFec apiClientFecToDev(TerminalEthernetProtocolConfig.ClientFec clientFec){
        if(clientFec == null){
            return null;
        }
        switch (clientFec){
            case ENABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.TerminalEthernetProtocolConfig.ClientFec.ENABLED;
            case DISABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.terminal.device.rev200630.TerminalEthernetProtocolConfig.ClientFec.DISABLED;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(clientFec);
        }
    }
    /**
    * otn 参数 ClientAls转换
     * @param clientAls als
     * @return ClientAls
    * */
    default OtnConfig.ClientAls devOtnClientAlsToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.OtnConfig.ClientAls clientAls){
        if(clientAls == null){
            return null;
        }
        switch (clientAls){
            case ENABLED:
                return OtnConfig.ClientAls.ENABLED;
            case DISABLED:
                return OtnConfig.ClientAls.DISABLED;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(clientAls);
        }
    }
    /**
     * sdh 参数 ClientAls转换
     * @param clientAls als
     * @return ClientAls
     * */
    default SdhConfig.ClientAls devSdhClientAlsToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.SdhConfig.ClientAls clientAls){
        if(clientAls == null){
            return null;
        }
        switch (clientAls){
            case ENABLED:
                return SdhConfig.ClientAls.ENABLED;
            case DISABLED:
                return SdhConfig.ClientAls.DISABLED;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(clientAls);
        }
    }
    /**
     * otn 参数 ClientAls转换
     * @param clientAls als
     * @return ClientAls
     * */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.OtnConfig.ClientAls apiOtnClientAlsToDev( OtnConfig.ClientAls  clientAls){
        if(clientAls == null){
            return null;
        }
        switch (clientAls){
            case ENABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.OtnConfig.ClientAls.ENABLED;
            case DISABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.OtnConfig.ClientAls.DISABLED;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(clientAls);
        }
    }
    /**
     * sddh 参数 ClientAls转换
     * @param clientAls als
     * @return ClientAls
     * */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.SdhConfig.ClientAls apiSdhClientAlsToDev( SdhConfig.ClientAls  clientAls){
        if(clientAls == null){
            return null;
        }
        switch (clientAls){
            case ENABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.SdhConfig.ClientAls.ENABLED;
            case DISABLED:
                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.interfaces.rev191119.SdhConfig.ClientAls.DISABLED;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(clientAls);
        }
    }

}
