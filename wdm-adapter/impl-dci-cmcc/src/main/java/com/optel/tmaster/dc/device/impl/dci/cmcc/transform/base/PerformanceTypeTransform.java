/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base;

import com.optel.tmaster.dc.dci.impl.base.transform.ITransform;
import com.optel.tmaster.dc.general.base.exception.manage.NoMatchEnumValueException;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.types.rev220208.PmParameterType;

/**
 * ClassName: PerformanceTypeTransform
 * <ul>
 * <li>性能基本类型转换</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2022/3/18 10:24
 */
public interface PerformanceTypeTransform extends ITransform {

    /**
     * pmPointRef转换
     *
     * @param pmPointRef dev
     * @return api
     */
    //待确定
//    default PmPointRef devPmPointRefToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmPointRef pmPointRef) {
//        if (pmPointRef == null) {
//            return null;
//        }
//        return new PmPointRef(pmPointRef.getValue());
//    }

    /**
     * pmpObjectType转换
     *
     * @param pmpObjectType dev
     * @return api
     */
    //待确定
//    default PmpObjectType devPmpObjectTypeToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType pmpObjectType) {
//        if (pmpObjectType == null) {
//            return null;
//        }
//        switch (pmpObjectType) {
//            case CHASSIS:
//                return PmpObjectType.CHASSIS;
//            case CARD:
//                return PmpObjectType.CARD;
//            case PORT:
//                return PmpObjectType.PORT;
//            case TRANSCEIVER:
//                return PmpObjectType.TRANSCEIVER;
//            case OPTICALCHANNEL:
//                return PmpObjectType.OPTICALCHANNEL;
//            case PHYSCIALCHANNEL:
//                return PmpObjectType.PHYSCIALCHANNEL;
//            case OLP:
//                return PmpObjectType.OLP;
//            case AMPLIFIER:
//                return PmpObjectType.AMPLIFIER;
//            case OTU:
//                return PmpObjectType.OTU;
//            case ODU:
//                return PmpObjectType.ODU;
//            case ETH:
//                return PmpObjectType.ETH;
//            case OSC:
//                return PmpObjectType.OSC;
//            case OCM:
//                return PmpObjectType.OCM;
//            case ALL:
//                return PmpObjectType.ALL;
//            default:
//                throw NoMatchEnumValueException.getNoMatchEnumValueException(pmpObjectType);
//        }
//    }

    /**
     * pmParameterType转换
     *
     * @param pmParameterType dev
     * @return api
     */
    default PmParameterType devPmParameterTypeToApi(org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType pmParameterType) {
        if (pmParameterType == null) {
            return null;
        }
        //待修改 完善
        switch (pmParameterType) {
            case RXPKTS:
                return PmParameterType.RXPKTS;
            case TXPKTS:
                return PmParameterType.TXPKTS;
            case RXCRCERRSUM:
                return PmParameterType.RXCRCERRSUM;
            case RXPKTS64OCTETS:
                return PmParameterType.RXPKTS64OCTETS;
            case RXPKTS65TO127OCTETS:
                return PmParameterType.RXPKTS65TO127OCTETS;
            case RXPKTS128TO255OCTETS:
                return PmParameterType.RXPKTS128TO255OCTETS;
            case RXPKTS256TO511OCTETS:
                return PmParameterType.RXPKTS256TO511OCTETS;
            case RXPKTS512TO1023OCTETS:
                return PmParameterType.RXPKTS512TO1023OCTETS;
            case RXPKTS1024TO1518OCTETS:
                return PmParameterType.RXPKTS1024TO1518OCTETS;
            case TXPKTS64OCTETS:
                return PmParameterType.TXPKTS64OCTETS;
            case TXPKTSOCTETS65TO127:
                return PmParameterType.TXPKTSOCTETS65TO127;
            case TXPKTS128TO255OCTETS:
                return PmParameterType.TXPKTS128TO255OCTETS;
            case TXPKTS256TO511OCTETS:
                return PmParameterType.TXPKTS256TO511OCTETS;
            case TXPKTS512TO1023OCTETS:
                return PmParameterType.TXPKTS512TO1023OCTETS;
            case TXPKTS1024TO1518OCTETS:
                return PmParameterType.TXPKTS1024TO1518OCTETS;
            case RXSTATOSZ:
                return PmParameterType.RXSTATOSZ;
            case TXSTATOSZ:
                return PmParameterType.TXSTATOSZ;
            case RXSTATUSZ:
                return PmParameterType.RXSTATUSZ;
            case TXSTATUSZ:
                return PmParameterType.TXSTATUSZ;
            case BBE:
                return PmParameterType.BBE;
            case BBER:
                return PmParameterType.BBER;
            case ES:
                return PmParameterType.ES;
            case SES:
                return PmParameterType.SES;
            case SESR:
                return PmParameterType.SESR;
            case UAS:
                return PmParameterType.UAS;
            case FEBBE:
                return PmParameterType.FEBBE;
            case FEBBER:
                return PmParameterType.FEBBER;
            case FEES:
                return PmParameterType.FEES;
            case FESES:
                return PmParameterType.FESES;
            case FEUAS:
                return PmParameterType.FEUAS;
            case FESESR:
                return PmParameterType.FESESR;
            case PREFECBER:
                return PmParameterType.PREFECBER;
            case POSTFECBER:
                return PmParameterType.POSTFECBER;
            case RXMESSAGENUM:
                return PmParameterType.RXMESSAGENUM;
            case TXMESSAGENUM:
                return PmParameterType.TXMESSAGENUM;
            case RXDROPMESSAGENUM:
                return PmParameterType.RXDROPMESSAGENUM;
            case INPUTPOWER:
                return PmParameterType.INPUTPOWER;
            case OUTPUTPOWER:
                return PmParameterType.OUTPUTPOWER;
            case LASERBIASCURRENT:
                return PmParameterType.LASERBIASCURRENT;
            case TEMPERATURE:
                return PmParameterType.TEMPERATURE;
            case TECCURRENT:
                return PmParameterType.TECCURRENT;
            case ACTUALGAIN:
                return PmParameterType.ACTUALGAIN;
            case ACTUALGAINTILT:
                return PmParameterType.ACTUALGAINTILT;
            case INPUTPOWERTOTAL:
                return PmParameterType.INPUTPOWERTOTAL;
            case OUTPUTPOWERTOTAL:
                return PmParameterType.OUTPUTPOWERTOTAL;
            case OPTICALRETURNLOSS:
                return PmParameterType.OPTICALRETURNLOSS;
            case ACTUALVOAATTENUATION:
                return PmParameterType.ACTUALVOAATTENUATION;
            case LOWERFREQUENCY:
                return PmParameterType.LOWERFREQUENCY;
            case UPPERFREQUENCY:
                return PmParameterType.UPPERFREQUENCY;
            case OSNR:
                return PmParameterType.OSNR;
            case CHANNELOPTICALPOWER:
                return PmParameterType.CHANNELOPTICALPOWER;
            case SUPPLYVOLTAGE:
                return PmParameterType.SUPPLYVOLTAGE;
            case RXBYTES:
                return PmParameterType.RXBYTES;
            case TXBYTES:
                return PmParameterType.TXBYTES;
            case TXCRCERRSUM:
                return PmParameterType.TXCRCERRSUM;
            case ALL:
                return PmParameterType.ALL;
            case PILOTTONESTATUS:
                return PmParameterType.PILOTTONESTATUS;
            case OUTPCSBIPERRORS:
                return PmParameterType.OUTPCSBIPERRORS;
            case INPCSBIPERRORS:
                return PmParameterType.INPCSBIPERRORS;
            case RXERRSUM:
                return PmParameterType.RXERRSUM;
            case RXPKTSERR:
                return PmParameterType.RXPKTSERR;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(pmParameterType);
        }
    }

    /**
     * pmGranularityType转换
     *
     * @param pmGranularityType dev
     * @return api
     */
    //待确定
//    default PmGranularityType devPmGranularityTypeToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmGranularityType pmGranularityType) {
//        if (pmGranularityType == null) {
//            return null;
//        }
//        switch (pmGranularityType) {
//            case _24H:
//                return PmGranularityType._24H;
//            case _15MIN:
//                return PmGranularityType._15MIN;
//            default:
//                throw NoMatchEnumValueException.getNoMatchEnumValueException(pmGranularityType);
//        }
//    }

    /**
     * pmPointRef转换
     *
     * @param pmPointRef api
     * @return dev
     */
    //待确定
//    default PmPointRef apiPmPointRefToDev(PmPointRef pmPointRef) {
//        if (pmPointRef == null) {
//            return null;
//        }
//        return new org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmPointRef(pmPointRef.getValue());
//    }

    /**
     * pmpObjectType转换
     *
     * @param pmpObjectType api
     * @return dev
     */
    //// 待确定
//    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType apiPmpObjectTypeToDev(PmpObjectType pmpObjectType) {
//        if (pmpObjectType == null) {
//            return null;
//        }
//        switch (pmpObjectType) {
//            case CHASSIS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType.CHASSIS;
//            case CARD:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType.CARD;
//            case PORT:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType.PORT;
//            case TRANSCEIVER:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType.TRANSCEIVER;
//            case OPTICALCHANNEL:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType.OPTICALCHANNEL;
//            case PHYSCIALCHANNEL:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType.PHYSCIALCHANNEL;
//            case OLP:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType.OLP;
//            case AMPLIFIER:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType.AMPLIFIER;
//            case OTU:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType.OTU;
//            case ODU:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType.ODU;
//            case ETH:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType.ETH;
//            case OSC:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType.OSC;
//            case OCM:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType.OCM;
//            case ALL:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmpObjectType.ALL;
//            default:
//                throw NoMatchEnumValueException.getNoMatchEnumValueException(pmpObjectType);
//        }
//    }

    /**
     * PmParameterType转换
     *
     * @param pmParameterType api
     * @return dev
     */
    //待确定
//    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType apiPmParameterTypeToDev(PmParameterType pmParameterType) {
//        if (pmParameterType == null) {
//            return null;
//        }
//        switch (pmParameterType) {
//            case RXPKTS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.RXPKTS;
//            case TXPKTS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.TXPKTS;
//            case RXCRCERRSUM:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.RXCRCERRSUM;
//            case RXPKTS64OCTETS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.RXPKTS64OCTETS;
//            case RXPKTS65TO127OCTETS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.RXPKTS65TO127OCTETS;
//            case RXPKTS128TO255OCTETS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.RXPKTS128TO255OCTETS;
//            case RXPKTS256TO511OCTETS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.RXPKTS256TO511OCTETS;
//            case RXPKTS512TO1023OCTETS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.RXPKTS512TO1023OCTETS;
//            case RXPKTS1024TO1518OCTETS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.RXPKTS1024TO1518OCTETS;
//            case TXPKTS64OCTETS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.TXPKTS64OCTETS;
//            case TXPKTSOCTETS65TO127:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.TXPKTSOCTETS65TO127;
//            case TXPKTS65TO127OCTETS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.TXPKTS65TO127OCTETS;
//            case TXPKTS128TO255OCTETS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.TXPKTS128TO255OCTETS;
//            case TXPKTS256TO511OCTETS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.TXPKTS256TO511OCTETS;
//            case TXPKTS512TO1023OCTETS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.TXPKTS512TO1023OCTETS;
//            case TXPKTS1024TO1518OCTETS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.TXPKTS1024TO1518OCTETS;
//            case RXSTATOSZ:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.RXSTATOSZ;
//            case TXSTATOSZ:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.TXSTATOSZ;
//            case RXSTATUSZ:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.RXSTATUSZ;
//            case TXSTATUSZ:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.TXSTATUSZ;
//            case BBE:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.BBE;
//            case BBER:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.BBER;
//            case ES:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.ES;
//            case SES:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.SES;
//            case SESR:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.SESR;
//            case UAS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.UAS;
//            case FEBBE:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.FEBBE;
//            case FEBBER:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.FEBBER;
//            case FEES:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.FEES;
//            case FESES:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.FESES;
//            case FEUAS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.FEUAS;
//            case FESESR:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.FESESR;
//            case PREFECBER:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.PREFECBER;
//            case POSTFECBER:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.POSTFECBER;
//            case RXMESSAGENUM:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.RXMESSAGENUM;
//            case TXMESSAGENUM:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.TXMESSAGENUM;
//            case RXDROPMESSAGENUM:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.RXDROPMESSAGENUM;
//            case INPUTPOWER:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.INPUTPOWER;
//            case OUTPUTPOWER:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.OUTPUTPOWER;
//            case LASERBIASCURRENT:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.LASERBIASCURRENT;
//            case TEMPERATURE:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.TEMPERATURE;
//            case TECCURRENT:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.TECCURRENT;
//            case ACTUALGAIN:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.ACTUALGAIN;
//            case ACTUALGAINTILT:
//            case ACTUALGAINTITL:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.ACTUALGAINTILT;
//            case INPUTPOWERTOTAL:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.INPUTPOWERTOTAL;
//            case OUTPUTPOWERTOTAL:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.OUTPUTPOWERTOTAL;
//            case OPTICALRETURNLOSS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.OPTICALRETURNLOSS;
//            case ACTUALVOAATTENUATION:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.ACTUALVOAATTENUATION;
//            case LOWERFREQUENCY:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.LOWERFREQUENCY;
//            case UPPERFREQUENCY:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.UPPERFREQUENCY;
//            case OSNR:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.OSNR;
//            case CHANNELOPTICALPOWER:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.CHANNELOPTICALPOWER;
//            case SUPPLYVOLTAGE:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.SUPPLYVOLTAGE;
//            case RXBYTES:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.RXBYTES;
//            case TXBYTES:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.TXBYTES;
//            case TXCRCERRSUM:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.TXCRCERRSUM;
//            case ALL:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.ALL;
//            case PILOTTONESTATUS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.PILOTTONESTATUS;
//            case OPTICALPOWER:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.OPTICALPOWER;
//            case OUTPCSBIPERRORS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.OUTPCSBIPERRORS;
//            case INPCSBIPERRORS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.INPCSBIPERRORS;
//            case RXERRSUM:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.RXERRSUM;
//            case RXPKTSERR:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.RXPKTSERR;
//            case OUTPKTS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.OUTPKTS;
//            case INPKTS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmParameterType.INPKTS;
//                default:
//                throw NoMatchEnumValueException.getNoMatchEnumValueException(pmParameterType);
//        }
//    }

    /**
     * PmGranularityType转换
     *
     * @param pmGranularityType api
     * @return dev
     */
//    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmGranularityType apiPmGranularityTypeToDev(PmGranularityType pmGranularityType) {
//        if (pmGranularityType == null) {
//            return null;
//        }
//        switch (pmGranularityType) {
//            case _24H:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmGranularityType._24H;
//            case _15MIN:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmGranularityType._15MIN;
//            default:
//                throw NoMatchEnumValueException.getNoMatchEnumValueException(pmGranularityType);
//        }
//    }

    /**
     * HistoryDataType 转换
     *
     * @param historyPmType dev
     * @return api
     */
//    default HistoryPmType devHistoryDataTypeToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.HistoryPmType historyPmType) {
//        if (historyPmType == null) {
//            return null;
//        }
//        switch (historyPmType) {
//            case TIME:
//                return HistoryPmType.TIME;
//            case RECORDS:
//                return HistoryPmType.RECORDS;
//            default:
//                throw NoMatchEnumValueException.getNoMatchEnumValueException(historyPmType);
//        }
//    }

    /**
     * HistoryDataType 转换
     * @param historyPmType api
     * @return dev
     */
//    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.HistoryPmType apiHistoryDataTypeToDev(HistoryPmType historyPmType){
//        if(historyPmType ==null){
//            return null;
//        }
//        switch (historyPmType){
//            case TIME:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.HistoryPmType.TIME;
//            case RECORDS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.HistoryPmType.RECORDS;
//            default:
//                throw NoMatchEnumValueException.getNoMatchEnumValueException(historyPmType);
//        }
//    }

    /**
     * PmRpcType 转换
     * @param pmRpcType dev
     * @return api
     */
//    default PmRpcType devPmRpcTypeToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmRpcType pmRpcType){
//        if(pmRpcType ==null){
//            return null;
//        }
//        switch (pmRpcType){
//            case HISTORY:
//                return PmRpcType.HISTORY;
//            case CURRENT:
//                return PmRpcType.CURRENT;
//            default:
//                throw NoMatchEnumValueException.getNoMatchEnumValueException(pmRpcType);
//        }
//    }

    /**
     * PmRpcType 转换
     * @param pmRpcType api
     * @return dev
     */
//    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmRpcType apiPmRpcTypeToDev(PmRpcType pmRpcType){
//        if(pmRpcType ==null){
//            return null;
//        }
//        switch (pmRpcType){
//            case HISTORY:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmRpcType.HISTORY;
//            case CURRENT:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmRpcType.CURRENT;
//            default:
//                throw NoMatchEnumValueException.getNoMatchEnumValueException(pmRpcType);
//        }
//    }

    /**
     * pmValueScope 转换
     * @param pmValueScope api
     * @return dev
     */
//    default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmValueScope apiPmValueScopeToDev(PmValueScope pmValueScope){
//        if(pmValueScope == null){
//            return null;
//        }
//        switch (pmValueScope){
//            case INSTANT:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmValueScope.INSTANT;
//            case INSTANTAVGMINMAX:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmValueScope.INSTANTAVGMINMAX;
//            default:
//                throw NoMatchEnumValueException.getNoMatchEnumValueException(pmValueScope);
//        }
//    }

    /**
     * PmRpcResult 转换
     * @param pmRpcResult api
     * @return dev
     */
//   default org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmRpcResult apiPmRpcResultToDev(PmRpcResult pmRpcResult){
//        if(pmRpcResult == null){
//            return null;
//        }
//        switch (pmRpcResult){
//            case SUCCESS:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmRpcResult.SUCCESS;
//            case FAIL:
//                return org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmRpcResult.FAIL;
//            default:
//                throw NoMatchEnumValueException.getNoMatchEnumValueException(pmRpcResult);
//        }
//   }

    /**
     * PmRpcResult 转换
     * @param pmRpcResult dev
     * @return api
     */
//    default PmRpcResult devPmRpcResultToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.types.rev200630.PmRpcResult pmRpcResult){
//        if(pmRpcResult == null){
//            return null;
//        }
//        switch (pmRpcResult){
//            case SUCCESS:
//                return PmRpcResult.SUCCESS;
//            case FAIL:
//                return PmRpcResult.FAIL;
//            default:
//                throw NoMatchEnumValueException.getNoMatchEnumValueException(pmRpcResult);
//        }
//    }

    /**
     * PmDataValue
     * @param pmDataValue dev
     * @return api
     */
//    default PmDataValue devPmDataValueToApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.performance.rev200630.get.pm.data.output.performance.PmDataValue pmDataValue){
//        if(pmDataValue == null){
//            return null;
//        }
//        if(pmDataValue instanceof Instant){
//
//            BigDecimal digitalPmValue = ((Instant) pmDataValue).getDigitalPmValue();
//            // 兼容II型机上报性能 值 pm-data-value
//            if(digitalPmValue == null){
//                digitalPmValue = ((Instant) pmDataValue).getPmDataValue();
//            }
//            //根据规范 将单位为个、秒的性能值置为数字性能，其他的都应该是顺时值
//            if(new BigDecimal(digitalPmValue.longValue()).compareTo(digitalPmValue)==0){
//                //判断其为整数
//                InstantBuilder instantBuilder =  new InstantBuilder();
//                instantBuilder.setDigitalPmValue(digitalPmValue);
//                return instantBuilder.build();
//            }else{
//                //判断其为小数，则是判断设备数据上报错误
//                InstantAvgMinMaxBuilder instantAvgMinMaxBuilder = new InstantAvgMinMaxBuilder();
//                instantAvgMinMaxBuilder.setCurrentValue(digitalPmValue);
//                return instantAvgMinMaxBuilder.build();
//            }
//
//        }else if(pmDataValue instanceof InstantAvgMinMax){
//            InstantAvgMinMaxBuilder instantAvgMinMaxBuilder = new InstantAvgMinMaxBuilder();
//            instantAvgMinMaxBuilder.setAverageValue(((InstantAvgMinMax) pmDataValue).getAverageValue())
//                    .setCurrentValue(((InstantAvgMinMax) pmDataValue).getCurrentValue())
//                    .setMaxValue(((InstantAvgMinMax) pmDataValue).getMaxValue())
//                    .setMinValue(((InstantAvgMinMax) pmDataValue).getMinValue());
//            return instantAvgMinMaxBuilder.build();
//        }else {
//            throw NoMatchEnumValueException.getNoMatchEnumValueException(pmDataValue);
//        }
//    }


}
