var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/pipesize/_create",
		"tenantIdRequired": true,
		"objectName": "pipeSize",
		"groups": [
			{
				"label": "wc.create.pipeSize.title",
				"name": "createpipeSize",
				"fields": [
						{
							"name": "name",
							"jsonPath": "pipeSize.sizeInMilimeter",
							"label": "  H.S.C Pipe Size (mm):",
							"pattern": "/^\d+(\.\d+)?$/",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"depedants":[{
									"jsonPath":"pipeSize.sizeInInch",
									"type":"textField",
									"pattern":"`${getVal('pipeSize.sizeInMilimeter')!=''?getVal('pipeSize.sizeInMilimeter'):0} * 0.039370`",
									"rg":"",
									"isRequired": false,
									"requiredErrMsg": "",
									"patternErrMsg": ""
								}]
						},
						{
							"name": "description",
							"jsonPath": "pipeSize.sizeInInch",
							"label": "H.S.C Pipe Size (Inches):",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "description",
							"jsonPath": "pipeSize.description",
							"label": "Description",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Active",
							"jsonPath": "pipeSize.active",
							"label": "Active",
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
	"wc.search": {
		"numCols": 12/3,
		"url": "/wcms/masters/pipesize/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "PipeSize",
		"groups": [
			{
				"label": "wc.search.PipeSize.title",
				"name": "searchPipeSize",
				"fields": [
						{
							"name": "name",
							"jsonPath": "sizeInMilimeter",
							"label": "H.S.C Pipe Size (mm):",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Active",
							"jsonPath": "active",
							"label": "Active",
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
			"header": [{label: "wc.create.code"},{label: "wc.create.sizeInInch"},{label: "wc.create.sizeInMilimeter"}, {label: "wc.create.description"}, {label: "wc.create.active"}],
			"values": ["code", "sizeInInch", "sizeInMilimeter","description","active"],
			"resultPath": "PipeSizes",
			"rowClickUrlUpdate": "/update/wc/pipesize/{id}",
			"rowClickUrlView": "/view/wc/pipesize/{id}"
			}
	},
	"wc.view": {
		"numCols": 12/3,
		"url": "/wcms/masters/pipesize/_search?id={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "PipeSizes",
		"groups": [
			{
				"label": "wc.view.PipeSize.title",
				"name": "createCategoryType",
				"fields": [
						{
							"name": "code",
							"jsonPath": "PipeSizes[0].code",
							"label": "Code",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "sizeInInch",
							"jsonPath": "PipeSizes[0].sizeInInch",
							"label": "H.S.C Pipe Size (Inches):",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "sizeInMilimeter",
							"jsonPath": "PipeSizes[0].sizeInMilimeter",
							"label": "H.S.C Pipe Size (mm):",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "PipeSizes[0].description",
							"label": "Description",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "active",
							"jsonPath": "PipeSizes[0].active",
							"label": "Active",
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
		"searchUrl": "/wcms/masters/pipesize/_search?id={id}",
		"url":"/wcms/masters/pipesize/{PipeSizes[0].code}/_update",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "PipeSizes",
		"groups": [
			{
				"label": "wc.create.categorytype.title",
				"name": "createCategoryType",
				"fields": [
					{
						"name": "name",
						"jsonPath": "PipeSizes[0].sizeInMilimeter",
						"label": "  H.S.C Pipe Size (mm):",
						"pattern": "",
						"type": "number",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"depedants":[{
								"jsonPath":"PipeSizes[0].sizeInInch",
								"type":"textField",
								"pattern":"`${getVal('PipeSizes[0].sizeInMilimeter')!=''?getVal('PipeSizes[0].sizeInMilimeter'):0} * 0.039370`",
								"rg":"",
								"isRequired": false,
								"requiredErrMsg": "",
								"patternErrMsg": ""
							}]
					},
					{
						"name": "description",
						"jsonPath": "PipeSizes[0].sizeInInch",
						"label": "H.S.C Pipe Size (Inches):",
						"pattern": "",
						"type": "number",
						"isRequired": false,
						"isDisabled": true,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "description",
						"jsonPath": "PipeSizes[0].description",
						"label": "Description",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "Active",
						"jsonPath": "PipeSizes[0].active",
						"label": "Active",
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
	}
}

export default dat;
