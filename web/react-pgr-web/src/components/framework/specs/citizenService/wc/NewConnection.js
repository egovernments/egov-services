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
        "groups": [{
                "label": "wc.create.groups.applicationParticular.title", //Cut short labels by taking initial path from parent
                "name": "applicationParticular", //Follow Title case pattern
                "children": [],
                "multiple": false,
                "fields": [{
                        "name": "NameOfApplicant",
                        "jsonPath": "Connection.connectionOwner.name",
                        "label": "Applicant Name",
                        "pattern": "",
                        "type": "text",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    }, {
                        "name": "Address",
                        "jsonPath": "Connection.address.addressLine1",
                        "label": "Applicant Address",
                        "pattern": "",
                        "type": "text",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    }, {
                        "name": "Email",
                        "jsonPath": "Connection.connectionOwner.emailId",
                        "label": "wc.create.groups.applicantDetails.email",
                        "pattern": "",
                        "type": "email",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": "",
                        "defaultValue": ""
                    }, {
                        "name": "MobileNumber",
                        "jsonPath": "Connection.connectionOwner.mobileNumber",
                        "label": "wc.create.groups.applicantDetails.mobileNumber",
                        "pattern": "",
                        "type": "mobileNumber",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    }, {
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
                    }, {
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
                    }, {
                        "name": "ownerAndApplicant",
                        "jsonPath": "",
                        "label": "Owner and Applicant is same ?",
                        "pattern": "",
                        "type": "singleValueList",
                        "url": "",
                        "isRequired": false,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": "",
                        "defaultValue": [{"key": "Yes", "value": true}, {"key": "No", "value": false}]
                    }]
            },
            {
                "label": "Connection Owner Details", //Cut short labels by taking initial path from parent
                "name": "connOwnerDet", //Follow Title case pattern
                "children": [],
                "multiple": false,
                "fields": [{
                        "name": "OwnerName",
                        "jsonPath": "Connection.connectionOwner.name",
                        "label": "Owner Name",
                        "pattern": "",
                        "type": "text",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    }, {
                        "name": "OrgName",
                        "jsonPath": "Connection.connectionOwner.name",
                        "label": "Organisation Name",
                        "pattern": "",
                        "type": "text",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    }, {
                        "name": "BuildingName",
                        "jsonPath": "Connection.connectionOwner.name",
                        "label": "Building Name",
                        "pattern": "",
                        "type": "text",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    }, {
                        "name": "HouseNo",
                        "jsonPath": "Connection.connectionOwner.name",
                        "label": "House No",
                        "pattern": "",
                        "type": "number",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    }, {
                        "name": "RoadName",
                        "jsonPath": "Connection.connectionOwner.name",
                        "label": "Road Name",
                        "pattern": "",
                        "type": "text",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    }, {
                        "name": "City",
                        "jsonPath": "Connection.connectionOwner.name",
                        "label": "City",
                        "pattern": "",
                        "type": "text",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    }, {
                        "name": "District",
                        "jsonPath": "Connection.connectionOwner.name",
                        "label": "District",
                        "pattern": "",
                        "type": "singleValueList",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    }, {
                        "name": "Taluk",
                        "jsonPath": "Connection.connectionOwner.name",
                        "label": "Taluk",
                        "pattern": "",
                        "type": "singleValueList",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    }, {
                        "name": "PinCode",
                        "jsonPath": "Connection.connectionOwner.name",
                        "label": "Pin Code",
                        "pattern": "",
                        "type": "number",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    }, {
                        "name": "AadharNumber",
                        "jsonPath": "Connection.connectionOwner.name",
                        "label": "Aadhar Number",
                        "pattern": "",
                        "type": "number",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    }, {
                        "name": "EmailId",
                        "jsonPath": "Connection.connectionOwner.name",
                        "label": "Email ID",
                        "pattern": "",
                        "type": "email",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    }, {
                        "name": "PhoneNumber",
                        "jsonPath": "Connection.connectionOwner.name",
                        "label": "Phone Number",
                        "pattern": "",
                        "type": "mobileNumber",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    }]
            },
            {
                "label": "Property Details", //Cut short labels by taking initial path from parent
                "name": "propDet", //Follow Title case pattern
                "children": [],
                "multiple": false,
                "fields": [{
                      "name": "PropertyNumber",
                      "jsonPath": "Connection.connectionOwner.name",
                      "label": "Property Number",
                      "pattern": "",
                      "type": "number",
                      "isRequired": true,
                      "isDisabled": false,
                      "requiredErrMsg": "",
                      "patternErrMsg": ""
                }]
            },
            {
                "label": "wc.create.groups.connectionDetails.title",
                "name": "connectionDetails",
                "multiple": false,
                "fields": [{
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
                }, {
                  "name": "hscPipeSizeType",
                  "jsonPath": "Connection[0].hscPipeSizeType",
                  "label": "wc.create.groups.connectionDetails.hscPipeSizeType",
                  "pattern": "",
                  "type": "singleValueList",
                  "isRequired": false,
                  "isDisabled": false,
                  "requiredErrMsg": "",
                  "patternErrMsg": ""
                }, {
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
                }, {
                  "name": "meterMake",
                  "jsonPath": "Connection.subUsageType",
                  "label": "Meter Make",
                  "pattern": "",
                  "type": "singleValueList",
                  "isRequired": true,
                  "isDisabled": false,
                  "requiredErrMsg": "",
                  "patternErrMsg": "",
                  "url":""
                }, {
                  "name": "PresentConnNumber",
                  "jsonPath": "Connection.subUsageType",
                  "label": "Present Connection Number",
                  "pattern": "",
                  "type": "text",
                  "isRequired": true,
                  "isDisabled": false,
                  "requiredErrMsg": "",
                  "patternErrMsg": "",
                }, {
                  "name": "MaxMtrReading",
                  "jsonPath": "Connection.subUsageType",
                  "label": "Max Meter Reading",
                  "pattern": "",
                  "type": "text",
                  "isRequired": true,
                  "isDisabled": false,
                  "requiredErrMsg": "",
                  "patternErrMsg": "",
                }, {
                  "name": "CurrMtrReading",
                  "jsonPath": "Connection.subUsageType",
                  "label": "Current Meter Reading",
                  "pattern": "",
                  "type": "text",
                  "isRequired": true,
                  "isDisabled": false,
                  "requiredErrMsg": "",
                  "patternErrMsg": "",
                }]
            }
        ]
    }
};

export default dat;