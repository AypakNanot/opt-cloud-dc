/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciFileServiceImpl;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.rpc.FileServiceTransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.util.DciUtils;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.*;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.*;
import org.opendaylight.yangtools.yang.common.RpcResult;

/**
 * 文件管理
 * 2022/3/14 16:09
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class CmccOptFileServiceImpl extends BaseDciFileServiceImpl implements IDeviceServiceWdmCmcc {
    @Override
    public ListenableFuture<RpcResult<DownloadFileNeOutput>> downloadFileNe(DownloadFileNeInput input) {

        MiniotnRpcService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnRpcService.class);
        FileServiceTransform transform = new FileServiceTransform();
        ListenableFuture<RpcResult<DownloadFileOutput>> rpcResult =
                rpcService.downloadFile(transform.apiDownloadFileToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResult, transform::devDownloadFileOutputToApi);

    }

    @Override
    public ListenableFuture<RpcResult<UploadFileNeOutput>> uploadFileNe(UploadFileNeInput input) {

        MiniotnRpcService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnRpcService.class);
        FileServiceTransform transform = new FileServiceTransform();
        ListenableFuture<RpcResult<UploadFileOutput>> rpcResult =
                rpcService.uploadFile(transform.apiUploadFileToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResult, transform::devUploadFileToApi,
                r -> DciUtils.getOcRpcResult(r.getResult(), r.getMsg() == null ? null : r.getMsg().getName()));

    }

    @Override
    public ListenableFuture<RpcResult<GetFileListNeOutput>> getFileListNe(GetFileListNeInput input) {

        MiniotnRpcService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnRpcService.class);
        FileServiceTransform transform = new FileServiceTransform();
        ListenableFuture<RpcResult<GetFileListOutput>> rpcResult = rpcService.getFileList(transform.apiGetFileToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResult, transform::devGetFileToApi,
                r -> DciUtils.getOcRpcResult(r.getResult(), r.getMsg()));
    }

    @Override
    public ListenableFuture<RpcResult<ActiveFileNeOutput>> activeFileNe(ActiveFileNeInput input) {

        MiniotnRpcService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnRpcService.class);
        FileServiceTransform transform = new FileServiceTransform();
        ListenableFuture<RpcResult<ActiveFileOutput>> rpcResult = rpcService.activeFile(transform.apiActiveFileToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResult, transform::devActiveFileToApi,
                r -> DciUtils.getOcRpcResult(r.getResult(), r.getMsg()));
    }

    @Override
    public ListenableFuture<RpcResult<GetActiveStatusNeOutput>> getActiveStatusNe(GetActiveStatusNeInput input) {

        MiniotnRpcService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnRpcService.class);
        FileServiceTransform transform = new FileServiceTransform();
        ListenableFuture<RpcResult<GetActiveStatusOutput>> rpcResult =
                rpcService.getActiveStatus(transform.apiGetActiveStatusToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResult, transform::devGetActiveStatusToApi);

    }

    @Override
    public ListenableFuture<RpcResult<GetDownloadStatusNeOutput>> getDownloadStatusNe(GetDownloadStatusNeInput input) {

        MiniotnRpcService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnRpcService.class);
        FileServiceTransform transform = new FileServiceTransform();
        ListenableFuture<RpcResult<GetDownloadStatusOutput>> rpcResult =
                rpcService.getDownloadStatus(transform.apiGetDownloadStatusToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResult, transform::devGetDownloadStatusToApi);

    }
}
