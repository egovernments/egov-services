#!/bin/sh
 if [ -z "${JAVA_OPTS}" ];then
    export JAVA_OPTS="-Xmx64m -Xms64m"
fi

DATE=`cat /opt/datefile`
for i in $DATE; do
 java ${JAVA_OPTS} -jar /opt/egov/egov-telemetry-batch-process.jar $i
 sleep 30;
done
