var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/sourcetype/_create",
		"tenantIdRequired": true,
		"objectName": "SourceType",
		"groups": [
			{
				"label": "wc.create.sourceType.title",
				"name": "createSourceType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "SourceType.name",
							"label": "Source Type",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "SourceType.description",
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
							"jsonPath": "SourceType.active",
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
