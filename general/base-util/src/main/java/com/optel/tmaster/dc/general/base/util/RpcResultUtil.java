/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.util;

import com.google.common.collect.ImmutableList;
import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.general.base.exception.ExceptionApplicationTag;
import com.optel.tmaster.dc.general.base.exception.device.DeviceOperaFailException;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;

import java.util.Arrays;
import java.util.concurrent.Future;
import java.util.function.Function;

/**
 * 
 * ClassName: RpcResultUtil
 * <ul>
 * <li>(构建RPC接口的返回结果类)</li>
 * </ul>
 * @author LWX 2019年9月24日下午1:46:55
 *
 */
public class RpcResultUtil {


    /**
     * 将失败的操作结果错误信息，返回失败的结果
     * @param result 失败的结果
     * @param <T> 返回类型
     * @return 失败结果
     */
    public static <T> ListenableFuture<RpcResult<T>> failed(RpcResult<?> result){
        return RpcResultBuilder.<T>failed().withRpcErrors(result.getErrors()).buildFuture();
    }

    /**
     * 返回失败的结果
     * @param errors 错误信息
     * @param <T> 类型
     * @return 失败结果
     */
    public static <T> ListenableFuture<RpcResult<T>> failed(RpcError... errors){
        if (errors != null && errors.length > 0) {
            return RpcResultBuilder.<T>failed().withRpcErrors( ImmutableList.copyOf( Arrays.asList( errors ) )).buildFuture();
        } else {
            return RpcResultBuilder.<T>failed().buildFuture();
        }
    }
	
	/**
	 * 
	 * MethodName: success
	 * <ul>
	 * <li>(返回成功结果)</li>
	 * </ul>
	 * @param <T> 结果数据类型
	 * @return 成功结果
	 * @author LWX 2019年9月24日下午5:11:06
	 */
	public static <T> ListenableFuture<RpcResult<T>> success(){
		return RpcResultBuilder.<T>success().buildFuture();
	}
	
	/**
	 * 
	 * MethodName: success
	 * <ul>
	 * <li>(返回成功结果)</li>
	 * </ul>
	 * @param result 返回信息
	 * @param <T> 结果类型
	 * @return 成功
	 * @author LWX 2019年9月24日下午5:11:25
	 */
	public static <T> ListenableFuture<RpcResult<T>> success(T result){
		return RpcResultBuilder.success(result).buildFuture();
	}


	public static <R,T> ListenableFuture<RpcResult<R>> buildFutureResult(Future<RpcResult<T>> resultFuture){
		return buildFutureResult(resultFuture, null);
	}
	/**
	 * 解析RPC调用结果
	 * @param resultFuture 调用RPC返回的Future结果
	 * @param function 结果 result 转换函数
	 * @param <R> 转换后的泛型
	 * @param <T> 转换前的泛型
	 * @return 转换结果
	 */
	public static <R,T> ListenableFuture<RpcResult<R>> buildFutureResult(Future<RpcResult<T>> resultFuture, Function<? super T, ? extends R> function){
		try {
			RpcResult<T> result = resultFuture.get();
			if(result.isSuccessful()){
				if (result.getResult() != null && function != null) {
					return RpcResultUtil.success(function.apply(result.getResult()));
				}
				return RpcResultUtil.success();
			}
			else{
				return RpcResultUtil.failed(result);
			}
		} catch (Exception ex) {
			throw new DeviceOperaFailException(ex);
		}
	}

	/**
	 * 解析RPC调用结果(特别地，当RPC通过输出参数返回结果时使用)
	 * @param resultFuture 调用RPC返回的Future结果
	 * @param function 结果 result 转换函数
	 * @param rpcResFun RPC成功失败标识结果方法。需要从输出参数中获取结果时，将输出对象作为输入参数，方法返回 OutRpcResult 表示操作结果
	 * @param <R> 转换后的泛型
	 * @param <T> 转换前的泛型
	 * @return 转换结果
	 */
	public static <R,T> ListenableFuture<RpcResult<R>> buildFutureResult(Future<RpcResult<T>> resultFuture, Function<? super T, ? extends R> function,Function<? super T, OutRpcResult> rpcResFun){
		try {
			RpcResult<T> result = resultFuture.get();
			if(result.isSuccessful()){
				if (result.getResult() != null) {
					if(rpcResFun != null){
						OutRpcResult outRpcResult = rpcResFun.apply(result.getResult());
						// 当返回结果为失败时，返回失败
						if(outRpcResult.getSuccess() != null && !outRpcResult.getSuccess()){
							return failed(RpcErrorUtil.getRpcError(outRpcResult.getMsg(),"FAIL", ExceptionApplicationTag.DEVICE_OPERATION_FAILED));
						}
					}
					if(function != null){
						return RpcResultUtil.success(function.apply(result.getResult()));
					}
				}
				return RpcResultUtil.success();
			}
			else{
				return RpcResultUtil.failed(result);
			}
		} catch (Exception ex) {
			throw new DeviceOperaFailException(ex);
		}
	}

	public static class OutRpcResult{
		private Boolean isSuccess;

		private String msg;

		public Boolean getSuccess() {
			return isSuccess;
		}

		public void setSuccess(Boolean success) {
			isSuccess = success;
		}

		public String getMsg() {
			return msg;
		}

		public void setMsg(String msg) {
			this.msg = msg;
		}
	}
	

}
