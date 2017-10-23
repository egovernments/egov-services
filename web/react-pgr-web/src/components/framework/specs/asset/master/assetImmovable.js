var dat = {
	"asset.create": {
		"numCols": 12/3,
		"url": "",
		"tenantIdRequired": true,
		"idJsonPath": "",
		"objectName": "Asset",
		"groups": [
			{
				"label": "ac.create.Header.Details",
				"name": "createAsset",
				"fields": [
						{
							"name": "AssetIdNo",
							"jsonPath": "Asset[0].code",
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
  						"jsonPath": "Asset[0].oldCode",
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
              "jsonPath": "Asset[0].dateOfCreation",
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
  						"jsonPath": "Asset[0].wipReferenceNo",
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
  						"jsonPath": "Asset[0].name",
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
  						"jsonPath": "Asset[0].assetCategory[0].name",
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
  						"jsonPath": "Asset[0].department",
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
  						"jsonPath": "Asset[0].orderNumber",
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
              "jsonPath": "Asset[0].orderDate",
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
              "jsonPath": "Asset[0].acquisitionDate",
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
  						"jsonPath": "Asset[0].modeOfAcquisition",
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
  						"jsonPath": "Asset[0].",
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
  						"jsonPath": "Asset[0].landSurveyNo",
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
        "jsonPath":"Asset",
				"fields": [
          {
            "name": "Location",
            "jsonPath": "Asset[0].locationDetails",
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
            "jsonPath": "Asset[0].longitude",
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
            "jsonPath": "Asset[0].latitude",
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
            "jsonPath": "Asset[0].address",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].floors",
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
            "jsonPath": "Asset[0].plinthArea",
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
            "jsonPath": "Asset[0].cubicContents",
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
            "jsonPath": "Asset[0].usage",
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
            "jsonPath": "Asset[0].length",
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
            "jsonPath": "Asset[0].width",
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
            "jsonPath": "Asset[0].height",
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
            "jsonPath": "Asset[0].totalArea",
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
        "jsonPath":"Asset",
				"fields": [
          {
            "name": "AnticipatedLifeOfAsset",
            "jsonPath": "Asset[0].anticipatedLife",
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
            "jsonPath": "Asset[0].originalValue",
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
            "jsonPath": "Asset[0].acquiredFrom",
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
            "jsonPath": "Asset[0].titleDocumentsAvalable",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].warrantyAvailable",
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
            "jsonPath": "Asset[0].warrantyExpiryDate",
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
            "jsonPath": "Asset[0].securityDepositRetained",
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
            "jsonPath": "Asset[0].securityDepositRealized",
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
            "jsonPath": "Asset[0].defectLiabilityPeriod[0].year",
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
            "jsonPath": "Asset[0].defectLiabilityPeriod[0].month",
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
            "jsonPath": "Asset[0].defectLiabilityPeriod[0].day",
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
            "jsonPath": "Asset[0].description",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].accumulatedDepreciationAccount",
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
            "jsonPath": "Asset[0].revaluationReserveAccount",
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
            "jsonPath": "Asset[0].depreciationExpenseAccount",
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
        "jsonPath":"Asset",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
		"objectName": "Asset",
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
			"resultPath": "Asset",
			// "rowClickUrlUpdate": "/update/asset/assetMovable/{id}",
			// "rowClickUrlView": "/view/asset/assetMovable/{id}"
			}
	},
	"asset.view": {
		"numCols": 12/2,
		"url": "/asset-services/assets/_search?ids={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "Asset",
		"groups": [
			{
				"label": "ac.create.Header.Details",
				"name": "createAsset",
				"fields": [
						{
							"name": "AssetIdNo",
							"jsonPath": "Asset[0].code",
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
  						"jsonPath": "Asset[0].oldCode",
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
              "jsonPath": "Asset[0].",
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
  						"jsonPath": "Asset[0].wipReferenceNo",
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
  						"jsonPath": "Asset[0].name",
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
  						"jsonPath": "Asset[0].",
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
  						"jsonPath": "Asset[0].department",
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
  						"jsonPath": "Asset[0].orderNumber",
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
              "jsonPath": "Asset[0].orderDate",
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
              "jsonPath": "Asset[0].acquisitionDate",
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
  						"jsonPath": "Asset[0].modeOfAcquisition",
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
  						"jsonPath": "Asset[0].",
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
  						"jsonPath": "Asset[0].landSurveyNo",
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
        "jsonPath":"Asset",
				"fields": [
          {
            "name": "Location",
            "jsonPath": "Asset[0].locationDetails",
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
            "jsonPath": "Asset[0].longitude",
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
            "jsonPath": "Asset[0].latitude",
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
            "jsonPath": "Asset[0].address",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].floors",
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
            "jsonPath": "Asset[0].plinthArea",
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
            "jsonPath": "Asset[0].cubicContents",
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
            "jsonPath": "Asset[0].usage",
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
            "jsonPath": "Asset[0].length",
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
            "jsonPath": "Asset[0].width",
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
            "jsonPath": "Asset[0].height",
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
            "jsonPath": "Asset[0].totalArea",
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
        "jsonPath":"Asset[0].",
				"fields": [
          {
            "name": "AnticipatedLifeOfAsset",
            "jsonPath": "Asset[0].anticipatedLife",
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
            "jsonPath": "Asset[0].originalValue",
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
            "jsonPath": "Asset[0].acquiredFrom",
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
            "jsonPath": "Asset[0].titleDocumentsAvalable",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].warrantyAvailable",
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
            "jsonPath": "Asset[0].warrantyExpiryDate",
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
            "jsonPath": "Asset[0].securityDepositRetained",
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
            "jsonPath": "Asset[0].securityDepositRealized",
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
            "jsonPath": "Asset[0].defectLiabilityPeriod[0].year",
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
            "jsonPath": "Asset[0].defectLiabilityPeriod[0].month",
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
            "jsonPath": "Asset[0].defectLiabilityPeriod[0].day",
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
            "jsonPath": "Asset[0].description",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].accumulatedDepreciationAccount",
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
            "jsonPath": "Asset[0].revaluationReserveAccount",
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
            "jsonPath": "Asset[0].depreciationExpenseAccount",
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
        "jsonPath":"Asset",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
		"objectName": "Asset",
		"groups": [
			{
				"label": "ac.update.Header.Details",
				"name": "createAsset",
				"fields": [
						{
							"name": "AssetIdNo",
							"jsonPath": "Asset[0].code",
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
  						"jsonPath": "Asset[0].oldCode",
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
              "jsonPath": "Asset[0].",
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
  						"jsonPath": "Asset[0].wipReferenceNo",
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
  						"jsonPath": "Asset[0].name",
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
  						"jsonPath": "Asset[0].",
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
  						"jsonPath": "Asset[0].department",
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
  						"jsonPath": "Asset[0].orderNumber",
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
              "jsonPath": "Asset[0].orderDate",
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
              "jsonPath": "Asset[0].acquisitionDate",
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
  						"jsonPath": "Asset[0].modeOfAcquisition",
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
  						"jsonPath": "Asset[0].",
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
  						"jsonPath": "Asset[0].landSurveyNo",
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
        "jsonPath":"Asset",
				"fields": [
          {
            "name": "Location",
            "jsonPath": "Asset[0].locationDetails",
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
            "jsonPath": "Asset[0].longitude",
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
            "jsonPath": "Asset[0].latitude",
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
            "jsonPath": "Asset[0].address",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].floors",
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
            "jsonPath": "Asset[0].plinthArea",
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
            "jsonPath": "Asset[0].cubicContents",
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
            "jsonPath": "Asset[0].usage",
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
            "jsonPath": "Asset[0].length",
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
            "jsonPath": "Asset[0].width",
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
            "jsonPath": "Asset[0].height",
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
            "jsonPath": "Asset[0].totalArea",
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
        "jsonPath":"Asset",
				"fields": [
          {
            "name": "AnticipatedLifeOfAsset",
            "jsonPath": "Asset[0].anticipatedLife",
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
            "jsonPath": "Asset[0].originalValue",
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
            "jsonPath": "Asset[0].acquiredFrom",
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
            "jsonPath": "Asset[0].titleDocumentsAvalable",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].warrantyAvailable",
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
            "jsonPath": "Asset[0].warrantyExpiryDate",
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
            "jsonPath": "Asset[0].securityDepositRetained",
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
            "jsonPath": "Asset[0].securityDepositRealized",
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
            "jsonPath": "Asset[0].defectLiabilityPeriod[0].year",
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
            "jsonPath": "Asset[0].defectLiabilityPeriod[0].month",
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
            "jsonPath": "Asset[0].defectLiabilityPeriod[0].day",
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
            "jsonPath": "Asset[0].description",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].accumulatedDepreciationAccount",
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
            "jsonPath": "Asset[0].revaluationReserveAccount",
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
            "jsonPath": "Asset[0].depreciationExpenseAccount",
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
        "jsonPath":"Asset",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
            "jsonPath": "Asset[0].",
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
