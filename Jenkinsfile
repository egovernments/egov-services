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
        code_builder = load("jenkins/code_builder.groovy")
        archiver = load("jenkins/archiver.groovy")
        image_builder = load("jenkins/image_builder.groovy")
        notifier = load("jenkins/notifier.groovy")

        code_builder.build(service_name, ci_image)

        archiver.archive(service_name)

        image_builder.build(service_name, commit_id)

        image_builder.publish(service_name, commit_id)

        image_builder.clean(service_name, commit_id)
    } catch (e) {
        notifier.notifyBuild("FAILED")
        throw e
    }
}
