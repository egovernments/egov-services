node("slave") {
	try {
	    def app = "";
	    def commit_id="";
	    echo "${env.JOB_NAME}"
	    def service_name = "${env.JOB_BASE_NAME}";
		stage("Build")
		{
		    checkout scm
		    sh "git rev-parse --short HEAD > .git/commit-id".trim()
			commit_id = readFile('.git/commit-id')
			docker.image("egovio/ci").inside 
			{
			        dir("${service_name}")
			        {
			            def settings_exists = fileExists("settings.xml");
			            if (settings_exists)
			            {
			                sh "cd ${service_name}; mvn clean package -s settings.xml";
			            } 
			            else 
			            {
			                sh "cd ${service_name}; mvn clean package";
			            }
			        }
			}
	    }

		stage("Archive Results")
		{
			archive "${service_name}/target/*.jar"
	    }

		stage("Build docker image")
		{
	        sh "cd ${service_name}; docker build -t egovio/${service_name}:${commit_id} ."
		}

		stage("Publish docker image")
		{
		    sh "docker push egovio/${service_name}:${commit_id}"
		}
	} catch (e) {
	    currentBuild.result = "FAILED"
	    throw e
	} finally {
	    notifyBuild(currentBuild.result)
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

  // Send notifications
  slackSend (color: colorCode, message: summary)

  emailext (
  	   body: '$DEFAULT_CONTENT', recipientProviders: [[$class: 'CulpritsRecipientProvider'], [$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']], replyTo: '$DEFAULT_REPLYTO', subject: '$PROJECT_NAME - Build # $BUILD_NUMBER - ${BUILD_STATUS}!', to: '$DEFAULT_RECIPIENTS'
    )
}
