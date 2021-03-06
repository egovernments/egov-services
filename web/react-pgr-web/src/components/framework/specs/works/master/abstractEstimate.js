var dat = {
	"works.create": {
		"numCols": 4,
    "useTimestamp": true,
		"title":"works.create.abstractEstimate",
    "objectName": "abstractEstimates",
		"url":"works-estimate/v1/abstractestimates/_create",
		"tenantIdRequired":true,
		"groups": [
			{
				"label": "works.create.groups.label.estandadminsanction",
				"name": "Abstract Estimate",
				"fields": [
            {
              "name": "dateOfProposal",
              "jsonPath": "abstractEstimates[0].dateOfProposal",
              "label": "works.create.groups.fields.dateOfProposal",
              "pattern": "",
              "type": "datePicker",
              "url": "",
              "isRequired": true,
              "isDisabled": true,
              "requiredErrMsg": "Please enter valid date",
              "patternErrMsg": ""
            },
						{
              "name": "department",
              "jsonPath": "abstractEstimates[0].department.code",
              "label": "works.create.groups.fields.department",
              "pattern": "",
              "type": "singleValueList",
							"url": "/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Department|$..code|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "referenceType",
              "jsonPath": "abstractEstimates[0].referenceType.code",
              "label": "works.create.groups.fields.referenceType",
              "pattern": "",
              "type": "singleValueList",
							"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=ReferenceType|$..code|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "referenceNumber",
              "jsonPath": "abstractEstimates[0].referenceNumber",
              "label": "works.create.groups.fields.referenceNumber",
              "pattern": "",
              "type": "text",
              "url": "",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"maxLength": 100,
	            "minLength": 1
            },
						{
							"name": "subject",
							"jsonPath": "abstractEstimates[0].subject",
							"label": "works.create.groups.fields.subject",
							"pattern": "",
							"type": "textarea",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"maxLength": 256,
	            "minLength": 1
						},
						{
							"name": "description",
							"jsonPath": "abstractEstimates[0].description",
							"label": "works.create.groups.fields.description",
							"pattern": "",
							"type": "textarea",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"maxLength": 1024,
	            "minLength": 1
						},
						{
              "name": "natureOfWork",
              "jsonPath": "abstractEstimates[0].natureOfWork.code",
              "label": "works.create.groups.fields.natureOfWork",
              "pattern": "",
              "type": "singleValueList",
							"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=NatureOfWork|$..code|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"showHideFields": [
								{
									"ifValue": "New",
									"hide": [
										{
												"name": "Asset Details",
												"isGroup": true,
												"isField": false
										}
									],
									"show": [{
											"name": "Assets - Land",
											"isGroup": true,
											"isField": false
									}
								]},
							]
            },
						{
              "name": "modeOfAllotment",
              "jsonPath": "abstractEstimates[0].modeOfAllotment.code",
              "label": "works.create.groups.fields.modeOfAllotment",
              "pattern": "",
              "type": "singleValueList",
              "url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=ModeOfAllotment|$..code|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "typeOfWork",
              "jsonPath": "abstractEstimates[0].typeOfWork.code",
              "label": "works.create.groups.fields.typeOfWork",
              "pattern": "",
              "type": "singleValueList",
              "url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=TypeOfWork&parent=null|$..code|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"depedants": [{
	              "jsonPath": "abstractEstimates[0].subTypeOfWork.code",
	              "type": "dropDown",
								"pattern": "/egov-mdms-service/v1/_get?&moduleName=Works&masterName=TypeOfWork&filter=%5B%3F%28%40.parent%3D%3D'{abstractEstimates[0].typeOfWork.code}'%29%5D|$..code|$..name"
	            }]
            },
						{
              "name": "subTypeOfWork",
              "jsonPath": "abstractEstimates[0].subTypeOfWork.code",
              "label": "works.create.groups.fields.subTypeOfWork",
              "pattern": "",
              "type": "singleValueList",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "workCategory",
              "jsonPath": "abstractEstimates[0].workCategory",
              "label": "works.create.groups.fields.workCategory",
              "pattern": "",
              "type": "singleValueList",
              "url": "",
							"defaultValue":[
								{key: null, value: "-- Please Select --"},
								{
                    "key": "NON_SLUM",
                    "value": "NON SLUM"
                },
								{
                    "key": "NOTIFIED_SLUM",
                    "value": "NOTIFIED SLUM"
                },
								{
                    "key": "NON_NOTIFIED_SLUM",
                    "value": "NON NOTIFIED SLUM"
                },
							],
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "beneficiary",
              "jsonPath": "abstractEstimates[0].beneficiary",
              "label": "works.create.groups.fields.beneficiary",
              "pattern": "",
              "type": "singleValueList",
              "url": "",
							"defaultValue":[
								{key: null, value: "-- Please Select --"},
								{
                    "key": "SC",
                    "value": "SC"
                },
								{
                    "key": "ST",
                    "value": "ST"
                },
								{
                    "key": "BC",
                    "value": "BC"
                },
								{
                    "key": "MINORITY",
                    "value": "MINORITY"
                },
								{
                    "key": "WOMEN_CHILDREN_WELFARE",
                    "value": "WOMEN CHILDREN WELFARE"
                },
								{
                    "key": "GENERAL",
                    "value": "GENERAL"
                },
							],
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "ward",
              "jsonPath": "abstractEstimates[0].ward.code",
              "label": "works.create.groups.fields.ward",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?tenantId=default&boundaryTypeName=Ward&hierarchyTypeName=ADMINISTRATION|$..id|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "locality",
              "jsonPath": "abstractEstimates[0].locality.code",
              "label": "works.create.groups.fields.locality",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=LOCALITY&hierarchyTypeName=LOCATION|$..id|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "workProposedAsPerDP",
              "jsonPath": "abstractEstimates[0].workProposedAsPerDP",
              "label": "works.create.groups.fields.workProposedAsPerDP",
              "pattern": "",
              "type": "singleValueList",
              "url": "",
							"defaultValue":[
								{key: null, value: "-- Please Select --"},
								{
                    "key": true,
                    "value": "Yes"
                },
								{
                    "key": false,
                    "value": "No"
                }
							],
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
							"name": "dpRemarks",
							"jsonPath": "abstractEstimates[0].dpRemarks",
							"label": "works.create.groups.fields.dpRemarks",
							"pattern": "",
							"type": "textarea",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"maxLength":512
						},
						{
							"name": "implementationPeriod",
							"jsonPath": "abstractEstimates[0].implementationPeriod",
							"label": "works.create.groups.fields.implementationPeriod",
							"pattern": "^[0-9]+$",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": "works.create.pattern.fields.implementationPeriod",
							"maxLength":5
						},
						{
              "name": "pmcRequired",
              "jsonPath": "abstractEstimates[0].pmcRequired",
              "label": "works.create.groups.fields.pmcRequired",
              "pattern": "",
              "type": "singleValueList",
              "url": "",
							"isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"defaultValue":[
								{key: null, value: "-- Please Select --"},
								{
                    "key": true,
                    "value": "Yes"
                },
								{
                    "key": false,
                    "value": "No"
                }
							],
							"showHideFields": [
								{
									"ifValue": true,
									"hide": [],
									"show": [
										{
												"name": "pmcType",
												"jpath":"abstractEstimates[0].pmcType",
												"isGroup": false,
												"isField": true
										}
									]
								},
								{
									"ifValue": false,
									"hide": [
										{
											"name": "pmcName",
											"jpath":"abstractEstimates[0].pmcName",
											"isGroup": false,
											"isField": true
										}
									],
									"show": []
								}
							]
            },
						{
              "name": "pmcType",
							"hide":true,
              "jsonPath": "abstractEstimates[0].pmcType",
              "label": "works.create.groups.fields.pmcType",
              "pattern": "",
              "type": "singleValueList",
              "url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=PMCType|$..id|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"showHideFields": [
								{
									"ifValue": 1,
									"hide": [],
									"show": [
										{
												"name": "pmcName",
												"jpath":"abstractEstimates[0].pmcName",
												"isGroup": false,
												"isField": true
										}
									]
								}
							]
            },
						{
              "name": "pmcName",
							"hide":true,
              "jsonPath": "abstractEstimates[0].pmcName",
              "label": "works.create.groups.fields.pmcName",
              "pattern": "",
              "type": "singleValueList",
              "url": "/works-masters/v1/contractors/_search?&pmc=true&statuses=ACTIVE|$.contractors[*].code|$.contractors[*].name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
            }
				]
			},
			{
				"label": "works.create.groups.label.assetdetails",
				"name": "Asset Details",
				"hide":true,
				"fields":[
					{
						"name": "code",
						"jsonPath": "abstractEstimates[0].assetDetails[0].asset.code",
						"label": "works.create.groups.fields.assetCode",
						"pattern": "",
						"type": "text",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "assetCondition",
						"jsonPath": "abstractEstimates[0].assetDetails[0].assetCondition.name",
						"label": "works.create.groups.fields.assetCondition",
						"pattern": "",
						"type": "singleValueList",
						"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=AssetPresentCondition|$..id|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "assetRemarks",
						"jsonPath": "abstractEstimates[0].assetDetails[0].assetRemarks",
						"label": "works.create.groups.fields.assetRemarks",
						"pattern": "",
						"type": "textarea",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"maxLength":1024
					},
				]
			},
			{
				"label": "works.create.groups.label.asssetsland",
				"name": "Assets - Land",
				"hide":true,
				"fields":[
					{
						"name": "landAssetRequired",
						"jsonPath": "abstractEstimates[0].landAssetRequired",
						"label": "works.create.groups.fields.landAssetRequired",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"defaultValue":[
							{key: null, value: "-- Please Select --"},
							{
									"key": true,
									"value": "Yes"
							},
							{
									"key": false,
									"value": "No"
							}
						],
						"checkMandatory":[
							{
								"ifValue": "true",
								"required": [
									{
										"jpath": "abstractEstimates[0].noOfLands",
									}
								],
								"notRequired":[

								]
							},
							{
								"ifValue": "false",
								"notRequired": [
									{
										"jpath": "abstractEstimates[0].noOfLands",
									}
								]
							}
						]
					},
					{
						"name": "noOfLands",
						"jsonPath": "abstractEstimates[0].noOfLands",
						"label": "works.create.groups.fields.noOfLands",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
			      "type": "tableList",
			      "jsonPath": "abstractEstimates[0].assetDetails",
			      "tableList": {
			        "header": [
								{
				          "label": "works.create.groups.label.landassetId"
				        },
								{
				          "label": "works.create.groups.label.natureofownership"
				        },
								{
				          "label": "works.create.groups.label.arealand"
				        },
								{
				          "label": "works.create.groups.label.plotno"
				        },
								{
				          "label": "works.create.groups.label.surveyno"
				        },
								{
				          "label": "works.create.groups.label.landAssetCondition"
				        }, {
				          "label": "works.create.groups.label.constructionArea"
			        }],
			        "values": [
								{
				          "name": "landassetId",
				          "pattern": "",
				          "type": "text",
				          "jsonPath": "abstractEstimates[0].assetDetails[0].landassetId",
				          "isRequired": false,
				          "isDisabled": false
				        },
								{
				          "name": "natureofownership",
				          "pattern": "",
				          "type": "text",
				          "jsonPath": "abstractEstimates[0].assetDetails[0].natureofownership",
				          "isDisabled": true
				        },
								{
				          "name": "arealand",
				          "pattern": "",
				          "type": "text",
				          "jsonPath": "abstractEstimates[0].assetDetails[0].arealand",
				          "isDisabled": true
				        },
								{
				          "name": "plotno",
				          "pattern": "",
				          "type": "text",
				          "jsonPath": "abstractEstimates[0].assetDetails[0].plotno",
				          "isDisabled": true
				        },
								{
				          "name": "surveyno",
				          "pattern": "",
				          "type": "text",
				          "jsonPath": "abstractEstimates[0].assetDetails[0].surveyno",
				          "isDisabled": true
				        },
								{
			          "name": "landAssetCondition",
			          "pattern": "",
			          "type": "singleValueList",
								"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=LandAssetPresentCondition|$..id|$..name",
			          "jsonPath": "abstractEstimates[0].assetDetails[0].landAssetCondition.name",
			          "isRequired": false,
			          "isDisabled": false,
								"maxLength":1024
			        }, {
			          "name": "constructionArea",
			          "pattern": "",
			          "type": "text",
			          "jsonPath": "abstractEstimates[0].assetDetails[0].constructionArea",
			          "isRequired": false,
			          "isDisabled": false
			        }]
			      }
			    },
					{
						"name": "remarks",
						"jsonPath": "abstractEstimates[0].assetDetails[0].assetRemarks",
						"label": "works.create.groups.fields.remarks",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
				]
			},
			{
			  "label": "works.create.groups.label.workdetails",
			  "name": "Work Details",
			  "fields":[
			    {
			      "type": "tableList",
			      "jsonPath": "abstractEstimates[0].abstractEstimateDetails",
			      "tableList": {
			        "header": [{
			          "label": "works.create.groups.label.nameofthework"
			        }, {
			          "label": "works.create.groups.label.estimateAmount"
			        }],
			        "values": [{
			          "name": "nameOfWork",
			          "pattern": "",
			          "type": "textarea",
			          "jsonPath": "abstractEstimates[0].abstractEstimateDetails[0].nameOfWork",
			          "isRequired": true,
			          "isDisabled": false,
								"maxLength":1024
			        }, {
			          "name": "estimateAmount",
			          "pattern": "^\\d{0,20}(\\.\\d{0,2})?$",
								"patternErrMsg": "works.create.pattern.fields.label.estimateAmount",
			          "type": "text",
			          "jsonPath": "abstractEstimates[0].abstractEstimateDetails[0].estimateAmount",
			          "isRequired": true,
			          "textAlign":'right',
			          "isDisabled": false
			        }],
			        "hasFooter":true,
			        "footer":[
			          {1:"abstractEstimates[*].abstractEstimateDetails[*].estimateAmount"}
			        ]
			      }
			    }
			  ]
			},
			{
			  "label": "works.create.groups.label.sanctionDetails",
			  "name": "Sanction Details",
			  "fields":[
			    {
			      "type": "tableList",
			      "jsonPath": "abstractEstimates[0].sanctionDetails",
			      "tableList": {
			        "header": [{
			          "label": "works.create.groups.fields.sanctionType"
			        }, {
			          "label": "works.create.groups.fields.sanctionAuthority"
			        }],
			        "values": [{
			          "name": "sanctionType",
			          "pattern": "",
			          "type": "singleValueList",
			          "jsonPath": "abstractEstimates[0].sanctionDetails[0].sanctionType",
			          "isRequired": false,
			          "isDisabled": true,
								"defaultValue":[
									{key: null, value: "-- Please Select --"},
						      {"key":'FINANCIAL_SANCTION',"value":"FINANCIAL SANCTION"},
						      {"key":'ADMINISTRATIVE_SANCTION',"value":'ADMINISTRATIVE SANCTION'},
						      {"key":'TECHNICAL_SANCTION',"value":'TECHNICAL SANCTION'}]
			        }, {
			          "name": "sanctionAuthority",
			          "pattern": "",
			          "type": "singleValueList",
			          "jsonPath": "abstractEstimates[0].sanctionDetails[0].sanctionAuthority.name",
			          "isRequired": false,
			          "isDisabled": false,
								"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=EstimateSanctionAuthority|$..id|$..name"
			        }],
							"actionsNotRequired":true
			      }
			    }
			  ]
			},
			{
				"label": "works.create.groups.label.uploadDocs",
				"name": "Documents",
				"fields":[
					{
						"name": "File",
						"type": "viewDocuments",
						"url":"",
						"jsonPath": "abstractEstimates[0].documents",
						"maxFile":5,
						"addRequired":true
					}
				]
			}
		]
	},
	"works.update": {
		"numCols": 4,
    "useTimestamp": true,
    "objectName": "abstractEstimates",
		"searchUrl": "works-estimate/v1/abstractestimates/_search?abstractEstimateNumbers={id}",
		"url":"works-estimate/v1/abstractestimates/_update",
		"tenantIdRequired": true,
		"groups": [
			{
				"label": "works.create.groups.label.estandadminsanction",
				"name": "Abstract Estimate",
				"fields": [
            {
              "name": "dateOfProposal",
              "jsonPath": "abstractEstimates[0].dateOfProposal",
              "label": "works.create.groups.fields.dateOfProposal",
              "pattern": "",
              "type": "datePicker",
              "url": "",
              "isRequired": false,
              "isDisabled": true,
              "requiredErrMsg": "Please enter valid date",
              "patternErrMsg": ""
            },
						{
              "name": "department",
              "jsonPath": "abstractEstimates[0].department.code",
              "label": "works.create.groups.fields.department",
              "pattern": "",
              "type": "singleValueList",
							"url": "/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Department|$..code|$..name",
              "isRequired": false,
              "isDisabled": true,
							"convertToString":true,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "referenceType",
              "jsonPath": "abstractEstimates[0].referenceType.code",
              "label": "works.create.groups.fields.referenceType",
              "pattern": "",
              "type": "singleValueList",
							"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=ReferenceType|$..code|$..name",
              "isRequired": false,
              "isDisabled": true,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "referenceNumber",
              "jsonPath": "abstractEstimates[0].referenceNumber",
              "label": "works.create.groups.fields.referenceNumber",
              "pattern": "",
              "type": "text",
              "url": "",
              "isRequired": false,
              "isDisabled": true,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
							"name": "subject",
							"jsonPath": "abstractEstimates[0].subject",
							"label": "works.create.groups.fields.subject",
							"pattern": "",
							"type": "textarea",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "abstractEstimates[0].description",
							"label": "works.create.groups.fields.description",
							"pattern": "",
							"type": "textarea",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
              "name": "natureOfWork",
              "jsonPath": "abstractEstimates[0].natureOfWork.code",
              "label": "works.create.groups.fields.natureOfWork",
              "pattern": "",
              "type": "singleValueList",
							"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=NatureOfWork|$..code|$..name",
              "isRequired": false,
              "isDisabled": true,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"showHideFields": [
								{
									"ifValue": "New",
									"hide": [
										{
												"name": "Asset Details",
												"isGroup": true,
												"isField": false
										}
									],
									"show": [{
											"name": "Assets - Land",
											"isGroup": true,
											"isField": false
									}
								]},
							]
            },
						{
              "name": "modeOfAllotment",
              "jsonPath": "abstractEstimates[0].modeOfAllotment.code",
              "label": "works.create.groups.fields.modeOfAllotment",
              "pattern": "",
              "type": "singleValueList",
              "url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=ModeOfAllotment|$..code|$..name",
              "isRequired": false,
              "isDisabled": true,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "typeOfWork",
              "jsonPath": "abstractEstimates[0].typeOfWork.code",
              "label": "works.create.groups.fields.typeOfWork",
              "pattern": "",
              "type": "singleValueList",
              "url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=TypeOfWork&parent=null|$..code|$..name",
              "isRequired": false,
              "isDisabled": true,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"depedants": [{
	              "jsonPath": "abstractEstimates[0].subTypeOfWork.code",
	              "type": "dropDown",
								"pattern": "/egov-mdms-service/v1/_get?&moduleName=Works&masterName=TypeOfWork&filter=%5B%3F%28%40.parent%3D%3D'{abstractEstimates[0].typeOfWork.code}'%29%5D|$..code|$..name"
	            }]
            },
						{
              "name": "subTypeOfWork",
              "jsonPath": "abstractEstimates[0].subTypeOfWork.code",
              "label": "works.create.groups.fields.subTypeOfWork",
              "pattern": "",
              "type": "singleValueList",
              "isRequired": false,
              "isDisabled": true,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "workCategory",
              "jsonPath": "abstractEstimates[0].workCategory",
              "label": "works.create.groups.fields.workCategory",
              "pattern": "",
              "type": "singleValueList",
              "url": "",
							"defaultValue":[
								{key: null, value: "-- Please Select --"},
								{
                    "key": "NON_SLUM",
                    "value": "NON SLUM"
                },
								{
                    "key": "NOTIFIED_SLUM",
                    "value": "NOTIFIED SLUM"
                },
								{
                    "key": "NON_NOTIFIED_SLUM",
                    "value": "NON NOTIFIED SLUM"
                },
							],
              "isRequired": false,
              "isDisabled": true,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "beneficiary",
              "jsonPath": "abstractEstimates[0].beneficiary",
              "label": "works.create.groups.fields.beneficiary",
              "pattern": "",
              "type": "singleValueList",
              "url": "",
							"defaultValue":[
								{key: null, value: "-- Please Select --"},
								{
                    "key": "SC",
                    "value": "SC"
                },
								{
                    "key": "ST",
                    "value": "ST"
                },
								{
                    "key": "BC",
                    "value": "BC"
                },
								{
                    "key": "MINORITY",
                    "value": "MINORITY"
                },
								{
                    "key": "WOMEN_CHILDREN_WELFARE",
                    "value": "WOMEN CHILDREN WELFARE"
                },
								{
                    "key": "GENERAL",
                    "value": "GENERAL"
                },
							],
              "isRequired": false,
              "isDisabled": true,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "locality",
              "jsonPath": "abstractEstimates[0].locality.code",
              "label": "works.create.groups.fields.locality",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=LOCALITY&hierarchyTypeName=LOCATION|$..id|$..name",
              "isRequired": false,
              "isDisabled": true,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "ward",
              "jsonPath": "abstractEstimates[0].ward.code",
              "label": "works.create.groups.fields.ward",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?tenantId=default&boundaryTypeName=Ward&hierarchyTypeName=ADMINISTRATION|$..id|$..name",
              "isRequired": false,
              "isDisabled": true,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "workProposedAsPerDP",
              "jsonPath": "abstractEstimates[0].workProposedAsPerDP",
              "label": "works.create.groups.fields.workProposedAsPerDP",
              "pattern": "",
              "type": "singleValueList",
							"convertToString":true,
              "url": "",
							"defaultValue":[
								{key: null, value: "-- Please Select --"},
								{
                    "key": true,
                    "value": "Yes"
                },
								{
                    "key": false,
                    "value": "No"
                }
							],
              "isRequired": false,
              "isDisabled": true,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
							"name": "dpRemarks",
							"jsonPath": "abstractEstimates[0].dpRemarks",
							"label": "works.create.groups.fields.dpRemarks",
							"pattern": "",
							"type": "textarea",
							"isRequired": false,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
              "name": "pmcRequired",
              "jsonPath": "abstractEstimates[0].pmcRequired",
              "label": "works.create.groups.fields.pmcRequired",
              "pattern": "",
              "type": "singleValueList",
							"convertToString":true,
              "url": "",
							"isRequired": false,
              "isDisabled": true,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"defaultValue":[
								{key: null, value: "-- Please Select --"},
								{
                    "key": true,
                    "value": "Yes"
                },
								{
                    "key": false,
                    "value": "No"
                }
							],
							"showHideFields": [
								{
									"ifValue": true,
									"hide": [],
									"show": [
										{
												"name": "pmcType",
												"jpath":"abstractEstimates[0].pmcType",
												"isGroup": false,
												"isField": true
										}
									]
								}
							]
            },
						{
              "name": "pmcType",
							"hide":true,
              "jsonPath": "abstractEstimates[0].pmcType",
              "label": "works.create.groups.fields.pmcType",
              "pattern": "",
              "type": "singleValueList",
              "url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=PMCType|$..id|$..name",
              "isRequired": false,
              "isDisabled": true,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"showHideFields": [
								{
									"ifValue": 1,
									"hide": [],
									"show": [
										{
												"name": "pmcName",
												"jpath":"abstractEstimates[0].pmcName",
												"isGroup": false,
												"isField": true
										}
									]
								}]
            },
						{
              "name": "pmcName",
							"hide":true,
              "jsonPath": "abstractEstimates[0].pmcName",
              "label": "works.create.groups.fields.pmcName",
              "pattern": "",
              "type": "singleValueList",
              "url": "/works-masters/v1/contractors/_search?&pmc=true|$.contractors[*].code|$.contractors[*].name",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
            },
						{
							"name": "implementationPeriod",
							"jsonPath": "abstractEstimates[0].implementationPeriod",
							"label": "works.create.groups.fields.implementationPeriod",
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
				"label": "works.create.groups.label.assetdetails",
				"name": "Asset Details",
				"hide":true,
				"fields":[
					{
						"name": "code",
						"jsonPath": "abstractEstimates[0].assetDetails[0].asset.code",
						"label": "works.create.groups.fields.assetCode",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": true,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "assetCondition",
						"jsonPath": "abstractEstimates[0].assetDetails[0].assetCondition.name",
						"label": "works.create.groups.fields.assetCondition",
						"pattern": "",
						"type": "singleValueList",
						"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=AssetPresentCondition|$..id|$..name",
						"isRequired": false,
						"isDisabled": true,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "assetRemarks",
						"jsonPath": "abstractEstimates[0].assetDetails[0].assetRemarks",
						"label": "works.create.groups.fields.assetRemarks",
						"pattern": "",
						"type": "textarea",
						"isRequired": false,
						"isDisabled": true,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"maxLength":1024
					},
				]
			},
			{
				"label": "works.create.groups.label.asssetsland",
				"name": "Assets - Land",
				"hide":true,
				"fields":[
					{
						"name": "landAssetRequired",
						"jsonPath": "abstractEstimates[0].landAssetRequired",
						"label": "works.create.groups.fields.landAssetRequired",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": false,
						"isDisabled": true,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"defaultValue":[
							{key: null, value: "-- Please Select --"},
							{
									"key": true,
									"value": "Yes"
							},
							{
									"key": false,
									"value": "No"
							}
						],
						"checkMandatory":[
							{
								"ifValue": "true",
								"required": [
									{
										"jpath": "abstractEstimates[0].noOfLands",
									}
								],
								"notRequired":[

								]
							},
							{
								"ifValue": "false",
								"notRequired": [
									{
										"jpath": "abstractEstimates[0].noOfLands",
									}
								]
							}
						]
					},
					{
						"name": "noOfLands",
						"jsonPath": "abstractEstimates[0].noOfLands",
						"label": "works.create.groups.fields.noOfLands",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": true,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
			      "type": "tableList",
			      "jsonPath": "abstractEstimates[0].assetDetails",
			      "tableList": {
			        "header": [
								{
				          "label": "works.create.groups.label.landassetId"
				        },
								{
				          "label": "works.create.groups.label.natureofownership"
				        },
								{
				          "label": "works.create.groups.label.arealand"
				        },
								{
				          "label": "works.create.groups.label.plotno"
				        },
								{
				          "label": "works.create.groups.label.surveyno"
				        },
								{
				          "label": "works.create.groups.label.landAssetCondition"
				        }, {
				          "label": "works.create.groups.label.constructionArea"
			        }],
			        "values": [
								{
				          "name": "landassetId",
				          "pattern": "",
				          "type": "text",
				          "jsonPath": "abstractEstimates[0].assetDetails[0].landassetId",
				          "isRequired": false,
				          "isDisabled": true
				        },
								{
				          "name": "natureofownership",
				          "pattern": "",
				          "type": "text",
				          "jsonPath": "abstractEstimates[0].assetDetails[0].natureofownership",
									"isRequired": false,
				          "isDisabled": true
				        },
								{
				          "name": "arealand",
				          "pattern": "",
				          "type": "text",
				          "jsonPath": "abstractEstimates[0].assetDetails[0].arealand",
									"isRequired": false,
				          "isDisabled": true
				        },
								{
				          "name": "plotno",
				          "pattern": "",
				          "type": "text",
				          "jsonPath": "abstractEstimates[0].assetDetails[0].plotno",
									"isRequired": false,
				          "isDisabled": true
				        },
								{
				          "name": "surveyno",
				          "pattern": "",
				          "type": "text",
				          "jsonPath": "abstractEstimates[0].assetDetails[0].surveyno",
									"isRequired": false,
				          "isDisabled": true
				        },
								{
			          "name": "landAssetCondition",
			          "pattern": "",
			          "type": "singleValueList",
								"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=LandAssetPresentCondition|$..id|$..name",
			          "jsonPath": "abstractEstimates[0].assetDetails[0].landAssetCondition.name",
								"isRequired": false,
								"isDisabled": true,
								"maxLength":1024
			        }, {
			          "name": "constructionArea",
			          "pattern": "",
			          "type": "text",
			          "jsonPath": "abstractEstimates[0].assetDetails[0].constructionArea",
								"isRequired": false,
								"isDisabled": true
			        }]
			      }
			    },
					{
						"name": "remarks",
						"jsonPath": "abstractEstimates[0].assetDetails[0].assetRemarks",
						"label": "works.create.groups.fields.remarks",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": true,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
				]
			},
			{
			  "label": "works.create.groups.label.workdetails",
			  "name": "Work Details",
			  "fields":[
			    {
			      "type": "tableList",
			      "jsonPath": "abstractEstimates[0].abstractEstimateDetails",
			      "tableList": {
			        "header": [{
			          "label": "works.create.groups.label.nameofthework"
			        }, {
			          "label": "works.create.groups.label.estimateAmount"
			        }],
			        "values": [{
			          "name": "nameofthework",
			          "pattern": "",
			          "type": "textarea",
			          "jsonPath": "abstractEstimates[0].abstractEstimateDetails[0].nameOfWork",
			          "isRequired": false,
			          "isDisabled": true
			        }, {
			          "name": "estimateAmount",
			          "pattern": "",
			          "type": "text",
			          "jsonPath": "abstractEstimates[0].abstractEstimateDetails[0].estimateAmount",
			          "isRequired": false,
			          "textAlign":'right',
			          "isDisabled": true
			        }],
							"actionsNotRequired":true,
			        "hasFooter":true,
			        "footer":[
			          {1:"abstractEstimates[*].abstractEstimateDetails[*].estimateAmount"}
			        ]
			      }
			    }
			  ]
			},
			{
			  "label": "works.create.groups.label.sanctionDetails",
			  "name": "Sanction Details",
			  "fields":[
			    {
			      "type": "tableList",
			      "jsonPath": "abstractEstimates[0].sanctionDetails",
			      "tableList": {
			        "header": [{
			          "label": "works.create.groups.fields.sanctionType"
			        }, {
			          "label": "works.create.groups.fields.sanctionAuthority"
			        }],
			        "values": [{
			          "name": "sanctionType",
			          "pattern": "",
			          "type": "singleValueList",
			          "jsonPath": "abstractEstimates[0].sanctionDetails[0].sanctionType",
			          "isRequired": false,
			          "isDisabled": true,
								"defaultValue":[
									{key: null, value: "-- Please Select --"},
						      {"key":'FINANCIAL_SANCTION',"value":"FINANCIAL SANCTION"},
						      {"key":'ADMINISTRATIVE_SANCTION',"value":'ADMINISTRATIVE SANCTION'},
						      {"key":'TECHNICAL_SANCTION',"value":'TECHNICAL SANCTION'}]
			        }, {
			          "name": "sanctionAuthority",
			          "pattern": "",
			          "type": "singleValueList",
			          "jsonPath": "abstractEstimates[0].sanctionDetails[0].sanctionAuthority.name",
			          "isRequired": false,
			          "isDisabled": true,
								"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=EstimateSanctionAuthority|$..id|$..name"
			        }],
							"actionsNotRequired":true
			      }
			    }
			  ]
			},
			{
			  "label": "works.create.groups.label.financialDetails",
			  "name": "Financial Details",
			  "fields":[
					{
						"name": "fund",
						"jsonPath": "abstractEstimates[0].fund.code",
						"label": "works.create.groups.fields.fundCodes",
						"pattern": "",
						"type": "singleValueList",
						"url": "/egov-mdms-service/v1/_get?&moduleName=egf-master&masterName=funds|$..code|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "function",
						"jsonPath": "abstractEstimates[0].function.code",
						"label": "works.create.groups.fields.functionCodes",
						"pattern": "",
						"type": "singleValueList",
						"url": "/egov-mdms-service/v1/_get?&moduleName=egf-master&masterName=Function|$..code|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "objectCode",
						"jsonPath": "abstractEstimates[0].budgetGroup.id",
						"label": "works.create.groups.fields.objectCode",
						"pattern": "",
						"type": "singleValueList",
						"url": "/egov-mdms-service/v1/_get?&moduleName=Works&masterName=BudgetGroup|$..id|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "accountCode",
						"jsonPath": "abstractEstimates[0].accountCode",
						"label": "works.create.groups.fields.accountCode",
						"pattern": "",
						"type": "singleValueList",
						"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=AssetPresentCondition|$..id|$..name",
						"isRequired": false,
						"isDisabled": true,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "scheme",
						"jsonPath": "abstractEstimates[0].scheme.code",
						"label": "works.create.groups.fields.scheme",
						"pattern": "",
						"type": "singleValueList",
						"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=Scheme|$..code|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"isStateLevel":true,
						"depedants": [{
							"jsonPath": "abstractEstimates[0].subScheme.code",
							"type": "dropDown",
							"pattern": "/egov-mdms-service/v1/_get?&moduleName=Works&masterName=SubScheme&filter=%5B%3F%28%40.schemeid%3D%3D'{abstractEstimates[0].scheme.code}'%29%5D|$..code|$..name"
						}]
					},
					{
						"name": "subScheme",
						"jsonPath": "abstractEstimates[0].subScheme.code",
						"label": "works.create.groups.fields.subScheme",
						"type": "singleValueList",
						"isRequired": false,
						"isDisabled": false,
						"isStateLevel":true
					},
					{
						"name": "fundSanctioningAuthority",
						"jsonPath": "abstractEstimates[0].fundSanctioningAuthority",
						"label": "works.create.groups.fields.fundSanctioningAuthority",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"maxLength": 100,
						"minLength": 1
					},
					{
						"name": "fundAvailable",
						"jsonPath": "abstractEstimates[0].fundAvailable",
						"label": "works.create.groups.fields.fundAvailable",
						"pattern": "",
						"type": "singleValueList",
						"defaultValue":[
							{key: null, value: "-- Please Select --"},
							{
									"key": true,
									"value": "Yes"
							},
							{
									"key": false,
									"value": "No"
							}
						],
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
			  ]
			}
		]
	},
	"works.search":{
		"numCols": 4,
    "useTimestamp": true,
		"title":"works.create.searchEstimate",
    "objectName": "abstractEstimates",
    "url": "works-estimate/v1/abstractestimates/_search",
		"groups": [
      {
				"name": "searchEstimate",
        "label": "works.create.searchEstimate",
				"fields": [
					{
            "name": "adminSanctionNumbers",
            "jsonPath": "adminSanctionNumbers",
            "label": "works.create.groups.fields.administrativesanction",
            "type": "autoCompelete",
            "url":"works-estimate/v1/abstractestimates/_search?&|$..adminSanctionNumber|$..adminSanctionNumber",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
					{
						"name": "departmentCodes",
						"jsonPath": "departmentCodes",
						"label": "works.create.groups.fields.department",
						"pattern": "",
						"type": "multiValueList",
						"url": "/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Department|$..code|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "adminSanctionFromDate",
						"jsonPath": "adminSanctionFromDate",
						"label": "works.create.groups.fields.adminSanctionFromDate",
						"pattern": "",
						"type": "datePicker",
						"url": "",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "Please enter valid date",
						"patternErrMsg": ""
					},
					{
						"name": "adminSanctionToDate",
						"jsonPath": "adminSanctionToDate",
						"label": "works.create.groups.fields.adminSanctionToDate",
						"pattern": "",
						"type": "datePicker",
						"url": "",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "Please enter valid date",
						"patternErrMsg": ""
					},
					{
						"name": "fundCodes",
						"jsonPath": "fundCodes",
						"label": "works.create.groups.fields.fundCodes",
						"pattern": "",
						"type": "multiValueList",
						"url": "/egov-mdms-service/v1/_get?&moduleName=egf-master&masterName=funds|$..code|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "functionCodes",
						"jsonPath": "functionCodes",
						"label": "works.create.groups.fields.functionCodes",
						"pattern": "",
						"type": "multiValueList",
						"url": "/egov-mdms-service/v1/_get?&moduleName=egf-master&masterName=Function|$..code|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "budgetHeadCodes",
						"jsonPath": "budgetHeadCodes",
						"label": "works.create.groups.fields.objectCode",
						"pattern": "",
						"type": "multiValueList",
						"url": "/egov-mdms-service/v1/_get?&moduleName=Works&masterName=BudgetGroup|$..id|$..name",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
            "name": "spillOverFlag",
            "jsonPath": "spillOverFlag",
            "label": "works.create.groups.fields.spillOverFlag",
            "type": "checkbox",
            "defaultValue":false,
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
					{
						"name": "statuses",
						"jsonPath": "statuses",
						"label": "works.create.groups.fields.statuses",
						"pattern": "",
						"type": "multiValueList",
						"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=WorksStatus&filter=%5B%3F%28%40.moduletype%3D%3D'AbstractEstimate'%29%5D|$..code|$..description",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
            "name": "abstractEstimateNumbers",
						"onLoad":false,
            "jsonPath": "abstractEstimateNumbers",
            "label": "works.create.groups.fields.abstractEstimateNumbers",
            "type": "autoCompelete",
            "url":"works-estimate/v1/abstractestimates/_search?abstractEstimateNumbers=&|$..abstractEstimateNumber|$..abstractEstimateNumber",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
					{
            "name": "workIdentificationNumbers",
            "jsonPath": "workIdentificationNumbers",
            "label": "works.create.groups.fields.workIdentificationNumbers",
            "type": "autoCompelete",
            "url":"works-estimate/v1/abstractestimates/_search?&|$..projectCode.code|$..projectCode.code",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
					{
            "name": "nameOfWork",
            "jsonPath": "nameOfWork",
            "label": "works.create.groups.label.nameofthework",
            "type": "textarea",
            "url":"",
            "isRequired": false,
            "isDisabled": false,
            "patternErrorMsg": ""
          },
					{
						"name": "employees",
						"jsonPath": "employees",
						"label": "works.create.groups.fields.employee",
						"pattern": "",
						"type": "singleValueList",
						"url": "/hr-employee/employees/_search?&|$..assignments[*].position|$..name",
						"hide":true,
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					}
				]
			}
		],
		"result": {
      "header": [
        {
          "label": "works.create.groups.fields.department"
        }, {
          "label": "works.create.groups.fields.abstractEstimateNumbers"
        }, {
          "label": "works.create.groups.fields.administrativesanction"
        }, {
          "label": "works.create.groups.fields.fundCodes"
        }, {
          "label": "works.create.groups.fields.functionCodes"
        }, {
          "label": "works.create.groups.fields.budgetHeadCodes"
        }, {
          "label": "works.create.groups.fields.createdby"
        }, {
          "label": "works.create.groups.fields.owner"
        }, {
          "label": "works.create.groups.fields.statuses"
        }, {
          "label": "works.create.groups.fields.totalamount"
        }
      ],
      "values": [
				{
					 path:"department.code",
					 valExp:`getValFromDropdownData('departmentCodes', _.get(values[i], specsValuesList[j].path), 'value')`
				},
				"abstractEstimateNumber",
				"adminSanctionNumber",
				{
					 path:"fund.code",
					 valExp:`getValFromDropdownData('fundCodes', _.get(values[i], specsValuesList[j].path), 'value')`
				},
				{
					 path:"function.code",
					 valExp:`getValFromDropdownData('functionCodes', _.get(values[i], specsValuesList[j].path), 'value')`
				},
				{
					 path:"budgetGroup.name",
					 valExp:`getValFromDropdownData('budgetHeadCodes', _.get(values[i], specsValuesList[j].path), 'value')`
				},
				"auditDetails.createdBy",
				{
					 path:"workFlowDetails.assignee",
					 valExp:`getValFromDropdownData('employees', _.get(values[i], specsValuesList[j].path), 'value')`
				},
				{
					 path:"status",
					 valExp:`getValFromDropdownData('statuses', _.get(values[i], specsValuesList[j].path), 'value')`
				},
				"estimateAmount"
      ],
      "resultPath": "abstractEstimates",
      "resultIdKey":"abstractEstimateNumber",
      "rowClickUrlView": "/non-framework/works/transaction/viewAbstractEstimate/{abstractEstimateNumber}",
    }
	},
	"works.view": {
		"numCols": 4,
    "useTimestamp": true,
    "objectName": "abstractEstimates",
		"url":"works-estimate/v1/abstractestimates/_search?abstractEstimateNumbers={id}",
		"tenantIdRequired": true,
		"groups": [
			{
				"label": "works.create.groups.label.estandadminsanction",
				"name": "Abstract Estimate",
				"fields": [
						{
							"name": "abstractEstimateNumber",
							"jsonPath": "abstractEstimates[0].abstractEstimateNumber",
							"label": "works.create.groups.fields.abstractEstimateNumber",
							"type": "text",
							"isRequired": true,
							"isDisabled": true,
							"requiredErrMsg": "",
							"patternErrMsg": "",
							"maxLength": 50,
							"minLength": 1
						},
            {
              "name": "dateOfProposal",
              "jsonPath": "abstractEstimates[0].dateOfProposal",
              "label": "works.create.groups.fields.dateOfProposal",
              "pattern": "",
              "type": "datePicker",
              "url": "",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "Please enter valid date",
              "patternErrMsg": ""
            },
						{
              "name": "department",
              "jsonPath": "abstractEstimates[0].department.code",
              "label": "works.create.groups.fields.department",
              "pattern": "",
              "type": "singleValueList",
							"url": "/egov-mdms-service/v1/_get?&moduleName=common-masters&masterName=Department|$..code|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "referenceType",
              "jsonPath": "abstractEstimates[0].referenceType.code",
              "label": "works.create.groups.fields.referenceType",
              "pattern": "",
              "type": "singleValueList",
							"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=ReferenceType|$..code|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "referenceNumber",
              "jsonPath": "abstractEstimates[0].referenceNumber",
              "label": "works.create.groups.fields.referenceNumber",
              "pattern": "",
              "type": "text",
              "url": "",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
							"name": "subject",
							"jsonPath": "abstractEstimates[0].subject",
							"label": "works.create.groups.fields.subject",
							"pattern": "",
							"type": "textarea",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
							"name": "description",
							"jsonPath": "abstractEstimates[0].description",
							"label": "works.create.groups.fields.description",
							"pattern": "",
							"type": "textarea",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
              "name": "natureOfWork",
              "jsonPath": "abstractEstimates[0].natureOfWork.code",
              "label": "works.create.groups.fields.natureOfWork",
              "pattern": "",
              "type": "singleValueList",
							"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=NatureOfWork|$..code|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"showHideFields": [
								{
									"ifValue": "New",
									"hide": [
										{
												"name": "Asset Details",
												"isGroup": true,
												"isField": false
										}
									],
									"show": [{
											"name": "Assets - Land",
											"isGroup": true,
											"isField": false
									}
								]},
								{
									"ifValue": "Addition",
									"show": [
										{
												"name": "Asset Details",
												"isGroup": true,
												"isField": false
										}
									],
									"hide": [{
											"name": "Assets - Land",
											"isGroup": true,
											"isField": false
									}
								]},
								{
									"ifValue": "Repairs",
									"show": [
										{
												"name": "Asset Details",
												"isGroup": true,
												"isField": false
										}
									],
									"hide": [{
											"name": "Assets - Land",
											"isGroup": true,
											"isField": false
									}
								]},
							]
            },
						{
              "name": "modeOfAllotment",
              "jsonPath": "abstractEstimates[0].modeOfAllotment.code",
              "label": "works.create.groups.fields.modeOfAllotment",
              "pattern": "",
              "type": "singleValueList",
              "url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=ModeOfAllotment|$..code|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "typeOfWork",
              "jsonPath": "abstractEstimates[0].typeOfWork.code",
              "label": "works.create.groups.fields.typeOfWork",
              "pattern": "",
              "type": "singleValueList",
              "url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=TypeOfWork&parent=null|$..code|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"depedants": [{
	              "jsonPath": "abstractEstimates[0].subTypeOfWork.code",
	              "type": "dropDown",
								"pattern": "/egov-mdms-service/v1/_get?&moduleName=Works&masterName=TypeOfWork&filter=%5B%3F%28%40.parent%3D%3D'{abstractEstimates[0].typeOfWork.code}'%29%5D|$..code|$..name"
	            }]
            },
						{
              "name": "subTypeOfWork",
              "jsonPath": "abstractEstimates[0].subTypeOfWork.code",
              "label": "works.create.groups.fields.subTypeOfWork",
              "pattern": "",
              "type": "singleValueList",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "workCategory",
              "jsonPath": "abstractEstimates[0].workCategory",
              "label": "works.create.groups.fields.workCategory",
              "pattern": "",
              "type": "singleValueList",
              "url": "",
							"defaultValue":[
								{key: null, value: "-- Please Select --"},
								{
                    "key": "NON_SLUM",
                    "value": "NON SLUM"
                },
								{
                    "key": "NOTIFIED_SLUM",
                    "value": "NOTIFIED SLUM"
                },
								{
                    "key": "NON_NOTIFIED_SLUM",
                    "value": "NON NOTIFIED SLUM"
                },
							],
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "beneficiary",
              "jsonPath": "abstractEstimates[0].beneficiary",
              "label": "works.create.groups.fields.beneficiary",
              "pattern": "",
              "type": "singleValueList",
              "url": "",
							"defaultValue":[
								{key: null, value: "-- Please Select --"},
								{
                    "key": "SC",
                    "value": "SC"
                },
								{
                    "key": "ST",
                    "value": "ST"
                },
								{
                    "key": "BC",
                    "value": "BC"
                },
								{
                    "key": "MINORITY",
                    "value": "MINORITY"
                },
								{
                    "key": "WOMEN_CHILDREN_WELFARE",
                    "value": "WOMEN CHILDREN WELFARE"
                },
								{
                    "key": "GENERAL",
                    "value": "GENERAL"
                },
							],
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "locality",
              "jsonPath": "abstractEstimates[0].locality.code",
              "label": "works.create.groups.fields.locality",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?&boundaryTypeName=LOCALITY&hierarchyTypeName=LOCATION|$..id|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "ward",
              "jsonPath": "abstractEstimates[0].ward.code",
              "label": "works.create.groups.fields.ward",
              "pattern": "",
              "type": "singleValueList",
              "url": "/egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?tenantId=default&boundaryTypeName=Ward&hierarchyTypeName=ADMINISTRATION|$..id|$..name",
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
              "name": "workProposedAsPerDP",
              "jsonPath": "abstractEstimates[0].workProposedAsPerDP",
              "label": "works.create.groups.fields.workProposedAsPerDP",
              "pattern": "",
              "type": "singleValueList",
              "url": "",
							"defaultValue":[
								{key: null, value: "-- Please Select --"},
								{
                    "key": true,
                    "value": "Yes"
                },
								{
                    "key": false,
                    "value": "No"
                }
							],
              "isRequired": true,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
							"name": "dpRemarks",
							"jsonPath": "abstractEstimates[0].dpRemarks",
							"label": "works.create.groups.fields.dpRemarks",
							"pattern": "",
							"type": "textarea",
							"isRequired": true,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						},
						{
              "name": "pmcRequired",
              "jsonPath": "abstractEstimates[0].pmcRequired",
              "label": "works.create.groups.fields.pmcRequired",
              "pattern": "",
              "type": "singleValueList",
              "url": "",
							"defaultValue":[
								{key: null, value: "-- Please Select --"},
								{
                    "key": true,
                    "value": "Yes"
                },
								{
                    "key": false,
                    "value": "No"
                }
							],
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"showHideFields": [
								{
									"ifValue": "true",
									"hide": [],
									"show": [
										{
												"name": "pmcType",
												"isGroup": false,
												"isField": true
										}
									]
								}]
            },
						{
              "name": "pmcType",
              "jsonPath": "abstractEstimates[0].pmcType",
              "label": "works.create.groups.fields.pmcType",
              "pattern": "",
							"hide":true,
              "type": "singleValueList",
              "url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=PMCType|$..id|$..name",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": "",
							"showHideFields": [
								{
									"ifValue": "1",
									"hide": [],
									"show": [
										{
												"name": "pmcName",
												"isGroup": false,
												"isField": true
										}
									]
								}]
            },
						{
              "name": "pmcName",
              "jsonPath": "abstractEstimates[0].pmcName",
							"hide":true,
              "label": "works.create.groups.fields.pmcName",
              "pattern": "",
              "type": "singleValueList",
              "url": "/works-masters/v1/contractors/_search?&pmc=true|$.contractors[*].code|$.contractors[*].name",
              "isRequired": false,
              "isDisabled": false,
              "requiredErrMsg": "",
              "patternErrMsg": ""
            },
						{
							"name": "implementationPeriod",
							"jsonPath": "abstractEstimates[0].implementationPeriod",
							"label": "works.create.groups.fields.implementationPeriod",
							"pattern": "",
							"type": "text",
							"isRequired": false,
							"isDisabled": false,
							"requiredErrMsg": "",
							"patternErrMsg": ""
						}
				]
			},
			{
				"label": "works.create.groups.label.assetdetails",
				"name": "Asset Details",
				"hide":true,
				"fields":[
					{
						"name": "code",
						"jsonPath": "abstractEstimates[0].assetDetails[0].asset.code",
						"label": "works.create.groups.fields.assetCode",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "assetCondition",
						"jsonPath": "abstractEstimates[0].assetDetails[0].assetCondition.name",
						"label": "works.create.groups.fields.assetCondition",
						"pattern": "",
						"type": "singleValueList",
						"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=AssetPresentCondition|$..id|$..name",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
						"name": "assetRemarks",
						"jsonPath": "abstractEstimates[0].assetDetails[0].assetRemarks",
						"label": "works.create.groups.fields.assetRemarks",
						"pattern": "",
						"type": "textarea",
						"isRequired": true,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
				]
			},
			{
				"label": "works.create.groups.label.asssetsland",
				"name": "Assets - Land",
				"hide":true,
				"fields":[
					{
						"name": "landAssetRequired",
						"jsonPath": "abstractEstimates[0].landAssetRequired",
						"label": "works.create.groups.fields.landAssetRequired",
						"pattern": "",
						"type": "singleValueList",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": "",
						"defaultValue":[
							{key: null, value: "-- Please Select --"},
							{
									"key": true,
									"value": "Yes"
							},
							{
									"key": false,
									"value": "No"
							}
						]
					},
					{
						"name": "noOfLands",
						"jsonPath": "abstractEstimates[0].noOfLands",
						"label": "works.create.groups.fields.noOfLands",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": false,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
					{
			      "type": "tableList",
			      "jsonPath": "abstractEstimates[0].assetDetails",
			      "tableList": {
			        "header": [
								{
				          "label": "works.create.groups.label.landassetId"
				        },
								{
				          "label": "works.create.groups.label.natureofownership"
				        },
								{
				          "label": "works.create.groups.label.arealand"
				        },
								{
				          "label": "works.create.groups.label.plotno"
				        },
								{
				          "label": "works.create.groups.label.surveyno"
				        },
								{
				          "label": "works.create.groups.label.landAssetCondition"
				        }, {
				          "label": "works.create.groups.label.constructionArea"
			        }],
							"actionsNotRequired":true,
			        "values": [
								{
				          "name": "landassetId",
				          "pattern": "",
				          "type": "text",
				          "jsonPath": "abstractEstimates[0].assetDetails[0].landassetId",
				          "isRequired": false,
				          "isDisabled": true
				        },
								{
				          "name": "natureofownership",
				          "pattern": "",
				          "type": "text",
				          "jsonPath": "abstractEstimates[0].assetDetails[0].natureofownership",
				          "isDisabled": true
				        },
								{
				          "name": "arealand",
				          "pattern": "",
				          "type": "text",
				          "jsonPath": "abstractEstimates[0].assetDetails[0].arealand",
				          "isDisabled": true
				        },
								{
				          "name": "plotno",
				          "pattern": "",
				          "type": "text",
				          "jsonPath": "abstractEstimates[0].assetDetails[0].plotno",
				          "isDisabled": true
				        },
								{
				          "name": "surveyno",
				          "pattern": "",
				          "type": "text",
				          "jsonPath": "abstractEstimates[0].assetDetails[0].surveyno",
				          "isDisabled": true
				        },
								{
			          "name": "landAssetCondition",
			          "pattern": "",
			          "type": "singleValueList",
								"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=LandAssetPresentCondition|$..id|$..name",
			          "jsonPath": "abstractEstimates[0].assetDetails[0].landAssetCondition.name",
			          "isRequired": false,
			          "isDisabled": true,
								"maxLength":1024
			        }, {
			          "name": "constructionArea",
			          "pattern": "",
			          "type": "text",
			          "jsonPath": "abstractEstimates[0].assetDetails[0].constructionArea",
			          "isRequired": false,
			          "isDisabled": true
			        }]
			      }
			    },
					{
						"name": "remarks",
						"jsonPath": "abstractEstimates[0].assetDetails[0].assetRemarks",
						"label": "works.create.groups.fields.remarks",
						"pattern": "",
						"type": "text",
						"isRequired": false,
						"isDisabled": true,
						"requiredErrMsg": "",
						"patternErrMsg": ""
					},
				]
			},
			{
			  "label": "works.create.groups.label.workdetails",
			  "name": "Work Details",
			  "fields":[
			    {
			      "type": "tableList",
			      "jsonPath": "abstractEstimates[0].abstractEstimateDetails",
			      "tableList": {
			        "header": [{
			          "label": "works.create.groups.label.nameofthework"
			        }, {
			          "label": "works.create.groups.label.estimateAmount"
			        }],
			        "values": [{
			          "name": "nameofthework",
			          "pattern": "",
			          "type": "textarea",
			          "jsonPath": "abstractEstimates[0].abstractEstimateDetails[0].nameOfWork",
			          "isRequired": true,
			          "isDisabled": true
			        }, {
			          "name": "estimateAmount",
			          "pattern": "",
			          "type": "text",
			          "jsonPath": "abstractEstimates[0].abstractEstimateDetails[0].estimateAmount",
			          "isRequired": true,
			          "textAlign":'right',
			          "isDisabled": true
			        }],
							"actionsNotRequired":true,
			        "hasFooter":true,
			        "footer":[
			          {1:"abstractEstimates[*].abstractEstimateDetails[*].estimateAmount"}
			        ]
			      }
			    }
			  ]
			},
			{
			  "label": "works.create.groups.label.sanctionDetails",
			  "name": "Sanction Details",
			  "fields":[
			    {
			      "type": "tableList",
			      "jsonPath": "abstractEstimates[0].sanctionDetails",
			      "tableList": {
			        "header": [{
			          "label": "works.create.groups.fields.sanctionType"
			        }, {
			          "label": "works.create.groups.fields.sanctionAuthority"
			        }],
			        "values": [{
			          "name": "sanctionType",
			          "pattern": "",
			          "type": "singleValueList",
			          "jsonPath": "abstractEstimates[0].sanctionDetails[0].sanctionType",
			          "isRequired": true,
			          "isDisabled": true,
								"defaultValue":[
									{key: null, value: "-- Please Select --"},
						      {"key":'FINANCIAL_SANCTION',"value":"FINANCIAL SANCTION"},
						      {"key":'ADMINISTRATIVE_SANCTION',"value":'ADMINISTRATIVE SANCTION'},
						      {"key":'TECHNICAL_SANCTION',"value":'TECHNICAL SANCTION'}]
			        }, {
			          "name": "sanctionAuthority",
			          "pattern": "",
			          "type": "singleValueList",
			          "jsonPath": "abstractEstimates[0].sanctionDetails[0].sanctionAuthority.name",
			          "isRequired": true,
			          "isDisabled": true,
								"url":"/egov-mdms-service/v1/_get?&moduleName=Works&masterName=EstimateSanctionAuthority|$..id|$..name"
			        }],
							"actionsNotRequired":true
			      }
			    }
			  ]
			},
			{
				"label": "works.create.groups.label.uploadDocs",
				"name": "Documents",
				"fields":[
					{
						"name": "File",
						"type": "viewDocuments",
						"url":"/works-services/v1/documentdetails/_search?&objectIds={abstractEstimates[0].abstractEstimateNumber}",
						"jsonPath": "abstractEstimates[0].documents",
						"maxFile":5,
						"addRequired":false
					}
				]
			}
		]
	}
}

export default dat;
