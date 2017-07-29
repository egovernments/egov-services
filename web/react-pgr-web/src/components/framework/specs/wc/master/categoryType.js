var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/categorytype/_create",
		"tenantIdRequired": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "wc.create.categorytype.title",
				"name": "createCategoryType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "CategoryType.name",
							"label": "Category Type",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "CategoryType.description",
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
							"jsonPath": "CategoryType.active",
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
		"url": "/wcms/masters/categorytype/_search",
		"tenantIdRequired": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "wc.search.categorytype.title",
				"name": "createCategoryType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "CategoryType.name",
							"label": "Category Type",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Active",
							"jsonPath": "CategoryType.active",
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
	"wc.view": {
		"numCols": 12/3,
		"url": "/wcms/masters/categorytype/_search?id={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "wc.create.categorytype.title",
				"name": "createCategoryType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "CategoryTypes[0].name",
							"label": "Category Type",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "CategoryTypes[0].description",
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
							"jsonPath": "CategoryTypes[0].active",
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
	"wc.update": {
		"numCols": 12/3,
		"searchUrl": "/wcms/masters/categorytype/_search?id={id}",
		"url":"/wcms/masters/categorytype/{CategoryTypes[0].code}/_update",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "CategoryTypes",
		"groups": [
			{
				"label": "wc.create.categorytype.title",
				"name": "createCategoryType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "CategoryTypes[0].name",
							"label": "Category Type",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "CategoryTypes[0].description",
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
							"jsonPath": "CategoryTypes[0].active",
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
		"searchUrl": "/wcms/masters/categorytype/_search?id={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "wc.create.categorytype.title",
				"name": "createCategoryType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "CategoryTypes[0].name",
							"label": "Category Type",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "CategoryTypes[0].description",
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
							"jsonPath": "CategoryTypes[0].active",
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
