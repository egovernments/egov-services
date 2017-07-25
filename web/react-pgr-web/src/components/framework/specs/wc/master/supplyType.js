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
	}
}

export default dat;
