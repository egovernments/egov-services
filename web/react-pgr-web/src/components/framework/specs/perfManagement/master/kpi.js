var dat = {
    "perfManagement.create": {
        "numCols": 12 / 2,
        "url": "perfmanagement/v1/kpimaster/_create",
        "useTimestamp": true,
        "objectName": "KPIs",
        "groups": [{
                "label": "perfManagement.create.KPIs.groups.kpiMaster",
                "name": "kpiMaster",
                "fields": [
                    //  {
                    //   "name": "kpiTenant",
                    //   "jsonPath": "KPIs[0].tenantIdCustom",
                    //   "label": "Tenant",
                    //   "pattern": "",
                    //   "type": "singleValueList",
                    //   "url": "/tenant/v1/tenant/_search?tenantId=|$.tenant.*.code|$.tenant.*.name",
                    //   "isDisabled": false,
                    //   "requiredErrMsg": "",
                    //   "depedants": [{
                    //     "jsonPath": "KPIs[0].departmentId",
                    //     "type": "dropDown",
                    //     "pattern": "egov-mdms-service/v1/_get?tenantId=default&tenantIdCustom={KPIs[0].tenantIdCustom}&moduleName=common-masters&masterName=Department|$..id|$..name"
                    //   }]
                    // },
                    {
                        "name": "kpiDepartment",
                        "jsonPath": "KPIs[0].departmentId",
                        "label": "perfManagement.create.KPIs.groups.kpiDepartment",
                        "isRequired": true,
                        "pattern": "",
                        "type": "singleValueList",
                        "url": "egov-mdms-service/v1/_get?tenantId=default&moduleName=common-masters&masterName=Department|$..id|$..name",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    }, {
                        "name": "kpiDate",
                        "jsonPath": "KPIs[0].financialYear",
                        "label": "perfManagement.create.KPIs.groups.kpiDate",
                        "isRequired": true,
                        "pattern": "",
                        "type": "singleValueList",
                        "url": "egf-master/financialyears/_search?tenantId=default|$.financialYears.*.finYearRange|$.financialYears.*.finYearRange",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    }
                ]
            },
            {
                "label": "",
                "name": "kpiNameBlock",
                "fields": [{
                        "name": "kpiName",
                        "jsonPath": "KPIs[0].name",
                        "label": "perfManagement.create.KPIs.groups.kpiName",
                        "isRequired": true,
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    }, {
                        "name": "kpiCode",
                        "jsonPath": "KPIs[0].code",
                        "label": "perfManagement.create.KPIs.groups.kpiCode",
                        "isRequired": true,
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    },
                    {
                        "name": "kpitype",
                        "jsonPath": "KPIs[0].targetType",
                        "label": "perfManagement.create.KPIs.groups.kpitype",
                        "pattern": "",
                        "type": "radio",
                        "isRequired": false,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": "",
                        "values": [{
                                "label": "perfManagement.update.KPIs.groups.updatekpitype.text",
                                "value": "TEXT"
                                  },{
                                  "label": "perfManagement.create.KPIs.groups.kpitype.value",
                                  "value": "VALUE"
                              }, {
                                  "label": "perfManagement.create.KPIs.groups.kpitype.objective",
                                  "value": 'OBJECTIVE'
                              }],
                        "defaultValue": "TEXT",
                        "showHideFields": [{
                            "ifValue": "OBJECTIVE",

                            "hide": [{
                                "name": "kpiTargetBlock",
                                "isGroup": true,
                                "isField": false
                            },
                            {
                                "name": "kpiTargetTextBlock",
                                "isGroup": true,
                                "isField": false
                            }
                          ],
                            "show": [{
                                "name": "kpiTargetRadioBlock",
                                "isGroup": true,
                                "isField": false
                            }]
                        },
                        {
                            "ifValue": "VALUE",
                            "hide": [{
                                "name": "kpiTargetRadioBlock",
                                "isGroup": true,
                                "isField": false
                            },
                            {
                                "name": "kpiTargetTextBlock",
                                "isGroup": true,
                                "isField": false
                            }
                          ],
                            "show": [{
                                "name": "kpiTargetBlock",
                                "isGroup": true,
                                "isField": false
                            }]
                        },
                        {
                            "ifValue": "TEXT",
                            "hide": [{
                                "name": "kpiTargetRadioBlock",
                                "isGroup": true,
                                "isField": false
                            },
                            {
                                "name": "kpiTargetBlock",
                                "isGroup": true,
                                "isField": false
                            }
                          ],
                            "show": [{
                                "name": "kpiTargetTextBlock",
                                "isGroup": true,
                                "isField": false
                            }]
                        }
                      ]
                    }
                ]
            },
            {
                "label": "Text",
                "name": "kpiTargetTextBlock",
                "hide": false,
                "multiple": false,
                "fields": [{
                    "name": "kpiTargetText",
                    "jsonPath": "KPIs[0].targetDescription",
                    "label": "",
                    "pattern": "",
                    "type": "text",
                    "isDisabled": false,
                    "requiredErrMsg": ""
                }]
            },
            {
                "label": "value",
                "name": "kpiTargetBlock",
                "hide": true,
                "multiple": false,
                "fields": [{
                    "name": "kpiTarget",
                    //"hide":false,
                    "jsonPath": "KPIs[0].targetValue",
                    "label": "",
                    "pattern": "[0-9]",
                    "type": "text",
                    "isDisabled": false,
                    "patternErrMsg": "Please enter a valid number",
                    "requiredErrMsg": ""
                }]
            },
            {
                "label": "perfManagement.create.KPIs.groups.kpiTargetRadioBlock",
                "name": "kpiTargetRadioBlock",
                "hide": true,
                "multiple": false,
                "fields": [{
                    "name": "kpiTargetRadio",
                    //"hide":true,
                    "jsonPath": "KPIs[0].targetValue",
                    "label": "",
                    "pattern": "",
                    "type": "radio",
                    "isRequired": false,
                    "isDisabled": false,
                    "requiredErrMsg": "",
                    "patternErrMsg": "",
                    "values": [{
                        "label": "perfManagement.create.KPIs.groups.kpiTargetRadioBlock.yes",
                        "value": 1
                    }, {
                        "label": "perfManagement.create.KPIs.groups.kpiTargetRadioBlock.no",
                        "value": 2
                    }, {
                        "label": "perfManagement.create.KPIs.groups.kpiTargetRadioBlock.inprogress",
                        "value": 3
                    }]
                }]
            },
            {
                "label": "perfManagement.create.KPIs.groups.kpiInstruct",
                "name": "kpiInstruct",
                "fields": [{
                    "name": "kpiInstruction",
                    "jsonPath": "KPIs[0].instructions",
                    "label": "",
                    "pattern": "",
                    "type": "textarea",
                    "fullWidth": true,
                    "isDisabled": false,
                    "requiredErrMsg": ""
                }]
            },
            {
                "label": "perfManagement.create.KPIs.groups.kpiDoc",
                "name": "kpiDoc",
                "multiple": true,
                "jsonPath": "KPIs[0].documentsReq",
                "fields": [{
                        "name": "kpidocName",
                        "jsonPath": "KPIs[0].documentsReq[0].name",
                        "label": "perfManagement.create.KPIs.groups.kpidocName",
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "url": "",
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    },
                    /*{
                        "name": "kpidocCode",
                        "jsonPath": "KPIs[0].documentsReq[0].code",
                        "label": "perfManagement.create.KPIs.groups.kpidocCode",
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": "",
                        "url": ""
                    },*/
                    {
                        "name": "kpidocMandatory",
                        "jsonPath": "KPIs[0].documentsReq[0].active",
                        "label": "perfManagement.create.KPIs.groups.kpidocMandatory",
                        "pattern": "",
                        "type": "checkbox",
                        "defaultValue": false,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": "",
                        "url": ""
                    }
                ]
            }
        ]
    },
    "perfManagement.search": {
        "numCols": 12 / 3,
        "url": "/perfmanagement/v1/kpimaster/_search",
        "useTimestamp": true,
        "objectName": "KPIs.kpiList",
        "groups": [{
            "label": "perfManagement.search.KPIs.groups.kpiSearch",
            "name": "kpiSearch",
            "fields": [{
                    "name": "searchkpiDepartment",
                    "jsonPath": "departmentId",
                    "label": "perfManagement.search.KPIs.groups.searchkpiDepartment",
                    "pattern": "",
                    "type": "singleValueList",
                    "url": "egov-mdms-service/v1/_get?tenantId="+localStorage.tenantId.split(".")[0]+"&moduleName=common-masters&masterName=Department|$..id|$..name",
                    "isDisabled": false,
                    "requiredErrMsg": ""
                },
                {
                    "name": "searchkpiCode",
                    "jsonPath": "kpiCode",
                    "label": "perfManagement.search.KPIs.groups.searchkpiCode",
                    "pattern": "",
                    "type": "text",
                    "isDisabled": false,
                    "requiredErrMsg": ""
                },
                {
                    "name": "searchkpiName",
                    "jsonPath": "kpiName",
                    "label": "perfManagement.search.KPIs.groups.searchkpiName",
                    "pattern": "",
                    "type": "text",
                    "isDisabled": false,
                    "requiredErrMsg": ""
                }
            ]
        }],
        "result": {
            "header": [{ label: "perfManagement.search.KPIs.groups.searchkpiName" }, { label: "perfManagement.search.KPIs.groups.searchkpiCode" }, { label: "perfManagement.search.KPIs.groups.searchkpiTarget" }],
            "values": ["name", "code", "targetDescription"],
            "resultPath": "KPIs",
            "rowClickUrlUpdate": "/update/perfManagement/kpi/{code}",
            "rowClickUrlView": "/view/perfManagement/kpi/{code}"
        }
    },
    "perfManagement.view": {
        "numCols": 12 / 2,
        "url": "/perfmanagement/v1/kpimaster/_search?kpiCode={code}",
        "useTimestamp": true,
        "objectName": "KPIs",
        "groups": [{
                "label": "perfManagement.view.KPIs.groups.viewKPI",
                "name": "viewKPI",
                "fields": [{
                        "name": "viewkpiDepartment",
                        "jsonPath": "KPIs[0].department.name",
                        // "url": "egov-mdms-service/v1/_get?tenantId=default&tenantIdCustom={KPIs[0].tenantId}&moduleName=common-masters&masterName=Department|$..id|$..name",
                        "label": "perfManagement.view.KPIs.groups.viewkpiDepartment",
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    },
                    {
                        "name": "viewkpiDate",
                        "jsonPath": "KPIs[0].financialYear",
                        "label": "perfManagement.view.KPIs.groups.viewkpiDate",
                        "isRequired": true,
                        "pattern": "",
                        "type": "singleValueList",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    }
                ]
            },
            {
                "label": "",
                "name": "viewKpiNameBlock",
                "fields": [{
                        "name": "viewkpiName",
                        "jsonPath": "KPIs[0].name",
                        "label": "perfManagement.view.KPIs.groups.viewkpiName",
                        "isRequired": true,
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    },
                    {
                        "name": "viewkpiCode",
                        "jsonPath": "KPIs[0].code",
                        "label": "perfManagement.view.KPIs.groups.viewkpiCode",
                        "isRequired": true,
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    }/* {
                        "name": "viewkpitype",
                        "jsonPath": "KPIs[0].targetType",
                        "label": "perfManagement.view.KPIs.groups.viewkpitype",
                        "pattern": "",
                        "type": "radio",
                        "isRequired": false,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": "",
                        "defaultValue": false,
                        "values": [{
                            "label": "KPI Value Number",
                            "value": true
                        }, {
                            "label": "KPI Value Objective Type",
                            "value": false
                        }],
                        "showHideFields": [{
                            "ifValue": false,
                            "hide": [{
                                "name": "viewkpiTargetBlock",
                                "isGroup": true,
                                "isField": false
                            }],
                            "show": [{
                                "name": "viewkpiTargetRadioBlock",
                                "isGroup": true,
                                "isField": false
                            }]
                        }]
                    }*/
                ]
            },


            {
                "label": "perfManagement.view.KPIs.groups.viewkpiTargetBlock",
                "name": "viewkpiTargetBlock",
                "hide": false,
                "multiple": false,
                "fields": [{
                    "name": "viewkpiTarget",
                    //"hide": false,
                    "jsonPath": "KPIs[0].targetDescription",
                    "label": "",
                    "pattern": "",
                    "type": "text",
                    "isDisabled": false,
                    "requiredErrMsg": ""
                }]
            },

            {
                "label": "perfManagement.view.KPIs.groups.viewkpiTargetRadioBlock",
                "name": "viewkpiTargetRadioBlock",
                "hide": true,
                "multiple": false,
                "fields": [{
                    "name": "viewkpiTargetRadio",
                    "jsonPath": "KPIs[0].targetDescription",
                    "label": "",
                    "pattern": "",
                    //"type": "radio",
                    "type":"text",
                    "isRequired": false,
                    "isDisabled": false,
                    "requiredErrMsg": "",
                    "patternErrMsg": "",
                }]
            },

            {
                "label": "perfManagement.view.KPIs.groups.viewKpiInstruc",
                "name": "viewKpiInstruc",
                "fields": [{
                    "name": "viewkpiInstruction",
                    "jsonPath": "KPIs[0].instructions",
                    "label": "",
                    "pattern": "",
                    "type": "textarea",
                    "fullWidth": true,
                    "isDisabled": false,
                    "requiredErrMsg": ""
                }]
            },
            {
                "label": "perfManagement.view.KPIs.groups.viewkpiDocuments",
                "name": "viewkpiDocuments",
                "multiple": true,
                //"hide": "`${this.props.getVal('KPIs[0].documentsReq.length')==0?true:false}`",
                "jsonPath": "KPIs[0].documentsReq",
                "fields": [{
                        "name": "viewkpidoc",
                        "jsonPath": "KPIs[0].documentsReq[0].name",
                        "label": "perfManagement.view.KPIs.groups.viewkpidoc",
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "url": "",
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    },
                    {
                        "name": "viewkpidocCode",
                        "jsonPath": "KPIs[0].documentsReq[0].code",
                        "label": "perfManagement.view.KPIs.groups.viewkpidocCode",
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": "",
                        "url": ""
                    },
                    {
                        "name": "viewkpidocMandatory",
                        "jsonPath": "KPIs[0].documentsReq[0].active",
                        "label": "perfManagement.view.KPIs.groups.viewkpidocMandatory",
                        "pattern": "",
                        "type": "checkbox",
                        "defaultValue": false,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": "",
                        "url": ""
                    }
                ]
            }
        ]
    },
    "perfManagement.update": {
        "numCols": 12 / 2,
        "searchUrl": "/perfmanagement/v1/kpimaster/_search?kpiCode={code}",
        "url": "perfmanagement/v1/kpimaster/_update",
        "useTimestamp": true,
        "objectName": "KPIs",
        "groups": [{
                "label": "perfManagement.update.KPIs.groups.updateKpiDepartmentBlock",
                "name": "updateKpiDepartmentBlock",
                "fields": [{
                        "name": "updatekpiDepartment",
                        "jsonPath": "KPIs[0].department.id",
                        "url": "/egov-mdms-service/v1/_get?tenantId=default&moduleName=common-masters&masterName=Department|$..id|$..name",
                        "label": "perfManagement.update.KPIs.groups.updateKpiDepartmentBlock",
                        "pattern": "",
                        "type": "singleValueList",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    },
                    {
                        "name": "updatekpiDate",
                        "jsonPath": "KPIs[0].financialYear",
                        "label": "perfManagement.update.KPIs.groups.updatekpiDate",
                        "isRequired": true,
                        "pattern": "",
                        "type": "singleValueList",
                        "url": "egf-master/financialyears/_search?tenantId=default|$.financialYears.*.finYearRange|$.financialYears.*.finYearRange",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    },
                ]
            },
            {
                "label": "",
                "name": "updateKpi",
                "fields": [{
                        "name": "updatekpiName",
                        "jsonPath": "KPIs[0].name",
                        "label": "perfManagement.update.KPIs.groups.updatekpiName",
                        "isRequired": true,
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    },
                    {
                        "name": "updatekpiCode",
                        "jsonPath": "KPIs[0].code",
                        "label": "perfManagement.update.KPIs.groups.updatekpiCode",
                        "isRequired": true,
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    }, {
                        "name": "updatekpitype",
                        "jsonPath": "KPIs[0].targetType",
                        "label": "perfManagement.update.KPIs.groups.updatekpitype",
                        "pattern": "",
                        "type": "radio",
                        "isRequired": false,
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": "",
                        "defaultValue": true,
                        "values": [{
                              "label": "perfManagement.update.KPIs.groups.updatekpitype.text",
                              "value": "TEXT"
                          },
                          {
                            "label": "perfManagement.update.KPIs.groups.updatekpitype.value",
                            "value": "VALUE"
                        }, {
                            "label": "perfManagement.update.KPIs.groups.updatekpitype.objective",
                            "value": "OBJECTIVE"
                        }],
                        "showHideFields": [{
                                						"ifValue": "OBJECTIVE",

                                						"hide": [{
                                							"name": "updatekpiTargetBlock",
                                							"isGroup": true,
                                							"isField": false
                                						},
                                						{
                                							"name": "updatekpiTargetTextBlock",
                                							"isGroup": true,
                                							"isField": false
                                						}
                                					  ],
                                						"show": [{
                                							"name": "updatekpiTargetRadioBlock",
                                							"isGroup": true,
                                							"isField": false
                                						}]
                                					},
                                					{
                                						"ifValue": "VALUE",
                                						"hide": [{
                                							"name": "updatekpiTargetRadioBlock",
                                							"isGroup": true,
                                							"isField": false
                                						},
                                						{
                                							"name": "updatekpiTargetTextBlock",
                                							"isGroup": true,
                                							"isField": false
                                						}
                                					  ],
                                						"show": [{
                                							"name": "updatekpiTargetBlock",
                                							"isGroup": true,
                                							"isField": false
                                						}]
                                					},
                                					{
                                						"ifValue": "TEXT",
                                						"hide": [{
                                							"name": "updatekpiTargetRadioBlock",
                                							"isGroup": true,
                                							"isField": false
                                						},
                                						{
                                							"name": "updatekpiTargetBlock",
                                							"isGroup": true,
                                							"isField": false
                                						}
                                					  ],
                                						"show": [{
                                							"name": "updatekpiTargetTextBlock",
                                							"isGroup": true,
                                							"isField": false
                                						}]
                                					}
                                				  ]
                    }
                ]
            },
            {
                "label": "perfManagement.update.KPIs.groups.updatekpiTargetTextBlock",
                "name": "updatekpiTargetTextBlock",
                "hide": false,
                "multiple": false,
                "fields": [{
                    "name": "updatekpiTarget",
                    "jsonPath": "KPIs[0].targetDescription",
                    "label": "",
                    "pattern": "",
                    "type": "text",
                    "isDisabled": false,
                    "requiredErrMsg": ""
                }]
            },

            {
                "label": "perfManagement.update.KPIs.groups.updatekpiTargetBlock",
                "name": "updatekpiTargetBlock",
                "hide": false,
                "multiple": false,
                "fields": [{
                    "name": "updatekpiTargetText",
                    "jsonPath": "KPIs[0].targetValue",
                    "label": "",
                    "pattern": "",
                    "type": "text",
                    "isDisabled": false,
                    "requiredErrMsg": ""
                }]
            },
            {
                "label": "perfManagement.update.KPIs.groups.updatekpiTargetRadioBlock",
                "name": "updatekpiTargetRadioBlock",
                "hide":true,
                //"hide": "`${getVal('KPIs[0].targetType')}`",
                "multiple": false,
                "fields": [{
                    "name": "updatekpiTargetRadio",
                    "jsonPath": "KPIs[0].targetValue",
                    "label": "",
                    "pattern": "",
                    "type": "radio",
                    "isRequired": false,
                    "isDisabled": false,
                    "requiredErrMsg": "",
                    "patternErrMsg": "",
                    "values": [{
                        "label": "perfManagement.update.KPIs.groups.updatekpiTargetBlock.yes",
                        "value": 1
                    }, {
                        "label": "perfManagement.update.KPIs.groups.updatekpiTargetBlock.no",
                        "value": 2
                    }, {
                        "label": "perfManagement.update.KPIs.groups.updatekpiTargetBlock.inprogress",
                        "value": 3
                    }]
                }]
            },
            {
                "name": "updateKpiInstruc",
                "label": "perfManagement.update.KPIs.groups.updatekpidocMandatory",
                "name": "updateKpiInstruc",
                "fields": [{
                    "name": "updatekpiInstruction",
                    "jsonPath": "KPIs[0].instructions",
                    "label": "",
                    "pattern": "",
                    "type": "textarea",
                    "fullWidth": true,
                    "isDisabled": false,
                    "requiredErrMsg": ""
                }]
            },
            {
                "label": "perfManagement.update.KPIs.groups.updatekpiDocuments",
                "name": "updatekpiDocuments",
                "multiple": true,
                "jsonPath": "KPIs[0].documentsReq",
                "fields": [{
                        "name": "updatekpidoc",
                        "jsonPath": "KPIs[0].documentsReq[0].name",
                        "label": "perfManagement.update.KPIs.groups.updatekpidoc",
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "url": "",
                        "requiredErrMsg": "",
                        "patternErrMsg": ""
                    },
                    {
                        "name": "updatekpidocMandatory",
                        "jsonPath": "KPIs[0].documentsReq[0].active",
                        "label": "perfManagement.update.KPIs.groups.updatekpidocMandatory",
                        "pattern": "",
                        "type": "checkbox",
                        "isDisabled": false,
                        "requiredErrMsg": "",
                        "patternErrMsg": "",
                        "url": ""
                    }
                ]
            }
        ]
    }
}
export default dat;
