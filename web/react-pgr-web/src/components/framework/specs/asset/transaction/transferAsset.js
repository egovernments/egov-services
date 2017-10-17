var dat = {
	"asset.transaction": {
		"numCols": 12/3,
		"url": "",
    "tenantIdRequired": true,
    "objectName": "",
    "useTimestamp": true,
    "groups": [{
        "label": "ac.transaction.TA.title",
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
              "label": "ac.create.Asset.SubCategory",
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
              "label": "ac.create.Asset.Code",
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
              "label": "ac.create.Asset.Name",
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
            "label": "ac.create.Select",
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
            "label": "ac.create.Asset.Code",
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
            "label": "ac.create.Name.of.Asset",
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
            "label": "ac.transaction.create.datePurchaseConstruction",
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
            "label": "ac.create.WDV.Value",
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
            "values": [{"label":"ac.transaction.TA.TransferAsset", "value":true},{"label":"ac.transaction.TA.DisposalAsset", "value":false}],
            "defaultValue":true
          },,
	        {
	          "name": "Transfer/DisposalDate",
	          "jsonPath": "",
	          "label": "ac.transaction.TA.TransferDisposalDate",
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
	          "label": "ac.transaction.create.order.no",
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
	          "label": "ac.transaction.create.order.date",
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
	          "label": "ac.transaction.TA.purpose",
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
	          "label": "ac.transaction.TA.amount",
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
	          "label": "ac.transaction.TA.assetTransferSold",
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
	          "label": "ac.transaction.TA.remarks",
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
