var dat = {
  "legal.update": {
    numCols: 4,
    useTimestamp: true,
    objectName: "cases",
    searchUrl:
      "http://192.168.1.116:9090/lcms-services/legalcase/case/_search?code={id}",
    groups: [
      {
        name: "CaseTypeDetails",
        label: "legal.create.group.title.CaseTypeDetails",
        fields: [
          {
            name: "referenceNo",
            jsonPath: "cases[0].summon.summonReferenceNo",
            label: "legal.create.referenceNo",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "summonDate",
            jsonPath: "cases[0].summon.summonDate",
            label: "legal.create.summonDate",
            type: "datePicker",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "year",
            jsonPath: "cases[0].summon.year",
            label: "legal.create.year",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            url:
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=year|$..code|$..name",
            patternErrorMsg: ""
          },
          {
            name: "caseType",
            jsonPath: "cases[0].summon.caseType.name",
            label: "legal.create.caseType",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            url:
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=caseType|$..code|$..name"
          },
          {
            name: "plantiffName",
            jsonPath: "cases[0].summon.plantiffName",
            label: "legal.create.plantiffName",
            type: "text",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "caseNo",
            jsonPath: "cases[0].summon.caseNo",
            label: "legal.create.caseNo",
            type: "text",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "plantiffAddress",
            jsonPath: "cases[0].summon.plantiffAddress.addressLine1",
            label: "legal.create.plantiffAddress",
            type: "text",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "caseDetails",
            jsonPath: "cases[0].summon.caseDetails",
            label: "legal.create.caseDetails",
            type: "text",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "defendant",
            jsonPath: "cases[0].summon.defendant",
            label: "legal.create.defendant",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "departmentName",
            jsonPath: "cases[0].summon.departmentName.id",
            label: "legal.create.departmentName",
            type: "singleValueList",
            isRequired: true,
            isDisabled: true,
            patternErrorMsg: "",
            url: "/egov-common-masters/departments/_search?|$..code|$..name"
          },
          {
            name: "courtName",
            jsonPath: "cases[0].summon.courtName.name",
            label: "legal.create.courtName",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            url:
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=court|$..code|$..name"
          },
          {
            name: "hearingTime",
            jsonPath: "cases[0].summon.hearingTime",
            label: "legal.create.hearingTime",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "hearingDate",
            jsonPath: "cases[0].summon.hearingDate",
            label: "legal.create.hearingDate",
            type: "datePicker",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "side",
            jsonPath: "cases[0].summon.side.name",
            label: "legal.create.side",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            url:
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=side|$..code|$..name"
          },
          {
            name: "ward",
            jsonPath: "cases[0].summon.ward",
            label: "legal.create.ward",
            type: "text",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "stamp",
            jsonPath: "cases[0].summon.stamp.name",
            label: "legal.create.stamp",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            url:
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=stamp|$..code|$..name"
          },
          {
            name: "bench",
            jsonPath: "cases[0].summon.bench.name",
            label: "legal.create.bench",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            url:
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=bench|$..code|$..name"
          },
          {
            name: "sectionApplied",
            jsonPath: "cases[0].summon.sectionApplied",
            label: "legal.create.sectionApplied",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          }
        ]
      },
      {
        name: "UploadDocument",
        label: "legal.create.group.title.UploadDocument",
        fields: [
          {
          "name":"UploadDocument",
          "jsonPath": "cases[0].summon.documents",
          "label": "legal.create.sectionApplied",
           "type": "fileTable",
            "isRequired": false,
            "isDisabled": false,
            
            "patternErrMsg": "",
            "fileList":{
                "name":"documentName",
                "id":"fileStoreId"
            },
              // "fileCount":3



        }
        ]
      },
      {
        name: "assignAdvocate",
        label: "legal.create.group.title.assignAdvocate",
        fields: [
          {
            type: "tableList",
            jsonPath: "cases[0].summon.advocateDetails",
            tableList: {
              header: [
                {
                  label: "legal.create.advocateName"
                },
                {
                  label: "legal.create.advocateAssignDate"
                },
                {
                  label: "legal.create.advocateFee"
                }
              ],
              values: [
                {
                  name: "advocateName",
                  pattern: "",
                  type: "singleValueList",
                  jsonPath: "cases[0].advocateDetails[0].advocate.name",
                  isRequired: true,
                  isDisabled: false,
                  url:
                    "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate|$..code|$..name"
                },
                {
                  name: "advocateAssignDate",
                  pattern: "",
                  type: "datePicker",
                  jsonPath: "cases[0].advocateDetails[0].advocateAssignDate",
                  isRequired: true,
                  isDisabled: false
                },
                {
                  name: "advocateFee",
                  pattern: "",
                  type: "text",
                  jsonPath: "assignAdvocate[0].advocateFee",
                  isRequired: true,
                  isDisabled: false
                }
              ]
            }
          }
        ]
      }
    ],
    url:
      "http://192.168.1.116:9090/lcms-services/legalcase/summon/_assignadvocate",
    tenantIdRequired: true
  }
};
export default dat;
