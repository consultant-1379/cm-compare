#!/usr/bin/python
"""
Exposes utility queries.
Needs to be called within an open SSH session.
"""
import session_manager

QUERY_SUBNETWORKS = "cmedit get * SubNetwork"
QUERY_SUBNETWORK_NODES = "cmedit get SubNetwork=%s MeContext"
QUERY_UNSYNCED_NODES = "cmedit get * CmFunction.syncStatus!=SYNCHRONIZED"

FDN_LINE_PREFIX = "FDN : "
SUBNETWORK_FDN_PREFIX = FDN_LINE_PREFIX + "SubNetwork="


def query_subnetworks():
    """Query subnetworks"""
    subnetworks = []
    print "Querying SubNetworks"
    output = session_manager.exec_command(QUERY_SUBNETWORKS)
    for line in output:
        if not line.strip():
            continue
        elif SUBNETWORK_FDN_PREFIX in line:
            subnetwork = (line[len(SUBNETWORK_FDN_PREFIX):]).rstrip()
            subnetworks.append(subnetwork)
    print "%s subNetwork(s) found." % len(subnetworks)
    return subnetworks


def query_synced_nodes(subnetwork, unsynced_node_ids):
    """Query existing nodes under subnetwork"""
    nodes = []
    print "Querying nodes for SubNetwork %s" % subnetwork
    output = session_manager.exec_command(QUERY_SUBNETWORK_NODES % subnetwork)
    for line in output:
        if not line.strip():
            continue
        elif FDN_LINE_PREFIX in line:
            node = line[len(FDN_LINE_PREFIX):]
            if not any(str(unsynced_node_id) in node
                       for unsynced_node_id in unsynced_node_ids):
                nodes.append(node)
    print "%s synchronized node(s) found under SubNetwork %s" \
          % (len(nodes), subnetwork)
    return nodes


def query_unsynced_nodes():
    """Query for any unsynced nodes"""
    unsynced_node_ids = []
    output = session_manager.exec_command(QUERY_UNSYNCED_NODES)
    for line in output:
        if not line.strip():
            continue
        elif FDN_LINE_PREFIX in line:
            unsynced_node_ids.append(line[line.index('=')+1:line.index(',')])
    print "%s unsynced node(s) found" % len(unsynced_node_ids)
    return unsynced_node_ids
