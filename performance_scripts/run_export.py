#!/usr/bin/python

import sys, paramiko, datetime, time, argparse, re

ROOT = "root"
DEFAULT_ROOT_PASS = "12shroot"
DEFAULT_MS_IPADDRESS = "192.168.0.42"
DEFAULT_CLI_USERNAME = "administrator"
DEFAULT_CLI_PASSWORD = "TestPassw0rd"
CLI_USER_PROMPT = "Username: "
CLI_PASSWORD_PROMPT = "Password: "
DEFAULT_EXPORT_CONFIG = "Live"
DEFAULT_FILTER_NAMESPACE = ""
DEFAULT_FILTER_NAME = ""
DEFAULT_JOB_NAME = ""
DEFAULT_FILTER_VERSION = ""
# Check status of export every 30 seconds
CHECK_STATUS_INTERVAL = 30
DATE = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%YT%H:%M:%S_')
EXPORT_LOG_FILENAME = "export_status.log"
EXPORT_LOG_PATH = "/tmp/%s%s" % (DATE, EXPORT_LOG_FILENAME)

SSH = paramiko.SSHClient()
SSH.set_missing_host_key_policy(paramiko.AutoAddPolicy())


def main():
    # Parse command line
    parser = argparse.ArgumentParser(description='Process command line args.')
    parser.add_argument('-ms', '--ms_server_ip', help='IP address of ms-1', required=False,
                        default=DEFAULT_MS_IPADDRESS)
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
    parser.add_argument('-logging', '--logging_enabled', help='switch for separate log file', required=False,
                        action='store_true')
    args = parser.parse_args()
    default_ms_ipaddress = args.ms_server_ip
    default_export_config = args.config_name
    check_status_interval = args.interval
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

    # Start Export
    print "***** STARTING EXPORT *****"
    start_remote_connection(default_ms_ipaddress, ROOT, DEFAULT_ROOT_PASS)
    export_command = "cmedit export * -f=3GPP -c=%s %s %s" % (default_export_config, filter_string, jobname_string)
    job_id = run_export_command(export_command, CLI_USER_PROMPT, DEFAULT_CLI_USERNAME, CLI_PASSWORD_PROMPT,
                                DEFAULT_CLI_PASSWORD)
    if job_id == "-1":
        print "Problem starting export with CLI App"
        sys.exit()
    else:
        print "Started with job ID %s" % job_id
    exit_status = poll_for_export_finished_status(job_id, check_status_interval)
    log_date = get_formatted_datetime()
    print "%s Export of job %s finished with status %s " % (log_date, job_id, exit_status)
    close_remote_connection()
    print "***** EXPORT COMPLETED *****"


def start_remote_connection(remote_ip, remote_user, remote_pass):
    SSH.connect(remote_ip, username=remote_user, password=remote_pass)
    log_date = get_formatted_datetime()
    print "%s Remote connection started to %s" % (log_date, remote_ip)


def close_remote_connection():
    SSH.close()
    log_date = get_formatted_datetime()
    print "%s Remote connection is closed" % log_date


def get_formatted_datetime():
    log_date = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%Y %H:%M:%S')
    return log_date


def cmd_cli_command(command, user_expect, user, pwd_expect, pwd):
    return (
        "/usr/bin/expect -c 'spawn /opt/ericsson/enmutils/bin/cli_app \"%s\"; expect \"%s\"; send \"%s\r\n\"; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
            command, user_expect, user, pwd_expect, pwd))


def run_export_command(command, user_expect, user, pwd_expect, pwd):
    log_date = get_formatted_datetime()
    print "%s Starting export - %s" % (log_date, command)
    _, stdout, _ = SSH.exec_command(cmd_cli_command(command, user_expect, user, pwd_expect, pwd))
    output = stdout.readlines()
    export_job_id = "-1"
    for line in output:
        if "job ID" in line:
            export_job_id = line.split(' ')[7].strip()
            export_job_id = re.sub('[,]', '', export_job_id)
    return export_job_id


def run_export_status_command(command, user_expect, user, pwd_expect, pwd):
    _, stdout, _ = SSH.exec_command(cmd_cli_command(command, user_expect, user, pwd_expect, pwd))
    output = stdout.readlines()
    offset = 0
    data_line_offset = 0
    for line in output:
        if "Job ID" in line:
            data_line_offset = offset + 1
        elif "Suggested Solution" in line:
            log_date = get_formatted_datetime()
            print "%s Problem with Export Status Command" % log_date
            print line
            return "%s FAILED" % log_date
        offset = offset + 1
    export_data = output[data_line_offset].strip()
    status = export_data.split('|')[3].strip()
    return status


def poll_for_export_finished_status(export_job_id, check_status_interval):
    export_status_command = "cmedit export -s -j=%s" % export_job_id
    status = "STARTED"
    while status in ("STARTED", "STARTING"):
        time.sleep(check_status_interval)
        log_date = get_formatted_datetime()
        status = run_export_status_command(export_status_command, CLI_USER_PROMPT, DEFAULT_CLI_USERNAME,
                                           CLI_PASSWORD_PROMPT, DEFAULT_CLI_PASSWORD)
        print "%s Status of job %s is %s" % (log_date, export_job_id, status)
    return status


if __name__ == '__main__':
    main()
