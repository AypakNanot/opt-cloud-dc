/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.transform.rpc;

import com.optel.tmaster.dc.device.impl.dci.ctc.transform.base.FileTypeTransform;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.ActiveFileNeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.ActiveFileNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.ActiveFileNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.DownloadFileNeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.DownloadFileNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.DownloadFileNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetActiveStatusNeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetActiveStatusNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetActiveStatusNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetDownloadStatusNeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetDownloadStatusNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetDownloadStatusNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetFileListNeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetFileListNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetFileListNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.UploadFileNeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.UploadFileNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.UploadFileNeOutputBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.ActiveFileInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.ActiveFileInputBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.ActiveFileOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.DownloadFileInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.DownloadFileInputBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.DownloadFileOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetActiveStatusInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetActiveStatusInputBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetActiveStatusOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetDownloadStatusInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetDownloadStatusInputBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetDownloadStatusOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetFileListInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetFileListInputBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetFileListOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.UploadFileInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.UploadFileInputBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.UploadFileOutput;

/**
 * dc 文件维护
 * 2022/3/14 16:19
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public class FileServiceTransform implements FileTypeTransform {

    public DownloadFileInput apiDownloadFileToDev(DownloadFileNeInput input){
        if(input==null){
            return null;
        }
        DownloadFileInputBuilder builder = new DownloadFileInputBuilder();
        builder.setFileName(input.getFileName());
        builder.setFileType(apiFileTypeToDev(input.getFileType()));
        builder.setPassword(input.getPassword());
        builder.setPath(input.getPath());
        builder.setUsername(input.getUsername());
        return builder.build();
    }

    public DownloadFileNeOutput devDownloadFileOutputToApi(DownloadFileOutput output){
        if(output==null){
            return null;
        }
        DownloadFileNeOutputBuilder builder = new DownloadFileNeOutputBuilder();
        builder.setMsg(output.getMsg());
        return builder.build();
    }

    public UploadFileInput apiUploadFileToDev(UploadFileNeInput input){
        if(input==null){
            return null;
        }
        UploadFileInputBuilder builder = new UploadFileInputBuilder();
        builder.setFileName(input.getFileName());
        builder.setFileType(apiFileTypeToDev(input.getFileType()));
        builder.setPassword(input.getPassword());
        builder.setPath(input.getPath());
        builder.setUsername(input.getUsername());
        return builder.build();
    }

    public UploadFileNeOutput devUploadFileToApi(UploadFileOutput output){
        if(output==null){
            return null;
        }
        UploadFileNeOutputBuilder builder = new UploadFileNeOutputBuilder();
        builder.setFileName(output.getFileName());
        builder.setMd5(output.getMd5());
        builder.setMsg(output.getMsg());
        return builder.build();
    }

    public GetFileListInput apiGetFileToDev(GetFileListNeInput input){
        if(input==null){
            return null;
        }
        GetFileListInputBuilder builder = new GetFileListInputBuilder();
        builder.setFileType(apiFileTypeToDev(input.getFileType()));
        return builder.build();
    }

    public GetFileListNeOutput devGetFileToApi(GetFileListOutput output){
        if(output==null){
            return null;
        }
        GetFileListNeOutputBuilder builder = new GetFileListNeOutputBuilder();
        builder.setFileNames(devFileNameToApi(output.getFileNames()));
        builder.setMsg(output.getMsg());
        return builder.build();
    }

    public ActiveFileInput apiActiveFileToDev(ActiveFileNeInput input){
        if(input==null){
            return null;
        }
        ActiveFileInputBuilder builder = new ActiveFileInputBuilder();
        builder.setFileName(input.getFileName());
        builder.setFileType(apiFileTypeToDev(input.getFileType()));
        return builder.build();
    }

    public ActiveFileNeOutput devActiveFileToApi(ActiveFileOutput output){
        if(output==null){
            return null;
        }
        ActiveFileNeOutputBuilder builder = new ActiveFileNeOutputBuilder();
        builder.setMsg(output.getMsg());
        return builder.build();
    }

    public GetActiveStatusInput apiGetActiveStatusToDev(GetActiveStatusNeInput input){
        if(input==null){
            return null;
        }
        GetActiveStatusInputBuilder builder = new GetActiveStatusInputBuilder();
        builder.setFileType(apiFileTypeToDev(input.getFileType()));
        return builder.build();
    }

    public GetActiveStatusNeOutput devGetActiveStatusToApi(GetActiveStatusOutput output){
        if(output==null){
            return null;
        }
        GetActiveStatusNeOutputBuilder builder = new GetActiveStatusNeOutputBuilder();
        builder.setActiveTime(output.getActiveTime());
        builder.setFileName(output.getFileName());
        builder.setMsg(output.getMsg());
        builder.setResult(devResultToApi(output.getResult()));
        return builder.build();
    }

    public GetDownloadStatusInput apiGetDownloadStatusToDev(GetDownloadStatusNeInput input){
        if(input==null){
            return null;
        }
        GetDownloadStatusInputBuilder builder = new GetDownloadStatusInputBuilder();
        builder.setFileName(input.getFileName());
        builder.setFileType(apiFileTypeToDev(input.getFileType()));
        return builder.build();
    }

    public GetDownloadStatusNeOutput devGetDownloadStatusToApi(GetDownloadStatusOutput output){
        if(output==null){
            return null;
        }
        GetDownloadStatusNeOutputBuilder builder = new GetDownloadStatusNeOutputBuilder();
        builder.setMsg(output.getMsg());
        builder.setResult(devResultToApi(output.getResult()));
        return builder.build();
    }
}
