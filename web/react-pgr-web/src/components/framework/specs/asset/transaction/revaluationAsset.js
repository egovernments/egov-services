var dat = {
	"asset.transaction": {
		"numCols": 12/1,
		"url": "",
    "tenantIdRequired": true,
    "objectName": "Revaluations",
    "useTimestamp": true,
    "groups": [{
        "label": "ac.transaction.RA.title",
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
            "jsonPath": "Revaluations[0].",
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
            "jsonPath": "Revaluations[0].",
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
            "jsonPath": "Revaluations[0].",
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
            "jsonPath": "Revaluations[0].",
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
            "jsonPath": "Revaluations[0].",
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
	          "name": "ValuationDate",
	          "jsonPath": "Revaluations[0].revaluationDate",
	          //"label": "ac.create.Revaluation.date",
						"label": "Revaluation date",
	          "pattern": "",
	          "type": "date",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "",
	          "patternErrMsg": "",
	        },
	        {
	          "name": "OrderNo",
	          "jsonPath": "Revaluations[0].orderNumber",
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
	          "jsonPath": "Revaluations[0].orderDate",
	          //"label": "ac.transaction.create.order.date",
						"label": "Order Date",
	          "pattern": "",
	          "type": "date",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "", //Remove required messages
	          "patternErrMsg": "",
	        },
	        {
	          "name": "ValuationAmount",
	          "jsonPath": "Revaluations[0].valueAfterRevaluation",
	          "label": "Valuation Amount",
	          "pattern": "",
	          "type": "text",
	          "isRequired": true,
	          "isDisabled": false,
	          "requiredErrMsg": "", //Remove required messages
	          "patternErrMsg": ""
	        },
					{
	          "name": "Addition/deductedAmount",
	          "jsonPath": "Revaluations[0].revaluationAmount",
	          //"label": "ac.transaction.create.AdditionDeductedAmount",
						"label": "Addition/ deducted amount",
	          "pattern": "",
	          "type": "text",
	          "isRequired": false,
	          "isDisabled": true,
	          "requiredErrMsg": "", //Remove required messages
	          "patternErrMsg": ""
	        }
	      ]
	    }]
	}
}

export default dat;
