/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.test;

import com.optel.api.common.base.Constant;
import org.apache.commons.collections.CollUtils;
import com.optel.tmaster.dc.general.base.util.MdsalUtil;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.AlarmDelayInserts;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.AlarmDelayInsertsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EhInstallationCapabilitys;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EhInstallationCapabilitysBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EqInstallationCapabilitys;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EqInstallationCapabilitysBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthLmWorkingMode;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthPtpFrameSpaces;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthPtpFrameSpacesBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthPtpsBandWidth;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.EthPtpsBandWidthBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GfpfParameters;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.GfpfParametersBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.InsertType;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LearningMode;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LptMode;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LptPacs;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.LptPacsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.OamEthConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.OamEthConfigBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.PortLightsState;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.PortLightsStateBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.SfPersistencyTimers;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.SfPersistencyTimersBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.UnknownFrameProcess;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.Vsis;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.VsisBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.alarm.delay.inserts.grouping.AlarmDelayInsert;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.alarm.delay.inserts.grouping.AlarmDelayInsertBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.alarm.delay.inserts.grouping.AlarmDelayInsertKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eh.installation.capabilitys.grouping.EhInstallationCapability;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eh.installation.capabilitys.grouping.EhInstallationCapabilityBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eh.installation.capabilitys.grouping.EhInstallationCapabilityKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eq.installation.capability.grouping.PtpCapability;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eq.installation.capability.grouping.PtpCapabilityBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eq.installation.capability.grouping.PtpCapabilityKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eq.installation.capabilitys.grouping.EqInstallationCapability;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eq.installation.capabilitys.grouping.EqInstallationCapabilityBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eq.installation.capabilitys.grouping.EqInstallationCapabilityKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eq.obj.eq.Light;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eq.obj.eq.LightBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.dm.configs.grouping.EthDmConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.dm.configs.grouping.EthDmConfigBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.dm.configs.grouping.EthDmConfigKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.lm.configs.grouping.EthLmConfig;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.lm.configs.grouping.EthLmConfigBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.lm.configs.grouping.EthLmConfigKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.ptp.band.width.grouping.EthPtpBandWidth;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.ptp.band.width.grouping.EthPtpBandWidthBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.ptp.band.width.grouping.EthPtpBandWidthKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.ptp.band.width.grouping.eth.ptp.band.width.EgressCapacityBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.ptp.band.width.grouping.eth.ptp.band.width.IngressCapacityBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.ptp.frame.spaces.grouping.EthPtpFrameSpace;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.ptp.frame.spaces.grouping.EthPtpFrameSpaceBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eth.ptp.frame.spaces.grouping.EthPtpFrameSpaceKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.gfpf.parameter.grouping.GfpfParameter;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.gfpf.parameter.grouping.GfpfParameterBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.gfpf.parameter.grouping.GfpfParameterKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.lpt.grouping.Lpt;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.lpt.grouping.LptBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.lpt.grouping.LptKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.oam.eth.config.EthDmConfigsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.oam.eth.config.EthLmConfigsBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.sf.persistency.timers.grouping.SfPersistencyTimer;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.sf.persistency.timers.grouping.SfPersistencyTimerBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.sf.persistency.timers.grouping.SfPersistencyTimerKey;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.vsis.grouping.Vsi;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.vsis.grouping.VsiBuilder;
import org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.vsis.grouping.VsiKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.AdaptationType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.OduSwitchType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.SdhSwitchType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc._enum.rev240702.SignalType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.AlarmMaskStates;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.AlarmMaskStatesBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.AlarmState;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.Alarms;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.AlarmsBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.PerceivedSeverity;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.TcaParameters;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.TcaParametersBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.Tcas;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.TcasBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.alarm.mask.states.AlarmMaskState;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.alarm.mask.states.AlarmMaskStateBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.alarm.mask.states.AlarmMaskStateKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.alarms.grouping.Alarm;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.alarms.grouping.AlarmBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.alarms.grouping.AlarmKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tca.TcaParameterBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tca.parameters.TcaParameter;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tca.parameters.TcaParameterKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tcas.grouping.Tca;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tcas.grouping.TcaBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tcas.grouping.TcaKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.AccessAction;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.Connections;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.ConnectionsBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.VlanType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.capacity.cir.or.totalsize.ForCirBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connection.RequestedCapacityBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connections.Connection;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connections.ConnectionBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connections.ConnectionKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.*;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ctps.Ctp;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ctps.CtpBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ctps.CtpKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.eqs.Eq;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.eqs.EqBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.eqs.EqKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ftps.Ftp;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ftps.FtpBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ftps.FtpKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.get.optical.module.info.output.OpticalModuleInfos;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.get.optical.module.info.output.OpticalModuleInfosBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.get.optical.module.info.output.optical.module.infos.OpticalModuleInfo;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.get.optical.module.info.output.optical.module.infos.OpticalModuleInfoBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.get.optical.module.info.output.optical.module.infos.OpticalModuleInfoKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.me.NtpServers;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.me.NtpServersBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.me.ntp.servers.NtpServer;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.me.ntp.servers.NtpServerBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.me.ntp.servers.NtpServerKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ptps.grouping.Ptp;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ptps.grouping.PtpBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ptps.grouping.PtpKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ptps.grouping.ptp.OpticalPowerPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ptps.grouping.ptp.StatePacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.EthFtpPac1;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.EthFtpPac1Builder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eos.rev240702.ftps.ftp.eth.ftp.pac.VcgPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.Ctp1;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.Ctp1Builder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ETH;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.EthMappingType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.Ftp1;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.Ftp1Builder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.OamTransmitInterval;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.PortType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.Ptp1;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.Ptp1Builder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.Ptp2;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.Ptp2Builder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.WorkingMode;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.EthCtpPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.ClientVlanSpecBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.OamConfigBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.OamStatePacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.PerformanceBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ctps.ctp.eth.ctp.pac.VlanSpecBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ftps.ftp.EthFtpPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ptps.ptp.EthMappingPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.eth.rev240702.ptps.ptp.EthPtpPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.GHaoStatus;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.ODU;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.ctps.ctp.OduCtpPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.ptps.ptp.OduPtpPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev210611.E1FrameType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev210611.E1Opcode;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev210611.E1PhyType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev210611.specifc.ptps.ptp.PdhPtpPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.Performances;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.PerformancesBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.PerformanceKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.digital.or.analog.AnalogBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.digital.or.analog.DigitalBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev210131.performances.grouping.performance.digital.or.analog.analog.AnalogPmValueBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.EqPgs;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.EqPgsBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.Pgs;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.PgsBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionDirection;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ProtectionTypeEqPg;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.ReversionMode;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SdTrigger;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchReason;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.SwitchType;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.eq.pgs.EqPg;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.eq.pgs.EqPgBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.eq.pgs.EqPgKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.Pg;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.PgBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.pgs.PgKey;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.SDH;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.ctps.ctp.VcCtpPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.ftps.ftp.SdhFtpPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.ptps.ptp.SdhPtpPacBuilder;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.Granularity;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.LayerProtocolName;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.Real;
import org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.yang.types.rev190213.ThresholdType;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.Ipv4Address;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.inet.types.rev130715.PortNumber;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DateAndTime;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.DottedQuad;
import org.opendaylight.yang.gen.v1.urn.ietf.params.xml.ns.yang.ietf.yang.types.rev130715.MacAddress;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.management.StringValueExp;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.*;

/**
 * <ul>
 * <li>(为了测试构造数据)</li>
 * </ul>
 *
 * @author LWX 2020/7/2 16:41
 */
public class TestData {
    private final DataBroker dataBroker;
    private static final Logger LOG = LoggerFactory.getLogger(TestData.class);

    /**
     * 系统启动时加载，将数据写入Operation库
     */
    public TestData(final DataBroker dataBroker) {
        this.dataBroker = dataBroker;
        // try {
        //     me();
        //     eq();
        //     ptp();
        //     ftp();
        //     ctp();
        //     alarms();
        //     ethPtpBandwidth();
        //     buildPgs();
        //     tacs();
        //     performances();
        //     alarmMask();
        //     tcaParameters();
        //     getOamEthConfig();
        //     vsis();
        //     connection();
        //     ltp();
        //     alarmDelayInserts();
        //     sfPersistencyTimer();
        //     EthPtpFrameSpaces();
        //     objInstallation();
        //     GfpfParameters();
        //     GetEqs();
        //     pdhPtp();
        //     EqPgTest();
        //     eq();
        // } catch (Exception e) {
        //     LOG.error("Add Test Data Error", e);
        // }

    }

//     private void objInstallation(){
//         List<EhInstallationCapability> ehInstallationCapabilityList = new ArrayList<>();
//         for(int i=1;i<=10;i++){
//             EhInstallationCapabilityBuilder ehInstallationCapabilityBuilder = new EhInstallationCapabilityBuilder();
//             String name = "EH=/shelf=1/slot=" + i;
//             ehInstallationCapabilityBuilder.withKey(new EhInstallationCapabilityKey(name));
//             ehInstallationCapabilityBuilder.setName(name);
//             if(i <= 4){
//                 ehInstallationCapabilityBuilder.setEqType("SH2_08");
//             }
//             if(i >=4 && i<=6){
//                 ehInstallationCapabilityBuilder.setActualEqType("SH2_08");
//             }
//             ehInstallationCapabilityBuilder.setSupportEqType(CollUtil.newArrayList("SH2_08","SH2_02", "TOS_08","NULL"));
//             ehInstallationCapabilityList.add(ehInstallationCapabilityBuilder.build());
//         }
//         InstanceIdentifier<EhInstallationCapabilitys> iid = InstanceIdentifier.create(EhInstallationCapabilitys.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, new EhInstallationCapabilitysBuilder().setEhInstallationCapability(ehInstallationCapabilityList).build());
//
//         List<EqInstallationCapability> eqInstallationCapabilityList = new ArrayList<>();
//         for(int i=1;i<=5;i++){
//             if(i == 3){
//                 continue;
//             }
//             EqInstallationCapabilityBuilder eqInstallationCapabilityBuilder = new EqInstallationCapabilityBuilder();
//             String name = "EH=/shelf=1/slot=" + i + "/EQ=" + i;
//             eqInstallationCapabilityBuilder.withKey(new EqInstallationCapabilityKey(name));
//             eqInstallationCapabilityBuilder.setEqName(name);
//             List<PtpCapability> ptpCapabilities = new ArrayList<>();
//             for(int j=1;j<=8;j++){
//                 PtpCapabilityBuilder ptpCapabilityBuilder = new PtpCapabilityBuilder();
//                 ptpCapabilityBuilder.withKey(new PtpCapabilityKey(j+""));
//                 ptpCapabilityBuilder.setPortId(j+"");
//                 if( j<=4 ){
//                     ptpCapabilityBuilder.setPtpType("STM4");
//                 }
//                 if(j>2 && j<=6){
//                     ptpCapabilityBuilder.setActualPtpType("STM4");
//                 }
//                 ptpCapabilityBuilder.setSupportPtpType(CollUtil.newArrayList("STM16","STM4", "NULL"));
//                 ptpCapabilities.add(ptpCapabilityBuilder.build());
//             }
//             eqInstallationCapabilityBuilder.setPtpCapability(ptpCapabilities);
//             eqInstallationCapabilityList.add(eqInstallationCapabilityBuilder.build());
//         }
//         InstanceIdentifier<EqInstallationCapabilitys> eqIid = InstanceIdentifier.create(EqInstallationCapabilitys.class);
//         MdsalUtil.doMergeToOperational(dataBroker, eqIid, new EqInstallationCapabilitysBuilder().setEqInstallationCapability(eqInstallationCapabilityList).build());
//
//     }
//
//     /**
//      * 网元数据 入库
//      */
//     private void me() {
//         String ip="192.168.9.10";
//         MeBuilder meBuilder = getMe(ip);
//         InstanceIdentifier<Me> iid = InstanceIdentifier.create(Me.class);
//         MdsalUtil.doMergeToOperational(this.dataBroker, iid, meBuilder.build());
//     }
//
//     private MeBuilder getMe(String ip){
//         MeBuilder meBuilder = new MeBuilder();
//         meBuilder.setName("ME=Ne");
//         meBuilder.setUuid("UUid"+ip);
//         meBuilder.setStatus(MeStatus.Installing);
//         meBuilder.setManufacturer("OPTEL");
//         meBuilder.setProductName("OTN");
//         meBuilder.setSoftwareVersion("VS5.00");
//         meBuilder.setHardwareVersion("VH1.00");
//         meBuilder.setDeviceType(DeviceType.OtmBox);
//         List<String> eqs = new ArrayList<>();
// //        eqs.add();
//         meBuilder.setEq(eqs);
//         List<Class<? extends LayerProtocolName>> layerProtocolNames = new ArrayList<>();
//         layerProtocolNames.add(SDH.class);
//         layerProtocolNames.add(ETH.class);
//         layerProtocolNames.add(ODU.class);
//         meBuilder.setLayerProtocolName(layerProtocolNames);
//         meBuilder.setIpAddress(new Ipv4Address(ip));
//         meBuilder.setMask(new DottedQuad("255.255.255.0"));
//         meBuilder.setNtpEnable(true);
//         meBuilder.setNtpServers(getNtpServers());
//         meBuilder.setNtpState(NtpState.FSET);
//         meBuilder.setGateWay1(new Ipv4Address("192.16.10.1"));
//         meBuilder.setGateWay2(new Ipv4Address("192.16.10.2"));
//         return meBuilder;
//     }
//
//     /**
//      * 网元 NTP服务器数据
//      *
//      * @return 网元 NTP服务器数据
//      */
//     private NtpServers getNtpServers() {
//         List<NtpServer> ntpServers = new ArrayList<>();
//         for (int i = 1; i < 4; i++) {
//             String name = "ntp" + i;
//             NtpServerBuilder ntpServerBuilder = new NtpServerBuilder().withKey(new NtpServerKey(name)).setName(name)
//                     .setIpAddress(new Ipv4Address("192.16.2.3")).setPort(new PortNumber(920)).setNtpVersion("V" + i);
//             ntpServers.add(ntpServerBuilder.build());
//         }
//         return new NtpServersBuilder().setNtpServer(ntpServers).build();
//     }
//
//     /**
//      * 单板数据入库
//      */
//     private void eq() {
//         List<Eq> eqList = new ArrayList<>();
//         for (int i = 1; i < 6; i++) {
//             EqBuilder eqBuilder = new EqBuilder();
//             String name = "EH=/shelf=1/slot=" + i + "/EQ=" + i;
//             eqBuilder.withKey(new EqKey(name)).setName(name);
// //            eqBuilder.setPtp();
//             eqBuilder.setPlugState(true);
//             eqBuilder.setSoftwareVersion("Veq1.00");
//             eqBuilder.setHardwareVersion("VeqH2.00");
//             List<EqType> eqType = new ArrayList<>();
//             if (i == 1) {
//                 eqType.add(EqType.SystemControl);
//                 eqType.add(EqType.CrossConnect);
//                 eqBuilder.setWorkingState(WorkingState.Normal);
//                 eqBuilder.setXcCapability(true);
//             } else if (i == 3) {
//                 eqType.add(EqType.Fan);
//                 eqBuilder.setPlugState(false);
//                 eqBuilder.setXcCapability(false);
//             } else {
//                 eqType.add(EqType.Service);
//                 eqBuilder.setWorkingState(WorkingState.Backup);
//             }
//             eqBuilder.setEqType(eqType);
//             eqList.add(eqBuilder.build());
//         }
//         InstanceIdentifier<Eqs> iid = InstanceIdentifier.create(Eqs.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, new EqsBuilder().setEq(eqList).build());
//     }
//
//
//     /**
//      * ptp 物理端口数据入库
//      */
//     private void ptp() {
//         PtpsBuilder ptpBuilder = new PtpsBuilder().setPtp(getPtps());
//         InstanceIdentifier<Ptps> iid = InstanceIdentifier.create(Ptps.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, ptpBuilder.build());
//     }
//
//     private void ftp() {
//         FtpsBuilder ftpsBuilder = new FtpsBuilder().setFtp(getFtps());
//         InstanceIdentifier<Ftps> iid = InstanceIdentifier.create(Ftps.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, ftpsBuilder.build());
//     }
//
//     private void alarmDelayInserts(){
//         AlarmDelayInsertsBuilder builder = new AlarmDelayInsertsBuilder().setAlarmDelayInsert(getAlarmDelayInsert());
//         InstanceIdentifier<AlarmDelayInserts> iid = InstanceIdentifier.create(AlarmDelayInserts.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, builder.build());
//     }
//
//     private void pdhPtp(){
//         SpecifcPtpsBuilder builder = new SpecifcPtpsBuilder().setPtp(getPdhPtps());
//         InstanceIdentifier<SpecifcPtps> iid = InstanceIdentifier.create(SpecifcPtps.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, builder.build());
//     }
//
//     private void ltp(){
//         LptPacsBuilder lptPacBuilder = new LptPacsBuilder().setLpt(getLpt());
//         InstanceIdentifier<LptPacs> iid = InstanceIdentifier.create(LptPacs.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, lptPacBuilder.build());
//     }
//
//     private void EthPtpFrameSpaces(){
//         EthPtpFrameSpacesBuilder builder = new EthPtpFrameSpacesBuilder().setEthPtpFrameSpace(getEthPtpFrameSpaces());
//         InstanceIdentifier<EthPtpFrameSpaces> iid = InstanceIdentifier.create(EthPtpFrameSpaces.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, builder.build());
//     }
//
//     private List<EthPtpFrameSpace> getEthPtpFrameSpaces(){
//         List<EthPtpFrameSpace> list = new LinkedList<>();
//         for(int i=1;i<=4;i++){
//             EthPtpFrameSpaceBuilder builder = new EthPtpFrameSpaceBuilder();
//             builder.withKey(new EthPtpFrameSpaceKey("PTP=/shelf=1/slot=1/port="+i));
//             builder.setFrameSpace(i+8);
//             builder.setName("PTP=/shelf=1/slot=1/port="+i);
//             list.add(builder.build());
//         }
//         return list;
//     }
//
//     private void GfpfParameters(){
//         GfpfParametersBuilder builder = new GfpfParametersBuilder().setGfpfParameter(getGfpfParameters());
//         InstanceIdentifier<GfpfParameters> iid = InstanceIdentifier.create(GfpfParameters.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, builder.build());
//     }
//
//     private List<Ptp> getPdhPtps(){
//         List<Ptp> list = new LinkedList<>();
//         for(int i=10;i<=14;i++){
//             String name = "PTP=/shelf=1/slot=1/port=" + i;
//             org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ptps.grouping.PtpBuilder builder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ptps.grouping.PtpBuilder();
//             builder.withKey(new PtpKey(name));
//             builder.setName(name);
//             builder.setLayerProtocolName(SDH.class);
//             org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev210611.Ptp1Builder pdhBuilder
//                     = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev210611.Ptp1Builder();
//
//             PdhPtpPacBuilder pdhPtpPacBuilder = new PdhPtpPacBuilder();
//             pdhPtpPacBuilder.setE1FrameType(E1FrameType.PCM30);
//             pdhPtpPacBuilder.setE1Opcode(E1Opcode.AMI);
//             pdhPtpPacBuilder.setE1PhyType(E1PhyType._120o);
//             pdhBuilder.setPdhPtpPac(pdhPtpPacBuilder.build());
//             builder.addAugmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev210611.Ptp1.class,pdhBuilder.build());
//             list.add(builder.build());
//         }
//         return list;
//     }
//
//     private List<GfpfParameter> getGfpfParameters(){
//         List<GfpfParameter> list = new LinkedList<>();
//         for(int i=2;i<=6;i++){
//             GfpfParameterBuilder builder = new GfpfParameterBuilder();
//             builder.withKey(new GfpfParameterKey("FTP=/shelf=1/slot=1/port=" + i));
//             if(i%2==0){
//                 builder.setPayloadFcs(true);
//                 builder.setScrambler(true);
//             }else{
//                 builder.setPayloadFcs(false);
//                 builder.setScrambler(false);
//             }
//             builder.setEthFtp("FTP=/shelf=1/slot=1/port=" + i);
//             list.add(builder.build());
//         }
//         return list;
//     }
//
//     private List<AlarmDelayInsert> getAlarmDelayInsert(){
//         List<AlarmDelayInsert> list = new LinkedList<>();
//         for(int i=1;i<=4;i++){
//             AlarmDelayInsertBuilder builder = new AlarmDelayInsertBuilder();
//             builder.withKey(new AlarmDelayInsertKey("PTP=/shelf=1/slot=1/port="+i));
//             if(i%2==0){
//                 builder.setInsertType(InsertType.DelayInsert);
//                 builder.setDelayTime(new Short("4"));
//             }else{
//                 builder.setInsertType(InsertType.DirectInsert);
//                 builder.setDelayTime(new Short("2"));
//             }
//             builder.setName("PTP=/shelf=1/slot=1/port="+i);
//             list.add(builder.build());
//         }
//         return list;
//     }
//
//     private List<Lpt> getLpt(){
//         List<Lpt> list = new LinkedList<>();
//         for(int i=0;i<4;i++){
//             LptBuilder builder = new LptBuilder();
//             builder.withKey(new LptKey("PTP=/shelf=1/slot=1/port="+i));
//             if(i%2==0){
//                 builder.setAdminState(AdminState.Enabled);
//                 builder.setLptMode(LptMode.ICSD);
//             }else{
//                 builder.setAdminState(AdminState.Disabled);
//                 builder.setLptMode(LptMode.TOL);
//             }
//             builder.setName("PTP=/shelf=1/slot=1/port="+i);
//             list.add(builder.build());
//         }
//         return list;
//
//     }
//
//
//
//
//
//     private void ctp() {
//         CtpsBuilder ctpsBuilder = new CtpsBuilder().setCtp(getCtps());
//         InstanceIdentifier<Ctps> iid = InstanceIdentifier.create(Ctps.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, ctpsBuilder.build());
//     }
//
//     private List<Ptp> getPtps() {
//         List<Ptp> ptpList = new ArrayList<>();
//         //板卡下
//         for (int i = 1; i < 6; i++) {
//             int size = 4;
//             if (i == 3) {
//                 //风扇盘
//                 size = 0;
//             }
//             //端口
//             for (int j = 1; j <= size; j++) {
//                 PtpBuilder ptpBuilder = new PtpBuilder();
//                 String name = "PTP=/shelf=1/slot=" + i + "/port=" + j;
//                 buildPtpComm(ptpBuilder, name, j);
//                 // ptpBuilder.setCtp();
//                 if (i == 1 && j == 2) {
//                     ptpBuilder.setPeerIpAddress(new Ipv4Address("192.168.9.9"));
//                     // PTP=/shelf=1/slot=1/port=989986817
//                     ptpBuilder.setPeerTcpId("01010002").setLocalTcpId("010" + i + "000" + j);
//                 } else if (i == 2 && j == 2) {
//                     ptpBuilder.setPeerIpAddress(new Ipv4Address("192.168.3.2"));
//                     // PTP=/shelf=1/slot=1/port=989986817
//                     ptpBuilder.setPeerTcpId("01010001").setLocalTcpId("010" + i + "000" + j);
//                 }
//                 //TODO ETH SDH
//                 int co = j % 4;
//                 switch (co) {
//                     case 0:
//                         ptpBuilder.setInterfaceType(InterfaceType.UNI);
//                         buildEthPac(ptpBuilder, i);
//                         //远端设备信息
//                         // String rePtpName = name+";REPTP=/port=1";
//                         // ptpBuilder.setRemotePtp(CollUtil.newArrayList(rePtpName));
//                         // PtpBuilder rePtpBuilder = new PtpBuilder();
//                         // buildPtpComm(rePtpBuilder, rePtpName, 0);
//                         // rePtpBuilder.setInterfaceType(InterfaceType.NNI);
//                         // buildEthPac(rePtpBuilder, i);
//                         // ptpList.add(rePtpBuilder.build());
//                         break;
//                     case 1:
//                     case 2:
//                         ptpBuilder.setLayerProtocolName(SDH.class);
//                         org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ptp2Builder sdhPapPac =
//                                 new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ptp2Builder();
//                         SdhPtpPacBuilder sdhPtpPacBuilder = new SdhPtpPacBuilder();
//                         List<SignalType> signal = new ArrayList<>();
//                         signal.add(SignalType.STM1);
//                         signal.add(SignalType.STM4);
//                         signal.add(SignalType.STM16);
//                         sdhPtpPacBuilder.setSupportedSignalTypes(signal);
//                         if (co == 2) {
//                             sdhPtpPacBuilder.setCurrentSignalType(SignalType.STM4);
//                             // signal.add(SignalType.E1);
//                             // //E1时，有PDH相关参数
//                             // sdhPtpPacBuilder.setCurrentSdhSignalType(E1.class);
//                             // org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.Ptp1Builder pdhPtpPac = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.Ptp1Builder();
//                             // PdhPtpPacBuilder pdhPtpPacBuilder = new PdhPtpPacBuilder();
//                             // pdhPtpPacBuilder.setE1FrameType(E1FrameType.PCM30);
//                             // pdhPtpPacBuilder.setE1Opcode(E1Opcode.AMI);
//                             // pdhPtpPacBuilder.setE1PhyType(E1PhyType._120o);
//                             // pdhPtpPac.setPdhPtpPac(pdhPtpPacBuilder.build());
//                             // ptpBuilder.addAugmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.pdh.rev200611.Ptp1.class, pdhPtpPac.build());
//                         } else if (i == 2) {
//                             sdhPtpPacBuilder.setCurrentSignalType(SignalType.STM16);
//                         } else {
//                             sdhPtpPacBuilder.setCurrentSignalType(SignalType.STM1);
//                         }
//                         List<SdhSwitchType> switchList = new ArrayList<>();
//                         switchList.add(SdhSwitchType.VC3);
//                         switchList.add(SdhSwitchType.VC4);
//                         sdhPtpPacBuilder.setSwitchCapability(switchList);
// //                        sdhPtpPacBuilder.setJ0ActualTx("003132330000000000000000000000");
// //                        sdhPtpPacBuilder.setJ0ActualRx("003132330000000000000000000000");
// //                        sdhPtpPacBuilder.setJ0ExpectedRx("003132330000000000000000000000");
//                         sdhPapPac.setSdhPtpPac(sdhPtpPacBuilder.build());
//                         ptpBuilder.addAugmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ptp2.class, sdhPapPac.build());
//                         break;
//                     case 3:
//                         ptpBuilder.setLayerProtocolName(ODU.class);
//                         org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Ptp1Builder oduPtpPac
//                                 = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Ptp1Builder();
//                         OduPtpPacBuilder oduPtpPacBuilder = new OduPtpPacBuilder();
//                         List<SignalType> value = new ArrayList<>();
//                         value.add(SignalType.ODU0);
//                         value.add(SignalType.ODU1);
//                         value.add(SignalType.ODU2);
//                         if (i == 2) {
//                             oduPtpPacBuilder.setOduCapacity(BigInteger.valueOf(1));
//                         } else if(i == 1) {
//                             oduPtpPacBuilder.setOduCapacity(BigInteger.valueOf(8));
//                             value.add(SignalType.ODU2e);
//                         }
//                         else if(i == 4) {
//                             oduPtpPacBuilder.setOduCapacity(BigInteger.valueOf(8));
//                         }
//                         else{
//                             oduPtpPacBuilder.setOduCapacity(BigInteger.valueOf(2));
//                             value.add(SignalType.ODU2e);
//                         }
//                         oduPtpPacBuilder.setOduSignalType(value);
//                         List<AdaptationType> adaptationType = new ArrayList<>();
//                         adaptationType.add(AdaptationType.ODUk);
//                         adaptationType.add(AdaptationType.ODUij);
//                         adaptationType.add(AdaptationType.ODUj21);
//                         oduPtpPacBuilder.setAdaptationType(adaptationType);
//                         List<OduSwitchType> switchType = new ArrayList<>();
//                         switchType.add(OduSwitchType.ODU0);
//                         switchType.add(OduSwitchType.ODU1);
//                         oduPtpPacBuilder.setSwitchCapability(switchType);
//                         oduPtpPacBuilder.setTsDetail("2-80");
// //                        oduPtpPacBuilder.setSmtrailTraceActualTx("33340000000000000000000000000000333400000000000000000000000000003334000000000000000000000000000000000000000000000000000000000000");
// //                        oduPtpPacBuilder.setSmtrailTraceActualRx("33340000000000000000000000000000333400000000000000000000000000003334000000000000000000000000000000000000000000000000000000000000");
// //                        oduPtpPacBuilder.setSmtrailTraceExpectedRx("33340000000000000000000000000000333400000000000000000000000000003334000000000000000000000000000000000000000000000000000000000000");
// //                        oduPtpPacBuilder.setPmtrailTraceActualTx("33340000000000000000000000000000333400000000000000000000000000003334000000000000000000000000000000000000000000000000000000000000");
// //                        oduPtpPacBuilder.setPmtrailTraceActualRx("33340000000000000000000000000000333400000000000000000000000000003334000000000000000000000000000000000000000000000000000000000000");
// //                        oduPtpPacBuilder.setPmtrailTraceExpectedRx("33340000000000000000000000000000333400000000000000000000000000003334000000000000000000000000000000000000000000000000000000000000");
//                         oduPtpPac.setOduPtpPac(oduPtpPacBuilder.build());
//                         ptpBuilder.addAugmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Ptp1.class, oduPtpPac.build());
//                         break;
//                     default:
//                         break;
//                 }
//                 ptpList.add(ptpBuilder.build());
//             }
//         }
//         return ptpList;
//     }
//
//     private void buildEthPac(PtpBuilder ptpBuilder, int i){
//         ptpBuilder.setLayerProtocolName(ETH.class);
//         List<Class<? extends LayerProtocolName>> supportedLayer = new ArrayList<>();
//         supportedLayer.add(ETH.class);
//         supportedLayer.add(SDH.class);
//         ptpBuilder.setSupportedLayerProtocolName(supportedLayer);
//
//         Ptp1Builder ethMapping = new Ptp1Builder();
//         EthMappingPacBuilder ethMappingPacBuilder = new EthMappingPacBuilder();
//         List<EthMappingType> ethMappingTypes = new ArrayList<>();
//         ethMappingTypes.add(EthMappingType.EoO);
//         ethMappingTypes.add(EthMappingType.EoS);
//         ethMappingPacBuilder.setSupportedMappingType(ethMappingTypes);
//         List<OduSwitchType> oduTypes = new ArrayList<>();
//         oduTypes.add(OduSwitchType.ODU0);
//         oduTypes.add(OduSwitchType.ODU1);
//         ethMappingPacBuilder.setSupportedOduServerSwitchCapability(oduTypes);
//         ethMapping.setEthMappingPac(ethMappingPacBuilder.build());
//         ptpBuilder.addAugmentation(Ptp1.class, ethMapping.build());
//
//         Ptp2Builder ethPapPac = new Ptp2Builder();
//         EthPtpPacBuilder ethPtpPacBuilder = new EthPtpPacBuilder();
//         List<WorkingMode> list = new LinkedList<>();
//         list.add(WorkingMode._100MFullDuplex);
//         list.add(WorkingMode._1000MFullDuplex);
//         list.add(WorkingMode.Auto);
//         if (i == 2) {
//             list.add(WorkingMode._10GEFullDuplex);
//         }
//         ethPtpPacBuilder.setSupportedWorkingMode(list);
//         if (i == 2) {
//             ethPtpPacBuilder.setCurrentWorkingMode(WorkingMode.Auto);
//         } else {
//             ethPtpPacBuilder.setCurrentWorkingMode(WorkingMode._100MFullDuplex);
//         }
//         ethPtpPacBuilder.setSupportedMtu(BigInteger.valueOf(128));
//         ethPtpPacBuilder.setCurrentMtu(BigInteger.valueOf(64));
//         ethPtpPacBuilder.setPauseControl(true);
//         ethPtpPacBuilder.setMacAddress(new MacAddress("11:22:33:44:AA:0" + i));
//         ethPtpPacBuilder.setPortType(PortType.Electrical);
//         if (i < 2) {
//             ethPtpPacBuilder.setLldpEnable(true);
//         }
//         ethPtpPacBuilder.setLldpPeerChassisId("LldpPeerChassisId");
//         ethPtpPacBuilder.setLldpPeerPortId("LldpPeerPortId");
//         ethPtpPacBuilder.setLldpPeerSystemName("LldpPeerSystemName");
//         ethPapPac.setEthPtpPac(ethPtpPacBuilder.build());
//         ptpBuilder.addAugmentation(Ptp2.class, ethPapPac.build());
//     }
//
//     private void buildPtpComm(PtpBuilder ptpBuilder, String name, int j){
//         ptpBuilder.withKey(new PtpKey(name));
//         ptpBuilder.setName(name);
//         ptpBuilder.setInterfaceType(InterfaceType.NNI);
//         if (j <= 1) {
//             ptpBuilder.setLoopBack(LoopbackType.NonLoopback);
//             ptpBuilder.setLaserStatus(LaserStatus.LaserOff);
//             ptpBuilder.setStatePac(new StatePacBuilder().setAdminState(AdminState.Enabled).setOperationalState(OperationalState.Up).build());
//         } else if(j <= 3){
//             ptpBuilder.setLoopBack(LoopbackType.TerminalLoopback);
//             ptpBuilder.setLaserStatus(LaserStatus.LaserOn);
//             ptpBuilder.setOpticalPowerPac(new OpticalPowerPacBuilder().setInputPower(new Real(new BigDecimal(1111L))).setOutputPower(new Real(new BigDecimal(303)))
//                     .setInputPowerLowerThreshold(new Real(new BigDecimal(100))).setInputPowerUpperThreshold(new Real(new BigDecimal(30000))).build());
//             ptpBuilder.setStatePac(new StatePacBuilder()
//                     .setAdminState(AdminState.Disabled).setOperationalState(OperationalState.Down).build());
//         } else{
//             ptpBuilder.setLoopBack(LoopbackType.FacilityLoopback);
//             ptpBuilder.setLaserStatus(LaserStatus.NoOpticalModule);
//             ptpBuilder.setStatePac(new StatePacBuilder().setAdminState(AdminState.Disabled).setOperationalState(OperationalState.Down).build());
//         }
//     }
//
//     private List<Ftp> getFtps() {
//         List<Ftp> list = new LinkedList<>();
//         //板卡
//         for (int i = 1; i <= 5; i++) {
//             //端口
//             for (int j = 1; j <= 8; j++) {
//                 FtpBuilder ftpBuilder = new FtpBuilder();
//                 String name = "FTP=/shelf=1/slot=" + i + "/port=" + j;
//                 ftpBuilder.setName(name);
//                 ftpBuilder.withKey(new FtpKey(name));
//                 org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ftp.StatePacBuilder builder =
//                         new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ftp.StatePacBuilder()
//                                 .setAdminState(AdminState.Enabled).setOperationalState(OperationalState.Up);
//                 ftpBuilder.setStatePac(builder.build());
//                 //该FTP承载的CTP和承载该 FTP 的服务层 CTP暂不使用，不填写测试数据
//                 List<String> serverCtpId = new LinkedList<>();
//                 serverCtpId.add("ctp 1");
//                 serverCtpId.add("ctp 2");
//                 ftpBuilder.setServerCtp(serverCtpId);
//                 int col = j % 2;
//                 switch (col) {
//                     case 0:
//                         //eth端口测试数据
//                         ftpBuilder.setLayerProtocolName(ETH.class);
//                         Ftp1Builder eth = new Ftp1Builder();
//                         EthFtpPacBuilder ethFtpPacBuilder = new EthFtpPacBuilder();
//                         ethFtpPacBuilder.setSupportedMtu(BigInteger.valueOf(128));
//                         ethFtpPacBuilder.setCurrentMtu(BigInteger.valueOf(64));
//                         ethFtpPacBuilder.setMappingType(EthMappingType.EoO);
//                         ethFtpPacBuilder.setServiceMappingMode(AdaptationType.GMP);
//                         ethFtpPacBuilder.setServiceMappingMode(AdaptationType.GMP);
//                         //添加vcg
//                         VcgPacBuilder vcgParametersBuilder = new VcgPacBuilder();
//                         vcgParametersBuilder.setVcType(SdhSwitchType.VC4);
//                         vcgParametersBuilder.setLcas(true);
//                         vcgParametersBuilder.setHoldOff(1000L);
//                         vcgParametersBuilder.setWtr(300L);
//                         vcgParametersBuilder.setTsd(true);
//                         vcgParametersBuilder.setTxNumber(BigInteger.valueOf(50));
//                         vcgParametersBuilder.setRxNumber(BigInteger.valueOf(50));
//                         vcgParametersBuilder.setSoHandshakeState(true);
//                         EthFtpPac1Builder ethFtpPac1Builder = new EthFtpPac1Builder();
//                         ethFtpPac1Builder.setVcgPac(vcgParametersBuilder.build());
//                         ethFtpPacBuilder.addAugmentation(EthFtpPac1.class, ethFtpPac1Builder.build());
//                         eth.setEthFtpPac(ethFtpPacBuilder.build());
//                         ftpBuilder.addAugmentation(Ftp1.class, eth.build());
//                         break;
//                     case 1:
//                         //sdh端口测试数据
//                         ftpBuilder.setLayerProtocolName(SDH.class);
//                         org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ftp1Builder sdh =
//                                 new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ftp1Builder();
//                         SdhFtpPacBuilder sdhFtpPacBuilder = new SdhFtpPacBuilder();
//                         sdhFtpPacBuilder.setSdhSignalType(SignalType.STM4);
//                         List<SdhSwitchType> switchType = new LinkedList<>();
//                         switchType.add(SdhSwitchType.VC3);
//                         switchType.add(SdhSwitchType.VC4);
//                         sdhFtpPacBuilder.setSwitchCapability(switchType);
//                         sdhFtpPacBuilder.setServiceMappingMode(AdaptationType.GMP);
//                         sdhFtpPacBuilder.setJ0ActualTx("313233000000000000000000000000");
//                         sdhFtpPacBuilder.setJ0ActualRx("313233000000000000000000000000");
//                         sdhFtpPacBuilder.setJ0ExpectedRx("313233000000000000000000000000");
//                         sdh.setSdhFtpPac(sdhFtpPacBuilder.build());
//                         ftpBuilder.addAugmentation(
//                                 org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ftp1.class, sdh.build());
//                         break;
//                     default:
//                         break;
//                 }
//                 list.add(ftpBuilder.build());
//             }
//         }
//         return list;
//     }
//
//     private List<Ctp> getCtps() {
//         List<Ctp> list = new LinkedList<>();
//         //板卡
//         for (int i = 1; i <= 3; i++) {
//             if(i == 3){
//                 continue;
//             }
//             //端口
//             for (int j = 1; j <= 4; j++) {
//                 for (int k = 1; k <= 3; k++) {
//                     CtpBuilder ctpBuilder = new CtpBuilder();
//                     String serverTp = "PTP=/shelf=1/slot=" + i + "/port=" + j ;
//                     String name = serverTp + "/CTP=" + k;
//                     ctpBuilder.setName(name);
//                     ctpBuilder.withKey(new CtpKey(name));
//                     org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ctp.StatePacBuilder ctpStatePacBuilder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.ctp.StatePacBuilder()
//                             .setAdminState(AdminState.Enabled).setOperationalState(OperationalState.Up);
//                     ctpBuilder.setStatePac(ctpStatePacBuilder.build());
//                     ctpBuilder.setServerTp(serverTp);
//                     int col = k % 3;
//                     switch (col) {
//                         case 0:
//                             ctpBuilder.setLayerProtocolName(ETH.class);
//                             ctpBuilder.setPortRole(PortRole.Root);
//                             ctpBuilder.setProtectRole(ProtectRole.Protected);
//                             ctpBuilder.setLoopBack(LoopbackType.FacilityLoopback);
//                             Ctp1Builder ctp1Builder = new Ctp1Builder();
//
//                             EthCtpPacBuilder ethCtpPacBuilder = new EthCtpPacBuilder();
//                             //clientVlanSpac
//                             ClientVlanSpecBuilder clientVlanSpecBuilder = new ClientVlanSpecBuilder();
//                             String value = String.valueOf(k);
//                             clientVlanSpecBuilder.setVlanId(value);
//                             clientVlanSpecBuilder.setVlanPriority(BigInteger.ONE);
//                             clientVlanSpecBuilder.setAccessAction(AccessAction.Exchange);
//                             clientVlanSpecBuilder.setVlanType(VlanType.CTag);
//                             //vlanSpac
//                             VlanSpecBuilder vlanSpecBuilder = new VlanSpecBuilder();
//                             vlanSpecBuilder.setVlanId(value);
//                             vlanSpecBuilder.setVlanPriority(BigInteger.ONE);
//                             vlanSpecBuilder.setAccessAction(AccessAction.Keep);
//                             vlanSpecBuilder.setVlanType(VlanType.STag);
//                             //ethPerformance
//                             PerformanceBuilder performanceBuilder = new PerformanceBuilder();
//                             performanceBuilder.setDelay(BigInteger.ONE);
//                             performanceBuilder.setFarPacketLossRate(new Real(BigDecimal.ONE));
//                             performanceBuilder.setNearPacketLossRate(new Real(BigDecimal.ONE));
//                             performanceBuilder.setRxBytes(BigInteger.ONE);
//                             performanceBuilder.setTxBytes(BigInteger.ONE);
//                             //ethOamStatePac
//                             OamStatePacBuilder oamStatePacBuilder = new OamStatePacBuilder();
//                             oamStatePacBuilder.setCcState(true);
//                             oamStatePacBuilder.setDmState(true);
//                             oamStatePacBuilder.setLmState(false);
//                             oamStatePacBuilder.setTmState(false);
//                             //ethOamConfig
//                             OamConfigBuilder oamConfigBuilder = new OamConfigBuilder();
//                             oamConfigBuilder.setMepId(1)
//                                     .setMegId("1")
//                                     .setRemoteMepId(2)
//                                     .setMel(Short.valueOf("1"))
//                                     .setMdName("mdName" + k)
//                                     .setLmInterval(OamTransmitInterval.Interval1min)
//                                     .setDmInterval(OamTransmitInterval.Interval3ms33)
//                                     .setCcInterval(OamTransmitInterval.Interval1min);
//                             //ethCtpPac
//                             ethCtpPacBuilder.setClientVlanSpec(clientVlanSpecBuilder.build())
//                                     .setVlanSpec(vlanSpecBuilder.build())
//                                     .setPerformance(performanceBuilder.build())
//                                     .setOamConfig(oamConfigBuilder.build())
//                                     .setOamStatePac(oamStatePacBuilder.build());
//                             ctp1Builder.setEthCtpPac(ethCtpPacBuilder.build());
//                             ctpBuilder.addAugmentation(Ctp1.class, ctp1Builder.build());
//                             if(j<3){
//                                 // stormSuppressions.add(getStormSuppression(name));
//                             }
//                             break;
//                         case 1:
//                             ctpBuilder.setLayerProtocolName(ODU.class);
//                             ctpBuilder.setPortRole(PortRole.LeafNode);
//                             ctpBuilder.setProtectRole(ProtectRole.Primary);
//                             ctpBuilder.setLoopBack(LoopbackType.TerminalLoopback);
//                             org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Ctp1Builder otnBuilder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Ctp1Builder();
//                             OduCtpPacBuilder oduCtpPacBuilder = new OduCtpPacBuilder();
// //                            oduCtpPacBuilder.setPmtrailTraceActualTx("323300000000000000000000000000");
// //                            oduCtpPacBuilder.setPmtrailTraceActualRx("323300000000000000000000000000");
// //                            oduCtpPacBuilder.setPmtrailTraceExpectedRx("323300000000000000000000000000");
//                             oduCtpPacBuilder.setOduSignalType(SignalType.ODU2);
//                             oduCtpPacBuilder.setAdaptationType(AdaptationType.ODUk);
//                             oduCtpPacBuilder.setSwitchCapability(OduSwitchType.ODUflexGFP);
//                             oduCtpPacBuilder.setTsDetail("8-c0");
//                             List<BigInteger> cur = new ArrayList<>();
//                             cur.add(BigInteger.valueOf(k));
//                             oduCtpPacBuilder.setCurrentNumberOfTributarySlots(cur);
//                             oduCtpPacBuilder.setGHaoStatus(GHaoStatus.BwrDec);
//                             otnBuilder.setOduCtpPac(oduCtpPacBuilder.build());
//                             ctpBuilder.addAugmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.Ctp1.class, otnBuilder.build());
//                             break;
//                         case 2:
//                             ctpBuilder.setLayerProtocolName(SDH.class);
//                             ctpBuilder.setPortRole(PortRole.Symmetric);
//                             ctpBuilder.setProtectRole(ProtectRole.Secondary);
//                             ctpBuilder.setLoopBack(LoopbackType.NonLoopback);
//                             VcCtpPacBuilder vcCtpPacBuilder = new VcCtpPacBuilder();
//                             vcCtpPacBuilder.setVcType(SdhSwitchType.VC12);
//                             vcCtpPacBuilder.setMappingPath("4-3-7-3");
//                             vcCtpPacBuilder.setJ1ActualTx("313233000000000000000000000000");
//                             vcCtpPacBuilder.setJ1ActualRx("313233000000000000000000000000");
//                             vcCtpPacBuilder.setJ1ExpectedRx("313233000000000000000000000000");
//                             org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ctp1Builder sdhBuilder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ctp1Builder();
//                             sdhBuilder.setVcCtpPac(vcCtpPacBuilder.build());
//                             ctpBuilder.addAugmentation(org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.sdh.rev240702.Ctp1.class, sdhBuilder.build());
//                             break;
//                         default:
//                             throw new IllegalStateException("Unexpected value: " + col);
//                     }
//                     list.add(ctpBuilder.build());
//                 }
//             }
//         }
//         return list;
//     }
//
//     private void alarms() {
//         AlarmsBuilder alarmsBuilder = new AlarmsBuilder().setAlarm(getAlarms());
//         InstanceIdentifier<Alarms> iid = InstanceIdentifier.create(Alarms.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, alarmsBuilder.build());
//     }
//
//     private void sfPersistencyTimer(){
//         SfPersistencyTimersBuilder builder = new SfPersistencyTimersBuilder().setSfPersistencyTimer(getSfPersistencyTimers());
//         InstanceIdentifier<SfPersistencyTimers> iid = InstanceIdentifier.create(SfPersistencyTimers.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, builder.build());
//     }
//
//     private void ethPtpBandwidth() {
//         EthPtpsBandWidthBuilder ethPtpsBandWidthBuilder = new EthPtpsBandWidthBuilder().setEthPtpBandWidth(getEthPtpBandwidths());
//         InstanceIdentifier<EthPtpsBandWidth> iid = InstanceIdentifier.create(EthPtpsBandWidth.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, ethPtpsBandWidthBuilder.build());
//     }
//
//     private List<SfPersistencyTimer> getSfPersistencyTimers(){
//         List<SfPersistencyTimer> list = new LinkedList<>();
//         for(int i=1;i<=4;i++){
//             SfPersistencyTimerBuilder builder = new SfPersistencyTimerBuilder();
//             builder.withKey(new SfPersistencyTimerKey(i));
//             builder.setPersistencyTime(i);
//             builder.setPgId(i);
//             list.add(builder.build());
//         }
//         return list;
//     }
//
//     private List<EthPtpBandWidth> getEthPtpBandwidths() {
//         List<EthPtpBandWidth> ethPtpBandWidths = new LinkedList<>();
//         for (int i = 1; i <= 2; i++) {
//             String name = "PTP=/shelf=1/slot=" + i + "/port=4";
//             EthPtpBandWidthBuilder ethPtpBandWidthBuilder = new EthPtpBandWidthBuilder();
//             ethPtpBandWidthBuilder.withKey(new EthPtpBandWidthKey(name));
//             ethPtpBandWidthBuilder.setName(name);
//             EgressCapacityBuilder egressCapacityBuilder = new EgressCapacityBuilder();
//             IngressCapacityBuilder ingressCapacityBuilder = new IngressCapacityBuilder();
//             ForCirBuilder bandWidth = new ForCirBuilder();
//             bandWidth.setPir(new BigInteger("100")).setCir(new BigInteger("100"));
//             bandWidth.setCbs(new BigInteger("100")).setPbs(new BigInteger("100"));
//             egressCapacityBuilder.setCirOrTotalsize(bandWidth.build());
//             ethPtpBandWidthBuilder.setEgressCapacity(egressCapacityBuilder.build());
//             ingressCapacityBuilder.setCirOrTotalsize(bandWidth.build());
//             ethPtpBandWidthBuilder.setIngressCapacity(ingressCapacityBuilder.build());
//             ethPtpBandWidths.add(ethPtpBandWidthBuilder.build());
//         }
//         return ethPtpBandWidths;
//     }
//
//     private void tacs() {
//         TcasBuilder tcasBuilder = new TcasBuilder().setTca(getTcas());
//         InstanceIdentifier<Tcas> iid = InstanceIdentifier.create(Tcas.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, tcasBuilder.build());
//     }
//
//     private void alarmMask() {
//         AlarmMaskStatesBuilder alarmMaskStatesBuilder = new AlarmMaskStatesBuilder().setAlarmMaskState(getAlarmMask());
//         InstanceIdentifier<AlarmMaskStates> iid = InstanceIdentifier.create(AlarmMaskStates.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, alarmMaskStatesBuilder.build());
//     }
//
//     private List<AlarmMaskState> getAlarmMask() {
//         List<AlarmMaskState> list = new LinkedList<>();
//         for (int i = 0; i < 10; i++) {
//             AlarmMaskStateBuilder alarmMaskStateBuilder = new AlarmMaskStateBuilder();
//             String name = "PTP=/shelf=1/slot=" + i + "/port=" + i * 13;
//             String code = "LOS";
//             alarmMaskStateBuilder.withKey(new AlarmMaskStateKey(code, name));
//             alarmMaskStateBuilder.setObjectName(name);
//             alarmMaskStateBuilder.setAlarmCode(code);
//             alarmMaskStateBuilder.setObjectType("PTP");
//             if (i % 2 == 0) {
//                 alarmMaskStateBuilder.setMaskState(true);
//             } else {
//                 alarmMaskStateBuilder.setMaskState(false);
//             }
//             list.add(alarmMaskStateBuilder.build());
//
//         }
//         return list;
//     }
//
//     private List<Tca> getTcas() {
//         List<Tca> tcas = new LinkedList<>();
//         for (int i = 0; i < 50; i++) {
//             TcaBuilder tcaBuilder = new TcaBuilder();
//             int key = 131313 + i;
//             tcaBuilder.withKey(new TcaKey(BigInteger.valueOf(key)));
//             tcaBuilder.setTcaSerialNo(BigInteger.valueOf(key));
//             tcaBuilder.setTcaState(AlarmState.Start);
//             tcaBuilder.setCurrentValue(new Real(new BigDecimal(10.11)));
//             TcaParameterBuilder tcaParameterBuilder = new TcaParameterBuilder();
//             tcaParameterBuilder.setObjectName("PTP=/shelf=1/slot=" + i + "/port=" + i * 12);
//             tcaParameterBuilder.setObjectType("PTP");
//             tcaParameterBuilder.setPmParameterName("TestPmParameter");
//             if (i % 3 == 0) {
//                 tcaParameterBuilder.setGranularity(Granularity._15min);
//             } else {
//                 tcaParameterBuilder.setGranularity(Granularity._24h);
//             }
//             tcaParameterBuilder.setThresholdValue(new Real(new BigDecimal(11.11)));
//             if (i % 2 == 0) {
//                 tcaBuilder.setStartTime(new DateAndTime("2020-06-17T19:10:06+08:00"));
//                 tcaBuilder.setEndTime(new DateAndTime("2020-07-18T19:10:06+08:00"));
//                 tcaParameterBuilder.setThresholdType(ThresholdType.High);
//             } else {
//                 tcaBuilder.setStartTime(new DateAndTime("2020-07-18T19:10:06+08:00"));
//                 tcaBuilder.setEndTime(new DateAndTime("2020-08-19T19:10:06+08:00"));
//                 tcaParameterBuilder.setThresholdType(ThresholdType.Low);
//             }
//             tcaBuilder.setTcaParameter(tcaParameterBuilder.build());
//             tcas.add(tcaBuilder.build());
//         }
//         return tcas;
//     }
//
//     private List<Alarm> getAlarms() {
//         List<Alarm> alarms = new LinkedList<>();
//         for (int i = 0; i < 50; i++) {
//             AlarmBuilder alarmBuilder = new AlarmBuilder();
//             alarmBuilder.setAlarmSerialNo(BigInteger.valueOf(i + 300380));
//             alarmBuilder.withKey(new AlarmKey(BigInteger.valueOf(i + 300380)));
//             String objectName = "";
//             if (i % 3 == 0) {
//                 objectName = "PTP=/shelf=1/slot=" + (i % 4 + 1) + "/port=" + i;
//                 alarmBuilder.setObjectType("PTP");
//             } else {
//                 objectName = "FTP=/shelf=1/slot=" + (i % 4 + 1) + "/port=" + i;
//                 alarmBuilder.setObjectType("FTP");
//             }
//             alarmBuilder.setObjectName(objectName);
//             alarmBuilder.setAlarmCode("LOS");
//             alarmBuilder.setAlarmState(AlarmState.Start);
//             if (i % 4 == 0) {
//                 alarmBuilder.setPerceivedSeverity(PerceivedSeverity.Critical);
//             } else {
//                 alarmBuilder.setPerceivedSeverity(PerceivedSeverity.Major);
//             }
//
//             if (i % 2 == 0) {
//                 alarmBuilder.setStartTime(new DateAndTime("2020-06-18T19:10:06+08:00"));
//                 alarmBuilder.setEndTime(new DateAndTime("2020-06-20T19:10:06+08:00"));
//             } else {
//                 alarmBuilder.setStartTime(new DateAndTime("2020-06-19T19:10:06+08:00"));
//             }
//             alarmBuilder.setAdditionalText("this is a test data");
//             alarms.add(alarmBuilder.build());
//         }
//         return alarms;
//     }
//
//     private void buildPgs() {
//         PgsBuilder pgsBuilder = new PgsBuilder();
//         pgsBuilder.setPg(getPgs());
//         InstanceIdentifier<Pgs> iid = InstanceIdentifier.create(Pgs.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, pgsBuilder.build());
//     }
//
//     private List<Pg> getPgs() {
//         List<Pg> pgs = new ArrayList<>(16);
//         for (int i = 1; i < 236; i++) {
//             PgBuilder pgBuilder = new PgBuilder();
//             pgBuilder.withKey(new PgKey(i));
//             pgBuilder.setPgId(i);
//             pgBuilder.setSwitchType(SwitchType.BiSwitch);
//             pgBuilder.setReversionMode(ReversionMode.NonRevertive);
//             pgBuilder.setProtectionDirection(ProtectionDirection.ToPrimary);
//             pgBuilder.setProtectionType(ProtectionType.Lag);
//             pgBuilder.setSwitchReason(SwitchReason.ManualSwitch);
//             pgBuilder.setHoldOff(30L);
//             pgBuilder.setWaitToRestoreTime(BigInteger.valueOf(300));
//             pgBuilder.setSdTrigger(SdTrigger.Enabled);
//             if (i == 1) {
//                 pgBuilder.setProtectionType(ProtectionType.Msp1Plus1);
//                 pgBuilder.setReversionMode(ReversionMode.Revertive);
//                 pgBuilder.setProtectionDirection(ProtectionDirection.ToSecondary);
//                 pgBuilder.setSwitchReason(SwitchReason.Cleared);
//                 pgBuilder.setWaitToRestoreTime(BigInteger.valueOf(200));
//                 pgBuilder.setHoldOff(12L);
//             } else if (i == 2) {
//                 pgBuilder.setProtectionType(ProtectionType.Och1Plus1);
//                 pgBuilder.setSwitchType(SwitchType.UniSwitch);
//                 pgBuilder.setSwitchReason(SwitchReason.ForceSwitch);
//                 pgBuilder.setTcm(3);
//                 pgBuilder.setHoldOff(15L);
//                 pgBuilder.setSdTrigger(SdTrigger.Disabled);
//             }else if(i % 11 == 0){
//                 pgBuilder.setProtectionType(ProtectionType.LagLoadBlancing);
//             } else if(i%7 == 0){
//                 pgBuilder.setProtectionType(ProtectionType.OduSncpI);
//             }else if(i%6 == 0){
//                 pgBuilder.setProtectionType(ProtectionType.OduSncpN);
//             } else if(i%3 == 0){
//                 pgBuilder.setProtectionType(ProtectionType.OduSncpS);
//             }else if(i%4 == 0){
//                 pgBuilder.setProtectionType(ProtectionType.VcSncp);
//             }
//             else if(i%5 == 0){
//                 pgBuilder.setProtectionType(ProtectionType.Msp1Plus1);
//             }
//             pgBuilder.setPrimaryPort("PTP=/shelf=1/slot=" + 1 + "/port=" + 1);
//             pgBuilder.setSecondaryPort("PTP=/shelf=1/slot=" + 1 + "/port=" + 2);
//             pgBuilder.setSelectedPort("PTP=/shelf=1/slot=" + 1 + "/port=" + 1);
//             pgs.add(pgBuilder.build());
//         }
//         return pgs;
//     }
//
//     private void performances() {
//         PerformancesBuilder performancesBuilder = new PerformancesBuilder();
//         performancesBuilder.setPerformance(getPerformances());
//         InstanceIdentifier<Performances> iid = InstanceIdentifier.create(Performances.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, performancesBuilder.build());
//     }
//
//     private List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev240702.performances.grouping.Performance> getPerformances() {
//         List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev240702.performances.grouping.Performance> performances = new ArrayList<>();
//         for (int i = 1; i < 6; i++) {
//             org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev240702.performances.grouping.PerformanceBuilder performanceBuilder =
//                     new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.performance.rev240702.performances.grouping.PerformanceBuilder();
//             Granularity granularity;
//             String objType = "PTP";
//             String objName = "PTP=/shelf=1/slot=1/port=" + i;
//             String pmParameter;
//             AnalogPmValueBuilder analogPmValueBuilder = new AnalogPmValueBuilder();
//             analogPmValueBuilder.setAverageValue(new Real(BigDecimal.valueOf(5.5)));
//             analogPmValueBuilder.setCurrentValue(new Real(BigDecimal.valueOf(5)));
//             analogPmValueBuilder.setMaxValue(new Real(BigDecimal.TEN));
//             analogPmValueBuilder.setMinValue(new Real(BigDecimal.ZERO));
//             AnalogBuilder analogBuilder = new AnalogBuilder().setAnalogPmValue(analogPmValueBuilder.build());
//             String startTimeStr = "2020-08-01T00:00:00+08:00";
//             for (int j = 0; j < 3; j++) {
//                 switch (j) {
//                     case 0:
//                         granularity = Granularity._15min;
//                         break;
//                     case 1: granularity = Granularity._24h;
//
//                         startTimeStr = "2020-08-0"+ (i%9+1) +"T00:" + (j * 15) + ":00+08:00";
//
//                         break;
//                     default:
//                         granularity = Granularity._15min;
//
//                         startTimeStr = "2020-08-0"+ (i%9+1) +"T00:"+ (j * 15) +":00+08:00";
//
//                         break;
//                 }
//                 for (int k = 0; k < 3; k++) {
//                     switch (k) {
//                         case 0:
//                             pmParameter = "FEC_CORRECT_ERROR";
//                             DigitalBuilder digitalBuilder = new DigitalBuilder().setDigitalPmValue(new Real(BigDecimal.ONE));
//                             performanceBuilder.setDigitalOrAnalog(digitalBuilder.build());
//                             break;
//                         case 1:
//                             pmParameter = "OPT_INPUTPOW";
//                             performanceBuilder.setDigitalOrAnalog(analogBuilder.build());
//                             break;
//                         default:
//                             pmParameter = "OPT_OUTPOW";
//                             performanceBuilder.setDigitalOrAnalog(analogBuilder.build());
//                             break;
//                     }
//                     DateAndTime startTime = new DateAndTime(startTimeStr);
//                     performanceBuilder.withKey(new PerformanceKey(granularity, objName, pmParameter, startTime));
//                     performanceBuilder.setGranularity(granularity);
//                     performanceBuilder.setObjectName(objName);
//                     performanceBuilder.setObjectType(objType);
//                     performances.add(performanceBuilder.build());
//                 }
//             }
//         }
//         return performances;
//     }
//
//     private void tcaParameters(){
//         TcaParametersBuilder tcaParametersBuilder = new TcaParametersBuilder();
//         tcaParametersBuilder.setTcaParameter(getTcaParameters());
//         InstanceIdentifier<TcaParameters> iid = InstanceIdentifier.create(TcaParameters.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid,tcaParametersBuilder.build());
//     }
//
//     private List<TcaParameter> getTcaParameters(){
//         List<TcaParameter> tcaParameterList = new ArrayList<>();
//         for (int i = 1; i < 11; i++) {
//             String objType = "PTP";
//             String objName =  "PTP=/shelf=1/slot=1/port="+i;
//             if(i%2==0){
//                 objType = "CTP";
//                 objName = "PTP=/shelf=1/slot=1/port="+i+";CTP="+i;
//             }
//             for (int j = 0; j < 3; j++) {
//                 Granularity granularity = Granularity._15min;
//                 switch (j) {
//                     case 0:
//                     case 1:
//                         granularity = Granularity._15min;
//                         break;
//                     case 2:
//                         granularity = Granularity._24h;
//                         break;
//                     default:
//                         break;
//                 }
//                 for (int k = 0; k < 3; k++) {
//                     String pmParameterName ="";
//                     ThresholdType thresholdType = ThresholdType.High;
//                     int thresholdValue = 0;
//                     switch (k) {
//                         case 0:
//                             pmParameterName = "FEC_CORRECT_ERROR";
//                             thresholdValue = 100;
//                             break;
//                         case 1:
//                             pmParameterName = "OPT_INPUTPOW";
//                             thresholdValue = -100;
//                             thresholdType = ThresholdType.Low;
//                             break;
//                         case 2:
//                             pmParameterName = "OPT_OUTPOW";
//                             thresholdType = ThresholdType.Low;
//                             thresholdValue = -50;
//                             break;
//                         default:
//                             break;
//                     }
//                     org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tca.parameters.TcaParameterBuilder
//                             tcaParameterBuilder = new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.tca.parameters.TcaParameterBuilder();
//                     TcaParameterKey tcaParameterKey = new TcaParameterKey(granularity,objName,pmParameterName,thresholdType);
//                     tcaParameterBuilder.withKey(tcaParameterKey).setGranularity(granularity)
//                             .setObjectName(objName)
//                             .setObjectType(objType)
//                             .setPmParameterName(pmParameterName)
//                             .setThresholdType(thresholdType)
//                             .setThresholdValue(new Real(BigDecimal.valueOf(thresholdValue)));
//                     tcaParameterList.add(tcaParameterBuilder.build());
//                 }
//             }
//         }
//         return tcaParameterList;
//     }
//
//
//     public void getOamEthConfig(){
//         OamEthConfigBuilder oamEthConfigBuilder=new OamEthConfigBuilder();
//         EthLmConfigsBuilder lmConfigsBuilder=new EthLmConfigsBuilder();
//
//         List<EthLmConfig> lmConfigs=new ArrayList<>();
//         int num=10;
//         for(int i=0;i<num;i++){
//             EthLmConfigBuilder ethLmConfigBuilder=new EthLmConfigBuilder();
//             ethLmConfigBuilder.setCtpName("test_lm_ctp"+i);
//             ethLmConfigBuilder.setDuration(i+2);
//             ethLmConfigBuilder.withKey(new EthLmConfigKey(String.valueOf(i)));
//             ethLmConfigBuilder.setName(String.valueOf(i));
//             ethLmConfigBuilder.setPriority((int)(Math.random()*7+1));
//             ethLmConfigBuilder.setTimes(i+30);
//             if(i%2==0) {
//                 ethLmConfigBuilder.setWorkingMode(EthLmWorkingMode.DualEnded);
//             }else{
//                 ethLmConfigBuilder.setWorkingMode(EthLmWorkingMode.SingleEnded);
//             }
//             lmConfigs.add(ethLmConfigBuilder.build());
//         }
//         lmConfigsBuilder.setEthLmConfig(lmConfigs);
//         EthDmConfigsBuilder dmConfigsBuilder=new EthDmConfigsBuilder();
//         List<EthDmConfig> dmConfigList=new ArrayList<>();
//         for(int i=0;i<num;i++){
//             EthDmConfigBuilder ethDmConfigBuilder=new EthDmConfigBuilder();
//             ethDmConfigBuilder.setCtpName("test_dm_ctp"+i);
//             ethDmConfigBuilder.setDuration(i+2);
//             ethDmConfigBuilder.withKey(new EthDmConfigKey(String.valueOf(i)));
//             ethDmConfigBuilder.setName(String.valueOf(i));
//             ethDmConfigBuilder.setPriority((int)(Math.random()*7+1));
//             ethDmConfigBuilder.setTimes(i+30);
//             dmConfigList.add(ethDmConfigBuilder.build());
//         }
//         dmConfigsBuilder.setEthDmConfig(dmConfigList);
//         oamEthConfigBuilder.setEthDmConfigs(dmConfigsBuilder.build());
//         oamEthConfigBuilder.setEthLmConfigs(lmConfigsBuilder.build());
//         InstanceIdentifier<OamEthConfig> iid = InstanceIdentifier.create(OamEthConfig.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, oamEthConfigBuilder.build());
//     }
//
//     public void vsis(){
//         List<Vsi> vsiList = new ArrayList<>();
//         for(int i=0; i<6; i++){
//             VsiBuilder vsiBuilder = new VsiBuilder();
//             String vsiName = "vsiName"+i;
//             vsiBuilder.setName(vsiName);
//             vsiBuilder.withKey(new VsiKey(vsiName));
//             vsiBuilder.setMtu(9216);
//             if(i%2 == 0){
//                 vsiBuilder.setServiceType(ServiceType.ELAN);
//                 vsiBuilder.setBroadcastProc(UnknownFrameProcess.Discard);
//                 vsiBuilder.setUnknownMulticastProc(UnknownFrameProcess.Transmit);
//                 vsiBuilder.setUnknownUnicastProc(UnknownFrameProcess.Discard);
//                 vsiBuilder.setMacAutoLearningCapacity(AdminState.Enabled);
//                 vsiBuilder.setMacLearningMode(LearningMode.IVL);
//             }
//             else{
//                 vsiBuilder.setServiceType(ServiceType.ETREE);
//                 vsiBuilder.setBroadcastProc(UnknownFrameProcess.Transmit);
//                 vsiBuilder.setUnknownMulticastProc(UnknownFrameProcess.Discard);
//                 vsiBuilder.setUnknownUnicastProc(UnknownFrameProcess.Transmit);
//                 vsiBuilder.setMacAutoLearningCapacity(AdminState.Disabled);
//                 vsiBuilder.setMacLearningMode(LearningMode.SVL);
//             }
//             vsiList.add(vsiBuilder.build());
//
//         }
//         VsisBuilder vsisBuilder = new VsisBuilder();
//         vsisBuilder.setVsi(vsiList);
//         InstanceIdentifier<Vsis> iid = InstanceIdentifier.create(Vsis.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, vsisBuilder.build());
//     }
//
//     public void connection(){
//         List<Connection> connectionList = new ArrayList<>();
//         for(int i=1;i<=20;i++){
//             ConnectionBuilder connectionBuilder = new ConnectionBuilder();
//             String connName = "connection"+i;
//             connectionBuilder.withKey(new ConnectionKey(connName));
//             connectionBuilder.setName(connName);
//             // connectionBuilder.setCtp(name);
//             connectionBuilder.setLabel("aaa");
//             connectionBuilder.setLayerProtocolName(ETH.class);
//             if(i % 5 == 0){
//                 connectionBuilder.setServiceType(ServiceType.EPL);
//             }
//             else if(i % 5 == 2){
//                 connectionBuilder.setServiceType(ServiceType.EVPL);
//             }
//             else{
//                 connectionBuilder.setServiceType(ServiceType.ODU);
//             }
//             org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connection.StatePacBuilder statePacBuilder =
//                     new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connection.StatePacBuilder();
//             statePacBuilder.setAdminState(AdminState.Enabled);
//             statePacBuilder.setOperationalState(OperationalState.Up);
//             connectionBuilder.setStatePac(statePacBuilder.build());
//
//             ForCirBuilder forEthOrEosBuilder = new ForCirBuilder();
//             forEthOrEosBuilder.setCbs(BigInteger.valueOf(111));
//             forEthOrEosBuilder.setCir(BigInteger.valueOf(222));
//             forEthOrEosBuilder.setPbs(BigInteger.valueOf(333));
//             forEthOrEosBuilder.setPir(BigInteger.valueOf(444));
//             RequestedCapacityBuilder requestedCapacityBuilder = new RequestedCapacityBuilder().setCirOrTotalsize(forEthOrEosBuilder.build());;
//             connectionBuilder.setRequestedCapacity(requestedCapacityBuilder.build());
//             org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connection.EgressCapacityBuilder egressCapacityBuilder =
//                     new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.connection.rev240702.connection.EgressCapacityBuilder();
//             egressCapacityBuilder.setCirOrTotalsize(new ForCirBuilder().setPir(BigInteger.valueOf(600)).setPbs(BigInteger.valueOf(700))
//                     .setCir(BigInteger.valueOf(600)).setCbs(BigInteger.valueOf(1000)).build());
//             connectionBuilder.setEgressCapacity(egressCapacityBuilder.build());
//             connectionList.add(connectionBuilder.build());
//         }
//         ConnectionsBuilder connectionsBuilder = new ConnectionsBuilder().setConnection(connectionList);
//         InstanceIdentifier<Connections> iid = InstanceIdentifier.create(Connections.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, connectionsBuilder.build());
//     }
//
//     public void EqPgTest(){
//         List<org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.eq.pgs.EqPg> eqPgs=new ArrayList<>();
//         for(int i=0;i<20;i++){
//             org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.eq.pgs.EqPgBuilder eqPgBuilder=new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.eq.pgs.EqPgBuilder();
//             eqPgBuilder.setPgId(i);
//             eqPgBuilder.withKey(new org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.eq.pgs.EqPgKey(i));
//             eqPgBuilder.setPrimaryEq("primarykey");
//             eqPgBuilder.setProtectionDirection(ProtectionDirection.ToPrimary);
//             eqPgBuilder.setProtectionType(ProtectionTypeEqPg.Control1Plus1);
//             eqPgBuilder.setSecondaryEq("secondaryEq");
//             eqPgBuilder.setSelectedEq("selectEq") ;
//             eqPgBuilder.setSwitchReason(SwitchReason.ManualSwitch);
//             eqPgs.add(eqPgBuilder.build());
//         }
//         EqPgsBuilder eqPgsBuilder=new EqPgsBuilder();
//         eqPgsBuilder.setEqPg(eqPgs);
//         InstanceIdentifier<EqPgs> iid = InstanceIdentifier.create(EqPgs.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, eqPgsBuilder.build());
//     }
//
//
//     private void devPg(){
//         List<EqPg> pgs = new ArrayList<>(16);
//         for (int i = 1; i < 10; i++) {
//             EqPgBuilder pgBuilder = new EqPgBuilder();
//             pgBuilder.withKey(new EqPgKey(i));
//             pgBuilder.setPgId(i);
//             pgBuilder.withKey(new EqPgKey(i));
//             pgBuilder.setProtectionType(ProtectionTypeEqPg.Control1Plus1);
//
//             if (i%2 == 0) {
//                 pgBuilder.setSwitchReason(SwitchReason.ManualSwitch);
//             } else {
//                 pgBuilder.setSwitchReason(SwitchReason.NoSwitch);
//             }
//             pgBuilder.setPrimaryEq("EH=/shelf=1/slot=1/EQ=1");
//             pgBuilder.setSecondaryEq("EH=/shelf=1/slot=3/EQ=3");
//             pgBuilder.setSelectedEq("EH=/shelf=1/slot=1/EQ=1");
//             pgs.add(pgBuilder.build());
//         }
//         EqPgsBuilder eqPgsBuilder = new EqPgsBuilder();
//         eqPgsBuilder.setEqPg(pgs);
//         InstanceIdentifier<EqPgs> iid = InstanceIdentifier.create(EqPgs.class);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, eqPgsBuilder.build());
//     }
//
//     public void GetEqs(){
//         PortLightsStateBuilder portLightsStateBuilder=new PortLightsStateBuilder();
//         List<org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eq.obj.Eq> eqs=new ArrayList<>();
//         for(int i=0;i<6;i++){
//             org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eq.obj.EqBuilder eq=new org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.eq.obj.EqBuilder();
//             switch (i){
//                 case 0:
//                     eq.setName("DC_02");
//                     eq.setSlot(12);
//                     eq.setLight(getLight(Light.Type.ACT,0));
//                     break;
//                 case 1:
//                     eq.setName("DC_02");
//                     eq.setSlot(13);
//                     break;
//                 case 2:
//                     eq.setName("SH2_02");
//                     eq.setSlot(1);
//                     eq.setLight(getLight(Light.Type.NORMAL,1));
//                     break;
//                 case 3:
//                     eq.setName("TXG_04");
//                     eq.setSlot(2);
//                     eq.setLight(getLight(Light.Type.NORMAL,2));
//                     break;
//                 case 4:
//                     eq.setName("USS_02");
//                     eq.setSlot(9);
//                     eq.setLight(getLight(Light.Type.RECEIVE,1));
//                     break;
//                 case 5:
//                     eq.setName("USS_02");
//                     eq.setSlot(10);
//                     eq.setLight(getLight(Light.Type.SEND,3));
//                     break;
//                 default:break;
//             }
//             eqs.add(eq.build());
//         }
//         InstanceIdentifier<PortLightsState> iid= InstanceIdentifier.create(PortLightsState.class);
//         portLightsStateBuilder.setEq(eqs);
//         MdsalUtil.doMergeToOperational(dataBroker, iid, portLightsStateBuilder.build());
//     }
//     private List<Light> getLight(Light.Type type, Integer port){
//         List<Light> lights=new ArrayList<>();
//         LightBuilder lightBuilder=new LightBuilder();
//         lightBuilder.setPort(port);
//         lightBuilder.setState(true);
//         lightBuilder.setType(type);
//         lights.add(lightBuilder.build());
//         return  lights;
//     }
    public GetOpticalModuleInfoOutput getGetOpticalModuleInfoOutput(String ptpName){
        GetOpticalModuleInfoOutputBuilder builder=new GetOpticalModuleInfoOutputBuilder();
        OpticalModuleInfosBuilder opticalModuleInfosBuilder=new OpticalModuleInfosBuilder();
        int num = 1;
        if(ptpName==null||"".equals(ptpName)){
            num = 200;
        }

        Map<OpticalModuleInfoKey, OpticalModuleInfo> values=new HashMap<>(num);
        for(int i=0;i<num;i++){
            org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.get.optical.module.info.output.optical.module.infos.OpticalModuleInfoBuilder opticalModuleInfoBuilder=new OpticalModuleInfoBuilder();
            if(ptpName!=null&&!"".equals(ptpName)){
                opticalModuleInfoBuilder.withKey(new OpticalModuleInfoKey(ptpName));
                opticalModuleInfoBuilder.setPtpName(ptpName);
            }else{
                opticalModuleInfoBuilder.withKey(new OpticalModuleInfoKey("PTP=/shelf=1/slot=1/port="+i));
                opticalModuleInfoBuilder.setPtpName(opticalModuleInfoBuilder.key().getPtpName());
            }

            opticalModuleInfoBuilder.setAdditional(String.valueOf(i));
            opticalModuleInfoBuilder.setCur(String.valueOf(i));
            opticalModuleInfoBuilder.setInputPow(String.valueOf(i));
            opticalModuleInfoBuilder.setOutputPow(String.valueOf(i));
            opticalModuleInfoBuilder.setPn(String.valueOf(i));

            opticalModuleInfoBuilder.setSn(String.valueOf(i));
            opticalModuleInfoBuilder.setTemperature(String.valueOf(i));
            opticalModuleInfoBuilder.setVendor(String.valueOf(i));
            opticalModuleInfoBuilder.setVer(String.valueOf(i));
            opticalModuleInfoBuilder.setVoltage(String.valueOf(i));
            values.put(opticalModuleInfoBuilder.key(),opticalModuleInfoBuilder.build());
        }
        opticalModuleInfosBuilder.setOpticalModuleInfo(values);

        builder.setOpticalModuleInfos(opticalModuleInfosBuilder.build());

        return builder.build();
    }
}
