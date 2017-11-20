curl -X PUT -u "elastic:changeme" 'localhost:9200/openingbalance/_mapping/openingbalance?pretty' -H 'Content-Type: application/json' -d @update_opening_balance_mapping.json
