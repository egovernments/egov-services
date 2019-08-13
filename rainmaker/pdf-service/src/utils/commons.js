import { httpRequest } from "../api/api";

import envVariables from "../EnvironmentVariables";

let egovLocHost=envVariables.EGOV_LOCALISATION_HOST;
let localisationEndpoint=envVariables.EGOV_LOCALISATION_SERVICE_ENDPOINT;
export const getTransformedLocale = label => {
    return label.toUpperCase().replace(/[.:-\s\/]/g, "_");
  };

/**
 * This function returns localisation label from keys based on needs
 * This function does optimisation to fetch one module localisation values only once
 * @param {*} requestInfo - requestinfo from client
 * @param {*} localisationMap - localisation map containing localisation key,label fetched till now
 * @param {*} prefix - prefix to be added before key before fetching localisation ex:-"MODULE_NAME_MASTER_NAME"
 * @param {*} key - key to fetch localisation
 * @param {*} moduleName - "module name for fetching localisation"
 * @param {*} localisationModuleList - "list of modules for which localisation was already fetched"
 * @param {*} isCategoryRequired - ex:- "GOODS_RETAIL_TST-1" = get "GOODS"
 * @param {*} isMainTypeRequired  - ex:- "GOODS_RETAIL_TST-1" = get "RETAIL"
 * @param {*} isSubTypeNotRequired  - ignore fetching localisation from full key
 */
export const  findAndUpdateLocalisation=async(requestInfo,localisationMap,prefix,key,moduleName,localisationModuleList,isCategoryRequired,isMainTypeRequired,isSubTypeNotRequired)=>{
    let keyArray=[];
    let localisedLabels=[]
    let isArray=false;
    if((typeof key)=="string")
    {
      keyArray.push(key);
    }
    else
    {
      keyArray=key;
      isArray=true;
    }

    if(!localisationModuleList.includes(moduleName)){
      var res = await httpRequest(
          `${egovLocHost}${localisationEndpoint}?locale=en_IN&tenantId=pb&module=${moduleName}`,
          {"RequestInfo":requestInfo}
          ); 
      res.messages.map(
          item=>{
              localisationMap[item.code]=item.message;   
          }
      );
      localisationModuleList.push(moduleName);
    }
    keyArray.map(item=>{

      let labelFromKey="";
      
      // append main category in the beginning
      if(isCategoryRequired)
      {
        labelFromKey=getLocalisationLabel(item.split(".")[0],localisationMap,prefix);
      }

      if(isMainTypeRequired)
      {
        if(isCategoryRequired)
          labelFromKey=`${labelFromKey} / `;
        labelFromKey=getLocalisationLabel(item.split(".")[1],localisationMap,prefix);
      }
      
      if(isSubTypeNotRequired!=true)
      {
          if(isMainTypeRequired || isCategoryRequired)
            labelFromKey=`${labelFromKey} / `;
          labelFromKey=`${labelFromKey}${getLocalisationLabel(item,localisationMap,prefix)}`;
      }

      localisedLabels.push(labelFromKey===""?"NA":labelFromKey);
    });
    if(isArray)
    {
      return localisedLabels;
    }
    return localisedLabels[0];
  };

const getLocalisationLabel=(key,localisationMap,prefix)=>{
  if(prefix!=undefined)
  {
    key=`${prefix}_${key}`;
  }
  key=getTransformedLocale(key);
  if(localisationMap[key]){
      return localisationMap[key];
  } 
  else{
    console.log("no localisation value found for key",key);
      return "NA";
  }
}

export const getDateInRequiredFormat = et => {
    if (!et) return "NA";
    var date = new Date(Math.round(Number(et)));
    var formattedDate =
      date.getDate() + "/" + (date.getMonth() + 1) + "/" + date.getFullYear();
    return formattedDate;
  };

// export const getDateInRequiredFormat=(date)=>{
//     return date.toLocaleDateString('en-GB', {
//       month : '2-digit',
//       day : '2-digit',
//       year : 'numeric'
//   }); 
//   }

/**
 * 
 * @param {*} value - values to be checked
 * @param {*} setTo - default value
 * @param {*} path  - jsonpath from where the value was fetched
 */
  export const checkifNullAndSetValue=(value,setTo,path)=>{
    if((value==undefined)||(value==null)||(value.length===0)||((value.length===1)&&(value[0]===null)))
   {
    console.log("no value found for path",path);
    return setTo;
   }
    else
      return value;
  }