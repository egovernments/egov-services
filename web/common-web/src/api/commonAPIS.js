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

let authorization = "Basic ZWdvdi11c2VyLWNsaWVudDplZ292LXVzZXItc2VjcmV0";

var instanceBeforeLogin = axios.create({
  baseURL: window.location.origin,
  // timeout: 5000,
  headers: {
    "Content-Type": "application/x-www-form-urlencoded",
    // "SESSIONID":"75dedd21-1145-4745-a8aa-1790a737b7c5",
    // "JSESSIONID":"Nw2kKeNF6Eu42vtXypb3kP4fER1ghjXNMNISiIF5.ip-10-0-0-100",
    "Authorization":authorization
  }
});

// document.cookie = "SESSIONID=262a86bb-fbbc-469b-b57d-5f5fa22519c1; JSESSIONID=aGB5rngaHCQOjf2MPGPWEt1Ft11lXX-9LfCjUT0_.ip-10-0-0-100; Authorization=Basic Og==";


const formatDate = function() {
    var date = new Date();
    return (date.getDate() + "-" + ((date.getMonth() + 1) < 10 ? ("0" + (date.getMonth() + 1)) : (date.getMonth() + 1)) + "-" + date.getFullYear() + " " + date.getHours() + ":" + date.getMinutes() + ":" + date.getSeconds());
}

// var authToken = localStorage.getItem("auth-token");
var authToken="2e4d0780-3fbd-4383-80fb-73872269503e";

//request info from cookies
var requestInfo = {
  "apiId": "org.egov.common",
  "ver": "1.0",
  "ts": formatDate(),
  "action": "asd",
  "did": "4354648646",
  "key": "xyz",
  "msgId": "654654",
  "requesterId": "61",
  "authToken": authToken
};
//uncomment for ap
// var tenantId = "ap." + window.location.origin.split("-")[0].split("//")[1];
var tenantId = "default";

module.exports = {
  commonApiPost: (context, queryObject = {}, body = {}, dontSendRqstInfo) => {
    var url = "/" + context;
    url += "?tenantId=" + tenantId;
    for (var variable in queryObject) {
      if (queryObject[variable]) {
        url += "&" + variable + "=" + queryObject[variable];
      }
    }
    if(!dontSendRqstInfo) body["RequestInfo"] = requestInfo;
    console.log(body);
    if (authToken) {
      return instance.post(url, body).then(function(response) {
        return response.data;
      }).catch(function(response) {
        throw new Error(response.data.message);
      });
    }
    else {
      return instanceBeforeLogin.post(url, body).then(function(response) {
        return response.data;
      }).catch(function(response) {
        throw new Error(response.data.message);
      });
    }

  },
  commonApiGet: (context, queryObject = {}) => {
    var url = "/" + context;
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
