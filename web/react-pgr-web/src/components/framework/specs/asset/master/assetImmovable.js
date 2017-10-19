var dat = {
	"asset.create": {
		"numCols": 12/3,
		"url": "",
		"tenantIdRequired": true,
		"idJsonPath": "",
		"objectName": "Assets",
		"groups": [
			{
				"label": "ac.create.Header.Details",
				"name": "createAsset",
				"fields": [
						{
							"name": "AssetIdNo",
							"jsonPath": "Assets[0].code",
							"label": "ac.create.Asset.Id.No",
							"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
							"type": "text",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
  						"name": "OldReferenceNumber",
  						"jsonPath": "Assets[0].oldCode",
  						"label": "ac.create.Old.Asset.Id.No",
  						"pattern": "",
  						"type": "text",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
              "name": "Date",
              "jsonPath": "Assets[0].",
              "label": "ac.create.Date",
              "pattern": "",
              "type": "date",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
  						"name": "RefNoOfWIP",
  						"jsonPath": "Assets[0].wipReferenceNo",
  						"label": "ac.create.Ref.WIP.Register",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "AssetName",
  						"jsonPath": "Assets[0].name",
  						"label": "ac.create.Asset.Name",
  						"pattern": "",
  						"type": "text",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "AssetCategoryType",
  						"jsonPath": "Assets[0].",
  						"label": "ac.create.Asset.SubCategory.Name",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "Department",
  						"jsonPath": "Assets[0].department",
  						"label": "ac.create.Department",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "NoOfOrder",
  						"jsonPath": "Assets[0].orderNumber",
  						"label": "ac.create.No.Of.Order",
  						"pattern": "",
  						"type": "number",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
              "name": "DateOfOrder",
              "jsonPath": "Assets[0].orderDate",
              "label": "ac.create.Date.Of.Order",
              "pattern": "",
              "type": "date",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "DateOfAcquisition",
              "jsonPath": "Assets[0].acquisitionDate",
              "label": "ac.create.Date.Of.Acquisition",
              "pattern": "",
              "type": "date",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
  						"name": "ModeofAcquisition",
  						"jsonPath": "Assets[0].modeOfAcquisition",
  						"label": "ac.create.Mode.of.Acquisition",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "LandAssetID",
  						"jsonPath": "Assets[0].",
  						"label": "ac.create.Land.Asset.ID",
  						"pattern": "",
  						"type": "autoCompelete",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": "",
              "autoCompleteDependancy": {
								"autoCompleteUrl": "/egf-masters/chartofaccounts/_search?id={value}",
								"autoFillFields": {
									"BusinessDetails[0].accountDetails[0].chartOfAccounts": "chartOfAccounts[0].glcode"
								 }
							 }
  					},
            {
  						"name": "SurveyNoOfLandOnWhichStructureIsLocated ",
  						"jsonPath": "Assets[0].landSurveyNo",
  						"label": "ac.create.Survey.no.of.land",
  						"pattern": "",
  						"type": "number",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": true,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
				]
			},
      {
				"label": "ac.create.Location.Details",
				"name": "LocationField",
        "multiple":false,
        "jsonPath":"Assets[0].",
				"fields": [
          {
            "name": "Location",
            "jsonPath": "Assets[0].locationDetails",
            "label": "ac.create.Location",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Longitude",
            "jsonPath": "Assets[0].longitude",
            "label": "ac.create.Longitude",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Latitude",
            "jsonPath": "Assets[0].latitude",
            "label": "ac.create.Latitude",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Address",
            "jsonPath": "Assets[0].address",
            "label": "ac.create.Address",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "ElectionWard",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Election.Ward",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "No of Floors",
            "jsonPath": "Assets[0].floors",
            "label": "ac.create.No.of.Floors",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "PlinthArea",
            "jsonPath": "Assets[0].plinthArea",
            "label": "ac.create.Plinth.Area",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "CubicContents",
            "jsonPath": "Assets[0].cubicContents",
            "label": "ac.create.Cubic.Contents",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "Usage",
            "jsonPath": "Assets[0].usage",
            "label": "ac.create.Usage",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DimensionOfStructure(L,B,H)",
            "jsonPath": "Assets[0].length",
            "label": "ac.create.Dimension.of.Structure",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "DimensionOfStructure(L,B,H)",
            "jsonPath": "Assets[0].width",
            "label": "ac.create.Dimension.breadth",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "DimensionOfStructure(L,B,H)",
            "jsonPath": "Assets[0].height",
            "label": "ac.create.Dimension.height",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "AreaofLandonwhichconstructed",
            "jsonPath": "Assets[0].totalArea",
            "label": "Area of Land on which constructed",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },

				]
			},
      {
				"label": "ac.create.Asset.Details",
				"name": "AssetField",
        "multiple":false,
        "jsonPath":"Assets[0].",
				"fields": [
          {
            "name": "AnticipatedLifeOfAsset",
            "jsonPath": "Assets[0].anticipatedLife",
            "label": "ac.create.Anticipated.life.of.Asset",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "OriginalValueOfAsset",
            "jsonPath": "Assets[0].originalValue",
            "label": "ac.create.Original.Value.of.Asset",
            "pattern": "^[1-9]\\d{0,3}(\\.\\d{0,3})*(,\\d+)?$",
            "type": "number",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "FromWhomAcquired",
            "jsonPath": "Assets[0].acquiredFrom",
            "label": "ac.create.From.whom.acquired",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "TitleDocumentsAvailable",
            "jsonPath": "Assets[0].titleDocumentsAvalable",
            "label": "ac.create.Title.documents.available",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "SourceOfFunds",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Source.of.funds",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Warranty",
            "jsonPath": "Assets[0].warrantyAvailable",
            "label": "ac.create.Warranty",
            "pattern": "",
            "type": "radio",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "values": [{"label":"Yes", "value":true},{"label":"No", "value":false}],
            "defaultValue":true
          },
          {
            "name": "WarrantyExpiryDate",
            "jsonPath": "Assets[0].warrantyExpiryDate",
            "label": "ac.create.Warranty.expiry.date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "SecurityDepositRetained",
            "jsonPath": "Assets[0].securityDepositRetained",
            "label": "ac.create.Security.deposit.retained",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "SecurityDepositRealized",
            "jsonPath": "Assets[0].securityDepositRealized",
            "label": "ac.create.Security.deposit.realized",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DefectLiabilityPeriod",
            "jsonPath": "Assets[0].defectLiabilityPeriod[0].year",
            "label": "ac.create.Defect.liability.Period",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"defaultValue": [	{
								"key": null,
								"value": "Years"
							},  {
            "key": "1",
            "value": "1"
          },  {
            "key": "2",
            "value": "2"
          },  {
            "key": "3",
            "value": "3"
          }
				]
          },
					{
            "name": "DefectLiabilityPeriod",
            "jsonPath": "Assets[0].defectLiabilityPeriod[0].month",
            "label": "ac.create.Defect.liability.Period.month",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"defaultValue": [	{
								"key": null,
								"value": "Months"
							},  {
            "key": "1",
            "value": "1"
          },  {
            "key": "2",
            "value": "2"
          },  {
            "key": "3",
            "value": "3"
          }
				]
          },
					{
            "name": "DefectLiabilityPeriod",
            "jsonPath": "Assets[0].defectLiabilityPeriod[0].day",
            "label": "ac.create.Defect.liability.Period.day",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"defaultValue": [	{
								"key": null,
								"value": "Days"
							},  {
            "key": "1",
            "value": "1"
          },  {
            "key": "2",
            "value": "2"
          },  {
            "key": "3",
            "value": "3"
          }
				]
          },

          {
            "name": "AssetDescription",
            "jsonPath": "Assets[0].description",
            "label": "ac.create.Asset.description",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "AssetAcountCode",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Asset.account.code",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "AccumulatedDepreciationAccount",
            "jsonPath": "Assets[0].accumulatedDepreciationAccount",
            "label": "ac.create.Accumulated.Depreciation.Account",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "RevaluationReserveAccount",
            "jsonPath": "Assets[0].revaluationReserveAccount",
            "label": "ac.create.Revaluation.Reserve.Account",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DepreciationExpensesAccount ",
            "jsonPath": "Assets[0].depreciationExpenseAccount",
            "label": "ac.create.Depreciation.Expenses.Account",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },

				]
			},
      {
				"name": "TableField",
        "jsonPath":"Assets",
				"fields": [
          // {
          //   "name": "AnticipatedLifeOfAsset",
          //   "label": "Anticipated life of Asset",
          //   "type": "dynamicTable"
          //   "resultList": {
          //     "resultHeader": [{"label": "asset.create.test"}],
          //     "resultValues": [[{}, ], [], []]
          //   }
          // },
          {
            "name": "OpeningDate",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Opening.date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "OpeningWrittenDownValue",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Opening.Written.down.Value",
            "pattern": "^[1-9]\\d{0,3}(\\.\\d{0,3})*(,\\d+)?$",
            "type": "number",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "AdditionDate",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Addition.Date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "AdditionValue",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Addition.Value",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "RevaluationDate",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Revaluation.date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "RevaluationValue",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Revaluation.Value",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DepreciationDate",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Depreciation.Date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DepreciationValue",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Depreciation.Value",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Closing date",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Closing.Date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "ClosingValue",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Closing.written.Down.Value",
            "pattern": "",
            "type": "date",
            "url": "",
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
		"url": "/asset-services/assets/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "Assets",
		"groups": [
			{
				"label": "ac.search.asset.title",
				"name": "createCategoryType",
				"fields": [
					{
						"name": "AssetSearchCode",
						"jsonPath": "",
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
						"jsonPath": "",
						"label": "ac.search.asset.name",
						"pattern": "",
						"type": "text",
						"url": "",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "AssetSearchAssetCategory",
						"jsonPath": "",
						"label": "ac.search.category",
						"pattern": "",
						"type": "singleValueList",
						"url": "",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "AssetSearchDepartment",
						"jsonPath": "",
						"label": "ac.search.department",
						"pattern": "",
						"type": "singleValueList",
						"url": "",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "AssetSearchStatus",
						"jsonPath": "",
						"label": "ac.search.status",
						"pattern": "",
						"type": "singleValueList",
						"url": "",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					}
				]
			}
		],
		"result": {
			"header": [{label: "ac.create.Asset.account.code"},{label: "ac.create.Asset.Name"}, {label: "ac.create.asset.asset.category.type"}, {label: "ac.create.Department"}, {label: "ac.search.status"}],
			"values": ["code","name", "assetCategory.assetCategoryType", "department.id", "status"],
			"resultPath": "Assets",
			// "rowClickUrlUpdate": "/update/asset/assetMovable/{id}",
			// "rowClickUrlView": "/view/asset/assetMovable/{id}"
			}
	},
	"asset.view": {
		"numCols": 12/2,
		"url": "/asset-services/assets/_search?ids={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "Assets[0]",
		"groups": [
			{
				"label": "ac.create.Header.Details",
				"name": "createAsset",
				"fields": [
						{
							"name": "AssetIdNo",
							"jsonPath": "Assets[0].code",
							"label": "ac.create.Asset.Id.No",
							"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
							"type": "text",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
  						"name": "OldReferenceNumber",
  						"jsonPath": "Assets[0].oldCode",
  						"label": "ac.create.Old.Asset.Id.No",
  						"pattern": "",
  						"type": "text",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
              "name": "Date",
              "jsonPath": "Assets[0].",
              "label": "ac.create.Date",
              "pattern": "",
              "type": "date",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
  						"name": "RefNoOfWIP",
  						"jsonPath": "Assets[0].wipReferenceNo",
  						"label": "ac.create.Ref.WIP.Register",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "AssetName",
  						"jsonPath": "Assets[0].name",
  						"label": "ac.create.Asset.Name",
  						"pattern": "",
  						"type": "text",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "AssetCategoryType",
  						"jsonPath": "Assets[0].",
  						"label": "ac.create.Asset.SubCategory.Name",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "Department",
  						"jsonPath": "Assets[0].department",
  						"label": "ac.create.Department",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "NoOfOrder",
  						"jsonPath": "Assets[0].orderNumber",
  						"label": "ac.create.No.Of.Order",
  						"pattern": "",
  						"type": "number",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
              "name": "DateOfOrder",
              "jsonPath": "Assets[0].orderDate",
              "label": "ac.create.Date.Of.Order",
              "pattern": "",
              "type": "date",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "DateOfAcquisition",
              "jsonPath": "Assets[0].acquisitionDate",
              "label": "ac.create.Date.Of.Acquisition",
              "pattern": "",
              "type": "date",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
  						"name": "ModeofAcquisition",
  						"jsonPath": "Assets[0].modeOfAcquisition",
  						"label": "ac.create.Mode.of.Acquisition",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "LandAssetID",
  						"jsonPath": "Assets[0].",
  						"label": "ac.create.Land.Asset.ID",
  						"pattern": "",
  						"type": "autoCompelete",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": "",
              "autoCompleteDependancy": {
								"autoCompleteUrl": "/egf-masters/chartofaccounts/_search?id={value}",
								"autoFillFields": {
									"BusinessDetails[0].accountDetails[0].chartOfAccounts": "chartOfAccounts[0].glcode"
								 }
							 }
  					},
            {
  						"name": "SurveyNoOfLandOnWhichStructureIsLocated ",
  						"jsonPath": "Assets[0].landSurveyNo",
  						"label": "ac.create.Survey.no.of.land",
  						"pattern": "",
  						"type": "number",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": true,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
				]
			},
      {
				"label": "ac.create.Location.Details",
				"name": "LocationField",
        "multiple":false,
        "jsonPath":"Assets[0].",
				"fields": [
          {
            "name": "Location",
            "jsonPath": "Assets[0].locationDetails",
            "label": "ac.create.Location",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Longitude",
            "jsonPath": "Assets[0].longitude",
            "label": "ac.create.Longitude",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Latitude",
            "jsonPath": "Assets[0].latitude",
            "label": "ac.create.Latitude",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Address",
            "jsonPath": "Assets[0].address",
            "label": "ac.create.Address",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "ElectionWard",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Election.Ward",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "No of Floors",
            "jsonPath": "Assets[0].floors",
            "label": "ac.create.No.of.Floors",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "PlinthArea",
            "jsonPath": "Assets[0].plinthArea",
            "label": "ac.create.Plinth.Area",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "CubicContents",
            "jsonPath": "Assets[0].cubicContents",
            "label": "ac.create.Cubic.Contents",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "Usage",
            "jsonPath": "Assets[0].usage",
            "label": "ac.create.Usage",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DimensionOfStructure(L,B,H)",
            "jsonPath": "Assets[0].length",
            "label": "ac.create.Dimension.of.Structure",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "DimensionOfStructure(L,B,H)",
            "jsonPath": "Assets[0].width",
            "label": "ac.create.Dimension.breadth",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "DimensionOfStructure(L,B,H)",
            "jsonPath": "Assets[0].height",
            "label": "ac.create.Dimension.height",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "AreaofLandonwhichconstructed",
            "jsonPath": "Assets[0].totalArea",
            "label": "Area of Land on which constructed",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },

				]
			},
      {
				"label": "ac.create.Asset.Details",
				"name": "AssetField",
        "multiple":false,
        "jsonPath":"Assets[0].",
				"fields": [
          {
            "name": "AnticipatedLifeOfAsset",
            "jsonPath": "Assets[0].anticipatedLife",
            "label": "ac.create.Anticipated.life.of.Asset",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "OriginalValueOfAsset",
            "jsonPath": "Assets[0].originalValue",
            "label": "ac.create.Original.Value.of.Asset",
            "pattern": "^[1-9]\\d{0,3}(\\.\\d{0,3})*(,\\d+)?$",
            "type": "number",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "FromWhomAcquired",
            "jsonPath": "Assets[0].acquiredFrom",
            "label": "ac.create.From.whom.acquired",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "TitleDocumentsAvailable",
            "jsonPath": "Assets[0].titleDocumentsAvalable",
            "label": "ac.create.Title.documents.available",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "SourceOfFunds",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Source.of.funds",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Warranty",
            "jsonPath": "Assets[0].warrantyAvailable",
            "label": "ac.create.Warranty",
            "pattern": "",
            "type": "radio",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "values": [{"label":"Yes", "value":true},{"label":"No", "value":false}],
            "defaultValue":true
          },
          {
            "name": "WarrantyExpiryDate",
            "jsonPath": "Assets[0].warrantyExpiryDate",
            "label": "ac.create.Warranty.expiry.date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "SecurityDepositRetained",
            "jsonPath": "Assets[0].securityDepositRetained",
            "label": "ac.create.Security.deposit.retained",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "SecurityDepositRealized",
            "jsonPath": "Assets[0].securityDepositRealized",
            "label": "ac.create.Security.deposit.realized",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DefectLiabilityPeriod",
            "jsonPath": "Assets[0].defectLiabilityPeriod[0].year",
            "label": "ac.create.Defect.liability.Period",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"defaultValue": [	{
								"key": null,
								"value": "Years"
							},  {
            "key": "1",
            "value": "1"
          },  {
            "key": "2",
            "value": "2"
          },  {
            "key": "3",
            "value": "3"
          }
				]
          },
					{
            "name": "DefectLiabilityPeriod",
            "jsonPath": "Assets[0].defectLiabilityPeriod[0].month",
            "label": "ac.create.Defect.liability.Period.month",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"defaultValue": [	{
								"key": null,
								"value": "Months"
							},  {
            "key": "1",
            "value": "1"
          },  {
            "key": "2",
            "value": "2"
          },  {
            "key": "3",
            "value": "3"
          }
				]
          },
					{
            "name": "DefectLiabilityPeriod",
            "jsonPath": "Assets[0].defectLiabilityPeriod[0].day",
            "label": "ac.create.Defect.liability.Period.day",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"defaultValue": [	{
								"key": null,
								"value": "Days"
							},  {
            "key": "1",
            "value": "1"
          },  {
            "key": "2",
            "value": "2"
          },  {
            "key": "3",
            "value": "3"
          }
				]
          },

          {
            "name": "AssetDescription",
            "jsonPath": "Assets[0].description",
            "label": "ac.create.Asset.description",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "AssetAcountCode",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Asset.account.code",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "AccumulatedDepreciationAccount",
            "jsonPath": "Assets[0].accumulatedDepreciationAccount",
            "label": "ac.create.Accumulated.Depreciation.Account",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "RevaluationReserveAccount",
            "jsonPath": "Assets[0].revaluationReserveAccount",
            "label": "ac.create.Revaluation.Reserve.Account",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DepreciationExpensesAccount ",
            "jsonPath": "Assets[0].depreciationExpenseAccount",
            "label": "ac.create.Depreciation.Expenses.Account",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },

				]
			},
      {
				"name": "TableField",
        "jsonPath":"Assets",
				"fields": [
          // {
          //   "name": "AnticipatedLifeOfAsset",
          //   "label": "Anticipated life of Asset",
          //   "type": "dynamicTable"
          //   "resultList": {
          //     "resultHeader": [{"label": "asset.create.test"}],
          //     "resultValues": [[{}, ], [], []]
          //   }
          // },
          {
            "name": "OpeningDate",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Opening.date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "OpeningWrittenDownValue",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Opening.Written.down.Value",
            "pattern": "^[1-9]\\d{0,3}(\\.\\d{0,3})*(,\\d+)?$",
            "type": "number",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "AdditionDate",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Addition.Date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "AdditionValue",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Addition.Value",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "RevaluationDate",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Revaluation.date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "RevaluationValue",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Revaluation.Value",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DepreciationDate",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Depreciation.Date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DepreciationValue",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Depreciation.Value",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Closing date",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Closing.Date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "ClosingValue",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Closing.written.Down.Value",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          }

				]
			}
		]
	},
	"asset.update": {
		"numCols": 12/2,
		"searchUrl": "/asset-services/assets/_search?ids={id}",
		"url": "",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "Assets[0]",
		"groups": [
			{
				"label": "ac.update.Header.Details",
				"name": "createAsset",
				"fields": [
						{
							"name": "AssetIdNo",
							"jsonPath": "Assets[0].code",
							"label": "ac.create.Asset.Id.No",
							"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
							"type": "text",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
  						"name": "OldReferenceNumber",
  						"jsonPath": "Assets[0].oldCode",
  						"label": "ac.create.Old.Asset.Id.No",
  						"pattern": "",
  						"type": "text",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
              "name": "Date",
              "jsonPath": "Assets[0].",
              "label": "ac.create.Date",
              "pattern": "",
              "type": "date",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
  						"name": "RefNoOfWIP",
  						"jsonPath": "Assets[0].wipReferenceNo",
  						"label": "ac.create.Ref.WIP.Register",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "AssetName",
  						"jsonPath": "Assets[0].name",
  						"label": "ac.create.Asset.Name",
  						"pattern": "",
  						"type": "text",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "AssetCategoryType",
  						"jsonPath": "Assets[0].",
  						"label": "ac.create.Asset.SubCategory.Name",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "Department",
  						"jsonPath": "Assets[0].department",
  						"label": "ac.create.Department",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "NoOfOrder",
  						"jsonPath": "Assets[0].orderNumber",
  						"label": "ac.create.No.Of.Order",
  						"pattern": "",
  						"type": "number",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
              "name": "DateOfOrder",
              "jsonPath": "Assets[0].orderDate",
              "label": "ac.create.Date.Of.Order",
              "pattern": "",
              "type": "date",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "DateOfAcquisition",
              "jsonPath": "Assets[0].acquisitionDate",
              "label": "ac.create.Date.Of.Acquisition",
              "pattern": "",
              "type": "date",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
  						"name": "ModeofAcquisition",
  						"jsonPath": "Assets[0].modeOfAcquisition",
  						"label": "ac.create.Mode.of.Acquisition",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "LandAssetID",
  						"jsonPath": "Assets[0].",
  						"label": "ac.create.Land.Asset.ID",
  						"pattern": "",
  						"type": "autoCompelete",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": "",
              "autoCompleteDependancy": {
								"autoCompleteUrl": "/egf-masters/chartofaccounts/_search?id={value}",
								"autoFillFields": {
									"BusinessDetails[0].accountDetails[0].chartOfAccounts": "chartOfAccounts[0].glcode"
								 }
							 }
  					},
            {
  						"name": "SurveyNoOfLandOnWhichStructureIsLocated ",
  						"jsonPath": "Assets[0].landSurveyNo",
  						"label": "ac.create.Survey.no.of.land",
  						"pattern": "",
  						"type": "number",
  						"url": "",
  						"isRequired": false,
  						"isDisabled": true,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
				]
			},
      {
				"label": "ac.create.Location.Details",
				"name": "LocationField",
        "multiple":false,
        "jsonPath":"Assets[0].",
				"fields": [
          {
            "name": "Location",
            "jsonPath": "Assets[0].locationDetails",
            "label": "ac.create.Location",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Longitude",
            "jsonPath": "Assets[0].longitude",
            "label": "ac.create.Longitude",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Latitude",
            "jsonPath": "Assets[0].latitude",
            "label": "ac.create.Latitude",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Address",
            "jsonPath": "Assets[0].address",
            "label": "ac.create.Address",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "ElectionWard",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Election.Ward",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "No of Floors",
            "jsonPath": "Assets[0].floors",
            "label": "ac.create.No.of.Floors",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "PlinthArea",
            "jsonPath": "Assets[0].plinthArea",
            "label": "ac.create.Plinth.Area",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "CubicContents",
            "jsonPath": "Assets[0].cubicContents",
            "label": "ac.create.Cubic.Contents",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "Usage",
            "jsonPath": "Assets[0].usage",
            "label": "ac.create.Usage",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DimensionOfStructure(L,B,H)",
            "jsonPath": "Assets[0].length",
            "label": "ac.create.Dimension.of.Structure",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "DimensionOfStructure(L,B,H)",
            "jsonPath": "Assets[0].width",
            "label": "ac.create.Dimension.breadth",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "DimensionOfStructure(L,B,H)",
            "jsonPath": "Assets[0].height",
            "label": "ac.create.Dimension.height",
            "pattern": "",
            "type": "number",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "AreaofLandonwhichconstructed",
            "jsonPath": "Assets[0].totalArea",
            "label": "Area of Land on which constructed",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },

				]
			},
      {
				"label": "ac.create.Asset.Details",
				"name": "AssetField",
        "multiple":false,
        "jsonPath":"Assets[0].",
				"fields": [
          {
            "name": "AnticipatedLifeOfAsset",
            "jsonPath": "Assets[0].anticipatedLife",
            "label": "ac.create.Anticipated.life.of.Asset",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "OriginalValueOfAsset",
            "jsonPath": "Assets[0].originalValue",
            "label": "ac.create.Original.Value.of.Asset",
            "pattern": "^[1-9]\\d{0,3}(\\.\\d{0,3})*(,\\d+)?$",
            "type": "number",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "FromWhomAcquired",
            "jsonPath": "Assets[0].acquiredFrom",
            "label": "ac.create.From.whom.acquired",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "TitleDocumentsAvailable",
            "jsonPath": "Assets[0].titleDocumentsAvalable",
            "label": "ac.create.Title.documents.available",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "SourceOfFunds",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Source.of.funds",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Warranty",
            "jsonPath": "Assets[0].warrantyAvailable",
            "label": "ac.create.Warranty",
            "pattern": "",
            "type": "radio",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "values": [{"label":"Yes", "value":true},{"label":"No", "value":false}],
            "defaultValue":true
          },
          {
            "name": "WarrantyExpiryDate",
            "jsonPath": "Assets[0].warrantyExpiryDate",
            "label": "ac.create.Warranty.expiry.date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "SecurityDepositRetained",
            "jsonPath": "Assets[0].securityDepositRetained",
            "label": "ac.create.Security.deposit.retained",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "SecurityDepositRealized",
            "jsonPath": "Assets[0].securityDepositRealized",
            "label": "ac.create.Security.deposit.realized",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DefectLiabilityPeriod",
            "jsonPath": "Assets[0].defectLiabilityPeriod[0].year",
            "label": "ac.create.Defect.liability.Period",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"defaultValue": [	{
								"key": null,
								"value": "Years"
							},  {
            "key": "1",
            "value": "1"
          },  {
            "key": "2",
            "value": "2"
          },  {
            "key": "3",
            "value": "3"
          }
				]
          },
					{
            "name": "DefectLiabilityPeriod",
            "jsonPath": "Assets[0].defectLiabilityPeriod[0].month",
            "label": "ac.create.Defect.liability.Period.month",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"defaultValue": [	{
								"key": null,
								"value": "Months"
							},  {
            "key": "1",
            "value": "1"
          },  {
            "key": "2",
            "value": "2"
          },  {
            "key": "3",
            "value": "3"
          }
				]
          },
					{
            "name": "DefectLiabilityPeriod",
            "jsonPath": "Assets[0].defectLiabilityPeriod[0].day",
            "label": "ac.create.Defect.liability.Period.day",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
						"defaultValue": [	{
								"key": null,
								"value": "Days"
							},  {
            "key": "1",
            "value": "1"
          },  {
            "key": "2",
            "value": "2"
          },  {
            "key": "3",
            "value": "3"
          }
				]
          },

          {
            "name": "AssetDescription",
            "jsonPath": "Assets[0].description",
            "label": "ac.create.Asset.description",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "AssetAcountCode",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Asset.account.code",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "AccumulatedDepreciationAccount",
            "jsonPath": "Assets[0].accumulatedDepreciationAccount",
            "label": "ac.create.Accumulated.Depreciation.Account",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "RevaluationReserveAccount",
            "jsonPath": "Assets[0].revaluationReserveAccount",
            "label": "ac.create.Revaluation.Reserve.Account",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DepreciationExpensesAccount ",
            "jsonPath": "Assets[0].depreciationExpenseAccount",
            "label": "ac.create.Depreciation.Expenses.Account",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },

				]
			},
      {
				"name": "TableField",
        "jsonPath":"Assets",
				"fields": [
          // {
          //   "name": "AnticipatedLifeOfAsset",
          //   "label": "Anticipated life of Asset",
          //   "type": "dynamicTable"
          //   "resultList": {
          //     "resultHeader": [{"label": "asset.create.test"}],
          //     "resultValues": [[{}, ], [], []]
          //   }
          // },
          {
            "name": "OpeningDate",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Opening.date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "OpeningWrittenDownValue",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Opening.Written.down.Value",
            "pattern": "^[1-9]\\d{0,3}(\\.\\d{0,3})*(,\\d+)?$",
            "type": "number",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "AdditionDate",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Addition.Date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "AdditionValue",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Addition.Value",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "RevaluationDate",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Revaluation.date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "RevaluationValue",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Revaluation.Value",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DepreciationDate",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Depreciation.Date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DepreciationValue",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Depreciation.Value",
            "pattern": "",
            "type": "text",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Closing date",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Closing.Date",
            "pattern": "",
            "type": "date",
            "url": "",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "ClosingValue",
            "jsonPath": "Assets[0].",
            "label": "ac.create.Closing.written.Down.Value",
            "pattern": "",
            "type": "date",
            "url": "",
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
