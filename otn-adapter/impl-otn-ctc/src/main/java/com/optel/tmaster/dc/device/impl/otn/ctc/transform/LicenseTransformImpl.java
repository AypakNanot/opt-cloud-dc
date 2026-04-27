/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform;

import cn.hutool.core.collection.CollUtil;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.AbstractCtcTransformer;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.CommonTransform;
import com.optel.tmaster.dc.device.impl.otn.ctc.transform.base.EnumTransform;
import org.apache.commons.lang3.StrUtil;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetLicenseInfoOutput;
import org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.GetLicenseInfoOutputBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.get.license.info.output.licenses.License;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.get.license.info.output.licenses.LicenseKey;

import java.util.HashMap;
import java.util.Map;

/**
 * @author dengzhiming
 * @since 2024/2/20
 */
public class LicenseTransformImpl extends AbstractCtcTransformer implements CommonTransform, EnumTransform {

    public GetLicenseInfoOutput devToGetLicenseInfoOutput(org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension
                                                                  .rev210329.GetLicenseInfoOutput devOutput) {
        if (devOutput == null || devOutput.getLicenses() == null || CollUtil.isEmpty(devOutput.getLicenses().getLicense())) {
            return null;
        }
        Map<LicenseKey, License> devLicenseMap = devOutput.getLicenses().getLicense();
        Map<org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.get.license.info.output.LicenseKey, org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.get.license.info.output.License> map = new HashMap<>();
        var outputBuilder = new GetLicenseInfoOutputBuilder();
        devLicenseMap.values().forEach(item -> {
            if (item != null && StrUtil.isNotEmpty(item.getLicenseName())) {
                var key = new org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.get.license.info.output.LicenseKey(item.getLicenseName(), item.getSerialNumber());
                var licenseBuilder = new org.opendaylight.yang.gen.v1.com.optel.devconf.opt.otn.ext.dev.rev210422.get.license.info.output.LicenseBuilder();
                licenseBuilder.setCreateDate(item.getCreateDate());
                licenseBuilder.setCustomer(item.getCustomer());
                licenseBuilder.setExpirationDate(item.getExpirationDate());
                licenseBuilder.setFeatureSet(item.getFeatureSet());
                licenseBuilder.setLicenseName(item.getLicenseName());
                licenseBuilder.setPartNumber(item.getPartNumber());
                licenseBuilder.setSerialNumber(item.getSerialNumber());
                licenseBuilder.setVendor(item.getVendor());
                map.put(key, licenseBuilder.build());
            }
        });
        outputBuilder.setLicense(map);
        return outputBuilder.build();
    }

}
