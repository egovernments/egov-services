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
#sed "s#ULB_JS_URL#$ULB_JS_URL#g";"s#TELEMETRY_JS_URL#$ULB_JS_URL#g" /tmp/sub_filter.conf > /etc/nginx/conf.d/sub_filter.conf
echo "$KIBANA_CREDENTIALS" > /etc/nginx/.htpasswd
echo "$JAEGER_CREDENTIALS" > /etc/nginx/.jaegerhtpasswd
nginx -g "daemon off;"