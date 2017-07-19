var dat = {
	"pt.create": {
		"numCols": 2,
		"url": "/connections/v1/_create",
		"groups": [
			{
				"label": "pt.create.groups.createNewProperty.title",
				"name": "createNewProperty",
				"fields": [
					{
						"name": "SourceType",
						"jsonPath": "connection.sourceType",
						"label": "pt.create.groups.connectionDetails.sourceType",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": false,
						"isDisabled": false,
						"url": "",
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
						{
							"name": "NameOfApplicant",
							"jsonPath": "connection.asset.nameOfApplicant",
							"label": "pt.create.groups.applicantDetails.nameOfApplicant",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "MobileNumber",
							"jsonPath": "connection.asset.mobileNumber",
							"label": "pt.create.groups.applicantDetails.mobileNumber",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Email",
							"jsonPath": "connection.asset.email",
							"label": "pt.create.groups.applicantDetails.email",
							"pattern": "",
							"type": "email",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "AadharNumber",
							"jsonPath": "connection.asset.adharNumber",
							"label": "pt.create.groups.applicantDetails.adharNumber",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Locality",
							"jsonPath": "connection.asset.locality",
							"label": "pt.create.groups.applicantDetails.locality",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Address",
							"jsonPath": "connection.asset.address",
							"label": "pt.create.groups.applicantDetails.address",
							"pattern": "",
							"type": "textarea",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Zone",
							"jsonPath": "connection.asset.zone",
							"label": "pt.create.groups.applicantDetails.zone",
							"pattern": "",
							"type": "textarea",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "adharNumber",
							"jsonPath": "connection.asset.adharNumber",
							"label": "No of floors",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "propertyTaxDue",
							"jsonPath": "connection.property.propertyTaxDue",
							"label": "pt.create.groups.applicantDetails.propertyTaxDue",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			},
			{
				"label": "pt.create.groups.connectionDetails.title",
				"name": "connectionDetails",
				"fields": [
						{
							"name": "ConnectionType",
							"jsonPath": "connection.connectionType",
							"label": "pt.create.groups.connectionDetails.connectionType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": true,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "SourceType",
							"jsonPath": "connection.sourceType",
							"label": "pt.create.groups.connectionDetails.sourceType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "SourceType",
							"jsonPath": "connection.sourceType",
							"label": "pt.create.groups.connectionDetails.sourceType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "PropertyType",
							"jsonPath": "connection.property.propertyType",
							"label": "pt.create.groups.connectionDetails.propertyType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "CategoryType",
							"jsonPath": "connection.categoryType",
							"label": "pt.create.groups.connectionDetails.categoryType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "UsageType",
							"jsonPath": "connection.property.usageType",
							"label": "pt.create.groups.connectionDetails.usageType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "hscPipeSizeType",
							"jsonPath": "connection.hscPipeSizeType",
							"label": "pt.create.groups.connectionDetails.hscPipeSizeType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "sumpCapacity",
							"jsonPath": "connection.sumpCapacity",
							"label": "pt.create.groups.connectionDetails.fields.sumpCapacity",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "numberOfPersons",
							"jsonPath": "connection.numberOfPersons",
							"label": "pt.create.groups.connectionDetails.fields.numberOfPersons",
							"pattern": "",
							"type": "number",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			},
			{
				"label": "pt.create.groups.approvalDetails.title",
				"name": "approvalDetails",
				"fields": [
						{
							"name": "department",
							"jsonPath": "connection.workflowDetails.department",
							"label": "pt.create.groups.approvalDetails.fields.department",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "designation",
							"jsonPath": "connection.workflowDetails.designation",
							"label": "pt.create.groups.approvalDetails.fields.designation",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "approver",
							"jsonPath": "connection.workflowDetails.approver",
							"label": "pt.create.groups.approvalDetails.fields.approver",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "comments",
							"jsonPath": "connection.workflowDetails.comments",
							"label": "pt.create.groups.approvalDetails.fields.comments",
							"pattern": "",
							"type": "textarea",
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
