import httpClient from "../config/httpClient";
import { addQueryArg } from "./index";
import envVariables from "../envVariables";

export const httpRequest = async ({
  hostURL,
  endPoint,
  queryObject = [],
  requestBody = {},
  headers = [],
  customRequestInfo = {}
}) => {
  console.log(hostURL);
  let apiError = "Api Error";
  let instance = httpClient(hostURL);
  if (headers)
    instance.defaults = Object.assign(instance.defaults, {
      headers
    });
  endPoint = addQueryArg(endPoint, queryObject);
  try {
    const response = await instance.post(endPoint, requestBody);
    const responseStatus = parseInt(response.status, 10);
    if (responseStatus === 200 || responseStatus === 201) {
      return response.data;
    }
  } catch (error) {
    const { data, status } = error.response;
    apiError =
      (data.hasOwnProperty("Errors") &&
        data.Errors &&
        data.Errors.length &&
        data.Errors[0].message) ||
      (data.hasOwnProperty("error") &&
        data.error.fields &&
        data.error.fields.length &&
        data.error.fields[0].message) ||
      (data.hasOwnProperty("error_description") && data.error_description) ||
      apiError;
  }
  // unhandled error
  throw new Error(apiError);
};
