#FROM egovio/apline-jre:8u121
FROM egovio/ci:0.0.4

MAINTAINER Sivaprakash Ramasamy<sivaprakash.ramasamy@tarento.com>

#RUN apk update && apk add git && git clone https://github.com/pinterest/secor.git && cd secor && mvn package
# copy pre-built JAR into image
#
# INSTRUCTIONS ON HOW TO BUILD JAR:
# Move to the location where pom.xml is exist in project and build project using below command
# "mvn clean package"
RUN mkdir -p /opt/secor
ADD target/secor-*-bin.tar.gz /opt/secor/
COPY properties/* /opt/secor/
COPY src/main/scripts/docker-entrypoint.sh /docker-entrypoint.sh
RUN chmod +x /docker-entrypoint.sh

ENTRYPOINT ["/docker-entrypoint.sh"]
