/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.transform.performance;

import com.optel.tmaster.dc.device.impl.dci.ctc.transform.base.*;
import org.eclipse.jdt.annotation.*;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.rev220208.get.pm.data.output.grouping.*;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.rev220208.performance.pmp.top.*;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.rev220208.performance.tca.top.*;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.types.rev220208.*;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.*;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.*;

import java.util.*;

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

    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataInput apiGetPmDataInputToDev(GetPmDataInput input) {
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataInputBuilder getPmDataInputBuilder
                = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataInputBuilder();
        getPmDataInputBuilder.setEndMonDateTime(input.getEndMonDateTime());
        getPmDataInputBuilder.setHistoryDataType(apiHistoryDataTypeToDev(input.getHistoryDataType()));
        getPmDataInputBuilder.setNumberOfRecords(input.getNumberOfRecords());
        getPmDataInputBuilder.setPmGranularity(apiPmGranularityTypeToDev(input.getPmGranularity()));
        // 2023 0728 pmPoint由原下发ALL修改为 正常下发
        getPmDataInputBuilder.setPmParameter(apiPmParameterTypeToDev(input.getPmParameter()));
        getPmDataInputBuilder.setPmPoint(apiPmPointRefToDev(input.getPmPoint()));
        getPmDataInputBuilder.setPmPointType(apiPmpObjectTypeToDev(input.getPmPointType()));
        getPmDataInputBuilder.setPmType(apiPmRpcTypeToDev(input.getPmType()));
        getPmDataInputBuilder.setStartMonDateTime(input.getStartMonDateTime());
        getPmDataInputBuilder.setValueScope(apiPmValueScopeToDev(input.getValueScope()));
        return getPmDataInputBuilder.build();
    }

    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataOutput filterPmData(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataOutput getPmDataOutput, String pmPoint) {
        if ("ALL".equals(pmPoint)) {
            return getPmDataOutput;
        }
        @Nullable Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.PerformanceKey
                , org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.Performance> devPerfMap =
                getPmDataOutput.getPerformance();
        if (devPerfMap == null || devPerfMap.isEmpty()) {
            return getPmDataOutput;
        }
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataOutputBuilder builder
                = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataOutputBuilder();
        Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.PerformanceKey
                , org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.Performance> map
                = new HashMap<>(devPerfMap.size());

        for (Map.Entry<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.PerformanceKey, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.Performance> entry : devPerfMap.entrySet()) {
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.Performance value = entry.getValue();
            if (pmPoint.equals(value.getPmPoint().getValue())) {
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.PerformanceKey key =
                        new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output
                                .PerformanceKey(value.getMonitoringDateTime(), value.getPmParameter(), value.getPmPoint());
                map.put(key, value);
            }
        }
        builder.setPerformance(map);
        return builder.build();
    }

    public GetPmDataOutput devGetPmDataOutputToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.GetPmDataOutput getPmDataOutput) {
        GetPmDataOutputBuilder getPmDataOutputBuilder = new GetPmDataOutputBuilder();
        if (getPmDataOutput != null) {
            getPmDataOutputBuilder.setNumberOfBin(getPmDataOutput.getNumberOfBin());
            @Nullable Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.PerformanceKey
                    , org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.Performance> devPerfMap =
                    getPmDataOutput.getPerformance();
            Map<PerformanceKey, Performance> apiPerfMap = new HashMap<>(16);
            if (devPerfMap != null && !devPerfMap.isEmpty()) {
                devPerfMap.forEach((key, value) -> {
                    PmPointRef pmPoint = devPmPointRefToApi(key.getPmPoint());
                    PmParameterType pmParameter = devPmParameterTypeToApi(key.getPmParameter());
                    DateAndTime monitoringDateTime = key.getMonitoringDateTime();
                    PerformanceKey apiPerformanceKey = new PerformanceKey(monitoringDateTime, pmParameter, pmPoint);
                    PerformanceBuilder performanceBuilder = new PerformanceBuilder();
                    if (value != null) {
                        performanceBuilder.setMonitoringDateTime(value.getMonitoringDateTime());
                        performanceBuilder.setPmDataValue(devPmDataValueToApi(value.getPmDataValue()));
                        performanceBuilder.setPmParameter(devPmParameterTypeToApi(value.getPmParameter()));
                        performanceBuilder.setPmPoint(devPmPointRefToApi(value.getPmPoint()));
                    }
                    apiPerfMap.put(apiPerformanceKey, performanceBuilder.build());
                });
            }
            getPmDataOutputBuilder.setPerformance(apiPerfMap);
        }
        return getPmDataOutputBuilder.build();
    }

    public GetTcaOutput devGetTcaOutputToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.Tcas tcas) {
        GetTcaOutputBuilder getTcaOutputBuilder = new GetTcaOutputBuilder();
        Map<TcaKey, Tca> apiTcaMap = new HashMap<>(16);
        @Nullable Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaKey,
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.Tca> tcaMap = tcas.getTca();
        if (tcaMap != null && !tcaMap.isEmpty()) {
            tcaMap.forEach((tcaKey,value)-> {
                TcaBuilder tcaBuilder = new TcaBuilder();
                PmGranularityType pmGranularityType = devPmGranularityTypeToApi(tcaKey.getPmGranularity());
                PmParameterType pmParameterType = devPmParameterTypeToApi(tcaKey.getPmParameter());
                PmPointRef pmPointRef = devPmPointRefToApi(tcaKey.getPmPoint());
                PmpObjectType pmpObjectType = devPmpObjectTypeToApi(tcaKey.getPmPointType());
                TcaKey apiTcaKey = new TcaKey(pmGranularityType, pmParameterType, pmPointRef, pmpObjectType);
                if (value != null) {
                    tcaBuilder.setPmGranularity(devPmGranularityTypeToApi(value.getPmGranularity()));
                    tcaBuilder.setPmParameter(devPmParameterTypeToApi(value.getPmParameter()));
                    tcaBuilder.setPmPoint(devPmPointRefToApi(value.getPmPoint()));
                    tcaBuilder.setPmPointType(devPmpObjectTypeToApi(value.getPmPointType()));
                    tcaBuilder.setThresholdValueHigh(value.getThresholdValueHigh());
                    tcaBuilder.setThresholdValueLow(value.getThresholdValueLow());
                }
                apiTcaMap.put(apiTcaKey, tcaBuilder.build());
            });
        }
        getTcaOutputBuilder.setTca(apiTcaMap);
        return getTcaOutputBuilder.build();
    }

    public Tcas apiSetTcaListInputToDev(SetTcaListInput input) {
        TcasBuilder tcasBuilder = new TcasBuilder();

        Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaKey,
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.Tca> values = new HashMap<>();
        if (input != null && input.getTca() != null && !input.getTca().isEmpty()) {
            @Nullable Map<TcaKey, Tca> tcas = input.getTca();
            tcas.forEach((key,value)->{
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaBuilder
                        tcaBuilder = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaBuilder();
                tcaBuilder.setPmGranularity(apiPmGranularityTypeToDev(value.getPmGranularity()));
                tcaBuilder.setPmParameter(apiPmParameterTypeToDev(value.getPmParameter()));
                tcaBuilder.setPmPoint(apiPmPointRefToDev(value.getPmPoint()));
                tcaBuilder.setPmPointType(apiPmpObjectTypeToDev(value.getPmPointType()));
                tcaBuilder.setThresholdValueHigh(value.getThresholdValueHigh());
                tcaBuilder.setThresholdValueLow(value.getThresholdValueLow());
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaKey
                        tcaKey = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaKey(
                        apiPmGranularityTypeToDev(key.getPmGranularity()),
                        apiPmParameterTypeToDev(key.getPmParameter()),
                        apiPmPointRefToDev(key.getPmPoint()),
                        apiPmpObjectTypeToDev(key.getPmPointType())
                );
                values.put(tcaKey, tcaBuilder.build());
            });
            tcasBuilder.setTca(values);
        }
        return tcasBuilder.build();
    }

    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.Tca apiSetTcaInputToDev(SetTcaInput input) {
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaBuilder tcaBuilder
                = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.tca.top.tcas.TcaBuilder();
        if (input != null) {
            tcaBuilder.setPmGranularity(apiPmGranularityTypeToDev(input.getPmGranularity()));
            tcaBuilder.setPmParameter(apiPmParameterTypeToDev(input.getPmParameter()));
            tcaBuilder.setPmPoint(apiPmPointRefToDev(input.getPmPoint()));
            tcaBuilder.setPmPointType(apiPmpObjectTypeToDev(input.getPmPointType()));
            tcaBuilder.setThresholdValueHigh(input.getThresholdValueHigh());
            tcaBuilder.setThresholdValueLow(input.getThresholdValueLow());
        }
        return tcaBuilder.build();
    }

    public GetPmPmpOutput devGetPmPmpOutputToApi(Pmps pmps) {
        GetPmPmpOutputBuilder getPmPmpOutputBuilder = new GetPmPmpOutputBuilder();
        Map<org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.get.pm.pmp.output.PmpKey,
                org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.get.pm.pmp.output.Pmp> apiMap = new HashMap<>(16);
        @Nullable Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpKey,
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.Pmp> pmpMap = pmps.getPmp();
        if (pmpMap != null && !pmpMap.isEmpty()) {
            pmpMap.forEach((pmpKey, pmp) -> {
                org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.get.pm.pmp.output.PmpBuilder pmpBuilder
                        = new org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.get.pm.pmp.output.PmpBuilder();
                // CTC设备这里不需要周期，前台也不会显示,OPT接口为兼容其他设备做的扩展加了周期。
                org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.get.pm.pmp.output.PmpKey apiPmpKey
                        = new org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.get.pm.pmp.output.PmpKey(PmGranularityType._15MIN,devPmPointRefToApi(pmpKey.getPmPoint()));
                if (pmp != null) {
                    pmpBuilder.setPmPoint(devPmPointRefToApi(pmp.getPmPoint()));
                    pmpBuilder.setPmPointEnable(pmp.getPmPointEnable());
                    pmpBuilder.setPmGranularity(PmGranularityType._15MIN);
                }
                apiMap.put(apiPmpKey, pmpBuilder.build());
            });
        }
        getPmPmpOutputBuilder.setPmp(apiMap);
        return getPmPmpOutputBuilder.build();
    }

    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.Pmp apiSetPmPmpOutputToDev(SetPmPmpInput setPmPmpInput) {
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpBuilder pmpBuilder
                = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpBuilder();
        if (setPmPmpInput != null) {
            pmpBuilder.setPmPoint(apiPmPointRefToDev(setPmPmpInput.getPmPoint()));
            pmpBuilder.setPmPointEnable(setPmPmpInput.getPmPointEnable());
        }
        return pmpBuilder.build();
    }

    public Pmps apiSetPmPmpListInputToDev(SetPmPmpListInput input) {
        PmpsBuilder pmpsBuilder = new PmpsBuilder();
        Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpKey,
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.Pmp> values = new HashMap<>(16);
        if (input != null && input.getPmp() != null && !input.getPmp().isEmpty()) {
            @Nullable Map<PmpKey, Pmp> pmpMap = input.getPmp();
            pmpMap.forEach((e, pmp) -> {
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpBuilder pmpBuilder
                        = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpBuilder();
                pmpBuilder.setPmPoint(apiPmPointRefToDev(pmp.getPmPoint()));
                pmpBuilder.setPmPointEnable(pmp.getPmPointEnable());
                org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpKey
                        pmpKey = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.performance.pmp.top.pmps.PmpKey(
                        apiPmPointRefToDev(e.getPmPoint()));
                values.put(pmpKey, pmpBuilder.build());
            });
            pmpsBuilder.setPmp(values);
        }
        return pmpsBuilder.build();
    }

    public org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.ResetCounterInput apiResetCounterInputToDev(ResetCounterInput input) {
        org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.ResetCounterInputBuilder resetCounterInputBuilder
                = new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.ResetCounterInputBuilder();
        if (input != null) {
            resetCounterInputBuilder.setPmPoint(apiPmPointRefToDev(input.getPmPoint()));
            resetCounterInputBuilder.setPmPointType(apiPmpObjectTypeToDev(input.getPmPointType()));
        }
        return resetCounterInputBuilder.build();
    }

}
