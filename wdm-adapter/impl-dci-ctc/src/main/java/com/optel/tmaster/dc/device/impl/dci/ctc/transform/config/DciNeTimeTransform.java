/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.transform.config;

import cn.hutool.core.collection.CollUtil;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.base.GeneralUtilTransform;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.system.rev220208.system.ntp.server.top.ServersBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.ne.time.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.ntp.server.top.servers.Server;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.ntp.server.top.servers.ServerKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.ntp.server.top.servers.server.ConfigBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.ntp.top.Ntp;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.top.system.State;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.Host;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.inet.rev190425.PortNumber;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: DciNeTimeTransform
 * <ul>
 * <li>网元时间服务 转换类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2022/3/11 11:08
 */
public class DciNeTimeTransform implements GeneralUtilTransform {
    /**
     * 查询Ntp服务
     *
     * @param ntp ntp
     * @return api
     */
    public QueryMeNtpOutput devQueryMeNtpOutputToApi(Ntp ntp) {
        var outputBuilder = new QueryMeNtpOutputBuilder();
        var ntpBuilder = new org.opendaylight.yang.gen.v1.com.optel.dci.yang.system.rev220208.system.ntp.top.NtpBuilder();
        // 为NTP全局使能赋值
        ntpBuilder.setEnabled(ntp.getState().getEnabled());
        // 为NTP服务器赋值
        if (ntp.getServers() == null || CollUtil.isEmpty(ntp.getServers().getServer())) {
            outputBuilder.setNtp(ntpBuilder.build());
            return outputBuilder.build();
        }
        Map<ServerKey, Server> serverDevMap = ntp.getServers().getServer();
        Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.system.rev220208.system.ntp.server.top.servers.ServerKey, org.opendaylight.yang.gen.v1.com.optel.dci.yang.system.rev220208.system.ntp.server.top.servers.Server> serverApiMap = new HashMap<>(serverDevMap.size());
        var serversBuilder = new ServersBuilder();
        serverDevMap.forEach((key, value) -> {
            var serverKey = new org.opendaylight.yang.gen.v1.com.optel.dci.yang.system.rev220208.system.ntp.server.top.servers.ServerKey(devHostToApi(key.getAddress()));
            var valueBuilder = new org.opendaylight.yang.gen.v1.com.optel.dci.yang.system.rev220208.system.ntp.server.top.servers.ServerBuilder();
            var stateBuilder = new org.opendaylight.yang.gen.v1.com.optel.dci.yang.system.rev220208.system.ntp.server.top.servers.server.StateBuilder();
            var state = value.getState();
            var host = devHostToApi(value.getAddress());
            valueBuilder.setAddress(host);
            stateBuilder.setAddress(host);
            stateBuilder.setPort(devPortNumberToApi(state.getPort()));
            stateBuilder.setVersion(state.getVersion());
            valueBuilder.setState(stateBuilder.build());
            serverApiMap.put(serverKey, valueBuilder.build());
        });
        serversBuilder.setServer(serverApiMap);
        ntpBuilder.setServers(serversBuilder.build());
        outputBuilder.setNtp(ntpBuilder.build());
        return outputBuilder.build();
    }

    /**
     * 修改、添加NTP服务
     *
     * @param input api
     * @return dev
     */
    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.ntp.server.top.servers.Server apiAddModifyMeNtpInputToDev(AddModifyMeNtpInput input) {
        if (input == null) {
            return null;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.ntp.server.top.servers.ServerBuilder serverBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.system.ntp.server.top.servers.ServerBuilder();
        ConfigBuilder configBuilder = new ConfigBuilder();
        configBuilder.setAddress(apiHostToDev(input.getAddress()));
        configBuilder.setPort(apiPortNumberToDev(input.getPort()));
        configBuilder.setVersion(input.getVersion());
        serverBuilder.setAddress(apiHostToDev(input.getAddress()));
        serverBuilder.setConfig(configBuilder.build());
        return serverBuilder.build();
    }

    /**
     * 删除NTP服务
     *
     * @param input api
     * @return dev
     */
    public Host apiDeleteMeNtpInputToDev(DeleteMeNtpInput input) {
        return apiHostToDev(input.getAddress());
    }

    /**
     * 网元时间 转换
     *
     * @param sysState 系统信息 State dev
     * @return 网元时间输出 api
     */
    public GetNeTimeOutput devGetNeTimeOutputToApi(State sysState) {
        if (sysState == null) {
            return null;
        }
        GetNeTimeOutputBuilder getNeTimeOutputBuilder = new GetNeTimeOutputBuilder();
        getNeTimeOutputBuilder.setDatetime(sysState.getCurrentDatetime());
        return getNeTimeOutputBuilder.build();
    }
}
