import os
import shlex
import signal
import sys
import yaml

from subprocess import Popen, PIPE, STDOUT

file_path = os.path.dirname(os.path.abspath(__file__))


def launch():
    conf_path = "{}/launcher-conf.yml".format(file_path)
    conf = yaml.load(open(conf_path, "r"))
    for app in conf['apps']:
        app_path = "{}/{}".format(file_path, app['path'])
        _kill(app["name"])
        _build(app_path, app["buildCmd"])
        print("Launching {}".format(app["artifact"]))
        app_proc = _launch_app(app)
        _save_pid(app, app_proc)


def killall():
    for dir_path, subdirs, files in os.walk("/tmp/egov/pids"):
        for f in files:
            print "Killing {}".format(f)
            _kill(f.split(".")[0])


def _kill(f):
    if os.path.exists("/tmp/egov/pids/{}.pid".format(f), ):
        with open("/tmp/egov/pids/{}.pid".format(f), "r") as file_to_read:
            pid = file_to_read.read()
            try:
                os.kill(int(pid), signal.SIGTERM)
            except OSError:
                pass


def _launch_app(app):
    launch_cmd = "java -jar -Dserver.port={} {}".format(app["port"],
                                                        app["artifact"])
    Popen(shlex.split("mkdir -p /tmp/egov/logs"))
    Popen(shlex.split("touch /tmp/egov/logs/{}.log".format(app["name"])))
    app_log = open("/tmp/egov/logs/{}.log".format(app["name"]), "w+")
    app_proc = Popen(shlex.split(launch_cmd), stdout=app_log,
                     stderr=STDOUT)
    return app_proc


def _save_pid(app, app_proc):
    Popen(shlex.split("mkdir -p /tmp/egov/pids"))
    Popen(shlex.split("touch /tmp/egov/pids/{}.pid".format(app["name"])))
    with open("/tmp/egov/pids/{}.pid".format(app["name"]), "w+") as f:
        f.write(str(app_proc.pid))


def _build(app_path, cmd):
    print "Building {}".format(app_path)
    cwd = os.getcwd()
    os.chdir(app_path)
    try:
        build_proc = Popen(shlex.split(cmd), stdout=PIPE)
        out, err = build_proc.communicate()
        if not build_proc.returncode == 0:
            raise Exception("Build failed for {}\n"
                            "STDOUT:{}\nERROR:{}".format(app_path, out, err))
    finally:
        os.chdir(cwd)


def main():
    globals()[sys.argv[1]]()


if __name__ == "__main__":
    main()
