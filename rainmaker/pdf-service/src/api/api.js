import axios from 'axios';

// const instance = axios.create({
//     endPoint: "https://egov-micro-dev.egovernments.org/",
//     headers: {
//       "Content-Type": "application/json",
//     }
//   });

export const httpRequest= async(
    endPoint,    
    requestBody=apiRequest,
    headers=defaultheader
)=>{
    let instance=axios.create({
      endPoint:endPoint,
      requestBody:requestBody
      
    })
    if (headers)
    instance.defaults = Object.assign(instance.defaults, {
      headers,
    });
    
try {
   const response =  await instance.post(endPoint,requestBody);
  const responseStatus = parseInt(response.status, 10);
  if (responseStatus === 200 || responseStatus === 201) {
     //console.log(response.data);
    return response.data;
    }
} catch (error) {
  var errorReponse = error.response;
  console.log(errorReponse) ; 
  throw {message:"error occured while making request to "+endPoint+": response returned by call :"+parseInt(errorReponse.status, 10)};  
}
    
}

const apiRequest = {
  RequestInfo: {
      action: "",
      apiId: "Rainmaker",
      authToken: "67bbb246-b035-4323-9e4a-216d519e60b7",
      did: "1",
      key: "",
      msgId: "20170310130900|en_IN",
      requesterId:   "",
      ver: ".01"
     }
  }


  const defaultheader={
    "content-type": "application/json;charset=UTF-8",
    "accept":"application/json, text/plain, */*" 
   }

