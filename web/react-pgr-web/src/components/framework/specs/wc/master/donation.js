var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/donation/_create",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "Donation",
		"groups": [
			{
				"label": "wc.create.donation.title",
				"name": "donation",
				"fields": [
					{
						"name": "propertyType",
						"jsonPath": "Donation.propertyType",
						"label": "wc.create.propertyType",
						"pattern": "",
						"type": "singleValueList",
						"url": "/pt-property/property/propertytypes/_search?|$..name|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
          {
								"name": "CategoryType",
								"jsonPath": "Donation.category",
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
						"jsonPath": "Donation.usageType",
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
						"jsonPath": "Donation.minPipeSize",
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
						"jsonPath": "Donation.maxPipeSize",
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
            "jsonPath": "Donation.donationAmount",
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
            "jsonPath": "Donation.fromDate",
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
            "jsonPath": "Donation.toDate",
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
						"jsonPath": "Donation.active",
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
						"url": "/pt-property/property/propertytypes/_search?|$..name|$..name",
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
								"url": "/wcms/masters/categorytype/_search?|$..name|$..name",
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
						"url": "/pt-property/property/usages/_search?|$..name|$..name",
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
	}
}

export default dat;
