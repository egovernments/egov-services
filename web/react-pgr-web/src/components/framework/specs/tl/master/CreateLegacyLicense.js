var dat = {
	"tl.create": {
		"numCols": 12/2,
		"url": "/tl-masters/v1/category/_create",
    "useTimestamp": true,
		"tenantIdRequired": true,
		"objectName": "licenses",
		"groups": [
			{
				"label": "tl.create.licenses.groups.TradeDetails",
				"name": "TradeDetails",
				"fields": [
						{
							"name": "OldLicenseNumber",
							"jsonPath": "licenses.oldLicenseNumber",
							"label": "tl.create.licenses.groups.TradeDetails.OldLicenseNumber",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			},

      {
				"label": "tl.create.licenses.groups.TradeOwnerDetails",
				"name": "TradeOwnerDetails",
				"fields": [
						{
							"name": "AadharNumber",
							"jsonPath": "licenses.adhaarNumber",
							"label": "tl.create.licenses.groups.TradeOwnerDetails.AadharNumber",
							"pattern": "^.{12,12}$",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Aadhar Number must be of 12 digits"
						},
            {
							"name": "MobileNumber",
							"jsonPath": "licenses.mobileNumber",
							"label": "tl.create.licenses.groups.TradeOwnerDetails.Mobile Number",
							"pattern": "^.{10,10}$",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Mobile Number must be of 10 digits"
						},
            {
							"name": "TradeOwnerName",
							"jsonPath": "licenses.ownerName",
							"label": "tl.create.licenses.groups.TradeOwnerDetails.TradeOwnerName",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "FatherSpouseName",
							"jsonPath": "licenses.fatherSpouseName",
							"label": "tl.create.licenses.groups.TradeOwnerDetails.FatherSpouseName",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "EmailID",
							"jsonPath": "licenses.emailId",
							"label": "tl.create.licenses.TradeOwnerDetails.groups.EmailID",
							"pattern": "",
							"type": "email",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "TradeOwnerAddress",
							"jsonPath": "licenses.ownerAddress",
							"label": "tl.create.licenses.groups.TradeOwnerDetails.TradeOwnerAddress",
							"pattern": "",
							"type": "textarea",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			},

      {
				"label": "tl.create.licenses.groups.TradeLocationDetails",
				"name": "TradeLocationDetails",
				"fields": [
						{
							"name": "PropertyAssessmentNo",
							"jsonPath": "licenses.propertyAssesmentNo",
							"label": "tl.create.licenses.groups.TradeLocationDetails.PropertyAssessmentNo",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "Locality",
							"jsonPath": "licenses.localityId",
							"label": "tl.create.licenses.groups.TradeLocationDetails.Locality",
							"pattern": "",
							"type": "singleValueList",
              "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=LOCALITY&hierarchyTypeName=LOCATION|$..id|$..name",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "Ward",
							"jsonPath": "licenses.wardId",
							"label": "tl.create.licenses.groups.TradeLocationDetails.Ward",
							"pattern": "",
							"type": "singleValueList",
              "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=WARD&hierarchyTypeName=REVENUE|$..boundaryNum|$..name",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "OwnershipType",
							"jsonPath": "licenses.ownerShipType",
							"label": "tl.create.licenses.groups.TradeLocationDetails.OwnershipType",
							"pattern": "",
							"type": "singleValueList",
              "url": "",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
              "defaultValue": [{
            "key": "",
            "value": null
          },
          {
            "key": "STATE_GOVERNMENT_OWNED",
            "value": "STATE_GOVERNMENT_OWNED"
          },
          {
            "key": "RENTED",
            "value": "RENTED"
          },
          {
            "key": "CENTRAL_GOVERNMENT_OWNED",
            "value": "CENTRAL_GOVERNMENT_OWNED"
          },
          {
            "key": "ULB",
            "value": "ULB"
          }
          ]
						},
            {
              "name": "TradeAddress",
              "jsonPath": "licenses.tradeAddress",
              "label": "tl.create.licenses.groups.TradeLocationDetails.TradeAddress",
              "pattern": "",
              "type": "textarea",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            }
				]
			},
      {
        "label": "tl.create.licenses.groups.TradeDetails",
        "name": "TradeDetails",
        "fields": [
            {
              "name": "TradeTitle",
              "jsonPath": "licenses.tradeTitle",
              "label": "tl.create.licenses.groups.TradeDetails.TradeTitle",
              "pattern": "",
              "type": "text",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "TradeType",
              "jsonPath": "licenses.tradeType",
              "label": "tl.create.licenses.groups.TradeDetails.TradeType",
              "pattern": "",
              "type": "singleValueList",
              "url": "",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
              "defaultValue": [{
            "key": "",
            "value": null
          },
          {
            "key": "PERMANENT",
            "value": "PERMANENT"
          },
          {
            "key": "TEMPORARY",
            "value": "TEMPORARY"
          }
          ]

            },
            {
							"name": "TradeCategory",
							"jsonPath": "licenses.categoryId",
							"label": "tl.create.licenses.groups.TradeDetails.TradeCategory",
							"pattern": "",
							"type": "singleValueList",
              "url": "/tl-masters/v1/category/_search?|$..name|$..name",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "TradeSubCategory",
							"jsonPath": "licenses.subCategoryId",
							"label": "tl.create.licenses.groups.TradeDetails.TradeSubCategory",
							"pattern": "",
							"type": "singleValueList",
              "url": "/tl-masters/v1/category/_search?|$..name|$..name",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
              "name": "UOM",
              "jsonPath": "licenses.uomId",
              "label": "tl.create.licenses.groups.TradeDetails.UOM",
              "pattern": "",
              "type": "text",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "TradeAreaWeight",
              "jsonPath": "licenses.uomValue",
              "label": "tl.create.licenses.groups.TradeDetails.TradeAreaPremises",
              "pattern": "",
              "type": "text",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "Remarks",
              "jsonPath": "licenses.remarks",
              "label": "tl.create.licenses.groups.TradeDetails.Remarks",
              "pattern": "",
              "type": "text",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "TradeCommencementDate",
              "jsonPath": "licenses.tradeCommencementDate",
              "label": "tl.create.licenses.groups.TradeDetails.TradeCommencementDate",
              "pattern": "",
              "type": "datePicker",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
							"name": "TradeOwner",
							"jsonPath": "licenses.active",
							"label": "tl.create.licenses.groups.TradeDetails.TraderOwnerProperty",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"defaultValue":true
						}
        ]
      },

      {
				"label": "tl.create.licenses.groups.FeeDetails",
				"name": "FeeDetails",
				"fields": [
						{
							"name": "FinancialYear",
							"jsonPath": "licenses.FinancialYear",
							"label": "tl.create.licenses.groups.FeeDetails.FinancialYear",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "Amount",
							"jsonPath": "licenses.Amount",
							"label": "tl.create.licenses.groups.FeeDetails.Amount",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "IsPaid",
							"jsonPath": "licenses.IsPaid",
							"label": "tl.create.licenses.groups.FeeDetails.IsPaid",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}

		]
	},



	"tl.search": {
		"numCols": 12/1,
		"url": "/tl-masters/v1/category/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "categories",
		"groups": [
			{
				"label": "tl.search.groups.categorytype.title",
				"name": "createCategoryType",
				"fields": [
						{
							"name": "category",
							"jsonPath": "licenses.name",
							"label": "tl.search.groups.categorytype.category",
							"pattern": "",
							"type": "singleValueList",
              "url": "/tl-masters/v1/category/_search?|$..name|$..name",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}
		],
		"result": {
			"header": [{label: "tl.create.groups.categorytype.code"},{label: "tl.create.groups.categorytype.name"}, {label: "tl.create.groups.categorytype.active"}],
			"values": ["code","name", "active"],
			"resultPath": "categories",
			"rowClickUrlUpdate": "/update/tl/CreateLicenseCategory/{id}",
			"rowClickUrlView": "/view/tl/CreateLicenseCategory/{id}"
			}
	},
	"tl.view": {
		"numCols": 12/2,
		"url": "/tl-masters/v1/category/_search?id={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "categories",
		"groups": [
			{
				"label": "tl.view.groups.categorytype.title",
				"name": "viewCategoryType",
				"fields": [
					{
						"name": "name",
						"jsonPath": "categories[0].name",
						"label": "tl.view.groups.categorytype.name",
						"pattern": "",
						"type": "text",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "code",
						"jsonPath": "categories[0].code",
						"label": "tl.view.groups.categorytype.code",
						"pattern": "",
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
	"tl.update": {
		"numCols": 12/2,
		"searchUrl": "/tl-masters/v1/category/_search?id={id}",
		"url": "/tl-masters/v1/tl-tradelicense/category/Flammables/{CategoryType.code}/_update",
		"isResponseArray":true,
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "categories",
		"groups": [
			{
				"label": "tl.update.groups.categorytype.title",
				"name": "createCategoryType",
				"fields": [
					{
						"name": "name",
						"jsonPath": "categories.name",
						"label": "tl.update.groups.categorytype.name",
						"pattern": "",
						"type": "text",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "code",
						"jsonPath": "categories.code",
						"label": "tl.update.groups.categorytype.code",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "active",
						"jsonPath": "categories.active",
						"label": "tl.update.groups.categorytype.active",
						"pattern": "",
						"type": "checkbox",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"defaultValue":true
					}
				]
			}
		]
	}
}

export default dat;
