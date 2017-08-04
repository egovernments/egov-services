var dat = {
	"tl.create": {
		"numCols": 12/2,
		"url": "/tl-masters/category/_create",
		"tenantIdRequired": true,
		"objectName": "categories",
		"groups": [
			{
				"label": "tl.create.categorytype.title",
				"name": "createLicenseCategoryType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "categories[0].name",
							"label": "tl.create.groups.categorytype.name",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "code",
							"jsonPath": "categories[0].code",
							"label": "tl.create.groups.categorytype.code",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "active",
							"jsonPath": "categories[0].active",
							"label": "Active",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"defaultValue":true
						}
				]
			}
		]
	},
	"tl.search": {
		"numCols": 12/1,
		"url": "/v1/category/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "tl.search.groups.categorytype.title",
				"name": "createCategoryType",
				"fields": [
						{
							"name": "category",
							"jsonPath": "name",
							"label": "tl.search.groups.categorytype.category",
							"pattern": "",
							"type": "singleValueList",
              "url": "",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}
		],
		"result": {
			"header": [{label: "wc.create.code"},{label: "wc.search.result.categoryType"}, {label: "wc.search.result.description"}, {label: "wc.search.result.active"}],
			"values": ["code","name", "description", "active"],
			"resultPath": "CategoryTypes",
			"rowClickUrlUpdate": "/update/wc/categoryType/{id}",
			"rowClickUrlView": "/view/wc/categoryType/{id}"
			}
	},
	"tl.view": {
		"numCols": 12/2,
		"url": "/v1/category/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "tl.view.groups.categorytype.title",
				"name": "viewCategoryType",
				"fields": [
					{
						"name": "name",
						"jsonPath": "categories.name",
						"label": "tl.view.groups.categorytype.name",
						"pattern": "",
						"type": "text",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "code",
						"jsonPath": "categories.code",
						"label": "tl.view.groups.categorytype.code",
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
	},
	"tl.update": {
		"numCols": 12/2,
		"searchUrl": "/v1/category/_search",
		"url":"/v1/category/_update",
		"isResponseArray":true,
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "tl.update.groups.categorytype.title",
				"name": "createCategoryType",
				"fields": [
					{
						"name": "name",
						"jsonPath": "categories.name",
						"label": "tl.update.groups.categorytype.name",
						"pattern": "",
						"type": "text",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "code",
						"jsonPath": "categories.code",
						"label": "tl.update.groups.categorytype.code",
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
