/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.performance;

import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base.PerformanceTypeTransform;

/**
 * ClassName: PerformanceServiceTransform
 * <ul>
 * <li>性能转换类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2022/3/18 10:23
 */
public class DciPerformanceTransform implements PerformanceTypeTransform {
//待确定
//    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataInput apiGetPmDataInputToDev(GetPmDataInput input){
//        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataInputBuilder getPmDataInputBuilder
//                = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataInputBuilder();
//        getPmDataInputBuilder.setEndMonDateTime(input.getEndMonDateTime());
//        getPmDataInputBuilder.setHistoryDataType(apiHistoryDataTypeToDev(input.getHistoryDataType()));
//        getPmDataInputBuilder.setNumberOfRecords(input.getNumberOfRecords());
//        getPmDataInputBuilder.setPmGranularity(apiPmGranularityTypeToDev(input.getPmGranularity()));
//        getPmDataInputBuilder.setPmParameter(apiPmParameterTypeToDev(input.getPmParameter()));
//        if(!"ALL".equals(input.getPmPoint().getValue())){
//            //需要改为ALL下发设备 然后将设备数据进行过滤
//            getPmDataInputBuilder.setPmPoint(
//                    new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmPointRef("ALL"));
//            getPmDataInputBuilder.setPmPointType(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType.ALL);
//        }else{
//            getPmDataInputBuilder.setPmPoint(apiPmPointRefToDev(input.getPmPoint()));
//            getPmDataInputBuilder.setPmPointType(apiPmpObjectTypeToDev(input.getPmPointType()));
//        }
//        getPmDataInputBuilder.setPmType(apiPmRpcTypeToDev(input.getPmType()));
//        getPmDataInputBuilder.setStartMonDateTime(input.getStartMonDateTime());
//        getPmDataInputBuilder.setValueScope(apiPmValueScopeToDev(input.getValueScope()));
//        return getPmDataInputBuilder.build();
//    }
//
//    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataOutput filterPmData(
//            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataOutput getPmDataOutput,String pmPoint){
//        if("ALL".equals(pmPoint)){
//            return getPmDataOutput;
//        }
//        @Nullable Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.PerformanceKey
//        , org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.Performance> devPerfMap =
//                getPmDataOutput.getPerformance();
//        if(getPmDataOutput==null || devPerfMap == null || devPerfMap.isEmpty()){
//            return getPmDataOutput;
//        }
//        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataOutputBuilder builder
//                = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataOutputBuilder();
//        Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.PerformanceKey
//        , org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.Performance> map
//                = new HashMap<>(devPerfMap.size());
//
//        for(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.PerformanceKey performanceKey:devPerfMap.keySet()){
//            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.Performance performance = devPerfMap.get(performanceKey);
//            if(pmPoint.equals(performance.getPmPoint().getValue())){
//                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.PerformanceKey key =
//                        new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output
//                                .PerformanceKey(performance.getMonitoringDateTime(),performance.getPmParameter(),performance.getPmPoint());
//                map.put(key,performance);
//            }
//        }
//        builder.setPerformance(map);
//        return builder.build();
//    }
//
//    public GetPmDataOutput devGetPmDataOutputToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataOutput getPmDataOutput){
//        GetPmDataOutputBuilder getPmDataOutputBuilder = new GetPmDataOutputBuilder();
//        if(getPmDataOutput!=null){
//            getPmDataOutputBuilder.setNumberOfBin(getPmDataOutput.getNumberOfBin());
//            @Nullable Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.PerformanceKey
//            ,org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.Performance> devPerfMap =
//                    getPmDataOutput.getPerformance();
//            Map<PerformanceKey, Performance>apiPerfMap = new HashMap<>(16);
//            if(devPerfMap != null && !devPerfMap.isEmpty()){
//                for(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.PerformanceKey performanceKey:devPerfMap.keySet()){
//                    PmPointRef pmPoint = devPmPointRefToApi(performanceKey.getPmPoint());
//                    PmParameterType pmParameter = devPmParameterTypeToApi(performanceKey.getPmParameter());
//                    DateAndTime monitoringDateTime = performanceKey.getMonitoringDateTime();
//                    PerformanceKey apiPerformanceKey = new PerformanceKey(monitoringDateTime,pmParameter,pmPoint);
//                    PerformanceBuilder performanceBuilder = new PerformanceBuilder();
//                    org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.Performance performance = devPerfMap.get(performanceKey);
//                    if(performance != null){
//                        performanceBuilder.setMonitoringDateTime(performance.getMonitoringDateTime());
//                        performanceBuilder.setPmDataValue(devPmDataValueToApi(performance.getPmDataValue()));
//                        performanceBuilder.setPmParameter(devPmParameterTypeToApi(performance.getPmParameter()));
//                        performanceBuilder.setPmPoint(devPmPointRefToApi(performance.getPmPoint()));
//                    }
//                    apiPerfMap.put(apiPerformanceKey,performanceBuilder.build());
//                }
//            }
//            getPmDataOutputBuilder.setPerformance(apiPerfMap);
//        }
//        return getPmDataOutputBuilder.build();
//    }
//
//    public GetTcaOutput devGetTcaOutputToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.Tcas tcas){
//        GetTcaOutputBuilder getTcaOutputBuilder = new GetTcaOutputBuilder();
//        Map<TcaKey, Tca> apiTcaMap = new HashMap<>(16);
//        @Nullable Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaKey,
//                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.Tca> tcaMap = tcas.getTca();
//        if(tcaMap != null && !tcaMap.isEmpty()){
//            for(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaKey tcaKey:tcaMap.keySet()){
//                TcaBuilder tcaBuilder = new TcaBuilder();
//                PmGranularityType pmGranularityType = devPmGranularityTypeToApi(tcaKey.getPmGranularity());
//                PmParameterType pmParameterType = devPmParameterTypeToApi(tcaKey.getPmParameter());
//                PmPointRef pmPointRef = devPmPointRefToApi(tcaKey.getPmPoint());
//                PmpObjectType pmpObjectType = devPmpObjectTypeToApi(tcaKey.getPmPointType());
//                TcaKey apiTcaKey = new TcaKey(pmGranularityType, pmParameterType, pmPointRef, pmpObjectType);
//                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.Tca tca = tcaMap.get(tcaKey);
//                if(tca != null){
//                    tcaBuilder.setPmGranularity(devPmGranularityTypeToApi(tca.getPmGranularity()));
//                    tcaBuilder.setPmParameter(devPmParameterTypeToApi(tca.getPmParameter()));
//                    tcaBuilder.setPmPoint(devPmPointRefToApi(tca.getPmPoint()));
//                    tcaBuilder.setPmPointType(devPmpObjectTypeToApi(tca.getPmPointType()));
//                    tcaBuilder.setThresholdValueHigh(tca.getThresholdValueHigh());
//                    tcaBuilder.setThresholdValueLow(tca.getThresholdValueLow());
//                }
//                apiTcaMap.put(apiTcaKey,tcaBuilder.build());
//            }
//        }
//        getTcaOutputBuilder.setTca(apiTcaMap);
//        return getTcaOutputBuilder.build();
//    }
//
//    public Tcas apiSetTcaListInputToDev(SetTcaListInput input){
//        TcasBuilder tcasBuilder = new TcasBuilder();
//
//        Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaKey,
//                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.Tca> values = new HashMap<>();
//        if(input != null && input.getTca() != null && !input.getTca().isEmpty()){
//            @Nullable Map<TcaKey, Tca> tcas = input.getTca();
//            for (TcaKey e : tcas.keySet()) {
//                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaBuilder
//                        tcaBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaBuilder();
//                Tca tca = tcas.get(e);
//                tcaBuilder.setPmGranularity(apiPmGranularityTypeToDev(tca.getPmGranularity()));
//                tcaBuilder.setPmParameter(apiPmParameterTypeToDev(tca.getPmParameter()));
//                tcaBuilder.setPmPoint(apiPmPointRefToDev(tca.getPmPoint()));
//                tcaBuilder.setPmPointType(apiPmpObjectTypeToDev(tca.getPmPointType()));
//                tcaBuilder.setThresholdValueHigh(tca.getThresholdValueHigh());
//                tcaBuilder.setThresholdValueLow(tca.getThresholdValueLow());
//                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaKey
//                        tcaKey = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaKey(
//                        apiPmGranularityTypeToDev(e.getPmGranularity())  ,
//                        apiPmParameterTypeToDev(e.getPmParameter()),
//                        apiPmPointRefToDev(e.getPmPoint()),
//                        apiPmpObjectTypeToDev(e.getPmPointType())
//                );
//                values.put(tcaKey,tcaBuilder.build());
//            }
//            tcasBuilder.setTca(values);
//        }
//        return tcasBuilder.build();
//    }
//
//    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.Tca apiSetTcaInputToDev(SetTcaInput input){
//        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaBuilder tcaBuilder
//                = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaBuilder();
//        if(input != null){
//            tcaBuilder.setPmGranularity(apiPmGranularityTypeToDev(input.getPmGranularity()));
//            tcaBuilder.setPmParameter(apiPmParameterTypeToDev(input.getPmParameter()));
//            tcaBuilder.setPmPoint(apiPmPointRefToDev(input.getPmPoint()));
//            tcaBuilder.setPmPointType(apiPmpObjectTypeToDev(input.getPmPointType()));
//            tcaBuilder.setThresholdValueHigh(input.getThresholdValueHigh());
//            tcaBuilder.setThresholdValueLow(input.getThresholdValueLow());
//        }
//        return tcaBuilder.build();
//    }
//
//    public GetPmPmpOutput devGetPmPmpOutputToApi(Pmps pmps){
//        GetPmPmpOutputBuilder getPmPmpOutputBuilder = new GetPmPmpOutputBuilder();
//        Map<PmpKey, Pmp> apiMap = new HashMap<>(16);
//        @Nullable Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpKey,
//        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.Pmp> pmpMap = pmps.getPmp();
//        if(pmpMap != null  && !pmpMap.isEmpty()){
//            for(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpKey pmpKey:pmpMap.keySet()){
//                PmpBuilder pmpBuilder = new PmpBuilder();
//                PmpKey apiPmpKey = new PmpKey(devPmPointRefToApi(pmpKey.getPmPoint()));
//                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.Pmp pmp = pmpMap.get(pmpKey);
//                if(pmp != null){
//                    pmpBuilder.setPmPoint(devPmPointRefToApi(pmp.getPmPoint()));
//                    pmpBuilder.setPmPointEnable(pmp.getPmPointEnable());
//                }
//                apiMap.put(apiPmpKey,pmpBuilder.build());
//            }
//        }
//        getPmPmpOutputBuilder.setPmp(apiMap);
//        return getPmPmpOutputBuilder.build();
//    }
//
//    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.Pmp apiSetPmPmpOutputToDev (SetPmPmpInput setPmPmpInput){
//        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpBuilder pmpBuilder
//                = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpBuilder();
//        if(setPmPmpInput != null){
//            pmpBuilder.setPmPoint(apiPmPointRefToDev(setPmPmpInput.getPmPoint()));
//            pmpBuilder.setPmPointEnable(setPmPmpInput.getPmPointEnable());
//        }
//        return pmpBuilder.build();
//    }
//
//    public Pmps apiSetTcaListInputToDev(SetPmPmpListInput input){
//        PmpsBuilder pmpsBuilder = new PmpsBuilder();
//        Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpKey,
//                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.Pmp> values = new HashMap<>(16);
//       if(input != null && input.getPmp() != null && !input.getPmp().isEmpty()){
//           @Nullable Map<PmpKey, Pmp> pmpMap = input.getPmp();
//           for (PmpKey e : pmpMap.keySet()) {
//               org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpBuilder pmpBuilder
//                       =new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpBuilder();
//                Pmp pmp = pmpMap.get(e);
//                pmpBuilder.setPmPoint(apiPmPointRefToDev(pmp.getPmPoint()));
//                pmpBuilder.setPmPointEnable(pmp.getPmPointEnable());
//               org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpKey
//                       pmpKey = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpKey(
//                               apiPmPointRefToDev(e.getPmPoint()));
//                values.put(pmpKey,pmpBuilder.build());
//            }
//            pmpsBuilder.setPmp(values);
//        }
//        return pmpsBuilder.build();
//    }
//
//    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.ResetCounterInput apiResetCounterInputToDev(ResetCounterInput input){
//        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.ResetCounterInputBuilder resetCounterInputBuilder
//                = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.ResetCounterInputBuilder();
//        if(input != null){
//            resetCounterInputBuilder.setPmPoint(apiPmPointRefToDev(input.getPmPoint()));
//            resetCounterInputBuilder.setPmPointType(apiPmpObjectTypeToDev(input.getPmPointType()));
//        }
//        return resetCounterInputBuilder.build();
//    }

}
