
import get from "lodash/get";
import { httpRequest } from "../api/api";
import {findAndUpdateLocalisation,getDateInRequiredFormat,checkifNullAndSetValue} from "./commons";

/**
 * 
 * @param {*} key -name of the key used to identify module configs. Provided request URL
 * @param {*} req -current module object, picked from request body
 * @param {*} dataconfig - data config
 * @param {*} variableTovalueMap -map used for filling values by template engine 'mustache'
 * @param {*} localisationMap -Map to store localisation key, value pair
 * @param {*} requestInfo -request info from request body
 */

export const externalAPIMapping=async function(key,req,dataconfig,variableTovalueMap,localisationMap,requestInfo,localisationModuleList){
var jp = require('jsonpath');

   var objectOfExternalAPI = checkifNullAndSetValue(jp.query(dataconfig, "$.DataConfigs.mappings.*.mappings.*.externalAPI.*"),[],"$.DataConfigs.mappings.*.mappings.*.externalAPI.*");
   var externalAPIArray = objectOfExternalAPI.map(item => {
    return {
      uri: item.path,
      queryParams: item.queryParam,
      jPath: item.responseMapping,
      variable: "",
      val: ""
    };
  });
  for (let i = 0; i < externalAPIArray.length; i++) {
    
    var temp1 = "";
    var temp2 = "";
    var flag = 0;
//to convert queryparam and uri into properURI

  //for PT module
if(key=="pt-receipt")
{
  for (let j = 0; j < externalAPIArray[i].queryParams.length; j++) {
    if (externalAPIArray[i].queryParams[j] == "$") {
      flag = 1;
    }
    if (externalAPIArray[i].queryParams[j] == "," || externalAPIArray[i].queryParams[j] == ":") {
      if (flag == 1) {
        temp2 = temp1;
        // temp1 = temp1.replace("$.", "");
        var temp3 = checkifNullAndSetValue(jp.query(req,temp1),"NA",temp1);          
        externalAPIArray[i].queryParams = externalAPIArray[i].queryParams.replace(temp2, temp3);

        j = 0;
        flag = 0;
        temp1 = "";
        temp2 = "";
      }
    }   
    
    if (flag == 1) {
      temp1 += externalAPIArray[i].queryParams[j];
    }
    if (j == externalAPIArray[i].queryParams.length - 1 && flag == 1) {
      temp2 = temp1;
      // temp1 = temp1.replace("$.", "");
      var temp3 = checkifNullAndSetValue(jp.query(req,temp1),"NA",temp1) ;

      externalAPIArray[i].queryParams = externalAPIArray[i].queryParams.replace(temp2, temp3);

      flag = 0;
      temp1 = "";
      temp2 = "";
    }
  } 
}
  //for other modules
else
{ 
    for (let j = 0; j < externalAPIArray[i].queryParams.length; j++) {
      if (externalAPIArray[i].queryParams[j] == "$") {
        flag = 1;
      }
      if (externalAPIArray[i].queryParams[j] == ",") {
        if (flag == 1) {
          temp2 = temp1;
          // temp1 = temp1.replace("$.", "");
          var temp3 = checkifNullAndSetValue((req,temp1),"NA",temp1);          
          externalAPIArray[i].queryParams = externalAPIArray[i].queryParams.replace(temp2, temp3);

          j = 0;
          flag = 0;
          temp1 = "";
          temp2 = "";
        }
      }
      if (flag == 1) {
        temp1 += externalAPIArray[i].queryParams[j];
      }
      if (j == externalAPIArray[i].queryParams.length - 1 && flag == 1) {
        temp2 = temp1;
        // temp1 = temp1.replace("$.", "");
        var temp3 = checkifNullAndSetValue(jp.query(req,temp1),"NA",temp1);

        externalAPIArray[i].queryParams = externalAPIArray[i].queryParams.replace(temp2, temp3);

        flag = 0;
        temp1 = "";
        temp2 = "";
      }
    }    
  
}
    externalAPIArray[i].queryParams = externalAPIArray[i].queryParams.replace(/,/g,"&");
      // console.log(externalAPIArray[i].queryParams);
    let headers={
      "content-type": "application/json;charset=UTF-8",
      "accept":"application/json, text/plain, */*" 
     }  
    var res = await httpRequest(
        externalAPIArray[i].uri + "?" + externalAPIArray[i].queryParams,
        {"RequestInfo":requestInfo},
        headers
      );     
      
      //putting required data from external API call in format config      
      
      for (let j = 0; j < externalAPIArray[i].jPath.length; j++) {          
        let replaceValue=checkifNullAndSetValue(jp.query(res,externalAPIArray[i].jPath[j].value),"NA",externalAPIArray[i].jPath[j].value);
        let loc=externalAPIArray[i].jPath[j].localisation;
        if((replaceValue!=="NA")&&externalAPIArray[i].jPath[j].localisation && externalAPIArray[i].jPath[j].localisation.required && externalAPIArray[i].jPath[j].localisation.prefix)
          variableTovalueMap[externalAPIArray[i].jPath[j].variable]= await findAndUpdateLocalisation(requestInfo,localisationMap,loc.prefix,replaceValue,loc.module,localisationModuleList,loc.isCategoryRequired,loc.isMainTypeRequired,loc.isSubTypeRequired,loc.delimiter);
        else if(externalAPIArray[i].jPath[j].value && externalAPIArray[i].jPath[j].value.toLowerCase().search("date")!="-1")
        {         
          let myDate = new Date(replaceValue[0]);
          if(isNaN(myDate)||(replaceValue[0]===0))
          {
            variableTovalueMap[externalAPIArray[i].jPath[j].variable]="NA";
          }
          else
          {
            replaceValue=getDateInRequiredFormat(replaceValue[0]);          
            // set(formatconfig,externalAPIArray[i].jPath[j].variable,replaceValue);
            variableTovalueMap[externalAPIArray[i].jPath[j].variable]=replaceValue;
          }
        }  
        else          
          variableTovalueMap[externalAPIArray[i].jPath[j].variable]=replaceValue;
        if(externalAPIArray[i].jPath[j].isUpperCaseRequired)
        {
          let currentValue=variableTovalueMap[externalAPIArray[i].jPath[j].variable];
          // handle case ['value']
          if(((typeof currentValue)=='object')&&(currentValue.length>0))
            currentValue=currentValue[0];

          variableTovalueMap[externalAPIArray[i].jPath[j].variable]=currentValue.toUpperCase();
        }
        }   
  }  
  // return fillValues(variableTovalueMap,formatconfig); 
};
