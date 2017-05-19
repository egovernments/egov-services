nohup java -jar /opt/egov/jars/pgr-crn-generation.jar --server.port=9001 > /var/log/pgr-crn-generation.log 2>&1 &

nohup java -jar /opt/egov/jars/pgr-employee-enrichment.jar > /var/log/pgr-employee-enrichment.log 2>&1 &

java -jar /opt/egov/jars/pgr-location-enrichment.jar 2>&1 | tee /var/log/pgr-location-enrichment.log