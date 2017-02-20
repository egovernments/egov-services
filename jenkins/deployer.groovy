def deploy(service_name, commit_id){
    def namespace = ${JOB_NAME}.split("/")[-2]
    def tag = "${BUILD_ID}-${commit_id}"
    def image = "${service_name}:${tag}"
    sh "kubectl set image deployments/${service_name} ${service_name}=${image} --namespace=${namespace}"
    deployStatus = sh(returnStdout: true, script: "kubectl rollout status deployments/${service_name} --namespace=${namespace}").trim()
    println(deploystatus)
    if (deployStatus.contains("deployment \"${service_name}\" successfully rolled out")) {
        println("deployment SUCCEDED!!")
    } else {
        throw new Exception("Deployment FAILED!!")
    }
}

return this;
