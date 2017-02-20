def deploy(service_name, commit_id, timeoutInMins=5){
    milestone()
    try {
	timeout(time:timeoutInMins, unit:'MINUTES') {
	    input(message:'Approve deployment?')
        }
        node("slave"){
            doDeploy()
	}	
    } catch (err) {
        def user = err.getCauses()[0].getUser()
        echo "Aborted by:\n ${user}"
    }
    milestone()
}

def doDeploy(){
    def namespace = "${JOB_NAME}".split("/")[-2]
    def tag = "${BUILD_ID}-${commit_id}"
    def image = "${service_name}:${tag}"
    def clusters = sh(returnStdout: true, script:"kubectl config get-clusters")
    println(clusters)
    sh "kubectl set image deployments/${service_name} ${service_name}=${image} --namespace=${namespace}"
    def deployStatus = sh(returnStdout: true, script: "kubectl rollout status deployments/${service_name} --namespace=${namespace}").trim()
    println(deploystatus)
    if (deployStatus.contains("deployment \"${service_name}\" successfully rolled out")) {
        println("deployment SUCCEDED!!")
    } else {
        throw new Exception("Deployment FAILED!!")
    }
}

return this;
