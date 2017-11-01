var dat =  {
  "legal.search": {
    numCols: 4,
    useTimestamp: true,
    objectName: "advocatepayments",
    url: "/legalcase/advocatepayment/_search",
    groups: [
      {
        name: "search",
        label: "legal.search.advocatepayment.title",
        fields: [
          // {
          //   label: "advocatepayment.createundefined",
          //   type: "",
          //   isDisabled: false,
          //   patternErrorMsg: "advocatepayment.create.field.message.undefined"
          // },
          // {
          //   name: "sortCase",
          //   jsonPath: "sortCase",
          //   label: "advocatepayment.createsortCase",
          //   type: "text",
          //   isDisabled: false,
          //   patternErrorMsg: "advocatepayment.create.field.message.sortCase"
          // },
          // {
          //   name: "ids",
          //   jsonPath: "ids",
          //   label: "advocatepayment.createids",
          //   type: "",
          //   isDisabled: false,
          //   patternErrorMsg: "advocatepayment.create.field.message.ids"
          // },
          {
            name: "advocateName",
            jsonPath: "advocatepayments[0].advocate.name",
            label: "legal.create.advocateName",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg:
              "advocatepayment.create.field.message.advocateName",
            url:
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate|$..code|$..name"
        
         },
          {
            name: "fromDate",
            jsonPath: "fromDate",
            label: "legal.create.fromDate",
            type: "number",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: "advocatepayment.create.field.message.fromDate"
          },
          {
            name: "toDate",
            jsonPath: "toDate",
             label: "legal.create.toDate",
            type: "number",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: "advocatepayment.create.field.message.toDate"
          }
        ]
      }
    ],
    result: {
      header: [
        {
          label: "advocatepayment.search.result.id"
        },
        {
          label: "advocatepayment.search.result.advocateName"
        },
        {
          label: "advocatepayment.search.result.demandDate"
        },
        {
          label: "advocatepayment.search.result.caseStatus"
        },
        {
          label: "advocatepayment.search.result.caseType"
        },
        {
          label: "advocatepayment.search.result.caseNo"
        },
        {
          label: "advocatepayment.search.result.isPartialPayment"
        },
        {
          label: "advocatepayment.search.result.amountRecived"
        }
      ],
      values: [
        "id",
        "advocateName",
        "demandDate",
        "caseStatus",
        "caseType",
        "caseNo",
        "isPartialPayment",
        "amountRecived"
      ],
      resultPath: "advocatepayments",
      rowClickUrlUpdate: "/update/advocatepayment/{id}",
      rowClickUrlView: "/view/advocatepayment/{id}"
    }
  },
  "legal.create": {
    numCols: 4,
    useTimestamp: true,
    objectName: "advocatepayments",
    groups: [
      {
        name: "AdvocatePaymentDetails",
        label: "advocatepayment.create.group.title.AdvocatePaymentDetails",
        fields: [
          {
            name: "advocateName",
            jsonPath: "advocatepayments[0].advocate.name",
            label: "advocatepayment.create.advocateName",
            type: "singleValueList",
            isRequired: false,
            isDisabled: false,
            "url": "/lcms-services/legalcase/advocate/_search?|$..code|$..name",
            patternErrorMsg: "",
            //  autoCompleteDependancy: {
						// 		"autoCompleteUrl": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate/_search?tenantId=default&name={value}",
						// 		"autoFillFields": {
            //       "advocatepayments[0].advocate.pan": "advocate.panNo",
            //       "advocatepayments[0].advocate.bankName": "advocate.bankName",
            //       "advocatepayments[0].advocate.bankBranch": "advocate.bankBranch",
            //       "advocatepayments[0].advocate.bankAccountNo": "advocate.bankAccountNo"
						// 		 }
						// 	 }
            depedants: [
              {
                jsonPath: "advocatepayments[0].advocate.pan",
                type: "dropDown",
                pattern:
                "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate/_search?tenantId=default&name={advocatepayments[0].advocate.name}|$..code|$..panNo",
                // "http://192.168.1.116:9090/lcms-services/legalcase/advocate/_search?advocateName={advocatepayments[0].advocate.name}|$..id|$..pan"
               },
              {
                jsonPath: "advocatepayments[0].advocate.bankName",
                type: "dropDown",
                pattern:
                  "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate/_search?tenantId=default&name={advocatepayments[0].advocate.name}|$..code|$..bankName",
              // },
              // {
              //   jsonPath: "advocatepayments[0].advocate.bankBranch",
              //   type: "dropDown",
              //   pattern:
              //     "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate&filter=[?(@.name == {advocatepayments[0].advocateName})]|$..code|$..bankBranch"
              // },
              // {
              //   jsonPath: "advocatepayments[0].bankAccountNo",
              //   type: "dropDown",
              //   pattern:
              //     "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate&filter=[?(@.name == {advocatepayments[0].advocateName})]|$..code|$..bankAccountNo"
              }
            ]
          },
          {
            name: "demandDate",
            jsonPath: "advocatepayments[0].demandDate",
            label: "advocatepayment.create.demandDate",
            type: "datePicker",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "bankName",
            jsonPath: "advocatepayments[0].advocate.bankName",
            label: "advocatepayment.create.bankName",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "panNo",
            jsonPath: "advocatepayments[0].advocate.pan",
            label: "advocatepayment.create.panNo",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "bankBranch",
            jsonPath: "advocatepayments[0].advocate.bankBranch",
            label: "advocatepayment.create.bankBranch",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "amountClaimed",
            jsonPath: "advocatepayments[0].amountClaimed",
            label: "advocatepayment.create.amountClaimed",
            type: "number",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "bankAccountNo",
            jsonPath: "advocatepayments[0].advocate.bankAccountNo",
            label: "advocatepayment.create.bankAccountNo",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "amountRecived",
            jsonPath: "advocatepayments[0].amountRecived",
            label: "advocatepayment.create.amountRecived",
            type: "number",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "voucherNumber",
            jsonPath: "advocatepayments[0].voucherNumber",
            label: "advocatepayment.create.voucherNumber",
            type: "number",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },{
              "name": "modeOfPayment",
              "jsonPath": "advocatepayments[0].modeOfPayment",
              "label": "legal.create.modeOfPayment",
              "type": "singleValueList",
              "isRequired": true,
              "isDisabled": false,
              "patternErrorMsg": "",
              "url": "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=stamp|$..code|$..name"
            }, {
            name: "instrumentNumber_InstrumentDate.",
            jsonPath: "advocatepayments[0].instrumentNumber_InstrumentDate",
            label: "advocatepayment.create.instrumentNumber_InstrumentDate",
            type: "datePicker",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },{
            name: "totalAmount",
            pattern: "",
            label: "legal.create.amount",
            type: "number",
            jsonPath: "assignAdvocate[0].totalAmount",
            isRequired: false,
            isDisabled: false
          }, {
                type: "tableList",
                jsonPath: "assignAdvocate",
                tableList: {
                  header: [
                    {
                      label: "legal.create.charge"
                    },
                    {
                      label: "legal.create.case"
                    },
                    {
                      label: "legal.create.amount"
                    }
                  ],
                  values: [
                    {
                      name: "charge",
                      pattern: "",
                      type: "singleValueList",
                      jsonPath: "assignAdvocate[0].charge",
                      isRequired: true,
                      isDisabled: false
                    },
                    {
                      name: "case",
                      pattern: "",
                      type: "singleValueList",
                      jsonPath: "assignAdvocate[0].case",
                      isRequired: false,
                      isDisabled: false
                    },
                    {
                      name: "amount",
                      pattern: "",
                      type: "number",
                      jsonPath: "assignAdvocate[0].amount",
                      isRequired: false,
                      isDisabled: false
                    }
                  ]
                }
              },{
            name: "advocateLabel",
            jsonPath: "advocatepayments[0].bankAccountNo",
            label: "Note: Total amount is window is valid and it is not necessarily same with amount mentioned in invoice document that is attached",
            type: "label",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
              }
        ]
      }
    ],
    url: "/legalcase/advocatepayment/_create",
    tenantIdRequired: true
  },
  "legal.view": {
    numCols: 4,
    useTimestamp: true,
    objectName: "advocatepayments",
    groups: [
      {
        name: "AdvocatePaymentDetails",
        label: "advocatepayment.create.group.title.AdvocatePaymentDetails",
        fields: [
          {
            name: "advocateName",
            jsonPath: "advocatepayments[0].advocateName",
            label: "advocatepayment.create.advocateName",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: "",
            depedants: [
              {
                jsonPath: "advocatepayments[0].panNo",
                type: "dropDown",
                pattern:
                  "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate&filter=[?(@.name == {advocatepayments[0].advocateName})]|$..code|$..panNo"
              },
              {
                jsonPath: "advocatepayments[0].bankName",
                type: "dropDown",
                pattern:
                  "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate&filter=[?(@.name == {advocatepayments[0].advocateName})]|$..code|$..bankName"
              },
              {
                jsonPath: "advocatepayments[0].bankBranch",
                type: "dropDown",
                pattern:
                  "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate&filter=[?(@.name == {advocatepayments[0].advocateName})]|$..code|$..bankBranch"
              },
              {
                jsonPath: "advocatepayments[0].bankAccountNo",
                type: "dropDown",
                pattern:
                  "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate&filter=[?(@.name == {advocatepayments[0].advocateName})]|$..code|$..bankAccountNo"
              }
            ]
          },
          {
            name: "demandDate",
            jsonPath: "advocatepayments[0].demandDate",
            label: "advocatepayment.create.demandDate",
            type: "number",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "caseNo",
            jsonPath: "advocatepayments[0].caseNo",
            label: "advocatepayment.create.caseNo",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            url:
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=caseno|$..code|$..name"
          },
          {
            name: "caseType",
            jsonPath: "advocatepayments[0].caseType",
            label: "advocatepayment.create.caseType",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            url:
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=caseType|$..code|$..name"
          },
          {
            name: "caseStatus",
            jsonPath: "advocatepayments[0].caseStatus",
            label: "advocatepayment.create.caseStatus",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            url:
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=caseStatus|$..code|$..name"
          },
          {
            name: "bankName",
            jsonPath: "advocatepayments[0].advocate.bankName",
            label: "advocatepayment.create.bankName",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "panNo",
            jsonPath: "advocatepayments[0].advocate.pan",
            label: "advocatepayment.create.panNo",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "bankBranch",
            jsonPath: "advocatepayments[0].advocate.bankBranch",
            label: "advocatepayment.create.bankBranch",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "amountClaimed",
            jsonPath: "advocatepayments[0].amountClaimed",
            label: "advocatepayment.create.amountClaimed",
            type: "number",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "bankAccountNo",
            jsonPath: "advocatepayments[0].advocate.bankAccountNo",
            label: "advocatepayment.create.bankAccountNo",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "amountRecived",
            jsonPath: "advocatepayments[0].amountRecived",
            label: "advocatepayment.create.amountRecived",
            type: "number",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "allowance",
            jsonPath: "advocatepayments[0].allowance",
            label: "advocatepayment.create.allowance",
            type: "number",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "isPartialPayment",
            jsonPath: "advocatepayments[0].isPartialPayment",
            label: "advocatepayment.create.isPartialPayment",
            type: "radio",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            values: [
              {
                label: "advocatepayment.create.PartialPayment",
                value: true
              },
              {
                label: "advocatepayment.create.FullPayment",
                value: false
              }
            ]
          }
        ]
      }
    ],
    tenantIdRequired: true
  },
  "legal.update": {
    numCols: 4,
    useTimestamp: true,
    objectName: "advocatepayments",
    groups: [
      {
        name: "AdvocatePaymentDetails",
        label: "advocatepayment.create.group.title.AdvocatePaymentDetails",
        fields: [
          {
            name: "advocateName",
            jsonPath: "advocatepayments[0].advocateName",
            label: "advocatepayment.create.advocateName",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: "",
            depedants: [
              {
                jsonPath: "advocatepayments[0].advocate.pan",
                type: "dropDown",
                pattern:
                  "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate&filter=[?(@.name == {advocatepayments[0].advocateName})]|$..code|$..panNo"
              },
              {
                jsonPath: "advocatepayments[0].advocate.bankName",
                type: "dropDown",
                pattern:
                  "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate&filter=[?(@.name == {advocatepayments[0].advocateName})]|$..code|$..bankName"
              },
              {
                jsonPath: "advocatepayments[0].advocate.bankBranch",
                type: "dropDown",
                pattern:
                  "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate&filter=[?(@.name == {advocatepayments[0].advocateName})]|$..code|$..bankBranch"
              },
              {
                jsonPath: "advocatepayments[0].advocate.bankAccountNo",
                type: "dropDown",
                pattern:
                  "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=advocate&filter=[?(@.name == {advocatepayments[0].advocateName})]|$..code|$..bankAccountNo"
              }
            ]
          },
          {
            name: "demandDate",
            jsonPath: "advocatepayments[0].demandDate",
            label: "advocatepayment.create.demandDate",
            type: "number",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "caseNo",
            jsonPath: "advocatepayments[0].caseNo",
            label: "advocatepayment.create.caseNo",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            url:
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=caseno|$..code|$..name"
          },
          {
            name: "caseType",
            jsonPath: "advocatepayments[0].caseType",
            label: "advocatepayment.create.caseType",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            url:
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=caseType|$..code|$..name"
          },
          {
            name: "caseStatus",
            jsonPath: "advocatepayments[0].caseStatus",
            label: "advocatepayment.create.caseStatus",
            type: "singleValueList",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            url:
              "/egov-mdms-service/v1/_get?&moduleName=lcms&masterName=caseStatus|$..code|$..name"
          },
          {
            name: "bankName",
            jsonPath: "advocatepayments[0].advocate.bankName",
            label: "advocatepayment.create.bankName",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "panNo",
            jsonPath: "advocatepayments[0].advocate.pan",
            label: "advocatepayment.create.panNo",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "bankBranch",
            jsonPath: "advocatepayments[0].advocate.bankBranch",
            label: "advocatepayment.create.bankBranch",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "amountClaimed",
            jsonPath: "advocatepayments[0].amountClaimed",
            label: "advocatepayment.create.amountClaimed",
            type: "number",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "bankAccountNo",
            jsonPath: "advocatepayments[0].advocate.bankAccountNo",
            label: "advocatepayment.create.bankAccountNo",
            type: "text",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "amountRecived",
            jsonPath: "advocatepayments[0].amountRecived",
            label: "advocatepayment.create.amountRecived",
            type: "number",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "voucherNumber",
            jsonPath: "advocatepayments[0].voucherNumber",
            label: "advocatepayment.create.voucherNumber",
            type: "number",
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: ""
          },
          {
            name: "isPartialPayment",
            jsonPath: "advocatepayments[0].isPartialPayment",
            label: "advocatepayment.create.isPartialPayment",
            type: "radio",
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: "",
            values: [
              {
                label: "advocatepayment.create.PartialPayment",
                value: true
              },
              {
                label: "advocatepayment.create.FullPayment",
                value: false
              }
            ]
          }
        ]
      }
    ],
    url: "/legalcase/advocatepayment/_update",
    tenantIdRequired: true
  }
};
export default dat;
