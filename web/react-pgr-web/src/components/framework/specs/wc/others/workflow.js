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
                        "name": "acknowledgementNumber",
                        "jsonPath": "Connection[0].acknowledgementNumber",
                        "label": "wc.create.groups.applicantDetails.acknowledgementNumber",
                        "pattern": "",
                        "type": "label",
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
                        "type": "label",
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
                        "type": "label",
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
                        "type": "label",
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
                        "type": "label",
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
                        "type": "label",
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
                        "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=LOCALITY&hierarchyTypeName=LOCATION|$.Boundary.*.boundaryNum|$.Boundary.*.name",
                        "type": "label",
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
                        "type": "label",
                        "isRequired": false,
                        "isDisabled": true,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    },
                    {
                        "name": "Zone",
                        "jsonPath": "Connection[0].property.zone",
                        "label": "wc.create.groups.applicantDetails.zone",
                        "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=ZONE&hierarchyTypeName=REVENUE|$.Boundary.*.boundaryNum|$.Boundary.*.name",
                        "pattern": "",
                        "type": "label",
                        "isRequired": false,
                        "isDisabled": true,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    },
                    {
                        "name": "adharNumber",
                        "jsonPath": "Connection[0].property.adharNumber",
                        "label": "wc.create.groups.applicantDetails.noOfFloors",
                        "pattern": "",
                        "type": "label",
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
                        "type": "label",
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
                "fields": [{
                        "name": "PropertyType",
                        "jsonPath": "Connection[0].property.propertyType",
                        "label": "wc.create.groups.connectionDetails.propertyType",
                        "pattern": "",
                        "type": "singleValueList",
                        "isRequired": true,
                        "isDisabled": false,
                        "url": "/pt-property/property/propertytypes/_search?|$..name|$..name",
                        "depedants": [{
                                "jsonPath": "Connection[0].categoryType",
                                "type": "dropDown",
                                "pattern": "/wcms/masters/propertytype-categorytype/_search?tenantId=default&propertyTypeName={Connection[0].property.propertyType}|$..categoryTypeName|$..categoryTypeName"
                            },
                            {
                                "jsonPath": "Connection[0].property.usageType",
                                "type": "dropDown",
                                "pattern": "/wcms/masters/propertytype-usagetype/_search?tenantId=default&propertyTypeName={Connection[0].property.propertyType}|$..usageType|$..usageType"
                            },
                            {
                                "jsonPath": "Connection[0].hscPipeSizeType",
                                "type": "dropDown",
                                "pattern": "/wcms/masters/propertytype-pipesize/_search?tenantId=default&propertyTypeName={Connection[0].property.propertyType}|$..pipeSize|$..pipeSizeInInch"
                            }
                        ],
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    },
                    {
                        "name": "CategoryType",
                        "jsonPath": "Connection[0].categoryType",
                        "label": "wc.create.groups.connectionDetails.categoryType",
                        "pattern": "",
                        "type": "singleValueList",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": "",
                        "defaultValue": [],
                        "url": '/wcms/masters/propertytype-categorytype/_search?tenantId=default&propertyTypeName={Connection[0].property.propertyType}|$..categoryTypeName|$..categoryTypeName'
                    },
                    {
                        "name": "UsageType",
                        "jsonPath": "Connection[0].property.usageType",
                        "label": "wc.create.groups.connectionDetails.usageType",
                        "pattern": "",
                        "type": "singleValueList",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": "",
                        "defaultValue": [],
                        "url": '',
                        "depedants": [{
                            "jsonPath": "Connection[0].subUsageType",
                            "type": "dropDown",
                            "pattern": "/pt-property/property/usages/_search?tenantId=default&parent={Connection[0].property.usageType}|$..code|$..name"
                        }]
                    },
                    {
                        "name": "subUsageType",
                        "jsonPath": "Connection[0].subUsageType",
                        "label": "wc.create.groups.connectionDetails.subUsageType",
                        "pattern": "",
                        "type": "singleValueList",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": "",
                        "defaultValue": [],
                        "url": ""
                    },
                    {
                        "name": "hscPipeSizeType",
                        "jsonPath": "Connection[0].hscPipeSizeType",
                        "label": "wc.create.groups.connectionDetails.hscPipeSizeType",
                        "pattern": "",
                        "type": "singleValueList",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": "",
                        "defaultValue": [],
                        "url": ''
                    },
                    {
                        "name": "applicationType",
                        "jsonPath": "Connection[0].applicationType",
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
                        "jsonPath": "Connection[0].connectionStatus",
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
                        "jsonPath": "Connection[0].billingType",
                        "label": "wc.create.groups.connectionDetails.billingType",
                        "pattern": "",
                        "type": "singleValueList",
                        "isRequired": true,
                        "isDisabled": true,
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
                        "isRequired": true,
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
                        "isRequired": true,
                        "isDisabled": false,
                        "url": "/wcms/masters/sourcetype/_search?|$..name|$..name",
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    },
                    {
                        "name": "supplyType",
                        "jsonPath": "Connection[0].supplyType",
                        "label": "wc.create.groups.connectionDetails.supplyType",
                        "pattern": "",
                        "type": "singleValueList",
                        "isRequired": true,
                        "isDisabled": false,
                        "url": "/wcms/masters/supplytype/_search?|$..name|$..name",
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    },

                    {
                        "name": "waterTreatment",
                        "jsonPath": "Connection[0].waterTreatment",
                        "label": "wc.create.groups.connectionDetails.waterTreatment",
                        "pattern": "",
                        "type": "singleValueList",
                        "isRequired": true,
                        "isDisabled": false,
                        "url": "/wcms/masters/treatmentplant/_search?|$..name|$..name",
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    },
                    {
                        "name": "sumpCapacity",
                        "jsonPath": "Connection[0].sumpCapacity",
                        "label": "wc.create.groups.connectionDetails.fields.sumpCapacity",
                        "pattern": "",
                        "type": "number",
                        "isRequired": true,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    },
                    {
                        "name": "Sequence No",
                        "jsonPath": "Connection[0].waterTreatment",
                        "label": "wc.create.groups.connectionDetails.fields.billingNumber",
                        "pattern": "",
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
                        "type": "text",
                        "isRequired": true,
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
                        "jsonPath": "Connection[0].numberOfPersons",
                        "label": "wc.create.groups.connectionDetails.fields.numberOfPersons",
                        "type": "number",
                        "isRequired": false,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": "",
                        "depedants": [{
                            "jsonPath": "Connection[0].numberOfFamily",
                            "type": "textField",
                            "pattern": "getVal('Connection[0].numberOfPersons')!=''? (Math.ceil(getVal('Connection[0].numberOfPersons')/4)):0",
                            "rg": "",
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
                        "name": "Outside ULB",
                        "jsonPath": "Connection[0].outsideUlb",
                        "label": "wc.create.groups.connectionDetails.fields.outSide",
                        "pattern": "",
                        "type": "checkbox",
                        "isRequired": false,
                        "defaultValue": true,
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