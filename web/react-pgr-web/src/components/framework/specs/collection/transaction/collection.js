var dat = {
	"collection.transaction": {
		"numCols": 12/2,
		"url":  "/billing-service/bill/_generate?tenantId=default&consumerCode=consumerCode&businessService=businessService",
		"tenantIdRequired": true,
		"objectName": "search",
		"groups": [
			{
				"label": "collection.create.Search.title",
				"name": "createDocumentType",
				"fields": [
						{
							"name": "mobile",
							"jsonPath": "collection.search.mobile",
							"label": "Mobile",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "email",
							"jsonPath": "collection.search.email",
							"label": "Email",
							"pattern": "",
							"type": "email",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
  						"name": "billerService",
  						"jsonPath": "collection.search.billerService",
  						"label": "Billing service name",
  						"pattern": "",
  						"type": "singleValueList",
  						"isRequired": false,
  						"isDisabled": false,
  						"url": "/egov-common-masters/departments/_search?|$..id|$..name",
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  						},
						{
							"name": "consumerCode",
							"jsonPath": "collection.search.consumerCode",
							"label": "Consumer Code",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}
		]
	}
}

export default dat;
