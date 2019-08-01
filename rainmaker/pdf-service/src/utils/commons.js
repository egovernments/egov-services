import { httpRequest } from "../api/api";

import envVariables from "../EnvironmentVariables";

let egovHost=envVariables.EGOV_HOST;
let localisationEndpoint=envVariables.EGOV_LOCALISATION_SERVICE_ENDPOINT;
export const getTransformedLocale = label => {
    return label.toUpperCase().replace(/[.:-\s\/]/g, "_");
  };


export const  findAndUpdateLocalisation=async(localisationMap,key,moduleName)=>{
    let standardisedKey=getTransformedLocale(key);
    if((localisationMap[key]===undefined)&&(localisationMap[standardisedKey]===undefined)){
        var res = await httpRequest(
            `${egovHost}${localisationEndpoint}?locale=en_IN&tenantId=pb&module=${moduleName}`
          
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