#!/bin/bash
SERVICES=(pgr/pgr-rest pgr/pgr-location-enrichment core/egov-location)
for d in "${SERVICES[@]}"; do
    module=`echo "core/egov-notification" | awk '{split($0,a,"/"); print a[1]}'
    service=`echo "core/egov-notification" | awk '{split($0,a,"/"); print a[2]}'
    echo "Module name:"${module}
    echo "Service name:"${service}
    image_name=egovio/${service}:latest
    cd ${module}
    mvn clean package
    sudo docker build -t $image_name .
    cd ..
done
pwd
