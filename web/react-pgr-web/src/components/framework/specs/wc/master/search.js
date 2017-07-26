var dat = {
	"wc.create": {
		"numCols": 12/2,
		"url":  "/wcms/masters/documenttype/_create",
		"tenantIdRequired": true,
		"objectName": "search",
		"groups": [
			{
				"label": "wc.create.Search.title",
				"name": "createDocumentType",
				"fields": [
						{
							"name": "mobile",
							"jsonPath": "search.mobile",
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
							"jsonPath": "search.email",
							"label": "Email",
							"pattern": "",
							"type": "email",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
  						"name": "department",
  						"jsonPath": "connection.workflowDetails.department",
  						"label": "wc.create.groups.approvalDetails.fields.department",
  						"pattern": "",
  						"type": "singleValueList",
  						"isRequired": false,
  						"isDisabled": false,
  						"url": "/egov-common-masters/departments/_search?tenantId=default|$..id|$..name",
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  						},
						{
							"name": "consumerCode",
							"jsonPath": "search.consumerCode",
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
