var dat ={
  "legal.search": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "",
    "url": "/lcms-services/legalcase/advocate/_search",
    "groups": [
      {
        "name": "search",
        "label": "legal.search.title",
        "fields": [
          {
            "label": "legal.createundefined",
            "type": "",
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.undefined"
          },
          {
            "name": "sortProperty",
            "jsonPath": "sortProperty",
            "label": "legal.createsortProperty",
            "type": "text",
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.sortProperty"
          },
          {
            "name": "ids",
            "jsonPath": "ids",
            "label": "legal.createids",
            "type": "",
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.ids"
          },
          {
            "name": "referenceNo",
            "jsonPath": "referenceNo",
            "label": "legal.createreferenceNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.referenceNo"
          },
          {
            "name": "isSummon",
            "jsonPath": "isSummon",
            "label": "legal.createisSummon",
            "type": "radio",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.isSummon",
            "values": [
              {
                "label": "legal.create.Summon",
                "value": true
              },
              {
                "label": "legal.create.Warrant",
                "value": false
              }
            ]
          },
          {
            "name": "referenceCaseNo",
            "jsonPath": "referenceCaseNo",
            "label": "legal.createreferenceCaseNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.referenceCaseNo"
          },
          {
            "name": "caseStatus",
            "jsonPath": "caseStatus",
            "label": "legal.createcaseStatus",
            "type": "radio",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.caseStatus"
          },
          {
            "name": "caseType",
            "jsonPath": "caseType",
            "label": "legal.createcaseType",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.caseType"
          },
          {
            "name": "departmentName",
            "jsonPath": "departmentName",
            "label": "legal.createdepartmentName",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.departmentName"
          },
          {
            "name": "advocateName",
            "jsonPath": "advocateName",
            "label": "legal.createadvocateName",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.advocateName"
          },
          {
            "name": "caseCategory",
            "jsonPath": "caseCategory",
            "label": "legal.createcaseCategory",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.caseCategory"
          },
          {
            "name": "fromDate",
            "jsonPath": "fromDate",
            "label": "legal.createfromDate",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.fromDate"
          },
          {
            "name": "toDate",
            "jsonPath": "toDate",
            "label": "legal.createtoDate",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.toDate"
          }
        ]
      }
    ],
    "result": {
      "header": [
        {
          "label": "legal.search.result.referenceNo"
        },
        {
          "label": "legal.search.result.advocateName"
        },
        {
          "label": "legal.search.result.isIndividual"
        }
      ],
      "values": [
        "code",
        "name",
        "isIndividual"
      ],
      "resultPath": "advocates[0]",
      "rowClickUrlUpdate": "/update/legalcase/{id}",
      "rowClickUrlView": "/view/legalcase/{id}"
    }
  },
  "legal.create": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "summons",
    "groups": [
      {
        "name": "CaseType",
        "label": "legal.create.group.title.CaseType",
        "fields": [
          {
            "name": "isSummon",
            "jsonPath": "summons[0].isSummon",
            "label": "legal.create.isSummon",
            "type": "radio",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "values": [
              {
                "label": "legal.create.Summon",
                "value": true
              },
              {
                "label": "legal.create.Warrant",
                "value": false
              }
            ]
          }
        ]
      },
      {
        "name": "CaseTypeDetails",
        "label": "legal.create.group.title.CaseTypeDetails",
        "fields": [
          // {
          //   "name": "orgnatedBYULB",
          //   "jsonPath": "summons[0].orgnatedBYULB",
          //   "label": "legal.create.orgnatedBYULB",
          //   "type": "checkbox",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "patternErrorMsg": ""
          // },
          {
            "name": "referenceNo",
            "jsonPath": "summons[0].summonReferenceNo",
            "label": "legal.create.referenceNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "summonDate",
            "jsonPath": "summons[0].summonDate",
            "label": "legal.create.summonDate",
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }, {
            "name": "side",
            "jsonPath": "summons[0].side.name",
            "label": "legal.create.side",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=side|$..code|$..name"
          },
          {
            "name": "caseType",
            "jsonPath": "summons[0].caseType.name",
            "label": "legal.create.caseType",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=caseType|$..code|$..name"
          }, {
            "name": "caseCategory",
            "jsonPath": "summons[0].caseCategory.name",
            "label": "legal.create.caseCategory",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=caseCategory|$..code|$..name"
          }, {
            "name": "caseNo",
            "jsonPath": "summons[0].caseNo",
            "label": "legal.create.caseNo",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "plantiffName",
            "jsonPath": "summons[0].plantiffName",
            "label": "legal.create.plantiffName",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "plantiffAddress",
            "jsonPath": "summons[0].plantiffAddress.addressLine1",
            "label": "legal.create.plantiffAddress",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "caseDetails",
            "jsonPath": "summons[0].caseDetails",
            "label": "legal.create.caseDetails",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "defendant",
            "jsonPath": "summons[0].defendant",
            "label": "legal.create.defendant",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "departmentName",
            "jsonPath": "summons[0].departmentName.code",
            "label": "legal.create.departmentName",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-common-masters/departments/_search?|$..code|$..name"
          },{
            "name": "year",
            "jsonPath": "summons[0].year",
            "label": "legal.create.year",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=year|$..code|$..name",
            "patternErrorMsg": ""
          }, 
          {
            "name": "hearingDate",
            "jsonPath": "summons[0].hearingDate",
            "label": "legal.create.hearingDate",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "hearingTime",
            "jsonPath": "summons[0].hearingTime",
            "label": "legal.create.hearingTime",
            "type": "number",
            "isNumber":true,
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "courtName",
            "jsonPath": "summons[0].courtName.name",
            "label": "legal.create.courtName",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=court|$..code|$..name"
          },{
            "name": "ward",
            "jsonPath": "summons[0].ward",
            "label": "legal.create.ward",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "bench",
            "jsonPath": "summons[0].bench.name",
            "label": "legal.create.bench",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=bench|$..code|$..name"
          },   
          {
            "name": "stamp",
            "jsonPath": "summons[0].stamp.name",
            "label": "legal.create.stamp",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=stamp|$..code|$..name"
          },
          {
            "name": "sectionApplied",
            "jsonPath": "summons[0].sectionApplied",
            "label": "legal.create.sectionApplied",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      },{
        "name": "UploadDocument",
        "label": "legal.create.group.title.UploadDocument",
        fields:[{
          "name":"UploadDocument",
          "jsonPath": "summons[0].documents",
          "label": "legal.create.sectionApplied",
           "type": "fileTable",
            "isRequired": false,
            "isDisabled": false,
            "patternErrMsg": "",
            "fileList":{
                "name":"documentName",
                "id":"fileStoreId"
            },
              "fileCount":3



        }]
      }
    ],
    "url": "/lcms-services/legalcase/summon/_create",
    "tenantIdRequired": true
  },
  "legal.view": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "summons",
    "groups": [
      {
        "name": "CaseType",
        "label": "legal.create.group.title.CaseType",
        "fields": [
          {
            "name": "isSummon",
            "jsonPath": "summons[0].isSummon",
            "label": "legal.create.isSummon",
            "type": "radio",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "values": [
              {
                "label": "legal.create.Summon",
                "value": true
              },
              {
                "label": "legal.create.Warrant",
                "value": false
              }
            ]
          }
        ]
      },
      {
        "name": "CaseTypeDetails",
        "label": "legal.create.group.title.CaseTypeDetails",
        "fields": [
          {
            "name": "referenceNo",
            "jsonPath": "summons[0].referenceNo",
            "label": "legal.create.referenceNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "summonDate",
            "jsonPath": "summons[0].summonDate",
            "label": "legal.create.summonDate",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "year",
            "jsonPath": "summons[0].year",
            "label": "legal.create.year",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 4,
            "patternErrorMsg": ""
          },
          {
            "name": "caseType",
            "jsonPath": "summons[0].caseType.name",
            "label": "legal.create.caseType",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=caseType|$..code|$..name"
          },
          {
            "name": "plantiffName",
            "jsonPath": "summons[0].plantiffName",
            "label": "legal.create.plantiffName",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "caseNo",
            "jsonPath": "summons[0].caseNo",
            "label": "legal.create.caseNo",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "plantiffAddress",
            "jsonPath": "summons[0].plantiffAddress",
            "label": "legal.create.plantiffAddress",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "caseDetails",
            "jsonPath": "summons[0].caseDetails",
            "label": "legal.create.caseDetails",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "defendant",
            "jsonPath": "summons[0].defendant",
            "label": "legal.create.defendant",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "departmentName",
            "jsonPath": "summons[0].departmentName.code",
            "label": "legal.create.departmentName",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-common-masters/departments/_search?|$..code|$..name"
          },
          {
            "name": "courtName",
            "jsonPath": "summons[0].courtName",
            "label": "legal.create.courtName",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=court|$..code|$..name"
          },
          {
            "name": "hearingDate",
            "jsonPath": "summons[0].hearingDate",
            "label": "legal.create.hearingDate",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "ward",
            "jsonPath": "summons[0].ward",
            "label": "legal.create.ward",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "hearingTime",
            "jsonPath": "summons[0].hearingTime",
            "label": "legal.create.hearingTime",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "bench",
            "jsonPath": "summons[0].bench",
            "label": "legal.create.bench",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=bench|$..code|$..name"
          },
          {
            "name": "side",
            "jsonPath": "summons[0].side",
            "label": "legal.create.side",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=side|$..code|$..name"
          },
          {
            "name": "stamp",
            "jsonPath": "summons[0].stamp",
            "label": "legal.create.stamp",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=stamp|$..code|$..name"
          },
          {
            "name": "sectionApplied",
            "jsonPath": "summons[0].sectionApplied",
            "label": "legal.create.sectionApplied",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "tenantIdRequired": true
  },
  "legal.update": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "summons",
     "searchUrl": "legalcase/_search?id={id}",
    "groups": [
      {
        "name": "CaseType",
        "label": "legal.create.group.title.CaseType",
        "fields": [
          {
            "name": "isSummon",
            "jsonPath": "summons[0].isSummon",
            "label": "legal.create.isSummon",
            "type": "radio",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "values": [
              {
                "label": "legal.create.Summon",
                "value": true
              },
              {
                "label": "legal.create.Warrant",
                "value": false
              }
            ]
          }
        ]
      },
      {
        "name": "CaseTypeDetails",
        "label": "legal.create.group.title.CaseTypeDetails",
        "fields": [
          {
            "name": "referenceNo",
            "jsonPath": "summons[0].referenceNo",
            "label": "legal.create.referenceNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "summonDate",
            "jsonPath": "summons[0].summonDate",
            "label": "legal.create.summonDate",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "year",
            "jsonPath": "summons[0].year",
            "label": "legal.create.year",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "maxLength": 4,
            "patternErrorMsg": ""
          },
          {
            "name": "caseType",
            "jsonPath": "summons[0].caseType",
            "label": "legal.create.caseType",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=caseType|$..code|$..name"
          },
          {
            "name": "plantiffName",
            "jsonPath": "summons[0].plantiffName",
            "label": "legal.create.plantiffName",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "caseNo",
            "jsonPath": "summons[0].caseNo",
            "label": "legal.create.caseNo",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "plantiffAddress",
            "jsonPath": "summons[0].plantiffAddress",
            "label": "legal.create.plantiffAddress",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "caseDetails",
            "jsonPath": "summons[0].caseDetails",
            "label": "legal.create.caseDetails",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "defendant",
            "jsonPath": "summons[0].defendant",
            "label": "legal.create.defendant",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "departmentName",
            "jsonPath": "summons[0].departmentName",
            "label": "legal.create.departmentName",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-common-masters/departments/_search?|$..code|$..name"
          },
          {
            "name": "courtName",
            "jsonPath": "summons[0].courtName",
            "label": "legal.create.courtName",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=court|$..code|$..name"
          },
          {
            "name": "hearingDate",
            "jsonPath": "summons[0].hearingDate",
            "label": "legal.create.hearingDate",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "ward",
            "jsonPath": "summons[0].ward",
            "label": "legal.create.ward",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "hearingTime",
            "jsonPath": "summons[0].hearingTime",
            "label": "legal.create.hearingTime",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
          {
            "name": "bench",
            "jsonPath": "summons[0].bench",
            "label": "legal.create.bench",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=bench|$..code|$..name"
          },
          {
            "name": "side",
            "jsonPath": "summons[0].side",
            "label": "legal.create.side",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=side|$..code|$..name"
          },
          {
            "name": "stamp",
            "jsonPath": "summons[0].stamp",
            "label": "legal.create.stamp",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=stamp|$..code|$..name"
          },
          {
            "name": "sectionApplied",
            "jsonPath": "summons[0].sectionApplied",
            "label": "legal.create.sectionApplied",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          }
        ]
      }
    ],
    "url": "/legalcase/_update",
    "tenantIdRequired": true
  }
}
 export default dat;