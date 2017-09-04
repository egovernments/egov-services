var dat = {
	"tl.create": {
		"numCols": 12/2,
		"url": "/tl-masters/category/v1/_create",
		"useTimestamp": true,
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
							"pattern": "^.[a-zA-Z. ]{2,99}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Enter Valid Trade Owner Name (Min:3, Max:100)",
							"maxLength": "100"
						},
						{
							"name": "code",
							"jsonPath": "categories[0].code",
							"label": "tl.create.groups.categorytype.code",
							"pattern": "^.[A-Za-z0-9]{0,19}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Enter Valid Code (Alpha-Numeric, Max:20)",
							"maxLength": "20"
						},
						{
							"name": "active",
							"jsonPath": "categories[0].active",
							"label": "tl.create.groups.categorytype.active",
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
		"url": "/tl-masters/category/v1/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "categories",
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
              "url": "/tl-masters/category/v1/_search?|$..id|$..name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}
		],
		"result": {
			"header": [{label: "tl.create.groups.categorytype.name"},{label: "tl.create.groups.categorytype.code"}, {label: "tl.create.groups.categorytype.active"}],
			"values": ["code","name", "active"],
			"resultPath": "categories",
			"rowClickUrlUpdate": "/update/tl/CreateLicenseCategory/{id}",
			"rowClickUrlView": "/view/tl/CreateLicenseCategory/{id}"
			}
	},
	"tl.view": {
		"numCols": 12/2,
		"url": "/tl-masters/category/v1/_search?id={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "categories",
		"groups": [
			{
				"label": "tl.view.groups.categorytype.title",
				"name": "viewCategoryType",
				"fields": [
					{
						"name": "name",
						"jsonPath": "categories[0].name",
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
						"jsonPath": "categories[0].code",
						"label": "tl.view.groups.categorytype.code",
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
						"label": "tl.view.groups.categorytype.active",
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
	"tl.update": {
		"numCols": 12/2,
		"searchUrl": "/tl-masters/category/v1/_search?id={id}",
		"url": "/tl-masters/tl-tradelicense/category/v1/{categories.code}/_update",
		"isResponseArray":true,
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "categories",
		"groups": [
			{
				"label": "tl.update.groups.categorytype.title",
				"name": "createCategoryType",
				"fields": [
					{
						"name": "name",
						"jsonPath": "categories.name",
						"label": "tl.update.groups.categorytype.name",
						"pattern": "^.[a-zA-Z. ]{2,49}$",
						"type": "text",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "Enter Valid Name"
					},
					{
						"name": "code",
						"jsonPath": "categories.code",
						"label": "tl.update.groups.categorytype.code",
						"pattern": "^.[A-Za-z0-9]{14,14}$",
						"type": "text",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "Enter 15 digit Alpha/Numeric Code"
					},
					{
						"name": "active",
						"jsonPath": "categories.active",
						"label": "tl.update.groups.categorytype.active",
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
	}
}

export default dat;
