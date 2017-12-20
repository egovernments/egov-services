import axios from "axios";
import * as apiEndpoints from "../constants/ApiEndpoints";
import {
  prepareFormData,
  getRequestUrl,
  fetchFromLocalStorage,
  persistInLocalStorage
} from "../utils";
import * as prepareRequestBody from "./createRequestBody";

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
      if (responseStatus === 200 || responseStatus === 201) {
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
    const requestBody = prepareRequestBody.uploadDefinitionsRequest(authToken);
    const endPoint = apiEndpoints.UPLOAD_DEFINITIONS_ENDPOINT;

    try {
      const response = await httpRequest(endPoint, requestBody);
      return response.ModuleDefs;
    } catch (error) {
      throw new Error(error);
    }
  };

  // job search request
  const fetchUserJobs = async (
    codes = [],
    statuses = [],
    startDate,
    endDate
  ) => {
    const requestBody = prepareRequestBody.jobSearchRequest(
      authToken,
      tenantId,
      codes,
      statuses,
      startDate,
      endDate
    );

    const endPoint = apiEndpoints.SEARCH_USER_JOBS_ENDPOINT;
    try {
      const response = await httpRequest(endPoint, requestBody);
      return response.uploadJobs;
    } catch (error) {
      throw new Error(error);
    }
  };

  // upload job request
  const createJob = async (requestFilePath, moduleName, defName) => {
    const requestBody = prepareRequestBody.jobCreateRequest(
      authToken,
      tenantId,
      requestFilePath,
      moduleName,
      defName
    );
    const endPoint = apiEndpoints.CREATE_JOB_ENDPOINT;

    try {
      const response = await httpRequest(endPoint, requestBody);
      const { uploadJobs } = response;
      const jobId = uploadJobs.length ? uploadJobs[0].code : null;
      return jobId;
    } catch (error) {
      throw new Error(error);
    }
  };

  const loginUser = async (username, password) => {
    const grant_type = "password";
    const scope = "read";
    const requestParams = { tenantId, username, password, scope, grant_type };
    const headers = {
      "Content-Type": "application/x-www-form-urlencoded",
      Authorization: "Basic ZWdvdi11c2VyLWNsaWVudDplZ292LXVzZXItc2VjcmV0"
    };
    const endPoint = getRequestUrl(
      apiEndpoints.USER_LOGIN_ENDPOINT,
      requestParams
    );
    const response = await axios.post(endPoint, {}, { headers });
    const responseObj = {};
    responseObj["token"] = response.data.access_token;
    responseObj["userRequest"] = JSON.stringify(response.data.UserRequest);
    responseObj["type"] = response.data.UserRequest.type;
    responseObj["id"] = response.data.UserRequest.id;
    responseObj["tenantId"] = response.data.UserRequest.tenantId;
    responseObj["refresh-token"] = response.data.refresh_token;
    responseObj["expires-in"] = response.data.expires_in;

    // persist the results in local storage
    persistInLocalStorage(responseObj);
    return response;
  };

  // try to make a generic api call for this
  const uploadFile = async (module, file) => {
    const requestParams = { tenantId, module, file };
    const requestBody = prepareFormData(requestParams);
    const endPoint = getRequestUrl(apiEndpoints.FILE_UPLOAD_ENDPOINT, {
      tenantId
    });

    const response = await axios.post(endPoint, requestBody, {
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
    loginUser,
    uploadFile,
    createJob,
    fetchUserJobs,
    fetchUploadDefintions
  };
};
