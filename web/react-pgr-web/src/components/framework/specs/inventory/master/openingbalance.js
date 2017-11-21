var dat = {
  "inventory.search": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "",
    "url":"/inventory-services/openingbalance/_search",
    "groups": [
      {
        "name": "search",
        "label": "inventory.openingbalance.search.title",
        "fields": [
          {
            "name": "financialYear",
            "jsonPath": "financialYear",
            "label": "inventory.financialYear",
            "type": "singleValueList",
            "isDisabled": false,
            "defaultValue":[
              {key: null, value: "-- Please Select --"},
                    {"key":"2017","value":"2017"},
                       {"key":"2018", "value":"2018"},
                          {"key":"2019", "value":"2019"},
                             {"key":"2020", "value":"2020"},
                                {"key":"2021","value":"2021"}
                              ],
            "patternErrorMsg": "inventory.create.field.message.financialYear"
          },
          {
             "name":"store",
             "pattern":"",
             "label":"inventory.store.name",
             "type":"autoCompelete",
             "jsonPath":"storeName",
             "isKeyValuePair":true,
             "isRequired":false,
             "isDisabled":false,
             "url":"inventory-services/stores/_search?|$.stores[*].code|$.stores[*].name"
          },
          {
            "name": "materialTypeName",
            "jsonPath": "materialTypeName",
            "label": "inventory.materialTypeName",
            "type": "singleValueList",
            "isDisabled": false,
            "defaultValue":[
              {key: null, value: "-- Please Select --"},
                    {"key":"PURCHASE RECEIPT","value":"PURCHASE RECEIPT"},
                       {"key":"OPENING BALANCE", "value":"OPENING BALANCE"},
                          {"key":"INWARD RECEIPT", "value":"INWARD RECEIPT"},
                             {"key":"MISCELLANEOUS RECEIPT", "value":"MISCELLANEOUS RECEIPT"},
                              ],
            "patternErrorMsg": "inventory.create.field.message.materialTypeName"
          }
        ]
      }
    ],
    "result": {
      "header": [
        {
          "label": "inventory.financialYear"
        },
        {
          "label": "inventory.materialcode"
        },
        {
          "label": "inventory.Uom"
        },
        {
          "label": "inventory.receiptType"
        },
        {
          "label": "inventory.receiptDate"
        },
        {
          "label": "inventory.mrnNumber"
        },
        {
          "label": "inventory.qty"
        },
        {
          "label": "inventory.unitRate"
        },
        {
          "label": "inventory.totalamount"
        },
        {
          "label": "inventory.remarks"
        }
      ],
      "values": [
        "financialYear",
        "receiptDetails[0].material.code",
        "receiptDetails[0].uom.code",
        "receiptType",
        "receiptDate",
        "mrnNumber",
        "receiptDetails[0].receivedQty",
        "receiptDetails[0].unitRate",
        "totalamount",
        "remarks"
      ],
      "resultPath": "materialReceipt",
      "rowClickUrlUpdate": "/update/inventory/openingbalance/{receiptDetails[0].id}",
      "rowClickUrlView": "/view/inventory/openingbalance/{mrnNumber}"
    }
  },
  "inventory.view":{
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "",
    "url":"/inventory-services/openingbalance/_search",
    "groups": [
      {
        "name": "search",
        "label": "inventory.openingbalance.search.title",
        "fields": [
          {
            "name": "financialYear",
            "jsonPath": "financialYear",
            "label": "inventory.financialYear",
            "type": "singleValueList",
            "isDisabled": true,
            "defaultValue":[
                    {"key":"2017","value":"2017"},
                       {"key":"2018", "value":"2018"},
                          {"key":"2019","value":"2019"},
                             {"key":"2020","value":"2020"},
                                {"key":"2021", "value":"2021"}],
            "patternErrorMsg": "inventory.create.field.message.financialYear"
          },
          {
             "name":"store",
             "pattern":"",
             "label":"inventory.store.name",
             "type":"autoCompelete",
             "jsonPath":"store",
             "isKeyValuePair":true,
             "isRequired":false,
             "isDisabled":true,
             "url":"inventory-services/stores/_search?|$.stores[*].code|$.stores[*].name"
          },
          {
            "name": "receiptNumber",
            "jsonPath": "receiptNumber",
            "label": "inventory.receiptNumber",
            "type": "text",
            "isDisabled": true,
            "patternErrorMsg": "inventory.create.field.message.receiptNumber"
          },
          {
            "name": "materialName",
            "jsonPath": "materialName",
            "label": "inventory.materialName",
            "type": "text",
            "isDisabled": true,
            "patternErrorMsg": "inventory.create.field.message.materialName"
          }
        ]
      }
    ],
    "result": {
      "header": [
        {
          "label": "inventory.financialYear"
        },
        {
          "label": "inventory.materialcode"
        },
        {
          "label": "inventory.Uom"
        },
        {
          "label": "inventory.materialTypeName"
        },
        {
          "label": "inventory.receiptDate"
        },
        {
          "label": "inventory.mrnNumber"
        },
        {
          "label": "inventory.qty"
        },
        {
          "label": "inventory.unitRate"
        },
        {
          "label": "inventory.totalamount"
        },
        {
          "label": "inventory.remarks"
        }
      ],
      "values": [
        "financialYear",
        "receiptDetails[0].material.code",
        "receiptDetails[0].uom.code",
        "materialTypeName",
        "receiptDate",
        "mrnNumber",
        "receiptDetails[0].receivedQty",
        "receiptDetails[0].unitRate",
        "totalamount",
        "remarks"
      ],
      "resultPath": "openingbalance",
      "rowClickUrlUpdate": "/update/inventory/openingbalance/{mrnNumber}",
      "rowClickUrlView": "/view/inventory/openingbalance/{mrnNumber}"
    }
  },
  "inventory.create": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "materialReceipt",
    "groups": [
      {
        "name": "openingBalance",
        "label": "inventory.create.openingBalance.title",
        "fields": [
          {
            "name": "financialYear",
            "jsonPath": "materialReceipt[0].financialYear",
            "label": "inventory.financialYear",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
      "defaultValue":[
              {key: null, value: "-- Please Select --"},
                    {"key":"2017","value":"2017"},
                       {"key":"2018", "value":"2018"},
                          {"key":"2019", "value":"2019"},
                             {"key":"2020", "value":"2020"},
                                {"key":"2021","value":"2021"}
                        ],
            "patternErrorMsg": ""
          },
          {
            "name": "code",
            "jsonPath": "materialReceipt[0].receivingStore.code",
            "label": "inventory.store.name",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": "inventory.create.field.message.code",
            "url":"inventory-services/stores/_search?|$.stores[*].code|$.stores[*].name"
          }
        ]
      },

    {

             "name":"Opening Balance Details",
            "label":"inventory.openingbalance.opbdetails.title",
            "fields":[
               {
                  "type":"tableList",
                  "jsonPath":"",
                  "tableList":{
                     "header":[
                        {
                           "label":"inventory.materialName"
                        },
                        {
                           "label":"inventory.materialDesc"
                        },
                        {
                           "label":"inventory.Uom"
                        },
                         {
                           "label":"inventory.quantity"
                        },
                        {
                           "label": "inventory.Rate"
                        },
                        {
                           "label": "inventory.receiptNumber"
                        },
                        {
                           "label": "inventory.receiptDate"
                        },
                        {
                           "label":   "inventory.lotNumber"
                        },
                        {
                           "label":"inventory.expiryDate"
                        }

                     ],
                     "values":[

               {
                "name":"material",
                 "pattern":"",
                 "type":"singleValueList",
                 "jsonPath":"materialReceipt[0].receiptDetails[0].material.code",
                 "isRequired":true,
                 "isDisabled":false,
                 "url":"/egov-mdms-service/v1/_get?&moduleName=inventory&masterName=Material|$.MdmsRes.inventory.Material[*].code|$.MdmsRes.inventory.Material[*].name|$.MdmsRes.inventory.Material[*].description",
                 "depedants":[
                      {
                         "jsonPath":"materialReceipt[0].receiptDetails[0].material.description",
                         "type":"textField",
                         "valExp":"getValFromDropdownData('materialStoreMappings[*].material.code', getVal('materialStoreMappings[*].material.code'), 'others[0]')"
                      }
                    ]
               },
               {
                  "name":"materialDescription",
                  "jsonPath":"materialReceipt[0].receiptDetails[0].material.description",
                  "pattern":"",
                  "type":"text",
                  "isRequired":false,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
                  "name":"uom",
                  "jsonPath":"materialReceipt[0].receiptDetails[0].uom.code",
                  "pattern":"",
                  "type":"singleValueList",
                  "isRequired":true,
                  "isDisabled":false,
                   "url":"/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Uom|$..code|$..description"
               },

               {
                  "name":"receivedQty",
                  "jsonPath":"materialReceipt[0].receiptDetails[0].receivedQty",
                  "pattern":"",
                  "type":"number",
                  "isRequired":true,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               }, {
                  "name":"unitRate",
                  "jsonPath":"materialReceipt[0].receiptDetails[0].unitRate",
                  "pattern":"",
                  "type":"number",
                  "isRequired":true,
                  "isDisabled":false,
                  "defaultValue":"",
                  "maxLength":100,
                  "patternErrorMsg":"inventory.create.field.message.code"
               },
               {
            "name": "oldReceiptNumber",
            "jsonPath": "materialReceipt[0].receiptDetails[0].receiptDetailsAddnInfo[0].oldReceiptNumber",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "receivedDate",
            "jsonPath": "materialReceipt[0].receiptDetails[0].receiptDetailsAddnInfo[0].receivedDate",
             "pattern": "",
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "lotNo",
            "jsonPath": "materialReceipt[0].receiptDetails[0].receiptDetailsAddnInfo[0].lotNo",
             "pattern": "^[a-zA-Z0-9]+$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "expiryDate",
            "jsonPath": "materialReceipt[0].receiptDetails[0].receiptDetailsAddnInfo[0].expiryDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          }


     ]
                  }
               }
            ]
         }
    ],
    "url": "/inventory-services/openingbalance/_create",
    "tenantIdRequired": true
  },
  "inventory.update": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "materialReceipt",
    "url":"/inventory-services/openingbalance/_search",
    "title":"inventory.material.title",
    "groups": [
      {
        "name": "openingBalance",
        "label": "inventory.create.openingBalance.title",
        "fields": [
          {
            "name": "financialYear",
            "jsonPath": "materialReceipt[0].financialYear",
            "label": "inventory.financialYear",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
      "defaultValue":[
              {key: null, value: "-- Please Select --"},
                    {"key":"2017","value":"2017"},
                       {"key":"2018", "value":"2018"},
                          {"key":"2019", "value":"2019"},
                             {"key":"2020", "value":"2020"},
                                {"key":"2021","value":"2021"}
                        ],
            "patternErrorMsg": ""
          },
          {
            "name": "code",
            "jsonPath": "materialReceipt[0].receivingStore.code",
            "label": "inventory.store.name",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": "inventory.create.field.message.code",
            "url":"inventory-services/stores/_search?|$.stores[*].code|$.stores[*].name"
          }
        ]
      },

    {

             "name":"Opening Balance Details",
            "label":"inventory.openingbalance.opbdetails.title",
            "fields":[
               {
                  "type":"tableList",
                  "jsonPath":"",
                  "tableList":{
                     "header":[
                        {
                           "label":"inventory.materialName"
                        },
                        {
                           "label":"inventory.materialDesc"
                        },
                        {
                           "label":"inventory.Uom"
                        },
                         {
                           "label":"inventory.quantity"
                        },
                        {
                           "label": "inventory.Rate"
                        },
                        {
                           "label": "inventory.receiptNumber"
                        },
                        {
                           "label": "inventory.receiptDate"
                        },
                        {
                           "label":   "inventory.lotNumber"
                        },
                        {
                           "label":"inventory.expiryDate"
                        }

                     ],
                     "values":[

               {
                "name":"material",
                 "pattern":"",
                 "type":"singleValueList",
                 "jsonPath":"materialReceipt[0].receiptDetails[0].material.code",
                 "isRequired":true,
                 "isDisabled":false,
                 "url":"/egov-mdms-service/v1/_get?&moduleName=inventory&masterName=Material|$.MdmsRes.inventory.Material[*].code|$.MdmsRes.inventory.Material[*].name|$.MdmsRes.inventory.Material[*].description",
                 "depedants":[
                      {
                         "jsonPath":"materialReceipt[0].receiptDetails[0].material.description",
                         "type":"textField",
                         "valExp":"getValFromDropdownData('materialStoreMappings[*].material.code', getVal('materialStoreMappings[*].material.code'), 'others[0]')"
                      }
                    ]
               },
               {
                  "name":"materialDescription",
                  "jsonPath":"materialReceipt[0].receiptDetails[0].material.description",
                  "pattern":"",
                  "type":"text",
                  "isRequired":false,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
                  "name":"uom",
                  "jsonPath":"materialReceipt[0].receiptDetails[0].uom.code",
                  "pattern":"",
                  "type":"singleValueList",
                  "isRequired":true,
                  "isDisabled":false,
                   "url":"/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Uom|$..code|$..description"
               },

               {
                  "name":"receivedQty",
                  "jsonPath":"materialReceipt[0].receiptDetails[0].receivedQty",
                  "pattern":"",
                  "type":"number",
                  "isRequired":true,
                  "isDisabled":false,
                  "defaultValue":"0",
                  "patternErrorMsg":""
               },
                {
                  "name":"unitRate",
                  "jsonPath":"materialReceipt[0].receiptDetails[0].unitRate",
                  "pattern":"",
                  "type":"number",
                  "isRequired":true,
                  "isDisabled":false,
                  "defaultValue":"0",
                  "maxLength":100,
                  "patternErrorMsg":"inventory.create.field.message.code"
               },
               {
                  "name":"receiptNumber",
                  "jsonPath":"materialReceipt[0].mrnNumber",
                  "pattern":"",
                  "type":"text",
                  "isRequired":true,
                  "isDisabled":true,
                  "defaultValue":"0",
                  "maxLength":100,
                  "patternErrorMsg":"inventory.create.field.message.code"
               },
          {
            "name": "receivedDate",
            "jsonPath": "materialReceipt[0].receiptDetails[0].receiptDetailsAddnInfo[0].receivedDate",
             "pattern": "",
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "lotNo",
            "jsonPath": "materialReceipt[0].receiptDetails[0].receiptDetailsAddnInfo[0].lotNo",
             "pattern": "^[a-zA-Z0-9]+$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "expiryDate",
            "jsonPath": "materialReceipt[0].receiptDetails[0].receiptDetailsAddnInfo[0].expiryDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          }


     ]
                  }
               }
            ]
         }
    ],
    "url": "/inventory-services/openingbalance/_update",
    "tenantIdRequired": true,
    "searchUrl":"/inventory-services/openingbalance/_search?code={mrnNumber}"

  }

}
 export default dat;
