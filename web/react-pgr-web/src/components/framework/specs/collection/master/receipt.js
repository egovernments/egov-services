var dat = {
	// "collection.create": {
	// 	"numCols": 12/3,
	// 	"url": "/collectionms/masters/categorytype/_create",
	// 	"tenantIdRequired": true,
	// 	"idJsonPath": "CategoryTypes[0].code",
	// 	"objectName": "CategoryType",
	// 	"groups": [
	// 		{
	// 			"label": "collection.create.categorytype.title",
	// 			"name": "createCategoryType",
	// 			"fields": [
	// 					{
	// 						"name": "name",
	// 						"jsonPath": "CategoryType.name",
	// 						"label": "collection.create.categorytype",
	// 						"pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
	// 						"type": "text",
	// 						"isRequired": true,
	// 						"isDisabled": false,
	// 						"requiredErrMsg": "",
	// 						"patternErrMsg": "Length is more than 100"
	// 					},
	// 					{
	// 						"name": "description",
	// 						"jsonPath": "CategoryType.description",
	// 						"label": "collection.create.description",
	// 						"pattern": "^[\s.]*([^\s.][\s.]*){0,250}$",
	// 						"type": "text",
	// 						"isRequired": false,
	// 						"isDisabled": false,
	// 						"requiredErrMsg": "",
	// 						"patternErrMsg": "Length is more than 250"
	// 					},
	// 					{
	// 						"name": "Active",
	// 						"jsonPath": "CategoryType.active",
	// 						"label": "collection.create.active",
	// 						"pattern": "",
	// 						"type": "checkbox",
	// 						"isRequired": false,
	// 						"defaultValue":true,
	// 						"isDisabled": false,
	// 						"requiredErrMsg": "",
	// 						"patternErrMsg": ""
	// 					}
	// 			]
	// 		}
	// 	]
	// },
	"collection.search": {
		"numCols": 12/3,
		"url": "/collection-services/receipts/_search",
		"tenantIdRequired": true,
		"useTimestamp": true,
		"objectName": "",
		"groups": [
			{
				"label": "collection.search.categorytype.title",
				"name": "createCategoryType",
				"fields": [
						{
							"name": "serviceType",
							"jsonPath": "serviceType",
							"label": "collection.create.serviceType",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"url":"/collection-services/receipts/_getDistinctBusinessDetails|$..code|$..name"
						},
						{
							"name": "fromDate",
							"jsonPath": "fromDate",
							"label": "collection.create.fromDate",
							"pattern": "",
							"type": "datePicker",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "toDate",
							"jsonPath": "toDate",
							"label": "collection.create.toDate",
							"pattern": "",
							"type": "datePicker",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "receiptNumber",
							"jsonPath": "receiptNumber",
							"label": "collection.create.receiptNumber",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "collectedBy",
							"jsonPath": "collectedBy",
							"label": "collection.create.collectedBy",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"url":"/collection-services/receipts/_getDistinctCollectedBy|$..id|$..name"
						},
						{
							"name": "status",
							"jsonPath": "status",
							"label": "collection.create.status",
							"pattern": "",
							"type": "singleValueList",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"url":"",
							"defaultValue":[]
						},
						{
		        "name": "modeOfPayment",
		        "jsonPath": "modeOfPayment",
		        "label": "collection.create.modeOfPayment",
		        "pattern": "",
		        "type": "singleValueList",
		        "isRequired": false,
		        "isDisabled": false,
		        "url": "",
		        "requiredErrMsg": "",
		        "patternErrMsg": "",
		        "defaultValue": [{
		            "key": "",
		            "value": null
		          },
		          {
		            "key": "Cash",
		            "value": "Cash"
		          },
		          {
		            "key": "Cheque",
		            "value": "Cheque"
		          },
		          {
		            "key": "DD",
		            "value": "DD"
		          },
		          // {
		          //   "key": "4",
		          //   "value": "Credit/Debit Card"
		          // },
		          // {
		          //   "key": "5",
		          //   "value": "Direct Bank"
		          // },
		          // {
		          //   "key": "6",
		          //   "value": "SBI MOPS Bank Callan"
		          // }
		        ]
		      }

				]
			}
		],
		"result": {
			"header": [{label: "collection.create.code"},{label: "collection.search.result.categoryType"}, {label: "collection.search.result.description"}, {label: "collection.search.result.active"}],
			"values": ["code","name", "description", "active"],
			"resultPath": "CategoryTypes",
			"rowClickUrlUpdate": "/update/collection/categoryType/{id}",
			"rowClickUrlView": "/view/collection/categoryType/{id}"
			}
	}
  // ,
	// "collection.view": {
	// 	"numCols": 12/3,
	// 	"url": "/collectionms/masters/categorytype/_search?id={id}",
	// 	"tenantIdRequired": true,
	// 	"useTimestamp": true,
	// 	"objectName": "CategoryType",
	// 	"groups": [
	// 		{
	// 			"label": "collection.view.categorytype.title",
	// 			"name": "viewCategoryType",
	// 			"fields": [
	// 					{
	// 						"name": "name",
	// 						"jsonPath": "CategoryTypes[0].name",
	// 						"label": "collection.create.categorytype",
	// 						"pattern": "",
	// 						"type": "text",
	// 						"isRequired": true,
	// 						"isDisabled": false,
	// 						"requiredErrMsg": "",
	// 						"patternErrMsg": ""
	// 					},
	// 					{
	// 						"name": "description",
	// 						"jsonPath": "CategoryTypes[0].description",
	// 						"label": "collection.create.description",
	// 						"pattern": "",
	// 						"type": "text",
	// 						"isRequired": false,
	// 						"isDisabled": false,
	// 						"requiredErrMsg": "",
	// 						"patternErrMsg": ""
	// 					},
	// 					{
	// 						"name": "Active",
	// 						"jsonPath": "CategoryTypes[0].active",
	// 						"label": "collection.create.active",
	// 						"pattern": "",
	// 						"type": "checkbox",
	// 						"isRequired": false,
	// 						"isDisabled": false,
	// 						"requiredErrMsg": "",
	// 						"patternErrMsg": ""
	// 					}
	// 			]
	// 		}
	// 	]
	// },
	// "collection.update": {
	// 	"numCols": 12/3,
	// 	"searchUrl": "/collectionms/masters/categorytype/_search?id={id}",
	// 	"url":"/collectionms/masters/categorytype/{CategoryType.code}/_update",
	// 	"isResponseArray":true,
	// 	"tenantIdRequired": true,
	// 	"useTimestamp": true,
	// 	"objectName": "CategoryType",
	// 	"groups": [
	// 		{
	// 			"label": "collection.update.categorytype.title",
	// 			"name": "createCategoryType",
	// 			"fields": [
	// 					{
	// 						"name": "name",
	// 						"jsonPath": "CategoryType.name",
	// 						"label": "collection.create.categorytype",
	// 						"pattern": "^[\s.]*([^\s.][\s.]*){0,100}",
	// 						"type": "text",
	// 						"isRequired": true,
	// 						"isDisabled": false,
	// 						"requiredErrMsg": "",
	// 						"patternErrMsg": "Length is more than 100"
	// 					},
	// 					{
	// 						"name": "description",
	// 						"jsonPath": "CategoryType.description",
	// 						"label": "collection.create.description",
	// 						"pattern": "^[\s.]*([^\s.][\s.]*){0,250}",
	// 						"type": "text",
	// 						"isRequired": false,
	// 						"isDisabled": false,
	// 						"requiredErrMsg": "",
	// 						"patternErrMsg": "Length is more than 250"
	// 					},
	// 					{
	// 						"name": "Active",
	// 						"jsonPath": "CategoryType.active",
	// 						"label": "collection.create.active",
	// 						"pattern": "",
	// 						"type": "checkbox",
	// 						"isRequired": false,
	// 						"isDisabled": false,
	// 						"requiredErrMsg": "",
	// 						"patternErrMsg": ""
	// 					}
	// 			]
	// 		}
	// 	]
	// }
}

export default dat;
