node("slave") {
    def app = "";
    def commit_id="";
	stage("Build"){
	    checkout scm
	    sh "git rev-parse --short HEAD > .git/commit-id".trim()
        commit_id = readFile('.git/commit-id')
        docker.image("egovio/ci").inside {
                dir("${env.JOB_BASE_NAME}"){
                    def settings_exists = fileExists("settings.xml");
                    if (settings_exists){
                        sh "mvn clean package -s settings.xml";
                    } else {
                        sh "mvn clean package";
                    }
                }
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
            app.push()
        }
    }
}
