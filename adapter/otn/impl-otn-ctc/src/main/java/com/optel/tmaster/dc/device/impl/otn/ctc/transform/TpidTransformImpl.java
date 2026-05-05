/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform;

import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.AbstractCtcTransformer;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.EnumTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.ModifyTpidsInput;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.tpid.grouping.Tpid;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.tpid.grouping.TpidBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.otn.extension.rev210927.tpid.grouping.TpidKey;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * tpid 转换
 * @author Quan Jingyuan
 * @since 2021/10/14
 **/
public class TpidTransformImpl extends AbstractCtcTransformer implements CommonTransform, EnumTransform {

    public List<Tpid> devSelectTpidsOutputToApi(Collection<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tpid.grouping.Tpid> tpids) {
        if (tpids == null) {
            return null;
        }
        List<Tpid> tpidList = new ArrayList<>();
        for (org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tpid.grouping.Tpid tpid : tpids) {
            TpidBuilder tpidBuilder = new TpidBuilder();
            tpidBuilder.withKey(new TpidKey(tpid.key().getName()));
            tpidBuilder.setName(tpid.getName());
            if (tpid.getTpidValue() != null) {
                tpidBuilder.setTpidValue(devTpidDifinitionToApi(tpid.getTpidValue()));
            }
            tpidList.add(tpidBuilder.build());
        }
        return tpidList;
    }

    public org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tpid.grouping.TpidBuilder apiTpidToDev(ModifyTpidsInput input){
        org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tpid.grouping.TpidBuilder builder=new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tpid.grouping.TpidBuilder();
        if(input==null){
            return null;
        }
        builder.withKey(new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.tpid.grouping.TpidKey(input.getName()));
        builder.setName(input.getName());
        if(input.getTpidValue()!=null){
            builder.setTpidValue(apiTpidDifinitionToDev(input.getTpidValue()));
        }
        return  builder;
    }
}
