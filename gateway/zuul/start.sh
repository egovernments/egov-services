#!/bin/sh

if [[ -z "${JAVA_OPTS}" ]];then
    export JAVA_OPTS="-Xmx32m -Xms32m"
fi

java ${JAVA_OPTS} -jar /opt/egov/zuul.jar
