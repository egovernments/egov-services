FROM nginx:1.11.10-alpine

ENV WEB_DIR=/var/wcms-web
COPY ./nginx/wcms.conf /etc/nginx/conf.d/default.conf
COPY ./app ${WEB_DIR}/app
COPY ./resources ${WEB_DIR}/resources
COPY ./index.html ${WEB_DIR}/
