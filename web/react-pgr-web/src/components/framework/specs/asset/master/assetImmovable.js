var dat = {
	"asset.create": {
		"numCols": 12/3,
		"url": "asset-services-maha/assets/_create",
		"tenantIdRequired": true,
		"idJsonPath": "",
		"objectName": "Asset",
		"groups": [
			{
				"label": "ac.create.Header.Details",
				"name": "createAsset",
				"fields": [
						// {
						// 	"name": "AssetIdNo",
						// 	"jsonPath": "Asset[0].code",
						// 	"label": "ac.create.Asset.Id.No",
						// 	"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
						// 	"type": "text",
						// 	"isRequired": false,
						// 	"isDisabled": true,
						// 	"requiredErrMsg": "",
						// 	"patternErrMsg": ""
						// },
            {
  						"name": "OldReferenceNumber",
  						"jsonPath": "Asset.oldCode",
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
              "jsonPath": "Asset.dateOfCreation",
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
  						"jsonPath": "Asset.wipReferenceNo",
  						"label": "ac.create.Ref.WIP.Register",
  						"pattern": "",
  						"type": "text",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
  						"name": "AssetName",
  						"jsonPath": "Asset.name",
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
  						"jsonPath": "Asset.assetCategory.assetCategoryType",
  						"label": "Asset Category Type",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": "",
							"defaultValue": [	  {
	            "key": "IMMOVABLE",
	            "value": "IMMOVABLE"
	          }
					],
					// "depedants": [{
					// 	"jsonPath": "Asset.assetCategory",
					// 	"type": "dropDown",
					// 	"pattern": "/asset-services/assetCategories/_search?tenantId=default&assetCategoryType={Asset.assetCategory[0].assetCategoryType}|$..name|$..name"
					// }]
  					},

						{
  						"name": "AssetSubType",
  						"jsonPath": "Asset.assetCategory.id",
  						"label": "ac.create.Asset.SubCategory.Name",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": "",
							"defaultValue": [	{
									"key": "1",
									"value": "BUILDING"
								},  {
	            "key": "2",
	            "value": "SCHOOL"
	          },  {
	            "key": "3",
	            "value": "FIRE STATION"
	          }
					],
  					},

            {
  						"name": "Department",
  						"jsonPath": "Asset.departmentCode",
  						"label": "ac.create.Department",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": "",
							"defaultValue": [	{
									"key": "1",
									"value": "REVENUE"
								},  {
	            "key": "2",
	            "value": "ADMINISTARTION"
	          }
					]
  					},
            {
  						"name": "NoOfOrder",
  						"jsonPath": "Asset.orderNumber",
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
              "jsonPath": "Asset.orderDate",
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
              "jsonPath": "Asset.acquisitionDate",
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
  						"jsonPath": "Asset.modeOfAcquisition",
  						"label": "ac.create.Mode.of.Acquisition",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": "",
							"defaultValue": [  {
	            "key": "ACQUIRED",
	            "value": "ACQUIRED"
	          },  {
	            "key": "CONSTRUCTION",
	            "value": "CONSTRUCTION"
	          },  {
	            "key": "PURCHASE",
	            "value": "PURCHASE"
	          }
					]
  					},
            {
  						"name": "LandAssetID",
  						"jsonPath": "Asset.",
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
  						"jsonPath": "Asset.landSurveyNo",
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
          // {
          //   "name": "Location",
          //   "jsonPath": "Asset.locationDetails",
          //   "label": "ac.create.Location",
          //   "pattern": "",
          //   "type": "text",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "Longitude",
          //   "jsonPath": "Asset.longitude",
          //   "label": "ac.create.Longitude",
          //   "pattern": "",
          //   "type": "text",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": true,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "Latitude",
          //   "jsonPath": "Asset.latitude",
          //   "label": "ac.create.Latitude",
          //   "pattern": "",
          //   "type": "text",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": true,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          {
            "name": "Address",
            "jsonPath": "Asset.address",
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
            "jsonPath": "Asset.locationDetails.electionWard",
            "label": "ac.create.Election.Ward",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egov-location/boundarys/getByBoundaryType?tenantId=default&boundaryTypeId=10|$..id|$..name",
					//"url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "No of Floors",
            "jsonPath": "Asset.floors",
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
            "jsonPath": "Asset.plinthArea",
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
            "jsonPath": "Asset.cubicContents",
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
            "jsonPath": "Asset.usage",
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
            "jsonPath": "Asset.length",
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
            "jsonPath": "Asset.width",
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
            "jsonPath": "Asset.height",
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
            "jsonPath": "Asset.totalArea",
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
            "jsonPath": "Asset.anticipatedLife",
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
            "jsonPath": "Asset.originalValue",
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
            "jsonPath": "Asset.acquiredFrom",
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
            "jsonPath": "Asset.titleDocumentsAvalable",
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
            "jsonPath": "Asset.funds",
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
            "jsonPath": "Asset.warrantyAvailable",
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
            "jsonPath": "Asset.warrantyExpiryDate",
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
            "jsonPath": "Asset.securityDepositRetained",
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
            "jsonPath": "Asset.securityDepositRealized",
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
            "jsonPath": "Asset.defectLiabilityPeriod.year",
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
            "jsonPath": "Asset.defectLiabilityPeriod.month",
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
            "jsonPath": "Asset.defectLiabilityPeriod.day",
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
            "jsonPath": "Asset.description",
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
            "jsonPath": "Asset.",
            "label": "ac.create.Asset.account.code",
            "pattern": "",
            "type": "singleValueList",
						"url": "",
            //"url": "/egf-masters/accountcodepurposes/_search?tenantId=default&name=Fixed Assets|$..name|$..name",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "AccumulatedDepreciationAccount",
            "jsonPath": "Asset.accumulatedDepreciationAccount",
            "label": "ac.create.Accumulated.Depreciation.Account",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egf-masters/accountcodepurposes/_search?tenantId=default&name=Accumulated Depreciation|$..name|$..name",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "RevaluationReserveAccount",
            "jsonPath": "Asset.revaluationReserveAccount",
            "label": "ac.create.Revaluation.Reserve.Account",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egf-masters/accountcodepurposes/_search?tenantId=default&name=Revaluation Reserve Account|$..name|$..name",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "DepreciationExpensesAccount ",
            "jsonPath": "Asset.depreciationExpenseAccount",
            "label": "ac.create.Depreciation.Expenses.Account",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egf-masters/accountcodepurposes/_search?tenantId=default&name=Depreciation Expense Account|$..name|$..name",
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


          // {
          //   "name": "OpeningDate",
          //   "jsonPath": "Asset.",
          //   "label": "ac.create.Opening.date",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": true,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          {
            "name": "OpeningWrittenDownValue",
            "jsonPath": "Asset.grossValue",
            "label": "ac.create.Opening.Written.down.Value",
            "pattern": "^[1-9]\\d{0,3}(\\.\\d{0,3})*(,\\d+)?$",
            "type": "number",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          // {
          //   "name": "AdditionDate",
          //   "jsonPath": "Asset.",
          //   "label": "ac.create.Addition.Date",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "AdditionValue",
          //   "jsonPath": "Asset.",
          //   "label": "ac.create.Addition.Value",
          //   "pattern": "",
          //   "type": "text",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "RevaluationDate",
          //   "jsonPath": "Asset.",
          //   "label": "ac.create.Revaluation.date",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "RevaluationValue",
          //   "jsonPath": "Asset.",
          //   "label": "ac.create.Revaluation.Value",
          //   "pattern": "",
          //   "type": "text",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "DepreciationDate",
          //   "jsonPath": "Asset.",
          //   "label": "ac.create.Depreciation.Date",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "DepreciationValue",
          //   "jsonPath": "Asset.",
          //   "label": "ac.create.Depreciation.Value",
          //   "pattern": "",
          //   "type": "text",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "Closing date",
          //   "jsonPath": "Asset.",
          //   "label": "ac.create.Closing.Date",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "ClosingValue",
          //   "jsonPath": "Asset.",
          //   "label": "ac.create.Closing.written.Down.Value",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // }

				]
			}
		]
	},
	"asset.search": {
		"numCols": 12/3,
		"url": "asset-services-maha/assets/_search",
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
					},
					// {
					// 	"name": "AssetSearchAssetCategory",
					// 	"jsonPath": "assetCategory",
					// 	"label": "ac.search.category",
					// 	"pattern": "",
					// 	"type": "singleValueList",
					// 	"url": "",
					// 	"isRequired": false,
					// 	"isDisabled": false,
					// 	"requiredErrMsg": "",
					// 	"patternErrMsg": ""
					// },
					// {
					// 	"name": "AssetSearchDepartment",
					// 	"jsonPath": "department",
					// 	"label": "ac.search.department",
					// 	"pattern": "",
					// 	"type": "singleValueList",
					// 	"url": "",
					// 	"isRequired": false,
					// 	"isDisabled": false,
					// 	"requiredErrMsg": "",
					// 	"patternErrMsg": ""
					// },
					// {
					// 	"name": "AssetSearchStatus",
					// 	"jsonPath": "status",
					// 	"label": "ac.search.status",
					// 	"pattern": "",
					// 	"type": "singleValueList",
					// 	"url": "",
					// 	"isRequired": false,
					// 	"isDisabled": false,
					// 	"requiredErrMsg": "",
					// 	"patternErrMsg": ""
					// }
				]
			}
		],
		"result": {
			"header": [{label: "ac.create.Asset.account.code"},{label: "ac.create.Asset.Name"},  {label: "ac.create.Department"}, {label: "Date of Creation"}],
			"values": ["code","name", "departmentCode", "dateOfCreation"],
			"resultPath": "Assets",
			// "rowClickUrlUpdate": "/update/asset/assetMovable/{id}",
			 "rowClickUrlView": "/view/asset/assetImmovable/{id}"
			}
	},
	"asset.view": {
		"numCols": 12/2,
<<<<<<< f13a53aa7f500b1f27d7ade4a75d3b4e5ef57dc9
		"url": "asset-services-maha/assets/_search?ids={id}",
=======
		"url": "asset-services/assets/_search?id={id}",
>>>>>>> view screen updated for search
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "Assets",
		"groups": [
			{
				"label": "ac.create.Header.Details",
				"name": "createAsset",
				"fields": [
						// {
						// 	"name": "AssetIdNo",
						// 	"jsonPath": "Asset[0].code",
						// 	"label": "ac.create.Asset.Id.No",
						// 	"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
						// 	"type": "text",
						// 	"isRequired": false,
						// 	"isDisabled": true,
						// 	"requiredErrMsg": "",
						// 	"patternErrMsg": ""
						// },
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
              "jsonPath": "Assets[0].dateOfCreation",
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
            // {
  					// 	"name": "AssetCategoryType",
  					// 	"jsonPath": "Asset[0].",
  					// 	"label": "ac.create.Asset.SubCategory.Name",
  					// 	"pattern": "",
  					// 	"type": "singleValueList",
  					// 	"url": "",
  					// 	"isRequired": true,
  					// 	"isDisabled": false,
  					// 	"requiredErrMsg": "",
  					// 	"patternErrMsg": ""
  					// },
            {
  						"name": "Department",
  						"jsonPath": "Assets[0].departmentCode",
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
            // {
  					// 	"name": "LandAssetID",
  					// 	"jsonPath": "Asset[0].",
  					// 	"label": "ac.create.Land.Asset.ID",
  					// 	"pattern": "",
  					// 	"type": "autoCompelete",
  					// 	"url": "",
  					// 	"isRequired": false,
  					// 	"isDisabled": false,
  					// 	"requiredErrMsg": "",
  					// 	"patternErrMsg": "",
            //   "autoCompleteDependancy": {
						// 		"autoCompleteUrl": "/egf-masters/chartofaccounts/_search?id={value}",
						// 		"autoFillFields": {
						// 			"BusinessDetails[0].accountDetails[0].chartOfAccounts": "chartOfAccounts[0].glcode"
						// 		 }
						// 	 }
  					// },
            // {
  					// 	"name": "SurveyNoOfLandOnWhichStructureIsLocated ",
  					// 	"jsonPath": "Asset[0].landSurveyNo",
  					// 	"label": "ac.create.Survey.no.of.land",
  					// 	"pattern": "",
  					// 	"type": "number",
  					// 	"url": "",
  					// 	"isRequired": false,
  					// 	"isDisabled": true,
  					// 	"requiredErrMsg": "",
  					// 	"patternErrMsg": ""
  					// },
				]
			},
      {
				"label": "ac.create.Location.Details",
				"name": "LocationField",
        "multiple":false,
        "jsonPath":"Asset",
				"fields": [
          // {
          //   "name": "Location",
          //   "jsonPath": "Asset[0].locationDetails",
          //   "label": "ac.create.Location",
          //   "pattern": "",
          //   "type": "text",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "Longitude",
          //   "jsonPath": "Asset[0].longitude",
          //   "label": "ac.create.Longitude",
          //   "pattern": "",
          //   "type": "text",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": true,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "Latitude",
          //   "jsonPath": "Asset[0].latitude",
          //   "label": "ac.create.Latitude",
          //   "pattern": "",
          //   "type": "text",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": true,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
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
            "jsonPath": "Assets[0].locationDetails.electionWard",
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
        "jsonPath":"Asset[0].",
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
            "jsonPath": "Assets[0].fund",
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
            "jsonPath": "Assets[0].assetAccount",
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
          // {
          //   "name": "OpeningDate",
          //   "jsonPath": "Assets[0].",
          //   "label": "ac.create.Opening.date",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": true,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          {
            "name": "OpeningWrittenDownValue",
            "jsonPath": "Assets[0].grossValue",
            "label": "ac.create.Opening.Written.down.Value",
            "pattern": "^[1-9]\\d{0,3}(\\.\\d{0,3})*(,\\d+)?$",
            "type": "number",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          // {
          //   "name": "AdditionDate",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Addition.Date",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "AdditionValue",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Addition.Value",
          //   "pattern": "",
          //   "type": "text",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "RevaluationDate",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Revaluation.date",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "RevaluationValue",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Revaluation.Value",
          //   "pattern": "",
          //   "type": "text",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "DepreciationDate",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Depreciation.Date",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "DepreciationValue",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Depreciation.Value",
          //   "pattern": "",
          //   "type": "text",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "Closing date",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Closing.Date",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "ClosingValue",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Closing.written.Down.Value",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // }

				]
			}
		]
	},
	"asset.update": {
		"numCols": 12/2,
		"searchUrl": "asset-services/assets/_search?ids={id}",
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
  						"jsonPath": "Asset[0].assetCategory.name",
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
            "jsonPath": "Asset[0].assetAccount",
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


          // {
          //   "name": "OpeningDate",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Opening.date",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": true,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          {
            "name": "OpeningWrittenDownValue",
            "jsonPath": "Asset[0].grossValue",
            "label": "ac.create.Opening.Written.down.Value",
            "pattern": "^[1-9]\\d{0,3}(\\.\\d{0,3})*(,\\d+)?$",
            "type": "number",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          // {
          //   "name": "AdditionDate",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Addition.Date",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "AdditionValue",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Addition.Value",
          //   "pattern": "",
          //   "type": "text",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "RevaluationDate",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Revaluation.date",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "RevaluationValue",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Revaluation.Value",
          //   "pattern": "",
          //   "type": "text",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "DepreciationDate",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Depreciation.Date",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "DepreciationValue",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Depreciation.Value",
          //   "pattern": "",
          //   "type": "text",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "Closing date",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Closing.Date",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "ClosingValue",
          //   "jsonPath": "Asset[0].",
          //   "label": "ac.create.Closing.written.Down.Value",
          //   "pattern": "",
          //   "type": "date",
          //   "url": "",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // }

				]
			}
		]
	}
}

export default dat;
