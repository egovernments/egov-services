nohup java -jar /opt/egov/jars/pgr-rest.jar --server.port=9001 > /var/log/pgr-rest.log 2>&1 &

nohup java -jar /opt/egov/jars/pgr-persist.jar > /var/log/pgr-persist.log 2>&1 &

java -jar /opt/egov/jars/pgr-search-indexer.jar 2>&1 | tee /var/log/pgr-search-indexer.log