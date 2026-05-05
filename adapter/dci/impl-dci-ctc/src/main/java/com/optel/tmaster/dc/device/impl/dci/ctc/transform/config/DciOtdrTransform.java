/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.dci.ctc.transform.config;

import com.optel.tmaster.dc.device.impl.dci.ctc.transform.base.PortAndTransceiverAndOtdrAndOpticalChannelTypeTransform;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.otdr.result.event.Event;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.otdr.result.event.EventBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.otdr.result.event.EventKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.otdr.result.top.Result;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.otdr.result.top.ResultBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.otdr.result.top.ResultKey;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.otdr.top.Otdr;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.otdr.top.OtdrBuilder;
import org.opendaylight.yang.gen.v1.com.optel.dci.yang.otdr.rev220208.otdr.top.OtdrKey;
import org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.otdr.rev200210.*;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.StartOtdrInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.StartOtdrInputBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.StopOtdrInput;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.StopOtdrInputBuilder;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.otdr.result.event.Events;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.otdr.top.Otdrs;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.otdr.top.otdrs.otdr.Config;
import org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.otdr.top.otdrs.otdr.ConfigBuilder;

import java.util.HashMap;
import java.util.Map;

/**
 * otdr 转换类
 *
 * @author Quan Jingyuan
 * @since 2022/3/7
 **/
public class DciOtdrTransform implements PortAndTransceiverAndOtdrAndOpticalChannelTypeTransform {

    public StartOtdrInput apiToStartOtdrInputDev(org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.otdr.rev200210.StartOtdrInput input) {
        StartOtdrInputBuilder builder = new StartOtdrInputBuilder();
        builder.setName(input.getName());
        builder.setMeasureMode(apiToMeasureModeDev(input.getMeasureMode()));
        if (builder.getMeasureMode() != null && input.getMeasureMode().getIntValue() == 0) {
            builder.setMeasuringRange(input.getMeasuringRange());
            builder.setMeasuringTime(input.getMeasuringTime());
            builder.setPulseWidth(input.getPulseWidth());
        }
        return builder.build();
    }

    public StartOtdrOutput devToStartOtdrOutputApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.StartOtdrOutput otdrOutput) {
        StartOtdrOutputBuilder builder = new StartOtdrOutputBuilder();
        builder.setIndex(otdrOutput.getIndex());
        builder.setOtdrFileType(devToOtdrFileFormatTypeApi(otdrOutput.getOtdrFileType()));
        builder.setStartOtdrResult(devToActionResultApi(otdrOutput.getStartOtdrResult()));
        return builder.build();
    }

    public StopOtdrInput apiToStopOtdrOutputDev(org.opendaylight.yang.gen.v1.com.optel.device.opt.dci.otdr.rev200210.StopOtdrInput input) {
        StopOtdrInputBuilder builder = new StopOtdrInputBuilder();
        builder.setName(input.getName());
        return builder.build();
    }

    public GetOtdrConfigOutput devToGetOtdrConfigOutputApi(Otdrs otdrs) {
        if(otdrs==null){
            return null;
        }
        GetOtdrConfigOutputBuilder builder = new GetOtdrConfigOutputBuilder();
        if (otdrs.getOtdr() != null && otdrs.getOtdr().size() != 0) {
            Map<OtdrKey, Otdr> values = new HashMap<>(otdrs.getOtdr().size());
            for (Map.Entry<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.otdr.top.otdrs.OtdrKey, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.otdr.top.otdrs.Otdr> otdrEntry : otdrs.getOtdr().entrySet()) {
                OtdrBuilder otdrBuilder = new OtdrBuilder();
                if(otdrEntry.getValue()!=null){
                    otdrBuilder.setName(otdrEntry.getValue().getName());
                    if(otdrEntry.getValue().getState()!=null){
                        otdrBuilder.setFiberRefractiveIndex(otdrEntry.getValue().getState().getFiberRefractiveIndex());
                        otdrBuilder.setActiveLocalPort(otdrEntry.getValue().getState().getActiveLocalPort());
                        otdrBuilder.setMonitorPort(otdrEntry.getValue().getState().getMonitorPort());
                        otdrBuilder.setMeasuringState(devToMeasuringStateApi(otdrEntry.getValue().getState().getMeasuringState()));
                        otdrBuilder.setOtdrWavelength(otdrEntry.getValue().getState().getOtdrWavelength());
                        values.put(new OtdrKey(otdrEntry.getKey().getName()), otdrBuilder.build());
                    }

                }
            }
           builder.setOtdr(values);
        }
        return builder.build();
    }

    public GetOtdrResultOutput devToGetOtdrResultOutputApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.otdr.top.otdrs.Otdr otdr) {
        if(otdr==null||otdr.getResults()==null||otdr.getResults().getResult()==null){
            return null;
        }
        GetOtdrResultOutputBuilder builder = new GetOtdrResultOutputBuilder();
        Map<ResultKey, Result> values = new HashMap<>(otdr.getResults().getResult().size());
        if (otdr.getResults() != null && otdr.getResults().getResult() != null && otdr.getResults().getResult().size() != 0) {
            for (Map.Entry<org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.otdr.result.top.results.ResultKey, org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.otdr.result.top.results.Result> result : otdr.getResults().getResult().entrySet()) {
                if (result.getValue() == null) {
                    continue;
                }
                ResultBuilder resultBuilder = new ResultBuilder();
                resultBuilder.setIndex(result.getValue().getIndex());
                resultBuilder.setMonitorPort(result.getValue().getMonitorPort());
                resultBuilder.setDetectTime(result.getValue().getDetectTime());
                resultBuilder.setMeasureMode(devToMeasureModeApi(result.getValue().getMeasureMode()));
                resultBuilder.setPulseWidth(result.getValue().getPulseWidth());
                resultBuilder.setMeasuringRange(result.getValue().getMeasuringRange());
                resultBuilder.setMeasuringTime(result.getValue().getMeasuringTime());
                resultBuilder.setMeasuringResolution(result.getValue().getMeasuringResolution());
                resultBuilder.setFiberLength(result.getValue().getFiberLength());
                resultBuilder.setTotalLoss(result.getValue().getTotalLoss());
                resultBuilder.setFiberAttenuation(result.getValue().getFiberAttenuation());
                resultBuilder.setOtdrFileType(devToOtdrFileFormatTypeApi(result.getValue().getOtdrFileType()));
                resultBuilder.setTraceData(result.getValue().getTraceData());
                values.put(new ResultKey(result.getKey().getIndex(), result.getKey().getMonitorPort()), resultBuilder.build());
            }
        }
        builder.setResult(values);
        return builder.build();
    }

    public GetOtdrResultEventOutput devToGetOtdrResultEventOutputApi(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.otdr.result.top.results.Result result){
        if(result==null||result.getEvents()==null){
            return null;
        }
        Events events = result.getEvents();
        GetOtdrResultEventOutputBuilder builder=new GetOtdrResultEventOutputBuilder();
        Map<EventKey, Event> values=new HashMap<>(events.getEvent().size());

        for(org.opendaylight.yang.gen.v1.http.openconfig.net.yang.otdr.rev200630.otdr.result.event.events.Event event:events.getEvent().values()){
            EventBuilder eventBuilder=new EventBuilder();
            eventBuilder.setCumulateLoss(event.getCumulateLoss());
            eventBuilder.setDistance(event.getDistance());
            eventBuilder.setEventIndex(event.getEventIndex());
            eventBuilder.setReturnLoss(event.getReturnLoss());
            eventBuilder.setSpliceLoss(event.getSpliceLoss());
            eventBuilder.setEventType(devToEventTypeApi(event.getEventType()));
            eventBuilder.withKey(new EventKey(event.getEventIndex()));
            values.put(eventBuilder.key(),eventBuilder.build());
        }
        builder.setEvent(values);
        return builder.build();
    }
    public Config apiToOtdrDev(SetOtdrConfigInput input) {
        ConfigBuilder configBuilder = new ConfigBuilder();
        configBuilder.setActiveLocalPort(input.getActiveLocalPort());
        configBuilder.setName(input.getName());
        configBuilder.setFiberRefractiveIndex(input.getFiberRefractiveIndex());
        configBuilder.setMonitorPort(input.getMonitorPort());
        return configBuilder.build();
    }
}
