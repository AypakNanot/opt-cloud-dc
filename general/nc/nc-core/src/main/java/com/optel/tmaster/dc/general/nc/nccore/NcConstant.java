/*
 * Copyright © 2019 optel and others.  All rights reserved.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License v1.0 which accompanies this distribution,
 * and is available at http://www.eclipse.org/legal/epl-v10.html
 */
package com.optel.tmaster.dc.general.nc.nccore;

import org.opendaylight.yang.gen.v1.urn.opendaylight.netconf.node.topology.rev150114.network.topology.topology.topology.types.TopologyNetconf;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NetworkTopology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.NodeId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.TopologyId;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.Topology;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.TopologyKey;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.Node;
import org.opendaylight.yang.gen.v1.urn.tbd.params.xml.ns.yang.network.topology.rev131021.network.topology.topology.NodeKey;
import org.opendaylight.yangtools.yang.binding.InstanceIdentifier;

/**
 * ClassName: NcConstant
 * <ul>
 * <li>(常量类)</li>
 * </ul>
 *
 * @author LWX 2019年9月24日下午3:31:13
 */
public class NcConstant {

    private NcConstant() {
    }

    /**
     * copy from org.opendaylight.netconf.console.utils.NetconfIidFactory
     */
    public static final InstanceIdentifier<Topology> NETCONF_TOPOLOGY_IID =
            InstanceIdentifier.builder(NetworkTopology.class)
                    .child(Topology.class, new TopologyKey(new TopologyId(TopologyNetconf.QNAME.getLocalName())))
                    .build();

    /**
     * copy from org.opendaylight.netconf.console.utils.NetconfIidFactory
     */
    public static InstanceIdentifier<Node> netconfNodeIid(final String nodeId) {
        return NETCONF_TOPOLOGY_IID.child(Node.class, new NodeKey(new NodeId(nodeId)));
    }

    public static InstanceIdentifier<Node> netconfNodeIid(final Node node) {
        return NETCONF_TOPOLOGY_IID.child(Node.class, node.key());
    }

    public static InstanceIdentifier<Node> netconfNodeIid() {
        return NETCONF_TOPOLOGY_IID.child(Node.class);
    }

}
