/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.impl;

import com.google.common.util.concurrent.ListenableFuture;
import com.optel.tmaster.dc.dci.impl.base.dci.BaseDciApsServiceImpl;
import com.optel.tmaster.dc.dci.impl.base.transform.ITransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.config.DciApsServiceTransform;
import com.optel.tmaster.dc.device.impl.dci.cmcc.util.DciUtils;
import com.optel.tmaster.dc.general.base.util.RpcResultUtil;
import com.optel.tmaster.dc.general.nc.nccore.MountTools;
import org.eclipse.jdt.annotation.NonNull;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.aps.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.automatic.protection._switch.top.ApsModules;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.automatic.protection._switch.top.aps.modules.ApsModule;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.automatic.protection._switch.top.aps.modules.ApsModuleKey;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.automatic.protection._switch.top.aps.modules.aps.module.Config;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.optical.transport.line.protection.rev230426.transport.line.protection.top.Aps;
import org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.MiniotnRpcService;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.opendaylight.yangtools.yang.common.RpcResult;

/**
 * @author Quan Jingyuan
 * @since 2022/3/25
 **/
public class CmccOptDciApsServiceImpl extends BaseDciApsServiceImpl implements ITransform, IDeviceServiceWdmCmcc {
    private static final String IS_OPT_TEST="isOptTest";
    @Override
    public ListenableFuture<RpcResult<GetApsConfigsOutput>> getApsConfigs(GetApsConfigsInput input) {
         @NonNull InstanceIdentifier<ApsModules> child = create(Aps.class).child(ApsModules.class);

        ApsModules apsModules = null;
        String property = System.getProperty(IS_OPT_TEST, Boolean.FALSE.toString());
        if(Boolean.valueOf(property)){
//            apsModules =ComponentsTestUtil.getApsModules();
        }else{
            apsModules = MountTools.queryFromOperational(input.getNeId(), child);
        }
        return RpcResultUtil.success(new DciApsServiceTransform().devToGetApsConfigsOutputApi(apsModules,input.getName()));
    }

    @Override
    public ListenableFuture<RpcResult<SetApsConfigOutput>> setApsConfig(SetApsConfigInput input) {
        @NonNull InstanceIdentifier<Config> child = create(Aps.class).child(ApsModules.class).child(ApsModule.class, new ApsModuleKey(input.getName())).child(Config.class);
        MountTools.doMergeToConfig(input.getNeId(),child,new DciApsServiceTransform().apiToApsModuleDev(input));
        return RpcResultUtil.success();
    }

    @Override
    public ListenableFuture<RpcResult<SwitchOlpOutput>> switchOlp(SwitchOlpInput input) {

        MiniotnRpcService rpcService = MountTools.getRpcService(input.getNeId(), MiniotnRpcService.class);
        DciApsServiceTransform dciApsServiceTransform = new DciApsServiceTransform();
        ListenableFuture<RpcResult<org.opendaylight.yang.gen.v1.urn.cmcc.yang.rpc.rev230426.SwitchOlpOutput>> rpcResultListenableFuture = rpcService.switchOlp(dciApsServiceTransform.apiToSwitchOlpInputDev(input));
        return RpcResultUtil.buildFutureResult(rpcResultListenableFuture,null, r-> DciUtils.getOcRpcResult(r.getSwitchOlpResult()));
    }
}
