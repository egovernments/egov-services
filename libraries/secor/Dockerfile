#FROM egovio/apline-jre:8u121
FROM egovio/ci:0.0.4

MAINTAINER Sivaprakash Ramasamy<sivaprakash.ramasamy@tarento.com>

RUN apk update && apk add git && git clone https://github.com/pinterest/secor.git && cd secor && mvn package
# copy pre-built JAR into image
#
# INSTRUCTIONS ON HOW TO BUILD JAR:
# Move to the location where pom.xml is exist in project and build project using below command
# "mvn clean package"
RUN mkdir -p /opt/egov
RUN target/secor-0.27-SNAPSHOT-bin.tar.gz -C /opt/egov/

RUN cd /opt/egov
COPY start.sh /usr/bin/start.sh

RUN chmod +x /usr/bin/start.sh

CMD ["/usr/bin/start.sh"]
