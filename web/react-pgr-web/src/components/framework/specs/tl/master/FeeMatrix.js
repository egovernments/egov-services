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
		"url": "/tl-masters/feematrix/v1/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "feeMatrices",
		"groups": [
			{
				"label": "tl.search.groups.feematrixtype.title",
				"name": "feeMatrices",
				"fields": [
					{
						"name": "categoryId",
						"jsonPath": "categoryId",
						"label": "tl.search.groups.feematrixtype.licensecategory",
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
						"name": "subCategoryId",
						"jsonPath": "subCategoryId",
						"label": "tl.search.groups.feematrixtype.subcategory",
						"pattern": "",
						"type": "singleValueList",
						"url": "",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
						},
						{
              "name": "financialYear",
              "jsonPath": "financialYear",
              "label": "tl.search.groups.feematrixtype.effectivefinancialyear",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egf-masters/financialyears/_search?tenantId=default|$..id|$..finYearRange",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            }
				]
			}
		],
		"result": {
			"header": [{label: "tl.create.groups.feematrixtype.natureofbusiness"},{label: "tl.create.groups.feematrixtype.applicationtype"}, {label: "tl.create.groups.feematrixtype.licensecategory"}, {label: "tl.create.groups.feematrixtype.subcategory"}, {label: "tl.create.groups.feematrixtype.feetype"}, {label: "tl.create.groups.feematrixtype.effectivefinancialyear"}],
			"values": ["businessNature","applicationType", "categoryName", "subCategoryName", "feeType", "financialYear"],
			"resultPath": "feeMatrices",
			"rowClickUrlUpdate": "/update/tl/FeeMatrix/{id}",
			"rowClickUrlView": "/non-framework/tl/masters/viewFeeMatrix/{id}"
			}
	},

  "tl.view": {
		"numCols": 12/2,
		"url": "/tl-masters/feematrix/v1/_search?ids={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "feeMatrices[0]",
		"groups": [
			{
				"label": "tl.view.groups.feematrixtype.title",
				"name": "viewfeeMatrices",
				"fields": [
          {
            "name": "applicationtype",
            "jsonPath": "feeMatrices[0].applicationType",
            "label": "tl.view.groups.feematrixtype.applicationtype",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "natureofbusiness",
            "jsonPath": "feeMatrices[0].businessNature",
            "label": "tl.view.groups.feematrixtype.natureofbusiness",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "licensecategory",
            "jsonPath": "feeMatrices[0].categoryName",
            "label": "tl.view.groups.feematrixtype.licensecategory",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "subcategory",
            "jsonPath": "feeMatrices[0].subCategoryName",
            "label": "tl.view.groups.feematrixtype.subcategory",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "effectivefinancialyear",
            "jsonPath": "feeMatrices[0].financialYear",
            "label": "tl.view.groups.feematrixtype.effectivefinancialyear",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "feetype",
            "jsonPath": "feeMatrices[0].feeType",
            "label": "tl.view.groups.feematrixtype.feetype",
            "pattern": "",
            "type": "text",
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
    "searchUrl": "/tl-masters/feematrix/v1/_search?ids={id}",
    "url":"/v1/feematrix/_update",
    "isResponseArray":true,
    "tenantIdRequired": true,
    "useTimestamp": true,
    "objectName": "feeMatrices[0]",
    "groups": [
      {
        "label": "tl.update.groups.feematrixtype.title",
        "name": "updatefeeMatrices",
        "fields": [
          {
            "name": "applicationtype",
            "jsonPath": "feeMatrices[0].applicationType",
            "label": "tl.update.groups.feematrixtype.applicationtype",
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
            "label": "tl.update.groups.feematrixtype.natureofbusiness",
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
            "label": "tl.update.groups.feematrixtype.licensecategory",
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
