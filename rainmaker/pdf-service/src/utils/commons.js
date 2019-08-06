import { httpRequest } from "../api/api";

import envVariables from "../EnvironmentVariables";

let egovLocHost=envVariables.EGOV_LOCALISATION_HOST;
let localisationEndpoint=envVariables.EGOV_LOCALISATION_SERVICE_ENDPOINT;
export const getTransformedLocale = label => {
    return label.toUpperCase().replace(/[.:-\s\/]/g, "_");
  };


export const  findAndUpdateLocalisation=async(requestInfo,localisationMap,key,moduleName)=>{
    let standardisedKey=getTransformedLocale(key);
    if((localisationMap[key]===undefined)&&(localisationMap[standardisedKey]===undefined)){
        var res = await httpRequest(
            `${egovLocHost}${localisationEndpoint}?locale=en_IN&tenantId=pb&module=${moduleName}`,
            requestInfo
            ); 
        res.messages.map(
            item=>{
                localisationMap[item.code]=item.message;   
            }
        );
    }
    if(localisationMap[key])
    {
        return localisationMap[key];
    }
    else if(localisationMap[standardisedKey]){
        return localisationMap[standardisedKey];
    }
    else{
        return key;
    }
  };


export const getDateInRequiredFormat=(date)=>{
    return date.toLocaleDateString('en-GB', {
      day : '2-digit',
      month : '2-digit',
      year : 'numeric'
  }); 
  }

  export const checkifNullAndSetValue=(value,setTo)=>{
    if((value==undefined)||(value==null)||(value.length===0)||((value.length===1)&&(value[0]===null)))
      return setTo;
    else
      return value;
  }