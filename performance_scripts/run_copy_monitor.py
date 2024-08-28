#!/usr/bin/python

import sys, paramiko, datetime, argparse, re
import run_copy, run_export_monitor

# For copy script
DEFAULT_MS_IPADDRESS = "192.168.0.42"
DEFAULT_SOURCE_CONFIG = "Live"
DEFAULT_TARGET_CONFIG = ""
DEFAULT_NETWORK_ELEMENT = "*"
# Check status of copy every 30 seconds
CHECK_STATUS_INTERVAL = 30
# Defaults for database connections script
DEFAULT_SVC_IPADDRESS = "192.168.0.202"
DEFAULT_DB_IPADDRESS = "192.168.0.204"
DEFAULT_VERSANT_CLIENT_VM_LIST = "svc-1-cmserv,svc-2-cmserv"
# Logging
DATE = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%YT%H:%M:%S_')
COPY_LOG_FILENAME = "Copy_monitor.log"
COPY_LOG_PATH = "/tmp/%s%s" % (DATE, COPY_LOG_FILENAME)

SSH = paramiko.SSHClient()
SSH.set_missing_host_key_policy(paramiko.AutoAddPolicy())


def main():
    # Parse command line
    parser = argparse.ArgumentParser(description='Process command line args.')

    # Args for copy
    parser.add_argument('-ms', '--ms_server_ip', help='IP address of ms', required=False, default=DEFAULT_MS_IPADDRESS)
    parser.add_argument('-ne', '--network_element', help='NE to copy', required=False, default=DEFAULT_NETWORK_ELEMENT)
    parser.add_argument('-sc', '--source_config_name', help='Source configuration to copy', required=False,
                        default=DEFAULT_SOURCE_CONFIG)
    parser.add_argument('-tc', '--target_config_name', help='Target configuration for copy', required=False,
                        default=DEFAULT_TARGET_CONFIG)
    parser.add_argument('-i', '--interval', help='Interval for checking export status', required=False, type=float,
                        default=CHECK_STATUS_INTERVAL)
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
    check_status_interval = args.interval
    source_config = args.source_config_name
    target_config = args.target_config_name
    network_element = args.network_element
    ne_string = re.escape(network_element)
    # Assemble script command strings for checking Versant and Postgres connections
    svc_ipaddress = args.svc_server_ip
    db_ipaddress = args.db_service_ip
    versant_client_vm_list = args.versant_clients.split(',')
    default_logging_enabled = args.logging_enabled

    if default_logging_enabled:
        print "Output redirecting to %s......." % COPY_LOG_PATH
        file_to_write = open(COPY_LOG_PATH, 'w')
        sys.stdout = file_to_write

    # Start testing.........
    run_export_monitor.check_bandwidth_to_sfs(svc_ipaddress, versant_client_vm_list, "true")
    run_export_monitor.check_database_connections(svc_ipaddress, versant_client_vm_list, db_ipaddress)
    run_copy_command(ms_ipaddress, source_config, target_config, ne_string, check_status_interval)
    run_export_monitor.check_database_connections(svc_ipaddress, versant_client_vm_list, db_ipaddress)
    run_export_monitor.check_bandwidth_to_sfs(svc_ipaddress, versant_client_vm_list, "false")


def run_copy_command(ms_ipaddress, source_config, target_config, ne_string, check_status_interval):
    print "***** STARTING Copy *****"
    run_copy.start_remote_connection(ms_ipaddress, run_copy.ROOT, run_copy.DEFAULT_ROOT_PASS)
    run_copy.create_target_config(target_config, run_copy.CLI_USER_PROMPT, run_copy.DEFAULT_CLI_USERNAME,
                                  run_copy.CLI_PASSWORD_PROMPT,
                                  run_copy.DEFAULT_CLI_PASSWORD)
    copy_command = "config copy --ne %s --source %s --target %s" % (
        ne_string, source_config, target_config)
    job_id = run_copy.run_copy_command(copy_command, run_copy.CLI_USER_PROMPT, run_copy.DEFAULT_CLI_USERNAME,
                                       run_copy.CLI_PASSWORD_PROMPT,
                                       run_copy.DEFAULT_CLI_PASSWORD)
    if job_id == "-1":
        print "Problem starting copy with CLI App"
        sys.exit()
    else:
        print "Started with job ID %s" % job_id
    exit_status = run_copy.poll_for_copy_finished_status(job_id, check_status_interval)
    log_date = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%Y %H:%M:%S')
    print "%s Copy of job %s finished with status %s " % (log_date, job_id, exit_status)
    run_copy.close_remote_connection()
    print "***** Copy COMPLETED *****"


if __name__ == '__main__':
    main()
