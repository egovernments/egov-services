var baseUrl = window.location.origin;



var tenantId = "ap." + window.location.origin.split("-")[0].split("//")[1];
var authToken = localStorage.getItem("auth-token");
var now = new Date();
var year = now.getFullYear();
var month = now.getMonth();
var date = now.getDate();

//request info from cookies
var requestInfo = {
    "apiId": "eis",
    "ver": "1.0",
    "ts": "01-04-2017 01:01:01",
    "action": "asd",
    "did": "4354648646",
    "key": "xyz",
    "msgId": "654654",
    "requesterId": "61",
    "authToken": authToken
};

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

var employeeType, employeeStatus, group, motherTounge, religion, community, category, bank, recruitmentMode, recruitmentType, recruitmentQuota, assignments_grade, assignments_designation, assignments_department, assignments_fund, assignments_functionary, assignments_function,assignments_position, maritalStatus, user_bloodGroup;
try { employeeType = !localStorage.getItem("employeeType") || localStorage.getItem("employeeType") == "undefined" ? (localStorage.setItem("employeeType", JSON.stringify(getCommonMaster("hr-masters", "employeetypes", "EmployeeType").responseJSON["EmployeeType"] || [])), JSON.parse(localStorage.getItem("employeeType"))) : JSON.parse(localStorage.getItem("employeeType")); } catch (e) {
    console.log(e);
    employeeType = [];
}
try { employeeStatus = !localStorage.getItem("employeeStatus") || localStorage.getItem("employeeStatus") == "undefined" ? (localStorage.setItem("employeeStatus", JSON.stringify(getCommonMaster("hr-masters", "hrstatuses", "HRStatus").responseJSON["HRStatus"] || [])), JSON.parse(localStorage.getItem("employeeStatus"))) : JSON.parse(localStorage.getItem("employeeStatus")); } catch (e) {
    console.log(e);
    employeeStatus = [];
}
try { group = !localStorage.getItem("group") || localStorage.getItem("group") == "undefined" ? (localStorage.setItem("group", JSON.stringify(getCommonMaster("hr-masters", "groups", "Group").responseJSON["Group"] || [])), JSON.parse(localStorage.getItem("group"))) : JSON.parse(localStorage.getItem("group")); } catch (e) {
    console.log(e);
    group = [];
}
try { maritalStatus = !localStorage.getItem("maritalStatus") || localStorage.getItem("maritalStatus") == "undefined" ? (localStorage.setItem("maritalStatus", JSON.stringify(commonApiPost("hr-employee", "maritalstatuses", "_search", {tenantId, pageSize:500}).responseJSON["MaritalStatus"] || [])), JSON.parse(localStorage.getItem("maritalStatus"))) : JSON.parse(localStorage.getItem("maritalStatus")); } catch (e) {
    console.log(e);
    maritalStatus = [];
}
try { user_bloodGroup = !localStorage.getItem("user_bloodGroup") || localStorage.getItem("user_bloodGroup") == "undefined" ? (localStorage.setItem("user_bloodGroup", JSON.stringify(commonApiPost("hr-employee", "bloodgroups", "_search", {tenantId, pageSize:500}).responseJSON["BloodGroup"] || [])), JSON.parse(localStorage.getItem("user_bloodGroup"))) : JSON.parse(localStorage.getItem("user_bloodGroup")); } catch (e) {
    console.log(e);
    user_bloodGroup = [];
}
try { motherTounge = !localStorage.getItem("motherTounge") || localStorage.getItem("motherTounge") == "undefined" ? (localStorage.setItem("motherTounge", JSON.stringify(getCommonMaster("egov-common-masters", "languages", "Language").responseJSON["Language"] || [])), JSON.parse(localStorage.getItem("motherTounge"))) : JSON.parse(localStorage.getItem("motherTounge")); } catch (e) {
    console.log(e);
    motherTounge = [];
}
try { religion = !localStorage.getItem("religion") || localStorage.getItem("religion") == "undefined" ? (localStorage.setItem("religion", JSON.stringify(getCommonMaster("egov-common-masters", "religions", "Religion").responseJSON["Religion"] || [])), JSON.parse(localStorage.getItem("religion"))) : JSON.parse(localStorage.getItem("religion")); } catch (e) {
    console.log(e);
    religion = [];
}
try { community = !localStorage.getItem("community") || localStorage.getItem("community") == "undefined" ? (localStorage.setItem("community", JSON.stringify(getCommonMaster("egov-common-masters", "communities", "Community").responseJSON["Community"] || [])), JSON.parse(localStorage.getItem("community"))) : JSON.parse(localStorage.getItem("community")); } catch (e) {
    console.log(e);
    community = [];
}
try { category = !localStorage.getItem("category") || localStorage.getItem("category") == "undefined" ? (localStorage.setItem("category", JSON.stringify(getCommonMaster("egov-common-masters", "categories", "Category").responseJSON["Category"] || [])), JSON.parse(localStorage.getItem("category"))) : JSON.parse(localStorage.getItem("category")); } catch (e) {
    console.log(e);
    category = [];
}
try { bank = !localStorage.getItem("bank") || localStorage.getItem("bank") == "undefined" ? (localStorage.setItem("bank", JSON.stringify(getCommonMaster("egf-masters", "banks", "banks").responseJSON["banks"] || [])), JSON.parse(localStorage.getItem("bank"))) : JSON.parse(localStorage.getItem("bank")); } catch (e) {
    console.log(e);
    bank = [];
}
try { recruitmentMode = !localStorage.getItem("recruitmentMode") || localStorage.getItem("recruitmentMode") == "undefined" ? (localStorage.setItem("recruitmentMode", JSON.stringify(getCommonMaster("hr-masters", "recruitmentmodes", "RecruitmentMode").responseJSON["RecruitmentMode"] || [])), JSON.parse(localStorage.getItem("recruitmentMode"))) : JSON.parse(localStorage.getItem("recruitmentMode")); } catch (e) {
    console.log(e);
    recruitmentMode = [];
}
try { recruitmentType = !localStorage.getItem("recruitmentType") || localStorage.getItem("recruitmentType") == "undefined" ? (localStorage.setItem("recruitmentType", JSON.stringify(getCommonMaster("hr-masters", "recruitmenttypes", "RecruitmentType").responseJSON["RecruitmentType"] || [])), JSON.parse(localStorage.getItem("recruitmentType"))) : JSON.parse(localStorage.getItem("recruitmentType")); } catch (e) {
    console.log(e);
    recruitmentType = [];
}
try { recruitmentQuota = !localStorage.getItem("recruitmentQuota") || localStorage.getItem("recruitmentQuota") == "undefined" ? (localStorage.setItem("recruitmentQuota", JSON.stringify(getCommonMaster("hr-masters", "recruitmentquotas", "RecruitmentQuota").responseJSON["RecruitmentQuota"] || [])), JSON.parse(localStorage.getItem("recruitmentQuota"))) : JSON.parse(localStorage.getItem("recruitmentQuota")); } catch (e) {
    console.log(e);
    recruitmentQuota = [];
}
try { assignments_grade = !localStorage.getItem("assignments_grade") || localStorage.getItem("assignments_grade") == "undefined" ? (localStorage.setItem("assignments_grade", JSON.stringify(getCommonMaster("hr-masters", "grades", "Grade").responseJSON["Grade"] || [])), JSON.parse(localStorage.getItem("assignments_grade"))) : JSON.parse(localStorage.getItem("assignments_grade")); } catch (e) {
    console.log(e);
    assignments_grade = [];
}
try { assignments_designation = !localStorage.getItem("assignments_designation") || localStorage.getItem("assignments_designation") == "undefined" ? (localStorage.setItem("assignments_designation", JSON.stringify(getCommonMaster("hr-masters", "designations", "Designation").responseJSON["Designation"] || [])), JSON.parse(localStorage.getItem("assignments_designation"))) : JSON.parse(localStorage.getItem("assignments_designation")); } catch (e) {
    console.log(e);
    assignments_designation = [];
}
try { assignments_department = !localStorage.getItem("assignments_department") || localStorage.getItem("assignments_department") == "undefined" ? (localStorage.setItem("assignments_department", JSON.stringify(getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"] || [])), JSON.parse(localStorage.getItem("assignments_department"))) : JSON.parse(localStorage.getItem("assignments_department")); } catch (e) {
    console.log(e);
    assignments_department = [];
}
try { assignments_fund = !localStorage.getItem("assignments_fund") || localStorage.getItem("assignments_fund") == "undefined" ? (localStorage.setItem("assignments_fund", JSON.stringify(getCommonMaster("egf-masters", "funds", "funds").responseJSON["funds"])) || []) : JSON.parse(localStorage.getItem("assignments_fund")); } catch (e) {
    console.log(e);
    assignments_fund = [];
}
try { assignments_functionary = !localStorage.getItem("assignments_functionary") || localStorage.getItem("assignments_functionary") == "undefined" ? (localStorage.setItem("assignments_functionary", JSON.stringify(getCommonMaster("egf-masters", "functionaries", "funds").responseJSON["functionaries"] || [])), JSON.parse(localStorage.getItem("assignments_functionary"))) : JSON.parse(localStorage.getItem("assignments_functionary")); } catch (e) {
    console.log(e);
    assignments_functionary = [];
}
try { assignments_function = !localStorage.getItem("assignments_function") || localStorage.getItem("assignments_function") == "undefined" ? (localStorage.setItem("assignments_function", JSON.stringify(getCommonMaster("egf-masters", "functions", "functions").responseJSON["functions"] || [])), JSON.parse(localStorage.getItem("assignments_function"))) : JSON.parse(localStorage.getItem("assignments_function")); } catch (e) {
    console.log(e);
    assignments_function = [];
}
try { year = !localStorage.getItem("year") || localStorage.getItem("year") == "undefined" ? (localStorage.setItem("year", JSON.stringify(getCommonMaster("egov-common-masters", "calendaryears", "CalendarYear").responseJSON["CalendarYear"] || [])), JSON.parse(localStorage.getItem("year"))) : JSON.parse(localStorage.getItem("year")); } catch (e) {
    console.log(e);
    year = [];
}
try { jurisdictions_jurisdictionsType = !localStorage.getItem("jurisdictions_jurisdictionsType") || localStorage.getItem("jurisdictions_jurisdictionsType") == "undefined" ? (localStorage.setItem("jurisdictions_jurisdictionsType", JSON.stringify(commonApiPost("egov-location/boundarytypes", "getByHierarchyType", "", { tenantId, hierarchyTypeName: "ADMINISTRATION" }).responseJSON["BoundaryType"] || [])), JSON.parse(localStorage.getItem("jurisdictions_jurisdictionsType"))) : JSON.parse(localStorage.getItem("jurisdictions_jurisdictionsType")); } catch (e) {
    console.log(e);
    jurisdictions_jurisdictionsType = [];
}
try {
  assignments_position = !localStorage.getItem("assignments_position") || localStorage.getItem("assignments_position") == "undefined" ? (localStorage.setItem("assignments_position", JSON.stringify(getCommonMaster("hr-masters", "positions", "Position").responseJSON["Position"] || [])), JSON.parse(localStorage.getItem("assignments_position"))) : JSON.parse(localStorage.getItem("assignments_position"));
} catch (e) {
  console.log(e);
  assignments_position = [];

}

function getCommonMaster(mainRoute, resource, returnObject, pageSize) {
    blockUI();
    var res = $.ajax({
        url: baseUrl + "/" + mainRoute + "/" + resource + "/_search?tenantId=" + tenantId + "&" + "pageSize=" + (pageSize || 500),
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

// commonApiPost("asset","assetCategories","",{boundaryTypeName:"LOCALITY",hierarchyTypeName:"LOCATION"})


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

function getCookie(cname) {
    var name = cname + "=";
    var decodedCookie = decodeURIComponent(document.cookie);
    var ca = decodedCookie.split(';');
    for (var i = 0; i < ca.length; i++) {
        var c = ca[i];
        while (c.charAt(0) == ' ') {
            c = c.substring(1);
        }
        if (c.indexOf(name) == 0) {
            return c.substring(name.length, c.length);
        }
    }
    return "";
}
