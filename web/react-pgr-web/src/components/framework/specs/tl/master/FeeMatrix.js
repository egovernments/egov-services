var dat = {
	"tl.create": {
		"numCols": 12/3,
		"url": "/tl-masters/feematrix/v1/_create",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "feeMatrices",
		"groups": [
			{
				"label": "tl.create.groups.feematrixtype.title",
				"name": "createFeeMatrixType",
				"fields": [
            {
              "name": "applicationType",
              "jsonPath": "feeMatrices[0].applicationType",
              "label": "tl.create.groups.feematrixtype.applicationtype",
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
              "name": "businessNature",
              "jsonPath": "feeMatrices[0].businessNature",
              "label": "tl.create.groups.feematrixtype.natureofbusiness",
              "pattern": "",
              "type": "singleValueList",
              "url": "",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"defaultValue": [
	          {
	            "key": "PERMANENT",
	            "value": "PERMANENT"
	          },
	          {
	            "key": "TEMPORARY",
	            "value": "TEMPORARY"
	          }
	            ]
            },
            {
              "name": "categoryId",
              "jsonPath": "feeMatrices[0].categoryId",
              "label": "tl.create.groups.feematrixtype.licensecategory",
              "pattern": "",
              "type": "singleValueList",
              "url": "/tl-masters/category/v1/_search?tenantId=default&type=category|$..id|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"depedants": [{
	              "jsonPath": "feeMatrices[0].subCategoryId",
	              "type": "dropDown",
	              "pattern": "/tl-masters/category/v1/_search?tenantId=default&type=subcategory&categoryId={feeMatrices[0].categoryId}|$.categories.*.id|$.categories.*.name"
	            }]
            },
            {
              "name": "subCategoryId",
              "jsonPath": "feeMatrices[0].subCategoryId",
              "label": "tl.create.groups.feematrixtype.subcategory",
              "pattern": "",
              "type": "singleValueList",
              "url": "",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"depedants": [
	              {
	              "jsonPath": "feeMatrices[0].uomName",
	              "type": "text",
	              "isRequired": false,
	              "isDisabled": true,
	              "pattern": ""
	            },
		          {
		          "jsonPath": "feeMatrices[0].rateType",
		          "type": "text",
		          "isRequired": false,
		          "isDisabled": true,
		          "pattern": ""
		         	}
	          ]
            },
            {
              "name": "feeType",
              "jsonPath": "feeMatrices[0].feeType",
              "label": "tl.create.groups.feematrixtype.feetype",
              "pattern": "",
              "type": "singleValueList",
              "url": "",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"defaultValue": [
	          {
	            "key": "LICENSE",
	            "value": "LICENSE"
	          }
	            ]
            },
						{
							"name": "uomName",
							"jsonPath": "feeMatrices[0].uomName",
							"label": "tl.create.groups.feematrixtype.unitofmeasurement",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "ratetype",
							"jsonPath": "feeMatrices[0].rateType",
							"label": "tl.create.groups.feematrixtype.ratetype",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
              "name": "financialYear",
              "jsonPath": "feeMatrices[0].financialYear",
              "label": "tl.create.groups.feematrixtype.effectivefinancialyear",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egf-masters/financialyears/_search?tenantId=default|$..id|$..finYearRange",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
						// 	"defaultValue": [
	          // {
	          //   "key": "2012-13",
	          //   "value": "2012-13"
	          // },
	          // {
	          //   "key": "2013-14",
	          //   "value": "2013-14"
	          // }
	          //   ]
            },
						{
							"name": "effectiveFrom",
							"jsonPath": "feeMatrices[0].effectiveFrom",
							"label": "tl.create.groups.feematrixtype.effectiveFrom",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"isHidden": true
						},
						{
							"name": "effectiveTo",
							"jsonPath": "feeMatrices[0].effectiveTo",
							"label": "tl.create.groups.feematrixtype.effectiveTo",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"defaultValue": null,
							"isHidden": true
						}
				]
			},
		]
	},

  "tl.search": {
		"numCols": 12/2,
		"url": "/v1/feematrix/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "tl.search.groups.feematrixtype.title",
				"name": "createCategoryType",
				"fields": [
						{
							"name": "licensecategory",
							"jsonPath": "name",
							"label": "tl.search.groups.feematrixtype.licensecategory",
							"pattern": "",
							"type": "singleValueList",
              "url": "",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "subcategory",
							"jsonPath": "name",
							"label": "tl.search.groups.feematrixtype.subcategory",
							"pattern": "",
							"type": "singleValueList",
              "url": "",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "effectivefinancialyear",
							"jsonPath": "name",
							"label": "tl.search.groups.feematrixtype.effectivefinancialyear",
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
		"url": "/v1/feematrix/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "tl.view.groups.feematrixtype.title",
				"name": "viewCategoryType",
				"fields": [
          {
            "name": "applicationtype",
            "jsonPath": "name",
            "label": "tl.view.groups.feematrixtype.applicationtype",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "natureofbusiness",
            "jsonPath": "name",
            "label": "tl.view.groups.feematrixtype.natureofbusiness",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "licensecategory",
            "jsonPath": "name",
            "label": "tl.view.groups.feematrixtype.licensecategory",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "subcategory",
            "jsonPath": "name",
            "label": "tl.view.groups.feematrixtype.subcategory",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "feetype",
            "jsonPath": "name",
            "label": "tl.view.groups.feematrixtype.feetype",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "unitofmeasurement",
            "jsonPath": "categories.name",
            "label": "tl.view.groups.feematrixtype.unitofmeasurement",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "ratetype",
            "jsonPath": "categories.code",
            "label": "tl.view.groups.feematrixtype.ratetype",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "effectivefinancialyear",
            "jsonPath": "name",
            "label": "tl.view.groups.feematrixtype.effectivefinancialyear",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "effectivefrom",
            "jsonPath": "categories.name",
            "label": "tl.view.groups.feematrixtype.effectivefrom",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "effectiveto",
            "jsonPath": "categories.code",
            "label": "tl.view.groups.feematrixtype.effectiveto",
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
    "searchUrl": "/v1/feematrix/_search",
    "url":"/v1/feematrix/_update",
    "isResponseArray":true,
    "tenantIdRequired": true,
    "useTimestamp": true,
    "objectName": "CategoryType",
    "groups": [
      {
        "label": "tl.update.groups.feematrixtype.title",
        "name": "createCategoryType",
        "fields": [
          {
            "name": "applicationtype",
            "jsonPath": "name",
            "label": "tl.update.groups.feematrixtype.applicationtype",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "natureofbusiness",
            "jsonPath": "name",
            "label": "tl.update.groups.feematrixtype.natureofbusiness",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "licensecategory",
            "jsonPath": "name",
            "label": "tl.update.groups.feematrixtype.licensecategory",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "subcategory",
            "jsonPath": "name",
            "label": "tl.update.groups.feematrixtype.subcategory",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "feetype",
            "jsonPath": "name",
            "label": "tl.update.groups.feematrixtype.feetype",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "unitofmeasurement",
            "jsonPath": "categories.name",
            "label": "tl.update.groups.feematrixtype.unitofmeasurement",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "ratetype",
            "jsonPath": "categories.code",
            "label": "tl.update.groups.feematrixtype.ratetype",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "effectivefinancialyear",
            "jsonPath": "name",
            "label": "tl.update.groups.feematrixtype.effectivefinancialyear",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "effectivefrom",
            "jsonPath": "categories.name",
            "label": "tl.update.groups.feematrixtype.effectivefrom",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "effectiveto",
            "jsonPath": "categories.code",
            "label": "tl.update.groups.feematrixtype.effectiveto",
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
