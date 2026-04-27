/*
 * Copyright © 2025 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.util;

import com.optel.tmaster.dc.general.nc.nccore.NcTools;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StrUtil;

import java.util.List;

/**
 * @author tanghaiqing
 * @version V1.0.0
 * @since 2025/09/04 11:06
 */
public class OtnRevisionUtils {
    private static final String DEVM_2024_07_02 = "(urn:ccsa:yang:acc-devm?revision=2024-07-02)acc-devm";

    private OtnRevisionUtils() {
        // ignored
    }

    /**
     * 是否包含指定能力
     *
     * @param nodeId     netconf node id
     * @param capability 能力
     * @return 是否包含
     */
    public static boolean isIncludeCapability(String nodeId, String capability) {
        if (StrUtil.isEmpty(capability)) {
            return Boolean.FALSE;
        }
        List<String> availableCapabilities = NcTools.getAvailableCapabilities(nodeId);
        if (CollectionUtils.isEmpty(availableCapabilities)) {
            return Boolean.FALSE;
        }
        return availableCapabilities.stream().anyMatch(e -> e.contains(capability));
    }

    /**
     * 判断是否为20240702版本
     *
     * @param nodeId netconf node id
     * @return 结果
     */
    public static boolean isRevision20240702(String nodeId) {
        return isIncludeCapability(nodeId, DEVM_2024_07_02);
    }
}
