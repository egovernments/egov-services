var dat = {
  "wc.searchconnection": {
    "numCols": 12 / 3,
    "url": "/wcms-connection/connection/_search",
    "tenantIdRequired": true,
    "useTimestamp": true,
    "objectName": "Connection",
    "groups": [{
      "label": "wc.search.searchnewconnection.title",
      "name": "createCategoryType",
      "fields": [{
          "name": "AcknowledgementNumber",
          "jsonPath": "acknowledgementNumber",
          "label": "wc.create.groups.applicantDetails.acknowledgementNumber",
          "pattern": "",
          "type": "text",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        },
        {
          "name": "ConsumerNumber",
          "jsonPath": "consumerNumber",
          "label": "wc.create.groups.applicantDetails.consumerNumber",
          "pattern": "",
          "type": "text",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        },
        {
          "name": "Name",
          "jsonPath": "name",
          "label": "wc.create.name",
          "pattern": "",
          "type": "text",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        },
        {
          "name": "MobileNumber",
          "jsonPath": "mobileNumber",
          "label": "wc.create.groups.applicantDetails.mobileNumber",
          "pattern": "",
          "type": "text",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        },
        {
          "name": "Locality",
          "jsonPath": "locality",
          "label": "wc.create.groups.applicantDetails.locality",
          "pattern": "",
          "type": "text",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        },
        {
          "name": "RevenueWard",
          "jsonPath": "revenueWard",
          "label": "tl.licenses.view.groups.revenueWardId",
          "pattern": "",
          "type": "text",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        }
      ]
    }]
  }

}

export default dat;
