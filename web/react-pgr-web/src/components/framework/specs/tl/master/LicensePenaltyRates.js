var dat = {
	"tl.create": {
		"numCols": 12/2,
		"url": "/tl-masters/penaltyrate/v1/_create",
		"useTimestamp": true,
		"tenantIdRequired": true,
		"objectName": "penaltyRatesOne",
		"groups": [
			{
				"label": "tl.create.penaltyRates.title",
				"name": "createpenaltyRatesOne",
				"fields": [
					{
						"name": "applicationType",
						"jsonPath": "penaltyRatesOne.applicationType",
						"label": "tl.search.groups.penaltyRates.applicationtype",
						"pattern": "",
						"type": "singleValueList",
						"url": "",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"defaultValue": [
							{
								"key": null,
								"value": "--Please Select--"
							},
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
			}
		]
	},
	"tl.search": {
		"numCols": 12/1,
		"url": "/tl-masters/penaltyrate/v1/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "penaltyRates",
		"groups": [
			{
				"label": "tl.search.groups.penaltyRates.title",
				"name": "createPenaltyRates",
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
								"key": null,
								"value": "--Please Select--"
							},
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
			}
		],
		"result": {
			"header": [{label: "tl.create.groups.penaltyRates.applicationtype"},{label: "tl.create.groups.penaltyRates.fromDays"}, {label: "tl.create.groups.penaltyRates.toDays"}, {label: "tl.create.groups.penaltyRates.range"}],
			"values": ["applicationType","fromRange", "toRange", "rate"],
			"resultPath": "penaltyRates",
			"rowClickUrlUpdate": "/update/tl/LicensePenaltyRates/{id}",
			"rowClickUrlView": "/view/tl/LicensePenaltyRates/{id}"
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
