import json
import os
import sys
import shlex
from subprocess import PIPE, Popen

group = sys.argv[1]


def get_manifests():
    get_manifests_cmd = "kubectl get svc,deployments --namespace=egov -l " \
                        "'group in ({})' " \
                        "-o json".format(group)
    filter_manifests_cmd = "jq '.items[] | " \
                           "del(.spec.clusterIP," \
                           ".spec.sessionAffinity," \
                           ".spec.type," \
                           ".status," \
                           ".metadata.creationTimestamp," \
                           ".metadata.resourceVersion," \
                           ".metadata.selfLink," \
                           ".metadata.uid," \
                           ".metadata.annotations," \
                           ".spec.template.spec.dnsPolicy," \
                           ".spec.template.spec.restartPolicy," \
                           ".spec.template.spec.securityContext," \
                           ".spec.template.spec.terminationGracePeriodSeconds," \
                           ".spec.template.metadata.creationTimestamp," \
                           ".spec.strategy," \
                           ".metadata.generation," \
                           ".spec.template.spec.containers[]?.resources," \
                           ".spec.template.spec.containers[]?." \
                           "terminationMessagePath)'"
    get_full_manifests = Popen(shlex.split(get_manifests_cmd),stdout=PIPE)
    get_filtered_manifests = Popen(shlex.split(filter_manifests_cmd),
                                   stdin=get_full_manifests.stdout,
                                   stdout=PIPE)
    manifests, error = get_filtered_manifests.communicate()
    if error:
        raise Exception("Manifests doesn't exist for group {}\nERROR:{}"
                        .format(group, error))
    return manifests


def main():
    manifests = get_manifests()
    manifests_file = "{}/kubernetes_manifests.json".format(os.path.dirname(
        os.path.abspath(__file__)))
    try:
        os.remove(manifests_file)
    except:
        pass
    with open(manifests_file, "w") as f:
        f.write(manifests)


if __name__ == "__main__":
    main()
