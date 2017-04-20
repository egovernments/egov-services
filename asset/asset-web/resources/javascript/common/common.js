var baseUrl = window.location.origin;

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

var tenantId = "ap." + window.location.origin.split("-")[0].split("//")[1];

function blockUI() {
    $('body').css('overflow', 'hidden');
    $('body').append(
        `<div class="blockUI" style="z-index: 100000; border: none; margin: 0px; padding: 0px; width: 100%; height: 100%; top: 0px; left: 0px; background-color: rgb(0, 0, 0); opacity: 0.6; cursor: wait; position: fixed;"></div>
      <div class="blockUI" style="z-index: 100011; position: fixed; padding: 15px; margin: 0px; width: 30%; top: 40%; left: 35%; text-align: center; color: rgb(255, 255, 255); border: none; background-color: rgb(0, 0, 0); cursor: wait; border-radius: 5px; opacity: 0.5;"><span>Please wait. . .</span><br/><img src="data:image/gif;base64,R0lGODlhEAALAPQAAP////7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/v7+/gAAAAAAAAAAACH/C05FVFNDQVBFMi4wAwEAAAAh/hpDcmVhdGVkIHdpdGggYWpheGxvYWQuaW5mbwAh+QQJCwAAACwAAAAAEAALAAAFLSAgjmRpnqSgCuLKAq5AEIM4zDVw03ve27ifDgfkEYe04kDIDC5zrtYKRa2WQgAh+QQJCwAAACwAAAAAEAALAAAFJGBhGAVgnqhpHIeRvsDawqns0qeN5+y967tYLyicBYE7EYkYAgAh+QQJCwAAACwAAAAAEAALAAAFNiAgjothLOOIJAkiGgxjpGKiKMkbz7SN6zIawJcDwIK9W/HISxGBzdHTuBNOmcJVCyoUlk7CEAAh+QQJCwAAACwAAAAAEAALAAAFNSAgjqQIRRFUAo3jNGIkSdHqPI8Tz3V55zuaDacDyIQ+YrBH+hWPzJFzOQQaeavWi7oqnVIhACH5BAkLAAAALAAAAAAQAAsAAAUyICCOZGme1rJY5kRRk7hI0mJSVUXJtF3iOl7tltsBZsNfUegjAY3I5sgFY55KqdX1GgIAIfkECQsAAAAsAAAAABAACwAABTcgII5kaZ4kcV2EqLJipmnZhWGXaOOitm2aXQ4g7P2Ct2ER4AMul00kj5g0Al8tADY2y6C+4FIIACH5BAkLAAAALAAAAAAQAAsAAAUvICCOZGme5ERRk6iy7qpyHCVStA3gNa/7txxwlwv2isSacYUc+l4tADQGQ1mvpBAAIfkECQsAAAAsAAAAABAACwAABS8gII5kaZ7kRFGTqLLuqnIcJVK0DeA1r/u3HHCXC/aKxJpxhRz6Xi0ANAZDWa+kEAA7AAAAAAAAAAAA"/></div>`
    );
}

function unblockUI() {
    setTimeout(function(){ 
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


// var natureOfAllotments = !localStorage.getItem("natureOfAllotments") || localStorage.getItem("natureOfAllotments") == "undefined" ? (localStorage.setItem("natureOfAllotments", JSON.stringify(commonApiPost("lams-services","","getnatureofallotment",{}).responseJSON)) || {}) : JSON.parse(localStorage.getItem("natureOfAllotments"));
// var paymentCycle= !localStorage.getItem("paymentCycle") || localStorage.getItem("paymentCycle") == "undefined" ? (localStorage.setItem("paymentCycle", JSON.stringify(commonApiPost("lams-services","","getpaymentcycle",{}).responseJSON)) || {}) : JSON.parse(localStorage.getItem("paymentCycle"));
// var employeeType=JSON.parse(localStorage.getItem("employeeType"))==null?(localStorage.setItem("employeeType",JSON.stringify(getCommonMaster("hr-masters", "employeetypes", "EmployeeType").responseJSON["EmployeeType"]))|| []) :JSON.parse(localStorage.getItem("employeeType"));
// var employeeStatus=JSON.parse(localStorage.getItem("employeeStatus"))==null?(localStorage.setItem("employeeStatus",JSON.stringify(getCommonMaster("hr-masters", "hrstatuses", "HRStatus").responseJSON["HRStatus"])) || []) :JSON.parse(localStorage.getItem("employeeStatus"));
// var group=JSON.parse(localStorage.getItem("group"))==null?(localStorage.setItem("group",JSON.stringify(getCommonMaster("hr-masters", "groups", "Group").responseJSON["Group"])) || []) :JSON.parse(localStorage.getItem("group"));
// var maritalStatus=JSON.parse(localStorage.getItem("maritalStatus"))==null?(localStorage.setItem("maritalStatus",JSON.stringify(["MARRIED", "UNMARRIED", "DIVORCED", "WIDOWER", "WIDOW"]))) :JSON.parse(localStorage.getItem("maritalStatus"));
// var user_bloodGroup=JSON.parse(localStorage.getItem("user_bloodGroup"))==null?(localStorage.setItem("user_bloodGroup",JSON.stringify(["O-", "O+", "A-", "A+", "B-", "B+", "AB-", "AB+"]))) :JSON.parse(localStorage.getItem("user_bloodGroup"));
// var motherTounge=JSON.parse(localStorage.getItem("motherTounge"))==null?(localStorage.setItem("motherTounge",JSON.stringify(getCommonMaster("egov-common-masters", "languages", "Language").responseJSON["Language"])) || []) :JSON.parse(localStorage.getItem("motherTounge"));
// var religion=JSON.parse(localStorage.getItem("religion"))==null?(localStorage.setItem("religion",JSON.stringify(getCommonMaster("egov-common-masters", "religions", "Religion").responseJSON["Religion"])) || []) :JSON.parse(localStorage.getItem("religion"));
// var community=JSON.parse(localStorage.getItem("community"))==null?(localStorage.setItem("community",JSON.stringify(getCommonMaster("egov-common-masters", "communities", "Community").responseJSON["Community"])) || []) :JSON.parse(localStorage.getItem("community"));
// var category=JSON.parse(localStorage.getItem("category"))==null?(localStorage.setItem("category",JSON.stringify(getCommonMaster("egov-common-masters", "categories", "Category").responseJSON["Category"])) || []) :JSON.parse(localStorage.getItem("category"));
// var bank=JSON.parse(localStorage.getItem("bank"))==null?(localStorage.setItem("bank",JSON.stringify(getCommonMaster("egf-masters", "banks", "banks").responseJSON["banks"])) || []) :JSON.parse(localStorage.getItem("bank"));
// var recruitmentMode=JSON.parse(localStorage.getItem("recruitmentMode"))==null?(localStorage.setItem("recruitmentMode",JSON.stringify(getCommonMaster("hr-masters", "recruitmentmodes", "RecruitmentMode").responseJSON["RecruitmentMode"])) || []) :JSON.parse(localStorage.getItem("recruitmentMode"));
// var recruitmentType=JSON.parse(localStorage.getItem("recruitmentType"))==null?(localStorage.setItem("recruitmentType",JSON.stringify(getCommonMaster("hr-masters", "recruitmenttypes", "RecruitmentType").responseJSON["RecruitmentType"])) || []) :JSON.parse(localStorage.getItem("recruitmentType"));
// var assignments_grade=JSON.parse(localStorage.getItem("assignments_grade"))==null?(localStorage.setItem("assignments_grade",JSON.stringify(getCommonMaster("hr-masters", "grades", "Grade").responseJSON["Grade"])) || []) :JSON.parse(localStorage.getItem("assignments_grade"));
// var designation = !localStorage.getItem("assignments_designation") || localStorage.getItem("assignments_designation") == "undefined" ? (localStorage.setItem("assignments_designation", JSON.stringify(getCommonMaster("hr-masters", "designations", "Designation").responseJSON["Designation"])) || []) : JSON.parse(localStorage.getItem("assignments_designation"));
var departments, locality, electionwards, street, revenueWards, revenueZone, revenueBlock, assetCategories, assignments_unitOfMeasurement,
    asset_category_type, statusList, acquisitionList, depreciationMethod, assetAccount, accumulatedDepreciationAccount, revaluationReserveAccount, depreciationExpenseAccount, recruitmentQuota;
setTimeout(function() {
    try { recruitmentQuota = !localStorage.getItem("recruitmentQuota") || localStorage.getItem("recruitmentQuota") == "undefined" ? (localStorage.setItem("recruitmentQuota", JSON.stringify(getCommonMaster("hr-masters", "recruitmentquotas", "RecruitmentQuota").responseJSON["RecruitmentQuota"] || [])), JSON.parse(localStorage.getItem("recruitmentQuota"))) : JSON.parse(localStorage.getItem("recruitmentQuota")); } catch (e) {
        console.log(e);
        recruitmentQuota = [];
    }
    try { assetCategories = !localStorage.getItem("assetCategories") || localStorage.getItem("assetCategories") == "undefined" ? (localStorage.setItem("assetCategories", JSON.stringify(commonApiPost("asset-services", "assetCategories", "_search", {}).responseJSON["AssetCategory"] || [])), JSON.parse(localStorage.getItem("assetCategories"))) : JSON.parse(localStorage.getItem("assetCategories")); } catch (e) {
        console.log(e);
        assetCategories = [];
    }
    try { departments = !localStorage.getItem("assignments_department") || localStorage.getItem("assignments_department") == "undefined" ? (localStorage.setItem("assignments_department", JSON.stringify(getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"] || [])), JSON.parse(localStorage.getItem("assignments_department"))) : JSON.parse(localStorage.getItem("assignments_department")); } catch (e) {
        console.log(e);
        departments = [];
    }
    try { locality = !localStorage.getItem("locality") || localStorage.getItem("locality") == "undefined" ? (localStorage.setItem("locality", JSON.stringify(commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "LOCALITY", hierarchyTypeName: "LOCATION" }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("locality"))) : JSON.parse(localStorage.getItem("locality")); } catch (e) {
        console.log(e);
        locality = [];
    }
    try { electionwards = !localStorage.getItem("ward") || localStorage.getItem("ward") == "undefined" ? (localStorage.setItem("ward", JSON.stringify(commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "ADMINISTRATION" }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("ward"))) : JSON.parse(localStorage.getItem("ward")); } catch (e) {
        console.log(e);
        electionwards = [];
    }
    try { street = !localStorage.getItem("street") || localStorage.getItem("street") == "undefined" ? (localStorage.setItem("street", JSON.stringify(commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "STREET", hierarchyTypeName: "LOCATION" }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("street"))) : JSON.parse(localStorage.getItem("street")); } catch (e) {
        console.log(e);
        street = [];
    }
    try { revenueWards = !localStorage.getItem("revenueWard") || localStorage.getItem("revenueWard") == "undefined" ? (localStorage.setItem("revenueWard", JSON.stringify(commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "REVENUE" }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("revenueWard"))) : JSON.parse(localStorage.getItem("revenueWard")); } catch (e) {
        console.log(e);
        revenueWard = [];
    }
    try { revenueZone = !localStorage.getItem("revenueZone") || localStorage.getItem("revenueZone") == "undefined" ? (localStorage.setItem("revenueZone", JSON.stringify(commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "ZONE", hierarchyTypeName: "REVENUE" }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("revenueZone"))) : JSON.parse(localStorage.getItem("revenueZone")); } catch (e) {
        console.log(e);
        revenueZone = [];
    }
    try { revenueBlock = !localStorage.getItem("revenueBlock") || localStorage.getItem("revenueBlock") == "undefined" ? (localStorage.setItem("revenueBlock", JSON.stringify(commonApiPost("v1/location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "BLOCK", hierarchyTypeName: "REVENUE" }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("revenueBlock"))) : JSON.parse(localStorage.getItem("revenueBlock")); } catch (e) {
        console.log(e);
        revenueBlock = [];
    }
    try { assignments_unitOfMeasurement = !localStorage.getItem("assignments_unitOfMeasurement") || localStorage.getItem("assignments_unitOfMeasurement") == "undefined" ? (localStorage.setItem("assignments_unitOfMeasurement", JSON.stringify(commonApiPost("egov-common-masters", "uom", "_search", {}).responseJSON["uoms"] || [])), JSON.parse(localStorage.getItem("assignments_unitOfMeasurement"))) : JSON.parse(localStorage.getItem("assignments_unitOfMeasurement")); } catch (e) {
        console.log(e);
        assignments_unitOfMeasurement = [];
    }
    try { asset_category_type = !localStorage.getItem("asset_category_type") || localStorage.getItem("asset_category_type") == "undefined" ? (localStorage.setItem("asset_category_type", JSON.stringify(commonApiGet("asset-services", "", "GET_ASSET_CATEGORY_TYPE", {}).responseJSON || {})), JSON.parse(localStorage.getItem("asset_category_type"))) : JSON.parse(localStorage.getItem("asset_category_type")); } catch (e) {
        console.log(e);
        asset_category_type = {};
    }
    try { statusList = !localStorage.getItem("statusList") || localStorage.getItem("statusList") == "undefined" ? (localStorage.setItem("statusList", JSON.stringify(commonApiGet("asset-services", "", "GET_STATUS", {}).responseJSON || {})), JSON.parse(localStorage.getItem("statusList"))) : JSON.parse(localStorage.getItem("statusList")); } catch (e) {
        console.log(e);
        statusList = {};
    }
    try { acquisitionList = !localStorage.getItem("acquisitionList") || localStorage.getItem("acquisitionList") == "undefined" ? (localStorage.setItem("acquisitionList", JSON.stringify(commonApiGet("asset-services", "", "GET_MODE_OF_ACQUISITION", {}).responseJSON || [])), JSON.parse(localStorage.getItem("acquisitionList"))) : JSON.parse(localStorage.getItem("acquisitionList")); } catch (e) {
        console.log(e);
        acquisitionList = [];
    }
    try { depreciationMethod = !localStorage.getItem("depreciationMethod") || localStorage.getItem("depreciationMethod") == "undefined" ? (localStorage.setItem("depreciationMethod", JSON.stringify(commonApiGet("asset-services", "", "GET_DEPRECIATION_METHOD", {}).responseJSON || {})), JSON.parse(localStorage.getItem("depreciationMethod"))) : JSON.parse(localStorage.getItem("depreciationMethod")); } catch (e) {
        console.log(e);
        depreciationMethod = {};
    }
    try { assetAccount = !localStorage.getItem("assetAccount") || localStorage.getItem("assetAccount") == "undefined" ? (localStorage.setItem("assetAccount", JSON.stringify(commonApiPost("egf-masters", "chartofaccounts", "_search", { tenantId }).responseJSON["chartOfAccounts"] || [])), JSON.parse(localStorage.getItem("assetAccount"))) : JSON.parse(localStorage.getItem("assetAccount")); } catch (e) {
        console.log(e);
        assetAccount = [];
    }
    try { accumulatedDepreciationAccount = !localStorage.getItem("accumulatedDepreciationAccount") || localStorage.getItem("accumulatedDepreciationAccount") == "undefined" ? (localStorage.setItem("accumulatedDepreciationAccount", JSON.stringify(commonApiPost("egf-masters", "chartofaccounts", "_search", { tenantId }).responseJSON["chartOfAccounts"] || [])), JSON.parse(localStorage.getItem("accumulatedDepreciationAccount"))) : JSON.parse(localStorage.getItem("accumulatedDepreciationAccount")); } catch (e) {
        console.log(e);
        accumulatedDepreciationAccount = [];
    }
    try { revaluationReserveAccount = !localStorage.getItem("revaluationReserveAccount") || localStorage.getItem("revaluationReserveAccount") == "undefined" ? (localStorage.setItem("revaluationReserveAccount", JSON.stringify(commonApiPost("egf-masters", "chartofaccounts", "_search", { tenantId }).responseJSON["chartOfAccounts"] || [])), JSON.parse(localStorage.getItem("revaluationReserveAccount"))) : JSON.parse(localStorage.getItem("revaluationReserveAccount")); } catch (e) {
        console.log(e);
        revaluationReserveAccount = [];
    }
    try { depreciationExpenseAccount = !localStorage.getItem("depreciationExpenseAccount") || localStorage.getItem("depreciationExpenseAccount") == "undefined" ? (localStorage.setItem("depreciationExpenseAccount", JSON.stringify(commonApiPost("egf-masters", "chartofaccounts", "_search", { tenantId }).responseJSON["chartOfAccounts"] || [])), JSON.parse(localStorage.getItem("depreciationExpenseAccount"))) : JSON.parse(localStorage.getItem("depreciationExpenseAccount")); } catch (e) {
        console.log(e);
        depreciationExpenseAccount = [];
    }
}, 200);
var employees = [];
// var assignments_fund=JSON.parse(localStorage.getItem("assignments_fund"))==null?(localStorage.setItem("assignments_fund",JSON.stringify(getCommonMaster("egf-masters", "funds", "funds").responseJSON["funds"])) || []) :JSON.parse(localStorage.getItem("assignments_fund"));
// var assignments_functionary=JSON.parse(localStorage.getItem("assignments_functionary"))==null?(localStorage.setItem("assignments_functionary",JSON.stringify(getCommonMaster("egf-masters", "functionaries", "funds").responseJSON["functionaries"])) || []) :JSON.parse(localStorage.getItem("assignments_functionary"));
// var assignments_function=JSON.parse(localStorage.getItem("assignments_function"))==null?(localStorage.setItem("assignments_function",JSON.stringify(getCommonMaster("egf-masters", "functions", "functions").responseJSON["functions"])) || []) :JSON.parse(localStorage.getItem("assignments_function"));

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
    // return response.statusText==="Ok"?response.responseJSON[returnObject]:[];
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

function addMandatoryStart(validationObject, prefix = "") {
    for (var key in validationObject) {
        if (prefix === "") {
            if (validationObject[key].required) {
                $(`label[for=${key}]`).append(`<span> *</span>`);
            }
        } else {
            if (validationObject[key].required) {
                $(`label[for=${prefix}\\.${key}]`).append(`<span> *</span>`);
            }
        }

        // $(`#${key}`).attr("disabled",true);
    };
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

// commonApiPost("asset","assetCategories","",{boundaryTypeName:"LOCALITY",hierarchyTypeName:"LOCATION"})

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
