curl -X PUT -u "elastic:changeme" 'localhost:9200/service-request/_mapping/ServiceRequest?pretty' -H 'Content-Type: application/json' -d @update_service_request_mapping.json
