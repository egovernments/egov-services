# eGov Postman Collections

This project contains automated api tests for different egov modules. The tech stack used for the project are:
1. **PostMan** as testing tool for writing test 
2. **JAVASCRIPT** as the programming language for writing test code
3. **NewMan** as the build tool 
4. **PostMan** as the preferred IDE for writing javaScript code. 
5. **NODE** is need to install before newman.
6. **NPM** is need to install before newman.

#### Getting Started
Setup your machine. 

1. Install PostMan : https://www.getpostman.com/docs/postman/launching_postman/installation_and_updates

Linux:
================================
2. Install NPM : http://www.hostingadvice.com/how-to/install-nodejs-ubuntu-14-04/
3. Install Newman : https://www.npmjs.com/package/newman

Windows:
================================
1. Install NewMan : http://blog.getpostman.com/2015/04/09/installing-newman-on-windows/

Once setup, checkout the egov project from here ```https://github.com/egovernments/egov-services```
Switch to branch **egov-mr-functional-test-automation**
Go to egov_postman_collection
You should be all set.

#### Running tests
From PostMan - 

    when you are done with importing your collections and environment from the git folder you can open your runner window and select the collection folder and environment and start the test. 


From CommandLine via NewMan - 

         https://www.getpostman.com/docs/postman/collection_runs/command_line_integration_with_newman 

         refer above link for proper documentation for CommandLine newman.

Postman Documentation : https://www.getpostman.com/docs/
For Running in Jenkins : http://blog.getpostman.com/2015/09/03/how-to-write-powerful-automated-api-tests-with-postman-newman-and-jenkins/