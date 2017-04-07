import os
import shlex
from subprocess import Popen, PIPE


def deploy():
    kubernetes_manifests_file = "{}/kubernetes_manifests.json".format(
        os.path.dirname(os.path.abspath(__file__)))
    deployment_cmd = "kubectl apply -f {}".format(kubernetes_manifests_file)
    out, err = (Popen(shlex.split(deployment_cmd),
                      stdout=PIPE).communicate())
    if err:
        raise Exception("Deployment failed\n"
                        "STDOUT:{}\nERROR:{}".
                        format(out, err))
    print out


if __name__ == "__main__":
    deploy()
