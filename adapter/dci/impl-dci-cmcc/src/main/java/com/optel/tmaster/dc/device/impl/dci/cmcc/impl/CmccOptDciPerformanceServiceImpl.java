/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.impl;

import cn.hutool.core.text.CharSequenceUtil;
import com.google.common.util.concurrent.ListenableFuture;
import cn.hutool.core.map.MapUtil;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciPerformanceServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.performance.PerformanceTransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.util.DciUtils;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.apache.commons.lang3.StrUtil;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.rev220208.get.pm.data.output.grouping.Performance;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.rev220208.get.pm.data.output.grouping.PerformanceKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.types.rev220208.PmRpcType;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.Granularity;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.ObjectType;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.ThresholdType;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.*;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.get.history.performance.monitoring.data.output.Performances;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.performance.pmp.top.Pmps;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.performance.pmp.top.PmpsBuilder;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.performance.pmp.top.pmps.Pmp;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.performance.pmp.top.pmps.PmpKey;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.MiniotnRpcService;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.TcaTop;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tca.top.TcaParameters;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tca.top.TcaParametersBuilder;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tca.top.tca.parameters.TcaParameter;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tca.top.tca.parameters.TcaParameterKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.math.BigDecimal;
import java.util.*;

/**
 * ClassName: CtcOptDciPerformanceServiceImpl
 * <ul>
 * <li>中电信 性能 服务实现类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2022/3/18 10:21
 */
public class CmccOptDciPerformanceServiceImpl extends BaseDciPerformanceServiceImpl implements IDeviceServiceWdmCmcc {
    private final static String IS_OPT_TEST = "isOptTest";
    private final static String FALSE_STR = "false";

    private PerformanceTransform performanceTransform;

    public CmccOptDciPerformanceServiceImpl() {
        this.performanceTransform = new PerformanceTransform();
    }


    /**
     * Invoke {@code get-pm-data} RPC.
     *
     * @param input of {@code get-pm-data}
     * @return output of {@code get-pm-data}
     */
    @Override
    public ListenableFuture<RpcResult<GetPmDataOutput>> getPmData(GetPmDataInput input) {
        PmRpcType pmType = input.getPmType();
        if (PmRpcType.CURRENT.equals(pmType)) {
            return getCurrentPmData(input);
        } else {
            return getHisPmData(input);
        }
    }

    private ListenableFuture<RpcResult<GetPmDataOutput>> getCurrentPmData(GetPmDataInput input) {
        String property = System.getProperty(IS_OPT_TEST, FALSE_STR);
        if (Boolean.parseBoolean(property)) {
            return getCurrentPmDataTest(input);
        } else {
            return getCurrentPmDataN(input);
        }
    }

    /**
     * 查询当前性能 生产环境
     */
    private ListenableFuture<RpcResult<GetPmDataOutput>> getCurrentPmDataN(GetPmDataInput input) {
        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.Performances> iid = create(org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.Performances.class);
        org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.Performances performances = MountTools.queryFromOperational(input.getNeId(), iid);
        MiniotnPerformanceService miniotnPerformanceService = MountTools.getRpcService(input.getNeId(), MiniotnPerformanceService.class);
        GetCurrentPerformanceMonitoringDataInputBuilder getCurrentPerformanceMonitoringDataInput = new GetCurrentPerformanceMonitoringDataInputBuilder();
        PerformanceTransform performanceTransform = new PerformanceTransform();
        getCurrentPerformanceMonitoringDataInput.setObjectName(performanceTransform.apiPmPointRefToDev(input.getPmPoint()));
        getCurrentPerformanceMonitoringDataInput.setPmGranularity(performanceTransform.apiPmGranularityTypeToDev(input.getPmGranularity()));
        getCurrentPerformanceMonitoringDataInput.setPmParameterName(performanceTransform.apiPmParameterTypeToDev(input.getPmParameter()));

        ListenableFuture<RpcResult<GetCurrentPerformanceMonitoringDataOutput>> currentPerformanceMonitoringData = miniotnPerformanceService.getCurrentPerformanceMonitoringData(getCurrentPerformanceMonitoringDataInput.build());

        return RpcResultUtil.buildFutureResult(currentPerformanceMonitoringData, (rpcResult -> {
            // 转化前 miniOTN返回结果包含 周期，转化后不含周期，所以过滤后再转化
            Map<PerformanceKey, Performance> apiPmMap = performanceTransform.devPerformanceMapToApi(performanceFilter(rpcResult.getPerformances().getPerformance(), input));
            GetPmDataOutputBuilder getPmDataOutputBuilder = new GetPmDataOutputBuilder();
            getPmDataOutputBuilder.setPerformance(apiPmMap);
            return   getPmDataOutputBuilder.build();
        }));
    }

    /**
     * 查询当前性能 测试数据
     */
    private ListenableFuture<RpcResult<GetPmDataOutput>> getCurrentPmDataTest(GetPmDataInput input) {
//        TestData testData = new TestData();
//        Map<org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.PerformanceKey,
//                org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.Performance> testMap = testData.getPerformances();
//        GetPmDataOutputBuilder getPmDataOutputBuilder = new GetPmDataOutputBuilder();
//        if (MapUtil.isNotEmpty(testMap)) {
//            PerformanceTransform performanceTransform = new PerformanceTransform();
//            // 转化前 miniOTN返回结果包含 周期，转化后不含周期，所以过滤后再转化
//            Map<PerformanceKey, Performance> apiPmMap = performanceTransform.devPerformanceMapToApi(performanceFilter(testMap, input));
//            getPmDataOutputBuilder.setPerformance(apiPmMap);
//            return RpcResultUtil.success(getPmDataOutputBuilder.build());
//        }
        return null;
    }


    private ListenableFuture<RpcResult<GetPmDataOutput>> getHisPmData(GetPmDataInput input) {
        String property = System.getProperty(IS_OPT_TEST, FALSE_STR);
        if (Boolean.parseBoolean(property)) {
            return getHisPmDataTest(input);
        } else {
            return getHisPmDataN(input);
        }
    }

    private ListenableFuture<RpcResult<GetPmDataOutput>> getHisPmDataN(GetPmDataInput input) {
        MiniotnPerformanceService miniotnPerformanceService = MountTools.getRpcService(input.getNeId(), MiniotnPerformanceService.class);
        GetHistoryPerformanceMonitoringDataInputBuilder getHistoryPerformanceMonitoringDataInputBuilder = new GetHistoryPerformanceMonitoringDataInputBuilder();
        PerformanceTransform performanceTransform = new PerformanceTransform();
        getHistoryPerformanceMonitoringDataInputBuilder.setObjectName(performanceTransform.apiPmPointRefToDev(input.getPmPoint()));
        getHistoryPerformanceMonitoringDataInputBuilder.setPmGranularity(performanceTransform.apiPmGranularityTypeToDev(input.getPmGranularity()));
        getHistoryPerformanceMonitoringDataInputBuilder.setPmParameterName(performanceTransform.apiPmParameterTypeToDev(input.getPmParameter()));
        getHistoryPerformanceMonitoringDataInputBuilder.setCollectStartTime(input.getStartMonDateTime());
        getHistoryPerformanceMonitoringDataInputBuilder.setCollectEndTime(input.getEndMonDateTime());
        //获取设备rpc接口返回的值
        ListenableFuture<RpcResult<GetHistoryPerformanceMonitoringDataOutput>> rpcResultFuture = miniotnPerformanceService.getHistoryPerformanceMonitoringData(getHistoryPerformanceMonitoringDataInputBuilder.build());
        return RpcResultUtil.buildFutureResult(rpcResultFuture, (rpcResult -> {
            /*
             * 将cmcc中的历史性能查询结果转换成opt中的历史性能查询的输出结果类型
             * 1.声明opt上的数据类型builder
             * 2.在该builder中填充数据
             * 3.将该builder填充到opt输出结果的builder中
             * 4.返回opt输出结果builder.build()
             */
            GetPmDataOutputBuilder getPmDataOutputBuilder = new GetPmDataOutputBuilder();
            if (rpcResult != null && rpcResult.getPerformances() != null) {
                Performances performances = rpcResult.getPerformances();
                Map<PerformanceKey, Performance> apiPmMap = performanceTransform.devPerformanceMapToApi(performanceFilter(performances.getPerformance(), input));
                getPmDataOutputBuilder.setPerformance(apiPmMap);
            }
            return getPmDataOutputBuilder.build();
        }));
    }

    private ListenableFuture<RpcResult<GetPmDataOutput>> getHisPmDataTest(GetPmDataInput input) {
//        PerformanceTransform performanceTransform = new PerformanceTransform();
//        TestData testData = new TestData();
//        Map<org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.PerformanceKey,
//                org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.Performance> testMap = testData.getPerformances();
//        GetPmDataOutputBuilder getPmDataOutputBuilder = new GetPmDataOutputBuilder();
//        getPmDataOutputBuilder.setPerformance(performanceTransform.devPerformanceMapToApi(testMap));
//
//        return RpcResultUtil.success(getPmDataOutputBuilder.build()
//        );
        return null;
    }


    /**
     * 根据输入参数过滤
     *
     * @param performanceMap OPT返回查询全部结果
     * @param input          输入
     * @return 过滤结果
     */
    private Map<org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.PerformanceKey,
            org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.Performance> performanceFilter(Map<org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.PerformanceKey,
            org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.Performance> performanceMap,
                                                                                                                          GetPmDataInput input) {
        if (MapUtil.isEmpty(performanceMap)) {
            return MapUtil.emptyMap();
        }
        PerformanceTransform performanceTransform = new PerformanceTransform();
        String objectName = performanceTransform.apiPmPointRefToDev(input.getPmPoint());
        Granularity granularity = performanceTransform.apiPmGranularityTypeToDev(input.getPmGranularity());
        PmParameterType pmParameterType = performanceTransform.apiPmParameterTypeToDev(input.getPmParameter());
        ObjectType objectType = performanceTransform.apiPmObjectTypeToDev(input.getPmPointType());

        Map<org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.PerformanceKey,
                org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.Performance> resMap = new HashMap<>(performanceMap.size());
        for (Map.Entry<org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.PerformanceKey,
                org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.Performance> entry : performanceMap.entrySet()) {
            boolean flag1 = false;
            boolean flag2 = false;
            boolean flag3 = false;
            boolean flag4 = false;
            org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.PerformanceKey key = entry.getKey();
            org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.Performance value = entry.getValue();
            if (PmParameterType.UNKNOWN.equals(key.getPmParameterName())) {
                continue;
            }
            if (StrUtil.isEmpty(objectName) || objectName.equals(key.getObjectName()) || "ALL".equalsIgnoreCase(objectName)) {
                flag1 = true;
            }
            if (pmParameterType == null || pmParameterType.equals(key.getPmParameterName()) || pmParameterType.equals(PmParameterType.ALL)) {
                flag2 = true;
            }
            if (granularity == null || granularity.equals(key.getPmGranularity())) {
                flag3 = true;
            }
            if (objectType == null || objectType.equals(value.getObjectType())) {
                flag4 = true;
            }
            if (flag1 && flag2 && flag3 && flag4) {
                resMap.put(key, value);
            }
        }
        return resMap;
    }

    /**
     * Invoke {@code get-tca} RPC.
     *
     * @param input of {@code get-tca}
     * @return output of {@code get-tca}
     */
    @Override
    public ListenableFuture<RpcResult<GetTcaOutput>> getTca(GetTcaInput input) {
        String property = System.getProperty(IS_OPT_TEST, FALSE_STR);
        if (Boolean.parseBoolean(property)) {
            return getTcaTest(input);
        } else {
            return getTcaN(input);
        }
    }

    private ListenableFuture<RpcResult<GetTcaOutput>> getTcaN(GetTcaInput input) {
        GetTcaOutputBuilder getTcaOutputBuilder = new GetTcaOutputBuilder();
        PerformanceTransform performanceTransform = new PerformanceTransform();
        String objectName = performanceTransform.apiPmPointRefToDev(input.getPmPoint());
        Granularity granularity = performanceTransform.apiPmGranularityTypeToDev(input.getPmGranularity());
        PmParameterType pmParameterType = performanceTransform.apiPmParameterTypeToDev(input.getPmParameter());
        ObjectType objectType = performanceTransform.apiPmObjectTypeToDev(input.getPmPointType());
        // 单独查一个tca
        if (CharSequenceUtil.isNotBlank(objectName) && granularity != null && pmParameterType != null && objectType != null) {
            // 没有传入高低门限，这里分别查询两次
            TcaParameterKey tcaParameterKey1 = new TcaParameterKey(granularity, objectName, objectType, pmParameterType, ThresholdType.High);
            TcaParameterKey tcaParameterKey2 = new TcaParameterKey(granularity, objectName, objectType, pmParameterType, ThresholdType.Low);
            InstanceIdentifier<TcaParameter> iid1 = create(TcaTop.class).child(TcaParameters.class).child(TcaParameter.class, tcaParameterKey1);
            TcaParameter tcaParameter1 = MountTools.queryFromOperational(input.getNeId(), iid1);
            InstanceIdentifier<TcaParameter> iid2 = create(TcaTop.class).child(TcaParameters.class).child(TcaParameter.class, tcaParameterKey2);
            TcaParameter tcaParameter2 = MountTools.queryFromOperational(input.getNeId(), iid2);
            HashMap<TcaParameterKey, TcaParameter> map = new HashMap<>(2);
            if (tcaParameter1 != null) {
                map.put(tcaParameterKey1, tcaParameter1);
            }
            if (tcaParameter2 != null) {
                map.put(tcaParameterKey2, tcaParameter2);
            }
            removeInvalidTca(map);
            if (MapUtil.isNotEmpty(map)) {
                getTcaOutputBuilder.setTca(performanceTransform.devTcaParameterMapToApi(map));
            }
        }
        // 查全部 然后根据条件过滤
        else {
            InstanceIdentifier<TcaParameters> iid = create(TcaTop.class).child(TcaParameters.class);
            TcaParameters tcaParameters = MountTools.queryFromOperational(input.getNeId(), iid);
            TcaParameters newTcas = tcaFilter(tcaParameters, objectName, objectType, pmParameterType, granularity);
            if (newTcas != null && newTcas.getTcaParameter() != null) {
                getTcaOutputBuilder.setTca(performanceTransform.devTcaParameterMapToApi(newTcas.getTcaParameter()));
            }
        }
        return RpcResultUtil.success(getTcaOutputBuilder.build());
    }

    private void removeInvalidTca(HashMap<TcaParameterKey, TcaParameter> map) {
        map.entrySet().removeIf(entry -> PmParameterType.UNKNOWN.equals(entry.getValue().getPmParameterName()));
    }

    private ListenableFuture<RpcResult<GetTcaOutput>> getTcaTest(GetTcaInput input) {
//        GetTcaOutputBuilder getTcaOutputBuilder = new GetTcaOutputBuilder();
//        PerformanceTransform performanceTransform = new PerformanceTransform();
//        String objectName = performanceTransform.apiPmPointRefToDev(input.getPmPoint());
//        Granularity granularity = performanceTransform.apiPmGranularityTypeToDev(input.getPmGranularity());
//        PmParameterType pmParameterType = performanceTransform.apiPmParameterTypeToDev(input.getPmParameter());
//        ObjectType objectType = performanceTransform.apiPmObjectTypeToDev(input.getPmPointType());
//        Map<TcaParameterKey, TcaParameter> tcaParameters = new TestData().getTcaParameters();
//        TcaParametersBuilder tcaParametersBuilder = new TcaParametersBuilder();
//        tcaParametersBuilder.setTcaParameter(tcaParameters);
//        TcaParameters newTcas = tcaFilter(tcaParametersBuilder.build(), objectName, objectType, pmParameterType, granularity);
//        getTcaOutputBuilder.setTca(performanceTransform.devTcaParameterMapToApi(newTcas.getTcaParameter()));
        return RpcResultUtil.success(null);
    }

    /**
     * 过滤TCA
     *
     * @param tcaParameters   TCA数据
     * @param objectName      tca监测点
     * @param objectType      监测点类型
     * @param pmParameterType 性能类型
     * @param granularity     周期
     * @return 过滤后的数据
     */
    private TcaParameters tcaFilter(TcaParameters tcaParameters, String objectName, ObjectType objectType, PmParameterType pmParameterType, Granularity granularity) {
        if (tcaParameters == null) {
            return null;
        }
        Map<TcaParameterKey, TcaParameter> tcaMap = tcaParameters.getTcaParameter();
        if (MapUtil.isEmpty(tcaMap)) {
            return null;
        }
        Map<TcaParameterKey, TcaParameter> newTcaMap = new HashMap<>(tcaMap.size());

        for (Map.Entry<TcaParameterKey, TcaParameter> entry : tcaMap.entrySet()) {
            TcaParameterKey tcaKey = entry.getKey();
            TcaParameter tca = entry.getValue();
            if (tca != null) {
                if (PmParameterType.UNKNOWN.equals(tca.getPmParameterName())) {
                    continue;
                }
                boolean flag1 = StrUtil.isEmpty(objectName) || (tca.getObjectName() != null && objectName.equalsIgnoreCase(tca.getObjectName()));
                boolean flag2 = objectType == null || (tca.getObjectType() != null && objectType.equals(tca.getObjectType()));
                boolean flag3 = pmParameterType == null || (tca.getPmParameterName() != null && pmParameterType.equals(tca.getPmParameterName()));
                boolean flag4 = granularity == null || (tca.getGranularity() != null && granularity.equals(tca.getGranularity()));
                if (flag1 && flag2 && flag3 && flag4) {
                    newTcaMap.put(tcaKey, tca);
                }
            }
        }
        TcaParametersBuilder tcasBuilder = new TcaParametersBuilder();
        tcasBuilder.setTcaParameter(newTcaMap);
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
        PerformanceTransform t = new PerformanceTransform();
        // 修改高门限
        BigDecimal thresholdValueHigh = input.getThresholdValueHigh().decimalValue();
        setTcaByType(input, t, thresholdValueHigh, ThresholdType.High);
        // 修改低门限（部分门限只有高门限，判断一下是否有低门限修改参数）
        if (null != input.getThresholdValueLow()) {
            BigDecimal thresholdValueLow = input.getThresholdValueLow().decimalValue();
            setTcaByType(input, t, thresholdValueLow, ThresholdType.Low);
        }
        return RpcResultUtil.success();
    }

    private void setTcaByType(SetTcaInput input, PerformanceTransform t, BigDecimal thresholdValue, ThresholdType highOrLow) {
        if (thresholdValue != null) {
            TcaParameterKey tcaParameterKey = new TcaParameterKey(t.apiPmGranularityTypeToDev(input.getPmGranularity()),
                    t.apiPmPointRefToDev(input.getPmPoint()),
                    t.apiPmObjectTypeToDev(input.getPmPointType()),
                    t.apiPmParameterTypeToDev(input.getPmParameter()), highOrLow);
            InstanceIdentifier<TcaParameter> iid = create(TcaTop.class).child(TcaParameters.class).child(TcaParameter.class, tcaParameterKey);
            MountTools.doMergeToConfig(input.getNeId(), iid, t.apiSetTcaInputToDev(input, highOrLow));
        }
    }

    /**
     * Invoke {@code set-tca-list} RPC.
     *
     * @param input of {@code set-tca-list}
     * @return output of {@code set-tca-list}
     */
    @Override
    public ListenableFuture<RpcResult<SetTcaListOutput>> setTcaList(SetTcaListInput input) {
        PerformanceTransform t = new PerformanceTransform();
        InstanceIdentifier<TcaParameters> iid = create(TcaTop.class).child(TcaParameters.class);
        MountTools.doMergeToConfig(input.getNeId(), iid, t.apiSetTcaListInputToDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetPmPmpOutput>> getPmPmp(GetPmPmpInput input) {
        String property = System.getProperty(IS_OPT_TEST, FALSE_STR);
        if (Boolean.parseBoolean(property)) {
            return getPmPmpTest(input);
        } else {
            return getPmPmpN(input);
        }
    }

    private ListenableFuture<RpcResult<GetPmPmpOutput>> getPmPmpTest(GetPmPmpInput input) {
//        TestData testData = new TestData();
//        Map<PmpKey, Pmp> pmp1 = testData.getPmp();
//        PerformanceTransform transform = new PerformanceTransform();
//        PmpsBuilder pmpsBuilder =new PmpsBuilder();
//        // 单查
//        if(input.getPmPoint() != null && input.getPmPoint().size() == 1){
//            PmpKey pmpKey = new PmpKey(Granularity._15min,transform.apiPmPointRefToDev(input.getPmPoint().iterator().next()));
//            Pmp pmp = pmp1.get(pmpKey);
//            Map<PmpKey,Pmp> map = new HashMap<>(2);
//            if(pmp != null){
//                map.put(pmpKey,pmp);
//                pmpsBuilder.setPmp(map);
//            }
//            PmpKey pmpKey2 = new PmpKey(Granularity._24h,transform.apiPmPointRefToDev(input.getPmPoint().iterator().next()));
//            Pmp pmp2 = pmp1.get(pmpKey2);
//            if(pmp2 != null){
//                map.put(pmpKey2,pmp2);
//                pmpsBuilder.setPmp(map);
//            }
//        }else {
//            PmpsBuilder pmpsBuilder1 = new PmpsBuilder();
//            pmpsBuilder1.setPmp(pmp1);
//            Pmps pmps = pmpsBuilder1.build();
//            if(pmps != null){
//                if(input.getPmPoint()==null){
//                    pmpsBuilder.setPmp(pmps.getPmp());
//                }else{
//                    Map<PmpKey, Pmp> pmpMap = pmps.getPmp();
//                    if(pmpMap != null && !pmpMap.isEmpty()){
//                        Map<PmpKey,Pmp> newMap = new HashMap<>(16);
//                        pmpMap.keySet().stream().filter(pmpKey -> input.getPmPoint().contains(transform.devPmPointRefToApi(pmpKey.getPmPoint())))
//                                .forEach(pmpKey -> newMap.put(pmpKey,pmpMap.get(pmpKey)));
//                        pmpsBuilder.setPmp(newMap);
//                    }
//                }
//            }
//        }
//        return RpcResultUtil.success(transform.devGetPmPmpOutputToApi(pmpsBuilder.build()));
        return null;
    }

    /**
     * Invoke {@code get-pm-pmp} RPC.
     *
     * @param input of {@code get-pm-pmp}
     * @return output of {@code get-pm-pmp}
     */
    private ListenableFuture<RpcResult<GetPmPmpOutput>> getPmPmpN(GetPmPmpInput input) {
        PerformanceTransform transform = new PerformanceTransform();
        PmpsBuilder pmpsBuilder = new PmpsBuilder();
        // 单查
        if (input.getPmPoint() != null && input.getPmPoint().size() == 1) {
            PmpKey pmpKey = new PmpKey(Granularity._15min, transform.apiPmPointRefToDev(input.getPmPoint().iterator().next()));
            InstanceIdentifier<Pmp> iid = create(org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.Performances.class).child(Pmps.class).child(Pmp.class, pmpKey);
            Pmp pmp = MountTools.queryFromOperational(input.getNeId(), iid);
            Map<PmpKey, Pmp> map = new HashMap<>(2);
            if (pmp != null) {
                map.put(pmpKey, pmp);
                pmpsBuilder.setPmp(map);
            }
            PmpKey pmpKey2 = new PmpKey(Granularity._24h, transform.apiPmPointRefToDev(input.getPmPoint().iterator().next()));
            Pmp pmp2 = MountTools.queryFromOperational(input.getNeId(), iid);
            if (pmp2 != null) {
                map.put(pmpKey2, pmp2);
                pmpsBuilder.setPmp(map);
            }
        } else {
            InstanceIdentifier<Pmps> iid = create(org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.Performances.class).child(Pmps.class);
            Pmps pmps = MountTools.queryFromOperational(input.getNeId(), iid);
            if (pmps != null) {
                if (input.getPmPoint() == null) {
                    pmpsBuilder.setPmp(pmps.getPmp());
                } else {
                    Map<PmpKey, Pmp> pmpMap = pmps.getPmp();
                    if (pmpMap != null && !pmpMap.isEmpty()) {
                        Map<PmpKey, Pmp> newMap = new HashMap<>(16);
                        pmpMap.keySet().stream().filter(pmpKey -> input.getPmPoint().contains(transform.devPmPointRefToApi(pmpKey.getPmPoint())))
                                .forEach(pmpKey -> newMap.put(pmpKey, pmpMap.get(pmpKey)));
                        pmpsBuilder.setPmp(newMap);
                    }
                }
            }
        }
        return RpcResultUtil.success(transform.devGetPmPmpOutputToApi(pmpsBuilder.build()));
    }

    /**
     * Invoke {@code set-pm-pmp} RPC.
     *
     * @param input of {@code set-pm-pmp}
     * @return output of {@code set-pm-pmp}
     */
    @Override
    public ListenableFuture<RpcResult<SetPmPmpOutput>> setPmPmp(SetPmPmpInput input) {
        PerformanceTransform transform = new PerformanceTransform();
        PmpKey pmpKey = new PmpKey(transform.apiPmGranularityTypeToDev(input.getPmGranularity()), transform.apiPmPointRefToDev(input.getPmPoint()));
        InstanceIdentifier<Pmp> iid = create(org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.Performances.class).child(Pmps.class).child(Pmp.class, pmpKey);
        MountTools.doMergeToConfig(input.getNeId(), iid, transform.apiSetPmPmpInputToDev(input));
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
        PerformanceTransform t = new PerformanceTransform();
        InstanceIdentifier<Pmps> iid = create(org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.Performances.class).child(Pmps.class);
        MountTools.doMergeToConfig(input.getNeId(), iid, t.apiSetPmpListInputToDev(input));
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
        MiniotnRpcService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnRpcService.class);
        org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.ResetCounterInput resetCounterInput = performanceTransform.apiResetCounterInputToDev(input);
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.ResetCounterOutput>> rpcResultListenableFuture =
                rpcService.resetCounter(resetCounterInput);

        return RpcResultUtil.buildFutureResult(rpcResultListenableFuture,null,
                r-> DciUtils.getOcRpcResult(r.getResult()));
    }
}
