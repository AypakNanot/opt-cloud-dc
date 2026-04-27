/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform.service;

import com.optel.tmaster.dc.general.nc.nccore.NcTools;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.ModifyEthConnectionCapacityNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.ModifyOduConnectionCapacityNeInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.ModifyVcgConnectionCapacityNeInput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.Capacity;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connection.EgressCapacityBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connection.RequestedCapacityBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connections.Connection;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connections.ConnectionBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connections.ConnectionKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.ModifyVcgConnectionCapacityInput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.ModifyVcgConnectionCapacityInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.ModifyOduConnectionCapacityInput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.ModifyOduConnectionCapacityInputBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.modify.odu.connection.capacity.input.line.or.client.ClientBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.modify.odu.connection.capacity.input.line.or.client.LineBuilder;


/**
 * ConnectionMngTransformImpl
 * 连接管理
 * date       time        author
 * ─────────────────────────────
 * 2021/10/13   14:34      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class ConnectionMngTransformImpl extends ConnectionTransformImpl{

    public Connection apiModifyEthConnectionCapacityNeInputToDev(ModifyEthConnectionCapacityNeInput input){
        if(input == null){
            return null;
        }
        ConnectionBuilder connectionBuilder = new ConnectionBuilder();
        connectionBuilder.withKey(new ConnectionKey(input.getConName()));
        connectionBuilder.setLabel(input.getLabel());
        Capacity requestCapacity = apiCapacityToDev(input.getRequestedCapacity());
        if(requestCapacity != null){
            connectionBuilder.setRequestedCapacity(new RequestedCapacityBuilder(requestCapacity).build());
        }
        if (NcTools.isSupportOptelExt(input.getNeId())) {
            Capacity egressCapacity = apiCapacityToDev(input.getEgressCapacity());
            if(egressCapacity != null){
                connectionBuilder.setEgressCapacity(new EgressCapacityBuilder(egressCapacity).build());
            }
        }
        return connectionBuilder.build();
    }


    public ModifyOduConnectionCapacityInput apiModifyOduConnectionCapacityInputToDev(ModifyOduConnectionCapacityNeInput input){
        if(input == null){
            return null;
        }
        ModifyOduConnectionCapacityInputBuilder builder = new ModifyOduConnectionCapacityInputBuilder();
        builder.setAction(apiModifyActionToDev(input.getAction()));
        builder.setAdjust(apiOduflexAdjustToDev(input.getAdjust()));
        builder.setOduCtpName(input.getOduCtpName());
        builder.setPosition(apiOduPositionToDev(input.getPosition()));
        builder.setTimeout(input.getTimeout());
        if(input.getTsDetail() != null){
            LineBuilder lineBuilder = new LineBuilder().setTsDetail(input.getTsDetail());
            builder.setLineOrClient(lineBuilder.build());
        }
        else if(input.getCurrentNumberOfTributarySlots() != null){
            ClientBuilder clientBuilder = new ClientBuilder().setCurrentNumberOfTributarySlots(input.getCurrentNumberOfTributarySlots());
            builder.setLineOrClient(clientBuilder.build());
        }
        return builder.build();
    }

    public ModifyVcgConnectionCapacityInput apiModifyVcgConnectionCapacityInputToDev(ModifyVcgConnectionCapacityNeInput input){
        if(input == null){
            return null;
        }
        ModifyVcgConnectionCapacityInputBuilder builder = new ModifyVcgConnectionCapacityInputBuilder();
        builder.setEthFtpName(input.getEthFtpName());
        builder.setMappingPath(input.getMappingPath());
        builder.setMappingPathProtected(input.getMappingPathProtected());
        builder.setSdhFtpName(input.getSdhFtpName());
        builder.setSdhProtectFtpName(input.getSdhProtectFtpName());
        return builder.build();
    }

}
