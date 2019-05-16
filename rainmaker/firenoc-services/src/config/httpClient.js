import axios from "axios";
import logger from "./logger";
import {HOST_URL, HTTP_CLIENT_DETAILED_LOGGING_ENABLED} from '../envVariables'

// Remove host url
const instance = axios.create({
  baseURL: HOST_URL ,
  headers: {
    "Content-Type": "application/json"
  }
});

// Add interceptor to log before request is made
instance.interceptors.request.use(
  function(config) {
    logRequest(config);
    return config;
  },
  function(error) {
      logErrorResponse(error)
    // Do something with request error
    return Promise.reject(error);
  }
);

// Add interceptor to log after response is received
instance.interceptors.response.use(
  function(response) {
    // Do something with response data
    logResponse(response);
    return response;
  },
  function(error) {
    //   logErrorResponse(error)
    // Do something with response error
    return Promise.reject(error);
  }
);

function logRequest(config){
    const {url, method, data} = config;
    if(HTTP_CLIENT_DETAILED_LOGGING_ENABLED){
        logger.info(`Sending request to ${url} with verb ${method} with body ${JSON.stringify(data)}`);
    }
    else{
        logger.info(`Sending request to ${url} with verb ${method}`)
    }
}

function logResponse(res){
    const {status, headers, data, config} = res;
    if(HTTP_CLIENT_DETAILED_LOGGING_ENABLED && headers['content-type'] && headers['content-type'].includes('application/json')){
        logger.info(`Received from ${config.url} response code ${status} and body ${JSON.stringify(data)}: `);
    }
    else{
        logger.info(`Received response from ${config.url}`);
    }
}

function logErrorResponse(error){
    // if (error.response) {
    //     // The request was made and the server responded with a status code
    //     // that falls out of the range of 2xx
    //     console.log(error.response.data);
    //     console.log(error.response.status);
    //     console.log(error.response.headers);
    //   } else if (error.request) {
    //     // The request was made but no response was received
    //     // `error.request` is an instance of XMLHttpRequest in the browser and an instance of
    //     // http.ClientRequest in node.js
    //     console.log(error.request);
    //   } else {
    //     // Something happened in setting up the request that triggered an Error
    //     console.log('Error', error.message);
    //   }
    //   console.log(error.config);
}

export default instance;
