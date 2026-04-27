/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.nc.nccore;

import com.optel.tmaster.dc.general.base.util.DcFileUtil;
import com.optel.tmaster.dc.general.nc.nccore.listener.NetConfNodeListener;
import org.opendaylight.mdsal.binding.api.DataBroker;
import org.opendaylight.mdsal.binding.api.DataTreeIdentifier;
import org.opendaylight.mdsal.binding.api.MountPointService;
import org.opendaylight.mdsal.common.api.LogicalDatastoreType;
import org.opendaylight.netconf.console.api.NetconfCommands;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.opendaylight.yangtools.concepts.ListenerRegistration;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 
 * ClassName: NcProvider
 * <ul>
 * <li>(blueprint 初始化加载类)</li>
 * </ul>
 * @author LWX 2019年9月24日下午3:31:26
 *
 */
public class NcProvider {

    private static final Logger LOG = LoggerFactory.getLogger(NcProvider.class);

    private final DataBroker dataBroker;
    private final MountPointService mountService;
    private final NetconfCommands netconfCommands;
    private NetConfNodeListener netConfNodeListener = null;
    private ListenerRegistration<NetConfNodeListener> netconfNodeRegistration = null;

    public NcProvider(final DataBroker dataBroker, MountPointService mountService, NetconfCommands netconfCommands) {
        this.dataBroker = dataBroker;
        this.mountService = mountService;
        this.netconfCommands = netconfCommands;
    }

    /**
     * Method called when the blueprint container is created.
     */
    public void init() {
        MountTools.init(mountService);
        NcTools.init(dataBroker);
        //声明node的path路径
        final InstanceIdentifier<Node> path = NcConstant.netconfNodeIid();
        //将OPERATIONAL的node数据变化的监听器(NetConfNodeListener)注册到dataBroker
        netConfNodeListener = new NetConfNodeListener(dataBroker, netconfCommands);
        netconfNodeRegistration = dataBroker.registerDataTreeChangeListener(
                DataTreeIdentifier.create(LogicalDatastoreType.OPERATIONAL, path), netConfNodeListener);
        // 删除临时文件夹
        DcFileUtil.deleteCacheTemp(null);

        LOG.info("NcProvider Session Initiated");
    }

    /**
     * Method called when the blueprint container is destroyed.
     */
    public void close() {
        if(netconfNodeRegistration != null){
            netconfNodeRegistration.close();
        }
        if(netConfNodeListener != null){
            netConfNodeListener.close();
        }
        LOG.info("NcProvider Closed");
    }
}