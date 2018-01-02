var dat = {
   "swm.search":{
      "numCols":4,
      "useTimestamp":true,
      "objectName":"",
      "url":"/swm-services/vehicleschedules/_search",
      "groups":[
         {
            "name":"search",
            "label":"swm.create.page.title.VehicleSchedules",
            "fields":[
               {
                  "name":"scheduledFrom",
                  "jsonPath":"scheduledFrom",
                  "label":"swm.create.scheduledFrom",
                  "pattern":"",
                  "type":"datePicker",
                  "isRequired":false,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
                  "name":"scheduledTo",
                  "jsonPath":"scheduledTo",
                  "label":"swm.create.scheduledTo",
                  "pattern":"",
                  "type":"datePicker",
                  "isRequired":false,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
                 "name": "route",
                 "jsonPath": "name",
                  "label": "swm.create.route",
                  "type": "autoCompelete",
                  "isRequired": false,
                  "isDisabled": false,
                  "maxLength": 256,
                  "minLength": 1,
                  "patternErrorMsg": "",
                  "url": "swm-services/routes/_search?|$.routes.*.code|$.routes.*.name"
               },
               {
                 "name": "regNumber",
                 "jsonPath": "regNumber",
                  "label": "swm.create.regNumber",
                  "type": "autoCompelete",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "maxLength": 12,
                  "minLength": 6,
                  "patternErrorMsg": '',
                  "url": "swm-services/vehicles/_search?|$.vehicles.*.regNumber|$.vehicles.*.regNumber"
               },
               
            ]
         }
      ],
      "result":{
         "header":[
            {
               "label":"swm.create.scheduledFrom",
               "isDate": true
            },
            {
               "label":"swm.create.scheduledTo",
               "isDate": true
            },
            {
               "label": "swm.create.route"
            },
            {
               "label": "swm.create.regNumber"
            }
         ],
         "values":[
            "scheduledFrom",
            "scheduledTo",
            "route.name",
            "vehicle.regNumber"
         ],
         "resultPath":"vehicleSchedules",
         "rowClickUrlUpdate":"/update/swm/vehicleschedules/{transactionNo}",
         "rowClickUrlView":"/view/swm/vehicleschedules/{transactionNo}"
      }
   },
   "swm.create":{
      "numCols":4,
      "useTimestamp":true,
      "idJsonPath": 'vehicleSchedules[0].transactionNo',
      "objectName":"vehicleSchedules",
      "title": "swm.create.page.title.VehicleSchedules",
      "groups":[
         {
            "name":"createVehicleSchedules",
            "fields":[
               {
                  "name":"scheduledFrom",
                  "jsonPath":"vehicleSchedules[0].scheduledFrom",
                  "label":"swm.create.scheduledFrom",
                  "pattern":"",
                  "type":"datePicker",
                  "isRequired":true,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
                  "name":"scheduledTo",
                  "jsonPath":"vehicleSchedules[0].scheduledTo",
                  "label":"swm.create.scheduledTo",
                  "pattern":"",
                  "type":"datePicker",
                  "isRequired":true,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
            	  "name": "route",
            	  "jsonPath": "vehicleSchedules[0].route.code",
                  "label": "swm.create.route",
                  "type": "autoCompelete",
                  "isRequired": true,
                  "isDisabled": false,
                  "maxLength": 256,
                  "minLength": 1,
                  "patternErrorMsg": "",
                  "url": "swm-services/routes/_search?|$.routes.*.code|$.routes.*.name",
                  "autoCompleteDependancy": {
                    "autoCompleteUrl": "swm-services/routes/_search?code={vehicleSchedules[0].route.code}",
                    "autoFillFields": {
                       "vehicleSchedules[0].route.collectionType.name": "routes[0].collectionType.name",
                     },
                  },
          	   },
               {
                 "name": "regNumber",
                 "jsonPath": "vehicleSchedules[0].vehicle.regNumber",
                  "label": "swm.create.regNumber",
                  "type": "autoCompelete",
                  "isRequired": true,
                  "isDisabled": false,
                  "defaultValue": "",
                  "maxLength": 12,
                  "minLength": 6,
                  "patternErrorMsg": '',
                  "url": "swm-services/vehicles/_search?|$.vehicles.*.regNumber|$.vehicles.*.regNumber",
                  "autoCompleteDependancy": {
                    "autoCompleteUrl": "/swm-services/vehicles/_search?regNumber={vehicleSchedules[0].vehicle.regNumber}",
                    "autoFillFields": {
                       "vehicleSchedules[0].vehicle.vehicleType.code": "vehicles[0].vehicleType.name",
                     },
                  },
               },
               {
                  'name': 'vehicleType',
                  'jsonPath': 'vehicleSchedules[0].vehicle.vehicleType.code',
                  'label': 'swm.vehicles.create.vehicleType',
                  'pattern': '',
                  'type': 'text',
                  'isRequired': false,
                  'isDisabled': true,
                  'defaultValue': '',
                  'maxLength': 128,
                  'minLength': 1,
                  'patternErrorMsg': '',
                  'url': '',
               },
               {
                 "name": "collectionType",
                 "jsonPath": "vehicleSchedules[0].route.collectionType.name",
                  "label": "swm.create.collectionType",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": true,
                  'defaultValue': '',
                  "maxLength": 256,
                  "minLength": 1,
                  "patternErrorMsg": "",
               },
               {
                  "name":"targetedGarbage",
                  "jsonPath":"vehicleSchedules[0].targetedGarbage",
                  "label":"swm.create.targetedGarbage",
                  "pattern":"",
                  "type":"text",
                  "isRequired":true,
                  "isDisabled":false,
                  "defaultValue":"",
                  "maxLength": 256,
                  "minLength": 1,
                  "patternErrorMsg":""
               }
            ]
         }
      ],
      "url":"/swm-services/vehicleschedules/_create",
      "tenantIdRequired":true
   },
   "swm.view":{
      "numCols":4,
      "useTimestamp":true,
      "objectName":"vehicleSchedules",
      "idJsonPath": 'vehicleSchedules[0].code',
      "groups":[
         {
            "name":"viewVehicleSchedulesView",
            "label":"swm.create.page.title.VehicleSchedules",
            "fields":[
               {
                  "name":"scheduledFrom",
                  "jsonPath":"vehicleSchedules[0].scheduledFrom",
                  "label":"swm.create.scheduledFrom",
                  "pattern":"",
                  "type":"datePicker",
                  "isRequired":false,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
                  "name":"scheduledTo",
                  "jsonPath":"vehicleSchedules[0].scheduledTo",
                  "label":"swm.create.scheduledTo",
                  "pattern":"",
                  "type":"datePicker",
                  "isRequired":false,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
                  "name": "route",
                  "jsonPath": "vehicleSchedules[0].route.name",
                  "label": "swm.create.route",
                  "type": "autoCompelete",
                  "isRequired": false,
                  "isDisabled": false,
                  "maxLength": 256,
                  "minLength": 1,
                  "patternErrorMsg": "",
                  "url": ""
               },
               {
                  "name": "regNumber",
                  "jsonPath": "vehicleSchedules[0].vehicle.regNumber",
                  "label": "swm.create.regNumber",
                  "type": "autoCompelete",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "maxLength": 12,
                  "minLength": 6,
                  "patternErrorMsg": '',
                  "url": ""
               },
               {
                  "name": "vehicleType",
                  "jsonPath": "vehicleSchedules[0].vehicle.vehicleType.code",
                  "label": "swm.vehicles.create.vehicleType",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": true,
                  "defaultValue": "",
                  "maxLength": 128,
                  "minLength": 1,
                  "patternErrorMsg": "",
                  "url": "",
               },
               {
                  "name": "collectionType",
                  "jsonPath": "vehicleSchedules[0].route.collectionType.code",
                  "label": "swm.create.collectionType",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": true,
                  "defaultValue": "",
                  "maxLength": 256,
                  "minLength": 1,
                  "patternErrorMsg": "",
               },
               {
                  "name":"targetedGarbage",
                  "jsonPath":"vehicleSchedules[0].targetedGarbage",
                  "label":"swm.create.targetedGarbage",
                  "pattern":"",
                  "type":"number",
                  "isRequired":false,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               }
            ]
         }
      ],
      "tenantIdRequired":true,
      "url":"/swm-services/vehicleschedules/_search?transactionNo={transactionNo}",
   },
   "swm.update":{
      "numCols":4,
      "useTimestamp":true,
      "objectName":"vehicleSchedules",
      "idJsonPath": 'vehicleSchedules[0].code',
      "groups":[
         {
            "name":"updateVehicle",
            "label":"swm.create.page.title.VehicleSchedules",
            "fields":[
               {
                  "name":"scheduledFrom",
                  "jsonPath":"vehicleSchedules[0].scheduledFrom",
                  "label":"swm.create.scheduledFrom",
                  "pattern":"",
                  "type":"datePicker",
                  "isRequired":true,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
                  "name":"scheduledTo",
                  "jsonPath":"vehicleSchedules[0].scheduledTo",
                  "label":"swm.create.scheduledTo",
                  "pattern":"",
                  "type":"datePicker",
                  "isRequired":true,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
                  "name": "route",
                  "jsonPath": "vehicleSchedules[0].route.name",
                  "label": "swm.create.route",
                  "type": "autoCompelete",
                  "isRequired": false,
                  "isDisabled": false,
                  "maxLength": 256,
                  "minLength": 1,
                  "patternErrorMsg": "",
                  "url": "swm-services/routes/_search?|$.routes.*.code|$.routes.*.name",
                  "autoCompleteDependancy": {
                    "autoCompleteUrl": "swm-services/routes/_search?code={vehicleSchedules[0].route.code}",
                    "autoFillFields": {
                       "vehicleSchedules[0].route.collectionType.name": "routes[0].collectionType.name",
                     },
                  },
               },
               {
                  "name": "regNumber",
                  "jsonPath": "vehicleSchedules[0].vehicle.regNumber",
                  "label": "swm.create.regNumber",
                  "type": "autoCompelete",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "maxLength": 12,
                  "minLength": 6,
                  "patternErrorMsg": '',
                  "url": "swm-services/vehicles/_search?|$.vehicles.*.regNumber|$.vehicles.*.regNumber",
                  "autoCompleteDependancy": {
                    "autoCompleteUrl": "/swm-services/vehicles/_search?regNumber={vehicleSchedules[0].vehicle.regNumber}",
                    "autoFillFields": {
                       "vehicleSchedules[0].vehicle.vehicleType.code": "vehicles[0].vehicleType.name",
                     },
                  },
               },
               {
                  'name': 'vehicleType',
                  'jsonPath': 'vehicleSchedules[0].vehicle.vehicleType.code',
                  'label': 'swm.vehicles.create.vehicleType',
                  'pattern': '',
                  'type': 'text',
                  'isRequired': false,
                  'isDisabled': true,
                  'defaultValue': '',
                  'maxLength': 128,
                  'minLength': 1,
                  'patternErrorMsg': '',
                  'url': '',
               },
               {
                  "name": "collectionType",
                  "jsonPath": "vehicleSchedules[0].route.collectionType.name",
                  "label": "swm.create.collectionType",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": true,
                  'defaultValue': '',
                  "maxLength": 256,
                  "minLength": 1,
                  "patternErrorMsg": "",
               },
               {
                  "name":"targetedGarbage",
                  "jsonPath":"vehicleSchedules[0].targetedGarbage",
                  "label":"swm.create.targetedGarbage",
                  "pattern":"",
                  "type":"number",
                  "isRequired":true,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               }
            ]
         }
      ],
      "url":"/swm-services/vehicleschedules/_update",
      "tenantIdRequired":true,
      "searchUrl":"/swm-services/vehicleschedules/_search?transactionNo={transactionNo}",
   },
};
export default dat;