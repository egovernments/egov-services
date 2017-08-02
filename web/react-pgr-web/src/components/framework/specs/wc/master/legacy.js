var meterReading = {
 "name": "MeterReading",
 "version": "v1",
 "level": 2,
 "jsonPath": "connection.floors[0].rooms",
 "groups": [
  {
   "label": "Meter Reading",
   "name": "MeterReading",
   "multiple": false,
   "children": [],
   "fields": [
    {
     "name": "Reading1",
     "jsonPath": "connection.floors[0].rooms[0].roomNo",
     "label": "Reading 1",
     "pattern": "",
     "type": "number",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",//Remove required messages
     "patternErrMsg": ""
    },
	{
     "name": "Reading2",
     "jsonPath": "connection.floors[0].rooms[0].roomNo",
     "label": "Reading 2",
     "pattern": "",
     "type": "number",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",//Remove required messages
     "patternErrMsg": ""
    },
	{
     "name": "Reading3",
     "jsonPath": "connection.floors[0].rooms[0].roomNo",
     "label": "Reading 3",
     "pattern": "",
     "type": "number",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",//Remove required messages
     "patternErrMsg": ""
    },
    
   ]
  }
 ]
}



var dat = {
  "wc.create": {
    "numCols": 12 / 3,
    "version": "v1",
    "url": "/wcms-connection/connection/_create",
    "idJsonPath": "Connection[0].acknowledgementNumber",
    "useTimestamp": true,
    "tenantIdRequired": true, //Instead of boolean value give json path
    "objectName": "Connection",
    "level": 0,
    "groups": [
	{
        "label": "wc.create.groups.applicantDetails.title", //Cut short labels by taking initial path from parent
        "name": "applicantDetails", //Follow Title case pattern
        "children": [],
        "multiple": false,
        "fields": [{
            "name": "PrimaryConnection",
            "jsonPath": "connection.property.propertyIdentifier",
            "label": "",
            "pattern": "",
            "type": "radio",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
			"values": [{"label":"primary connection", "value":true}]
          },
          {
            "name": "With Property",
            "jsonPath": "connection.property.propertyIdentifier",
            "label": "",
            "pattern": "",
            "type": "radio",
            "isRequired": true,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
			"values": [{"label":"With Property", "value":true},{"label":"Without Property", "value":false}],
			"defaultValue":true
          }			  
        ]
      },{
        "label": "wc.create.groups.applicantDetails.title", //Cut short labels by taking initial path from parent
        "name": "applicantDetails", //Follow Title case pattern
        "children": [],
        "multiple": false,
        "fields": [{
            "name": "AssessmentNumber",
            "jsonPath": "connection.property.propertyIdentifier",
            "label": "wc.create.groups.applicantDetails.propertyIdentifier",
            "pattern": "",
            "type": "textSearch",
            "isRequired": true,
            "isDisabled": false,
            "autoCompleteDependancy": {
              "autoCompleteUrl": "/pt-property/properties/_search?upicNo={value}&tenantId=default",
              "autoFillFields": {
                "Connection.asset.mobileNumber": "properties[0].owners[0].mobileNumber",
                "Connection.asset.nameOfApplicant": "properties[0].owners[0].name",
                "Connection.asset.email": "properties[0].owners[0].emailId",
                "Connection.asset.aadhaarNumber": "properties[0].owners[0].aadhaarNumber",
                "Connection.asset.": "properties[0].propertyDetail.noOfFloors"

              }
            },
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "NameOfApplicant",
            "jsonPath": "Connection.asset.nameOfApplicant",
            "label": "wc.create.groups.applicantDetails.nameOfApplicant",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "MobileNumber",
            "jsonPath": "Connection.asset.mobileNumber",
            "label": "wc.create.groups.applicantDetails.mobileNumber",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Email",
            "jsonPath": "Connection.asset.email",
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
            "jsonPath": "Connection.asset.adharNumber",
            "label": "wc.create.groups.applicantDetails.adharNumber",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Locality",
            "jsonPath": "Connection.asset.locality",
            "label": "wc.create.groups.applicantDetails.locality",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Address",
            "jsonPath": "Connection.asset.address",
            "label": "wc.create.groups.applicantDetails.address",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Zone",
            "jsonPath": "Connection.asset.zone",
            "label": "wc.create.groups.applicantDetails.zone",
            "pattern": "",
            "type": "textarea",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "noOfFloors",
            "jsonPath": "Connection.asset.noOfFloors",
            "label": "No of floors",
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
            "jsonPath": "Connection.property.propertyTaxDue",
            "label": "wc.create.groups.applicantDetails.consumerNo",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
		  {
            "name": "connectionDate",
            "jsonPath": "Connection.property.propertyTaxDue",
            "label": "wc.create.groups.applicantDetails.connectionDate",
            "pattern": "",
            "type": "datePicker",
            "isRequired": false,
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
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "url": "/pt-property/property/propertytypes/_search?|$..name|$..name",
            "depedants": [{
                "jsonPath": "Connection.categoryType",
                "type": "dropDown",
                "pattern": "/wcms/masters/propertytype-categorytype/_search?tenantId=default&propertyTypeName={Connection.property.propertyType}|$..categoryTypeName|$..categoryTypeName"
              },
              {
                "jsonPath": "Connection.property.usageType",
                "type": "dropDown",
                "pattern": "/wcms/masters/propertytype-usagetype/_search?tenantId=default&propertyTypeName={Connection.property.propertyType}|$..usageType|$..usageType"
              },
              {
                "jsonPath": "Connection.hscPipeSizeType",
                "type": "dropDown",
                "pattern": "/wcms/masters/propertytype-pipesize/_search?tenantId=default&propertyTypeName={Connection.property.propertyType}|$..pipeSize|$..pipeSize"
              }
            ],
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
		  {
            "name": "NoOfFlats",
            "jsonPath": "Connection.NoOfFlats",
            "label": "No of Flats",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "CategoryType",
            "jsonPath": "Connection.categoryType",
            "label": "wc.create.groups.connectionDetails.categoryType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "UsageType",
            "jsonPath": "Connection.property.usageType",
            "label": "wc.create.groups.connectionDetails.usageType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "hscPipeSizeType",
            "jsonPath": "Connection.hscPipeSizeType",
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
            "jsonPath": "Connection.applicationType",
            "label": "wc.create.groups.connectionDetails.applicationType",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "isHidden": true,
            "defaultValue": "NEWCONNECTION",
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "connectionStatus",
            "jsonPath": "Connection.connectionStatus",
            "label": "wc.create.groups.connectionDetails.applicationType",
            "pattern": "",
            "type": "text",
            "isRequired": true,
            "isDisabled": false,
            "isHidden": true,
            "defaultValue": "INPROGRESS",
            "requiredErrMsg": "",
            "patternErrMsg": ""
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
            "patternErrMsg": ""
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
            "patternErrMsg": ""
          },
          {
            "name": "SourceType",
            "jsonPath": "Connection.sourceType",
            "label": "wc.create.groups.connectionDetails.sourceType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "url": "/wcms/masters/sourcetype/_search?|$..name|$..name",
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "supplyType",
            "jsonPath": "Connection.supplyType",
            "label": "wc.create.groups.connectionDetails.supplyType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "url": "/wcms/masters/supplytype/_search?|$..name|$..name",
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },

          {
            "name": "waterTreatment",
            "jsonPath": "Connection.waterTreatment",
            "label": "wc.create.groups.connectionDetails.waterTreatment",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
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
            "pattern": "",
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
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
		  {
            "name": "FromDate",
            "jsonPath": "Connection.fromDate",
            "label": "From Date",
            "pattern": "",
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "ToDate",
            "jsonPath": "Connection.toDate",
            "label": "To Date",
            "pattern": "",
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          }
        ]
      }, 
	  {
        "label": "Metered",
        "name": "Metered",
		"children":[meterReading],
        "fields": [{
            "name": "meterMake",
            "jsonPath": "Connection.meterMake",
            "label": "Meter Make",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
		  {
            "name": "meterCost",
            "jsonPath": "Connection.meterCost",
            "label": "Meter Make",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
		  {
            "name": "initialMeterReading",
            "jsonPath": "Connection.initialMeterReading",
            "label": "Initial Meter Reading",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          }]
      },
	  {
        "label": "Donation",
        "name": "Donation",
        "fields": [{
            "name": "SpecialDonationCharges",
            "jsonPath": "Connection.toDate",
            "label": "Special Donation Charges",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          }]
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
  }
}

export default dat;
