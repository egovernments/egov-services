var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/documenttype-applicationtype/_create",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "donation",
		"groups": [
			{
				"label": "wc.create.donation.title",
				"name": "donation",
				"fields": [
					{
						"name": "propertyType",
						"jsonPath": "donation.propertyType",
						"label": "wc.create.propertyType",
						"pattern": "",
						"type": "singleValueList",
						"url": "/pt-property/property/propertytypes/_search?|$..id|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
          {
								"name": "CategoryType",
								"jsonPath": "donation.categoryType",
								"label": "wc.create.groups.connectionDetails.categoryType",
								"pattern": "",
								"type": "singleValueList",
								"isRequired": false,
								"isDisabled": false,
								"url": "/wcms/masters/categorytype/_search?|$..id|$..name",
								"requiredErrMsg": "",
								"patternErrMsg": ""
					},
          {
						"name": "UsageType",
						"jsonPath": "donation.usageType",
						"label": "wc.create.groups.connectionDetails.usageType",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": false,
						"isDisabled": false,
						"url": "/pt-property/property/usages/_search?|$..id|$..name",
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
          {
						"name": "hscPipeSizeType",
						"jsonPath": "donation.minHscPipeSizeType",
						"label": "Min H.S.C Pipe Size :",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": false,
						"isDisabled": false,
						"url": "/wcms/masters/pipesize/_search?|$..id|$..sizeInMilimeter",
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
          {
						"name": "hscPipeSizeType",
						"jsonPath": "donation.maxHscPipeSizeType",
						"label": "Max H.S.C Pipe Size",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": false,
						"isDisabled": false,
						"url": "/wcms/masters/pipesize/_search?|$..id|$..sizeInMilimeter",
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
          {
            "name": "donationAmount",
            "jsonPath": "pipeSize.donationAmount",
            "label": "Donation Amount",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },

				]
			}
		]
	}
}

export default dat;
