#!/bin/bash

#switch context to dev
kubectl config use-context dev

#Get dev db podname
postgres_pod=$(kubectl get pods --namespace=backbone | grep postgres | awk {'print $1'})

#Execute command to take db dump
local_dump="devdb_$(date +"%d-%m-%y").sql"
local_dump_hr="devdb_hr_$(date +"%d-%m-%y").sql"
kubectl exec -it --namespace=backbone ${postgres_pod} -- pg_dump -U postgres -d devdb > /tmp/${local_dump}
kubectl exec -it --namespace=backbone ${postgres_pod} -- pg_dump -U postgres -d devdb_hr > /tmp/${local_dump_hr}
