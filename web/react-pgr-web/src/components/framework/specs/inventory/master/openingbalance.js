var dat = {
  "inventory.openingbalance.search.title": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "",
    "url": "/inventory-inventory/v110/openingbalance/_search",
    "groups": [
      {
        "name": "search",
        "label": "inventory.openingbalance.search.title",
        "fields": [
          {
            "name": "finanncilaYear",
            "jsonPath": "finanncilaYear",
            "label": "inventory.create.finanncilaYear",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.finanncilaYear"
          },
          {
            "name": "receiptNumber",
            "jsonPath": "receiptNumber",
            "label": "inventory.create.receiptNumber",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.receiptNumber"
          },
          {
            "name": "materialName",
            "jsonPath": "materialName",
            "label": "inventory.create.materialName",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.materialName"
          },
          {
            "name": "sortBy",
            "jsonPath": "sortBy",
            "label": "inventory.create.sortBy",
            "type": "text",
            "isDisabled": false,
            "defaultValue": "id",
            "patternErrorMsg": "inventory.create.field.message.sortBy"
          }
        ]
      }
    ],
    "result": {
      "header": [
        {
          "label": "inventory.search.result.materialReceipt[0].receivingStore.code"
        },
        {
          "label": "inventory.search.result.materialReceipt[0].receiptNumber"
        },
        {
          "label": "inventory.search.result.materialReceipt[0].materialName"
        }
      ],
      "values": [
        "materialReceipt[0].receivingStore.code",
        "materialReceipt[0].receiptNumber",
        "materialReceipt[0].materialName"
      ],
      "resultPath": "openingbalance",
      "rowClickUrlUpdate": "/update/openingbalance/{mrnNumber}",
      "rowClickUrlView": "/view/openingbalance/{mrnNumber}"
    }
  },
  "inventory.create": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "materialReceipt",
    "groups": [
      {
        "name": "openingBalance",
        "label": "inventory.create.group.title.openingBalance",
        "fields": [
          {
            "name": "financialYear",
            "jsonPath": "materialReceipt[0].financialYear",
            "label": "inventory.create.financialYear",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": [
              {
                "key": "2017",
                "value": "2017"
              }
            ],
            "patternErrorMsg": ""
          },
          {
            "name": "code",
            "jsonPath": "materialReceipt[0].receivingStore.code",
            "label": "inventory.create.group.store.name",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": "inventory.create.field.message.code",
            "url": "inventory-services/stores/_search?|$.stores[*].code|$.stores[*].name"
          }
        ]
      },
      {
        "name": "Opening Balance Details",
        "label": "inventory.create.group.openingbalance.title.opbdetails",
        "fields": [
          {
            "type": "tableList",
            "jsonPath": "",
            "tableList": {
              "header": [
                {
                  "label": "Material Name"
                },
                {
                  "label": "Material Descr."
                },
                {
                  "label": "Uom"
                },
                {
                  "label": "Quantity"
                },
                {
                  "label": "Rate"
                },
                {
                  "label": "Receipt Number"
                },
                {
                  "label": "Receipt Date"
                },
                {
                  "label": "Lot Number"
                },
                {
                  "label": "Expiry Date"
                }
              ],
              "values": [
                {
                  "name": "material",
                  "pattern": "",
                  "type":"autoCompelete",
                  "jsonPath": "materialReceipt[0].materialReceiptDetail[0].material.code",
		  "displayJsonPath":"materialReceipt[0].materialReceiptDetail[0].material.name",
                  "isRequired": true,
                  "isDisabled": false,
                  "url": "/egov-mdms-service/v1/_get?&moduleName=inventory&masterName=Material|$.MdmsRes.inventory.Material[*].code|$.MdmsRes.inventory.Material[*].name|$.MdmsRes.inventory.Material[*].description",
                  "depedants": [
                    {
                      "jsonPath": "materialReceipt[0].materialReceiptDetail[0].material.description",
                      "type": "textField",
                      "valExp": "getValFromDropdownData('materialStoreMappings[*].material.code', getVal('materialStoreMappings[*].material.code'), 'others[0]')"
                    }
                  ]
                },
                {
                  "name": "materialDescription",
                  "jsonPath": "materialReceipt[0].materialReceiptDetail[0].material.description",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": true,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "uom",
                  "jsonPath": "materialReceipt[0].materialReceiptDetail[0].uom.code",
                  "pattern": "",
                  "type": "singleValueList",
                  "isRequired": true,
                  "isDisabled": false,
                  "url": "/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Uom|$..code|$..description"
                },
                {
                  "name": "receivedQty",
                  "jsonPath": "materialReceipt[0].materialReceiptDetail[0].receivedQty",
                  "pattern": "",
                  "type": "number",
                  "isRequired": true,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "openingRate",
                  "jsonPath": "materialReceipt[0].materialReceiptDetail[0].openingRate",
                  "pattern": "",
                  "type": "number",
                  "isRequired": true,
                  "isDisabled": false,
                  "defaultValue": "",
                  "maxLength": 100,
                  "patternErrorMsg": "inventory.create.field.message.code"
                },
                {
                  "name": "oldReceiptNumber",
                  "jsonPath": "materialReceipt[0].materialReceiptDetail[0].receiptDetailsAddnInfo[0].oldReceiptNumber",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "receivedDate",
                  "jsonPath": "materialReceipt[0].materialReceiptDetail[0].receiptDetailsAddnInfo[0].receivedDate",
                  "pattern": "",
                  "type": "datePicker",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "lotNo",
                  "jsonPath": "materialReceipt[0].materialReceiptDetail[0].receiptDetailsAddnInfo[0].lotNo",
                  "pattern": "^[a-zA-Z ]+$",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "expiryDate",
                  "jsonPath": "materialReceipt[0].materialReceiptDetail[0].receiptDetailsAddnInfo[0].expiryDate",
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
    "url": "/inventory-inventory/v110/openingbalance/_create",
    "tenantIdRequired": true
  }
}
 export default dat;
