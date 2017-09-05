var dat = {
	"tl.create": {
		"numCols": 12/2,
		"url": "/tl-masters/category/v1/_create",
		"useTimestamp": true,
		"tenantIdRequired": true,
		"objectName": "categories",
		"groups": [
			{
				"label": "tl.create.groups.subcategorytype.title",
				"name": "createLicenseSubCategoryType",
				"fields": [

          {
            "name": "category",
            "jsonPath": "categories[0].parentId",
            "label": "tl.create.groups.subcategorytype.category",
            "pattern": "",
            "type": "singleValueList",
            "url": "/tl-masters/category/v1/_search?tenantId=default&type=category|$..id|$..name",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
						{
							"name": "name",
							"jsonPath": "categories[0].name",
							"label": "tl.create.groups.subcategorytype.name",
							"pattern": "^.[a-zA-Z. ]{2,99}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Enter Valid Name"
						},
						{
							"name": "code",
							"jsonPath": "categories[0].code",
							"label": "tl.create.groups.subcategorytype.code",
							"pattern": "^.[A-Za-z0-9]{0,19}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Enter Valid Code (Alpha-Numeric, Min:1, Max:20)",
							"maxLength": "20"
						},
						{
							"name": "validityYears",
							"jsonPath": "categories[0].validityYears",
							"label": "tl.create.groups.subcategorytype.validityYears",
							"pattern": "^([1-9]|10)$",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Enter Valid Validity Year (Min: 1, Max:10)"
						},
						{
							"name": "active",
							"jsonPath": "categories[0].active",
							"label": "tl.create.groups.subcategorytype.active",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"defaultValue":true
						}
				]
			},

			{
				"label": "Details",
				"name": "Details",
				"jsonPath": "categories[0].details",
				"multiple":true,
				"fields": [
					{
						"name": "feeType",
						"jsonPath": "categories[0].details[0].feeType",
						"label": "tl.create.groups.subcategorytype.categories.details.feeType",
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
          },
          {
            "key": "MOTOR",
            "value": "MOTOR"
          },
          {
            "key": "WORKFORCE",
            "value": "WORKFORCE"
          }
            ]
					},
					{
            "name": "rateType",
            "jsonPath": "categories[0].details[0].rateType",
            "label": "tl.create.groups.subcategorytype.categories.details.rateType",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"defaultValue": [{
					 "key": "",
					 "value": null
				 },
				 {
					 "key": "FLAT_BY_RANGE",
					 "value": "FLAT BY RANGE"
				 },
				 {
					 "key": "FLAT_BY_PERCENTAGE",
					 "value": "FLAT BY PERCENTAGE"
				 },
				 {
					 "key": "UNIT_BY_RANGE",
					 "value": "UNIT BY RANGE"
				 }
					 ]
          },
					{
            "name": "uomId",
            "jsonPath": "categories[0].details[0].uomId",
            "label": "tl.create.groups.subcategorytype.categories.details.uomId",
            "pattern": "",
            "type": "singleValueList",
            "url": "/tl-masters/uom/v1/_search?|$..id|$..name",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
						"name": "tenantID",
						"jsonPath": "categories[0].details[0].tenantId",
						"label": "tenantId",
						"pattern": "",
						"type": "text",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"defaultValue": localStorage.getItem("tenantId"),
						"hide": "true"
					}
					]
				}

		]
	},


  "tl.search": {
    "numCols": 12/2,
    "url": "/tl-masters/category/v1/_search",
    "tenantIdRequired": true,
    "useTimestamp": true,
    "objectName": "categories",
    "groups": [
      {
        "label": "tl.search.groups.subcategorytype.title",
        "name": "createCategoryType",
        "fields": [

            {
              "name": "category",
              "jsonPath": "categoryId",
              "label": "tl.search.groups.subcategorytype.category",
              "pattern": "",
              "type": "singleValueList",
              "url": "/tl-masters/category/v1/_search?tenantId=default|$..id|$..name",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "subCategory",
              "jsonPath": "ids",
              "label": "tl.search.groups.subcategorytype.subcategory",
              "pattern": "",
              "type": "singleValueList",
              "url": "/tl-masters/category/v1/_search?tenantId=default&type=subcategory|$..id|$..name",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
							"name": "category",
							"jsonPath": "type",
							"label": "tl.search.groups.subcategorytype.category",
							"pattern": "",
							"type": "",
							"url": "",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"defaultValue": "subcategory",
							"hide": true
						}
        ]
      }
    ],
    "result": {
      "header": [{label: "tl.create.groups.subcategorytype.code"},{label: "tl.create.groups.subcategorytype.categories.details.rateType"}, {label: "tl.create.groups.subcategorytype.category"}, {label: "tl.create.groups.subcategorytype.active"}],
      "values": ["code","details[0].rateType", "parentId", "active"],
      "resultPath": "categories",
      "rowClickUrlUpdate": "/update/tl/CreateLicenseSubCategory/{id}",
      "rowClickUrlView": "/view/tl/CreateLicenseSubCategory/{id}"
      }
  },

  "tl.view": {
    "numCols": 12/2,
    "url": "/tl-masters/category/v1/_search?ids={id}",
    "tenantIdRequired": true,
    "useTimestamp": true,
    "objectName": "categories",
    "groups": [
			{
				"label": "tl.view.groups.categorytype.title", 
				"name": "viewCategoryType",
				"fields": [

					{
            "name": "category",
            "jsonPath": "categories[0].parentId",
            "label": "tl.view.groups.subcategorytype.category",
            "pattern": "",
            "type": "singleValueList",
            "url": "/tl-masters/category/v1/_view?|$..id|$..name",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "name",
            "jsonPath": "categories[0].name",
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
            "jsonPath": "categories[0].code",
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
		"searchUrl": "/tl-masters/category/v1/_search?ids={id}",
		"url":"/tl-masters/category/v1/_update",
		"isResponseArray":true,
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "categories[0]",
		"groups": [
			{
				"label": "tl.update.groups.subcategorytype.title",
				"name": "createCategoryType",
				"fields": [
          {
            "name": "category",
            "jsonPath": "categories[0].parentId",
            "label": "tl.update.groups.subcategorytype.category",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
						"name": "name",
						"jsonPath": "categories[0].name",
						"label": "tl.update.groups.subcategorytype.name",
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
						"label": "tl.update.groups.subcategorytype.code",
						"pattern": "^.[A-Za-z0-9]{14,14}$",
						"type": "text",
						"isRequired": false,
						"isDisabled": true,
						"requiredErrMsg": "",
						"patternErrMsg": "Enter 15 digit Alpha/Numeric Code"
					},
					{
						"name": "validityYears",
						"jsonPath": "categories[0].validityYears",
						"label": "tl.update.groups.subcategorytype.validityYears",
						"pattern": "^([1-9]|10)$",
						"type": "number",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "Enter Valid Validity Year (Min: 1, Max:10)"
					},
					{
						"name": "active",
						"jsonPath": "categories[0].active",
						"label": "tl.update.groups.subcategorytype.active",
						"pattern": "",
						"type": "checkbox",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"defaultValue":true
					}
				]
			},
			{
				"label": "Details",
				"name": "Details",
				"jsonPath": "categories[0].details",
				"multiple":true,
				"fields": [
					{
						"name": "feeType",
						"jsonPath": "categories[0].details[0].feeType",
						"label": "tl.update.groups.subcategorytype.categories.details.feeType",
						"pattern": "",
						"type": "singleValueList",
						"url": "",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"defaultValue": [{
            "key": "",
            "value": null
          },
          {
            "key": "LICENSE",
            "value": "LICENSE"
          },
          {
            "key": "MOTOR",
            "value": "MOTOR"
          },
          {
            "key": "WORKFORCE",
            "value": "WORKFORCE"
          }
            ]
					},
					{
            "name": "rateType",
            "jsonPath": "categories[0].details[0].rateType",
            "label": "tl.update.groups.subcategorytype.categories.details.rateType",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"defaultValue": [{
					 "key": "",
					 "value": null
				 },
				 {
					 "key": "FLAT_BY_RANGE",
					 "value": "FLAT BY RANGE"
				 },
				 {
					 "key": "FLAT_BY_PERCENTAGE",
					 "value": "FLAT BY PERCENTAGE"
				 },
				 {
					 "key": "UNIT_BY_RANGE",
					 "value": "UNIT BY RANGE"
				 }
					 ]
          },
					{
            "name": "uomId",
            "jsonPath": "categories[0].details[0].uomId",
            "label": "tl.update.groups.subcategorytype.categories.details.uomId",
            "pattern": "",
            "type": "singleValueList",
            "url": "/tl-masters/uom/v1/_search?|$..id|$..name",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
						"name": "tenantID",
						"jsonPath": "categories[0].details[0].tenantId",
						"label": "tenantId",
						"pattern": "",
						"type": "text",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"defaultValue": localStorage.getItem("tenantId"),
						"hide": "true"
					}
					]
				}
		]
	}
}

export default dat;
