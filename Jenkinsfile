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

		stage("Build Artifacts")
		{
			archive "${service_name}/target/*.jar"
	    }

	    docker.withRegistry("https://registry.hub.docker.com", "dockerhub") 
	    {
			stage("Build docker image")
			{
		    	dir("${service_name}") 
		    	{
		        	app = docker.build("egovio/${service_name}")
		    	}
			}
			stage("Publish docker image")
			{
		    	app.push()
			}
	    }
	} catch (e) {
	    notifyBuild("FAILED")
	    throw e
	} finally {
	    notifyBuild(currentBuild.result)
 	}
}

def notifyBuild(String buildStatus) {
  buildStatus =  buildStatus ?: 'SUCCESSFUL'
  BUILD_STARUS = "${buildStatus}"

  def colorName = 'RED'
  def colorCode = '#FF0000'
  def subject = "${buildStatus}: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]'"
  def summary = "${subject} (${env.BUILD_URL})"
  def details = """<p>STARTED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]':</p>
    <p>Check console output at &QUOT;<a href='${env.BUILD_URL}'>${env.JOB_NAME} [${env.BUILD_NUMBER}]</a>&QUOT;</p>"""

 if (buildStatus == 'SUCCESSFUL') {
    color = 'GREEN'
    colorCode = '#00FF00'
  } else {
    color = 'RED'
    colorCode = '#FF0000'
  }

  //slackSend (color: colorCode, message: summary)

  emailext (
  	   body: '${JELLY_SCRIPT,template="html"}', recipientProviders: [[$class: 'CulpritsRecipientProvider'], [$class: 'DevelopersRecipientProvider'], [$class: 'RequesterRecipientProvider']], replyTo: "${DEFAULT_REPLYTO}", subject: "$PROJECT_NAME - Build # $BUILD_NUMBER - ${BUILD_STATUS}!", to: 'vasanth@egovernments.org' //$DEFAULT_RECIPIENTS'
    )
}
