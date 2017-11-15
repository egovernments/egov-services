curl -X PUT -u "elastic:changeme" 'localhost:9200/stores/_mapping/Store?pretty' -H 'Content-Type: application/json' -d @update_store_mapping.json
