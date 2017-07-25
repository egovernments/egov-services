var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url":  "/wcms/masters/documenttype/_create",
		"tenantIdRequired": true,
		"objectName": "documentType",
		"groups": [
			{
				"label": "wc.create.documentType.title",
				"name": "createDocumentType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "documentType.name",
							"label": "Document Type*",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "documentType.description",
							"label": "Description",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Active",
							"jsonPath": "documentType.active",
							"label": "Active",
							"pattern": "",
							"type": "checkbox",
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
