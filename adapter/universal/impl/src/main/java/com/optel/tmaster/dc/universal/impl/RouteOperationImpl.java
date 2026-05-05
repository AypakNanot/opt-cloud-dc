/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.universal.impl;

import cn.hutool.core.net.NetUtil;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.AddRouteInput;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.AddRouteOutput;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.DeleteRouteInput;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.DeleteRouteOutput;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.GetEmsRouteExistInput;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.GetEmsRouteExistOutput;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.GetEmsRouteExistOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.JudgeIpConnectInput;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.JudgeIpConnectOutput;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.JudgeIpConnectOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.RouteOperationService;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.add.route.input.Ips;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.add.route.input.IpsKey;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.get.ems.route.exist.output.RouteIp;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.get.ems.route.exist.output.RouteIpBuilder;
import org.opendaylight.yang.gen.v1.com.optel.route.operation.rev220922.get.ems.route.exist.output.RouteIpKey;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Map;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 路由操作实现类
 *
 * @author Quan Jingyuan
 * @since 2022/9/22
 **/
public class RouteOperationImpl implements RouteOperationService {
    private static final String OS_NAME = System.getProperty("os.name");
    private static final Logger LOG = LoggerFactory.getLogger(RouteOperationImpl.class);
    /**
     * CASE_INSENSITIVE 忽略大小写
     */
    private static final Pattern PATTERN = Pattern.compile("(\\d+ms)(\\s+)(TTL=\\d+)", Pattern.CASE_INSENSITIVE);
    private static final String BIN_SH = "/bin/sh";

    @Override
    public ListenableFuture<RpcResult<AddRouteOutput>> addRoute(AddRouteInput input) {
        try {
            @Nullable Map<IpsKey, Ips> ips = input.getIps();
            assert ips != null;
            for (Ips ip : ips.values()) {
                if (isWindows()) {
                    Runtime.getRuntime().exec(String.format("route add %s mask 255.255.255.255 %s", ip.getTargetIp(), ip.getSourceIp()));
                } else if (isLinux()) {
                    Runtime.getRuntime().exec(new String[]{BIN_SH, "-c", String.format("echo '%s' | sudo -S route add -net %s netmask 255.255.255.255 gw %s", input.getSudoPassword(), ip.getTargetIp(), ip.getSourceIp())});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<DeleteRouteOutput>> deleteRoute(DeleteRouteInput input) {
        try {
            for (String ip : Objects.requireNonNull(input.getIps())) {
                if (isWindows()) {
                    Runtime.getRuntime().exec(String.format("route delete %s", ip));
                } else if (isLinux()) {
                    Runtime.getRuntime().exec(new String[]{BIN_SH, "-c", String.format("echo '%s' | sudo -S route del -net %s netmask 255.255.255.255", input.getSudoPassword(), ip)});
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetEmsRouteExistOutput>> getEmsRouteExist(GetEmsRouteExistInput input) {
        try {
            Process process = null;
            if (isWindows()) {
                process = Runtime.getRuntime().exec("route print");
            } else if (isLinux()) {
                process = Runtime.getRuntime().exec(new String[]{BIN_SH, "-c", String.format("echo '%s' | sudo -S route -n", input.getSudoPassword())});
            }
            if (Objects.isNull(process)) {
                return null;
            }

            StringBuilder buffer = new StringBuilder();
            try (InputStream inputStream = process.getInputStream();
                 InputStreamReader inputStreamReader = new InputStreamReader(inputStream, "GBK");
                 BufferedReader reader = new BufferedReader(inputStreamReader)) {
                String result = "";
                while ((result = reader.readLine()) != null) {
                    buffer.append(result).append("\n");
                }
            }
            if (buffer.length() > 0) {
                GetEmsRouteExistOutputBuilder builder = new GetEmsRouteExistOutputBuilder();
                Map<RouteIpKey, RouteIp> values = Maps.newHashMap();
                String tempStr = buffer.toString();
                String ipv4;
                if (isWindows()) {
                    ipv4 = tempStr.substring(tempStr.indexOf("IPv4"), tempStr.indexOf("IPv6"));
                } else {
                    ipv4 = tempStr;
                }
                LOG.info("route info : " + ipv4);
                for (String ip : Objects.requireNonNull(input.getIps())) {
                    RouteIpBuilder routeIp = new RouteIpBuilder();
                    routeIp.setIp(ip);
                    routeIp.withKey(new RouteIpKey(ip));
                    routeIp.setIsExist(ipv4.contains(ip));
                    values.put(routeIp.key(), routeIp.build());
                }
                builder.setRouteIp(values);
                return RpcResultUtil.success(builder.build());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<JudgeIpConnectOutput>> judgeIpConnect(JudgeIpConnectInput input) {
        JudgeIpConnectOutputBuilder builder = new JudgeIpConnectOutputBuilder();
        builder.setIpResult(NetUtil.ping(input.getIp(), 2000));
        //判断端口是否被占用
        if (Objects.nonNull(input.getPort())) {
            builder.setPortResult(NetUtil.isUsableLocalPort(Integer.parseInt(input.getPort())));
        }
        return RpcResultUtil.success(builder.build());
    }


    private boolean isWindows() {
        final String win = "windows";
        return OS_NAME != null && OS_NAME.toLowerCase().startsWith(win);
    }

    private boolean isLinux() {
        final String win = "linux";
        return OS_NAME != null && OS_NAME.toLowerCase().startsWith(win);
    }

    /**
     * 判断是否ping通
     *
     * @param ipAddress ip
     * @param pingTimes 次数
     * @param timeOut   超时时间
     */
    @SuppressWarnings("squid:S1144")
    private boolean ping(String ipAddress, int pingTimes, int timeOut) {
        // 将要执行的ping命令,此命令是windows格式的命令
        String pingCommand;
        if (isWindows()) {
            pingCommand = "ping " + ipAddress + " -n " + pingTimes + " -w " + timeOut;
        } else if (isLinux()) {
            pingCommand = "ping " + ipAddress + " -n " + pingTimes + " -w " + timeOut;
        } else {
            return false;
        }
        try {
            // 执行命令并获取输出
            Process p = Runtime.getRuntime().exec(pingCommand);
            if (p == null) {
                return false;
            }
            try (InputStreamReader inputStreamReader = new InputStreamReader(p.getInputStream());
                 BufferedReader in = new BufferedReader(inputStreamReader)) {
                int connectedCount = 0;
                String line;
                while ((line = in.readLine()) != null) {
                    connectedCount += getCheckResult(line);
                }
                // 如果出现类似=23ms TTL=62这样的字样,出现的次数=测试次数则返回真
                return connectedCount == pingTimes;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return false;
        }
    }

    /**
     * 正则比对含有=18ms TTL=16字样,说明已经ping通,返回1,否則返回0.
     *
     * @param line 比对的字符串
     * @return ping通返回1, 否則返回0.
     */
    private static int getCheckResult(String line) {
        Matcher matcher = PATTERN.matcher(line);
        if (matcher.find()) {
            return 1;
        }
        return 0;
    }
}
