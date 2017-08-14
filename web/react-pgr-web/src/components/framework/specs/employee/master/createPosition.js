var dat = {
	"employee.create": {
		"numCols": 12/3,
		"url": "/hr-masters/positions/_create",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "Position",
		"groups": [
			{
				"label": "employee.create.position.title",
				"name": "createPositionType",
				"fields": [
					{
						"name": "Department",
						"jsonPath": "Position[0].deptdesig.department",
						"label": "employee.create.groups.fields.department",
						"pattern": "",
						"type": "singleValueList",
						"url": "/egov-common-masters/departments/_search?|$..id|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "Designation",
						"jsonPath": "Position[0].deptdesig.designation.id",
						"label": "employee.create.groups.fields.designation",
						"pattern": "",
						"type": "singleValueList",
						"url": "/hr-masters/designations/_search?|$..id|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "Position",
						"jsonPath": "Position[0].noOfPositions",
						"label": "employee.create.group.fields.positionName",
						"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
						"type": "number",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "Outsourced Post",
						"jsonPath": "Position[0].isPostOutsourced",
						"label": "employee.create.group.fields.isPostOutsourced",
						"pattern": "",
						"type": "radio",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"values": [{"label":"Yes", "value":true},{"label":"No", "value":false}]

					}
				]
			}
		]
	},
	"employee.search": {
		"numCols": 12/3,
		"url": "/hr-masters/positions/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "BusinessDetails",
		"groups": [
			{
				"label": "employee.search.position.title",
				"name": "businessDetailsType",
				"fields": [
					{
						"name": "Department",
						"jsonPath": "departmentId",
						"label": "employee.create.groups.fields.department",
						"pattern": "",
						"type": "singleValueList",
						"url": "/egov-common-masters/departments/_search?|$..id|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "Designation",
						"jsonPath": "designationId",
						"label": "employee.create.groups.fields.designation",
						"pattern": "",
						"type": "singleValueList",
						"url": "/hr-masters/designations/_search?|$..id|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					}
				]
			}
		],
		"result": {
			"header": [{label: "employee.create.group.fields.positionName"}, {label: "employee.create.group.fields.department"}, {label: "employee.create.group.fields.designation"}, {label: "employee.create.group.fields.isPostOutsourced"}],
			"values": ["name", "deptdesig.department", "deptdesig.designation.id","isPostOutsourced"],
			"resultPath": "Position",
			"rowlickUrlUpdate": "/update/employee/createPosition/{id}",
			"rowClickUrlView": "/view/employee/createPosition/{id}"
			}
	},
	"employee.view": {
		"numCols": 12/3,
		"url": "/hr-masters/positions/_search?id={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "Position",
		"groups": [
			{
				"label": "employee.view.position.title",
				"name": "createPositionType",
				"fields": [
					{
						"name": "Department",
						"jsonPath": "Position[0].deptdesig.department",
						"label": "employee.create.groups.fields.department",
						"pattern": "",
						"type": "singleValueList",
						"url": "/egov-common-masters/departments/_search?|$..id|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "Designation",
						"jsonPath": "Position[0].deptdesig.designation.id",
						"label": "employee.create.groups.fields.designation",
						"pattern": "",
						"type": "singleValueList",
						"url": "/hr-masters/designations/_search?|$..id|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "Position",
						"jsonPath": "Position[0].noOfPositions",
						"label": "employee.create.group.fields.positionName",
						"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
						"type": "text",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "Length minimum is 3 and maximum is 100"
					},
					{
						"name": "Outsourced Post",
						"jsonPath": "Position[0].isPostOutsourced",
						"label": "employee.create.group.fields.isPostOutsourced",
						"pattern": "",
						"type": "radio",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"values": [{"label":"Yes", "value":true},{"label":"No", "value":false}],
						"defaultValue":true
					}
				]
			}
		]
	},
	"employee.update": {
		"numCols": 12/3,
		"searchUrl": "/hr-masters/positions/_search?id={id}",
		"url":"/hr-masters/positions/{position.id}/_update",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "Position",
		"groups": [
			{
				"label": "employee.update.position.title",
				"name": "createPositionType",
				"fields": [
					{
						"name": "Department",
						"jsonPath": "Position[0].deptdesig.department",
						"label": "employee.create.groups.fields.department",
						"pattern": "",
						"type": "singleValueList",
						"url": "/egov-common-masters/departments/_search?|$..id|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "Designation",
						"jsonPath": "Position[0].deptdesig.designation.id",
						"label": "employee.create.groups.fields.designation",
						"pattern": "",
						"type": "singleValueList",
						"url": "/hr-masters/designations/_search?|$..id|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "Position",
						"jsonPath": "Position[0].noOfPositions",
						"label": "employee.create.group.fields.positionName",
						"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
						"type": "text",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "Length minimum is 3 and maximum is 100"
					},
					{
						"name": "Outsourced Post",
						"jsonPath": "Position[0].isPostOutsourced",
						"label": "employee.create.group.fields.isPostOutsourced",
						"pattern": "",
						"type": "radio",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"values": [{"label":"Yes", "value":true},{"label":"No", "value":"false"}],
						"defaultValue":true
					},
					{
						"name": "Active",
						"jsonPath": "Position[0].active",
						"label": "wc.create.active",
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
