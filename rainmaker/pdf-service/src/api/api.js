import axios from 'axios';

const instance = axios.create({
    baseURL: "https://egov-micro-dev.egovernments.org/",
    headers: {
      "Content-Type": "application/json",
    }
  });

export const httpRequest= async(
    endPoint,
    queryObject=[],
    headers=[]
)=>{
    if (headers)
    instance.defaults = Object.assign(instance.defaults, {
      headers,
    });

    /*if (!some(queryObject, ["key", "tenantId"]) && !ignoreTenantId) {
        queryObject &&
          queryObject.push({
            key: "tenantId",
            value: tenantId,
          });
    }*/

//endPoint = addQueryArg(endPoint, queryObject);
try {
   const response =  await instance.post(endPoint);
  const responseStatus = parseInt(response.status, 10);
  if (responseStatus === 200 || responseStatus === 201) {
     //console.log(response.data);
    return response.data;
    }
} catch (error) {
  const { data, status } = error.response;
    console.log(error.response);
    }
}

