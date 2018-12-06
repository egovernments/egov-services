#!/bin/sh

if [[ -z "${JAVA_OPTS}" ]];then
    export JAVA_OPTS="-Xmx192m -Xms192m"
fi

java ${JAVA_OPTS} -jar /opt/egov/egov-indexer.jar
