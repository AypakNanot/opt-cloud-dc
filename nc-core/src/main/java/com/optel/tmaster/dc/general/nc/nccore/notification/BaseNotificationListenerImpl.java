/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.nc.nccore.notification;

/**
 * <ul>
 * <li>(通知实现基类)</li>
 * </ul>
 *
 * @author LWX 2020/5/4 15:15
 */
public class BaseNotificationListenerImpl {


    /** 网元ID，用于标识通知来自于哪个设备 */
    protected String nodeId;

    public BaseNotificationListenerImpl(String nodeId){
        this.nodeId = nodeId;
    }

}
