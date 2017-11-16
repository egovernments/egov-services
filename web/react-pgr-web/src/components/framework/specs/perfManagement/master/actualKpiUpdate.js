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
      "rowClickUrlView": "/update/perfManagement/actualKpiUpdate/{kpi.code}?finYear={kpi.financialYear}"
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
            {
              "name": "updateActualKpiActual",
              "jsonPath": "kpiValues[0].resultValue",
              "label": "Actual Value",
              "pattern": "",
              "type": "number",
              "isDisabled": false,
              "requiredErrMsg": ""
            }
            ]
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
  }
}
export default dat;
