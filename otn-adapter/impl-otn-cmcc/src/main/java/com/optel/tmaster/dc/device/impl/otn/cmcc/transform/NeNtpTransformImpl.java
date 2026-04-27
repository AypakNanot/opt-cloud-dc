/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.cmcc.transform;

import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.AbstractCmccTransformer;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.EnumTransform;
import com.optel.tmaster.dc.device.impl.otn.cmcc.transform.base.ServiceTransform;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.AddModifyMeNtpInput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.device.rev200425.ModifyMeInput;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.MeBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.me.ntp.servers.NtpServerBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev200708.me.ntp.servers.NtpServerKey;

/**
 * @author Quan Jingyuan
 * @since 2021/10/12
 **/
public class NeNtpTransformImpl extends AbstractCmccTransformer implements CommonTransform, EnumTransform, ServiceTransform {
    public NtpServerBuilder apiNtpServerToDev(AddModifyMeNtpInput input){
        NtpServerBuilder ntpServerBuilder=new NtpServerBuilder();
        if(input==null){
            return  ntpServerBuilder;
        }
        ntpServerBuilder.withKey(new NtpServerKey(input.getName()));
        ntpServerBuilder.setName(input.getName());
        ntpServerBuilder.setIpAddress(input.getIpAddress());
        ntpServerBuilder.setPort(input.getPort());
        ntpServerBuilder.setNtpVersion(input.getNtpVersion());
        return ntpServerBuilder;
    }
    public MeBuilder apiMeToDev(ModifyMeInput input){
        MeBuilder meBuilder=new MeBuilder();
        if(input==null){
            return  null;
        }
        meBuilder.setName(input.getName());
        meBuilder.setIpAddress(input.getIpAddress());
        meBuilder.setMask(input.getMask());
        meBuilder.setNtpEnable(input.getNtpEnable());
        meBuilder.setGateWay1(input.getGateWay1());
        meBuilder.setGateWay2(input.getGateWay2());
        if(input.getStatus()!=null){
            meBuilder.setStatus(apiMeStatusToDev(input.getStatus()));
        }
        return meBuilder;
    }
}
