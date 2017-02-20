def build(service_name, ci_image){
    def build_workflow_exists = fileExists("${service_name}/build.wkflo");
    if (build_workflow_exists) {
        build_wkflo = load("${service_name}/build.wkflo")
        build_wkflo.build(service_name, ci_image)
    } else {
        defaultMavenBuild(service_name, ci_image)
    }
}

def defaultMavenBuild(service_name, ci_image){
    stage("Build"){
        docker.image("${ci_image}").inside {
            sh "cd ${service_name}; mvn clean verify package";
        }
    }
}

return this;
