var dat = {
  "swm.search":{
     "numCols":4,
     "useTimestamp":true,
     "objectName":"",
     "url":"/swm-services/vehiclemaintenances/_search",
     "groups":[
        {
           "name":"search",
           "label":"swm.search.title",
           "fields":[
            {
              "name":"regNumber",
              "jsonPath":"vehicleMaintenances[0].vehicle.regNumber",
              "label":"swm.vehicles.create.regNumber",
              "pattern":"",
              "type":"autoCompelete",
              "isRequired":true,
              "isDisabled":false,
              "defaultValue":"",
              "maxLength":12,
              "minLength":6,
              "patternErrorMsg":"",
              "url": "swm-services/vehicles/_search?|$..regNumber|$..regNumber",
              "autoCompleteDependancy": {
                "autoCompleteUrl": "/swm-services/vehicles/_search?regNumber={vehicleMaintenances[0].vehicle.regNumber}",
                "autoFillFields": {
                  "vehicleMaintenances[0].vehicle.vehicleType.name": "vehicles[0].vehicleType.name"
                }
              }
            },
            {
               "name":"name",
               "jsonPath":"vehicleMaintenances[0].vehicle.vehicleType.code",
               "label":"swm.vehicles.create.vehicleType",
               "pattern":"",
               "type":"text",
               "isRequired":true,
               "isDisabled":false,
               "defaultValue":"",
               "maxLength":128,
               "minLength":1,
               "patternErrorMsg":"",
               "url": ""
            },
            {
               "name":"maintenanceAfter",
               "jsonPath":"vehicleMaintenances[0].maintenanceAfter",
               "label":"swm.create.maintenanceAfter",
               "pattern":"",
               "type":"number",
               "isRequired":true,
               "isDisabled":false,
               "defaultValue": "",
               "patternErrorMsg":""
            },
            {
               "name":"maintenanceUom",
               "jsonPath":"vehicleMaintenances[0].maintenanceUom",
               "label":"swm.create.maintenanceUom",
               "pattern":"",
               "type":"singleValueList",
               "isRequired":true,
               "isDisabled":false,
               "defaultValue": [
                  {
                    key: "Kms",
                    value: "Kms"
                  },
                  {
                    key: "Days",
                    value: "Days"
                }],
               "maxLength":5,
               "minLength":2,
               "patternErrorMsg":""
            },
            {
               "name":"downtimeforMaintenance",
               "jsonPath":"vehicleMaintenances[0].downtimeforMaintenance",
               "label":"swm.create.downtimeforMaintenance",
               "pattern":"",
               "type":"number",
               "isRequired":true,
               "isDisabled":false,
               "defaultValue":"",
               "patternErrorMsg":""
            },
            {
               "name":"downtimeforMaintenanceUom",
               "jsonPath":"vehicleMaintenances[0].downtimeforMaintenanceUom",
               "label":"swm.create.downtimeforMaintenanceUom",
               "pattern":"",
               "type":"singleValueList",
               "isRequired":true,
               "isDisabled":false,
               "defaultValue": [
                  {
                    key: "Hrs",
                    value: "Hrs"
                  },
                  {
                    key: "Days",
                    value: "Days"
                }],
               "maxLength":5,
               "minLength":3,
               "patternErrorMsg":""
            }
          ]
        }
     ],
     "result":{
        "header":[
           {
              "label":"swm.search.result.vehicleType"
           },
           {
              "label":"swm.search.result.regNumber"
           },
           {
              "label":"swm.search.result.maintenanceAfter"
           },
           {
              "label":"swm.search.result.downtimeforMaintenance"
           }
        ],
        "values":[
           "vehicleType",
           "regNumber",
           "maintenanceAfter",
           "downtimeforMaintenance"
        ],
        "resultPath":"vehiclemaintenances",
        "rowClickUrlUpdate":"/update/vehiclemaintenances/{regNumber}",
        "rowClickUrlView":"/view/vehiclemaintenances/{regNumber}"
     }
  },
  "swm.create":{
     "numCols":6,
     "useTimestamp":true,
     "objectName":"vehicleMaintenances",
     "title": "swm.create.page.title.VehicleMaintenanceDetails",
     "groups":[
        {
           "name":"VehicleMaintenanceDetails",
           "label":"swm.create.group.title.VehicleMaintenanceDetails",
           "fields":[
              {
                "name":"regNumber",
                "jsonPath":"vehicleMaintenances[0].vehicle.regNumber",
                "label":"swm.vehicles.create.regNumber",
                "pattern":"",
                "type":"autoCompelete",
                "isRequired":true,
                "isDisabled":false,
                "defaultValue":"",
                "maxLength":12,
                "minLength":6,
                "patternErrorMsg":"",
                "url": "swm-services/vehicles/_search?|$..regNumber|$..regNumber",
                "autoCompleteDependancy": {
                  "autoCompleteUrl": "/swm-services/vehicles/_search?regNumber={vehicleMaintenances[0].vehicle.regNumber}",
                  "autoFillFields": {
                    "vehicleMaintenances[0].vehicle.vehicleType.code": "vehicles[0].vehicleType.code"
                  }
                }
              },
              {
                 "name":"name",
                 "jsonPath":"vehicleMaintenances[0].vehicle.vehicleType.code",
                 "label":"swm.vehicles.create.vehicleType",
                 "pattern":"",
                 "type":"text",
                 "isRequired":true,
                 "isDisabled":false,
                 "defaultValue":"",
                 "maxLength":128,
                 "minLength":1,
                 "patternErrorMsg":"",
                 "url": ""
              },
              {
                 "name":"maintenanceAfter",
                 "jsonPath":"vehicleMaintenances[0].maintenanceAfter",
                 "label":"swm.create.maintenanceAfter",
                 "pattern":"",
                 "type":"number",
                 "isRequired":true,
                 "isDisabled":false,
                 "defaultValue": "",
                 "patternErrorMsg":""
              },
              {
                 "name":"maintenanceUom",
                 "jsonPath":"vehicleMaintenances[0].maintenanceUom",
                 "label":"swm.create.maintenanceUom",
                 "pattern":"",
                 "type":"singleValueList",
                 "isRequired":true,
                 "isDisabled":false,
                 "defaultValue": [
                    {
                      key: "Kms",
                      value: "Kms"
                    },
                    {
                      key: "Days",
                      value: "Days"
                  }],
                 "maxLength":5,
                 "minLength":2,
                 "patternErrorMsg":""
              },
              {
                 "name":"downtimeforMaintenance",
                 "jsonPath":"vehicleMaintenances[0].downtimeforMaintenance",
                 "label":"swm.create.downtimeforMaintenance",
                 "pattern":"",
                 "type":"number",
                 "isRequired":true,
                 "isDisabled":false,
                 "defaultValue":"",
                 "patternErrorMsg":""
              },
              {
                 "name":"downtimeforMaintenanceUom",
                 "jsonPath":"vehicleMaintenances[0].downtimeforMaintenanceUom",
                 "label":"swm.create.downtimeforMaintenanceUom",
                 "pattern":"",
                 "type":"singleValueList",
                 "isRequired":true,
                 "isDisabled":false,
                 "defaultValue": [
                    {
                      key: "Hrs",
                      value: "Hrs"
                    },
                    {
                      key: "Days",
                      value: "Days"
                  }],
                 "maxLength":5,
                 "minLength":3,
                 "patternErrorMsg":""
              }
           ]
        }
     ],
     "url":"/swm-services/vehiclemaintenances/_create",
     "tenantIdRequired":true
  },
  "swm.view":{
     "numCols":4,
     "useTimestamp":true,
     "objectName":"vehicleMaintenances",
     "groups":[
        {
           "name":"VehicleMaintenanceDetails",
           "label":"swm.create.group.title.VehicleMaintenanceDetails",
           "fields":[
            {
              "name":"regNumber",
              "jsonPath":"vehicleMaintenances[0].vehicle.regNumber",
              "label":"swm.vehicles.create.regNumber",
              "pattern":"",
              "type":"autoCompelete",
              "isRequired":true,
              "isDisabled":false,
              "defaultValue":"",
              "maxLength":12,
              "minLength":6,
              "patternErrorMsg":"",
              "url": "swm-services/vehicles/_search?|$..regNumber|$..regNumber",
              "autoCompleteDependancy": {
                "autoCompleteUrl": "/swm-services/vehicles/_search?regNumber={vehicleMaintenances[0].vehicle.regNumber}",
                "autoFillFields": {
                  "vehicleMaintenances[0].vehicle.vehicleType.name": "vehicles[0].vehicleType.name"
                }
              }
            },
            {
               "name":"name",
               "jsonPath":"vehicleMaintenances[0].vehicle.vehicleType.code",
               "label":"swm.vehicles.create.vehicleType",
               "pattern":"",
               "type":"text",
               "isRequired":true,
               "isDisabled":false,
               "defaultValue":"",
               "maxLength":128,
               "minLength":1,
               "patternErrorMsg":"",
               "url": ""
            },
            {
               "name":"maintenanceAfter",
               "jsonPath":"vehicleMaintenances[0].maintenanceAfter",
               "label":"swm.create.maintenanceAfter",
               "pattern":"",
               "type":"number",
               "isRequired":true,
               "isDisabled":false,
               "defaultValue": "",
               "patternErrorMsg":""
            },
            {
               "name":"maintenanceUom",
               "jsonPath":"vehicleMaintenances[0].maintenanceUom",
               "label":"swm.create.maintenanceUom",
               "pattern":"",
               "type":"singleValueList",
               "isRequired":true,
               "isDisabled":false,
               "defaultValue": [
                  {
                    key: "Kms",
                    value: "Kms"
                  },
                  {
                    key: "Days",
                    value: "Days"
                }],
               "maxLength":5,
               "minLength":2,
               "patternErrorMsg":""
            },
            {
               "name":"downtimeforMaintenance",
               "jsonPath":"vehicleMaintenances[0].downtimeforMaintenance",
               "label":"swm.create.downtimeforMaintenance",
               "pattern":"",
               "type":"number",
               "isRequired":true,
               "isDisabled":false,
               "defaultValue":"",
               "patternErrorMsg":""
            },
            {
               "name":"downtimeforMaintenanceUom",
               "jsonPath":"vehicleMaintenances[0].downtimeforMaintenanceUom",
               "label":"swm.create.downtimeforMaintenanceUom",
               "pattern":"",
               "type":"singleValueList",
               "isRequired":true,
               "isDisabled":false,
               "defaultValue": [
                  {
                    key: "Hrs",
                    value: "Hrs"
                  },
                  {
                    key: "Days",
                    value: "Days"
                }],
               "maxLength":5,
               "minLength":3,
               "patternErrorMsg":""
            }
          ]
        }
     ],
     "tenantIdRequired":true,
     "url":"/swm-servicesundefined"
  },
  "swm.update":{
     "numCols":4,
     "useTimestamp":true,
     "objectName":"vehicleMaintenances",
     "groups":[
        {
           "name":"VehicleMaintenanceDetails",
           "label":"swm.create.group.title.VehicleMaintenanceDetails",
           "fields":[
            {
              "name":"regNumber",
              "jsonPath":"vehicleMaintenances[0].vehicle.regNumber",
              "label":"swm.vehicles.create.regNumber",
              "pattern":"",
              "type":"autoCompelete",
              "isRequired":true,
              "isDisabled":false,
              "defaultValue":"",
              "maxLength":12,
              "minLength":6,
              "patternErrorMsg":"",
              "url": "swm-services/vehicles/_search?|$..regNumber|$..regNumber",
              "autoCompleteDependancy": {
                "autoCompleteUrl": "/swm-services/vehicles/_search?regNumber={vehicleMaintenances[0].vehicle.regNumber}",
                "autoFillFields": {
                  "vehicleMaintenances[0].vehicle.vehicleType.name": "vehicles[0].vehicleType.name"
                }
              }
            },
            {
               "name":"name",
               "jsonPath":"vehicleMaintenances[0].vehicle.vehicleType.code",
               "label":"swm.vehicles.create.vehicleType",
               "pattern":"",
               "type":"text",
               "isRequired":true,
               "isDisabled":false,
               "defaultValue":"",
               "maxLength":128,
               "minLength":1,
               "patternErrorMsg":"",
               "url": ""
            },
            {
               "name":"maintenanceAfter",
               "jsonPath":"vehicleMaintenances[0].maintenanceAfter",
               "label":"swm.create.maintenanceAfter",
               "pattern":"",
               "type":"number",
               "isRequired":true,
               "isDisabled":false,
               "defaultValue": "",
               "patternErrorMsg":""
            },
            {
               "name":"maintenanceUom",
               "jsonPath":"vehicleMaintenances[0].maintenanceUom",
               "label":"swm.create.maintenanceUom",
               "pattern":"",
               "type":"singleValueList",
               "isRequired":true,
               "isDisabled":false,
               "defaultValue": [
                  {
                    key: "Kms",
                    value: "Kms"
                  },
                  {
                    key: "Days",
                    value: "Days"
                }],
               "maxLength":5,
               "minLength":2,
               "patternErrorMsg":""
            },
            {
               "name":"downtimeforMaintenance",
               "jsonPath":"vehicleMaintenances[0].downtimeforMaintenance",
               "label":"swm.create.downtimeforMaintenance",
               "pattern":"",
               "type":"number",
               "isRequired":true,
               "isDisabled":false,
               "defaultValue":"",
               "patternErrorMsg":""
            },
            {
               "name":"downtimeforMaintenanceUom",
               "jsonPath":"vehicleMaintenances[0].downtimeforMaintenanceUom",
               "label":"swm.create.downtimeforMaintenanceUom",
               "pattern":"",
               "type":"singleValueList",
               "isRequired":true,
               "isDisabled":false,
               "defaultValue": [
                  {
                    key: "Hrs",
                    value: "Hrs"
                  },
                  {
                    key: "Days",
                    value: "Days"
                }],
               "maxLength":5,
               "minLength":3,
               "patternErrorMsg":""
            }
          ]
        }
     ],
     "url":"/swm-services/vehiclemaintenances/_update",
     "tenantIdRequired":true,
     "searchUrl":"/swm-servicesundefined"
  }
}
 export default dat;