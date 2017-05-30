FROM nginx:1.11.10-alpine

ENV WEB_DIR=/var/web/wcms-web
RUN mkdir -p ${WEB_DIR}
COPY ./app ${WEB_DIR}/app
COPY ./resources ${WEB_DIR}/resources
COPY ./favicon.png ${WEB_DIR}/favicon.png
COPY ./index.html ${WEB_DIR}/index.html
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
