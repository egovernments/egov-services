var dat ={
  "legal.search": {
    "numCols": 4,
    "useTimestamp": true,
    "objectName": "cases",
    "url": "/lcms-services/legalcase/case/_search",
    "groups": [
      {
        "name": "search",
        "label": "legal.search.case.title",
        "fields": [
          {
            "name": "referenceNo",
            "jsonPath": "cases[0].summon.summonReferenceNo",
            "label": "legal.create.referenceNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.referenceNo"
          },
          {
            "name": "referenceCaseNo",
            "jsonPath": "cases[0].caseRefernceNo",
            "label": "caseRegistration.create.referenceCaseNo",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.referenceCaseNo"
          },
          {
            "name": "caseStatus",
            "jsonPath": "cases[0].hearingDetails.caseStatus",
            "label": "advocatepayment.create.caseStatus",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url":
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=caseStatus|$..code|$..name"
          },
          {
            "name": "caseType",
            "jsonPath": "cases[0].summon.caseType.name",
            "label": "legal.create.caseType",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url":
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=caseType|$..code|$..name"
          },
          {
            "name": "departmentName",
            "jsonPath": "cases[0].summon.departmentName.id",
            "label": "legal.create.departmentName",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "",
            "url": "/egov-common-masters/departments/_search?|$..id|$..name"
          },
          {
            "name": "advocateName",
            "type": "singleValueList",
              "label": "legal.create.advocateName",
            "jsonPath": "cases[0].advocateDetails[0].advocate.name",
            "isRequired": false,
            "isDisabled": false,
            "url":
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate|$..code|$..name"
          },
          {
            "name": "fromDate",
            "jsonPath": "fromDate",
            "label": "legal.create.fromDate",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": "legal.create.field.message.fromDate"
          },
          {
            "name": "toDate",
            "jsonPath": "toDate",
            "label": "legal.create.toDate",
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
          "label": "legal.create.referenceNo"
        },
        {
          "label":"caseRegistration.create.referenceCaseNo"
        },
        {
          "label": "advocatepayment.create.caseStatus"
        },
        {
          "label": "legal.create.departmentName"
        },
        {
          "label": "legal.create.caseType"
        },
        {
          "label": "legal.create.advocateName"
        }
      ],
      "values": [
        "summon.summonReferenceNo",
        "caseRefernceNo",
        "hearingDetails.caseStatus",
        "departmentName",
        "caseType",
        "advocateName"
      ],
      "resultPath": "cases",
      "rowClickUrlUpdate": "/update/legalcase/case/{id}",
      "rowClickUrlView": "/view/legalcase/case/{id}"
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
          {
            "name": "orignatedBYULB",
            "jsonPath": "summons[0].orignatedBYULB",
            "label": "legal.create.orignatedBYULB",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
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
            "type": "textarea",
            "fullWidth":true,
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
            "type": "text",
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
            "type": "textarea",
            "fullWidth":true,
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
            "type": "textarea",
            "fullWidth":true,
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