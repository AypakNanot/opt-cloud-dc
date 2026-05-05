/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptConnectionServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.OsuTransformImpl;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.service.*;
import com.optel.tmaster.dc.device.impl.otn.ctc.util.OtnUtils;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import com.optel.tmaster.dc.general.nc.nccore.NcTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.*;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.query.connection.output.ConnectionOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.*;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.vsis.grouping.Vsi;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.vsis.grouping.VsiKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.Connections;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connections.Connection;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connections.ConnectionKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.AccEosService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.CreateEosConnectionOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.ModifyVcgConnectionCapacityOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.AccEthService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.CreateEthConnectionOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.AccOsuService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.CreateEoOsuConnectionOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.CreateEos2eoosuConnectionOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.CreateOsuConnectionOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.AccOtnService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.CreateOduConnectionOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.AccSdhService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.CreateSdhConnectionOutput;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.Future;

/**
 * <ul>
 * <li>(OTN 设备连接实现类)</li>
 * </ul>
 *
 * @author LWX 2020/4/28 16:05
 */

public class CtcOptConnectionServiceImpl extends BaseOptConnectionServiceImpl implements IDeviceServiceOtnCtc {

    @Override
    public ListenableFuture<RpcResult<ModifyVcgConnectionCapacityNeOutput>> modifyVcgConnectionCapacityNe(ModifyVcgConnectionCapacityNeInput input) {
        AccEosService accEosService = MountTools.getRpcService(input.getNeId(), AccEosService.class);
        ConnectionMngTransformImpl connectionMngTransform = new ConnectionMngTransformImpl();
        Future<RpcResult<ModifyVcgConnectionCapacityOutput>> resultFuture = accEosService.modifyVcgConnectionCapacity(
                connectionMngTransform.apiModifyVcgConnectionCapacityInputToDev(input));
        return RpcResultUtil.buildFutureResult(resultFuture, (result -> {
            if (Objects.isNull(result)) {
                return null;
            }
            if (result.getErrOrOk().implementedInterface().equals(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.modify.vcg.connection.capacity.output.err.or.ok.OperOk.class)) {
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.modify.vcg.connection.capacity.output.err.or.ok.OperOk operOk = (org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.modify.vcg.connection.capacity.output.err.or.ok.OperOk) result.getErrOrOk();
                ModifyVcgConnectionCapacityNeOutputBuilder modifyVcgConnectionCapacityNeOutputBuilder = new ModifyVcgConnectionCapacityNeOutputBuilder();
                modifyVcgConnectionCapacityNeOutputBuilder.setConnectionName(operOk.getConnectionName());
                modifyVcgConnectionCapacityNeOutputBuilder.setCtpName(operOk.getCtpName());
                return modifyVcgConnectionCapacityNeOutputBuilder.build();
            } else {
                // 这种情况不会发生
                return null;
            }
        }), r -> OtnUtils.getModifyVcgConnectionCapacityOutRpcResult(r.getErrOrOk()));
    }

    @Override
    public ListenableFuture<RpcResult<DeleteConnectionGroupNeOutput>> deleteConnectionGroupNe(DeleteConnectionGroupNeInput input) {
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        DeleteConnectionGroupInputBuilder groupInputBuilder = new DeleteConnectionGroupInputBuilder().setGroupId(input.getGroupId());
        return RpcResultUtil.buildFutureResult(optOtnExtensionService.deleteConnectionGroup(groupInputBuilder.build()));
    }

    @Override
    public ListenableFuture<RpcResult<CreateEoOsuConnectionNeOutput>> createEoOsuConnectionNe(CreateEoOsuConnectionNeInput input) {
        AccOsuService osuService = MountTools.getRpcService(input.getNeId(), AccOsuService.class);
        ConnectionOsuTransformImpl connectionOsuTransform = new ConnectionOsuTransformImpl();
        Future<RpcResult<CreateEoOsuConnectionOutput>> eoOsuConnection
                = osuService.createEoOsuConnection(connectionOsuTransform.apiCreateEoOsuConnectionToDev(input));
        return RpcResultUtil.buildFutureResult(eoOsuConnection, connectionOsuTransform::devCreateEoOsuConnectionToApi, r -> OtnUtils.getEoOsuConnectionOutRpcResult(r.getErrOrOk()));
    }

    @Override
    public ListenableFuture<RpcResult<ModifyEthConnectionCapacityNeOutput>> modifyEthConnectionCapacityNe(ModifyEthConnectionCapacityNeInput input) {
        InstanceIdentifier<Connection> iid = create(Connections.class).child(Connection.class, new ConnectionKey(input.getConName()));
        Connection connection = new ConnectionMngTransformImpl().apiModifyEthConnectionCapacityNeInputToDev(input);
        //下发数据至设备
        MountTools.doMergeToConfig(input.getNeId(), iid, connection);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<DeleteVsiMemberNeOutput>> deleteVsiMemberNe(DeleteVsiMemberNeInput input) {
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        return RpcResultUtil.buildFutureResult(optOtnExtensionService.deleteVsiMember(new VsiTransformImpl().apiDeleteVsiMemberInput(input)));
    }

    @Override
    public ListenableFuture<RpcResult<CreateEthLanServiceNeOutput>> createEthLanServiceNe(CreateEthLanServiceNeInput input) {
        EthLanTransformImpl ethLanTransform = new EthLanTransformImpl();
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        Future<RpcResult<CreateEthLanServiceOutput>> ethLanService = optOtnExtensionService.createEthLanService(ethLanTransform.apiCreateEthLanServiceInputToDev(input));
        return RpcResultUtil.buildFutureResult(ethLanService, ethLanTransform::devCreateEthLanServiceOutputToApi);
    }

    @Override
    public ListenableFuture<RpcResult<CreateEthConnectionNeOutput>> createEthConnectionNe(CreateEthConnectionNeInput input) {
        AccEthService accEthService = MountTools.getRpcService(input.getNeId(), AccEthService.class);
        ConnectionEthTransformImpl connectionTransform = new ConnectionEthTransformImpl();
        Future<RpcResult<CreateEthConnectionOutput>> resultFuture = accEthService.createEthConnection(connectionTransform.apiCreateEthConnectionInputToDev(input));
        return RpcResultUtil.buildFutureResult(resultFuture, connectionTransform::devCreateEthConnectionOutputToApi, r -> OtnUtils.getEthConnectionOutRpcResult(r.getErrOrOk()));
    }

    @Override
    public ListenableFuture<RpcResult<DeleteConnectionOutput>> deleteConnection(DeleteConnectionInput input) {
        InstanceIdentifier<Connection> iid = create(Connections.class).child(Connection.class, new ConnectionKey(input.getName()));
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryVsiOutput>> queryVsi(QueryVsiInput input) {
        if (!NcTools.isSupportOptelExt(input.getNeId())) {
            return RpcResultUtil.success();
        }
        InstanceIdentifier<Vsis> iid = create(Vsis.class);
        Vsis vsis = MountTools.queryFromOperational(input.getNeId(), iid);
        return RpcResultUtil.success(new VsiTransformImpl().devQueryVsiToApi(vsis));
    }

    @Override
    public ListenableFuture<RpcResult<AddVsiNniMemberNeOutput>> addVsiNniMemberNe(AddVsiNniMemberNeInput input) {
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(),
                OptOtnExtensionService.class);
        Future<RpcResult<AddVsiNniMemberOutput>> rpcResultFuture
                = optOtnExtensionService.addVsiNniMember(new VsiTransformImpl().apiAddVsiNniMemberToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResultFuture, (result -> new VsiTransformImpl().devAddVsiNniMemberOutputToDev(result)));
    }

    @Override
    public ListenableFuture<RpcResult<ModifyVsiOutput>> modifyVsi(ModifyVsiInput input) {
        InstanceIdentifier<Vsi> iid = create(Vsis.class).child(Vsi.class, new VsiKey(input.getName()));
        MountTools.doMergeToConfig(input.getNeId(), iid, new VsiTransformImpl().apiModifyVsiInputToDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<CreateEthTreeServiceNeOutput>> createEthTreeServiceNe(CreateEthTreeServiceNeInput input) {
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        EthTreeTransformImpl ethTreeTransform = new EthTreeTransformImpl();
        Future<RpcResult<CreateEthTreeServiceOutput>> ethTreeService = optOtnExtensionService.createEthTreeService(ethTreeTransform.apiCreateTreeInputToDev(input));
        return RpcResultUtil.buildFutureResult(ethTreeService, ethTreeTransform::devCreateEthTreeServiceToApi);
    }

    @Override
    public ListenableFuture<RpcResult<CreateOduConnectionNeOutput>> createOduConnectionNe(CreateOduConnectionNeInput input) {
        AccOtnService accEthService = MountTools.getRpcService(input.getNeId(), AccOtnService.class);
        ConnectionOduTransformImpl connectionOduTransform = new ConnectionOduTransformImpl();
        Future<RpcResult<CreateOduConnectionOutput>> resultFuture = accEthService.createOduConnection(connectionOduTransform.apiCreateOduConnectionInputToDev(input));
        return RpcResultUtil.buildFutureResult(resultFuture, connectionOduTransform::devCreateOduConnectionOutputToApi, r -> OtnUtils.getOduConnectionOutRpcResult(r.getErrOrOk()));
    }

    @Override
    public ListenableFuture<RpcResult<CreateOsuConnectionNeOutput>> createOsuConnectionNe(CreateOsuConnectionNeInput input) {
        AccOsuService osuService = MountTools.getRpcService(input.getNeId(), AccOsuService.class);
        ConnectionOsuTransformImpl osuTransform = new ConnectionOsuTransformImpl();
        Future<RpcResult<CreateOsuConnectionOutput>> osuConnection
                = osuService.createOsuConnection(osuTransform.apiCreateOsuConnectionToDev(input));
        return RpcResultUtil.buildFutureResult(osuConnection, osuTransform::devCreateOsuConnectionToApi, r -> OtnUtils.getOsuConnectionOutRpcResult(r.getErrOrOk()));
    }

    @Override
    public ListenableFuture<RpcResult<AddVsiUniMemberNeOutput>> addVsiUniMemberNe(AddVsiUniMemberNeInput input) {
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        Future<RpcResult<AddVsiUniMemberOutput>> rpcResultFuture
                = optOtnExtensionService.addVsiUniMember(new VsiTransformImpl().apiAddVsiUniMemberToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResultFuture, (result -> new VsiTransformImpl().devAddVsiUniMemberToApi(result)));
    }

    @Override
    public ListenableFuture<RpcResult<GetVsiMemberAttrNeOutput>> getVsiMemberAttrNe(GetVsiMemberAttrNeInput input) {
        GetVsiMemberAttrInputBuilder getVsiMemberAttrInputBuilder = new GetVsiMemberAttrInputBuilder();
        getVsiMemberAttrInputBuilder.setVsiName(input.getVsiName());
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        Future<RpcResult<GetVsiMemberAttrOutput>> rpcResultFuture
                = optOtnExtensionService.getVsiMemberAttr(getVsiMemberAttrInputBuilder.build());
        return RpcResultUtil.buildFutureResult(rpcResultFuture, (result -> new VsiTransformImpl().devGetVsiMemberAttrToApi(result)));
    }

    @Override
    public ListenableFuture<RpcResult<CreateSdhConnectionNeOutput>> createSdhConnectionNe(CreateSdhConnectionNeInput input) {
        AccSdhService accEthService = MountTools.getRpcService(input.getNeId(), AccSdhService.class);
        ConnectionSdhTransformImpl connectionSdhTransform = new ConnectionSdhTransformImpl();
        Future<RpcResult<CreateSdhConnectionOutput>> resultFuture = accEthService.createSdhConnection(connectionSdhTransform.apiCreateSdhInputToDev(input));
        return RpcResultUtil.buildFutureResult(resultFuture, connectionSdhTransform::devCreateSdhConnectionOutputToApi, r -> OtnUtils.getSdhConnectionOutRpcResult(r.getErrOrOk()));
    }

    @Override
    public ListenableFuture<RpcResult<DeleteVsiOutput>> deleteVsi(DeleteVsiInput input) {
        InstanceIdentifier<Vsi> iid = create(Vsis.class).child(Vsi.class, new VsiKey(input.getName()));
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<CreateEosConnectionNeOutput>> createEosConnectionNe(CreateEosConnectionNeInput input) {
        AccEosService accEthService = MountTools.getRpcService(input.getNeId(), AccEosService.class);
        ConnectionEosTransformImpl connectionEosTransform = new ConnectionEosTransformImpl();
        Future<RpcResult<CreateEosConnectionOutput>> resultFuture = accEthService.createEosConnection(connectionEosTransform.apiCreateEosConnectionInputToDev(input));
        return RpcResultUtil.buildFutureResult(resultFuture, (connectionEosTransform::devCreateEosConnectionOutputToApi), r -> OtnUtils.getEosConnectionOutRpcResult(r.getErrOrOk()));
    }

    /**
     * 查询连接
     *
     * @return c
     */
    @Override
    public ListenableFuture<RpcResult<QueryConnectionOutput>> queryConnection(QueryConnectionInput input) {
        List<ConnectionOutput> connectionOuts = new ArrayList<>();
        if (input.getName() != null && !"".equals(input.getName())) {
            InstanceIdentifier<Connection> iid = create(Connections.class).child(Connection.class, new ConnectionKey(input.getName()));
            Connection connection = MountTools.queryFromOperational(input.getNeId(), iid);
            if (connection != null) {
                connectionOuts.add(new ConnectionTransformImpl().devConnectionOutToApi(connection));
            }
        } else {
            InstanceIdentifier<Connections> iid = create(Connections.class);
            Connections connections = MountTools.queryFromOperational(input.getNeId(), iid);
            if (connections != null && connections.getConnection() != null) {
                ConnectionTransformImpl connectionTransform = new ConnectionTransformImpl();
                for (Connection connection : connections.getConnection().values()) {
                    connectionOuts.add(connectionTransform.devConnectionOutToApi(connection));
                }
            }
        }
        QueryConnectionOutputBuilder queryConnectionOutputBuilder = new QueryConnectionOutputBuilder();
        queryConnectionOutputBuilder.setConnectionOutput(ltm(connectionOuts));
        return RpcResultUtil.success(queryConnectionOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<CreateEos2eoosuConnectionNeOutput>> createEos2eoosuConnectionNe(CreateEos2eoosuConnectionNeInput input) {
        AccOsuService osuService = MountTools.getRpcService(input.getNeId(), AccOsuService.class);
        ConnectionOsuTransformImpl osuTransform = new ConnectionOsuTransformImpl();
        Future<RpcResult<CreateEos2eoosuConnectionOutput>> eos2eoosuConnection
                = osuService.createEos2eoosuConnection(osuTransform.apiCreateEos2eoosuConnectionToDev(input));
        return RpcResultUtil.buildFutureResult(eos2eoosuConnection, osuTransform::devCreateEos2eoosuConnectionToApi, r -> OtnUtils.getEos2eoosuConnectionOutRpcResult(r.getErrOrOk()));
    }

    @Override
    public ListenableFuture<RpcResult<ModifyOduConnectionCapacityNeOutput>> modifyOduConnectionCapacityNe(ModifyOduConnectionCapacityNeInput input) {
        AccOtnService accOtnService = MountTools.getRpcService(input.getNeId(), AccOtnService.class);
        return RpcResultUtil.buildFutureResult(accOtnService.modifyOduConnectionCapacity(new ConnectionMngTransformImpl().apiModifyOduConnectionCapacityInputToDev(input)));
    }

    @Override
    public ListenableFuture<RpcResult<OsuTwoWayDmOutput>> osuTwoWayDm(OsuTwoWayDmInput input) {
        AccOsuService osuService = MountTools.getRpcService(input.getNeId(), AccOsuService.class);
        OsuTransformImpl osuTransform = new OsuTransformImpl();
        Future<RpcResult<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.OsuTwoWayDmOutput>> rpcResultFuture = osuService.osuTwoWayDm(osuTransform.apiOsuTwoWayDelayToDev(input));
        return RpcResultUtil.buildFutureResult(rpcResultFuture, result -> new OsuTwoWayDmOutputBuilder(osuTransform.devOsuTwoWayDelayToApi(result)).build());
    }

    @Override
    public ListenableFuture<RpcResult<ModifyOsuConnectionCapacityOutput>> modifyOsuConnectionCapacity(ModifyOsuConnectionCapacityInput input) {
        AccOsuService osuService = MountTools.getRpcService(input.getNeId(), AccOsuService.class);
        OsuTransformImpl osuTransform = new OsuTransformImpl();
        return RpcResultUtil.buildFutureResult(osuService.modifyOsuConnectionCapacity(osuTransform.apiToOsuConnectionCapacityDev(input)));
    }
}
