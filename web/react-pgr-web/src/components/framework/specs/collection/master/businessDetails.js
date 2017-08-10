var dat = {
	"collection.create": {
		"numCols": 12/3,
		"url":  "/egov-common-masters/businessDetails/_create",
		"tenantIdRequired": true,
		// "idJsonPath": "BusinessDetails[0].id",
		"useTimestamp": true,
		"objectName": "BusinessDetails",
		"groups": [
			{
				"label": "wc.create.businessDetailsType.title",
				"name": "businessDetailsType",
				"fields": [
          {
            "name": "CategoryType",
            "jsonPath": "BusinessDetails.businessType",
            "label": "wc.create.groups.fields.businessType",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egov-common-masters/businessCategory/_search?&active=true|$..name|$..name",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
            {
							"name": "Code",
							"jsonPath": "BusinessDetails.code",
							"label": "wc.create.group.fields.serviceCode",
							"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Length minimum is 3 and maximum is 100"
						},
            {
							"name": "Name",
							"jsonPath": "BusinessDetails.name",
							"label": "wc.create.group.fields.businessName",
							"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Length minimum is 3 and maximum is 100"
						},
            {
              "name": "Classification",
              "jsonPath": "BusinessDetails.classification",
              "label": "wc.create.groups.fields.classification",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egov-common-masters/businessCategory/_search?&active=true|$..name|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
							"name": "Create voucher on receipt creation",
							"jsonPath": "BusinessDetails.voucherCreation",
							"label": "wc.create.voucherOnReceiptCreation",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"defaultValue":true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "Active",
							"jsonPath": "BusinessDetails.active",
							"label": "wc.create.active",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"defaultValue":true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			},
      {
				"label": "Financial Details",
				"name": "Financial Details",
				"fields": [
            {
              "name": "Fund",
              "jsonPath": "BusinessDetails.Fund",
              "label": "wc.create.groups.fields.Fund",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egf-masters/funds/_search?|$..name|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "Funtion",
              "jsonPath": "BusinessDetails.Funtion",
              "label": "wc.create.groups.fields.Funtion",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egf-masters/functions/_search?|$..name|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            }
				]
			},
      {
				"label": "Account Details",
				"name": "AccountDetails",
				"multiple":true,
        "jsonPath":"BusinessDetails[0].serviceAccountDetails",
				"fields": [
						{
							"name": "AccountHead",
							"jsonPath": "BusinessDetails[0].serviceAccountDetails[0].fromUnit",
							"label": "wc.create.groups.fields.accountHead",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "AccountCode",
							"jsonPath": "BusinessDetails[0].serviceAccountDetails[0].toUnit",
							"label": "wc.create.groups.fields.accountCode",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Amount",
							"jsonPath": "BusinessDetails[0].serviceAccountDetails[0].unitRate",
							"label": "wc.create.groups.fields.amount",
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
	"collection.search": {
		"numCols": 12/3,
		"url": "/egov-common-masters/businessDetails/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "BusinessDetailsInfo",
		"groups": [
			{
				"label": "wc.search.businessDetailsType.title",
				"name": "businessDetailsType",
				"fields": [
          {
            "name": "Name",
            "jsonPath": "name",
            "label": "wc.create.group.fields.businessName",
            "pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "Length minimum is 3 and maximum is 100"
          },
				]
			}
		],
		"result": {
			"header": [{label: "wc.search.result.businessName"}, {label: "wc.search.result.businessCode"}, {label: "wc.search.result.active"}],
			"values": ["name", "code", "active"],
			"resultPath": "BusinessDetailsInfo",
			"rowClickUrlUpdate": "/update/collection/businessDetails/{id}",
			"rowClickUrlView": "/view/collection/businessDetails/{id}"
			}
	},
	"collection.view": {
		"numCols": 12/3,
		"url":  "/egov-common-masters/businessDetails/_search?ids={id}",
		"tenantIdRequired": true,
		// "idJsonPath": "BusinessDetails[0].id",
		"useTimestamp": true,
		"objectName": "BusinessDetailsInfo",
		"groups": [
			{
				"label": "wc.create.businessDetailsType.title",
				"name": "businessDetailsType",
				"fields": [
          {
            "name": "CategoryType",
            "jsonPath": "BusinessDetailsInfo[0].businessCategory",
            "label": "wc.create.groups.fields.businessType",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egov-common-masters/businessCategory/_search?&active=true|$..name|$..name",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
            {
							"name": "Code",
							"jsonPath": "BusinessDetailsInfo[0].code",
							"label": "wc.create.group.fields.serviceCode",
							"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Length minimum is 3 and maximum is 100"
						},
            {
							"name": "Name",
							"jsonPath": "BusinessDetailsInfo[0].name",
							"label": "wc.create.group.fields.businessName",
							"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Length minimum is 3 and maximum is 100"
						},
            {
              "name": "Classification",
              "jsonPath": "BusinessDetailsInfo[0].classification",
              "label": "wc.create.groups.fields.classification",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egov-common-masters/businessCategory/_search?&active=true|$..name|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
							"name": "Create voucher on receipt creation",
							"jsonPath": "BusinessDetailsInfo[0].voucherCreation",
							"label": "wc.create.voucherOnReceiptCreation",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"defaultValue":true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "Active",
							"jsonPath": "BusinessDetailsInfo[0].active",
							"label": "wc.create.active",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"defaultValue":true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			},
      {
				"label": "Financial Details",
				"name": "Financial Details",
				"fields": [
            {
              "name": "Fund",
              "jsonPath": "BusinessDetailsInfo[0].fund",
              "label": "wc.create.groups.fields.Fund",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egf-masters/funds/_search?|$..name|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "Funtion",
              "jsonPath": "BusinessDetailsInfo[0].function",
              "label": "wc.create.groups.fields.Function",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egf-masters/functions/_search?|$..name|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            }
				]
			},
      {
				"label": "Account Details",
				"name": "AccountDetails",
				"multiple":true,
        "jsonPath":"BusinessDetailsInfo[0].serviceAccountDetails",
				"fields": [
						{
							"name": "AccountHead",
							"jsonPath": "BusinessDetailsInfo[0].serviceAccountDetails[0].fromUnit",
							"label": "wc.create.groups.fields.accountHead",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "AccountCode",
							"jsonPath": "BusinessDetailsInfo[0].serviceAccountDetails[0].toUnit",
							"label": "wc.create.groups.fields.accountCode",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Amount",
							"jsonPath": "BusinessDetails[0].serviceAccountDetails[0].unitRate",
							"label": "wc.create.groups.fields.amount",
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
	"collection.update": {
		"numCols": 12/3,
		"url":  "/egov-common-masters/businessDetails[0].id/_search",
    "searchUrl":  "/egov-common-masters/businessDetails/_search?ids={id}",
		"tenantIdRequired": true,
		// "idJsonPath": "BusinessDetails[0].id",
		"useTimestamp": true,
		"objectName": "BusinessDetails",
		"groups": [
			{
				"label": "wc.create.businessDetailsType.title",
				"name": "businessDetailsType",
				"fields": [
          {
            "name": "CategoryType",
            "jsonPath": "BusinessDetails.businessType",
            "label": "wc.create.groups.fields.businessType",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egov-common-masters/businessCategory/_search?&active=true|$..name|$..name",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
            {
							"name": "Code",
							"jsonPath": "BusinessDetails.code",
							"label": "wc.create.group.fields.serviceCode",
							"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Length minimum is 3 and maximum is 100"
						},
            {
							"name": "Name",
							"jsonPath": "BusinessDetails.name",
							"label": "wc.create.group.fields.businessName",
							"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
							"type": "text",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "Length minimum is 3 and maximum is 100"
						},
            {
              "name": "Classification",
              "jsonPath": "BusinessDetails.classification",
              "label": "wc.create.groups.fields.classification",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egov-common-masters/businessCategory/_search?&active=true|$..name|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
							"name": "Create voucher on receipt creation",
							"jsonPath": "BusinessDetails.voucherCreation",
							"label": "wc.create.voucherOnReceiptCreation",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"defaultValue":true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
            {
							"name": "Active",
							"jsonPath": "BusinessDetails.active",
							"label": "wc.create.active",
							"pattern": "",
							"type": "checkbox",
							"isRequired": false,
							"defaultValue":true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			},
      {
				"label": "Financial Details",
				"name": "Financial Details",
				"fields": [
            {
              "name": "Fund",
              "jsonPath": "BusinessDetails.Fund",
              "label": "wc.create.groups.fields.Fund",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egf-masters/funds/_search?|$..name|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "Funtion",
              "jsonPath": "BusinessDetails.Funtion",
              "label": "wc.create.groups.fields.Funtion",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egf-masters/functions/_search?|$..name|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            }
				]
			},
      {
				"label": "Account Details",
				"name": "AccountDetails",
				"multiple":true,
        "jsonPath":"BusinessDetails[0].serviceAccountDetails",
				"fields": [
						{
							"name": "AccountHead",
							"jsonPath": "BusinessDetails[0].serviceAccountDetails[0].fromUnit",
							"label": "wc.create.groups.fields.accountHead",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"url": "",
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "AccountCode",
							"jsonPath": "BusinessDetails[0].serviceAccountDetails[0].toUnit",
							"label": "wc.create.groups.fields.accountCode",
							"pattern": "",
							"type": "number",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "Amount",
							"jsonPath": "BusinessDetails[0].serviceAccountDetails[0].unitRate",
							"label": "wc.create.groups.fields.amount",
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
