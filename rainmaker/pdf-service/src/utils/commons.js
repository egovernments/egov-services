import { httpRequest } from "../api/api";

export const getTransformedLocale = label => {
    return label.toUpperCase().replace(/[.:-\s\/]/g, "_");
  };


export const  findAndUpdateLocalisation=async(localisationMap,key,moduleName)=>{
    let standardisedKey=getTransformedLocale(key);
    if((localisationMap[key]===undefined)&&(localisationMap[standardisedKey]===undefined)){
        var res = await httpRequest(
            `https://egov-micro-dev.egovernments.org/localization/messages/v1/_search?locale=en_IN&tenantId=pb&module=${moduleName}`
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