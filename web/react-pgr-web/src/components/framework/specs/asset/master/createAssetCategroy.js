var dat = {
	"asset.create": {
		"numCols": 12/3,
		"url": "",
		"tenantIdRequired": true,
		"idJsonPath": "",
		"objectName": "",
		"groups": [
			{
				"label": "ac.create.title",
				"name": "createCategoryType",
				"fields": [
						{
							"name": "SubCategoryName",
							"jsonPath": "asset[0].name",
							"label": "ac.create.asset.sub.categroy",
							"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
  						"name": "AssetCategoryType",
  						"jsonPath": "",
  						"label": "ac.create.asset.asset.category.type",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "AssetCategoryName",
  						"jsonPath": "",
  						"label": "ac.create.asset.category.name",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "DepericiationMethod",
  						"jsonPath": "",
  						"label": "ac.create.depericiation.method",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "UnitOfMeasurement",
  						"jsonPath": "",
  						"label": "ac.create.unit.of.measurement",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "DepericiationRate",
  						"jsonPath": "",
  						"label": "ac.create.depericiation.rate",
  						"pattern": "",
  						"type": "number",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
						{
							"name": "IsDepreciationApplicable",
							"jsonPath": "",
							"label": "ac.create.depreciation.applicable",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"defaultValue":true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			},
      {
				"label": "ac.create.additional.field",
				"name": "AdditionalField",
        "multiple":true,
        "jsonPath":"",
				"fields": [
						{
							"name": "additionalName",
							"jsonPath": "",
							"label": "ac.create.additional.field.name",
							"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
  						"name": "additionalDataType",
  						"jsonPath": "",
  						"label": "ac.create.additional.field.data.type",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "Active",
  						"jsonPath": "",
  						"label": "wc.create.active",
  						"pattern": "",
  						"type": "checkbox",
  						"isRequired": false,
  						"isDisabled": false,
  						"defaultValue":true,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "Mandatory",
  						"jsonPath": "",
  						"label": "wc.create.mandatory",
  						"pattern": "",
  						"type": "checkbox",
  						"isRequired": false,
  						"isDisabled": false,
  						"defaultValue":true,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
							"name": "additionalOrder",
							"jsonPath": "",
							"label": "ac.create.additional.field.order",
							"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "additionalValue",
							"jsonPath": "",
							"label": "ac.create.additional.field.value",
							"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
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
	"asset.search": {
		"numCols": 12/3,
		"url": "/wcms/masters/categorytype/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "CategoryType",
		"groups": [
			{
				"label": "wc.search.categorytype.title",
				"name": "createCategoryType",
				"fields": [
					{
						"name": "AssetCategoryName",
						"jsonPath": "",
						"label": "ac.create.asset.category.name",
						"pattern": "",
						"type": "singleValueList",
						"url": "",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "AssetCategoryType",
						"jsonPath": "",
						"label": "ac.create.asset.asset.category.type",
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
			"header": [{label: "code"},{label: "ac.create.asset.category.name"}, {label: "ac.create.asset.asset.category.type"}, {label: "Parent Category"}, {label: "ac.create.unit.of.measurement"}],
			"values": ["code","name", "description", "active", "UOM"],
			"resultPath": "CategoryTypes",
			"rowClickUrlUpdate": "/update/wc/categoryType/{id}",
			"rowClickUrlView": "/view/wc/categoryType/{id}"
			}
	}
}

export default dat;
