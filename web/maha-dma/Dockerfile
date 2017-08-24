FROM nginx:1.11.10-alpine

ENV WEB_DIR=/var/web/maha-dma
RUN mkdir -p ${WEB_DIR}
COPY ./css ${WEB_DIR}/css
COPY ./data ${WEB_DIR}/data
COPY ./images ${WEB_DIR}/images
COPY ./vendor ${WEB_DIR}/vendor
COPY ./ulb-home.html ${WEB_DIR}/ulb-home.html
COPY ./index.html ${WEB_DIR}/index.html
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
