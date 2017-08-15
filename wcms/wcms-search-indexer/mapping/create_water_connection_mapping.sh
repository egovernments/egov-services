curl -X PUT -u "elastic:changeme" 'localhost:9200/water-connection/_mapping/WaterConnection?pretty' -H 'Content-Type: application/json' -d @create_water_connection_mapping.json
