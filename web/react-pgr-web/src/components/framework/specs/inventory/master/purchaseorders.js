var dat = {
  "inventory.search": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "",
    "url": "/inventory-services/purchaseorders/_search",
    "groups": [
      {
        "name": "search",
        "label": "nventory.purchaseorder.search.group.title",
        "fields": [
          {
            "name": "ids",
            "jsonPath": "ids",
            "label": "purchaseorder.create.ids",
            "type": "",
            "isDisabled": false,
            "patternErrorMsg": "purchaseorder.create.field.message.ids"
          },
          {
            "name": "store",
            "jsonPath": "store",
            "label": "inventory.store.name",
            "type": "number",
            "isDisabled": false,
            "patternErrorMsg": "purchaseorder.create.field.message.store"
          },
          {
            "name": "purchaseOrderNumber",
            "jsonPath": "purchaseOrderNumber",
            "label":  "inventory.purchaseorder.number",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "purchaseorder.create.field.message.purchaseOrderNumber"
          },
          {
            "name": "purchaseOrderDate",
            "jsonPath": "purchaseOrderDate",
            "label":  "inventory.purchaseorder.date",
            "type": "number",
            "isDisabled": false,
            "patternErrorMsg": "purchaseorder.create.field.message.purchaseOrderDate"
          },
          {
            "name": "rateType",
            "jsonPath": "rateType",
            "label": "inventory.rateType",
            "type": "singleValueList",
            "isDisabled": false,
            "patternErrorMsg": "purchaseorder.create.field.message.rateType"
          },
          {
            "name": "supplierCode",
            "jsonPath": "supplierCode",
            "label": "inventory.supplier.name",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "purchaseorder.create.field.message.supplierCode"
          },
          {
            "name": "status",
            "jsonPath": "status",
            "label": "inventory.purchaseorder.status",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "purchaseorder.create.field.message.status"
          }
        ]
      }
    ],
    "result": {
      "header": [
        {
          "label": "inventory.purchaseorder.number"
        },
        {
          "label": "inventory.purchaseorder.date"
        },
        {
          "label": "inventory.store.name"
        }
      ],
      "values": [
        "purchaseOrders[0].purchaseOrderNumber",
        "purchaseOrders[0].purchaseOrderDate",
        "purchaseOrders[0].store.code"
      ],
      "resultPath": "purchaseOrders",
      "rowClickUrlUpdate": "/update/inventory/purchaseorders/{purchaseOrderNumber}",
      "rowClickUrlView": "/view/inventory/purchaseorders/{purchaseOrderNumber}"
    }
  },
  "inventory.create": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "purchaseOrders",
    "groups": [
      {
        "name": "Group1",
        "label": "inventory.purchaseorder.create.group.title",
        "fields": [
          {
            "name": "code",
            "jsonPath": "purchaseOrders[0].store.code",
            "label": "inventory.store.name",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": "purchaseorder.create.field.message.code",
            "url": "/inventory-services/stores/_search?|$..code|$..name"
          },
          {
            "name": "purchaseOrderNumber",
            "jsonPath": "purchaseOrders[0].purchaseOrderNumber",
            "label": "inventory.purchaseorder.number",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue": "",
            "patternErrorMsg": "",
            "isHidden":true
          },
          {
            "name": "purchaseOrderDate",
            "jsonPath": "purchaseOrders[0].purchaseOrderDate",
            "label": "inventory.purchaseorder.date",
            "pattern": "",
            "type": "datePicker",
            "maxDate":"today",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "rateType",
            "jsonPath": "purchaseOrders[0].rateType",
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
            "name": "code",
            "jsonPath": "purchaseOrders[0].supplier.code",
            "label":  "inventory.supplier.name",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": "purchaseorder.create.field.message.code",
            "url": "/inventory-services/suppliers/_search?|$..code|$..name"
          },
          {
            "name": "advanceAmount",
            "jsonPath": "purchaseOrders[0].advanceAmount",
            "label": "inventory.advanceAmount",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 10,
            "patternErrorMsg": ""
          },
          {
            "name": "advancePercentage",
            "jsonPath": "purchaseOrders[0].advancePercentage",
            "label":  "inventory.advancePercentage",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 3,
            "patternErrorMsg": ""
          },
          {
            "name": "expectedDeliveryDate",
            "jsonPath": "purchaseOrders[0].expectedDeliveryDate",
            "label":"inventory.expectedDeliveryDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "deliveryTerms",
            "jsonPath": "purchaseOrders[0].deliveryTerms",
             "label":"inventory.deliveryTerms",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 512,
            "patternErrorMsg": ""
          },
          {
            "name": "paymentTerms",
            "jsonPath": "purchaseOrders[0].paymentTerms",
            "label":"inventory.paymentTerms",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 512,
            "patternErrorMsg": ""
          },
          {
            "name": "remarks",
            "jsonPath": "purchaseOrders[0].remarks",
            "label":  "inventory.remarks",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 1000,
            "patternErrorMsg": ""
          },
          {
            "name": "designation",
            "jsonPath": "purchaseOrders[0].designation",
            "label": "inventory.designation",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue": "",
            "maxLength": 128,
            "patternErrorMsg": ""
          },
          {
            "type": "tableList",
            "jsonPath": "purchaseOrders[0].purchaseOrderDetails",
            "tableList": {
              "header": [
                {
                  "label": "inventory.materialName"
                },
                {
                  "label": "inventory.indent.number"
                },
                {
                  "label": "inventory.materialDesc"
                },
                {
                  "label": "inventory.totalindent.quantity"
                },
                {
                  "label": "inventory.Uom"
                },
                {
                  "label": "inventory.ratecontract"
                },
                {
                  "label": "inventory.orderqty"
                },
                {
                  "label": "inventory.unitRate"
                },
                {
                  "label": "reports.inventory.openbal.TotalAmount"
                },
                {
                  "label": "inventory.TenderQuantity"
                },
                {
                  "label": "inventory.usedQuantity"
                }
              ],
              "values": [
                {
                  "name": "material",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].material.code",
                  "label": "",
                  "pattern": "",
                  "type": "autoCompelete",
                  "isRequired": true,
                  "isDisabled": false,
                  "defaultValue": "",
                  "maxLength": 50,
                  "patternErrorMsg": "",
                  "url": "/egov-mdms-service/v1/_get?&moduleName=inventory&masterName=Material|$..code|$..name|$..description|$..purchaseUom.code",
                  "depedants": [
                    {
                      "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].material.description",
                      "type": "textField",
                      "valExp": "getValFromDropdownData('purchaseOrders[0].purchaseOrderDetails[*].material.code', getVal('purchaseOrders[0].purchaseOrderDetails[*].material.code'), 'others[0]')"
                    },
                    {
                      "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].uom.code",
                      "type": "textField",
                      "valExp": "getValFromDropdownData('purchaseOrders[0].purchaseOrderDetails[*].material.code', getVal('purchaseOrders[0].purchaseOrderDetails[*].material.code'), 'others[1]')"
                    }
                    // {
                    //   "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].uom.conversionFactor",
                    //   "type": "textField",
                    //   "valExp": "getValFromDropdownData('purchaseOrders[0].purchaseOrderDetails[*].uom.code', getVal('purchaseOrders[0].purchaseOrderDetails[*].uom.code'), 'others[0]')"
                    // }
                  ]
                },
                {
                  "name": "indentNumber",
                  "jsonPath": "purchaseOrders[0].purchaseIndentDetails[0].purchaseIndentDetails[0].indentDetail[0].indentNumber",
                  "label": "",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": true,
                  "defaultValue": "",
                  "maxLength": 50,
                  "minLength": 5,
                  "patternErrorMsg": ""
                },
                {
                  "name": "materialDescription",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].material.description",
                  "label": "",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "maxLength": 50,
                  "minLength": 5,
                  "patternErrorMsg": ""
                },
                {
                  "name": "totalIndentQuantity",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].purchaseIndentDetails[0].quantity",
                  "label": "",
                  "pattern": "",
                  "type": "number",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "uom",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].uom.code",
                  "label":"",
                  "pattern": "",
                  "type": "singleValueList",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": "",
                  "url": "/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Uom|$..code|$..description"
                },
                {
                  "name": "rateContractNumber",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].priceList.rateContractNumber",
                  "label": "",
                  "pattern": "",
                  "type": "singleValueList",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "orderQuantity",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].orderQuantity",
                  "label": "",
                  "pattern": "",
                  "type": "number",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "unitPrice",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].unitPrice",
                  "label": "",
                  "pattern": "",
                  "type": "number",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "TotalAmount",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].totalAmount",
                  "label": "",
                  "pattern": "",
                  "type": "number",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "tenderQuantity",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].tenderQuantity",
                  "label": "",
                  "pattern": "",
                  "type": "number",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "usedQuantity",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].usedQuantity",
                  "label": "",
                  "pattern": "",
                  "type": "number",
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
    "url": "/inventory-services/v110/purchaseorders/_create",
    "onloadFetchUrl":"/inventory-services/purchaseorders/_preparepofromindents",
    "tenantIdRequired": true
  },
  "inventory.view": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "purchaseOrders",
    "groups": [
      {
        "name": "Group1",
        "label": "purchaseorder.create.group.title.Group1",
        "fields": [
          {
            "name": "code",
            "jsonPath": "purchaseOrders[0].store.code",
            "label": "purchaseorder.create.store.code",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": "purchaseorder.create.field.message.code",
            "url": "/inventory-services/stores/_search?|$..code|$..name"
          },
          {
            "name": "purchaseOrderNumber",
            "jsonPath": "purchaseOrders[0].purchaseOrderNumber",
            "label": "purchaseorder.create.purchaseOrderNumber",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "purchaseOrderDate",
            "jsonPath": "purchaseOrders[0].purchaseOrderDate",
            "label": "purchaseorder.create.purchaseOrderDate",
            "pattern": "",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "purchaseType",
            "jsonPath": "purchaseOrders[0].purchaseType",
            "label": "purchaseorder.create.purchaseType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "rateType",
            "jsonPath": "purchaseOrders[0].rateType",
            "label": "purchaseorder.create.rateType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "code",
            "jsonPath": "purchaseOrders[0].supplier.code",
            "label": "purchaseorder.create.supplier.code",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": "purchaseorder.create.field.message.code",
            "url": "/inventory-services/suppliers/_search?|$..code|$..name"
          },
          {
            "name": "advanceAmount",
            "jsonPath": "purchaseOrders[0].advanceAmount",
            "label": "purchaseorder.create.advanceAmount",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "advancePercentage",
            "jsonPath": "purchaseOrders[0].advancePercentage",
            "label": "purchaseorder.create.advancePercentage",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "expectedDeliveryDate",
            "jsonPath": "purchaseOrders[0].expectedDeliveryDate",
            "label": "purchaseorder.create.expectedDeliveryDate",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "deliveryTerms",
            "jsonPath": "purchaseOrders[0].deliveryTerms",
            "label": "purchaseorder.create.deliveryTerms",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 512,
            "patternErrorMsg": ""
          },
          {
            "name": "paymentTerms",
            "jsonPath": "purchaseOrders[0].paymentTerms",
            "label": "purchaseorder.create.paymentTerms",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 512,
            "patternErrorMsg": ""
          },
          {
            "name": "remarks",
            "jsonPath": "purchaseOrders[0].remarks",
            "label": "purchaseorder.create.remarks",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 1000,
            "patternErrorMsg": ""
          },
          {
            "name": "stateId",
            "jsonPath": "purchaseOrders[0].stateId",
            "label": "purchaseorder.create.stateId",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "designation",
            "jsonPath": "purchaseOrders[0].designation",
            "label": "purchaseorder.create.designation",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 128,
            "patternErrorMsg": ""
          },
          {
            "type": "tableList",
            "jsonPath": "purchaseOrders[0].purchaseOrderDetails",
            "tableList": {
              "header": [
                {
                  "label": "purchaseorder.create.material"
                },
                {
                  "label": "purchaseorder.create.material description"
                },
                {
                  "label": "purchaseorder.create.uom"
                },
                {
                  "label": "purchaseorder.create.Rate contract"
                },
                {
                  "label": "purchaseorder.create.quantity"
                },
                {
                  "label": "purchaseorder.create.unit price"
                },
                {
                  "label": "purchaseorder.create.total value"
                },
                {
                  "label": "purchaseorder.create.Tender Quantity"
                },
                {
                  "label": "purchaseorder.create.Used Quantity"
                }
              ],
              "values": [
                {
                  "name": "code",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].material.code",
                  "label": "purchaseorder.create.material.code",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "maxLength": 50,
                  "minLength": 5,
                  "patternErrorMsg": "",
                  "url": "/egov-mdms-service/v1/_get?&moduleName=inventory&masterName=Material|$..code|$..name"
                },
                {
                  "name": "name",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].material.name",
                  "label": "purchaseorder.create.material.name",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "maxLength": 50,
                  "minLength": 5,
                  "patternErrorMsg": ""
                },
                {
                  "name": "code",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].uom.code",
                  "label": "purchaseorder.create.uom.code",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": "",
                  "url": "/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Uom|$..code|$..description"
                },
                {
                  "name": "rateContractNumber",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].priceList.rateContractNumber",
                  "label": "purchaseorder.create.priceList.rateContractNumber",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "orderQuantity",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].orderQuantity",
                  "label": "purchaseorder.create.purchaseOrderDetails[0].orderQuantity",
                  "pattern": "",
                  "type": "number",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "unitPrice",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].unitPrice",
                  "label": "purchaseorder.create.purchaseOrderDetails[0].unitPrice",
                  "pattern": "",
                  "type": "number",
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
    "tenantIdRequired": true,
    "url": "/inventory-services/v110/purchaseorders/_search?tenantId={tenantId}"
  },
  "inventory.update": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "purchaseOrders",
    "groups": [
      {
        "name": "Group1",
        "label": "purchaseorder.create.group.title.Group1",
        "fields": [
          {
            "name": "code",
            "jsonPath": "purchaseOrders[0].store.code",
            "label": "purchaseorder.create.store.code",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": "purchaseorder.create.field.message.code",
            "url": "/inventory-services/stores/_search?|$..code|$..name"
          },
          {
            "name": "purchaseOrderNumber",
            "jsonPath": "purchaseOrders[0].purchaseOrderNumber",
            "label": "purchaseorder.create.purchaseOrderNumber",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "purchaseOrderDate",
            "jsonPath": "purchaseOrders[0].purchaseOrderDate",
            "label": "purchaseorder.create.purchaseOrderDate",
            "pattern": "",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "purchaseType",
            "jsonPath": "purchaseOrders[0].purchaseType",
            "label": "purchaseorder.create.purchaseType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "rateType",
            "jsonPath": "purchaseOrders[0].rateType",
            "label": "purchaseorder.create.rateType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "code",
            "jsonPath": "purchaseOrders[0].supplier.code",
            "label": "purchaseorder.create.supplier.code",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": "purchaseorder.create.field.message.code",
            "url": "/inventory-services/suppliers/_search?|$..code|$..name"
          },
          {
            "name": "advanceAmount",
            "jsonPath": "purchaseOrders[0].advanceAmount",
            "label": "purchaseorder.create.advanceAmount",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "advancePercentage",
            "jsonPath": "purchaseOrders[0].advancePercentage",
            "label": "purchaseorder.create.advancePercentage",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "expectedDeliveryDate",
            "jsonPath": "purchaseOrders[0].expectedDeliveryDate",
            "label": "purchaseorder.create.expectedDeliveryDate",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "deliveryTerms",
            "jsonPath": "purchaseOrders[0].deliveryTerms",
            "label": "purchaseorder.create.deliveryTerms",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 512,
            "patternErrorMsg": ""
          },
          {
            "name": "paymentTerms",
            "jsonPath": "purchaseOrders[0].paymentTerms",
            "label": "purchaseorder.create.paymentTerms",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 512,
            "patternErrorMsg": ""
          },
          {
            "name": "remarks",
            "jsonPath": "purchaseOrders[0].remarks",
            "label": "purchaseorder.create.remarks",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 1000,
            "patternErrorMsg": ""
          },
          {
            "name": "stateId",
            "jsonPath": "purchaseOrders[0].stateId",
            "label": "purchaseorder.create.stateId",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "patternErrorMsg": ""
          },
          {
            "name": "designation",
            "jsonPath": "purchaseOrders[0].designation",
            "label": "purchaseorder.create.designation",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "defaultValue": "",
            "maxLength": 128,
            "patternErrorMsg": ""
          },
          {
            "type": "tableList",
            "jsonPath": "purchaseOrders[0].purchaseOrderDetails",
            "tableList": {
              "header": [
                {
                  "label": "purchaseorder.create.material"
                },
                {
                  "label": "purchaseorder.create.material description"
                },
                {
                  "label": "purchaseorder.create.uom"
                },
                {
                  "label": "purchaseorder.create.Rate contract"
                },
                {
                  "label": "purchaseorder.create.quantity"
                },
                {
                  "label": "purchaseorder.create.unit price"
                },
                {
                  "label": "purchaseorder.create.total value"
                },
                {
                  "label": "purchaseorder.create.Tender Quantity"
                },
                {
                  "label": "purchaseorder.create.Used Quantity"
                }
              ],
              "values": [
                {
                  "name": "code",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].material.code",
                  "label": "purchaseorder.create.material.code",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "maxLength": 50,
                  "minLength": 5,
                  "patternErrorMsg": "",
                  "url": "/egov-mdms-service/v1/_get?&moduleName=inventory&masterName=Material|$..code|$..name"
                },
                {
                  "name": "name",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].material.name",
                  "label": "purchaseorder.create.material.name",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "maxLength": 50,
                  "minLength": 5,
                  "patternErrorMsg": ""
                },
                {
                  "name": "code",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].uom.code",
                  "label": "purchaseorder.create.uom.code",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": "",
                  "url": "/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Uom|$..code|$..description"
                },
                {
                  "name": "rateContractNumber",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].priceList.rateContractNumber",
                  "label": "purchaseorder.create.priceList.rateContractNumber",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "orderQuantity",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].orderQuantity",
                  "label": "purchaseorder.create.purchaseOrderDetails[0].orderQuantity",
                  "pattern": "",
                  "type": "number",
                  "isRequired": false,
                  "isDisabled": false,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "unitPrice",
                  "jsonPath": "purchaseOrders[0].purchaseOrderDetails[0].unitPrice",
                  "label": "purchaseorder.create.purchaseOrderDetails[0].unitPrice",
                  "pattern": "",
                  "type": "number",
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
    "url": "/inventory-services/v110/purchaseorders/_update",
    "tenantIdRequired": true,
    "searchUrl": "/inventory-services/v110/purchaseorders/_search?tenantId={tenantId}"
  }
}
 export default dat;
