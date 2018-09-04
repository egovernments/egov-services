#!/bin/sh
sed "s/DOMAIN/$DOMAIN/g" /tmp/default_ssl.conf > /etc/nginx/conf.d/default_ssl.conf
echo "$KIBANA_CREDENTIALS" > /etc/nginx/.htpasswd
nginx -g "daemon off;"