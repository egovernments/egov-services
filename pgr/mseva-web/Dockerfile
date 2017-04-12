FROM nginx:1.11.10-alpine

ENV WEB_DIR=/var/mseva-web
RUN mkdir -p ${WEB_DIR}/fonts
RUN mkdir -p ${WEB_DIR}/images
RUN mkdir -p ${WEB_DIR}/js
RUN mkdir -p ${WEB_DIR}/static
RUN mkdir -p ${WEB_DIR}/styles
COPY ./nginx/mseva.conf /etc/nginx/conf.d/default.conf
COPY ./fonts ${WEB_DIR}/fonts
COPY ./images ${WEB_DIR}/images
COPY ./js ${WEB_DIR}/js
COPY ./static ${WEB_DIR}/static
COPY ./styles ${WEB_DIR}/styles
COPY ./asset-manifest.json ${WEB_DIR}/
COPY ./favicon.png ${WEB_DIR}/
COPY ./index.html ${WEB_DIR}/