var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/documenttype-applicationtype/_create",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "documentTypeApplicationType",
		"groups": [
			{
				"label": "wc.create.documenttype.title",
				"name": "createdocumentTypeApplicationType",
				"fields": [
					{
						"name": "applicationType",
						"jsonPath": "createdocumentTypeApplicationType.applicationType",
						"label": "wc.create.applicationtype",
						"pattern": "",
						"type": "singleValueList",
						"url": "/wcms/masters/master/_getapplicationtypes?tenantId={tenantId}|$..key|$..object",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "documenttype",
						"jsonPath": "createdocumentTypeApplicationType.documentType",
						"label": "wc.create.documenttype",
						"pattern": "",
						"type": "singleValueList",
						"url": "/wcms/masters/documenttype/_search?tenantId={tenantId}|$..code|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
						{
							"name": "Active",
							"jsonPath": "createdocumentTypeApplicationType.active",
							"label": "Active",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"isDisabled": false,
							"default": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "mandatory",
							"jsonPath": "createdocumentTypeApplicationType.mandatory",
							"label": "Mandatory",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"isDisabled": false,
							"default": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}
		]
	}
}

export default dat;
