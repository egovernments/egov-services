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
          "name": "acknowledgementNumber",
          "jsonPath": "acknowledgementNumber",
          "label": "Acknowledgement Number",
          "pattern": "",
          "type": "text",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        },
        {
          "name": "consumerNumber",
          "jsonPath": "consumerNumber",
          "label": "Consumer Number",
          "pattern": "",
          "type": "text",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        },
        {
          "name": "name",
          "jsonPath": "name",
          "label": "Name",
          "pattern": "",
          "type": "text",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        },
        {
          "name": "mobileNumber",
          "jsonPath": "mobileNumber",
          "label": "Mobile Number",
          "pattern": "",
          "type": "text",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        },
        {
          "name": "locality",
          "jsonPath": "locality",
          "label": "Locality",
          "pattern": "",
          "type": "text",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        },
        {
          "name": "legacyConsumerNumber",
          "jsonPath": "revenueWard",
          "label": "Revenue Ward",
          "pattern": "",
          "type": "text",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        },
        {
          "name": "doorNumber",
          "jsonPath": "doorNumber",
          "label": "Door Number",
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
