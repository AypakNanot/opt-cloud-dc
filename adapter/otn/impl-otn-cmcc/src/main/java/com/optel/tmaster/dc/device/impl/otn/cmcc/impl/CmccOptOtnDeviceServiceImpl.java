/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import cn.hutool.core.util.StrUtil;
import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptOtnDeviceServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.*;
import com.optel.tmaster.dc.general.base.exception.device.DeviceOperaFailException;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import com.optel.tmaster.dc.general.nc.nccore.NcTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.GetServiceDelayMeasureResultInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.GetServiceDelayMeasureResultOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.GetServiceDelayMeasureResultOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.alarm.delay.inserts.grouping.AlarmDelayInsert;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.alarm.delay.inserts.grouping.AlarmDelayInsertKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ctp.extension.grouping.CtpExtension;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ctp.extension.grouping.CtpExtensionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ctp.extension.grouping.CtpExtensionKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ctp.extension.parameter.grouping.OduCtpExtensionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.band.width.grouping.EthPtpBandWidth;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.band.width.grouping.EthPtpBandWidthKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.frame.spaces.grouping.EthPtpFrameSpace;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.eth.ptp.frame.spaces.grouping.EthPtpFrameSpaceKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.extension.CtpExtensions;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.extension.CtpExtensionsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.extension.PtpExtensions;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.extension.PtpExtensionsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lpt.grouping.Lpt;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.lpt.grouping.LptKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.msi.auto.parameter.grouping.MsiAutoParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.msi.auto.parameter.grouping.MsiAutoParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.msi.auto.parameter.grouping.MsiAutoParameterKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.msi.parameter.grouping.MsiParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.msi.parameter.grouping.MsiParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.msi.parameter.grouping.MsiParameterKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtension;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.ptp.extension.grouping.PtpExtensionKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.service.delay.measure.configs.grouping.ServiceDelayMeasureConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.service.delay.measure.configs.grouping.ServiceDelayMeasureConfigKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.tcm.parameters.grouping.TcmParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.tcm.parameters.grouping.TcmParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.tcm.parameters.grouping.TcmParameterKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.*;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ctps.Ctp;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ctps.CtpKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.eqs.Eq;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.eqs.EqKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.mc.ports.McPort;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.mc.ports.McPortKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.me.NtpServers;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.me.ntp.servers.NtpServer;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.me.ntp.servers.NtpServerBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.me.ntp.servers.NtpServerKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ptps.Ptp;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ptps.PtpKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ctp1;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ctp1Builder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.Ptp2;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ctps.ctp.EthCtpPac;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ctps.ctp.EthCtpPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ctps.ctp.eth.ctp.pac.ClientVlanSpecBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.ctps.ctp.eth.ctp.pac.Performance;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.ctps.ctp.OduCtpPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.ctps.ctp.VcCtpPacBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.stream.Collectors;

/**
 * <ul>
 * <li>(OTN设备配置)</li>
 * </ul>
 *
 * @author LWX 2020/4/25 11:21
 */
public class CmccOptOtnDeviceServiceImpl extends BaseOptOtnDeviceServiceImpl implements IDeviceServiceOtnCmcc {

    private static final Logger LOG = LoggerFactory.getLogger(CmccOptOtnDeviceServiceImpl.class);

    @Override
    public ListenableFuture<RpcResult<ModifyMsiAutoParameterOutput>> modifyMsiAutoParameter(ModifyMsiAutoParameterInput input) {
        MsiAutoParameterKey msiAutoParameterKey = new MsiAutoParameterKey(input.getName());
        InstanceIdentifier<MsiAutoParameter> iid = create(MsiAutoParameters.class).child(MsiAutoParameter.class, msiAutoParameterKey);
        MsiAutoParameterBuilder msiAutoParameterBuilder = new MsiAutoParameterBuilder();
        msiAutoParameterBuilder.withKey(msiAutoParameterKey);
        msiAutoParameterBuilder.setName(input.getName());
        msiAutoParameterBuilder.setAuto(input.getAuto());
        //下发数据至设备
        MountTools.doMergeToConfig(input.getNeId(), iid, msiAutoParameterBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryMsiAutoParameterOutput>> queryMsiAutoParameter(QueryMsiAutoParameterInput input) {
        QueryMsiAutoParameterOutputBuilder queryMsiParameterOutputBuilder = new QueryMsiAutoParameterOutputBuilder();
        List<MsiAutoParameter> msiAutoParameters = new ArrayList<>();
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            Set<String> ptpNames = input.getName();
            if (ptpNames != null && ptpNames.size() == 1) {
                String ptpName = CollUtil.getFirst(ptpNames);
                MsiAutoParameterKey msiAutoParameterKey = new MsiAutoParameterKey(ptpName);
                InstanceIdentifier<MsiAutoParameter> iid = create(MsiAutoParameters.class).child(MsiAutoParameter.class, msiAutoParameterKey);
                MsiAutoParameter msiAutoParameter = MountTools.queryFromOperational(input.getNeId(), iid);
                if (msiAutoParameter != null) {
                    msiAutoParameters.add(msiAutoParameter);
                }
            } else {
                InstanceIdentifier<MsiAutoParameters> iid = create(MsiAutoParameters.class);
                MsiAutoParameters msiAutoParameters1 = MountTools.queryFromOperational(input.getNeId(), iid);
                if (msiAutoParameters1 != null && MapUtil.isNotEmpty(msiAutoParameters1.getMsiAutoParameter())) {
                    if (CollUtil.isEmpty(input.getName())) {
                        msiAutoParameters.addAll(msiAutoParameters1.getMsiAutoParameter().values());
                    } else {
                        //根据输入的端口名称范围过滤
                        for (String name : input.getName()) {
                            MsiAutoParameter autoParam = msiAutoParameters1.getMsiAutoParameter().get(new MsiAutoParameterKey(name));
                            if (autoParam != null) {
                                msiAutoParameters.add(autoParam);
                            }
                        }
                    }
                }
            }
        }
        if (CollUtil.isNotEmpty(msiAutoParameters)) {
            queryMsiParameterOutputBuilder.setMsiAutoParameter(ltm(new MsiTransformImpl().devMsiAutoToApiList(msiAutoParameters)));
        }
        return RpcResultUtil.success(queryMsiParameterOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<ModifyTcmParameterOutput>> modifyTcmParameter(ModifyTcmParameterInput input) {
        TcmParameterKey key = new TcmParameterKey(input.getName(), input.getTcmGrade());
        InstanceIdentifier<TcmParameter> iid = create(TcmParameters.class).child(TcmParameter.class, key);
        TcmParameterBuilder builder = new TcmParameterBuilder();
        TcmTransformImpl tcmTransform = new TcmTransformImpl();
        builder.withKey(key)
                .setName(input.getName())
                .setSourceMode(tcmTransform.apiActiveModeToDev(input.getSourceMode()))
                .setDestMode(tcmTransform.apiActiveModeToDev(input.getDestMode()))
                .setTcmGrade(input.getTcmGrade())
                .setTtiActualTx(input.getTtiActualTx())
                .setTtiExpectedRx(input.getTtiExpectedRx())
                .setLckInsert(input.getLckInsert())
                .setTimAction(input.getTimAction())
                .setLtcAction(input.getLtcAction())
                .setTimMode(tcmTransform.apiTimModeToDev(input.getTimMode()));
        //下发数据至设备
        MountTools.doMergeToConfig(input.getNeId(), iid, builder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<DeleteEqOutput>> deleteEq(DeleteEqInput input) {
        InstanceIdentifier<Eq> iid = create(Eqs.class).child(Eq.class, new EqKey(input.getEqName()));
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyMeOutput>> modifyMe(ModifyMeInput input) {
        InstanceIdentifier<Me> meIid = create(Me.class);
        MeBuilder meBuilder = new NeNtpTransformImpl().apiMeToDev(input);
        //下发数据至设备
        MountTools.doMergeToConfig(input.getNeId(), meIid, meBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyOduPtpTypeOutput>> modifyOduPtpType(ModifyOduPtpTypeInput input) {
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        return RpcResultUtil.buildFutureResult(service.setOduTerminationPointType(new PtpTransformImpl().apiModifyOduTypeInputToDev(input)));
    }

    @Override
    public ListenableFuture<RpcResult<AddModifyMeNtpOutput>> addModifyMeNtp(AddModifyMeNtpInput input) {
        NtpServerKey key = new NtpServerKey(input.getName());
        InstanceIdentifier<NtpServer> iid = create(Me.class).child(NtpServers.class).child(NtpServer.class, key);
        NtpServerBuilder ntpServerBuilder = new NeNtpTransformImpl().apiNtpServerToDev(input);
        //下发数据至设备
        MountTools.doMergeToConfig(input.getNeId(), iid, ntpServerBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryEthPtpExtensionOutput>> queryEthPtpExtension(QueryEthPtpExtensionInput input) {
        QueryEthPtpExtensionOutputBuilder outputBuilder = new QueryEthPtpExtensionOutputBuilder();
        if (NcTools.isSupportOptelExt(input.getNeId())) {

            //填充EthPtpBandwidth
            EthPtpsBandWidth ethPtpsBandWidth;
            //填充ethPtpExtensions
            // EthPtpExtensions ethPtpExtensions;
            //填充PtpExtensions
            PtpExtensions ptpExtensions = null;
            try {
                if (StrUtil.isEmpty(input.getPtpName())) {
                    InstanceIdentifier<EthPtpsBandWidth> iid = create(EthPtpsBandWidth.class);
                    InstanceIdentifier<PtpExtensions> extensionIid =
                            create(Extension.class).child(PtpExtensions.class);
                    ethPtpsBandWidth = MountTools.queryFromOperational(input.getNeId(), iid);
                    PtpExtensions ptpExtensionsData = MountTools.queryFromOperational(input.getNeId(), extensionIid);
                    List<PtpExtension> listDate = new PtpTransformImpl().getPtpExtensionList(ptpExtensionsData);
                    if (!listDate.isEmpty()) {
                        ptpExtensions = new PtpExtensionsBuilder().setPtpExtension(ltm(listDate)).build();
                    }

                } else {
                    InstanceIdentifier<EthPtpBandWidth> iid = create(EthPtpsBandWidth.class)
                            .child(EthPtpBandWidth.class, new EthPtpBandWidthKey(input.getPtpName()));
                    InstanceIdentifier<PtpExtension> extensionIid
                            = create(Extension.class).child(PtpExtensions.class)
                            .child(PtpExtension.class, new PtpExtensionKey(input.getPtpName()));
                    EthPtpBandWidth ethPtpBandWidth = MountTools.queryFromOperational(input.getNeId(), iid);
                    PtpExtension ptpExtension = MountTools.queryFromOperational(input.getNeId(), extensionIid);
                    List<EthPtpBandWidth> list = new LinkedList<>();
                    List<PtpExtension> ptpExtensionList = new LinkedList<>();
                    if (ethPtpBandWidth != null) {
                        list.add(ethPtpBandWidth);
                    }
                    if (ptpExtension != null && ptpExtension.getEthPtpExtension() != null) {
                        ptpExtensionList.add(ptpExtension);
                    }
                    ethPtpsBandWidth = new EthPtpsBandWidthBuilder().setEthPtpBandWidth(ltm(list)).build();
                    ptpExtensions = new PtpExtensionsBuilder().setPtpExtension(ltm(ptpExtensionList)).build();
                }
                if (ethPtpsBandWidth != null) {
                    outputBuilder.setEthPtpBandWidth(ltm(new PtpTransformImpl().devEthPtpsBandWidthToApiList(ethPtpsBandWidth)));
                }
                if (ptpExtensions != null && ptpExtensions.getPtpExtension() != null) {
                    outputBuilder.setPtpExtension(ltm(new PtpTransformImpl().devPtpExtensionToApiList(new ArrayList<>(ptpExtensions.getPtpExtension().values()))));
                }
            } catch (Exception e) {
                //不作处理
                LOG.error("query EthPtpExtension error:", e);
            }
        }
        return RpcResultUtil.success(outputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<QueryPtpsOutput>> queryPtps(QueryPtpsInput input) {
        List<Ptp> ptpList = new ArrayList<>();
        if (CollUtil.isNotEmpty(input.getPtpName()) && input.getPtpName().size() == 1) {
            //查询对应Ptp
            InstanceIdentifier<Ptp> iid = create(Ptps.class).child(Ptp.class, new PtpKey(CollUtil.getFirst(input.getPtpName())));
            Ptp ptp = MountTools.queryFromOperational(input.getNeId(), iid);
            if (ptp != null) {
                ptpList.add(ptp);
            }
        } else {
            //查询所有
            InstanceIdentifier<Ptps> iid = create(Ptps.class);
            //从设备查询的数据
            Ptps ptps = MountTools.queryFromOperational(input.getNeId(), iid);
            if (ptps != null && MapUtil.isNotEmpty(ptps.getPtp())) {
                if (CollUtil.isEmpty(input.getPtpName())) {
                    ptpList = new ArrayList<>(ptps.getPtp().values());
                } else {
                    //根据输入的端口名称范围过滤
                    for (String name : input.getPtpName()) {
                        Ptp ptp = ptps.getPtp().get(new PtpKey(name));
                        if (ptp != null) {
                            ptpList.add(ptp);
                        }
                    }
                }
            }
        }
        QueryPtpsOutputBuilder outputBuilder = new QueryPtpsOutputBuilder();
        outputBuilder.setPtp(ltm(new PtpTransformImpl().devPtpToApiList(ptpList)));
        return RpcResultUtil.success(outputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<DelServiceDelayMeasureConfigOutput>> delServiceDelayMeasureConfig(DelServiceDelayMeasureConfigInput input) {
        ServiceDelayMeasureConfigKey serviceDelayMeasureConfigKey = new ServiceDelayMeasureConfigKey(input.getPtpName());
        InstanceIdentifier<ServiceDelayMeasureConfig> iid = create(ServiceDelayMeasureConfigs.class).child(ServiceDelayMeasureConfig.class, serviceDelayMeasureConfigKey);
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ResetNeOutput>> resetNe(ResetNeInput input) {
        AccDevmService accDevmService = MountTools.getRpcService(input.getNeId(), AccDevmService.class);
        MaintaceTransformImpl maintaceTransform = new MaintaceTransformImpl();
        return RpcResultUtil.buildFutureResult(accDevmService.reset(maintaceTransform.apiResetToDev(input).build()));
    }

    @Override
    public ListenableFuture<RpcResult<ModifyAlarmDelayInsertOutput>> modifyAlarmDelayInsert(ModifyAlarmDelayInsertInput input) {
        AlarmDelayInsertKey key = new AlarmDelayInsertKey(input.getName());
        InstanceIdentifier<AlarmDelayInsert> iid = create(AlarmDelayInserts.class)
                .child(AlarmDelayInsert.class, key);
        //下发数据至设备
        MountTools.doMergeToConfig(input.getNeId(), iid, new PtpTransformImpl().apiAlarmDelayInsertToDev(input).build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetServiceDelayMeasureResultOutput>> getServiceDelayMeasureResult(GetServiceDelayMeasureResultInput input) {
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.GetServiceDelayMeasureResultInputBuilder getServiceDelayMeasureResultInputBuilder = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.GetServiceDelayMeasureResultInputBuilder();
        getServiceDelayMeasureResultInputBuilder.setPtpName(input.getPtpName());
        if (input.getConnectionName() != null) {
            getServiceDelayMeasureResultInputBuilder.setConnectionName(input.getConnectionName());
        }
        Future<RpcResult<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.GetServiceDelayMeasureResultOutput>> serviceDelayMeasureResult =
                service.getServiceDelayMeasureResult(getServiceDelayMeasureResultInputBuilder.build());
        MaintaceTransformImpl maintaceTransform = new MaintaceTransformImpl();
        return RpcResultUtil.buildFutureResult(serviceDelayMeasureResult, result -> new GetServiceDelayMeasureResultOutputBuilder(maintaceTransform.devServiceDelayResultToApi(result)).build());
    }

    @Override
    public ListenableFuture<RpcResult<QueryMeOutput>> queryMe(QueryMeInput input) {
        InstanceIdentifier<Me> meIid = create(Me.class);
        //从设备查询的数据
        Me me = MountTools.queryFromOperational(input.getNeId(), meIid);
        QueryMeOutput queryMeOutput = new MeTransformImpl().devMeToApiOut(me);
        if (queryMeOutput != null) {
            return RpcResultUtil.success(queryMeOutput);
        }
        return RpcResultUtil.success();
    }

    private List<Lpt> fileterLpt(LptPacs lptPacs, Collection<String> ptpNameList) {
        if (lptPacs == null || lptPacs.getLpt() == null) {
            return null;
        }
        if (ptpNameList == null || ptpNameList.isEmpty()) {
            return new ArrayList<>(lptPacs.getLpt().values());
        }
        List<Lpt> resultList = new LinkedList<>();
        for (String ptpName : ptpNameList) {
            Lpt ltp1 = lptPacs.getLpt().get(new LptKey(ptpName));
            if (ltp1 != null) {
                resultList.add(ltp1);
            }
        }
        return resultList;
    }

    @Override
    public ListenableFuture<RpcResult<QueryLptOutput>> queryLpt(QueryLptInput input) {
        QueryLptOutputBuilder queryLptOutputBuilder = new QueryLptOutputBuilder();
        //设备是否支持该功能
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            List<Lpt> list = new LinkedList<>();
            if (input.getPtpName() == null || input.getPtpName().isEmpty() || input.getPtpName().size() > 1) {
                InstanceIdentifier<LptPacs> iid = create(LptPacs.class);
                LptPacs lptPacs = MountTools.queryFromOperational(input.getNeId(), iid);
                if (CollUtil.isNotEmpty(input.getPtpName())) {
                    list = fileterLpt(lptPacs, input.getPtpName());
                } else {
                    if (lptPacs != null && lptPacs.getLpt() != null) {
                        list = new ArrayList<>(lptPacs.getLpt().values());
                    }
                }
            } else {
                InstanceIdentifier<Lpt> iid = create(LptPacs.class).child(Lpt.class, new LptKey(CollUtil.getFirst(input.getPtpName())));
                Lpt lpt = MountTools.queryFromOperational(input.getNeId(), iid);
                if (lpt != null) {
                    list.add(lpt);
                }
            }
            if (list != null && !list.isEmpty()) {
                queryLptOutputBuilder.setLpt(ltm(new PtpTransformImpl().devLptToApiList(list)));
            }
        }
        return RpcResultUtil.success(queryLptOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<ModifyEthPtpPacOutput>> modifyEthPtpPac(ModifyEthPtpPacInput input) {
        InstanceIdentifier<Ptp2> iid = create(Ptps.class)
                .child(Ptp.class, new PtpKey(input.getPtpName())).augmentation(Ptp2.class);
        //下发数据至设备
        MountTools.doMergeToConfig(input.getNeId(), iid, new PtpTransformImpl().apiEthPtpToDev(input).build());
        //判断是否是奥普泰设备
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            //带宽限速设置下发
            if (input.getIngressCapacity() != null || input.getEgressCapacity() != null) {
                InstanceIdentifier<EthPtpBandWidth> sid = create(EthPtpsBandWidth.class)
                        .child(EthPtpBandWidth.class, new EthPtpBandWidthKey(input.getPtpName()));
                //下发数据至设备
                MountTools.doMergeToConfig(input.getNeId(), sid, new PtpTransformImpl().apiEthPtpBandWidthToDev(input).build());
            }
            //EthPtpExt
            if (input.getPvid() != null || input.getPortMode() != null || input.getQinqEnable() != null) {
                try {
                    InstanceIdentifier<PtpExtension> extensionIid
                            = create(Extension.class).child(PtpExtensions.class)
                            .child(PtpExtension.class, new PtpExtensionKey(input.getPtpName()));
                    //下发数据至设备
                    MountTools.doMergeToConfig(input.getNeId(), extensionIid, new PtpTransformImpl().apiEthPtpExtensionToDev(input).build());
                } catch (Exception e) {
                    //不捕捉异常，防止设备还没有更新yang文件，报错
                }
            }
            //端口类型修改
            if (input.getPortType() != null) {
                OptOtnExtensionService optOtnExtensionService
                        = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
                ListenableFuture<RpcResult<SetEthPtpOpticalElectricalOutput>> result =
                        optOtnExtensionService.setEthPtpOpticalElectrical(new PtpTransformImpl().apiPtpPortTypeToDev(input));
                try {
                    if (!result.get().isSuccessful()) {
                        return RpcResultUtil.buildFutureResult(result);
                    }
                } catch (InterruptedException | ExecutionException e) {
                    throw new DeviceOperaFailException(e.getCause());
                }
            }
            //ETH物理端口帧间隔对象
            if (input.getFrameSpace() != null) {
                InstanceIdentifier<EthPtpFrameSpace> eid = create(EthPtpFrameSpaces.class)
                        .child(EthPtpFrameSpace.class, new EthPtpFrameSpaceKey(input.getPtpName()));
                MountTools.doMergeToConfig(input.getNeId(), eid, new PtpTransformImpl().apiEthPtpFrameSpaceToDev(input).build());
            }
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyCtpOutput>> modifyCtp(ModifyCtpInput input) {
        CtpKey ctpKey = new CtpKey(input.getCtpName());
        InstanceIdentifier<Ctp> iid = create(Ctps.class).child(Ctp.class, ctpKey);
        CtpTransformImpl ctpTransform = new CtpTransformImpl();
        MountTools.doMergeToConfig(input.getNeId(), iid, ctpTransform.apiCtpToDev(input).build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryMsiParameterOutput>> queryMsiParameter(QueryMsiParameterInput input) {
        QueryMsiParameterOutputBuilder queryMsiParameterOutputBuilder = new QueryMsiParameterOutputBuilder();
        List<MsiParameter> msiParameters = new ArrayList<>();
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            InstanceIdentifier<MsiParameters> iid = create(MsiParameters.class);
            MsiParameters msiParameters1 = MountTools.queryFromOperational(input.getNeId(), iid);
            if (msiParameters1 != null && MapUtil.isNotEmpty(msiParameters1.getMsiParameter())) {
                Set<String> ptpNameList = input.getName();
                if (ptpNameList != null && !ptpNameList.isEmpty()) {
                    //根据输入的端口名称范围过滤
                    msiParameters = msiParameters1.getMsiParameter().values().stream().filter(e -> ptpNameList.contains(e.getName())).collect(Collectors.toList());
                } else {
                    msiParameters.addAll(msiParameters1.getMsiParameter().values());
                }
            }
        }
        if (CollUtil.isNotEmpty(msiParameters)) {
            queryMsiParameterOutputBuilder.setMsiParameter(ltm(new MsiTransformImpl().devMsiParameterToApiList(msiParameters)));
        }
        return RpcResultUtil.success(queryMsiParameterOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<ModifyOduCtpExtensionOutput>> modifyOduCtpExtension(ModifyOduCtpExtensionInput input) {
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            InstanceIdentifier<CtpExtension> extensionIid
                    = create(Extension.class).child(CtpExtensions.class)
                    .child(CtpExtension.class, new CtpExtensionKey(input.getCtpName()));
            CtpExtensionBuilder ctpExtensionBuilder = new CtpExtensionBuilder();
            OduCtpExtensionBuilder oduCtpExtensionBuilder = new OduCtpExtensionBuilder();
            oduCtpExtensionBuilder.setPmTimAction(input.getPmTimAction())
                    .setPmTimMode(new CtpExtensionTransformImpl().apiTimModeToDev(input.getPmTimMode()));
            ctpExtensionBuilder.withKey(new CtpExtensionKey(input.getCtpName()))
                    .setOduCtpExtension(oduCtpExtensionBuilder.build());
            //下发数据至设备
            MountTools.doMergeToConfig(input.getNeId(), extensionIid, ctpExtensionBuilder.build());
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyServiceDelayMeasureConfigOutput>> modifyServiceDelayMeasureConfig(ModifyServiceDelayMeasureConfigInput input) {
        ServiceDelayMeasureConfigKey serviceDelayMeasureConfigKey = new ServiceDelayMeasureConfigKey(input.getPtpName());
        InstanceIdentifier<ServiceDelayMeasureConfig> iid = create(ServiceDelayMeasureConfigs.class).child(ServiceDelayMeasureConfig.class, serviceDelayMeasureConfigKey);
        MaintaceTransformImpl maintaceTransform = new MaintaceTransformImpl();
        MountTools.doMergeToConfig(input.getNeId(), iid, maintaceTransform.apiServiceDelayConfigToDev(new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.service.delay.measure.configs.grouping.ServiceDelayMeasureConfigBuilder(input).build()));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyLaserAutoEnableOutput>> modifyLaserAutoEnable(ModifyLaserAutoEnableInput input) {
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            if (NcTools.isSupportOptelExt(input.getNeId())) {
                InstanceIdentifier<PtpExtension> extensionIid
                        = create(Extension.class).child(PtpExtensions.class)
                        .child(PtpExtension.class, new PtpExtensionKey(input.getPtpName()));
                //下发数据至设备
                MountTools.doMergeToConfig(input.getNeId(), extensionIid, new PtpTransformImpl().apiPtpExtensionToDev(input).build());
            }
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryCtpsOutput>> queryCtps(QueryCtpsInput input) {
        List<Ctp> ctpList = new ArrayList<>();
        if (!StrUtil.isEmpty(input.getCtpName())) {
            InstanceIdentifier<Ctp> iid = create(Ctps.class).child(Ctp.class, new CtpKey(input.getCtpName()));
            Ctp ctp = MountTools.queryFromOperational(input.getNeId(), iid);
            if (ctp != null) {
                ctpList.add(ctp);
            }
        } else {
            //查询所有
            InstanceIdentifier<Ctps> iid = create(Ctps.class);
            Ctps ctps = MountTools.queryFromOperational(input.getNeId(), iid);
            if (ctps != null && ctps.getCtp() != null) {
                ctpList = new ArrayList<>(ctps.getCtp().values());
            }
        }
        QueryCtpsOutputBuilder queryCtpsOutputBuilder = new QueryCtpsOutputBuilder();
        CtpTransformImpl ctpTransform = new CtpTransformImpl();
        queryCtpsOutputBuilder.setCtp(ltm(ctpTransform.devCtpToApi(ctpList)));
        return RpcResultUtil.success(queryCtpsOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<ModifyOduPtpPacOutput>> modifyOduPtpPac(ModifyOduPtpPacInput input) {
        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Ptp1> iid =
                create(Ptps.class).child(Ptp.class, new PtpKey(input.getPtpName()))
                        .augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Ptp1.class);
        //下发数据至设备
        MountTools.doMergeToConfig(input.getNeId(), iid, new PtpTransformImpl().apiOduPtpToApi(input).build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyOduPtpExtensionOutput>> modifyOduPtpExtension(ModifyOduPtpExtensionInput input) {
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            InstanceIdentifier<PtpExtension> extensionIid
                    = create(Extension.class).child(PtpExtensions.class)
                    .child(PtpExtension.class, new PtpExtensionKey(input.getPtpName()));
            //下发数据至设备
            MountTools.doMergeToConfig(input.getNeId(), extensionIid, new PtpTransformImpl().apiPtpExtensionToDev(input).build());
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryTcmParameterOutput>> queryTcmParameter(QueryTcmParameterInput input) {
        QueryTcmParameterOutputBuilder queryTcmParameterOutputBuilder = new QueryTcmParameterOutputBuilder();
        //设备是否支持该功能
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            InstanceIdentifier<TcmParameters> iid = create(TcmParameters.class);
            TcmParameters tcmParameters = MountTools.queryFromOperational(input.getNeId(), iid);
            List<TcmParameter> list;
            if (tcmParameters != null && MapUtil.isNotEmpty(tcmParameters.getTcmParameter())) {
                if (input.getName() != null && !StrUtil.isEmpty(input.getName())) {
                    list = tcmParameters.getTcmParameter().values().stream().filter(e -> (input.getName().equals(e.getName()))).collect(Collectors.toList());
                    tcmParameters = new TcmParametersBuilder().setTcmParameter(ltm(list)).build();
                }
            }
            TcmTransformImpl tcmTransform = new TcmTransformImpl();
            if (tcmParameters != null && tcmParameters.getTcmParameter() != null) {
                queryTcmParameterOutputBuilder.setTcmParameter(ltm(tcmTransform.devTcmToApiList(new ArrayList<>(tcmParameters.getTcmParameter().values()))));
            }
        }
        return RpcResultUtil.success(queryTcmParameterOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<ModifySdhPtpPacOutput>> modifySdhPtpPac(ModifySdhPtpPacInput input) {
        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ptp2> iid = create(Ptps.class).child(Ptp.class, new PtpKey(input.getPtpName()))
                .augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ptp2.class);
        //下发数据至设备
        MountTools.doMergeToConfig(input.getNeId(), iid, new PtpTransformImpl().apiSdhPtpToDev(input).build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryCtpExtensionOutput>> queryCtpExtension(QueryCtpExtensionInput input) {
        QueryCtpExtensionOutputBuilder queryCtpExtensionOutputBuilder = new QueryCtpExtensionOutputBuilder();
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            CtpExtensions ctpExtensions = null;
            try {
                if (input.getCtpName() == null || StrUtil.isEmpty(input.getCtpName())) {
                    InstanceIdentifier<CtpExtensions> extensionIid =
                            create(Extension.class).child(CtpExtensions.class);
                    ctpExtensions = MountTools.queryFromOperational(input.getNeId(), extensionIid);
                } else {
                    InstanceIdentifier<CtpExtension> extensionIid
                            = create(Extension.class).child(CtpExtensions.class)
                            .child(CtpExtension.class, new CtpExtensionKey(input.getCtpName()));
                    CtpExtension ctpExtension = MountTools.queryFromOperational(input.getNeId(), extensionIid);
                    List<CtpExtension> ctpExtensionList = new LinkedList<>();
                    if (ctpExtension != null) {
                        ctpExtensionList.add(ctpExtension);
                    }
                    ctpExtensions = new CtpExtensionsBuilder().setCtpExtension(ltm(ctpExtensionList)).build();
                }
                if (ctpExtensions != null && ctpExtensions.getCtpExtension() != null) {
                    queryCtpExtensionOutputBuilder.setCtpExtension(ltm(new CtpExtensionTransformImpl().devCtpExtensionToApiList(ctpExtensions.getCtpExtension().values())));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return RpcResultUtil.success(queryCtpExtensionOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<GetAlarmDelayInsertOutput>> getAlarmDelayInsert(GetAlarmDelayInsertInput input) {
        GetAlarmDelayInsertOutputBuilder outBuilder = new GetAlarmDelayInsertOutputBuilder();
        if (input.getName() != null) {
            InstanceIdentifier<AlarmDelayInsert> iid = create(AlarmDelayInserts.class)
                    .child(AlarmDelayInsert.class, new AlarmDelayInsertKey(input.getName()));
            AlarmDelayInsert alarmDelayInsert = MountTools.queryFromOperational(input.getNeId(), iid);
            if (alarmDelayInsert != null) {
                List<AlarmDelayInsert> list = new LinkedList<>();
                list.add(alarmDelayInsert);
                outBuilder.setAlarmDelayInsert(ltm(new PtpTransformImpl().devAlarmDelayInsertToApiList(list)));
            }
        } else {
            //查询全部
            InstanceIdentifier<AlarmDelayInserts> iid = create(AlarmDelayInserts.class);
            AlarmDelayInserts alarmDelayInserts = MountTools.queryFromOperational(input.getNeId(), iid);
            if (alarmDelayInserts != null && alarmDelayInserts.getAlarmDelayInsert() != null) {
                outBuilder.setAlarmDelayInsert(ltm(new PtpTransformImpl().devAlarmDelayInsertToApiList(alarmDelayInserts.getAlarmDelayInsert().values())));
            }
        }
        return RpcResultUtil.success(outBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<ModifyPdhPtpOutput>> modifyPdhPtp(ModifyPdhPtpInput input) {
        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.Ptp1> iid = create(Ptps.class)
                .child(Ptp.class, new PtpKey(input.getPtpName())).augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.Ptp1.class);
        MountTools.doMergeToConfig(input.getNeId(), iid, new PtpTransformImpl().apiPdhPtpToDev(input).build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryMcPortOutput>> queryMcPort(QueryMcPortInput input) {
        QueryMcPortOutputBuilder builder = new QueryMcPortOutputBuilder();
        String name = input.getName();
        if (name != null && !name.isEmpty()) {
            InstanceIdentifier<McPort> iid =
                    create(McPorts.class).child(McPort.class, new McPortKey(name));
            McPort mcPort = MountTools.queryFromOperational(input.getNeId(), iid);
            List<McPort> mcPortList = new LinkedList<>();
            if (mcPort != null) {
                mcPortList.add(mcPort);
            }
            builder.setMcPort(ltm(new PtpTransformImpl().devMcPortToApiList(mcPortList)));
        } else {
            InstanceIdentifier<McPorts> iid = create(McPorts.class);
            McPorts mcPorts = MountTools.queryFromOperational(input.getNeId(), iid);
            if (mcPorts != null && mcPorts.getMcPort() != null) {
                builder.setMcPort(ltm(new PtpTransformImpl().devMcPortToApiList(mcPorts.getMcPort().values())));
            }
        }
        return RpcResultUtil.success(builder.build());
    }

    @Override
    public ListenableFuture<RpcResult<QueryEqsOutput>> queryEqs(QueryEqsInput input) {
        Collection<Eq> eqList = null;
        if (input.getEqName() != null && !"".equals(input.getEqName())) {
            //查询对应Eq
            InstanceIdentifier<Eq> iid = create(Eqs.class).child(Eq.class, new EqKey(input.getEqName()));
            Eq eq = MountTools.queryFromOperational(input.getNeId(), iid);
            eqList = new ArrayList<>();
            eqList.add(eq);
        } else {
            //查询所有
            InstanceIdentifier<Eqs> iid = create(Eqs.class);
            //从设备查询的数据
            Eqs eqs = MountTools.queryFromOperational(input.getNeId(), iid);
            if (eqs != null && eqs.getEq() != null) {
                eqList = eqs.getEq().values();
            }
        }
        QueryEqsOutputBuilder queryEqsOutputBuilder = new QueryEqsOutputBuilder();
        queryEqsOutputBuilder.setEq(ltm(new EqTransformImpl().devEqToApiList(eqList)));
        return RpcResultUtil.success(queryEqsOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<QueryPtpExtensionOutput>> queryPtpExtension(QueryPtpExtensionInput input) {
        QueryPtpExtensionOutputBuilder queryPtpExtensionOutputBuilder = new QueryPtpExtensionOutputBuilder();
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            PtpExtensions ptpExtensions = null;
            try {
                if (input.getPtpName() == null || StrUtil.isEmpty(input.getPtpName())) {
                    InstanceIdentifier<PtpExtensions> extensionIid =
                            create(Extension.class).child(PtpExtensions.class);
                    ptpExtensions = MountTools.queryFromOperational(input.getNeId(), extensionIid);
                } else {
                    InstanceIdentifier<PtpExtension> extensionIid
                            = create(Extension.class).child(PtpExtensions.class)
                            .child(PtpExtension.class, new PtpExtensionKey(input.getPtpName()));
                    PtpExtension ptpExtension = MountTools.queryFromOperational(input.getNeId(), extensionIid);
                    List<PtpExtension> ptpExtensionList = new LinkedList<>();
                    if (ptpExtension != null && ptpExtension.getEthPtpExtension() != null) {
                        ptpExtensionList.add(ptpExtension);
                    }
                    ptpExtensions = new PtpExtensionsBuilder().setPtpExtension(ltm(ptpExtensionList)).build();
                }
                if (ptpExtensions != null && ptpExtensions.getPtpExtension() != null) {
                    queryPtpExtensionOutputBuilder.setPtpExtension(ltm(new PtpTransformImpl().devPtpExtensionToApiList(ptpExtensions.getPtpExtension().values())));
                }
            } catch (Exception e) {
                System.out.println(e);
            }
        }
        return RpcResultUtil.success(queryPtpExtensionOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<QueryEthPerformanceOutput>> queryEthPerformance(QueryEthPerformanceInput input) {
        InstanceIdentifier<Performance> iid = create(Ctps.class)
                .child(Ctp.class, new CtpKey(input.getCtpName()))
                .augmentation(Ctp1.class)
                .child(EthCtpPac.class)
                .child(Performance.class);
        Performance ethPerformance = MountTools.queryFromOperational(input.getNeId(), iid);
        QueryEthPerformanceOutputBuilder queryEthPerformanceOutputBuilder = new QueryEthPerformanceOutputBuilder();
        if (ethPerformance != null) {
            queryEthPerformanceOutputBuilder = new QueryEthPerformanceOutputBuilder();
            CtpTransformImpl ctpTransform = new CtpTransformImpl();
            queryEthPerformanceOutputBuilder.setDelay(ethPerformance.getDelay());
            queryEthPerformanceOutputBuilder.setFarPacketLossRate(ctpTransform.devRealToApi(ethPerformance.getFarPacketLossRate()));
            queryEthPerformanceOutputBuilder.setNearPacketLossRate(ctpTransform.devRealToApi(ethPerformance.getNearPacketLossRate()));
            queryEthPerformanceOutputBuilder.setRxBytes(ethPerformance.getRxBytes());
            queryEthPerformanceOutputBuilder.setTxBytes(ethPerformance.getTxBytes());
        }
        return RpcResultUtil.success(queryEthPerformanceOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<QueryEthPtpBandwidthOutput>> queryEthPtpBandwidth(QueryEthPtpBandwidthInput input) {
        QueryEthPtpBandwidthOutputBuilder outputBuilder = new QueryEthPtpBandwidthOutputBuilder();
        // 校验设备是否有此能力
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            EthPtpsBandWidth ethPtpsBandWidth;
            try {
                if (StrUtil.isEmpty(input.getPtpName())) {
                    InstanceIdentifier<EthPtpsBandWidth> iid = create(EthPtpsBandWidth.class);
                    ethPtpsBandWidth = MountTools.queryFromOperational(input.getNeId(), iid);
                } else {
                    InstanceIdentifier<EthPtpBandWidth> iid = create(EthPtpsBandWidth.class)
                            .child(EthPtpBandWidth.class, new EthPtpBandWidthKey(input.getPtpName()));
                    EthPtpBandWidth ethPtpBandWidth = MountTools.queryFromOperational(input.getNeId(), iid);
                    List<EthPtpBandWidth> list = new LinkedList<>();
                    if (ethPtpBandWidth != null) {
                        list.add(ethPtpBandWidth);
                    }
                    ethPtpsBandWidth = new EthPtpsBandWidthBuilder().setEthPtpBandWidth(ltm(list)).build();
                }
                if (ethPtpsBandWidth != null) {
                    outputBuilder.setEthPtpBandWidth(ltm(new PtpTransformImpl().devEthPtpsBandWidthToApiList(ethPtpsBandWidth)));
                }
            } catch (Exception e) {
                //不作处理
            }
        }
        return RpcResultUtil.success(outputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<ModifyLptOutput>> modifyLpt(ModifyLptInput input) {
        LptKey key = new LptKey(input.getPtpName());
        InstanceIdentifier<Lpt> iid = create(LptPacs.class).child(Lpt.class, key);
        //下发数据至设备
        MountTools.doMergeToConfig(input.getNeId(), iid, new PtpTransformImpl().apiLptToDev(input).build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyMcPortOutput>> modifyMcPort(ModifyMcPortInput input) {
        McPortKey key = new McPortKey(input.getName());
        InstanceIdentifier<McPort> iid =
                create(McPorts.class).child(McPort.class, new McPortKey(key));
        MountTools.doMergeToConfig(input.getNeId(), iid, new PtpTransformImpl().apiMcPortToDev(input).build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyUserPasswordNeOutput>> modifyUserPasswordNe(ModifyUserPasswordNeInput input) {
        AccDevmService accDevmService = MountTools.getRpcService(input.getNeId(), AccDevmService.class);
        DeviceUserManageTransformImpl deviceUserManageTransform = new DeviceUserManageTransformImpl();
        return RpcResultUtil.buildFutureResult(accDevmService.modifyUserPassword(deviceUserManageTransform.apiModifyUserPasswordToDev(input).build()));
    }

    /**
     * Invoke {@code query-account} RPC.
     *
     * @param input of {@code query-account}
     * @return output of {@code query-account}
     */
    @Override
    public ListenableFuture<RpcResult<QueryAccountOutput>> queryAccount(QueryAccountInput input) {
        return RpcResultUtil.success();
    }

    /**
     * Invoke {@code modify-account} RPC.
     *
     * @param input of {@code modify-account}
     * @return output of {@code modify-account}
     */
    @Override
    public ListenableFuture<RpcResult<ModifyAccountOutput>> modifyAccount(ModifyAccountInput input) {
        return RpcResultUtil.success();
    }

    /**
     * Invoke {@code del-account} RPC.
     *
     * @param input of {@code del-account}
     * @return output of {@code del-account}
     */
    @Override
    public ListenableFuture<RpcResult<DelAccountOutput>> delAccount(DelAccountInput input) {
        return RpcResultUtil.success();
    }

    /**
     * Invoke {@code add-account} RPC.
     *
     * @param input of {@code add-account}
     * @return output of {@code add-account}
     */
    @Override
    public ListenableFuture<RpcResult<AddAccountOutput>> addAccount(AddAccountInput input) {
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyPtpOutput>> modifyPtp(ModifyPtpInput input) {
        PtpKey key = new PtpKey(input.getPtpName());
        InstanceIdentifier<Ptp> iid = create(Ptps.class).child(Ptp.class, key);
        PtpTransformImpl ptpTransform = new PtpTransformImpl();
        //下发数据至设备
        MountTools.doMergeToConfig(input.getNeId(), iid, ptpTransform.apiModifyPtpToDev(input).build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyOsuCtpPacOutput>> modifyOsuCtpPac(ModifyOsuCtpPacInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<DeleteMeNtpOutput>> deleteMeNtp(DeleteMeNtpInput input) {
        NtpServerKey key = new NtpServerKey(input.getName());
        InstanceIdentifier<NtpServer> iid = create(Me.class).child(NtpServers.class).child(NtpServer.class, key);
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyPtpOpticalPowerPacOutput>> modifyPtpOpticalPowerPac(ModifyPtpOpticalPowerPacInput input) {
        PtpKey key = new PtpKey(input.getPtpName());
        InstanceIdentifier<Ptp> iid = create(Ptps.class).child(Ptp.class, key);
        PtpTransformImpl ptpTransform = new PtpTransformImpl();
        //下发设备
        MountTools.doMergeToConfig(input.getNeId(), iid, ptpTransform.apiOpticalPowerToDev(input).build());
        return RpcResultUtil.success();
    }

    /**
     * Invoke {@code delete-eth-ptp-pac-vlan} RPC.
     *
     * @param input of {@code delete-eth-ptp-pac-vlan}
     * @return output of {@code delete-eth-ptp-pac-vlan}
     */
    @Override
    public ListenableFuture<RpcResult<DeleteEthPtpPacVlanOutput>> deleteEthPtpPacVlan(DeleteEthPtpPacVlanInput input) {
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyEthCtpPacOutput>> modifyEthCtpPac(ModifyEthCtpPacInput input) {
        InstanceIdentifier<Ctp1> iid = create(Ctps.class).child(Ctp.class, new CtpKey(input.getCtpName())).augmentation(Ctp1.class);
        EthCtpPacBuilder ethCtpPacBuilder = new EthCtpPacBuilder();
        CtpTransformImpl ctpTransform = new CtpTransformImpl();
        ClientVlanSpecBuilder clientVlanSpecBuilder = new ClientVlanSpecBuilder(ctpTransform.apiVlanSpecToDev(input.getClientVlanSpec()));
        ethCtpPacBuilder.setClientVlanSpec(clientVlanSpecBuilder.build());
        Ctp1Builder ctp1Builder = new Ctp1Builder().setEthCtpPac(ethCtpPacBuilder.build());
        MountTools.doMergeToConfig(input.getNeId(), iid, ctp1Builder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryMeNtpOutput>> queryMeNtp(QueryMeNtpInput input) {
        InstanceIdentifier<NtpServers> iid = create(Me.class).child(NtpServers.class);
        //从设备查询的数据
        NtpServers ntpServers = MountTools.queryFromOperational(input.getNeId(), iid);
        org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.me.grouping.NtpServers ntpServersApi
                = new MeTransformImpl().devNtpServersToApi(ntpServers);
        QueryMeNtpOutputBuilder outputBuilder = new QueryMeNtpOutputBuilder();
        if (ntpServersApi != null) {
            outputBuilder.setNtpServer(ntpServersApi.getNtpServer());
        }
        return RpcResultUtil.success(outputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<ModifyOduCtpPacOutput>> modifyOduCtpPac(ModifyOduCtpPacInput input) {
        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Ctp1> iid = create(Ctps.class).child(Ctp.class, new CtpKey(input.getCtpName())).augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Ctp1.class);
        OduCtpPacBuilder oduCtpPacBuilder = new OduCtpPacBuilder();
        CtpTransformImpl ctpTransform = new CtpTransformImpl();
        oduCtpPacBuilder.setOduSignalType(ctpTransform.apiClientSignalTypeToDev(input.getOduSignalType()))
                .setAdaptationType(ctpTransform.apiAdaptationTypeToDev(input.getAdaptationType()))
                .setSwitchCapability(ctpTransform.apiSwitchTypeToDev(input.getSwitchCapability()))
                .setTsDetail(input.getTsDetail())
                .setCurrentNumberOfTributarySlots(input.getCurrentNumberOfTributarySlots())
                .setPmtrailTraceActualTx(input.getPmtrailTraceActualTx())
                .setPmtrailTraceExpectedRx(input.getPmtrailTraceExpectedRx());
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Ctp1Builder ctp1Builder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Ctp1Builder().setOduCtpPac(oduCtpPacBuilder.build());
        MountTools.doMergeToConfig(input.getNeId(), iid, ctp1Builder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyMsiParameterOutput>> modifyMsiParameter(ModifyMsiParameterInput input) {
        MsiParameterKey msiParameterKey = new MsiParameterKey(input.getName(), input.getTributaryPortNum());
        InstanceIdentifier<MsiParameter> iid = create(MsiParameters.class).child(MsiParameter.class, msiParameterKey);
        MsiParameterBuilder builder = new MsiParameterBuilder();
        builder.withKey(msiParameterKey)
                .setName(input.getName())
                .setTributaryPortNum(input.getTributaryPortNum())
                .setOdukLevel(new MsiTransformImpl().apiSwitchTypeToDev(input.getOdukLevel()))
                .setTsNum(input.getTsNum())
                .setTsOccupiedRx(input.getTsOccupiedRx())
                .setTsOccupiedTx(input.getTsOccupiedTx());
        //下发数据至设备
        MountTools.doMergeToConfig(input.getNeId(), iid, builder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifySdhCtpPacOutput>> modifySdhCtpPac(ModifySdhCtpPacInput input) {
        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ctp1> iid = create(Ctps.class).child(Ctp.class, new CtpKey(input.getCtpName())).augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ctp1.class);
        VcCtpPacBuilder vcCtpPacBuilder = new VcCtpPacBuilder();
        vcCtpPacBuilder.setVcType(new CtpTransformImpl().apiSwitchTypeToDev(input.getVcType())).setMappingPath(input.getMappingPath());
        vcCtpPacBuilder.setJ1ActualTx(input.getJ1ActualTx()).setJ1ExpectedRx(input.getJ1ExpectedRx());
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ctp1Builder ctp1Builder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ctp1Builder().setVcCtpPac(vcCtpPacBuilder.build());
        MountTools.doMergeToConfig(input.getNeId(), iid, ctp1Builder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetServiceDelayMeasureConfigOutput>> getServiceDelayMeasureConfig(GetServiceDelayMeasureConfigInput input) {
        InstanceIdentifier<ServiceDelayMeasureConfigs> iid = create(ServiceDelayMeasureConfigs.class);
        ServiceDelayMeasureConfigs serviceDelayMeasureConfigs = MountTools.queryFromOperational(input.getNeId(), iid);
        if (Objects.nonNull(serviceDelayMeasureConfigs)) {
            GetServiceDelayMeasureConfigOutputBuilder outputBuilder = new GetServiceDelayMeasureConfigOutputBuilder();
            MaintaceTransformImpl maintaceTransform = new MaintaceTransformImpl();
            outputBuilder.setServiceDelayMeasureConfig(ltm(maintaceTransform.devServiceDelayConfigToApi(serviceDelayMeasureConfigs)));
            return RpcResultUtil.success(outputBuilder.build());
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<AddServiceDelayMeasureConfigOutput>> addServiceDelayMeasureConfig(AddServiceDelayMeasureConfigInput input) {
        MaintaceTransformImpl maintaceTransform = new MaintaceTransformImpl();
        ServiceDelayMeasureConfigKey serviceDelayMeasureConfigKey = new ServiceDelayMeasureConfigKey(input.getPtpName());
        InstanceIdentifier<ServiceDelayMeasureConfig> iid = create(ServiceDelayMeasureConfigs.class).child(ServiceDelayMeasureConfig.class, serviceDelayMeasureConfigKey);

        MountTools.putConfig(input.getNeId(), iid, maintaceTransform.apiServiceDelayConfigToDev(new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.service.delay.measure.configs.grouping.ServiceDelayMeasureConfigBuilder(input).build()));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetEthPtpFrameSpaceOutput>> getEthPtpFrameSpace(GetEthPtpFrameSpaceInput input) {
        GetEthPtpFrameSpaceOutputBuilder outputBuilder = new GetEthPtpFrameSpaceOutputBuilder();
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            Set<String> ptpNameList = input.getPtpName();
            if (ptpNameList == null || ptpNameList.isEmpty()) {
                InstanceIdentifier<EthPtpFrameSpaces> iid = create(EthPtpFrameSpaces.class);
                EthPtpFrameSpaces ethPtpFrameSpaces = MountTools.queryFromOperational(input.getNeId(), iid);
                if (ethPtpFrameSpaces != null && ethPtpFrameSpaces.getEthPtpFrameSpace() != null) {
                    outputBuilder.setEthPtpFrameSpace(ltm(new PtpTransformImpl().devEthPtpFrameSpaceToApiList(ethPtpFrameSpaces.getEthPtpFrameSpace().values())));
                }
            } else if (ptpNameList.size() == 1) {
                InstanceIdentifier<EthPtpFrameSpace> iid =
                        create(EthPtpFrameSpaces.class)
                                .child(EthPtpFrameSpace.class, new EthPtpFrameSpaceKey(CollUtil.getFirst(ptpNameList)));
                EthPtpFrameSpace ethPtpFrameSpace = MountTools.queryFromOperational(input.getNeId(), iid);
                if (ethPtpFrameSpace != null) {
                    List<EthPtpFrameSpace> list = new LinkedList<>();
                    list.add(ethPtpFrameSpace);
                    outputBuilder.setEthPtpFrameSpace(ltm(new PtpTransformImpl().devEthPtpFrameSpaceToApiList(list)));
                }
            } else {
                InstanceIdentifier<EthPtpFrameSpaces> iid = create(EthPtpFrameSpaces.class);
                EthPtpFrameSpaces ethPtpFrameSpaces = MountTools.queryFromOperational(input.getNeId(), iid);
                if (ethPtpFrameSpaces != null) {
                    outputBuilder.setEthPtpFrameSpace(ltm(
                            new PtpTransformImpl().devEthPtpFrameSpaceToApiList(
                                    filterEthPtpFrameSpace(ethPtpFrameSpaces.getEthPtpFrameSpace(), ptpNameList))));
                }
            }
        }
        return RpcResultUtil.success(outputBuilder.build());
    }

    /**
     * 根据ptpName过滤EthPtpFrameSpace数据
     *
     * @param dataList    待过滤数据
     * @param ptpNameList ptpName list
     * @return 过滤后的数据
     */
    private List<EthPtpFrameSpace> filterEthPtpFrameSpace(Map<EthPtpFrameSpaceKey, EthPtpFrameSpace> dataList, Set<String> ptpNameList) {
        if (dataList != null && !dataList.isEmpty()) {
            if (ptpNameList != null && !ptpNameList.isEmpty()) {
                List<EthPtpFrameSpace> list = new LinkedList<>();
                for (String ptpName : ptpNameList) {
                    EthPtpFrameSpace ethPtpFrameSpace = dataList.get(new EthPtpFrameSpaceKey(ptpName));
                    if (ethPtpFrameSpace != null) {
                        list.add(ethPtpFrameSpace);
                    }
                }
                return list;
            }
            return new ArrayList<>(dataList.values());
        }
        return new ArrayList<>();
    }

    /**
     * Invoke {@code get-optical-module-info} RPC.
     *
     * @param input of {@code get-optical-module-info}
     * @return output of {@code get-optical-module-info}
     */
    @Override
    public ListenableFuture<RpcResult<GetOpticalModuleInfoOutput>> getOpticalModuleInfo(GetOpticalModuleInfoInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<GetVcTrailTraceJ1Output>> getVcTrailTraceJ1(GetVcTrailTraceJ1Input input) {
        InstanceIdentifier<Ctp> iid = create(Ctps.class).child(Ctp.class, new CtpKey(input.getCtpName()));
        Ctp ctp = MountTools.queryFromOperational(input.getNeId(), iid);
        GetVcTrailTraceJ1OutputBuilder getVcTrailTraceJ1OutputBuilder = new GetVcTrailTraceJ1OutputBuilder();
        if (Objects.nonNull(ctp)) {
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ctp1 sdhCtpPac = ctp.augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.Ctp1.class);
            if (sdhCtpPac != null && sdhCtpPac.getVcCtpPac() != null) {
                getVcTrailTraceJ1OutputBuilder.setActualRx(sdhCtpPac.getVcCtpPac().getJ1ActualRx());
                getVcTrailTraceJ1OutputBuilder.setActualTx(sdhCtpPac.getVcCtpPac().getJ1ActualTx());
                getVcTrailTraceJ1OutputBuilder.setExpectedRx(sdhCtpPac.getVcCtpPac().getJ1ExpectedRx());
            }
        }
        return RpcResultUtil.success(getVcTrailTraceJ1OutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<GetFecStatusOutput>> getFecStatus(GetFecStatusInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<SetFecStatusOutput>> setFecStatus(SetFecStatusInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<GetSmPmTrailTraceOutput>> getSmPmTrailTrace(GetSmPmTrailTraceInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<SetSmPmTrailTraceOutput>> setSmPmTrailTrace(SetSmPmTrailTraceInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<GetSdhTerminationPointDataOutput>> getSdhTerminationPointData(GetSdhTerminationPointDataInput input) {
        return null;
    }

    @Override
    public ListenableFuture<RpcResult<SetSdhTerminationPointDataOutput>> setSdhTerminationPointData(SetSdhTerminationPointDataInput input) {
        return null;
    }
}
