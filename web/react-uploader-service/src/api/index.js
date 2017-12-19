import axios from "axios";
import * as apiEndpoints from "../constants/ApiEndpoints";
import {
  prepareFormData,
  getRequestUrl,
  fetchFromLocalStorage
} from "../utils";
import * as prepareRequestBody from "./createRequestBody";
import {
  uploadDefinitionsResponse,
  createJobResponse,
  searchUserJobsReponse
} from "./apiResponse";

// application/x-www-form-urlencoded
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

  const httpRequest = async (endPoint, requestBody, headers) => {
    let apiError = "Api Error";
    try {
      const response = await instance.post(endPoint, requestBody);
      const responseStatus = parseInt(response.status);
      if (responseStatus == 200 || responseStatus == 201) {
        return response.data;
      } else {
        apiError =
          response.hasOwnProperty("Errors") && response.Errors.length
            ? response.Errors[0].message
            : apiError;
      }
    } catch (error) {
      apiError = error;
    }
    throw new Error(apiError);
  };

  const fetchUploadDefintions = async () => {
    const response = uploadDefinitionsResponse();
    const uploadDefinitionsRequestBody = prepareRequestBody.uploadDefinitionsRequest(
      authToken
    );
    const endPoint = apiEndpoints.UPLOAD_DEFINITIONS_ENDPOINT;

    return new Promise((resolve, reject) => {
      setTimeout(function() {
        resolve(response.ModuleDefs);
      }, 1000);
    });
  };

  // job search request
  const fetchUserJobs = async (
    codes = [],
    statuses = [],
    startDate,
    endDate
  ) => {
    const response = searchUserJobsReponse();
    const jobSearchRequestBody = prepareRequestBody.jobSearchRequest(
      authToken,
      tenantId,
      codes,
      statuses,
      startDate,
      endDate
    );

    const endPoint = apiEndpoints.SEARCH_USER_JOBS_ENDPOINT;
    console.log(jobSearchRequestBody);

    return new Promise((resolve, reject) => {
      setTimeout(function() {
        resolve(response.UploadJobs);
      }, 1000);
    });
  };

  // upload job request
  const createJob = async (requestFilePath, moduleName, defName) => {
    const response = createJobResponse();
    const createJobRequestBody = prepareRequestBody.jobCreateRequest(
      authToken,
      tenantId,
      requestFilePath,
      moduleName,
      defName
    );
    const endPoint = apiEndpoints.CREATE_JOB_ENDPOINT;
    // create job request body
    console.log(createJobRequestBody);

    return new Promise((resolve, reject) => {
      setTimeout(function() {
        const uploadJobs = response.UploadJobs;
        resolve(uploadJobs.length ? uploadJobs[0].code : null);
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
    const requestBody = prepareFormData(requestParams);
    const endPoint = apiEndpoints.FILE_UPLOAD_ENDPOINT;

    const url = "/filestore/v1/files?tenantId=default";
    const response = await axios.post(url, requestBody, {
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

    return fileStoreId;
  };

  return {
    uploadFile,
    uploadFileMock,
    createJob,
    fetchUserJobs,
    fetchUploadDefintions
  };
};
