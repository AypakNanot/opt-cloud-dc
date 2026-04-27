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
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.rpc.rev220208.SetNeighbourIpInputGrouping;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.DomainName;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.Host;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.IpAddress;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.yang.rev220208.MacAddress;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.Ipv4Address;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.PortNumber;

import static org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.SetNeighborIpInput.NeighborIpType.*;


/**
 * ClassName: GeneralUtilTransform
 * <ul>
 * <li>公共工具 转换类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2022/3/11 17:28
 */
public interface GeneralUtilTransform extends ITransform {

    /**
     * PortNumber 转换
     *
     * @param portNumber dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.PortNumber devPortNumberToApi(
            PortNumber portNumber) {
        if (portNumber == null) {
            return null;
        }
        return new org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.PortNumber(portNumber.getValue());
    }


    /**
     * PortNumber 转换
     *
     * @param portNumber api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.PortNumber apiPortNumberToDev(org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.PortNumber portNumber) {
        if (portNumber == null) {
            return null;
        }
        return new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.PortNumber(portNumber.getValue());
    }

    /**
     * 以太网 mac地址 dev to api
     *
     * @param macAddress dev mac
     * @return api mac
     */
    default org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.yang.rev220208.MacAddress devMacAddressToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.yang.rev181121.MacAddress macAddress) {
        if (macAddress == null) {
            return null;
        }
        return new MacAddress(macAddress.getValue());
    }


    /**
     * Ipv4转换
     *
     * @param ipv4Address dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.Ipv4Address devIpv4AddressToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.Ipv4Address ipv4Address) {
        if (ipv4Address == null) {
            return null;
        }
        return new org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.Ipv4Address(ipv4Address.getValue());
    }


    /**
     * Ipv4转换
     *
     * @param ipv4Address api
     * @return dev
     */
    default Ipv4Address apiIpv4AddressToDev(org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.Ipv4Address ipv4Address) {
        if (ipv4Address == null) {
            return null;
        }
        return new Ipv4Address(ipv4Address.getValue());
    }

    /**
     * host 转换
     *
     * @param host dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.Host devHostToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.Host host) {
        if (host == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.IpAddress ipAddress = host.getIpAddress();
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.DomainName domainName = host.getDomainName();
        org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.IpAddress address;
        if (ipAddress != null) {
            address = new org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.IpAddress(devIpv4AddressToApi(ipAddress.getIpv4Address()));
            return new org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.Host(address);
        }
        org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.DomainName domainName1;
        if (domainName != null) {
            domainName1 = new org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.DomainName(domainName.getValue());
            return new org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.inet.rev220208.Host(domainName1);
        }
        throw new IllegalStateException("No value assinged");
    }

    /**
     * host 转换
     *
     * @param host dev
     * @return api
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.Host apiHostToDev(Host host) {
        if (host == null) {
            return null;
        }

        IpAddress ipAddress = host.getIpAddress();
        DomainName domainName = host.getDomainName();
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.IpAddress address;
        if (ipAddress != null) {
            address = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.IpAddress(apiIpv4AddressToDev(ipAddress.getIpv4Address()));
            return new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.Host(address);
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.DomainName domainName1;
        if (domainName != null) {
            domainName1 = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.DomainName(domainName.getValue());
            return new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.Host(domainName1);
        }
        throw new IllegalStateException("No value assinged");
    }

    /**
     * NeighbourIpType 转换
     * @param neighbourIpType api
     * @return dev rpc
     */
    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.SetNeighborIpInput.NeighborIpType apiSetNeighbourIpInputToDev(SetNeighbourIpInputGrouping.NeighborIpType neighbourIpType) {
        if (neighbourIpType == null) {
            return null;
        }
        switch (neighbourIpType) {
            case NM:
                return NM;
            case OSC:
                return OSC;
            case LOOPBACK:
                return LOOPBACK;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(neighbourIpType);
        }
    }


}
