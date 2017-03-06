import json
import os
import shlex
from subprocess import Popen, PIPE


def read_image_tags():
    image_tag_file = "{}/image_tags.txt".format(os.path.dirname(
        os.path.abspath(__file__)))
    with open(image_tag_file, "r") as f:
        deployment_image_tags = json.load(f)
    return deployment_image_tags


def is_rollout_successful(stdout):
    return "successfully rolled out" in stdout


def deploy():
    deployment_image_tags = read_image_tags()
    for deployment, container_tags in deployment_image_tags.iteritems():
        for image_tag in container_tags:
            for container, tag in image_tag.iteritems():
                deployment_cmd = "kubectl set image deployments {} {}={} " \
                                 "--namespace=egov".format(deployment,
                                                           container, tag)
                out, err = (Popen(shlex.split(deployment_cmd),
                                  stdout=PIPE).communicate())
                if err:
                    raise Exception("Deployment failed for deployment - {}, "
                                    "container - {}, image - {}\n"
                                    "STDOUT:{}\nERROR:{}".
                                    format(deployment, container, tag, out,
                                           err))
                rollout_status_cmd = ("kubectl rollout status deployments/{}"
                                      " --namespace=egov".format(deployment))
                rollout_status, rollout_err = (Popen(shlex.split(
                    rollout_status_cmd), stdout=PIPE).communicate())
                if rollout_err or not is_rollout_successful(rollout_status):
                    raise Exception("Rollout failed for deployment - {},"
                                    "container = {}, image - {}\n"
                                    "STDOUT:{}\nERROR:{}".
                                    format(deployment, container, image_tag,
                                           rollout_status, rollout_err))
                print rollout_status


if __name__ == "__main__":
    deploy()
