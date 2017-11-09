var insuranceDetails ={
  "name": "insuranceDetails",
  "version": "v1",
  "level": 1,
  "jsonPath": "",
  "hide":false,
  "groups":[{
    "name": "details",
    "multiple":false,
    "label":"swm.vehicles.create.group.title.InsuranceDetails",
    "fields": [
      {
        "name": "insuranceNumber",
        "jsonPath": "vehicles[0].insuranceNumber",
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
        "jsonPath": "vehicles[0].insuranceValidityDate",
        "label": "swm.vehicles.create.insuranceValidityDate",
        "type": "datePicker",
        "isRequired": true,
        "isDisabled": false,
        "patternErrorMsg": ""
      },
      {
        "name": "uploadInsuranceDetails",
        "jsonPath": "vehicles[0].insuranceDocument.fileStoreId",
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
  }]
}

var dat = {
  "swm.search": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "",
    "url": "/swm-services/vehicles/_search",
    "groups": [
      {
        "name": "search",
        "label": "swm.vehicles.search.title",
        "fields": [
          {
            "name": "vehicleTypeCode",
            "jsonPath": "vehicleTypeCode",
            "label": "swm.vehicles.create.vehicleType",
            "type": "singleValueList",
            "isDisabled": false,
            "maxLength": 256,
            "patternErrorMsg": "swm.vehicles.create.field.message.vehicleTypeCode",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=VehicleType|$..code|$..code"
          },
          {
            "name": "fuelTypeCode",
            "jsonPath": "fuelTypeCode",
            "label": "swm.vehicles.create.fuelType",
            "type": "singleValueList",
            "isDisabled": false,
            "maxLength": 256,
            "patternErrorMsg": "swm.vehicles.create.field.message.fuelTypeCode",
	          "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=FuelType|$..code|$..code"
          },
          {
            "name": "regNumber",
            "jsonPath": "regNumber",
            "label": "swm.vehicles.create.regNumber",
            "type": "text",
            "isDisabled": false,
            "maxLength": 12,
            "patternErrorMsg": "swm.vehicles.create.field.message.regNumber"
          },
          {
            "name": "engineSrNumber",
            "jsonPath": "engineSrNumber",
            "label": "swm.vehicles.create.engineSrNumber",
            "type": "text",
            "isDisabled": false,
            "maxLength": 256,
            "patternErrorMsg": "swm.vehicles.create.field.message.engineSrNumber"
          },
          {
            "name": "chassisSrNumber",
            "jsonPath": "chassisSrNumber",
            "label": "swm.vehicles.create.chassisSrNumber",
            "type": "text",
            "isDisabled": false,
            "maxLength": 256,
            "patternErrorMsg": "swm.vehicles.create.field.message.chassisSrNumber"
          },
          {
            "name": "model",
            "jsonPath": "model",
            "label": "swm.vehicles.create.model",
            "type": "text",
            "isDisabled": false,
            "maxLength": 256,
            "patternErrorMsg": "swm.vehicles.create.field.message.model"
          },
          {
            "name": "ulbOwnedVehicle",
            "jsonPath": "ulbOwnedVehicle",
            "label": "swm.vehicles.create.ulbOwnedVehicle",
            "type": "checkbox",
            "isDisabled": false,
            "patternErrorMsg": "swm.vehicles.create.field.message.ulbOwnedVehicle"
          },
          {
            "name": "vendorName",
            "jsonPath": "vendorName",
            "label": "swm.vehicles.create.vendor",
            "type": "singleValueList",
            "isDisabled": false,
            "maxLength": 265,
            "patternErrorMsg": "swm.vehicles.create.field.message.vendorName",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=Vendor|$..name|$..name"
          },
          {
            "name": "purchaseDate",
            "jsonPath": "purchaseDate",
            "label": "swm.vehicles.create.purchaseDate",
            "type": "datePicker",
            "isDisabled": false,
            "patternErrorMsg": "swm.vehicles.create.field.message.purchaseDate"
          },
          {
            "name": "purchaseYear",
            "jsonPath": "purchaseYear",
            "label": "swm.vehicles.create.purchaseYear",
            "type": "text",
            "isDisabled": false,
            "maxLength": 265,
            "patternErrorMsg": "swm.vehicles.create.field.message.purchaseYear"
          },
          {
            "name": "insuranceNumber",
            "jsonPath": "insuranceNumber",
            "label": "swm.vehicles.create.insuranceNumber",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "swm.vehicles.create.field.message.insuranceNumber"
          },
          {
            "name": "insuranceValidityDate",
            "jsonPath": "insuranceValidityDate",
            "label": "swm.vehicles.create.insuranceValidityDate",
            "type": "datePicker",
            "isDisabled": false,
            "patternErrorMsg": "swm.vehicles.create.field.message.insuranceValidityDate"
          },
          {
            "name": "isUnderWarranty",
            "jsonPath": "isUnderWarranty",
            "label": "swm.vehicles.create.isUnderWarranty",
            "type": "checkbox",
            "isDisabled": false,
            "patternErrorMsg": "swm.vehicles.create.field.message.isUnderWarranty"
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
            "jsonPath": "vehicles[0].vehicleCapacity",
            "label": "swm.vehicles.search.result.vehicleCapacity",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "engineSrNumber",
            "jsonPath": "vehicles[0].engineSrNumber",
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
            "jsonPath": "vehicles[0].numberOfPersonsReq",
            "label": "swm.vehicles.create.numberOfPersonsReq",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "chassisSrNumber",
            "jsonPath": "vehicles[0].chassisSrNumber",
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
            "jsonPath": "vehicles[0].model",
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
            "patternErrorMsg": ""
          },
          {
            "name": "kilometers",
            "jsonPath": "vehicles[0].kilometers",
            "label": "swm.vehicles.create.kilometers",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "endOfWarranty",
            "jsonPath": "vehicles[0].endOfWarranty",
            "label": "swm.vehicles.create.endOfWarranty",
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails2",
        "label": "swm.vehicles.create.group.title.VehicleDetails2",
        "children":[insuranceDetails],
        "fields": [
          {
            "name": "ulbOwnedVehicle",
            "jsonPath": "vehicles[0].ulbOwnedVehicle",
            "label": "swm.vehicles.create.ulbOwnedVehicle",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "name",
            "jsonPath": "vehicles[0].vendor.name",
            "label": "swm.vehicles.create.vendor",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=Vendor|$..name|$..name"
          }
        ]
      },
      {
        "name": "PurchaseDetails",
        "label": "swm.vehicles.create.group.title.PurchaseDetails",
        "fields": [
          {
            "name": "purchaseDate",
            "jsonPath": "vehicles[0].purchaseDate",
            "label": "swm.vehicles.create.purchaseDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "price",
            "jsonPath": "vehicles[0].price",
            "label": "swm.vehicles.create.price",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "sourceOfPurchase",
            "jsonPath": "vehicles[0].sourceOfPurchase",
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
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": ""
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
            "jsonPath": "vehicles[0].vehicleCapacity",
            "label": "swm.vehicles.create.vehicleCapacity",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "engineSrNumber",
            "jsonPath": "vehicles[0].engineSrNumber",
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
            "jsonPath": "vehicles[0].numberOfPersonsReq",
            "label": "swm.vehicles.create.numberOfPersonsReq",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "chassisSrNumber",
            "jsonPath": "vehicles[0].chassisSrNumber",
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
            "jsonPath": "vehicles[0].model",
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
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 128,
            "minLength": 1,
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
            "jsonPath": "vehicles[0].ulbOwnedVehicle",
            "label": "swm.vehicles.create.ulbOwnedVehicle",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "name",
            "jsonPath": "vehicles[0].vendor.name",
            "label": "swm.vehicles.create.vendor",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "InsuranceDetails",
        "label": "swm.vehicles.create.group.title.InsuranceDetails",
        "fields": [
          {
            "name": "insuranceNumber",
            "jsonPath": "vehicles[0].insuranceNumber",
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
            "jsonPath": "vehicles[0].insuranceValidityDate",
            "label": "swm.vehicles.create.insuranceValidityDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
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
            "jsonPath": "vehicles[0].purchaseDate",
            "label": "swm.vehicles.create.purchaseDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "price",
            "jsonPath": "vehicles[0].price",
            "label": "swm.vehicles.create.price",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "sourceOfPurchase",
            "jsonPath": "vehicles[0].sourceOfPurchase",
            "label": "swm.vehicles.create.sourceOfPurchase",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 0,
            "patternErrorMsg": ""
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
            "patternErrorMsg": ""
          },
          {
            "name": "kilometers",
            "jsonPath": "vehicles[0].kilometers",
            "label": "swm.vehicles.create.kilometers",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "endOfWarranty",
            "jsonPath": "vehicles[0].endOfWarranty",
            "label": "swm.vehicles.create.endOfWarranty",
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": false,
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
            "jsonPath": "vehicles[0].vehicleCapacity",
            "label": "swm.vehicles.create.vehicleCapacity",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "engineSrNumber",
            "jsonPath": "vehicles[0].engineSrNumber",
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
            "jsonPath": "vehicles[0].numberOfPersonsReq",
            "label": "swm.vehicles.create.numberOfPersonsReq",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "chassisSrNumber",
            "jsonPath": "vehicles[0].chassisSrNumber",
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
            "jsonPath": "vehicles[0].model",
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
        "name": "VehicleDetails2",
        "label": "swm.vehicles.create.group.title.VehicleDetails2",
        "fields": [
          {
            "name": "ulbOwnedVehicle",
            "jsonPath": "vehicles[0].ulbOwnedVehicle",
            "label": "swm.vehicles.create.ulbOwnedVehicle",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "name",
            "jsonPath": "vehicles[0].vendor.name",
            "label": "swm.vehicles.create.vendor",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=Vendor|$..name|$..name"
          }
        ]
      },
      {
        "name": "InsuranceDetails",
        "label": "swm.vehicles.create.group.title.InsuranceDetails",
        "fields": [
          {
            "name": "insuranceNumber",
            "jsonPath": "vehicles[0].insuranceNumber",
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
            "jsonPath": "vehicles[0].insuranceValidityDate",
            "label": "swm.vehicles.create.insuranceValidityDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "refId",
            "jsonPath": "vehicles[0].insuranceDocument.fileStoreId",
            "label": "swm.vehicles.create.insurance.details",
            "type": "text",
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
            "jsonPath": "vehicles[0].purchaseDate",
            "label": "swm.vehicles.create.purchaseDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "price",
            "jsonPath": "vehicles[0].price",
            "label": "swm.vehicles.create.price",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "sourceOfPurchase",
            "jsonPath": "vehicles[0].sourceOfPurchase",
            "label": "swm.vehicles.create.sourceOfPurchase",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 0,
            "patternErrorMsg": ""
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
            "patternErrorMsg": ""
          },
          {
            "name": "kilometers",
            "jsonPath": "vehicles[0].kilometers",
            "label": "swm.vehicles.create.kilometers",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "endOfWarranty",
            "jsonPath": "vehicles[0].endOfWarranty",
            "label": "swm.vehicles.create.endOfWarranty",
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": false,
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
