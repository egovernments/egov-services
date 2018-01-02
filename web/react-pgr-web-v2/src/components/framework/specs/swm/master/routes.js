var dat = {
  'swm.create': {
    numCols: 2,
    useTimestamp: true,
    objectName: 'routes',
    title: 'swm.routes.create.title',
    groups: [
      {
        name: 'routeDetails',
        label: '',
        fields: [
          {
            name: 'name',
            jsonPath: 'routes[0].name',
            label: 'swm.routes.create.name',
            type: 'text',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
          },
          {
            name: 'code',
            jsonPath: 'routes[0].collectionType.code',
            label: 'swm.routes.create.collectionType',
            type: 'singleValueList',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
            url:'/egov-mdms-service/v1/_get?&moduleName=swm&masterName=CollectionType|$..code|$..name',
            depedants: [
              {
                jsonPath: 'routes[0].startingCollectionPoint.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}|$..code|$..name',
                autoFillFields: {
                  'routes[0].startingCollectionPoint.name': 'startingCollectionPoint.name',
                },
              },
              {
                jsonPath: 'routes[0].startingCollectionPoint.location.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'routes[0].startingCollectionPoint.location.name': 'location.name',
                },
              },
              {
                jsonPath: 'routes[0].collectionPoints[0].name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'routes[0].collectionPoints[0].name': 'collectionPoints[0].name',
                },
              },
              {
                jsonPath: 'routes[0].collectionPoints[0].location.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'routes[0].collectionPoints[0].location.name': 'location.name',
                },
              },

              {
                jsonPath: 'routes[0].endingDumpingGroundPoint.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'routes[0].endingDumpingGroundPoint.name': 'endingDumpingGroundPoint.name',
                },
              },

              {
                jsonPath: 'route[0].endingCollectionPoint.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'route[0].endingCollectionPoint.name': 'endingCollectionPoint.name',
                },
              },
              {
                jsonPath: 'route[0].endingCollectionPoint.location.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'route[0].endingCollectionPoint.location.name': 'location.name',
                },
              },

            ]
          },
        ]
      },
      {
        name: 'startingCollectionPoint',
        label: 'swm.routes.create.group.title.starting',
        jsonPath: "routes[0].startingCollectionPoint",
        fields: [
          {
            name: 'startingCollectionPointWard',
            jsonPath: 'routes[0].startingCollectionPoint[0].ward',
            label: 'swm.routes.create.ward',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'startingCollectionPointZone',
            jsonPath: 'routes[0].startingCollectionPoint[0].zone',
            label: 'swm.routes.create.zone',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'startingCollectionPointRoad',
            jsonPath: 'routes[0].startingCollectionPoint[0].road',
            label: 'swm.routes.create.road',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'startingCollectionPointColony',
            jsonPath: 'route[0].startingCollectionPoint.location.name',
            label: 'swm.routes.create.colonysociety',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'startingCollectionPointName',
            jsonPath: 'routes[0].startingCollectionPoint.name',
            label: 'swm.routes.create.collectionpoint',
            type: 'text',
            isRequired: true,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'startingCollectionPointDistanceGarbageEstimate',
            jsonPath: 'routes[0].collectionType',
           // label: 'swm.routes.create.collectionType',
            type: 'custom',
            numCols: 6,
            fields : [
              {
                name: 'startingCollectionPointDistance',
                jsonPath: 'routes[0].name',
                label: 'Distance From Last Stop(KMS)',
                checkCustom : true,
                type: 'number',
                add: true,
                isRequired: false,
                isDisabled: false,
                patternErrorMsg: '',
            
              },
              {
                name: 'startingCollectionPointGarbageEstimate',
                jsonPath: 'routes[0].startingCollectionPoint.collectionPointDetails[0].garbageEstimate',
                label: 'Expected Garbage Collection(TONS)',
                checkCustom : true,
                type: 'number',
                isRequired: true,
                isDisabled: false,
                patternErrorMsg: '',
               
              },
            ],
          },
        ],
      },
      {
        name: 'collectionPoints',
        multiple:true,
        label: 'swm.routes.create.group.title.routestops',
        jsonPath: "routes[0].collectionPoints",
        fields: [
          {
            name: 'collectionPointsWard',
            jsonPath: 'routes[0].collectionPoints[0].name',
            label: 'swm.routes.create.ward',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'collectionPointsZone',
            jsonPath: 'routes[0].stops[0].zone',
            label: 'swm.routes.create.zone',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'collectionPointsRoad',
            jsonPath: 'routes[0].stops[0].road',
            label: 'swm.routes.create.road',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'collectionPointsColony',
            jsonPath: 'routes[0].collectionPoints[0].location.name',
            label: 'swm.routes.create.colonysociety',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'collectionPointsName',
            jsonPath: 'routes[0].collectionPoints[0].name',
            label: 'swm.routes.create.collectionpoint',
            type: 'text',
            isRequired: true,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'collectionType',
            jsonPath: 'routes[0].collectionType',
            type: 'custom',
            numCols: 6,
            fields : [
              {
                name: 'collectionPointsDistance',
                jsonPath: 'routes[0].name',
                label: 'Distance From Last Stop(KMS)',
                checkCustom : true,
                type: 'number',
                isRequired: false,
                isDisabled: false,
                patternErrorMsg: '',
            
              },
              {
                name: 'collectionPointsGarbageEstimate',
                jsonPath: 'routes[0].collectionPoints[0].collectionType.garbageEstimate',
                label: 'Expected Garbage Collection(TONS)',
                checkCustom : true,
                type: 'number',
                isRequired: true,
                isDisabled: false,
                patternErrorMsg: '',
               
              },
            ],
          },
        ],
      },

      {
        name: 'endingDumpingGroundPoint',
        label: 'swm.routes.create.group.title.end',
        fields: [
          {
            name: 'endingDumpingGroundPointDistance',
            jsonPath: 'routes[0].endingDumpingGroundPoint.isProcessingSite',
            label: 'swm.routes.create.collectionPoint',
            type: 'checkbox',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
          },
          {
            name: 'customtext',
            jsonPath: '',
            label: '',
            type: 'label',
            defaultValue:'OR',
            patternErrorMsg: '',
          },
          {
            name: 'endingDumpingGroundPointName',
            jsonPath: 'routes[0].endingDumpingGroundPoint.name',
            label: 'swm.routes.create.dumping',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
       
        ],
      },
      {
        name: 'endingCollectionPoint',
        label: '',
        jsonPath: "routes[0].endingCollectionPoint",
        fields: [
          {
            name: 'endingCollectionPointWard',
            jsonPath: 'routes[0].endingCollectionPoint.name',
            label: 'swm.routes.create.ward',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'endingCollectionPointZone',
            jsonPath: 'routes[0].endingCollectionPoint.name',
            label: 'swm.routes.create.zone',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'endingCollectionPointRoad',
            jsonPath: 'route[0].endingCollectionPoint.name',
            label: 'swm.routes.create.road',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'endingCollectionPointColony',
            jsonPath: 'routes[0].endingCollectionPoint.location.name',
            label: 'swm.routes.create.colonysociety',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'endingCollectionPointName',
            jsonPath: 'routes[0].endingCollectionPoint.name',
            label: 'swm.routes.create.collectionpoint',
            type: 'text',
            isRequired: true,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'code',
            jsonPath: 'routes[0].collectionType',
            type: 'custom',
            numCols: 6,
            fields : [
              {
                name: 'endingCollectionPointDistance',
                jsonPath: 'routes[0].endingDumpingGroundPoint.distanceFromProcessingSite',
                label: 'Distance From Last Stop(KMS)',
                checkCustom : true,
                type: 'number',
                isRequired: false,
                isDisabled: false,
                patternErrorMsg: '',
            
              },
              {
                name: 'endingCollectionPointGarbageEstimate',
                jsonPath: 'routes[0].endingCollectionPoint.collectionPointDetails[0].garbageEstimate',
                label: 'Expected Garbage Collection(TONS)',
                checkCustom : true,
                type: 'number',
                isRequired: true,
                isDisabled: false,
                patternErrorMsg: '',
               
              },
            ],
          },
        ],
      },
      
      {
        name: 'totalDistanceGarbageEstimate',
        label: '',
        fields: [
          {
            name: 'collectionType',
            jsonPath: 'routes',
            type: 'custom',
            numCols: 6,
            fields : [
              {
                name: 'totalDistance',
                jsonPath: 'routes[0].distance',
                label: 'Total Distance Covered(KMS)',
                checkCustom : true,
                type: 'number',
                total:true,
                isRequired: true,
                isDisabled: false,
                patternErrorMsg: '',
              },
              {
                name: 'totalGarbageEstimate',
                jsonPath: 'routes[0].garbageEstimate',
                label: 'Total Expected Garbage(TONS)',
                checkCustom : true,
                type: 'number',
                isRequired: true,
                isDisabled: false,
                patternErrorMsg: '',
              },
            ],
          },
        ],
      },
    ],
    url: '/swm-services/routes/_create',
    tenantIdRequired: true,
  },

  'swm.update': {
    numCols: 2,
    useTimestamp: true,
    objectName: 'routes',
    title: 'swm.routes.create.title',
    groups: [
      {
        name: 'routeDetails',
        label: '',
        fields: [
          {
            name: 'name',
            jsonPath: 'routes[0].name',
            label: 'swm.routes.create.name',
            type: 'text',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
          },
          {
            name: 'code',
            jsonPath: 'routes[0].collectionType.code',
            label: 'swm.routes.create.collectionType',
            type: 'singleValueList',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
            url:'/egov-mdms-service/v1/_get?&moduleName=swm&masterName=CollectionType|$..code|$..name',
            depedants: [
              {
                jsonPath: 'routes[0].startingCollectionPoint.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}|$..code|$..name',
                autoFillFields: {
                  'routes[0].startingCollectionPoint.name': 'startingCollectionPoint.name',
                },
              },
              {
                jsonPath: 'routes[0].startingCollectionPoint.location.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'routes[0].startingCollectionPoint.location.name': 'location.name',
                },
              },
              {
                jsonPath: 'routes[0].collectionPoints[0].name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'routes[0].collectionPoints[0].name': 'collectionPoints[0].name',
                },
              },
              {
                jsonPath: 'routes[0].collectionPoints[0].location.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'routes[0].collectionPoints[0].location.name': 'location.name',
                },
              },

              {
                jsonPath: 'routes[0].endingDumpingGroundPoint.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'routes[0].endingDumpingGroundPoint.name': 'endingDumpingGroundPoint.name',
                },
              },

              {
                jsonPath: 'route[0].endingCollectionPoint.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'route[0].endingCollectionPoint.name': 'endingCollectionPoint.name',
                },
              },
              {
                jsonPath: 'route[0].endingCollectionPoint.location.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'route[0].endingCollectionPoint.location.name': 'location.name',
                },
              },

            ]
          },
        ]
      },
      {
        name: 'startingCollectionPoint',
        label: 'swm.routes.create.group.title.starting',
        jsonPath: "routes[0].startingCollectionPoint",
        fields: [
          {
            name: 'startingCollectionPointWard',
            jsonPath: 'routes[0].startingCollectionPoint[0].ward',
            label: 'swm.routes.create.ward',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'startingCollectionPointZone',
            jsonPath: 'routes[0].startingCollectionPoint[0].zone',
            label: 'swm.routes.create.zone',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'startingCollectionPointRoad',
            jsonPath: 'routes[0].startingCollectionPoint[0].road',
            label: 'swm.routes.create.road',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'startingCollectionPointColony',
            jsonPath: 'route[0].startingCollectionPoint.location.name',
            label: 'swm.routes.create.colonysociety',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'startingCollectionPointName',
            jsonPath: 'routes[0].startingCollectionPoint.name',
            label: 'swm.routes.create.collectionpoint',
            type: 'text',
            isRequired: true,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'startingCollectionPointDistanceGarbageEstimate',
            jsonPath: 'routes[0].collectionType',
           // label: 'swm.routes.create.collectionType',
            type: 'custom',
            numCols: 6,
            fields : [
              {
                name: 'startingCollectionPointDistance',
                jsonPath: 'routes[0].name',
                label: 'Distance From Last Stop(KMS)',
                checkCustom : true,
                type: 'number',
                add: true,
                isRequired: false,
                isDisabled: false,
                patternErrorMsg: '',
            
              },
              {
                name: 'startingCollectionPointGarbageEstimate',
                jsonPath: 'routes[0].startingCollectionPoint.collectionPointDetails[0].garbageEstimate',
                label: 'Expected Garbage Collection(TONS)',
                checkCustom : true,
                type: 'number',
                isRequired: true,
                isDisabled: false,
                patternErrorMsg: '',
               
              },
            ],
          },
        ],
      },
      {
        name: 'collectionPoints',
        multiple:true,
        label: 'swm.routes.create.group.title.routestops',
        jsonPath: "routes[0].collectionPoints",
        fields: [
          {
            name: 'collectionPointsWard',
            jsonPath: 'routes[0].collectionPoints[0].name',
            label: 'swm.routes.create.ward',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'collectionPointsZone',
            jsonPath: 'routes[0].stops[0].zone',
            label: 'swm.routes.create.zone',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'collectionPointsRoad',
            jsonPath: 'routes[0].stops[0].road',
            label: 'swm.routes.create.road',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'collectionPointsColony',
            jsonPath: 'routes[0].collectionPoints[0].location.name',
            label: 'swm.routes.create.colonysociety',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'collectionPointsName',
            jsonPath: 'routes[0].collectionPoints[0].name',
            label: 'swm.routes.create.collectionpoint',
            type: 'text',
            isRequired: true,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'collectionType',
            jsonPath: 'routes[0].collectionPoints',
            type: 'custom',
            numCols: 6,
            fields : [
              {
                name: 'collectionPointsDistance',
                jsonPath: 'routes[0].name',
                label: 'Distance From Last Stop(KMS)',
                checkCustom : true,
                type: 'number',
                isRequired: false,
                isDisabled: false,
                patternErrorMsg: '',
            
              },
              {
                name: 'collectionPointsGarbageEstimate',
                jsonPath: 'routes[0].collectionPoints[0].collectionType.garbageEstimate',
                label: 'Expected Garbage Collection(TONS)',
                checkCustom : true,
                type: 'number',
                isRequired: true,
                isDisabled: false,
                patternErrorMsg: '',
               
              },
            ],
          },
        ],
      },

      {
        name: 'endingDumpingGroundPoint',
        label: 'swm.routes.create.group.title.end',
        fields: [
          {
            name: 'endingDumpingGroundPointDistance',
            jsonPath: 'routes[0].endingDumpingGroundPoint.isProcessingSite',
            label: 'swm.routes.create.collectionPoint',
            type: 'checkbox',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
          },
          {
            name: 'endingDumpingGroundPointName',
            jsonPath: 'routes[0].endingDumpingGroundPoint.name',
            label: 'swm.routes.create.dumping',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
       
        ],
      },
      {
        name: 'endingCollectionPoint',
        label: '',
        jsonPath: "routes[0].endingCollectionPoint",
        fields: [
          {
            name: 'endingCollectionPointWard',
            jsonPath: 'route[0].endingCollectionPoint.name',
            label: 'swm.routes.create.ward',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'endingCollectionPointZone',
            jsonPath: 'route[0].endingCollectionPoint.name',
            label: 'swm.routes.create.zone',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'endingCollectionPointRoad',
            jsonPath: 'route[0].endingCollectionPoint.name',
            label: 'swm.routes.create.road',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'endingCollectionPointColony',
            jsonPath: 'route[0].endingCollectionPoint.location.name',
            label: 'swm.routes.create.colonysociety',
            type: 'text',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'endingCollectionPointName',
            jsonPath: 'route[0].endingCollectionPoint.name',
            label: 'swm.routes.create.collectionpoint',
            type: 'text',
            isRequired: true,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'code',
            jsonPath: 'routes[0].collectionType',
            type: 'custom',
            numCols: 6,
            fields : [
              {
                name: 'endingCollectionPointDistance',
                jsonPath: 'routes[0].endingDumpingGroundPoint.distanceFromProcessingSite',
                label: 'Distance From Last Stop(KMS)',
                checkCustom : true,
                type: 'number',
                isRequired: false,
                isDisabled: false,
                patternErrorMsg: '',
            
              },
              {
                name: 'endingCollectionPointGarbageEstimate',
                jsonPath: 'routes[0].endingCollectionPoint.collectionPointDetails[0].garbageEstimate',
                label: 'Expected Garbage Collection(TONS)',
                checkCustom : true,
                type: 'number',
                isRequired: true,
                isDisabled: false,
                patternErrorMsg: '',
               
              },
            ],
          },
        ],
      },
      
      {
        name: 'totalDistanceGarbageEstimate',
        label: '',
        fields: [
          {
            name: 'collectionType',
            jsonPath: 'routes',
            type: 'custom',
            numCols: 6,
            fields : [
              {
                name: 'totalDistance',
                jsonPath: 'routes[0].distance',
                label: 'Total Distance Covered(KMS)',
                checkCustom : true,
                type: 'number',
                total:true,
                isRequired: true,
                isDisabled: false,
                patternErrorMsg: '',
              },
              {
                name: 'totalGarbageEstimate',
                jsonPath: 'routes[0].garbageEstimate',
                label: 'Total Expected Garbage(TONS)',
                type: 'number',
                isRequired: true,
                isDisabled: false,
                patternErrorMsg: '',
              },
            ],
          },
        ],
      },
    ],
    url: '/swm-services/routes/_update',
    tenantIdRequired: true,
    searchUrl: '/swm-services/routes/_search?code={code}',
  },

  'swm.view': {
    numCols: 2,
    useTimestamp: true,
    objectName: 'routes',
    title: 'swm.routes.create.title',
    groups: [
      {
        name: 'routeDetails',
        label: '',
        fields: [
          {
            name: 'name',
            jsonPath: 'routes[0].name',
            label: 'swm.routes.create.name',
            type: 'label',
            isRequired: true,
            isDisabled: false,
            patternErrorMsg: '',
          },
          {
            name: 'code',
            jsonPath: 'routes[0].collectionType.code',
            label: 'swm.routes.create.collectionType',
            type: 'label',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
            url:'/egov-mdms-service/v1/_get?&moduleName=swm&masterName=CollectionType|$..code|$..name',
            depedants: [
              {
                jsonPath: 'routes[0].startingCollectionPoint.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}|$..code|$..name',
                autoFillFields: {
                  'routes[0].startingCollectionPoint.name': 'startingCollectionPoint.name',
                },
              },
              {
                jsonPath: 'routes[0].startingCollectionPoint.location.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'routes[0].startingCollectionPoint.location.name': 'location.name',
                },
              },
              {
                jsonPath: 'routes[0].collectionPoints[0].name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'routes[0].collectionPoints[0].name': 'collectionPoints[0].name',
                },
              },
              {
                jsonPath: 'routes[0].collectionPoints[0].location.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'routes[0].collectionPoints[0].location.name': 'location.name',
                },
              },

              {
                jsonPath: 'routes[0].endingDumpingGroundPoint.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'routes[0].endingDumpingGroundPoint.name': 'endingDumpingGroundPoint.name',
                },
              },

              {
                jsonPath: 'route[0].endingCollectionPoint.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'route[0].endingCollectionPoint.name': 'endingCollectionPoint.name',
                },
              },
              {
                jsonPath: 'route[0].endingCollectionPoint.location.name',
                type: 'autoFill',
                pattern: '/swm-services/routes/_search?tenantId=default&code={routes[0].collectionType.code}',
                autoFillFields: {
                  'route[0].endingCollectionPoint.location.name': 'location.name',
                },
              },

            ]
          },
        ]
      },
      {
        name: 'startingCollectionPoint',
        label: 'swm.routes.create.group.title.starting',
        jsonPath: "routes[0].startingCollectionPoint",
        fields: [
          {
            name: 'startingCollectionPointWard',
            jsonPath: 'routes[0].startingCollectionPoint[0].ward',
            label: 'swm.routes.create.ward',
            type: 'label',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'startingCollectionPointZone',
            jsonPath: 'routes[0].startingCollectionPoint[0].zone',
            label: 'swm.routes.create.zone',
            type: 'label',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'startingCollectionPointRoad',
            jsonPath: 'routes[0].startingCollectionPoint[0].road',
            label: 'swm.routes.create.road',
            type: 'label',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'startingCollectionPointColony',
            jsonPath: 'route[0].startingCollectionPoint.location.name',
            label: 'swm.routes.create.colonysociety',
            type: 'label',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'startingCollectionPointName',
            jsonPath: 'routes[0].startingCollectionPoint.name',
            label: 'swm.routes.create.collectionpoint',
            type: 'label',
            isRequired: true,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'startingCollectionPointDistanceGarbageEstimate',
            jsonPath: 'routes[0].collectionType',
           // label: 'swm.routes.create.collectionType',
            type: 'custom',
            numCols: 6,
            fields : [
              {
                name: 'startingCollectionPointDistance',
                jsonPath: 'routes[0].name',
                label: 'Distance From Last Stop(KMS)',
                type: 'label',
                add: true,
                isRequired: false,
                isDisabled: false,
                patternErrorMsg: '',
            
              },
              {
                name: 'startingCollectionPointGarbageEstimate',
                jsonPath: 'routes[0].startingCollectionPoint.collectionPointDetails[0].garbageEstimate',
                label: 'Expected Garbage Collection(TONS)',
                type: 'label',
                isRequired: true,
                isDisabled: false,
                patternErrorMsg: '',
               
              },
            ],
          },
        ],
      },

      {
        name: 'collectionPoints',
        multiple:false,
        label: 'swm.routes.create.group.title.routestops',
        jsonPath: "routes[0].collectionPoints",
        fields: [
          {
            name: 'collectionPointsWard',
            jsonPath: 'routes[0].collectionPoints[0].name',
            label: 'swm.routes.create.ward',
            type: 'label',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'collectionPointsZone',
            jsonPath: 'routes[0].stops[0].zone',
            label: 'swm.routes.create.zone',
            type: 'label',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'collectionPointsRoad',
            jsonPath: 'routes[0].stops[0].road',
            label: 'swm.routes.create.road',
            type: 'label',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'collectionPointsColony',
            jsonPath: 'routes[0].collectionPoints[0].location.name',
            label: 'swm.routes.create.colonysociety',
            type: 'label',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'collectionPointsName',
            jsonPath: 'routes[0].collectionPoints[0].name',
            label: 'swm.routes.create.collectionpoint',
            type: 'label',
            isRequired: true,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'collectionType',
            jsonPath: 'routes[0].collectionType',
            type: 'custom',
            numCols: 6,
            fields : [
              {
                name: 'collectionPointsDistance',
                jsonPath: 'routes[0].name',
                label: 'Distance From Last Stop(KMS)',
                checkCustom : true,
                type: 'label',
                isRequired: false,
                isDisabled: false,
                patternErrorMsg: '',
            
              },
              {
                name: 'collectionPointsGarbageEstimate',
                jsonPath: 'routes[0].collectionPoints[0].collectionType.garbageEstimate',
                label: 'Expected Garbage Collection(TONS)',
                checkCustom : true,
                type: 'label',
                isRequired: true,
                isDisabled: false,
                patternErrorMsg: '',
               
              },
            ],
          },
        ],
      },

      {
        name: 'endingDumpingGroundPoint',
        label: 'swm.routes.create.group.title.end',
        jsonPath: 'routes[0].endingDumpingGroundPoint',
        fields: [
          {
            name: 'endingDumpingGroundPointDistance',
            jsonPath: 'routes[0].endingDumpingGroundPoint.isProcessingSite',
            label: 'swm.routes.create.collectionPoint',
            type: 'checkbox',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
          },
          {
            name: 'endingDumpingGroundPointName',
            jsonPath: 'routes[0].endingDumpingGroundPoint.name',
            label: 'swm.routes.create.dumping',
            type: 'label',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
       
        ],
      },
      {
        name: 'endingCollectionPoint',
        label: '',
        jsonPath: "routes[0].endingCollectionPoint",
        fields: [
          {
            name: 'endingCollectionPointWard',
            jsonPath: 'route[0].endingCollectionPoint.name',
            label: 'swm.routes.create.ward',
            type: 'label',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'endingCollectionPointZone',
            jsonPath: 'route[0].endingCollectionPoint.name',
            label: 'swm.routes.create.zone',
            type: 'label',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'endingCollectionPointRoad',
            jsonPath: 'route[0].endingCollectionPoint.name',
            label: 'swm.routes.create.road',
            type: 'label',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'endingCollectionPointColony',
            jsonPath: 'route[0].endingCollectionPoint.location.name',
            label: 'swm.routes.create.colonysociety',
            type: 'label',
            isRequired: false,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'endingCollectionPointName',
            jsonPath: 'route[0].endingCollectionPoint.name',
            label: 'swm.routes.create.collectionpoint',
            type: 'label',
            isRequired: true,
            isDisabled: true,
            patternErrorMsg: '',
          },
          {
            name: 'code',
            jsonPath: 'routes[0].collectionType',
            type: 'custom',
            numCols: 6,
            fields : [
              {
                name: 'endingCollectionPointDistance',
                jsonPath: 'routes[0].endingDumpingGroundPoint.distanceFromProcessingSite',
                label: 'Distance From Last Stop(KMS)',
                checkCustom : true,
                type: 'label',
                isRequired: false,
                isDisabled: false,
                patternErrorMsg: '',
            
              },
              {
                name: 'endingCollectionPointGarbageEstimate',
                jsonPath: 'routes[0].endingCollectionPoint.collectionPointDetails[0].garbageEstimate',
                label: 'Expected Garbage Collection(TONS)',
                checkCustom : true,
                type: 'label',
                isRequired: true,
                isDisabled: false,
                patternErrorMsg: '',
               
              },
            ],
          },
        ],
      },
      
      {
        name: 'totalDistanceGarbageEstimate',
        label: '',
        fields: [
          {
            name: 'collectionType',
            jsonPath: 'routes[0]',
            type: 'custom',
            numCols: 6,
            fields : [
              {
                name: 'totalDistance',
                jsonPath: 'routes[0].distance',
                label: 'Total Distance Covered(KMS)',
                checkCustom : true,
                type: 'label',
                total:true,
                isRequired: true,
                isDisabled: false,
                patternErrorMsg: '',
              },
              {
                name: 'totalGarbageEstimate',
                jsonPath: 'routes[0].garbageEstimate',
                label: 'Total Expected Garbage(TONS)',
                checkCustom : true,
                type: 'label',
                isRequired: true,
                isDisabled: false,
                patternErrorMsg: '',
              },
            ],
          },
        ],
      },
    ],
    url: '/swm-services/routes/_search?code={code}',
    tenantIdRequired: true,
  },
 
  'swm.search':{
    numCols: 4,
    useTimestamp: true,
    objectName: 'routes',
    url: 'swm-services/routes/_search',
    groups: [
      {
        name: 'VehicleDetails1',
        label: 'swm.vehicles.search.title',
        fields: [
          {
            name: 'name',
            jsonPath: 'name',
            label: 'swm.routes.create.name',
            type: 'text',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
          },
          {
            name: 'endingDumpingGroundPointCode',
            jsonPath: 'endingDumpingGroundPointCode',
            label: 'swm.routes.create.dumping',
            type: 'text',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
          },
          {
            name: 'collectionPointCode',
            jsonPath: 'collectionPointCode',
            label: 'swm.routes.create.dumping',
            type: 'text',
            isRequired: false,
            isDisabled: false,
            patternErrorMsg: '',
          },
        ]
      }
    ],
    result: {
      header: [
        {
          label: 'swm.routes.search.result.name',
        },
        {
          label: 'swm.routes.search.result.collectionPoint',
        },
        {
          label: 'swm.routes.search.result.startingCollectionPoint',
        },
        {
          label: 'swm.routes.search.result.endingPoint',
        },
        {
          label: 'swm.routes.search.result.distance',
        }
      ],
      values: [
        'name',
        'collectionType.name',
        'startingCollectionPoint.name',
        'endingCollectionPoint.name',
        'distance'
      ],
      resultPath: 'routes',
      rowClickUrlUpdate: '/update/swm/routes/{code}',
      rowClickUrlView: '/view/swm/routes/{code}',
    }
  }
};
export default dat;