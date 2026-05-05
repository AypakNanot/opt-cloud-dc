/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.transform.base;

import com.optel.tmaster.dc.device.impl.base.transform.ITransform;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.performance.rev210927.performances.grouping.performance.DigitalOrAnalog;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.performance.rev210927.performances.grouping.performance.digital.or.analog.Analog;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.performance.rev210927.performances.grouping.performance.digital.or.analog.AnalogBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.performance.rev210927.performances.grouping.performance.digital.or.analog.Digital;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.performance.rev210927.performances.grouping.performance.digital.or.analog.DigitalBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.performance.rev210927.performances.grouping.performance.digital.or.analog.analog.AnalogPmValue;
import org.opendaylight.yang.gen.v1.com.optel.yang.api.optel.performance.rev210927.performances.grouping.performance.digital.or.analog.analog.AnalogPmValueBuilder;

/**
 * InterfaceName: performanceTransform
 * <ul>
 * <li>性能有关功能公共转换类</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2021/10/12 15:36
 */
public interface PerformanceTransform extends ITransform,CommonTransform {
     /**
     * dev  To api
     * @param digitalOrAnalog dev
     * @return api
     */
    default DigitalOrAnalog devDigitalOrAnalogToApi(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.DigitalOrAnalog digitalOrAnalog) {
        if(digitalOrAnalog == null) {
            return null;
        }
        if(digitalOrAnalog instanceof org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.digital.or.analog.Digital){
            DigitalBuilder digitalBuilder = new DigitalBuilder();
            digitalBuilder.setDigitalPmValue(devRealToApi(((org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.digital.or.analog.Digital) digitalOrAnalog).getDigitalPmValue()));
            return digitalBuilder.build();
        }
        if(digitalOrAnalog instanceof org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.digital.or.analog.Analog){
            AnalogBuilder analogBuilder = new AnalogBuilder();
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.digital.or.analog.analog.AnalogPmValue analogPmValue =
                    ((org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.digital.or.analog.Analog)digitalOrAnalog).getAnalogPmValue();
            AnalogPmValueBuilder analogPmValueBuilder = new AnalogPmValueBuilder();
            analogPmValueBuilder.setAverageValue(devRealToApi(analogPmValue.getAverageValue()));
            analogPmValueBuilder.setCurrentValue(devRealToApi(analogPmValue.getCurrentValue()));
            analogPmValueBuilder.setMaxValue(devRealToApi(analogPmValue.getMaxValue()));
            analogPmValueBuilder.setMinValue(devRealToApi(analogPmValue.getMinValue()));
            analogBuilder.setAnalogPmValue(analogPmValueBuilder.build());
            return analogBuilder.build();
        }
        throw getNoMatchEnumValueException(digitalOrAnalog);
    }

    /**
     * api  To dev
     * @param digitalOrAnalog api
     * @return dev
     */
    default org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.DigitalOrAnalog apiDigitalOrAnalogToDev(DigitalOrAnalog digitalOrAnalog) {
        if(digitalOrAnalog == null) {
            return null;
        }
        if(digitalOrAnalog instanceof Digital){
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.digital.or.analog.DigitalBuilder digitalBuilder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.digital.or.analog.DigitalBuilder();
            digitalBuilder.setDigitalPmValue(apiRealToDev(((Digital) digitalOrAnalog).getDigitalPmValue()));
            return digitalBuilder.build();
        }
        if(digitalOrAnalog instanceof Analog){
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.digital.or.analog.AnalogBuilder analogBuilder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.digital.or.analog.AnalogBuilder();
            AnalogPmValue analogPmValue =((Analog)digitalOrAnalog).getAnalogPmValue();
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.digital.or.analog.analog.AnalogPmValueBuilder analogPmValueBuilder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.digital.or.analog.analog.AnalogPmValueBuilder();
            analogPmValueBuilder.setAverageValue(apiRealToDev(analogPmValue.getAverageValue()));
            analogPmValueBuilder.setCurrentValue(apiRealToDev(analogPmValue.getCurrentValue()));
            analogPmValueBuilder.setMaxValue(apiRealToDev(analogPmValue.getMaxValue()));
            analogPmValueBuilder.setMinValue(apiRealToDev(analogPmValue.getMinValue()));
            analogBuilder.setAnalogPmValue(analogPmValueBuilder.build());
            return analogBuilder.build();
        }
        throw getNoMatchEnumValueException(digitalOrAnalog);
    }
}
