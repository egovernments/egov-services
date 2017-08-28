var dat = {
  "tl.create": {
    "numCols": 12 / 3,
    "url": "/tl-services/license/v1/_create",
    "useTimestamp": true,
    "tenantIdRequired": false,
    "objectName": "licenses",
    "idJsonPath": "licenses[0].licenseNumber",
    "groups": [{
        "label": "tl.create.licenses.groups.TradeDetailsTab",
        "name": "TradeDetailsTab",
        "fields": [{
            "name": "OldLicenseNumber",
            "jsonPath": "licenses[0].oldLicenseNumber",
            "label": "tl.create.licenses.groups.TradeDetails.OldLicenseNumber",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "applicationType",
            "jsonPath": "licenses[0].applicationType",
            "label": "tl.create.licenses.groups.TradeDetails.applicationType",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "hide": true,
            "defaultValue": "NEW",
            "patternErrMsg": ""
          },
          {
            "name": "isLegacy",
            "jsonPath": "licenses[0].isLegacy",
            "label": "tl.create.licenses.groups.TradeDetails.isLegacy",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "hide": true,
            "defaultValue": "true",
            "patternErrMsg": ""
          },
          {
            "name": "active",
            "jsonPath": "licenses[0].active",
            "label": "tl.create.licenses.groups.TradeDetails.isLegacy",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "hide": true,
            "defaultValue": "true",
            "patternErrMsg": ""
          },
          {
            "name": "applicationDate",
            "jsonPath": "licenses[0].applicationDate",
            "label": "tl.create.licenses.groups.TradeDetails.isLegacy",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "hide": true,
            "defaultValue": "1503144186841",
            "patternErrMsg": ""
          }
        ]
      },

      {
        "label": "tl.create.licenses.groups.TradeOwnerDetails",
        "name": "TradeOwnerDetails",
        "fields": [{
            "name": "AadharNumber",
            "jsonPath": "licenses[0].adhaarNumber",
            "label": "tl.create.licenses.groups.TradeOwnerDetails.AadharNumber",
            "pattern": "aadhar",
            "type": "aadhar",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "Enter Valid Aadhar Number (12 Digit Number)"
          },
          {
            "name": "MobileNumber",
            "jsonPath": "licenses[0].mobileNumber",
            "label": "tl.create.licenses.groups.TradeOwnerDetails.Mobile Number",
            "pattern": "",
            "type": "mobileNumber",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "Enter Valid Mobile Number (10 Digit Number)"
          },
          {
            "name": "TradeOwnerName",
            "jsonPath": "licenses[0].ownerName",
            "label": "tl.create.licenses.groups.TradeOwnerDetails.TradeOwnerName",
            "pattern": "^.[a-zA-Z. ]{3,99}$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "Enter Valid Trade Owner Name (Min:4, Max:100)",
            "maxLength": "100"
          },
          {
            "name": "FatherSpouseName",
            "jsonPath": "licenses[0].fatherSpouseName",
            "label": "tl.create.licenses.groups.TradeOwnerDetails.FatherSpouseName",
            "pattern": "^.[a-zA-Z. ]{3,99}$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "Enter Valid Father/Spouse Name (Min:4, Max:100)",
            "maxLength": "100"
          },
          {
            "name": "EmailID",
            "jsonPath": "licenses[0].emailId",
            "label": "tl.create.licenses.TradeOwnerDetails.groups.EmailID",
            "pattern": "",
            "type": "email",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "Enter Valid Email ID (ex: abc@xyz.com)"
          },
          {
            "name": "TradeOwnerAddress",
            "jsonPath": "licenses[0].ownerAddress",
            "label": "tl.create.licenses.groups.TradeOwnerDetails.TradeOwnerAddress",
            "pattern": "^.{4,250}$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "Enter Valid Trade Owner Address (Min:4, Max:250)",
            "maxLength": "250"
          }
        ]
      },

      {
        "label": "tl.create.licenses.groups.TradeLocationDetails",
        "name": "TradeLocationDetails",
        "fields": [{
            "name": "PropertyAssessmentNo",
            "jsonPath": "licenses[0].propertyAssesmentNo",
            "label": "tl.create.licenses.groups.TradeLocationDetails.PropertyAssessmentNo",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "Enter Valid Property Assessment Number"
          },
          {
            "name": "Locality",
            "jsonPath": "licenses[0].localityId",
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
            "name": "adminWardId",
            "jsonPath": "licenses[0].adminWardId",
            "label": "tl.create.licenses.groups.TradeLocationDetails.adminWardId",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?tenantId=default&boundaryTypeName=Ward&hierarchyTypeName=ADMINISTRATION|$..boundaryNum|$..name",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "revenueWardId",
            "jsonPath": "licenses[0].revenueWardId",
            "label": "tl.create.licenses.groups.TradeLocationDetails.revenueWardId",
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
            "jsonPath": "licenses[0].ownerShipType",
            "label": "tl.create.licenses.groups.TradeLocationDetails.OwnershipType",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "defaultValue": [{
                "key": "STATE_GOVERNMENT_OWNED",
                "value": "STATE GOVERNMENT OWNED"
              },
              {
                "key": "RENTED",
                "value": "RENTED"
              },
              {
                "key": "CENTRAL_GOVERNMENT_OWNED",
                "value": "CENTRAL GOVERNMENT OWNED"
              },
              {
                "key": "ULB",
                "value": "ULB"
              }
            ]
          },
          {
            "name": "TradeAddress",
            "jsonPath": "licenses[0].tradeAddress",
            "label": "tl.create.licenses.groups.TradeLocationDetails.TradeAddress",
            "pattern": "^.{4,250}$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "Enter Valid Trade Address (Min:4, Max:100)",
            "maxLength": "100"
          }
        ]
      },
      {
        "label": "tl.create.licenses.groups.TradeDetails",
        "name": "TradeDetails",
        "fields": [{
            "name": "TradeTitle",
            "jsonPath": "licenses[0].tradeTitle",
            "label": "tl.create.licenses.groups.TradeDetails.TradeTitle",
            "pattern": "^.[a-zA-Z./ -]{3,100}$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "Enter Valid Trade Title (Min:4, Max:100)",
            "maxLength": "100"
          },
          {
            "name": "TradeType",
            "jsonPath": "licenses[0].tradeType",
            "label": "tl.create.licenses.groups.TradeDetails.TradeType",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "defaultValue": [{
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
            "jsonPath": "licenses[0].categoryId",
            "label": "tl.create.licenses.groups.TradeDetails.TradeCategory",
            "pattern": "",
            "type": "singleValueList",
            "url": "/tl-masters/category/v1/_search?tenantId=default&type=category|$..id|$..name",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "depedants": [{
              "jsonPath": "licenses[0].subCategoryId",
              "type": "dropDown",
              "pattern": "/tl-masters/category/v1/_search?tenantId=default&type=subcategory&categoryId={licenses[0].categoryId}|$.categories.*.id|$.categories.*.name"
            }]
          },
          {
            "name": "TradeSubCategory",
            "jsonPath": "licenses[0].subCategoryId",
            "label": "tl.create.licenses.groups.TradeDetails.TradeSubCategory",
            "pattern": "",
            "type": "singleValueList",
            "url": "",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "depedants": [
              {
              "jsonPath": "licenses[0].uomName",
              "type": "text",
              "isRequired": true,
              "isDisabled": true,
              "pattern": ""
            },
          {
          "jsonPath": "licenses[0].validityYears",
          "type": "text",
          "isRequired": true,
          "isDisabled": true,
          "pattern": ""
        }
          ]
          },
          {
            "name": "UOM",
            "jsonPath": "licenses[0].uomName",
            "label": "tl.create.licenses.groups.TradeDetails.UOM",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "tradeValueForUOM",
            "jsonPath": "licenses[0].quantity",
            "label": "tl.create.licenses.groups.TradeDetails.tradeValueForUOM",
            "pattern": "^[0-9]+(\.[0-9]{1,2})?$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "Enter Valid Trade Value for the UOM (Upto two decimal points)"
          },
          {
            "name": "validity",
            "jsonPath": "licenses[0].validityYears",
            "label": "tl.create.licenses.groups.validity",
            "pattern": "",
            "url": "/tl-masters/category/v1/_search?tenantId=default&ids=1",
            "type": "text",
            "isRequired": true,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Remarks",
            "jsonPath": "licenses[0].remarks",
            "label": "tl.create.licenses.groups.TradeDetails.Remarks",
            "pattern": "^.{0,1000}$",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "maxLength": "1000"
          },
          {
            "name": "TradeCommencementDate",
            "jsonPath": "licenses[0].tradeCommencementDate",
            "label": "tl.create.licenses.groups.TradeDetails.TradeCommencementDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "Enter in dd/mm/yyyy Format",
            "maxLength": "10"
          },
          {
            "name": "licenseValidFromDate",
            "jsonPath": "licenses[0].licenseValidFromDate",
            "label": "tl.create.licenses.groups.TradeDetails.licenseValidFromDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "Enter in dd/mm/yyyy Format",
            "maxLength": "10"
          },
          {
            "name": "TradeOwner",
            "jsonPath": "licenses[0].isPropertyOwner",
            "label": "tl.create.licenses.groups.TradeDetails.TraderOwnerProperty",
            "pattern": "",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "defaultValue": false,
            "showHideFields": [{
              "ifValue": true,
              "hide": [],
              "show": [{
                "name": "createLicenseCategoryType",
                "isGroup": true,
                "isField": false
              }]
            }]
          },
          {
            "name": "UOM ID",
            "jsonPath": "licenses[0].uomId",
            "label": "tl.create.licenses.groups.TradeDetails.UOM",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "hide": "true"
          }
        ]
      },

      {
        "label": "tl.create.licenses.groups.agreementDetails",
        "name": "createLicenseCategoryType",
        "hide": true,
        "fields": [{
            "name": "dateOfExecution",
            "jsonPath": "licenses[0].agreementDate",
            "label": "tl.create.licenses.groups.agreementDetails.agreementDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "Enter in dd/mm/yyyy Format",
            "maxLength": "10"
          },
          {
            "name": "agreementNo",
            "jsonPath": "licenses[0].agreementNo",
            "label": "tl.create.licenses.groups.agreementDetails.agreementNo",
            "pattern": "^[a-zA-Z0-9]{4,128}$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "Enter Valid Agreement No (Min:4, Max:128, Alpha/Numeric)",
            "maxLength": "128"
          }
        ]
      },
      {
          "label": "wc.create.groups.fileDetails.title",
          "name": "Documents",
          "fields": [
            {
              "name": "File",
              "jsonPath": "licenses[0].supportDocuments",
              "type": "documentList",
              "pathToArray": "documentTypes",
              "displayNameJsonPath": "name",
              "url": "/tl-masters/documenttype/v2/_search?applicationType=NEW",
              "autoFillFields": [
                {
                  "name": "documentTypeId",
                  "jsonPath": "id"
                }
              ]
            }
          ]
        },
      // {
      // 	"label": "tl.create.licenses.groups.FeeDetails",
      // 	"name": "FeeDetails",
      // 	"fields": [
      // 			{
      // 				"name": "FinancialYear",
      // 				"jsonPath": "licenses[0].FinancialYear",
      // 				"label": "tl.create.licenses.groups.FeeDetails.FinancialYear",
      // 				"pattern": "",
      // 				"type": "number",
      // 				"isRequired": true,
      // 				"isDisabled": false,
      // 				"requiredErrMsg": "",
      // 				"patternErrMsg": ""
      // 			},
      //       {
      // 				"name": "Amount",
      // 				"jsonPath": "licenses[0].Amount",
      // 				"label": "tl.create.licenses.groups.FeeDetails.Amount",
      // 				"pattern": "",
      // 				"type": "number",
      // 				"isRequired": true,
      // 				"isDisabled": false,
      // 				"requiredErrMsg": "",
      // 				"patternErrMsg": ""
      // 			},
      //       {
      // 				"name": "IsPaid",
      // 				"jsonPath": "licenses[0].IsPaid",
      // 				"label": "tl.create.licenses.groups.FeeDetails.IsPaid",
      // 				"pattern": "",
      // 				"type": "checkbox",
      // 				"isRequired": true,
      // 				"isDisabled": false,
      // 				"requiredErrMsg": "",
      // 				"patternErrMsg": ""
      // 			}
      // 	]
      // }

    ]
  },



  "tl.search": {
       "numCols": 3,
       "url": "/tl-services/license/v1/_search",
       "tenantIdRequired": true,
       "useTimestamp": true,
       "objectName": "licenses",
       "groups": [{
         "label": "tl.search.groups.license.searchTradeLicense",
         "name": "searchTradeLicense",
         "fields": [{
             "name": "applicationNumber",
             "jsonPath": "applicationNumber",
             "label": "tl.search.groups.applicationNumber",
             "pattern": "^[a-zA-Z0-9]*$",
             "type": "autoCompelete",
             "isRequired": false,
             "isDisabled": false,
             "allowWrite":true,
             "requiredErrMsg": "",
             "patternErrMsg": "",
             "url":"/tl-services/license/v1/_search?|$..applicationNumber|$..applicationNumber",
             "autoCompleteDependancy": {
               "autoCompleteUrl": "/tl-services/license/v1/_search?id={value}"
              }
           },
           {
             "name": "showActiveLicense",
             "jsonPath": "active",
             "label": "tl.search.groups.showActiveLicense",
             "pattern": "",
             "type": "checkbox",
             "isRequired": false,
             "isDisabled": false,
             "requiredErrMsg": "",
             "patternErrMsg": "",
             "defaultValue":true
           },
           {
             "name": "status",
             "jsonPath": "status",
             "label": "tl.search.groups.status",
             "pattern": "",
             "type": "singleValueList",
             "url": "/tl-masters/status/v1/_search?tenantId=default|$..id|$..name",
             "isRequired": false,
             "isDisabled": false,
             "requiredErrMsg": "",
             "patternErrMsg": "",

           },
           {
             "name": "licenseNumber",
             "jsonPath": "licenseNumber",
             "label": "tl.search.groups.licenseNumber",
             "pattern": "",
             "type": "autoCompelete",
             "isRequired": false,
             "isDisabled": false,
             "requiredErrMsg": "",
              "allowWrite":true,
             "patternErrMsg": "",
             "url":"/tl-services/license/v1/_search?|$..licenseNumber|$..licenseNumber",
             "autoCompleteDependancy": {
               "autoCompleteUrl": "/tl-services/license/v1/_search?id={value}"
              }
           },
           {
             "name": "oldLicenseNumber",
             "jsonPath": "oldLicenseNumber",
             "label": "tl.search.groups.oldLicenseNumber",
             "pattern": "",
             "type": "autoCompelete",
             "isRequired": false,
             "isDisabled": false,
             "requiredErrMsg": "",
              "allowWrite":true,
             "patternErrMsg": "",
             "url":"/tl-services/license/v1/_search?|$..oldLicenseNumber|$..oldLicenseNumber",
             "autoCompleteDependancy": {
               "autoCompleteUrl": "/tl-services/license/v1/_search?id={value}"
              }
           },
           {
             "name": "tradeCategory",
             "jsonPath": "tradeCategory",
             "label": "tl.search.groups.tradeCategory",
             "pattern": "",
             "type": "singleValueList",
             "url": "/tl-masters/category/v1/_search?tenantId=default&type=category|$..id|$..name",
             "isRequired": false,
             "isDisabled": false,
             "requiredErrMsg": "",
             "patternErrMsg": "",
             "depedants": [{
               "jsonPath": "tradeSubCategory",
               "type": "dropDown",
               "pattern": "/tl-masters/category/v1/_search?tenantId=default&type=subcategory&categoryId={tradeCategory}|$.categories.*.id|$.categories.*.name"
             }]
           },
           {
             "name": "tradeSubCategory",
             "jsonPath": "tradeSubCategory",
             "label": "tl.create.licenses.groups.TradeDetails.TradeSubCategory",
             "pattern": "",
             "type": "singleValueList",
             "url": "",
             "isRequired": false,
             "isDisabled": false,
             "requiredErrMsg": "",
             "patternErrMsg": ""
           },
           {
             "name": "tradeTitle",
             "jsonPath": "tradeTitle",
             "label": "tl.search.groups.tradeTitle",
             "pattern": "",
             "type": "text",
             "isRequired": false,
             "isDisabled": false,
             "requiredErrMsg": "",
             "patternErrMsg": ""
           },
           {
             "name": "ownerName",
             "jsonPath": "ownerName",
             "label": "tl.search.groups.tradeOwnerName",
             "pattern": "^.[a-zA-Z. ]{3,99}$",
             "type": "text",
             "isRequired": false,
             "isDisabled": false,
             "requiredErrMsg": "",
             "patternErrMsg": "Enter Valid Father/Spouse Name (Min:4, Max:100)"
           },
           {
             "name": "propertyAssesmentNo",
             "jsonPath": "propertyAssesmentNo",
             "label": "tl.search.groups.propertyAssesmentNo",
             "pattern": "",
             "type": "text",
             "isRequired": false,
             "isDisabled": false,
             "requiredErrMsg": "",
             "patternErrMsg": ""
           },
           {
             "name": "adminWardName",
             "jsonPath": "adminWard",
             "label": "tl.search.groups.adminWardName",
             "pattern": "",
             "type": "singleValueList",
             "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?tenantId=default&boundaryTypeName=Ward&hierarchyTypeName=ADMINISTRATION|$..boundaryNum|$..name",
             "isRequired": false,
             "isDisabled": false,
             "requiredErrMsg": "",
             "patternErrMsg": ""
           },
           {
             "name": "mobileNumber",
             "jsonPath": "mobileNumber",
             "label": "tl.search.groups.mobileNumber",
             "pattern": "^.{10,10}$",
             "type": "text",
             "isRequired": false,
             "isDisabled": false,
             "requiredErrMsg": "",
             "patternErrMsg": "Mobile Number must be of 10 digits"
           }


         ]
       }],
       "result": {
         "header": [{
           label: "tl.search.result.groups.applicationNumber"
         }, {
           label: "tl.search.result.groups.tlNumber"
         }, {
           label: "tl.search.result.groups.oldTLNumber"
         }, {
           label: "tl.search.result.groups.category"
         }, {
           label: "tl.search.result.groups.subCategory"
         }, {
           label: "tl.search.result.groups.titleOfTrade"
         }, {
           label: "tl.search.result.groups.tradeOwner"
         }, {
           label: "tl.search.result.groups.mobileNumber"
         }, {
           label: "tl.search.result.groups.propertyAssessmentNumber"
         }, {
           label: "tl.search.groups.adminWardName"
         }, {
           label: "tl.search.result.groups.validityYears"
         }, {
           label: "tl.search.result.groups.licenseStatus"
         }, {
           label: "tl.search.result.groups.applicationStatus"
         }, {
           label: "tl.search.result.groups.workflowOwnerName"
         }],
         "values": ["applicationNumber","licenseNumber", "oldLicenseNumber", "category", "subCategory", "tradeTitle", "ownerName", "mobileNumber", "propertyAssesmentNo", "adminWardName", "validityYears","active","statusName",""],
         "resultPath": "licenses",
         "rowClickUrlUpdate": "/update/tl/CreateLegacyLicense/{licenseNumber}",
         "rowClickUrlView": "/non-framework/tl/transaction/viewLegacyLicense/{licenseNumber}"
       }
     },


     "tl.view": {
        "numCols": 12 / 3,
        "url": "/tl-services/license/v1/_search?licenseNumber={licenseNumber}",
        "tenantIdRequired": true,
        "useTimestamp": true,
        "objectName": "licenses",
        "label": "tl.view.groups.title",
        "groups": [  {
            "label": "tl.licenses.view.groups.tradeDetails",
            "name": "tradeDetails",
            "fields": [{
                "name": "licenseNumber",
                "jsonPath": "licenses[0].licenseNumber",
                "label": "tl.search.groups.licenseNumber",
                "pattern": "",
                "type": "text",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              },
              {
                "name": "OldLicenseNumber",
                "jsonPath": "licenses[0].oldLicenseNumber",
                "label": "tl.create.licenses.groups.TradeDetails.OldLicenseNumber",
                "pattern": "",
                "type": "text",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              }

            ]
          },
          {
            "label": "tl.view.groups.tradeOwnerDetails",
            "name": "viewCategoryType",
            "fields": [{
                "name": "aadharNumber",
                "jsonPath": "licenses[0].adhaarNumber",
                "label": "tl.view.groups.aadharNumber",
                "pattern": "^.{12,12}$",
                "type": "text",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": "Aadhar Number must be of 12 digits"
              },
              {
                "name": "mobileNumber",
                "jsonPath": "licenses[0].mobileNumber",
                "label": "tl.view.groups.mobileNumber",
                "pattern": "^.{10,10}$",
                "type": "text",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": "Mobile Number must be of 10 digits"
              },
              {
                "name": "tradeOwnerName",
                "jsonPath": "licenses[0].ownerName",
                "label": "tl.view.groups.tradeOwnerName",
                "pattern": "^.[a-zA-Z. ]{2,49}$",
                "type": "text",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": "Enter Valid Name"
              },
              {
                "name": "fatherSpouseName",
                "jsonPath": "licenses[0].fatherSpouseName",
                "label": "tl.view.groups.fatherSpouseName",
                "pattern": "",
                "type": "text",
                "isRequired": true,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              },
              {
                "name": "emailId",
                "jsonPath": "licenses[0].emailId",
                "label": "tl.view.groups.emailId",
                "pattern": "^[a-zA-Z0-9_.]+@[a-zA-Z0-9]+.[a-zA-Z0-9]+$",
                "type": "text",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": "Enter Valid EmailID"
              },
              {
                "name": "tradeOwnerAddress",
                "jsonPath": "licenses[0].ownerAddress",
                "label": "tl.view.groups.tradeOwnerAddress",
                "pattern": "",
                "type": "textarea",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              }
            ]
          },
          {
            "label": "tl.licenses.view.groups.TradeLocationDetails",
            "name": "TradeLocationDetails",
            "fields": [{
                "name": "PropertyAssessmentNo",
                "jsonPath": "licenses[0].propertyAssesmentNo",
                "label": "tl.licenses.view.groups.PropertyAssessmentNo",
                "pattern": "",
                "type": "text",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              },
              {
                "name": "Locality",
                "jsonPath": "licenses[0].localityName",
                "label": "tl.licenses.view.groups.Locality",
                "pattern": "",
                "type": "singleValueList",
                "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=LOCALITY&hierarchyTypeName=LOCATION|$..id|$..name",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              },
              {
                "name": "adminWardId",
                "jsonPath": "licenses[0].adminWardName",
                "label": "tl.licenses.view.groups.adminWardId",
                "pattern": "",
                "type": "singleValueList",
                "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=WARD&hierarchyTypeName=REVENUE|$..boundaryNum|$..name",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              },
              {
                "name": "revenueWardId",
                "jsonPath": "licenses[0].revenueWardName",
                "label": "tl.licenses.view.groups.revenueWardId",
                "pattern": "",
                "type": "singleValueList",
                "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=WARD&hierarchyTypeName=REVENUE|$..boundaryNum|$..name",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              },
              {
                "name": "OwnershipType",
                "jsonPath": "licenses[0].ownerShipType",
                "label": "tl.licenses.view.groups.OwnershipType",
                "pattern": "",
                "type": "singleValueList",
                "url": "",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": "",
                "defaultValue": [{
                    "key": "STATE_GOVERNMENT",
                    "value": "STATE_GOVERNMENT"
                  },
                  {
                    "key": "RENTED",
                    "value": "RENTED"
                  }
                ]
              },
              {
                "name": "TradeAddress",
                "jsonPath": "licenses[0].tradeAddress",
                "label": "tl.licenses.view.groups.TradeAddress",
                "pattern": "",
                "type": "textarea",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              }
            ]
          },

          {
            "label": "tl.view.licenses.groups.TradeDetails",
            "name": "TradeDetails",
            "fields": [{
                "name": "TradeTitle",
                "jsonPath": "licenses[0].tradeTitle",
                "label": "tl.view.licenses.groups.TradeTitle",
                "pattern": "",
                "type": "text",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              },
              {
                "name": "TradeType",
                "jsonPath": "licenses[0].tradeType",
                "label": "tl.view.licenses.groups.TradeType",
                "pattern": "",
                "type": "singleValueList",
                "url": "",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": "",
                "defaultValue": [{
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
                "jsonPath": "licenses[0].categoryId",
                "label": "tl.view.licenses.groups.TradeCategory",
                "pattern": "",
                "type": "singleValueList",
                "url": "/tl-masters/category/v1/_search?|$..id|$..name",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              },
              {
                "name": "TradeSubCategory",
                "jsonPath": "licenses[0].subCategory",
                "label": "tl.view.licenses.groups.TradeSubCategory",
                "pattern": "",
                "type": "singleValueList",
                "url": "/tl-masters/category/v1/_search?type=subcategory|$..id|$..name",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              },
              {
                "name": "UOM",
                "jsonPath": "licenses[0].uom",
                "label": "tl.view.licenses.groups.UOM",
                "pattern": "",
                "type": "text",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              },
              {
                "name": "TradeValueforUOM",
                "jsonPath": "licenses[0].quantity",
                "label": "tl.view.licenses.groups.TradeValuefortheUOM",
                "pattern": "",
                "type": "text",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              },
              {
                "name": "validity",
                "jsonPath": "categories[0].validityYears",
                "label": "tl.view.licenses.groups.validity",
                "pattern": "",
                "type": "text",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              },
              {
                "name": "Remarks",
                "jsonPath": "licenses[0].remarks",
                "label": "tl.view.licenses.groups.Remarks",
                "pattern": "",
                "type": "text",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              },
              {
                "name": "TradeCommencementDate",
                "jsonPath": "licenses[0].tradeCommencementDate",
                "label": "tl.view.licenses.groups.TradeCommencementDate",
                "pattern": "",
                "type": "date",
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
    "numCols": 12 / 2,
    "searchUrl": "/tl-masters/v1/category/_search?id={id}",
    "url": "/tl-masters/v1/tl-tradelicense/category/Flammables/{CategoryType.code}/_update",
    "isResponseArray": true,
    "tenantIdRequired": true,
    "useTimestamp": true,
    "objectName": "categories",
    "groups": [{
      "label": "tl.update.groups.categorytype.title",
      "name": "createCategoryType",
      "fields": [{
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
          "defaultValue": true
        }
      ]
    }]
  }
}

export default dat;
