var room = {
	"name": "RoomDetailsComponent",
	"version": "v1",
	"level": 2,
	"jsonPath": "connection.floors[0].rooms",
	"groups": [
		{
			"label": "wc.create.groups.roomDetails.title",
			"name": "RoomDetails",
			"multiple": true,
			"children": [],
			"fields": [
				{
					"name": "RoomNo",
					"jsonPath": "connection.floors[0].rooms[0].roomNo",
					"label": "wc.create.groups.roomDetails.roomNo",
					"pattern": "",
					"type": "number",
					"isRequired": false,
					"isDisabled": false,
					"requiredErrMsg": "",//Remove required messages
					"patternErrMsg": ""
				},
				{
					"name": "RoomName",
					"jsonPath": "connection.floors[0].rooms[0].roomName",
					"label": "wc.create.groups.roomDetails.roomName",
					"pattern": "",
					"type": "text",
					"isRequired": false,
					"isDisabled": false,
					"requiredErrMsg": "",//Remove required messages
					"patternErrMsg": ""
				}
			]
		}
	]
};

var floor = {
	"name": "FloorDetailsComponent",
	"version": "v1", //Maps to parent version
	"level": 1,
	"jsonPath": "connection.floors",
	"groups": [
		{
			"label": "wc.create.groups.floorDetails.title",
			"name": "FloorDetails",
			"multiple": true, //If true, its an array
			"children": [room],
			"fields": [
				{
					"name": "FloorNo",
					"jsonPath": "connection.floors[0].floorNo",
					"label": "wc.create.groups.floorDetails.floorNo",
					"pattern": "",
					"type": "number",
					"isRequired": true,
					"isDisabled": false,
					"requiredErrMsg": "",//Remove required messages
					"patternErrMsg": ""
				},
				{
					"name": "FloorName",
					"jsonPath": "connection.floors[0].floorName",
					"label": "wc.create.groups.floorDetails.floorName",
					"pattern": "",
					"type": "text",
					"isRequired": false,
					"isDisabled": false,
					"requiredErrMsg": "",//Remove required messages
					"patternErrMsg": ""
				}
			]
		}
	]
};

var dat = {
	"wc.create": {
		"numCols": 12/3,
		"version": "v1",
		"url": "/connections/v1/_create",
		"useTimestamp": true,
		"tenantIdRequired": false, //Instead of boolean value give json path
		"objectName": "connection",
		"level": 0,
		"groups": [
			{
				"label": "wc.create.groups.applicantDetails.title", //Cut short labels by taking initial path from parent
				"name": "applicantDetails",//Follow Title case pattern
				"children": [floor],
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
							"name": "ConnectionType",
							"jsonPath": "connection.connectionType",
							"label": "wc.create.groups.connectionDetails.connectionType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": true,
							"isDisabled": false,
							"url": "/wcms-connection/connection/_getconnectiontypes",
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
							"url": "/wcms/masters/sourcetype/_search",
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
							"url": "/pt-property/property/propertytypes/_search?|$..id|$..propertyType",
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
							"url": "/wcms/masters/propertytype-categorytype/_search",
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
							"url": "/wcms/masters/propertytype-usagetype/_search?|$..id|$..name",
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
							"url": "/wcms/masters/propertytype-pipesize/_search",
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
				"label": "wc.create.groups.approvalDetails.title",
				"name": "approvalDetails",
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
							"patternErrMsg": ""
						},
						{
							"name": "designation",
							"jsonPath": "connection.workflowDetails.designation",
							"label": "wc.create.groups.approvalDetails.fields.designation",
							"pattern": "",
							"type": "singleValueList",
							"url": "/egov-common-masters/departments/_search?tenantId={tenantId}&dept={department}|$..id|$..name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "approver",
							"jsonPath": "connection.workflowDetails.approver",
							"label": "wc.create.groups.approvalDetails.fields.approver",
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
