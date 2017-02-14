node("slave") {
    def app = "";
    def commit_id="";
	stage("Build"){
	    checkout scm
	    sh "git rev-parse --short HEAD > .git/commit-id".trim()
        commit_id = readFile('.git/commit-id')
        docker.image("egovio/ci").inside {
                sh "cd ${env.JOB_BASE_NAME}; mvn clean package;"
        }
    }

	stage("Archive Results"){
        archive "${env.JOB_BASE_NAME}/target/*.jar"
    }

    docker.withRegistry("https://registry.hub.docker.com", "dockerhub") {
        stage("Build docker image"){
            dir("${env.JOB_BASE_NAME}") {
                app = docker.build("egovio/${env.JOB_BASE_NAME}")
            }
        }

        stage("Build publish docker image"){
            app.push("${env.BUILD_ID}-${commit_id}")
        }
    }

    stage("Finished"){
        echo "Finished"
    }
}
