/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform;

import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.AbstractCmccTransformer;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.EnumTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.ActiveFileNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.DeleteFileNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.DownloadFileNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.GetActiveStatusNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.GetActiveStatusNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.GetActiveStatusNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.GetDownloadStatusNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.GetDownloadStatusNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.GetFileListNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.GetFileListNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.UploadFileNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.get.file.list.ne.output.Files;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.get.file.list.ne.output.FilesKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.DownloadFileInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.GetDownloadStatusInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.get.file.list.output.files.File;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * 文件上传 转换器
 * 2021/10/14 17:11
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class FileTransformImpl extends AbstractCmccTransformer implements CommonTransform, EnumTransform {

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.UploadFileInputBuilder apiUploadFileToDev(UploadFileNeInput input){
        if(input==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.UploadFileInputBuilder uploadFileInputBuilder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.UploadFileInputBuilder();
        uploadFileInputBuilder.setPath(input.getPath()).setUsername(input.getUsername())
                .setPassword(input.getPassword()).setFileName(input.getFileName())
                .setFileType(apiFileTypeToDev(input.getFileType()));
        return uploadFileInputBuilder;
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.GetDownloadStatusInputBuilder apiGetDownloadStatusToDev(
            GetDownloadStatusNeInput input){
        if(input==null){
            return null;
        }
        GetDownloadStatusInputBuilder builder = new GetDownloadStatusInputBuilder();
        builder.setFileName(input.getFileName()).setFileType(apiFileTypeToDev(input.getFileType()));
        return builder;
    }

    public GetDownloadStatusNeOutputBuilder devGetDownloadStatusToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.GetDownloadStatusOutput getDownloadStatusOutput){
        GetDownloadStatusNeOutputBuilder getDownloadStatusNeOutputBuilder = new GetDownloadStatusNeOutputBuilder();
        getDownloadStatusNeOutputBuilder.setDownloadBytes(getDownloadStatusOutput.getDownloadBytes())
                .setStatus(devDownloadStatusToApi(getDownloadStatusOutput.getStatus()))
                .setFailReason(devFailReasonToApi(getDownloadStatusOutput.getFailReason()));
        return getDownloadStatusNeOutputBuilder;
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.ActiveFileInputBuilder apiActiveFileToDev(
            ActiveFileNeInput input){
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.ActiveFileInputBuilder activeFileInputBuilder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.ActiveFileInputBuilder();
        activeFileInputBuilder.setFileName(input.getFileName())
                .setFileType(apiFileTypeToDev(input.getFileType()));
        return activeFileInputBuilder;
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.DeleteFileInputBuilder apiDeleteFileToDev(
            DeleteFileNeInput input){
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.DeleteFileInputBuilder deleteFileInputBuilder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.DeleteFileInputBuilder();
        deleteFileInputBuilder.setFileName(input.getFileName())
                .setFileType(apiFileTypeToDev(input.getFileType()));
        return deleteFileInputBuilder;
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.DownloadFileInputBuilder apiDownloadFileToDev(
            DownloadFileNeInput input){
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.DownloadFileInputBuilder downloadFileInputBuilder
                = new DownloadFileInputBuilder();
        downloadFileInputBuilder.setPath(input.getPath()).setUsername(input.getUsername())
                .setPassword(input.getPassword()).setFileName(input.getFileName())
                .setFileType(apiFileTypeToDev(input.getFileType())).setFileSize(input.getFileSize());
        return downloadFileInputBuilder;
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.GetFileListInputBuilder apiGetFileListToDev(GetFileListNeInput input){
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.GetFileListInputBuilder getFileListInputBuilder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.GetFileListInputBuilder();
        getFileListInputBuilder.setFileType(input.getFileType());
        return getFileListInputBuilder;
    }

    public GetFileListNeOutputBuilder devGetFileListToApi(
            Collection<File> list){
        if(list==null || list.isEmpty()){
            return null;
        }
        GetFileListNeOutputBuilder getFileListNeOutputBuilder = new GetFileListNeOutputBuilder();
        List<Files> resultList = new LinkedList<>();
        for(File f:list){
            org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.get.file.list.ne.output.FilesBuilder filesBuilder
                    = new org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.file.rev210331.get.file.list.ne.output.FilesBuilder();
            filesBuilder.withKey(new FilesKey(f.getFileName()))
                    .setFileName(f.getFileName()).setFileType(devFileTypeToApi(f.getFileType()));
            resultList.add(filesBuilder.build());
        }
        getFileListNeOutputBuilder.setFiles(ltm(resultList));
        return getFileListNeOutputBuilder;
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.GetActiveStatusInputBuilder apiGetActiveStatusToDev(
            GetActiveStatusNeInput input){
        org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.GetActiveStatusInputBuilder builder
                = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.GetActiveStatusInputBuilder();
        builder.setFileType(apiFileTypeToDev(input.getFileType()));
        return builder;
    }

    public GetActiveStatusNeOutput devGetActiveStatusToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.file.rev210312.GetActiveStatusOutput getActiveStatusOutput){
        if(getActiveStatusOutput == null){
            return null;
        }
        GetActiveStatusNeOutputBuilder outputBuilder = new GetActiveStatusNeOutputBuilder();
        outputBuilder.setFileName(getActiveStatusOutput.getFileName())
                .setActiveTime(getActiveStatusOutput.getActiveTime())
                .setStatus(devActiveStatusToApi(getActiveStatusOutput.getStatus()))
                .setFailReason(devFailReasonToApi(getActiveStatusOutput.getFailReason()));
        return outputBuilder.build();
    }



}
