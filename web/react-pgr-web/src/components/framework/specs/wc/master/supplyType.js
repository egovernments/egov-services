var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/supplytype/_create",
		"tenantIdRequired": true,
		"objectName": "SupplyType",
		"groups": [
			{
				"label": "wc.create.supplyType.title",
				"name": "createSupplyType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "SupplyType.name",
							"label": "Supply Type",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "SupplyType.description",
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
							"jsonPath": "SupplyType.active",
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
	},
	"wc.search": {
		"numCols": 12/3,
		"url": "/wcms/masters/supplytype/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "SupplyType",
		"groups": [
			{
				"label": "wc.search.supplyType.title",
				"name": "searchSupplyType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "name",
							"label": "wc.create.supplyType",
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
			"header": [{label: "wc.create.code"},{label: "wc.create.supplyType"}, {label: "wc.search.result.description"}, {label: "wc.search.result.active"}],
			"values": ["code","name", "description", "active"],
			"resultPath": "SupplyTypes",
			"rowClickUrlUpdate": "/update/wc/supplyType/{id}",
			"rowClickUrlView": "/view/wc/supplyType/{id}"
			}
	},
	"wc.view": {
		"numCols": 12/3,
		"url": "/wcms/masters/supplytype/_search?id={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "SupplyType",
		"groups": [
			{
				"label": "wc.view.supplytype.title",
				"name": "viewSupplyType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "SupplyTypes[0].name",
							"label": "wc.create.supplyType",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "SupplyTypes[0].description",
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
							"jsonPath": "SupplyTypes[0].active",
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
		"searchUrl": "/wcms/masters/supplytype/_search?id={id}",
		"url":"/wcms/masters/supplytype/{SupplyType.code}/_update",
		"isResponseArray":true,
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "SupplyType",
		"groups": [
			{
				"label": "wc.update.supplyType.title",
				"name": "updateSupplyType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "SupplyType.name",
							"label": "wc.create.supplyType",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "SupplyType.description",
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
							"jsonPath": "SupplyType.active",
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
