/*
 * Copyright © 2025 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.util;

import org.apache.commons.lang3.StrUtil;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.SdhSwitchType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.TrailTraceType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.AccSdhService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.GetVcTrailTraceInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.GetVcTrailTraceOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.get.vc.trail.trace.output.vc.trail.trace.values.VcTrailTraceValue;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.get.vc.trail.trace.output.vc.trail.trace.values.VcTrailTraceValueKey;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.function.Consumer;

/**
 * 兼容 2024-07-02 yang 部分属性移至 RPC 接口方式获取
 *
 * @author tanghaiqing
 * @version V1.0.0
 * @since 2025/09/04 09:45
 */
public class CompatibleRpcUtil {
    private static final Logger log = LoggerFactory.getLogger(CompatibleRpcUtil.class);

    public static void getVcTrailTraceThenConsumer(AccSdhService accSdhService, String tpName, String mappingPath, SdhSwitchType vcType, Consumer<Map<VcTrailTraceValueKey, VcTrailTraceValue>> consumer) {
        if (Objects.isNull(accSdhService)) {
            return;
        }
        if (StrUtil.isEmpty(tpName) || StrUtil.isEmpty(mappingPath) || Objects.isNull(vcType)) {
            return;
        }
        GetVcTrailTraceInputBuilder getVcTrailTraceInputBuilder = new GetVcTrailTraceInputBuilder();
        getVcTrailTraceInputBuilder.setTpName(tpName);
        getVcTrailTraceInputBuilder.setMappingPath(mappingPath);
        getVcTrailTraceInputBuilder.setVcType(vcType);
        try {
            Future<RpcResult<GetVcTrailTraceOutput>> getVcTrailTrace = accSdhService.getVcTrailTrace(getVcTrailTraceInputBuilder.build());
            RpcResult<GetVcTrailTraceOutput> result = getVcTrailTrace.get();
            GetVcTrailTraceOutput getVcTrailTraceOutput = result.getResult();
            if (Objects.nonNull(getVcTrailTraceOutput) && Objects.nonNull(getVcTrailTraceOutput.getVcTrailTraceValues())) {
                consumer.accept(Optional.ofNullable(getVcTrailTraceOutput.getVcTrailTraceValues().getVcTrailTraceValue()).orElseGet(Collections::emptyMap));
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("rpc getVcTrailTrace error, check: {}", tpName, e);
        } catch (ExecutionException e) {
            log.error("rpc getVcTrailTrace error, check: {}", tpName, e);
        }
    }

    public static void getVcTrailTraceThenConsumer(AccSdhService accSdhService, String tpName, String mappingPath, SdhSwitchType vcType, TrailTraceType trailTraceType, Consumer<VcTrailTraceValue> consumer) {
        if (Objects.isNull(trailTraceType)) {
            return;
        }
        getVcTrailTraceThenConsumer(accSdhService, tpName, mappingPath, vcType, vcTrailTraceValueMap -> {
            VcTrailTraceValue vcTrailTraceValue = vcTrailTraceValueMap.get(new VcTrailTraceValueKey(trailTraceType));
            if (Objects.isNull(vcTrailTraceValue)) {
                return;
            }
            consumer.accept(vcTrailTraceValue);
        });
    }
}
