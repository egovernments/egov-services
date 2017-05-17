var baseUrl = window.location.origin;
//
var authToken =   localStorage.getItem("auth-token");
//
// request info from cookies
var requestInfo = {
   "apiId": "org.egov.pgr",
   "ver": "1.0",
   "ts": "01-04-2017 01:01:01",
   "action": "asd",
   "did": "4354648646",
   "key": "xyz",
   "msgId": "654654",
   "requesterId": "61",
   "authToken": authToken,
   "userInfo":{
     "id":"1",
   }
};

var tenantId = "default"//"ap." + window.location.origin.split("-")[0].split("//")[1];
//
function blockUI() {
   $('body').css('overflow', 'hidden');
   $('body').append(
       `<div class="blockUI" style="z-index: 100000; border: none; margin: 0px; padding: 0px; width: 100%; height: 100%; top: 0px; left: 0px; background-color: rgb(0, 0, 0); opacity: 0.6; cursor: wait; position: fixed;"></div>
     <div class="blockUI" style="z-index: 100011; position: fixed; padding: 15px; margin: 0px; width: 30%; top: 40%; left: 35%; text-align: center; color: rgb(255, 255, 255); border: none; background-color: rgb(0, 0, 0); cursor: wait; border-radius: 5px; opacity: 0.5;"><span>Please wait. . .</span><br/><img src="data:image/gif;base64,R0lGODlhEAALAPQAAP////7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/gAAAAAAAAAAACH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCwAAACwAAAAAEAALAAAFLSAgjmRpnqSgCuLKAq5AEIM4zDVw03ve27ifDgfkEYe04kDIDC5zrtYKRa2WQgAh+QQJCwAAACwAAAAAEAALAAAFJGBhGAVgnqhpHIeRvsDawqns0qeN5+y967tYLyicBYE7EYkYAgAh+QQJCwAAACwAAAAAEAALAAAFNiAgjothLOOIJAkiGgxjpGKiKMkbz7SN6zIawJcDwIK9W/HISxGBzdHTuBNOmcJVCyoUlk7CEAAh+QQJCwAAACwAAAAAEAALAAAFNSAgjqQIRRFUAo3jNGIkSdHqPI8Tz3V55zuaDacDyIQ+YrBH+hWPzJFzOQQaeavWi7oqnVIhACH5BAkLAAAALAAAAAAQAAsAAAUyICCOZGme1rJY5kRRk7hI0mJSVUXJtF3iOl7tltsBZsNfUegjAY3I5sgFY55KqdX1GgIAIfkECQsAAAAsAAAAABAACwAABTcgII5kaZ4kcV2EqLJipmnZhWGXaOOitm2aXQ4g7P2Ct2ER4AMul00kj5g0Al8tADY2y6C+4FIIACH5BAkLAAAALAAAAAAQAAsAAAUvICCOZGme5ERRk6iy7qpyHCVStA3gNa/7txxwlwv2isSacYUc+l4tADQGQ1mvpBAAIfkECQsAAAAsAAAAABAACwAABS8gII5kaZ7kRFGTqLLuqnIcJVK0DeA1r/u3HHCXC/aKxJpxhRz6Xi0ANAZDWa+kEAA7AAAAAAAAAAAA"/></div>`
   );
}

function unblockUI() {
   setTimeout(function() {
       $('body').css('overflow', '');
       $('.blockUI').remove();
   }, 100);
}

$(document).ready(function() {
   $(document).ajaxStart(function() {
       blockUI();
   });

   $(document).ajaxStop(function() {
       unblockUI();
   });
})

function getCommonMaster(mainRoute, resource, returnObject) {
   blockUI();
   var res = $.ajax({
       url: baseUrl + "/" + mainRoute + "/" + resource + "/_search?tenantId=" + tenantId,
       type: 'POST',
       dataType: 'json',
       data: JSON.stringify({ RequestInfo: requestInfo }),
       async: false,
       // crossDomain: true, // set this to ensure our $.ajaxPrefilter hook fires
       // processData: false, // We want this to remain an object for  $.ajaxPrefilter
       headers: {
           'auth-token': authToken
       },
       contentType: 'application/json'
   });
   unblockUI();
   return res;
}

function commonApiPost(context, resource = "", action = "", queryObject = {}) {
   blockUI();

   var url = baseUrl + "/" + context + (resource ? "/" + resource : "") + (action ? "/" + action : "") + (queryObject ? "?" : "");
   for (var variable in queryObject) {
       if (queryObject[variable]) {
           url += "&" + variable + "=" + queryObject[variable];
       }
   }
   var res = $.ajax({
       url: url,
       type: 'POST',
       dataType: 'json',
       data: JSON.stringify({ RequestInfo: requestInfo }),
       async: false,
       contentType: 'application/json',
       headers: {
           'auth-token': authToken
       }
   });
   unblockUI();
   return res;
}
//
function commonApiGet(context, resource = "", action = "", queryObject = {}) {
   blockUI();
   var url = baseUrl + "/" + context + (resource ? "/" + resource : "") + (action ? "/" + action : "") + (queryObject ? "?" : "");
   for (var variable in queryObject) {
       if (queryObject[variable]) {
           url += "&" + variable + "=" + queryObject[variable];
       }
   }
   var res = $.ajax({
       url: url,
       type: 'GET',
       dataType: 'json',
       headers: {
           'auth-token': authToken
       },
       // data:JSON.stringify({RequestInfo: requestInfo}),
       async: false,
       contentType: 'application/json'
   });
   unblockUI();
   return res;
}
//
function titleCase (field) {
 var newField = field[0].toUpperCase();
 for(let i=1; i<field.length; i++) {
     if(field[i-1] != " " && field[i] != " ") {
       newField += field.charAt(i).toLowerCase();
     } else {
       newField += field[i]
     }
   }
   return newField;
}

function getUrlVars() {
   var vars = [],
       hash;
   var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
   for (var i = 0; i < hashes.length; i++) {
       hash = hashes[i].split('=');
       vars.push(hash[0]);
       vars[hash[0]] = hash[1];
   }
   return vars;
}

function getCommonMasterById(mainRoute, resource, returnObject, id) {
   blockUI();
   var res = $.ajax({
       url: baseUrl + "/" + mainRoute + "/" + resource + "/_search?tenantId=" + tenantId + "&" + "id=" + id,
       type: 'POST',
       dataType: 'json',
       data: JSON.stringify({ RequestInfo: requestInfo }),
       async: false,
       headers: {
           'auth-token': authToken
       },
       contentType: 'application/json'
   });
   unblockUI();
   return res;
}



function getNameById(object, id, property = "") {
   if (id == "" || id == null) {
       return "";
   }
   for (var i = 0; i < object.length; i++) {
       if (property == "") {
           if (object[i].id == id) {
               return object[i].name;
           }
       } else {
           if (object[i].hasOwnProperty(property)) {
               if (object[i].id == id) {
                   return object[i][property];
               }
           } else {
               return "";
           }
       }
   }
   return "";
}

function getDesignations(status, cb) {
   $.ajax({
       url: baseUrl + "/egov-common-workflows/designations/_search?businessKey=Agreement&approvalDepartmentName=&departmentRule=&currentStatus=" + (status || "") + "&additionalRule=&pendingAction=&designation=&amountRule=",
       type: 'POST',
       dataType: 'json',
       data: JSON.stringify({ RequestInfo: requestInfo }),
       headers: {
           'auth-token': authToken
       },
       contentType: 'application/json',
       success: function(result) {
           cb(result);
       },
       error: function(error) {
           console.log(error);
       }
   });
}

function showSuccess(message) {
   $("body").append(
       `<div id="success-alert-div" class="alert alert-success alert-dismissible alert-toast" role="alert" style="display:none">
           <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
           <span id="success-alert-span"></span>
       </div>`
   );

   $('#success-alert-span').text(message);
   $('#success-alert-div').show();
   setTimeout(function() {
       $('#success-alert-div').remove();
   }, 3000);
}

function showError(message) {
   $("body").append(
       `<div id="error-alert-div" class="alert alert-danger alert-dismissible alert-toast" role="alert" style="display:none; z-index:100000">
           <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
           <span id="error-alert-span"></span>
       </div>`
   )
   $('#error-alert-span').text(message);
   $('#error-alert-div').show();
   setTimeout(function() {
       $('#error-alert-div').remove();
   }, 3000);
}
