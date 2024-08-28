#!/usr/bin/python

import sys, paramiko, datetime, argparse, subprocess

LITP_USER = "litp-admin"
DEFAULT_LITP_PASS = "12shroot"
ROOT_USER = "root"
DEFAULT_VM_PASS = "passw0rd"
DEFAULT_SVC_IPADDRESS = "192.168.0.202"
DB_SERVICE = "db1-service"
DEFAULT_CONTAINERS = "svc-1-impexpserv,svc-2-impexpserv,svc-1-cmserv,svc-2-cmserv"
DEFAULT_FATAL_LOGGING = ""
DEFAULT_ERROR_LOGGING = ""
DEFAULT_WARN_LOGGING = ""
DEFAULT_INFO_LOGGING = ""
DEFAULT_DEBUG_LOGGING = ""
DEFAULT_TRACE_LOGGING = "com.ericsson.oss.mediation.core.interceptor,com.ericsson.oss.itpf.datalayer.dps"
DEFAULT_FINE_TRACING = "com.versant"
DATE = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%YT%H:%M:%S_')
OUTPUT_LOG_FILENAME = "set_container_log_levels.log"
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
    parser.add_argument('-remove', '--remove_logging_resource', help='remove logging resources specified',
                        required=False,
                        action='store_true')
    parser.add_argument('-all_containers', '--all_container_logs',
                        help='switch for getting logs from all containers',
                        action='store_true',
                        required=False)
    parser.add_argument('-fatal', '--fatal_logging_components',
                        help='Components whose trace levels are to be set to FATAL',
                        required=False,
                        default=DEFAULT_FATAL_LOGGING)
    parser.add_argument('-error', '--error_logging_components',
                        help='Components whose trace levels are to be set to ERROR',
                        required=False,
                        default=DEFAULT_ERROR_LOGGING)
    parser.add_argument('-warn', '--warn_logging_components',
                        help='Components whose trace levels are to be set to WARN',
                        required=False,
                        default=DEFAULT_WARN_LOGGING)
    parser.add_argument('-info', '--info_logging_components',
                        help='Components whose trace levels are to be set to INFO',
                        required=False,
                        default=DEFAULT_INFO_LOGGING)
    parser.add_argument('-debug', '--debug_logging_components',
                        help='Components whose trace levels are to be set to DEBUG',
                        required=False,
                        default=DEFAULT_DEBUG_LOGGING)
    parser.add_argument('-trace', '--trace_logging_components',
                        help='Components whose trace levels are to be set to TRACE',
                        required=False,
                        default=DEFAULT_TRACE_LOGGING)
    parser.add_argument('-fine', '--fine_logging_components',
                        help='Components whose trace levels are to be set to FINE',
                        required=False,
                        default=DEFAULT_DEBUG_LOGGING)
    args = parser.parse_args()
    default_svc_ipaddress = args.svc_server_ip
    containers = args.container_clients
    default_logging_enabled = args.logging_enabled
    list_containers = args.list_all_containers
    all_containers = args.all_container_logs
    fatal_logging_components = args.fatal_logging_components
    error_logging_components = args.error_logging_components
    warn_logging_components = args.warn_logging_components
    info_logging_components = args.info_logging_components
    debug_logging_components = args.debug_logging_components
    trace_logging_components = args.trace_logging_components
    fine_logging_components = args.fine_logging_components
    remove = args.remove_logging_resource

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

    # log_dir = create_root_log_dir()

    container_list = containers.split(',')
    check_hostnames_exist(container_list)

    # First remove the logging resource in each container if it exists
    for container in container_list:
        remove_fatal_logging_resources(container, fatal_logging_components)
        remove_error_logging_resources(container, error_logging_components)
        remove_warn_logging_resources(container, warn_logging_components)
        remove_info_logging_resources(container, info_logging_components)
        remove_debug_logging_resources(container, debug_logging_components)
        remove_trace_logging_resources(container, trace_logging_components)
        remove_fine_logging_resources(container, fine_logging_components)

    # Cleanup option
    if remove:
        sys.exit()

    # Now set the levels passed in on each container
    for container in container_list:
        set_fatal_logging_resources(container, fatal_logging_components)
        set_error_logging_resources(container, error_logging_components)
        set_warn_logging_resources(container, warn_logging_components)
        set_info_logging_resources(container, info_logging_components)
        set_debug_logging_resources(container, debug_logging_components)
        set_trace_logging_resources(container, trace_logging_components)
        set_fine_logging_resources(container, fine_logging_components)

    message = "Finished setting log levels for the following containers ... "
    print_message(message)
    print containers


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
    containers = containers.split(',')
    message = "List of containers on deployment %s ..." % svc_ipaddress
    print_message(message)
    for container in containers:
        print container
    sys.exit()


def remove_fatal_logging_resources(container, fatal_logging_components):
    if fatal_logging_components != "":
        components = fatal_logging_components.split(',')
        for component in components:
            remove_logging_levels_on_host(container, component)


def remove_error_logging_resources(container, error_logging_components):
    if error_logging_components != "":
        components = error_logging_components.split(',')
        for component in components:
            remove_logging_levels_on_host(container, component)


def remove_warn_logging_resources(container, warn_logging_components):
    if warn_logging_components != "":
        components = warn_logging_components.split(',')
        for component in components:
            remove_logging_levels_on_host(container, component)


def remove_info_logging_resources(container, info_logging_components):
    if info_logging_components != "":
        components = info_logging_components.split(',')
        for component in components:
            remove_logging_levels_on_host(container, component)


def remove_debug_logging_resources(container, debug_logging_components):
    if debug_logging_components != "":
        components = debug_logging_components.split(',')
        for component in components:
            remove_logging_levels_on_host(container, component)


def remove_trace_logging_resources(container, trace_logging_components):
    if trace_logging_components != "":
        components = trace_logging_components.split(',')
        for component in components:
            remove_logging_levels_on_host(container, component)


def remove_fine_logging_resources(container, fine_logging_components):
    if fine_logging_components != "":
        components = fine_logging_components.split(',')
        for component in components:
            remove_logging_levels_on_host(container, component)


def set_fatal_logging_resources(container, fatal_logging_components):
    if fatal_logging_components != "":
        components = fatal_logging_components.split(',')
        for component in components:
            set_logging_levels_on_host(container, component, "FATAL")


def set_error_logging_resources(container, error_logging_components):
    if error_logging_components != "":
        components = error_logging_components.split(',')
        for component in components:
            set_logging_levels_on_host(container, component, "ERROR")


def set_warn_logging_resources(container, warn_logging_components):
    if warn_logging_components != "":
        components = warn_logging_components.split(',')
        for component in components:
            set_logging_levels_on_host(container, component, "WARN")


def set_info_logging_resources(container, info_logging_components):
    if info_logging_components != "":
        components = info_logging_components.split(',')
        for component in components:
            set_logging_levels_on_host(container, component, "INFO")


def set_debug_logging_resources(container, debug_logging_components):
    if debug_logging_components != "":
        components = debug_logging_components.split(',')
        for component in components:
            set_logging_levels_on_host(container, component, "DEBUG")


def set_trace_logging_resources(container, trace_logging_components):
    if trace_logging_components != "":
        components = trace_logging_components.split(',')
        for component in components:
            set_logging_levels_on_host(container, component, "TRACE")


def set_fine_logging_resources(container, fine_logging_components):
    if fine_logging_components != "":
        components = fine_logging_components.split(',')
        for component in components:
            set_logging_levels_on_host(container, component, "FINE")


def create_root_log_dir():
    log_dir = "container_logs_%s" % get_formatted_datetime()
    message = "Creating root directory for logs - %s " % log_dir
    print_message(message)
    out_bytes = subprocess.check_output(['mkdir', log_dir], stderr=subprocess.STDOUT)
    out_text = out_bytes.decode('utf-8')
    print out_text
    return log_dir


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
    # Allway include db_service
    container_list = "%s,%s" % (container_list, DB_SERVICE)
    return container_list


def cmd_set_log_level_for_component(hostname, user, server_pass, expecting, component, log_level):
    command = '''/ericsson/3pp/jboss/bin/jboss-cli.sh --connect \\"/subsystem=logging/logger=%s:add(level=%s)\\"''' % (
        component, log_level)
    interact_command = "/usr/bin/expect -c  'spawn ssh -o StrictHostKeyChecking=no %s@%s %s; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        user, hostname, command, expecting, server_pass)
    return interact_command


def set_logging_levels_on_host(hostname, component, log_level):
    message = "Generating host key for %s ... " % hostname
    print_message(message)
    _, _, _ = SSH.exec_command(cmd_enable_ssh_for_host(hostname))
    if hostname.startswith("db"):
        user = LITP_USER
        passwd = DEFAULT_LITP_PASS
    else:
        user = ROOT_USER
        passwd = DEFAULT_VM_PASS
    message = "Setting trace levels on %s for %s - %s ..." % (hostname, component, log_level)
    print_message(message)
    _, stdout, _ = SSH.exec_command(
        cmd_set_log_level_for_component(hostname, user, passwd, "password: ", component, log_level))
    output = stdout.read()
    print output


def cmd_remove_log_level_for_component(hostname, user, server_pass, expecting, component):
    command = '''/ericsson/3pp/jboss/bin/jboss-cli.sh --connect \\"/subsystem=logging/logger=%s:remove\\"''' % (
        component)
    interact_command = "/usr/bin/expect -c  'spawn ssh -o StrictHostKeyChecking=no %s@%s %s; expect \"%s\"; send \"%s\r\n\"; interact;'" % (
        user, hostname, command, expecting, server_pass)
    return interact_command


def remove_logging_levels_on_host(hostname, component):
    message = "Generating host key for %s ... " % hostname
    print_message(message)
    _, _, _ = SSH.exec_command(cmd_enable_ssh_for_host(hostname))
    if hostname.startswith("db"):
        user = LITP_USER
        passwd = DEFAULT_LITP_PASS
    else:
        user = ROOT_USER
        passwd = DEFAULT_VM_PASS
    message = "Removing trace level resource on %s for %s ... " % (hostname, component)
    print_message(message)
    message = "NOTE 'not found' BELOW INDICATES THAT THE RESOURCE NEED NOT BE REMOVED' ..."
    print_message(message)
    _, stdout, _ = SSH.exec_command(cmd_remove_log_level_for_component(hostname, user, passwd, "password: ", component))
    output = stdout.read()
    print output


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
    return "ssh-keygen -R %s" % hostname


def get_formatted_datetime():
    log_date = datetime.datetime.strftime(datetime.datetime.now(), '%d-%m-%Y_%H:%M:%S')
    return log_date


def print_message(message):
    print ""
    print "************      %s ...     **************" % message
    print ""


if __name__ == '__main__':
    main()
