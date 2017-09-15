var dat = {
	"tl.create": {
		"numCols": 12/3,
		"url": "/tl-masters/documenttype/v2/_create",
		"useTimestamp": true,
		"tenantIdRequired": true,
		"objectName": "documentTypes",
		"groups": [
			{
				"label": "tl.create.groups.licensedocumenttype.title",
				"name": "createLicenseDocumentType",
				"jsonPath": "documentTypesPartOne",
				"fields": [
						{
							"name": "licenseapptype",
							"jsonPath": "documentTypesPartOne.applicationType",
							"label": "tl.create.groups.licensedocumenttype.licenseapptype",
							"pattern": "",
							"type": "singleValueList",
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
	            "jsonPath": "documentTypesPartOne.categoryId",
	            "label": "tl.create.licensedocumenttype.groups.TradeDetails.TradeCategory",
	            "pattern": "",
	            "type": "singleValueList",
	            "url": "/tl-masters/category/v1/_search?tenantId=default&type=category|$..id|$..name",
	            "isRequired": false,
	            "isDisabled": false,
	            "requiredErrMsg": "",
	            "patternErrMsg": "",
	            "depedants": [{
	              "jsonPath": "documentTypesPartOne.subCategoryId",
	              "type": "dropDown",
	              "pattern": "/tl-masters/category/v1/_search?tenantId=default&type=subcategory&categoryId={documentTypesPartOne.categoryId}|$.categories.*.id|$.categories.*.name"
	            }]
	          },
						{
	            "name": "SubCategory",
	            "jsonPath": "documentTypesPartOne.subCategoryId",
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

				"name": "licensedocument",
				"multiple":true,
				"jsonPath": "documentTypesPartTwo",
				"fields": [
					{
						"name": "name",
						"jsonPath": "documentTypesPartTwo[0].name",
						"label": "tl.create.groups.licensedocumenttype.name",
						"pattern": "^.[a-zA-Z. ]{2,49}$",
						"type": "text",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "Enter Valid Name (Min:3, Max:50)",
						"maxLength": "50"
					},
					{
            "name": "mandatory",
            "jsonPath": "documentTypesPartTwo[0].mandatory",
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
            "jsonPath": "documentTypesPartTwo[0].enabled",
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
		"url": "/tl-masters/documenttype/v2/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "documentTypes",
		"groups": [
			{
				"label": "tl.search.groups.licensedocumenttype.title",
				"name": "createCategoryType",
				"fields": [
					{
						"name": "name",
						"jsonPath": "name",
						"label": "tl.search.groups.licensedocumenttype.name",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "licenseapptype",
						"jsonPath": "applicationType",
						"label": "tl.search.groups.licensedocumenttype.licenseapptype",
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
            "jsonPath": "categoryId",
            "label": "tl.create.licensedocumenttype.groups.TradeDetails.TradeCategory",
            "pattern": "",
            "type": "singleValueList",
            "url": "/tl-masters/category/v1/_search?tenantId=default&type=category|$..id|$..name",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "depedants": [{
              "jsonPath": "subCategoryId",
              "type": "dropDown",
              "pattern": "/tl-masters/category/v1/_search?tenantId=default&type=subcategory&categoryId={categoryId}|$.categories.*.id|$.categories.*.name"
            }]
          },

					{
            "name": "SubCategory",
            "jsonPath": "subCategoryId",
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
			}
		],
		"result": {
			"header": [{label: "tl.create.groups.licensedocumenttype.licenseapptype"},{label: "tl.create.licensedocumenttype.groups.TradeDetails.TradeCategory"}, {label: "tl.create.licensedocumenttype.groups.TradeDetails.TradeSubCategory"}, {label: "tl.create.groups.licensedocumenttype.name"}, {label: "tl.create.licenses.groups.TradeDetails.mandatory"}, {label: "tl.create.licenses.groups.TradeDetails.enabled"}],
			"values": ["applicationType","categoryName", "subCategoryName", "name", "mandatory", "enabled"],
			"resultPath": "documentTypes",
			"rowClickUrlUpdate": "/update/tl/LicenseDocumentType/{id}",
			"rowClickUrlView": "/view/tl/LicenseDocumentType/{id}"
			}
	},
	"tl.view": {
		"numCols": 12/3,
		"url": "/tl-masters/documenttype/v2/_search?ids={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "documentTypes[0]",
		"groups": [
			{
				"label": "tl.view.groups.licensedocumenttype.title",
				"name": "viewLicenseDocumentType",
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
						"jsonPath": "documentTypes[0].categoryName",
						"label": "tl.create.licensedocumenttype.groups.TradeDetails.TradeCategory",
						"pattern": "",
						"type": "text",
						"url": "/tl-masters/category/v1/_search?tenantId=default&type=category|$..id|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						// "depedants": [{
						// 	"jsonPath": "documentTypes[0].subCategoryId",
						// 	"type": "dropDown",
						// 	"pattern": "/tl-masters/category/v1/_search?tenantId=default&type=subcategory&categoryId={documentTypes[0].categoryId}|$.categories.*.id|$.categories.*.name"
						// }]
					},
					{
						"name": "SubCategory",
						"jsonPath": "documentTypes[0].subCategoryName",
						"label": "tl.create.licensedocumenttype.groups.TradeDetails.TradeSubCategory",
						"pattern": "",
						"type": "text",
						"url": "",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					}

				]
			},

			{

				"name": "viewCategoryType",
				"fields": [
					{
						"name": "name",
						"jsonPath": "documentTypes[0].name",
						"label": "tl.view.groups.licensedocumenttype.name",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
            "name": "mandatory",
            "jsonPath": "documentTypes[0].mandatory",
            "label": "tl.view.groups.licensedocumenttype.mandatory",
            "pattern": "",
            "type": "checkbox",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "enabled",
            "jsonPath": "documentTypes[0].enabled",
            "label": "tl.view.groups.licensedocumenttype.enabled",
            "pattern": "",
            "type": "checkbox",
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
	"tl.update": {
		"numCols": 12/3,
		"searchUrl": "/tl-masters/documenttype/v2/_search?ids={id}",
		"url": "/tl-masters/documenttype/v2/_update",
		"isResponseArray":true,
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "documentTypes[0]",
		"groups": [
			{
				"label": "tl.update.groups.licensedocumenttype.title",
				"name": "updateLicenseDocumentType",
				"fields": [

					{
						"name": "licenseapptype",
						"jsonPath": "documentTypes[0].applicationType",
						"label": "tl.update.groups.licensedocumenttype.licenseapptype",
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
            "label": "tl.update.licensedocumenttype.groups.TradeDetails.TradeCategory",
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
            "label": "tl.update.licensedocumenttype.groups.TradeDetails.TradeSubCategory",
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
				"name": "createLicenseDocumentType2",
				"jsonPath": "documentTypes",
				"multiple":true,
				"fields": [
					{
						"name": "name",
						"jsonPath": "documentTypes[0].name",
						"label": "tl.update.groups.licensedocumenttype.name",
						"pattern": "^.[a-zA-Z. ]{2,49}$",
						"type": "text",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "Enter Valid Name (Min:3, Max:50)",
						"maxLength": "50"
					},
					{
            "name": "mandatory",
            "jsonPath": "documentTypes[0].mandatory",
            "label": "tl.update.groups.licensedocumenttype.mandatory",
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
            "label": "tl.update.groups.licensedocumenttype.enabled",
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
	}
}

export default dat;
