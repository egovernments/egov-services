var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/propertytype-categorytype/_create",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "propertyCategory",
		"groups": [
			{
				"label": "wc.create.propertyCategory.title",
				"name": "createpropertyCategory",
				"fields": [
					{
						"name": "propertyType",
						"jsonPath": "createpropertyCategory.propertyType",
						"label": "wc.create.propertytype",
						"pattern": "",
						"type": "singleValueList",
						"url": "/pt-property/property/propertytypes/_search?tenantId={tenantId}|$..id|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "categoryType",
						"jsonPath": "createpropertyCategory.categoryType",
						"label": "wc.create.categoryType",
						"pattern": "",
						"type": "singleValueList",
						"url": "/wcms/masters/categorytype/_search?tenantId={tenantId}|$..code|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
						{
							"name": "Active",
							"jsonPath": "createpropertyCategory.active",
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
