/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform;

import cn.hutool.core.collection.CollectionUtil;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.AbstractCtcTransformer;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.ServiceTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.ModifyEthCtpInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.connection.rev210927.Capacity;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ctp.extension.grouping.CtpExtension;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ctp.extension.grouping.CtpExtensionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ctp.extension.grouping.CtpExtensionKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.ctp.extension.parameter.grouping.OduCtpExtensionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.ctp.extension.parameter.grouping.EgressCapacityBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.ctp.extension.parameter.grouping.IngressCapacityBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.ctp.extension.parameter.grouping.StormSuppressionPac;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.eth.ctp.extension.parameter.grouping.StormSuppressionPacBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ctp.extension.parameter.grouping.EthCtpExtension;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ctp.extension.parameter.grouping.EthCtpExtensionBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ctp.extension.parameter.grouping.OduCtpExtension;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * ClassName: CtpExtensionTransformImpl
 * <ul>
 * <li>CtpExtension转换</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2021/10/12 11:25
 */
public class CtpExtensionTransformImpl extends AbstractCtcTransformer implements CommonTransform, ServiceTransform {

    /**
     * dev to api List
     * @param ctpExtensions dev List
     * @return api List
     */
    public List<CtpExtension> devCtpExtensionToApiList(Collection<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ctp.extension.grouping.CtpExtension> ctpExtensions){
        if(CollectionUtil.isEmpty(ctpExtensions)){
            return null;
        }
        List<CtpExtension> res = new ArrayList<>();
        for(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ctp.extension.grouping.CtpExtension ctpExtension :ctpExtensions){
            res.add(devCtpExtensionToApi(ctpExtension).build());
        }
        return res;
    }

    /**
     * dev to api
     * @param ctpExtension dev
     * @return api
     */
    public CtpExtensionBuilder devCtpExtensionToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ctp.extension.grouping.CtpExtension ctpExtension){
        if(ctpExtension == null){
            return null;
        }
        CtpExtensionBuilder ctpExtensionBuilder = new CtpExtensionBuilder();
        ctpExtensionBuilder.withKey(new CtpExtensionKey(ctpExtension.getName()));
        ctpExtensionBuilder.setName(ctpExtension.getName());
        //odu ctp ext
        OduCtpExtension o = ctpExtension.getOduCtpExtension();
        if(o!=null){
            OduCtpExtensionBuilder oduCtpExtensionBuilder = new OduCtpExtensionBuilder();
            oduCtpExtensionBuilder.setPmTimAction(o.getPmTimAction());
            oduCtpExtensionBuilder.setPmTimMode(new CtpTransformImpl().devTimModeToApi(o.getPmTimMode()));
            ctpExtensionBuilder.setOduCtpExtension(oduCtpExtensionBuilder.build());
        }
        //eth ctp ext
        EthCtpExtension ethCtpExtension = ctpExtension.getEthCtpExtension();
        if(ctpExtension.getEthCtpExtension() != null){
            org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension
                    .rev210927.ctp.extension.parameter.grouping.EthCtpExtensionBuilder ethCtpExtensionBuilder
                    = new org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension
                    .rev210927.ctp.extension.parameter.grouping.EthCtpExtensionBuilder();
            ethCtpExtensionBuilder.setPartitionId(ethCtpExtension.getPartitionId());
            Capacity egressCapacity = devCapacityToApi(ethCtpExtension.getEgressCapacity());
            if(egressCapacity!=null){
                ethCtpExtensionBuilder.setEgressCapacity(new EgressCapacityBuilder(egressCapacity).build());
            }
            Capacity ingressCapacity = devCapacityToApi(ethCtpExtension.getIngressCapacity());
            if(ingressCapacity!=null){
                ethCtpExtensionBuilder.setIngressCapacity(new IngressCapacityBuilder(ingressCapacity).build());
            }
            ethCtpExtensionBuilder.setStormSuppressionPac(devStormSuppressionToApi(ethCtpExtension.getStormSuppressionPac()));
            ctpExtensionBuilder.setEthCtpExtension(ethCtpExtensionBuilder.build());
        }
        return ctpExtensionBuilder;
    }

    public StormSuppressionPac
            devStormSuppressionToApi(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.ctp.extension.parameter.grouping.StormSuppressionPac stormSuppressionPac){
        if(stormSuppressionPac==null){
            return null;
        }
        StormSuppressionPacBuilder builder
                = new StormSuppressionPacBuilder();
        builder.setBroadcastSuppress(stormSuppressionPac.getBroadcastSuppress());
        builder.setUnknownMulticastSuppress(stormSuppressionPac.getUnknownMulticastSuppress());
        builder.setUnknownUnicastSuppress(stormSuppressionPac.getUnknownUnicastSuppress());
        return builder.build();
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ctp.extension.grouping.CtpExtension
            apiCtpExtensionToDev(ModifyEthCtpInput input){
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ctp.extension.grouping.CtpExtensionBuilder ctpExtensionBuilder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.ctp.extension.grouping.CtpExtensionBuilder();
        EthCtpExtensionBuilder ethCtpExtensionBuilder = new EthCtpExtensionBuilder();
        if(input.getPartitionId()!=null){
            //修改以太网专网汇聚业务端口水平分割组ID
            ethCtpExtensionBuilder.setPartitionId(input.getPartitionId());
        }
        if(input.getEgressCapacity()!=null && input.getIngressCapacity()!=null){
            //修改以太网专网汇聚业务端口限速容量接口
            if(input.getEgressCapacity()!=null){
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.Capacity egressCapacity
                        = apiCapacityToDev(input.getEgressCapacity());
                if(egressCapacity!=null){
                    ethCtpExtensionBuilder.setEgressCapacity(
                            new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329
                                    .eth.ctp.extension.parameter.grouping.EgressCapacityBuilder(egressCapacity).build());
                }
            }
            if(input.getIngressCapacity()!=null){
                org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.Capacity ingressCapacity
                        = apiCapacityToDev(input.getIngressCapacity());
                if(ingressCapacity!=null){
                    ethCtpExtensionBuilder.setIngressCapacity(
                            new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329
                                    .eth.ctp.extension.parameter.grouping.IngressCapacityBuilder(ingressCapacity).build());
                }
            }
        }
        if (input.getStormSuppressionPac()!=null) {
            //修改风暴压抑制门限
            org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329
                    .eth.ctp.extension.parameter.grouping.StormSuppressionPac stormSuppressionPac
                    = apiStormSuppressionToDev(input.getStormSuppressionPac());
            if(stormSuppressionPac!=null){
                ethCtpExtensionBuilder.setStormSuppressionPac(stormSuppressionPac);
            }
        }
        ctpExtensionBuilder
                .withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329
                        .ctp.extension.grouping.CtpExtensionKey(input.getCtpName()))
                .setName(input.getCtpName())
                .setEthCtpExtension(ethCtpExtensionBuilder.build());

        return ctpExtensionBuilder.build();
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.ctp.extension.parameter.grouping.StormSuppressionPac
            apiStormSuppressionToDev(org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422
                                             .modify.eth.ctp.input.StormSuppressionPac stormSuppressionPac){
        if(stormSuppressionPac==null){
            return null;
        }
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329
                    .eth.ctp.extension.parameter.grouping.StormSuppressionPacBuilder builder
                = new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329
                    .eth.ctp.extension.parameter.grouping.StormSuppressionPacBuilder();
        builder.setBroadcastSuppress(stormSuppressionPac.getBroadcastSuppress());
        builder.setUnknownMulticastSuppress(stormSuppressionPac.getUnknownMulticastSuppress());
        builder.setUnknownUnicastSuppress(stormSuppressionPac.getUnknownUnicastSuppress());
        return builder.build();
    }


}
