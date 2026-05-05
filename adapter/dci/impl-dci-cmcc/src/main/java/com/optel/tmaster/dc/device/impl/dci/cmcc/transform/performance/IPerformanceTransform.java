/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.cmcc.transform.performance;

import com.optel.commons.tools.base.*;
import com.optel.tmaster.dc.device.impl.dci.cmcc.transform.base.*;
import com.optel.tmaster.dc.general.base.exception.manage.*;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.performance.types.rev220208.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.openconfig.types.rev230426.*;

/**
 * InterfaceName: IPerformanceTransform
 * <ul>
 * <li>性能相关 通用 转换方法</li>
 * </ul>
 *
 * @author GongHaiLong
 * 2023/7/27 17:32
 */
interface IPerformanceTransform extends CommonTransform {

    /**
     * OPT DCI 性能对象 转 mini otn 性能对象
     * @param pmPointRef pmPoint性能监视点
     * @return object-name
     */
    default String apiPmPointRefToDev(PmPointRef pmPointRef) {
        if (pmPointRef == null) {
            return "";
        }
        return pmPointRef.getValue();
    }

    /**
     * 性能周期 api To dev
     * @param pmGranularityType 性能周期api 0-15,1-24
     * @return 性能周期dev 1-15,2-24
     */
    default Granularity apiPmGranularityTypeToDev(PmGranularityType pmGranularityType){
        if(pmGranularityType == null){
            return null;
        }
        switch (pmGranularityType){
            case _15MIN:
                return Granularity._15min;
            case _24H:
                return Granularity._24h;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(pmGranularityType);
        }
    }

    /**
     * api objectType To dev type
     * @param pmpObjectType api type
     * @return dev type
     */
    default ObjectType apiPmObjectTypeToDev(PmpObjectType pmpObjectType){
        if(pmpObjectType == null){
            return null;
        }
        switch (pmpObjectType){
            case CHASSIS:
                return ObjectType.CHASSIS;
            case CARD:
                return ObjectType.CARD;
            case PORT:
                return ObjectType.PORT;
            case TRANSCEIVER:
                return ObjectType.TRANSCEIVER;
            case OPTICALCHANNEL:
                return ObjectType.OPTICALCHANNEL;
            case PHYSCIALCHANNEL:
                return ObjectType.PHYSCIALCHANNEL;
            case OLP:
                return ObjectType.APS;
            case AMPLIFIER:
                return ObjectType.EDFA;
            case OTU:
                return ObjectType.OTU;
            case ODU:
                return ObjectType.ODU;
            case ETH:
                return ObjectType.ETH;
            case OCM:
                return ObjectType.OCM;
            case ME:
                return ObjectType.ME;
            case SLOT:
                return ObjectType.SLOT;
            case SDH:
                return ObjectType.SDH;
            case WSS:
                return ObjectType.WSS;
            case OADM:
                return ObjectType.OADM;
            case OTDR:
                return ObjectType.OTDR;
            case INTERFACE:
                return ObjectType.INTERFACE;
            case FREQUENCYCHANNEL:
                return ObjectType.FREQUENCYCHANNEL;
            case OTHER:
            case OSC:
            case ALL:
            default:
            return ObjectType.OTHER;
        }
    }

    /**
     * api to dev
     * @param pmParameterType api pm
     * @return dev pm
     */
    default org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType apiPmParameterTypeToDev(PmParameterType pmParameterType){
        if(pmParameterType == null){
            return null;
        }
        switch (pmParameterType) {
            case RXPKTS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.RXPKTS;
            case TXPKTS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.TXPKTS;
            case RXCRCERRSUM:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.RXCRCERRSUM;
            case RXPKTS64OCTETS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.RXPKTS64OCTETS;
            case RXPKTS65TO127OCTETS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.RXPKTS65TO127OCTETS;
            case RXPKTS128TO255OCTETS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.RXPKTS128TO255OCTETS;
            case RXPKTS256TO511OCTETS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.RXPKTS256TO511OCTETS;
            case RXPKTS512TO1023OCTETS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.RXPKTS512TO1023OCTETS;
            case RXPKTS1024TO1518OCTETS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.RXPKTS1024TO1518OCTETS;
            case TXPKTS64OCTETS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.TXPKTS64OCTETS;
            case TXPKTSOCTETS65TO127:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.TXPKTSOCTETS65TO127;
            case TXPKTS128TO255OCTETS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.TXPKTS128TO255OCTETS;
            case TXPKTS256TO511OCTETS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.TXPKTS256TO511OCTETS;
            case TXPKTS512TO1023OCTETS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.TXPKTS512TO1023OCTETS;
            case TXPKTS1024TO1518OCTETS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.TXPKTS1024TO1518OCTETS;
            case RXSTATOSZ:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.RXSTATOSZ;
            case TXSTATOSZ:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.TXSTATOSZ;
            case RXSTATUSZ:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.RXSTATUSZ;
            case TXSTATUSZ:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.TXSTATUSZ;
            case BBE:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.BBE;
            case BBER:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.BBER;
            case ES:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.ES;
            case SES:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.SES;
            case SESR:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.SESR;
            case UAS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.UAS;
            case FEBBE:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.FEBBE;
            case FEBBER:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.FEBBER;
            case FEES:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.FEES;
            case FESES:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.FESES;
            case FEUAS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.FEUAS;
            case FESESR:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.FESESR;
            case PREFECBER:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.PREFECBER;
            case POSTFECBER:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.POSTFECBER;
            case RXMESSAGENUM:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.RXMESSAGENUM;
            case TXMESSAGENUM:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.TXMESSAGENUM;
            case RXDROPMESSAGENUM:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.RXDROPMESSAGENUM;
            case INPUTPOWER:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.INPUTPOWER;
            case OUTPUTPOWER:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.OUTPUTPOWER;
            case LASERBIASCURRENT:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.LASERBIASCURRENT;
            case TEMPERATURE:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.TEMPERATURE;
            case TECCURRENT:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.TECCURRENT;
            case ACTUALGAIN:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.ACTUALGAIN;
            case ACTUALGAINTILT:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.ACTUALGAINTILT;
            case INPUTPOWERTOTAL:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.INPUTPOWERTOTAL;
            case OUTPUTPOWERTOTAL:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.OUTPUTPOWERTOTAL;
            case OPTICALRETURNLOSS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.OPTICALRETURNLOSS;
            case ACTUALVOAATTENUATION:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.ACTUALVOAATTENUATION;
            case LOWERFREQUENCY:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.LOWERFREQUENCY;
            case UPPERFREQUENCY:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.UPPERFREQUENCY;
            case OSNR:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.OSNR;
            case CHANNELOPTICALPOWER:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.CHANNELOPTICALPOWER;
            case SUPPLYVOLTAGE:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.SUPPLYVOLTAGE;
            case RXBYTES:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.RXBYTES;
            case TXBYTES:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.TXBYTES;
            case TXCRCERRSUM:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.TXCRCERRSUM;
            case ALL:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.ALL;
            case PILOTTONESTATUS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.PILOTTONESTATUS;
            case OUTPCSBIPERRORS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.OUTPCSBIPERRORS;
            case INPCSBIPERRORS:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.INPCSBIPERRORS;
            case RXPKTSERR:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.RXPKTSERR;
            case RXERRSUM:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.RXERRSUM;
            case USEDPOWER:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.USEDPOWER;
            case TEMPERATURECHANGE:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.TEMPERATURECHANGE;
            case SPEEDCHANGE:
                return org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType.SPEEDCHANGE;
            /*
              适配V1型机
             */
            case OPTICALPOWER:

            /*
              适配V1型机
             */
            case ACTUALGAINTITL:

            /*
              适配II型机
             */
            case TXPKTS65TO127OCTETS:
            /*
              适配II型机
             */
            case OUTPKTS:
            /*
              适配II型机
             */
            case INPKTS:
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(pmParameterType);
        }
    }



    default PmpObjectType devObjectTypeToApi(ObjectType objectType){
        if(objectType == null){
            return null;
        }
        switch (objectType){
            case ME:
                return PmpObjectType.ME;
            case CHASSIS:
                return PmpObjectType.CHASSIS;
            case SLOT:
                return PmpObjectType.SLOT;
            case CARD:
                return PmpObjectType.CARD;
            case PORT:
                return PmpObjectType.PORT;
            case TRANSCEIVER:
                return PmpObjectType.TRANSCEIVER;
            case OPTICALCHANNEL:
                return PmpObjectType.OPTICALCHANNEL;
            case PHYSCIALCHANNEL:
                return PmpObjectType.PHYSCIALCHANNEL;
            case APS:
                return PmpObjectType.OLP;
            case EDFA:
                return PmpObjectType.AMPLIFIER;
            case OTU:
                return PmpObjectType.OTU;
            case ODU:
                return PmpObjectType.ODU;
            case ETH:
                return PmpObjectType.ETH;
            case SDH:
                return PmpObjectType.SDH;
            case OTDR:
                return PmpObjectType.OTDR;
            case OCM:
                return PmpObjectType.OCM;
            case INTERFACE:
                return PmpObjectType.INTERFACE;
            case OADM:
                return PmpObjectType.OADM;
            case WSS:
                return PmpObjectType.WSS;
            case FREQUENCYCHANNEL:
                return PmpObjectType.FREQUENCYCHANNEL;
            case OTHER:
                return PmpObjectType.OTHER;
            default:
                return PmpObjectType.ALL;
        }
    }


    /**
     * mini otn 性能对象 转 OPT DCI接口性能对象
     * @param objectName 性能对象名称
     * @return pmPoint 性能监视点
     */
    default PmPointRef devPmPointRefToApi(String objectName) {
        if (StrUtil.isEmpty(objectName)) {
            return new PmPointRef("");
        }
        return new PmPointRef(objectName);
    }

    /**
     * dev to Api
     * @param pmParameterType dev
     * @return api
     */
    default PmParameterType devPmParameterTypeToApi(org.opendaylight.yang.gen.v1.urn.cmcc.yang.performance.types.rev230426.PmParameterType pmParameterType) {
        if (pmParameterType == null) {
            throw NoMatchEnumValueException.getNoMatchEnumValueException(null);
        }
        switch (pmParameterType) {
            case ALL:
                return PmParameterType.ALL;
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
            case PILOTTONESTATUS:
                return PmParameterType.PILOTTONESTATUS;
            case OUTPCSBIPERRORS:
                return PmParameterType.OUTPCSBIPERRORS;
            case INPCSBIPERRORS:
                return PmParameterType.INPCSBIPERRORS;
            case RXPKTSERR:
                return PmParameterType.RXPKTSERR;
            case RXERRSUM:
                return PmParameterType.RXERRSUM;
            case USEDPOWER:
                return PmParameterType.USEDPOWER;
            case TEMPERATURECHANGE:
                return PmParameterType.TEMPERATURECHANGE;
            case SPEEDCHANGE:
                return PmParameterType.SPEEDCHANGE;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(pmParameterType);
        }
    }

    /**
     * dev to api
     * @param granularity dev 周期
     * @return api
     */
   default PmGranularityType devGranularityToApi(Granularity granularity){
        if(granularity == null){
            return null;
        }
        switch (granularity){
            case _15min:
                return PmGranularityType._15MIN;
            case _24h:
                return PmGranularityType._24H;
            default:
                throw NoMatchEnumValueException.getNoMatchEnumValueException(granularity);
        }
   }

}
