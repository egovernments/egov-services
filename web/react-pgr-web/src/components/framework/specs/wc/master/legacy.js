var meterReading = {
 "name": "MeterReading",
 "version": "v1",
 "level": 2,
 "jsonPath": "Connection.meter[0].meterReadings",
 "groups": [
  {
   "label": "Meter Reading",
   "name": "MeterReading",
   "multiple": false,
   "hide":false,
   "children": [],
   "fields": [
    {
     "name": "Reading1",
     "jsonPath": "Connection.meter[0].meterReadings[0].reading",
     "label": "Reading 1",
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
     "label": "Reading1 Date",
     "pattern": "",
     "type": "datePicker",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",//Remove required messages
     "patternErrMsg": ""
    },
	{
     "name": "Reading2",
     "jsonPath": "Connection.meter[0].meterReadings[1].reading",
     "label": "Reading 2",
     "pattern": "",
     "type": "number",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",//Remove required messages
     "patternErrMsg": ""
    },
    {
     "name": "Reading2",
     "jsonPath": "Connection.meter[0].meterReadings[1].readingDate",
     "label": "Reading2 Date",
     "pattern": "",
     "type": "datePicker",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",//Remove required messages
     "patternErrMsg": ""
    },
	{
     "name": "Reading3",
     "jsonPath": "Connection.meter[0].meterReadings[2].reading",
     "label": "Reading 3",
     "pattern": "",
     "type": "number",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",//Remove required messages
     "patternErrMsg": ""
    },
    {
     "name": "Reading3",
     "jsonPath": "Connection.meter[0].meterReadings[2].readingDate",
     "label": "Reading3 Date",
     "pattern": "",
     "type": "datePicker",
     "isRequired": false,
     "isDisabled": false,
     "requiredErrMsg": "",//Remove required messages
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
    "idJsonPath": "Connection[0].acknowledgementNumber",
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
          			"values": [{"label":"Primary Connection", "value":"NEWCONNECTION"},{"label":"Additional Connection", "value":"ADDITIONCONNECTION"}],
          			"defaultValue":true,

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
			"values": [{"label":"With Property", "value":true},{"label":"Without Property", "value":false}],
			"defaultValue":true,
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
             }]
            }]
          }
        ]
      },{
        "label": "wc.create.groups.applicantDetails.title", //Cut short labels by taking initial path from parent
        "name": "applicantDetails", //Follow Title case pattern
        "children": [],
        "multiple": false,
        "fields": [{
            "name": "AssessmentNumber",
            "jsonPath": "Connection.property.propertyIdentifier",
            "label": "wc.create.groups.applicantDetails.propertyIdentifier",
            "pattern": "",
            "type": "textSearch",
            "isRequired": false,
            "isDisabled": false,
            "autoCompleteDependancy": {
              "autoCompleteUrl": "/pt-property/properties/_search?upicNo={value}&tenantId=default",
              "autoFillFields": {
                "Connection.asset.mobileNumber": "properties[0].owners[0].mobileNumber",
                "Connection.asset.nameOfApplicant": "properties[0].owners[0].name",
                "Connection.asset.email": "properties[0].owners[0].emailId",
                "Connection.asset.aadhaarNumber": "properties[0].owners[0].aadhaarNumber",
                "Connection.asset.noOfFloors": "properties[0].propertyDetail.noOfFloors",
                "Connection.asset.locality":"properties[0].boundary.revenueBoundary.id"
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
            "type": "singleValueList",
            "url": "/egov-location/boundarys/_search?&boundaryType=Locality|$.Boundary.*.boundaryNum|$.Boundary.*.name",
            "isRequired": false,
            "isDisabled": false,
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
            "jsonPath": "Connection.property.consumerNo",
            "label": "wc.create.groups.applicantDetails.consumerNo",
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
            "patternErrMsg": "",
            "showHideFields": [{
                   "ifValue": "Private",
                   "hide": [{
                    "name": "description",
                    "isGroup": false,
                    "isField": true
                   }],
                   "show": [{
                    "name": "NoOfFlats",
                    "isGroup": false,
                    "isField": true
                   }]
                  }]
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
            "patternErrMsg": "",
			"defaultValue": [],
			"url":""
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
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
			"defaultValue": [],
			"url":""
          },
          {
            "name": "billingType",
            "jsonPath": "Connection.billingType",
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
            "jsonPath": "Connection.connectionType",
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
            "type": "number",
            "isRequired": false,
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
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": true,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
		  {
            "name": "fromDate",
            "jsonPath": "Connection.fromDate",
            "label": "From Date",
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
            "label": "To Date",
            "pattern": "",
            "hide":true,
            "type": "datePicker",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
                "name": "NoOfFlats",
                "jsonPath": "Connection.NoOfFlats",
                "label": "No of Flats",
                "pattern": "",
                "type": "number",
                "hide":true,
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
        "hide":true,
        		"children":[meterReading],
                "fields": [{
                    "name": "meterMake",
                    "jsonPath": "Connection.meter[0].meterMake",
                    "label": "Meter Make",
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
                      "label": "Meter SlNo",
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
                    "label": "Meter Cost",
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
        "label": "wc.create.donation.subtitle",
        "name": "Donation",
        "fields": [{
            "name": "SpecialDonationCharges",
            "jsonPath": "Connection.donationCharge",
            "label": "Special Donation Charges",
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
                "type": "number",
                "isRequired": false,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
            },
            {
             "name": "receiptDate",
             "jsonPath": "Connection.receiptDate",
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
  }
}

export default dat;
