#!/bin/sh
sed -e "s/DOMAIN/$DOMAIN/g" -e "s#ASSET_BUCKET_URL#$ASSET_BUCKET_URL#g" /tmp/default_ssl.conf > /etc/nginx/conf.d/default_ssl.conf
if [[ "$SUBFILTER" = "YES" ]]; then
echo "sub_filter  '<head>' '" >> /etc/nginx/conf.d/sub_filter.conf
  if [[ -n "$ULB_JS_URL" ]]; then
    echo "<script src="$ULB_JS_URL" type="text/javascript"></script>" >> /etc/nginx/conf.d/sub_filter.conf
  fi

  if [[ -n "$TELEMETRY_JS_URL" ]]; then
    echo "<script src="$TELEMETRY_JS_URL" type="text/javascript"></script>" >> /etc/nginx/conf.d/sub_filter.conf
  fi
echo "<"/"head>';" >> /etc/nginx/conf.d/sub_filter.conf
echo "sub_filter_once on;" >> /etc/nginx/conf.d/sub_filter.conf
fi
if [[ "$PMIDC" = "YES" ]]; then
sed -i '$iset $pmidc http://13.127.211.3:8080;\nlocation /pmidc {\
proxy_set_header  Host $host;\nproxy_set_header  X-Real-IP $remote_addr; \
proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for; \
proxy_set_header  X-Forwarded-Proto $scheme; \nclient_max_body_size 40M; \
proxy_pass $pmidc$request_uri;\nproxy_pass_request_headers on;\nproxy_read_timeout 90; \
}\n' /etc/nginx/conf.d/default_ssl.conf
fi
#sed "s#ULB_JS_URL#$ULB_JS_URL#g";"s#TELEMETRY_JS_URL#$ULB_JS_URL#g" /tmp/sub_filter.conf > /etc/nginx/conf.d/sub_filter.conf
printf "$KIBANA_CREDENTIALS" > /etc/nginx/.htpasswd
echo "$JAEGER_CREDENTIALS" > /etc/nginx/.jaegerhtpasswd
nginx -g "daemon off;"
