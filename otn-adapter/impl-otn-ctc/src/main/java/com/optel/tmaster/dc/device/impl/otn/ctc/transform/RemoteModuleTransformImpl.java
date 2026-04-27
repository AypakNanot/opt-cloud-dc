/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform;

import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.EnumTransform;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.AbstractCtcTransformer;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.remote.module.grouping.RemoteModule;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.remote.module.grouping.RemoteModuleBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.remote.module.grouping.RemoteModuleKey;

import java.util.ArrayList;
import java.util.List;

/**
 * RemoteModuleTransformImpl
 * 远端模块
 * date       time        author
 * ─────────────────────────────
 * 2021/10/15   10:17      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class RemoteModuleTransformImpl extends AbstractCtcTransformer implements EnumTransform {

    public List<RemoteModule> devRemoteModuleToApiList(
            List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.remote.module.grouping.RemoteModule> remoteModules){
        if(remoteModules == null){
            return null;
        }
        List<RemoteModule> result = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.remote.module.grouping.RemoteModule remoteModule:remoteModules){
            result.add(devRemoteModuleToApi(remoteModule));
        }
        return result;
    }

    public RemoteModule devRemoteModuleToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.remote.module.grouping.RemoteModule remoteModule){
        if(remoteModule == null){
            return null;
        }
        RemoteModuleBuilder remoteModuleBuilder = new RemoteModuleBuilder();
        remoteModuleBuilder.withKey(new RemoteModuleKey(remoteModule.key().getName()));
        remoteModuleBuilder.setName(remoteModule.getName());
        remoteModuleBuilder.setRemoteModuleMac(remoteModule.getRemoteModuleMac());
        remoteModuleBuilder.setRemoteModuleName(remoteModule.getRemoteModuleName());
        remoteModuleBuilder.setRemoteModuleUuid(remoteModule.getRemoteModuleUuid());
        remoteModuleBuilder.setRemotePtp(remoteModule.getRemotePtp());
        remoteModuleBuilder.setRemoteState(devRemoteStateToApi(remoteModule.getRemoteState()));
        remoteModuleBuilder.setVlanId(remoteModule.getVlanId());
        remoteModuleBuilder.setMonitoringEnable(remoteModule.getMonitoringEnable());
        return remoteModuleBuilder.build();
    }

}
