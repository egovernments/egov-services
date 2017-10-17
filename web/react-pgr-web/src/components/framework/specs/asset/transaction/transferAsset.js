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
            },
            {
              "name": "AssetCode",
              "jsonPath": "",
              "label": "Asset Code",
              "pattern": "",
              "type": "text",
              "url": "",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "AssetName",
              "jsonPath": "",
              "label": "Asset Name",
              "pattern": "",
              "type": "text",
              "url": "",
              "isRequired": false,
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
            "name": "DateofPurchase/Construction/Acquisition",
            "jsonPath": "",
            "label": "Date of Purchase/Construction/Acquisition",
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
        ],
        // "values": ["businessService", "consumerCode", "totalAmount","minimumAmount","bill"],
        "resultPath": "",
        "tableResultPath": "",

        // "rowClickUrlUpdate": "/update/wc/pipeSize/{id}",
        // "rowClickUrlView": "/view/wc/pipeSize/{id}"
      },
			"transaction": [{
	      "label": "",
	      "name": "OtherDetails",
	      "children": [],
	      "fields": [
					{
            "name": "TypeofAsset",
            "jsonPath": "",
            "label": "",
            "pattern": "",
            "type": "radio",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "values": [{"label":"Transfer of Asset", "value":true},{"label":"Disposal of Asset", "value":false}],
            "defaultValue":true
          },,
	        {
	          "name": "Transfer/DisposalDate",
	          "jsonPath": "",
	          "label": "Transfer / Disposal date",
	          "pattern": "",
	          "type": "date",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "",
	          "patternErrMsg": "",
	        },
	        {
	          "name": "OrderNo",
	          "jsonPath": "",
	          "label": "Order No",
	          "pattern": "",
	          "type": "text",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "", //Remove required messages
	          "patternErrMsg": ""
	        },
	        {
	          "name": "OrderDate",
	          "jsonPath": "",
	          "label": "Order Date",
	          "pattern": "",
	          "type": "date",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "", //Remove required messages
	          "patternErrMsg": "",
	        },
					{
	          "name": "Purpose",
	          "jsonPath": "",
	          "label": "Purpose",
	          "pattern": "",
	          "type": "text",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "", //Remove required messages
	          "patternErrMsg": ""
	        },
	        {
	          "name": "Amount",
	          "jsonPath": "",
	          "label": "Amount",
	          "pattern": "",
	          "type": "number",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "", //Remove required messages
	          "patternErrMsg": ""
	        },
					{
	          "name": "AssetTransfer/SoldTo ",
	          "jsonPath": "",
	          "label": "Asset Transfer / Sold to ",
	          "pattern": "",
	          "type": "text",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "", //Remove required messages
	          "patternErrMsg": ""
	        },
					{
	          "name": "Remarks",
	          "jsonPath": "",
	          "label": "Remarks",
	          "pattern": "",
	          "type": "text",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "", //Remove required messages
	          "patternErrMsg": ""
	        },
	      ]
	    }]
	}
}

export default dat;
