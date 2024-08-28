#!/usr/bin/python
"""
Manages SSH session
"""
import datetime, paramiko, sys, socket
from paramiko.ssh_exception import BadHostKeyException, \
                                   AuthenticationException, \
                                   SSHException

ROOT = "root"
DEFAULT_ROOT_PASS = "12shroot"
DEFAULT_MS_IPADDRESS = "192.168.0.42"
DEFAULT_NETWORK_ELEMENT = "*"
DEFAULT_CLI_USERNAME = "administrator"
DEFAULT_CLI_PASSWORD = "TestPassw0rd"
CLI_USER_PROMPT = "Username: "
CLI_PASSWORD_PROMPT = "Password: "

SSH = paramiko.SSHClient()
SSH.set_missing_host_key_policy(paramiko.AutoAddPolicy())

def start_session(ms_ip=DEFAULT_MS_IPADDRESS,
                  username=ROOT,
                  password=DEFAULT_ROOT_PASS):
    """Starts a session"""
    try:
        SSH.connect(ms_ip, username=username, password=password)
        log_date = datetime.datetime.strftime(datetime.datetime.now(),
                                              '%d-%m-%Y %H:%M:%S')
        print "%s - Remote connection started to %s" % (log_date, ms_ip)
    except (BadHostKeyException, AuthenticationException,
        SSHException, socket.error) as exc:
        print "Remote connection could not be started. Error:[%s]" % exc
        sys.exit(1)

def close_session():
    """Closes a session"""
    try:
        SSH.close()
        log_date = datetime.datetime.strftime(datetime.datetime.now(),
                                              '%d-%m-%Y %H:%M:%S')
        print "%s - Remote connection is closed" % log_date
    except (BadHostKeyException, AuthenticationException,
        SSHException, socket.error) as exc:
        print "Remote connection could not be closed. Error:[%s]" % exc
        sys.exit(1)

def exec_command(command,
                user_expect=CLI_USER_PROMPT,
                user=DEFAULT_CLI_USERNAME,
                pwd_expect=CLI_PASSWORD_PROMPT,
                pwd=DEFAULT_CLI_PASSWORD):
    """Executes the command"""
    if  SSH.get_transport() is None or not SSH.get_transport().is_active():
        print """Cannot run command without SSH open session"""
        sys.exit(1)
    try:
        cli_command = (
            "/usr/bin/expect -c 'spawn"\
            " /opt/ericsson/enmutils/bin/cli_app \"%s\";" \
            " expect \"%s\"; send \"%s\r\n\"; expect \"%s\";" \
            " send \"%s\r\n\"; interact;'"
            % (command, user_expect, user, pwd_expect, pwd))
        _, stdout, _ = SSH.exec_command(cli_command)
        output = stdout.readlines()
        return output
    except SSHException as exc:
        print "Could not execute command [%s]. Error:[%s]" % exc
        sys.exit(1)
