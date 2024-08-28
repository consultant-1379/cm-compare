#!/usr/bin/python

import sys, paramiko, datetime, argparse, subprocess

LITP_USER = "litp-admin"
DEFAULT_LITP_PASS = "12shroot"
ROOT_USER = "root"
DEFAULT_VM_PASS = "passw0rd"
DEFAULT_SVC_IPADDRESS = "192.168.0.202"
DB_SERVICE = "db1-service"
DEFAULT_CONTAINERS = "svc-1-impexpserv,svc-2-impexpserv,svc-1-cmserv,svc-2-cmserv"
DATE = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%YT%H:%M:%S_')
CONTAINER_LOGFILE = "/ericsson/3pp/jboss/standalone/log/server.log"
CONTAINER_LOGFILE_SVC = "/home/litp-admin/server.log"
OUTPUT_LOG_FILENAME = "fetch_container_logs.log"
OUTPUT_LOG_PATH = "/tmp/%s%s" % (DATE, OUTPUT_LOG_FILENAME)

SSH = paramiko.SSHClient()
SSH.set_missing_host_key_policy(paramiko.AutoAddPolicy())


def main():
    # Parse command line
    parser = argparse.ArgumentParser(description='Process command line args.')
    parser.add_argument('-svc', '--svc_server_ip', help='IP address of svc', required=False,
                        default=DEFAULT_SVC_IPADDRESS)
    parser.add_argument('-containers', '--container_clients', help='Comma separated list of containers', required=False,
                        default=DEFAULT_CONTAINERS)
    parser.add_argument('-logging', '--logging_enabled', help='generate separate log file', required=False,
                        action='store_true')
    parser.add_argument('-list', '--list_all_containers', help='list containers in deployment and exit', required=False,
                        action='store_true')
    parser.add_argument('-all_containers', '--all_container_logs',
                        help='switch for getting logs from all containers',
                        action='store_true',
                        required=False)
    args = parser.parse_args()
    default_svc_ipaddress = args.svc_server_ip
    containers = args.container_clients
    default_logging_enabled = args.logging_enabled
    list_containers = args.list_all_containers
    all_containers = args.all_container_logs

    start_remote_connection(default_svc_ipaddress, LITP_USER, DEFAULT_LITP_PASS)

    # Logging
    if default_logging_enabled:
        print "Output redirecting to %s......." % OUTPUT_LOG_PATH
        file_to_write = open(OUTPUT_LOG_PATH, 'w')
        sys.stdout = file_to_write

    if list_containers:
        get_container_list_and_exit(default_svc_ipaddress)

    if all_containers:
        containers = ""
        containers = get_container_hostnames()

    log_dir = create_root_log_dir()

    copy_all_container_logs_to_localhost(containers, log_dir)
    copy_db_logs_to_localhost(log_dir)

    message = "Finished gathering logs for the following containers ..."
    print_message(message)
    message = "%s,%s" % (DB_SERVICE, containers)
    print_message(message)
    message = "LOG FILES GATHERED RESIDE AT %s ..." % log_dir
    print_message(message)


def start_remote_connection(remote_ip, remote_user, remote_pass):
    SSH.connect(remote_ip, username=remote_user, password=remote_pass)
    log_date = get_formatted_datetime()
    print "%s Remote connection started to %s" % (log_date, remote_ip)


def close_remote_connection():
    log_date = get_formatted_datetime()
    print "%s Remote connection is closed" % log_date
    SSH.close()


def get_container_list_and_exit(svc_ipaddress):
    containers = get_container_hostnames()
    containers = "%s,%s" % (containers, DB_SERVICE)
    containers = containers.split(',')
    message = "List of containers on deployment %s ..." % svc_ipaddress
    print_message(message)
    for container in containers:
        print container
    sys.exit()


def create_root_log_dir():
    log_dir = "/tmp/container_logs_%s" % get_formatted_datetime()
    message = "Creating root directory for logs - %s " % log_dir
    print_message(message)
    out_bytes = subprocess.check_output(['mkdir', log_dir], stderr=subprocess.STDOUT)
    out_text = out_bytes.decode('utf-8')
    print out_text
    return log_dir


def copy_all_container_logs_to_localhost(containers, log_dir):
    container_list = containers.split(',')
    check_hostnames_exist(container_list)
    for container in container_list:
        copy_log_file_container_to_svc(container)
        copy_log_file_svc_to_localhost(log_dir, container)
        print_separator()


def get_container_hostnames():
    command = "cat /etc/hosts | grep 'svc-[0-9]*-'"
    _, stdout, _ = SSH.exec_command(command)
    output = stdout.readlines()
    iteration = 0
    container_list = ""
    for line in output:
        hostname = line.split()[1]
        if iteration == 0:
            container_list = hostname
        else:
            container_list += ",%s" % hostname
        iteration += 1
    return container_list


def cmd_copy_log_file_container_to_svc(hostname, user, server_pass, expecting):
    message = "Copying log from %s to SVC" % hostname
    print_message(message)
    _, _, _ = SSH.exec_command(cmd_enable_ssh_for_host(hostname))
    command = "scp -r -o StrictHostKeyChecking=no %s@%s:%s ." % (
        user, hostname, CONTAINER_LOGFILE)
    interact_command = "/usr/bin/expect -c  'spawn %s; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        command, expecting, server_pass)
    return interact_command


def copy_log_file_container_to_svc(hostname):
    _, _, _ = SSH.exec_command(cmd_enable_ssh_for_host(hostname))
    _, stdout, _ = SSH.exec_command(
        cmd_copy_log_file_container_to_svc(hostname, ROOT_USER, DEFAULT_VM_PASS, "password: "))
    output = stdout.read()
    print output


def copy_db_logs_to_localhost(log_dir):
    copy_db_log_file_container_to_svc(DB_SERVICE)
    copy_log_file_svc_to_localhost(log_dir, DB_SERVICE)
    print_separator()


def copy_db_log_file_container_to_svc(hostname):
    _, _, _ = SSH.exec_command(cmd_enable_ssh_for_host(hostname))
    _, stdout, _ = SSH.exec_command(
        cmd_copy_log_file_container_to_svc(hostname, LITP_USER, DEFAULT_LITP_PASS, "password: "))
    output = stdout.read()
    print output


def copy_log_file_svc_to_localhost(root_dir, hostname):
    log_full_path = root_dir + "/" + hostname
    message = "****** Copying file from SVC to %s ******" % log_full_path
    print_message(message)
    subprocess.check_output(['mkdir', log_full_path], stderr=subprocess.STDOUT)
    log_full_path += "/server.log"
    sftp = SSH.open_sftp()
    sftp.get(CONTAINER_LOGFILE_SVC, log_full_path)
    sftp.close()


def check_hostname_exists(hostname):
    command = "getent hosts | grep %s" % hostname
    _, stdout, _ = SSH.exec_command(command)
    output = stdout.readlines()
    exists = False
    for line in output:
        if hostname in line:
            exists = True
    return exists


def check_hostnames_exist(hostnames):
    for host in hostnames:
        if not check_hostname_exists(host):
            message = "%s - host does not exist - please specify a valid hostname - EXITING......." % host
            print_message(message)
            sys.exit()


def cmd_enable_ssh_for_host(hostname):
    message = "Generating host key for %s ..." % hostname
    print_message(message)
    return "ssh-keygen -R %s" % hostname


def get_formatted_datetime():
    log_date = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%Y_%H:%M:%S')
    return log_date


def print_message(message):
    print ""
    print "************      %s ...     **************" % message
    print ""


def print_separator():
    print ""
    print "***************************************************************************************************************"
    print ""


if __name__ == '__main__':
    main()
