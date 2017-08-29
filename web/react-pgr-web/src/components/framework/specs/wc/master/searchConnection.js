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
          "name": "ConsumerNumber",
          "jsonPath": "manualConsumerNumber",
          "label": "wc.create.groups.applicantDetails.manualConsumerNo",
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
          "type": "singleValueList",
          "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=LOCALITY&hierarchyTypeName=LOCATION|$.Boundary.*.boundaryNum|$.Boundary.*.name",
          "isRequired": false,
          "isDisabled": false,
          "requiredErrMsg": "",
          "patternErrMsg": ""
        },
        {
          "name": "RevenueWard",
          "jsonPath": "revenueWard",
          "label": "wc.create.groups.fields.ward",
          "pattern": "",
          "type": "singleValueList",
          "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=WARD&hierarchyTypeName=ADMINISTRATION|$.Boundary.*.boundaryNum|$.Boundary.*.name",
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
