/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import org.apache.commons.collections.CollUtils;
import cn.hutool.core.map.MapUtil;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptOtnPerformanceServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.PerformanceTransformImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.PmStateTransformImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.TcaTransformImpl;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import com.optel.tmaster.dc.general.nc.nccore.NcTools;
import org.apache.commons.lang3.StrUtil;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.performance.rev200428.GetAllPmStateInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.performance.rev200428.GetAllPmStateOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.performance.rev200428.GetAllPmStateOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.performance.rev200428.GetTcaParameterInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.performance.rev200428.GetTcaParameterOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.performance.rev200428.GetTcaParameterOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.performance.rev200428.*;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.performance.rev200428.SetPmStateInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.performance.rev200428.SetPmStateOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.performance.rev200428.get.all.pm.state.output.PmStatesBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.Granularity;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.*;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.TcaParameters;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tca.parameters.TcaParameterBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tca.parameters.TcaParameterKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.*;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.Performance;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.PerformanceKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.pm.states.PmState;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.*;
import java.util.concurrent.Future;

/**
 * ClassName: CtcOptOtnPerformanceServiceImpl
 * <ul>
 * <li>OTN性能管理</li>
 * </ul>
 *
 * @author GHL
 * 2020/4/28 15:37
 */
public class CtcOptOtnPerformanceServiceImpl extends BaseOptOtnPerformanceServiceImpl implements IDeviceServiceOtnCtc {
    @Override
    public ListenableFuture<RpcResult<GetAllCurrentPerformanceMonitoringDataNeOutput>> getAllCurrentPerformanceMonitoringDataNe(GetAllCurrentPerformanceMonitoringDataNeInput input) {

        String objName = input.getObjectName();
        int objType = 0;
        if (input.getObjectType() != null) {
            objType = input.getObjectType().intValue();
        }
        Granularity granularity = input.getGranularity();
        String pmParameter = input.getPmParameterName();
        InstanceIdentifier<Performances> iid = create(Performances.class);
        Performances performances = MountTools.queryFromOperational(input.getNeId(), iid);
        GetAllCurrentPerformanceMonitoringDataNeOutputBuilder getAllCurrentPerformanceMonitoringDataNeOutputBuilder = new GetAllCurrentPerformanceMonitoringDataNeOutputBuilder();
        if (performances != null) {
            Map<PerformanceKey, Performance> performanceList = performancesFilter(performances.getPerformance(), objName, objType, granularity, pmParameter);
            if (MapUtil.isNotEmpty(performanceList)) {
                getAllCurrentPerformanceMonitoringDataNeOutputBuilder.setPerformance(new PerformanceTransformImpl().devPerformanceToApi(performanceList));
            }
        }
        return RpcResultUtil.success(getAllCurrentPerformanceMonitoringDataNeOutputBuilder.build());
    }

    /**
     * 根据条件筛选性能
     *
     * @param performances 全部性能
     * @param objName      对象名称
     * @param granularity  周期
     * @param pmParam      性能参数
     * @return 筛选后的结果
     */
    private Map<PerformanceKey, Performance> performancesFilter(Map<PerformanceKey, Performance> performances, String objName, int objType, Granularity granularity, String pmParam) {
        if (MapUtil.isEmpty(performances)) {
            return performances;
        }
        String granularityStr = "";
        if (granularity != null) {
            granularityStr = granularity.getName();
        }
        if (StrUtil.isEmpty(objName) && StrUtil.isEmpty(granularityStr) && StrUtil.isEmpty(pmParam)) {
            return performances;
        }
        Map<PerformanceKey, Performance> result = new HashMap<>(64);
        for (Performance p : performances.values()) {
            boolean devNameFlag = StrUtil.isEmpty(objName) || objType == 0 || objName.equals(p.getObjectName());

            boolean granularityFlag = StrUtil.isEmpty(granularityStr) || granularityStr.equals(p.getGranularity().getName());
            boolean pmParamFlag = StrUtil.isEmpty(pmParam) || pmParam.equals(p.getPmParameterName());
            if (devNameFlag && granularityFlag && pmParamFlag) {
                result.put(p.key(), p);
            }
        }
        return result;
    }

    @Override
    public ListenableFuture<RpcResult<GetAllCurrentPerformanceMonitoringDataNeByRpcOutput>> getAllCurrentPerformanceMonitoringDataNeByRpc(GetAllCurrentPerformanceMonitoringDataNeByRpcInput input) {
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        GetCurrentPerformanceMonitoringDataInputBuilder getCurrentPerformanceMonitoringDataInputBuilder = new GetCurrentPerformanceMonitoringDataInputBuilder();
        getCurrentPerformanceMonitoringDataInputBuilder.setObjectName(input.getObjectName());
        PerformanceTransformImpl performanceTransform = new PerformanceTransformImpl();
        getCurrentPerformanceMonitoringDataInputBuilder.setGranularity(performanceTransform.apiGranularityToDev(input.getGranularity()));
        getCurrentPerformanceMonitoringDataInputBuilder.setPmParameterName(input.getPmParameterName());
        Future<RpcResult<GetCurrentPerformanceMonitoringDataOutput>> rpcResultFuture = optOtnExtensionService.getCurrentPerformanceMonitoringData(getCurrentPerformanceMonitoringDataInputBuilder.build());
        return RpcResultUtil.buildFutureResult(rpcResultFuture, (rpcResult -> {
            GetAllCurrentPerformanceMonitoringDataNeByRpcOutputBuilder getAllCurrentPerformanceMonitoringDataNeByRpcOutputBuilder = new GetAllCurrentPerformanceMonitoringDataNeByRpcOutputBuilder();
            if (rpcResult != null && rpcResult.getPerformances() != null) {
                getAllCurrentPerformanceMonitoringDataNeByRpcOutputBuilder.setPerformance(performanceTransform.devPerformanceToApi(rpcResult.getPerformances().getPerformance()));
            }
            return getAllCurrentPerformanceMonitoringDataNeByRpcOutputBuilder.build();
        }));
    }

    @Override
    public ListenableFuture<RpcResult<GetHistoryPerformanceMonitoringDataNeOutput>> getHistoryPerformanceMonitoringDataNe(GetHistoryPerformanceMonitoringDataNeInput input) {
        AccPerformanceService accPerformanceService = MountTools.getRpcService(input.getNeId(), AccPerformanceService.class);
        GetHistoryPerformanceMonitoringDataInputBuilder getHistoryPerformanceMonitoringDataInputBuilder = new GetHistoryPerformanceMonitoringDataInputBuilder();
        getHistoryPerformanceMonitoringDataInputBuilder.setObjectName(input.getObjectName());
        getHistoryPerformanceMonitoringDataInputBuilder.setPmParameterName(Collections.singleton(input.getPmParameterName()));
        PerformanceTransformImpl performanceTransform = new PerformanceTransformImpl();
        getHistoryPerformanceMonitoringDataInputBuilder.setGranularity(performanceTransform.apiGranularityToDev(input.getGranularity()));
        getHistoryPerformanceMonitoringDataInputBuilder.setStartTime(input.getStartTime());
        getHistoryPerformanceMonitoringDataInputBuilder.setEndTime(input.getEndTime());
        //获取设备rpc接口返回的值
        Future<RpcResult<GetHistoryPerformanceMonitoringDataOutput>> rpcResultFuture = accPerformanceService.getHistoryPerformanceMonitoringData(getHistoryPerformanceMonitoringDataInputBuilder.build());
        return RpcResultUtil.buildFutureResult(rpcResultFuture, (rpcResult -> {
            /*
             * 将cmcc中的历史性能查询结果转换成opt中的历史性能查询的输出结果类型
             * 1.声明opt上的数据类型builder
             * 2.在该builder中填充数据
             * 3.将该builder填充到opt输出结果的builder中
             * 4.返回opt输出结果builder.build()
             */
            GetHistoryPerformanceMonitoringDataNeOutputBuilder getHistoryPerformanceMonitoringDataNeOutputBuilder = new GetHistoryPerformanceMonitoringDataNeOutputBuilder();
            if (rpcResult != null && rpcResult.getPerformances() != null) {
                getHistoryPerformanceMonitoringDataNeOutputBuilder.setPerformance(performanceTransform.devPerformanceToApi(rpcResult.getPerformances().getPerformance()));
            }
            return getHistoryPerformanceMonitoringDataNeOutputBuilder.build();
        }));
    }

    @Override
    public ListenableFuture<RpcResult<GetTcaParameterOutput>> getTcaParameter(GetTcaParameterInput input) {
        List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.TcaParameter> tcaParameterList = new ArrayList<>();
        TcaTransformImpl tcaTransform = new TcaTransformImpl();
        if (input.getGranularity() != null &&
                !StrUtil.isEmpty(input.getObjectName()) &&
                !StrUtil.isEmpty(input.getPmParameterName()) &&
                input.getThresholdType() != null &&
                input.getObjectType() != null && input.getObjectType().intValue() != 0) {
            InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tca.parameters.TcaParameter> iid = create(TcaParameters.class).child(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tca.parameters.TcaParameter.class, new TcaParameterKey(tcaTransform.apiGranularityToDev(input.getGranularity()),
                    input.getObjectName(), input.getPmParameterName(), tcaTransform.apiThresholdTypeToDev(input.getThresholdType())));
            //从设备查询数据
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tca.parameters.TcaParameter tcaParameter = MountTools.queryFromOperational(input.getNeId(), iid);
            if (tcaParameter != null) {
                tcaParameterList.add(tcaParameter);
            }
        } else {
            InstanceIdentifier<TcaParameters> iid = create(TcaParameters.class);
            TcaParameters tcaParameters = MountTools.queryFromOperational(input.getNeId(), iid);
            if (tcaParameters != null && tcaParameters.getTcaParameter() != null) {
                tcaParameterList.addAll(tcaParameters.getTcaParameter().values());
            }
        }
        GetTcaParameterOutputBuilder getTcaParameterOutputBuilder = new GetTcaParameterOutputBuilder();
        if (CollUtil.isNotEmpty(tcaParameterList)) {
            getTcaParameterOutputBuilder.setTcaParameter(ltm(tcaTransform.devTcaToApiList(tcaParameterFilter(tcaParameterList, input))));
        }
        return RpcResultUtil.success(getTcaParameterOutputBuilder.build());
    }

    /**
     * 对tca 参数进行过滤
     *
     * @param tcaParameterList 查回来的tca 参数
     * @param input            输入参数
     * @return 过滤结果
     */
    private List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.TcaParameter> tcaParameterFilter(List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.TcaParameter> tcaParameterList, GetTcaParameterInput input) {
        String objName = input.getObjectName();
        int objType = 0;
        if (input.getObjectType() != null) {
            objType = input.getObjectType().intValue();
        }
        String granularity = "";
        if (input.getGranularity() != null) {
            granularity = input.getGranularity().getName();
        }
        String pmParam = input.getPmParameterName();
        String threshold = "";
        if (input.getThresholdType() != null) {
            threshold = input.getThresholdType().getName();
        }
        if (StrUtil.isEmpty(objName) && StrUtil.isEmpty(pmParam) && StrUtil.isEmpty(threshold) && StrUtil.isEmpty(granularity)) {
            return tcaParameterList;
        }
        List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.TcaParameter> resList = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.TcaParameter tcaParameter : tcaParameterList) {
            boolean objNameFlag = StrUtil.isEmpty(objName) || objType == 0 || objName.equals(tcaParameter.getObjectName());
            boolean granularityFlag = StrUtil.isEmpty(granularity) || granularity.equals(tcaParameter.getGranularity() == null ? "" : tcaParameter.getGranularity().getName());
            boolean pmParamFlag = StrUtil.isEmpty(pmParam) || pmParam.equals(tcaParameter.getPmParameterName());
            boolean thresholdFlag = StrUtil.isEmpty(threshold) || threshold.equals(tcaParameter.getThresholdType() == null ? "" : tcaParameter.getThresholdType().getName());
            if (objNameFlag && granularityFlag && pmParamFlag && thresholdFlag) {
                resList.add(tcaParameter);
            }
        }
        return resList;
    }

    @Override
    public ListenableFuture<RpcResult<GetTcaParameterByRpcOutput>> getTcaParameterByRpc(GetTcaParameterByRpcInput input) {
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetTcaParameterInputBuilder getTcaParameterInputBuilder = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetTcaParameterInputBuilder();
        TcaTransformImpl tcaTransform = new TcaTransformImpl();
        getTcaParameterInputBuilder.setGranularity(tcaTransform.apiGranularityToDev(input.getGranularity()));
        getTcaParameterInputBuilder.setObjectName(input.getObjectName());
        getTcaParameterInputBuilder.setPmParameterName(input.getPmParameterName());
        Future<RpcResult<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetTcaParameterOutput>> resultFuture = optOtnExtensionService.getTcaParameter(getTcaParameterInputBuilder.build());
        return RpcResultUtil.buildFutureResult(resultFuture, result -> {
            GetTcaParameterByRpcOutputBuilder getTcaParameterByRpcOutputBuilder = new GetTcaParameterByRpcOutputBuilder();
            if (result != null && result.getTcaParameters() != null) {
                getTcaParameterByRpcOutputBuilder.setTcaParameter(ltm(tcaTransform.devTcaExtensionToApiList(result.getTcaParameters().getTcaParameter())));
            }
            return getTcaParameterByRpcOutputBuilder.build();
        });
    }

    @Override
    public ListenableFuture<RpcResult<SetTcaParameterOutput>> setTcaParameter(SetTcaParameterInput input) {
        TcaTransformImpl tcaTransform = new TcaTransformImpl();
        TcaParameterKey tcaParameterKey = new TcaParameterKey(
                tcaTransform.apiGranularityToDev(input.getGranularity()),
                input.getObjectName(),
                input.getPmParameterName(),
                tcaTransform.apiThresholdTypeToDev(input.getThresholdType())
        );
        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tca.parameters.TcaParameter> iid = create(TcaParameters.class).child(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tca.parameters.TcaParameter.class, tcaParameterKey);
        TcaParameterBuilder tcaParameterBuilder = new TcaParameterBuilder();
        tcaParameterBuilder.withKey(tcaParameterKey)
                .setObjectName(input.getObjectName())
                .setGranularity(tcaTransform.apiGranularityToDev(input.getGranularity()))
                .setPmParameterName(input.getPmParameterName())
                .setThresholdType(tcaTransform.apiThresholdTypeToDev(input.getThresholdType()))
                .setThresholdValue(tcaTransform.apiRealToDev(input.getThresholdValue()));
        //下发到设备
        MountTools.doMergeToConfig(input.getNeId(), iid, tcaParameterBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetPmCounterAttrOutput>> getPmCounterAttr(GetPmCounterAttrInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<PmCounterAttr> iid = create(PmCounterAttr.class);
        PmCounterAttr pmCounterAttr = MountTools.queryFromOperational(input.getNeId(), iid);
        GetPmCounterAttrOutputBuilder getPmCounterAttrOutputBuilder = new GetPmCounterAttrOutputBuilder();
        if (pmCounterAttr != null) {
            getPmCounterAttrOutputBuilder.setZeroPmCountEnable(pmCounterAttr.getZeroPmCountEnable());
        }
        return RpcResultUtil.success(getPmCounterAttrOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<SetPmCounterAttrOutput>> setPmCounterAttr(SetPmCounterAttrInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<PmCounterAttr> iid = create(PmCounterAttr.class);
        PmCounterAttrBuilder pmCounterAttrBuilder = new PmCounterAttrBuilder();
        pmCounterAttrBuilder.setZeroPmCountEnable(input.getZeroPmCountEnable());
        MountTools.doMergeToConfig(input.getNeId(), iid, pmCounterAttrBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetAllPmStateOutput>> getAllPmState(GetAllPmStateInput input) {
        AccPerformanceService accPerformanceService = MountTools.getRpcService(input.getNeId(), AccPerformanceService.class);
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.GetAllPmStateInputBuilder getAllPmStateInputBuilder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.GetAllPmStateInputBuilder();
        getAllPmStateInputBuilder.setObjectName(input.getObjectName());
        PmStateTransformImpl pmStateTransform = new PmStateTransformImpl();
        getAllPmStateInputBuilder.setGranularity(pmStateTransform.apiGranularityToDev(input.getGranularity()));
        getAllPmStateInputBuilder.setObjectType(input.getObjectType());
        getAllPmStateInputBuilder.setIncludeSubObj(input.getIncludeSubObj());
        getAllPmStateInputBuilder.setPmParameterName(input.getPmParameterName());
        //获取设备rpc接口返回的值
        Future<RpcResult<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.GetAllPmStateOutput>> rpcResultFuture = accPerformanceService.getAllPmState(getAllPmStateInputBuilder.build());
        return RpcResultUtil.buildFutureResult(rpcResultFuture, (rpcResult -> {
            GetAllPmStateOutputBuilder getAllPmStateOutputBuilder = new GetAllPmStateOutputBuilder();
            if (rpcResult != null && rpcResult.getPmStates() != null) {
                PmStatesBuilder pmStatesBuilder = new PmStatesBuilder();
                Collection<PmState> pmStates = Optional.of(rpcResult)
                        .map(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.GetAllPmStateOutput::getPmStates)
                        .map(PmStates::getPmState)
                        .map(Map::values)
                        .orElse(null);
                pmStatesBuilder.setPmState(ltm(pmStateTransform.devPmStateToApiList(pmStates)));
                getAllPmStateOutputBuilder.setPmStates(pmStatesBuilder.build());
                getAllPmStateOutputBuilder.setReportPolicy(rpcResult.getReportPolicy());
            }
            return getAllPmStateOutputBuilder.build();
        }));
    }

    @Override
    public ListenableFuture<RpcResult<SetPmStateOutput>> setPmState(SetPmStateInput input) {
        AccPerformanceService accPerformanceService = MountTools.getRpcService(input.getNeId(), AccPerformanceService.class);
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.SetPmStateInputBuilder setPmStateInputBuilder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.SetPmStateInputBuilder(new PmStateTransformImpl().apiSetPmStateInputDev(input));
        return RpcResultUtil.buildFutureResult(accPerformanceService.setPmState(setPmStateInputBuilder.build()));
    }
}
