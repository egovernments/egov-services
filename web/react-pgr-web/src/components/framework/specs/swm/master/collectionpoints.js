var collectionPointDetails ={
  "name": "collectionPointDetails",
  "version": "v1",
  "level": 1,
  "hide":false,
  "groups":[{
    "name": "details",
    "multiple":true,
    "jsonPath": "collectionPoints[0].collectionPointDetails[0]",
    "label":"swm.collectionpoints.create.group.title.CollectionPoints",
    "fields": [
      {  
        "name":"name",
        "jsonPath":"collectionPoints[0].collectionPointDetails[0].collectionType.code",
        "label":"swm.collectionpoints.create.name",
        "type":"singleValueList",
        "isRequired":true,
        "isDisabled":false,
        "maxLength":128,
        "minLength":1,
        "patternErrorMsg":"",
        "url":"/swm-services/collectionpoints/_search?&|$.collectionPoints.*.collectionPointDetails.*..code|$.collectionPoints.*.collectionPointDetails.*..code"
     },
     {  
        "name":"garbageEstimate",
        "jsonPath":"collectionPoints[0].collectionPointDetails[0].garbageEstimate",
        "label":"swm.collectionpoints.create.garbageEstimate",
        "type":"number",
        "isRequired":true,
        "isDisabled":false,
        "patternErrorMsg":""
     },
     {  
        "name":"description",
        "jsonPath":"collectionPoints[0].collectionPointDetails[0].description",
        "label":"swm.collectionpoints.create.description",
        "type":"textarea",
        "isRequired":false,
        "isDisabled":false,
        "maxLength":300,
        "minLength":15,
        "patternErrorMsg":""
     }
    ]
  }]
}
var binDetails ={
  "name": "collectionPointDetails",
  "version": "v1",
  "level": 1,
  "hide":false,
  "groups":[{
    "name": "details",
    "label":"swm.collectionpoints.create.group.title.BinDetails",
    "jsonPath": "collectionPoints[0].binDetails[0]",
    "multiple":true,
    "fields": [
      {  
        "name":"assetOrBinId",
        "jsonPath":"collectionPoints[0].binDetails[0].assetOrBinId",
        "label":"swm.collectionpoints.create.assetOrBinId",
        "type":"text",
        "isRequired":false,
        "isDisabled":false,
        "maxLength":256,
        "minLength":5,
        "patternErrorMsg":""
     },
     {  
        "name":"rfidAssigned",
        "jsonPath":"collectionPoints[0].binDetails[0].rfidAssigned",
        "label":"swm.collectionpoints.create.rfidAssigned",
        "type":"checkbox",
        "isRequired":false,
        "isDisabled":false,
        "patternErrorMsg":"",
        "defaultValue": false,
        "showHideFields": [{
          "ifValue": true,
          "hide": [],
          "show": [{
            "name": "rfid",
            "isGroup": false,
            "isField": true
          }]
        }]
     },
     {  
        "name":"rfid",
        "hide": true,
        "jsonPath":"collectionPoints[0].binDetails[0].rfid",
        "label":"swm.collectionpoints.create.rfid",
        "type":"text",
        "isRequired":false,
        "isDisabled":false,
        "maxLength":256,
        "minLength":1,
        "patternErrorMsg":""
     },
     {  
        "name":"latitude",
        "jsonPath":"collectionPoints[0].binDetails[0].latitude",
        "label":"ac.create.Latitude",
        "type":"number",
        "isRequired":false,
        "isDisabled":false,
        "patternErrorMsg":""
     },
     {  
        "name":"longitude",
        "jsonPath":"collectionPoints[0].binDetails[0].longitude",
        "label":"ac.create.Longitude",
        "type":"number",
        "isRequired":false,
        "isDisabled":false,
        "patternErrorMsg":""
     }
    ]
  }]
}

var dat ={  
  "swm.search":{  
     "numCols":4,
     "useTimestamp":true,
     "objectName":"",
     "url":"/swm-services/collectionpoints/_search",
     "groups":[  
        {  
           "name":"search",
           "label":"swm.search.title",
           "fields":[  
              {  
                 "label":"swm.create.undefined",
                 "type":"",
                 "isDisabled":false,
                 "patternErrorMsg":"swm.create.field.message.undefined"
              },
              {  
                 "name":"codes",
                 "jsonPath":"codes",
                 "label":"swm.create.codes",
                 "type":"",
                 "isDisabled":false,
                 "patternErrorMsg":"swm.create.field.message.codes"
              },
              {  
                 "name":"name",
                 "jsonPath":"name",
                 "label":"swm.create.name",
                 "type":"singleValueList",
                 "isDisabled":false,
                 "maxLength":256,
                 "patternErrorMsg":"swm.create.field.message.name"
              },
              {  
                 "name":"locationCode",
                 "jsonPath":"locationCode",
                 "label":"swm.create.locationCode",
                 "type":"singleValueList",
                 "isDisabled":false,
                 "maxLength":256,
                 "patternErrorMsg":"swm.create.field.message.locationCode"
              },
              {  
                 "name":"offSet",
                 "jsonPath":"offSet",
                 "label":"swm.create.offSet",
                 "type":"number",
                 "isDisabled":false,
                 "patternErrorMsg":"swm.create.field.message.offSet"
              },
              {  
                 "name":"sortBy",
                 "jsonPath":"sortBy",
                 "label":"swm.create.sortBy",
                 "type":"text",
                 "isDisabled":false,
                 "patternErrorMsg":"swm.create.field.message.sortBy"
              }
           ]
        }
     ],
     "result":{  
        "header":[  
           {  
              "label":"swm.search.result.code"
           },
           {  
              "label":"swm.search.result.name"
           }
        ],
        "values":[  
           "code",
           "name"
        ],
        "resultPath":"collectionpoints",
        "rowClickUrlUpdate":"/update/collectionpoints/{code}",
        "rowClickUrlView":"/view/collectionpoints/{code}"
     }
  },
  "swm.create":{  
     "numCols":4,
     "useTimestamp":true,
     "objectName":"collectionPoints",
     "idJsonPath": "collectionPoints[0].code",
     "title": "swm.collectionpoints.create.title",
     "groups":[  
        {  
          "name":"LocationDetails",
          "label":"swm.collectionpoints.create.group.title.LocationDetails",
          "fields":[  
            {  
              "name":"Ward",
              "jsonPath":"collectionPoints[0].location.ward",
              "label":"swm.collectionpoints.create.ward",
              "type":"singleValueList",
              "isRequired":true,
              "isDisabled":false,
              "maxLength":128,
              "url": "egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?tenantId=default&boundaryTypeName=Ward&hierarchyTypeName=REVENUE|$.Boundary.*.id|$.Boundary.*.name",
              "minLength":1,
              "patternErrorMsg":"",
              "depedants": [{
                "jsonPath": "collectionPoints[0].location.zone",
                "type": "dropDown",
                "pattern": "egov-location/boundarys/childLocationsByBoundaryId?tenantId=default&boundaryId={collectionPoints[0].location.ward}|$.Boundary.*.id|$.Boundary.*.name"
              }]
            },
            {  
                "name":"Zone",
                "jsonPath":"collectionPoints[0].location.zone",
                "label":"swm.collectionpoints.create.zone",
                "type":"singleValueList",
                "isRequired":true,
                "isDisabled":false,
                "maxLength":128,
                "minLength":1,
                "patternErrorMsg":"",
                "depedants": [{
                  "jsonPath": "collectionPoints[0].location.block",
                  "type": "dropDown",
                  "pattern": "egov-location/boundarys/childLocationsByBoundaryId?tenantId=default&boundaryId={collectionPoints[0].location.code}|$.Boundary.*.id|$.Boundary.*.name"
                }]
            },
            {  
                "name":"Road/Street",
                "jsonPath":"collectionPoints[0].location.block",
                "label":"swm.collectionpoints.create.block",
                "type":"singleValueList",
                "isRequired":true,
                "isDisabled":false,
                "maxLength":128,
                "minLength":1,
                "patternErrorMsg":"",
                "depedants": [{
                  "jsonPath": "collectionPoints[0].location.code",
                  "type": "dropDown",
                  "pattern": "egov-location/boundarys/childLocationsByBoundaryId?tenantId=default&boundaryId={collectionPoints[0].location.block}|$.Boundary.*.id|$.Boundary.*.name"
                }]
            },
            {  
                "name":"Colony",
                "jsonPath":"collectionPoints[0].location.code",
                "label":"swm.collectionpoints.create.colony",
                "type":"singleValueList",
                "isRequired":true,
                "isDisabled":false,
                "maxLength":128,
                "minLength":1,
                "patternErrorMsg":""
            }
          ]
        },
        {  
          "name":"CollectionPointDetails",
          "label":"swm.collectionpoints.create.group.title.CollectionPointDetails",
          "children":[collectionPointDetails,binDetails],
          "fields":[  
            {  
                "name":"name",
                "jsonPath":"collectionPoints[0].name",
                "label":"swm.collectionpoints.create.name",
                "type":"text",
                "isRequired":true,
                "isDisabled":false,
                "maxLength":128,
                "minLength":1,
                "patternErrorMsg":""
            }
          ]
        }
     ],
     "url":"/swm-services/collectionpoints/_create",
     "tenantIdRequired":true
  },
  "swm.view":{  
     "numCols":4,
     "useTimestamp":true,
     "objectName":"collectionPoints",
     "groups":[  
      {  
        "name":"LocationDetails",
        "label":"swm.collectionpoints.create.group.title.LocationDetails",
        "fields":[  
          {  
            "name":"Ward",
            "jsonPath":"collectionPoints[0].location.ward",
            "label":"swm.collectionpoints.create.ward",
            "type":"singleValueList",
            "isRequired":true,
            "isDisabled":false,
            "maxLength":128,
            "url": "egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?tenantId=default&boundaryTypeName=Ward&hierarchyTypeName=REVENUE|$.Boundary.*.id|$.Boundary.*.name",
            "minLength":1,
            "patternErrorMsg":"",
            "depedants": [{
              "jsonPath": "collectionPoints[0].location.zone",
              "type": "dropDown",
              "pattern": "egov-location/boundarys/childLocationsByBoundaryId?tenantId=default&boundaryId={collectionPoints[0].location.ward}|$.Boundary.*.id|$.Boundary.*.name"
            }]
          },
          {  
              "name":"Zone",
              "jsonPath":"collectionPoints[0].location.zone",
              "label":"swm.collectionpoints.create.zone",
              "type":"singleValueList",
              "isRequired":true,
              "isDisabled":false,
              "maxLength":128,
              "minLength":1,
              "patternErrorMsg":"",
              "depedants": [{
                "jsonPath": "collectionPoints[0].location.block",
                "type": "dropDown",
                "pattern": "egov-location/boundarys/childLocationsByBoundaryId?tenantId=default&boundaryId={collectionPoints[0].location.zone}|$.Boundary.*.id|$.Boundary.*.name"
              }]
          },
          {  
              "name":"Road/Street",
              "jsonPath":"collectionPoints[0].location.block",
              "label":"swm.collectionpoints.create.block",
              "type":"singleValueList",
              "isRequired":true,
              "isDisabled":false,
              "maxLength":128,
              "minLength":1,
              "patternErrorMsg":"",
              "depedants": [{
                "jsonPath": "collectionPoints[0].location.name",
                "type": "dropDown",
                "pattern": "egov-location/boundarys/childLocationsByBoundaryId?tenantId=default&boundaryId={collectionPoints[0].location.block}|$.Boundary.*.id|$.Boundary.*.name"
              }]
          },
          {  
              "name":"Colony",
              "jsonPath":"collectionPoints[0].location.name",
              "label":"swm.collectionpoints.create.colony",
              "type":"singleValueList",
              "isRequired":true,
              "isDisabled":false,
              "maxLength":128,
              "minLength":1,
              "patternErrorMsg":""
          }
        ]
      },
      {  
        "name":"CollectionPointDetails",
        "label":"swm.collectionpoints.create.group.title.CollectionPointDetails",
        "children":[collectionPointDetails,binDetails],
        "fields":[  
          {  
              "name":"name",
              "jsonPath":"collectionPoints[0].name",
              "label":"swm.collectionpoints.create.name",
              "type":"text",
              "isRequired":true,
              "isDisabled":false,
              "maxLength":128,
              "minLength":1,
              "patternErrorMsg":""
          }
        ]
      }
     ],
     "tenantIdRequired":true,
     "url":"/swm-services/collectionpoints/_search?code={code}"
  },
  "swm.update":{  
     "numCols":4,
     "useTimestamp":true,
     "objectName":"collectionPoints",
     "groups":[  
      {  
        "name":"LocationDetails",
        "label":"swm.collectionpoints.create.group.title.LocationDetails",
        "fields":[  
          {  
            "name":"Ward",
            "jsonPath":"collectionPoints[0].location.ward",
            "label":"swm.collectionpoints.create.ward",
            "type":"singleValueList",
            "isRequired":true,
            "isDisabled":false,
            "maxLength":128,
            "url": "egov-location/boundarys/boundariesByBndryTypeNameAndHierarchyTypeName?tenantId=default&boundaryTypeName=Ward&hierarchyTypeName=REVENUE|$.Boundary.*.id|$.Boundary.*.name",
            "minLength":1,
            "patternErrorMsg":"",
            "depedants": [{
              "jsonPath": "collectionPoints[0].location.zone",
              "type": "dropDown",
              "pattern": "egov-location/boundarys/childLocationsByBoundaryId?tenantId=default&boundaryId={collectionPoints[0].location.ward}|$.Boundary.*.id|$.Boundary.*.name"
            }]
          },
          {  
              "name":"Zone",
              "jsonPath":"collectionPoints[0].location.zone",
              "label":"swm.collectionpoints.create.zone",
              "type":"singleValueList",
              "isRequired":true,
              "isDisabled":false,
              "maxLength":128,
              "minLength":1,
              "patternErrorMsg":"",
              "depedants": [{
                "jsonPath": "collectionPoints[0].location.block",
                "type": "dropDown",
                "pattern": "egov-location/boundarys/childLocationsByBoundaryId?tenantId=default&boundaryId={collectionPoints[0].location.zone}|$.Boundary.*.id|$.Boundary.*.name"
              }]
          },
          {  
              "name":"Road/Street",
              "jsonPath":"collectionPoints[0].location.block",
              "label":"swm.collectionpoints.create.block",
              "type":"singleValueList",
              "isRequired":true,
              "isDisabled":false,
              "maxLength":128,
              "minLength":1,
              "patternErrorMsg":"",
              "depedants": [{
                "jsonPath": "collectionPoints[0].location.name",
                "type": "dropDown",
                "pattern": "egov-location/boundarys/childLocationsByBoundaryId?tenantId=default&boundaryId={collectionPoints[0].location.block}|$.Boundary.*.id|$.Boundary.*.name"
              }]
          },
          {  
              "name":"Colony",
              "jsonPath":"collectionPoints[0].location.name",
              "label":"swm.collectionpoints.create.colony",
              "type":"singleValueList",
              "isRequired":true,
              "isDisabled":false,
              "maxLength":128,
              "minLength":1,
              "patternErrorMsg":""
          }
        ]
      },
      {  
        "name":"CollectionPointDetails",
        "label":"swm.collectionpoints.create.group.title.CollectionPointDetails",
        "children":[collectionPointDetails,binDetails],
        "fields":[  
          {  
              "name":"name",
              "jsonPath":"collectionPoints[0].name",
              "label":"swm.collectionpoints.create.name",
              "type":"text",
              "isRequired":true,
              "isDisabled":false,
              "maxLength":128,
              "minLength":1,
              "patternErrorMsg":""
          }
        ]
      }
     ],
     "url":"/swm-services/collectionpoints/_update",
     "tenantIdRequired":true,
     "searchUrl":"/swm-services/collectionpoints/_search?code={code}"
  }
}
export default dat;