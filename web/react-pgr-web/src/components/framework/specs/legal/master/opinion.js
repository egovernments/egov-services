var dat = {
  "legal.search": {
    numCols: 4,
    useTimestamp: true,
    objectName: "",
    url: "/lcms-services/legalcase/opinion/_search",
    groups: [
      {
        name: "search",
        label: "opinion.search.title",
        fields: [
          {
            label: "opinion.createundefined",
            type: "",
            isDisabled: false,
            patternErrorMsg: "opinion.create.field.message.undefined"
          },
          {
            name: "sortProperty",
            jsonPath: "sort",
            label: "opinion.createsortProperty",
            type: "text",
            isDisabled: false,
            patternErrorMsg: "opinion.create.field.message.sortProperty"
          },
          {
            name: "ids",
            jsonPath: "ids",
            label: "opinion.createids",
            type: "",
            isDisabled: false,
            patternErrorMsg: "opinion.create.field.message.ids"
          },
          {
            name: "opinionRequestDate",
            jsonPath: "opinionRequestDate",
            label: "opinion.createopinionRequestDate",
            type: "number",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: "opinion.create.field.message.opinionRequestDate"
          },
          {
            name: "opinionBy",
            jsonPath: "opinionBy",
            label: "opinion.createopinionBy",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: "opinion.create.field.message.opinionBy"
          },
          {
            name: "departMentName",
            jsonPath: "departMentName",
            label: "opinion.createdepartMentName",
            type: "singleValueList",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: "opinion.create.field.message.departMentName"
          }
        ]
      }
    ],
    result: {
      header: [
        { label: "opinion.search.result.id" },
        { label: "opinion.search.result.departmentName" },
        { label: "opinion.search.result.opinionOn" }
      ],
      values: ["id", "departMentName", "opinionOn"],
      resultPath: "legleCases",
      rowClickUrlUpdate: "/update/opinion/{id}",
      rowClickUrlView: "/view/opinion/{id}"
    }
  },
  "legal.create": {
    numCols: 4,
    useTimestamp: true,
    objectName: "opinions",
    groups: [
      {
        name: "OpinionRequest",
        label: "opinion.create.group.title.OpinionRequest",
        fields: [
          {
            name: "opinionRequestDate",
            jsonPath: "opinions[0].opinionRequestDate",
            label: "opinion.create.opinionRequestDate",
            type: "datePicker",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "departmentName",
            jsonPath: "opinions[0].departmentName",
            label: "opinion.create.departmentName",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            url: "/egov-common-masters/departments/_search?|$..code|$..name"
          },{
            name: "Case",
            jsonPath: "opinions[0].case",
            label: "opinion.create.case",
            type: "singleValueList",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: "",
            url: "/lcms-services/legalcase/case/_search?|$..summon.summonReferenceNo|$..summon.caseNo"
          },
          {
            name: "opinionOn",
            jsonPath: "opinions[0].opinionOn",
            label: "opinion.create.opinionOn",
            type: "textarea",
            fullWidth:true,
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: ""
          }
        ]
      },{
        "name": "UploadDocument",
        "label": "legal.create.group.title.UploadDocument",
        fields:[ {
          "name": "UploadDocument",
          "jsonPath": "opinions[0].documents",
          "label": "legal.create.sectionApplied",
          "type": "fileTable",
          "isRequired": false,
          "isDisabled": false,
          "patternErrMsg": "",
          "fileList": {
            "name": "documentName",
            "id": "fileStoreId"
          },
          "fileCount": 3
        }]
      }
    ],
    url: "/lcms-services/legalcase/opinion/_create",
    tenantIdRequired: true
  },
  "legal.view": {
    numCols: 4,
    useTimestamp: true,
    objectName: "opinions",
    groups: [
      {
        name: "OpinionRequest",
        label: "opinion.create.group.title.OpinionRequest",
        fields: [
          {
            name: "opinionRequestDate",
            jsonPath: "opinions[0].opinionRequestDate",
            label: "opinion.create.opinionRequestDate",
            type: "number",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "departmentName",
            jsonPath: "opinions[0].departmentName",
            label: "opinion.create.departmentName",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            url: "/egov-common-masters/departments/_search?|$..code|$..name"
          },
          {
            name: "opinionOn",
            jsonPath: "opinions[0].opinionOn",
            label: "opinion.create.opinionOn",
            type: "text",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: ""
          }
        ]
      }
    ],
    tenantIdRequired: true
  },
  "legal.update": {
    numCols: 4,
    useTimestamp: true,
    objectName: "opinions",
    groups: [
      {
        name: "OpinionRequest",
        label: "opinion.create.group.title.OpinionRequest",
        fields: [
          {
            name: "opinionRequestDate",
            jsonPath: "opinions[0].opinionRequestDate",
            label: "opinion.create.opinionRequestDate",
            type: "number",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "departmentName",
            jsonPath: "opinions[0].departmentName",
            label: "opinion.create.departmentName",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            url: "/egov-common-masters/departments/_search?|$..code|$..name"
          },
          {
            name: "opinionOn",
            jsonPath: "opinions[0].opinionOn",
            label: "opinion.create.opinionOn",
            type: "text",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: ""
          }
        ]
      }
    ],
    url: "//opinion/_update",
    tenantIdRequired: true
  }
};
export default dat;
