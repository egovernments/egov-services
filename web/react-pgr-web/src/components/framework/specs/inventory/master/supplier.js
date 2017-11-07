var dat = {
  "inventory.search": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "",
    "url": "inventory-services/suppliers/_search",
    "groups": [
      {
        "name": "search",
        "label": "supplier.search.title",
        "fields": [
          {
            "name": "supplierName",
            "jsonPath": "codes",
            "label": "supplier.search.name",
            "type": "singleValueList",
            "url":"inventory-services/suppliers/_search?|$..code|$..name",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "supplier.type",
            "jsonPath": "type",
            "label": "supplier.search.type",
            "type": "singleValueList",
            "defaultValue":[
              {key: null, value: "-- Please Select --"},
              {
                 "key":"INDIVIDUAL",
                 "value":"Individual"
              },
              {
                 "key":"FIRM",
                 "value":"Firm"
              },
              {
                 "key":"COMPANY",
                 "value":"Company"
              },
              {
                 "key":"REGISTERED SOCIETY",
                 "value":"Registered society"
              },
              {
                 "key":"GOVERNMENT DEPARTMENT",
                 "value":"Government Department"
              },
              {
                 "key":"OTHERS",
                 "value":"Others"
              }
            ],
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "isActive",
            "jsonPath": "active",
            "label": "supplier.search.isactive",
            "type": "checkbox",
            "defaultValue":true,
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "result": {
      "header": [
        {
          "label": "inventory.search.result.Supplier Code"
        }, {
          "label": "inventory.search.result.Supplier Name"
        }, {
          "label": "inventory.search.result.Active"
        }
      ],
      "values": [
        "code", "name", {valuePath:"active", type:"checkbox"}
      ],
      "resultPath": "suppliers",
      "rowClickUrlUpdate": "/update/inventory/supplier/{code}",
      "rowClickUrlView": "/view/inventory/supplier/{code}",
      "rowClickUrlAdd" : "/create/inventory/supplier",
      "rowClickUrlDelete" : "inventory-services/suppliers/_update"
    }
  },
  "inventory.create": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "suppliers",
    "groups": [
      {
        "name": "Add Supplie",
        "label": "inventory.create.group.title.Add Supplie",
        "fields": [
          {
            "name": "supplierType",
            "jsonPath": "suppliers[0].type",
            "label": "inventory.create.supplierType",
            "type": "singleValueList",
            "defaultValue":[
              {key: null, value: "-- Please Select --"},
              {
                 "key":"INDIVIDUAL",
                 "value":"Individual"
              },
              {
                 "key":"FIRM",
                 "value":"Firm"
              },
              {
                 "key":"COMPANY",
                 "value":"Company"
              },
              {
                 "key":"REGISTERED SOCIETY",
                 "value":"Registered society"
              },
              {
                 "key":"GOVERNMENT DEPARTMENT",
                 "value":"Government Department"
              },
              {
                 "key":"OTHERS",
                 "value":"Others"
              }
            ],
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }, {
            "name": "code",
            "jsonPath": "suppliers[0].code",
            "label": "inventory.create.code",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": ""
          }, {
            "name": "name",
            "jsonPath": "suppliers[0].name",
            "label": "inventory.create.name",
            "pattern": "^[a-zA-Z ]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": "inventory.create.field.message.name"
          }, {
            "name": "address",
            "jsonPath": "suppliers[0].address",
            "label": "inventory.create.address",
            "pattern": "^[a-zA-Z0-9 ]+$",
            "type": "textarea",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 1000,
            "patternErrorMsg": "inventory.create.field.message.address"
          }, {
            "name": "description",
            "jsonPath": "suppliers[0].description",
            "label": "inventory.create.description",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 1000,
            "patternErrorMsg": ""
          }, {
            "name": "contactNo",
            "jsonPath": "suppliers[0].contactNo",
            "label": "inventory.create.contactNo",
            "pattern": "^[0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": "inventory.create.field.message.supplierContactNo"
          }, {
            "name": "faxNo",
            "jsonPath": "suppliers[0].faxNo",
            "label": "inventory.create.faxNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }, {
            "name": "website",
            "jsonPath": "suppliers[0].website",
            "label": "inventory.create.website",
            "pattern": "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.website"
          }, {
            "name": "email",
            "jsonPath": "suppliers[0].email",
            "label": "inventory.create.email",
            "pattern": "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 100,
            "patternErrorMsg": "inventory.create.field.message.email"
          }, {
            "name": "panNo",
            "jsonPath": "suppliers[0].panNo",
            "label": "inventory.create.panNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": ""
          }, {
            "name": "tinNo",
            "jsonPath": "suppliers[0].tinNo",
            "label": "inventory.create.tinNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": ""
          }, {
            "name": "cstNo",
            "jsonPath": "suppliers[0].cstNo",
            "label": "inventory.create.cstNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": ""
          }, {
            "name": "vatNo",
            "jsonPath": "suppliers[0].vatNo",
            "label": "inventory.create.vatNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": ""
          }, {
            "name": "gstNo",
            "jsonPath": "suppliers[0].gstNo",
            "label": "inventory.create.gstNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": ""
          }, {
            "name": "contactPerson",
            "jsonPath": "suppliers[0].contactPerson",
            "label": "inventory.create.contactPerson",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": ""
          }, {
            "name": "contactPersonNo",
            "jsonPath": "suppliers[0].contactPersonNo",
            "label": "inventory.create.contactPersonNo",
            "pattern": "^[0-9]+$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": "inventory.create.field.message.contactPersonNo"
          }, {
            "name": "active",
            "jsonPath": "suppliers[0].active",
            "label": "inventory.create.active",
            "type": "checkbox",
            "defaultValue":true,
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "depedants": [
                {
									"jsonPath":"suppliers[0].inActiveDate",
									"type":"textField",
                  "isHidden":true,
									"pattern":"`${!getVal('suppliers[0].active') ? new Date().getTime():undefined}`",
									"rg":"",
									"isRequired": false,
									"requiredErrMsg": "",
									"patternErrMsg": ""
							 }
            ]
          }
        ]
      }, {
        "name": "Bank Information",
        "label": "inventory.create.group.title.Bank Information",
        "fields": [
          {
            "name": "name",
            "jsonPath": "suppliers[0].bankCode",
            "label": "inventory.create.name",
            "pattern": "^[a-zA-Z ]$",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.name",
            "url": "/egf-master/banks/_search?|$..code|$..name"
          }, {
            "name": "bankBranch",
            "jsonPath": "suppliers[0].bankBranch",
            "label": "inventory.create.bankBranch",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.bankBranch"
          },{
            "name": "acctNo",
            "jsonPath": "suppliers[0].acctNo",
            "label": "inventory.create.acctNo",
            "pattern": "^[0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.acctNo"
          }, {
            "name": "ifsc",
            "jsonPath": "suppliers[0].ifsc",
            "label": "inventory.create.ifsc",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.ifsc"
          }, {
            "name": "micr",
            "jsonPath": "suppliers[0].micr",
            "label": "inventory.create.micr",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "url": "/inventory-services/suppliers/_create",
    "tenantIdRequired": true
  },
  "inventory.view": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "suppliers",
    "groups": [
      {
        "name": "Add Supplie",
        "label": "inventory.create.group.title.Add Supplie",
        "fields": [
          {
            "name": "name",
            "jsonPath": "suppliers[0].type",
            "label": "inventory.create.name",
            "type": "singleValueList",
            "defaultValue":[
              {key: null, value: "-- Please Select --"},
              {
                 "key":"INDIVIDUAL",
                 "value":"Individual"
              },
              {
                 "key":"FIRM",
                 "value":"Firm"
              },
              {
                 "key":"COMPANY",
                 "value":"Company"
              },
              {
                 "key":"REGISTERED SOCIETY",
                 "value":"Registered society"
              },
              {
                 "key":"GOVERNMENT DEPARTMENT",
                 "value":"Government Department"
              },
              {
                 "key":"OTHERS",
                 "value":"Others"
              }
            ],
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }, {
            "name": "code",
            "jsonPath": "suppliers[0].code",
            "label": "inventory.create.code",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": ""
          }, {
            "name": "name",
            "jsonPath": "suppliers[0].name",
            "label": "inventory.create.name",
            "pattern": "^[a-zA-Z ]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": "inventory.create.field.message.name"
          }, {
            "name": "address",
            "jsonPath": "suppliers[0].address",
            "label": "inventory.create.address",
            "pattern": "^[a-zA-Z0-9 ]+$",
            "type": "textarea",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 1000,
            "patternErrorMsg": "inventory.create.field.message.address"
          }, {
            "name": "description",
            "jsonPath": "suppliers[0].description",
            "label": "inventory.create.description",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 1000,
            "patternErrorMsg": ""
          }, {
            "name": "contactNo",
            "jsonPath": "suppliers[0].contactNo",
            "label": "inventory.create.contactNo",
            "pattern": "^[0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": "inventory.create.field.message.supplierContactNo"
          }, {
            "name": "faxNo",
            "jsonPath": "suppliers[0].faxNo",
            "label": "inventory.create.faxNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }, {
            "name": "website",
            "jsonPath": "suppliers[0].website",
            "label": "inventory.create.website",
            "pattern": "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.website"
          }, {
            "name": "email",
            "jsonPath": "suppliers[0].email",
            "label": "inventory.create.email",
            "pattern": "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 100,
            "patternErrorMsg": "inventory.create.field.message.email"
          }, {
            "name": "panNo",
            "jsonPath": "suppliers[0].panNo",
            "label": "inventory.create.panNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": ""
          }, {
            "name": "tinNo",
            "jsonPath": "suppliers[0].tinNo",
            "label": "inventory.create.tinNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": ""
          }, {
            "name": "cstNo",
            "jsonPath": "suppliers[0].cstNo",
            "label": "inventory.create.cstNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": ""
          }, {
            "name": "vatNo",
            "jsonPath": "suppliers[0].vatNo",
            "label": "inventory.create.vatNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": ""
          }, {
            "name": "gstNo",
            "jsonPath": "suppliers[0].gstNo",
            "label": "inventory.create.gstNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": ""
          }, {
            "name": "contactPerson",
            "jsonPath": "suppliers[0].contactPerson",
            "label": "inventory.create.contactPerson",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": ""
          }, {
            "name": "contactPersonNo",
            "jsonPath": "suppliers[0].contactPersonNo",
            "label": "inventory.create.contactPersonNo",
            "pattern": "^[0-9]+$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": "inventory.create.field.message.contactPersonNo"
          }, {
            "name": "active",
            "jsonPath": "suppliers[0].active",
            "label": "inventory.create.active",
            "type": "checkbox",
            "defaultValue":true,
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      }, {
        "name": "Bank Information",
        "label": "inventory.create.group.title.Bank Information",
        "fields": [
          {
            "name": "name",
            "jsonPath": "suppliers[0].bankCode",
            "label": "inventory.create.name",
            "pattern": "^[a-zA-Z ]$",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.name",
            "url": "/egf-master/banks/_search?|$..code|$..name"
          }, {
            "name": "bankBranch",
            "jsonPath": "suppliers[0].bankBranch",
            "label": "inventory.create.bankBranch",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.bankBranch"
          },{
            "name": "acctNo",
            "jsonPath": "suppliers[0].acctNo",
            "label": "inventory.create.acctNo",
            "pattern": "^[0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.acctNo"
          }, {
            "name": "ifsc",
            "jsonPath": "suppliers[0].ifsc",
            "label": "inventory.create.ifsc",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.ifsc"
          }, {
            "name": "micr",
            "jsonPath": "suppliers[0].micr",
            "label": "inventory.create.micr",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "tenantIdRequired": true,
    "url": "inventory-services/suppliers/_search?codes={codes}"
  },
  "inventory.update": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "suppliers",
    "groups": [
      {
        "name": "Add Supplie",
        "label": "inventory.create.group.title.Add Supplie",
        "fields": [
          {
            "name": "supplierType",
            "jsonPath": "suppliers[0].type",
            "label": "inventory.create.supplierType",
            "type": "singleValueList",
            "defaultValue":[
              {key: null, value: "-- Please Select --"},
              {
                 "key":"INDIVIDUAL",
                 "value":"Individual"
              },
              {
                 "key":"FIRM",
                 "value":"Firm"
              },
              {
                 "key":"COMPANY",
                 "value":"Company"
              },
              {
                 "key":"REGISTERED SOCIETY",
                 "value":"Registered society"
              },
              {
                 "key":"GOVERNMENT DEPARTMENT",
                 "value":"Government Department"
              },
              {
                 "key":"OTHERS",
                 "value":"Others"
              }
            ],
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          }, {
            "name": "code",
            "jsonPath": "suppliers[0].code",
            "label": "inventory.create.code",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": ""
          }, {
            "name": "name",
            "jsonPath": "suppliers[0].name",
            "label": "inventory.create.name",
            "pattern": "^[a-zA-Z ]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": "inventory.create.field.message.name"
          }, {
            "name": "address",
            "jsonPath": "suppliers[0].address",
            "label": "inventory.create.address",
            "pattern": "^[a-zA-Z0-9 ]+$",
            "type": "textarea",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 1000,
            "patternErrorMsg": "inventory.create.field.message.address"
          }, {
            "name": "description",
            "jsonPath": "suppliers[0].description",
            "label": "inventory.create.description",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 1000,
            "patternErrorMsg": ""
          }, {
            "name": "contactNo",
            "jsonPath": "suppliers[0].contactNo",
            "label": "inventory.create.contactNo",
            "pattern": "^[0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": "inventory.create.field.message.supplierContactNo"
          }, {
            "name": "faxNo",
            "jsonPath": "suppliers[0].faxNo",
            "label": "inventory.create.faxNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }, {
            "name": "website",
            "jsonPath": "suppliers[0].website",
            "label": "inventory.create.website",
            "pattern": "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.website"
          }, {
            "name": "email",
            "jsonPath": "suppliers[0].email",
            "label": "inventory.create.email",
            "pattern": "^([a-zA-Z0-9_\\-\\.]+)@([a-zA-Z0-9_\\-\\.]+)\\.([a-zA-Z]{2,5})$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 100,
            "patternErrorMsg": "inventory.create.field.message.email"
          }, {
            "name": "panNo",
            "jsonPath": "suppliers[0].panNo",
            "label": "inventory.create.panNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": ""
          }, {
            "name": "tinNo",
            "jsonPath": "suppliers[0].tinNo",
            "label": "inventory.create.tinNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": ""
          }, {
            "name": "cstNo",
            "jsonPath": "suppliers[0].cstNo",
            "label": "inventory.create.cstNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": ""
          }, {
            "name": "vatNo",
            "jsonPath": "suppliers[0].vatNo",
            "label": "inventory.create.vatNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": ""
          }, {
            "name": "gstNo",
            "jsonPath": "suppliers[0].gstNo",
            "label": "inventory.create.gstNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": ""
          }, {
            "name": "contactPerson",
            "jsonPath": "suppliers[0].contactPerson",
            "label": "inventory.create.contactPerson",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 50,
            "minLength": 5,
            "patternErrorMsg": ""
          }, {
            "name": "contactPersonNo",
            "jsonPath": "suppliers[0].contactPersonNo",
            "label": "inventory.create.contactPersonNo",
            "pattern": "^[0-9]+$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 10,
            "patternErrorMsg": "inventory.create.field.message.contactPersonNo"
          }, {
            "name": "active",
            "jsonPath": "suppliers[0].active",
            "label": "inventory.create.active",
            "type": "checkbox",
            "defaultValue":true,
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "depedants": [
                {
									"jsonPath":"suppliers[0].inActiveDate",
									"type":"textField",
                  "isHidden":true,
									"pattern":"`${!getVal('suppliers[0].active') ? new Date().getTime():undefined}`",
									"rg":"",
									"isRequired": false,
									"requiredErrMsg": "",
									"patternErrMsg": ""
							 }
            ]
          }
        ]
      }, {
        "name": "Bank Information",
        "label": "inventory.create.group.title.Bank Information",
        "fields": [
          {
            "name": "name",
            "jsonPath": "suppliers[0].bankCode",
            "label": "inventory.create.name",
            "pattern": "^[a-zA-Z ]$",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.name",
            "url": "/egf-master/banks/_search?|$..code|$..name"
          }, {
            "name": "bankBranch",
            "jsonPath": "suppliers[0].bankBranch",
            "label": "inventory.create.bankBranch",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.bankBranch"
          },{
            "name": "acctNo",
            "jsonPath": "suppliers[0].acctNo",
            "label": "inventory.create.acctNo",
            "pattern": "^[0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.acctNo"
          }, {
            "name": "ifsc",
            "jsonPath": "suppliers[0].ifsc",
            "label": "inventory.create.ifsc",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.ifsc"
          }, {
            "name": "micr",
            "jsonPath": "suppliers[0].micr",
            "label": "inventory.create.micr",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "url": "inventory-services/suppliers/_update",
    "tenantIdRequired": true,
    "searchUrl": "inventory-services/suppliers/_search?codes={codes}"
  }
}

export default dat;
