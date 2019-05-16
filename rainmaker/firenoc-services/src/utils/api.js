import httpClient from '../config/httpClient'
import { addQueryArg } from "./index";
import {HOST_URL} from '../envVariables'
import axios from 'axios'

console.log("host name", HOST_URL);
const instance = axios.create({
  baseURL: HOST_URL,
  headers: {
    "Content-Type": "application/json"
  }
});

export const httpRequest = async ({
  endPoint,
  queryObject = [],
  requestBody = {},
  headers = [],
  customRequestInfo = {}
}) => {
  let apiError = "Api Error";

  if (headers)
    httpClient.defaults = Object.assign(httpClient.defaults, {
      headers
    });
  endPoint = addQueryArg(endPoint, queryObject);
  try {
    const response = await httpClient.post(endPoint, requestBody);
    const responseStatus = parseInt(response.status, 10);
    if (responseStatus === 200 || responseStatus === 201) {
      return response.data;
    }
  } catch (error) {
    console.log(error)
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
