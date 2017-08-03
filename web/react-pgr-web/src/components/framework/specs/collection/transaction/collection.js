var cashOrMops = {
  "name": "FloorDetailsComponent",
  "version": "v1", //Maps to parent version
  "level": 1,
  "jsonPath": "collection.modeOfPayment",
  "groups": [{
    "label": "Cash",
    "name": "cash",
    "multiple": false, //If true, its an array
    "children": [],
    "fields": [{
        "name": "paidBy",
        "jsonPath": "collection.modeOfPayment.paidBy",
        "label": "Paid By",
        "pattern": "",
        "type": "text",
        "isRequired": true,
        "isDisabled": false,
        "requiredErrMsg": "", //Remove required messages
        "patternErrMsg": ""
      }
    ]
  }]
}

var chequeOrDD={
  "name": "chequeOrDD",
  "version": "v1", //Maps to parent version
  "level": 1,
  "jsonPath": "collection.modeOfPayment",
  "groups": [{
    "label": "Cheque Or DD",
    "name": "chequeOrDD",
    "multiple": false, //If true, its an array
    "children": [],
    "fields": [{
        "name": "chequeOrDDNumber",
        "jsonPath": "collection.modeOfPayment.chequeOrDD.chequeOrDDNumber",
        "label": "Cheque/DD Number",
        "pattern": "",
        "type": "text",
        "isRequired": true,
        "isDisabled": false,
        "requiredErrMsg": "",
        "patternErrMsg": ""
      },
			{
	        "name": "chequeOrDDDate",
	        "jsonPath": "collection.modeOfPayment.chequeOrDD.chequeOrDDDate",
	        "label": "Cheque/DD Date",
	        "pattern": "",
	        "type": "datePicker",
	        "isRequired": true,
	        "isDisabled": false,
	        "requiredErrMsg": "",
	        "patternErrMsg": ""
	      }
			,
			{
					"name": "chequeOrDDBankName",
					"jsonPath": "collection.modeOfPayment.chequeOrDD.chequeOrDDBankName",
					"label": "Cheque/DD Bank Name",
					"pattern": "",
					"type": "text",
					"isRequired": true,
					"isDisabled": false,
					"requiredErrMsg": "",
					"patternErrMsg": ""
				}
				,
				{
						"name": "chequeOrDDBranchName",
						"jsonPath": "collection.modeOfPayment.chequeOrDD.chequeOrDDBranchName",
						"label": "Cheque/DD Branch Name",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
			        "name": "paidBy",
			        "jsonPath": "collection.modeOfPayment.paidBy",
			        "label": "Paid By",
			        "pattern": "",
			        "type": "text",
			        "isRequired": true,
			        "isDisabled": false,
			        "requiredErrMsg": "", //Remove required messages
			        "patternErrMsg": ""
			      }
    ]
  }]
}

// var creditOrDebitCard={
//   "name": "FloorDetailsComponent",
//   "version": "v1", //Maps to parent version
//   "level": 1,
//   "jsonPath": "connection.floors",
//   "groups": [{
//     "label": "wc.create.groups.floorDetails.title",
//     "name": "FloorDetails",
//     "multiple": false, //If true, its an array
//     "children": [],
//     "fields": [{
//         "name": "FloorNo",
//         "jsonPath": "connection.floors[0].floorNo",
//         "label": "wc.create.groups.floorDetails.floorNo",
//         "pattern": "",
//         "type": "number",
//         "isRequired": true,
//         "isDisabled": false,
//         "requiredErrMsg": "", //Remove required messages
//         "patternErrMsg": ""
//       },
//       {
//         "name": "FloorName",
//         "jsonPath": "connection.floors[0].floorName",
//         "label": "wc.create.groups.floorDetails.floorName",
//         "pattern": "",
//         "type": "text",
//         "isRequired": false,
//         "isDisabled": false,
//         "requiredErrMsg": "", //Remove required messages
//         "patternErrMsg": ""
//       }
//     ]
//   }]
// }

// var directBank={
//   "name": "FloorDetailsComponent",
//   "version": "v1", //Maps to parent version
//   "level": 1,
//   "jsonPath": "connection.floors",
//   "groups": [{
//     "label": "wc.create.groups.floorDetails.title",
//     "name": "FloorDetails",
//     "multiple": false, //If true, its an array
//     "children": [],
//     "fields": [{
//         "name": "FloorNo",
//         "jsonPath": "connection.floors[0].floorNo",
//         "label": "wc.create.groups.floorDetails.floorNo",
//         "pattern": "",
//         "type": "number",
//         "isRequired": true,
//         "isDisabled": false,
//         "requiredErrMsg": "", //Remove required messages
//         "patternErrMsg": ""
//       },
//       {
//         "name": "FloorName",
//         "jsonPath": "connection.floors[0].floorName",
//         "label": "wc.create.groups.floorDetails.floorName",
//         "pattern": "",
//         "type": "text",
//         "isRequired": false,
//         "isDisabled": false,
//         "requiredErrMsg": "", //Remove required messages
//         "patternErrMsg": ""
//       }
//     ]
//   }]
// }

var dat = {
  "collection.transaction": {
    "numCols": "6",
    "url": "/billing-service/bill/_generate",
    "tenantIdRequired": true,
    "objectName": "search",
    "groups": [{
      "label": "Pay tax",
      "name": "createDocumentType",
      "fields": [{
          "name": "mobile",
          "jsonPath": "collection.search.mobile",
          "label": "Mobile",
          "pattern": "",
          "type": "number",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        },
        {
          "name": "email",
          "jsonPath": "collection.search.email",
          "label": "Email",
          "pattern": "",
          "type": "email",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        },
        {
          "name": "billerService",
          "jsonPath": "collection.search.billerService",
          "label": "Billing service name",
          "pattern": "",
          "type": "singleValueList",
          "isRequired": true,
          "isDisabled": false,
          "url": "egov-common-masters/businessDetails/_search?tenantId=default|$..code|$..name",
          "requiredErrMsg": "",
          "patternErrMsg": ""
        },
        {
          "name": "consumerCode",
          "jsonPath": "collection.search.consumerCode",
          "label": "Consumer Code",
          "pattern": "",
          "type": "text",
          "isRequired": true,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        }
      ]
    }],
    "transaction": [{
      "label": "Payment",
      "name": "paymentMode",
      "children": [cashOrMops,chequeOrDD],
      "fields": [
				{
					"name": "totalAmountPaid",
					"jsonPath": "collection.create.paymentMode.totalAmountPaid",
					"label": "Total Amount Paid",
					"pattern": "",
					"type": "label",
					"isRequired": false,
					"isDisabled": false,
					"isHidden": false,
					"defaultValue": "",
					"requiredErrMsg": "",
					"patternErrMsg": ""
				},
				{
        "name": "modeOfPayment",
        "jsonPath": "collection.create.modeOfPayment",
        "label": "Mode Of Payment",
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
            "key": "1",
            "value": "Cash"
          },
          {
            "key": "2",
            "value": "Check"
          },
          {
            "key": "3",
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
      }]
    }]
  }
}





export default dat;
