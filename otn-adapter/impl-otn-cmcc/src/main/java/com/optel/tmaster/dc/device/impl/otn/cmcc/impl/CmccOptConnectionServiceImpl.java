/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.device.impl.base.otn.BaseOptConnectionServiceImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.service.ConnectionEosTransformImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.service.ConnectionEthTransformImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.service.ConnectionMngTransformImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.service.ConnectionOduTransformImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.service.ConnectionSdhTransformImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.service.ConnectionTransformImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.service.EthLanTransformImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.service.EthTreeTransformImpl;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.service.VsiTransformImpl;
import com.optel.tmaster.dc.general.base.exception.device.DeviceNotSupportException;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.AddVsiNniMemberNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.AddVsiNniMemberNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.AddVsiUniMemberNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.AddVsiUniMemberNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEoOsuConnectionNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEoOsuConnectionNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEos2eoosuConnectionNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEos2eoosuConnectionNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEosConnectionNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEosConnectionNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEthConnectionNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEthConnectionNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEthLanServiceNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEthLanServiceNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEthTreeServiceNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateEthTreeServiceNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateOduConnectionNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateOduConnectionNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateOsuConnectionNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateOsuConnectionNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateSdhConnectionNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.CreateSdhConnectionNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.DeleteConnectionGroupNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.DeleteConnectionGroupNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.DeleteConnectionInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.DeleteConnectionOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.DeleteVsiInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.DeleteVsiMemberNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.DeleteVsiMemberNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.DeleteVsiOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.GetVsiMemberAttrNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.GetVsiMemberAttrNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.ModifyEthConnectionCapacityNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.ModifyEthConnectionCapacityNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.ModifyOduConnectionCapacityNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.ModifyOduConnectionCapacityNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.ModifyOsuConnectionCapacityInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.ModifyOsuConnectionCapacityOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.ModifyVcgConnectionCapacityNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.ModifyVcgConnectionCapacityNeOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.ModifyVcgConnectionCapacityNeOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.ModifyVsiInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.ModifyVsiOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.OsuTwoWayDmInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.OsuTwoWayDmOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.QueryConnectionInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.QueryConnectionOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.QueryConnectionOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.QueryVsiInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.QueryVsiOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.query.connection.output.ConnectionOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.query.connection.output.ConnectionOutputKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.AddVsiNniMemberOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.AddVsiUniMemberOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.CreateEthLanServiceOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.CreateEthTreeServiceOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.DeleteConnectionGroupInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.GetVsiMemberAttrInputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.GetVsiMemberAttrOutput;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.OptOtnExtensionService;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.Vsis;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.vsis.grouping.Vsi;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev200805.vsis.grouping.VsiKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.Connections;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.connections.Connection;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.connections.ConnectionKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev190213.AccEosService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev190213.CreateEosConnectionOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev190213.ModifyVcgConnectionCapacityOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.AccEthService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev200611.CreateEthConnectionOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.AccOtnService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.CreateOduConnectionOutput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.AccSdhService;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev200708.CreateSdhConnectionOutput;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

import java.util.HashMap;
import java.util.concurrent.Future;

/**
 * <ul>
 * <li>(OTN 设备连接实现类)</li>
 * </ul>
 *
 * @author LWX 2020/4/28 16:05
 */
public class CmccOptConnectionServiceImpl extends BaseOptConnectionServiceImpl implements IDeviceServiceOtnCmcc {

    @Override
    public ListenableFuture<RpcResult<ModifyVcgConnectionCapacityNeOutput>> modifyVcgConnectionCapacityNe(ModifyVcgConnectionCapacityNeInput input) {
        AccEosService accEosService = MountTools.getRpcService(input.getNeId(), AccEosService.class);
        ConnectionMngTransformImpl connectionMngTransform = new ConnectionMngTransformImpl();
        Future<RpcResult<ModifyVcgConnectionCapacityOutput>> resultFuture = accEosService.modifyVcgConnectionCapacity(
                connectionMngTransform.apiModifyVcgConnectionCapacityInputToDev(input));
        return RpcResultUtil.buildFutureResult(resultFuture, (result -> {
            ModifyVcgConnectionCapacityNeOutputBuilder modifyVcgConnectionCapacityNeOutputBuilder = new ModifyVcgConnectionCapacityNeOutputBuilder();
            modifyVcgConnectionCapacityNeOutputBuilder.setConnectionName(result.getConnectionName());
            modifyVcgConnectionCapacityNeOutputBuilder.setCtpName(result.getCtpName());
            return modifyVcgConnectionCapacityNeOutputBuilder.build();
        }));
    }

    @Override
    public ListenableFuture<RpcResult<DeleteConnectionGroupNeOutput>> deleteConnectionGroupNe(DeleteConnectionGroupNeInput input) {
        OptOtnExtensionService optOtnExtensionService = MountTools.getRpcService(input.getNeId(), OptOtnExtensionService.class);
        DeleteConnectionGroupInputBuilder groupInputBuilder = new DeleteConnectionGroupInputBuilder().setGroupId(input.getGroupId());
        return RpcResultUtil.buildFutureResult(optOtnExtensionService.deleteConnectionGroup(groupInputBuilder.build()));
    }

    @Override
    public ListenableFuture<RpcResult<CreateEoOsuConnectionNeOutput>> createEoOsuConnectionNe(CreateEoOsuConnectionNeInput input) {
        throw new DeviceNotSupportException(input.getNeId(), "Cmcc Device Not Supported");
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
        return RpcResultUtil.buildFutureResult(resultFuture, (connectionTransform::devCreateEthConnectionOutputToApi));
    }

    @Override
    public ListenableFuture<RpcResult<DeleteConnectionOutput>> deleteConnection(DeleteConnectionInput input) {
        InstanceIdentifier<Connection> iid = create(Connections.class).child(Connection.class, new ConnectionKey(input.getName()));
        MountTools.deleteFromConfig(input.getNeId(), iid);
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<QueryVsiOutput>> queryVsi(QueryVsiInput input) {
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
        return RpcResultUtil.buildFutureResult(resultFuture, connectionOduTransform::devCreateOduConnectionOutputToApi);
    }

    @Override
    public ListenableFuture<RpcResult<CreateOsuConnectionNeOutput>> createOsuConnectionNe(CreateOsuConnectionNeInput input) {
        throw new DeviceNotSupportException(input.getNeId(), "Cmcc Device Not Supported");
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
        return RpcResultUtil.buildFutureResult(resultFuture, connectionSdhTransform::devCreateSdhConnectionOutputToApi);
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
        return RpcResultUtil.buildFutureResult(resultFuture, (connectionEosTransform::devCreateEosConnectionOutputToApi));
    }

    @Override
    public ListenableFuture<RpcResult<QueryConnectionOutput>> queryConnection(QueryConnectionInput input) {
        HashMap<ConnectionOutputKey, ConnectionOutput> connectionOutMap = new HashMap<>(16);
        if (input.getName() != null && !"".equals(input.getName())) {
            InstanceIdentifier<Connection> iid = create(Connections.class).child(Connection.class, new ConnectionKey(input.getName()));
            Connection connection = MountTools.queryFromOperational(input.getNeId(), iid);
            if (connection != null) {
                ConnectionOutput connectionOutput = new ConnectionTransformImpl().devConnectionOutToApi(connection);
                connectionOutMap.put(connectionOutput.key(), connectionOutput);
            }
        } else {
            InstanceIdentifier<Connections> iid = create(Connections.class);
            Connections connections = MountTools.queryFromOperational(input.getNeId(), iid);
            if (connections != null && connections.getConnection() != null) {
                ConnectionTransformImpl connectionTransform = new ConnectionTransformImpl();
                for (Connection connection : connections.getConnection().values()) {
                    ConnectionOutput connectionOutput = connectionTransform.devConnectionOutToApi(connection);
                    connectionOutMap.put(connectionOutput.key(), connectionOutput);
                }
            }
        }
        QueryConnectionOutputBuilder queryConnectionOutputBuilder = new QueryConnectionOutputBuilder();
        queryConnectionOutputBuilder.setConnectionOutput(connectionOutMap);
        return RpcResultUtil.success(queryConnectionOutputBuilder.build());
    }

    @Override
    public ListenableFuture<RpcResult<CreateEos2eoosuConnectionNeOutput>> createEos2eoosuConnectionNe(CreateEos2eoosuConnectionNeInput input) {
        throw new DeviceNotSupportException(input.getNeId(), "Cmcc Device Not Supported");
    }

    @Override
    public ListenableFuture<RpcResult<ModifyOduConnectionCapacityNeOutput>> modifyOduConnectionCapacityNe(ModifyOduConnectionCapacityNeInput input) {
        AccOtnService accOtnService = MountTools.getRpcService(input.getNeId(), AccOtnService.class);
        return RpcResultUtil.buildFutureResult(accOtnService.modifyOduConnectionCapacity(new ConnectionMngTransformImpl().apiModifyOduConnectionCapacityInputToDev(input)));
    }

    @Override
    public ListenableFuture<RpcResult<OsuTwoWayDmOutput>> osuTwoWayDm(OsuTwoWayDmInput input) {
        throw new DeviceNotSupportException(input.getNeId(), "Cmcc Device Not Supported");
    }

    @Override
    public ListenableFuture<RpcResult<ModifyOsuConnectionCapacityOutput>> modifyOsuConnectionCapacity(ModifyOsuConnectionCapacityInput input) {
        throw new DeviceNotSupportException(input.getNeId(), "Cmcc Device Not Supported");
    }
}
