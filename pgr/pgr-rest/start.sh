#!/bin/sh

if [[ -z "${JAVA_OPTS}" ]];then
    export JAVA_OPTS="-Xmx64m -Xms64m"
fi

exec java ${JAVA_OPTS} -jar /opt/egov/pgr-rest.jar
