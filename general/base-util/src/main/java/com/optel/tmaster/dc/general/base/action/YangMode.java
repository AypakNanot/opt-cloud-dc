/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.action;

import cn.hutool.core.collection.CollUtil;
import com.google.common.base.Objects;

import java.util.Collection;
import java.util.Optional;


/**
 * <pre>
 * opt-cloud-dc - YangMode
 * </pre>
 * Yang Mode
 * <pre>
 * date              time          author
 * ───────────────────────────────────────
 * 2024-10-19 0019   8:54:14      LiHua
 * Copyright (c) 2024, H-OPTEL All Rights Reserved.
 * </pre>
 *
 * @author LiHua
 * @version V2.0.0
 */
public class YangMode {
    /**
     * 移动
     */
    public static final String OTN_CMCC_MODE_TYPE = "1";
    /**
     * 电信
     */
    public static final String OTN_CTC_MODE_TYPE = "2";
    /**
     * 电信 兼容老版本设备
     */
    public static final String OTN_CTC_OLD_MODE_TYPE = "3";
    /**
     * DCI设备
     */
    public static final String WDM_CTC_MODE_TYPE = "201";
    /**
     * 极简OTN
     */
    public static final String WDM_CMCC_MODE_TYPE = "202";
    /**
     * 标准 移动
     */
    public static final String OTN_CMCC_STANDARD_MODE_TYPE = "10001";
    /**
     * 标准 电信
     */
    public static final String OTN_CTC_STANDARD_MODE_TYPE = "10002";
    /**
     * 标准 DCI设备（电信）
     */
    public static final String WDM_CTC_STANDARD_MODE_TYPE = "10201";
    /**
     * 标准 极简OTN（移动）
     */
    public static final String WDM_CMCC_STANDARD_MODE_TYPE = "10202";
    /**
     * 移动
     */
    public static final YangMode OTN_CMCC_MODE = new YangMode(YangType.OTN_CMCC, OTN_CMCC_MODE_TYPE);
    public static final YangMode OTN_CTC_MODE = new YangMode(YangType.OTN_CTC, OTN_CTC_MODE_TYPE);
    public static final YangMode OTN_CTC_OLD_MODE = new YangMode(YangType.OTN_CTC, OTN_CTC_OLD_MODE_TYPE);
    public static final YangMode WDM_CTC_MODE = new YangMode(YangType.WDM_CTC, WDM_CTC_MODE_TYPE);
    public static final YangMode WDM_CMCC_MODE = new YangMode(YangType.WDM_CMCC, WDM_CMCC_MODE_TYPE);
    public static final YangMode OTN_CMCC_STANDARD_MODE = new YangMode(YangType.OTN_CMCC, OTN_CMCC_STANDARD_MODE_TYPE);
    public static final YangMode OTN_CTC_STANDARD_MODE = new YangMode(YangType.OTN_CTC, OTN_CTC_STANDARD_MODE_TYPE);
    public static final YangMode WDM_CTC_STANDARD_MODE = new YangMode(YangType.WDM_CTC, WDM_CTC_STANDARD_MODE_TYPE);
    public static final YangMode WDM_CMCC_STANDARD_MODE = new YangMode(YangType.WDM_CMCC, WDM_CMCC_STANDARD_MODE_TYPE);

    /**
     * 产品线类型
     */
    YangType type;
    /**
     * yang model
     */
    String mode;

    private YangMode(YangType type, String yangMode) {
        this.type = type;
        this.mode = yangMode;
    }

    public YangType getType() {
        return type;
    }

    public String getMode() {
        return mode;
    }

    /**
     * 集合
     *
     * @return yang mode
     */
    public static Collection<YangMode> valueOf() {
        return CollUtil.newArrayList(OTN_CMCC_MODE, OTN_CTC_MODE, OTN_CTC_OLD_MODE, WDM_CTC_MODE, WDM_CMCC_MODE,
                OTN_CMCC_STANDARD_MODE, OTN_CTC_STANDARD_MODE,
                WDM_CTC_STANDARD_MODE, WDM_CMCC_STANDARD_MODE);
    }

    /**
     * 根据 mode 值获取yang mode
     *
     * @param mode mode
     * @return mode
     */
    public static Optional<YangMode> from(String mode) {
        return YangMode.valueOf().stream().filter(e -> e.mode.equals(mode)).findAny();
    }

    /**
     * 根据 yang type 获取yang mode
     *
     * @param yangType yang type
     * @return mode
     */
    public static Collection<YangMode> typeOf(YangType yangType) {
        return valueOf().stream().filter(e -> e.type.equals(yangType)).toList();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YangMode yangMode = (YangMode) o;
        return Objects.equal(type, yangMode.type) && Objects.equal(mode, yangMode.mode);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type, mode);
    }
}