node("slave") {
        stage "Build"
            git "https://github.com/egovernments/pgr-services.git"
            docker.image("egovio/ci").inside {
                sh "cd ${env.JOB_NAME}; mvn clean package;"
            }
            
        stage "Publish to docker hub" 
            echo "publishing docker image"
        
}
