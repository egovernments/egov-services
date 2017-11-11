var dat = {
  "inventory.search": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "",
    "url": "/inventory-inventory/v110/pricelists/_search",
    "groups": [
      {
        "name": "search",
        "label": "inventory.search.title",
        "fields": [
          {
            "name": "supplierName",
            "jsonPath": "supplierName",
            "label": "inventory.create.supplierName",
            "type": "singleValueList",
            "isDisabled": false,
	    "url":"inventory-services/supplier/_search?|$.supplier[*].code|$.supplier[*].name",
            "patternErrorMsg": "inventory.create.field.message.supplierName"
          },
          {
            "name": "rateContractNumber",
            "jsonPath": "rateContractNumber",
            "label": "inventory.create.rateContractNumber",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.rateContractNumber"
          },
          {
            "name": "agreementNumber",
            "jsonPath": "agreementNumber",
            "label": "inventory.create.agreementNumber",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.agreementNumber"
          },
          {
            "name": "rateContractDate",
            "jsonPath": "rateContractDate",
            "label": "inventory.create.rateContractDate",
            "type": "datePicker",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.rateContractDate"
          },
          {
            "name": "agreementDate",
            "jsonPath": "agreementDate",
            "label": "inventory.create.agreementDate",
           "type": "datePicker",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.agreementDate"
          },
          {
            "name": "agreementStartDate",
            "jsonPath": "agreementStartDate",
            "label": "inventory.create.agreementStartDate",
           "type": "datePicker",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.agreementStartDate"
          },
          {
            "name": "agreementEndDate",
            "jsonPath": "agreementEndDate",
            "label": "inventory.create.agreementEndDate",
            "type": "datePicker",
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.agreementEndDate"
          },
          {
            "name": "rateType",
            "jsonPath": "rateType",
            "label": "inventory.create.rateType",
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
          "label": "Supplier"
        },
        {
          "label": "inventory.search.result.rateType"
        },
        {
          "label": "inventory.search.result.rateContractNumber"
        },
        {
          "label": "inventory.search.result.rateContractDate"
        },
        {
          "label": "inventory.search.result.agreementNumber"
        },
        {
          "label": "inventory.search.result.agreementDate"
        },
        {
          "label": "inventory.search.result.agreementStartDate"
        },
        {
          "label": "inventory.search.result.agreementEndDate"
        },
        {
          "label": "inventory.search.result.active"
        }
      ],
      "values": [
        "priceLists[0].supplier.name"
      ],
      "resultPath": "pricelists",
      "rowClickUrlUpdate": "/update/pricelists/{id}",
      "rowClickUrlView": "/view/pricelists/{id}"
    }
  },
  "inventory.create": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "priceLists",
    "groups": [
      {
        "name": "Group1",
        "label": "Supplier Details",
        "fields": [
          {
            "name": "supplier",
            "jsonPath": "priceLists[0].supplier.code",
            "label": "inventory.create.supplier",
            "url":"inventory-services/supplier/_search?|$.supplier[*].code|$.supplier[*].name",
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
            "label": "inventory.create.rateType",
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
            "label": "inventory.create.rateContractNumber",
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
            "label": "inventory.create.rateContractDate",
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
            "label": "inventory.create.agreementNumber",
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
            "label": "inventory.create.agreementDate",
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
            "label": "inventory.create.agreementStartDate",
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
            "label": "inventory.create.agreementEndDate",
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
            "label": "inventory.create.active",
            "pattern": "",
              "type":"checkbox",
            "defaultValue":true,
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": true,
            "patternErrorMsg": ""
          }
        ]
      },
      {
        "name": "Rate contract Details",
        "label": "inventory.create.group.pricelist.title.pricebdetails",
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
                  "label": "Rate"
                },
                {
                  "label": "From Date"
                },
                {
                  "label": "To Date"
                },
                {
                  "label": "Quantity"
                }
              ],
              "values": [
                {
                  "name": "material",
                  "pattern": "",
                  "type":"autoCompelete",
                  "jsonPath": "priceLists[0].PriceListDetails[0].material.code",
		  "displayJsonPath":"priceLists[0].PriceListDetails[0].material.name",
                  "isRequired": true,
                  "isDisabled": false,
                  "url": "/egov-mdms-service/v1/_get?&moduleName=inventory&masterName=Material|$.MdmsRes.inventory.Material[*].code|$.MdmsRes.inventory.Material[*].name|$.MdmsRes.inventory.Material[*].description",
                  "depedants": [
                    {
                      "jsonPath": "priceLists[0].PriceListDetails[0].material.description",
                      "type": "textField",
                      "valExp": "getValFromDropdownData('materialStoreMappings[*].material.code', getVal('materialStoreMappings[*].material.code'), 'others[0]')"
                    }
                  ]
                },
                {
                  "name": "materialDescription",
                  "jsonPath": "priceLists[0].PriceListDetails[0].material.description",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": true,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "uom",
                  "jsonPath": "priceLists[0].PriceListDetails[0].uom.code",
                  "pattern": "",
                  "type": "singleValueList",
                  "isRequired": true,
                  "isDisabled": false,
                  "url": "/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Uom|$..code|$..description"
                },
                {
                  "name": "ratePerUnit",
                  "jsonPath": "priceLists[0].PriceListDetails[0].ratePerUnit",
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
                  "jsonPath": "priceLists[0].PriceListDetails[0].fromDate",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": true,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "toDate",
                  "jsonPath": "priceLists[0].PriceListDetails[0].toDate",
                  "pattern": "",
                  "type": "text",
                  "isRequired": false,
                  "isDisabled": true,
                  "defaultValue": "",
                  "patternErrorMsg": ""
                },
                {
                  "name": "quantity",
                  "jsonPath": "priceLists[0].PriceListDetails[0].quantity",
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
    "url": "/inventory-inventory/v110/pricelists/_create",
    "tenantIdRequired": true
  },
  "inventory.view": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "priceLists",
    "groups": [
      {
        "name": "Group1",
        "label": "inventory.create.group.title.Group1",
        "fields": [
          {
            "name": "code",
            "jsonPath": "priceLists[0].supplier.code",
            "label": "inventory.create.supplier.code",
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
            "label": "inventory.create.rateType",
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
            "label": "inventory.create.rateContractNumber",
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
            "label": "inventory.create.rateContractDate",
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
            "label": "inventory.create.agreementNumber",
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
            "label": "inventory.create.agreementDate",
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
            "label": "inventory.create.agreementStartDate",
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
            "label": "inventory.create.agreementEndDate",
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
            "label": "inventory.create.active",
            "pattern": "",
            "type": "radio",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": true,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "tenantIdRequired": true,
    "url": "/inventory-inventory/v110/pricelists/_search?tenantId={tenantId}"
  },
  "inventory.update": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "priceLists",
    "groups": [
      {
        "name": "Group1",
        "label": "inventory.create.group.title.Group1",
        "fields": [
          {
            "name": "code",
            "jsonPath": "priceLists[0].supplier.code",
            "label": "inventory.create.supplier.code",
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
            "label": "inventory.create.rateType",
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
            "label": "inventory.create.rateContractNumber",
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
            "label": "inventory.create.rateContractDate",
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
            "label": "inventory.create.agreementNumber",
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
            "label": "inventory.create.agreementDate",
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
            "label": "inventory.create.agreementStartDate",
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
            "label": "inventory.create.agreementEndDate",
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
            "label": "inventory.create.active",
            "pattern": "",
            "type": "radio",
            "isRequired": true,
            "isDisabled": false,
            "defaultValue": true,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "url": "/inventory-inventory/v110/pricelists/_update",
    "tenantIdRequired": true,
    "searchUrl": "/inventory-inventory/v110/pricelists/_search?tenantId={tenantId}"
  }
}
 export default dat;
