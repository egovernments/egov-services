def build(path, ci_image){

    String npm_modules = "npm-modules"
    def build_wkflo_file = "${path}/build.wkflo"

    def build_workflow_exists = fileExists("${path}/build.wkflo");

    if (module_name == npm_modules) {
      build_wkflo_file = "web/ui-web-app/src/development/${service_name}/build.wkflo"
      build_workflow_exists = True
      path="web/ui-web-app/src/development/${service_name}"
    }

    if (build_workflow_exists) {
        build_wkflo = load("${build_wkflo_file}")
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
