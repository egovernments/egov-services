import axios from "axios";
import * as apiEndpoints from "../constants/ApiEndpoints";
import { prepareFormData, fetchFromLocalStorage } from "../utils";
import * as requestObjects from "./requestObjects";
import {
  uploadDefinitionsResponse,
  createJobResponse,
  searchUserJobsReponse
} from "./apiResponse";

// application/json
// application/x-www-form-urlencoded
// required for login only
// Authorization: "Basic ZWdvdi11c2VyLWNsaWVudDplZ292LXVzZXItc2VjcmV0

export const Api = () => {
  const instance = axios.create({
    baseURL: window.location.origin,
    headers: {
      "Content-Type": "application/json"
    }
  });

  const authToken = fetchFromLocalStorage("token");
  const tenantId = fetchFromLocalStorage("tenantId");

  const httpRequest = async (endPoint, requestBody) => {
    const response = await instance.post(endPoint, requestBody);
    return response;
  };

  //doesn't take any input parameters
  const fetchUploadDefintions = async () => {
    const response = uploadDefinitionsResponse();
    return new Promise((resolve, reject) => {
      setTimeout(function() {
        resolve(response.ModuleDefs);
      }, 1000);
    });
  };

  // job search request
  const fetchUserJobs = async (codes = [], statuses = [], fromDate, toDate) => {
    const response = searchUserJobsReponse();
    return new Promise((resolve, reject) => {
      setTimeout(function() {
        resolve(response.UploadJobs);
      }, 1000);
    });
  };

  // upload job request
  const createJob = async (requestFilePath, moduleName, defName) => {
    const response = createJobResponse();
    return new Promise((resolve, reject) => {
      setTimeout(function() {
        const jobStatusId = "acds133";
        resolve(response.UploadJobs);
      }, 1000);
    });
  };

  const uploadFileMock = async (module, file) => {
    return new Promise((resolve, reject) => {
      setTimeout(function() {
        const fileStoreId = "2323424243";
        resolve(fileStoreId);
      }, 1000);
    });
  };

  // upload file API
  const uploadFile = async (module, file) => {
    const requestParams = { tenantId, module, file };
    const formData = prepareFormData(requestParams);
    const endPoint = apiEndpoints.FILE_UPLOAD_ENDPOINT;

    const url = "/filestore/v1/files?tenantId=default";
    const response = await axios.post(url, formData, {
      headers: {
        "Content-Type": "multipart/form-data"
      }
    });

    const responseStatus = parseInt(response.status);
    let fileStoreId = false;

    if (responseStatus === 201) {
      // this has to be sent
      const responseData = response.data;
      fileStoreId = responseData.files.length
        ? responseData.files[0].fileStoreId
        : false;
    }

    console.log(fileStoreId);
  };

  return {
    uploadFile,
    uploadFileMock,
    createJob,
    fetchUserJobs,
    fetchUploadDefintions
  };
};
