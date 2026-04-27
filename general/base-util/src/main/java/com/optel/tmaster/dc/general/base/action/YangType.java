/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.action;

import com.google.common.base.Objects;

public class YangType {
    /**
     * 产品线 种类 -- 移动 OTN
     */
    public static final String OTN_CMCC_TYPE = "OTN_CMCC";
    /**
     * 产品线 种类 -- 电信 OTN
     */
    public static final String OTN_CTC_TYPE = "OTN_CTC";
    /**
     * 产品线 种类 -- 移动 WDM
     */
    public static final String WDM_CMCC_TYPE = "WDM_CMCC";
    /**
     * 产品线 种类 -- 电信 WDM
     */
    public static final String WDM_CTC_TYPE = "WDM_CTC";

    public static final YangType OTN_CMCC = new YangType(OTN_CMCC_TYPE);
    public static final YangType OTN_CTC = new YangType(OTN_CTC_TYPE);
    public static final YangType WDM_CMCC = new YangType(WDM_CMCC_TYPE);
    public static final YangType WDM_CTC = new YangType(WDM_CTC_TYPE);
    /**
     * 类型， 产品线
     */
    String type;

    private YangType(String type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        YangType yangType = (YangType) o;
        return Objects.equal(type, yangType.type);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(type);
    }
}