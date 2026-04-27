/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.nc.nccore.listener;

import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.opendaylight.yangtools.yang.binding.RpcService;

/**
 * dc-aggregator - NetconfNodeService
 * netconf连接操作
 * date       time        author
 * ─────────────────────────────
 * 2021/9/29   17:48      liwenxue
 * Copyright (c) 2021, H-OPTEL All Rights Reserved.
 *
 * @author liwenxue
 * @version V1.0.0
 */
public interface NetconfNodeService extends RpcService {

    /**
     * 连接成功后需要操作
     *
     * @param nodeData 连接成功的node
     */
    void netconfConnected(Node nodeData);

    /**
     * 断开连接后需要操作
     *
     * @param nodeData 断开连接的node数据
     */
    void netconfDisconnected(Node nodeData);

}
