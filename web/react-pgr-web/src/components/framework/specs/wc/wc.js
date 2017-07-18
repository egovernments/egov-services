var dat = {
	"wc.create": {
		"numCols": 3,
		"url": "/connections/v1/_create",
		"groups": [
			{
				"label": "",
				"name": "applicantDetails",
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
							"jsonPath": "connection.asset.nameOfApplicant",
							"label": "Name Of Applicant",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "mobileNumber",
							"jsonPath": "connection.asset.mobileNumber",
							"label": "Mobile Number",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "email",
							"jsonPath": "connection.asset.email",
							"label": "Email",
							"pattern": "",
							"type": "email",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "adharNumber",
							"jsonPath": "connection.asset.adharNumber",
							"label": "Aadhaar Number",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "locality",
							"jsonPath": "connection.asset.locality",
							"label": "Locality",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "address",
							"jsonPath": "connection.asset.address",
							"label": "Address",
							"pattern": "",
							"type": "textarea",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "zone",
							"jsonPath": "connection.asset.zone",
							"label": "Zone / Ward / Block",
							"pattern": "",
							"type": "textarea",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "adharNumber",
							"jsonPath": "connection.asset.adharNumber",
							"label": "No of floors",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "propertyTaxDue",
							"jsonPath": "connection.property.propertyTaxDue",
							"label": "Property Tax",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false
						}
				]
			},
			{
				"label": "",
				"name": "connectionDetails",
				"fields": [
						{
							"name": "ConnectionType",
							"jsonPath": "connection.connectionType",
							"label": "Connection Type",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": true,
							"isDisabled": false,
							"url": ""
						},
						{
							"name": "sourceType",
							"jsonPath": "connection.sourceType",
							"label": "Water Source Type",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "sourceType",
							"jsonPath": "connection.sourceType",
							"label": "Water Source Type",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "propertyType",
							"jsonPath": "connection.property.propertyType",
							"label": "Property Type",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "categoryType",
							"jsonPath": "connection.categoryType",
							"label": "Category",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "usageType",
							"jsonPath": "connection.property.usageType",
							"label": "Usage Type",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "hscPipeSizeType",
							"jsonPath": "connection.hscPipeSizeType",
							"label": "H.S.C Pipe Size (Inches)",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "sumpCapacity",
							"jsonPath": "connection.sumpCapacity",
							"label": "Sump Capacity (Litres)",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "numberOfPersons",
							"jsonPath": "connection.numberOfPersons",
							"label": "No.of persons",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": false
						}
				]
			},
			{
				"label": "",
				"name": "approvalDetails",
				"fields": [
						{
							"name": "department",
							"jsonPath": "connection.workflowDetails.department",
							"label": "Approver Department",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": ""
						},
						{
							"name": "designation",
							"jsonPath": "connection.workflowDetails.designation",
							"label": "Approver Designation",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "approver",
							"jsonPath": "connection.workflowDetails.approver",
							"label": "Approver",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false
						},
						{
							"name": "comments",
							"jsonPath": "connection.workflowDetails.comments",
							"label": "Comments",
							"pattern": "",
							"type": "textarea",
							"isRequired": false,
							"isDisabled": false
						}
				]
			}
		]
	}
}

export default dat;
