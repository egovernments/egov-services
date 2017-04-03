var baseUrl = window.location.origin;



var tenantId = "ap.kurnool";


var authToken = localStorage.getItem("auth-token");

//request info from cookies
var requestInfo = {
    "apiId": "org.egov.pgr",
    "ver": "1.0",
    "ts": "2015-04-12",
    "action": "asd",
    "did": "4354648646",
    "key": "xyz",
    "msgId": "654654",
    "requesterId": "61",
    "authToken": authToken
};

var employeeType = !localStorage.getItem("employeeType") || localStorage.getItem("employeeType") == "undefined" ? (localStorage.setItem("employeeType", JSON.stringify(getCommonMaster("hr-masters", "employeetypes", "EmployeeType").responseJSON["EmployeeType"])) || []) : JSON.parse(localStorage.getItem("employeeType"));
var employeeStatus = !localStorage.getItem("employeeStatus") || localStorage.getItem("employeeStatus") == "undefined" ? (localStorage.setItem("employeeStatus", JSON.stringify(getCommonMaster("hr-masters", "hrstatuses", "HRStatus").responseJSON["HRStatus"])) || []) : JSON.parse(localStorage.getItem("employeeStatus"));
var group = !localStorage.getItem("group") || localStorage.getItem("group") == "undefined" ? (localStorage.setItem("group", JSON.stringify(getCommonMaster("hr-masters", "groups", "Group").responseJSON["Group"])) || []) : JSON.parse(localStorage.getItem("group"));
var maritalStatus = !localStorage.getItem("maritalStatus") || localStorage.getItem("maritalStatus") == "undefined" ? (localStorage.setItem("maritalStatus", JSON.stringify(["MARRIED", "UNMARRIED", "DIVORCED", "WIDOWER", "WIDOW"]))) : JSON.parse(localStorage.getItem("maritalStatus"));
var user_bloodGroup = !localStorage.getItem("user_bloodGroup") || localStorage.getItem("user_bloodGroup") == "undefined" ? (localStorage.setItem("user_bloodGroup", JSON.stringify(["O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+"]))) : JSON.parse(localStorage.getItem("user_bloodGroup"));
var motherTounge = !localStorage.getItem("motherTounge") || localStorage.getItem("motherTounge") == "undefined" ? (localStorage.setItem("motherTounge", JSON.stringify(getCommonMaster("egov-common-masters", "languages", "Language").responseJSON["Language"])) || []) : JSON.parse(localStorage.getItem("motherTounge"));
var religion = !localStorage.getItem("religion") || localStorage.getItem("religion") == "undefined" ? (localStorage.setItem("religion", JSON.stringify(getCommonMaster("egov-common-masters", "religions", "Religion").responseJSON["Religion"])) || []) : JSON.parse(localStorage.getItem("religion"));
var community = !localStorage.getItem("community") || localStorage.getItem("community") == "undefined" ? (localStorage.setItem("community", JSON.stringify(getCommonMaster("egov-common-masters", "communities", "Community").responseJSON["Community"])) || []) : JSON.parse(localStorage.getItem("community"));
var category = !localStorage.getItem("category") || localStorage.getItem("category") == "undefined" ? (localStorage.setItem("category", JSON.stringify(getCommonMaster("egov-common-masters", "categories", "Category").responseJSON["Category"])) || []) : JSON.parse(localStorage.getItem("category"));
var bank = !localStorage.getItem("bank") || localStorage.getItem("bank") == "undefined" ? (localStorage.setItem("bank", JSON.stringify(getCommonMaster("egf-masters", "banks", "banks").responseJSON["banks"])) || []) : JSON.parse(localStorage.getItem("bank"));
var recruitmentMode = !localStorage.getItem("recruitmentMode") || localStorage.getItem("recruitmentMode") == "undefined" ? (localStorage.setItem("recruitmentMode", JSON.stringify(getCommonMaster("hr-masters", "recruitmentmodes", "RecruitmentMode").responseJSON["RecruitmentMode"])) || []) : JSON.parse(localStorage.getItem("recruitmentMode"));
var recruitmentType = !localStorage.getItem("recruitmentType") || localStorage.getItem("recruitmentType") == "undefined" ? (localStorage.setItem("recruitmentType", JSON.stringify(getCommonMaster("hr-masters", "recruitmenttypes", "RecruitmentType").responseJSON["RecruitmentType"])) || []) : JSON.parse(localStorage.getItem("recruitmentType"));
var recruitmentQuota = !localStorage.getItem("recruitmentQuota") || localStorage.getItem("recruitmentQuota") == "undefined" ? (localStorage.setItem("recruitmentQuota", JSON.stringify(getCommonMaster("hr-masters", "recruitmentquotas", "RecruitmentQuota").responseJSON["RecruitmentQuota"])) || []) : JSON.parse(localStorage.getItem("recruitmentQuota"));
var assignments_grade = !localStorage.getItem("assignments_grade") || localStorage.getItem("assignments_grade") == "undefined" ? (localStorage.setItem("assignments_grade", JSON.stringify(getCommonMaster("hr-masters", "grades", "Grade").responseJSON["Grade"])) || []) : JSON.parse(localStorage.getItem("assignments_grade"));
var assignments_designation = !localStorage.getItem("assignments_designation") || localStorage.getItem("assignments_designation") == "undefined" ? (localStorage.setItem("assignments_designation", JSON.stringify(getCommonMaster("hr-masters", "designations", "Designation").responseJSON["Designation"])) || []) : JSON.parse(localStorage.getItem("assignments_designation"));
var assignments_department = !localStorage.getItem("assignments_department") || localStorage.getItem("assignments_department") == "undefined" ? (localStorage.setItem("assignments_department", JSON.stringify(getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"])) || []) : JSON.parse(localStorage.getItem("assignments_department"));
var assignments_fund = !localStorage.getItem("assignments_fund") || localStorage.getItem("assignments_fund") == "undefined" ? (localStorage.setItem("assignments_fund", JSON.stringify(getCommonMaster("egf-masters", "funds", "funds").responseJSON["funds"])) || []) : JSON.parse(localStorage.getItem("assignments_fund"));
var assignments_functionary = !localStorage.getItem("assignments_functionary") || localStorage.getItem("assignments_functionary") == "undefined" ? (localStorage.setItem("assignments_functionary", JSON.stringify(getCommonMaster("egf-masters", "functionaries", "funds").responseJSON["functionaries"])) || []) : JSON.parse(localStorage.getItem("assignments_functionary"));
var assignments_function = !localStorage.getItem("assignments_function") || localStorage.getItem("assignments_function") == "undefined" ? (localStorage.setItem("assignments_function", JSON.stringify(getCommonMaster("egf-masters", "functions", "functions").responseJSON["functions"])) || []) : JSON.parse(localStorage.getItem("assignments_function"));
var assignments_parent= !localStorage.getItem("assignments_parent") || localStorage.getItem("assignments_parent") == "undefined" ? (localStorage.setItem("assignments_parent", JSON.stringify(commonApiPost("asset-services","assetCategories","_search",{}).responseJSON["AssetCategory"])) || []) : JSON.parse(localStorage.getItem("assignments_parent"));
var assignments_unitOfMeasurement= !localStorage.getItem("assignments_unitOfMeasurement") || localStorage.getItem("assignments_unitOfMeasurement") == "undefined" ? (localStorage.setItem("assignments_unitOfMeasurement", JSON.stringify(commonApiPost("egov-common-masters","uom","_search",{}).responseJSON["uoms"])) || []) : JSON.parse(localStorage.getItem("assignments_unitOfMeasurement"));

// var response=$.ajax({
//           url: window.location.origin+"/user/_login?tenantId=ap.public&username=ramakrishna&password=demo&grant_type=password&scope=read",
//           type: 'POST',
//           dataType: 'json',
//           // data:JSON.stringify({RequestInfo: requestInfo}),
//           async: false,
//           contentType: 'application/json',
//           headers:{
//             'Authorization' :'Basic ZWdvdi11c2VyLWNsaWVudDplZ292LXVzZXItc2VjcmV0'
//           }
//       });
//
//       if(response["statusText"]==="OK")
//       {
//           localStorage.setItem("auth-token", response.responseJSON["access_token"]);
//           authToken=response.responseJSON["access_token"];
//         // alert("Successfully added");
//       }
//       else {
//         alert(response["statusText"]);
//       }



function getCommonMaster(mainRoute, resource, returnObject) {
    return $.ajax({
        url: baseUrl + "/" + mainRoute + "/" + resource + "/_search?tenantId=" + tenantId,
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify({RequestInfo: requestInfo}),
        async: false,
        // crossDomain: true, // set this to ensure our $.ajaxPrefilter hook fires
        // processData: false, // We want this to remain an object for  $.ajaxPrefilter
        headers: {
            'auth-token': authToken
        },
        contentType: 'application/json'
        // ,
        // success: function (result) {
        //     return result[returnObject];
        //     // console.log(result);
        //    // CallBack(result);
        // },
        // error: function (error) {
        //     return [];
        //     // console.log(error);
        // }
    });
    // return response.statusText==="Ok"?response.responseJSON[returnObject]:[];
}

function commonApiPost(context, resource = "", action = "", queryObject = {}) {
    var url = baseUrl + "/" + context + (resource ? "/" + resource : "") + (action ? "/" + action : "") + (queryObject ? "?" : "");
    for (var variable in queryObject) {
        if (queryObject[variable]) {
            url += "&" + variable + "=" + queryObject[variable];
        }
    }
    return $.ajax({
        url: url,
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify({RequestInfo: requestInfo}),
        async: false,
        contentType: 'application/json',
        headers: {
            'auth-token': authToken
        }
    });
}

function commonApiGet(context, resource = "", action = "", queryObject = {}) {
    var url = baseUrl + "/" + context + (resource ? "/" + resource : "") + (action ? "/" + action : "") + (queryObject ? "?" : "");
    for (var variable in queryObject) {
        if (queryObject[variable]) {
            url += "&" + variable + "=" + queryObject[variable];
        }
    }
    return $.ajax({
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
    return $.ajax({
        url: baseUrl + "/" + mainRoute + "/" + resource + "/_search?tenantId=" + tenantId + "&" + "id=" + id,
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify({RequestInfo: requestInfo}),
        async: false,
        // crossDomain: true, // set this to ensure our $.ajaxPrefilter hook fires
        // processData: false, // We want this to remain an object for  $.ajaxPrefilter
        headers: {
            'auth-token': authToken
        },
        contentType: 'application/json'
        // ,
        // success: function (result) {
        //     return result[returnObject];
        //     // console.log(result);
        //    // CallBack(result);
        // },
        // error: function (error) {
        //     return [];
        //     // console.log(error);
        // }
    });
    // return response.statusText==="Ok"?response.responseJSON[returnObject]:[];
}

// commonApiPost("asset","assetCategories","",{boundaryTypeName:"LOCALITY",hierarchyTypeName:"LOCATION"})


function getNameById(object,id,property="") {
    if (id==""||id==null) {
        return "";
    }
    for (var i = 0; i < object.length; i++) {
        if (property=="") {
            if (object[i].id==id) {
              return object[i].name;
            }
        }
        else {
          if (object[i].hasOwnProperty(property)) {
                if (object[i].id==id) {
                    return object[i][property];
                }
          }
          else {
              return "";
          }
        }
    }
    return "";
}
