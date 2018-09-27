#!/bin/sh
sed "s/DOMAIN/$DOMAIN/g" /tmp/default_ssl.conf > /etc/nginx/conf.d/default_ssl.conf
sed "s#ULB_JS_URL#$ULB_JS_URL#g" /tmp/sub_filter.conf > /etc/nginx/conf.d/sub_filter.conf
echo "$KIBANA_CREDENTIALS" > /etc/nginx/.htpasswd
nginx -g "daemon off;"