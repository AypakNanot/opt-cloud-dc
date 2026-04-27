/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.device.impl.otn.ctc.notification;

import org.opendaylight.yangtools.yang.binding.YangModuleInfo;

/**
 * <ul>
 * <li>(通知类常量)</li>
 * </ul>
 *
 * @author LWX 2020/5/4 14:27
 */
public class NotificationConstant {

    /**
     * acc-devm
     */
    private static YangModuleInfo ACC_DEVM_YANG = org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.devm.rev240702.$YangModuleInfoImpl.getInstance();
    public final static String ACC_DEVM__CAPABILITY =  ACC_DEVM_YANG.getName().intern().toString();
    /**
     * acc-notifications
     */
    private static YangModuleInfo ACC_NOTIFICATION_YANG = org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.notifications.rev210924.$YangModuleInfoImpl.getInstance();
    public final static String ACC_NOTIFICATION_CAPABILITY = ACC_NOTIFICATION_YANG.getName().intern().toString();
    /**
     * acc-protection-group
     */
    private static YangModuleInfo ACC_PROTECTION_GROUP_YANG = org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.protection.group.rev240702.$YangModuleInfoImpl.getInstance();
    public final static String ACC_PROTECTION_GROUP_CAPABILITY =  ACC_PROTECTION_GROUP_YANG.getName().intern().toString();
    /**
     * acc-otn
     */
    private static YangModuleInfo ACC_OTN_YANG = org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.otn.rev240702.$YangModuleInfoImpl.getInstance();
    public final static String ACC_OTN_YANG_CAPABILITY = ACC_OTN_YANG.getName().intern().toString();
    /**
     * acc-alarm
     */
    private static YangModuleInfo ACC_ALARM_YANG = org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.alarms.rev240702.$YangModuleInfoImpl.getInstance();
    public final static String ACC_ALARM_CAPABILITY = ACC_ALARM_YANG.getName().intern().toString();

    /**
     * opt-otn-extension
     */
    private static YangModuleInfo OPT_OTN_EXTENSION = org.opendaylight.yang.gen.v1.com.optel.yang.otn.extension.rev210329.$YangModuleInfoImpl.getInstance();
    public final static String OPT_OTN_EXTENSION_CAPABILITY = OPT_OTN_EXTENSION.getName().intern().toString();

    private static YangModuleInfo ACC_OSU=org.opendaylight.yang.gen.v1.urn.ccsa.yang.acc.osu.rev240702.$YangModuleInfoImpl.getInstance();
    public final static String ACC_OSU_CAPABILITY = ACC_OSU.getName().intern().toString();
}
