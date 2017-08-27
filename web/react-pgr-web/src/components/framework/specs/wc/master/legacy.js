var meterReading = {
 "name": "MeterReading",
 "version": "v1",
 "level": 2,
 "jsonPath": "Connection.meter[0].meterReadings",
 "groups": [
  {
   "label": "wc.group.meterReading",
   "name": "MeterReading",
   "multiple": false,
   "hide":false,
   "children": [],
   "fields": [
    {
     "name": "Reading1",
     "jsonPath": "Connection.meter[0].meterReadings[0].reading",
     "label": "wc.group.reading1",
     "pattern": "",
     "type": "number",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",//Remove required messages
     "patternErrMsg": ""
    },
	{
     "name": "Reading2",
     "jsonPath": "Connection.meter[0].meterReadings[1].reading",
     "label": "wc.group.reading2",
     "pattern": "",
     "type": "number",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",//Remove required messages
     "patternErrMsg": ""
    },
  	{
       "name": "Reading3",
       "jsonPath": "Connection.meter[0].meterReadings[2].reading",
       "label": "wc.group.reading3",
       "pattern": "",
       "type": "number",
       "isRequired": false,
       "isDisabled": false,
       "requiredErrMsg": "",//Remove required messages
       "patternErrMsg": ""
      },
    {
     "name": "Reading1",
     "jsonPath": "Connection.meter[0].meterReadings[0].readingDate",
     "label": "wc.group.reading1Date",
     "pattern": "",
     "type": "datePicker",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",//Remove required messages
     "patternErrMsg": ""
    },
    {
     "name": "Reading2",
     "jsonPath": "Connection.meter[0].meterReadings[1].readingDate",
     "label": "wc.group.reading2Date",
     "pattern": "",
     "type": "datePicker",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",//Remove required messages
     "patternErrMsg": ""
    },
    {
     "name": "Reading3",
     "jsonPath": "Connection.meter[0].meterReadings[2].readingDate",
     "label": "wc.group.reading3Date",
     "pattern": "",
     "type": "datePicker",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",//Remove required messages
     "patternErrMsg": ""
   },
   {
     "name": "GapCode1",
     "jsonPath": "",
     "label": "wc.create.groups.gapCode1",
     "pattern": "",
     "type": "singleValueList",
     "url": "",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",
     "patternErrMsg": ""
   },
   {
     "name": "GapCode2",
     "jsonPath": "",
     "label": "wc.create.groups.gapCode2",
     "pattern": "",
     "type": "singleValueList",
     "url": "",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",
     "patternErrMsg": ""
   },
   {
     "name": "GapCode3",
     "jsonPath": "",
     "label": "wc.create.groups.gapCode3",
     "pattern": "",
     "type": "singleValueList",
     "url": "",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",
     "patternErrMsg": ""
   }
   ]
  }
 ]
}




var dat = {
  "wc.create": {
    "numCols": 12 / 3,
    "version": "v1",
    "url": "/wcms-connection/connection/_create",
    "idJsonPath": "Connection[0].consumerNumber",
    "ackUrl":"/legacy/view",
    "useTimestamp": true,
    "tenantIdRequired": true, //Instead of boolean value give json path
    "objectName": "Connection",
    "level": 0,
    "groups": [
	{
        "label": "wc.create.groups.applicationParticular.title", //Cut short labels by taking initial path from parent
        "name": "applicationParticular", //Follow Title case pattern
        "children": [],
        "multiple": false,
        "fields": [
            {
              "name": "applicationType",
              "jsonPath": "Connection.applicationType",
              "label": "",
              "pattern": "",
              "type": "radio",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
        			"values": [{"label":"wc.create.primaryConnection", "value":"NEWCONNECTION"},{"label":"wc.create.additionalConnection" ,"value":"ADDITIONCONNECTION"}],
        			"defaultValue":"NEWCONNECTION"

            },
          {
            "name": "With Property",
            "jsonPath": "Connection.withProperty",
            "label": "",
            "pattern": "",
            "type": "radio",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
			"values": [{"label":"wc.group.withProperty", "value":true},{"label":"wc.group.withoutProperty", "value":false}],
			"defaultValue":false,
      "showHideFields": [{
             "ifValue": false,
             "hide": [{
              "name": "applicantDetails",
              "isGroup": true,
              "isField": false
             }],
             "show": [{
              "name": "NoOfFlats",
              "isGroup": false,
              "isField": true
            },
            {
             "name": "applicantDetailsWithProp",
             "isGroup": true,
             "isField": false
           }]
            }]
          }
        ]
      },{
        "label": "wc.create.groups.applicantDetails.title", //Cut short labels by taking initial path from parent
        "name": "applicantDetailsWithProp", //Follow Title case pattern
        "multiple": false,
        "fields": [
          {
            "name": "NameOfApplicant",
            "jsonPath": "Connection.connectionOwner.name",
            "label": "wc.create.groups.applicantDetails.nameOfApplicant",
            "pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "MobileNumber",
            "jsonPath": "Connection.connectionOwner.mobileNumber",
            "label": "wc.create.groups.applicantDetails.mobileNumber",
            "pattern": "",
            "type": "mobileNumber",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Email",
            "jsonPath": "Connection.connectionOwner.emailId",
            "label": "wc.create.groups.applicantDetails.email",
            "pattern": "",
            "type": "email",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "defaultValue": ""
          },
          {
            "name": "AadharNumber",
            "jsonPath": "Connection.connectionOwner.aadhaarNumber",
            "label": "wc.create.groups.applicantDetails.adharNumber",
            "pattern": "",
            "type": "aadhar",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "gender",
            "jsonPath": "Connection.connectionOwner.gender",
            "label": "employee.Employee.fields.User.gender",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egov-common-masters/genders/_search?|$.Gender.*|$.Gender.*",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "AddressNo",
            "jsonPath": "Connection.houseNumber",
            "label": "wc.create.groups.applicantDetails.addressNumber",
            "pattern": "^[\s.]*([^\s.][\s.]*){0,250}$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Address",
            "jsonPath": "Connection.address.addressLine1",
            "label": "wc.create.groups.applicantDetails.address",
            "pattern": "^[\s.]*([^\s.][\s.]*){0,250}$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "City",
            "jsonPath": "Connection.address.city",
            "label": "employee.Employee.fields.city",
            "pattern": "^[\s.]*([^\s.][\s.]*){0,55}$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "PinCode",
            "jsonPath": "Connection.address.pinCode",
            "label": "pt.create.groups.propertyAddress.fields.pin",
            "pattern": "^\\d{6}$",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "zoneName",
            "jsonPath": "Connection.connectionLocation.revenueBoundary.id",
            "label": "wc.create.groups.fields.zone",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=ZONE&hierarchyTypeName=REVENUE|$.Boundary.*.boundaryNum|$.Boundary.*.name",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "wardName",
            "jsonPath": "Connection.connectionLocation.adminBoundary.id",
            "label": "wc.create.groups.fields.ward",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=WARD&hierarchyTypeName=ADMINISTRATION|$.Boundary.*.boundaryNum|$.Boundary.*.name",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Locality",
            "jsonPath": "Connection.connectionLocation.locationBoundary.id",
            "label": "wc.create.groups.applicantDetails.locality",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=LOCALITY&hierarchyTypeName=LOCATION|$.Boundary.*.boundaryNum|$.Boundary.*.name",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "executionDate",
            "jsonPath": "Connection.executionDate",
            "label": "wc.create.groups.applicantDetails.connectionDate",
            "maxDate": "today-2",
            "pattern": "",
            "type": "datePicker",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "ConsumerNo",
            "jsonPath": "Connection.legacyConsumerNumber",
            "label": "wc.create.groups.applicantDetails.consumerNo",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "ManualConsumerNo",
            "jsonPath": "Connection.manualConsumerNumber",
            "label": "wc.create.groups.applicantDetails.manualConsumerNo",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Primary Owner",
            "jsonPath": "Connection.connectionOwner.isPrimaryOwner",
            "label": "",
            "pattern": "",
            "type": "radio",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
      			"values": [{"label":"pt.create.groups.ownerDetails.fields.primaryOwner", "value":true},{"label":"pt.create.groups.ownerDetails.fields.secondaryOwner", "value":false}],
      			"defaultValue":true
          }
        ]
      },
      {
        "label": "wc.create.groups.applicantDetails.title", //Cut short labels by taking initial path from parent
        "name": "applicantDetails", //Follow Title case pattern
        "children": [],
        "multiple": false,
        "hide":true,
        "fields": [{
            "name": "AssessmentNumber",
            "jsonPath": "Connection.property.propertyIdentifier",
            "label": "wc.create.groups.applicantDetails.propertyIdentifier",
            "pattern": "",
            "type": "textSearch",
            "isRequired": true,
            "isDisabled": false,
            "autoCompleteDependancy": {
              "autoCompleteUrl": "/pt-property/properties/_search?upicNumber={value}&tenantId=default",
              "autoFillFields": {
                "Connection.property.mobileNumber": "properties[0].owners[0].mobileNumber",
                "Connection.property.nameOfApplicant": "properties[0].owners[0].name",
                "Connection.property.email": "properties[0].owners[0].emailId",
                "Connection.property.aadhaarNumber": "properties[0].owners[0].aadhaarNumber",
                "Connection.property.noOfFloors": "properties[0].propertyDetail.noOfFloors",
                "Connection.property.locality":"properties[0].boundary.locationBoundary.id",
                "Connection.property.zone":"properties[0].boundary.revenueBoundary.id",
                "Connection.property.ward":"properties[0].boundary.adminBoundary.id",
                "Connection.property.address":"properties[0].address.addressLine1",
                "Connection.property.property":"properties[0].propertyDetail.propertyType"
              }
            },
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "NameOfApplicant",
            "jsonPath": "Connection.property.nameOfApplicant",
            "label": "wc.create.groups.applicantDetails.nameOfApplicant",
            "pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "MobileNumber",
            "jsonPath": "Connection.property.mobileNumber",
            "label": "wc.create.groups.applicantDetails.mobileNumber",
            "pattern": "",
            "type": "mobileNumber",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Email",
            "jsonPath": "Connection.property.email",
            "label": "wc.create.groups.applicantDetails.email",
            "pattern": "",
            "type": "email",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "isHidden": false,
            "defaultValue": ""
          },
          {
            "name": "AadharNumber",
            "jsonPath": "Connection.property.adharNumber",
            "label": "wc.create.groups.applicantDetails.adharNumber",
            "pattern": "",
            "type": "aadhar",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Locality",
            "jsonPath": "Connection.property.locality",
            "label": "wc.create.groups.applicantDetails.locality",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=LOCALITY&hierarchyTypeName=LOCATION|$.Boundary.*.boundaryNum|$.Boundary.*.name",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": "",
          },
          {
            "name": "Address",
            "jsonPath": "Connection.property.address",
            "label": "wc.create.groups.applicantDetails.address",
            "pattern": "^[\s.]*([^\s.][\s.]*){0,250}$",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Zone",
            "jsonPath": "Connection.property.zone",
            "label": "wc.create.groups.applicantDetails.zone",
            "pattern": "",
            "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=ZONE&hierarchyTypeName=REVENUE|$.Boundary.*.boundaryNum|$.Boundary.*.name",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "noOfFloors",
            "jsonPath": "Connection.property.noOfFloors",
            "label": "wc.create.groups.applicantDetails.noOfFloors",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "propertyTaxDue",
            "jsonPath": "Connection.property.propertyTaxDue",
            "label": "wc.create.groups.applicantDetails.propertyTaxDue",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
      	  {
            "name": "consumerNo",
            "jsonPath": "Connection.legacyConsumerNumber",
            "label": "wc.create.groups.applicantDetails.consumerNo",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "ManualConsumerNo",
            "jsonPath": "Connection.manualConsumerNumber",
            "label": "wc.create.groups.applicantDetails.manualConsumerNo",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
      	  {
            "name": "executionDate",
            "jsonPath": "Connection.executionDate",
            "label": "wc.create.groups.applicantDetails.connectionDate",
            "maxDate": "today-2",
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
        "label": "wc.create.groups.connectionDetails.title",
        "name": "connectionDetails",
        "multiple": false,
        "fields": [{
            "name": "PropertyType",
            "jsonPath": "Connection.property.propertyType",
            "label": "wc.create.groups.connectionDetails.propertyType",
            "isHidden":true,
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            // "url": "/pt-property/property/propertytypes/_search?|$..name|$..name",
            "defaultValue": "Others",
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "CategoryType",
            "jsonPath": "Connection.categoryType",
            "label": "wc.create.groups.connectionDetails.categoryType",
            "isHidden":true,
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
      			"defaultValue": "General",
      			// "url":"/wcms/masters/propertytype-categorytype/_search?tenantId=default&propertyTypeName=Vacant Land|$..categoryTypeName|$..categoryTypeName"
          },
          {
            "name": "UsageType",
            "jsonPath": "Connection.property.usageType",
            "label": "wc.create.groups.connectionDetails.usageType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
      			"defaultValue": [],
      			"url":"/wcms/masters/propertytype-usagetype/_search?tenantId=default&propertyTypeName=Others|$..usageCode|$..usageType",
            "depedants": [{
                "jsonPath": "Connection.subUsageType",
                "type": "dropDown",
                "pattern": "/pt-property/property/usages/_search?tenantId=default&parent={Connection.property.usageType}|$..code|$..name"
              }]
          },
          {
            "name": "subUsageType",
            "jsonPath": "Connection.subUsageType",
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
            "name": "hscPipeSizeType",
            "jsonPath": "Connection.hscPipeSizeType",
            "label": "wc.create.groups.connectionDetails.hscPipeSizeType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "defaultValue": [],
            "url":"/wcms/masters/propertytype-pipesize/_search?tenantId=default&propertyTypeName=Others|$..pipeSize|$..pipeSizeInInch"
          },
          {
            "name": "billingType",
            "jsonPath": "Connection.billingType",
            "label": "wc.create.groups.connectionDetails.billingType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "url": "/wcms-connection/connection/_getbillingtypes?|$..key|$..object",
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "showHideFields": [{
                   "ifValue": "METERED",
                   "hide": [{
                    "name": "description",
                    "isGroup": false,
                    "isField": true
                   }],
                   "show": [{
                    "name": "Metered",
                    "isGroup": true,
                    "isField": false
                   }]
                  }]
          },
          {
            "name": "ConnectionType",
            "jsonPath": "Connection.connectionType",
            "label": "wc.create.groups.connectionDetails.connectionType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "url": "/wcms-connection/connection/_getconnectiontypes?|$..key|$..object",
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "showHideFields": [{
                   "ifValue": "TEMPORARY",
                   "show": [{
                                "name": "fromDate",
                                "isGroup": false,
                                "isField": true
                              },
                              {
                               "name": "toDate",
                               "isGroup": false,
                               "isField": true
                             }],
                  "hide":[]
              }]
          },
          {
            "name": "SourceType",
            "jsonPath": "Connection.sourceType",
            "label": "wc.create.groups.connectionDetails.sourceType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "url": "/wcms/masters/sourcetype/_search?|$..name|$..name",
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "supplyType",
            "jsonPath": "Connection.supplyType",
            "label": "wc.create.groups.connectionDetails.supplyType",
            "isHidden": true,
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            // "url": "/wcms/masters/supplytype/_search?|$..name|$..name",
            "requiredErrMsg": "",
            "defaultValue":"Semi Bulk Type",
            "patternErrMsg": ""
          },
          {
            "name": "waterTreatment",
            "jsonPath": "Connection.waterTreatment",
            "label": "wc.create.groups.connectionDetails.waterTreatment",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "url": "/wcms/masters/treatmentplant/_search?|$..name|$..name",
            "requiredErrMsg": "",
			"isKeyValuePair":true,
            "patternErrMsg": ""
          },
          {
            "name": "sumpCapacity",
            "jsonPath": "Connection.sumpCapacity",
            "label": "wc.create.groups.connectionDetails.fields.sumpCapacity",
            "pattern": "^(0|[1-9][0-9]*)$",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Sequence No",
            "jsonPath": "Connection.billSequenceNumber",
            "label": "wc.create.groups.connectionDetails.fields.billingNumber",
            "pattern": "^\\d+(\\.\\d{1,3})?$",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "PlumberName",
            "jsonPath": "Connection.plumberName",
            "label": "wc.create.groups.connectionDetails.fields.plumberName",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "NoOfTaps",
            "jsonPath": "Connection.numberOfTaps",
            "label": "wc.create.groups.connectionDetails.fields.noOfTaps",
            "pattern": "^(0|[1-9][0-9]*)$",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "numberOfPersons",
            "jsonPath": "Connection.numberOfPersons",
            "label": "wc.create.groups.connectionDetails.fields.numberOfPersons",
            "type": "number",
            "pattern":"^(0|[1-9][0-9]*)$",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "depedants":[{
                "jsonPath":"Connection.numberOfFamily",
                "type":"textField",
                "pattern":"getVal('Connection.numberOfPersons')!=''? (Math.ceil(getVal('Connection.numberOfPersons')/4)):0",
                "rg":"",
                "isRequired": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              }]
          },
          {
            "name": "numberOfFamily",
            "jsonPath": "Connection.numberOfFamily",
            "label": "wc.create.numberOfFamily",
            "pattern": "^(0|[1-9][0-9]*)$",
            "type": "number",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Outside ULB",
            "jsonPath": "Connection.outsideULB",
            "label": "wc.create.groups.connectionDetails.fields.outSide",
            "pattern": "",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
		       {
            "name": "fromDate",
            "jsonPath": "Connection.fromDate",
            "label": "wc.create.fromDate",
            "pattern": "",
            "type": "datePicker",
            "hide":true,
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "toDate",
            "jsonPath": "Connection.toDate",
            "label": "wc.create.toDate",
            "pattern": "",
            "hide":true,
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          }
        ]
      },
	  {
        "label": "wc.create.meter.title",
        "name": "Metered",
        "hide":true,
        		"children":[meterReading],
                "fields": [{
                    "name": "meterOwner",
                    "jsonPath": "Connection.meter[0].meterOwner",
                    "label": "wc.group.meterOwner",
                    "pattern": "^[\s.]*([^\s.][\s.]*){0,50}$",
                    "type": "text",
                    "isRequired": false,
                    "isDisabled": false,
                    "requiredErrMsg": "",
                    "patternErrMsg": ""
                  },
                  {
                      "name": "meterModel",
                      "jsonPath": "Connection.meter[0].meterModel",
                      "label": "wc.group.meterModal",
                      "pattern": "^[\s.]*([^\s.][\s.]*){0,50}$",
                      "type": "text",
                      "isRequired": false,
                      "isDisabled": false,
                      "requiredErrMsg": "",
                      "patternErrMsg": ""
                    },
                    {
                    "name": "meterMake",
                    "jsonPath": "Connection.meter[0].meterMake",
                    "label": "wc.group.meterMake",
                    "pattern": "",
                    "type": "text",
                    "isRequired": false,
                    "isDisabled": false,
                    "requiredErrMsg": "",
                    "patternErrMsg": ""
                  },
                  {
                      "name": "meterSlNo",
                      "jsonPath": "Connection.meter[0].meterSlNo",
                      "label": "wc.group.meterNumber",
                      "pattern": "",
                      "type": "text",
                      "isRequired": false,
                      "isDisabled": false,
                      "requiredErrMsg": "",
                      "patternErrMsg": ""
                    },
        		  {
                    "name": "meterCost",
                    "jsonPath": "Connection.meter[0].meterCost",
                    "label": "wc.group.meterCost",
                    "pattern": "",
                    "type": "text",
                    "isRequired": false,
                    "isDisabled": false,
                    "requiredErrMsg": "",
                    "patternErrMsg": ""
                  },
        		  {
                    "name": "initialMeterReading",
                    "jsonPath": "Connection.meter[0].initialMeterReading",
                    "label": "wc.group.initialMeterReading",
                    "pattern": "",
                    "type": "text",
                    "isRequired": false,
                    "isDisabled": false,
                    "requiredErrMsg": "",
                    "patternErrMsg": ""
                  }]
              },
	  {
        "label": "wc.create.donation.subtitle",
        "name": "Donation",
        "fields": [{
            "name": "SpecialDonationCharges",
            "jsonPath": "Connection.donationCharge",
            "label": "wc.create.SpecialDonationCharges",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
                "name": "receiptNumber",
                "jsonPath": "Connection.receiptNumber",
                "label": "wc.create.receiptNumber",
                "pattern": "",
                "type": "text",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
            },
            {
             "name": "receiptDate",
             "jsonPath": "Connection.receiptDate",
             "maxDate": "today",
             "label": "wc.create.receiptDate",
             "pattern": "",
             "type": "datePicker",
             "isRequired": false,
             "isDisabled": false,
             "requiredErrMsg": "",//Remove required messages
             "patternErrMsg": ""
            },
        ]
      },
       {
          "label": "wc.create.groups.fileDetails.title",
          "name": "Documents",
          "fields": [{
            "name": "File",
            "jsonPath": "Connection.documents",
            "type": "documentList",
            "pathToArray": "DocumentTypeApplicationTypes",
            "displayNameJsonPath": "documentType",
            "url": "/wcms/masters/documenttype-applicationtype/_search?applicationType=NEWCONNECTION",
            "autoFillFields": [{
              "name": "document",
              "jsonPath": "documentTypeId"
            }]
          }]
        }
    ]
  },
  "wc.view": {
    "numCols": 12 / 3,
    "version": "v1",
    "url": "/wcms-connection/connection/_search?consumerNumber={consumerNumber}",
    "useTimestamp": true,
    "tenantIdRequired": true, //Instead of boolean value give json path
    "objectName": "Connection",
    "level": 0,
    "groups": [{
          "label": "wc.create.groups.applicationParticular.title", //Cut short labels by taking initial path from parent
          "name": "applicationParticular", //Follow Title case pattern
          "children": [],
          "multiple": false,
          "fields": [
            {
              "name": "With Property",
              "jsonPath": "Connection[0].withProperty",
              "label": "wc.group.withProperty",
              "pattern": "",
              "type": "radio",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
        			"values": [{"label":"wc.group.withProperty", "value":true},{"label":"wc.group.withoutProperty", "value":false}],
        			"defaultValue":true,
              "showHideFields": [{
               "ifValue": false,
               "hide": [{
                "name": "applicantDetails",
                "isGroup": true,
                "isField": false
               }],
               "show": [{
               "name": "applicantDetailsWithProp",
               "isGroup": true,
               "isField": false
             }]
              }]
            }
          ]
        },
        {
          "label": "wc.create.groups.applicantDetails.title", //Cut short labels by taking initial path from parent
          "name": "applicantDetailsWithProp", //Follow Title case pattern
          "hide":true,
          "multiple": false,
          "fields": [
            {
              "name": "NameOfApplicant",
              "jsonPath": "Connection[0].connectionOwner.name",
              "label": "wc.create.groups.applicantDetails.nameOfApplicant",
              "pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
              "type": "text",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
                "name": "acknowledgementNumber",
                "jsonPath": "Connection[0].acknowledgementNumber",
                "label": "wc.create.groups.applicantDetails.consumerNumber",
                "pattern": "",
                "type": "text",
                "isRequired": false,
                "isDisabled": true,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              },
            {
              "name": "MobileNumber",
              "jsonPath": "Connection[0].connectionOwner.mobileNumber",
              "label": "wc.create.groups.applicantDetails.mobileNumber",
              "pattern": "",
              "type": "mobileNumber",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "Email",
              "jsonPath": "Connection[0].connectionOwner.emailId",
              "label": "wc.create.groups.applicantDetails.email",
              "pattern": "",
              "type": "email",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
              "isHidden": false,
              "defaultValue": ""
            },
            {
              "name": "AadharNumber",
              "jsonPath": "Connection[0].connectionOwner.aadhaarNumber",
              "label": "wc.create.groups.applicantDetails.adharNumber",
              "pattern": "",
              "type": "aadhar",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "gender",
              "jsonPath": "Connection[0].connectionOwner.gender",
              "label": "employee.Employee.fields.User.gender",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egov-common-masters/genders/_search?|$.Gender.*|$.Gender.*",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "AddressNo",
              "jsonPath": "Connection[0].houseNumber",
              "label": "wc.create.groups.applicantDetails.addressNumber",
              "pattern": "^[\s.]*([^\s.][\s.]*){0,16}$",
              "type": "text",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "Address",
              "jsonPath": "Connection[0].address.addressLine1",
              "label": "wc.create.groups.applicantDetails.address",
              "pattern": "",
              "type": "text",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "City",
              "jsonPath": "Connection[0].address.city",
              "label": "employee.Employee.fields.city",
              "pattern": "",
              "type": "text",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "PinCode",
              "jsonPath": "Connection[0].address.pinCode",
              "label": "pt.create.groups.propertyAddress.fields.pin",
              "pattern": "^\\d{6}$",
              "type": "number",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "Locality",
              "jsonPath": "Connection[0].connectionLocation.locationBoundary.id",
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
              "name": "wardName",
              "jsonPath": "Connection[0].connectionLocation.adminBoundary.id",
              "label": "wc.create.groups.fields.ward",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=WARD&hierarchyTypeName=ADMINISTRATION|$.Boundary.*.boundaryNum|$.Boundary.*.name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "zoneName",
              "jsonPath": "Connection[0].connectionLocation.revenueBoundary.id",
              "label": "wc.create.groups.fields.zone",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=ZONE&hierarchyTypeName=REVENUE|$.Boundary.*.boundaryNum|$.Boundary.*.name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
            {
              "name": "Primary Owner",
              "jsonPath": "Connection[0].connectionOwner.isPrimaryOwner",
              "label": "pt.create.groups.ownerDetails.fields.primaryOwner",
              "pattern": "",
              "type": "radio",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
        			"values": [{"label":"pt.create.groups.ownerDetails.fields.primaryOwner", "value":true},{"label":"pt.create.groups.ownerDetails.fields.secondaryOwner", "value":false}],
        			"defaultValue":true
            }
          ]
        },
        {
        "label": "wc.create.groups.applicantDetails.title", //Cut short labels by taking initial path from parent
        "name": "applicantDetails", //Follow Title case pattern
        "children": [],
        "multiple": false,
        "hide":false,
        "fields": [{
            "name": "acknowledgementNumber",
            "jsonPath": "Connection[0].acknowledgementNumber",
            "label": "wc.create.groups.applicantDetails.acknowledgementNumber",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "AssessmentNumber",
            "jsonPath": "Connection[0].property.propertyidentifier",
            "label": "wc.create.groups.applicantDetails.propertyIdentifier",
            "pattern": "",
            "type": "textSearch",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          // {
          // 	"name": "AssessmentNumber",
          // 	"jsonPath": "Connection.property.propertyIdentifier",
          // 	"label": "wc.create.groups.applicantDetails.propertyIdentifier",
          // 	"pattern": "",
          // 	"type": "number",
          // 	"isRequired": true,
          // 	"isDisabled": false,
          // 	"autoCompleteUrl": "",
          // 	"autoFillFields": {
          // 		"NameOfApplicant": "",
          // 		"MobileNumber": "",
          // 		"Email": "",
          // 		"AadharNumber": ""
          // 	},
          // 	"requiredErrMsg": "",//Remove required messages
          // 	"patternErrMsg": ""
          // },
          {
            "name": "NameOfApplicant",
            "jsonPath": "Connection[0].property.nameOfApplicant",
            "label": "wc.create.groups.applicantDetails.nameOfApplicant",
            "pattern": "^[\s.]*([^\s.][\s.]*){0,100}$",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "MobileNumber",
            "jsonPath": "Connection[0].property.mobileNumber",
            "label": "wc.create.groups.applicantDetails.mobileNumber",
            "pattern": "",
            "type": "mobileNumber",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Email",
            "jsonPath": "Connection[0].property.email",
            "label": "wc.create.groups.applicantDetails.email",
            "pattern": "",
            "type": "email",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "isHidden": false,
            "defaultValue": ""
          },
          {
            "name": "AadharNumber",
            "jsonPath": "Connection[0].property.adharNumber",
            "label": "wc.create.groups.applicantDetails.adharNumber",
            "pattern": "",
            "type": "aadhar",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Locality",
            "jsonPath": "Connection[0].property.locality",
            "label": "wc.create.groups.applicantDetails.locality",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=LOCALITY&hierarchyTypeName=LOCATION|$.Boundary.*.boundaryNum|$.Boundary.*.name",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Address",
            "jsonPath": "Connection[0].property.address",
            "label": "wc.create.groups.applicantDetails.address",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Zone",
            "jsonPath": "Connection[0].property.zone",
            "label": "wc.create.groups.applicantDetails.zone",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": true,
            "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=ZONE&hierarchyTypeName=REVENUE|$.Boundary.*.boundaryNum|$.Boundary.*.name",
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "propertyTaxDue",
            "jsonPath": "Connection[0].property.propertyTaxDue",
            "label": "wc.create.groups.applicantDetails.propertyTaxDue",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          }
        ]
      },
      {
        "label": "wc.create.groups.connectionDetails.title",
        "name": "connectionDetails",
        "multiple": false,
        "fields": [
          // {
          //   "name": "PropertyType",
          //   "jsonPath": "Connection[0].property.propertyType",
          //   "label": "wc.create.groups.connectionDetails.propertyType",
          //   "pattern": "",
          //   "type": "singleValueList",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "url": "/pt-property/property/propertytypes/_search?|$..name|$..name",
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          // {
          //   "name": "CategoryType",
          //   "jsonPath": "Connection[0].categoryType",
          //   "label": "wc.create.groups.connectionDetails.categoryType",
          //   "pattern": "",
          //   "type": "singleValueList",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },
          {
            "name": "UsageType",
            "jsonPath": "Connection[0].property.usageType",
            "label": "wc.create.groups.connectionDetails.usageType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "url": "/pt-property/property/usages/_search?|$..name|$..name",
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "subUsageType",
            "jsonPath": "Connection[0].subUsageTypeId",
            "label": "wc.create.groups.connectionDetails.subUsageType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
      			"url":"/pt-property/property/usages/_search?|$..name|$..name",
          },
          {
            "name": "hscPipeSizeType",
            "jsonPath": "Connection[0].hscPipeSizeType",
            "label": "wc.create.groups.connectionDetails.hscPipeSizeType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "applicationType",
            "jsonPath": "Connection[0].applicationType",
            "label": "wc.create.groups.connectionDetails.applicationType",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "isHidden": true,
            "defaultValue": "NEWCONNECTION",
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "connectionStatus",
            "jsonPath": "Connection[0].connectionStatus",
            "label": "wc.create.groups.connectionDetails.applicationType",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "isHidden": true,
            "defaultValue": "INPROGRESS",
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "billingType",
            "jsonPath": "Connection[0].billingType",
            "label": "wc.create.groups.connectionDetails.billingType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "url": "/wcms-connection/connection/_getbillingtypes?|$..key|$..object",
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "showHideFields": [{
                   "ifValue": "METERED",
                   "hide": [{
                    "name": "description",
                    "isGroup": false,
                    "isField": true
                   }],
                   "show": [{
                    "name": "Metered",
                    "isGroup": true,
                    "isField": false
                   }]
                  }]
          },
          {
            "name": "ConnectionType",
            "jsonPath": "Connection[0].connectionType",
            "label": "wc.create.groups.connectionDetails.connectionType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "url": "/wcms-connection/connection/_getconnectiontypes?|$..key|$..object",
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "showHideFields": [{
                   "ifValue": "TEMPORARY",
                   "show": [{
                                "name": "fromDate",
                                "isGroup": false,
                                "isField": true
                              },
                              {
                               "name": "toDate",
                               "isGroup": false,
                               "isField": true
                             }],
                  "hide":[]
              }]
          },
          {
            "name": "SourceType",
            "jsonPath": "Connection[0].sourceType",
            "label": "wc.create.groups.connectionDetails.sourceType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "url": "/wcms/masters/sourcetype/_search?|$..name|$..name",
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          // {
          //   "name": "supplyType",
          //   "jsonPath": "Connection[0].supplyType",
          //   "label": "wc.create.groups.connectionDetails.supplyType",
          //   "pattern": "",
          //   "type": "singleValueList",
          //   "isRequired": false,
          //   "isDisabled": false,
          //   "url": "/wcms/masters/supplytype/_search?|$..name|$..name",
          //   "requiredErrMsg": "",
          //   "patternErrMsg": ""
          // },

          {
            "name": "waterTreatment",
            "jsonPath": "Connection[0].waterTreatment",
            "label": "wc.create.groups.connectionDetails.waterTreatment",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "url": "/wcms/masters/treatmentplant/_search?|$..name|$..name",
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "sumpCapacity",
            "jsonPath": "Connection[0].sumpCapacity",
            "label": "wc.create.groups.connectionDetails.fields.sumpCapacity",
            "pattern": "^(0|[1-9][0-9]*)$",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "numberOfPersons",
            "jsonPath": "Connection[0].numberOfPersons",
            "label": "wc.create.groups.connectionDetails.fields.numberOfPersons",
            "type": "number",
            "pattern":"^(0|[1-9][0-9]*)$",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "depedants":[{
                "jsonPath":"Connection[0].numberOfFamily",
                "type":"number",
                "pattern":"getVal('Connection.numberOfPersons')!=''? (Math.ceil(getVal('Connection.numberOfPersons')/4)):0",
                "rg":"",
                "isRequired": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              }]
          },
          {
            "name": "numberOfFamily",
            "jsonPath": "Connection[0].numberOfFamily",
            "label": "wc.create.numberOfFamily",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Sequence No",
            "jsonPath": "Connection[0].billSequenceNumber",
            "label": "wc.create.groups.connectionDetails.fields.billingNumber",
            "pattern": "^([0-9]+(\\.[0-9][0-9]*)$",
            "type": "number",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "PlumberName",
            "jsonPath": "Connection[0].plumberName",
            "label": "wc.create.groups.connectionDetails.fields.plumberName",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "NoOfTaps",
            "jsonPath": "Connection[0].numberOfTaps",
            "label": "wc.create.groups.connectionDetails.fields.noOfTaps",
            "pattern": "^(0|[1-9][0-9]*)$",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Outside ULB",
            "jsonPath": "Connection[0].outsideULB",
            "label": "wc.create.groups.connectionDetails.fields.outSide",
            "pattern": "",
            "type": "checkbox",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
           "name": "fromDate",
           "jsonPath": "Connection[0].fromDate",
           "label": "wc.create.fromDate",
           "pattern": "",
           "type": "datePicker",
           "isHidden":true,
           "isRequired": false,
           "isDisabled": false,
           "requiredErrMsg": "",
           "patternErrMsg": ""
         },
         {
           "name": "toDate",
           "jsonPath": "Connection[0].toDate",
           "label": "wc.create.toDate",
           "pattern": "",
           "isHidden":true,
           "type": "datePicker",
           "isRequired": false,
           "isDisabled": false,
           "requiredErrMsg": "",
           "patternErrMsg": ""
         }

        ]
      },
      {
          "label": "wc.create.meter.title",
          "name": "Metered",
          "hide":true,
              "children":[meterReading],
                  "fields": [{
                      "name": "meterOwner",
                      "jsonPath": "Connection[0].meter[0].meterOwner",
                      "label": "wc.group.meterOwner",
                      "pattern": "^[\s.]*([^\s.][\s.]*){0,50}$",
                      "type": "text",
                      "isRequired": false,
                      "isDisabled": false,
                      "requiredErrMsg": "",
                      "patternErrMsg": ""
                    },
                    {
                        "name": "meterModel",
                        "jsonPath": "Connection[0].meter[0].meterModel",
                        "label": "wc.group.meterModal",
                        "pattern": "^[\s.]*([^\s.][\s.]*){0,50}$",
                        "type": "text",
                        "isRequired": false,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                      },
                      {
                      "name": "meterMake",
                      "jsonPath": "Connection[0].meter[0].meterMake",
                      "label": "wc.group.meterMake",
                      "pattern": "",
                      "type": "text",
                      "isRequired": false,
                      "isDisabled": false,
                      "requiredErrMsg": "",
                      "patternErrMsg": ""
                    },
                    {
                        "name": "meterSlNo",
                        "jsonPath": "Connection[0].meter[0].meterSlNo",
                        "label": "wc.group.meterNumber",
                        "pattern": "",
                        "type": "text",
                        "isRequired": false,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                      },
                {
                      "name": "meterCost",
                      "jsonPath": "Connection[0].meter[0].meterCost",
                      "label": "wc.group.meterCost",
                      "pattern": "",
                      "type": "text",
                      "isRequired": false,
                      "isDisabled": false,
                      "requiredErrMsg": "",
                      "patternErrMsg": ""
                    },
                {
                      "name": "initialMeterReading",
                      "jsonPath": "Connection[0].meter[0].initialMeterReading",
                      "label": "wc.group.initialMeterReading",
                      "pattern": "",
                      "type": "text",
                      "isRequired": false,
                      "isDisabled": false,
                      "requiredErrMsg": "",
                      "patternErrMsg": ""
                    }]
                },
      // {
      //   "label": "wc.create.groups.fileDetails.title",
      //   "name": "Documents",
      //   "multiple": false,
      //   "fields": [
      //     // {
      //     // 	"name": " ",
      //     // 	"jsonPath": "Connection.documents[0].fileStoreId",
      //     // 	"label": "wc.create.groups.fileDetails.fields.PTaxReciept",
      //     // 	"pattern": "",
      //     // 	"type": "singleFileUpload",
      //     // 	"isRequired": true,
      //     // 	"isDisabled": false,
      //     // 	"requiredErrMsg": "",
      //     // 	"patternErrMsg": "",
      //     // 	"defaultValue": "4567"
      //     // },
      //     {
      //       "name": " ",
      //       "jsonPath": "Connection.documents[0].fileStoreId",
      //       "label": "wc.create.groups.fileDetails.fields.PTaxReciept",
      //       "pattern": "",
      //       "type": "text",
      //       "isRequired": true,
      //       "isDisabled": false,
      //       "requiredErrMsg": "",
      //       "patternErrMsg": "",
      //       "defaultValue": "4567",
      //       "isHidden": true
      //     },
      //     {
      //       "name": " ",
      //       "jsonPath": "Connection.documents[0].document",
      //       "label": "wc.create.groups.fileDetails.fields.PTaxReciept",
      //       "pattern": "",
      //       "isHidden": true,
      //       "type": "singleFileUpload",
      //       "isRequired": true,
      //       "isDisabled": false,
      //       "requiredErrMsg": "",
      //       "patternErrMsg": "",
      //       "defaultValue": "1"
      //     },
      //     {
      //       "name": " ",
      //       "jsonPath": "Connection.documents[0].name",
      //       "label": "wc.create.groups.fileDetails.fields.PTaxReciept",
      //       "pattern": "",
      //       "type": "singleFileUpload",
      //       "isRequired": true,
      //       "isHidden": true,
      //       "isDisabled": false,
      //       "requiredErrMsg": "",
      //       "patternErrMsg": "",
      //       "defaultValue": "Test"
      //     },
      //     {
      //       "name": " ",
      //       "jsonPath": "Connection.documents[1]",
      //       "label": "wc.create.groups.fileDetails.fields.DistributionLineLocationMap",
      //       "pattern": "",
      //       "type": "singleFileUpload",
      //       "isRequired": true,
      //       "isDisabled": false,
      //       "requiredErrMsg": "",
      //       "patternErrMsg": ""
      //     },
      //     {
      //       "name": " ",
      //       "jsonPath": "Connection.documents[2]",
      //       "label": "wc.create.groups.fileDetails.fields.WhiteRationCard",
      //       "pattern": "",
      //       "type": "singleFileUpload",
      //       "isRequired": true,
      //       "isDisabled": false,
      //       "requiredErrMsg": "",
      //       "patternErrMsg": ""
      //     },
      //     {
      //       "name": " ",
      //       "jsonPath": "Connection.documents[3]",
      //       "label": "wc.create.groups.fileDetails.fields.CourtFeeStamp",
      //       "pattern": "",
      //       "type": "singleFileUpload",
      //       "isRequired": true,
      //       "isDisabled": false,
      //       "requiredErrMsg": "",
      //       "patternErrMsg": ""
      //     }
      //   ]
      // }
    ]
  }
}

export default dat;
