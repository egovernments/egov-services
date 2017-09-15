var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/nonmeterwaterrates/_create",
		"tenantIdRequired": true,
		"idJsonPath": "NonMeterWaterRates[0].id",
		"useTimestamp": true,
		"objectName": "NonMeterWaterRates",
		"groups": [
			{
				"label": "wc.create.NonMeterWaterRates.title",
				"name": "meterWaterRateCreate",
				"fields": [
          {
            "name": "UsageType",
            "jsonPath": "NonMeterWaterRates[0].usageTypeName",
            "label": "wc.create.groups.connectionDetails.usageType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "defaultValue": [],
            "url":"/wcms/masters/usagetypes/_search?&active=true|$..code|$..name",
            "depedants": [{
                "jsonPath": "NonMeterWaterRates[0].subUsageType",
                "type": "dropDown",
                "pattern": "/wcms/masters/usagetypes/_search?&parent={Connection.usageTypeName}&isSubUsageType=true|$..code|$..name"
              }]
          },
          {
            "name": "subUsageType",
            "jsonPath": "NonMeterWaterRates[0].subUsageType",
            "label": "wc.create.groups.connectionDetails.subUsageType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "defaultValue": [],
            "url":""
          },
            {
							"name": "sourceTypeName",
							"jsonPath": "NonMeterWaterRates[0].sourceTypeName",
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
              "name": "ConnectionType",
              "jsonPath": "NonMeterWaterRates[0].connectionType",
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
  						"name": "pipeSize",
  						"jsonPath": "NonMeterWaterRates[0].pipeSize",
  						"label": "wc.create.pipeSize",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "/wcms/masters/pipesizes/_search?&active=true|$..sizeInMilimeter|$..sizeInMilimeter",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
              "name": "NoOfTaps",
              "jsonPath": "NonMeterWaterRates[0].nooftaps",
              "label": "wc.create.groups.connectionDetails.fields.noOfTaps",
              "pattern": "^(0|[1-9][0-9]*)$",
              "type": "number",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "label": "wc.create.donation.subtitle",
              "name": "Amount",
              "jsonPath": "NonMeterWaterRates[0].amount",
              "label": "wc.create.Amount",
              "pattern": "",
              "type": "number",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "fromDate",
              "jsonPath": "NonMeterWaterRates[0].fromDate",
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
              "jsonPath": "NonMeterWaterRates[0].toDate",
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
  						"jsonPath": "NonMeterWaterRates[0].active",
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
		"objectName": "NonMeterWaterRates",
		"groups": [
			{
				"label": "wc.search.NonMeterWaterRates.title",
				"name": "searchStorageReservoir",
				"fields": [
						{
							"name": "usageTypeName",
							"jsonPath": "usageTypeName",
							"label": "wc.create.groups.fields.usageTypeName",
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
							"jsonPath": "sourceTypeName",
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
  						"url": "/wcms/masters/pipesizes/_search?&active=true|$..sizeInMilimeter|$..sizeInMilimeter",
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
			"header": [{label: "wc.create.groups.fields.usageTypeName"},{label: "wc.create.groups.connectionDetails.subUsageType"},{label: "wc.create.groups.fields.sourceTypeName"}, {label: "wc.create.pipeSize"}],
			"values": ["usageTypeName", "subUsageType","sourceTypeName","pipeSize"],
			"resultPath": "NonMeterWaterRates",
			"rowClickUrlUpdate": "/update/wc/nonMeterWaterRate/{id}",
			"rowClickUrlView": "/view/wc/nonMeterWaterRate/{id}"
			}
	},
  "wc.view": {
		"numCols": 12/3,
		"url": "/wcms/masters/nonmeterwaterrates/_search?id={id}",
    "tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "NonMeterWaterRates",
		"groups": [
			{
				"label": "wc.view.NonMeterWaterRates.title",
				"name": "meterWaterRateCreate",
				"fields": [
          {
            "name": "UsageType",
            "jsonPath": "NonMeterWaterRates[0].usageTypeName",
            "label": "wc.create.groups.connectionDetails.usageType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "defaultValue": [],
            "url":"/wcms/masters/usagetypes/_search?|$..code|$..name",
            "depedants": [{
                "jsonPath": "NonMeterWaterRates[0].subUsageType",
                "type": "dropDown",
                "pattern": "/wcms/masters/usagetypes/_search?&parent={Connection.usageTypeName}&isSubUsageType=true|$..code|$..name"
              }]
          },
          {
            "name": "subUsageType",
            "jsonPath": "NonMeterWaterRates[0].subUsageType",
            "label": "wc.create.groups.connectionDetails.subUsageType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "defaultValue": [],
            "url":""
          },
            {
							"name": "sourceTypeName",
							"jsonPath": "NonMeterWaterRates[0].sourceTypeName",
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
              "name": "ConnectionType",
              "jsonPath": "NonMeterWaterRates[0].connectionType",
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
  						"name": "pipeSize",
  						"jsonPath": "NonMeterWaterRates[0].pipeSize",
  						"label": "wc.create.pipeSize",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "/wcms/masters/pipesizes/_search?&active=true|$..sizeInMilimeter|$..sizeInMilimeter",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
              "name": "NoOfTaps",
              "jsonPath": "NonMeterWaterRates[0].nooftaps",
              "label": "wc.create.groups.connectionDetails.fields.noOfTaps",
              "pattern": "^(0|[1-9][0-9]*)$",
              "type": "number",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "label": "wc.create.donation.subtitle",
              "name": "Amount",
              "jsonPath": "NonMeterWaterRates[0].amount",
              "label": "wc.create.SpecialDonationCharges",
              "pattern": "",
              "type": "number",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "fromDate",
              "jsonPath": "NonMeterWaterRates[0].fromDate",
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
              "jsonPath": "NonMeterWaterRates[0].toDate",
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
  						"jsonPath": "NonMeterWaterRates[0].active",
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
  "wc.update": {
		"numCols": 12/3,
    "searchUrl": "/wcms/masters/nonmeterwaterrates/_search?id={id}",
		"url":"/wcms/masters/nonmeterwaterrates/_update",
		"tenantIdRequired": true,
		"idJsonPath": "NonMeterWaterRates[0].code",
		"useTimestamp": true,
		"objectName": "NonMeterWaterRates",
		"groups": [
			{
				"label": "wc.update.NonMeterWaterRates.title",
				"name": "meterWaterRateCreate",
				"fields": [
          {
            "name": "UsageType",
            "jsonPath": "NonMeterWaterRates[0].usageTypeName",
            "label": "wc.create.groups.connectionDetails.usageType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "defaultValue": [],
            "url":"/wcms/masters/usagetypes/_search?|$..code|$..name",
            "depedants": [{
                "jsonPath": "NonMeterWaterRates[0].subUsageType",
                "type": "dropDown",
                "pattern": "/wcms/masters/usagetypes/_search?&parent={Connection.usageTypeName}&isSubUsageType=true|$..code|$..name"
              }]
          },
          {
            "name": "subUsageType",
            "jsonPath": "NonMeterWaterRates[0].subUsageType",
            "label": "wc.create.groups.connectionDetails.subUsageType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "defaultValue": [],
            "url":""
          },
            {
							"name": "sourceTypeName",
							"jsonPath": "NonMeterWaterRates[0].sourceTypeName",
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
              "name": "ConnectionType",
              "jsonPath": "NonMeterWaterRates[0].connectionType",
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
  						"name": "pipeSize",
  						"jsonPath": "NonMeterWaterRates[0].pipeSize",
  						"label": "wc.create.pipeSize",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "/wcms/masters/pipesizes/_search?&active=true|$..sizeInMilimeter|$..sizeInMilimeter",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
              "name": "NoOfTaps",
              "jsonPath": "NonMeterWaterRates[0].nooftaps",
              "label": "wc.create.groups.connectionDetails.fields.noOfTaps",
              "pattern": "^(0|[1-9][0-9]*)$",
              "type": "number",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "label": "wc.create.donation.subtitle",
              "name": "Amount",
              "jsonPath": "NonMeterWaterRates[0].amount",
              "label": "wc.create.Amount",
              "pattern": "",
              "type": "number",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "fromDate",
              "jsonPath": "NonMeterWaterRates[0].fromDate",
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
              "jsonPath": "NonMeterWaterRates[0].toDate",
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
  						"jsonPath": "NonMeterWaterRates[0].active",
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
