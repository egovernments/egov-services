# eGov Api Tests

This project contains automated api tests for different egov modules. The tech stack used for the project are:
1. **JAVA** as the programming language for writing test code
3. **TestNg** as framework
4. **Gradle** as the build tool 
5. **IntelliJ** as the preferred IDE for writing java code.


#### Getting Started
Setup your machine. 
1. Install JDK 1.8
2. Install IntelliJ (Community edition is fine)
3. Install cucumber for java plugin in intellij
4. Install gradle

Once setup, checkout the egov project from here ```https://github.com/egovernments/eGov.git```
Switch to branch **egov-functional-test-automation**
Create a new project using existing source from eGov/egov/egov_functional_test using build.gradle
You should be all set.

#### Running tests
1. You can run the test from IntelliJ directly, by right clicking and **Run test**..
2. From command line use the following commands to execute in particular environment
    
    For running all tests : "gradle egov -Denv=dev -Dtags=<tagName>"
    
                                        or 
                                        
                            "gradle egov -Denv=qa -Dtags=<tagName>"
    
    In QA Env : "gradle clean build Qa -Denv=qa"
    
    In DEV Env : "gradle clean build Dev -Denv=dev"
    
3. For windows user: from command prompt line use the following commands to execute in particular environment
                                                                    
    For running all tests : "gradlew clean build Sanity -Denv=qa" 
                                                                    
                                          or 
                                                                                                        
                             "gradlew clean build Sanity -Denv=dev"
                                                                    
    In QA Env : "gradlew clean build Qa -Denv=qa"
                                                                    
    In DEV Env : "gradlew clean build Dev -Denv=dev"