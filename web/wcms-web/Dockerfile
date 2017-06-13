FROM nginx:1.11.10-alpine

ENV WEB_DIR=/var/web/wcms-web
RUN mkdir -p ${WEB_DIR}
COPY ./public ${WEB_DIR}/public
COPY ./src ${WEB_DIR}/src
COPY ./index.html ${WEB_DIR}/index.html
COPY ./favicon.ico ${WEB_DIR}/favicon.ico
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
