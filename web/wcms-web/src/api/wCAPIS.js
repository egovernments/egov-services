var $ = require('jquery');
var axios = require('axios');
// var store = require('configureStore').configure();

var instance = axios.create({
  baseURL: window.location.origin,
  // timeout: 5000,
  headers: {
    "Content-Type": "application/json",
    // "SESSIONID":"75dedd21-1145-4745-a8aa-1790a737b7c5",
    // "JSESSIONID":"Nw2kKeNF6Eu42vtXypb3kP4fER1ghjXNMNISiIF5.ip-10-0-0-100",
    // "Authorization":"Basic Og=="
  }
});

//document.cookie = "SESSIONID=75dedd21-1145-4745-a8aa-1790a737b7c5; JSESSIONID=Nw2kKeNF6Eu42vtXypb3kP4fER1ghjXNMNISiIF5.ip-10-0-0-100; Authorization=Basic Og==";

var authToken = localStorage.getItem("auth");

//request info from cookies
var requestInfo = {
  "apiId": "org.egov.pt",
  "ver": "1.0",
  "ts": "01-04-2017 01:01:01",
  "action": "asd",
  "did": "4354648646",
  "key": "xyz",
  "msgId": "654654",
  "requesterId": "61",
  "authToken": authToken
};
//uncomment for ap
// var tenantId = "ap." + window.location.origin.split("-")[0].split("//")[1];
var tenantId = localStorage.getItem("tenantId");

module.exports = {
  commonApiPost: (context, resource = "", action = "", queryObject = {}, body = {},isOverride={}) => {
    var url = "/" + context + (resource
      ? "/" + resource
      : "") + (action
      ? "/" + action
      : "");
    url += "?tenantId=" + tenantId;
    for (var variable in queryObject) {
      if (queryObject[variable]) {
        url += "&" + variable + "=" + queryObject[variable];
      }
    }
    body["RequestInfo"] = requestInfo;
    console.log(body);
    return instance.post(url, body).then(function(response) {
      return response.data;
    }).catch(function(response) {
      throw new Error(response.data.message);
    });
  },
  commonApiGet: (context, resource = "", action = "", queryObject = {}) => {
    var url = "/" + context + (resource
      ? "/" + resource
      : "") + (action
      ? "/" + action
      : "");
    url += "?tenantId=" + tenantId;
    for (var variable in queryObject) {
      if (queryObject[variable]) {
        url += "&" + variable + "=" + queryObject[variable];
      }
    }
    return instance.get(url).then(function(response) {
      return response.data;
    }).catch(function(response) {
      throw new Error(response.data.message);
    });
  },
  getAll: (arrayOfRequest) => {
    return instance.all(arrayOfRequest).then(axios.spread(function(acct, perms) {
      // Both requests are now complete
    }));
  }
};
