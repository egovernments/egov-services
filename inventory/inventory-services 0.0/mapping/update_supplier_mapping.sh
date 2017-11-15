curl -X PUT -u "elastic:changeme" 'localhost:9200/suppliers/_mapping/Supplier?pretty' -H 'Content-Type: application/json' -d @update_supplier_mapping.json
