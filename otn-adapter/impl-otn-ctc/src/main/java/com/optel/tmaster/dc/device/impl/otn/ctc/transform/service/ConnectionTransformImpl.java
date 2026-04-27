/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform.service;

import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.AbstractCtcTransformer;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.ServiceTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.query.connection.output.ConnectionOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.query.connection.output.ConnectionOutputKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.Capacity;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.Connection;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.CreateConnection;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.connection.EgressCapacityBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.connection.RequestedCapacityBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.connection.StatePacBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.ServiceType;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.devm.rev210927.StatePac;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.rev210927.Nni;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.OduSwitchType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.SignalType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.ConnectionDirection;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.create.connection.EgressCapacity;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.create.connection.RequestedCapacity;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.LayerProtocolName;
import org.opendaylight.yangtools.yang.common.Uint16;

import java.util.Set;


/**
 * 连接
 * date       time        author
 * ─────────────────────────────
 * 2021/10/7   10:47      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public class ConnectionTransformImpl extends AbstractCtcTransformer implements CommonTransform, ServiceTransform {

    /**
     * 将DEV设备的连接信息转换为APi输出连接信息
     * @param connection ctc Connection数据
     * @return api connection 数据
     */
    public org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.query.connection.output.ConnectionOutput devConnectionOutToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connections.Connection connection) {
        Connection apiConnection = devConnectionToApi(connection);
        if(apiConnection == null){
            return null;
        }
        ConnectionOutputBuilder connectionOutputBuilder = new ConnectionOutputBuilder(apiConnection);
        connectionOutputBuilder.withKey(new ConnectionOutputKey(connection.key().getName()));
        return connectionOutputBuilder.build();
    }

    /**
     * 将设备连接 connection转换为 api连接 connection
     * @param connection 设备 connection
     * @return api connection
     */
    public Connection devConnectionToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.Connection connection){
        if (connection == null) {
            return null;
        }
        return new Connection() {
            @Override
            public Class<? extends Connection> implementedInterface() {
                return null;
            }

            @Override
            public String getName() {
                return connection.getName();
            }

            @Override
            public String getLabel() {
                return connection.getLabel();
            }

            @Override
            public org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.connection.StatePac getStatePac() {
                StatePac statePac = devStatePacToApi(connection.getStatePac());
                if (statePac != null) {
                    return new StatePacBuilder(statePac).build();
                }
                return null;
            }

            @Override
            public org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.yang.types.rev210927.LayerProtocolName getLayerProtocolName() {
                return devLayerNameToApi(connection.getLayerProtocolName());
            }

            @Override
            public org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.connection.RequestedCapacity getRequestedCapacity() {
                Capacity requestCapacity = devCapacityToApi(connection.getRequestedCapacity());
                if (requestCapacity != null) {
                    return new RequestedCapacityBuilder(requestCapacity).build();
                }
                return null;
            }

            @Override
            public org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.connection.EgressCapacity getEgressCapacity() {
                Capacity egressCapacity = devCapacityToApi(connection.getEgressCapacity());
                if (egressCapacity != null) {
                    return new EgressCapacityBuilder(egressCapacity).build();
                }
                return null;
            }

            @Override
            public Set<String> getCtp() {
                return connection.getCtp();
            }

            @Override
            public ServiceType getServiceType() {
                return devServiceTypeToApi(connection.getServiceType());
            }

            @Override
            public @org.eclipse.jdt.annotation.Nullable Set<Uint16> getPgId() {
                return connection.getPgId();
            }

            @Override
            public Set<String> getACtp() {
                return connection.getACtp();
            }

            @Override
            public Set<String> getZCtp() {
                return connection.getZCtp();
            }

            @Override
            public Uint16 getGroupId() {
                return connection.getGroupId();
            }
        };
    }


    /**
     * 创建连接 基础公共对象转换  api转为dev (grouping create-connection)
     * @param createConnection  api
     * @return dev
     */
    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.CreateConnection apiCreateConnectionToDev(CreateConnection createConnection){
        if(createConnection == null){
            return null;
        }
        return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.CreateConnection() {
            @Override
            public Class<? extends org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.CreateConnection> implementedInterface() {
                return null;
            }

            @Override
            public String getLabel() {
                return createConnection.getLabel();
            }

            @Override
            public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ServiceType getServiceType() {
                return apiServiceTypeToDev(createConnection.getServiceType());
            }

            @Override
            public LayerProtocolName getLayerProtocolName() {
                return apiLayerNameToDev(createConnection.getLayerProtocolName());
            }

            @Override
            public RequestedCapacity getRequestedCapacity() {
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.Capacity requestedCapacity = apiCapacityToDev(createConnection.getRequestedCapacity());
                if(requestedCapacity != null){
                    return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.create.connection.RequestedCapacityBuilder(requestedCapacity).build();
                }
                return null;
            }

            @Override
            public EgressCapacity getEgressCapacity() {
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.Capacity egressCapacity = apiCapacityToDev(createConnection.getEgressCapacity());
                if(egressCapacity != null){
                    return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.create.connection.EgressCapacityBuilder(egressCapacity).build();
                }
                return null;
            }

            @Override
            public ConnectionDirection getDirection() {
                return apiConnectionDirectionToDev(createConnection.getDirection());
            }
        };
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Nni apiNniToDev(Nni nni){
        if(nni == null){
            return null;
        }
        return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Nni() {
            @Override
            public Class<? extends org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Nni> implementedInterface() {
                return null;
            }

            @Override
            public String getNniPtpName() {
                return nni.getNniPtpName();
            }

            @Override
            public String getNniTsDetail() {
                return nni.getNniTsDetail();
            }

            @Override
            public AdaptationType getAdaptationType() {
                return apiAdaptationTypeToDev(nni.getAdaptationType());
            }

            @Override
            public SignalType getOduSignalType() {
                return apiClientSignalTypeToDev(nni.getOduSignalType());
            }

            @Override
            public OduSwitchType getSwitchCapability() {
                return apiSwitchTypeToDev(nni.getSwitchCapability());
            }

        };
    }


}
