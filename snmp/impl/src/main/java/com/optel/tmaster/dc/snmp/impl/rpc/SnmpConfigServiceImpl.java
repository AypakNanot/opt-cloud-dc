/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.snmp.impl.rpc;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.snmp.impl.internal.SnmpAsyncGetHandler;
import com.optel.tmaster.dc.snmp.impl.internal.SnmpAsyncSetHandler;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.snmp.config.rev211113.SnmpConfigService;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.snmp.config.rev211113.SnmpGetInput;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.snmp.config.rev211113.SnmpGetOutput;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.snmp.config.rev211113.SnmpSetInput;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.snmp.config.rev211113.SnmpSetOutput;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.snmp4j.Snmp;
import org.snmp4j.transport.DefaultUdpTransportMapping;

import java.io.IOException;

/**
 * SnmpConfigServiceImpl
 * SNMP
 * date       time        author
 * ─────────────────────────────
 * 2021/12/1   14:05      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class SnmpConfigServiceImpl implements SnmpConfigService {

    private static final Logger LOG = LoggerFactory.getLogger(SnmpConfigServiceImpl.class);

    @Override
    public ListenableFuture<RpcResult<SnmpGetOutput>> snmpGet(SnmpGetInput input) {
        Snmp snmp = null;
        try{
            snmp = initSnmp();
            SnmpAsyncGetHandler getHandler = new SnmpAsyncGetHandler(input, snmp);
            return RpcResultUtil.buildFutureResult(getHandler.getRpcResponse(),e->e);
        }finally {
            try {
                if (snmp != null) {
                    snmp.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public ListenableFuture<RpcResult<SnmpSetOutput>> snmpSet(SnmpSetInput input) {
        Snmp snmp = null;
        try{
            snmp = initSnmp();
            SnmpAsyncSetHandler setHandler = new SnmpAsyncSetHandler(input, snmp);
            return RpcResultUtil.buildFutureResult(setHandler.getRpcResponse(),e->null);
        }finally {
            try {
                if (snmp != null) {
                    snmp.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static Snmp initSnmp() {
        Snmp snmp = null;
        try {
            snmp = new Snmp( new DefaultUdpTransportMapping());
            snmp.listen();
        } catch (IOException e) {
            LOG.warn("Failed to create Snmp instance", e);
        }
        return snmp;
    }

}
