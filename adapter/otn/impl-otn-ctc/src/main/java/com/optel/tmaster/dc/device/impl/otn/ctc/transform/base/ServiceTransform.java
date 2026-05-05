/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform.base;


import cn.hutool.core.collection.CollUtil;
import com.optel.tmaster.dc.device.impl.base.transform.ITransform;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.AccessAction;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.Capacity;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.VlanSpec;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.VlanType;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.capacity.ForOduOrEth;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.capacity._for.odu.or.eth.ForEthOrEos;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.capacity._for.odu.or.eth.ForEthOrEosBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.capacity._for.odu.or.eth.ForOduOrSdh;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.capacity._for.odu.or.eth.ForOduOrSdhBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.opt.create.service.rev220309.ServiceDirection;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.capacity.CirOrTotalsize;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.capacity.cir.or.totalsize.ForCir;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.capacity.cir.or.totalsize.ForCirBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.capacity.cir.or.totalsize.ForTotalsize;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.capacity.cir.or.totalsize.ForTotalsizeBuilder;
import org.opendaylight.yangtools.yang.common.Uint64;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
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
public interface ServiceTransform extends ITransform,EnumTransform{

    /**
     * VLAN信息转换 将 dev VLAN转换为api VLAN
     * @param vlanSpec dev VLAN信息
     * @return api VLAN 信息
     */
    default VlanSpec devVlanSpecToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.VlanSpec vlanSpec){
        if(vlanSpec == null){
            return null;
        }
        return new VlanSpec() {
            @Override
            public Class<? extends VlanSpec> implementedInterface() {
                return null;
            }

            @Override
            public @org.eclipse.jdt.annotation.Nullable Set<Uint64> getVlanId() {
                if(StrUtil.isNotEmpty(vlanSpec.getVlanId())){
                    Set<Uint64> vlanIdList = new HashSet<>();
                    for(String vlan:vlanSpec.getVlanId().split(",")){
                        vlanIdList.add(Uint64.valueOf(vlan));
                    }
                    return vlanIdList;
                }
                return null;
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
        };
    }

    /**
     * VLAN信息转换 将 api VLAN转换为dev VLAN
     * @param vlanSpec api VLAN信息
     * @return dev VLAN 信息
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.VlanSpec apiVlanSpecToDev(VlanSpec vlanSpec){
        if(vlanSpec == null){
            return null;
        }
        return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.VlanSpec() {
            @Override
            public Class<? extends org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.VlanSpec> implementedInterface() {
                return null;
            }

            @Override
            public String getVlanId() {
                if(CollUtil.isNotEmpty(vlanSpec.getVlanId())){
                    List<String> vlanIdListString = new ArrayList<>();
                    for (Uint64 vlan:vlanSpec.getVlanId()){
                        vlanIdListString.add(vlan.toString());
                    }
                    return String.join(",",vlanIdListString);
                }
                return null;
            }

            @Override
            public Uint64 getVlanPriority() {
                return vlanSpec.getVlanPriority();
            }

            @Override
            public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.AccessAction getAccessAction() {
                if(vlanSpec.getAccessAction() != null){
                    return apiAccessActionToDev(vlanSpec.getAccessAction());
                }
                return null;
            }

            @Override
            public org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.VlanType getVlanType() {
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
    default Capacity devCapacityToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.Capacity capacity){
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
                CirOrTotalsize forOduOrEth = capacity.getCirOrTotalsize();
                if(forOduOrEth == null){
                    return null;
                }
                if(forOduOrEth instanceof ForCir){
                    ForEthOrEosBuilder forEthOrEosBuilder = new ForEthOrEosBuilder();
                    forEthOrEosBuilder.setCbs(((ForCir) forOduOrEth).getCbs());
                    forEthOrEosBuilder.setCir(((ForCir) forOduOrEth).getCir());
                    forEthOrEosBuilder.setPbs(((ForCir) forOduOrEth).getPbs());
                    forEthOrEosBuilder.setPir(((ForCir) forOduOrEth).getPir());
                    return forEthOrEosBuilder.build();
                }
                if(forOduOrEth instanceof ForTotalsize){
                    ForOduOrSdhBuilder forOduOrSdhBuilder = new ForOduOrSdhBuilder();
                    forOduOrSdhBuilder.setTotalSize(((ForTotalsize) forOduOrEth).getTotalSize());
                    return forOduOrSdhBuilder.build();
                }
                return null;
            }
        };
    }

    /**
     * 业务带宽 api转dev
     * @param capacity 业务带宽 api
     * @return 业务带宽 dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.Capacity apiCapacityToDev(Capacity capacity){
        if(capacity == null){
            return null;
        }
        return new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.Capacity() {

            @Override
            public Class<? extends org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.Capacity> implementedInterface() {
                return null;
            }

            @Override
            public CirOrTotalsize getCirOrTotalsize() {
                ForOduOrEth forOduOrEth = capacity.getForOduOrEth();
                if(forOduOrEth == null){
                    return null;
                }
                if(forOduOrEth instanceof ForEthOrEos){
                    ForCirBuilder forCirBuilder = new ForCirBuilder();
                    forCirBuilder.setCbs(((ForEthOrEos) forOduOrEth).getCbs());
                    forCirBuilder.setCir(((ForEthOrEos) forOduOrEth).getCir());
                    forCirBuilder.setPbs(((ForEthOrEos) forOduOrEth).getPbs());
                    forCirBuilder.setPir(((ForEthOrEos) forOduOrEth).getPir());
                    return forCirBuilder.build();
                }
                if(forOduOrEth instanceof ForOduOrSdh){
                    ForTotalsizeBuilder forTotalsizeBuilder = new ForTotalsizeBuilder();
                    forTotalsizeBuilder.setTotalSize(((ForOduOrSdh) forOduOrEth).getTotalSize());
                    return forTotalsizeBuilder.build();
                }
                throw getNoMatchEnumValueException(forOduOrEth);
            }
        };
    }
    /**
    * 业务方向 api to dev
     * @param direction api
     * @return dev
    * */
    default ServiceDirection apiToServiceDirectionDev(org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.otn.create.service.rev210927.ServiceDirection direction){
        if(direction==null){
            return null;
        }
        switch (direction){
            case BiDirection:
                return ServiceDirection.BiDirection;
            case UniDirection:
                return ServiceDirection.UniDirection;
            default:
                throw getNoMatchEnumValueException(direction);

        }

    }
}
