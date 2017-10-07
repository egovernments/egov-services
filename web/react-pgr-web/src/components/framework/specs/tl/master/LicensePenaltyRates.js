var dat = {
	"tl.create": {
		"numCols": 12/2,
		"url": "/tl-masters/penaltyrate/v1/_create",
		"useTimestamp": true,
		"tenantIdRequired": true,
		"objectName": "penaltyRates",
		"groups": [
			{
				"label": "tl.create.penaltyRates.title",
				"name": "createpenaltyRates",
				"fields": [
					{
						"name": "applicationType",
						"jsonPath": "applicationType",
						"label": "tl.create.groups.penaltyRates.applicationtype",
						"pattern": "",
						"type": "singleValueList",
						"url": "",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"defaultValue": [
					{
						"key": "NEW",
						"value": "NEW"
					},
					{
						"key": "RENEW",
						"value": "RENEW"
					}
						]
					}
				]
			},
			{
				"label": "",
				"name": "createpenaltyRates",
				"fields": [
					{
						"name": "applicationType",
						"jsonPath": "applicationType",
						"label": "tl.create.groups.penaltyRates.applicationtype",
						"pattern": "",
						"type": "singleValueList",
						"url": "",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
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
							"jsonPath": "ids",
							"label": "tl.search.groups.categorytype.category",
							"pattern": "",
							"type": "singleValueList",
              "url": "/tl-masters/category/v1/_search?tenantId=default|$..id|$..name",
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
			"values": ["name","code", "active"],
			"resultPath": "categories",
			"rowClickUrlUpdate": "/update/tl/CreateLicenseCategory/{id}",
			"rowClickUrlView": "/view/tl/CreateLicenseCategory/{id}"
			}
	},
	"tl.view": {
		"numCols": 12/2,
		"url": "/tl-masters/category/v1/_search?ids={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "categories[0]",
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
		"searchUrl": "/tl-masters/category/v1/_search?ids={id}",
		"url": "/tl-masters/category/v1/_update",
		"isResponseArray":true,
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "categories[0]",
		"groups": [
			{
				"label": "tl.update.groups.categorytype.title",
				"name": "createCategoryType",
				"fields": [
					{
						"name": "name",
						"jsonPath": "categories[0].name",
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
						"jsonPath": "categories[0].code",
						"label": "tl.update.groups.categorytype.code",
						"pattern": "^.[A-Za-z0-9]{14,14}$",
						"type": "text",
						"isRequired": true,
						"isDisabled": true,
						"requiredErrMsg": "",
						"patternErrMsg": "Enter 15 digit Alpha/Numeric Code"
					},
					{
						"name": "active",
						"jsonPath": "categories[0].active",
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
