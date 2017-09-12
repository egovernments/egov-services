var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/donation/_create",
		"tenantIdRequired": true,
		"idJsonPath": "Donations[0].code",
		"useTimestamp": true,
		"objectName": "Donation",
		"groups": [
			{
				"label": "wc.create.donation.title",
				"name": "donation",
				"fields": [
					{
						"name": "propertyType",
						"jsonPath": "Donation[0].propertyType",
						"label": "wc.create.propertyType",
						"pattern": "",
						"type": "singleValueList",
						"url": "/pt-property/property/propertytypes/_search?&active=true|$..name|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
          {
								"name": "CategoryType",
								"jsonPath": "Donation[0].category",
								"label": "wc.create.groups.connectionDetails.categoryType",
								"pattern": "",
								"type": "singleValueList",
								"isRequired": true,
								"isDisabled": false,
								"url": "/wcms/masters/categorytype/_search?&active=true|$..name|$..name",
								"requiredErrMsg": "",
								"patternErrMsg": ""
					},
          {
						"name": "UsageType",
						"jsonPath": "Donation[0].usageType",
						"label": "wc.create.groups.connectionDetails.usageType",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": true,
						"isDisabled": false,
						"url": "/pt-property/property/usages/_search?&active=true|$..name|$..name",
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
          {
						"name": "hscPipeSizeType",
						"jsonPath": "Donation[0].minPipeSize",
						"label": "wc.create.minPipeSize",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": true,
						"isDisabled": false,
						"url": "/wcms/masters/pipesize/_search?&active=true|$..sizeInMilimeter|$..sizeInMilimeter",
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
          {
						"name": "hscPipeSizeType",
						"jsonPath": "Donation[0].maxPipeSize",
						"label": "wc.create.maxPipeSize",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": true,
						"isDisabled": false,
						"url": "/wcms/masters/pipesize/_search?&active=true|$..sizeInMilimeter|$..sizeInMilimeter",
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
          {
            "name": "donationAmount",
            "jsonPath": "Donation[0].donationAmount",
            "label": "wc.create.donationAmount",
            "pattern": "^\\d+(\\.\\d+)?$",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "fromDate",
            "jsonPath": "Donation[0].fromDate",
            "label": "wc.create.fromDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
            "name": "fromDate",
            "jsonPath": "Donation[0].toDate",
            "label": "wc.create.toDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
					{
						"name": "Active",
						"jsonPath": "Donation[0].active",
						"label": "wc.create.active",
						"pattern": "",
						"type": "checkbox",
						"isRequired": false,
						"isDisabled": false,
						"defaultValue":true,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					}
				]
			}
		]
	},
	"wc.search": {
		"numCols": 12/3,
		"url": "/wcms/masters/donation/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "Donation",
		"groups": [
			{
				"label": "wc.search.Donation.title",
				"name": "createCategoryType",
				"fields": [
						{
						"name": "propertyType",
						"jsonPath": "propertyType",
						"label": "wc.create.propertyType",
						"pattern": "",
						"type": "singleValueList",
						"url": "/pt-property/property/propertytypes/_search?&active=true|$..name|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
								"name": "CategoryType",
								"jsonPath": "category",
								"label": "wc.create.groups.connectionDetails.categoryType",
								"pattern": "",
								"type": "singleValueList",
								"isRequired": false,
								"isDisabled": false,
								"url": "/wcms/masters/categorytype/_search?&active=true|$..name|$..name",
								"requiredErrMsg": "",
								"patternErrMsg": ""
					},
					{
						"name": "UsageType",
						"jsonPath": "usageType",
						"label": "wc.create.groups.connectionDetails.usageType",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": false,
						"isDisabled": false,
						"url": "/pt-property/property/usages/_search?&active=true|$..name|$..name",
						"requiredErrMsg": "",
						"patternErrMsg": ""
					}
				]
			}
		],
		"result": {
			"header": [{label: "wc.create.propertyType"}, {label: "wc.create.groups.connectionDetails.categoryType"}, {label: "wc.create.groups.connectionDetails.usageType"}, {label: "wc.create.minPipeSize"},{label: "wc.create.maxPipeSize"},{label: "wc.create.donationAmount"},{label: "wc.create.fromDate"},{label: "wc.create.toDate"},{label: "wc.create.active"}],
			"values": ["propertyType", "category", "usageType","minPipeSize","maxPipeSize","donationAmount","fromDate","toDate","active"],
			"resultPath": "Donations",
			"rowClickUrlUpdate": "/update/wc/donation/{id}",
			"rowClickUrlView": "/view/wc/donation/{id}"
			}
	},
	"wc.view": {
		"numCols": 12/3,
		"url": "/wcms/masters/donation/_search?id={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "Donations",
		"groups": [
			{
				"label": "wc.view.Donations.title",
				"name": "viewDonations",
				"fields": [
						{
							"name": "propertyType",
							"jsonPath": "Donations[0].propertyType",
							"label": "wc.create.propertyType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
	          {
									"name": "CategoryType",
									"jsonPath": "Donations[0].category",
									"label": "wc.create.groups.connectionDetails.categoryType",
									"pattern": "",
									"type": "singleValueList",
									"isRequired": false,
									"isDisabled": false,
									"url": "/wcms/masters/categorytype/_search?|$..name|$..name",
									"requiredErrMsg": "",
									"patternErrMsg": ""
						},
	          {
							"name": "UsageType",
							"jsonPath": "Donations[0].usageType",
							"label": "wc.create.groups.connectionDetails.usageType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "/pt-property/property/usages/_search?|$..name|$..name",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
	          {
							"name": "hscPipeSizeType",
							"jsonPath": "Donations[0].minPipeSize",
							"label": "wc.create.minPipeSize",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "/wcms/masters/pipesize/_search?|$..sizeInMilimeter|$..sizeInMilimeter",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
	          {
							"name": "hscPipeSizeType",
							"jsonPath": "Donations[0].maxPipeSize",
							"label": "wc.create.maxPipeSize",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"url": "/wcms/masters/pipesize/_search?|$..sizeInMilimeter|$..sizeInMilimeter",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
	          {
	            "name": "donationAmount",
	            "jsonPath": "Donations[0].donationAmount",
	            "label": "wc.create.donationAmount",
	            "pattern": "",
	            "type": "number",
	            "isRequired": false,
	            "isDisabled": false,
	            "requiredErrMsg": "",
	            "patternErrMsg": ""
	          },
						{
	            "name": "fromDate",
	            "jsonPath": "Donations[0].fromDate",
	            "label": "wc.create.fromDate",
	            "pattern": "",
	            "type": "datePicker",
	            "isRequired": false,
	            "isDisabled": false,
	            "requiredErrMsg": "",
	            "patternErrMsg": ""
	          },
						{
	            "name": "fromDate",
	            "jsonPath": "Donations[0].toDate",
	            "label": "wc.create.toDate",
	            "pattern": "",
	            "type": "datePicker",
	            "isRequired": false,
	            "isDisabled": false,
	            "requiredErrMsg": "",
	            "patternErrMsg": ""
	          },
						{
							"name": "Active",
							"jsonPath": "Donations[0].active",
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
	},
	"wc.update": {
		"numCols": 12/3,
		"searchUrl": "/wcms/masters/donation/_search?id={id}",
		"url":"/wcms/masters/donation/_update",
		"isResponseArray":true,
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "Donations",
		"groups": [
			{
				"label": "wc.update.Donation.title",
				"name": "updateDonation",
				"fields": [
					{
						"name": "propertyType",
						"jsonPath": "Donations[0].propertyType",
						"label": "wc.create.propertyType",
						"pattern": "",
						"type": "singleValueList",
						"url": "/pt-property/property/propertytypes/_search?&active=true|$..name|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "CategoryType",
						"jsonPath": "Donations[0].category",
						"label": "wc.create.groups.connectionDetails.categoryType",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": false,
						"isDisabled": false,
						"url": "/wcms/masters/categorytype/_search?&active=true|$..name|$..name",
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "UsageType",
						"jsonPath": "Donations[0].usageType",
						"label": "wc.create.groups.connectionDetails.usageType",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": false,
						"isDisabled": false,
						"url": "/pt-property/property/usages/_search?&active=true|$..name|$..name",
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "hscPipeSizeType",
						"jsonPath": "Donations[0].minPipeSize",
						"label": "wc.create.minPipeSize",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": false,
						"isDisabled": false,
						"url": "/wcms/masters/pipesize/_search?&active=true|$..sizeInMilimeter|$..sizeInMilimeter",
						"requiredErrMsg": "",
						"defaultValue":"Donation.minPipeSize",
						"patternErrMsg": ""
					},
					{
						"name": "hscPipeSizeType",
						"jsonPath": "Donations[0].maxPipeSize",
						"label": "wc.create.maxPipeSize",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": false,
						"isDisabled": false,
						"url": "/wcms/masters/pipesize/_search?&active=true|$..sizeInMilimeter|$..sizeInMilimeter",
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "donationAmount",
						"jsonPath": "Donations[0].donationAmount",
						"label": "wc.create.donationAmount",
						"pattern": "^\\d+(\\.\\d+)?$",
						"type": "number",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "fromDate",
						"jsonPath": "Donations[0].fromDate",
						"label": "wc.create.fromDate",
						"pattern": "",
						"type": "datePicker",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "fromDate",
						"jsonPath": "Donations[0].toDate",
						"label": "wc.create.toDate",
						"pattern": "",
						"type": "datePicker",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "Active",
						"jsonPath": "Donations[0].active",
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
