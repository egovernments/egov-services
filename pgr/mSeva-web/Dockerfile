FROM nginx:1.11.10-alpine

ENV WEB_DIR=/var/mseva-web
COPY ./nginx/mseva.conf /etc/nginx/conf.d/default.conf
COPY ./app ${WEB_DIR}/app
