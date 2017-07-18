var dat = {
	"wc.create": {
		"numCols": 3,
		"url": "/connections/v1/_create"
		"groups": [
			{	
				"label": "",
				"name": "applicantDetails"
				"fields": [
						{
							"name": "AssessmentNumber",
							"jsonPath": "connection.property.propertyIdentifier",
							"label": "Assessment Number",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"autoCompleteUrl": "",
							"autoFillFields": ["NameOfApplicant", "mobileNumber"]
						},
						{
							"name": "NameOfApplicant",
							"jsonPath": "connection.property.nameOfApplicant",
							"label": "Name Of Applicant",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false
						}
				]
			},
			{
				"label": "",
				"name": "connectionDetails"
				"fields": [
						{
							"name": "ConnectionType",
							"jsonPath": "connection.connectionType",
							"label": "Connection Type",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"url": ""
						}
				]
			}
		]
	}
}

export default dat;