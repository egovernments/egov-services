var dat = {
  'perfManagement.create': {
    numCols: 12 / 2,
    url: 'perfmanagement/v1/kpivalue/_create',
    useTimestamp: true,
    objectName: 'kpiValues',
    tenantIdRequired: true,
    groups: [
      {
        label: 'Key Performance Indicator Master',
        name: 'kpi',
        fields: [
          {
            name: 'kpiselect',
            jsonPath: 'kpiValues[0].KPI.code',
            label: 'KPI',
            url: '/perfmanagement/v1/kpimaster/_search?tenantId=|$.KPIs.*.code|$.KPIs.*.name',
            isRequired: true,
            pattern: '',
            type: 'singleValueList',
            isDisabled: false,
            requiredErrMsg: '',
            depedants: [
              {
                jsonPath: 'kpiValues[0].financialYear',
                type: 'textField',
                pattern: 'kpiValues[0].KPI.code|KPIs|code|financialYear',
                hasFromDropDownOriginalData: true,
                /*"pattern": "egf-master/financialyears/_search?tenantId=default|$.financialYears.*.finYearRange|$.financialYears.*.finYearRange"*/
              },
              {
                jsonPath: 'kpiValues[0].documents',
                type: 'documentList',
                pattern: '/perfmanagement/v1/kpimaster/_getDocumentForKpi?tenantId=default&kpiCode={KPIs}',
                //"url": "/perfmanagement/v1/kpimaster/_getDocumentForKpi?kpiCode=CODEEGOV"
              },
              {
                jsonPath: 'kpiValues[0].targetType',
                type: 'radio',
                pattern: 'kpiValues[0].KPI.code|KPIs|code|targetType',
                hasFromDropDownOriginalData: true,
              },
              // {
              // "jsonPath": "kpiValues[0].tenantId",
              // "type": "documentList",
              // "pattern": localStorage.tenantId
              // }
            ],
          },
          {
            name: 'kpiDateBlock',
            jsonPath: 'kpiValues[0].financialYear',
            label: 'Financial Year',
            // "pattern": "^[\s.]*([^\s.][\s.]*){0,250}$",
            type: 'text',
            isRequired: false,
            isDisabled: true,
            requiredErrMsg: '',
            patternErrMsg: 'Length is more than 250',
          },
          {
            name: 'kpitype',
            jsonPath: 'kpiValues[0].targetType',
            label: 'perfManagement.create.KPIs.groups.kpitype',
            pattern: '',
            type: 'radio',
            isRequired: false,
            isDisabled: true,
            requiredErrMsg: '',
            patternErrMsg: '',
            values: [
              {
                label: 'perfManagement.update.KPIs.groups.updatekpitype.text',
                value: 'TEXT',
              },
              {
                label: 'perfManagement.create.KPIs.groups.kpitype.value',
                value: 'VALUE',
              },
              {
                label: 'perfManagement.create.KPIs.groups.kpitype.objective',
                value: 'OBJECTIVE',
              },
            ],
            defaultValue: true,
            showHideFields: [
              {
                ifValue: 'OBJECTIVE',

                hide: [
                  {
                    name: 'kpiTargetBlock',
                    isGroup: true,
                    isField: false,
                  },
                  {
                    name: 'kpiTargetTextBlock',
                    isGroup: true,
                    isField: false,
                  },
                ],
                show: [
                  {
                    name: 'kpiTargetRadioBlock',
                    isGroup: true,
                    isField: false,
                  },
                ],
              },
              {
                ifValue: 'VALUE',
                hide: [
                  {
                    name: 'kpiTargetRadioBlock',
                    isGroup: true,
                    isField: false,
                  },
                  {
                    name: 'kpiTargetTextBlock',
                    isGroup: true,
                    isField: false,
                  },
                ],
                show: [
                  {
                    name: 'kpiTargetBlock',
                    isGroup: true,
                    isField: false,
                  },
                ],
              },
              {
                ifValue: 'TEXT',
                hide: [
                  {
                    name: 'kpiTargetRadioBlock',
                    isGroup: true,
                    isField: false,
                  },
                  {
                    name: 'kpiTargetBlock',
                    isGroup: true,
                    isField: false,
                  },
                ],
                show: [
                  {
                    name: 'kpiTargetTextBlock',
                    isGroup: true,
                    isField: false,
                  },
                ],
              },
            ],
          },
        ],
      },
      {
        label: 'Text',
        name: 'kpiTargetTextBlock',
        hide: false,
        multiple: false,
        fields: [
          {
            name: 'kpiTargetText',
            jsonPath: 'kpiValues[0].targetDescription',
            label: '',
            pattern: '',
            type: 'text',
            isDisabled: false,
            requiredErrMsg: '',
          },
        ],
      },
      {
        label: 'perfManagement.update.KPIs.groups.updatekpiTargetBlock',
        name: 'kpiTargetBlock',
        hide: true,
        multiple: false,
        fields: [
          {
            name: 'kpiTarget',
            //"hide":false,
            jsonPath: 'kpiValues[0].targetValue',
            label: '',
            pattern: '[0-9]',
            type: 'text',
            isDisabled: false,
            patternErrMsg: 'Please enter a valid number',
            requiredErrMsg: '',
          },
        ],
      },
      {
        label: 'perfManagement.create.KPIs.groups.kpiTargetRadioBlock',
        name: 'kpiTargetRadioBlock',
        hide: true,
        multiple: false,
        fields: [
          {
            name: 'kpiTargetRadio',
            //"hide":true,
            jsonPath: 'kpiValues[0].targetValue',
            label: '',
            pattern: '',
            type: 'radio',
            isRequired: false,
            isDisabled: false,
            requiredErrMsg: '',
            patternErrMsg: '',
            values: [
              {
                label: 'perfManagement.create.KPIs.groups.kpiTargetRadioBlock.yes',
                value: 1,
              },
              {
                label: 'perfManagement.create.KPIs.groups.kpiTargetRadioBlock.no',
                value: 2,
              },
              {
                label: 'perfManagement.create.KPIs.groups.kpiTargetRadioBlock.inprogress',
                value: 3,
              },
            ],
          },
        ],
      },

      {
        name: 'UploadDocument',
        label: 'legal.create.group.title.UploadDocument',
        fields: [
          {
            name: 'File',
            jsonPath: 'kpiValues[0].documents',
            type: 'documentList',
            pathToArray: 'documentTypes',
            displayNameJsonPath: 'name',
            url: '/tl-masters/documenttype/v2/_search',
            autoFillFields: [
              {
                name: 'documentTypeId',
                //"url": "/tl-masters/documenttype/v2/_search",
                jsonPath: 'id',
              },
            ],
          },
        ],
      },
    ],
  },
  'perfManagement.search': {
    numCols: 12 / 2,
    url: 'perfmanagement/v1/kpivalue/_search',
    useTimestamp: true,
    objectName: 'kpiValues',
    groups: [
      {
        name: 'searchkpiCode',
        jsonPath: 'kpiCode',
        label: 'kpiCode',
        pattern: '',
        type: 'text',
        isDisabled: false,
        requiredErrMsg: '',
      },
    ],
    result: {
      header: [{ label: 'Document' }, { label: 'Financial Year' }, { label: 'KPI Name' }],
      values: ['name', 'code', 'targetValue'],
      resultPath: 'KPIs',
      rowClickUrlUpdate: '/update/perfManagement/actualKpiCreate/{code}',
      //"rowClickUrlUpdate": "/create/perfManagement/actualKpiCreate/{code}"
    },
  },
  // "perfManagement.search": {
  //   "numCols": 12/3,
  //   "url": "/perfmanagement/v1/kpimaster/_search",
  //   "useTimestamp": true,
  //   "objectName": "KPIs",
  //   "groups": [
  //    {
  //       "label": "Search Key Performance Indicator",
  //       "name": "kpiDate",
  //       "fields": [
  //       {
  //             "name": "searchkpiName",
  //             "jsonPath": "tenantIdCustom",
  //             "label": "Tenant",
  //             "pattern": "",
  //             "type": "singleValueList",
  //             "url": "/tenant/v1/tenant/_search?tenantId=|$.tenant.*.code|$.tenant.*.name",
  //             "isDisabled": false,
  //             "requiredErrMsg": ""
  //         },
  //         {
  //             "name": "searchkpiDate",
  //             "jsonPath": "kpiCode",
  //             "label": "kpiCode",
  //             "pattern": "",
  //             "type": "text",
  //             "isDisabled": false,
  //             "requiredErrMsg": ""
  //           },
  //           {
  //             "name": "searchkpiName",
  //             "jsonPath": "kpiName",
  //             "label": "kpiName",
  //             "pattern": "",
  //             "type": "text",
  //             "isDisabled": false,
  //             "requiredErrMsg": ""
  //           }
  //       ]
  //     }
  //      ],
  //      "result": {
  //     "header": [{label: "Document"},{label: "Financial Year"},{label: "KPI Name"}],
  //     "values": ["name","code", "targetValue"],
  //     "resultPath": "KPIs",
  //    // "rowClickUrlUpdate": "/update/perfManagement/actualKpiCreate/{code}"
  //    "rowClickUrlUpdate": "/create/perfManagement/actualKpiCreate/{code}"
  //     }
  // },
  // "perfManagement.update": {
  //   "numCols": 12/2,
  //   "searchUrl": "/perfmanagement/v1/kpimaster/_search?kpiCode={code}",
  //   "url":"/wcms/masters/documenttypes-applicationtypes/_update",
  //   "tenantIdRequired": true,
  //   "useTimestamp": true,
  //   "objectName": "KPIs[0]",
  //   "groups": [
  //     {
  //       "label": "Create Actual Key Performance Indicator",
  //       "name": "kpiDate",
  //       "fields": [
  //       {
  //       "name": "kpitype",
  //       "jsonPath": "KPIs[0].type",
  //       "label": "KPI Type",
  //       "pattern": "",
  //       "type": "radio",
  //       "isRequired": false,
  //       "isDisabled": false,
  //       "requiredErrMsg": "",
  //       "patternErrMsg": "",
  //       "values": [{
  //         "label": "KPI Value Number",
  //         "value": true
  //       }, {
  //         "label": "KPI Value Objective Type",
  //         "value": false
  //       }],
  //       "defaultValue": true,
  //       "showHideFields": [{
  //         "ifValue": false,
  //         "hide": [{
  //           "name": "kpiTarget",
  //           "isGroup": false,
  //           "isField": true
  //         }],
  //         "show": [{
  //           "name": "kpiTargetRadio",
  //           "isGroup": false,
  //           "isField": true
  //         }]
  //       }]
  //     },
  //       {
  //             "name": "updatekpiTenant",
  //             "jsonPath": "KPIs[0].tenantIdCustom",
  //             "label": "Tenant",
  //             "isRequired": true,
  //             "pattern": "",
  //             "type": "singleValueList",
  //             "isDisabled": false,
  //             "requiredErrMsg": "",
  //             "url": "/tenant/v1/tenant/_search?tenantId=|$.tenant.*.code|$.tenant.*.name",
  //             "depedants": [{
  //               "jsonPath": "KPIs[0].department.code",
  //               "type": "dropDown",
  //               "pattern": "egov-mdms-service/v1/_get?tenantId=default&tenantIdCustom={KPIs[0].tenantId}&moduleName=common-masters&masterName=Department|$..code|$..name"
  //             }]
  //           },
  //           {
  //           "name": "kpiDepartment",
  //           "jsonPath": "KPIs[0].department.code",
  //           "url": "egov-mdms-service/v1/_get?tenantId=default&tenantIdCustom={KPIs[0].tenantId}&moduleName=common-masters&masterName=Department|$..code|$..name",
  //           "label": "Department",
  //           "pattern": "",
  //           "type": "singleValueList",
  //           "isDisabled": true,
  //           "requiredErrMsg": ""
  //         },
  //       {
  //             "name": "updatekpiDate",
  //             "jsonPath": "KPIs[0].kpiList[0].financialYear",
  //             "label": "Financial Year",
  //             "isRequired": true,
  //             "pattern": "",
  //             "type": "singleValueList",
  //             "url": "egf-master/financialyears/_search?tenantId=default|$.financialYears.*.finYearRange|$.financialYears.*.finYearRange",
  //             "isDisabled": true,
  //             "requiredErrMsg": ""
  //           },
  //           {
  //             "name": "updatekpiName",
  //             "jsonPath": "KPIs[0].name",
  //             "label": "KPI Name",
  //             "isRequired": true,
  //             "pattern": "",
  //             "type": "text",
  //             "isDisabled": true,
  //             "requiredErrMsg": ""
  //           },
  //           {
  //             "name": "updatekpiTarget",
  //             "jsonPath": "KPIs[0].targetValue",
  //             "label": "Target Value",
  //             "pattern": "",
  //             "type": "text",
  //             "isDisabled": true,
  //             "requiredErrMsg": ""
  //           },
  //           {
  //             "name": "updatekpiTargetRadio",
  //             "hide": true,
  //             "jsonPath": "KPIs[0].targetValue",
  //             "label": "Target Value",
  //             "pattern": "",
  //             "type": "radio",
  //             "isRequired": false,
  //             "isDisabled": true,
  //             "requiredErrMsg": "",
  //             "patternErrMsg": "",
  //             "values": [{
  //               "label": "Yes",
  //               "value": 1
  //             }, {
  //               "label": "No",
  //               "value": 2
  //             },{
  //               "label": "Inprogress",
  //               "value": 3
  //             }],
  //             "defaultValue": true
  //           },
  //           {
  //             "name": "updatekpiInstruction",
  //             "jsonPath": "KPIs[0].instructions",
  //             "label": "Instruction to Achieve Target",
  //             "pattern": "",
  //             "type": "textarea",
  //             "fullWidth":true,
  //             "isDisabled": true,
  //             "requiredErrMsg": ""
  //           },
  //           {
  //             "name": "updateKpiActual",
  //             "jsonPath": "KPIs[0].resultValue",
  //             "label": "Actual Value",
  //             "pattern": "",
  //             "type": "number",
  //             "isDisabled": false,
  //             "requiredErrMsg": ""
  //           }
  //           ]
  //       },
  //       {
  //        "name": "UploadDocument",
  //        "label": "legal.create.group.title.UploadDocument",
  //        fields:[{
  //             "name": "File",
  //             "jsonPath": "DocumentTypeApplicationTypes[0].supportDocuments",
  //             "type": "documentList",
  //             "pathToArray": "documentTypes",
  //             "displayNameJsonPath": "name",
  //             "url": "/tl-masters/documenttype/v2/_search",
  //             "autoFillFields": [
  //               {
  //                 "name": "documentTypeId",
  //                 "jsonPath": "id"
  //               }
  //             ]
  //           }]
  // }
  //       ]
  // }
};
export default dat;
