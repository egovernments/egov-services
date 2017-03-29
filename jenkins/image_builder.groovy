def build(module_name, service_name, commit_id){
    stage("Build docker image") {
        sh "cd ${module_name}/${service_name} && docker build -t egovio/${service_name} ."
        sh "docker tag egovio/${service_name} egovio/${service_name}:${BUILD_ID}-${commit_id}"
        sh "docker tag egovio/${service_name} egovio/${service_name}:latest"
    }
}

def publish(service_name, commit_id){
    stage("Publish docker image") {
        sh "docker push egovio/${service_name}:${BUILD_ID}-${commit_id}"
        sh "docker push egovio/${service_name}:latest"
    }
}


def clean(service_name, commit_id){
    stage("Clean docker image") {
        sh "docker rmi egovio/${service_name}:${BUILD_ID}-${commit_id}"
        sh "docker rmi egovio/${service_name}:latest"
    }
}

return this;
