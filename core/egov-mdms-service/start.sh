#!/bin/sh

cd /opt && wget "$EGOV_MDMS_GIT_URL${BRANCH:-master}" -O master.zip && unzip master.zip && rm -rf master.zip mdms && mv "$EGOV_MDMS_FOLDER-${BRANCH:-master}" mdms

if [[ -z "${JAVA_OPTS}" ]];then
    export JAVA_OPTS="-Xmx64m -Xms64m"
fi

java ${JAVA_OPTS} -jar /opt/egov/egov-mdms-service-test.jar