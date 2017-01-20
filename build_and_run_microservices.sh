#!/bin/bash
SERVICES=(notification pgr-locationassignment pgr-employeeassignment pgr-persist pgr-indexing)
for d in "${SERVICES[@]}"; do
 echo "Service name:"$d
dir_path=egov-$d
image_name=egovio/$d:latest
cd $dir_path
 echo "Directory Path:"$dir_path
 echo "Image name:"$image_name
mvn clean package
sudo docker build -t $image_name .
cd ..
done
pwd
cd egov-pgrrest/
