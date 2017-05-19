FROM nginx:1.11.10-alpine

ENV WEB_DIR=/var/web
RUN mkdir -p ${WEB_DIR}
COPY ./nginx.conf /etc/nginx/conf.d/default.conf
COPY ./asset-web ${WEB_DIR}/asset-web
COPY ./collections-web ${WEB_DIR}/collections-web
COPY ./hr-web ${WEB_DIR}/hr-web
COPY ./lams-web ${WEB_DIR}/lams-web
COPY ./mseva-web ${WEB_DIR}/mseva-web
COPY ./pgr-web ${WEB_DIR}/pgr-web
COPY ./propertytax-web ${WEB_DIR}/propertytax-web
COPY ./wcms-web ${WEB_DIR}/wcms-web
