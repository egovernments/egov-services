var dat = {
	"tl.create": {
		"numCols": 12/2,
		"url": "/tl-masters/documenttype/v2/_create",
		"useTimestamp": true,
		"tenantIdRequired": true,
		"objectName": "documentTypes",
		"groups": [
			{
				"label": "tl.create.groups.licensedocumenttype.title",
				"name": "createLicenseDocumentType",
				"fields": [
					{
						"name": "licenseapptype",
						"jsonPath": "documentTypes[0].applicationType",
						"label": "tl.create.groups.licensedocumenttype.licenseapptype",
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
					},

					{
            "name": "Category",
            "jsonPath": "documentTypes[0].categoryId",
            "label": "tl.create.licensedocumenttype.groups.TradeDetails.TradeCategory",
            "pattern": "",
            "type": "singleValueList",
            "url": "/tl-masters/category/v1/_search?tenantId=default&type=category|$..id|$..name",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "depedants": [{
              "jsonPath": "documentTypes[0].subCategoryId",
              "type": "dropDown",
              "pattern": "/tl-masters/category/v1/_search?tenantId=default&type=subcategory&categoryId={documentTypes[0].categoryId}|$.categories.*.id|$.categories.*.name"
            }]
          },
					{
            "name": "SubCategory",
            "jsonPath": "documentTypes[0].subCategoryId",
            "label": "tl.create.licensedocumenttype.groups.TradeDetails.TradeSubCategory",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
					}
				]
			},

			{
				//"label": "tl.create.groups.licensedocumenttype.title",
				"name": "licensedocument",
				"jsonPath": "",
				"multiple":true,
				"fields": [
					{
						"name": "name",
						"jsonPath": "documentTypes[0].name",
						"label": "tl.create.groups.licensedocumenttype.name",
						"pattern": "",
						"type": "text",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
            "name": "mandatory",
            "jsonPath": "documentTypes[0].mandatory",
            "label": "tl.create.licenses.groups.TradeDetails.mandatory",
            "pattern": "",
            "type": "checkbox",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"defaultValue": false
          },
					{
            "name": "enabled",
            "jsonPath": "documentTypes[0].enabled",
            "label": "tl.create.licenses.groups.TradeDetails.enabled",
            "pattern": "",
            "type": "checkbox",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"defaultValue": false
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
