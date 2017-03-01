#!/bin/bash

rm common-target/*

for f in ./*;
    do
        if [ -d $f ]; then
            cd $f;
            if [ -f pom.xml ]; then
                if [ -f settings.xml ]; then
                    mvn clean verify package -s settings.xml
                else
                    mvn clean verify package
                fi
            fi

            if [ -d target ]; then
                cp target/*.jar ../common-target/$f.jar
            fi
            cd ..
        fi
    done

sudo docker build --build-arg STARTUP_SCRIPT=start_crn_employee_location.sh -t nsready/pgr_crn_employee_location:latest .
sudo docker build --build-arg STARTUP_SCRIPT=start_rest_persist_search.sh -t nsready/pgr_rest_persist_search:latest .