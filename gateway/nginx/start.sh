#!/bin/sh
acme-client -a https://letsencrypt.org/documents/LE-SA-v1.2-November-15-2017.pdf -Nnmv $DOMAIN && renew=1
sed "s/DOMAIN/$DOMAIN/g" /tmp/default_ssl.conf > /etc/nginx/conf.d/default_ssl.conf
nginx -g "daemon off;"
