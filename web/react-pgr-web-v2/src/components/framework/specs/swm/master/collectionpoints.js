var collectionPointDetails ={
  "name": "collectionPointDetails",
  "version": "v1",
  "level": 2,
  "hide":false,
  "jsonPath": "collectionPoints[0].collectionPointDetails[0]",
  "groups":[{
    "name": "details",
    "multiple":true,
    "jsonPath": "collectionPoints[0].collectionPointDetails[0]",
    "label":"swm.collectionpoints.create.group.title.CollectionPointDetails",
    "fields": [
      {  
        "name":"name",
        "jsonPath":"collectionPoints[0].collectionPointDetails[0].collectionType.code",
        "label":"swm.collectionpoints.create.group.title.CollectionType",
        "type":"singleValueList",
        "isRequired":true,
        "isDisabled":false,
        "maxLength":128,
        "minLength":1,
        "patternErrorMsg":"",
        "url":"/egov-mdms-service/v1/_get?&moduleName=SWM&masterName=CollectionType|$..code|$..name"
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

var dat ={  
  "swm.search":{  
     "numCols":4,
     "useTimestamp":true,
     "objectName":"collectionPoints",
     "url":"/swm-services/collectionpoints/_search",
     "groups":[  
        {  
           "name":"search",
           "label":"swm.collectionpoints.search.title",
           "fields":[  
              {  
                 "name":"name",
                 "jsonPath":"name",
                 "label":"swm.collectionpoints.name",
                 "type":"autoCompelete",
                 "isDisabled":false,
                 "maxLength":256,
                 "patternErrorMsg":"swm.create.field.message.name",
                 "url": "swm-services/collectionpoints/_search?|$.collectionPoints.*.name|$.collectionPoints.*.name"
              }
           ]
        }
     ],
     "result":{  
        "header":[  
           {  
              "label":"swm.collectionpoints.create.name"
           },
           {  
              "label":"swm.collectionpoints.create.colony"
           }
        ],
        "values":[  
           "name",
           "location.name"
        ],
        "resultPath":"collectionPoints",
        "rowClickUrlUpdate":"/update/swm/collectionpoints/{code}",
        "rowClickUrlView":"/view/swm/collectionpoints/{code}"
     }
  },
  "swm.create":{  
     "numCols":3,
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
                  "jsonPath": "collectionPoints[0].location.code",
                  "type": "dropDown",
                  "pattern": "egov-location/boundarys/childLocationsByBoundaryId?tenantId=default&boundaryId={collectionPoints[0].location.block}|$.Boundary.*.code|$.Boundary.*.name"
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
          "label":"",
          "children":[collectionPointDetails],
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
        },
        {
          "name": "BinDetails",
          "label":"swm.collectionpoints.create.group.title.BinDetails",
          "jsonPath":"collectionPoints[0].binDetails[0]",
          "multiple":true,
          "fields": [
            {  
              "name":"assetOrBinId",
              "jsonPath":"collectionPoints[0].binDetails[0].assetOrBinId",
              "label":"swm.collectionpoints.create.assetOrBinId",
              "type":"text",
              "isRequired":true,
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
        }
     ],
     "url":"/swm-services/collectionpoints/_create",
     "tenantIdRequired":true
  },
  "swm.view":{  
     "numCols":3,
     "useTimestamp":true,
     "objectName":"collectionPoints",
     "groups":[  
      {  
        "name":"LocationDetails",
        "label":"swm.collectionpoints.create.group.title.LocationDetails",
        "fields":[  
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
        "label":"",
        "children":[collectionPointDetails],
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
      },
      {
        "name": "BinDetails",
        "label":"swm.collectionpoints.create.group.title.BinDetails",
        "multiple":true,
        "jsonPath":"collectionPoints[0].binDetails[0]",
        "fields": [
          {  
            "name":"assetOrBinId",
            "jsonPath":"collectionPoints[0].binDetails[0].assetOrBinId",
            "label":"swm.collectionpoints.create.assetOrBinId",
            "type":"text",
            "isRequired":true,
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
      }
    ],
     "tenantIdRequired":true,
     "url":"/swm-services/collectionpoints/_search?code={code}"
  },
  "swm.update":{  
     "numCols":3,
     "useTimestamp":true,
     "objectName":"collectionPoints",
     "idJsonPath": "collectionPoints[0].code",
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
        "label":"",
        "children":[collectionPointDetails],
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
      },
      {
        "name": "BinDetails",
        "jsonPath":"collectionPoints[0].binDetails[0]",
        "label":"swm.collectionpoints.create.group.title.BinDetails",
        "multiple":true,
        "fields": [
          {  
            "name":"assetOrBinId",
            "jsonPath":"collectionPoints[0].binDetails[0].assetOrBinId",
            "label":"swm.collectionpoints.create.assetOrBinId",
            "type":"text",
            "isRequired":true,
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
      }
     ],
     "url":"/swm-services/collectionpoints/_update",
     "tenantIdRequired":true,
     "searchUrl":"/swm-services/collectionpoints/_search?code={code}"
  }
}
export default dat;
