#!/bin/sh

if [[ -z "${JAVA_OPTS}" ]];then
    export JAVA_OPTS="-Xmx64m -Xms64m"
fi
cd /opt/egov
java -ea -Dsecor_group=secor_backup \
  -Dlog4j.configuration=log4j.prod.properties \
  -Dconfig=secor.prod.backup.properties \
  -cp secor-0.1-SNAPSHOT.jar:lib/* \
  com.pinterest.secor.main.ConsumerMain
