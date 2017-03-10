#!/bin/bash

if [[ -z "${JAVA_OPTS}" ]];then
    export JAVA_OPTS="-Xmx64m -Xms64m"
fi

java ${JAVA_OPTS} -jar /opt/egov/egov-eis.jar
