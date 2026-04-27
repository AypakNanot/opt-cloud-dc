/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.util;

import cn.hutool.core.util.StrUtil;
import org.opendaylight.yangtools.yang.common.ErrorTag;
import org.opendaylight.yangtools.yang.common.ErrorType;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;

/**
 * <ul>
 * <li>(提供一些构建RpcError的方法)</li>
 * </ul>
 *
 * @author LWX 2020/6/19 14:57
 */
public class RpcErrorUtil {

    /**
     * 构建RpcError
     *
     * @param message 错误信息
     * @return RpcError
     */
    public static RpcError getRpcError(String message) {
        return getRpcError(message, null, "0");
    }

    /**
     * 构建RpcError
     *
     * @param message        错误信息
     * @param info           额外信息
     * @param applicationTag applicationTage
     * @return RpcError
     */
    public static RpcError getRpcError(String message, String info, String applicationTag) {
        return RpcResultBuilder.newError(ErrorType.APPLICATION, ErrorTag.OPERATION_FAILED, StrUtil.isEmpty(message) ? "FAIL" : message, applicationTag, info, null);
    }

    public static RpcError getRpcError(String message, String applicationTag) {
        return getRpcError(message, null, applicationTag);
    }


}
