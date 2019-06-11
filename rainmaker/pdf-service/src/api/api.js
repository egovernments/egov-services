import axios from 'axios';

// const instance = axios.create({
//     endPoint: "https://egov-micro-dev.egovernments.org/",
//     headers: {
//       "Content-Type": "application/json",
//     }
//   });

export const httpRequest= async(
    endPoint,    
    requestBody,
    headers
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
  console.log(error) ;   
}
    throw errorReponse;
    
}

