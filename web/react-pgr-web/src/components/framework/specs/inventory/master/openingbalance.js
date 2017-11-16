var dat = {
  "inventory.search": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "",
    "url": "/inventory-services/openingbalance/_search",
    "groups": [
      {
        "name": "search",
        "label": "inventory.openingbalance.search.title",
        "fields": [
          {
            "name": "financialYear",
            "jsonPath": "financialYear",
            "label": "inventory.financialYear",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.financialYear"
          },
          {
            "name": "receiptNumber",
            "jsonPath": "receiptNumber",
            "label": "inventory.receiptNumber",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.receiptNumber"
          },
          {
            "name": "materialName",
            "jsonPath": "materialName",
            "label": "inventory.materialName",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.materialName"
          }
        ]
      }
    ],
    "result": {
      "header": [
        {
          "label": "inventory.receivingStore"
        },
        {
          "label": "inventory.mrnNumber"
        },
        {
          "label": "inventory.materialName"
        }
      ],
      "values": [
        "receivingStore.code",
	"mrnNumber",
	"materialName"
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
              {"key":"2017",
                 "value":"2017"}],
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
                           "label":	"inventory.Rate"
                        },
                        {
                           "label": "inventory.receiptNumber"
                        },
                        {
                           "label": "inventory.receiptDate"
                        },
                        {
                           "label": 	"inventory.lotNumber"
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
                  "isDisabled":true,
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
             "pattern": "^[a-zA-Z ]+$",
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
  }
  
}
 export default dat;
