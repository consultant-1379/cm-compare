#!/usr/bin/python
"""
Generates batch files used in activate performance tests:
add_mo_per_node.txt: batch file used to add 1 MO per node
delete_mo_per_node.txt: batch file used to delete 1 MO per node
change_mo_per_node.txt: batch file used to change 1 MO per node
"""
import sys
import argparse
import session_manager
import network_query
import re

NODE_FDN_PLACEHOLDER = "REPLACE_WITH_NODE_FDN"
NODE_ID_PLACEHOLDER = "REPLACE_WITH_NODE_ID"
ADD_MO_TEMPLATE = "cmedit create " + NODE_FDN_PLACEHOLDER + \
    ",ManagedElement=1,SwManagement=1,LoadModule=testId" + \
    " LoadModuleId=testId,loadModuleFilePath=path,productData=" + \
    "(productRevision=rev,productName=name,productInfo=info," + \
    "productNumber=123,productionDate=20150325) --config=%s \n"
ADD_MO_FILE_NAME = "/tmp/add_mo_per_node_%s.txt"
DELETE_MO_TEMPLATE = "cmedit delete " + NODE_FDN_PLACEHOLDER + \
    ",ManagedElement=1,SwManagement=1,LoadModule=testId --config=%s \n"
DELETE_MO_FILE_NAME = "/tmp/delete_mo_per_node_%s.txt"
CHANGE_MO_TEMPLATE = "cmedit set " + NODE_FDN_PLACEHOLDER + \
    ",ManagedElement=1,ENodeBFunction=1,EUtranCellFDD=" \
                     + NODE_ID_PLACEHOLDER + "-1 " + \
    "additionalPlmnReservedList=[false,true,false,true,false] --config=%s \n"
CHANGE_MO_FILE_NAME = "/tmp/change_mo_per_node_%s.txt"

MAX_LINES_PER_BATCH_FILE = 5000


def main():
    """Setup the program"""
    # Parse command line
    parser = argparse.ArgumentParser(description='Process command line args.')
    # Args
    parser.add_argument('-ms', '--ms_server_ip',
                        help='IP address of ms.', required=True)
    parser.add_argument('-c', '--config',
                        help='Config to be used in batch files.',
                        required=True)
    parser.add_argument('-l', '--max_lines',
                        help='Max lines in a batch file.',
                        default=MAX_LINES_PER_BATCH_FILE,
                        required=False)
    args = parser.parse_args()

    ms_ipaddress = args.ms_server_ip
    config = args.config
    max_lines = int(args.max_lines)
    all_nodes = []

    session_manager.start_session(ms_ipaddress)
    try:
        unsynced_node_ids = network_query.query_unsynced_nodes()
        subnetworks = network_query.query_subnetworks()
        if len(subnetworks) == 0:
            sys.exit()
        for subnetwork in subnetworks:
            nodes = network_query.query_synced_nodes(
                subnetwork, unsynced_node_ids)
            # help from adding duplicate nodes
            nodes_to_add = [node for node in nodes if not node in all_nodes]
            all_nodes += nodes_to_add
        populate_batch_files(all_nodes, config, max_lines)
    except KeyboardInterrupt:
        print "Exiting."
    finally:
        session_manager.close_session()
    print "Done."


def populate_batch_files(nodes, config, max_lines):
    """Writes batch files"""
    suffix = 1
    count = 0
    add_mo_file = open(ADD_MO_FILE_NAME % suffix, "w")
    delete_mo_file = open(DELETE_MO_FILE_NAME % suffix, "w")
    change_mo_file = open(CHANGE_MO_FILE_NAME % suffix, "w")
    try:
        for node in nodes:
            if count == max_lines:
                close_files(add_mo_file, delete_mo_file, change_mo_file)
                suffix += 1
                count = 0
                add_mo_file = open(ADD_MO_FILE_NAME % suffix, "w")
                delete_mo_file = open(DELETE_MO_FILE_NAME % suffix, "w")
                change_mo_file = open(CHANGE_MO_FILE_NAME % suffix, "w")
            write_to_add_mo_file(node, config, add_mo_file)
            write_to_delete_mo_file(node, config, delete_mo_file)
            write_to_change_mo_file(node, config, change_mo_file)
            count += 1
    finally:
        close_files(add_mo_file, delete_mo_file, change_mo_file)


def close_files(*batch_files):
    """Closes files"""
    for batch_file in batch_files:
        batch_file.close()


def write_to_add_mo_file(node, config, output_file):
    """Writes command line in batch file to add MO to the node"""
    new_line = ADD_MO_TEMPLATE
    output_file.write(
        new_line.replace(NODE_FDN_PLACEHOLDER, node.rstrip()) % config)


def write_to_delete_mo_file(node, config, output_file):
    """Writes command line in batch file to delete MO from the node"""
    new_line = DELETE_MO_TEMPLATE
    output_file.write(
        new_line.replace(NODE_FDN_PLACEHOLDER, node.rstrip()) % config)


def write_to_change_mo_file(node, config, output_file):
    """Writes command line in batch file to change MO in the node"""
    new_line = CHANGE_MO_TEMPLATE
    node_fdn = node.rstrip()
    node_id = str(re.search(r'(?<=MeContext=)\S+', node_fdn).group(0))
    output_file.write(
        new_line.replace(NODE_FDN_PLACEHOLDER, node_fdn).
        replace(NODE_ID_PLACEHOLDER, node_id) % config)

if __name__ == '__main__':
    main()
