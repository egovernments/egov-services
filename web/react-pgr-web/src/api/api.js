var common = require('../components/common/common');
var axios = require('axios');
// var store = require('configureStore').configure();

var instance = axios.create({
    baseURL: window.location.origin,
    // timeout: 5000,
    headers: {
        "Content-Type": "application/json"
    }
});

//document.cookie = "SESSIONID=75dedd21-1145-4745-a8aa-1790a737b7c5; JSESSIONID=Nw2kKeNF6Eu42vtXypb3kP4fER1ghjXNMNISiIF5.ip-10-0-0-100; Authorization=Basic Og==";

var authToken = localStorage.getItem("token");

//request info from cookies
var requestInfo = {
    "apiId": "org.egov.pt",
    "ver": "1.0",
    "ts": "27-06-2017 10:30:12",
    "action": "asd",
    "did": "4354648646",
    "key": "xyz",
    "msgId": "654654",
    "requesterId": "61",
    "authToken": authToken
};


var tenantId = localStorage.getItem("tenantId") ? localStorage.getItem("tenantId") : 'default';

module.exports = {
    commonApiPost: (context, queryObject = {}, body = {}, doNotOverride = false, isTimeLong = false) => {
        var url = context;
        if(url && url[url.length-1] == "/")
            url = url.substring(0, url.length-1);
        if (!doNotOverride)
            url += "?tenantId=" + tenantId;
        else
            url += "?"
        for (var variable in queryObject) {
            if (typeof(queryObject[variable]) != "undefined") {
                url += "&" + variable + "=" + queryObject[variable];
            }
        }

        if(/_search/.test(context)) {
            url += "&pageSize=500";
        }

        requestInfo.authToken = localStorage.getItem("token");
        if(isTimeLong){
            requestInfo.ts = new Date().getTime();
        }


        body["RequestInfo"] = requestInfo;

        return instance.post(url, body).then(function(response) {
            return response.data;
        }).catch(function(response) {
			
			console.log(response);
			
            if (response && response.response && response.response.data && response.response.data[0] && response.response.data[0].error) {
                var _err = response.response.data[0].error.message || "";
                if (response.response.data[0].error.errorFields && Object.keys(response.response.data[0].error.errorFields).length) {
                    for (var i = 0; i < response.response.data[0].error.errorFields.length; i++) {
                        _err += "\n " + response.response.data[0].error.errorFields[i].message + " ";
                    }
                    throw new Error(_err);
                }
            }else if(response && response.response && response.response.data && response.response.data.error){
              let _err = common.translate(response.response.data.error.fields[0].code);
              throw new Error(_err);
            }else if(response && response.response && !response.response.data && response.response.status == 400) {
                document.title = "eGovernments";
                var locale = localStorage.getItem('locale');
                localStorage.clear();
                localStorage.setItem('locale', locale);
                window.location.hash = "#/";
            } else if(response){
				throw new Error(response);
			}else {
                throw new Error("Server returned unexpected error. Please contact system administrator.");
            }
        });
    },
    commonApiGet: (context, queryObject = {}, doNotOverride = false) => {
        var url = context;
        if (!doNotOverride)
            url += "?tenantId=" + tenantId;
        else
            url += "?"
        for (var variable in queryObject) {
            if (typeof queryObject[variable] != "undefined") {
                url += "&" + variable + "=" + queryObject[variable];
            }
        }

        if(/_search/.test(context)) {
            url += "&pageSize=500";
        }
        return instance.get(url).then(function(response) {
            return response.data;
        }).catch(function(response) {
	if (response && response.response && response.response.data && response.response.data[0] && (response.response.data[0].error || response.response.data[0].Errors[0])) {
                var _err = response.response.data[0].error.message || "";
                if (response.response.data[0].error.errorFields && Object.keys(response.response.data[0].error.errorFields).length) {
                    for (var i = 0; i < response.response.data[0].error.errorFields.length; i++) {
                        _err += "\n " + response.response.data[0].error.errorFields[i].message + " ";
                    }
                    throw new Error(_err);
                }
            } else {
                throw new Error("Something went wrong, please try again later.");
            }
        });
    },
    getAll: (arrayOfRequest) => {
        return instance.all(arrayOfRequest).then(axios.spread(function(acct, perms) {
            // Both requests are now complete
        }));
    }
};
