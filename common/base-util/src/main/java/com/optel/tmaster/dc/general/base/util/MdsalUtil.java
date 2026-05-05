/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.base.util;

import com.google.common.util.concurrent.FluentFuture;
import com.optel.tmaster.dc.common.OptelDcException;
import com.optel.tmaster.dc.general.base.exception.manage.DataStoreOperateException;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.mdsal.binding.api.ReadTransaction;
import org.opendaylight.mdsal.binding.api.WriteTransaction;
import org.opendaylight.mdsal.common.api.CommitInfo;
import org.opendaylight.mdsal.common.api.LogicalDatastoreType;
import org.opendaylight.yangtools.yang.binding.DataObject;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.OperationFailedException;
import org.opendaylight.yangtools.yang.common.RpcError;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutionException;

/**
 * ClassName: MdsalUtil
 * <ul>
 * <li>(数据库操作类)</li>
 * </ul>
 *
 * @author LWX 2019年9月24日下午1:45:46
 */
public class MdsalUtil {
    private static final Logger LOG = LoggerFactory.getLogger(MdsalUtil.class);
    private MdsalUtil() {
    }

    /**
     * 从config库查询
     *
     * @param dataBroker 数据
     * @param iid        查询路径path
     * @param <D>        查询类
     * @return 查询结果
     */
    public static <D extends DataObject> D readFromConfig(DataBroker dataBroker, InstanceIdentifier<D> iid) {
        return read(dataBroker, iid, LogicalDatastoreType.CONFIGURATION);
    }

    /**
     * 从operational数据库中查询数据
     *
     * @param dataBroker dataBroker
     * @param iid        查询路径path
     * @param <D>        查询类
     * @return 查询结果
     */
    public static <D extends DataObject> D readFromOperational(DataBroker dataBroker, InstanceIdentifier<D> iid) {
        return read(dataBroker, iid, LogicalDatastoreType.OPERATIONAL);
    }

    /**
     * 查询
     */
    public static <D extends DataObject> D read(DataBroker dataBroker, InstanceIdentifier<D> iid, LogicalDatastoreType configuration) {
        @NonNull ReadTransaction readTransaction = dataBroker.newReadOnlyTransaction();
        try {
            Optional<D> dataOptional = readTransaction.read(configuration, iid).get();
            if (dataOptional.isPresent()) {
                return dataOptional.get();
            } else {
                LOG.debug("Read Data is not present.,iid is:{}", iid);
                return null;
            }
        } catch (InterruptedException | ExecutionException e) {
            List<RpcError> rpcErrors = new ArrayList<>();
            if (e.getCause() instanceof OperationFailedException) {
                OperationFailedException exception = (OperationFailedException) e.getCause();
                rpcErrors.addAll(exception.getErrorList());
            }
            LOG.error("Read Data occur error,iid:{}", iid, e);
            throw new DataStoreOperateException("Read Data Error", e, rpcErrors.toArray(new RpcError[0]));
        }

    }

    /**
     * MethodName: doMergeToConfig
     * <ul>
     * <li>(更新config数据库数据)</li>
     * </ul>
     *
     * @param dataBroker   dataBroker
     * @param resourcePath 数据路径
     * @param resource     数据对象
     * @param <D>          操作类
     * @author LWX 2019年9月24日下午3:52:00
     */
    public static <D extends DataObject> void doMergeToConfig(DataBroker dataBroker, InstanceIdentifier<D> resourcePath, D resource) {
        doMerge(dataBroker, resourcePath, resource, LogicalDatastoreType.CONFIGURATION);
    }

    public static <D extends DataObject> void doMergeToOperational(DataBroker dataBroker, InstanceIdentifier<D> resourcePath, D resource) {
        doMerge(dataBroker, resourcePath, resource, LogicalDatastoreType.OPERATIONAL);
    }

    /**
     * MethodName: doMerge
     * <ul>
     * <li>(更新数据，操作失败直接抛出异常)</li>
     * </ul>
     *
     * @param dataBroker    dataBroker
     * @param resourcePath  数据路径
     * @param resource      数据对象
     * @param <D>           操作类
     * @param datastoreType logicalDataStore类型
     * @author LWX 2019年9月24日下午6:20:23
     */
    public static <D extends DataObject> void doMerge(DataBroker dataBroker, InstanceIdentifier<D> resourcePath, D resource, LogicalDatastoreType datastoreType) {
        final WriteTransaction transaction = dataBroker.newWriteOnlyTransaction();
        transaction.merge(datastoreType, resourcePath, resource);
        writeTransactionCommit(transaction);
    }

    /**
     * MethodName: put
     * <ul>
     * <li>(添加/更新数据。如果需要确保父对象存在，但不希望使用put修改其先前存在的状态，考虑使用merge)</li>
     * </ul>
     *
     * @param broker        databroker
     * @param path          数据路径
     * @param data          数据对象
     * @param datastoreType logicalStore 类型
     * @param <D>           操作类
     * @author LWX 2019年9月24日下午4:08:54
     */
    public static <D extends DataObject> void put(DataBroker broker, InstanceIdentifier<D> path, D data, LogicalDatastoreType datastoreType) {
        final WriteTransaction tx = broker.newWriteOnlyTransaction();
        tx.put(datastoreType, path, data);
        writeTransactionCommit(tx);
    }

    private static void writeTransactionCommit(WriteTransaction transaction) {
        FluentFuture<? extends CommitInfo> commit = transaction.commit();
        try {
            commit.get();
        } catch (Throwable throwable) {
            throw new OptelDcException(throwable.getMessage(), throwable);
        }
    }

    /**
     * MethodName: deleteFromConfig
     * <ul>
     * <li>(从config库删除数据)</li>
     * </ul>
     *
     * @param databroker   databroker
     * @param resourcePath 删除路径
     * @param <D>          操作类
     * @author LWX 2019年9月24日下午3:53:47
     */
    public static <D extends DataObject> void deleteFromConfig(DataBroker databroker, InstanceIdentifier<D> resourcePath) {
        delete(databroker, resourcePath, LogicalDatastoreType.CONFIGURATION);
    }

    /**
     * 删除数据
     *
     * @param databroker   数据
     * @param resourcePath 数据路径path
     * @param storeType    数据库类型（config/operational）
     * @param <D>          操作类
     */
    public static <D extends DataObject> void delete(DataBroker databroker, InstanceIdentifier<D> resourcePath, LogicalDatastoreType storeType) {
        WriteTransaction transaction = databroker.newWriteOnlyTransaction();
        transaction.delete(storeType, resourcePath);
        writeTransactionCommit(transaction);
    }

}
