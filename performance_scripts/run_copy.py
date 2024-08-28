#!/usr/bin/python

import sys, paramiko, datetime, time, argparse, re

ROOT = "root"
DEFAULT_ROOT_PASS = "12shroot"
DEFAULT_MS_IPADDRESS = "192.168.0.42"
DEFAULT_NETWORK_ELEMENT = "*"
DEFAULT_CLI_USERNAME = "administrator"
DEFAULT_CLI_PASSWORD = "TestPassw0rd"
CLI_USER_PROMPT = "Username: "
CLI_PASSWORD_PROMPT = "Password: "
DEFAULT_SOURCE_CONFIG = "Live"
DEFAULT_TARGET_CONFIG = ""
# Check status of Copy every 30 seconds
CHECK_STATUS_INTERVAL = 30
DATE = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%YT%H:%M:%S_')
COPY_LOG_FILENAME = "Copy_status.log"
COPY_LOG_PATH = "/tmp/%s%s" % (DATE, COPY_LOG_FILENAME)

SSH = paramiko.SSHClient()
SSH.set_missing_host_key_policy(paramiko.AutoAddPolicy())


def main():
    # Parse command line
    parser = argparse.ArgumentParser(description='Process command line args.')
    parser.add_argument('-ms', '--ms_server_ip', help='IP address of ms-1', required=False,
                        default=DEFAULT_MS_IPADDRESS)
    parser.add_argument('-ne', '--network_element', help='NE to copy in single or double quotes', required=False,
                        default=DEFAULT_NETWORK_ELEMENT)
    parser.add_argument('-sc', '--source_config_name', help='Source configuration to copy', required=False,
                        default=DEFAULT_SOURCE_CONFIG)
    parser.add_argument('-tc', '--target_config_name', help='Target configuration for copy', required=False,
                        default=DEFAULT_TARGET_CONFIG)
    parser.add_argument('-i', '--interval', help='Interval for checking Copy status', required=False, type=float,
                        default=CHECK_STATUS_INTERVAL)
    parser.add_argument('-logging', '--logging_enabled', help='switch for separate log file', required=False,
                        action='store_true')
    args = parser.parse_args()
    ms_ipaddress = args.ms_server_ip
    network_element = args.network_element
    source_config = args.source_config_name
    target_config = args.target_config_name
    check_status_interval = args.interval
    logging_enabled = args.logging_enabled

    if logging_enabled:
        print "Output redirecting to %s......." % COPY_LOG_PATH
        file_to_write = open(COPY_LOG_PATH, 'w')
        sys.stdout = file_to_write

    # Start Copy
    print "***** STARTING Copy *****"
    start_remote_connection(ms_ipaddress, ROOT, DEFAULT_ROOT_PASS)
    create_target_config(target_config, CLI_USER_PROMPT, DEFAULT_CLI_USERNAME, CLI_PASSWORD_PROMPT,
                         DEFAULT_CLI_PASSWORD)
    copy_command = "config copy --ne %s --source %s --target %s" % (
        network_element, source_config, target_config)
    job_id = run_copy_command(copy_command, CLI_USER_PROMPT, DEFAULT_CLI_USERNAME, CLI_PASSWORD_PROMPT,
                              DEFAULT_CLI_PASSWORD)
    if job_id == "-1":
        print "Problem starting copy with CLI App"
        sys.exit()
    else:
        print "Started with job ID %s" % job_id
    exit_status = poll_for_copy_finished_status(job_id, check_status_interval)
    log_date = get_formatted_datetime()
    print "%s Copy of job %s finished with status %s " % (log_date, job_id, exit_status)
    close_remote_connection()
    print "***** Copy COMPLETED *****"


def start_remote_connection(remote_ip, remote_user, remote_pass):
    SSH.connect(remote_ip, username=remote_user, password=remote_pass)
    log_date = get_formatted_datetime()
    print "%s Remote connection started to %s" % (log_date, remote_ip)


def close_remote_connection():
    SSH.close()
    log_date = get_formatted_datetime()
    print "%s Remote connection is closed" % log_date


def cmd_cli_command(command, user_expect, user, pwd_expect, pwd):
    return (
        "/usr/bin/expect -c 'spawn /opt/ericsson/enmutils/bin/cli_app \"%s\"; expect \"%s\"; send \"%s\r\n\"; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
            command, user_expect, user, pwd_expect, pwd))


def create_target_config(target_config, user_expect, user, pwd_expect, pwd):
    log_date = get_formatted_datetime()
    print "%s Creating config - %s" % (log_date, target_config)
    command = "config create %s" % target_config
    _, stdout, _ = SSH.exec_command(cmd_cli_command(command, user_expect, user, pwd_expect, pwd))
    output = stdout.readlines()
    print output


def run_copy_command(command, user_expect, user, pwd_expect, pwd):
    log_date = get_formatted_datetime()
    print "%s Starting Copy - %s" % (log_date, command)
    _, stdout, _ = SSH.exec_command(cmd_cli_command(command, user_expect, user, pwd_expect, pwd))
    output = stdout.readlines()
    copy_job_id = "-1"
    for line in output:
        if "job ID" in line:
            copy_job_id = line.split(' ')[6].strip()
            copy_job_id = re.sub('[,]', '', copy_job_id)
    return copy_job_id


def run_copy_status_command(command, user_expect, user, pwd_expect, pwd):
    _, stdout, _ = SSH.exec_command(cmd_cli_command(command, user_expect, user, pwd_expect, pwd))
    output = stdout.readlines()
    offset = 0
    data_line_offset = 0
    for line in output:
        if "Job ID" in line:
            data_line_offset = offset + 1
        elif "Suggested Solution" in line:
            log_date = get_formatted_datetime()
            print "%s Problem with Copy Status Command" % log_date
            print line
            return "%s FAILED" % log_date
        offset += 1
    copy_data = output[data_line_offset].strip()
    status = copy_data.split('|')[2].strip()
    return status


def poll_for_copy_finished_status(copy_job_id, check_status_interval):
    copy_status_command = "config copy -st -j %s" % copy_job_id
    status = "STARTED"
    while status in ("STARTED", "STARTING"):
        time.sleep(check_status_interval)
        log_date = get_formatted_datetime()
        status = run_copy_status_command(copy_status_command, CLI_USER_PROMPT, DEFAULT_CLI_USERNAME,
                                         CLI_PASSWORD_PROMPT,
                                         DEFAULT_CLI_PASSWORD)
        print "%s Status of job %s is %s" % (log_date, copy_job_id, status)
    return status


def get_formatted_datetime():
    log_date = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%Y %H:%M:%S')
    return log_date


if __name__ == '__main__':
    main()
