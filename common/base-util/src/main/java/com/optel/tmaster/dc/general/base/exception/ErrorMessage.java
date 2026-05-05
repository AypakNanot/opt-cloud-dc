/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.exception;

/**
 * <pre>
 *    o o o o o o     p p p p p p   t t t t t t t    e e e e e e    l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p       p        t t         e e            l l
 *    o o     o o     p p p p p p        t t         e e e e e e    l l
 *    o o     o o     p p                t t         e e            l l
 *    o o     o o     p p                t t         e e            l l
 *    o o     o o     p p                t t         e e            l l
 *    o o o o o o     p p                t t         e e e e e e    l l l l l l
 *
 *
 *          l       i    h     h    u     u       a a
 *          l            h     h    u     u     a     a
 *          l       i    h h h h    u     u     a     a
 *          l       i    h     h    u     u     a     a
 *          l l l   i    h     h      u u  u      a a   a
 *
 *              LiHua       佛主保佑       永无bBUG
 * </pre>
 * 所有错误提示信息类
 * 2019/9/24 16:01:57
 *
 * @author LiH
 * @version V1.0.0
 */
public enum ErrorMessage {

    /**
     * 带宽不足
     */
    BANDWIDTH_INSUFFICIENT("Bandwidth insufficient", "带宽不足"),
    VLAN_CONFLICT("VLAN conflict", "VLAN冲突"),
    NE_NONEXIST( "NE non-exist", "网元信息下发错误，未发现网元"),
    SPECIFIED_PORT_OCCUPIED("Specified port occupied", "指定端口下有业务，不支持独占"),
    DEVICE_OFFLINE("Device offline","网元脱管"),
    CIR_BIGGER_THAN_PIR("CIR value bigger than PIR value.","CIR大于PIR"),
    RESIDUAL_CONF_ONTUNNEL("Residual configuration on the tunnel","Tunnel上有残留配置"),
    OAM_ERROR("OAM error","OAM配置出错"),
    TUNNEL_UNAVAILABLE("Tunnel unavailable","没有可用的隧道"),
    VCID_OCCUPIED("VCID occupied","伪线VCID已被占用"),
    MEG_ERROR("MEG error","MEG配置不正确"),
    TUNNEL_BANDWIDTH_AUTO_ADJUSTED("Tunnel bandwidth is automatically adjusted","自动调整带宽的隧道不能进行带宽调整操作"),
    SERVICES_EXIST_ONTUNNEL("Services exist on the tunnel","隧道上承载业务不能被删除"),
    IP_ADDRESS_CONFLICT("IP address conflict", "IP地址冲突");


    public String errorMsg;
    public String errorDesc;

    ErrorMessage(String errorMsg, String errorDesc) {
        this.errorMsg = errorMsg;
        this.errorDesc = errorDesc;
    }

}
