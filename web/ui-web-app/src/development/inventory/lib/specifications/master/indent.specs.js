var dat = {
  "inventory.search": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "",
    "url": "/inventory-services/indents/_search",
    "groups": [
      {
        "name": "search",
        "label": "inventory.search.title",
        "fields": [
					 {
            "name": "indentNumber",
            "jsonPath": "indentNumber",
            "label": "inventory.indent.number",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.indentNumber"
          },

         
          {
            "name": "indentDate",
            "jsonPath": "indentDate",
            "label": "inventory.indent.date",
            "type": "datePicker",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.indentDate"
          },
         
          {
            "name": "indentPurpose",
            "jsonPath": "indentPurpose",
            "label": "inventory.indent.purpose",
            "type": "singleValueList",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.indentPurpose",
						"defaultValue":[
              {key: null, value: "-- Please Select --"},
              {
                 "key":"Consumption",
                 "value":"Consumption"
              },
              {
                 "key":"Repairs and Maintenance",
                 "value":"Repairs and Maintenance"
              },
              {
                 "key":"Capital",
                 "value":"Capital"
              }
            ],
          },
           {
            "name": "inventoryType",
            "jsonPath": "inventoryType",
            "label": "inventory.inventory.type",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
						 "defaultValue":[ {key: null, value: "-- Please Select --"},
              {
                 "key":"Asset",
                 "value":"Asset"
              },
              {
                 "key":"Consumable",
                 "value":"Consumable"
              }
            ],

            "patternErrorMsg": ""
          },
				 {
            "name": "issueStore",
            "jsonPath": "issueStore",
            "label": "inventory.indenting.store",
            "type": "singleValueList",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.issueStore",
						"url":"inventory-services/stores/_search?|$.stores[*].code|$.stores[*].name"
          },
        ]
      }
    ],
    "result": {
      "header": [
        {
          "label": "inventory.indent.number"
        },
        {
          "label": "inventory.indent.date"
        },
 				{
          "label": "inventory.indenting.store"
        },
        {
          "label": "inventory.indent.purpose"
        },
         
        {
          "label": "inventory.indent.status"
        }
      ],
      "values": [
        "indentNumber",
        "indentDate",
				"issueStore.name",
        "indentPurpose",
        "status"
      ],

      "resultIdKey":"indentNumber",
      "resultPath": "indents",
      "rowClickUrlUpdate": "/update/inventory/indent/{indentNumber}",
      "rowClickUrlView": "/view/inventory/indent/{indentNumber}",
  		"rowClickUrlAdd" : "/create/inventory/indent"
    }
  },
  "inventory.create": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "indents",
    "groups": [
      {
        "name": "indent",
        "label": "inventory.create.group.title.indent",
        "fields": [
          {
            "name": "name",
            "jsonPath": "indents[0].issueStore.code",
            "label": "inventory.store.name",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": "inventory.create.field.message.store.name",
						"url":"inventory-services/stores/_search?|$.stores[*].code|$.stores[*].name"
          },
          {
            "name": "indentDate",
            "jsonPath": "indents[0].indentDate",
            "label": "inventory.indent.date",
            "pattern": "",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "indentNumber",
            "jsonPath": "indents[0].indentNumber",
            "label": "inventory.indent.number",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue": "",
            "maxLength": 64,
            "patternErrorMsg": ""
          },
          {
            "name": "indentPurpose",
            "jsonPath": "indents[0].indentPurpose",
            "label": "inventory.indent.purpose",
            "pattern": "",
            "type": "singleValueList",
						"defaultValue":[
              {key: null, value: "-- Please Select --"},
              {
                 "key":"Consumption",
                 "value":"Consumption"
              },
              {
                 "key":"Repairs and Maintenance",
                 "value":"Repairs and Maintenance"
              },
              {
                 "key":"Capital",
                 "value":"Capital"
              }
            ],
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },

          {
            "name": "indentType",
            "jsonPath": "indents[0].indentType",
            "label": "inventory.indent.type",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue":"Indent",
            "patternErrorMsg": ""
          },
          {
            "name": "inventoryType",
            "jsonPath": "indents[0].inventoryType",
            "label": "inventory.inventory.type",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
						 "defaultValue":[ {key: null, value: "-- Please Select --"},
              {
                 "key":"Asset",
                 "value":"Asset"
              },
              {
                 "key":"Consumable",
                 "value":"Consumable"
              }
            ],

            "patternErrorMsg": ""
          },
          {
            "name": "expectedDeliveryDate",
            "jsonPath": "indents[0].expectedDeliveryDate",
            "label": "inventory.expecteddeliverydate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "narration",
            "jsonPath": "indents[0].narration",
            "label": "inventory.narration",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 1000,
            "patternErrorMsg": ""
          },

          {
            "name": "materialHandOverTo",
            "jsonPath": "indents[0].materialHandOverTo",
            "label": "inventory.materialhandoverto",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 128,
            "patternErrorMsg": ""
          },
          {
            "name": "designation",
            "jsonPath": "indents[0].designation",
            "label": "inventory.designation",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          }

        ]
      },

    {

						 "name":"Indent Details",
            "label":"inventory.create.group.title.indentdetails",
            "fields":[
               {
                  "type":"tableList",
                  "jsonPath":"indents[0].indentDetails",
                  "tableList":{
                     "header":[
                        {
                           "label":"Material Name"
                        },
                        {
                           "label":"Material Descr."
                        },
                        {
                           "label":"Uom"
                        },
                        {
                           "label":"Asset Code"
                        },
                        {
                           "label":"Project Code"
                        },
												 {
                           "label":"Quantity Reqd."
                        }

                     ],
                     "values":[

               {
          			"name":"material",
                 "pattern":"",
                 "type":"singleValueList",
                 "jsonPath":"indents[0].indentDetails[0].material.code",
                 "isRequired":true,
                 "isDisabled":false,
                 "url":"/egov-mdms-service/v1/_get?&moduleName=inventory&masterName=Material|$.MdmsRes.inventory.Material[*].name|$.MdmsRes.inventory.Material[*].name|$.MdmsRes.inventory.Material[*].description",
                 "depedants":[
                      {
                         "jsonPath":"indents[0].indentDetails[0].material.description",
                         "type":"textField",
                         "valExp":"getValFromDropdownData('materialStoreMappings[*].material.code', getVal('materialStoreMappings[*].material.code'), 'others')"
                      }
                    ]
               },
               {
                  "name":"materialDescription",
                  "jsonPath":"indents[0].indentDetails[0].material.description",
                  "pattern":"",
                  "type":"text",
                  "isRequired":false,
                  "isDisabled":true,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
                  "name":"uom",
                  "jsonPath":"indents[0].indentDetails[0].uom.code",
                  "pattern":"",
                  "type":"singleValueList",
                  "isRequired":true,
                  "isDisabled":false,
                   "url":"/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Uom|$..code|$..description"
               },

               {
                  "name":"asset",
                  "jsonPath":"indents[0].indentDetails[0].asset.code",
                  "pattern":"",
                  "type":"text",
                  "isRequired":false,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               }, {
                  "name":"projectcode",
                  "jsonPath":"indents[0].indentDetails[0].projectCode.code",
                  "pattern":"[a-zA-Z0-9-\\\\]+",
                  "type":"text",
                  "isRequired":false,
                  "isDisabled":false,
                  "defaultValue":"",
                  "maxLength":100,
                  "patternErrorMsg":"inventory.create.field.message.code"
               },
               {
                  "name":"indentQuantity",
                  "jsonPath":"indents[0].indentDetails[0].indentQuantity",
                  "pattern":"",
                  "type":"number",
                  "isRequired":true,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               }
              ]
            }
           }
            ]
         }
      ],
    "url": "/inventory-services/indents/_create",
    "tenantIdRequired": true
  },
  "inventory.view": {

      "numCols": 4,
      "useTimestamp": true,
      "objectName": "indents",
      "groups": [
        {
          "name": "indent",
          "label": "inventory.create.group.title.indent",
          "fields": [
            {
              "name": "name",
              "jsonPath": "indents[0].issueStore.code",
              "label": "inventory.store.name",
              "type": "singleValueList",
              "isRequired": true,
              "isDisabled": false,
              "defaultValue": "",
              "patternErrorMsg": "inventory.create.field.message.store.name",
  						"url":"inventory-services/stores/_search?|$.stores[*].code|$.stores[*].name"
            },
            {
              "name": "indentDate",
              "jsonPath": "indents[0].indentDate",
              "label": "inventory.indent.date",
              "pattern": "",
              "type": "datePicker",
              "isRequired": true,
              "isDisabled": false,
              "defaultValue": "",
              "patternErrorMsg": ""
            },
            {
              "name": "indentNumber",
              "jsonPath": "indents[0].indentNumber",
              "label": "inventory.indent.number",
              "pattern": "",
              "type": "text",
              "isRequired": false,
              "isDisabled": true,
              "defaultValue": "",
              "maxLength": 64,
              "patternErrorMsg": ""
            },
            {
              "name": "indentPurpose",
              "jsonPath": "indents[0].indentPurpose",
              "label": "inventory.indent.purpose",
              "pattern": "",
              "type": "singleValueList",
  						"defaultValue":[
                {key: null, value: "-- Please Select --"},
                {
                   "key":"Consumption",
                   "value":"Consumption"
                },
                {
                   "key":"Repairs and Maintenance",
                   "value":"Repairs and Maintenance"
                },
                {
                   "key":"Capital",
                   "value":"Capital"
                }
              ],
              "isRequired": true,
              "isDisabled": false,
              "patternErrorMsg": ""
            },

           
            {
              "name": "inventoryType",
              "jsonPath": "indents[0].inventoryType",
              "label": "inventory.inventory.type",
              "pattern": "",
              "type": "singleValueList",
              "isRequired": true,
              "isDisabled": false,
  						 "defaultValue":[ {key: null, value: "-- Please Select --"},
                {
                   "key":"Asset",
                   "value":"Asset"
                },
                {
                   "key":"Consumable",
                   "value":"Consumable"
                }
              ],

              "patternErrorMsg": ""
            },
            {
              "name": "expectedDeliveryDate",
              "jsonPath": "indents[0].expectedDeliveryDate",
              "label": "inventory.expecteddeliverydate",
              "pattern": "",
              "type": "datePicker",
              "isRequired": false,
              "isDisabled": false,
              "defaultValue": "",
              "patternErrorMsg": ""
            },
            {
              "name": "narration",
              "jsonPath": "indents[0].narration",
              "label": "inventory.narration",
              "pattern": "",
              "type": "textarea",
              "isRequired": false,
              "isDisabled": false,
              "defaultValue": "",
              "maxLength": 1000,
              "patternErrorMsg": ""
            },

            {
              "name": "materialHandOverTo",
              "jsonPath": "indents[0].materialHandOverTo",
              "label": "inventory.materialhandoverto",
              "pattern": "",
              "type": "text",
              "isRequired": false,
              "isDisabled": false,
              "defaultValue": "",
              "maxLength": 128,
              "patternErrorMsg": ""
            },
            {
              "name": "designation",
              "jsonPath": "indents[0].designation",
              "label": "inventory.designation",
              "pattern": "",
              "type": "text",
              "isRequired": false,
              "isDisabled": false,
              "defaultValue": "",
              "patternErrorMsg": ""
            }

          ]
        },

      {

  					 "name":"Indent Details",
              "label":"inventory.create.group.title.indentdetails",
              "fields":[
                  {
                  "type":"tableList",
                  "jsonPath":"indents[0].indentDetails",
                  "tableList":{
                     "header":[
                        {
                           "label":"Material Name"
                        },
                        {
                           "label":"Material Descr."
                        },
                        {
                           "label":"Uom"
                        },
                        {
                           "label":"Asset Code"
                        },
                        {
                           "label":"Project Code"
                        },
												 {
                           "label":"Quantity Reqd."
                        }

                     ],
                     "values":[

               {
          			"name":"material",
                 "pattern":"",
                 "type":"singleValueList",
                 "jsonPath":"indents[0].indentDetails[0].material.code",
                 "isRequired":true,
                 "isDisabled":false,
                 "url":"/egov-mdms-service/v1/_get?&moduleName=inventory&masterName=Material|$.MdmsRes.inventory.Material[*].name|$.MdmsRes.inventory.Material[*].name|$.MdmsRes.inventory.Material[*].description",
                 "depedants":[
                      {
                         "jsonPath":"indents[0].indentDetails[0].material.description",
                         "type":"textField",
                         "valExp":"getValFromDropdownData('materialStoreMappings[*].material.code', getVal('materialStoreMappings[*].material.code'), 'others')"
                      }
                    ]
               },
               {
                  "name":"materialDescription",
                  "jsonPath":"indents[0].indentDetails[0].material.description",
                  "pattern":"",
                  "type":"text",
                  "isRequired":false,
                  "isDisabled":true,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
                  "name":"uom",
                  "jsonPath":"indents[0].indentDetails[0].uom.code",
                  "pattern":"",
                  "type":"singleValueList",
                  "isRequired":true,
                  "isDisabled":false,
                   "url":"/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Uom|$..code|$..description"
               },

               {
                  "name":"asset",
                  "jsonPath":"indents[0].indentDetails[0].asset.code",
                  "pattern":"",
                  "type":"text",
                  "isRequired":false,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               }, {
                  "name":"projectcode",
                  "jsonPath":"indents[0].indentDetails[0].projectCode.code",
                  "pattern":"[a-zA-Z0-9-\\\\]+",
                  "type":"text",
                  "isRequired":false,
                  "isDisabled":false,
                  "defaultValue":"",
                  "maxLength":100,
                  "patternErrorMsg":"inventory.create.field.message.code"
               },
               {
                  "name":"indentQuantity",
                  "jsonPath":"indents[0].indentDetails[0].indentQuantity",
                  "pattern":"",
                  "type":"number",
                  "isRequired":true,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               }


       ]
                    }
                 }
              ]
           }
        ],
      "tenantIdRequired": true,
       "url": "/inventory-services/indents/_search?indentNumber={indentNumber}"
  },
  "inventory.update": {
     "numCols": 4,
    "useTimestamp": true,
    "objectName": "indents",
    "groups": [
      {
        "name": "indent",
        "label": "inventory.create.group.title.indent",
        "fields": [
          {
            "name": "name",
            "jsonPath": "indents[0].issueStore.code",
            "label": "inventory.store.name",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": "inventory.create.field.message.store.name",
						"url":"inventory-services/stores/_search?|$.stores[*].code|$.stores[*].name"
          },
          {
            "name": "indentDate",
            "jsonPath": "indents[0].indentDate",
            "label": "inventory.indent.date",
            "pattern": "",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "indentNumber",
            "jsonPath": "indents[0].indentNumber",
            "label": "inventory.indent.number",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue": "",
            "maxLength": 64,
            "patternErrorMsg": ""
          },
          {
            "name": "indentPurpose",
            "jsonPath": "indents[0].indentPurpose",
            "label": "inventory.indent.purpose",
            "pattern": "",
            "type": "singleValueList",
						"defaultValue":[
              {key: null, value: "-- Please Select --"},
              {
                 "key":"Consumption",
                 "value":"Consumption"
              },
              {
                 "key":"Repairs and Maintenance",
                 "value":"Repairs and Maintenance"
              },
              {
                 "key":"Capital",
                 "value":"Capital"
              }
            ],
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },

          {
            "name": "indentType",
            "jsonPath": "indents[0].indentType",
            "label": "inventory.indent.type",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "defaultValue":"Indent",
            "patternErrorMsg": ""
          },
          {
            "name": "inventoryType",
            "jsonPath": "indents[0].inventoryType",
            "label": "inventory.inventory.type",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
						 "defaultValue":[ {key: null, value: "-- Please Select --"},
              {
                 "key":"Asset",
                 "value":"Asset"
              },
              {
                 "key":"Consumable",
                 "value":"Consumable"
              }
            ],

            "patternErrorMsg": ""
          },
          {
            "name": "expectedDeliveryDate",
            "jsonPath": "indents[0].expectedDeliveryDate",
            "label": "inventory.expecteddeliverydate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "narration",
            "jsonPath": "indents[0].narration",
            "label": "inventory.narration",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 1000,
            "patternErrorMsg": ""
          },

          {
            "name": "materialHandOverTo",
            "jsonPath": "indents[0].materialHandOverTo",
            "label": "inventory.materialhandoverto",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 128,
            "patternErrorMsg": ""
          },
          {
            "name": "designation",
            "jsonPath": "indents[0].designation",
            "label": "inventory.designation",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          }

        ]
      },

    {

						 "name":"Indent Details",
            "label":"inventory.create.group.title.indentdetails",
            "fields":[
               {
                  "type":"tableList",
                  "jsonPath":"indents[0].indentDetails",
                  "tableList":{
                     "header":[
                        {
                           "label":"Material Name"
                        },
                        {
                           "label":"Material Descr."
                        },
                        {
                           "label":"Uom"
                        },
                        {
                           "label":"Asset Code"
                        },
                        {
                           "label":"Project Code"
                        },
												 {
                           "label":"Quantity Reqd."
                        }

                     ],
                     "values":[

               {
          			"name":"material",
                 "pattern":"",
                 "type":"singleValueList",
                 "jsonPath":"indents[0].indentDetails[0].material.code",
                 "isRequired":true,
                 "isDisabled":false,
                 "url":"/egov-mdms-service/v1/_get?&moduleName=inventory&masterName=Material|$.MdmsRes.inventory.Material[*].name|$.MdmsRes.inventory.Material[*].name|$.MdmsRes.inventory.Material[*].description",
                 "depedants":[
                      {
                         "jsonPath":"indents[0].indentDetails[0].material.description",
                         "type":"textField",
                         "valExp":"getValFromDropdownData('materialStoreMappings[*].material.code', getVal('materialStoreMappings[*].material.code'), 'others')"
                      }
                    ]
               },
               {
                  "name":"materialDescription",
                  "jsonPath":"indents[0].indentDetails[0].material.description",
                  "pattern":"",
                  "type":"text",
                  "isRequired":false,
                  "isDisabled":true,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
                  "name":"uom",
                  "jsonPath":"indents[0].indentDetails[0].uom.code",
                  "pattern":"",
                  "type":"singleValueList",
                  "isRequired":true,
                  "isDisabled":false,
                   "url":"/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Uom|$..code|$..description"
               },

               {
                  "name":"asset",
                  "jsonPath":"indents[0].indentDetails[0].asset.code",
                  "pattern":"",
                  "type":"text",
                  "isRequired":false,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               }, {
                  "name":"projectcode",
                  "jsonPath":"indents[0].indentDetails[0].projectCode.code",
                  "pattern":"[a-zA-Z0-9-\\\\]+",
                  "type":"text",
                  "isRequired":false,
                  "isDisabled":false,
                  "defaultValue":"",
                  "maxLength":100,
                  "patternErrorMsg":"inventory.create.field.message.code"
               },
               {
                  "name":"indentQuantity",
                  "jsonPath":"indents[0].indentDetails[0].indentQuantity",
                  "pattern":"",
                  "type":"number",
                  "isRequired":true,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               }
              ]
            }
           }
            ]
         }
      ],
    "url": "/inventory-services/indents/_update",
    "tenantIdRequired": true,
    "searchUrl": "/inventory-services/indents/_search?indentNumber={indentNumber}"
  }
}
export default dat;
