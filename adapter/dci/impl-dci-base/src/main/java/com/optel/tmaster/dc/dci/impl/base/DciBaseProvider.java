/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.dci.impl.base;

import org.opendaylight.mdsal.binding.api.DataBroker;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * ClassName: DciBaseProvider
 * <ul>
 * <li>(blueprint初始化加载模块)</li>
 * </ul>
 * @author LWX 2022年2月8日上午11:14:29
 *
 */
public class DciBaseProvider {

    private static final Logger LOG = LoggerFactory.getLogger(DciBaseProvider.class);

    private final DataBroker dataBroker;
    
    public DciBaseProvider(final DataBroker dataBroker) {
        this.dataBroker = dataBroker;
    }

    /**
     * Method called when the blueprint container is created.
     */
    public void init() {
        LOG.info("DciBaseProvider Closed");
    }

    /**
     * Method called when the blueprint container is destroyed.
     */
    public void close() {
        LOG.info("DciBaseProvider Closed");
    }



}