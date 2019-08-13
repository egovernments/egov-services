import get from "lodash/get";
import {findAndUpdateLocalisation,getDateInRequiredFormat,checkifNullAndSetValue} from "./commons";

var jp = require('jsonpath');

/**
 * 
 * @param {*} req - current module object, picked from request body
 * @param {*} dataconfig  - data config 
 * @param {*} variableTovalueMap - map used for filling values by template engine 'mustache'
 * @param {*} localisationMap - Map to store localisation key, value pair
 * @param {*} requestInfo - request info from request body
 */
export const directMapping=async(req,dataconfig,variableTovalueMap,localisationMap,requestInfo,localisationModuleList)=>{    
    
    var directArr = [];        
    // using jp-jsonpath because loadash can not handele '*'
    var objectOfDirectMapping = jp.query(dataconfig, "$.DataConfigs.mappings.*.mappings.*.direct.*");
    objectOfDirectMapping=checkifNullAndSetValue(objectOfDirectMapping,[],"$.DataConfigs.mappings.*.mappings.*.direct.*");
    directArr = objectOfDirectMapping.map(item => {
      return {
        jPath: item.variable,
        val: checkifNullAndSetValue(jp.query(req,item.value.path),"NA",item.value.path),
        valJsonPath: item.value.path,
        type: item.type,
        format: item.format,
        localisation: item.localisation
      };
    });
  
    for (var i = 0; i < directArr.length; i++) {
      //for array type direct mapping
      if (directArr[i].type == "array") {   
        let arrayOfOwnerObject = [];
        // let ownerObject = JSON.parse(JSON.stringify(get(formatconfig, directArr[i].jPath + "[0]", [])));
        
        let { format = {}, val = [], variable } = directArr[i];
        let { scema = [] } = format;
  
        //taking values about owner from request body
        for (let j = 0; j < val.length; j++) {
          // var x = 1;
          let ownerObject={}
          for (let k = 0; k < scema.length; k++) {
            let fieldValue= get(val[j], scema[k], "NA");
            if(scema[k].toLowerCase().search("date")!="-1")
            {            
              let myDate = new Date(fieldValue);
              if(isNaN(myDate)||(fieldValue===0))
              {
                ownerObject[scema[k]]="NA";
              }
              else
              {
                let replaceValue=getDateInRequiredFormat(fieldValue);      
                // set(formatconfig,externalAPIArray[i].jPath[j].variable,replaceValue);
                ownerObject[scema[k]]=replaceValue;
              }
            }
            else
              ownerObject[scema[k]]=fieldValue;
            // set(ownerObject[x], "text", get(val[j], scema[k], ""));
            // x += 2;
          }
          arrayOfOwnerObject.push(ownerObject);
        }
        // set(formatconfig, directArr[i].jPath, arrayOfOwnerObject);
        variableTovalueMap[directArr[i].jPath]=arrayOfOwnerObject;
      }

      //setting value in pdf for array-column type direct mapping 
      else if(directArr[i].type=="array-column"){        
        let arrayOfBuiltUpDetails=[];
        // let arrayOfFields=get(formatconfig, directArr[i].jPath+"[0]",[]);
        // arrayOfBuiltUpDetails.push(arrayOfFields);
        
        let { format = {}, val = [], variable } = directArr[i];
        let { scema = [] } = format;
        //to get data of multiple floor Built up details       
        for(let j=0;j<val.length;j++)
        {
          let arrayOfItems=[];
          for(let k=0;k<scema.length;k++){
              let fieldValue=get(val[j], scema[k], "NA");
              if(scema[k].toLowerCase().search("date")!="-1")
              {            
                let myDate = new Date(fieldValue);
                if(isNaN(myDate)||(fieldValue===0))
                {
                  arrayOfItems.push("NA");
                }
                else
                {
                  let replaceValue=getDateInRequiredFormat(fieldValue);          
                  // set(formatconfig,externalAPIArray[i].jPath[j].variable,replaceValue);
                  arrayOfItems.push(replaceValue);
                }
              }
              else
                arrayOfItems.push(fieldValue);
          }
          
          arrayOfBuiltUpDetails.push(arrayOfItems);
        }   

        // remove enclosing [ &  ]
        let stringBuildpDetails=JSON.stringify(arrayOfBuiltUpDetails).replace('\[', '');
        stringBuildpDetails=stringBuildpDetails.substring(0,stringBuildpDetails.length-1);

        variableTovalueMap[directArr[i].jPath]=stringBuildpDetails;
        // set(formatconfig,directArr[i].jPath,arrayOfBuiltUpDetails);      
      }
      //setting value in pdf for no type direct mapping
      else 
      {
        directArr[i].val=checkifNullAndSetValue(directArr[i].val,"NA",directArr[i].valJsonPath);
        if((directArr[i].val!=="NA")&&directArr[i].localisation && directArr[i].localisation.required)
            variableTovalueMap[directArr[i].jPath]= await findAndUpdateLocalisation(requestInfo,localisationMap,directArr[i].localisation.prefix,directArr[i].val,directArr[i].localisation.module,localisationModuleList,directArr[i].localisation.isCategoryRequired,directArr[i].localisation.isMainTypeRequired,directArr[i].localisation.isSubTypeNotRequired);
        else if(directArr[i].valJsonPath.toLowerCase().search("date")!="-1")
        {            
          let myDate = new Date(directArr[i].val[0]);
          if(isNaN(myDate)||(directArr[i].val[0]===0))
          {
            variableTovalueMap[directArr[i].jPath]="NA";
          }
          else
          {
            let replaceValue=getDateInRequiredFormat(directArr[i].val[0]);           
            // set(formatconfig,externalAPIArray[i].jPath[j].variable,replaceValue);
            variableTovalueMap[directArr[i].jPath]=replaceValue;
          }
        }
        else
            variableTovalueMap[directArr[i].jPath]=directArr[i].val;
      }
          // set(formatconfig, directArr[i].jPath, directArr[i].val);
    }
}






