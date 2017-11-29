var dat = {
  "swm.search": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "vehiclemaintenancedetails",
    "url": "/swm-services/vehiclemaintenancedetails/_search",
    "groups": [
      {
        "name": "search",
        "label": "swm.search.title",
        "fields": [
          {
            "name": "maintenanceType",
            "jsonPath": "maintenanceType",
            "label": "swm.create.maintenanceType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": [{
                key: "MAINTENANCE",
                value: "Maintenance"
              },{
                key: "REPAIR",
                value: "Repair"
              }],
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": ""
          },
          {
            "name": "regNumber",
            "jsonPath": "regNumber",
            "label": "vehiclefuellingdetails.create.regNumber",
            "pattern": "",
            "type": "autoCompelete",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 12,
            "minLength": 6,
            "patternErrorMsg": "",
            "url": "swm-services/vehicles/_search?|$.vehicles.*.regNumber|$.vehicles.*.regNumber",
          }
        ]
      }
    ],
    "result": {
      "header": [
        {
          "label": "vehiclefuellingdetails.create.regNumber"
        },
        {
          "label": "vehiclefuellingdetails.create.vehicleType"
        },
        {
          "label": "swm.create.vehicleScheduledMaintenanceDate"
        },
        {
          "label": "swm.create.costIncurred"
        }
      ],
      "values": [
        "regNumber",
        "name",
        "vehicleScheduledMaintenanceDate",
        "costIncurred"
      ],
      "resultPath":"vehicleMaintenanceDetails",
      "rowClickUrlUpdate": "/update/swm/vehiclemaintenancedetails/{code}",
      "rowClickUrlView": "/view/swm/vehiclemaintenancedetails/{code}"
    }
  },
  "swm.create": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "vehicleMaintenanceDetails",
    "idJsonPath": "vehicleMaintenanceDetails[0].code",
    "groups": [
      {
        "name": "Selection",
        "label": "swm.create.group.title.Selection",
        "fields": [
          {
            "name": "",
            "jsonPath": "vehicleMaintenanceDetails[0].isScheduled",
            "label": "swm.create.isScheduled",
            "type": "checkbox",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "defaultValue": false,
            "showHideFields": [{
              "ifValue": true,
              "hide": [],
              "show": [
                {
                  "name": "vehicleScheduledMaintenanceDate",
                  "isGroup": false,
                  "isField": true
                },
                {
                  "name": "insuranceValidityDate",
                  "isGroup": false,
                  "isField": true
                },
                {
                  "name": "Downtimedefined",
                  "isGroup": false,
                  "isField": true
                }
              ]
            }]
          },
          {
            "name": "maintenanceType",
            "jsonPath": "vehicleMaintenanceDetails[0].maintenanceType",
            "label": "swm.create.maintenanceType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": [{
                key: "MAINTENANCE",
                value: "Maintenance"
              },{
                key: "REPAIR",
                value: "Repair"
              }],
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleMaintenanceDeatils",
        "label": "swm.create.group.title.VehicleMaintenanceDeatils",
        "fields": [
          {
            "name": "regNumber",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicle.regNumber",
            "label": "vehiclefuellingdetails.create.regNumber",
            "pattern": "",
            "type": "autoCompelete",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 12,
            "minLength": 6,
            "patternErrorMsg": "",
            "url": "swm-services/vehicles/_search?|$.vehicles.*.regNumber|$.vehicles.*.regNumber",
            "autoCompleteDependancy": {
              "autoCompleteUrl": "/swm-services/vehicles/_search?regNumber={vehicleMaintenanceDetails[0].vehicle.regNumber}",
              "autoFillFields": {
                "vehicleMaintenanceDetails[0].vehicle.vehicleType.code": "vehicles[0].vehicleType.name",
                "vehicleMaintenanceDetails[0].vehicle.insuranceDetails.insuranceValidityDate": "vehicles[0].insuranceDetails.insuranceValidityDate",
              }
            },
            "depedants": [{
                "jsonPath": "vehicleMaintenanceDetails[0].vehicleScheduledMaintenanceDate",
                "type": "date",
                "pattern": "/swm-services/vehiclemaintenancedetails/_getnextscheduleddate?vehicleRegNumber={vehicleMaintenanceDetails[0].vehicle.regNumber}|$..*.id|$..*.name"
              },
              {
                "jsonPath": "vehicleMaintenanceDetails[0].downtime",
                "type": "text",
                "pattern": "/swm-services/vehiclemaintenances/_search?regNumber={vehicleMaintenanceDetails[0].vehicle.regNumber}|$..*.id|$..*.name"
              }
            ]
          },
          {
            "name": "name",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicle.vehicleType.code",
            "label": "vehiclefuellingdetails.create.vehicleType",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue": "",
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": ""
          },
          {
            "name": "vehicleScheduledMaintenanceDate",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicleScheduledMaintenanceDate",
            "label": "swm.create.vehicleScheduledMaintenanceDate",
            "pattern": "",
            "hide": true,
            "type": "date",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue": "",
            "patternErrorMsg": "",
            "url": "",
            "isStateLevel":true
            
          },
          {
            "name": "actualMaintenanceDate",
            "jsonPath": "vehicleMaintenanceDetails[0].actualMaintenanceDate",
            "label": "swm.create.actualMaintenanceDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "costIncurred",
            "jsonPath": "vehicleMaintenanceDetails[0].costIncurred",
            "label": "swm.create.costIncurred",
            "pattern": "",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "insuranceValidityDate",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicle.insuranceDetails.insuranceValidityDate",
            "label": "swm.create.insuranceDetails.insuranceValidityDate",
            "pattern": "",
            "type": "date",
            "isRequired": false,
            "isDisabled": true,
            "hide": true,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "Downtimedefined",
            "jsonPath": "vehicleMaintenanceDetails[0].downtime",
            "label": "swm.create.DowntimeDefined",
            "pattern": "",
            "type": "text",
            "hide": true,
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": "",
            "url": ""
          },
          {
            "name": "vehicleReadingDuringMaintenance",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicleReadingDuringMaintenance",
            "label": "swm.create.vehicleReadingDuringMaintenance",
            "pattern": "",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "vehicleDowntimeActual",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicleDowntimeActual",
            "label": "swm.create.vehicleDowntimeActual",
            "pattern": "",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "vehicleDownTimeActualUom",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicleDownTimeActualUom",
            "label": "swm.create.vehicleDownTimeActualUom",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": [
              {
                key: "Hrs",
                value: "Hrs"
              },
              {
                key: "Days",
                value: "Days"
            }],
            "patternErrorMsg": ""
          },
          {
            "name": "remarks",
            "jsonPath": "vehicleMaintenanceDetails[0].remarks",
            "label": "swm.create.remarks",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 300,
            "minLength": 0,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "url": "/swm-services/vehiclemaintenancedetails/_create",
    "tenantIdRequired": true
  },
  "swm.view": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "vehicleMaintenanceDetails",
    "groups": [
      {
        "name": "Selection",
        "label": "swm.create.group.title.Selection",
        "fields": [
          {
            "name": "",
            "jsonPath": "vehicleMaintenanceDetails[0].isScheduled",
            "label": "swm.create.isScheduled",
            "type": "checkbox",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "defaultValue": false,
            "showHideFields": [{
              "ifValue": true,
              "hide": [],
              "show": [
                {
                  "name": "vehicleScheduledMaintenanceDate",
                  "isGroup": false,
                  "isField": true
                },
                {
                  "name": "insuranceValidityDate",
                  "isGroup": false,
                  "isField": true
                },
                {
                  "name": "Downtimedefined",
                  "isGroup": false,
                  "isField": true
                }
              ]
            }] 
          },
          {
            "name": "maintenanceType",
            "jsonPath": "vehicleMaintenanceDetails[0].maintenanceType",
            "label": "swm.create.maintenanceType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": [{
                key: "MAINTENANCE",
                value: "Maintenance"
              },{
                key: "REPAIR",
                value: "Repair"
              }],
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleMaintenanceDeatils",
        "label": "swm.create.group.title.VehicleMaintenanceDeatils",
        "fields": [
          {
            "name": "regNumber",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicle.regNumber",
            "label": "vehiclefuellingdetails.create.regNumber",
            "pattern": "",
            "type": "autoCompelete",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 12,
            "minLength": 6,
            "patternErrorMsg": "",
            "url": "swm-services/vehicles/_search?|$.vehicles.*.regNumber|$.vehicles.*.regNumber",
            "autoCompleteDependancy": {
              "autoCompleteUrl": "/swm-services/vehicles/_search?regNumber={vehicleMaintenanceDetails[0].vehicle.regNumber}",
              "autoFillFields": {
                "vehicleMaintenanceDetails[0].vehicle.vehicleType.code": "vehicles[0].vehicleType.name",
                "vehicleMaintenanceDetails[0].vehicle.insuranceDetails.insuranceValidityDate": "vehicles[0].insuranceDetails.insuranceValidityDate",
              }
            },
            "depedants": [{
                "jsonPath": "vehicleMaintenanceDetails[0].vehicleScheduledMaintenanceDate",
                "type": "date",
                "pattern": "/swm-services/vehiclemaintenancedetails/_getnextscheduleddate?vehicleRegNumber={vehicleMaintenanceDetails[0].vehicle.regNumber}|$..*.id|$..*.name"
              },
              {
                "jsonPath": "vehicleMaintenanceDetails[0].downtime",
                "type": "text",
                "pattern": "/swm-services/vehiclemaintenances/_search?regNumber={vehicleMaintenanceDetails[0].vehicle.regNumber}|$..*.id|$..*.name"
              }
            ]
          },
          {
            "name": "name",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicle.vehicleType.code",
            "label": "vehiclefuellingdetails.create.vehicleType",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue": "",
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": ""
          },
          {
            "name": "vehicleScheduledMaintenanceDate",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicleScheduledMaintenanceDate",
            "label": "swm.create.vehicleScheduledMaintenanceDate",
            "pattern": "",
            "hide": true,
            "type": "date",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue": "",
            "patternErrorMsg": "",
            "url": "",
            "isStateLevel":true
            
          },
          {
            "name": "actualMaintenanceDate",
            "jsonPath": "vehicleMaintenanceDetails[0].actualMaintenanceDate",
            "label": "swm.create.actualMaintenanceDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "costIncurred",
            "jsonPath": "vehicleMaintenanceDetails[0].costIncurred",
            "label": "swm.create.costIncurred",
            "pattern": "",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "insuranceValidityDate",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicle.insuranceDetails.insuranceValidityDate",
            "label": "swm.create.insuranceDetails.insuranceValidityDate",
            "pattern": "",
            "type": "date",
            "isRequired": false,
            "isDisabled": true,
            "hide": true,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "Downtimedefined",
            "jsonPath": "vehicleMaintenanceDetails[0].downtime",
            "label": "swm.create.DowntimeDefined",
            "pattern": "",
            "type": "text",
            "hide": true,
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": "",
            "url": ""
          },
          {
            "name": "vehicleReadingDuringMaintenance",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicleReadingDuringMaintenance",
            "label": "swm.create.vehicleReadingDuringMaintenance",
            "pattern": "",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "vehicleDowntimeActual",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicleDowntimeActual",
            "label": "swm.create.vehicleDowntimeActual",
            "pattern": "",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "vehicleDownTimeActualUom",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicleDownTimeActualUom",
            "label": "swm.create.vehicleDownTimeActualUom",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": [
              {
                key: "Hrs",
                value: "Hrs"
              },
              {
                key: "Days",
                value: "Days"
            }],
            "patternErrorMsg": ""
          },
          {
            "name": "remarks",
            "jsonPath": "vehicleMaintenanceDetails[0].remarks",
            "label": "swm.create.remarks",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 300,
            "minLength": 15,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "tenantIdRequired": true,
    "url": "/swm-services/vehiclemaintenancedetails/_search?code={code}"
  },
  "swm.update": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "vehicleMaintenanceDetails",
    "groups": [
      {
        "name": "Selection",
        "label": "swm.create.group.title.Selection",
        "fields": [
          {
            "name": "",
            "jsonPath": "vehicleMaintenanceDetails[0].isScheduled",
            "label": "swm.create.isScheduled",
            "type": "checkbox",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "defaultValue": false,
            "showHideFields": [{
              "ifValue": true,
              "hide": [],
              "show": [
                {
                  "name": "vehicleScheduledMaintenanceDate",
                  "isGroup": false,
                  "isField": true
                },
                {
                  "name": "insuranceValidityDate",
                  "isGroup": false,
                  "isField": true
                },
                {
                  "name": "Downtimedefined",
                  "isGroup": false,
                  "isField": true
                }
              ]
            }]
          },
          {
            "name": "maintenanceType",
            "jsonPath": "vehicleMaintenanceDetails[0].maintenanceType",
            "label": "swm.create.maintenanceType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": [{
                key: "MAINTENANCE",
                value: "Maintenance"
              },{
                key: "REPAIR",
                value: "Repair"
              }],
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "VehicleMaintenanceDeatils",
        "label": "swm.create.group.title.VehicleMaintenanceDeatils",
        "fields": [
          {
            "name": "regNumber",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicle.regNumber",
            "label": "vehiclefuellingdetails.create.regNumber",
            "pattern": "",
            "type": "autoCompelete",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 12,
            "minLength": 6,
            "patternErrorMsg": "",
            "url": "swm-services/vehicles/_search?|$.vehicles.*.regNumber|$.vehicles.*.regNumber",
            "autoCompleteDependancy": {
              "autoCompleteUrl": "/swm-services/vehicles/_search?regNumber={vehicleMaintenanceDetails[0].vehicle.regNumber}",
              "autoFillFields": {
                "vehicleMaintenanceDetails[0].vehicle.vehicleType.code": "vehicles[0].vehicleType.name",
                "vehicleMaintenanceDetails[0].vehicle.insuranceDetails.insuranceValidityDate": "vehicles[0].insuranceDetails.insuranceValidityDate",
              }
            },
            "depedants": [{
                "jsonPath": "vehicleMaintenanceDetails[0].vehicleScheduledMaintenanceDate",
                "type": "date",
                "pattern": "/swm-services/vehiclemaintenancedetails/_getnextscheduleddate?vehicleRegNumber={vehicleMaintenanceDetails[0].vehicle.regNumber}|$..*.id|$..*.name"
              },
              {
                "jsonPath": "vehicleMaintenanceDetails[0].downtime",
                "type": "text",
                "pattern": "/swm-services/vehiclemaintenances/_search?regNumber={vehicleMaintenanceDetails[0].vehicle.regNumber}|$..*.id|$..*.name"
              }
            ]
          },
          {
            "name": "name",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicle.vehicleType.code",
            "label": "vehiclefuellingdetails.create.vehicleType",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue": "",
            "maxLength": 128,
            "minLength": 1,
            "patternErrorMsg": "",
            "url": ""
          },
          {
            "name": "vehicleScheduledMaintenanceDate",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicleScheduledMaintenanceDate",
            "label": "swm.create.vehicleScheduledMaintenanceDate",
            "pattern": "",
            "hide": true,
            "type": "date",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue": "",
            "patternErrorMsg": "",
            "url": "",
            "isStateLevel":true
            
          },
          {
            "name": "actualMaintenanceDate",
            "jsonPath": "vehicleMaintenanceDetails[0].actualMaintenanceDate",
            "label": "swm.create.actualMaintenanceDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "costIncurred",
            "jsonPath": "vehicleMaintenanceDetails[0].costIncurred",
            "label": "swm.create.costIncurred",
            "pattern": "",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "insuranceValidityDate",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicle.insuranceDetails.insuranceValidityDate",
            "label": "swm.create.insuranceDetails.insuranceValidityDate",
            "pattern": "",
            "type": "date",
            "isRequired": false,
            "isDisabled": true,
            "hide": true,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "Downtimedefined",
            "jsonPath": "vehicleMaintenanceDetails[0].downtime",
            "label": "swm.create.DowntimeDefined",
            "pattern": "",
            "type": "text",
            "hide": true,
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": "",
            "url": ""
          },
          {
            "name": "vehicleReadingDuringMaintenance",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicleReadingDuringMaintenance",
            "label": "swm.create.vehicleReadingDuringMaintenance",
            "pattern": "",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "vehicleDowntimeActual",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicleDowntimeActual",
            "label": "swm.create.vehicleDowntimeActual",
            "pattern": "",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "vehicleDownTimeActualUom",
            "jsonPath": "vehicleMaintenanceDetails[0].vehicleDownTimeActualUom",
            "label": "swm.create.vehicleDownTimeActualUom",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": [
              {
                key: "Hrs",
                value: "Hrs"
              },
              {
                key: "Days",
                value: "Days"
            }],
            "patternErrorMsg": ""
          },
          {
            "name": "remarks",
            "jsonPath": "vehicleMaintenanceDetails[0].remarks",
            "label": "swm.create.remarks",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 300,
            "minLength": 15,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "url": "/swm-services/vehiclemaintenancedetails/_update",
    "tenantIdRequired": true,
    "searchUrl": "/swm-services/vehiclemaintenancedetails/_search?code={code}"
  }
}
export default dat;