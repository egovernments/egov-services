FROM egovio/nginx-alpine:1.13.8
RUN apk update && apk add acme-client libressl
RUN mkdir /var/www/acme
COPY ./nginx.conf /etc/nginx/conf.d/default.conf