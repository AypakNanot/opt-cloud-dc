/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.snmp.impl;

import com.optel.tmaster.dc.snmp.impl.oid.ToptelMibOid;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.test.MultiThreadedTrapReceiver;

import java.util.HashMap;
import java.util.Map;
import java.util.Vector;

/**
 * SnmpTrap
 * 接收 SNMP TRAP 信息
 * date       time        author
 * ─────────────────────────────
 * 2021/11/13   10:14      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class SnmpTrapReceiver extends MultiThreadedTrapReceiver {

    /**
     * 实现CommandResponder的processPdu方法, 用于处理传入的请求、PDU等信息
     * 当接收到trap时，会自动进入这个方法
     *
     * @param respEvnt SNMP信息
     */
    @Override
    public void processPdu(CommandResponderEvent respEvnt) {
        // 解析Response
        if (respEvnt != null && respEvnt.getPDU() != null) {
            @SuppressWarnings("unchecked")
            Vector<VariableBinding> recVBs = (Vector<VariableBinding>) respEvnt.getPDU().getVariableBindings();
            Map<String, String> trapMessage = new HashMap<String, String>(16);
            for (int i = 0; i < recVBs.size(); i++) {
                VariableBinding recVB = recVBs.elementAt(i);
                trapMessage.put(recVB.getOid().toDottedString(), recVB.getVariable().toString());
            }
            if(ToptelMibOid.register.equals(trapMessage.get(ToptelMibOid.trapType.getOid()))){
                /*ToptelDeviceRegisterDTO toptelDeviceInfoDTO = new ToptelDeviceRegisterDTO();
                toptelDeviceInfoDTO.setIpAddress(trapMessage.get(ToptelMibOid.trapDeviceIp.getOid()));
                toptelDeviceInfoDTO.setDeviceType(Integer.parseInt(trapMessage.get(ToptelMibOid.deviceType.getOid())));
                toptelDeviceInfoDTO.setMacAddress(trapMessage.get(ToptelMibOid.deviceID.getOid()));
                //向OTN-Server请求注册
                RpcRfcUtil.callInvoke("com.optel.api.generic.service.DeviceConnect", "toptelDeviceSnmpRegister",
                        new String[]{ToptelDeviceRegisterDTO.class.getName()},  new Object[]{toptelDeviceInfoDTO});*/
            }
        }
    }

    public void close(){
        //TODO
    }

}

