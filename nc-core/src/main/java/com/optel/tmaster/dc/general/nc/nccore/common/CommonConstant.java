/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.nc.nccore.common;

/**
 * dc-aggregator - CommonConstant
 * 常量定义
 * date       time        author
 * ─────────────────────────────
 * 2021/9/28   10:14      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class CommonConstant {

    /**
     * ODL
     */
    public static final String ODL_CAPABILITY = "(com:optel:devconf:opt:otn:device?revision=2020-04-25)opt-otn-device";
    /**
     * 判断设备为CMCC OTN YANG的南向能力
     */
    public static final String ACC_OTN_YANG_CAPABILITY = "(urn:ccsa:yang:acc-devm?revision=2020-07-08)acc-devm";
    /**
     * 判断设备为CTC OTN YANG的南向能力
     */
    public static final String ACC_OTN_YANG_CTC_CAPABILITY = "(urn:ccsa:yang:acc-protection-group?revision=2022-01-27)acc-protection-group";

    /**
     * 判断设备为CTC OTN 老版本 YANG的南向能力
     */
    public static final String ACC_OTN_OLD_YANG_CTC_CAPABILITY = "(urn:ccsa:yang:acc-protection-group?revision=2021-09-24)acc-protection-group";
    /**
     * WDM CTC设备的南向能力
     */
    public static final String OPENCONFIG_YANG_CTC = "(http://openconfig.net/yang/platform?revision=2020-06-30)openconfig-platform";

    /**
     * 极简OTN设备的南向能力
     */
    public static final String DCI_YANG_CMCC_CAPABILITY = "(http://openconfig.net/yang/platform?revision=2023-04-26)openconfig-platform";

    /**
     * 奥普泰移动扩展能力
     */
    public static final String OPT_EXT_CMCC = "(com:optel:yang:otn:extension?revision=2020-08-05)opt-otn-extension";

    /**
     * 奥普泰电信扩展能力
     */
    public static final String OPT_EXT_CTC = "(com:optel:yang:otn:extension?revision=2021-03-29)opt-otn-extension";
}
