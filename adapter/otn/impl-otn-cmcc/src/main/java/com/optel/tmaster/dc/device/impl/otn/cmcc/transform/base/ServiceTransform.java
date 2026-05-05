/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base;


import cn.hutool.core.collection.CollUtil;
import com.optel.tmaster.dc.device.impl.base.transform.ITransform;
import org.eclipse.jdt.annotation.Nullable;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.AccessAction;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.Capacity;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.VlanSpec;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.VlanType;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.capacity.ForOduOrEth;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.capacity._for.odu.or.eth.ForEthOrEos;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.capacity._for.odu.or.eth.ForEthOrEosBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.capacity._for.odu.or.eth.ForOduOrSdh;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.capacity._for.odu.or.eth.ForOduOrSdhBuilder;
import org.opendaylight.yangtools.yang.common.Uint64;

import java.util.Set;

/**
 * VlanSpecTransform
 * VLAN 转换
 * date       time        author
 * ─────────────────────────────
 * 2021/10/7   11:06      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public interface ServiceTransform extends ITransform,EnumTransform {

    /**
     * VLAN信息转换 将 dev VLAN转换为api VLAN
     * @param vlanSpec dev VLAN信息
     * @return api VLAN 信息
     */
    default VlanSpec devVlanSpecToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.VlanSpec vlanSpec){
        if(vlanSpec == null){
            return null;
        }
        return new VlanSpec() {
            @Override
            public @Nullable Set<Uint64> getVlanId() {
                return vlanSpec.getVlanId();
            }

            @Override
            public Uint64 getVlanPriority() {
                return vlanSpec.getVlanPriority();
            }

            @Override
            public AccessAction getAccessAction() {
                if(vlanSpec.getAccessAction() != null){
                    return devAccessactionToApi(vlanSpec.getAccessAction());
                }
                return null;
            }

            @Override
            public VlanType getVlanType() {
                if(vlanSpec.getVlanType() != null){
                    return devVlanTypeToApi(vlanSpec.getVlanType());
                }
                return null;
            }
            @Override
            public Class<? extends VlanSpec> implementedInterface() {
                return null;
            }
        };
    }


    /**
     * VLAN信息转换 将 api VLAN转换为dev VLAN
     * @param vlanSpec api VLAN信息
     * @return dev VLAN 信息
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.VlanSpec apiVlanSpecToDev(VlanSpec vlanSpec){
        if(vlanSpec == null){
            return null;
        }
        return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.VlanSpec() {
            @Override
            public Class<? extends org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.VlanSpec> implementedInterface() {
                return null;
            }

            @Override
            public @Nullable Set<Uint64> getVlanId() {
                if(CollUtil.isNotEmpty(vlanSpec.getVlanId())){
                   return vlanSpec.getVlanId();
                }
                return null;
            }

            @Override
            public Uint64 getVlanPriority() {
                return vlanSpec.getVlanPriority();
            }

            @Override
            public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.AccessAction getAccessAction() {
                if(vlanSpec.getAccessAction() != null){
                    return apiAccessActionToDev(vlanSpec.getAccessAction());
                }
                return null;
            }

            @Override
            public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.VlanType getVlanType() {
                if(vlanSpec.getVlanType() != null){
                    return apiVlanTypeToDev(vlanSpec.getVlanType());
                }
                return null;
            }
        };
    }


    /**
     * 带宽信息转换  将dev带宽信息转换为api带宽信息
     * @param capacity 带宽信息 CMCC DEV
     * @return api带宽信息
     */
    default Capacity devCapacityToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.Capacity capacity){
        if(capacity == null){
            return null;
        }
        return new Capacity() {
            @Override
            public Class<? extends Capacity> implementedInterface() {
                return null;
            }

            @Override
            public ForOduOrEth getForOduOrEth() {
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.capacity.ForOduOrEth forOduOrEth = capacity.getForOduOrEth();
                if(forOduOrEth == null){
                    return null;
                }
                if(forOduOrEth instanceof org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.capacity._for.odu.or.eth.ForEthOrEos){
                    ForEthOrEosBuilder forEthOrEosBuilder = new ForEthOrEosBuilder();
                    forEthOrEosBuilder.setCbs(((org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.capacity._for.odu.or.eth.ForEthOrEos) forOduOrEth).getCbs());
                    forEthOrEosBuilder.setCir(((org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.capacity._for.odu.or.eth.ForEthOrEos) forOduOrEth).getCir());
                    forEthOrEosBuilder.setPbs(((org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.capacity._for.odu.or.eth.ForEthOrEos) forOduOrEth).getPbs());
                    forEthOrEosBuilder.setPir(((org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.capacity._for.odu.or.eth.ForEthOrEos) forOduOrEth).getPir());
                    return forEthOrEosBuilder.build();
                }
                if(forOduOrEth instanceof org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.capacity._for.odu.or.eth.ForOduOrSdh){
                    ForOduOrSdhBuilder forOduOrSdhBuilder = new ForOduOrSdhBuilder();
                    forOduOrSdhBuilder.setTotalSize(((org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.capacity._for.odu.or.eth.ForOduOrSdh) forOduOrEth).getTotalSize());
                    return forOduOrSdhBuilder.build();
                }
                throw getNoMatchEnumValueException(forOduOrEth);
            }
        };
    }

    /**
     * 业务带宽 api转dev
     * @param capacity 业务带宽 api
     * @return 业务带宽 dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.Capacity apiCapacityToDev(Capacity capacity){
        if(capacity == null){
            return null;
        }
        return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.Capacity() {
            @Override
            public Class<? extends org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.Capacity> implementedInterface() {
                return null;
            }

            @Override
            public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.capacity.ForOduOrEth getForOduOrEth() {
                ForOduOrEth forOduOrEth = capacity.getForOduOrEth();
                if(forOduOrEth == null){
                    return null;
                }
                if(forOduOrEth instanceof ForEthOrEos){
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.capacity._for.odu.or.eth.ForEthOrEosBuilder forEthOrEosBuilder =
                            new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.capacity._for.odu.or.eth.ForEthOrEosBuilder();
                    forEthOrEosBuilder.setCbs(((ForEthOrEos) forOduOrEth).getCbs());
                    forEthOrEosBuilder.setCir(((ForEthOrEos) forOduOrEth).getCir());
                    forEthOrEosBuilder.setPbs(((ForEthOrEos) forOduOrEth).getPbs());
                    forEthOrEosBuilder.setPir(((ForEthOrEos) forOduOrEth).getPir());
                    return forEthOrEosBuilder.build();
                }
                if(forOduOrEth instanceof ForOduOrSdh){
                    org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.capacity._for.odu.or.eth.ForOduOrSdhBuilder forOduOrSdhBuilder = new
                            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev200209.capacity._for.odu.or.eth.ForOduOrSdhBuilder();
                    forOduOrSdhBuilder.setTotalSize(((ForOduOrSdh) forOduOrEth).getTotalSize());
                    return forOduOrSdhBuilder.build();
                }
                throw getNoMatchEnumValueException(forOduOrEth);
            }
        };
    }

}
