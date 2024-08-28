#!/usr/bin/python

import sys, paramiko, datetime, argparse, time

LITP_USER = "litp-admin"
DEFAULT_LITP_PASS = "12shroot"
ROOT_USER = "root"
DEFAULT_VM_PASS = "passw0rd"
DEFAULT_SFS_USER = "support"
DEFAULT_SFS_PASS = "symantec"
DEFAULT_SVC_IPADDRESS = "192.168.0.202"
DEFAULT_SFS_CLIENTS = "svc-1-impexpserv,svc-2-impexpserv"
IP_ADDRESSES = {}
DATE = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%YT%H:%M:%S_')
IPERF_LOG_FILENAME = "iperf_vm_bandwidth.log"
IPERF_LOG_PATH = "/tmp/%s%s" % (DATE, IPERF_LOG_FILENAME)

SSH = paramiko.SSHClient()
SSH.set_missing_host_key_policy(paramiko.AutoAddPolicy())


def main():
    # Parse command line
    parser = argparse.ArgumentParser(description='Process command line args.')
    parser.add_argument('-svc', '--svc_server_ip', help='IP address of svc', required=False,
                        default=DEFAULT_SVC_IPADDRESS)
    parser.add_argument('-clients', '--sfs_clients', help='Comma separated list of SFS clients', required=False,
                        default=DEFAULT_SFS_CLIENTS)
    parser.add_argument('-logging', '--logging_enabled', help='switch for separate log file', required=False,
                        action='store_true')
    parser.add_argument('-install', '--install_iperf3', help='switch for iperf3 installation', required=False,
                        action='store_true')
    args = parser.parse_args()
    default_svc_ipaddress = args.svc_server_ip
    sfs_clients = args.sfs_clients.split(',')
    default_logging_enabled = args.logging_enabled
    install = args.install_iperf3

    # Logging
    if default_logging_enabled:
        print "Output redirecting to %s......." % IPERF_LOG_PATH
        file_to_write = open(IPERF_LOG_PATH, 'w')
        sys.stdout = file_to_write

    start_remote_connection(default_svc_ipaddress, LITP_USER, DEFAULT_LITP_PASS)
    check_hostnames_exist(sfs_clients)
    get_primary_ip_of_export_vms(sfs_clients)
    sfs_server_ip = get_ip_for_sfs_server()
    if install:
        install_iperf3_rpm_on_svc()
        copy_iperf3_files_to_vms(sfs_clients)
        copy_iperf3_files_to_sfs_server(sfs_server_ip)
    start_iperf3_server_on_clients(sfs_clients)
    run_iperf3_from_sfs_server_to_clients(sfs_clients, sfs_server_ip)
    kill_iperf3_server_on_clients(sfs_clients)
    close_remote_connection()


def start_remote_connection(remote_ip, remote_user, remote_pass):
    SSH.connect(remote_ip, username=remote_user, password=remote_pass)
    log_date = get_formatted_datetime()
    print "%s Remote connection started to %s" % (log_date, remote_ip)


def close_remote_connection():
    log_date = get_formatted_datetime()
    print "%s Remote connection is closed" % log_date
    SSH.close()


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


def cmd_get_primary_ip_of_client(hostname, server_pass, expecting):
    return (
        "/usr/bin/expect -c 'spawn ssh -o StrictHostKeyChecking=no %s@%s /sbin/ifconfig eth0; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
            ROOT_USER, hostname, expecting, server_pass))


def get_primary_ip_of_client(hostname):
    _, stdout, _ = SSH.exec_command(cmd_enable_ssh_for_host(hostname))
    _, stdout, _ = SSH.exec_command(cmd_get_primary_ip_of_client(hostname, DEFAULT_VM_PASS, "Password: "))
    output = stdout.readlines()
    primary_ip = ""
    for line in output:
        if "inet addr:" in line:
            primary_ip = line.split(':')[1].strip().split(' ')[0].strip()
    print "Primary IP address for %s is %s" % (hostname, primary_ip)
    return primary_ip


def get_ip_for_sfs_server():
    _, stdout, _ = SSH.exec_command("df | grep '[0-9]*[\.][0-9]*[\.][0-9]*[\.][0-9]*[:].*-batch'")
    output = stdout.read()
    sfs_server_ip = output.split(':')[0].strip()
    print "SFS server IP address is %s" % sfs_server_ip
    return sfs_server_ip


def get_primary_ip_of_export_vms(sfs_clients):
    for host in sfs_clients:
        host_ip = get_primary_ip_of_client(host)
        IP_ADDRESSES[host] = host_ip


def cmd_start_iperf3_server_on_client(sfs_client_ip, server_pass, expecting):
    command = "iperf3 -s -B %s -4" % sfs_client_ip
    print "Executing command on %s - %s" % (sfs_client_ip, command)
    interact_command = "/usr/bin/expect -c  'spawn ssh -o StrictHostKeyChecking=no %s@%s %s; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        ROOT_USER, sfs_client_ip, command, expecting, server_pass)
    return interact_command


def start_iperf3_server_on_client(hostname):
    _, _, _ = SSH.exec_command(cmd_enable_ssh_for_host(IP_ADDRESSES.get(hostname)))
    transport = SSH.get_transport()
    channel = transport.open_session()
    channel.exec_command(cmd_start_iperf3_server_on_client(IP_ADDRESSES.get(hostname), DEFAULT_VM_PASS, "password: "))
    time.sleep(1)
    print "Server started on %s" % hostname


def start_iperf3_server_on_clients(sfs_clients):
    for host in sfs_clients:
        start_iperf3_server_on_client(host)


def cmd_get_iperf3_server_pid(sfs_client_ip, server_pass, expecting):
    _, _, _ = SSH.exec_command(cmd_enable_ssh_for_host(sfs_client_ip))
    command = "ps -ef | grep iperf3 | grep -v grep"
    interact_command = "/usr/bin/expect -c  'spawn ssh -o StrictHostKeyChecking=no %s@%s %s; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        ROOT_USER, sfs_client_ip, command, expecting, server_pass)
    return interact_command


def get_iperf3_server_pid(host_ip):
    _, _, _ = SSH.exec_command(cmd_enable_ssh_for_host(host_ip))
    _, stdout, _ = SSH.exec_command(cmd_get_iperf3_server_pid(host_ip, DEFAULT_VM_PASS, "password: "))
    output = stdout.readlines()
    pid = ""
    for line in output:
        if "iperf3 -s -B" in line:
            pid = line.split()[1]
    return pid


def cmd_kill_iperf3_server_on_client(sfs_client_ip, pid, server_pass, expecting):
    _, _, _ = SSH.exec_command(cmd_enable_ssh_for_host(sfs_client_ip))
    command = "kill -9 %s" % pid
    print "Executing command on %s - %s" % (sfs_client_ip, command)
    interact_command = "/usr/bin/expect -c  'spawn ssh -o StrictHostKeyChecking=no %s@%s %s; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        ROOT_USER, sfs_client_ip, command, expecting, server_pass)
    return interact_command


def kill_iperf3_server_on_client(hostname):
    host_ip = IP_ADDRESSES.get(hostname)
    pid = get_iperf3_server_pid(host_ip)
    _, stdout, _ = SSH.exec_command(cmd_kill_iperf3_server_on_client(host_ip, pid, DEFAULT_VM_PASS, "password: "))
    _ = stdout.read()
    print "Process %s killed on %s" % (pid, hostname)


def kill_iperf3_server_on_clients(sfs_clients):
    for host in sfs_clients:
        kill_iperf3_server_on_client(host)


def cmd_run_iperf3_from_sfs_server_to_client(sfs_client_ip, sfs_server_ip, server_pass, expecting):
    _, _, _ = SSH.exec_command(cmd_enable_ssh_for_host(sfs_client_ip))
    command = "iperf3 -c %s -4" % sfs_client_ip
    print "Client side command on SFS Server %s - %s " % (sfs_server_ip, command)
    interact_command = "/usr/bin/expect -c  'spawn ssh -o StrictHostKeyChecking=no %s@%s %s; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        DEFAULT_SFS_USER, sfs_server_ip, command, expecting, server_pass)
    return interact_command


def run_iperf3_from_sfs_server_to_client(hostname, sfs_server_ip):
    _, _, _ = SSH.exec_command(cmd_enable_ssh_for_host(IP_ADDRESSES.get(hostname)))
    _, stdout, _ = SSH.exec_command(
        cmd_run_iperf3_from_sfs_server_to_client(IP_ADDRESSES.get(hostname), sfs_server_ip, DEFAULT_SFS_PASS,
                                                 "Password: "))
    print "******************************************************"
    print "Checking bandwidth from SFS Server to %s" % hostname
    print "******************************************************"
    output = stdout.read()
    print output


def run_iperf3_from_sfs_server_to_clients(sfs_clients, sfs_server_ip):
    for host in sfs_clients:
        run_iperf3_from_sfs_server_to_client(host, sfs_server_ip)


def install_iperf3_rpm_on_svc():
    sftp = SSH.open_sftp()
    sftp.put("iperf3-3.0.11-1.el6.x86_64.rpm", "iperf3-3.0.11-1.el6.x86_64.rpm")
    sftp.close()
    _, stdout, _ = SSH.exec_command("chmod 777 iperf3-3.0.11-1.el6.x86_64.rpm")
    install_command = '''\"rpm -ivh --force /home/%s/iperf3-3.0.11-1.el6.x86_64.rpm\"''' % LITP_USER
    command = "su - %s -c %s" % (ROOT_USER, install_command)
    interract_command = "/usr/bin/expect -c 'spawn %s; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        command, "Password: ", DEFAULT_LITP_PASS)
    _, stdout, _ = SSH.exec_command(interract_command)
    output = stdout.read()
    print output


def cmd_copy_iperf3_binary_to_server(server_ip, user, server_pass, expecting):
    _, _, _ = SSH.exec_command(cmd_enable_ssh_for_host(server_ip))
    command = "scp -r -o StrictHostKeyChecking=no /usr/bin/iperf3 %s@%s:/usr/bin" % (user, server_ip)
    interact_command = "/usr/bin/expect -c  'spawn %s; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        command, expecting, server_pass)
    return interact_command


def cmd_copy_iperf3_libraries_to_server(server_ip, user, server_pass, expecting):
    _, _, _ = SSH.exec_command(cmd_enable_ssh_for_host(server_ip))
    command = "scp -r -o StrictHostKeyChecking=no /usr/lib64/libiperf.so.0.0.0 %s@%s:/usr/lib64" % (user, server_ip)
    interact_command = "/usr/bin/expect -c  'spawn %s; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        command, expecting, server_pass)
    return interact_command


def cmd_create_iperf3_libraries_simlink_on_server(server_ip, user, server_pass, expecting):
    _, _, _ = SSH.exec_command(cmd_enable_ssh_for_host(server_ip))
    command = "ln -s /usr/lib64/libiperf.so.0.0.0 /usr/lib64/libiperf.so.0"
    interact_command = "/usr/bin/expect -c  'spawn ssh -o StrictHostKeyChecking=no %s@%s %s; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        user, server_ip, command, expecting, server_pass)
    return interact_command


def copy_iperf3_files_to_vm(hostname):
    _, stdout, _ = SSH.exec_command(cmd_enable_ssh_for_host(IP_ADDRESSES.get(hostname)))
    _, stdout, _ = SSH.exec_command(
        cmd_copy_iperf3_binary_to_server(IP_ADDRESSES.get(hostname), ROOT_USER, DEFAULT_VM_PASS, "Password: "))
    output = stdout.read()
    print "Copying binary to vm output..."
    print output
    _, stdout, _ = SSH.exec_command(
        cmd_copy_iperf3_libraries_to_server(IP_ADDRESSES.get(hostname), ROOT_USER, DEFAULT_VM_PASS, "Password: "))
    print "Copying libraries to vm output..."
    output = stdout.read()
    print output
    _, stdout, _ = SSH.exec_command(
        cmd_create_iperf3_libraries_simlink_on_server(IP_ADDRESSES.get(hostname), ROOT_USER, DEFAULT_VM_PASS,
                                                      "Password: "))
    print "Creating simlink on server ..."
    output = stdout.read()
    print output


def copy_iperf3_files_to_vms(vm_hostnames):
    for host in vm_hostnames:
        copy_iperf3_files_to_vm(host)


def copy_iperf3_files_to_sfs_server(sfs_server_ip):
    _, _, _ = SSH.exec_command(cmd_enable_ssh_for_host(sfs_server_ip))
    _, stdout, _ = SSH.exec_command(
        cmd_copy_iperf3_binary_to_server(sfs_server_ip, DEFAULT_SFS_USER, DEFAULT_SFS_PASS, "Password: "))
    output = stdout.read()
    print "Copying binary to SFS server output..."
    print output
    _, stdout, _ = SSH.exec_command(
        cmd_copy_iperf3_libraries_to_server(sfs_server_ip, DEFAULT_SFS_USER, DEFAULT_SFS_PASS, "Password: "))
    print "Copying libraries to SFS server output..."
    output = stdout.read()
    print output
    _, stdout, _ = SSH.exec_command(
        cmd_create_iperf3_libraries_simlink_on_server(sfs_server_ip, DEFAULT_SFS_USER, DEFAULT_SFS_PASS, "Password: "))
    print "Creating simlink on SFS server ..."
    output = stdout.read()
    print output


def get_formatted_datetime():
    log_date = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%Y %H:%M:%S')
    return log_date


if __name__ == '__main__':
    main()
