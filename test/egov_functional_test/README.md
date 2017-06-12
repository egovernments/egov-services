# eGov Functional Tests

This project contains automated functional tests for different egov modules. The tech stack used for the project are:
1. **Cucumber-jvm** as testing framework for writing test scenarios in BDD format
2. **JAVA** as the programming language for writing test code
3. **Selenium** to drive browser interaction
4. **Gradle** as the build tool 
5. **IntelliJ** as the preferred IDE for writing java code.


#### Getting Started
Setup your machine. 
1. Install JDK 1.8
2. Install IntelliJ (Community edition is fine)
3. Install cucumber for java plugin in intellij
4. Install gradle

Once setup, checkout the egov project from here ```https://github.com/egovernments/eGov.git```
Switch to branch **test-automation**
Create a new project using existing source from eGov/egov/egov_functional_test using build.gradle
You should be all set.

#### Running tests
1. You can run the test from IntelliJ directly, by right clicking and **Run feature file**.This will require you to set the VM configurations before trying to run the tests. Add the following VM params before running tests.
```-Dbrowser=chrome -Denv=staging``` This will run the tests using chrome browser and against the staging environment.
2. You can run directly from command line. Use the following command to run the tests
```gradle clean build runInSequence -Dbrowser=chrome -Denv=staging -Ptags=@Sanity```
This will run all tests that are tagged as @Sanity. 

