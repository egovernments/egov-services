def app = "";
def commit_id="";
def service_name = "${env.JOB_BASE_NAME}";
def build_wkflo;
def ci_image = "egovio/ci:0.0.1"
def notifier = "";

node("slave"){
    try {
        checkout scm
        sh "git rev-parse --short HEAD > .git/commit-id".trim()
        commit_id = readFile('.git/commit-id')
        notifier = load("jenkins/notifier.groovy")

        def build_workflow_exists = fileExists("${service_name}/build.wkflo");
        if (build_workflow_exists) {
            build_wkflo = load "${service_name}/build.wkflo"
            build_wkflo.build("${service_name}", "${ci_image}")
        } else {
            defaultMavenBuild("${service_name}", "${ci_image}")
        }

        stage("Archive Results") {
        archive "${service_name}/target/*.jar"
	archive "${service_name}/target/**/*.html"
        }

        stage("Build docker image") {
            sh "cd ${service_name} && docker build -t egovio/${service_name} ."
            sh "docker tag egovio/${service_name} egovio/${service_name}:${BUILD_ID}-${commit_id}"
            sh "docker tag egovio/${service_name} egovio/${service_name}:latest"
        }

        stage("Publish docker image") {
            sh "docker push egovio/${service_name}:${BUILD_ID}-${commit_id}"
            sh "docker push egovio/${service_name}:latest"
        }

        stage("Clean docker image locally") {
            sh "docker rmi egovio/${service_name}:${BUILD_ID}-${commit_id}"
            sh "docker rmi egovio/${service_name}:latest"
        }
    } catch (e) {
        notifier.notifyBuild("FAILED")
        throw e
    }
}

def defaultMavenBuild(service_name, ci_image){
    stage("Build"){
        docker.image("${ci_image}").inside {
            sh "cd ${service_name}; mvn clean verify package";
        }
    }
}
