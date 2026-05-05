/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform.service;

import cn.hutool.core.collection.CollUtil;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.AbstractCmccTransformer;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.ServiceTransform;
import org.eclipse.jdt.annotation.Nullable;
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
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.ConnectionDirection;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.create.connection.EgressCapacity;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.create.connection.RequestedCapacity;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.AdaptationType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.ClientSignalType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.types.rev200610.SwitchType;
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
public class ConnectionTransformImpl extends AbstractCmccTransformer implements CommonTransform, ServiceTransform {

    /**
     * 将DEV设备的连接信息转换为APi输出连接信息
     *
     * @param connection CMCC Connection数据
     * @return api connection 数据
     */
    public org.opendaylight.yang.gen.v1.com.optel.devconf.opt.connection.rev200427.query.connection.output.ConnectionOutput devConnectionOutToApi(
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.connections.Connection connection) {
        Connection apiConnection = devConnectionToApi(connection);
        if (apiConnection == null) {
            return null;
        }
        ConnectionOutputBuilder connectionOutputBuilder = new ConnectionOutputBuilder(apiConnection);
        connectionOutputBuilder.withKey(new ConnectionOutputKey(connection.key().getName()));
        return connectionOutputBuilder.build();
    }

    /**
     * 将设备连接 connection转换为 api连接 connection
     *
     * @param connection 设备 connection
     * @return api connection
     */
    public Connection devConnectionToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.Connection connection) {
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
            public @Nullable Set<String> getCtp() {
                return connection.getCtp();
            }

            @Override
            public ServiceType getServiceType() {
                return devServiceTypeToApi(connection.getServiceType());
            }

            @Override
            public @Nullable Set<Uint16> getPgId() {
                return connection.getPgId();
            }

            @Override
            public @Nullable Set<String> getACtp() {
                if (connection.getACtp() == null) {
                    return null;
                }
                return CollUtil.newHashSet(connection.getACtp());
            }

            @Override
            public @Nullable Set<String> getZCtp() {
                if (connection.getZCtp() == null) {
                    return null;
                }
                return CollUtil.newHashSet(connection.getZCtp());
            }

            @Override
            public Uint16 getGroupId() {
                return connection.getGroupId();
            }
        };
    }


    /**
     * 创建连接 基础公共对象转换  api转为dev (grouping create-connection)
     *
     * @param createConnection api
     * @return dev
     */
    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.CreateConnection apiCreateConnectionToDev(CreateConnection createConnection) {
        if (createConnection == null) {
            return null;
        }
        return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.CreateConnection() {
            @Override
            public Class<? extends org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.CreateConnection> implementedInterface() {
                return null;
            }

            @Override
            public String getLabel() {
                return createConnection.getLabel();
            }

            @Override
            public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.ServiceType getServiceType() {
                return apiServiceTypeToDev(createConnection.getServiceType());
            }

            @Override
            public LayerProtocolName getLayerProtocolName() {
                return apiLayerNameToDev(createConnection.getLayerProtocolName());
            }

            @Override
            public RequestedCapacity getRequestedCapacity() {
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.Capacity requestedCapacity = apiCapacityToDev(createConnection.getRequestedCapacity());
                if (requestedCapacity != null) {
                    return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.create.connection.RequestedCapacityBuilder(requestedCapacity).build();
                }
                return null;
            }

            @Override
            public EgressCapacity getEgressCapacity() {
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.Capacity egressCapacity = apiCapacityToDev(createConnection.getEgressCapacity());
                if (egressCapacity != null) {
                    return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.create.connection.EgressCapacityBuilder(egressCapacity).build();
                }
                return null;
            }

            @Override
            public ConnectionDirection getDirection() {
                return apiConnectionDirectionToDev(createConnection.getDirection());
            }
        };
    }

    public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Nni apiNniToDev(Nni nni) {
        if (nni == null) {
            return null;
        }
        return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Nni() {
            @Override
            public Class<? extends org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev200708.Nni> implementedInterface() {
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
            public ClientSignalType getOduSignalType() {
                return apiClientSignalTypeToDev(nni.getOduSignalType());
            }

            @Override
            public SwitchType getSwitchCapability() {
                return apiSwitchTypeToDev(nni.getSwitchCapability());
            }

        };
    }


}
