var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/pipesize/_create",
		"tenantIdRequired": true,
		"objectName": "pipeSize",
		"groups": [
			{
				"label": "wc.create.pipesize.title",
				"name": "createpipeSize",
				"fields": [
						{
							"name": "name",
							"jsonPath": "pipeSize.sizeInMilimeter",
							"label": "  H.S.C Pipe Size (mm):",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"dependency": {
								"pipeSize.sizeInInch" :  "{pipeSize.sizeInMilimeter} * 0.039370"
							}
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
	}
}

export default dat;
