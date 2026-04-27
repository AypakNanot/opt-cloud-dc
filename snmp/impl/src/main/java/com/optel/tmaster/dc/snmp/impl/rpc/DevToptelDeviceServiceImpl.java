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
import com.optel.tmaster.dc.snmp.impl.oid.ToptelMibOid;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.dev.toptel.device.rev211113.DevToptelDeviceService;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.dev.toptel.device.rev211113.HeartBeatInput;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.dev.toptel.device.rev211113.HeartBeatOutput;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.dev.toptel.device.rev211113.QueryToptelDeviceInfoInput;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.dev.toptel.device.rev211113.QueryToptelDeviceInfoOutput;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.dev.toptel.device.rev211113.QueryToptelDeviceInfoOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.dev.toptel.device.rev211113.RegisterResultInput;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.dev.toptel.device.rev211113.RegisterResultOutput;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.snmp.config.rev211113.SnmpGetInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.snmp.config.rev211113.SnmpGetOutput;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.snmp.config.rev211113.SnmpGetType;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.snmp.config.rev211113.SnmpSetInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.params.yang.snmp.config.rev211113.snmp.get.output.Result;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.List;
import java.util.Map;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * DevSnmpServiceImpl
 * DevSnmpService 实现类
 * date       time        author
 * ─────────────────────────────
 * 2021/11/13   15:32      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class DevToptelDeviceServiceImpl implements DevToptelDeviceService {


    @Override
    public ListenableFuture<RpcResult<QueryToptelDeviceInfoOutput>> queryToptelDeviceInfo(QueryToptelDeviceInfoInput input) {
        SnmpGetInputBuilder snmpGetInput = new SnmpGetInputBuilder(input);
        snmpGetInput.setOid(ToptelMibOid.deviceInfo.getOid());
        snmpGetInput.setGetType(SnmpGetType.GETBULK);
        SnmpConfigServiceImpl snmpRequest = new SnmpConfigServiceImpl();
        Future<RpcResult<SnmpGetOutput>> rpcResultFuture = snmpRequest.snmpGet(snmpGetInput.build());
        return RpcResultUtil.buildFutureResult(rpcResultFuture, e->{
            QueryToptelDeviceInfoOutputBuilder queryToptelDeviceInfoOutputBuilder = new QueryToptelDeviceInfoOutputBuilder();
            List<Result> results = e.getResult();
            if(results != null){
                //先过滤掉value为endOfMibView的值。结束oid，无效值
                Map<String, String> getResult = results.stream().filter(value->!"endOfMibView".equals(value.getValue())).collect(Collectors.toMap(Result::getOid, Result::getValue));
                queryToptelDeviceInfoOutputBuilder.setIpAddress(input.getIpAddress());
                queryToptelDeviceInfoOutputBuilder.setMacAddress(getResult.get(ToptelMibOid.deviceID.getOid()));
                queryToptelDeviceInfoOutputBuilder.setDeviceType(getResult.get(ToptelMibOid.deviceType.getOid()));
            }
            return queryToptelDeviceInfoOutputBuilder.build();
        });
    }

    @Override
    public ListenableFuture<RpcResult<RegisterResultOutput>> registerResult(RegisterResultInput input) {
        SnmpSetInputBuilder snmpSetInput = new SnmpSetInputBuilder(input);
        snmpSetInput.setOid(ToptelMibOid.trapRegisterResult.getOid());
        snmpSetInput.setValue(String.valueOf(input.getResult()));
        SnmpConfigServiceImpl snmpRequest = new SnmpConfigServiceImpl();
        return RpcResultUtil.buildFutureResult(snmpRequest.snmpSet(snmpSetInput.build()), e->null);
    }


    @Override
    public ListenableFuture<RpcResult<HeartBeatOutput>> heartBeat(HeartBeatInput input) {
        SnmpGetInputBuilder snmpGetInput = new SnmpGetInputBuilder(input);
        snmpGetInput.setOid(ToptelMibOid.deviceType.getOid());
        snmpGetInput.setGetType(SnmpGetType.GET);
        SnmpConfigServiceImpl snmpRequestUtil = new SnmpConfigServiceImpl();
        Future<RpcResult<SnmpGetOutput>> rpcResultFuture = snmpRequestUtil.snmpGet(snmpGetInput.build());
        return RpcResultUtil.buildFutureResult(rpcResultFuture, e-> null);
    }

}
