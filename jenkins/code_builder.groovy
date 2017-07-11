def build(path, ci_image){
    def build_workflow_exists = fileExists("${path}/build.wkflo");
    def docker_file_exists = fileExists("${path}/Dockerfile")
    if (build_workflow_exists) {
        build_wkflo = load("${path}/build.wkflo")
        build_wkflo.build(path, ci_image)
    } else if (docker_file_exists) {
        return this
    } else {
        defaultMavenBuild(path, ci_image)
    }
}

def defaultMavenBuild(path, ci_image){
    stage("Build"){
        sh "./build.sh ${path}"
    }
}

return this;
