FROM nginx:1.11.10-alpine

ENV WEB_DIR=/var/web/maha-dma
RUN mkdir -p ${WEB_DIR}
COPY ./index.html ${WEB_DIR}/index.html
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
