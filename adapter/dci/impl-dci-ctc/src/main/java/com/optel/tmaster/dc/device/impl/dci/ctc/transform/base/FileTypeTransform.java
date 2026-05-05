/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.transform.base;

import com.optel.tmaster.dc.dci.impl.base.transform.ITransform;
import com.optel.tmaster.dc.general.base.exception.manage.NoMatchEnumValueException;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.rpc.rev220208.DownloadMsgType;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.rpc.rev220208.UploadMsgType;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetActiveStatusNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetDownloadStatusNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.GetFileListNeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.UploadFileNeInput;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.get.file.list.ne.output.FileNames;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.get.file.list.ne.output.FileNamesBuilder;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.file.rev200210.get.file.list.ne.output.FileNamesKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.FileType;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetActiveStatusOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetDownloadStatusOutput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.GetFileListInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.RpcResult;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.UploadFileInput;

import java.util.HashMap;
import java.util.Map;

/**
 * 文件管理 数据类型转换
 * 2022/3/14 16:20
 *
 * @author LongXianYong
 * @version V1.0.0
 */
public interface FileTypeTransform extends ITransform {

    /**
     * FileType api to dev
     * @param fileType api
     * @return dev
     */
    default FileType apiFileTypeToDev(org.opendaylight.yang.gen.v1.com.optel.dci.yang.rpc.rev220208.FileType fileType){
        if(fileType==null){
            return null;
        }
        switch (fileType){
            case Upgrade:
                return FileType.Upgrade;
            case Data:
                return FileType.Data;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(fileType);
        }
    }

    /**
     * DownloadMsgType dev to api
     * @param downloadMsgType dev
     * @return api
     */
    default DownloadMsgType devDownloadMsgTypeToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.DownloadMsgType downloadMsgType){
        if(downloadMsgType==null){
            return null;
        }
        switch (downloadMsgType){
            case _0Success:
                return DownloadMsgType._0Success;
            case _999Custom:
                return DownloadMsgType._999Custom;
            case _130Timeout:
                return DownloadMsgType._130Timeout;
            case _110AuthFail:
                return DownloadMsgType._110AuthFail;
            case _100SocketFail:
                return DownloadMsgType._100SocketFail;
            case _120FileNotExist:
                return DownloadMsgType._120FileNotExist;
            case _140ValidateFail:
                return DownloadMsgType._140ValidateFail;
            case _150FileTypeMismatch:
                return DownloadMsgType._150FileTypeMismatch;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(downloadMsgType);
        }
    }

    /**
     * RpcResult dev to api
     * @param rpcResult dev
     * @return api
     */
    default RpcResult devRpcResultToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.RpcResult rpcResult){
        if(rpcResult==null){
            return null;
        }
        switch (rpcResult){
            case FAIL:
                return RpcResult.FAIL;
            case SUCCESS:
                return RpcResult.SUCCESS;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(rpcResult);
        }
    }

    /**
     * FileType dev to api
     * @param fileType dev
     * @return api
     */
    default UploadFileInput.FileType apiFileTypeToDev(UploadFileNeInput.FileType fileType){
        if(fileType==null){
            return null;
        }
        switch (fileType){
            case Data:
                return UploadFileInput.FileType.Data;
            case Log:
                return UploadFileInput.FileType.Log;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(fileType);
        }
    }

    /**
     * uploadMsgType dev to api
     * @param uploadMsgType dev
     * @return api
     */
    default UploadMsgType devUploadMsgTypeToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.UploadMsgType uploadMsgType){
        if(uploadMsgType==null){
            return null;
        }
        switch (uploadMsgType){
            case _150FileTypeMismatch:
                return UploadMsgType._150FileTypeMismatch;
            case _100SocketFail:
                return UploadMsgType._100SocketFail;
            case _110AuthFail:
                return UploadMsgType._110AuthFail;
            case _130Timeout:
                return UploadMsgType._130Timeout;
            case _999Custom:
                return UploadMsgType._999Custom;
            case _0Success:
                return UploadMsgType._0Success;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(uploadMsgType);
        }
    }

    /**
     * RpcResult dev to api
     * @param rpcResult dev
     * @return api
     */
    default RpcResult devUploadFileRpcResultToApi(
            org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.RpcResult rpcResult){
        if(rpcResult==null){
            return null;
        }
        switch (rpcResult){
            case SUCCESS:
                return RpcResult.SUCCESS;
            case FAIL:
                return RpcResult.FAIL;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(rpcResult);
        }
    }

    /**
     * FileType api to dev
     * @param fileType api
     * @return dev
     */
    default GetFileListInput.FileType apiFileTypeToDev(GetFileListNeInput.FileType fileType){
        if(fileType==null){
            return null;
        }
        switch (fileType){
            case Log:
                return GetFileListInput.FileType.Log;
            case Data:
                return GetFileListInput.FileType.Data;
            case Upgrade:
                return GetFileListInput.FileType.Upgrade;
            case All:
                return GetFileListInput.FileType.All;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(fileType);
        }
    }

    /**
     * file name dev to api
     * @param map dev
     * @return api
     */
    default Map<FileNamesKey, FileNames> devFileNameToApi(Map<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.get.file.list.output.FileNamesKey, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.get.file.list.output.FileNames> map){
        if(map==null || map.size()==0){
            return null;
        }
        Map<FileNamesKey, FileNames> resultMap = new HashMap<>(map.size());
        for(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.rpc.rev200630.get.file.list.output.FileNames e: map.values()){
            FileNamesBuilder builder = new FileNamesBuilder();
            builder.setFileName(e.getFileName());
            resultMap.put(new FileNamesKey(e.getFileName()),builder.build());
        }
        return resultMap;
    }

    /**
     * dev to api
     * @param result dev
     * @return api
     */
    default GetActiveStatusNeOutput.Result devResultToApi(GetActiveStatusOutput.Result result){
        if(result==null){
            return null;
        }
        switch (result){
            case Success:
            case SUCCESS:
                return GetActiveStatusNeOutput.Result.Success;
            case Fail:
            case FAIL:
                return GetActiveStatusNeOutput.Result.Fail;
            case Processing:
            case PROCESSING:
                return GetActiveStatusNeOutput.Result.Processing;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(result);
        }
    }

    /**
     * dev to api
     * @param result dev
     * @return api
     */
    default GetDownloadStatusNeOutput.Result devResultToApi(GetDownloadStatusOutput.Result result){
        if(result==null){
            return null;
        }
        switch (result){
            case Processing:
            case PROCESSING:
                return GetDownloadStatusNeOutput.Result.Processing;
            case Fail:
            case FAIL:
                return GetDownloadStatusNeOutput.Result.Fail;
            case Success:
            case SUCCESS:
                return GetDownloadStatusNeOutput.Result.Success;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(result);
        }
    }


}
