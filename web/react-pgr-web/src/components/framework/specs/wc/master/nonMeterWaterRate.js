var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/nonmeterwaterrates/_create",
		"tenantIdRequired": true,
		"idJsonPath": "NonMeterWaterrates[0].code",
		"useTimestamp": true,
		"objectName": "NonMeterWaterrates",
		"groups": [
			{
				"label": "wc.create.NonMeterWaterrates.title",
				"name": "meterWaterRateCreate",
				"fields": [
					{
						"name": "UsageType",
						"jsonPath": "NonMeterWaterrates[0].usageTypeCode",
						"label": "wc.create.groups.connectionDetails.usageType",
						"pattern": "",
						"type": "singleValueList",
						"url": "/wcms/masters/usagetypes/_search?&active=true|$..code|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"depedants": [{
								"jsonPath": "NonMeterWaterrates[0].subUsageTypeCode",
								"type": "dropDown",
								"pattern": "/wcms/masters/usagetypes/_search?&isSubUsageType=true&parent={NonMeterWaterrates[0].usageTypeCode}|$..code|$..name"
							}]
					},
					{
						"name": "SubUsageType",
						"jsonPath": "NonMeterWaterrates[0].subUsageTypeCode",
						"label": "wc.create.groups.connectionDetails.subUsageType",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
            {
							"name": "sourceTypeName",
							"jsonPath": "NonMeterWaterrates[0].sourceTypeName",
							"label": "wc.create.groups.fields.sourceTypeName",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/sourcetypes/_search?&active=true|$..name|$..name",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
  						"name": "pipeSize",
  						"jsonPath": "NonMeterWaterrates[0].pipeSize",
  						"label": "wc.create.pipeSize",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "/wcms/masters/pipesizes/_search?&active=true|$..sizeInMilimeter|$..sizeInInch",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
              "name": "fromDate",
              "jsonPath": "NonMeterWaterrates[0].fromDate",
              "label": "wc.create.fromDate",
              "pattern": "",
              "type": "datePicker",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
  					{
              "name": "toDate",
              "jsonPath": "NonMeterWaterrates[0].toDate",
              "label": "wc.create.toDate",
              "pattern": "",
              "type": "datePicker",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "NoOfTaps",
              "jsonPath": "NonMeterWaterrates[0].noOfTaps",
              "label": "wc.create.groups.connectionDetails.fields.noOfTaps",
              "pattern": "^\\d{1,15}$",
              "type": "number",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "donationAmount",
              "jsonPath": "NonMeterWaterrates[0].amount",
              "label": "collection.search.amount",
              "pattern": "^\\d+(\\.\\d+)?$",
              "type": "number",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
  						"name": "Active",
  						"jsonPath": "NonMeterWaterrates[0].active",
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
		"url": "/wcms/masters/nonmeterwaterrates/_search",
		"tenantIdRequired": true,

		"useTimestamp": true,
		"objectName": "NonMeterWaterrates",
		"groups": [
			{
				"label": "wc.search.NonMeterWaterrates.title",
				"name": "searchStorageReservoir",
				"fields": [
						{
							"name": "usageTypeCode",
							"jsonPath": "NonMeterWaterrates[0].usageTypeCode",
							"label": "wc.create.groups.fields.usageTypeCode",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/usagetypes/_search?&active=true|$..name|$..name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "sourceTypeName",
							"jsonPath": "NonMeterWaterrates[0].sourceTypeName",
							"label": "wc.create.groups.fields.sourceTypeName",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/sourcetypes/_search?&active=true|$..name|$..name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
  						"name": "pipeSize",
  						"jsonPath": "pipeSize",
  						"label": "wc.create.pipeSize",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "/wcms/masters/pipesizes/_search?&active=true|$..sizeInMilimeter|$..sizeInInch",
  						"isRequired": false,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
							"name": "Active",
							"jsonPath": "active",
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
		],
		"result": {
			"header": [{label: "wc.create.groups.fields.usageTypeCode"},{label: "wc.create.groups.fields.subUsageTypeCode"},{label: "wc.create.groups.fields.sourceTypeName"}, {label: "wc.create.pipeSize"}],
			"values": ["usageTypeCode","subUsageTypeCode","sourceTypeName","pipeSize"],
			"resultPath": "NonMeterWaterrates",
			"rowClickUrlUpdate": "/update/wc/nonMeterWaterrate/{id}",
			"rowClickUrlView": "/view/wc/nonMeterWaterrate/{id}"
			}
	},
	"wc.view": {
		"numCols": 12/3,
		"url": "/wcms/masters/nonmeterwaterrates/_search?ids={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "NonMeterWaterrates",
		"groups": [
			{
				"label": "wc.view.NonMeterWaterrates.title",
				"name": "meterWaterRateCreate",
				"fields": [
					{
						"name": "UsageType",
						"jsonPath": "NonMeterWaterrates[0].usageTypeCode",
						"label": "wc.create.groups.connectionDetails.usageType",
						"pattern": "",
						"type": "singleValueList",
						"url": "/wcms/masters/usagetypes/_search?&active=true|$..code|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"depedants": [{
								"jsonPath": "NonMeterWaterrates[0].subUsageTypeCode",
								"type": "dropDown",
								"pattern": "/wcms/masters/usagetypes/_search?&isSubUsageType=true&parent={NonMeterWaterrates[0].usageTypeCode}|$..code|$..name"
							}]
					},
					{
						"name": "SubUsageType",
						"jsonPath": "NonMeterWaterrates[0].subUsageTypeCode",
						"label": "wc.create.groups.connectionDetails.subUsageType",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
            {
							"name": "sourceTypeName",
							"jsonPath": "NonMeterWaterrates[0].sourceTypeName",
							"label": "wc.create.groups.fields.sourceTypeName",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/sourcetypes/_search?&active=true|$..id|$..name",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
  						"name": "pipeSize",
  						"jsonPath": "NonMeterWaterrates[0].pipeSize",
  						"label": "wc.create.pipeSize",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "/wcms/masters/pipesizes/_search?&active=true|$..sizeInMilimeter|$..sizeInInch",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
              "name": "fromDate",
              "jsonPath": "NonMeterWaterrates[0].fromDate",
              "label": "wc.create.fromDate",
              "pattern": "",
              "type": "datePicker",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
  					{
              "name": "toDate",
              "jsonPath": "NonMeterWaterrates[0].toDate",
              "label": "wc.create.toDate",
              "pattern": "",
              "type": "datePicker",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "NoOfTaps",
              "jsonPath": "NonMeterWaterrates[0].noOfTaps",
              "label": "wc.create.groups.connectionDetails.fields.noOfTaps",
              "pattern": "^\\d{1,15}$",
              "type": "number",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "donationAmount",
              "jsonPath": "NonMeterWaterrates[0].amount",
              "label": "collection.search.amount",
              "pattern": "^\\d+(\\.\\d+)?$",
              "type": "number",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
  						"name": "Active",
  						"jsonPath": "NonMeterWaterrates[0].active",
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
	"wc.update" : {
		"numCols": 12/3,
    "searchUrl": "/wcms/masters/nonmeterwaterrates/_search?ids={id}",
		"url":"/wcms/masters/nonmeterwaterrates/_update",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "NonMeterWaterrates",
		"groups": [
			{
				"label": "wc.create.NonMeterWaterrates.title",
				"name": "meterWaterRateCreate",
				"fields": [
					{
						"name": "UsageType",
						"jsonPath": "NonMeterWaterrates[0].usageTypeCode",
						"label": "wc.create.groups.connectionDetails.usageType",
						"pattern": "",
						"type": "singleValueList",
						"url": "/wcms/masters/usagetypes/_search?&active=true|$..code|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"depedants": [{
								"jsonPath": "NonMeterWaterrates[0].subUsageTypeCode",
								"type": "dropDown",
								"pattern": "/wcms/masters/usagetypes/_search?&isSubUsageType=true&parent={NonMeterWaterrates[0].usageTypeCode}|$..code|$..name"
							}]
					},
					{
						"name": "SubUsageType",
						"jsonPath": "NonMeterWaterrates[0].subUsageTypeCode",
						"label": "wc.create.groups.connectionDetails.subUsageType",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
            {
							"name": "sourceTypeName",
							"jsonPath": "NonMeterWaterrates[0].sourceTypeName",
							"label": "wc.create.groups.fields.sourceTypeName",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/sourcetypes/_search?&active=true|$..name|$..name",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
  						"name": "pipeSize",
  						"jsonPath": "NonMeterWaterrates[0].pipeSize",
  						"label": "wc.create.pipeSize",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "/wcms/masters/pipesizes/_search?&active=true|$..sizeInMilimeter|$..sizeInInch",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
              "name": "fromDate",
              "jsonPath": "NonMeterWaterrates[0].fromDate",
              "label": "wc.create.fromDate",
              "pattern": "",
              "type": "datePicker",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
  					{
              "name": "toDate",
              "jsonPath": "NonMeterWaterrates[0].toDate",
              "label": "wc.create.toDate",
              "pattern": "",
              "type": "datePicker",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "NoOfTaps",
              "jsonPath": "NonMeterWaterrates[0].noOfTaps",
              "label": "wc.create.groups.connectionDetails.fields.noOfTaps",
              "pattern": "^\\d{1,15}$",
              "type": "number",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "donationAmount",
              "jsonPath": "NonMeterWaterrates[0].amount",
              "label": "collection.search.amount",
              "pattern": "^\\d+(\\.\\d+)?$",
              "type": "number",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
  						"name": "Active",
  						"jsonPath": "NonMeterWaterrates[0].active",
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
	}
}

export default dat;
