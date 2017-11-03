var dat = {
  "inventory.search": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "",
    "url": "http://www.mocky.io/v2/59fae53f3700005b4066bb34",
    "groups": [
      {
        "name": "search",
        "label": "supplier.search.title",
        "fields": [
          {
            "name": "supplierName",
            "jsonPath": "ids",
            "label": "supplier.search.name",
            "type": "singleValueList",
            "url":"http://www.mocky.io/v2/59fae53f3700005b4066bb34?|$..name|$..code",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "supplierType",
            "jsonPath": "type",
            "label": "supplier.search.type",
            "url":"/egov-mdms-service/v1/_get?&moduleName=inventory&masterName=SuplierType|$..name|$..code",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "isActive",
            "jsonPath": "isActive",
            "label": "supplier.search.isactive",
            "type": "checkbox",
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
        "code", "name", "status.name"
      ],
      "resultPath": "suppliers",
      "rowClickUrlUpdate": "/update/suppliers/{code}",
      "rowClickUrlView": "/view/suppliers/{code}",
      "rowClickUrlAdd" : "/create/inventory/supplier",
      "rowClickUrlDelete" : ""
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
            "name": "name",
            "jsonPath": "suppliers[0].supplierType.name",
            "label": "inventory.create.name",
            "type": "singleValueList",
            "isRequired": false,
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
            "name": "narration",
            "jsonPath": "suppliers[0].narration",
            "label": "inventory.create.narration",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 1000,
            "patternErrorMsg": ""
          }, {
            "name": "supplierContactNo",
            "jsonPath": "suppliers[0].supplierContactNo",
            "label": "inventory.create.supplierContactNo",
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
            "name": "narration",
            "jsonPath": "suppliers[0].narration",
            "label": "inventory.create.narration",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 1000,
            "patternErrorMsg": ""
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
          }
        ]
      }, {
        "name": "Bank Information",
        "label": "inventory.create.group.title.Bank Information",
        "fields": [
          {
            "name": "name",
            "jsonPath": "suppliers[0].bank.name",
            "label": "inventory.create.name",
            "pattern": "^[a-zA-Z ]$",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.name",
            "url": "/egf-master/banks/_search?|$..code|$..name"
          }, {
            "name": "acctNo",
            "jsonPath": "suppliers[0].bank.acctNo",
            "label": "inventory.create.acctNo",
            "pattern": "^[0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.acctNo"
          }, {
            "name": "ifsc",
            "jsonPath": "suppliers[0].bank.ifsc",
            "label": "inventory.create.ifsc",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.ifsc"
          }, {
            "name": "micr",
            "jsonPath": "suppliers[0].bank.micr",
            "label": "inventory.create.micr",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "url": "/inventory-inventory/v110/suppliers/_create",
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
            "jsonPath": "suppliers[0].supplierType.name",
            "label": "inventory.create.name",
            "type": "singleValueList",
            "isRequired": false,
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
            "name": "narration",
            "jsonPath": "suppliers[0].narration",
            "label": "inventory.create.narration",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 1000,
            "patternErrorMsg": ""
          }, {
            "name": "supplierContactNo",
            "jsonPath": "suppliers[0].supplierContactNo",
            "label": "inventory.create.supplierContactNo",
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
            "name": "narration",
            "jsonPath": "suppliers[0].narration",
            "label": "inventory.create.narration",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 1000,
            "patternErrorMsg": ""
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
          }
        ]
      }, {
        "name": "Bank Information",
        "label": "inventory.create.group.title.Bank Information",
        "fields": [
          {
            "name": "name",
            "jsonPath": "suppliers[0].bank.name",
            "label": "inventory.create.name",
            "pattern": "^[a-zA-Z ]$",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.name",
            "url": "/egf-master/banks/_search?tenantId={tenantId}|$..code|$..name"
          }, {
            "name": "acctNo",
            "jsonPath": "suppliers[0].bank.acctNo",
            "label": "inventory.create.acctNo",
            "pattern": "^[0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.acctNo"
          }, {
            "name": "ifsc",
            "jsonPath": "suppliers[0].bank.ifsc",
            "label": "inventory.create.ifsc",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.ifsc"
          }, {
            "name": "micr",
            "jsonPath": "suppliers[0].bank.micr",
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
    "url": "http://www.mocky.io/v2/59fae53f3700005b4066bb34"
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
            "name": "name",
            "jsonPath": "suppliers[0].supplierType.name",
            "label": "inventory.create.name",
            "type": "singleValueList",
            "isRequired": false,
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
            "name": "narration",
            "jsonPath": "suppliers[0].narration",
            "label": "inventory.create.narration",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 1000,
            "patternErrorMsg": ""
          }, {
            "name": "supplierContactNo",
            "jsonPath": "suppliers[0].supplierContactNo",
            "label": "inventory.create.supplierContactNo",
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
            "name": "narration",
            "jsonPath": "suppliers[0].narration",
            "label": "inventory.create.narration",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "maxLength": 1000,
            "patternErrorMsg": ""
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
          }
        ]
      }, {
        "name": "Bank Information",
        "label": "inventory.create.group.title.Bank Information",
        "fields": [
          {
            "name": "name",
            "jsonPath": "suppliers[0].bank.name",
            "label": "inventory.create.name",
            "pattern": "^[a-zA-Z ]$",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.name",
            "url": "/egf-master/banks/_search?tenantId={tenantId}|$..code|$..name"
          }, {
            "name": "acctNo",
            "jsonPath": "suppliers[0].bank.acctNo",
            "label": "inventory.create.acctNo",
            "pattern": "^[0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.acctNo"
          }, {
            "name": "ifsc",
            "jsonPath": "suppliers[0].bank.ifsc",
            "label": "inventory.create.ifsc",
            "pattern": "^[a-zA-Z0-9]+$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "inventory.create.field.message.ifsc"
          }, {
            "name": "micr",
            "jsonPath": "suppliers[0].bank.micr",
            "label": "inventory.create.micr",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "url": "/inventory-inventory/v110/suppliers/_update",
    "tenantIdRequired": true,
    "searchUrl": "/inventory-inventory/v110/suppliers/_search?tenantId={tenantId}"
  }
}

export default dat;
