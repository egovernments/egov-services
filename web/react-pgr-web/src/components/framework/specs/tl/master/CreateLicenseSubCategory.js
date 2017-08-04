var dat = {
	"tl.create": {
		"numCols": 12/2,
		"url": "/v1/category/_create",
		"tenantIdRequired": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "tl.create.subcategorytype.title",
				"name": "createLicenseSubCategoryType",
				"fields": [

          {
            "name": "category",
            "jsonPath": "name",
            "label": "tl.create.groups.subcategorytype.category",
            "pattern": "",
            "type": "singleValueList",
            "url": "/wcms/masters/master/_getapplicationtypes?|$..key|$..object",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
						{
							"name": "name",
							"jsonPath": "categories.name",
							"label": "tl.create.groups.subcategorytype.name",
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
							"label": "tl.create.groups.subcategorytype.code",
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
    "url": "/v1/category/_search",
    "tenantIdRequired": true,
    "useTimestamp": true,
    "objectName": "CategoryType",
    "groups": [
      {
        "label": "tl.search.groups.subcategorytype.title",
        "name": "createCategoryType",
        "fields": [
            {
              "name": "category",
              "jsonPath": "name",
              "label": "tl.search.groups.subcategorytype.category",
              "pattern": "",
              "type": "singleValueList",
              "url": "",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "category",
              "jsonPath": "name",
              "label": "tl.search.groups.subcategorytype.subcategory",
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
            "name": "category",
            "jsonPath": "categories.name",
            "label": "tl.view.groups.subcategorytype.category",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "name",
            "jsonPath": "categories.name",
            "label": "tl.view.groups.subcategorytype.name",
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
            "label": "tl.view.groups.subcategorytype.code",
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
				"label": "tl.update.groups.subcategorytype.title",
				"name": "createCategoryType",
				"fields": [
          {
            "name": "category",
            "jsonPath": "categories.name",
            "label": "tl.update.groups.subcategorytype.category",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
						"name": "name",
						"jsonPath": "categories.name",
						"label": "tl.update.groups.subcategorytype.name",
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
						"label": "tl.update.groups.subcategorytype.code",
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
