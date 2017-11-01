var dat = {
  "legal.create": {
    numCols: 12 / 3,
    url:
      "/lcms-services/legalcase/case/_vakalatnamageneration",
    tenantIdRequired: true,
    useTimestamp: true,
    objectName: "vakalatnama",
    groups: [
      {
        label: "legal.vakalatnama.create.group.title.generateVakalatnama",
        name: "Vakalatnama",
        fields: [
          {
            name: "caseNumber",
            jsonPath: "cases[0].code",
            label: "legal.vakalatnama.create.caseNumber",
            pattern: "",
            type: "text",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "referenceCaseNumber",
            jsonPath: "cases[0].caseRefernceNo",
            label: "legal.vakalatnama.create.referenceCaseNumber",
            pattern: "",
            type: "text",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "summonWarrantReferenceNumber",
            jsonPath: "cases[0].summon.summonReferenceNo",
            label: "legal.vakalatnama.create.summon_WarrantReferenceNumber",
            pattern: "",
            type: "text",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "Exhibit Number",
            jsonPath: "exhibitNumber",
            label: "legal.vakalatnama.create.exhibitNumber",
            pattern: "",
            type: "text",
            isRequired: false,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "department",
            jsonPath: "cases[0].summon.workFlowDetails.department",
            label: "legal.vakalatnama.create.department",
            pattern: "",
            type: "singleValueList",
            url: "/lcms-services/legalcase/advocate/_search?|$..code|$..name",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "chiefOfficerDetails",
            jsonPath: "ChiefOfficerDetails",
            label: "legal.vakalatnama.create.chiefOfficerDetails",
            pattern: "",
            type: "text",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "vakalatnamDate",
            jsonPath: "cases[0].days",
            label: "legal.vakalatnama.create.vakalatnamaDate",
            pattern: "",
            type: "text",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "departmentName",
            jsonPath: "cases[0].summon.departmentName",
            label: "legal.vakalatnama.create.departmentName",
            pattern: "",
            type: "text",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "courtName",
            jsonPath: "CourtName",
            label: "legal.vakalatnama.create.courtName",
            pattern: "",
            type: "text",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "hearingTime",
            jsonPath: "HearingTime",
            label: "legal.vakalatnama.create.hearingTime",
            pattern: "",
            type: "datePicker",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "witness",
            jsonPath: "witness",
            label: "legal.vakalatnama.create.witness",
            pattern: "",
            type: "text",
            isRequired: true,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "addWitness",
            jsonPath: "cases[0].witness",
            label: "legal.vakalatnama.create.addWitness",
            pattern: "",
            type: "arrayText",
            isRequired: false,
            isDisabled: false,
            requiredErrMsg: "",
            patternErrMsg: ""
          },
          {
            name: "GenerateVakalatnama",
            jsonPath: "cases[0].isVakalatnamaGenerated",
            label: "legal.vakalatnama.create.generateVakalatnama",
            pattern: "",
            type: "checkbox",
            isRequired: true,
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
