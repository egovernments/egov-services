var dat = {
    "legal.update": {
        "numCols": 6,
        "url": "/lcms-services/legalcase/opinion/_update",
        "tenantIdRequired": true,
        "useTimestamp": true,
        "objectName": "opinions",
        "searchUrl": "/lcms-services/legalcase/opinion/_search?opinionRequestDate={date}",
        "groups": [
          {
            "label": "opiniontwo.update.group.title.OpinionRequest",
            "name": "entryType",
            "fields": [
               {
                "name": "Opinion  Date",
                "jsonPath": "opinions[0].opinionRequestDate",
                "label": "opiniontwo.update.opinionRequestDate",
                "pattern": "",
                "type": "date",
                "isRequired": false,
                "isDisabled": true,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              },{
                "name": "Department Name",
                "jsonPath": "opinions[0].departmentName",
                "label": "opiniontwo.update.departmentName",
                "pattern": "",
                "type": "singleValueList",
                "isRequired": false,
                "isDisabled": true,
                "requiredErrMsg": "",
                "patternErrMsg": "",
                "url": "/egov-common-masters/departments/_search?|$..code|$..name"
              },{
                "name": "Opinion On",
                "jsonPath": "opinions[0].opinionOn",
                "label": "opiniontwo.update.opinionOn",
                "pattern": "",
                "type": "text",
                "isRequired": false,
                "isDisabled": true,
                "requiredErrMsg": "",
                "patternErrMsg": ""
              }
            ]
          }, {
        "name": "UploadDocument",
        "label": "legal.create.group.title.UploadDocument",
        fields:[{
          "name":"UploadDocument",
          "jsonPath": "opinions[0].documents",
          "label": "legal.create.sectionApplied",
           "type": "fileTable",
            "isRequired": false,
            "isDisabled": true,
            "screenView":true,
            "patternErrMsg": "",
            "fileList":{
                "name":"documentName",
                "id":"fileStoreId"
            }
             // "fileCount":3



        }]
      },{
            "label": "opiniontwo.update.Action",
            "name": "action",
            "fields": [{
                 "name": "Inword Date",
                "jsonPath": "opinions[0].inWardDate",
                "label": "opiniontwo.update.inWardDate",
                "pattern": "",
                "type": "datePicker",
                "isRequired": true,
                "isDisabled": false,
                "requiredErrMsg": "",
                "patternErrMsg": ""
            },
            // {
            //     "name": "Forward To",
            //     "jsonPath": "forwardTo",
            //     "label": "opiniontwo.update.forwardTo",
            //     "pattern": "",
            //     "type": "singleValueList",
            //     "isRequired": true,
            //     "isDisabled": false,
            //     "requiredErrMsg": "",
            //     "patternErrMsg": ""
            //   }
              ]
        }
        ]
      }
}

export default dat;