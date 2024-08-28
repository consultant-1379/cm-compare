#!/usr/bin/python

import sys, paramiko, datetime, argparse
import run_export, check_network_bandwidth_sfs, all_db_connections

# For export script
DEFAULT_MS_IPADDRESS = "192.168.0.42"
DEFAULT_EXPORT_CONFIG = "Live"
DEFAULT_FILTER_NAMESPACE = ""
DEFAULT_FILTER_NAME = ""
DEFAULT_FILTER_VERSION = ""
FILTER_STRING = ""
DEFAULT_JOB_NAME = ""
EXPORT_FILTERED_SCRIPT_COMMAND = ""
# Check status of export every 30 seconds
CHECK_STATUS_INTERVAL = 30

# Defaults for database connections script
DEFAULT_SVC_IPADDRESS = "192.168.0.202"
DEFAULT_DB_IPADDRESS = "192.168.0.204"
DEFAULT_VERSANT_CLIENT_VM_LIST = "svc-1-impexpserv,svc-2-impexpserv"
# Logging
DATE = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%YT%H:%M:%S_')
EXPORT_LOG_FILENAME = "Copy_monitor.log"
EXPORT_LOG_PATH = "/tmp/%s%s" % (DATE, EXPORT_LOG_FILENAME)

SSH = paramiko.SSHClient()
SSH.set_missing_host_key_policy(paramiko.AutoAddPolicy())


def main():
    # Parse command line
    parser = argparse.ArgumentParser(description='Process command line args.')

    # Args for export/filtered export
    parser.add_argument('-ms', '--ms_server_ip', help='IP address of ms', required=False, default=DEFAULT_MS_IPADDRESS)
    parser.add_argument('-c', '--config_name', help='Configuration to export', required=False,
                        default=DEFAULT_EXPORT_CONFIG)
    parser.add_argument('-i', '--interval', help='Interval for checking export status', required=False, type=float,
                        default=CHECK_STATUS_INTERVAL)
    parser.add_argument('-fns', '--filternamespace', help='Export filter namespace', required=False,
                        default=DEFAULT_FILTER_NAMESPACE)
    parser.add_argument('-fn', '--filtername', help='Export filter name', required=False, default=DEFAULT_FILTER_NAME)
    parser.add_argument('-fv', '--filterversion', help='Export filter version', required=False,
                        default=DEFAULT_FILTER_VERSION)
    parser.add_argument('-jn', '--jobname', help='Export job name to set', required=False, default=DEFAULT_JOB_NAME)
    # Args for database connections
    parser.add_argument('-svc', '--svc_server_ip', help='IP address of svc', required=False,
                        default=DEFAULT_SVC_IPADDRESS)
    parser.add_argument('-db', '--db_service_ip', help='IP address of db service', required=False,
                        default=DEFAULT_DB_IPADDRESS)
    parser.add_argument('-vc', '--versant_clients', help='Comma separated list of Versant VM clients', required=False,
                        default=DEFAULT_VERSANT_CLIENT_VM_LIST)
    parser.add_argument('-logging', '--logging_enabled', help='switch for separate log file', required=False,
                        action='store_true')

    args = parser.parse_args()

    # Assemble script command strings for export/filtered export
    ms_ipaddress = args.ms_server_ip
    export_config = args.config_name
    check_status_interval = args.interval

    # Versant and Postgres connections
    svc_ipaddress = args.svc_server_ip
    db_ipaddress = args.db_service_ip
    versant_client_vm_list = args.versant_clients.split(',')
    default_logging_enabled = args.logging_enabled

    if default_logging_enabled:
        print "Output redirecting to %s......." % EXPORT_LOG_PATH
        file_to_write = open(EXPORT_LOG_PATH, 'w')
        sys.stdout = file_to_write

    filter_string = ""

    if args.filtername != "":
        default_filter_namespace = args.filternamespace
        default_filter_name = args.filtername
        default_filter_version = args.filterversion
        filter_string = "-fns %s -fn %s -fv %s" % (
            default_filter_namespace, default_filter_name, default_filter_version)

    jobname_string = ""
    if args.jobname != "":
        jobname_string = "-jn=%s" %args.jobname

    # Start testing.........
    check_bandwidth_to_sfs(svc_ipaddress, versant_client_vm_list, "true")
    check_database_connections(svc_ipaddress, versant_client_vm_list, db_ipaddress)
    run_export_command(ms_ipaddress, export_config, filter_string, jobname_string, check_status_interval)
    check_database_connections(svc_ipaddress, versant_client_vm_list, db_ipaddress)
    check_bandwidth_to_sfs(svc_ipaddress, versant_client_vm_list, "false")


def check_bandwidth_to_sfs(svc_ipaddress, versant_client_vm_list, install):
    check_network_bandwidth_sfs.start_remote_connection(svc_ipaddress, check_network_bandwidth_sfs.LITP_USER,
                                                        check_network_bandwidth_sfs.DEFAULT_LITP_PASS)
    check_network_bandwidth_sfs.check_hostnames_exist(versant_client_vm_list)
    check_network_bandwidth_sfs.get_primary_ip_of_export_vms(versant_client_vm_list)
    sfs_server_ip = check_network_bandwidth_sfs.get_ip_for_sfs_server()
    if install == "true":
        check_network_bandwidth_sfs.install_iperf3_rpm_on_svc()
        check_network_bandwidth_sfs.copy_iperf3_files_to_vms(versant_client_vm_list)
        check_network_bandwidth_sfs.copy_iperf3_files_to_sfs_server(sfs_server_ip)
    check_network_bandwidth_sfs.start_iperf3_server_on_clients(versant_client_vm_list)
    check_network_bandwidth_sfs.run_iperf3_from_sfs_server_to_clients(versant_client_vm_list, sfs_server_ip)
    check_network_bandwidth_sfs.kill_iperf3_server_on_clients(versant_client_vm_list)
    check_network_bandwidth_sfs.close_remote_connection()


def check_database_connections(svc_ipaddress, versant_client_vm_list, db_ipaddress):
    all_db_connections.start_remote_connection(svc_ipaddress, all_db_connections.LITP_USER,
                                               all_db_connections.DEFAULT_LITP_PASS)
    all_db_connections.check_hostnames_exist(versant_client_vm_list)
    all_db_connections.display_versant_clientside_details(versant_client_vm_list)
    all_db_connections.close_remote_connection()
    all_db_connections.start_remote_connection(svc_ipaddress, all_db_connections.LITP_USER,
                                               all_db_connections.DEFAULT_LITP_PASS)
    all_db_connections.check_serverside_connections(versant_client_vm_list, db_ipaddress)
    all_db_connections.close_remote_connection()


def run_export_command(ms_ipaddress, export_config, filter_string, jobname_string, check_status_interval):
    print "***** STARTING EXPORT *****"
    run_export.start_remote_connection(ms_ipaddress, run_export.ROOT, run_export.DEFAULT_ROOT_PASS)
    export_command = "cmedit export * -f=3GPP -c=%s %s %s" % (export_config, filter_string, jobname_string)
    job_id = run_export.run_export_command(export_command, run_export.CLI_USER_PROMPT, run_export.DEFAULT_CLI_USERNAME,
                                           run_export.CLI_PASSWORD_PROMPT,
                                           run_export.DEFAULT_CLI_PASSWORD)
    if job_id == "-1":
        print "Problem starting export with CLI App"
        sys.exit()
    else:
        print "Started with job ID %s" % job_id
    exit_status = run_export.poll_for_export_finished_status(job_id, check_status_interval)
    log_date = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%Y %H:%M:%S')
    print "%s Export of job %s finished with status %s " % (log_date, job_id, exit_status)
    run_export.close_remote_connection()
    print "***** EXPORT COMPLETED *****"


if __name__ == '__main__':
    main()
