#!/bin/bash

set -ex
echo "Which environment you want to burst cache? :"

read kubeenv

kubectl config use-context $kubeenv

#Get localization  podname
localization=$(kubectl get pods --namespace=egov | grep egov-localization |awk {'print $1'})

for pod in $localization; do
kubectl exec -it ${pod}  -n egov -- sh -c "apk update; apk add curl; curl -X POST http://localhost:8080/localization/messages/cache-bust;"
done
