node("slave") {
	stage "Build"
	    checkout scm
            docker.image("egovio/ci").inside {
                sh "cd ${env.JOB_NAME}; mvn clean package;"
            }
            
        stage "Publish to docker hub" 
            echo "publishing docker image"
        
}
