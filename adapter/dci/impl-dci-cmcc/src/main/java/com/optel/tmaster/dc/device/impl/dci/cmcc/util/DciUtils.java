/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.util;

import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.RpcResult;

/**
 * DciUtils
 * 工具类
 * date       time        author
 * ─────────────────────────────
 * 2022/2/10   13:59      liwenxue
 * Copyright (c) 2022, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class DciUtils {

    public static RpcResultUtil.OutRpcResult getOcRpcResult(Boolean isSuccess){
        RpcResultUtil.OutRpcResult result = new RpcResultUtil.OutRpcResult();
        result.setSuccess(isSuccess);
        return result;
    }


    public static RpcResultUtil.OutRpcResult getOcRpcResult(Boolean isSuccess, String msg){
        RpcResultUtil.OutRpcResult result = new RpcResultUtil.OutRpcResult();
        result.setSuccess(isSuccess);
        result.setMsg(msg);
        return result;
    }

    public static RpcResultUtil.OutRpcResult getOcRpcResult(RpcResult rpcResult){
        RpcResultUtil.OutRpcResult result = new RpcResultUtil.OutRpcResult();
        result.setSuccess(rpcResult == RpcResult.SUCCESS);
        return result;
    }


    public static RpcResultUtil.OutRpcResult getOcRpcResult(RpcResult rpcResult, String msg){
        RpcResultUtil.OutRpcResult result = new RpcResultUtil.OutRpcResult();
        result.setSuccess(rpcResult == RpcResult.SUCCESS);
        result.setMsg(msg);
        return result;
    }
}
