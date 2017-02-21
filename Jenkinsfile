def app = "";
def commit_id="";
def service_name = "${env.JOB_BASE_NAME}";
def build_wkflo;
def ci_image = "egovio/ci:0.0.1"
def notifier = "";

try {
    node("slave"){
        checkout scm
        sh "git rev-parse --short HEAD > .git/commit-id".trim()
        commit_id = readFile('.git/commit-id')
        code_builder = load("jenkins/code_builder.groovy")
        archiver = load("jenkins/archiver.groovy")
        image_builder = load("jenkins/image_builder.groovy")
        notifier = load("jenkins/notifier.groovy")
        deployer = load("jenkins/deployer.groovy")

        code_builder.build(service_name, ci_image)

        archiver.archiveArtifacts(service_name)

        image_builder.build(service_name, commit_id)

        image_builder.publish(service_name, commit_id)

        image_builder.clean(service_name, commit_id)
    }

//    stage("Deploy to QA") {
//        deployer.deploy(service_name, commit_id)
//    }
} catch (e) {
    currentBuild.result = "FAILED"
    throw e
  } finally {
    notifyBuild(currentBuild.result)
  }
}
