FROM nginx:1.11.10-alpine

ENV WEB_DIR=/var/financials-web
RUN mkdir -p ${WEB_DIR}/resources
RUN mkdir -p ${WEB_DIR}/templates
COPY ./nginx/financials_qa.conf /etc/nginx/conf.d/default.conf
COPY ./resources ${WEB_DIR}/resources
COPY ./templates ${WEB_DIR}/templates
COPY ./index.html ${WEB_DIR}/
