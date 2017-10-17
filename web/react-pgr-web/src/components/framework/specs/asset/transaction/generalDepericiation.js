var dat = {
	"asset.transaction": {
		"numCols": 12/3,
		"url": "",
    "tenantIdRequired": true,
    "objectName": "",
    "useTimestamp": true,
    "groups": [{
        "label": "wc.search.categorytype.title",
        "name": "createCategoryType",
        "fields": [
          {
            "name": "DateofDepreciation",
            "jsonPath": "",
            "label": "Date of Depreciation",
            "pattern": "^[\s.]*([^\s.][\s.]*){0,100}",
            "type": "date",
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
            "name": "AssetCategoryType",
            "jsonPath": "",
            "label": "Asset Sub Category",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          }
        ]
      }],
      "result": {
        "header": [{
            "name": "SelectButon",
            "jsonPath": "",
            "label": "Select",
            "pattern": "",
            "type": "checkbox",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "url":"",
            "isLabel": false
          },
					{
            "name": "AssetCode",
            "jsonPath": "",
            "label": "Asset Code",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "url":"",
            "isLabel": false
          },
          {
            "name": "NameAsset",
            "jsonPath": "",
            "label": "Name of Asset",
            "pattern": "",
            "type": "label",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "isLabel": false,
            "hyperLink": ""
          },
          {
            "name": "WdvValue",
            "jsonPath": "",
            "label": "WDV Value",
            "pattern": "",
            "type": "label",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "isLabel": false,
            "textAlign":"right"
          },
          {
            "name": "DepreciationRate",
            "jsonPath": "",
            "label": "Depreciation Rate (%)",
            "pattern": "",
            "type": "label",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "isLabel": false,
            "textAlign":"right"
          },
          {
            "name": "DepreciationAmount",
            "jsonPath": "",
            "label": "Depreciation Amount",
            "pattern": "",
            "type": "label",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "isLabel": false,
            "textAlign":"right"
          },
          {
            "name": "WdvAfterDepreciation",
            "jsonPath": "",
            "label": "WDV after Depreciation",
            "pattern": "",
            "type": "label",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "isLabel": false,
            "textAlign":"right"
          },
        ],
        // "values": ["businessService", "consumerCode", "totalAmount","minimumAmount","bill"],
        "resultPath": "",
        "tableResultPath": "",

        // "rowClickUrlUpdate": "/update/wc/pipeSize/{id}",
        // "rowClickUrlView": "/view/wc/pipeSize/{id}"
      },
			"transaction": [{
	      
	    }]
	}
}

export default dat;
