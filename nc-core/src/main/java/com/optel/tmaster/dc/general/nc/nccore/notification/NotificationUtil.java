/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.nc.nccore.notification;

import org.opendaylight.yangtools.yang.binding.YangModuleInfo;

import java.util.List;

/**
 * NotificationUtil
 * 通知工具类
 * date       time        author
 * ─────────────────────────────
 * 2022/3/24   16:50      liwenxue
 * Copyright (c) 2022, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class NotificationUtil {

    /**
     * NETCONF 通知能力
     */
    private static YangModuleInfo NOTIFICATION_YANG = org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.netconf.notification._1._0.rev080714.$YangModuleInfoImpl.getInstance();
    public final static String NOTIFICATION_CAPABILITY = NOTIFICATION_YANG.getName().intern().toString();

    public static  boolean isSupportSubscribe(List<String> availableCapability){
        if(availableCapability == null){
            return false;
        }
        return availableCapability.contains(NOTIFICATION_CAPABILITY);
    }
}
