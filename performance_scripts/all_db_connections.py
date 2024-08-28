#!/usr/bin/python

import sys, paramiko, datetime, argparse

LITP_USER = "litp-admin"
DEFAULT_LITP_PASS = "12shroot"
ROOT_USER = "root"
DEFAULT_VM_PASS = "passw0rd"
DEFAULT_SVC_IPADDRESS = "192.168.0.202"
DEFAULT_DB_IPADDRESS = "192.168.0.204"
CONNECTIONS_LOG_FILENAME = "export_connections.log"
DATE = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%YT%H:%M:%S_')
EXPORT_CONNECTIONS_LOG_PATH = "/tmp/%s%s" % (DATE, CONNECTIONS_LOG_FILENAME)
DEFAULT_VERSANT_CLIENT_VM_LIST = "svc-1-impexpserv,svc-2-impexpserv"
DB_NAME = "exportds"
DB_CONNECTIONS_LOG_FILENAME = "all_db_connections.log"
DB_CONNECTIONS_LOG_PATH = "/tmp/%s%s" % (DATE, DB_CONNECTIONS_LOG_FILENAME)

SSH = paramiko.SSHClient()
SSH.set_missing_host_key_policy(paramiko.AutoAddPolicy())

# Module level variable
ip_addresses = {}


def main():
    parser = argparse.ArgumentParser(description='Process command line args.')
    parser.add_argument('-svc', '--svc_server_ip', help='IP address of svc', required=False,
                        default=DEFAULT_SVC_IPADDRESS)
    parser.add_argument('-db', '--db_service_ip', help='IP address of db service', required=False,
                        default=DEFAULT_DB_IPADDRESS)
    parser.add_argument('-vc', '--versant_clients', help='Comma separated list of Versant VM clients', required=False,
                        default=DEFAULT_VERSANT_CLIENT_VM_LIST)
    parser.add_argument('-datasource', '--postgres_datasource', help='Postgres datasource to monitor', required=False,
                        default=DB_NAME)
    parser.add_argument('-logging', '--logging_enabled', help='switch for separate log file', required=False,
                        action='store_true')
    args = parser.parse_args()
    svc_ipaddress = args.svc_server_ip
    db_ipaddress = args.db_service_ip
    versant_connected_clients = args.versant_clients.split(',')
    postgres_ds = args.postgres_datasource
    default_logging_enabled = args.logging_enabled

    # Logging
    if default_logging_enabled:
        print "Output redirecting to %s......." % DB_CONNECTIONS_LOG_PATH
        file_to_write = open(DB_CONNECTIONS_LOG_PATH, 'w')
        sys.stdout = file_to_write

    # Get Versant and Postgres connections
    start_remote_connection(svc_ipaddress, LITP_USER, DEFAULT_LITP_PASS)
    check_hostnames_exist(versant_connected_clients)
    display_versant_clientside_details(versant_connected_clients)
    close_remote_connection()
    start_remote_connection(svc_ipaddress, LITP_USER, DEFAULT_LITP_PASS)
    check_serverside_connections(versant_connected_clients, db_ipaddress)
    close_remote_connection()


def start_remote_connection(remote_ip, remote_user, remote_pass):
    SSH.connect(remote_ip, username=remote_user, password=remote_pass)
    log_date = get_formatted_datetime()
    print "%s Remote connection started to %s" % (log_date, remote_ip)


def close_remote_connection():
    log_date = get_formatted_datetime()
    print "%s Remote connection is closed" % log_date
    SSH.close()


def display_command_result(stdout, stderr, message, result_lines):
    exit_code = stdout.channel.recv_exit_status()
    log_date = get_formatted_datetime()
    if exit_code == 0:
        output = stdout.readlines()[result_lines].strip()
        print "%s %s%s" % (log_date, message, output)
    else:
        print"%s %s - Command failure" % (log_date, message)
        output = stderr.read()
        print output


def cmd_get_host_ipaddress(hostname, server_pass, expecting):
    return "/usr/bin/expect -c 'spawn ssh -o StrictHostKeyChecking=no %s@%s hostname --ip-address; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        ROOT_USER, hostname, expecting, server_pass)


def cmd_get_clientside_versant_connections(hostname, server_pass, expecting):
    return "/usr/bin/expect -c 'spawn ssh -o StrictHostKeyChecking=no %s@%s lsof -n -P -i TCP:5019-5021 | grep java | wc -l; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        ROOT_USER, hostname, expecting, server_pass)


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
            print "%s - host does not exist - please specify a valid hostname - EXITING......." % host
            sys.exit()


def cmd_enable_ssh_for_host(hostname):
    return "ssh-keygen -R %s" % hostname


def cmd_get_clientside_postgres_connections(host_ip, db_ipaddress, postgres_ds):
    host_ip_string = "\\'%s\\'" % host_ip
    postgres_command = '''/opt/rh/postgresql92/%s/usr/bin/psql -U export_admin --host=postgresql01 -d %s -p 5432 -c \\"SELECT count(*) FROM pg_stat_activity where client_addr='%s'\\"''' % (
        ROOT_USER, postgres_ds, host_ip_string)
    interact_command = "/usr/bin/expect -c  'spawn ssh -o StrictHostKeyChecking=no %s@%s %s; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        LITP_USER, db_ipaddress, postgres_command, "password", DEFAULT_LITP_PASS)
    return interact_command


def cmd_get_total_postgres_connections(db_ipaddress, postgres_ds):
    db_name_string = "\\'%s\\'" % postgres_ds
    postgres_command = '''/opt/rh/postgresql92/%s/usr/bin/psql -U export_admin --host=postgresql01 -d %s -p 5432 -c \\"SELECT count(*) FROM pg_stat_activity where datname='%s'\\"''' % (
        ROOT_USER, postgres_ds, db_name_string)
    interact_command = "/usr/bin/expect -c  'spawn ssh -o StrictHostKeyChecking=no %s@%s %s; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        LITP_USER, db_ipaddress, postgres_command, "password", DEFAULT_LITP_PASS)
    return interact_command


def cmd_get_serverside_versant_connections(hostname, db_ipaddress):
    dps_server_conns = '''/usr/bin/sudo -S <<< %s su - versant -c \\"/ericsson/versant/bin/vstats  -d dps_integration -connections\\"''' % DEFAULT_LITP_PASS
    command = ("%s | grep -c %s" % (dps_server_conns, hostname))
    interact_command = "/usr/bin/expect -c  'spawn ssh -o StrictHostKeyChecking=no %s@%s %s; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        LITP_USER, db_ipaddress, command, "password", DEFAULT_LITP_PASS)
    return interact_command


def display_versant_connections_on_client(hostname):
    _, stdout, stderr = SSH.exec_command(cmd_enable_ssh_for_host(hostname))
    _, stdout, stderr = SSH.exec_command(
        cmd_get_clientside_versant_connections(hostname, DEFAULT_VM_PASS, "Password: "))
    message = "Client side connections to versant on %s - " % hostname
    expected_result_lines = 3
    display_command_result(stdout, stderr, message, expected_result_lines)


def get_host_ip_address(hostname):
    _, stdout, _ = SSH.exec_command(cmd_get_host_ipaddress(hostname, DEFAULT_VM_PASS, "Password: "))
    output = stdout.readlines()[2]
    host_ip = output.strip()
    log_date = get_formatted_datetime()
    print "%s IP address for host %s - %s " % (log_date, hostname, host_ip)
    return host_ip


def display_postgres_client_connections_on_server(hostname, host_ip, db_ipaddress):
    _, stdout, stderr = SSH.exec_command(cmd_get_clientside_postgres_connections(host_ip, db_ipaddress))
    message = "Client side connections to Postgres on %s - " % hostname
    expected_result_lines = 4
    display_command_result(stdout, stderr, message, expected_result_lines)


def display_postgres_total_connections_on_server(db_ipaddress):
    _, stdout, stderr = SSH.exec_command(cmd_get_total_postgres_connections(db_ipaddress))
    message = "Total server side connections to Postgres - "
    expected_result_lines = 4
    display_command_result(stdout, stderr, message, expected_result_lines)


def display_versant_connections_on_server(hostname, db_ipaddress):
    _, stdout, stderr = SSH.exec_command(cmd_get_serverside_versant_connections(hostname, db_ipaddress))
    message = "Server side connections to versant on %s - " % hostname
    output = stdout.readlines()
    exit_code = stdout.channel.recv_exit_status()
    log_date = get_formatted_datetime()
    num_connections = ""
    if exit_code == 0:
        for line in output:
            if "[sudo] password for" in line:
                num_connections = line.split(' ')[4].strip()
        print "%s %s%s" % (log_date, message, num_connections)
    else:
        print "%s %s - Command failure" % (log_date, message)
        output = stderr.read()
        print output


def display_versant_clientside_details(server_hosts):
    for host in server_hosts:
        display_versant_connections_on_client(host)
        host_ip = get_host_ip_address(host)
        ip_addresses[host] = host_ip


def check_serverside_connections(server_hosts, db_ipaddress):
    display_postgres_total_connections_on_server(db_ipaddress)
    for host in server_hosts:
        display_versant_connections_on_server(host, db_ipaddress)
        display_postgres_client_connections_on_server(host, ip_addresses.get(host), db_ipaddress)


def get_formatted_datetime():
    log_date = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%Y %H:%M:%S')
    return log_date


if __name__ == '__main__':
    main()
