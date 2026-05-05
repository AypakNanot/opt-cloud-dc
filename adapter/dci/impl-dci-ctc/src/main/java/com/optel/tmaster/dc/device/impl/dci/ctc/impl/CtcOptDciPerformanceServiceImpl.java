/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciPerformanceServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.performance.DciPerformanceTransform;
import com.optel.tmaster.dc.device.impl.dci.ctc.util.DciUtils;
import com.optel.tmaster.dc.general.base.exception.device.DeviceOperaFailException;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.types.rev220208.PmGranularityType;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.types.rev220208.PmParameterType;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.types.rev220208.PmPointRef;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.types.rev220208.PmpObjectType;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.OpenconfigPerformanceService;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.Pmps;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.PmpsBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.Pmp;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.Tcas;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.TcasBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.Tca;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.top.Performance;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.OpenconfigRpcService;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassName: CtcOptDciPerformanceServiceImpl
 * <ul>
 * <li>中电信 性能 服务实现类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2022/3/18 10:21
 */
public class CtcOptDciPerformanceServiceImpl extends BaseDciPerformanceServiceImpl implements IDeviceServiceWdmCtc {
    /**
     * Invoke {@code get-pm-data} RPC.
     *
     * @param input of {@code get-pm-data}
     * @return output of {@code get-pm-data}
     */
    @Override
    public ListenableFuture<RpcResult<GetPmDataOutput>> getPmData(GetPmDataInput input) {
        OpenconfigPerformanceService openconfigPerformanceService = MountTools.getRpcService(input.getNeId(), OpenconfigPerformanceService.class);

        DciPerformanceTransform transform = new DciPerformanceTransform();

        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataOutput>> resultListenableFuture
                = openconfigPerformanceService.getPmData(transform.apiGetPmDataInputToDev(input));
        try {
            RpcResult<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataOutput> result = resultListenableFuture.get();
            if (result.isSuccessful()) {
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataOutput re = result.getResult();
                if (re != null) {
                    return RpcResultUtil.success(transform.devGetPmDataOutputToApi(re));
                }
            } else {
                return RpcResultUtil.failed(result);
            }
        } catch (Exception e) {
            throw new DeviceOperaFailException(e);
        }
        return RpcResultUtil.failed();
    }


    /**
     * Invoke {@code get-tca} RPC.
     *
     * @param input of {@code get-tca}
     * @return output of {@code get-tca}
     */
    @Override
    public ListenableFuture<RpcResult<GetTcaOutput>> getTca(GetTcaInput input) {
        TcasBuilder tcasBuilder = new TcasBuilder();
        PmPointRef pmPoint = input.getPmPoint();
        PmpObjectType pmPointType = input.getPmPointType();
        PmParameterType pmParameter = input.getPmParameter();
        PmGranularityType pmGranularity = input.getPmGranularity();
        DciPerformanceTransform transform = new DciPerformanceTransform();
        // 单独查一个tca
        if (pmPoint != null && pmPointType != null && pmParameter != null && pmGranularity != null) {
            TcaKey tcaKey = new TcaKey(transform.apiPmGranularityTypeToDev(pmGranularity),
                    transform.apiPmParameterTypeToDev(pmParameter),
                    transform.apiPmPointRefToDev(pmPoint),
                    transform.apiPmpObjectTypeToDev(pmPointType));
            InstanceIdentifier<Tca> iid = create(Performance.class).child(Tcas.class).child(Tca.class, tcaKey);
            Tca tca = MountTools.queryFromOperational(input.getNeId(), iid);
            HashMap<TcaKey, Tca> map = new HashMap<>(2);
            map.put(tcaKey, tca);
            tcasBuilder.setTca(map);
        }
        // 查全部 然后根据条件过滤
        else {
            InstanceIdentifier<Tcas> iid = create(Performance.class).child(Tcas.class);
            Tcas tcas = MountTools.queryFromOperational(input.getNeId(), iid);
            Tcas newTcas = tcaFilter(tcas, pmPoint, pmPointType, pmParameter, pmGranularity);
            if (newTcas != null && newTcas.getTca() != null) {
                tcasBuilder.setTca(newTcas.getTca());
            }
        }
        return RpcResultUtil.success(new DciPerformanceTransform().devGetTcaOutputToApi(tcasBuilder.build()));
    }

    /**
     * 过滤TCA
     *
     * @param tcas              TCA数据
     * @param pmPointRef        tca监测点
     * @param pmpObjectType     监测点类型
     * @param pmParameterType   性能类型
     * @param pmGranularityType 周期
     * @return 过滤后的数据
     */
    private Tcas tcaFilter(Tcas tcas, PmPointRef pmPointRef, PmpObjectType pmpObjectType, PmParameterType pmParameterType, PmGranularityType pmGranularityType) {
        if (tcas == null || tcas.getTca() == null || tcas.getTca().size() == 0) {
            return null;
        }
        @Nullable Map<TcaKey, Tca> tcaMap = tcas.getTca();
        Map<TcaKey, Tca> newTcaMap = new HashMap<>(16);
        DciPerformanceTransform transform = new DciPerformanceTransform();
        tcaMap.forEach((tcaKey, value) -> {
            if (value != null) {
                boolean flag1 = pmPointRef == null || (value.getPmPoint() != null && pmPointRef.equals(transform.devPmPointRefToApi(value.getPmPoint())));
                boolean flag2 = pmpObjectType == null || (value.getPmPointType() != null && pmpObjectType.equals(transform.devPmpObjectTypeToApi(value.getPmPointType())));
                boolean flag3 = pmParameterType == null || (value.getPmParameter() != null && pmParameterType.equals(transform.devPmParameterTypeToApi(value.getPmParameter())));
                boolean flag4 = pmGranularityType == null || (value.getPmPoint() != null && pmGranularityType.equals(transform.devPmGranularityTypeToApi(value.getPmGranularity())));
                if (flag1 && flag2 && flag3 && flag4) {
                    newTcaMap.put(tcaKey, value);
                }
            }
        });
        TcasBuilder tcasBuilder = new TcasBuilder();
        tcasBuilder.setTca(newTcaMap);
        return tcasBuilder.build();
    }


    /**
     * Invoke {@code set-tca} RPC.
     *
     * @param input of {@code set-tca}
     * @return output of {@code set-tca}
     */
    @Override
    public ListenableFuture<RpcResult<SetTcaOutput>> setTca(SetTcaInput input) {
        DciPerformanceTransform t = new DciPerformanceTransform();
        TcaKey tcaKey = new TcaKey(t.apiPmGranularityTypeToDev(input.getPmGranularity()),
                t.apiPmParameterTypeToDev(input.getPmParameter()),
                t.apiPmPointRefToDev(input.getPmPoint()),
                t.apiPmpObjectTypeToDev(input.getPmPointType()));
        InstanceIdentifier<Tca> iid = create(Performance.class).child(Tcas.class).child(Tca.class, tcaKey);
        MountTools.doMergeToConfig(input.getNeId(), iid, t.apiSetTcaInputToDev(input));
        return RpcResultUtil.success();
    }

    /**
     * Invoke {@code set-tca-list} RPC.
     *
     * @param input of {@code set-tca-list}
     * @return output of {@code set-tca-list}
     */
    @Override
    public ListenableFuture<RpcResult<SetTcaListOutput>> setTcaList(SetTcaListInput input) {
        DciPerformanceTransform t = new DciPerformanceTransform();
        InstanceIdentifier<Tcas> iid = create(Performance.class).child(Tcas.class);
        MountTools.doMergeToConfig(input.getNeId(), iid, t.apiSetTcaListInputToDev(input));
        return RpcResultUtil.success();
    }

    /**
     * Invoke {@code get-pm-pmp} RPC.
     *
     * @param input of {@code get-pm-pmp}
     * @return output of {@code get-pm-pmp}
     */
    @Override
    public ListenableFuture<RpcResult<GetPmPmpOutput>> getPmPmp(GetPmPmpInput input) {
        DciPerformanceTransform transform = new DciPerformanceTransform();
        PmpsBuilder pmpsBuilder = new PmpsBuilder();
        // 单查
        if (input.getPmPoint() != null && input.getPmPoint().size() == 1) {
            PmpKey pmpKey = new PmpKey(transform.apiPmPointRefToDev(input.getPmPoint().iterator().next()));
            InstanceIdentifier<Pmp> iid = create(Performance.class).child(Pmps.class).child(Pmp.class, pmpKey);
            Pmp pmp = MountTools.queryFromOperational(input.getNeId(), iid);
            Map<PmpKey, Pmp> map = new HashMap<>(2);
            if (pmp != null) {
                map.put(pmpKey, pmp);
                pmpsBuilder.setPmp(map);
            }
        } else {
            InstanceIdentifier<Pmps> iid = create(Performance.class).child(Pmps.class);
            Pmps pmps = MountTools.queryFromOperational(input.getNeId(), iid);
            if (pmps != null) {
                if (input.getPmPoint() == null) {
                    pmpsBuilder.setPmp(pmps.getPmp());
                } else {
                    @Nullable Map<PmpKey, Pmp> pmpMap = pmps.getPmp();
                    if (pmpMap != null && !pmpMap.isEmpty()) {
                        Map<PmpKey, Pmp> newMap = new HashMap<>(16);
                        pmpMap.keySet().stream().filter(pmpKey -> input.getPmPoint().contains(transform.devPmPointRefToApi(pmpKey.getPmPoint())))
                                .forEach(pmpKey -> newMap.put(pmpKey, pmpMap.get(pmpKey)));
                        pmpsBuilder.setPmp(newMap);
                    }
                }
            }

        }
        return RpcResultUtil.success(new DciPerformanceTransform().devGetPmPmpOutputToApi(pmpsBuilder.build()));
    }

    /**
     * Invoke {@code set-pm-pmp} RPC.
     *
     * @param input of {@code set-pm-pmp}
     * @return output of {@code set-pm-pmp}
     */
    @Override
    public ListenableFuture<RpcResult<SetPmPmpOutput>> setPmPmp(SetPmPmpInput input) {
        DciPerformanceTransform transform = new DciPerformanceTransform();
        PmpKey pmpKey = new PmpKey(transform.apiPmPointRefToDev(input.getPmPoint()));
        InstanceIdentifier<Pmp> iid = create(Performance.class).child(Pmps.class).child(Pmp.class, pmpKey);
        MountTools.doMergeToConfig(input.getNeId(), iid, transform.apiSetPmPmpOutputToDev(input));
        return RpcResultUtil.success();
    }

    /**
     * Invoke {@code set-pm-pmp-list} RPC.
     *
     * @param input of {@code set-pm-pmp-list}
     * @return output of {@code set-pm-pmp-list}
     */
    @Override
    public ListenableFuture<RpcResult<SetPmPmpListOutput>> setPmPmpList(SetPmPmpListInput input) {
        DciPerformanceTransform t = new DciPerformanceTransform();
        InstanceIdentifier<Pmps> iid = create(Performance.class).child(Pmps.class);
        MountTools.doMergeToConfig(input.getNeId(), iid, t.apiSetPmPmpListInputToDev(input));
        return RpcResultUtil.success();
    }

    /**
     * Invoke {@code reset-counter} RPC.
     *
     * @param input of {@code reset-counter}
     * @return output of {@code reset-counter}
     */
    @Override
    public ListenableFuture<RpcResult<ResetCounterOutput>> resetCounter(ResetCounterInput input) {
        OpenconfigRpcService rpcService = MountTools.getRpcService(input.getNeId(), OpenconfigRpcService.class);
        DciPerformanceTransform transform = new DciPerformanceTransform();
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.ResetCounterOutput>> rpcResultListenableFuture
                = rpcService.resetCounter(transform.apiResetCounterInputToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResultListenableFuture, null,
                r -> DciUtils.getOcRpcResult(r.getResult()));
    }
}
