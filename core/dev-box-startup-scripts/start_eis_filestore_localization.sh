nohup java -jar /opt/egov/jars/egov-eis.jar --server.port=9001 > /var/log/egov-eis.log 2>&1 &

nohup java -jar /opt/egov/jars/egov-filestore.jar --server.port=9002 > /var/log/egov-filestore.log 2>&1 &

java -jar /opt/egov/jars/egov-localization.jar --server.port=9003 2>&1 | tee /var/log/egov-localization.log