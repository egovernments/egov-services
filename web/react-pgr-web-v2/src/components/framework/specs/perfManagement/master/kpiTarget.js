var dat = {
    "perfManagement.create": {
        "numCols": 12 / 2,
        "url": "perfmanagement/v1/kpimaster/_create",
        "useTimestamp": true,
        "objectName": "kpiTargets",
        "groups": [{
                "label": "perfManagement.create.KPIs.groups.kpiMaster",
                "name": "kpiMaster",
                "fields": [
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
            
        ]
    },
    "perfManagement.search": {
        "numCols": 12 / 2,
        "url": "perfmanagement/v1/kpitarget/_search",
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
                    "isRequired" : true,
                    "requiredErrMsg": ""
                },
                {
                    "name": "searchkpiCode",
                    "jsonPath": "kpiCodes",
                    "label": "perfManagement.search.KPIs.groups.searchkpiCode",
                    "pattern": "",
                    "type": "text",
                    "isDisabled": false,
                    "requiredErrMsg": ""
                }
            ]
        }],
        "result": {
            "header": [ { label: "perfManagement.search.KPIs.groups.searchkpiCode" }, { label: "perfManagement.search.KPIs.groups.searchkpiTarget" }],
            "values": ["kpiCode", "targetValue"],
            "resultPath": "kpiTargets",
            "rowClickUrlUpdate": "/update/perfManagement/kpiTarget/{kpiCode}",
            "rowClickUrlView": "/view/perfManagement/kpiTarget/{kpiCode}"
        }
    },
    "perfManagement.view": {
        "numCols": 12 / 2,
        "url": "/perfmanagement/v1/kpitarget/_search?kpiCodes={kpiCode}",
        "useTimestamp": true,
        "objectName": "KPIs",
        "groups": [{
                "label": "perfManagement.view.KPIs.groups.viewKPI",
                "name": "viewKPI",
                "fields": [{
                        "name": "viewkpiDepartment",
                        "jsonPath": "kpiTargets[0].kpi.department",
                        // "url": "egov-mdms-service/v1/_get?tenantId=default&tenantIdCustom={KPIs[0].tenantId}&moduleName=common-masters&masterName=Department|$..id|$..name",
                        "label": "perfManagement.view.KPIs.groups.viewkpiDepartment",
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    },
                    {
                        "name": "viewkpiDate",
                        "jsonPath": "kpiTargets[0].kpi.financialYear",
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
                        "jsonPath": "kpiTargets[0].kpi.name",
                        "label": "perfManagement.view.KPIs.groups.viewkpiName",
                        "isRequired": true,
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    },
                    {
                        "name": "viewkpiCode",
                        "jsonPath": "kpiTargets[0].kpi.code",
                        "label": "perfManagement.view.KPIs.groups.viewkpiCode",
                        "isRequired": true,
                        "pattern": "",
                        "type": "text",
                        "isDisabled": false,
                        "requiredErrMsg": ""
                    }
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
                    "jsonPath": "kpiTargets[0].targetValue",
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
                    "jsonPath": "kpiTargets[0].targetValue",
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
        ]
    },
    "perfManagement.update": {
        "numCols": 12 / 2,
        "searchUrl": "perfmanagement/v1/kpitarget/_search?kpiCodes={kpiCode}",
        "url": "/perfmanagement/v1/kpitarget/_update",
        "useTimestamp": true,
        "objectName": "kpiTargets",
        "groups": [
                {
                "label": "",
                "name": "updateKpi",
                "fields": [{
                        "name": "updatekpiName",
                        "jsonPath": "kpiTargets[0].kpi.name",
                        "label": "perfManagement.update.KPIs.groups.updatekpiName",
                        "isRequired": true,
                        "pattern": "",
                        "type": "text",
                        "isDisabled": true,
                        "requiredErrMsg": ""
                    },
                    {
                        "name": "updatekpiCode",
                        "jsonPath": "kpiTargets[0].kpiCode",
                        "label": "perfManagement.update.KPIs.groups.updatekpiCode",
                        "isRequired": true,
                        "pattern": "",
                        "type": "text",
                        "isDisabled": true,
                        "requiredErrMsg": ""
                    }, {
                        "name": "updatekpitype",
                        "jsonPath": "kpiTargets[0].kpi.targetType",
                        "label": "perfManagement.update.KPIs.groups.updatekpitype",
                        "pattern": "",
                        "type": "radio",
                        "isRequired": false,
                        "isDisabled": true,
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
                    "jsonPath": "kpiTargets[0].targetDescription",
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
                    "jsonPath": "kpiTargets[0].targetValue",
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
                    "jsonPath": "kpiTargets[0].targetValue",
                    "label": "",
                    "pattern": "",
                    "type": "radio",
                    "isRequired": false,
                    "isDisabled": false,
                    "requiredErrMsg": "",
                    "patternErrMsg": "",
                    "values": [{
                        "label": "perfManagement.update.KPIs.groups.updatekpiTargetBlock.yes",
                        "value": "1"
                    }, {
                        "label": "perfManagement.update.KPIs.groups.updatekpiTargetBlock.no",
                        "value": "2"
                    }, {
                        "label": "perfManagement.update.KPIs.groups.updatekpiTargetBlock.inprogress",
                        "value": "3"
                    }]
                }]
            },
        ]
    }
}
export default dat;
