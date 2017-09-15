var dat = {
	"wc.create": {
		"numCols": 12/3,
		"url": "/wcms/masters/meterwaterrates/_create",
		"tenantIdRequired": true,
		"idJsonPath": "MeterWaterRates[0].code",
		"useTimestamp": true,
		"objectName": "MeterWaterRates",
		"groups": [
			{
				"label": "wc.create.meterWaterRates.title",
				"name": "meterWaterRateCreate",
				"fields": [
						{
							"name": "usageTypeName",
							"jsonPath": "MeterWaterRates[0].usageTypeName",
							"label": "wc.create.groups.fields.usageTypeName",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/usagetype/_search?&active=true|$..name|$..name",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "sourceTypeName",
							"jsonPath": "MeterWaterRates[0].sourceTypeName",
							"label": "wc.create.groups.fields.sourceTypeName",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/sourcetype/_search?&active=true|$..name|$..name",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
  						"name": "pipeSize",
  						"jsonPath": "MeterWaterRates[0].pipeSize",
  						"label": "wc.create.pipeSize",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "/wcms/masters/pipesize/_search?&active=true|$..sizeInMilimeter|$..sizeInMilimeter",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
              "name": "fromDate",
              "jsonPath": "MeterWaterRates[0].fromDate",
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
              "jsonPath": "MeterWaterRates[0].toDate",
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
  						"jsonPath": "MeterWaterRates[0].active",
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
			},
      {
				"label": "wc.crete.Slabs",
				"name": "Slabs",
				"multiple":true,
        "jsonPath":"MeterWaterRates[0].slab",
				"fields": [
						{
							"name": "FromUnit",
							"jsonPath": "MeterWaterRates[0].slab[0].fromUnit",
							"label": "wc.create.groups.Slabs.fields.fromUnit",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "ToUnit",
							"jsonPath": "MeterWaterRates[0].slab[0].toUnit",
							"label": "wc.create.groups.Slabs.fields.toUnit",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "UnitRate",
							"jsonPath": "MeterWaterRates[0].slab[0].unitRate",
							"label": "wc.create.groups.Slabs.fields.unitRate",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}
		]
	},
	"wc.search": {
		"numCols": 12/3,
		"url": "/wcms/masters/meterwaterrates/_search",
		"tenantIdRequired": true,

		"useTimestamp": true,
		"objectName": "meterwaterrates",
		"groups": [
			{
				"label": "wc.search.meterWaterRates.title",
				"name": "searchStorageReservoir",
				"fields": [
						{
							"name": "usageTypeName",
							"jsonPath": "MeterWaterRates[0].usageTypeName",
							"label": "wc.create.groups.fields.usageTypeName",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/usagetype/_search?&active=true|$..name|$..name",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "sourceTypeName",
							"jsonPath": "MeterWaterRates[0].sourceTypeName",
							"label": "wc.create.groups.fields.sourceTypeName",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/sourcetype/_search?&active=true|$..name|$..name",
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
  						"url": "/wcms/masters/pipesize/_search?&active=true|$..sizeInMilimeter|$..sizeInMilimeter",
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
			"header": [{label: "wc.create.groups.fields.usageTypeName"},{label: "wc.create.groups.fields.sourceTypeName"}, {label: "wc.create.pipeSize"}],
			"values": ["usageTypeName", "sourceTypeName","pipeSize"],
			"resultPath": "MeterWaterRates",
			"rowClickUrlUpdate": "/update/wc/meterWaterRates/{id}",
			"rowClickUrlView": "/view/wc/meterWaterRates/{id}"
			}
	},
	"wc.view": {
		"numCols": 12/3,
		"url": "/wcms/masters/meterwaterrates/_search?id={id}",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "MeterWaterRates",
		"groups": [
			{
				"label": "wc.view.meterWaterRates.title",
				"name": "meterWaterRateCreate",
				"fields": [
						{
							"name": "usageTypeName",
							"jsonPath": "MeterWaterRates[0].usageTypeName",
							"label": "wc.create.groups.fields.usageTypeName",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/usagetype/_search?&active=true|$..name|$..name",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "sourceTypeName",
							"jsonPath": "MeterWaterRates[0].sourceTypeName",
							"label": "wc.create.groups.fields.sourceTypeName",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/sourcetype/_search?&active=true|$..id|$..name",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
  						"name": "pipeSize",
  						"jsonPath": "MeterWaterRates[0].pipeSize",
  						"label": "wc.create.pipeSize",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "/wcms/masters/pipesize/_search?&active=true|$..sizeInMilimeter|$..sizeInMilimeter",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
              "name": "fromDate",
              "jsonPath": "MeterWaterRates[0].fromDate",
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
              "jsonPath": "MeterWaterRates[0].toDate",
              "label": "wc.create.toDate",
              "pattern": "",
              "type": "datePicker",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            }
				]
			},
      {
				"label": "wc.crete.Slabs",
				"name": "Slabs",
				"multiple":true,
        "jsonPath":"MeterWaterRates[0].slab",
				"fields": [
						{
							"name": "FromUnit",
							"jsonPath": "slab[0].fromUnit",
							"label": "wc.create.groups.Slabs.fields.fromUnit",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "ToUnit",
							"jsonPath": "slab.toUnit",
							"label": "wc.create.groups.Slabs.fields.toUnit",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "UnitRate",
							"jsonPath": "slab[0].unitRate",
							"label": "wc.create.groups.Slabs.fields.unitRate",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}
		]
	},
	"wc.update" : {
		"numCols": 12/3,
    "searchUrl": "/wcms/masters/meterwaterrates/_search?id={id}",
		"url":"/wcms/masters/meterwaterrates/_update",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "MeterWaterRates",
		"groups": [
			{
				"label": "wc.create.meterWaterRates.title",
				"name": "meterWaterRateCreate",
				"fields": [
						{
							"name": "usageTypeName",
							"jsonPath": "MeterWaterRates[0].usageTypeName",
							"label": "wc.create.groups.fields.usageTypeName",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/usagetype/_search?&active=true|$..name|$..name",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "sourceTypeName",
							"jsonPath": "MeterWaterRates[0].sourceTypeName",
							"label": "wc.create.groups.fields.sourceTypeName",
							"pattern": "",
							"type": "singleValueList",
							"url": "/wcms/masters/sourcetype/_search?&active=true|$..name|$..name",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
  						"name": "pipeSize",
  						"jsonPath": "MeterWaterRates[0].pipeSize",
  						"label": "wc.create.pipeSize",
  						"pattern": "",
  						"type": "singleValueList",
  						"url": "/wcms/masters/pipesize/_search?&active=true|$..sizeInMilimeter|$..sizeInMilimeter",
  						"isRequired": true,
  						"isDisabled": false,
  						"requiredErrMsg": "",
  						"patternErrMsg": ""
  					},
            {
              "name": "fromDate",
              "jsonPath": "MeterWaterRates[0].fromDate",
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
              "jsonPath": "MeterWaterRates[0].toDate",
              "label": "wc.create.toDate",
              "pattern": "",
              "type": "datePicker",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            }
				]
			},
      {
				"label": "wc.crete.Slabs",
				"name": "Slabs",
				"multiple":true,
        "jsonPath":"MeterWaterRates[0].slab",
				"fields": [
						{
							"name": "FromUnit",
							"jsonPath": "slab[0].fromUnit",
							"label": "wc.create.groups.Slabs.fields.fromUnit",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "ToUnit",
							"jsonPath": "slab[0].toUnit",
							"label": "wc.create.groups.Slabs.fields.toUnit",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "UnitRate",
							"jsonPath": "slab[0].unitRate",
							"label": "wc.create.groups.Slabs.fields.unitRate",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			}
		]
	}
}

export default dat;
