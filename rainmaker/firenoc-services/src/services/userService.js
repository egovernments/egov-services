import envVariables from "../envVariables";
import { httpRequest } from "../utils/api";
import moment from "moment";

export const searchUser = async (requestInfo, userSearchReqCriteria) => {
  let requestBody = { RequestInfo: requestInfo, ...userSearchReqCriteria };
  try {
    var userSearchResponse = await httpRequest({
      hostURL: envVariables.EGOV_USER_HOST,
      endPoint: `${envVariables.EGOV_USER_CONTEXT_PATH}${
        envVariables.EGOV_USER_SEARCH_ENDPOINT
      }`,
      requestBody
    });
  } catch (error) {
    throw `Search User error ${error}`;
  }
  var dobFormat = "yyyy-MM-dd";
  userSearchResponse = parseResponse(userSearchResponse, dobFormat);
  return userSearchResponse;
};

export const createUser = async (requestInfo, user) => {
  try {
    let requestBody = { RequestInfo: body.RequestInfo, user: user };
    var userCreateResponse = await httpRequest({
      hostURL: envVariables.EGOV_USER_HOST,
      endPoint: `${envVariables.EGOV_USER_CONTEXT_PATH}${
        envVariables.EGOV_USER_SEARCH_ENDPOINT
      }`,
      requestBody
    });
  } catch (error) {
    throw `Create User error ${error}`;
  }
  var dobFormat = "dd/MM/yyyy";
  userCreateResponse = parseResponse(userSearchResponse, dobFormat);

  return userCreateResponse;
};

export const updateUser = async (requestInfo, user) => {
  try {
    let requestBody = { RequestInfo: body.RequestInfo, user: [user] };
    var userUpdateResponse = await httpRequest({
      hostURL: envVariables.EGOV_USER_HOST,
      endPoint: `${envVariables.EGOV_USER_CONTEXT_PATH}${
        envVariables.EGOV_USER_SEARCH_ENDPOINT
      }`,
      requestBody
    });
  } catch (error) {
    throw `Update User error ${error}`;
  }
  var dobFormat = "yyyy-MM-dd";
  userUpdateResponse = parseResponse(userSearchResponse, dobFormat);

  return userUpdateResponse;
};

const parseResponse = (userResponse, dobFormat) => {
  var format1 = "dd-MM-yyyy HH:mm:ss";
  userResponse.user &&
    userResponse.user.map(user => {
      user.createdDate =
        user.createdDate && dateToLong(user.createdDate, format1);
      user.lastModifiedDate =
        user.lastModifiedDate && dateToLong(user.lastModifiedDate, format1);
      user.dob = user.dob && dateToLong(user.dob, dobFormat);
      user.pwdExpiryDate =
        user.pwdExpiryDate && dateToLong(user.pwdExpiryDate, format1);
    });
  return userResponse;
};

const dateToLong = (date, format) => {
  var dt = new Date(date);
  return moment(dt).format(format);
};
