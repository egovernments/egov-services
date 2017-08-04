var dat = {
	"tl.create": {
		"numCols": 12/2,
		"url": "/v1/category/_create",
		"tenantIdRequired": true,
		"objectName": "UOMType",
		"groups": [
			{
				"label": "tl.create.UOMType.title",
				"name": "createUOMType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "categories.name",
							"label": "tl.create.groups.UOMType.name",
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
							"label": "tl.create.groups.UOMType.code",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "Active",
							"jsonPath": "CategoryType.active",
							"label": "tl.create.groups.UOMType.active",
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
		"numCols": 12/1,
		"url": "/v1/uom/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "tl.search.groups.UOMType.title",
				"name": "createCategoryType",
				"fields": [
						{
							"name": "category",
							"jsonPath": "name",
							"label": "tl.search.groups.UOMType.category",
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
		"url": "/v1/uom/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "tl.view.groups.UOMType.title",
				"name": "viewCategoryType",
				"fields": [
					{
						"name": "name",
						"jsonPath": "categories.name",
						"label": "tl.view.groups.UOMType.name",
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
						"label": "tl.view.groups.UOMType.code",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
          {
            "name": "Active",
            "jsonPath": "CategoryType.active",
            "label": "tl.view.groups.UOMType.active",
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
				"label": "tl.update.groups.UOMType.title",
				"name": "createCategoryType",
				"fields": [
					{
						"name": "name",
						"jsonPath": "categories.name",
						"label": "tl.update.groups.UOMType.name",
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
						"label": "tl.update.groups.UOMType.code",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
          {
            "name": "Active",
            "jsonPath": "CategoryType.active",
            "label": "tl.update.groups.UOMType.active",
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
