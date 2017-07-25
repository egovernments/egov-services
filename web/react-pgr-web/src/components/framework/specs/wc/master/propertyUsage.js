var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/propertytype-usagetype/_create",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "propertyUsage",
		"groups": [
			{
				"label": "wc.create.propertyUsage.title",
				"name": "propertyUsage",
				"fields": [
					{
						"name": "propertyType",
						"jsonPath": "propertyUsage.propertyType",
						"label": "wc.create.propertytype",
						"pattern": "",
						"type": "singleValueList",
						"url": "/pt-property/property/propertytypes/_search?tenantId={tenantId}|$..id|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "usageType",
						"jsonPath": "propertyUsage.usageType",
						"label": "wc.create.usageType",
						"pattern": "",
						"type": "singleValueList",
						"url": "/pt-property/property/usages/_search?tenantId={tenantId}|$..id|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
						{
							"name": "Active",
							"jsonPath": "propertyUsage.active",
							"label": "Active",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"isDisabled": false,
							"default": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}
		]
	}
}

export default dat;
