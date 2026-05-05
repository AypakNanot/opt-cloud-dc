/*
 * Copyright © 2021 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.snmp.impl;

import org.opendaylight.mdsal.binding.api.DataBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author  liwenxue
 */
public class SnmpProvider {

    private static final Logger LOG = LoggerFactory.getLogger(SnmpProvider.class);

    private final DataBroker dataBroker;
    private SnmpTrapReceiver snmpTrapReceiver = null;

    public SnmpProvider(final DataBroker dataBroker) {
        this.dataBroker = dataBroker;
    }

    /**
     * Method called when the blueprint container is created.
     */
    public void init() {
        LOG.info("SnmpProvider Session Initiated");
        snmpTrapReceiver = new SnmpTrapReceiver();
        snmpTrapReceiver.run();
    }

    /**
     * Method called when the blueprint container is destroyed.
     */
    public void close() {
        if(snmpTrapReceiver != null){
            snmpTrapReceiver.close();
        }
        LOG.info("SnmpProvider Closed");
    }
}