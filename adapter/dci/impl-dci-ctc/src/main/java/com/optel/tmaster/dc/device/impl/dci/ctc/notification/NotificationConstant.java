/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.notification;

import org.opendaylight.yangtools.yang.binding.YangModuleInfo;

/**
 * NotificationConstant
 * 通知定义
 * date       time        author
 * ─────────────────────────────
 * 2022/3/24   16:41      liwenxue
 * Copyright (c) 2022, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class NotificationConstant {

    private static YangModuleInfo OPEN_CONFIG_EVENT= org.opendaylight.yang.gen.v1.http.openconfig.net.yang.event.rev200630.$YangModuleInfoImpl.getInstance();
    public final static String OPEN_CONFIG_EVENT__CAPABILITY = OPEN_CONFIG_EVENT.getName().intern().toString();

    private static YangModuleInfo OPEN_CONFIG_SYSTEM= org.opendaylight.yang.gen.v1.http.openconfig.net.yang.system.rev200630.$YangModuleInfoImpl.getInstance();
    public final static String OPEN_CONFIG_SYSTEM__CAPABILITY = OPEN_CONFIG_SYSTEM.getName().intern().toString();

}
