FROM 2.7.13-alpine

MAINTAINER Ajay<ajay_886@hotmail.com>

ARG APP_PATH=/opt/alert/

RUN MKDIR ${APP_PATH}
COPY rules/ ${APP_PATH}
COPY ./requirements.txt ${APP_PATH}
COPY ./entrypoint.sh ${APP_PATH}

WORKDIR ${APP_PATH}

CMD ["entrypoint.sh"]

