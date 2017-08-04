var dat = {
	"tl.create": {
		"numCols": 12/2,
		"url": "/v1/feematrix/_create",
		"tenantIdRequired": true,
		"objectName": "FeeMatrixType",
		"groups": [
			{
				"label": "tl.create.groups.feematrixtype.title",
				"name": "createFeeMatrixType",
				"fields": [
            {
              "name": "applicationtype",
              "jsonPath": "name",
              "label": "tl.create.groups.feematrixtype.applicationtype",
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
              "label": "tl.create.groups.feematrixtype.natureofbusiness",
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
              "label": "tl.create.groups.feematrixtype.licensecategory",
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
              "label": "tl.create.groups.feematrixtype.subcategory",
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
              "label": "tl.create.groups.feematrixtype.feetype",
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
							"label": "tl.create.groups.feematrixtype.unitofmeasurement",
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
							"label": "tl.create.groups.feematrixtype.ratetype",
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
              "label": "tl.create.groups.feematrixtype.effectivefinancialyear",
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
							"label": "tl.create.groups.feematrixtype.effectivefrom",
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
							"label": "tl.create.groups.feematrixtype.effectiveto",
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
