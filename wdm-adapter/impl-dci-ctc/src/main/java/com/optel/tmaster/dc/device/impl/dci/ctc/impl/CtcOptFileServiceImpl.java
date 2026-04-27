/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciFileServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.ctc.transform.rpc.FileServiceTransform;
import com.optel.tmaster.dc.device.impl.dci.ctc.util.DciUtils;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.ActiveFileNeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.ActiveFileNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.DownloadFileNeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.DownloadFileNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetActiveStatusNeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetActiveStatusNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetDownloadStatusNeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetDownloadStatusNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetFileListNeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetFileListNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.UploadFileNeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.UploadFileNeOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.ActiveFileOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.DownloadFileOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetActiveStatusOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetDownloadStatusOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetFileListOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.OpenconfigRpcService;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.UploadFileOutput;
import org.opendaylight.yangtools.yang.common.RpcResult;

/**
 * 文件管理
 * 2022/3/14 16:09
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class CtcOptFileServiceImpl extends BaseDciFileServiceImpl implements IDeviceServiceWdmCtc {
    @Override
    public ListenableFuture<RpcResult<DownloadFileNeOutput>> downloadFileNe(DownloadFileNeInput input) {
        OpenconfigRpcService rpcService = MountTools.getRpcService(input.getNeId(), OpenconfigRpcService.class);
        FileServiceTransform transform = new FileServiceTransform();
        ListenableFuture<RpcResult<DownloadFileOutput>> rpcResult =
                rpcService.downloadFile(transform.apiDownloadFileToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResult, transform::devDownloadFileOutputToApi);
    }

    @Override
    public ListenableFuture<RpcResult<UploadFileNeOutput>> uploadFileNe(UploadFileNeInput input) {
        OpenconfigRpcService rpcService = MountTools.getRpcService(input.getNeId(), OpenconfigRpcService.class);
        FileServiceTransform transform = new FileServiceTransform();
        ListenableFuture<RpcResult<UploadFileOutput>> rpcResult =
                rpcService.uploadFile(transform.apiUploadFileToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResult, transform::devUploadFileToApi,
                r -> DciUtils.getOcRpcResult(r.getResult(), r.getMsg() == null ? null : r.getMsg()));
    }

    @Override
    public ListenableFuture<RpcResult<GetFileListNeOutput>> getFileListNe(GetFileListNeInput input) {
        OpenconfigRpcService rpcService = MountTools.getRpcService(input.getNeId(), OpenconfigRpcService.class);
        FileServiceTransform transform = new FileServiceTransform();
        ListenableFuture<RpcResult<GetFileListOutput>> rpcResult = rpcService.getFileList(transform.apiGetFileToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResult, transform::devGetFileToApi,
                r -> DciUtils.getOcRpcResult(r.getResult(), r.getMsg()));
    }

    @Override
    public ListenableFuture<RpcResult<ActiveFileNeOutput>> activeFileNe(ActiveFileNeInput input) {
        OpenconfigRpcService rpcService = MountTools.getRpcService(input.getNeId(), OpenconfigRpcService.class);
        FileServiceTransform transform = new FileServiceTransform();
        ListenableFuture<RpcResult<ActiveFileOutput>> rpcResult = rpcService.activeFile(transform.apiActiveFileToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResult, transform::devActiveFileToApi,
                r -> DciUtils.getOcRpcResult(r.getResult(), r.getMsg()));
    }

    @Override
    public ListenableFuture<RpcResult<GetActiveStatusNeOutput>> getActiveStatusNe(GetActiveStatusNeInput input) {
        OpenconfigRpcService rpcService = MountTools.getRpcService(input.getNeId(), OpenconfigRpcService.class);
        FileServiceTransform transform = new FileServiceTransform();
        ListenableFuture<RpcResult<GetActiveStatusOutput>> rpcResult =
                rpcService.getActiveStatus(transform.apiGetActiveStatusToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResult, transform::devGetActiveStatusToApi);
    }

    @Override
    public ListenableFuture<RpcResult<GetDownloadStatusNeOutput>> getDownloadStatusNe(GetDownloadStatusNeInput input) {
        OpenconfigRpcService rpcService = MountTools.getRpcService(input.getNeId(), OpenconfigRpcService.class);
        FileServiceTransform transform = new FileServiceTransform();
        ListenableFuture<RpcResult<GetDownloadStatusOutput>> rpcResult =
                rpcService.getDownloadStatus(transform.apiGetDownloadStatusToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResult, transform::devGetDownloadStatusToApi);
    }
}
