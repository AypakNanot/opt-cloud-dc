/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.nc.nccore;

import cn.hutool.cache.CacheUtil;
import cn.hutool.cache.impl.LRUCache;
import cn.hutool.core.util.StrUtil;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.optel.tmaster.dc.general.base.action.YangMode;
import com.optel.tmaster.dc.general.base.exception.device.DeviceCommicationException;
import com.optel.tmaster.dc.general.base.exception.device.DeviceNotSupportException;
import com.optel.tmaster.dc.general.base.util.MdsalUtil;
import com.optel.tmaster.dc.general.nc.nccore.common.CommonConstant;
import com.optel.tmaster.dc.general.nc.nccore.common.OptVersion;
import org.apache.commons.collections.CollectionUtils;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNode;
import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus.ConnectionStatus.Connecting;
import static org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.NetconfNodeConnectionStatus.ConnectionStatus.UnableToConnect;

/**
 * <ul>
 * <li>(工具类)</li>
 * </ul>
 *
 * @author LWX 2020/8/7 10:48
 */
public class NcTools {
    private static DataBroker dataBroker;

    private static final Logger LOG = LoggerFactory.getLogger(NcTools.class);
    /**
     * 缓存支持设备的 namespace，revision,项目执行根目录下的./etc/version.json，开发环境和打包环境都适用
     * 开发环境 获取到的目录为 opt-cloud-dc/dc-package/karaf/target/assembly/./etc/version.json
     * 打包环境获取到的目录为 TMaster-cloud-dc/./etc/version.json
     */
    protected static final List<OptVersion> OPT_VERSIONS = readJsonConfigs("./etc/version.json");

    private static final LRUCache<String, YangMode> DTT_CONNECT_CACHE = CacheUtil.newLRUCache(1000);

    private NcTools() {
        // ignored
    }

    public static void init(DataBroker dataBroker) {
        NcTools.dataBroker = dataBroker;
    }

    /**
     * 获取设备能力集
     *
     * @param nodeId 设备netconf nodeId
     * @return 能力集
     */
    public static List<String> getAvailableCapabilities(String nodeId) {
        Node node = MdsalUtil.readFromOperational(dataBroker, NcConstant.netconfNodeIid(nodeId));
        if (node == null || node.augmentation(NetconfNode.class) == null) {
            throw new DeviceCommicationException(nodeId, DeviceCommicationException.ConnectType.NETCONF);
        }
        NetconfNode ncNode = node.augmentation(NetconfNode.class);
        if (ncNode != null) {
            NetconfNodeConnectionStatus.ConnectionStatus status = ncNode.getConnectionStatus();
            if (Objects.isNull(status) || status == Connecting || status == UnableToConnect) {
                throw new DeviceCommicationException(nodeId, DeviceCommicationException.ConnectType.NETCONF);
            }
            return MountTools.getAvailableCapability(ncNode);
        }
        return Collections.emptyList();
    }

    /**
     * 是否支持奥普泰扩展yang文件
     *
     * @param nodeId 设备netconf nodeId
     * @return true -- 支持；false--不支持
     */
    public static boolean isSupportOptelExt(String nodeId) {
        List<String> availableCapabilities = getAvailableCapabilities(nodeId);
        if (CollectionUtils.isEmpty(availableCapabilities)) {
            return Boolean.FALSE;
        }
        return isSupportOptelExt(availableCapabilities);
    }

    /**
     * 是否支持奥普泰扩展yang文件
     *
     * @param availableCapability 能力集
     * @return true -- 支持；false--不支持
     */
    public static boolean isSupportOptelExt(List<String> availableCapability) {
        if (CollectionUtils.isEmpty(availableCapability)) {
            return false;
        }
        return availableCapability.contains(CommonConstant.OPT_EXT_CMCC) || availableCapability.contains(CommonConstant.OPT_EXT_CTC);
    }

    public static Optional<YangMode> getNodeYangMode(Node nodeData) {
        if (nodeData == null) {
            throw new DeviceCommicationException("", DeviceCommicationException.ConnectType.NETCONF);
        }
        NetconfNode ncNode = nodeData.augmentation(NetconfNode.class);
        if (ncNode != null) {
            String nodeId = nodeData.getNodeId().getValue();
            if (ncNode.getConnectionStatus() != NetconfNodeConnectionStatus.ConnectionStatus.Connected) {
                throw new DeviceCommicationException(nodeId, DeviceCommicationException.ConnectType.NETCONF);
            }
            List<String> availableCapability = MountTools.getAvailableCapability(ncNode);

            if (availableCapability == null || availableCapability.isEmpty()) {
                LOG.info("availableCapability is empty!!!");
                return Optional.empty();
            }
            LOG.info("nodeId:{} availableCapability includes:{}", nodeId, availableCapability);
            String property = System.getProperty("yangModel");
            if (Objects.nonNull(property)) {
                return YangMode.from(property);
            }
            Optional<YangMode> yangModeByOptVersion = getYangModeByOptVersion(availableCapability);
            if (yangModeByOptVersion.isPresent()) {
                LOG.info("get YangMode by opt-version,Id:{},YangMode:{}", nodeId, yangModeByOptVersion);
                return yangModeByOptVersion;
            }
            yangModeByOptVersion = Optional.ofNullable(getNodeYangMode(availableCapability));
            // 有opt-version 但是未在文件中找到对应的yangMode，可能需要网管进行兼容，添加一个新的yangMode，或将该opt-version信息添加到version.json,需根据实际情况
            return Optional.ofNullable(yangModeByOptVersion.orElseThrow(() -> new DeviceNotSupportException(nodeId, "not support device")));
        }
        return Optional.empty();
    }

    /**
     * 能力集解析 yangMode
     *
     * @param availableCapability 能力集
     * @return yangMode
     */
    private static YangMode getNodeYangMode(List<String> availableCapability) {
        boolean isSupportOptelExt = isSupportOptelExt(availableCapability);
        if (availableCapability.contains(CommonConstant.ACC_OTN_YANG_CAPABILITY)) {
            return isSupportOptelExt ? YangMode.OTN_CMCC_MODE : YangMode.OTN_CMCC_STANDARD_MODE;
        } else if (availableCapability.contains(CommonConstant.ACC_OTN_YANG_CTC_CAPABILITY)) {
            return isSupportOptelExt ? YangMode.OTN_CTC_MODE : YangMode.OTN_CTC_STANDARD_MODE;
        } else if (availableCapability.contains(CommonConstant.OPENCONFIG_YANG_CTC)) {
            return isSupportOptelExt ? YangMode.WDM_CTC_MODE : YangMode.WDM_CTC_STANDARD_MODE;
        } else if (availableCapability.contains(CommonConstant.ACC_OTN_OLD_YANG_CTC_CAPABILITY)) {
            return YangMode.OTN_CTC_OLD_MODE;
        } else if (availableCapability.contains(CommonConstant.DCI_YANG_CMCC_CAPABILITY)) {
            return isSupportOptelExt ? YangMode.WDM_CMCC_MODE : YangMode.WDM_CMCC_STANDARD_MODE;
        }
        return null;
    }

    /**
     * 通过opt-version获取yangMode
     * 若设备存在opt-version但是没找到对应支持的版本，则表示不支持，待开发
     * 若设备不存在opt-version就返回null，后续会根据能力集中特定的yang文件进行判断是否支持
     *
     * @param availableCapability 能力集
     * @return yangMode 参考 类：DeviceFunctionSet
     */
    private static Optional<YangMode> getYangModeByOptVersion(List<String> availableCapability) {
        String optVersion = availableCapability.stream().filter(e -> e.contains("opt-version")).findFirst().orElse(null);
        if (StrUtil.isEmpty(optVersion)) {
            return Optional.empty();
        }
        String startChar = "(";
        String endChar = ")";
        String regex = Pattern.quote(startChar) + "(.*?)" + Pattern.quote(endChar);
        Pattern compile = Pattern.compile(regex);
        Matcher matcher = compile.matcher(optVersion);
        if (matcher.find()) {
            String substring = matcher.group(1);
            for (OptVersion opt : OPT_VERSIONS) {
                Optional<String> first = opt.getCapability().stream().filter(e -> e.equals(substring)).findFirst();
                if (first.isPresent()) {
                    return YangMode.from(opt.getYangMode());
                }
            }
        }
        LOG.warn("not found yang-model by opt-version:{} support.", optVersion);
        return Optional.empty();
    }

    protected static List<OptVersion> readJsonConfigs(final String path) {
        if (StrUtil.isEmpty(path)) {
            return Collections.emptyList();
        }
        File file = Paths.get(path).toFile();
        if (!file.exists()) {
            return Collections.emptyList();
        }
        StringBuilder stringBuilder = new StringBuilder();
        try (FileReader fileReader = new FileReader(file);
             BufferedReader bufferedReader = new BufferedReader(fileReader)) {
            int data;
            while ((data = bufferedReader.read()) != -1) {
                stringBuilder.append((char) data);
            }
        } catch (IOException e) {
            LOG.error("parse json version.json error.", e);
            return Collections.emptyList();
        }
        JSONObject jsonObject = JSONUtil.parseObj(stringBuilder.toString());
        return jsonObject.getBeanList("optVersion", OptVersion.class);
    }

    /**
     * 尝试根据网元Id从缓存中获取YangMode，如果没有则从设备侧去获取
     *
     * @param nodeId 网元Id
     * @return Yang Mode
     */
    public static YangMode getYangMode(String nodeId) {
        return DTT_CONNECT_CACHE.get(nodeId, () -> {
            // 根据网元 Id 获取节点信息
            Node node = MdsalUtil.readFromConfig(dataBroker, NcConstant.netconfNodeIid(nodeId));
            if (Objects.isNull(node)) {
                return null;
            }
            return getNodeYangMode(node).orElse(null);
        });
    }


}
