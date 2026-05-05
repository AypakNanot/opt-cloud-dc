/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.text.CharSequenceUtil;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.commons.collections.CollUtils;
import cn.hutool.core.map.MapUtil;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptOtnDeviceServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.test.TestData;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.*;
import com.optel.tmaster.dc.device.impl.otn.ctc.util.CompatibleRpcUtil;
import com.optel.tmaster.dc.device.impl.otn.ctc.util.OtnRevisionUtils;
import com.optel.tmaster.dc.general.base.exception.device.DeviceOperaFailException;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import com.optel.tmaster.dc.general.nc.nccore.NcTools;
import org.apache.commons.lang3.StrUtil;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.GetOpticalModuleInfoInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.GetOpticalModuleInfoOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.GetServiceDelayMeasureResultInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.GetServiceDelayMeasureResultOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.GetServiceDelayMeasureResultOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetServiceDelayMeasureResultInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.alarm.delay.inserts.grouping.AlarmDelayInsert;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.alarm.delay.inserts.grouping.AlarmDelayInsertKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ctp.extension.grouping.CtpExtension;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ctp.extension.grouping.CtpExtensionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ctp.extension.grouping.CtpExtensionKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ctp.extension.parameter.grouping.OduCtpExtensionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.ptp.band.width.grouping.EthPtpBandWidth;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.ptp.band.width.grouping.EthPtpBandWidthKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.ptp.frame.spaces.grouping.EthPtpFrameSpace;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.ptp.frame.spaces.grouping.EthPtpFrameSpaceKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.extension.CtpExtensions;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.extension.CtpExtensionsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.extension.PtpExtensions;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.extension.PtpExtensionsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.lpt.grouping.Lpt;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.lpt.grouping.LptKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.msi.auto.parameter.grouping.MsiAutoParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.msi.auto.parameter.grouping.MsiAutoParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.msi.auto.parameter.grouping.MsiAutoParameterKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.msi.parameter.grouping.MsiParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.msi.parameter.grouping.MsiParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.msi.parameter.grouping.MsiParameterKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ptp.extension.grouping.PtpExtension;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ptp.extension.grouping.PtpExtensionKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.service.delay.measure.configs.grouping.ServiceDelayMeasureConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.service.delay.measure.configs.grouping.ServiceDelayMeasureConfigKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tcm.parameters.grouping.TcmParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tcm.parameters.grouping.TcmParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tcm.parameters.grouping.TcmParameterKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.TrailTraceType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.AccAccountService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.Accounts;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.DelAccountInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.UserRole;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.accounts.Account;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.accounts.AccountBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.accounts.AccountKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.*;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ctps.Ctp;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ctps.CtpKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.eqs.Eq;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.eqs.EqKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.mc.ports.McPortKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.me.NtpServers;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.me.ntp.servers.NtpServer;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.me.ntp.servers.NtpServerBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ptps.grouping.Ptp;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ptps.grouping.PtpKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.Ctp1;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.Ctp1Builder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.Ptp2;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.EthCtpPac;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.EthCtpPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.ClientVlanSpecBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.Performance;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ptps.ptp.EthPtpPac;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ptps.ptp.eth.ptp.pac.VlanSpec;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.ctps.ctp.OsuCtpPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.AccOtnService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.ctps.ctp.OduCtpPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.AccSdhService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.SetVcTrailTraceInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.ctps.ctp.VcCtpPacBuilder;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.binding.KeyedInstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.opendaylight.yangtools.yang.common.RpcResultBuilder;
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
public class CtcOptOtnDeviceServiceImpl extends BaseOptOtnDeviceServiceImpl implements IDeviceServiceOtnCtc {
    private static final String IS_OPT_TEST = "isOptTest";
    private final static Logger LOG = LoggerFactory.getLogger(CtcOptOtnAlarmServiceImpl.class);
    private final CtpTransformImpl CTP_TRANSFORM;

    public CtcOptOtnDeviceServiceImpl() {
        CTP_TRANSFORM = new CtpTransformImpl();
    }

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
                String ptpName = ptpNames.iterator().next();
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
        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.eqs.Eq> iid = create(Eqs.class).child(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.eqs.Eq.class, new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.eqs.EqKey(input.getEqName()));
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
        InstanceIdentifier<NtpServer> iid = create(Me.class).child(NtpServers.class).child(NtpServer.class, new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.me.ntp.servers.NtpServerKey(input.getName()));
        NtpServerBuilder ntpServerBuilder = new NeNtpTransformImpl().apiNtpServerToDev(input);
        //下发数据至设备
        MountTools.doMergeToConfig(input.getNeId(), iid, ntpServerBuilder.build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryEthPtpExtensionOutput>> queryEthPtpExtension(QueryEthPtpExtensionInput input) {
        QueryEthPtpExtensionOutputBuilder outputBuilder = new QueryEthPtpExtensionOutputBuilder();
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            try {
                //填充EthPtpBandwidth
                EthPtpsBandWidth ethPtpsBandWidth;
                //填充ethPtpExtensions
                // EthPtpExtensions ethPtpExtensions;
                //填充PtpExtensions
                PtpExtensions ptpExtensions = null;
                try {
                    if (input.getPtpName() == null || StrUtil.isEmpty(input.getPtpName())) {
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
                        outputBuilder.setPtpExtension(ltm(new PtpTransformImpl().devPtpExtensionToApiList(ptpExtensions.getPtpExtension().values())));
                    }
                } catch (Exception e) {
                    //不作处理
                    System.out.println(e);
                }
            } catch (Exception e) {
                //防止设备未更新报错
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
        ResetInputBuilder resetInputBuilder = new MaintaceTransformImpl().apiResetToDev(input);
        return RpcResultUtil.buildFutureResult(accDevmService.reset(resetInputBuilder.build()));
    }

    @Override
    public ListenableFuture<RpcResult<ModifyAlarmDelayInsertOutput>> modifyAlarmDelayInsert(ModifyAlarmDelayInsertInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        AlarmDelayInsertKey key = new AlarmDelayInsertKey(input.getName());
        InstanceIdentifier<AlarmDelayInsert> iid = create(AlarmDelayInserts.class)
                .child(AlarmDelayInsert.class, key);
        //下发数据至设备
        MountTools.doMergeToConfig(input.getNeId(), iid, new PtpTransformImpl().apiAlarmDelayInsertToDev(input).build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetServiceDelayMeasureResultOutput>> getServiceDelayMeasureResult(GetServiceDelayMeasureResultInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        OptOtnExtensionService service = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        GetServiceDelayMeasureResultInputBuilder getServiceDelayMeasureResultInputBuilder = new GetServiceDelayMeasureResultInputBuilder();
        getServiceDelayMeasureResultInputBuilder.setPtpName(input.getPtpName());
        if (input.getConnectionName() != null) {
            getServiceDelayMeasureResultInputBuilder.setConnectionName(input.getConnectionName());
        }
        Future<RpcResult<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GetServiceDelayMeasureResultOutput>> serviceDelayMeasureResult =
                service.getServiceDelayMeasureResult(getServiceDelayMeasureResultInputBuilder.build());
        MaintaceTransformImpl maintaceTransform = new MaintaceTransformImpl();
        return RpcResultUtil.buildFutureResult(serviceDelayMeasureResult, result -> new GetServiceDelayMeasureResultOutputBuilder(maintaceTransform.devServiceDelayResultToApi(result)).build());
    }

    /**
     * 查询网元属性
     *
     * @param input 输入参数
     * @return 查询结果
     */
    @Override
    public ListenableFuture<RpcResult<QueryMeOutput>> queryMe(QueryMeInput input) {
        InstanceIdentifier<Me> meIid = create(Me.class);
        //从设备查询的数据
        Me me = MountTools.queryFromOperational(input.getNeId(), meIid);
        QueryMeOutput queryMeOutput = new MeTransformImpl().devMeToApiOut(me);
        return RpcResultUtil.success(queryMeOutput);
    }

    private List<Lpt> fileterLpt(LptPacs lptPacs, Set<String> ptpNameList) {
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
            LptPacs lptPacs;
            List<Lpt> list = new LinkedList<>();
            if (input.getPtpName() == null || input.getPtpName().isEmpty() || input.getPtpName().size() > 1) {
                InstanceIdentifier<LptPacs> iid = create(LptPacs.class);
                lptPacs = MountTools.queryFromOperational(input.getNeId(), iid);
                if (input.getPtpName() != null && !input.getPtpName().isEmpty()) {
                    list = fileterLpt(lptPacs, input.getPtpName());
                } else {
                    if (lptPacs != null && lptPacs.getLpt() != null) {
                        list = new ArrayList<>(lptPacs.getLpt().values());
                    }
                }

            } else {
                InstanceIdentifier<Lpt> iid = create(LptPacs.class).child(Lpt.class, new LptKey(input.getPtpName().iterator().next()));
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
            if (input.getPvid() != null || input.getPortMode() != null || input.getQinqEnable() != null
                    || input.getRemoteManagementModeEnable() != null) {
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
        MountTools.doMergeToConfig(input.getNeId(), iid, CTP_TRANSFORM.apiCtpToDev(input).build());
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
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
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
            if (ctps != null) {
                ctpList = new ArrayList<>(ctps.getCtp().values());
            }
        }
        QueryCtpsOutputBuilder queryCtpsOutputBuilder = new QueryCtpsOutputBuilder();
        queryCtpsOutputBuilder.setCtp(ltm(CTP_TRANSFORM.devCtpToApi(ctpList)));
        return RpcResultUtil.success(queryCtpsOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<ModifyOduPtpPacOutput>> modifyOduPtpPac(ModifyOduPtpPacInput input) {
        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Ptp1> iid =
                create(Ptps.class).child(Ptp.class, new PtpKey(input.getPtpName()))
                        .augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Ptp1.class);
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
        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ptp2>
                iid = create(Ptps.class).child(Ptp.class, new PtpKey(input.getPtpName()))
                .augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ptp2.class);
        //下发数据至设备
        MountTools.doMergeToConfig(input.getNeId(), iid, new PtpTransformImpl().apiSdhPtpToDev(input).build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryCtpExtensionOutput>> queryCtpExtension(QueryCtpExtensionInput input) {
        QueryCtpExtensionOutputBuilder queryCtpExtensionOutputBuilder = new QueryCtpExtensionOutputBuilder();
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            CtpExtensions ctpExtensions;
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
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
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
        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ptp2> iid = create(Ptps.class)
                .child(Ptp.class, new PtpKey(input.getPtpName())).augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ptp2.class);
        MountTools.doMergeToConfig(input.getNeId(), iid, new PtpTransformImpl().apiPdhPtpToDev(input).build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryMcPortOutput>> queryMcPort(QueryMcPortInput input) {
        QueryMcPortOutputBuilder builder = new QueryMcPortOutputBuilder();
        String name = input.getName();
        if (name != null && !name.isEmpty()) {
            InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.mc.ports.McPort> iid =
                    create(McPorts.class).child(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.mc.ports.McPort.class, new McPortKey(name));
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.mc.ports.McPort mcPort = MountTools.queryFromOperational(input.getNeId(), iid);
            List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.mc.ports.McPort> mcPortList = new LinkedList<>();
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
        Eqs eqs;
        if (input.getEqName() != null && !"".equals(input.getEqName())) {
            //查询对应Eq
            InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.eqs.Eq> iid = create(Eqs.class).child(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.eqs.Eq.class, new EqKey(input.getEqName()));
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.eqs.Eq eq = MountTools.queryFromOperational(input.getNeId(), iid);
            List<Eq> eqList = new ArrayList<>();
            eqList.add(eq);
            eqs = new EqsBuilder().setEq(ltm(eqList)).build();
        } else {
            //查询所有
            InstanceIdentifier<Eqs> iid = create(Eqs.class);
            //从设备查询的数据
            eqs = MountTools.queryFromOperational(input.getNeId(), iid);
        }
        QueryEqsOutputBuilder queryEqsOutputBuilder = new QueryEqsOutputBuilder();
        if (eqs != null && eqs.getEq() != null) {
            queryEqsOutputBuilder.setEq(ltm(new EqTransformImpl().devEqToApiList(eqs.getEq().values())));
        }
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
            queryEthPerformanceOutputBuilder.setDelay(ethPerformance.getDelay());
            queryEthPerformanceOutputBuilder.setFarPacketLossRate(CTP_TRANSFORM.devRealToApi(ethPerformance.getFarPacketLossRate()));
            queryEthPerformanceOutputBuilder.setNearPacketLossRate(CTP_TRANSFORM.devRealToApi(ethPerformance.getNearPacketLossRate()));
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
        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.mc.ports.McPort> iid =
                create(McPorts.class).child(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.mc.ports.McPort.class, new McPortKey(key));
        MountTools.doMergeToConfig(input.getNeId(), iid, new PtpTransformImpl().apiMcPortToDev(input).build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyUserPasswordNeOutput>> modifyUserPasswordNe(ModifyUserPasswordNeInput input) {
        AccDevmService accDevmService = MountTools.getRpcService(input.getNeId(), AccDevmService.class);
        ModifyUserPasswordInputBuilder modifyUserPasswordInputBuilder = new DeviceUserManageTransformImpl().apiModifyUserPasswordToDev(input);
        return RpcResultUtil.buildFutureResult(accDevmService.modifyUserPassword(modifyUserPasswordInputBuilder.build()));
    }

    /**
     * Invoke {@code query-account} RPC.
     *
     * @param input of {@code query-account}
     * @return output of {@code query-account}
     */
    @Override
    public ListenableFuture<RpcResult<QueryAccountOutput>> queryAccount(QueryAccountInput input) {
        InstanceIdentifier<Accounts> iid = create(Accounts.class);
        Accounts accounts = MountTools.queryFromOperational(input.getNeId(), iid);
        DeviceAccountTransformImpl deviceAccountTransform = new DeviceAccountTransformImpl();
        QueryAccountOutput queryAccountOutput = deviceAccountTransform.devQueryAccountOutputToApi(accounts);
        return RpcResultUtil.success(queryAccountOutput);
    }

    /**
     * Invoke {@code modify-account} RPC.
     *
     * @param input of {@code modify-account}
     * @return output of {@code modify-account}
     */
    @Override
    public ListenableFuture<RpcResult<ModifyAccountOutput>> modifyAccount(ModifyAccountInput input) {
        AccountKey accountKey = new AccountKey(input.getUserName());
        KeyedInstanceIdentifier<Account, AccountKey> child = create(Accounts.class).child(Account.class, accountKey);

//        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.accounts.Account.class> iid = create(Accounts.class)
//                .child(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.accounts.Account.class, accountKey);
        AccountBuilder accountBuilder = new AccountBuilder();
        accountBuilder.setUserName(input.getUserName());
        accountBuilder.setAdditional(input.getAdditional());
        accountBuilder.setRole(UserRole.forValue(input.getRole().getIntValue()));
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.accounts.Account build = accountBuilder.build();
        MountTools.doMergeToConfig(input.getNeId(), child, build);
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
        AccAccountService accAccountService = MountTools.getRpcService(input.getNeId(), AccAccountService.class);
        DelAccountInputBuilder delAccountInputBuilder = new DelAccountInputBuilder();
        delAccountInputBuilder.setSudoPwd(input.getSudoPwd());
        delAccountInputBuilder.setUserName(input.getUserName());
        return RpcResultUtil.buildFutureResult(accAccountService.delAccount(delAccountInputBuilder.build()));
    }

    /**
     * Invoke {@code add-account} RPC.
     *
     * @param input of {@code add-account}
     * @return output of {@code add-account}
     */
    @Override
    public ListenableFuture<RpcResult<AddAccountOutput>> addAccount(AddAccountInput input) {
        AccAccountService accAccountService = MountTools.getRpcService(input.getNeId(), AccAccountService.class);
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.AddAccountInputBuilder addAccountInputBuilder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.AddAccountInputBuilder();
        addAccountInputBuilder.setUserName(input.getUserName());
        addAccountInputBuilder.setPwd(input.getPwd());
        UserRole userRole = UserRole.forValue(input.getRole().getIntValue());
        addAccountInputBuilder.setRole(userRole);
        addAccountInputBuilder.setSudoPwd(input.getSudoPwd());
        addAccountInputBuilder.setAdditional(input.getAdditional());
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.account.rev231216.AddAccountInput build = addAccountInputBuilder.build();
        return RpcResultUtil.buildFutureResult(accAccountService.addAccount(build));
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
    public ListenableFuture<RpcResult<DeleteMeNtpOutput>> deleteMeNtp(DeleteMeNtpInput input) {
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.me.ntp.servers.NtpServerKey key = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.me.ntp.servers.NtpServerKey(input.getName());
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
        InstanceIdentifier<VlanSpec> child = create(Ptps.class)
                .child(Ptp.class, new PtpKey(input.getPtpName()))
                .augmentation(Ptp2.class)
                .child(EthPtpPac.class)
                .child(VlanSpec.class);
        MountTools.deleteFromConfig(input.getNeId(), child);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyEthCtpPacOutput>> modifyEthCtpPac(ModifyEthCtpPacInput input) {
        InstanceIdentifier<Ctp1> iid = create(Ctps.class).child(Ctp.class, new CtpKey(input.getCtpName())).augmentation(Ctp1.class);
        EthCtpPacBuilder ethCtpPacBuilder = new EthCtpPacBuilder();
        ClientVlanSpecBuilder clientVlanSpecBuilder = new ClientVlanSpecBuilder(CTP_TRANSFORM.apiVlanSpecToDev(input.getClientVlanSpec()));
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
        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Ctp1> iid = create(Ctps.class).child(Ctp.class, new CtpKey(input.getCtpName())).augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Ctp1.class);
        OduCtpPacBuilder oduCtpPacBuilder = new OduCtpPacBuilder();
        oduCtpPacBuilder.setOduSignalType(CTP_TRANSFORM.apiClientSignalTypeToDev(input.getOduSignalType()))
                .setAdaptationType(CTP_TRANSFORM.apiAdaptationTypeToDev(input.getAdaptationType()))
                .setSwitchCapability(CTP_TRANSFORM.apiSwitchTypeToDev(input.getSwitchCapability()))
                .setTsDetail(input.getTsDetail())
                .setCurrentNumberOfTributarySlots(input.getCurrentNumberOfTributarySlots())
                .setPmtrailTraceActualTx(input.getPmtrailTraceActualTx())
                .setPmtrailTraceExpectedRx(input.getPmtrailTraceExpectedRx());
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Ctp1Builder ctp1Builder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Ctp1Builder().setOduCtpPac(oduCtpPacBuilder.build());
        MountTools.doMergeToConfig(input.getNeId(), iid, ctp1Builder.build());
        if (CharSequenceUtil.isAllNotBlank(input.getPmtrailTraceActualTx(), input.getPmtrailTraceExpectedRx())) {
            SetSmPmTrailTraceInputBuilder setSmPmTrailTraceInputBuilder = new SetSmPmTrailTraceInputBuilder();
            setSmPmTrailTraceInputBuilder.setNeId(input.getNeId());
            setSmPmTrailTraceInputBuilder.setPmActualTx(input.getPmtrailTraceActualTx());
            setSmPmTrailTraceInputBuilder.setPmExpectedRx(input.getPmtrailTraceExpectedRx());
            setSmPmTrailTraceInputBuilder.setTpName(input.getCtpName());
            setSmPmTrailTraceInputBuilder.setYangMode(input.getYangMode());
            setSmPmTrailTrace(setSmPmTrailTraceInputBuilder.build());
        }
        return RpcResultUtil.success();
    }


    @Override
    public ListenableFuture<RpcResult<ModifyOsuCtpPacOutput>> modifyOsuCtpPac(ModifyOsuCtpPacInput input) {
        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.Ctp1> iid = create(Ctps.class).child(Ctp.class, new CtpKey(input.getCtpName())).augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.Ctp1.class);
        OsuCtpPacBuilder osuCtpPacBuilder = new OsuCtpPacBuilder();
        osuCtpPacBuilder.setOsuSignalType(CTP_TRANSFORM.apiClientSignalTypeToDev(input.getOsuSignalType()))
                .setAdaptationType(CTP_TRANSFORM.apiAdaptationTypeToDev(input.getAdaptationType()))
                .setTpn(input.getTpn())
                .setPmTrailTraceActualTx(input.getPmTrailTraceActualTx())
                .setPmTrailTraceExpectedRx(input.getPmTrailTraceExpectedRx())
                .setTcm1Mode(CTP_TRANSFORM.apiTcmModeTypeToDev(input.getTcm1Mode()))
                .setTcm2Mode(CTP_TRANSFORM.apiTcmModeTypeToDev(input.getTcm2Mode()));
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.Ctp1Builder ctp1Builder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.Ctp1Builder().setOsuCtpPac(osuCtpPacBuilder.build());
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
        if (OtnRevisionUtils.isRevision20240702(input.getNeId())) {
            // 新版本根据 RPC 下发
            AccSdhService accSdhService = MountTools.getRpcService(input.getNeId(), AccSdhService.class);
            SetVcTrailTraceInputBuilder setVcTrailTraceInputBuilder = new SetVcTrailTraceInputBuilder();
            InstanceIdentifier<Ctp> iid = create(Ctps.class).child(Ctp.class, new CtpKey(input.getCtpName()));
            Ctp ctp = MountTools.queryFromOperational(input.getNeId(), iid);
            if (Objects.nonNull(ctp)) {
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ctp1 sdhCtpPac = ctp.augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ctp1.class);
                if (sdhCtpPac != null && sdhCtpPac.getVcCtpPac() != null) {
                    String tpName = StrUtil.isEmpty(ctp.getServerTp()) ? ctp.getClientTp(): ctp.getServerTp();
                    setVcTrailTraceInputBuilder
                            .setTpName(tpName)
                            .setMappingPath(sdhCtpPac.getVcCtpPac().getMappingPath())
                            .setVcType(sdhCtpPac.getVcCtpPac().getVcType())
                            .setTrailTraceType(TrailTraceType.J1)
                            .setActualTx(input.getJ1ActualTx())
                            .setExpectedRx(input.getJ1ExpectedRx());
                }
            }
            return RpcResultUtil.buildFutureResult(accSdhService.setVcTrailTrace(setVcTrailTraceInputBuilder.build()));
        } else {
            InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ctp1> iid = create(Ctps.class).child(Ctp.class, new CtpKey(input.getCtpName())).augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ctp1.class);
            VcCtpPacBuilder vcCtpPacBuilder = new VcCtpPacBuilder();
            vcCtpPacBuilder.setVcType(CTP_TRANSFORM.apiSdhSwitchTypeToDev(input.getVcType())).setMappingPath(input.getMappingPath());
            vcCtpPacBuilder.setJ1ActualTx(input.getJ1ActualTx()).setJ1ExpectedRx(input.getJ1ExpectedRx());
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ctp1Builder ctp1Builder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ctp1Builder().setVcCtpPac(vcCtpPacBuilder.build());
            MountTools.doMergeToConfig(input.getNeId(), iid, ctp1Builder.build());
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<GetServiceDelayMeasureConfigOutput>> getServiceDelayMeasureConfig(GetServiceDelayMeasureConfigInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<ServiceDelayMeasureConfigs> iid = create(ServiceDelayMeasureConfigs.class);
        ServiceDelayMeasureConfigs serviceDelayMeasureConfigs = MountTools.queryFromOperational(input.getNeId(), iid);
        if (Objects.nonNull(serviceDelayMeasureConfigs)) {
            MaintaceTransformImpl maintaceTransform = new MaintaceTransformImpl();
            GetServiceDelayMeasureConfigOutputBuilder outputBuilder = new GetServiceDelayMeasureConfigOutputBuilder();
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
                                .child(EthPtpFrameSpace.class, new EthPtpFrameSpaceKey(ptpNameList.iterator().next()));
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
                    outputBuilder.setEthPtpFrameSpace(
                            ltm(new PtpTransformImpl().devEthPtpFrameSpaceToApiList(
                                    filterEthPtpFrameSpace(ethPtpFrameSpaces.getEthPtpFrameSpace(), CollUtil.newArrayList(ptpNameList)))));
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
    private List<EthPtpFrameSpace> filterEthPtpFrameSpace(Map<EthPtpFrameSpaceKey, EthPtpFrameSpace> dataList, List<String> ptpNameList) {
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
     * 电信获取光模块信息
     *
     * @param input of {@code get-optical-module-info}
     * @return output of {@code get-optical-module-info}
     */
    @Override
    public ListenableFuture<RpcResult<GetOpticalModuleInfoOutput>> getOpticalModuleInfo(GetOpticalModuleInfoInput input) {
        AccDevmService rpcService = MountTools.getRpcService(input.getNeId(), AccDevmService.class);
        MeTransformImpl meTransform = new MeTransformImpl();
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.GetOpticalModuleInfoOutput>> opticalModuleInfo;
        String property = System.getProperty(IS_OPT_TEST);
        if (property != null) {
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.GetOpticalModuleInfoOutput getOpticalModuleInfoOutput = new TestData(null).getGetOpticalModuleInfoOutput(input.getPtpName());
            opticalModuleInfo = RpcResultBuilder.success(getOpticalModuleInfoOutput).buildFuture();
        } else {
            opticalModuleInfo = rpcService.getOpticalModuleInfo(meTransform.apiToGetOpticalModuleInfoInputDev(input));
        }

        return RpcResultUtil.buildFutureResult(opticalModuleInfo, meTransform::decToGetOpticalModuleInfoOutputApi);
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
        final AccOtnService rpcService = MountTools.getRpcService(input.getNeId(), AccOtnService.class);
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.GetSmPmTrailTraceInputBuilder inputBuilder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.GetSmPmTrailTraceInputBuilder();
        inputBuilder.setTpName(input.getTpName());
        inputBuilder.setTrailTraceType(input.getTrailTraceType().equals("SM") ? TrailTraceType.SM : TrailTraceType.PM);
        final ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn
                .rev240702.GetSmPmTrailTraceOutput>> resultFuture =
                rpcService.getSmPmTrailTrace(inputBuilder.build());
        try{
            final RpcResult<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn
                    .rev240702.GetSmPmTrailTraceOutput> rpcResult = resultFuture.get();
            if(rpcResult.isSuccessful()){
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn
                        .rev240702.GetSmPmTrailTraceOutput result = rpcResult.getResult();
                GetSmPmTrailTraceOutputBuilder resultBuild = new GetSmPmTrailTraceOutputBuilder();
                if(result!=null){
                    resultBuild.setActualRx(result.getActualRx());
                    resultBuild.setActualTx(result.getActualTx());
                    resultBuild.setExpectedRx(result.getExpectedRx());
                }
                return RpcResultUtil.success(resultBuild.build());
            }else{
                return RpcResultUtil.failed(rpcResult);
            }
        } catch (ExecutionException | InterruptedException e) {
            LOG.error("device request is failed");
            throw new DeviceOperaFailException(e);
        }
    }

    @Override
    public ListenableFuture<RpcResult<SetSmPmTrailTraceOutput>> setSmPmTrailTrace(SetSmPmTrailTraceInput input) {
        AccOtnService rpcService = MountTools.getRpcService(input.getNeId(), AccOtnService.class);
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn
                .rev240702.SetSmPmTrailTraceInputBuilder builder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn
                .rev240702.SetSmPmTrailTraceInputBuilder();
        builder.setTpName(input.getTpName());
        builder.setPmActualTx(input.getPmActualTx());
        builder.setPmExpectedRx(input.getPmExpectedRx());
        builder.setSmActualTx(input.getSmActualTx());
        builder.setSmExpectedRx(input.getSmExpectedRx());
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn
                .rev240702.SetSmPmTrailTraceOutput>> result = rpcService.setSmPmTrailTrace(builder.build());
        return RpcResultUtil.buildFutureResult(result);
    }

    @Override
    public ListenableFuture<RpcResult<GetSdhTerminationPointDataOutput>> getSdhTerminationPointData(GetSdhTerminationPointDataInput input) {
        AccSdhService rpcService = MountTools.getRpcService(input.getNeId(), AccSdhService.class);
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh
                .rev240702.GetSdhTerminationPointDataInputBuilder inputBuilder =
                new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh
                        .rev240702.GetSdhTerminationPointDataInputBuilder();
        inputBuilder.setSdhTpName(input.getSdhTpName());
        final ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh
                .rev240702.GetSdhTerminationPointDataOutput>> resultFuture =
                rpcService.getSdhTerminationPointData(inputBuilder.build());
        try{
            final RpcResult<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh
                    .rev240702.GetSdhTerminationPointDataOutput> rpcResult = resultFuture.get();
            if(rpcResult.isSuccessful()){
                final org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh
                        .rev240702.GetSdhTerminationPointDataOutput result = rpcResult.getResult();
                GetSdhTerminationPointDataOutputBuilder resultBuild = new GetSdhTerminationPointDataOutputBuilder();
                if(result!=null){
                    resultBuild.setActualRx(result.getActualRx());
                    resultBuild.setActualTx(result.getActualTx());
                    resultBuild.setExpectedRx(result.getExpectedRx());
                }
                return RpcResultUtil.success(resultBuild.build());
            }else{
                return RpcResultUtil.failed(rpcResult);
            }
        } catch (ExecutionException | InterruptedException e) {
            LOG.error("device request is failed");
            throw new DeviceOperaFailException(e);
        }
    }

    @Override
    public ListenableFuture<RpcResult<SetSdhTerminationPointDataOutput>> setSdhTerminationPointData(SetSdhTerminationPointDataInput input) {
        AccSdhService rpcService = MountTools.getRpcService(input.getNeId(), AccSdhService.class);
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh
                .rev240702.SetSdhTerminationPointDataInputBuilder inputBuilder =
                new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh
                        .rev240702.SetSdhTerminationPointDataInputBuilder();
        inputBuilder.setTpName(input.getTpName());
        inputBuilder.setExpectedRx(input.getExpectedRx());
        inputBuilder.setActualTx(input.getActualTx());
        final ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh
                .rev240702.SetSdhTerminationPointDataOutput>> result = rpcService.setSdhTerminationPointData(inputBuilder.build());
        return RpcResultUtil.buildFutureResult(result);
    }

    @Override
    public ListenableFuture<RpcResult<GetVcTrailTraceJ1Output>> getVcTrailTraceJ1(GetVcTrailTraceJ1Input input) {
        GetVcTrailTraceJ1OutputBuilder getVcTrailTraceJ1OutputBuilder = new GetVcTrailTraceJ1OutputBuilder();
        if (OtnRevisionUtils.isRevision20240702(input.getNeId())) {
            // 新版本根据 RPC 查询
            AccSdhService accSdhService = MountTools.getRpcService(input.getNeId(), AccSdhService.class);
            CompatibleRpcUtil.getVcTrailTraceThenConsumer(accSdhService, input.getTpName(), input.getMappingPath(), CTP_TRANSFORM.apiSdhSwitchTypeToDev(input.getVcType()), TrailTraceType.J1, vcTrailTraceValue -> {
                getVcTrailTraceJ1OutputBuilder.setActualRx(vcTrailTraceValue.getActualRx());
                getVcTrailTraceJ1OutputBuilder.setActualTx(vcTrailTraceValue.getActualTx());
                getVcTrailTraceJ1OutputBuilder.setExpectedRx(vcTrailTraceValue.getExpectedRx());
            });
        } else {
            InstanceIdentifier<Ctp> iid = create(Ctps.class).child(Ctp.class, new CtpKey(input.getCtpName()));
            Ctp ctp = MountTools.queryFromOperational(input.getNeId(), iid);
            if (Objects.nonNull(ctp)) {
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ctp1 sdhCtpPac = ctp.augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ctp1.class);
                if (sdhCtpPac != null && sdhCtpPac.getVcCtpPac() != null) {
                    getVcTrailTraceJ1OutputBuilder.setActualRx(sdhCtpPac.getVcCtpPac().getJ1ActualRx());
                    getVcTrailTraceJ1OutputBuilder.setActualTx(sdhCtpPac.getVcCtpPac().getJ1ActualTx());
                    getVcTrailTraceJ1OutputBuilder.setExpectedRx(sdhCtpPac.getVcCtpPac().getJ1ExpectedRx());
                }
            }
        }
        return RpcResultUtil.success(getVcTrailTraceJ1OutputBuilder.build());
    }
}
