var dat = {
  "legal.create": {
    numCols: 6,
    url:
      "/lcms-services/legalcase/parawisecomment/_create",
    tenantIdRequired: true,
    useTimestamp: true,
    objectName: "parawiseComments",
    groups: [
      {
        label: "legal.parawisecomments.create.group.title.viewDetails",
        name: "viewDetails",
        fields: [
          {
            name: "summonWarrantDetails",
            jsonPath: "summonWarrantDetails",
            label: "legal.parawisecomments.create.summonWarrantDetails",
            pattern: "",
            type: "button",
            isRequired: false,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          }
        ]
      },
      {
        label: "legal.parawisecomments.create.group.title.parawiseComment",
        name: "parawiseComments",
        fields: [
          {
            name: "parawiseCommentsAskedDate",
            jsonPath: "cases[0].parawiseComments[0].parawiseCommentsAskedDate",
            label: "legal.parawisecomments.create.dateOfCommentsAsked",
            pattern: "",
            type: "datePicker",
            isRequired: false,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "parawiseCommentsReceivedDate",
            jsonPath:
              "cases[0].parawiseComments[0].parawiseCommentsReceivedDate",
            label: "legal.parawisecomments.create.dateOfCommentsReceived",
            pattern: "",
            type: "datePicker",
            isRequired: false,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "hodProvidedDate",
            jsonPath: "cases[0].parawiseComments[0].hodProvidedDate",
            label: "legal.parawisecomments.create.dateOfInfoProvidedByHod",
            pattern: "",
            type: "datePicker",
            isRequired: false,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "resolutionDate",
            jsonPath: "cases[0].parawiseComments[0].resolutionDate",
            label: "legal.parawisecomments.create.resolutionDate",
            pattern: "",
            type: "datePicker",
            isRequired: false,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "referenceNo",
            jsonPath: "cases[0].caseRefernceNo",
            label: "legal.parawisecomments.create.referenceNo",
            pattern: "",
            type: "text",
            isRequired: false,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "paraWiseComments",
            jsonPath: "cases[0].parawiseComments[0].paraWiseComments",
            label: "legal.parawisecomments.create.group.parawiseComments",
            pattern: "",
            type: "textarea",
            isRequired: false,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          }
        ]
      },
      {
        name: "documents",
        label: "legal.parawisecomments.create.group.title.uploadDocument",
        fields: [
          {
            name: "documents",
            jsonPath: "cases[0].parawiseComments[0].documents",
            label: "legal.create.sectionApplied",
            type: "fileTable",
            isRequired: false,
            isDisabled: false,
            patternErrMsg: "",
            fileList: {
              name: "docName",
              id: "fileStoreid"
            },
            fileCount: 3
          }
        ]
      },
      {
        name: "action",
        label: "legal.parawisecomments.create.group.title.action",
        fields: [
          {
            name: "approve",
            jsonPath: "parawiseComments[0].approve",
            label: "",
            type: "radio",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: "",
            values: [
              {
                label: "legal.parawisecomments.create.approve",
                value: true
              },
              {
                label: "legal.parawisecomments.create.reject",
                value: false
              }
            ]
          },
          {
            name: "forwardTo",
            jsonPath: "parawiseComments[0].forwardTo",
            label: "legal.parawisecomments.create.forwardTo",
            url: "",
            pattern: "",
            type: "singleValueList",
            isRequired: false,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          }
        ]
      }
    ]
  }
};

export default dat;
