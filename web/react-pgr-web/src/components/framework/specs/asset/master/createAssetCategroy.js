var dat = {
	"asset.create": {
		"numCols": 12/3,
		"url": "",
		"tenantIdRequired": true,
		"idJsonPath": "",
		"objectName": "AssetCategory",
		"groups": [
			{
				"label": "ac.create.title",
				"name": "createCategoryType",
				"fields": [
						{
							"name": "SubCategoryName",
							"jsonPath": "AssetCategory[0].name",
							"label": "ac.create.asset.category.name",
							"pattern": "^.[a-zA-Z. ]{2,99}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Enter Valid Name"
						},
            {
  						"name": "AssetCategoryType",
  						"jsonPath": "AssetCategory[0].assetCategoryType",
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
  						"jsonPath": "AssetCategory[0].parent",
  						"label": "ac.create.asset.sub.categroy",
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
  						"jsonPath": "AssetCategory[0].depreciationMethod",
  						"label": "ac.create.depericiation.method",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "UnitOfMeasurement",
  						"jsonPath": "AssetCategory[0].unitOfMeasurement",
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
  						"jsonPath": "AssetCategory[0].depreciationRate",
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
							"jsonPath": "AssetCategory[0].isDepreciationApplicable",
							"label": "ac.create.depreciation.applicable",
							"url": "",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			},
      {
				//"label": "ac.create.additional.field",
				"label": "",
				"name": "AdditionalField",
        "multiple":true,
        "jsonPath":"",
				"fields": [
						{
							"name": "additionalName",
							"jsonPath": "AssetCategory[0].assetFieldsDefination[0].name",
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
  						"jsonPath": "AssetCategory[0].assetFieldsDefination[0].type",
  						"label": "ac.create.additional.field.data.type",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "Active",
  						"jsonPath": "AssetCategory[0].assetFieldsDefination[0].isActive",
  						"label": "wc.create.active",
  						"pattern": "",
  						"type": "checkbox",
  						"isRequired": false,
  						"isDisabled": false,
  						"defaultValue":false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "Mandatory",
  						"jsonPath": "AssetCategory[0].assetFieldsDefination[0].isMandatory",
  						"label": "wc.create.mandatory",
  						"pattern": "",
  						"type": "checkbox",
  						"isRequired": false,
  						"isDisabled": false,
  						"defaultValue":false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
							"name": "additionalOrder",
							"jsonPath": "AssetCategory[0].assetFieldsDefination[0].order",
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
							"jsonPath": "AssetCategory[0].assetFieldsDefination[0].values",
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
		"url": "/asset-services/assetCategories/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "AssetCategory",
		"groups": [
			{
				"label": "ac.search.assetCategory.title",
				"name": "createCategoryType",
				"fields": [
					{
						"name": "AssetCategoryName",
						"jsonPath": "name",
						"label": "ac.create.asset.category.name",
						"pattern": "",
						"type": "text",
						"url": "",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "AssetCategoryType",
						"jsonPath": "assetCategoryType",
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
			"values": ["code","name", "assetCategoryType", "parent", "unitOfMeasurement"],
			"resultPath": "AssetCategory",
			// "rowClickUrlUpdate": "/update/wc/categoryType/{id}",
			// "rowClickUrlView": "/view/wc/categoryType/{id}"
			}
	}
}

export default dat;
