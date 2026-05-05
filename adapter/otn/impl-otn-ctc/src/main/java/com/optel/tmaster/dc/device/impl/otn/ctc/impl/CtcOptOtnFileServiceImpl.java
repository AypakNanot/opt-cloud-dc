/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptOtnFileServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.FileTransformImpl;
import com.optel.tmaster.dc.general.base.exception.device.DeviceOperaFailException;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.ActiveFileNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.ActiveFileNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.DeleteFileNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.DeleteFileNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.DownloadFileNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.DownloadFileNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.GetActiveStatusNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.GetActiveStatusNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.GetDownloadStatusNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.GetDownloadStatusNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.GetDownloadStatusNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.GetFileListNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.GetFileListNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.GetFileListNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.UploadFileNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.UploadFileNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.UploadFileNeOutputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.AccFileService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.ActiveFileOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.DeleteFileOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.DownloadFileOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.GetActiveStatusOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.GetDownloadStatusOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.GetFileListOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.UploadFileOutput;
import org.opendaylight.yangtools.yang.common.RpcResult;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

/**
 * OTN sftp
 * 2021/3/31 17:03
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class CtcOptOtnFileServiceImpl extends BaseOptOtnFileServiceImpl implements IDeviceServiceOtnCtc {
    private static final Logger LOG = LoggerFactory.getLogger(CtcOptOtnFileServiceImpl.class);

    @Override
    public ListenableFuture<RpcResult<UploadFileNeOutput>> uploadFileNe(UploadFileNeInput input) {
        UploadFileNeOutputBuilder outputBuilder = new UploadFileNeOutputBuilder();
        AccFileService service = MountTools.getRpcService(input.getNeId(), AccFileService.class);
        Future<RpcResult<UploadFileOutput>> rpcResultFuture = service.uploadFile(new FileTransformImpl().apiUploadFileToDev(input).build());
        try {
            RpcResult<UploadFileOutput> out = rpcResultFuture.get();
            if (out.isSuccessful()) {
                UploadFileOutput result = out.getResult();
                if (result != null) {
                    outputBuilder.setFileName(result.getFileName())
                            .setMd5(result.getMd5()).setFileSize(result.getFileSize());
                }
            } else {
                return RpcResultUtil.failed(out);
            }
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("device request is failed");
            throw new DeviceOperaFailException(e);
        }
        return RpcResultUtil.success(outputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<GetDownloadStatusNeOutput>> getDownloadStatusNe(GetDownloadStatusNeInput input) {
        GetDownloadStatusNeOutputBuilder outputBuilder = new GetDownloadStatusNeOutputBuilder();
        AccFileService service = MountTools.getRpcService(input.getNeId(), AccFileService.class);
        Future<RpcResult<GetDownloadStatusOutput>> resultFuture
                = service.getDownloadStatus(new FileTransformImpl().apiGetDownloadStatusToDev(input).build());
        try {
            RpcResult<GetDownloadStatusOutput> out = resultFuture.get();
            if (out.isSuccessful()) {
                GetDownloadStatusOutput result = out.getResult();
                if (result != null) {
                    return RpcResultUtil.success(new FileTransformImpl().devGetDownloadStatusToApi(result).build());
                }
            } else {
                return RpcResultUtil.failed(out);
            }
        } catch (InterruptedException | ExecutionException e) {
            LOG.error("device request is failed");
            throw new DeviceOperaFailException(e);
        }
        return RpcResultUtil.success(outputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<ActiveFileNeOutput>> activeFileNe(ActiveFileNeInput input) {
        AccFileService service = MountTools.getRpcService(input.getNeId(), AccFileService.class);
        ListenableFuture<RpcResult<ActiveFileOutput>> result = service.activeFile(new FileTransformImpl().apiActiveFileToDev(input).build());
        return RpcResultUtil.buildFutureResult(result);
    }

    @Override
    public ListenableFuture<RpcResult<DeleteFileNeOutput>> deleteFileNe(DeleteFileNeInput input) {
        AccFileService service = MountTools.getRpcService(input.getNeId(), AccFileService.class);
        ListenableFuture<RpcResult<DeleteFileOutput>> result = service.deleteFile(new FileTransformImpl().apiDeleteFileToDev(input).build());
        return RpcResultUtil.buildFutureResult(result);
    }

    @Override
    public ListenableFuture<RpcResult<DownloadFileNeOutput>> downloadFileNe(DownloadFileNeInput input) {
        AccFileService service = MountTools.getRpcService(input.getNeId(), AccFileService.class);
        ListenableFuture<RpcResult<DownloadFileOutput>> result = service.downloadFile(new FileTransformImpl().apiDownloadFileToDev(input).build());
        return RpcResultUtil.buildFutureResult(result);
    }

    @Override
    public ListenableFuture<RpcResult<GetFileListNeOutput>> getFileListNe(GetFileListNeInput input) {
        AccFileService service = MountTools.getRpcService(input.getNeId(), AccFileService.class);
        Future<RpcResult<GetFileListOutput>> resultFuture = service.getFileList(
                new FileTransformImpl().apiGetFileListToDev(input).build());
        return RpcResultUtil.buildFutureResult(resultFuture, result -> {
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.get.file.list.output.Files files = result.getFiles();
            if (files != null && files.getFile() != null) {
                return new FileTransformImpl().devGetFileListToApi(files.getFile().values());
            }
            return new GetFileListNeOutputBuilder().build();
        });
    }

    @Override
    public ListenableFuture<RpcResult<GetActiveStatusNeOutput>> getActiveStatusNe(GetActiveStatusNeInput input) {
        AccFileService service = MountTools.getRpcService(input.getNeId(), AccFileService.class);
        Future<RpcResult<GetActiveStatusOutput>> resultFuture = service.getActiveStatus(
                new FileTransformImpl().apiGetActiveStatusToDev(input).build());
        return RpcResultUtil.buildFutureResult(resultFuture, result -> new FileTransformImpl().devGetActiveStatusToApi(result));
    }
}
