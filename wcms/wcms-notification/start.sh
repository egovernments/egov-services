/Users/senthil/eGov/source/egov-services/wcms/wcms-notification/start.sh#!/bin/sh

if [[ -z "${JAVA_OPTS}" ]];then
    export JAVA_OPTS="-Xmx64m -Xms64m"
fi

java ${JAVA_OPTS} -jar /opt/egov/wcms-notification.jar