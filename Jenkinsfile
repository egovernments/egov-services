node("slave") {
    def app = "";
	stage "Build"
	    checkout scm
	    sh "git rev-parse --short HEAD > .git/commit-id".trim()
        def commit_id = readFile('.git/commit-id')
        docker.image("egovio/ci").inside {
                sh "cd ${env.JOB_BASE_NAME}; mvn clean package;"
        }

	stage "Results"
        archive "${env.JOB_BASE_NAME}/target/*.jar"
            
    stage "Build docker image"
        dir("${env.JOB_BASE_NAME}") {
            app = docker.build("egovio/${env.JOB_BASE_NAME}")
        }

    stage "Push to docker hub"
        docker.withRegistry("https://registry.hub.docker.com", "dockerhub") {
            app.tag("daily-${commit_id}", false)
            app.push()
        }
}
