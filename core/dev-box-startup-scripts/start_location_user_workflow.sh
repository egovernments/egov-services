nohup java -jar /opt/egov/jars/egov-location.jar --server.port=9001 > /var/log/egov-location.log 2>&1 &

nohup java -jar /opt/egov/jars/egov-user.jar --server.port=9002 > /var/log/egov-user.log 2>&1 &

java -jar /opt/egov/jars/egov-workflow.jar --server.port=9003 2>&1 | tee /var/log/egov-workflow.log