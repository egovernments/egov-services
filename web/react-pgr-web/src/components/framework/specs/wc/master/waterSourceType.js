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
							"label": "wc.create.sourceType",
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
							"label": "wc.create.description",
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
							"label": "wc.create.active",
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
	},
	"wc.search": {
		"numCols": 12/3,
		"url": "/wcms/masters/sourcetype/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "SourceType",
		"groups": [
			{
				"label": "wc.search.sourceType.title",
				"name": "searchSourceType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "name",
							"label": "wc.create.sourceType",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Active",
							"jsonPath": "active",
							"label": "wc.create.active",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}
		],
		"result": {
			"header": [{label: "wc.create.code"},{label: "wc.create.sourceType"}, {label: "wc.search.result.description"}, {label: "wc.search.result.active"}],
			"values": ["code","name", "description", "active"],
			"resultPath": "SourceTypes",
			"rowClickUrlUpdate": "/update/wc/waterSourceType/{id}",
			"rowClickUrlView": "/view/wc/waterSourceType/{id}"
			}
	},
	"wc.view": {
		"numCols": 12/3,
		"url": "/wcms/masters/sourcetype/_search?id={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "SourceType",
		"groups": [
			{
				"label": "wc.view.sourceType.title",
				"name": "viewSourceType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "SourceTypes[0].name",
							"label": "wc.create.sourceType",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "SourceTypes[0].description",
							"label": "wc.create.description",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Active",
							"jsonPath": "SourceTypes[0].active",
							"label": "wc.create.active",
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
	},
	"wc.update": {
		"numCols": 12/3,
		"searchUrl": "/wcms/masters/sourcetype/_search?id={id}",
		"url":"/wcms/masters/sourcetype/{SourceType.code}/_update",
		"isResponseArray":true,
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "SourceType",
		"groups": [
			{
				"label": "wc.update.sourceType.title",
				"name": "createSourceType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "SourceType.name",
							"label": "wc.create.sourceType",
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
							"label": "wc.create.description",
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
							"label": "wc.create.active",
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
