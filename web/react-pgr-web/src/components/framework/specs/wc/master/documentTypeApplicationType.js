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
						"url": "/wcms/masters/master/_getapplicationtypes?|$..key|$..object",
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
						"url": "/wcms/masters/documenttype/_search?|$..code|$..name",
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
	},
	"wc.search": {
		"numCols": 12/3,
		"url": "/wcms/masters/documenttype-applicationtype/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "DocumentTypeApplicationType",
		"groups": [
			{
				"label": "wc.search.categorytype.title",
				"name": "createCategoryType",
				"fields": [
					{
						"name": "applicationType",
						"jsonPath": "DocumentTypeApplicationType.applicationType",
						"label": "wc.create.applicationtype",
						"pattern": "",
						"type": "singleValueList",
						"url": "/wcms/masters/master/_getapplicationtypes?|$..key|$..object",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "documenttype",
						"jsonPath": "DocumentTypeApplicationType.documentTypeId",
						"label": "wc.create.documenttype",
						"pattern": "",
						"type": "singleValueList",
						"url": "/wcms/masters/documenttype/_search?|$..id|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
						{
							"name": "Active",
							"jsonPath": "DocumentTypeApplicationType.active",
							"label": "Active",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Mandatory",
							"jsonPath": "DocumentTypeApplicationType.mandatory",
							"label": "Mandatory",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}
		],
		"result": {
			"header": [{label: "documentType"}, {label: "applicationType"}, {label: "wc.search.result.active"},{label: "wc.search.result.mandatory"}],
			"values": ["documentType", "applicationType", "active","mandatory"],
			"resultPath": "DocumentTypeApplicationTypes",
			"rowClickUrlUpdate": "/update/wc/documentTypeApplicationType/{id}",
			"rowClickUrlView": "/view/wc/documentTypeApplicationType/{id}"
			}
	},
	"wc.view": {
		"numCols": 12/3,
		"url": "/wcms/masters/documenttype-applicationtype/_search?id={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "DocumentTypeApplicationTypes",
		"groups": [
			{
				"label": "wc.create.categorytype.title",
				"name": "createCategoryType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "DocumentTypeApplicationTypes[0].applicationType",
							"label": "Application Type",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "documentType",
							"jsonPath": "DocumentTypeApplicationTypes[0].documentType",
							"label": "Document Type",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Active",
							"jsonPath": "DocumentTypeApplicationTypes[0].active",
							"label": "Active",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Mandatory",
							"jsonPath": "DocumentTypeApplicationTypes[0].mandatory",
							"label": "mandatory",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}
		]
	},
	"wc.update": {
		"numCols": 12/3,
		"searchUrl": "/wcms/masters/documenttype-applicationtype/_search?id={id}",
		"url":"/wcms/masters/documenttype-applicationtype/{DocumentTypeApplicationType.code}/_update",
		"tenantIdRequired": true,
		"isResponseArray":true,
		"useTimestamp": true,
		"objectName": "DocumentTypeApplicationType",
		"groups": [
			{
				"label": "wc.update.DocumentTypeApplicationTypes.title",
				"name": "createCategoryType",
				"fields": [
					{
						"name": "applicationType",
						"jsonPath": "DocumentTypeApplicationType.applicationType",
						"label": "wc.create.applicationtype",
						"pattern": "",
						"type": "singleValueList",
						"url": "/wcms/masters/master/_getapplicationtypes?|$..key|$..object",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "documenttype",
						"jsonPath": "DocumentTypeApplicationType.documentTypeId",
						"label": "wc.create.documenttype",
						"pattern": "",
						"type": "singleValueList",
						"url": "/wcms/masters/documenttype/_search?|$..id|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
						{
							"name": "Active",
							"jsonPath": "DocumentTypeApplicationType.active",
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
							"jsonPath": "DocumentTypeApplicationType.mandatory",
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
