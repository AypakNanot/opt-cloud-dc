/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.performance;

import com.optel.commons.tools.collection.*;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.rev220208.get.pm.data.output.grouping.*;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.rev220208.get.pm.data.output.grouping.performance.*;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.rev220208.get.pm.data.output.grouping.performance.pm.data.value.*;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.rev220208.performance.tca.top.*;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.*;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.performance.*;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.performance.performance.type.*;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.performance.performance.type.analog.*;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.performance.pmp.top.*;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.performance.pmp.top.pmps.*;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.*;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tca.top.*;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.tcas.rev230426.tca.top.tca.parameters.*;
import org.opendaylight.yangtools.yang.common.Decimal64;

import java.math.*;
import java.util.*;

/**
 * ClassName: PerformanceTransform
 * <ul>
 * <li>性能 mini OTN 转 OPTEL DCI</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2023/7/27 11:24
 */
public class PerformanceTransform implements IPerformanceTransform {

    public Map<PerformanceKey, Performance> devPerformanceMapToApi(
            Map<org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.PerformanceKey,
                    org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.Performance> gperformancesMap) {
        if (MapUtil.isEmpty(gperformancesMap)) {
            return MapUtil.emptyMap();
        }
        Map<PerformanceKey, Performance> resMap = new HashMap<>(gperformancesMap.size());
        for (Map.Entry<org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.PerformanceKey,
                org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.Performance> entry : gperformancesMap.entrySet()) {
            org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.PerformanceKey key = entry.getKey();
            org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.Performance value = entry.getValue();
            resMap.put(devPerformanceKeyToApi(key), devPerformanceToApi(value));
        }
        return resMap;
    }

    /**
     * 转换Tca dev to api
     *
     * @param map tcaMap dev
     * @return api
     */
    public Map<TcaKey, Tca> devTcaParameterMapToApi(Map<TcaParameterKey, TcaParameter> map) {
        if (MapUtil.isEmpty(map)) {
            return MapUtil.emptyMap();
        }
        Map<TcaKey, Tca> resMap = new HashMap<>(map.size());
        for (Map.Entry<TcaParameterKey, TcaParameter> entry : map.entrySet()) {
            TcaParameterKey key = entry.getKey();
            TcaParameter value = entry.getValue();
            TcaKey tcaKey = devTcaParameterKeyToApi(key);
            if (resMap.containsKey(tcaKey)) {
                Tca tca = resMap.get(tcaKey);
                TcaBuilder tcaBuilder = new TcaBuilder(tca);
                getThresholdValueByType(value, tcaBuilder);
                resMap.put(tcaKey, tcaBuilder.build());
            } else {
                resMap.put(tcaKey, devTcaParameterToApi(value));
            }
        }
        return resMap;
    }

    /**
     * 根据TCA越限类型 决定转高门限 还是 低门限
     *
     * @param value      dev TCA
     * @param tcaBuilder api tca builder
     */
    private void getThresholdValueByType(TcaParameter value, TcaBuilder tcaBuilder) {
        if (ThresholdType.High.equals(value.getThresholdType())) {
            Real thresholdValue = value.getThresholdValue();
            if (thresholdValue != null) {
                tcaBuilder.setThresholdValueHigh(thresholdValue.getDecimal64());
            }
        }
        if (ThresholdType.Low.equals(value.getThresholdType())) {
            Real thresholdValue = value.getThresholdValue();
            if (thresholdValue != null) {
                tcaBuilder.setThresholdValueLow(thresholdValue.getDecimal64());
            }
        }
    }

    private TcaKey devTcaParameterKeyToApi(TcaParameterKey key) {
        if (key == null) {
            return null;
        }
        return new TcaKey(devGranularityToApi(key.getGranularity()), devPmParameterTypeToApi(key.getPmParameterName()),
                devPmPointRefToApi(key.getObjectName()), devObjectTypeToApi(key.getObjectType()));
    }

    private Tca devTcaParameterToApi(TcaParameter tcaParameter) {
        if (tcaParameter == null) {
            return null;
        }
        TcaBuilder tcaBuilder = new TcaBuilder();
        tcaBuilder.setPmPoint(devPmPointRefToApi(tcaParameter.getObjectName()));
        tcaBuilder.setPmGranularity(devGranularityToApi(tcaParameter.getGranularity()));
        tcaBuilder.setPmPointType(devObjectTypeToApi(tcaParameter.getObjectType()));
        tcaBuilder.setPmParameter(devPmParameterTypeToApi(tcaParameter.getPmParameterName()));
        getThresholdValueByType(tcaParameter, tcaBuilder);
        return tcaBuilder.build();
    }


    private Performance devPerformanceToApi(org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.Performance gPerformance) {
        if (gPerformance == null) {
            return null;
        }
        PerformanceBuilder performanceBuilder = new PerformanceBuilder();
        performanceBuilder.setPmPoint(devPmPointRefToApi(gPerformance.getObjectName()));
        performanceBuilder.setPmParameter(devPmParameterTypeToApi(gPerformance.getPmParameterName()));
        performanceBuilder.setMonitoringDateTime(gPerformance.getPmStartTime());
        performanceBuilder.setPmDataValue(devPmDataValueToApi(gPerformance.getPerformanceType()));
        return performanceBuilder.build();
    }

    private PmDataValue devPmDataValueToApi(PerformanceType performanceType) {
        if (performanceType == null) {
            return null;
        }
        if (performanceType instanceof Digital) {
            Real digitalPmValue = ((Digital) performanceType).getDigitalPmValue();
            if (digitalPmValue != null) {
                InstantBuilder instantBuilder = new InstantBuilder();
                instantBuilder.setDigitalPmValue(digitalPmValue.getDecimal64());
                return instantBuilder.build();
            }
        } else if (performanceType instanceof Analog) {
            AnalogPmValue analogPmValue = ((Analog) performanceType).getAnalogPmValue();
            if (analogPmValue != null) {
                InstantAvgMinMaxBuilder instantAvgMinMaxBuilder = new InstantAvgMinMaxBuilder();
                Real minValue = analogPmValue.getMinValue();
                Real currentValue = analogPmValue.getCurrentValue();
                Real maxValue = analogPmValue.getMaxValue();
                Real averageValue = analogPmValue.getAverageValue();
                if (minValue != null) {
                    instantAvgMinMaxBuilder.setMinValue(minValue.getDecimal64());
                }
                if (maxValue != null) {
                    instantAvgMinMaxBuilder.setMaxValue(maxValue.getDecimal64());
                }
                if (currentValue != null) {
                    instantAvgMinMaxBuilder.setCurrentValue(currentValue.getDecimal64());
                }
                if (averageValue != null) {
                    instantAvgMinMaxBuilder.setAverageValue(averageValue.getDecimal64());
                }
                return instantAvgMinMaxBuilder.build();
            }
        }
        return null;
    }

    private PerformanceKey devPerformanceKeyToApi(org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.rev230426.gperformances.PerformanceKey key) {
        if (key == null) {
            return null;
        }
        return new PerformanceKey(key.getPmStartTime(), devPmParameterTypeToApi(key.getPmParameterName()), devPmPointRefToApi(key.getObjectName()));
    }

    public TcaParameter apiSetTcaInputToDev(SetTcaInput input, ThresholdType highOrLow) {
        TcaParameterBuilder tcaParameterBuilder
                = new TcaParameterBuilder();
        if (input != null) {
            tcaParameterBuilder.setGranularity(apiPmGranularityTypeToDev(input.getPmGranularity()));
            tcaParameterBuilder.setPmParameterName(apiPmParameterTypeToDev(input.getPmParameter()));
            tcaParameterBuilder.setObjectName(apiPmPointRefToDev(input.getPmPoint()));
            tcaParameterBuilder.setObjectType(apiPmObjectTypeToDev(input.getPmPointType()));
            if (ThresholdType.High.equals(highOrLow)) {
                BigDecimal thresholdValueHigh = input.getThresholdValueHigh().decimalValue();
                tcaParameterBuilder.setThresholdType(ThresholdType.High);
                tcaParameterBuilder.setThresholdValue(new Real(Decimal64.valueOf(thresholdValueHigh)));
            }
            if (ThresholdType.Low.equals(highOrLow)) {
                BigDecimal thresholdValueLow = input.getThresholdValueLow().decimalValue();
                tcaParameterBuilder.setThresholdType(ThresholdType.Low);
                tcaParameterBuilder.setThresholdValue(new Real(Decimal64.valueOf(thresholdValueLow)));
            }
        }
        return tcaParameterBuilder.build();
    }

    public TcaParameters apiSetTcaListInputToDev(SetTcaListInput inputList) {
        if (inputList == null) {
            return null;
        }
        Map<TcaKey, Tca> tca = inputList.getTca();
        if (MapUtil.isEmpty(tca)) {
            return null;
        }
        TcaParametersBuilder tcaParametersBuilder = new TcaParametersBuilder();
        tcaParametersBuilder.setTcaParameter(apiTcaMapToDev(inputList.getTca()));
        return tcaParametersBuilder.build();
    }

    private Map<TcaParameterKey, TcaParameter> apiTcaMapToDev(Map<TcaKey, Tca> tcaMap) {
        if (MapUtil.isEmpty(tcaMap)) {
            return MapUtil.emptyMap();
        }
        Map<TcaParameterKey, TcaParameter> resMap = new HashMap<>(tcaMap.size());
        for (Map.Entry<TcaKey, Tca> entry : tcaMap.entrySet()) {
            TcaKey tcaKey = entry.getKey();
            Tca tca = entry.getValue();

            Granularity granularity = apiPmGranularityTypeToDev(tcaKey.getPmGranularity());
            String objectName = apiPmPointRefToDev(tcaKey.getPmPoint());
            ObjectType objectType = apiPmObjectTypeToDev(tcaKey.getPmPointType());
            PmParameterType pmParameterType = apiPmParameterTypeToDev(tcaKey.getPmParameter());
            BigDecimal thresholdValueHigh = tca.getThresholdValueHigh().decimalValue();
            BigDecimal thresholdValueLow = tca.getThresholdValueLow().decimalValue();
            TcaParameterBuilder tcaParameterBuilder = new TcaParameterBuilder();
            tcaParameterBuilder.setGranularity(granularity);
            tcaParameterBuilder.setObjectName(objectName);
            tcaParameterBuilder.setObjectType(objectType);
            tcaParameterBuilder.setThresholdValue(new Real(Decimal64.valueOf(thresholdValueHigh)));
            if (thresholdValueHigh != null) {
                TcaParameterKey tcaParameterKey = new TcaParameterKey(granularity, objectName, objectType, pmParameterType, ThresholdType.High);
                tcaParameterBuilder.setPmParameterName(pmParameterType);
                tcaParameterBuilder.setThresholdType(ThresholdType.High);
                resMap.put(tcaParameterKey, tcaParameterBuilder.build());
            }
            if (thresholdValueLow != null) {
                TcaParameterKey tcaParameterKey = new TcaParameterKey(granularity, objectName, objectType, pmParameterType, ThresholdType.Low);
                tcaParameterBuilder.setPmParameterName(pmParameterType);
                tcaParameterBuilder.setThresholdType(ThresholdType.Low);
                resMap.put(tcaParameterKey, tcaParameterBuilder.build());
            }
        }
        return resMap;
    }

    public Pmp apiSetPmPmpInputToDev(SetPmPmpInput setPmPmpInput) {
        PmpBuilder pmpBuilder
                = new PmpBuilder();
        if (setPmPmpInput != null) {
            pmpBuilder.setPmPoint(apiPmPointRefToDev(setPmPmpInput.getPmPoint()));
            pmpBuilder.setPmPointEnable(setPmPmpInput.getPmPointEnable());
            pmpBuilder.setGranularity(apiPmGranularityTypeToDev(setPmPmpInput.getPmGranularity()));
        }
        return pmpBuilder.build();
    }

    public Pmps apiSetPmpListInputToDev(SetPmPmpListInput input) {
        PmpsBuilder pmpsBuilder = new PmpsBuilder();
        Map<PmpKey,Pmp> values = new HashMap<>(16);
        if (input != null) {
            Map<org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.rev220208.performance.pmp.top.PmpKey,
                    org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.rev220208.performance.pmp.top.Pmp> pmpMap = input.getPmp();
            if (pmpMap != null && !pmpMap.isEmpty()) {
                for (Map.Entry<org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.rev220208.performance.pmp.top.PmpKey,
                        org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.rev220208.performance.pmp.top.Pmp> pmpKeyPmpEntry : pmpMap.entrySet()) {
                    org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.rev220208.performance.pmp.top.PmpKey e = pmpKeyPmpEntry.getKey();
                    org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.rev220208.performance.pmp.top.Pmp pmp = pmpKeyPmpEntry.getValue();
                    PmpBuilder pmpBuilder = new PmpBuilder();
                    pmpBuilder.setPmPoint(apiPmPointRefToDev(pmp.getPmPoint()));
                    pmpBuilder.setGranularity(Granularity._15min);
                    pmpBuilder.setPmPointEnable(pmp.getPmPointEnable());
                    PmpKey pmpKey = new PmpKey(Granularity._15min, apiPmPointRefToDev(e.getPmPoint()));
                    values.put(pmpKey, pmpBuilder.build());

                    PmpBuilder pmpBuilder2 = new PmpBuilder();
                    pmpBuilder2.setPmPoint(apiPmPointRefToDev(pmp.getPmPoint()));
                    pmpBuilder2.setGranularity(Granularity._24h);
                    pmpBuilder2.setPmPointEnable(pmp.getPmPointEnable());
                    PmpKey pmpKey2 = new PmpKey(Granularity._24h, apiPmPointRefToDev(e.getPmPoint()));
                    values.put(pmpKey2, pmpBuilder2.build());
                }
                pmpsBuilder.setPmp(values);
            }
        }
        return pmpsBuilder.build();
    }

    public GetPmPmpOutput devGetPmPmpOutputToApi(Pmps pmps) {
        GetPmPmpOutputBuilder getPmPmpOutputBuilder = new GetPmPmpOutputBuilder();
        Map<PmpKey, Pmp> pmp = pmps.getPmp();
        if(pmp != null && !pmp.isEmpty()){
            Map<org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.get.pm.pmp.output.PmpKey,
                    org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.get.pm.pmp.output.Pmp> map = new HashMap<>(pmp.size());
            for (Map.Entry<PmpKey, Pmp> entry : pmp.entrySet()) {
                PmpKey key = entry.getKey();
                Pmp value = entry.getValue();
                org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.get.pm.pmp.output.PmpKey pmpKey
                        = new org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.get.pm.pmp.output.PmpKey(devGranularityToApi(key.getGranularity()),devPmPointRefToApi(key.getPmPoint()));
                org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.get.pm.pmp.output.PmpBuilder pmpBuilder
                        = new org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.performance.rev200210.get.pm.pmp.output.PmpBuilder();
                if(value != null){
                    pmpBuilder.setPmPoint(devPmPointRefToApi(value.getPmPoint()));
                    pmpBuilder.setPmGranularity(devGranularityToApi(value.getGranularity()));
                    pmpBuilder.setPmPointEnable(value.getPmPointEnable());
                }
                map.put(pmpKey,pmpBuilder.build());
            }
            getPmPmpOutputBuilder.setPmp(map);
        }
        return getPmPmpOutputBuilder.build();
    }

    public org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.ResetCounterInput apiResetCounterInputToDev(ResetCounterInput input) {
        org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.ResetCounterInputBuilder resetCounterInputBuilder = new org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.ResetCounterInputBuilder();
        if (null != input) {
            resetCounterInputBuilder.setObjectName(input.getPmPoint().getValue());
            resetCounterInputBuilder.setObjectType(apiPmObjectTypeToDev(input.getPmPointType()));
        }
        return resetCounterInputBuilder.build();
    }
}
