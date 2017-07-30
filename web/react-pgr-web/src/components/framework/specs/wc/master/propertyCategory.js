var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/propertytype-categorytype/_create",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "PropertyTypeCategoryType",
		"groups": [
			{
				"label": "wc.create.propertyCategory.title",
				"name": "PropertyTypeCategoryType",
				"fields": [
					{
						"name": "propertyType",
						"jsonPath": "PropertyTypeCategoryType.propertyTypeName",
						"label": "wc.create.propertytype",
						"pattern": "",
						"type": "singleValueList",
						"url": "/pt-property/property/propertytypes/_search?|$..name|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "categoryType",
						"jsonPath": "PropertyTypeCategoryType.categoryTypeName",
						"label": "wc.create.categoryType",
						"pattern": "",
						"type": "singleValueList",
						"url": "/wcms/masters/categorytype/_search?|$..name|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
						{
							"name": "Active",
							"jsonPath": "PropertyTypeCategoryType.active",
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
