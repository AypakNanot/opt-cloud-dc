/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base;

import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.StatInterval;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.optel.oc.types.rev220208.Timeticks64;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.types.yang.rev220208.Counter64;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime;

/**
 * CommonTypeTransform
 * 公共类型转换
 * date       time        author
 * ─────────────────────────────
 * 2022/4/22   13:51      liwenxue
 * Copyright (c) 2022, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public interface CommonTypeTransform {

    /**
     * Counter64 类型的数据转换 dev类型转为api
     * @param counter64 dev数据
     * @return api数据
     */
    default Counter64 devCounter64ToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.types.yang.rev181121.Counter64 counter64){
        if(counter64 == null){
            return null;
        }
        return new Counter64(counter64.getValue());
    }

    /**
     * Timeticks64 类型数据 dev转api
     * @param value dev值
     * @return api
     */
    default Timeticks64 devTimeticks64ToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.Timeticks64 value){
        if(value == null){
            return null;
        }
        return new Timeticks64(value.getValue());
    }

    /**
     * StatInterval 类型数据 dev转api
     * @param value dev值
     * @return api
     */
    default StatInterval devStatIntervalToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.StatInterval value){
        if(value == null){
            return null;
        }
        return new StatInterval(value.getValue());
    }

    default DateAndTime stringToDateTime(String date){
        if(date == null || "".equals(date)){
            return null;
        }
        if(date.contains("Z") && date.contains("+")){
            date = date.substring(0,date.indexOf("Z")+1);
        }
        return new DateAndTime(date);
    }

}
