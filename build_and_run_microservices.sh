#!/bin/bash
SERVICES=(locationassignment employeeassignment persist indexing)
for d in "${SERVICES[@]}"; do
 echo "Service name:"$d
dir_path=egov-pgr-$d
image_name=egovio/pgr-$d:latest
cd $dir_path
 echo "Directory Path:"$dir_path
 echo "Image name:"$image_name
mvn clean package
sudo docker build -t $image_name .
cd ..
done
cd egov-pgrrest/
