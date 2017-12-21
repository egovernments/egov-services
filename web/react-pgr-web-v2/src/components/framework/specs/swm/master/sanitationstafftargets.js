var dat = {
  'swm.search': {
    numCols: 4,
    useTimestamp: true,
    objectName: '',
    url: '/swm-services/sanitationstafftargets/_search',
    groups: [{
      name: 'search',
      label: 'swm.sanitationstaffschedules.search.title',
      fields: [{
          name: 'code',
          jsonPath: 'swmProcessCode',
          label: 'swm.create.sanitationStaffTargets.swmProcess',
          pattern: '',
          type: 'singleValueList',
          isRequired: false,
          isDisabled: false,
          maxLength: 256,
          minLength: 1,
          patternErrorMsg: '',
          url: '',
          defaultValue: [{
              key: null,
              value: '-- Please Select --'
            },
            {
              key: "Process 1",
              value: 'Collection',
            },
            {
              key: "Process 4",
              value: 'Disposal',
            },
            {
              key: "Process 2",
              value: 'Segregation',
            }
          ]


        },
        {
          name: 'targetNo',
          jsonPath: 'targetNo',
          label: 'swm.create.sanitationStaffTargets.targetNumber',
          type: 'text',
          isDisabled: false
        },
        {
          name: 'routeCode',
          jsonPath: 'routeCode',
          label: 'swm.create.sanitationstaffschedules.route.code',
          pattern: '',
          type: 'autoCompelete',
          isRequired: false,
          isDisabled: false,
          maxLength: 256,
          minLength: 1,
          patternErrorMsg: '',
          url: '/swm-services/routes/_search?|$.routes[*].code|$.routes[*].name'
        },
        {
          name: 'employeeName',
          jsonPath: 'employeeCode',
          label: 'swm.create.sanitationStaffTargets.employeeName',
          pattern: '',
          type: 'singleValueList',
          isRequired: false,
          isDisabled: false,

          maxLength: 128,
          minLength: 1,
          patternErrorMsg: '',
          url: '/hr-employee/employees/_search?&|$.Employee.*.code|$.Employee.*.name'
        }
      ],
    }, ],
    result: {
      header: [{
          label: 'swm.create.sanitationStaffTargets.swmProcess',
        },
        {
          label: 'swm.create.sanitationStaffTargets.employeeName',
        },
        {
          label: 'swm.create.sanitationStaffTargets.targetNumber',
        },
        {
          label: 'swm.create.sanitationstaffschedules.route.code',
        }
      ],
      values: ['swmProcess.name', 'employee.name', "targetNo", "route.name"],
      resultPath: 'sanitationStaffTargets',
      rowClickUrlUpdate: '/update/swm/sanitationstafftargets/{targetNo}',
      rowClickUrlView: '/view/swm/sanitationstafftargets/{targetNo}',
    },
  },
  'swm.create': {
    numCols: 4,
    useTimestamp: true,
    objectName: 'sanitationStaffTargets',
    idJsonPath: 'sanitationStaffTargets[0].transactionNo',
    groups: [{
        name: 'CardOne',
        label: '',
        fields: [{
            name: 'code',
            jsonPath: 'sanitationStaffTargets[0].swmProcess.code',
            label: 'swm.create.sanitationStaffTargets.swmProcess',
            pattern: '',
            type: 'singleValueList',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            maxLength: 256,
            minLength: 1,
            patternErrorMsg: '',
            url: '',
            defaultValue: [{
                key: null,
                value: '-- Please Select --'
              },
              {
                key: "Process 1",
                value: 'Collection',
              },
              {
                key: "Process 4",
                value: 'Disposal',
              },
              {
                key: "Process 2",
                value: 'Segregation',
              }
            ],
            showHideFields: [{
                ifValue: 'Process 1',

                hide: [{
                    name: 'CardFour',
                    isGroup: true,
                    isField: false,
                  },
                  {
                    name: 'CardFive',
                    isGroup: true,
                    isField: false,
                  },
                ],
                show: [{
                  name: 'CardThree',
                  isGroup: true,
                  isField: false,
                }, ],
              },
              {
                ifValue: 'Process 4',
                hide: [{
                    name: 'CardThree',
                    isGroup: true,
                    isField: false,
                  },
                  {
                    name: 'CardFive',
                    isGroup: true,
                    isField: false,
                  },
                ],
                show: [{
                  name: 'CardFour',
                  isGroup: true,
                  isField: false,
                }, ],
              },
              {
                ifValue: 'Process 2',
                hide: [{
                    name: 'CardThree',
                    isGroup: true,
                    isField: false,
                  },
                  {
                    name: 'CardFour',
                    isGroup: true,
                    isField: false,
                  },
                ],
                show: [{
                  name: 'CardFive',
                  isGroup: true,
                  isField: false,
                }, ],
              },
            ]


          },
          {
            name: 'targetFrom',
            jsonPath: 'sanitationStaffTargets[0].targetFrom',
            label: 'swm.create.sanitationStaffTargets.targetfrom',
            pattern: '',
            type: 'datePicker',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            patternErrorMsg: ''
          },
          {
            name: 'targetTo',
            jsonPath: 'sanitationStaffTargets[0].targetTo',
            label: 'swm.create.sanitationStaffTargets.targetto',
            pattern: '',
            type: 'datePicker',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            patternErrorMsg: '',
          }
        ]
      },
      {
        name: 'CardTwo',
        label: '',
        fields: [{
            name: 'departmentName',
            jsonPath: 'sanitationStaffTargets[0].employee.assignments[0].department',
            label: 'swm.create.sanitationStaffTargets.departmentName',
            pattern: '',
            type: 'singleValueList',
            isRequired: false,
            isDisabled: false,
            defaultValue: '',
            maxLength: 128,
            minLength: 1,
            patternErrorMsg: '',
            url: "/egov-mdms-service/v1/_get?tenantId=default&moduleName=common-masters&masterName=Department|$..id|$..name",
            hasIdConverion: true,
            depedants: [{
              jsonPath: 'sanitationStaffTargets[0].employee.code',
              type: 'dropDown',
              pattern: '/hr-employee/employees/_search?tenantId=default&departmentId={sanitationStaffTargets[0].employee.assignments[0].department}&designationId={sanitationStaffTargets[0].employee.assignments[0].designation}|$.Employee.*.code|$.Employee.*.name',
            }, ]
          },
          {
            name: 'designationName',
            jsonPath: 'sanitationStaffTargets[0].employee.assignments[0].designation',
            label: 'swm.create.sanitationStaffTargets.designationName',
            pattern: '',
            type: 'singleValueList',
            isRequired: false,
            isDisabled: false,
            defaultValue: '',
            maxLength: 128,
            minLength: 1,
            patternErrorMsg: '',
            url: '/hr-masters/designations/_search?tenantId=default|$..id|$..name',
            depedants: [{
              jsonPath: 'sanitationStaffTargets[0].employee.code',
              type: 'dropDown',
              pattern: '/hr-employee/employees/_search?tenantId=default&departmentId={sanitationStaffTargets[0].employee.assignments[0].department}&designationId={sanitationStaffTargets[0].employee.assignments[0].designation}|$.Employee.*.code|$.Employee.*.name',
            }, ]

          },
          {
            name: 'employeeName',
            jsonPath: 'sanitationStaffTargets[0].employee.code',
            label: 'swm.create.sanitationStaffTargets.employeeName',
            pattern: '',
            type: 'singleValueList',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            maxLength: 128,
            minLength: 1,
            patternErrorMsg: '',
            url: '/hr-employee/employees/_search?&departmentId={Connection.workflowDetails.department}&designationId={Connection.workflowDetails.designation}|$.Employee.*.code|$.Employee.*.name'
          }
        ]
      },
      {
        name: 'CardThree',
        label: 'swm.create.sanitationStaffTarget.cardThree.header',
        hide: false,
        fields: [{
            name: 'routeCode',
            jsonPath: 'sanitationStaffTargets[0].route.code',
            label: 'swm.create.sanitationstaffschedules.route.code',
            pattern: '',
            type: 'autoCompelete',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            maxLength: 256,
            minLength: 1,
            patternErrorMsg: '',
            url: '/swm-services/routes/_search?|$.routes[*].code|$.routes[*].name',
            autoCompleteDependancy: {
              autoCompleteUrl: '/swm-services/routes/_search?code={sanitationStaffTargets[0].route.code}',
              autoFillFields: {
                'sanitationStaffTargets[0].collectionPoints': 'routes[0].collectionPoints'
              }
            }
          },
          {
            type: 'tableList',
            jsonPath: 'sanitationStaffTargets[0].collectionPoints',
            tableList: {
              header: [{
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.isSelected',
                },
                {
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.location',
                },
                {
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.name',
                }
              ],
              values: [{
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.isSelected',
                  // label:'swm.create.sanitationstaffschedules.colletionPoint.isSelected',
                  pattern: '',
                  type: 'checkbox',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].isSelected',
                  isRequired: false,
                  isDisabled: false,
                },
                {
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.location',
                  pattern: '',
                  type: 'text',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].location.name',
                  isDisabled: true,
                },
                {
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.name',
                  pattern: '',
                  type: 'text',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].name',
                  isDisabled: true,
                }
              ],
              actionsNotRequired: true
            },
          },
          //   {
          //     name: 'collectionPoints',
          //     label: 'swm.create.sanitationStaffTarget.code',
          //     jsonPath: 'sanitationStaffTarget[0].collectionPoints[0].code',
          //     type: 'multiValueList',
          //     pattern: '',
          //     isRequired: true,
          //     isDisabled: false,
          //     url:'',
          //     patternErrorMsg: '',
          //     hasATOAATransform:true,
          //     aATransformInfo:{
          //       to:'sanitationStaffTarget[0].collectionPoints',
          //       key:'code'
          //     }
          // },
          {
            name: 'targetedGarbage',
            jsonPath: 'sanitationStaffTargets[0].targetedGarbage',
            label: 'swm.create.sanitationStaffTargets.targetedGarbage',
            pattern: '',
            type: 'number',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            patternErrorMsg: '',
          }

        ]
      },
      {
        name: 'CardFour',
        label: 'swm.create.sanitationStaffTarget.cardThree.header',
        hide: true,
        fields: [{
            name: 'dumpingGroundCode',
            jsonPath: 'sanitationStaffTargets[0].dumpingGround.code',
            label: 'swm.create.sanitationstaffschedules.dumpingGround.code',
            pattern: '',
            type: 'autoCompelete',
            isRequired: true,
            isDisabled: false,
            maxLength: 256,
            minLength: 1,
            patternErrorMsg: '',
            url: '/egov-mdms-service/v1/_get?&moduleName=swm&masterName=DumpingGround|$.MdmsRes.swm.DumpingGround[*].code|$.MdmsRes.swm.DumpingGround[*].name'
          },
          {
            name: 'targetedGarbage',
            jsonPath: 'sanitationStaffTargets[0].targetedGarbage',
            label: 'swm.create.sanitationStaffTargets.targetedGarbage',
            pattern: '',
            type: 'number',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
          }
        ]
      },
      {
        name: 'CardFive',
        label: 'swm.create.sanitationStaffTarget.cardThree.header',
        hide: true,
        fields: [{
            name: 'routeCode',
            jsonPath: 'sanitationStaffTargets[0].route.code',
            label: 'swm.create.sanitationstaffschedules.route.code',
            pattern: '',
            type: 'autoCompelete',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            maxLength: 256,
            minLength: 1,
            patternErrorMsg: '',
            url: '/swm-services/routes/_search?|$.routes[*].code|$.routes[*].name',
            autoCompleteDependancy: {
              autoCompleteUrl: '/swm-services/routes/_search?code={sanitationStaffTargets[0].route.code}',
              autoFillFields: {
                'sanitationStaffTargets[0].collectionPoints': 'routes[0].collectionPoints'
              }
            }
          },
          {
            type: 'tableList',
            jsonPath: 'sanitationStaffTargets[0].collectionPoints',
            tableList: {
              header: [{
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.isSelected',
                },
                {
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.location',
                },
                {
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.name',
                }
              ],
              values: [{
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.isSelected',
                  // label:'swm.create.sanitationstaffschedules.colletionPoint.isSelected',
                  pattern: '',
                  type: 'checkbox',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].isSelected',
                  isRequired: false,
                  isDisabled: false,
                },
                {
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.location',
                  pattern: '',
                  type: 'text',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].location.name',
                  isDisabled: true,
                },
                {
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.name',
                  pattern: '',
                  type: 'text',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].name',
                  isDisabled: true,
                }
              ],
              actionsNotRequired: true
            },
          },
          {
            name: 'wetWaste',
            jsonPath: 'sanitationStaffTargets[0].wetWaste',
            label: 'swm.create.sanitationStaffTargets.wetWaste',
            pattern: '',
            type: 'number',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            patternErrorMsg: '',
          },
          {
            name: 'dryWaste',
            jsonPath: 'sanitationStaffTargets[0].dryWaste',
            label: 'swm.create.sanitationStaffTargets.dryWaste',
            pattern: '',
            type: 'number',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            patternErrorMsg: '',
            depedants: [{
              jsonPath: 'sanitationStaffTargets[0].targetedGarbage',
              type: 'textField',
              pattern: "`${getVal('sanitationStaffTargets[0].wetWaste') && getVal('sanitationStaffTargets[0].dryWaste') ? (parseInt(getVal('sanitationStaffTargets[0].wetWaste')) + parseInt(getVal('sanitationStaffTargets[0].dryWaste'))):0}`",
              rg: '',
              isRequired: false,
              requiredErrMsg: '',
              patternErrMsg: '',
            }, ]
          },
          {
            name: 'targetedGarbage',
            jsonPath: 'sanitationStaffTargets[0].targetedGarbage',
            label: 'swm.create.sanitationStaffTargets.targetedGarbage',
            pattern: '',
            type: 'text',
            isRequired: true,
            isDisabled: true,
            defaultValue: '',
            patternErrorMsg: '',
          },

        ]
      }
    ],
    url: '/swm-services/sanitationstafftargets/_create',
    tenantIdRequired: true,
  },
  'swm.view': {
    numCols: 4,
    useTimestamp: true,
    objectName: 'sanitationStaffTargets',
    idJsonPath: 'sanitationStaffTargets[0].transactionNo',
    groups: [{
        name: 'CardOne',
        label: '',
        fields: [{
            name: 'code',
            jsonPath: 'sanitationStaffTargets[0].swmProcess.code',
            label: 'swm.create.sanitationStaffTargets.swmProcess',
            pattern: '',
            type: 'singleValueList',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            maxLength: 256,
            minLength: 1,
            patternErrorMsg: '',
            url: '',
            defaultValue: [{
                key: null,
                value: '-- Please Select --'
              },
              {
                key: "Process 1",
                value: 'Collection',
              },
              {
                key: "Process 4",
                value: 'Disposal',
              },
              {
                key: "Process 2",
                value: 'Segregation',
              }
            ],
            showHideFields: [{
                ifValue: 'Process 1',

                hide: [{
                    name: 'CardFour',
                    isGroup: true,
                    isField: false,
                  },
                  {
                    name: 'CardFive',
                    isGroup: true,
                    isField: false,
                  },
                ],
                show: [{
                  name: 'CardThree',
                  isGroup: true,
                  isField: false,
                }, ],
              },
              {
                ifValue: 'Process 4',
                hide: [{
                    name: 'CardThree',
                    isGroup: true,
                    isField: false,
                  },
                  {
                    name: 'CardFive',
                    isGroup: true,
                    isField: false,
                  },
                ],
                show: [{
                  name: 'CardFour',
                  isGroup: true,
                  isField: false,
                }, ],
              },
              {
                ifValue: 'Process 2',
                hide: [{
                    name: 'CardThree',
                    isGroup: true,
                    isField: false,
                  },
                  {
                    name: 'CardFour',
                    isGroup: true,
                    isField: false,
                  },
                ],
                show: [{
                  name: 'CardFive',
                  isGroup: true,
                  isField: false,
                }, ],
              },
            ]


          },
          {
            name: 'targetFrom',
            jsonPath: 'sanitationStaffTargets[0].targetFrom',
            label: 'swm.create.sanitationStaffTargets.targetfrom',
            pattern: '',
            type: 'datePicker',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            patternErrorMsg: ''
          },
          {
            name: 'targetTo',
            jsonPath: 'sanitationStaffTargets[0].targetTo',
            label: 'swm.create.sanitationStaffTargets.targetto',
            pattern: '',
            type: 'datePicker',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            patternErrorMsg: '',
          }
        ]
      },
      {
        name: 'CardTwo',
        label: '',
        fields: [{
            name: 'departmentName',
            jsonPath: 'sanitationStaffTargets[0].employee.assignments[0].department',
            label: 'swm.create.sanitationStaffTargets.departmentName',
            pattern: '',
            type: 'singleValueList',
            isRequired: false,
            isDisabled: false,
            defaultValue: '',
            maxLength: 128,
            minLength: 1,
            patternErrorMsg: '',
            url: "/egov-mdms-service/v1/_get?tenantId=default&moduleName=common-masters&masterName=Department|$..id|$..name",
            hasIdConverion: true,
            depedants: [{
              jsonPath: 'sanitationStaffTargets[0].employee.code',
              type: 'dropDown',
              pattern: '/hr-employee/employees/_search?tenantId=default&departmentId={sanitationStaffTargets[0].employee.assignments[0].department}&designationId={sanitationStaffTargets[0].employee.assignments[0].designation}|$.Employee.*.code|$.Employee.*.name',
            }, ]
          },
          {
            name: 'designationName',
            jsonPath: 'sanitationStaffTargets[0].employee.assignments[0].designation',
            label: 'swm.create.sanitationStaffTargets.designationName',
            pattern: '',
            type: 'singleValueList',
            isRequired: false,
            isDisabled: false,
            defaultValue: '',
            maxLength: 128,
            minLength: 1,
            patternErrorMsg: '',
            url: '/hr-masters/designations/_search?tenantId=default|$..id|$..name',
            depedants: [{
              jsonPath: 'sanitationStaffTargets[0].employee.code',
              type: 'dropDown',
              pattern: '/hr-employee/employees/_search?tenantId=default&departmentId={sanitationStaffTargets[0].employee.assignments[0].department}&designationId={sanitationStaffTargets[0].employee.assignments[0].designation}|$.Employee.*.code|$.Employee.*.name',
            }, ]

          },
          {
            name: 'employeeName',
            jsonPath: 'sanitationStaffTargets[0].employee.code',
            label: 'swm.create.sanitationStaffTargets.employeeName',
            pattern: '',
            type: 'singleValueList',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            maxLength: 128,
            minLength: 1,
            patternErrorMsg: '',
            url: '/hr-employee/employees/_search?&|$.Employee.*.code|$.Employee.*.name'
          }
        ]
      },
      {
        name: 'CardThree',
        label: 'swm.create.sanitationStaffTarget.cardThree.header',
        hide: false,
        fields: [{
            name: 'routeCode',
            jsonPath: 'sanitationStaffTargets[0].route.code',
            label: 'swm.create.sanitationstaffschedules.route.code',
            pattern: '',
            type: 'autoCompelete',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            maxLength: 256,
            minLength: 1,
            patternErrorMsg: '',
            url: '/swm-services/routes/_search?|$.routes[*].code|$.routes[*].name',
            autoCompleteDependancy: {
              autoCompleteUrl: '/swm-services/routes/_search?code={sanitationStaffTargets[0].route.code}',
              autoFillFields: {
                'sanitationStaffTargets[0].collectionPoints': 'routes[0].collectionPoints'
              }
            }
          },
          {
            type: 'tableList',
            jsonPath: 'sanitationStaffTargets[0].collectionPoints',
            tableList: {
              header: [
                {
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.location',
                },
                {
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.name',
                }
              ],
              values: [
                {
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.location',
                  pattern: '',
                  type: 'text',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].location.name',
                  isDisabled: true,
                },
                {
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.name',
                  pattern: '',
                  type: 'text',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].name',
                  isDisabled: true,
                }
              ],
              actionsNotRequired: true
            },
          },
          //   {
          //     name: 'collectionPoints',
          //     label: 'swm.create.sanitationStaffTarget.code',
          //     jsonPath: 'sanitationStaffTarget[0].collectionPoints[0].code',
          //     type: 'multiValueList',
          //     pattern: '',
          //     isRequired: true,
          //     isDisabled: false,
          //     url:'',
          //     patternErrorMsg: '',
          //     hasATOAATransform:true,
          //     aATransformInfo:{
          //       to:'sanitationStaffTarget[0].collectionPoints',
          //       key:'code'
          //     }
          // },
          {
            name: 'targetedGarbage',
            jsonPath: 'sanitationStaffTargets[0].targetedGarbage',
            label: 'swm.create.sanitationStaffTargets.targetedGarbage',
            pattern: '',
            type: 'number',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            patternErrorMsg: '',
          }

        ]
      },
      {
        name: 'CardFour',
        label: 'swm.create.sanitationStaffTarget.cardThree.header',
        hide: true,
        fields: [{
            name: 'dumpingGroundCode',
            jsonPath: 'sanitationStaffTargets[0].dumpingGround.code',
            label: 'swm.create.sanitationstaffschedules.dumpingGround.code',
            pattern: '',
            type: 'autoCompelete',
            isRequired: true,
            isDisabled: false,
            maxLength: 256,
            minLength: 1,
            patternErrorMsg: '',
            url: '/egov-mdms-service/v1/_get?&moduleName=swm&masterName=DumpingGround|$.MdmsRes.swm.DumpingGround[*].code|$.MdmsRes.swm.DumpingGround[*].name'
          },
          {
            name: 'targetedGarbage',
            jsonPath: 'sanitationStaffTargets[0].targetedGarbage',
            label: 'swm.create.sanitationStaffTargets.targetedGarbage',
            pattern: '',
            type: 'number',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
          }
        ]
      },
      {
        name: 'CardFive',
        label: 'swm.create.sanitationStaffTarget.cardThree.header',
        hide: true,
        fields: [{
            name: 'routeCode',
            jsonPath: 'sanitationStaffTargets[0].route.code',
            label: 'swm.create.sanitationstaffschedules.route.code',
            pattern: '',
            type: 'autoCompelete',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            maxLength: 256,
            minLength: 1,
            patternErrorMsg: '',
            url: '/swm-services/routes/_search?|$.routes[*].code|$.routes[*].name',
            autoCompleteDependancy: {
              autoCompleteUrl: '/swm-services/routes/_search?code={sanitationStaffTargets[0].route.code}',
              autoFillFields: {
                'sanitationStaffTargets[0].collectionPoints': 'routes[0].collectionPoints'
              }
            }
          },
          {
            type: 'tableList',
            jsonPath: 'sanitationStaffTargets[0].collectionPoints',
            tableList: {
              header: [
                {
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.location',
                },
                {
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.name',
                }
              ],
              values: [
                {
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.location',
                  pattern: '',
                  type: 'text',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].location.name',
                  isDisabled: true,
                },
                {
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.name',
                  pattern: '',
                  type: 'text',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].name',
                  isDisabled: true,
                }
              ],
              actionsNotRequired: true
            },
          },
          {
            name: 'wetWaste',
            jsonPath: 'sanitationStaffTargets[0].wetWaste',
            label: 'swm.create.sanitationStaffTargets.wetWaste',
            pattern: '',
            type: 'number',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            patternErrorMsg: '',
          },
          {
            name: 'dryWaste',
            jsonPath: 'sanitationStaffTargets[0].dryWaste',
            label: 'swm.create.sanitationStaffTargets.dryWaste',
            pattern: '',
            type: 'number',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            patternErrorMsg: '',
            depedants: [{
              jsonPath: 'sanitationStaffTargets[0].targetedGarbage',
              type: 'textField',
              pattern: "`${getVal('sanitationStaffTargets[0].wetWaste') && getVal('sanitationStaffTargets[0].dryWaste') ? (parseInt(getVal('sanitationStaffTargets[0].wetWaste')) + parseInt(getVal('sanitationStaffTargets[0].dryWaste'))):0}`",
              rg: '',
              isRequired: false,
              requiredErrMsg: '',
              patternErrMsg: '',
            }, ]
          },
          {
            name: 'targetedGarbage',
            jsonPath: 'sanitationStaffTargets[0].targetedGarbage',
            label: 'swm.create.sanitationStaffTargets.targetedGarbage',
            pattern: '',
            type: 'text',
            isRequired: true,
            isDisabled: true,
            defaultValue: '',
            patternErrorMsg: '',
          },

        ]
      }
    ],
    url: '/swm-services/sanitationstafftargets/_search?targetNo={targetNo}',
    tenantIdRequired: true
    // searchUrl: '/swm-services/sanitationstafftargets/_search?targetNo={targetNo}'
  },
  'swm.update': {
    numCols: 4,
    useTimestamp: true,
    objectName: 'sanitationStaffTargets',
    idJsonPath: 'sanitationStaffTargets[0].transactionNo',
    groups: [{
        name: 'CardOne',
        label: '',
        fields: [{
            name: 'code',
            jsonPath: 'sanitationStaffTargets[0].swmProcess.code',
            label: 'swm.create.sanitationStaffTargets.swmProcess',
            pattern: '',
            type: 'singleValueList',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            maxLength: 256,
            minLength: 1,
            patternErrorMsg: '',
            url: '',
            defaultValue: [{
                key: null,
                value: '-- Please Select --'
              },
              {
                key: "Process 1",
                value: 'Collection',
              },
              {
                key: "Process 4",
                value: 'Disposal',
              },
              {
                key: "Process 2",
                value: 'Segregation',
              }
            ],
            showHideFields: [{
                ifValue: 'Process 1',

                hide: [{
                    name: 'CardFour',
                    isGroup: true,
                    isField: false,
                  },
                  {
                    name: 'CardFive',
                    isGroup: true,
                    isField: false,
                  },
                ],
                show: [{
                  name: 'CardThree',
                  isGroup: true,
                  isField: false,
                }, ],
              },
              {
                ifValue: 'Process 4',
                hide: [{
                    name: 'CardThree',
                    isGroup: true,
                    isField: false,
                  },
                  {
                    name: 'CardFive',
                    isGroup: true,
                    isField: false,
                  },
                ],
                show: [{
                  name: 'CardFour',
                  isGroup: true,
                  isField: false,
                }, ],
              },
              {
                ifValue: 'Process 2',
                hide: [{
                    name: 'CardThree',
                    isGroup: true,
                    isField: false,
                  },
                  {
                    name: 'CardFour',
                    isGroup: true,
                    isField: false,
                  },
                ],
                show: [{
                  name: 'CardFive',
                  isGroup: true,
                  isField: false,
                }, ],
              },
            ]


          },
          {
            name: 'targetFrom',
            jsonPath: 'sanitationStaffTargets[0].targetFrom',
            label: 'swm.create.sanitationStaffTargets.targetfrom',
            pattern: '',
            type: 'datePicker',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            patternErrorMsg: ''
          },
          {
            name: 'targetTo',
            jsonPath: 'sanitationStaffTargets[0].targetTo',
            label: 'swm.create.sanitationStaffTargets.targetto',
            pattern: '',
            type: 'datePicker',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            patternErrorMsg: '',
          }
        ]
      },
      {
        name: 'CardTwo',
        label: '',
        fields: [{
            name: 'departmentName',
            jsonPath: 'sanitationStaffTargets[0].employee.assignments[0].department',
            label: 'swm.create.sanitationStaffTargets.departmentName',
            pattern: '',
            type: 'singleValueList',
            isRequired: false,
            isDisabled: false,
            defaultValue: '',
            maxLength: 128,
            minLength: 1,
            patternErrorMsg: '',
            url: "/egov-mdms-service/v1/_get?tenantId=default&moduleName=common-masters&masterName=Department|$..id|$..name",
            hasIdConverion: true,
            depedants: [{
              jsonPath: 'sanitationStaffTargets[0].employee.code',
              type: 'dropDown',
              pattern: '/hr-employee/employees/_search?tenantId=default&departmentId={sanitationStaffTargets[0].employee.assignments[0].department}&designationId={sanitationStaffTargets[0].employee.assignments[0].designation}|$.Employee.*.code|$.Employee.*.name',
            }, ]
          },
          {
            name: 'designationName',
            jsonPath: 'sanitationStaffTargets[0].employee.assignments[0].designation',
            label: 'swm.create.sanitationStaffTargets.designationName',
            pattern: '',
            type: 'singleValueList',
            isRequired: false,
            isDisabled: false,
            defaultValue: '',
            maxLength: 128,
            minLength: 1,
            patternErrorMsg: '',
            url: '/hr-masters/designations/_search?tenantId=default|$..id|$..name',
            depedants: [{
              jsonPath: 'sanitationStaffTargets[0].employee.code',
              type: 'dropDown',
              pattern: '/hr-employee/employees/_search?tenantId=default&departmentId={sanitationStaffTargets[0].employee.assignments[0].department}&designationId={sanitationStaffTargets[0].employee.assignments[0].designation}|$.Employee.*.code|$.Employee.*.name',
            }, ]

          },
          {
            name: 'employeeName',
            jsonPath: 'sanitationStaffTargets[0].employee.code',
            label: 'swm.create.sanitationStaffTargets.employeeName',
            pattern: '',
            type: 'singleValueList',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            maxLength: 128,
            minLength: 1,
            patternErrorMsg: '',
            url: '/hr-employee/employees/_search?&departmentId={Connection.workflowDetails.department}&designationId={Connection.workflowDetails.designation}|$.Employee.*.code|$.Employee.*.name'
          }
        ]
      },
      {
        name: 'CardThree',
        label: 'swm.create.sanitationStaffTarget.cardThree.header',
        hide: false,
        fields: [{
            name: 'routeCode',
            jsonPath: 'sanitationStaffTargets[0].route.code',
            label: 'swm.create.sanitationstaffschedules.route.code',
            pattern: '',
            type: 'autoCompelete',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            maxLength: 256,
            minLength: 1,
            patternErrorMsg: '',
            url: '/swm-services/routes/_search?|$.routes[*].code|$.routes[*].name',
            autoCompleteDependancy: {
              autoCompleteUrl: '/swm-services/routes/_search?code={sanitationStaffTargets[0].route.code}',
              autoFillFields: {
                'sanitationStaffTargets[0].collectionPoints': 'routes[0].collectionPoints'
              }
            }
          },
          {
            type: 'tableList',
            jsonPath: 'sanitationStaffTargets[0].collectionPoints',
            tableList: {
              header: [{
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.isSelected',
                },
                {
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.location',
                },
                {
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.name',
                }
              ],
              values: [{
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.isSelected',
                  // label:'swm.create.sanitationstaffschedules.colletionPoint.isSelected',
                  pattern: '',
                  type: 'checkbox',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].isSelected',
                  isRequired: false,
                  isDisabled: false,
                  defaultValue:true
                },
                {
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.location',
                  pattern: '',
                  type: 'text',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].location.name',
                  isDisabled: true,
                },
                {
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.name',
                  pattern: '',
                  type: 'text',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].name',
                  isDisabled: true,
                }
              ],
              actionsNotRequired: true
            },
          },
          //   {
          //     name: 'collectionPoints',
          //     label: 'swm.create.sanitationStaffTarget.code',
          //     jsonPath: 'sanitationStaffTarget[0].collectionPoints[0].code',
          //     type: 'multiValueList',
          //     pattern: '',
          //     isRequired: true,
          //     isDisabled: false,
          //     url:'',
          //     patternErrorMsg: '',
          //     hasATOAATransform:true,
          //     aATransformInfo:{
          //       to:'sanitationStaffTarget[0].collectionPoints',
          //       key:'code'
          //     }
          // },
          {
            name: 'targetedGarbage',
            jsonPath: 'sanitationStaffTargets[0].targetedGarbage',
            label: 'swm.create.sanitationStaffTargets.targetedGarbage',
            pattern: '',
            type: 'number',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            patternErrorMsg: '',
          }

        ]
      },
      {
        name: 'CardFour',
        label: 'swm.create.sanitationStaffTarget.cardThree.header',
        hide: true,
        fields: [{
            name: 'dumpingGroundCode',
            jsonPath: 'sanitationStaffTargets[0].dumpingGround.code',
            label: 'swm.create.sanitationstaffschedules.dumpingGround.code',
            pattern: '',
            type: 'autoCompelete',
            isRequired: true,
            isDisabled: false,
            maxLength: 256,
            minLength: 1,
            patternErrorMsg: '',
            url: '/egov-mdms-service/v1/_get?&moduleName=swm&masterName=DumpingGround|$.MdmsRes.swm.DumpingGround[*].code|$.MdmsRes.swm.DumpingGround[*].name'
          },
          {
            name: 'targetedGarbage',
            jsonPath: 'sanitationStaffTargets[0].targetedGarbage',
            label: 'swm.create.sanitationStaffTargets.targetedGarbage',
            pattern: '',
            type: 'number',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
          }
        ]
      },
      {
        name: 'CardFive',
        label: 'swm.create.sanitationStaffTarget.cardThree.header',
        hide: true,
        fields: [{
            name: 'routeCode',
            jsonPath: 'sanitationStaffTargets[0].route.code',
            label: 'swm.create.sanitationstaffschedules.route.code',
            pattern: '',
            type: 'autoCompelete',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            maxLength: 256,
            minLength: 1,
            patternErrorMsg: '',
            url: '/swm-services/routes/_search?|$.routes[*].code|$.routes[*].name',
            autoCompleteDependancy: {
              autoCompleteUrl: '/swm-services/routes/_search?code={sanitationStaffTargets[0].route.code}',
              autoFillFields: {
                'sanitationStaffTargets[0].collectionPoints': 'routes[0].collectionPoints'
              }
            }
          },
          {
            type: 'tableList',
            jsonPath: 'sanitationStaffTargets[0].collectionPoints',
            tableList: {
              header: [{
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.isSelected',
                },
                {
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.location',
                },
                {
                  label: 'swm.create.sanitationstaffschedules.colletionPoint.name',
                }
              ],
              values: [{
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.isSelected',
                  // label:'swm.create.sanitationstaffschedules.colletionPoint.isSelected',
                  type: 'checkbox',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].isSelected',
                  isRequired: false,
                  isDisabled: false,
                  defaultValue:true
                },
                {
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.location',
                  pattern: '',
                  type: 'text',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].location.name',
                  isDisabled: true,
                },
                {
                  name: 'swm.create.sanitationstaffschedules.colletionPoint.name',
                  pattern: '',
                  type: 'text',
                  jsonPath: 'sanitationStaffTargets[0].collectionPoints[0].name',
                  isDisabled: true,
                }
              ],
              actionsNotRequired: true
            },
          },
          {
            name: 'wetWaste',
            jsonPath: 'sanitationStaffTargets[0].wetWaste',
            label: 'swm.create.sanitationStaffTargets.wetWaste',
            pattern: '',
            type: 'number',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            patternErrorMsg: '',
          },
          {
            name: 'dryWaste',
            jsonPath: 'sanitationStaffTargets[0].dryWaste',
            label: 'swm.create.sanitationStaffTargets.dryWaste',
            pattern: '',
            type: 'number',
            isRequired: true,
            isDisabled: false,
            defaultValue: '',
            patternErrorMsg: '',
            depedants: [{
              jsonPath: 'sanitationStaffTargets[0].targetedGarbage',
              type: 'textField',
              pattern: "`${getVal('sanitationStaffTargets[0].wetWaste') && getVal('sanitationStaffTargets[0].dryWaste') ? (parseInt(getVal('sanitationStaffTargets[0].wetWaste')) + parseInt(getVal('sanitationStaffTargets[0].dryWaste'))):0}`",
              rg: '',
              isRequired: false,
              requiredErrMsg: '',
              patternErrMsg: '',
            }, ]
          },
          {
            name: 'targetedGarbage',
            jsonPath: 'sanitationStaffTargets[0].targetedGarbage',
            label: 'swm.create.sanitationStaffTargets.targetedGarbage',
            pattern: '',
            type: 'text',
            isRequired: true,
            isDisabled: true,
            defaultValue: '',
            patternErrorMsg: '',
          },

        ]
      }
    ],
    url: '/swm-services/sanitationstafftargets/_update',
    tenantIdRequired: true,
    searchUrl: '/swm-services/sanitationstafftargets/_search?targetNo={targetNo}'
  }
};
export default dat;
