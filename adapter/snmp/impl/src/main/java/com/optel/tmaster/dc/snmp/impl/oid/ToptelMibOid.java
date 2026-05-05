/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.snmp.impl.oid;

/**
 * ToptelMibOid
 * 拓普泰尔MIB
 * date       time        author
 * ─────────────────────────────
 * 2021/11/13   15:06      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public enum ToptelMibOid {
    /**
     * 设备信息
     */
    deviceInfo("1.3.6.1.4.1.25719.3.1"),
    /**
     * 注册类型 1-注册
     */
    trapType("1.3.6.1.4.1.25719.3.1.1.0"),
    /**
     * 设备类型
     * RG2000/-M/-H：0x2000 (8192)
     * RG2000-S：0x2001 (8193)
     * RG2000-E：0x2002 (8194)x
     * RG2000-V8：0x2008 (8200)
     * RG2000-NR：0x2020 (8224)
     * EC2000：0x2030 (8240)
     */
    deviceType("1.3.6.1.4.1.25719.3.1.2.0"),
    deviceID("1.3.6.1.4.1.25719.3.1.3.0"),
    trapVersion("1.3.6.1.4.1.25719.3.1.20.1.0"),
    trapDeviceIp("1.3.6.1.4.1.25719.3.1.20.2.0"),
    trapRegisterResult("1.3.6.1.4.1.25719.3.1.20.5.0"),
    trapDeviceWebAgentIpAddr("1.3.6.1.4.1.25719.3.1.20.6.0");

    public static String register = "1";

    private String oid;
    ToptelMibOid(String oid){
        this.oid = oid;
    }

    public String getOid() {
        return oid;
    }

}
