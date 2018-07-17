#!/bin/sh
sed "s/DOMAIN/$DOMAIN/g" /tmp/default_ssl.conf > /etc/nginx/conf.d/default_ssl.conf
nginx -g "daemon off;"