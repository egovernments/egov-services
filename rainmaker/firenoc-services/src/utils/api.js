//temropvory solution of adding again here dontenv dependencies
require("dotenv").config();
import axios from "axios";
import { addQueryArg } from "./index";
import some from "lodash/some";

console.log("host name",process.env.HOST_URL);
const instance = axios.create({
  baseURL: process.env.HOST_URL,
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
