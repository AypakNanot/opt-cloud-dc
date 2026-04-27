/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.exception.device;

import com.optel.tmaster.dc.common.OptelDcException;
import com.optel.tmaster.dc.general.base.exception.ExceptionApplicationTag;
import com.optel.tmaster.dc.general.base.util.RpcErrorUtil;

/**
 * <ul>
 * <li>(设备通信异常)</li>
 * </ul>
 *
 * @author LWX 2020/6/20 14:49
 */
public class DeviceCommicationException extends OptelDcException {
    public enum ConnectType{
        /**
         * netconf
         */
        NETCONF,
        /**
         * openflow
         */
        OPENFLOW;
    }

    /**
     * 构造方法
     * @param neId 网元ID
     * @param connectType 连接类型
     */
    public DeviceCommicationException(String neId, ConnectType connectType){
        super("Commication Exception", RpcErrorUtil.getRpcError(neId, connectType.name(), ExceptionApplicationTag.DEVICE_COMMICATION_ERROR));
    }

    @Override
    public String getApplicationTag(){
        return ExceptionApplicationTag.DEVICE_COMMICATION_ERROR;
    }

}
