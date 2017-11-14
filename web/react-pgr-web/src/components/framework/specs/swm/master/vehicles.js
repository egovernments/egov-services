var dat = {
  "swm.search": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "",
    "url": "/swm-services/vehicles/_search",
    "groups": [
      {
        "name": "VehicleDetails1",
        "label": "swm.vehicles.create.group.title.VehicleDetails1",
        "fields": [
          {
            "name": "code",
            "jsonPath": "vehicles[0].vehicleType.code",
            "label": "swm.vehicles.create.vehicleType",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=VehicleType|$..code|$..code"
          },
          {
            "name": "regNumber",
            "jsonPath": "vehicles[0].regNumber",
            "label": "swm.vehicles.create.regNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 12,
            "minLength": 6,
            "patternErrorMsg": ""
          },
          {
            "name": "vehicleCapacity",
            "jsonPath": "vehicles[0].manufacturingDetails.vehicleCapacity",
            "label": "swm.vehicles.search.result.vehicleCapacity",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "engineSrNumber",
            "jsonPath": "vehicles[0].manufacturingDetails.engineSrNumber",
            "label": "swm.vehicles.create.engineSrNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "numberOfPersonsReq",
            "jsonPath": "vehicles[0].operatorsReq",
            "label": "swm.vehicles.create.numberOfPersonsReq",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "chassisSrNumber",
            "jsonPath": "vehicles[0].manufacturingDetails.chassisSrNumber",
            "label": "swm.vehicles.create.chassisSrNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "model",
            "jsonPath": "vehicles[0].manufacturingDetails.model",
            "label": "swm.vehicles.create.model",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "code",
            "jsonPath": "vehicles[0].fuelType.code",
            "label": "swm.vehicles.create.fuelType",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=FuelType|$..code|$..code"
          }
        ]
      },
      {
        "name": "WarrantyDetails",
        "label": "swm.vehicles.create.group.title.WarrantyDetails",
        "fields": [
          {
            "name": "isUnderWarranty",
            "jsonPath": "vehicles[0].isUnderWarranty",
            "label": "swm.vehicles.create.isUnderWarranty",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "",
            "defaultValue": false,
            "showHideFields": [{
              "ifValue": true,
              "hide": [],
              "show": [{
                "name": "kilometers",
                "isGroup": false,
                "isField": true
              },
              {
                "name": "endOfWarranty",
                "isGroup": false,
                "isField": true
              }]
            }]
          },
          {
            "name": "kilometers",
            "jsonPath": "vehicles[0].kilometers",
            "label": "swm.vehicles.create.kilometers",
            "type": "number",
            "hide": true,
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "endOfWarranty",
            "jsonPath": "vehicles[0].endOfWarranty",
            "label": "swm.vehicles.create.endOfWarranty",
            "type": "datePicker",
            "hide": true,
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails2",
        "label": "swm.vehicles.create.group.title.VehicleDetails2",
        "fields": [
          {
            "name": "ulbOwnedVehicle",
            "jsonPath": "vehicles[0].isulbownedvehicle",
            "label": "swm.vehicles.create.ulbOwnedVehicle",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "",
            "defaultValue": true,
            "showHideFields": [{
              "ifValue": false,
              "hide": [],
              "show": [{
                "name": "vendorname",
                "isGroup": false,
                "isField": true
              }]
            }]
          },
          {
            "name": "vendorname",
            "jsonPath": "vehicles[0].vendor.name",
            "label": "swm.vehicles.create.vendor",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "hide": true,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=Vendor|$..name|$..name"
          }
        ]
      },
      {
        "name": "insuranceDetails",
        "label":"swm.vehicles.create.group.title.InsuranceDetails",
        "fields": [
          {
            "name": "insuranceNumber",
            "jsonPath": "vehicles[0].insuranceDetails.insuranceNumber",
            "label": "swm.vehicles.create.insuranceNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "insuranceValidityDate",
            "jsonPath": "vehicles[0].insuranceDetails.insuranceValidityDate",
            "label": "swm.vehicles.create.insuranceValidityDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "uploadInsuranceDetails",
            "jsonPath": "vehicles[0].insuranceDetails.insuranceDocument.fileStoreId",
            "label": "swm.vehicles.create.insurance.details",
            "type": "singleFileUpload",
            "pathToArray": "documentTypes",
            "displayNameJsonPath": "name",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "PurchaseDetails",
        "label": "swm.vehicles.create.group.title.PurchaseDetails",
        "fields": [
          {
            "name": "purchaseDate",
            "jsonPath": "vehicles[0].purchaseInfo.purchaseDate",
            "label": "swm.vehicles.create.purchaseDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "price",
            "jsonPath": "vehicles[0].purchaseInfo.price",
            "label": "swm.vehicles.create.price",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "sourceOfPurchase",
            "jsonPath": "vehicles[0].purchaseInfo.sourceOfPurchase",
            "label": "swm.vehicles.create.sourceOfPurchase",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 0,
            "patternErrorMsg": ""
          },
          {
            "name": "remarks",
            "jsonPath": "vehicles[0].remarks",
            "label": "swm.vehicles.create.remarks",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 300,
            "minLength": 15,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "result": {
      "header": [
        {
          "label": "swm.vehicles.search.result.vehicleType"
        },
        {
          "label": "swm.vehicles.search.result.regNumber"
        },
        {
          "label": "swm.vehicles.search.result.vehicleCapacity"
        },
        {
          "label": "swm.vehicles.search.result.engineSrNumber"
        },
        {
          "label": "swm.vehicles.search.result.numberOfPersonsReq"
        },
        {
          "label": "swm.vehicles.search.result.chassisSrNumber"
        },
        {
          "label": "swm.vehicles.search.result.model"
        },
        {
          "label": "swm.vehicles.search.result.fuelType"
        },
        {
          "label": "swm.vehicles.search.result.ulbOwnedVehicle"
        },
        {
          "label": "swm.vehicles.search.result.vendor"
        },
        {
          "label": "swm.vehicles.search.result.insuranceNumber"
        },
        {
          "label": "swm.vehicles.search.result.insuranceValidityDate",
          "isDate": true
        },
        {
          "label": "swm.vehicles.search.result.purchaseDate",
          "isDate": true
        },
        {
          "label": "swm.vehicles.search.result.yearOfPurchase"
        },
        {
          "label": "swm.vehicles.search.result.price"
        },
        {
          "label": "swm.vehicles.search.result.sourceOfPurchase"
        },
        {
          "label": "swm.vehicles.search.result.isUnderWarranty"
        },
        {
          "label": "swm.vehicles.search.result.kilometers"
        },
        {
          "label": "swm.vehicles.search.result.endOfWarranty"
        },
        {
          "label": "swm.vehicles.search.result.remarks"
        }
      ],
      "values": [
        "vehicleType.code",
        "regNumber",
        "vehicleCapacity",
        "engineSrNumber",
        "numberOfPersonsReq",
        "chassisSrNumber",
        "model",
        "fuelType.code",
        "ulbOwnedVehicle",
        "vendor.name",
        "insuranceNumber",
        "insuranceValidityDate",
        "purchaseDate",
        "yearOfPurchase",
        "price",
        "sourceOfPurchase",
        "isUnderWarranty",
        "kilometers",
        "endOfWarranty",
        "remarks"
      ],
      "resultPath": "vehicles",
      "rowClickUrlUpdate": "/update/vehicles/{regNumber}",
      "rowClickUrlView": "/view/vehicles/{regNumber}"
    }
  },
  "swm.create": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "vehicles",
    "idJsonPath": "vehicles[0].regNumber",
    "title": "swm.vehicles.create.title",
    "groups": [
      {
        "name": "VehicleDetails1",
        "label": "swm.vehicles.create.group.title.VehicleDetails1",
        "fields": [
          {
            "name": "code",
            "jsonPath": "vehicles[0].vehicleType.code",
            "label": "swm.vehicles.create.vehicleType",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=VehicleType|$..code|$..name"
          },
          {
            "name": "regNumber",
            "jsonPath": "vehicles[0].regNumber",
            "label": "swm.vehicles.create.regNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 12,
            "minLength": 6,
            "patternErrorMsg": ""
          },
          {
            "name": "vehicleCapacity",
            "jsonPath": "vehicles[0].manufacturingDetails.vehicleCapacity",
            "label": "swm.vehicles.search.result.vehicleCapacity",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "engineSrNumber",
            "jsonPath": "vehicles[0].manufacturingDetails.engineSrNumber",
            "label": "swm.vehicles.create.engineSrNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "numberOfPersonsReq",
            "jsonPath": "vehicles[0].operatorsReq",
            "label": "swm.vehicles.create.numberOfPersonsReq",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "chassisSrNumber",
            "jsonPath": "vehicles[0].manufacturingDetails.chassisSrNumber",
            "label": "swm.vehicles.create.chassisSrNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "model",
            "jsonPath": "vehicles[0].manufacturingDetails.model",
            "label": "swm.vehicles.create.model",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "fueltype",
            "jsonPath": "vehicles[0].fuelType.code",
            "label": "swm.vehicles.create.fuelType",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=FuelType|$..code|$..name"
          }
        ]
      },
      {
        "name": "WarrantyDetails",
        "label": "swm.vehicles.create.group.title.WarrantyDetails",
        "fields": [
          {
            "name": "isUnderWarranty",
            "jsonPath": "vehicles[0].isUnderWarranty",
            "label": "swm.vehicles.create.isUnderWarranty",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "",
            "defaultValue": false,
            "showHideFields": [{
              "ifValue": true,
              "hide": [],
              "show": [{
                "name": "kilometers",
                "isGroup": false,
                "isField": true
              },
              {
                "name": "endOfWarranty",
                "isGroup": false,
                "isField": true
              }]
            }]
          },
          {
            "name": "kilometers",
            "jsonPath": "vehicles[0].kilometers",
            "label": "swm.vehicles.create.kilometers",
            "type": "number",
            "hide": true,
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "endOfWarranty",
            "jsonPath": "vehicles[0].endOfWarranty",
            "label": "swm.vehicles.create.endOfWarranty",
            "type": "datePicker",
            "hide": true,
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails2",
        "label": "swm.vehicles.create.group.title.VehicleDetails2",
        "fields": [
          {
            "name": "ulbOwnedVehicle",
            "jsonPath": "vehicles[0].isulbownedvehicle",
            "label": "swm.vehicles.create.ulbOwnedVehicle",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "",
            "defaultValue": false,
            "showHideFields": [{
              "ifValue": true,
              "show": [],
              "hide": [{
                "name": "vendorname",
                "isGroup": false,
                "isField": true
              }]
            }]
          },
          {
            "name": "vendorname",
            "jsonPath": "vehicles[0].vendor.vendorNo",
            "label": "swm.vehicles.create.vendor",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "swm-services/vendors/_search?|$.vendors.*.vendorNo|$.vendors.*.name"
          }
        ]
      },
      {
        "name": "insuranceDetails",
        "label":"swm.vehicles.create.group.title.InsuranceDetails",
        "fields": [
          {
            "name": "insuranceNumber",
            "jsonPath": "vehicles[0].insuranceDetails.insuranceNumber",
            "label": "swm.vehicles.create.insuranceNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "insuranceValidityDate",
            "jsonPath": "vehicles[0].insuranceDetails.insuranceValidityDate",
            "label": "swm.vehicles.create.insuranceValidityDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "uploadInsuranceDetails",
            "jsonPath": "vehicles[0].insuranceDetails.insuranceDocument.fileStoreId",
            "label": "swm.vehicles.create.insurance.details",
            "type": "singleFileUpload",
            "pathToArray": "documentTypes",
            "displayNameJsonPath": "name",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "PurchaseDetails",
        "label": "swm.vehicles.create.group.title.PurchaseDetails",
        "fields": [
          {
            "name": "purchaseDate",
            "jsonPath": "vehicles[0].purchaseInfo.purchaseDate",
            "label": "swm.vehicles.create.purchaseDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "price",
            "jsonPath": "vehicles[0].purchaseInfo.price",
            "label": "swm.vehicles.create.price",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "sourceOfPurchase",
            "jsonPath": "vehicles[0].purchaseInfo.sourceOfPurchase",
            "label": "swm.vehicles.create.sourceOfPurchase",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 0,
            "patternErrorMsg": ""
          },
          {
            "name": "remarks",
            "jsonPath": "vehicles[0].remarks",
            "label": "swm.vehicles.create.remarks",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 300,
            "minLength": 15,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "url": "/swm-services/vehicles/_create",
    "tenantIdRequired": true
  },
  "swm.view": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "vehicles",
    "groups": [
      {
        "name": "VehicleDetails1",
        "label": "swm.vehicles.create.group.title.VehicleDetails1",
        "fields": [
          {
            "name": "code",
            "jsonPath": "vehicles[0].vehicleType.code",
            "label": "swm.vehicles.create.vehicleType",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=VehicleType|$..code|$..code"
          },
          {
            "name": "regNumber",
            "jsonPath": "vehicles[0].regNumber",
            "label": "swm.vehicles.create.regNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 12,
            "minLength": 6,
            "patternErrorMsg": ""
          },
          {
            "name": "vehicleCapacity",
            "jsonPath": "vehicles[0].manufacturingDetails.vehicleCapacity",
            "label": "swm.vehicles.search.result.vehicleCapacity",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "engineSrNumber",
            "jsonPath": "vehicles[0].manufacturingDetails.engineSrNumber",
            "label": "swm.vehicles.create.engineSrNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "numberOfPersonsReq",
            "jsonPath": "vehicles[0].operatorsReq",
            "label": "swm.vehicles.create.numberOfPersonsReq",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "chassisSrNumber",
            "jsonPath": "vehicles[0].manufacturingDetails.chassisSrNumber",
            "label": "swm.vehicles.create.chassisSrNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "model",
            "jsonPath": "vehicles[0].manufacturingDetails.model",
            "label": "swm.vehicles.create.model",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "code",
            "jsonPath": "vehicles[0].fuelType.code",
            "label": "swm.vehicles.create.fuelType",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=FuelType|$..code|$..code"
          }
        ]
      },
      {
        "name": "WarrantyDetails",
        "label": "swm.vehicles.create.group.title.WarrantyDetails",
        "fields": [
          {
            "name": "isUnderWarranty",
            "jsonPath": "vehicles[0].isUnderWarranty",
            "label": "swm.vehicles.create.isUnderWarranty",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "",
            "defaultValue": "",
            "showHideFields": [{
              "ifValue": true,
              "hide": [],
              "show": [{
                "name": "kilometers",
                "isGroup": false,
                "isField": true
              },
              {
                "name": "endOfWarranty",
                "isGroup": false,
                "isField": true
              }]
            }]
          },
          {
            "name": "kilometers",
            "jsonPath": "vehicles[0].kilometers",
            "label": "swm.vehicles.create.kilometers",
            "type": "number",
            "hide": true,
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "endOfWarranty",
            "jsonPath": "vehicles[0].endOfWarranty",
            "label": "swm.vehicles.create.endOfWarranty",
            "type": "datePicker",
            "hide": true,
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails2",
        "label": "swm.vehicles.create.group.title.VehicleDetails2",
        "fields": [
          {
            "name": "ulbOwnedVehicle",
            "jsonPath": "vehicles[0].isulbownedvehicle",
            "label": "swm.vehicles.create.ulbOwnedVehicle",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "",
            "defaultValue": "",
            "showHideFields": [{
              "ifValue": false,
              "hide": [],
              "show": [{
                "name": "vendorname",
                "isGroup": false,
                "isField": true
              }]
            }]
          },
          {
            "name": "vendorname",
            "jsonPath": "vehicles[0].vendor.name",
            "label": "swm.vehicles.create.vendor",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "hide": true,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "swm-services/vendor/_search?|$..name|$..name"
          }
        ]
      },
      {
        "name": "insuranceDetails",
        "label":"swm.vehicles.create.group.title.InsuranceDetails",
        "fields": [
          {
            "name": "insuranceNumber",
            "jsonPath": "vehicles[0].insuranceDetails.insuranceNumber",
            "label": "swm.vehicles.create.insuranceNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "insuranceValidityDate",
            "jsonPath": "vehicles[0].insuranceDetails.insuranceValidityDate",
            "label": "swm.vehicles.create.insuranceValidityDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "uploadInsuranceDetails",
            "jsonPath": "vehicles[0].insuranceDetails.insuranceDocument.fileStoreId",
            "label": "swm.vehicles.create.insurance.details",
            "type": "singleFileUpload",
            "pathToArray": "documentTypes",
            "displayNameJsonPath": "name",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "PurchaseDetails",
        "label": "swm.vehicles.create.group.title.PurchaseDetails",
        "fields": [
          {
            "name": "purchaseDate",
            "jsonPath": "vehicles[0].purchaseInfo.purchaseDate",
            "label": "swm.vehicles.create.purchaseDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "price",
            "jsonPath": "vehicles[0].purchaseInfo.price",
            "label": "swm.vehicles.create.price",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "sourceOfPurchase",
            "jsonPath": "vehicles[0].purchaseInfo.sourceOfPurchase",
            "label": "swm.vehicles.create.sourceOfPurchase",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 0,
            "patternErrorMsg": ""
          },
          {
            "name": "remarks",
            "jsonPath": "vehicles[0].remarks",
            "label": "swm.vehicles.create.remarks",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 300,
            "minLength": 15,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "tenantIdRequired": true,
    "url": "/swm-services/vehicles/_search?regNumber={regNumber}"
  },
  "swm.update": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "vehicles",
    "groups": [
      {
        "name": "VehicleDetails1",
        "label": "swm.vehicles.create.group.title.VehicleDetails1",
        "fields": [
          {
            "name": "code",
            "jsonPath": "vehicles[0].vehicleType.code",
            "label": "swm.vehicles.create.vehicleType",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=VehicleType|$..code|$..code"
          },
          {
            "name": "regNumber",
            "jsonPath": "vehicles[0].regNumber",
            "label": "swm.vehicles.create.regNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 12,
            "minLength": 6,
            "patternErrorMsg": ""
          },
          {
            "name": "vehicleCapacity",
            "jsonPath": "vehicles[0].manufacturingDetails.vehicleCapacity",
            "label": "swm.vehicles.search.result.vehicleCapacity",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "engineSrNumber",
            "jsonPath": "vehicles[0].manufacturingDetails.engineSrNumber",
            "label": "swm.vehicles.create.engineSrNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "numberOfPersonsReq",
            "jsonPath": "vehicles[0].operatorsReq",
            "label": "swm.vehicles.create.numberOfPersonsReq",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "chassisSrNumber",
            "jsonPath": "vehicles[0].manufacturingDetails.chassisSrNumber",
            "label": "swm.vehicles.create.chassisSrNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "model",
            "jsonPath": "vehicles[0].manufacturingDetails.model",
            "label": "swm.vehicles.create.model",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "code",
            "jsonPath": "vehicles[0].fuelType.code",
            "label": "swm.vehicles.create.fuelType",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=FuelType|$..code|$..code"
          }
        ]
      },
      {
        "name": "WarrantyDetails",
        "label": "swm.vehicles.create.group.title.WarrantyDetails",
        "fields": [
          {
            "name": "isUnderWarranty",
            "jsonPath": "vehicles[0].isUnderWarranty",
            "label": "swm.vehicles.create.isUnderWarranty",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "",
            "defaultValue": "",
            "showHideFields": [{
              "ifValue": true,
              "hide": [],
              "show": [{
                "name": "kilometers",
                "isGroup": false,
                "isField": true
              },
              {
                "name": "endOfWarranty",
                "isGroup": false,
                "isField": true
              }]
            }]
          },
          {
            "name": "kilometers",
            "jsonPath": "vehicles[0].kilometers",
            "label": "swm.vehicles.create.kilometers",
            "type": "number",
            "hide": true,
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "endOfWarranty",
            "jsonPath": "vehicles[0].endOfWarranty",
            "label": "swm.vehicles.create.endOfWarranty",
            "type": "datePicker",
            "hide": true,
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails2",
        "label": "swm.vehicles.create.group.title.VehicleDetails2",
        "fields": [
          {
            "name": "ulbOwnedVehicle",
            "jsonPath": "vehicles[0].isulbownedvehicle",
            "label": "swm.vehicles.create.ulbOwnedVehicle",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "",
            "defaultValue": "",
            "showHideFields": [{
              "ifValue": false,
              "hide": [],
              "show": [{
                "name": "vendorname",
                "isGroup": false,
                "isField": true
              }]
            }]
          },
          {
            "name": "vendorname",
            "jsonPath": "vehicles[0].vendor.name",
            "label": "swm.vehicles.create.vendor",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "hide": true,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=Vendor|$..name|$..name"
          }
        ]
      },
      {
        "name": "insuranceDetails",
        "label":"swm.vehicles.create.group.title.InsuranceDetails",
        "fields": [
          {
            "name": "insuranceNumber",
            "jsonPath": "vehicles[0].insuranceDetails.insuranceNumber",
            "label": "swm.vehicles.create.insuranceNumber",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "insuranceValidityDate",
            "jsonPath": "vehicles[0].insuranceDetails.insuranceValidityDate",
            "label": "swm.vehicles.create.insuranceValidityDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "uploadInsuranceDetails",
            "jsonPath": "vehicles[0].insuranceDetails.insuranceDocument.fileStoreId",
            "label": "swm.vehicles.create.insurance.details",
            "type": "singleFileUpload",
            "pathToArray": "documentTypes",
            "displayNameJsonPath": "name",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "PurchaseDetails",
        "label": "swm.vehicles.create.group.title.PurchaseDetails",
        "fields": [
          {
            "name": "purchaseDate",
            "jsonPath": "vehicles[0].purchaseInfo.purchaseDate",
            "label": "swm.vehicles.create.purchaseDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "price",
            "jsonPath": "vehicles[0].purchaseInfo.price",
            "label": "swm.vehicles.create.price",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "sourceOfPurchase",
            "jsonPath": "vehicles[0].purchaseInfo.sourceOfPurchase",
            "label": "swm.vehicles.create.sourceOfPurchase",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 0,
            "patternErrorMsg": ""
          },
          {
            "name": "remarks",
            "jsonPath": "vehicles[0].remarks",
            "label": "swm.vehicles.create.remarks",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 300,
            "minLength": 15,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "url": "/swm-services/vehicles/_update",
    "tenantIdRequired": true,
    "searchUrl": "/swm-services/vehicles/_search?regNumber={regNumber}"
  }
}
 export default dat;
