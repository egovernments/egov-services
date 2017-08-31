var dat={
  "fn.create": {
    "numCols": 12/3,
    "version": "v1",
    "url": "/wcms-connection/connection/_create",
    "idJsonPath": "Connection[0].acknowledgementNumber",
    "ackUrl": "/waterConnection/view",
    "useTimestamp": true,
    "tenantIdRequired": true,
    "level": 0,
    "groups": [
      {
        "label": "applicantDetails.title",
        "name":"Applicant Details",
        "fields": [
          {
            "name": "Applicant Name",
            "jsonPath": "ApplicationDetails.name",
            "label": "fn.ApplicationDetails.applicantName",
            "pattern": "^.{3,100}$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Mobile Number",
            "jsonPath": "ApplicationDetails.mobileNo",
            "label": "fn.ApplicationDetails.mobileNumber",
            "pattern": "",
            "type": "mobileNumber",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Email ID",
            "jsonPath": "ApplicationDetails.email",
            "label": "fn.ApplicationDetails.email",
            "pattern": "",
            "type": "email",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",

          },
          {
            "name": "Aadhar No",
            "jsonPath": "ApplicationDetails.aadhaarNo",
            "label": "fn.ApplicationDetails.aadharNo",
            "pattern": "",
            "type": "aadhar",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Address",
            "jsonPath": "ApplicationDetails.address",
            "label": "fn.ApplicationDetails.address",
            "pattern": "^.{3,255}$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Block",
            "jsonPath": "ApplicationDetails.block",
            "label": "fn.ApplicationDetails.block",
            "pattern": "^.{3,25}$",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Zone",
            "jsonPath": "ApplicationDetails.zone",
            "label": "fn.ApplicationDetails.zone",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=ZONE&hierarchyTypeName=REVENUE|$.Boundary.*.boundaryNum|$.Boundary.*.name",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Ward",
            "jsonPath": "ApplicationDetails.ward",
            "label": "fn.ApplicationDetails.ward",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=WARD&hierarchyTypeName=ADMINISTRATION|$.Boundary.*.boundaryNum|$.Boundary.*.name",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          }
        ]
      },
      {
        "label": "serviceDetails.title",
        "name": "Service Details",
        "multiple": false,
        "fields": [
          {
            "name": "Privisonal fire NOC Number",
            "jsonPath": "ServiceDetails.privisonalFireNOCNo",
            "label": "fn.serviceDetails.privisonalFireNOCNo",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Plot Number",
            "jsonPath": "ServiceDetails.plotNo",
            "label": "fn.serviceDetails.plotNo",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
          },
          {
            "name": "NOC Sub Category",
            "jsonPath": "ServiceDetails.nocSubCategory",
            "label": "fn.serviceDetails.nocSubCategory",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "defaultValue":[
              {"key":"Residential","value":"Residential"},
              {"key":"Commercial","value":"Commercial"}
            ]

          },
          {
            "name": "Vikas Prastav Number",
            "jsonPath": "ServiceDetails.vikasPrastavNo",
            "label": "fn.serviceDetails.vikasPrastavNo",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
          },
          {
            "name": "File Number",
            "jsonPath": "ServiceDetails.fileNo",
            "label": "fn.serviceDetails.fileNo",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
          },
          {
            "name": "Land Owner Name",
            "jsonPath": "ServiceDetails.landOwnerName",
            "label": "fn.serviceDetails.landOwnerName",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
          },
          {
            "name": "Developer Name",
            "jsonPath": "ServiceDetails.developerName",
            "label": "fn.serviceDetails.developerName",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
          },
          {
            "name": "Plot survey No",
            "jsonPath": "ServiceDetails.surveyNo",
            "label": "fn.serviceDetails.surveyNo",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
          },
          {
            "name": "Plot taluka name",
            "jsonPath": "ServiceDetails.talukaName",
            "label": "fn.serviceDetails.talukaName",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
          },
          {
            "name": "Architect Name",
            "jsonPath": "ServiceDetails.architectName",
            "label": "fn.serviceDetails.architectName",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
          },
          {
            "name": "Architect License Number",
            "jsonPath": "ServiceDetails.architectLicenseNo",
            "label": "fn.serviceDetails.architectLicenseNo",
            "pattern": "",
            "type": "number",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
          },
          {
            "name": "Locality",
            "jsonPath": "ServiceDetails.locality",
            "label": "fn.serviceDetails.locality",
            "pattern": "",
            "type": "singleValueList",
            "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=WARD&hierarchyTypeName=ADMINISTRATION|$.Boundary.*.boundaryNum|$.Boundary.*.name",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "Building Name",
            "jsonPath": "ServiceDetails.buildingName",
            "label": "fn.serviceDetails.buildingName",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
          },
        ]
      }
    ],
    "feeDetails": [
      {
        "id": null,
        "tenantId": localStorage.getItem("tenantId"),
        "consumerCode": "",
        "consumerType": "consumertype1",
        "businessService": "CS",
        "minimumAmountPayable": 10,
        "owner": {
          "id": 0,
          "userName": null,
          "name": null,
          "type": null,
          "mobileNumber": null,
          "emailId": null,
          "roles": null
        },
        "taxPeriodFrom": 0,
        "taxPeriodTo": 0,
        "demandDetails": [
          {
            "id": null,
            "demandId": null,
            "taxHeadMasterCode": "",
            "taxAmount": 20,
            "collectionAmount": 0
          }
        ]
      }
    ]
  },
  "wc.view": {
    "numCols": 12/3,
    "version": "v1",
    "url": "/wcms-connection/connection/_search?acknowledgementNumber={acknowledgementNumber}",
    "useTimestamp": true,
    "tenantIdRequired": true,
    //Insteadofbooleanvaluegivejsonpath"objectName": "Connection",
    "level": 0,
    "groups": [
      {
        "label": "wc.create.groups.applicationParticular.title",
        //Cutshortlabelsbytakinginitialpathfromparent"name": "applicationParticular",
        //FollowTitlecasepattern"children": [],
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
            "values": [
              {
                "label": "wc.group.withProperty",
                "value": true
              },
              {
                "label": "wc.group.withoutProperty",
                "value": false
              }
            ],
            "defaultValue": false,
            "showHideFields": [
              {
                "ifValue": false,
                "hide": [
                  {
                    "name": "applicantDetails",
                    "isGroup": true,
                    "isField": false
                  }
                ],
                "show": [
                  {
                    "name": "NoOfFlats",
                    "isGroup": false,
                    "isField": true
                  },
                  {
                    "name": "applicantDetailsWithProp",
                    "isGroup": true,
                    "isField": false
                  }
                ]
              }
            ]
          }
        ]
      },
      {
        "label": "wc.create.groups.applicantDetails.title",
        //Cutshortlabelsbytakinginitialpathfromparent"name": "applicantDetailsWithProp",
        //FollowTitlecasepattern,
        "hide": true,
        "multiple": false,
        "fields": [
          {
            "name": "NameOfApplicant",
            "jsonPath": "Connection[0].connectionOwner.name",
            "label": "wc.create.groups.applicantDetails.nameOfApplicant",
            "pattern": "",
            "type": "text",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
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
            "isRequired": false,
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
            "type": "textarea",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": ""
          },
          {
            "name": "PinCode",
            "jsonPath": "Connection[0].address.pinCode",
            "label": "pt.create.groups.propertyAddress.fields.pin",
            "pattern": "",
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
            "isRequired": false,
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
            "isRequired": false,
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
            "values": [
              {
                "label": "pt.create.groups.ownerDetails.fields.primaryOwner",
                "value": true
              },
              {
                "label": "pt.create.groups.ownerDetails.fields.secondaryOwner",
                "value": false
              }
            ],
            "defaultValue": true
          }
        ]
      },
      {
        "label": "wc.create.groups.applicantDetails.title",
        //Cutshortlabelsbytakinginitialpathfromparent"name": "applicantDetails",
        //FollowTitlecasepattern"children": [],
        "multiple": false,
        "hide": false,
        "fields": [
          {
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
          {
            "name": "NameOfApplicant",
            "jsonPath": "Connection[0].property.nameOfApplicant",
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
            "isRequired": false,
            "isDisabled": true,
            "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=LOCALITY&hierarchyTypeName=LOCATION|$.Boundary.*.boundaryNum|$.Boundary.*.name",
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
            "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=ZONE&hierarchyTypeName=REVENUE|$.Boundary.*.boundaryNum|$.Boundary.*.name",
            "isRequired": false,
            "isDisabled": true,
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
            "jsonPath": "Connection[0].subUsageType",
            "label": "wc.create.groups.connectionDetails.subUsageType",
            "pattern": "",
            "type": "singleValueList",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "url": "/pt-property/property/usages/_search?|$..name|$..name"
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
            "patternErrMsg": ""
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
            "patternErrMsg": ""
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
            "pattern": "^(0|[1-9][0-9]*)$",
            "isRequired": false,
            "isDisabled": false,
            "requiredErrMsg": "",
            "patternErrMsg": "",
            "depedants": [
              {
                "jsonPath": "Connection.numberOfFamily",
                "type": "textField",
                "pattern": "getVal('Connection.numberOfPersons')!=''? (Math.ceil(getVal('Connection.numberOfPersons')/4)):0",
                "rg": "",
                "isRequired": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              }
            ]
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
            "pattern": "^(0|[1-9][0-9]*)$",
            "type": "number",
            "isRequired": false,
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
          }
        ]
      }
    ]
  }
}

export default dat;
