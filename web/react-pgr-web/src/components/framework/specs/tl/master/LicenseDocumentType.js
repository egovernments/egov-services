var dat = {
	"tl.create": {
		"numCols": 12/2,
		"url": "/v1/documenttype/_create",
		"tenantIdRequired": true,
		"objectName": "LicenseDocumentType",
		"groups": [
			{
				"label": "tl.create.groups.licensedocumenttype.title",
				"name": "createLicenseDocumentType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "categories.name",
							"label": "tl.create.groups.licensedocumenttype.name",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "licenseapptype",
							"jsonPath": "name",
							"label": "tl.create.groups.licensedocumenttype.licenseapptype",
							"pattern": "",
							"type": "singleValueList",
              "url": "",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "mandatory",
							"jsonPath": "CategoryType.active",
							"label": "tl.create.groups.licensedocumenttype.mandatory",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "enabled",
							"jsonPath": "CategoryType.active",
							"label": "tl.create.groups.licensedocumenttype.enabled",
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

  "tl.search": {
		"numCols": 12/2,
		"url": "/v1/documenttype/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "tl.search.groups.licensedocumenttype.title",
				"name": "createCategoryType",
				"fields": [
            {
              "name": "name",
              "jsonPath": "categories.name",
              "label": "tl.search.groups.licensedocumenttype.name",
              "pattern": "",
              "type": "text",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
							"name": "licenseapptype",
							"jsonPath": "name",
							"label": "tl.search.groups.licensedocumenttype.licenseapptype",
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
		"url": "/v1/documenttype/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "tl.view.groups.licensedocumenttype.title",
				"name": "viewCategoryType",
				"fields": [
          {
            "name": "name",
            "jsonPath": "categories.name",
            "label": "tl.view.groups.licensedocumenttype.name",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "licenseapptype",
            "jsonPath": "name",
            "label": "tl.view.groups.licensedocumenttype.licenseapptype",
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
		]
	},

  "tl.update": {
		"numCols": 12/2,
		"searchUrl": "/v1/documenttype/_search",
		"url":"/documenttype/_update",
		"isResponseArray":true,
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "tl.update.groups.licensedocumenttype.title",
				"name": "createCategoryType",
				"fields": [
          {
            "name": "name",
            "jsonPath": "categories.name",
            "label": "tl.update.groups.licensedocumenttype.name",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "licenseapptype",
            "jsonPath": "name",
            "label": "tl.update.groups.licensedocumenttype.licenseapptype",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "mandatory",
            "jsonPath": "CategoryType.active",
            "label": "tl.update.groups.licensedocumenttype.mandatory",
            "pattern": "",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "enabled",
            "jsonPath": "CategoryType.active",
            "label": "tl.update.groups.licensedocumenttype.enabled",
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
