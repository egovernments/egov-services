FROM nginx:1.11.10-alpine

ENV WEB_DIR=/var/lams-web
COPY ./nginx/lams.conf /etc/nginx/conf.d/default.conf
COPY ./app ${WEB_DIR}/app
COPY ./assets ${WEB_DIR}/assets
COPY ./index.html ${WEB_DIR}/
