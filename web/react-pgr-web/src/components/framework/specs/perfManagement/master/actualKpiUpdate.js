var dat = {
    "perfManagement.search": {
    "numCols": 12/2,
    "url": "/perfmanagement/v1/kpivalue/_search",
    "useTimestamp": true,
    "objectName": "kpiValues",
    "groups": [
     {
        "label": "Search Key Performance Indicator",
        "name": "kpi",
        "fields": [
        {
              "name": "searchKpi",
              "jsonPath": "kpiCodes",
              "label": "KPI",
              "pattern": "",
              "type": "singleValueList",
              "isRequired":true,
              "url":"/perfmanagement/v1/kpimaster/_search?tenantId=default|$.KPIs.*.code|$.KPIs.*.name",
              "isDisabled": false,
              "requiredErrMsg": ""
          },
          {
              "name": "searchActualKpiDate",
              "jsonPath": "finYear",
              "label": "Financial Year",
              "pattern": "",
              "type": "multiValueList",
              "isRequired":true,
              "url": "egf-master/financialyears/_search?tenantId=default|$.financialYears.*.finYearRange|$.financialYears.*.finYearRange",
              "isDisabled": false,
              "requiredErrMsg": ""
            }
        ]
      }
       ],
       "result": {
      "header": [{label: "KPI Name"},{label: "Financial Year"},{label:"Target Value"},{label: "Actual Value"}],
      "values": ["kpi.name","kpi.financialYear","kpi.targetDescription", "resultDescription"],
      "resultPath": "kpiValues",
      "rowClickUrlUpdate": "/update/perfManagement/actualKpiUpdate/{kpi.code}?finYear={kpi.financialYear}",
      "rowClickUrlView": "/view/perfManagement/actualKpiUpdate/{kpi.code}?finYear={kpi.financialYear}"
      }
  },
  "perfManagement.update": {
    "numCols": 12/2,
    "searchUrl": "/perfmanagement/v1/kpivalue/_search?kpiCodes={code}&finYear={kpi.financialYear}",
    "url":"perfmanagement/v1/kpivalue/_update",
    "tenantIdRequired": true,
    "useTimestamp": true,
    "objectName": "kpiValues",
    "groups": [
      {
        "label": "Update Actual Key Performance Indicator",
        "name": "kpiDate",
        "fields": [
        {
              "name": "updateActualKpiDepartment",
              "jsonPath": "kpiValues[0].kpi.code",
              "label": "KPI Name",
              "isRequired": false,
              "pattern": "",
              "type": "text",
              //"url":"/perfmanagement/v1/kpivalue/_search?tenantId=default|$.kpiValues.KPI.code|$.kpiValues.KPI.name",
              "isDisabled": true,
              "requiredErrMsg": ""
            },
        {
              "name": "updateFinYear",
              "jsonPath": "kpiValues[0].kpi.financialYear",
              "label": "Financial Year",
              "isRequired": true,
              "pattern": "",
              "type": "text",
              "isDisabled": true,
              "requiredErrMsg": ""
            },
            {
              "name": "updateActualKpiName",
              "jsonPath": "kpiValues[0].kpi.name",
              "label": "KPI Name",
              "isRequired": true,
              "pattern": "",
              "type": "text",
              "isDisabled": true,
              "requiredErrMsg": ""
            },
            {
              "name": "updateActualKpiTarget",
              "jsonPath": "kpiValues[0].kpi.targetDescription",
              "label": "Target Value",
              "pattern": "",
              "type": "text",
              "isDisabled": true,
              "requiredErrMsg": ""
            },



            {
              "name": "updateActualKpiInstruction",
              "jsonPath": "kpiValues[0].kpi.instructions",
              "label": "Instruction to Achieve Target",
              "pattern": "",
              "type": "textarea",
              "fullWidth":true,
              "isDisabled": true,
              "requiredErrMsg": ""
            },
            /*{
              "name": "updateActualKpiActual",
              "jsonPath": "kpiValues[0].resultDescription",
              "label": "Actual Value",
              "pattern": "",
              "type": "number",
              "isDisabled": true,
              "requiredErrMsg": ""
            },*/
            {
          		"name": "kpitype",
          		"jsonPath": "kpiValues[0].kpi.targetType",
          		"label": "perfManagement.create.KPIs.groups.kpitype",
          		"pattern": "",
          		"type": "radio",
          		"isRequired": false,
          		"isDisabled": true,
          		"requiredErrMsg": "",
          		"patternErrMsg": "",
          		"values": [{
          			"label": "perfManagement.create.KPIs.groups.kpitype.value",
          			"value": true
          		}, {
          			"label": "perfManagement.create.KPIs.groups.kpitype.objective",
          			"value": false
          		}],
          		"defaultValue": true,
          		"showHideFields": [{
          			"ifValue": false,
          			"hide": [{
          				"name": "kpiTargetBlock",
          				"isGroup": true,
          				"isField": false
          			}],
          			"show": [{
          				"name": "kpiTargetRadioBlock",
          				"isGroup": true,
          				"isField": false
          			}]
          		}]
          	}

            ]
        },

        {
        "label": "perfManagement.create.KPIs.groups.kpiTargetBlock",
        "name": "kpiTargetBlock",
        "hide": false,
        "multiple": false,
        "fields": [{
          "name": "kpiTarget",
          //"hide":false,
          "jsonPath": "kpiValues[0].resultValue",
          "label": "",
          "pattern": "[0-9]",
          "type": "text",
          "isDisabled": false,
          "patternErrMsg": "Please enter a valid number",
          "requiredErrMsg": ""
        }]
        },
        {
        "label": "perfManagement.create.KPIs.groups.kpiTargetRadioBlock",
        "name": "kpiTargetRadioBlock",
        "hide": true,
        "multiple": false,
        "fields": [{
          "name": "kpiTargetRadio",
          //"hide":true,
          "jsonPath": "kpiValues[0].resultValue",
          "label": "",
          "pattern": "",
          "type": "radio",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": "",
          "values": [{
            "label": "perfManagement.create.KPIs.groups.kpiTargetRadioBlock.yes",
            "value": 1
          }, {
            "label": "perfManagement.create.KPIs.groups.kpiTargetRadioBlock.no",
            "value": 2
          }, {
            "label": "perfManagement.create.KPIs.groups.kpiTargetRadioBlock.inprogress",
            "value": 3
          }]
        }]
        },
        {
	        "name": "UploadDocument",
	        "label": "legal.create.group.title.UploadDocument",
	        fields:[{
              "name": "File",
              "jsonPath": "kpiValues[0].documents",
              "type": "documentList",
              "pathToArray": "documentTypes",
              "displayNameJsonPath": "name",
              //  "jsonPath": "kpiValues[0].documents",
              // "type": "documentList",
              // "pathToArray": "kpiValues[0].documents",
              // "displayNameJsonPath": "documents",
              //"url": "/tl-masters/documenttype/v2/_search",
              "url": "/perfmanagement/v1/kpivalue/_search?kpiCodes={code}&finYear={kpi.financialYear}",
              "autoFillFields": [
                {
                  "name": "documentTypeId",
                  "jsonPath": "id"
                }
              ]
            }]
		}
        ]
  },

  "perfManagement.view": {
        "numCols": 12 / 2,
        "url": "/perfmanagement/v1/kpivalue/_search?kpiCodes={code}&finYear={kpi.financialYear}",
        "useTimestamp": true,
        "objectName": "KPIs",
        "groups": [{
                "label": "Actual Key Performance Indicator",
                "name": "viewKPI",
                "fields": [{
                        "name": "viewkpiDepartment",
                        "jsonPath": "kpiValues[0].kpi.code",
                        // "url": "egov-mdms-service/v1/_get?tenantId=default&tenantIdCustom={KPIs[0].tenantId}&moduleName=common-masters&masterName=Department|$..id|$..name",
                        "label": "KPI Code",
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    },
                    {
                        "name": "viewkpiDate",
                        "jsonPath": "kpiValues[0].kpi.financialYear",
                        "label": "Financial Year",
                        "isRequired": true,
                        "pattern": "",
                        "type": "singleValueList",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    },

                    {
              "name": "updateActualKpiName",
              "jsonPath": "kpiValues[0].kpi.name",
              "label": "KPI Name",
              "isRequired": true,
              "pattern": "",
              "type": "text",
              "isDisabled": true,
              "requiredErrMsg": ""
            },
            {
              "name": "updateActualKpiTarget",
              "jsonPath": "kpiValues[0].kpi.targetDescription",
              "label": "Target Value",
              "pattern": "",
              "type": "text",
              "isDisabled": true,
              "requiredErrMsg": ""
            },
            {
                  "name": "updateActualKpiInstruction",
                  "jsonPath": "kpiValues[0].kpi.instructions",
                  "label": "Instruction to Achieve Target",
                  "pattern": "",
                  "type": "textarea",
                  "fullWidth":true,
                  "isDisabled": true,
                  "requiredErrMsg": ""
                },
                {
                  "name": "updateActualKpiActual",
                  "jsonPath": "kpiValues[0].resultDescription",
                  "label": "Actual Value",
                  "pattern": "",
                  "type": "number",
                  "isDisabled": false,
                  "requiredErrMsg": ""
                }
                ]
            }
        ]
    },
}
export default dat;
