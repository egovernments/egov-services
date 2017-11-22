var dat = {
  "swm.search": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "vehicleFuellingDetails",
    "url": "/swm-services/vehiclefuellingdetails/_search",
    "groups": [
      {
        "name": "search",
        "label": "swm.search.title",
        "fields": [
          {
            "name": "offSet",
            "jsonPath": "offSet",
            "label": "swm.createoffSet",
            "type": "number",
            "isDisabled": false,
            "patternErrorMsg": "swm.create.field.message.offSet"
          }
        ]
      }
    ],
    "result": {
      "header": [
        {
          "label": "swm.search.result.transactionNo"
        },
        {
          "label": "swm.search.result.transactionDate"
        },
        {
          "label": "swm.search.result.vehicleType"
        },
        {
          "label": "swm.search.result.vehicleRegNo"
        },
        {
          "label": "swm.search.result.vehicleReadingDuringFuelling"
        },
        {
          "label": "swm.search.result.refuellingStation"
        },
        {
          "label": "swm.search.result.fuelFilled"
        },
        {
          "label": "swm.search.result.typeOfFuel"
        },
        {
          "label": "swm.search.result.totalCostIncurred"
        },
        {
          "label": "swm.search.result.receiptNo"
        },
        {
          "label": "swm.search.result.receiptDate"
        }
      ],
      "values": [
        "transactionNo",
        "transactionDate",
        "vehicleType.name",
        "vehicleRegNo.regNumber",
        "vehicleReadingDuringFuelling",
        "refuellingStation.name",
        "fuelFilled",
        "typeOfFuel.name",
        "totalCostIncurred",
        "receiptNo",
        "receiptDate"
      ],
      "resultPath": "vehicleFuellingDetails",
      "rowClickUrlUpdate": "/update/swm/vehiclefuellingdetails/{transactionNo}",
      "rowClickUrlView": "/view/swm/vehiclefuellingdetails/{transactionNo}"
    }
  },
  "swm.create": {
    "numCols": 12/3,
    "useTimestamp": true,
    "objectName": "vehicleFuellingDetails",
    "idJsonPath": "vehicleFuellingDetails[0].transactionNo",
    "title": "vehiclefuellingdetails.create.group.title.VehicleDetails1",
    "groups": [
      {
        "name": "VehicleDetails1",
        "label": "",
        "fields": [
          {
            "name": "transactionDate",
            "jsonPath": "vehicleFuellingDetails[0].transactionDate",
            "label": "vehiclefuellingdetails.create.transactionDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails2",
        "label": "vehiclefuellingdetails.create.group.title.VehicleDetails2",
        "fields": [
          {
            "name": "regNumber",
            "jsonPath": "vehicleFuellingDetails[0].vehicle.regNumber",
            "label": "vehiclefuellingdetails.create.regNumber",
            "type": "autoCompelete",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/swm-services/vehicles/_search?|$.vehicles.*.regNumber|$.vehicles.*.regNumber",
            "autoCompleteDependancy": {
              "autoCompleteUrl": "/egov-mdms-service/v1/_get?moduleName=SWM&masterName=Vehicle&filter=[?(@.id=={value})]",
              "autoFillFields": {
                "vehicleFuellingDetails[0].vehicleType.name": "MdmsRes.SWM.Vehicle[0].vehicleType",
                "vehicleFuellingDetails[0].typeOfFuel": ""
              }
            }
          },
          {
            "name": "vehType",
            "jsonPath": "vehicleFuellingDetails[0].vehicleType.name",
            "label": "vehiclefuellingdetails.create.vehicleType",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 128,
            "minLength": 4,
            "patternErrorMsg": "",
            "url": ""
          },
          {
            "name": "vehicleReadingDuringFuelling",
            "jsonPath": "vehicleFuellingDetails[0].vehicleReadingDuringFuelling",
            "label": "vehiclefuellingdetails.create.vehicleReadingDuringFuelling",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails3",
        "label": "vehiclefuellingdetails.create.group.title.VehicleDetails3",
        "fields": [
          {
            "name": "name",
            "jsonPath": "vehicleFuellingDetails[0].refuellingStation.name",
            "label": "vehiclefuellingdetails.search.result.refuellingStation",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/swm-services/refillingpumpstations/_search?|$.refillingPumpStations.*.code|$.refillingPumpStations.*.name"
          },
          {
            "name": "typeOfFuel",
            "jsonPath": "vehicleFuellingDetails[0].typeOfFuel",
            "label": "vehiclefuellingdetails.search.result.typeOfFuel",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "fuelFilled",
            "jsonPath": "vehicleFuellingDetails[0].fuelFilled",
            "label": "vehiclefuellingdetails.search.result.fuelFilled",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "totalCostIncurred",
            "jsonPath": "vehicleFuellingDetails[0].totalCostIncurred",
            "label": "vehiclefuellingdetails.search.result.totalCostIncurred",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "receiptNo",
            "jsonPath": "vehicleFuellingDetails[0].receiptNo",
            "label": "vehiclefuellingdetails.search.result.receiptNo",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "receiptDate",
            "jsonPath": "vehicleFuellingDetails[0].receiptDate",
            "label": "vehiclefuellingdetails.search.result.receiptDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "url": "/swm-services/vehiclefuellingdetails/_create",
    "tenantIdRequired": true
  },
  "swm.view": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "vehicleFuellingDetails",
    "groups": [
      {
        "name": "VehicleDetails1",
        "label": "swm.create.group.title.VehicleDetails1",
        "fields": [
          {
            "name": "transactionDate",
            "jsonPath": "vehicleFuellingDetails[0].transactionDate",
            "label": "swm.create.transactionDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails2",
        "label": "swm.create.group.title.VehicleDetails2",
        "fields": [
          {
            "name": "name",
            "jsonPath": "vehicleFuellingDetails[0].vehicleType.name",
            "label": "swm.create.name",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 128,
            "minLength": 4,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=VehicleType|$..code|$..name"
          },
          {
            "name": "regNumber",
            "jsonPath": "vehicleFuellingDetails[0].vehicleRegNo.regNumber",
            "label": "swm.create.regNumber",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 12,
            "minLength": 6,
            "patternErrorMsg": "",
            "url": "/swm-services/vehicles/_search?|$.vehicles.*.regNumber|$.vehicles.*.regNumber"
          },
          {
            "name": "vehicleReadingDuringFuelling",
            "jsonPath": "vehicleFuellingDetails[0].vehicleReadingDuringFuelling",
            "label": "swm.create.vehicleReadingDuringFuelling",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails3",
        "label": "swm.create.group.title.VehicleDetails3",
        "fields": [
          {
            "name": "name",
            "jsonPath": "vehicleFuellingDetails[0].refuellingStation.name",
            "label": "swm.create.name",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/swm-services/refillingpumpstations/_search?|$.refillingPumpStations.*.code|$.refillingPumpStations.*.name"
          },
          {
            "name": "fuelFilled",
            "jsonPath": "vehicleFuellingDetails[0].fuelFilled",
            "label": "swm.create.fuelFilled",
            "type": "textArea",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "typeOfFuel",
            "jsonPath": "vehicleFuellingDetails[0].typeOfFuel",
            "label": "swm.create.typeOfFuel",
            "type": "textArea",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "totalCostIncurred",
            "jsonPath": "vehicleFuellingDetails[0].totalCostIncurred",
            "label": "swm.create.totalCostIncurred",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "receiptNo",
            "jsonPath": "vehicleFuellingDetails[0].receiptNo",
            "label": "swm.create.receiptNo",
            "type": "textArea",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "receiptDate",
            "jsonPath": "vehicleFuellingDetails[0].receiptDate",
            "label": "swm.create.receiptDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "tenantIdRequired": true
  },
  "swm.update": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "vehicleFuellingDetails",
    "idJsonPath": "vehicleFuellingDetails[0].transactionNo",
    "groups": [
      {
        "name": "VehicleDetails1",
        "label": "swm.create.group.title.VehicleDetails1",
        "fields": [
          {
            "name": "transactionDate",
            "jsonPath": "vehicleFuellingDetails[0].transactionDate",
            "label": "swm.create.transactionDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails2",
        "label": "swm.create.group.title.VehicleDetails2",
        "fields": [
          {
            "name": "name",
            "jsonPath": "vehicleFuellingDetails[0].vehicleType.name",
            "label": "swm.create.name",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 128,
            "minLength": 4,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=VehicleType|$..code|$..name"
          },
          {
            "name": "regNumber",
            "jsonPath": "vehicleFuellingDetails[0].vehicleRegNo.regNumber",
            "label": "swm.create.regNumber",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 12,
            "minLength": 6,
            "patternErrorMsg": "",
            "url": "/swm-services/vehicles/_search?|$.vehicles.*.regNumber|$.vehicles.*.regNumber"
          },
          {
            "name": "vehicleReadingDuringFuelling",
            "jsonPath": "vehicleFuellingDetails[0].vehicleReadingDuringFuelling",
            "label": "swm.create.vehicleReadingDuringFuelling",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleDetails3",
        "label": "swm.create.group.title.VehicleDetails3",
        "fields": [
          {
            "name": "name",
            "jsonPath": "vehicleFuellingDetails[0].refuellingStation.name",
            "label": "swm.create.name",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": "/swm-services/refillingpumpstations/_search?|$.refillingPumpStations.*.code|$.refillingPumpStations.*.name"
          },
          {
            "name": "fuelFilled",
            "jsonPath": "vehicleFuellingDetails[0].fuelFilled",
            "label": "swm.create.fuelFilled",
            "type": "textArea",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "typeOfFuel",
            "jsonPath": "vehicleFuellingDetails[0].typeOfFuel",
            "label": "swm.create.typeOfFuel",
            "type": "textArea",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "totalCostIncurred",
            "jsonPath": "vehicleFuellingDetails[0].totalCostIncurred",
            "label": "swm.create.totalCostIncurred",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "receiptNo",
            "jsonPath": "vehicleFuellingDetails[0].receiptNo",
            "label": "swm.create.receiptNo",
            "type": "textArea",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 256,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "receiptDate",
            "jsonPath": "vehicleFuellingDetails[0].receiptDate",
            "label": "swm.create.receiptDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "url": "/swm-services/vehiclefuellingdetails/_update",
    "tenantIdRequired": true,
    "searchUrl": "/swm-services/vehiclefuellingdetails/_search?transactionNo={transactionNo}"
  }
}
 export default dat;
