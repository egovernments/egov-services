var dat = {
  "inventory.search": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "",
    "url": "/inventory-services/pricelists/_search",
    "groups": [
      {
        "name": "search",
        "label": "inventory.pricelist.search.title",
        "fields": [
          {
            "name": "supplierName",
            "jsonPath": "supplierName",
            "label": "inventory.supplierName",
            "type": "singleValueList",
            "isDisabled": false,
      "url":"inventory-services/suppliers/_search?|$..code|$..name",
            "patternErrorMsg": "inventory.create.field.message.supplierName"
          },
          {
            "name": "rateContractNumber",
            "jsonPath": "rateContractNumber",
            "label": "inventory.rateContractNumber",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.rateContractNumber"
          },
          {
            "name": "agreementNumber",
            "jsonPath": "agreementNumber",
            "label": "inventory.agreementNumber",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.agreementNumber"
          },
          {
            "name": "rateContractDate",
            "jsonPath": "rateContractDate",
            "label": "inventory.rateContractDate",
            "type": "datePicker",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.rateContractDate"
          },
          {
            "name": "agreementDate",
            "jsonPath": "agreementDate",
            "label": "inventory.agreementDate",
           "type": "datePicker",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.agreementDate"
          },
          {
            "name": "agreementStartDate",
            "jsonPath": "agreementStartDate",
            "label": "inventory.agreementStartDate",
           "type": "datePicker",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.agreementStartDate"
          },
          {
            "name": "agreementEndDate",
            "jsonPath": "agreementEndDate",
            "label": "inventory.agreementEndDate",
            "type": "datePicker",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.agreementEndDate"
          },
          {
            "name": "rateType",
            "jsonPath": "rateType",
            "label": "inventory.rateType",
            "type": "singleValueList",
      "defaultValue":[
              {key: null, value: "-- Please Select --"},
              {
                 "key":"DGSC Rate Contract",
                 "value":"DGSC Rate Contract"
              },
              {
                 "key":"ULB Rate Contract",
                 "value":"ULB Rate Contract"
              },
              {
                 "key":"One Time Tender",
                 "value":"One Time Tender"
              },
              {
                 "key":"Quotation",
                 "value":"Quotation"
              }
            ],
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.rateType"
          }
        ]
      }
    ],
    "result": {
      "header": [
        {
          "label": "inventory.supplier"
        },
        {
          "label": "inventory.rateType"
        },
        {
          "label": "inventory.rateContractNumber"
        },
        {
          "label": "inventory.rateContractDate"
        },
        {
          "label": "inventory.agreementNumber"
        },
        {
          "label": "inventory.agreementDate"
        },
        {
          "label": "inventory.agreementStartDate"
        },
        {
          "label": "inventory.agreementEndDate"
        },
        {
          "label": "inventory.active"
        }
      ],
      "values": [
        "supplier.name",
        "rateType",
        "rateContractNumber",
        "rateContractDate",
        "agreementNumber",
        "agreementDate",
        "agreementStartDate",
        "agreementEndDate",
        "active"
      ],
      "resultPath": "priceLists",
      "resultIdKey":"rateContractNumber",
      "rowClickUrlUpdate": "/update/inventory/pricelists/{rateContractNumber}",
      "rowClickUrlView": "/view/inventory/pricelists/{rateContractNumber}",
      "rowClickUrlAdd" : "/create/inventory/pricelists",
      "rowClickUrlDelete" : {
        url:"inventory-services/pricelists/_update",
        body:{ active:false, inActiveDate:function(){ return new Date().getTime() } }
      }
    }
  },
  "inventory.create": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "priceLists",
    "groups": [
      {
        "name": "Group1",
        "label": "inventory.supplierDetail",
        "fields": [
          {
            "name": "supplier",
            "jsonPath": "priceLists[0].supplier.code",
            "label": "inventory.supplier",
            "url":"inventory-services/suppliers/_search?|$.suppliers[*].code|$.suppliers[*].name",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": "inventory.create.field.message.code"
          },
          {
            "name": "rateType",
            "jsonPath": "priceLists[0].rateType",
            "label": "inventory.rateType",
            "pattern": "",
            "type": "singleValueList",
      "defaultValue":[
              {key: null, value: "-- Please Select --"},
              {
                 "key":"DGSC Rate Contract",
                 "value":"DGSC Rate Contract"
              },
              {
                 "key":"ULB Rate Contract",
                 "value":"ULB Rate Contract"
              },
              {
                 "key":"One Time Tender",
                 "value":"One Time Tender"
              },
              {
                 "key":"Quotation",
                 "value":"Quotation"
              }
            ],
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "rateContractNumber",
            "jsonPath": "priceLists[0].rateContractNumber",
            "label": "inventory.rateContractNumber",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "rateContractDate",
            "jsonPath": "priceLists[0].rateContractDate",
            "label": "inventory.rateContractDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "agreementNumber",
            "jsonPath": "priceLists[0].agreementNumber",
            "label": "inventory.agreementNumber",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "agreementDate",
            "jsonPath": "priceLists[0].agreementDate",
            "label": "inventory.agreementDate",
            "pattern": "",
             "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "agreementStartDate",
            "jsonPath": "priceLists[0].agreementStartDate",
            "label": "inventory.agreementStartDate",
            "pattern": "",
             "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "agreementEndDate",
            "jsonPath": "priceLists[0].agreementEndDate",
            "label": "inventory.agreementEndDate",
            "pattern": "",
             "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "active",
            "jsonPath": "priceLists[0].active",
            "label": "inventory.active",
            "pattern": "",
              "type":"checkbox",
            "defaultValue":true,
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": true,
            "patternErrorMsg": ""
          },
     {
                  "name": "UploadDocument",
                  "jsonPath": "priceLists[0].fileStoreId",
      "label": "Upload Tender/Rate contract/Quotation",
                  "pattern": "",
                  "type": "singleFileUpload",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                }

        ]
      },
      {
        "name": "Rate contract Details",
        "label": "inventory.pricebdetails.title",
        "fields": [
          {
            "type": "tableList",
            "jsonPath": "priceLists[0].priceListDetails",
            "tableList": {
              "header": [
                {
                  "label": "inventory.materialName"
                },
                {
                  "label": "inventory.Uom"
                },
                {
                  "label":"inventory.Rate"
                },
                {
                  "label": "inventory.from.date"
                },
                {
                  "label": "inventory.TO.date"
                },
                {
                  "label": "inventory.quantity"
                },
                {
                  "label":"inventory.active"
                }
              ],
              "values": [
                {
                  "name": "material",
                  "pattern": "",
                  "type":"autoCompelete",
                  "jsonPath": "priceLists[0].priceListDetails[0].material.code",
                  "displayJsonPath":"priceLists[0].priceListDetails[0].material.name",
                  "isRequired": true,
                  "isDisabled": false,
                  "url": "/egov-mdms-service/v1/_get?&moduleName=inventory&masterName=Material|$.MdmsRes.inventory.Material[*].code|$.MdmsRes.inventory.Material[*].name|$.MdmsRes.inventory.Material[*].baseUom.code",
                  "depedants": [
                    {
                      "jsonPath": "priceLists[0].priceListDetails[0].uom.code",
                      "type": "textField",
                      "valExp": "getValFromDropdownData('priceLists[0].priceListDetails[*].material.code', getVal('priceLists[0].priceListDetails[*].material.code'), 'others[0]')"
                    },
                    {
                      "jsonPath": "priceLists[0].priceListDetails[0].uom.conversionFactor",
                      "type": "textField",
                      "valExp": "getValFromDropdownData('priceLists[0].priceListDetails[*].uom.code', getVal('priceLists[0].priceListDetails[*].uom.code'), 'others[0]')"
                    }
                  ]
                },
                {
                  "name": "uom",
                  "jsonPath": "priceLists[0].priceListDetails[0].uom.code",
                  "pattern": "",
                  "type": "singleValueList",
                  "isRequired": true,
                  "isDisabled": true,
                  "url": "/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Uom|$..code|$..description|$..conversionFactor",
                  "depedants": [
                    {
                      "jsonPath": "priceLists[0].priceListDetails[0].uom.conversionFactor",
                      "type": "textField",
                      "valExp": "getValFromDropdownData('priceLists[0].priceListDetails[*].uom.code', getVal('priceLists[0].priceListDetails[*].uom.code'), 'others[0]')"
                    }
                  ]
                },
                {
                  "name": "ratePerUnit",
                  "jsonPath": "priceLists[0].priceListDetails[0].ratePerUnit",
                  "pattern": "",
                  "type": "number",
                  "isRequired": true,
                  "isDisabled": false,
                  "defaultValue": "",
                  "maxLength": 100,
                  "patternErrorMsg": "inventory.create.field.message.code"
                },
                {
                  "name": "fromDate",
                  "jsonPath": "priceLists[0].priceListDetails[0].fromDate",
                  "pattern": "",
                  "type": "datePicker",
                  "isRequired": false,
                  "isDisabled": true,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "toDate",
                  "jsonPath": "priceLists[0].priceListDetails[0].toDate",
                  "pattern": "",
                  "type": "datePicker",
                  "isRequired": false,
                  "isDisabled": true,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "quantity",
                  "jsonPath": "priceLists[0].priceListDetails[0].quantity",
                  "pattern": "",
                  "type": "number",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "active",
                  "jsonPath": "priceLists[0].priceListDetails[0].active",
                  "label": "",
                  "pattern": "",
                  "type": "checkbox",
                  "isRequired": true,
                  "isDisabled": false,
                  "defaultValue": true,
                  "patternErrorMsg": ""
                }
              ]
            }
          }
        ]
      }
    ],
    "url": "/inventory-services/pricelists/_create",
    "tenantIdRequired": true
  },
  "inventory.view": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "priceLists",
    "groups": [
      {
        "name": "Group1",
        "label": "inventory.supplierDetail",
        "fields": [
          {
            "name": "code",
            "jsonPath": "priceLists[0].supplier.name",
            "label": "inventory.supplier",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": "inventory.create.field.message.code"
          },
          {
            "name": "rateType",
            "jsonPath": "priceLists[0].rateType",
            "label": "inventory.rateType",
            "pattern": "",
            "type": "singleValueList",
      "defaultValue":[
              {key: null, value: "-- Please Select --"},
              {
                 "key":"DGSC Rate Contract",
                 "value":"DGSC Rate Contract"
              },
              {
                 "key":"ULB Rate Contract",
                 "value":"ULB Rate Contract"
              },
              {
                 "key":"One Time Tender",
                 "value":"One Time Tender"
              },
              {
                 "key":"Quotation",
                 "value":"Quotation"
              }
            ],
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "rateContractNumber",
            "jsonPath": "priceLists[0].rateContractNumber",
            "label": "inventory.rateContractNumber",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "rateContractDate",
            "jsonPath": "priceLists[0].rateContractDate",
            "label": "inventory.rateContractDate",
            "pattern": "",
             "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "agreementNumber",
            "jsonPath": "priceLists[0].agreementNumber",
            "label": "inventory.agreementNumber",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "agreementDate",
            "jsonPath": "priceLists[0].agreementDate",
            "label": "inventory.agreementDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "agreementStartDate",
            "jsonPath": "priceLists[0].agreementStartDate",
            "label": "inventory.agreementStartDate",
            "pattern": "",
             "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "agreementEndDate",
            "jsonPath": "priceLists[0].agreementEndDate",
            "label": "inventory.agreementEndDate",
            "pattern": "",
          "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "active",
            "jsonPath": "priceLists[0].active",
            "label": "inventory.active",
            "pattern": "",
            "type": "checkbox",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": true,
            "patternErrorMsg": ""
          }
        ]
      },
      {
  "name": "Rate contract Details",
  "label": "inventory.pricebdetails.title",
  "fields": [
    {
      "type": "tableList",
      "jsonPath": "priceLists[0].priceListDetails",
      "tableList": {
        actionsNotRequired:true,
        "header": [
          {
            "label": "inventory.materialName"
          },
          {
            "label": "inventory.Uom"
          },
          {
            "label": "inventory.Rate"
          },
          {
            "label": "inventory.from.date"
          },
          {
            "label": "inventory.TO.date"
          },
          {
            "label": "inventory.quantity"
          },
          {
            "label": "inventory.active"
          }
        ],
        "values": [
          {
            "name": "material",
            "pattern": "",
            "type": "autoCompelete",
            "jsonPath": "priceLists[0].priceListDetails[0].material.code",
            "displayJsonPath": "priceLists[0].priceListDetails[0].material.name",
            "isRequired": true,
            "isDisabled": false,
            "url": "/egov-mdms-service/v1/_get?&moduleName=inventory&masterName=Material|$.MdmsRes.inventory.Material[*].code|$.MdmsRes.inventory.Material[*].name|$.MdmsRes.inventory.Material[*].description",
            "depedants": [
              {
                "jsonPath": "priceLists[0].priceListDetails[0].material.description",
                "type": "textField",
                "valExp": "getValFromDropdownData('materialStoreMappings[*].material.code', getVal('materialStoreMappings[*].material.code'), 'others[0]')"
              }
            ]
          },
          {
            "name": "uom",
            "jsonPath": "priceLists[0].priceListDetails[0].uom.code",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "url": "/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Uom|$..code|$..description|$..conversionFactor",
            "depedants": [
              {
                "jsonPath": "priceLists[0].priceListDetails[0].uom.conversionFactor",
                "type": "textField",
                "valExp": "getValFromDropdownData('priceLists[0].priceListDetails[*].uom.code', getVal('priceLists[0].priceListDetails[*].uom.code'), 'others[0]')"
              }
            ]
          },
          {
            "name": "ratePerUnit",
            "jsonPath": "priceLists[0].priceListDetails[0].ratePerUnit",
            "pattern": "",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 100,
            "patternErrorMsg": "inventory.create.field.message.code"
          },
          {
            "name": "fromDate",
            "jsonPath": "priceLists[0].priceListDetails[0].fromDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "toDate",
            "jsonPath": "priceLists[0].priceListDetails[0].toDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "quantity",
            "jsonPath": "priceLists[0].priceListDetails[0].quantity",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "active",
            "jsonPath": "priceLists[0].priceListDetails[0].active",
            "label": "",
            "pattern": "",
            "type": "checkbox",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": true,
            "patternErrorMsg": ""
          }
        ]
      }
    }
  ]
}
    ],
    "tenantIdRequired": true,
    "url": "/inventory-services/pricelists/_search?rateContractNumber={rateContractNumber}"
  },
  "inventory.update": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "priceLists",
    "groups": [
      {
        "name": "Group1",
        "label": "inventory.supplierDetail",
        "fields": [
          {
            "name": "code",
            "jsonPath": "priceLists[0].supplier.code",
            "label": "inventory.supplier",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": "inventory.create.field.message.code",
            "url":"inventory-services/suppliers/_search?|$..code|$..name"
          },
          {
            "name": "rateType",
            "jsonPath": "priceLists[0].rateType",
            "label": "inventory.rateType",
            "type": "singleValueList",
    "defaultValue":[
              {key: null, value: "-- Please Select --"},
              {
                 "key":"DGSC Rate Contract",
                 "value":"DGSC Rate Contract"
              },
              {
                 "key":"ULB Rate Contract",
                 "value":"ULB Rate Contract"
              },
              {
                 "key":"One Time Tender",
                 "value":"One Time Tender"
              },
              {
                 "key":"Quotation",
                 "value":"Quotation"
              }
            ],
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "rateContractNumber",
            "jsonPath": "priceLists[0].rateContractNumber",
            "label": "inventory.rateContractNumber",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "rateContractDate",
            "jsonPath": "priceLists[0].rateContractDate",
            "label": "inventory.rateContractDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "agreementNumber",
            "jsonPath": "priceLists[0].agreementNumber",
            "label": "inventory.agreementNumber",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "agreementDate",
            "jsonPath": "priceLists[0].agreementDate",
            "label": "inventory.agreementDate",
            "pattern": "",
             "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "agreementStartDate",
            "jsonPath": "priceLists[0].agreementStartDate",
            "label": "inventory.agreementStartDate",
            "pattern": "",
           "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "agreementEndDate",
            "jsonPath": "priceLists[0].agreementEndDate",
            "label": "inventory.agreementEndDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "active",
            "jsonPath": "priceLists[0].active",
            "label": "inventory.active",
            "pattern": "",
            "type": "checkbox",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": true,
            "patternErrorMsg": ""
          }
        ]
      },
      {
   "name":"Rate contract Details",
   "label":"inventory.pricebdetails.title",
   "fields":[
      {
         "type":"tableList",
         "jsonPath":"priceLists[0].priceListDetails",
         "tableList":{
            "header":[
               {
                  "label":"inventory.materialName"
               },
               {
                  "label":"inventory.Uom"
               },
               {
                  "label":"inventory.Rate"
               },
               {
                  "label":"inventory.from.date"
               },
               {
                  "label":"inventory.TO.date"
               },
               {
                  "label":"inventory.quantity"
               },
               {
                  "label":"inventory.active"
               }
            ],
            "values":[
               {
                  "name":"material",
                  "pattern":"",
                  "type":"singleValueList",
                  "jsonPath":"priceLists[0].priceListDetails[0].material.code",
                  "displayJsonPath":"priceLists[0].priceListDetails[0].material.name",
                  "isRequired":true,
                  "isDisabled":false,
                  "url":"/egov-mdms-service/v1/_get?&moduleName=inventory&masterName=Material|$.MdmsRes.inventory.Material[*].code|$.MdmsRes.inventory.Material[*].name|$.MdmsRes.inventory.Material[*].baseUom.code",
                  "depedants": [
                    {
                      "jsonPath": "priceLists[0].priceListDetails[0].uom.code",
                      "type": "textField",
                      "valExp": "getValFromDropdownData('priceLists[0].priceListDetails[*].material.code', getVal('priceLists[0].priceListDetails[*].material.code'), 'others[0]')"
                    },
                    {
                      "jsonPath": "priceLists[0].priceListDetails[0].uom.conversionFactor",
                      "type": "textField",
                      "valExp": "getValFromDropdownData('priceLists[0].priceListDetails[*].uom.code', getVal('priceLists[0].priceListDetails[*].uom.code'), 'others[0]')"
                    }
                  ]
               },
               {
                  "name":"uom",
                  "jsonPath":"priceLists[0].priceListDetails[0].uom.code",
                  "pattern":"",
                  "type":"singleValueList",
                  "isRequired":true,
                  "isDisabled":true,
                  "url":"/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Uom|$..code|$..description|$..description|$..conversionFactor1",
                  "depedants": [
                    {
                      "jsonPath": "priceLists[0].priceListDetails[0].uom.conversionFactor",
                      "type": "textField",
                      "valExp": "getValFromDropdownData('priceLists[0].priceListDetails[*].uom.code', getVal('priceLists[0].priceListDetails[*].uom.code'), 'others[0]')"
                    }
                  ]
               },
               {
                  "name":"ratePerUnit",
                  "jsonPath":"priceLists[0].priceListDetails[0].ratePerUnit",
                  "pattern":"",
                  "type":"number",
                  "isRequired":true,
                  "isDisabled":false,
                  "defaultValue":"",
                  "maxLength":100,
                  "patternErrorMsg":"inventory.create.field.message.code"
               },
               {
                  "name":"fromDate",
                  "jsonPath":"priceLists[0].priceListDetails[0].fromDate",
                  "pattern":"",
                  "type":"datePicker",
                  "isRequired":false,
                  "isDisabled":true,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
                  "name":"toDate",
                  "jsonPath":"priceLists[0].priceListDetails[0].toDate",
                  "pattern":"",
                  "type":"datePicker",
                  "isRequired":false,
                  "isDisabled":true,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
                  "name":"quantity",
                  "jsonPath":"priceLists[0].priceListDetails[0].quantity",
                  "pattern":"",
                  "type":"number",
                  "isRequired":false,
                  "isDisabled":false,
                  "defaultValue":"",
                  "patternErrorMsg":""
               },
               {
                  "name":"active",
                  "jsonPath":"priceLists[0].priceListDetails[0].active",
                  "label":"",
                  "pattern":"",
                  "type":"checkbox",
                  "isRequired":true,
                  "isDisabled":false,
                  "defaultValue":true,
                  "patternErrorMsg":""
               }
            ]
         }
      }
   ]
}
    ],
    "url": "/inventory-services/pricelists/_update",
    "tenantIdRequired": true,
    "searchUrl": "/inventory-services/pricelists/_search?rateContractNumber={rateContractNumber}"
  }
}
 export default dat;
