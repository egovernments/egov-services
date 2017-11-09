var dat = {
	"asset.transaction": {
		"numCols": 12/3,
		"url": "/asset-services-maha/assets/revaluation/_create",
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
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
							"name": "AssetSearchAssetSubCategory",
							"jsonPath": "assetCategory",
							"label": "ac.create.Asset.SubCategory.Name",
							"pattern": "",
							"type": "singleValueList",
							"url": "/egov-mdms-service/v1/_get?&moduleName=ASSET&masterName=AssetCategory&filter=%5B%3F(%20%40.isAssetAllow%20%3D%3D%20true%20%26%26%20%40.assetCategoryType%20%3D%3D%20%22IMMOVABLE%22)%5D|$.MdmsRes.ASSET.AssetCategory.*.id|$.MdmsRes.ASSET.AssetCategory.*.name",
							// "url": "/egov-mdms-service/v1/_get?&masterName=AssetCategory&moduleName=ASSET|$.MdmsRes.ASSET.AssetCategory.*.id|$.MdmsRes.ASSET.AssetCategory.*.name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"isStateLevel":true
						},
						{
							"name": "AssetSearchCode",
							"jsonPath": "code",
							"label": "ac.search.asset.code",
							"pattern": "",
							"type": "text",
							"url": "",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "AssetSearchName",
							"jsonPath": "name",
							"label": "ac.search.asset.name",
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
        "header": [
					{
						"name": "",
	          "jsonPath": "isRadio",
	          "label": "",
	          "pattern": "",
	          "type": "radio",
	          "isRequired": false,
	          "isDisabled": false,
	          "requiredErrMsg": "",
	          "patternErrMsg": "",
	    			"values": [{"label":"", "value":true}],
	    			"defaultValue":false
			},
					{
            "name": "AssetCode",
            "jsonPath": "code",
            "label": "ac.create.Asset.Code",
            "pattern": "",
            "type": "label",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"isLabel": false,
          },
          {
            "name": "NameAsset",
            "jsonPath": "name",
            "label": "ac.create.Name.of.Asset",
            "pattern": "",
            "type": "label",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"isLabel": false,
          },
          {
            "name": "DateofPurchase/Construction/Acquisition",
            "jsonPath": "acquisitionDate",
            "label": "ac.transaction.create.datePurchaseConstruction",
            "pattern": "",
            "type": "label",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"isDate":true,
						"isLabel": false,
          },
          {
            "name": "WdvValue",
            "jsonPath": "",
            "label": "ac.create.WDV.Value",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"defaultValue": "10000"
          },
        ],
        // "values": ["businessService", "consumerCode", "totalAmount","minimumAmount","bill"],
        "resultPath": "Assets",
        "tableResultPath": "Revaluation.Assets",

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
