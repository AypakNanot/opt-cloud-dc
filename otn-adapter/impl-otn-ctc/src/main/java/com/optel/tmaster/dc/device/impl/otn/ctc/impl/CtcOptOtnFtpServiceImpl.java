/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.impl;

import cn.hutool.core.collection.CollUtil;
import com.google.common.util.concurrent.ListenableFuture;
import org.apache.commons.collections.CollUtils;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptOtnFtpServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.FtpTransformImpl;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import com.optel.tmaster.dc.general.nc.nccore.NcTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ftp.rev200425.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GfpfParameters;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.gfpf.parameter.grouping.GfpfParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.gfpf.parameter.grouping.GfpfParameterKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.Ftps;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ftps.Ftp;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ftps.FtpKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.EthFtpPac1;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.Ftp1;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ftps.ftp.EthFtpPac;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.*;

/**
 * 2020/4/28 18:01
 *
 * @author lxy
 * @version V1.0.0
 */
public class CtcOptOtnFtpServiceImpl extends BaseOptOtnFtpServiceImpl implements IDeviceServiceOtnCtc {

    @Override
    public ListenableFuture<RpcResult<ModifyEthFtpPacOutput>> modifyEthFtpPac(ModifyEthFtpPacInput input) {
        InstanceIdentifier<Ftp1> iid =
                create(Ftps.class).child(Ftp.class, new FtpKey(input.getFtpName()))
                        .augmentation(Ftp1.class);
        //下发数据到设备
        MountTools.doMergeToConfig(input.getNeId(), iid, new FtpTransformImpl().apiEthFtpToDev(input).build());
        //判断是否是奥普泰设备
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            if (input.getPayloadFcs() != null || input.getScrambler() != null) {
                InstanceIdentifier<GfpfParameter> gid =
                        create(GfpfParameters.class)
                                .child(GfpfParameter.class, new GfpfParameterKey(input.getFtpName()));
                //下发数据至设备
                MountTools.doMergeToConfig(input.getNeId(), gid, new FtpTransformImpl().apiGfpfParameterToDev(input).build());
            }
        }
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<ModifyEthVcgOutput>> modifyEthVcg(ModifyEthVcgInput input) {
        InstanceIdentifier<EthFtpPac1> iid = create(Ftps.class).child(Ftp.class, new FtpKey(input.getFtpName()))
                .augmentation(Ftp1.class).child(EthFtpPac.class).augmentation(EthFtpPac1.class);
        MountTools.doMergeToConfig(input.getNeId(), iid, new FtpTransformImpl().apiEthVcgToApi(input).build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryGfpfParameterOutput>> queryGfpfParameter(QueryGfpfParameterInput input) {
        QueryGfpfParameterOutputBuilder outputBuilder = new QueryGfpfParameterOutputBuilder();
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            Set<String> ftpNameList = input.getFtpName();
            if (ftpNameList == null) {
                InstanceIdentifier<GfpfParameters> iid = create(GfpfParameters.class);
                GfpfParameters gfpfParameters = MountTools.queryFromOperational(input.getNeId(), iid);
                if (gfpfParameters != null) {
                    outputBuilder.setGfpfParameter(ltm(new FtpTransformImpl().devGfpfParameterToApiList(gfpfParameters.getGfpfParameter())));
                }
            } else if (ftpNameList.size() == 1) {
                InstanceIdentifier<GfpfParameter> iid =
                        create(GfpfParameters.class)
                                .child(GfpfParameter.class, new GfpfParameterKey(ftpNameList.iterator().next()));
                GfpfParameter gfpfParameter = MountTools.queryFromOperational(input.getNeId(), iid);
                if (gfpfParameter != null) {
                    outputBuilder.setGfpfParameter(ltm(CollUtil.newArrayList(new FtpTransformImpl().devGfpfParameterToApi(gfpfParameter))));
                }
            } else {
                InstanceIdentifier<GfpfParameters> iid = create(GfpfParameters.class);
                GfpfParameters gfpfParameters = MountTools.queryFromOperational(input.getNeId(), iid);
                if (gfpfParameters != null) {
                    outputBuilder.setGfpfParameter(
                            ltm(new FtpTransformImpl().devGfpfParameterToApiList(
                                    filterGfpfParameter(gfpfParameters.getGfpfParameter(), CollUtil.newArrayList(ftpNameList)))));
                }
            }
        }
        return RpcResultUtil.success(outputBuilder.build());
    }

    private Map<GfpfParameterKey, GfpfParameter> filterGfpfParameter(Map<GfpfParameterKey, GfpfParameter> gfpfParameterList, List<String> ftpNameList) {
        if (gfpfParameterList != null && !gfpfParameterList.isEmpty()) {
            if (ftpNameList != null && !ftpNameList.isEmpty()) {
                Map<GfpfParameterKey, GfpfParameter> nameMap = new HashMap<>(ftpNameList.size());
                for (String ftpName : ftpNameList) {
                    GfpfParameterKey gfpfParameterKey = new GfpfParameterKey(ftpName);
                    GfpfParameter gfpfParameter = gfpfParameterList.get(gfpfParameterKey);
                    if (gfpfParameter != null) {
                        nameMap.put(gfpfParameterKey, gfpfParameter);
                    }
                }
                return nameMap;
            }
        }
        return gfpfParameterList;
    }

    @Override
    public ListenableFuture<RpcResult<ModifySdhFtpPacOutput>> modifySdhFtpPac(ModifySdhFtpPacInput input) {
        InstanceIdentifier<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ftp1> iid =
                create(Ftps.class).child(Ftp.class, new FtpKey(input.getFtpName()))
                        .augmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ftp1.class);
        //下发数据到设备
        MountTools.doMergeToConfig(input.getNeId(), iid, new FtpTransformImpl().apiSdhFtpToDev(input).build());
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryFtpsOutput>> queryFtps(QueryFtpsInput input) {
        List<Ftp> ftpList = new ArrayList<>();
        if (input.getFtpName() == null || input.getFtpName().isEmpty()) {
            InstanceIdentifier<Ftps> iid = create(Ftps.class);
            //从设备处查询数据
            Ftps ftps = MountTools.queryFromOperational(input.getNeId(), iid);
            if (ftps != null && ftps.getFtp() != null) {
                ftpList.addAll(ftps.getFtp().values());
            }
        } else if (input.getFtpName().size() == 1) {
            for (String ftpName : input.getFtpName()) {
                InstanceIdentifier<Ftp> iid = create(Ftps.class).child(Ftp.class, new FtpKey(ftpName));
                Ftp ftp = MountTools.queryFromOperational(input.getNeId(), iid);
                if (ftp != null) {
                    ftpList.add(ftp);
                }
            }
        } else {
            InstanceIdentifier<Ftps> iid = create(Ftps.class);
            //从设备处查询数据
            Ftps devFtps = MountTools.queryFromOperational(input.getNeId(), iid);
            if (devFtps != null && devFtps.getFtp() != null) {
                for (String ftpName : input.getFtpName()) {
                    for (Ftp ftp : devFtps.getFtp().values()) {
                        if (ftpName.equals(ftp.getName())) {
                            ftpList.add(ftp);
                            break;
                        }
                    }
                }
            }
        }
        QueryFtpsOutputBuilder builder = new QueryFtpsOutputBuilder();
        if (ftpList.isEmpty()) {
            return RpcResultUtil.success(builder.build());
        }
        builder.setFtp(ltm(new FtpTransformImpl().devFtpToApiList(ftpList)));
        return RpcResultUtil.success(builder.build());
    }

}
