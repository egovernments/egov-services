var dat = {
	"wc.create": {
		"numCols": 12/3,
		"version": "v1",
		"url": "/wcms-connection/connection/_create",
		"useTimestamp": true,
		"tenantIdRequired": false, //Instead of boolean value give json path
		"objectName": "connection",
		"level": 0,
		"groups": [
			{
				"label": "wc.create.groups.applicantDetails.title", //Cut short labels by taking initial path from parent
				"name": "applicantDetails",//Follow Title case pattern
				"children": [],
				"multiple": false,
				"fields": [
						{
							"name": "AssessmentNumber",
							"jsonPath": "connection.property.propertyIdentifier",
							"label": "wc.create.groups.applicantDetails.propertyIdentifier",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"autoCompleteUrl": "",
							"autoFillFields": {
								"NameOfApplicant": "",
								"MobileNumber": "",
								"Email": "",
								"AadharNumber": ""
							},
							"requiredErrMsg": "",//Remove required messages
							"patternErrMsg": ""
						},
						{
							"name": "NameOfApplicant",
							"jsonPath": "connection.asset.nameOfApplicant",
							"label": "wc.create.groups.applicantDetails.nameOfApplicant",
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
							"label": "wc.create.groups.applicantDetails.mobileNumber",
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
							"label": "wc.create.groups.applicantDetails.email",
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
							"label": "wc.create.groups.applicantDetails.adharNumber",
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
							"label": "wc.create.groups.applicantDetails.locality",
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
							"label": "wc.create.groups.applicantDetails.address",
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
							"label": "wc.create.groups.applicantDetails.zone",
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
							"label": "wc.create.groups.applicantDetails.propertyTaxDue",
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
				"label": "wc.create.groups.connectionDetails.title",
				"name": "connectionDetails",
				"multiple": false,
				"fields": [
					{
						"name": "billingType",
						"jsonPath": "connection.billingType",
						"label": "wc.create.groups.connectionDetails.billingType",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": true,
						"isDisabled": false,
						"url": "/wcms-connection/connection/_getbillingtypes?|$..key|$..object",
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
						{
							"name": "ConnectionType",
							"jsonPath": "connection.connectionType",
							"label": "wc.create.groups.connectionDetails.connectionType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": true,
							"isDisabled": false,
							"url": "/wcms-connection/connection/_getconnectiontypes?|$..key|$..object",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "SourceType",
							"jsonPath": "connection.sourceType",
							"label": "wc.create.groups.connectionDetails.sourceType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "/wcms/masters/sourcetype/_search?|$..name|$..name",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "supplyType",
							"jsonPath": "connection.supplyType",
							"label": "wc.create.groups.connectionDetails.supplyType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "/wcms/masters/supplytype/_search?|$..name|$..name",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "PropertyType",
							"jsonPath": "connection.property.propertyType",
							"label": "wc.create.groups.connectionDetails.propertyType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "/pt-property/property/propertytypes/name?|$..id|$..name",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "CategoryType",
							"jsonPath": "connection.categoryType",
							"label": "wc.create.groups.connectionDetails.categoryType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "/wcms/masters/propertytype-categorytype/_search?|$..categoryTypeName|$..categoryTypeName",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "waterTreatment",
							"jsonPath": "connection.waterTreatment",
							"label": "wc.create.groups.connectionDetails.waterTreatment",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "/wcms/masters/treatmentplant/_search?|$..name|$..name",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "UsageType",
							"jsonPath": "connection.property.usageType",
							"label": "wc.create.groups.connectionDetails.usageType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "/wcms/masters/propertytype-usagetype/_search?|$..propertyType|$..propertyType",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "hscPipeSizeType",
							"jsonPath": "connection.hscPipeSizeType",
							"label": "wc.create.groups.connectionDetails.hscPipeSizeType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "/wcms/masters/propertytype-pipesize/_search?|$..id|$..pipeSize",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "sumpCapacity",
							"jsonPath": "connection.sumpCapacity",
							"label": "wc.create.groups.connectionDetails.fields.sumpCapacity",
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
							"label": "wc.create.groups.connectionDetails.fields.numberOfPersons",
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
				"label": "wc.create.groups.fileDetails.title",
				"name": "Documents",
				"multiple": false,
				"fields": [
					{
						"name": " ",
						"jsonPath": "connection.documents[0]",
						"label": "wc.create.groups.fileDetails.fields.PTaxReciept",
						"pattern": "",
						"type": "singleFileUpload",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": " ",
						"jsonPath": "connection.documents[1]",
						"label": "wc.create.groups.fileDetails.fields.DistributionLineLocationMap",
						"pattern": "",
						"type": "singleFileUpload",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": " ",
						"jsonPath": "connection.documents[2]",
						"label": "wc.create.groups.fileDetails.fields.WhiteRationCard",
						"pattern": "",
						"type": "singleFileUpload",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": " ",
						"jsonPath": "connection.documents[3]",
						"label": "wc.create.groups.fileDetails.fields.CourtFeeStamp",
						"pattern": "",
						"type": "singleFileUpload",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					}
				]
			},
			{
				"label": "wc.create.groups.approvalDetails.title",
				"name": "ApprovalDetails",
				"multiple": false,
				"fields": [
					{
						"name": "department",
						"jsonPath": "connection.workflowDetails.department",
						"label": "wc.create.groups.approvalDetails.fields.department",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": false,
						"isDisabled": false,
						"url": "/egov-common-masters/departments/_search?tenantId=default|$..id|$..name",
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"depedants":[{
								"name":"connection.workflowDetails.approver",
								"type":"dropDown",
								"pattern":"/hr-employee/employees/_search?tenantId=default&departmentId={connection.workflowDetails.department}&designationId={connection.workflowDetails.designation}|$..id|$..name"
							}]
						},
						{
						"name": "designation",
						"jsonPath": "connection.workflowDetails.designation",
						"label": "wc.create.groups.approvalDetails.fields.designation",
						"pattern": "",
						"type": "singleValueList",
						"url": "/egov-common-workflows/designations/_search?businessKey=WaterConnection&approvalDepartmentName&departmentRule&currentStatus&additionalRule&pendingAction&designation&amountRule|$..id|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"depedants":[{
								"name":"connection.workflowDetails.approver",
								"type":"dropDown",
								"pattern":"/hr-employee/employees/_search?tenantId=default&departmentId={connection.workflowDetails.department}&designationId={connection.workflowDetails.designation}|$..id|$..name"
							}]
						},
						{
						"name": "approver",
						"jsonPath": "connection.workflowDetails.approver",
						"label": "wc.create.groups.approvalDetails.fields.approver",
						"pattern": "",
						"type": "singleValueList",
						"url":"/hr-employee/employees/_search?tenantId=default&departmentId={connection.workflowDetails.department}&designationId={connection.workflowDetails.designation}|$..id|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
						},
						{
						"name": "comments",
						"jsonPath": "connection.workflowDetails.comments",
						"label": "wc.create.groups.approvalDetails.fields.comments",
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
