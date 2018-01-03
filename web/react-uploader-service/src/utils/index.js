import { FILE_DOWNLOAD_ENDPOINT } from "../constants/ApiEndpoints";

export const slugify = term => {
  return term.toLowerCase().replace(/\s+/, "-");
};

export const persistInLocalStorage = obj => {
  Object.keys(obj).forEach(objKey => {
    const objValue = obj[objKey];
    window.localStorage.setItem(objKey, objValue);
  }, this);
};

export const fetchFromLocalStorage = key => {
  return window.localStorage.getItem(key) || null;
};

export const getRequestUrl = (url, params) => {
  var query = Object.keys(params)
    .map(k => encodeURIComponent(k) + "=" + encodeURIComponent(params[k]))
    .join("&");

  return url + "?" + query;
};

export const prepareFormData = params => {
  var formData = new FormData();

  for (var k in params) {
    formData.append(k, params[k]);
  }
  return formData;
};

export const getDateFromEpoch = epoch => {
  const dateObj = new Date(epoch);
  const year = dateObj.getFullYear();
  const month = dateObj.getMonth() + 1;
  const day = dateObj.getDay();
  return day + "-" + month + "-" + year;
};

export const getFileDownloadLink = (tenantId, fileStoreId) => {
  const requestParams = { tenantId, fileStoreId };
  let downloadLink = getRequestUrl(FILE_DOWNLOAD_ENDPOINT, requestParams);
  // for developement prepend the dev environment
  if (process.env.NODE_ENV === "development") {
    downloadLink = "http://egov-micro-dev.egovernments.org" + downloadLink;
  }
  return downloadLink;
};
