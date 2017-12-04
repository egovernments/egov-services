#!/bin/bash

#switch context to dev
kubectl config use-context dev

#Get dev db podname
playground_pod=$(kubectl get pods --namespace=playground | grep playground |awk {'print $1'})

#Execute command to take db dump
local_dump="devdb_$(date +"%d-%m-%y").sql"
# local_dump_hr="devdb_hr_$(date +"%d-%m-%y").sql"
kubectl exec -it --namespace=playground ${playground_pod} -- pg_dump -d  egov_dev_ms -U egovdev  -h egov-micro-dev.ckqm8nba5pj5.ap-southeast-1.rds.amazonaws.com --no-owner> /tmp/${local_dump}
echo "You can find Dev DB dump under the location /tmp/${local_dump}"
# kubectl exec -it --namespace=backbone ${postgres_pod} -- pg_dump -U postgres -d devdb_hr > /tmp/${local_dump_hr}
