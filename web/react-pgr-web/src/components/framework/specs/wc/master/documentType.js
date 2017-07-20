var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/documenttype/_create",
		"groups": [
			{
				"label": "wc.create.documenttype.title",
				"name": "createCategoryType",
				"fields": [
						{
							"name": "name",
							"jsonPath": "documentTypes.name",
							"label": "wc.create.documenttype*",
							"pattern": "",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "",
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
							"jsonPath": "",
							"label": "Active",
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
