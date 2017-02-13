node("slave") {
	stage "Build"
	    checkout scm
            docker.image("egovio/ci").inside {
                sh "cd ${env.JOB_BASE_NAME}; mvn clean package;"
            }

	    stage "Results"
            archive "${env.JOB_BASE_NAME}/target/*.jar"
            
        stage "Publish to docker hub" 
            echo "publishing docker image"
        
}
