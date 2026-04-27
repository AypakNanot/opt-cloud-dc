/*
 * Copyright (c) 2013 Cisco Systems, Inc. and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package org.opendaylight.mdsal.binding.dom.adapter.invoke;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.util.concurrent.Futures;
import com.google.common.util.concurrent.ListenableFuture;
import org.opendaylight.mdsal.binding.spec.reflect.BindingReflections;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.RpcService;
import org.opendaylight.yangtools.yang.common.ErrorType;
import org.opendaylight.yangtools.yang.common.OperationFailedException;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.Objects;

final class RpcMethodInvoker {
    private static final Logger LOG = LoggerFactory.getLogger(RpcMethodInvoker.class);
    private static final MethodType INVOCATION_SIGNATURE = MethodType.methodType(ListenableFuture.class,
            RpcService.class, DataObject.class);

    private final MethodHandle handle;

    @VisibleForTesting
    RpcMethodInvoker(final MethodHandle handle) {
        this.handle = handle.asType(INVOCATION_SIGNATURE);
    }

    static RpcMethodInvoker from(final Method method) {
        BindingReflections.resolveRpcInputClass(method)
                .orElseThrow(() -> new IllegalArgumentException("Method " + method + " does not have an input argument"));

        final MethodHandle methodHandle;
        try {
            methodHandle = MethodHandles.publicLookup().unreflect(method);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Lookup on public method failed.", e);
        }

        return new RpcMethodInvoker(methodHandle);
    }

    @SuppressWarnings("checkstyle:illegalCatch")
    ListenableFuture<RpcResult<?>> invokeOn(final RpcService impl, final DataObject input) {
        try {
            return (ListenableFuture<RpcResult<?>>) handle.invokeExact(impl, input);
        } catch (Throwable e) {
            LOG.error("invokeOn Error", e);
            Throwable cause = e.getCause();
            if (Objects.nonNull(cause) && !(cause instanceof OperationFailedException)) {
                cause = cause.getCause();
            }
            if (Objects.nonNull(cause) && cause instanceof OperationFailedException) {
                return Futures.immediateFuture(RpcResultBuilder.failed().withRpcErrors(((OperationFailedException) cause).getErrorList()).build());
            }
            //返回错误的message不能为空,否则错误信息不能按照预期返回
            return Futures.immediateFuture(RpcResultBuilder.failed().withError(ErrorType.APPLICATION, null, e.getMessage() == null ? e.getClass().getSimpleName() : e.getMessage(), "0", null, e).build());
        }
    }
}
