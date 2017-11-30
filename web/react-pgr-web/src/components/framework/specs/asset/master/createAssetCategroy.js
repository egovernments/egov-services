var dat = {
	"asset.create": {
		"numCols": 12/3,
		"url": "",
		"tenantIdRequired": true,
		"useTimestamp":true,
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
  						"patternErrMsg": "",
							"defaultValue": [{
							"key": "IMMOVABLE",
							"value": "IMMOVABLE"
						},
						{
								"key": "MOVABLE",
								"value": "MOVABLE"
							}],
							"depedants": [{
								"jsonPath": "AssetCategory[0].parent",
								"type": "dropDown",
								"pattern": "/egov-mdms-service/v1/_get?&moduleName=ASSET&masterName=AssetCategory&filter=%5B%3F(%20%40.isAssetAllow%20%3D%3D%20false%20%26%26%20%40.assetCategoryType%3D%3D'{AssetCategory[0].assetCategoryType}')%5D|$.MdmsRes.ASSET.AssetCategory.*.id|$.MdmsRes.ASSET.AssetCategory.*.name"
							}]
  					},
						{
							"name": "AssetCategory",
							"jsonPath": "AssetCategory[0].parent",
  						"label": "ac.create.asset.sub.categroy",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"isStateLevel":true,
						},
            {
  						"name": "DepericiationMethod",
  						"jsonPath": "AssetCategory[0].depreciationMethod",
  						"label": "ac.create.depericiation.method",
  						"pattern": "",
  						"type": "text",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": true,
  						"requiredErrMsg": "",
  						"patternErrMsg": "",
							"defaultValue": "Straight Line method"
  					},
            {
  						"name": "UnitOfMeasurement",
  						"jsonPath": "AssetCategory[0].unitOfMeasurement",
  						"label": "ac.create.unit.of.measurement",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "/egov-mdms-service/v1/_get?&masterName=Uom&moduleName=common-masters|$..code|$..description",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": "",
							"isStateLevel":true
  					},
						{
							"name": "IsDepreciationApplicable",
							"jsonPath": "AssetCategory[0].isDepreciationApplicable",
							"label": "ac.create.depreciation.applicable",
							"url": "",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"defaultValue": [{
							"key": "YES",
							"value": "YES"
						},
						{
								"key": "NO",
								"value": "NO"
							}],
						},
            {
  						"name": "DepericiationRate",
  						"jsonPath": "AssetCategory[0].depreciationRate",
  						"label": "ac.create.depericiation.rate",
  						"pattern": "^[1-9]\\d{0,3}(\\.\\d{0,3})*(,\\d+)?$",
  						"type": "number",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},

				]
			},
      {
				"label": "ac.create.additional.field",
				// "label": "",
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
							"isRequired": false,
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
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": "",
							"defaultValue":[{
							    "key": "Text",
							    "value": "Text"
							  },
							  {
							    "key": "Number",
							    "value": "Number"
							  },
							  {
							    "key": "Select",
							    "value": "Select"
							  },
								// {
							  //   "key": "Email",
							  //   "value": "Email"
							  // },
							  // {
							  //   "key": "Multiselect",
							  //   "value": "Multiselect"
							  // },
							  // {
							  //   "key": "Date",
							  //   "value": "Date"
							  // },
							  // {
							  //   "key": "File",
							  //   "value": "File"
							  // },
							  // {
							  //   "key": "Table",
							  //   "value": "Table"
							  // }
							]
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
							"jsonPath": "AssetCategory[0].assetFieldsDefination[0].url",
							"label": "ac.create.additional.field.url",
							"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
            {
							"name": "additionalValue",
							"jsonPath": "AssetCategory[0].assetFieldsDefination[0].values",
							"label": "ac.create.additional.field.value",
							"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
							"type": "textarea",
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
