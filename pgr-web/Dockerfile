FROM nginx:1.11.10-alpine

ENV WEB_DIR=/var/pgr-web
RUN mkdir -p ${WEB_DIR}/resources
COPY ./nginx/mseva_qa.conf /etc/nginx/conf.d/default.conf
COPY ./resources ${WEB_DIR}/resources
COPY ./index.html ${WEB_DIR}/
