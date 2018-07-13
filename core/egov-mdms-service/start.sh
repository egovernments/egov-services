#!/bin/sh

cd /opt && wget "https://codeload.github.com/egovernments/egov-mdms-data/zip/master" -O master.zip && unzip master.zip && rm -rf master.zip mdms && mv egov-mdms-data-master mdms 

if [[ -z "${JAVA_OPTS}" ]];then
    export JAVA_OPTS="-Xmx64m -Xms64m"
fi

java ${JAVA_OPTS} -jar /opt/egov/egov-mdms-service-test.jar