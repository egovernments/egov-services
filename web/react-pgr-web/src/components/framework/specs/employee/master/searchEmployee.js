var dat = {
	"employee.search": {
		"numCols": 12/3,
		"url": "/hr-employee/employees/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "Employee",
		"groups": [
			{
				"label": "employee.search.emp.title",
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
					},
          {
						"name": "Code",
						"jsonPath": "code",
						"label": "employee.create.groups.fields.code",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
          {
						"name": "Name",
						"jsonPath": "name",
						"label": "employee.create.group.fields.empName",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					}
				]
			}
		],
		"result": {
			"header": [{label: "employee.create.groups.fields.code"},{label: "employee.create.group.fields.empName"},{label: "employee.create.groups.fields.department"}, {label: "employee.create.groups.fields.designation"}, {label: "employee.create.group.fields.position"}],
			"values": ["code","name", "assignments[0].department", "assignments[0].designation","assignments[0].position"],
			"resultPath": "Employee",
			"rowlickUrlUpdate": "/update/employee/employee/{id}",
			"rowClickUrlView": "/view/employee/employee/{id}"
			}
	}
}

export default dat;
