def app = "";
def commit_id="";
def service_name = "${env.JOB_BASE_NAME}";
def build_wkflo;
def ci_image = "egovio/ci:0.0.1"

node("slave"){
    try {
        checkout scm
        sh "git rev-parse --short HEAD > .git/commit-id".trim()
        commit_id = readFile('.git/commit-id')

        def build_workflow_exists = fileExists("${service_name}/build.wkflo");
        if (build_workflow_exists) {
            build_wkflo = load "${service_name}/build.wkflo"
            build_wkflo.build("${service_name}", "${ci_image}")
        } else {
            defaultMavenBuild("${service_name}")
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
        notifyBuild("FAILED")
        throw e
    }
}

def defaultMavenBuild(service_name){
    stage("Build"){
        docker.image("${ci_image}").inside {
            sh "cd ${service_name}; mvn clean verify package";
        }
    }
}

def notifyBuild(String buildStatus = 'STARTED') {
  buildStatus =  buildStatus ?: 'SUCCESSFUL'
  BUILD_STATUS = "${buildStatus}"
  def colorName = 'RED'
  def colorCode = '#FF0000'
  def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
  def summary = "${subject} (${env.BUILD_URL})"

  if (buildStatus == 'STARTED') {
    color = 'YELLOW'
    colorCode = '#FFFF00'
  } else if (buildStatus == 'SUCCESSFUL') {
    color = 'GREEN'
    colorCode = '#00FF00'
  } else {
    color = 'RED'
    colorCode = '#FF0000'
  }

  slackSend (color: colorCode, message: summary)
  emailext (
     body: '$DEFAULT_CONTENT', recipientProviders: [[$class: 'CulpritsRecipientProvider'], [$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']], replyTo: '$DEFAULT_REPLYTO', subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - ${BUILD_STATUS}!', to: '$DEFAULT_RECIPIENTS'
    )
}
