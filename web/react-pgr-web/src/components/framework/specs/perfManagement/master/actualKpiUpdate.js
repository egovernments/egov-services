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
              "url":"/perfmanagement/v1/kpimaster/_search?tenantId=|$.KPIs.*.code|$.KPIs.*.name",
              "isDisabled": false,
              "requiredErrMsg": ""
          },
          {
              "name": "searchActualKpiDate",
              "jsonPath": "finYear",
              "label": "Financial Year",
              "pattern": "",
              "type": "singleValueList",
              "url": "egf-master/financialyears/_search?tenantId=default|$.financialYears.*.finYearRange|$.financialYears.*.finYearRange",
              "isDisabled": false,
              "requiredErrMsg": ""
            }
            // {
            //   "name": "searchActualKpiName",
            //   "jsonPath": "active",
            //   "label": "KPI Name",
            //   "pattern": "",
            //   "type": "text",
            //   "isDisabled": false,
            //   "requiredErrMsg": ""
            // }
        ]
      }
       ],
       "result": {
      "header": [{label: "Document"},{label: "Financial Year"},{label: "KPI Name"}],
      "values": ["applicationType","documentType", "active"],
      "resultPath": "DocumentTypeApplicationTypes",
      "rowClickUrlUpdate": "/update/perfManagement/actualKpiUpdate/{id}"
      }
  },
  "perfManagement.update": {
    "numCols": 12/2,
    "searchUrl": "/wcms/masters/documenttypes-applicationtypes/_search?ids={id}",
    "url":"/wcms/masters/documenttypes-applicationtypes/_update",
    "tenantIdRequired": true,
    "useTimestamp": true,
    "objectName": "DocumentTypeApplicationTypes",
    "groups": [
      {
        "label": "Update Actual Key Performance Indicator",
        "name": "kpiDate",
        "fields": [
        {
              "name": "updateActualKpiDepartment",
              "jsonPath": "DocumentTypeApplicationTypes[0].applicationType",
              "label": "Tenant",
              "isRequired": true,
              "pattern": "",
              "type": "singleValueList",
              "isDisabled": true,
              "requiredErrMsg": ""
            },
        {
              "name": "updateActualKpiDate",
              "jsonPath": "DocumentTypeApplicationTypes[0].documentType",
              "label": "Financial Year",
              "isRequired": true,
              "pattern": "",
              "type": "singleValueList",
              "isDisabled": true,
              "requiredErrMsg": ""
            },
            {
              "name": "updateActualKpiName",
              "jsonPath": "DocumentTypeApplicationTypes[0].active",
              "label": "KPI Name",
              "isRequired": true,
              "pattern": "",
              "type": "text",
              "isDisabled": true,
              "requiredErrMsg": ""
            },
            {
              "name": "updateActualKpiTarget",
              "jsonPath": "DocumentTypeApplicationTypes[0].active",
              "label": "Target Value",
              "pattern": "",
              "type": "text",
              "isDisabled": true,
              "requiredErrMsg": ""
            },
            {
              "name": "updateActualKpiInstruction",
              "jsonPath": "DocumentTypeApplicationTypes[0].active",
              "label": "Instruction to Achieve Target",
              "pattern": "",
              "type": "textarea",
              "fullWidth":true,
              "isDisabled": true,
              "requiredErrMsg": ""
            },
            {
              "name": "updateActualKpiActual",
              "jsonPath": "DocumentTypeApplicationTypes[0].active",
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
              "jsonPath": "DocumentTypeApplicationTypes[0].supportDocuments",
              "type": "documentList",
              "pathToArray": "documentTypes",
              "displayNameJsonPath": "name",
              "url": "/tl-masters/documenttype/v2/_search",
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

