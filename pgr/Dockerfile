FROM egovio/apline-jre:8u121

ARG STARTUP_SCRIPT

MAINTAINER Sumanth<nsready@gmail.com>

COPY ./common-target /opt/egov/jars

COPY ./dev-box-startup-scripts/${STARTUP_SCRIPT} /usr/bin/startup.sh

RUN chmod +x /usr/bin/startup.sh

CMD ["bin/bash","/usr/bin/startup.sh"]
