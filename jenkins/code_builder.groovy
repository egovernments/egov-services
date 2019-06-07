def build(path, ci_image){
    def build_workflow_exists = fileExists("${path}/build.wkflo");
    echo 'build workflow file exists'
    if (build_workflow_exists) {
        build_wkflo = load("${path}/build.wkflo")
        echo 'path of .wkflo'
        echo build_wkflo
        build_wkflo.build(path, ci_image)
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
