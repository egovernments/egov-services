var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/propertytype-categorytype/_create",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "propertyPipeSize",
		"groups": [
			{
				"label": "wc.create.propertyPipeSize.title",
				"name": "propertyPipeSize",
				"fields": [
					{
						"name": "propertyType",
						"jsonPath": "propertyPipeSize.propertyType",
						"label": "wc.create.propertytype",
						"pattern": "",
						"type": "singleValueList",
						"url": "/pt-property/property/propertytypes/_search?tenantId={tenantId}|$..id|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "pipeSize",
						"jsonPath": "propertyPipeSize.pipeSize",
						"label": "wc.create.pipeSize",
						"pattern": "",
						"type": "singleValueList",
						"url": "/wcms/masters/pipesize/_search?tenantId={tenantId}|$..id|$..sizeInMilimeter",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
						{
							"name": "Active",
							"jsonPath": "propertyPipeSize.active",
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
