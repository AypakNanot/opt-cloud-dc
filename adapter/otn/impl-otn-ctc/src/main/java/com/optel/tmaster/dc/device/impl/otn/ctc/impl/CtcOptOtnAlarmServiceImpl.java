/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import cn.hutool.core.map.MapUtil;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptOtnAlarmServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.AlarmTransformImpl;
import com.optel.tmaster.dc.general.base.exception.device.DeviceOperaFailException;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import com.optel.tmaster.dc.general.nc.nccore.NcTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.alarm.rev200427.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.alarms.rev210927.PerceivedSeverity;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetAlarmAttrInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetAlarmAttrOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.OptOtnExtensionService;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.SetAlarmAttrOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.alarm.attrs.grouping.AlarmAttr;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.*;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.alarm.mask.states.AlarmMaskState;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.alarms.grouping.Alarm;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tcas.grouping.Tca;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * OTN 关于告警模块实现
 * 2020/4/27 13:41
 *
 * @author lxy
 * @version V1.0.0
 */
public class CtcOptOtnAlarmServiceImpl extends BaseOptOtnAlarmServiceImpl implements IDeviceServiceOtnCtc {

    private final static Logger LOG = LoggerFactory.getLogger(CtcOptOtnAlarmServiceImpl.class);


    @Override
    public ListenableFuture<RpcResult<ReportEvenTestOutput>> reportEvenTest(ReportEvenTestInput input) {
        /**
         * 测试接口 无需转义
         */
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<ReportAlarmTestOutput>> reportAlarmTest(ReportAlarmTestInput input) {
        /**
         * 测试接口 无需转义
         */
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<QueryAlarmAttrNeOutput>> queryAlarmAttrNe(QueryAlarmAttrNeInput input) {
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
            GetAlarmAttrInputBuilder builder = new GetAlarmAttrInputBuilder();
            builder.setObjectName(input.getObjectName())
                    .setAlarmCode(input.getAlarmCode());
            Future<RpcResult<GetAlarmAttrOutput>> resultFuture = service.getAlarmAttr(builder.build());
            return RpcResultUtil.buildFutureResult(resultFuture, result -> {
                QueryAlarmAttrNeOutputBuilder queryAlarmAttrNeOutputBuilder = new QueryAlarmAttrNeOutputBuilder();
                if (result.getAlarmAttrs() != null && result.getAlarmAttrs().getAlarmAttr() != null) {
                    List<AlarmAttr> list = new ArrayList<>(result.getAlarmAttrs().getAlarmAttr().values());
                    if (!list.isEmpty()) {
                        queryAlarmAttrNeOutputBuilder.setAlarmAttr(ltm(new AlarmTransformImpl().devAlarmAttrToApiList(list)));
                    }
                }
                return queryAlarmAttrNeOutputBuilder.build();
            });
        }
        return RpcResultUtil.failed();
    }

    @Override
    public ListenableFuture<RpcResult<GetAlarmMaskStatusOutput>> getAlarmMaskStatus(GetAlarmMaskStatusInput input) {
        //从设备处取得数据
        InstanceIdentifier<AlarmMaskStates> iid = create(AlarmMaskStates.class);
        AlarmMaskStates alarmMaskStates = MountTools.queryFromOperational(input.getNeId(), iid);
        //组装数据
        GetAlarmMaskStatusOutputBuilder getAlarmMaskStatusOutputBuilder =
                new GetAlarmMaskStatusOutputBuilder();
        if (alarmMaskStates != null && MapUtil.isNotEmpty(alarmMaskStates.getAlarmMaskState())) {
            List<AlarmMaskState> dataList = new ArrayList<>(alarmMaskStates.getAlarmMaskState().values());
            filterAlarmMask(dataList, input.getMaskState(), input.getObjectType());
            getAlarmMaskStatusOutputBuilder.setAlarmMaskState(ltm(new AlarmTransformImpl().devAlarmMaskStateToApi(dataList)));
        }
        return RpcResultUtil.success(getAlarmMaskStatusOutputBuilder.build());
    }

    private void filterAlarmMask(List<AlarmMaskState> list, Boolean maskState, String type) {
        if (list != null) {
            for (int i = 0; i < list.size(); i++) {
                AlarmMaskState alarmMaskState = list.get(i);
                if (maskState != null) {
                    if (alarmMaskState.getMaskState().booleanValue() != maskState.booleanValue()) {
                        list.remove(i);
                        i--;
                        continue;
                    }
                }
                if (type != null) {
                    if (!alarmMaskState.getObjectType().equals(type)) {
                        list.remove(i);
                        i--;
                    }
                }
            }
        }
    }

    @Override
    public ListenableFuture<RpcResult<SetAlarmMaskStatusOutput>> setAlarmMaskStatus(SetAlarmMaskStatusInput input) {

        InstanceIdentifier<AlarmMaskStates> iids = create(AlarmMaskStates.class);
        //下发设备
        MountTools.doMergeToConfig(input.getNeId(), iids, new AlarmTransformImpl().apiAlarmMaskStatesToDev(input).build());
        return RpcResultUtil.success();
    }

    private Future<RpcResult<GetHistoryAlarmsOutput>> getHistoryAlarmsOutput(String neOid, DateAndTime startTime, DateAndTime endTime) {
        AccAlarmsService service = MountTools.getRpcService(neOid, AccAlarmsService.class);
        GetHistoryAlarmsInputBuilder builder = new GetHistoryAlarmsInputBuilder();
        builder.setStartTime(startTime);
        builder.setEndTime(endTime);
        return service.getHistoryAlarms(builder.build());
    }

    @Override
    public ListenableFuture<RpcResult<GetHistoryTcaNeOutput>> getHistoryTcaNe(GetHistoryTcaNeInput input) {

        Future<RpcResult<GetHistoryAlarmsOutput>> resultFuture =
                getHistoryAlarmsOutput(input.getNeId(), input.getStartTime(), input.getEndTime());
        try {
            RpcResult<GetHistoryAlarmsOutput> rpcResult = resultFuture.get();
            if (rpcResult.isSuccessful()) {
                GetHistoryAlarmsOutput getHistoryAlarmsOutput = rpcResult.getResult();
                /*
                 * 将cmcc中的历史告警查询结果转换成opt中的历史告警查询的输出结果类型
                 * 1.声明opt上的数据类型builder
                 * 2.在该builder中填充数据
                 * 3.将该builder填充到opt输出结果的builder中
                 * 4.返回opt输出结果builder.build()
                 */
                GetHistoryTcaNeOutputBuilder getHistoryTcaNeOutputBuilder = new GetHistoryTcaNeOutputBuilder();
                if (getHistoryAlarmsOutput.getTcas() != null && getHistoryAlarmsOutput.getTcas().getTca() != null) {
                    getHistoryTcaNeOutputBuilder.setTca(
                            ltm(new AlarmTransformImpl().devTcaToApiList(
                                    new ArrayList<>(getHistoryAlarmsOutput.getTcas().getTca().values()))));
                }
                return RpcResultUtil.success(getHistoryTcaNeOutputBuilder.build());
            } else {
                return RpcResultUtil.failed(rpcResult);
            }
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("device request is failed");
            throw new DeviceOperaFailException(e);
        }
    }

    @Override
    public ListenableFuture<RpcResult<ModifyAlarmAttrNeOutput>> modifyAlarmAttrNe(ModifyAlarmAttrNeInput input) {
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
            ListenableFuture<RpcResult<SetAlarmAttrOutput>> result = service.setAlarmAttr(new AlarmTransformImpl().apiAlarmAttrToDev(input).build());
            RpcResultUtil.buildFutureResult(result);
        }
        return RpcResultUtil.failed();
    }

    @Override
    public ListenableFuture<RpcResult<QueryAlarmOutput>> queryAlarm(QueryAlarmInput input) {
        InstanceIdentifier<Alarms> iid = create(Alarms.class);
        //从设备处取数据
        Alarms alarms = MountTools.queryFromOperational(input.getNeId(), iid);
        //组装数据
        QueryAlarmOutputBuilder builder = new QueryAlarmOutputBuilder();
        if (alarms != null && alarms.getAlarm() != null) {
            List<Alarm> list = new ArrayList<>(alarms.getAlarm().values());
            filterAlarm(list, input.getPerceivedSeverity(), input.getStartTime(), input.getEndTime());
            builder.setAlarm(ltm(new AlarmTransformImpl().devAlarmToApiList(list)));
        }
        //返回
        return RpcResultUtil.success(builder.build());
    }

    /**
     * 根据过滤条件，过滤告警
     *
     * @param list              告警列表
     * @param perceivedSeverity 告警级别
     * @param startTime         告警产生开始时间
     * @param endTime           告警产生结束时间
     */
    public void filterAlarm(List<Alarm> list, PerceivedSeverity perceivedSeverity, DateAndTime startTime, DateAndTime endTime) {
        if (list == null || list.isEmpty()) {
        } else if (perceivedSeverity != null || endTime != null || startTime != null) {
            for (int i = 0; i < list.size(); i++) {
                Alarm alarm = list.get(i);
                //判断告警级别
                if (perceivedSeverity != null) {
                    if (!perceivedSeverity.getName().equals(alarm.getPerceivedSeverity().getName())) {
                        list.remove(i);
                        i--;
                        continue;
                    }
                }
                //判断告警产生时间段
                if (startTime != null && endTime != null) {
                    if (!isTimeBetween(alarm.getStartTime(), startTime, endTime)) {
                        list.remove(i);
                        i--;
                    }
                }
            }
        }
    }

    /**
     * 判断一个DateAndTime是否在某个区间内
     *
     * @param time      待判断时间
     * @param startTime 左边界时间
     * @param endTime   右边界时间
     * @return 在：true;不在：false
     */
    private boolean isTimeBetween(DateAndTime time, DateAndTime startTime, DateAndTime endTime) {
        if (time.toString().contains("Z")) {
            String formatter = "yyyy-MM-dd'T'HH:mm:ss'Z'";
            LocalDateTime alarmTime = LocalDateTime.parse(time.getValue(), DateTimeFormatter.ofPattern(formatter));
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss");
            SimpleDateFormat sdf2 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss+'08:00'");
            try {
                long a = sdf.parse(alarmTime.toString()).getTime();
                Calendar cal = Calendar.getInstance();
                cal.setTimeInMillis(a);
                cal.add(Calendar.HOUR, +8);
                Date time2 = cal.getTime();
                long time1 = time2.getTime();
                long startTime1 = sdf2.parse(startTime.getValue()).getTime();
                long endTime1 = sdf2.parse(endTime.getValue()).getTime();
                return time1 >= startTime1 && time1 <= endTime1;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return false;
        } else {
            String formatter = "yyyy-MM-dd'T'HH:mm:ss+'08:00'";
            LocalDateTime alarmTime = LocalDateTime.parse(time.getValue(), DateTimeFormatter.ofPattern(formatter));
            LocalDateTime start = LocalDateTime.parse(startTime.getValue(), DateTimeFormatter.ofPattern(formatter));
            LocalDateTime end = LocalDateTime.parse(endTime.getValue(), DateTimeFormatter.ofPattern(formatter));
            return !start.isAfter(alarmTime) && !alarmTime.isAfter(end);
        }
    }

    @Override
    public ListenableFuture<RpcResult<SetAlarmMaskBatchOutput>> setAlarmMaskBatch(SetAlarmMaskBatchInput input) {
        OptOtnExtensionService optOtnExtensionService =
                MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        return RpcResultUtil.buildFutureResult(optOtnExtensionService.setAlarmMaskBatch(new AlarmTransformImpl().apiAlarmMaskToDev(input)));
    }

    @Override
    public ListenableFuture<RpcResult<GetHistoryAlarmsNeOutput>> getHistoryAlarmsNe(GetHistoryAlarmsNeInput input) {
        Future<RpcResult<GetHistoryAlarmsOutput>> resultFuture =
                getHistoryAlarmsOutput(input.getNeId(), input.getStartTime(), input.getEndTime());
        return RpcResultUtil.buildFutureResult(resultFuture, result -> {
            /*
             * 将cmcc中的历史告警查询结果转换成opt中的历史告警查询的输出结果类型
             * 1.声明opt上的数据类型builder
             * 2.在该builder中填充数据
             * 3.将该builder填充到opt输出结果的builder中
             * 4.返回opt输出结果builder.build()
             */
            GetHistoryAlarmsNeOutputBuilder getHistoryAlarmsNeOutputBuilder = new GetHistoryAlarmsNeOutputBuilder();
            if (result.getAlarms() != null && result.getAlarms().getAlarm() != null) {
                List<Alarm> list = new ArrayList<>(result.getAlarms().getAlarm().values());
                filterAlarm(list, input.getPerceivedSeverity(), null, null);
                getHistoryAlarmsNeOutputBuilder.setAlarm(ltm(new AlarmTransformImpl().devAlarmToApiList(list)));
            }
            return getHistoryAlarmsNeOutputBuilder.build();
        });
    }

    @Override
    public ListenableFuture<RpcResult<QueryTcasOutput>> queryTcas(QueryTcasInput input) {
        InstanceIdentifier<Tcas> lid = create(Tcas.class);
        Tcas tcas = MountTools.queryFromOperational(input.getNeId(), lid);
        QueryTcasOutputBuilder outputBuilder = new QueryTcasOutputBuilder();
        if (tcas != null && tcas.getTca() != null) {
            List<Tca> list = new ArrayList<>(tcas.getTca().values());
            if (list.size() > 0) {
                filterTca(list, input.getStartTime(), input.getEndTime());
            }
            outputBuilder.setTca(ltm(new AlarmTransformImpl().devTcaToApiList(list)));
        }
        return RpcResultUtil.success(outputBuilder.build());
    }

    /**
     * 过滤性能越限告警信息
     *
     * @param list      待过滤的数据
     * @param startTime 告警产生开始时间
     * @param endTime   告警产生消失时间
     */
    private void filterTca(List<Tca> list, DateAndTime startTime, DateAndTime endTime) {
        if (list != null) {
            if (startTime != null && endTime != null) {
                for (int i = 0; i < list.size(); i++) {
                    Tca tca = list.get(i);
                    //判断告警产生时间段
                    if (!isTimeBetween(tca.getStartTime(), startTime, endTime)) {
                        list.remove(i);
                        i--;
                    }
                }
            }
        }
    }

    @Override
    public ListenableFuture<RpcResult<ReportTcaTestOutput>> reportTcaTest(ReportTcaTestInput input) {
        /**
         * 测试接口 无需转义
         */
        return null;
    }
}
