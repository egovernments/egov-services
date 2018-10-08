#!/bin/sh

if [[ -z "${JAVA_OPTS}" ]];then
    export JAVA_OPTS="-Xmx64m -Xms64m"
fi

if [ -z "$TARGET_ENV" ]; then
    export TARGET_ENV=default
fi

#if [[ -z "${NEWRELIC_MONITORING_ENABLED}" ]]; then
#  export APP_NAME=$(echo $HOSTNAME |  rev | cut -d - -f3- | rev)
#  sed -i 's/license_key: LICENSE_KEY/license_key: '"$NEWRELIC_LICENSE_KEY"'/' /opt/egov/newrelic.yml
#  sed -i 's/app_name: APP_NAME/app_name: '"$TARGET_ENV-$APP_NAME"'/' /opt/egov/newrelic.yml
#  java ${JAVA_OPTS} -javaagent:/opt/egov/newrelic.jar -jar /opt/egov/egov-user.jar
#else
  java ${JAVA_OPTS} -jar /opt/egov/egov-user.jar
#fi
