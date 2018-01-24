var baseUrl = window.location.origin;
var tenantId = JSON.parse(localStorage.getItem("userRequest")) || "";

if (tenantId) {
  tenantId=tenantId.tenantId;
} else {
  if(window.location.origin.split("-").length>1)
  {
    tenantId+=window.location.origin.split("-")[0].split("//")[1]
  }
  else {
    tenantId+=window.location.origin.split(".")[0].split("//")[1]
  }

  tenantId = tenantIds[tenantId] || "ap." + tenantId;

}

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
function getCommonMaster(mainRoute, resource, cb, pageSize) {
    $.ajax({
        url: baseUrl + "/" + mainRoute + "/" + resource + "/_search?tenantId=" + tenantId + "&" + "pageSize=" + (pageSize || 500),
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify({ RequestInfo: requestInfo }),
        headers: {
            'auth-token': authToken,
            Authorization:'Basic Og=='
        },
        contentType: 'application/json',
        success: function(res) {
            cb(null, res);
        },
        error: function(err) {
            cb(err);
        }
    });
}

function commonApiPost(context, resource = "", action = "", queryObject = {}, cb) {
    var url = baseUrl + "/" + context + (resource ? "/" + resource : "") + (action ? "/" + action : "") + (queryObject ? "?" : "");
    for (var variable in queryObject) {
        if (queryObject[variable]) {
            url += "&" + variable + "=" + queryObject[variable];
        }
    }
    $.ajax({
        url: url,
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify({ RequestInfo: requestInfo }),
        contentType: 'application/json',
        headers: {
            'auth-token': authToken,
            Authorization:'Basic Og=='
        },
        success: function(res) {
            cb(null, res);
        },
        error: function(err) {
            cb(err);
        }
    });
}
function commonApiGet(context, resource = "", action = "", queryObject = {}, cb) {
    var url = baseUrl + "/" + context + (resource ? "/" + resource : "") + (action ? "/" + action : "") + (queryObject ? "?" : "");
    for (var variable in queryObject) {
        if (queryObject[variable]) {
            url += "&" + variable + "=" + queryObject[variable];
        }
    }
    $.ajax({
        url: url,
        type: 'GET',
        dataType: 'json',
        headers: {
            'auth-token': authToken,
            Authorization:'Basic Og=='
        },
        contentType: 'application/json',
        success: function(res) {
            cb(null, res);
        },
        error: function(err) {
            cb(err);
        }
    });
}
function titleCase(field) {
    if (field) {
        var newField = field[0].toUpperCase();
        for (let i = 1; i < field.length; i++) {
            if (field[i - 1] != " " && field[i] != " ") {
                newField += field.charAt(i).toLowerCase();
            } else {
                newField += field[i]
            }
        }
        return newField;
    } else {
        return "";
    }
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
function getCommonMasterById(mainRoute, resource, id, cb) {
    $.ajax({
        url: baseUrl + "/" + mainRoute + "/" + resource + "/_search?tenantId=" + tenantId + "&" + "id=" + id,
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify({ RequestInfo: requestInfo }),
        headers: {
            'auth-token': authToken,
            Authorization:'Basic Og=='
        },
        contentType: 'application/json',
        success: function(res) {
            cb(null, res);
        },
        error: function(err) {
            cb(err);
        }
    });
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
            <span id="success-alert-span" style="white-space: pre-line;"></span>
        </div>`
    );
    $('#success-alert-span').text(message);
    $('#success-alert-div').show();
    setTimeout(function() {
        $('#success-alert-div').remove();
    }, 6000);
}
function showError(message) {
    $("body").append(
        `<div id="error-alert-div" class="alert alert-danger alert-dismissible alert-toast" role="alert" style="display:none; z-index:100000;">
            <button type="button" class="close" data-dismiss="alert" aria-label="Close"><span aria-hidden="true">&times;</span></button>
            <span id="error-alert-span" style="white-space: pre-line;"></span>
        </div>`
    )
    $('#error-alert-span').text(message);
    $('#error-alert-div').show();
    setTimeout(function() {
        $('#error-alert-div').remove();
    }, 6000);
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
function getDropdown(name, cb, params) {
    switch (name) {
        case 'employeeType':
            if (!localStorage.getItem("employeeType") || localStorage.getItem("employeeType") == "undefined") {
                getCommonMaster("hr-masters", "employeetypes", function(err, res) {
                    if (res) {
                        localStorage.setItem("employeeType", JSON.stringify(res["EmployeeType"]));
                        cb(res["EmployeeType"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("employeeType")));
            }
            break;
        case 'employeeStatus':
            if (!localStorage.getItem("employeeStatus") || localStorage.getItem("employeeStatus") == "undefined") {
              var queryString = {tenantId, pageSize:500, objectName:"Employee Master"};
              if (params && typeof params == "object")
                  queryString = Object.assign(queryString, params);
                commonApiPost("hr-masters", "hrstatuses", "_search", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("employeeStatus", JSON.stringify(res["HRStatus"]));
                        cb(res["HRStatus"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("employeeStatus")));
            }
            break;
            case 'employeeStatus':
                if (!localStorage.getItem("employeeStatus") || localStorage.getItem("employeeStatus") == "undefined") {
                  var queryString = {tenantId, pageSize:500, objectName:"Employee Master"};
                  if (params && typeof params == "object")
                      queryString = Object.assign(queryString, params);
                    commonApiPost("hr-masters", "hrstatuses", "_search", queryString, function(err, res) {
                        if (res) {
                            localStorage.setItem("employeeStatus", JSON.stringify(res["HRStatus"]));
                            cb(res["HRStatus"]);
                        } else {
                            cb([]);
                        }
                    })
                } else {
                    cb(JSON.parse(localStorage.getItem("employeeStatus")));
                }
                break;
            case 'leaveStatus':
                if (!localStorage.getItem("leaveStatus") || localStorage.getItem("leaveStatus") == "undefined") {
                  var queryString = {tenantId, pageSize:500, objectName:"LeaveApplication"};
                  if (params && typeof params == "object")
                      queryString = Object.assign(queryString, params);
                    commonApiPost("hr-masters", "hrstatuses", "_search", queryString, function(err, res) {
                        if (res) {
                            localStorage.setItem("leaveStatus", JSON.stringify(res["HRStatus"]));
                            cb(res["HRStatus"]);
                        } else {
                            cb([]);
                        }
                    })
                } else {
                    cb(JSON.parse(localStorage.getItem("leaveStatus")));
                }
                break;
        case 'group':
            if (!localStorage.getItem("group") || localStorage.getItem("group") == "undefined") {
                getCommonMaster("hr-masters", "groups", function(err, res) {
                    if (res) {
                        localStorage.setItem("group", JSON.stringify(res["Group"]));
                        cb(res["Group"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("group")));
            }
            break;
        case 'maritalStatus':
            if (!localStorage.getItem("maritalStatus") || localStorage.getItem("maritalStatus") == "undefined") {
                var queryString = {tenantId, pageSize:500};
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("hr-employee", "maritalstatuses", "_search", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("maritalStatus", JSON.stringify(res["MaritalStatus"]));
                        cb(res["MaritalStatus"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("maritalStatus")));
            }
            break;
        case 'user_bloodGroup':
            if (!localStorage.getItem("user_bloodGroup") || localStorage.getItem("user_bloodGroup") == "undefined") {
                var queryString = {tenantId, pageSize:500};
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("hr-employee", "bloodgroups", "_search", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("user_bloodGroup", JSON.stringify(res["BloodGroup"]));
                        cb(res["BloodGroup"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("user_bloodGroup")));
            }
            break;
        case 'motherTongue':
            if (!localStorage.getItem("motherTongue") || localStorage.getItem("motherTongue") == "undefined") {
                getCommonMaster("egov-common-masters", "languages", function(err, res) {
                    if (res) {
                        localStorage.setItem("motherTongue", JSON.stringify(res["Language"]));
                        cb(res["Language"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("motherTongue")));
            }
            break;
        case 'religion':
            if (!localStorage.getItem("religion") || localStorage.getItem("religion") == "undefined") {
                getCommonMaster("egov-common-masters", "religions", function(err, res) {
                    if (res) {
                        localStorage.setItem("religion", JSON.stringify(res["Religion"]));
                        cb(res["Religion"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("religion")));
            }
            break;
        case 'community':
            if (!localStorage.getItem("community") || localStorage.getItem("community") == "undefined") {
                getCommonMaster("egov-common-masters", "communities", function(err, res) {
                    if (res) {
                        localStorage.setItem("community", JSON.stringify(res["Community"]));
                        cb(res["Community"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("community")));
            }
            break;
        case 'category':
            if (!localStorage.getItem("category") || localStorage.getItem("category") == "undefined") {
                getCommonMaster("egov-common-masters", "categories", function(err, res) {
                    if (res) {
                        localStorage.setItem("category", JSON.stringify(res["Category"]));
                        cb(res["Category"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("category")));
            }
            break;
        case 'bank':
            if (!localStorage.getItem("bank") || localStorage.getItem("bank") == "undefined") {
                getCommonMaster("egf-masters", "banks", function(err, res) {
                    if (res) {
                        localStorage.setItem("bank", JSON.stringify(res["banks"]));
                        cb(res["banks"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("bank")));
            }
            break;
        case 'bankbranches':
            if (!localStorage.getItem("bankbranches") || localStorage.getItem("bankbranches") == "undefined") {
                getCommonMaster("egf-masters", "bankbranches", function(err, res) {
                    if (res) {
                        localStorage.setItem("bankbranches", JSON.stringify(res["bankBranches"]));
                        cb(res["bankBranches"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("bankbranches")));
            }
            break;
        case 'recruitmentMode':
            if (!localStorage.getItem("recruitmentMode") || localStorage.getItem("recruitmentMode") == "undefined") {
                getCommonMaster("hr-masters", "recruitmentmodes", function(err, res) {
                    if (res) {
                        localStorage.setItem("recruitmentMode", JSON.stringify(res["RecruitmentMode"]));
                        cb(res["RecruitmentMode"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("recruitmentMode")));
            }
            break;
        case 'recruitmentType':
            if (!localStorage.getItem("recruitmentType") || localStorage.getItem("recruitmentType") == "undefined") {
                getCommonMaster("hr-masters", "recruitmenttypes", function(err, res) {
                    if (res) {
                        localStorage.setItem("recruitmentType", JSON.stringify(res["RecruitmentType"]));
                        cb(res["RecruitmentType"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("recruitmentType")));
            }
            break;
        case 'assignments_grade':
            if (!localStorage.getItem("assignments_grade") || localStorage.getItem("assignments_grade") == "undefined") {
                getCommonMaster("hr-masters", "grades", function(err, res) {
                    if (res) {
                        localStorage.setItem("assignments_grade", JSON.stringify(res["Grade"]));
                        cb(res["Grade"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("assignments_grade")));
            }
            break;
        case 'assignments_fund':
            if (!localStorage.getItem("assignments_fund") || localStorage.getItem("assignments_fund") == "undefined") {
                getCommonMaster("egf-masters", "funds", function(err, res) {
                    if (res) {
                        localStorage.setItem("assignments_fund", JSON.stringify(res["funds"]));
                        cb(res["funds"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("assignments_fund")));
            }
            break;
        case 'assignments_functionary':
            if (!localStorage.getItem("assignments_functionary") || localStorage.getItem("assignments_functionary") == "undefined") {
                getCommonMaster("egf-masters", "functionaries", function(err, res) {
                    if (res) {
                        localStorage.setItem("assignments_functionary", JSON.stringify(res["functionaries"]));
                        cb(res["functionaries"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("assignments_functionary")));
            }
            break;
        case 'assignments_function':
            if (!localStorage.getItem("assignments_function") || localStorage.getItem("assignments_function") == "undefined") {
                getCommonMaster("egf-masters", "functions", function(err, res) {
                    if (res) {
                        localStorage.setItem("assignments_function", JSON.stringify(res["functions"]));
                        cb(res["functions"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("assignments_function")));
            }
            break;
        case 'jurisdictions_jurisdictionsType':
            if (!localStorage.getItem("jurisdictions_jurisdictionsType") || localStorage.getItem("jurisdictions_jurisdictionsType") == "undefined") {
                var queryString = { tenantId, hierarchyTypeName: "ADMINISTRATION" };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("egov-location/boundarytypes", "getByHierarchyType", "", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("jurisdictions_jurisdictionsType", JSON.stringify(res["BoundaryType"]));
                        cb(res["BoundaryType"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("jurisdictions_jurisdictionsType")));
            }
            break;
        case 'assignments_position':
            if (!localStorage.getItem("assignments_position") || localStorage.getItem("assignments_position") == "undefined") {
                commonApiPost("hr-masters", "positions", "_paginatedsearch", { tenantId, pageSize: 500 }, function (err, res) {

                    if (res) {
                        var positions = res["Position"];
                        if (res.Page && res.Page.totalPages > 1) {

                            try {
                                for (var i = 2; i <= res.Page.totalPages; i++) {

                                    var queryParam = { tenantId, pageSize: 500 }
                                    queryParam.pageNumber = i;
                                    commonApiPost("hr-masters", "positions", "_paginatedsearch", queryParam, function (err, res) {
                                        if (res) {
                                            positions = positions.concat(res["Position"]);
                                        }
                                    });
                                }
                            } catch (error) {
                                console.log(error);
                            }
                        }
                        localStorage.setItem("assignments_position", JSON.stringify(positions));
                        cb(positions);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("assignments_position")));
            }
            break;
        case 'assignments_designation':
            if (!localStorage.getItem("assignments_designation") || localStorage.getItem("assignments_designation") == "undefined") {
                getCommonMaster("hr-masters", "designations", function(err, res) {
                    if (res) {
                        localStorage.setItem("assignments_designation", JSON.stringify(res["Designation"]));
                        cb(res["Designation"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("assignments_designation")));
            }
            break;
        case 'assignments_department':
            if (!localStorage.getItem("assignments_department") || localStorage.getItem("assignments_department") == "undefined") {
                getCommonMaster("egov-common-masters", "departments", function(err, res) {
                    if (res) {
                        localStorage.setItem("assignments_department", JSON.stringify(res["Department"]));
                        cb(res["Department"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("assignments_department")));
            }
            break;
        case 'recruitmentQuota':
            if (!localStorage.getItem("recruitmentQuota") || localStorage.getItem("recruitmentQuota") == "undefined") {
                getCommonMaster("hr-masters", "recruitmentquotas", function(err, res) {
                    if (res) {
                        localStorage.setItem("recruitmentQuota", JSON.stringify(res["RecruitmentQuota"]));
                        cb(res["RecruitmentQuota"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("recruitmentQuota")));
            }
            break;
        case 'years':
            if (!localStorage.getItem("years") || localStorage.getItem("years") == "undefined") {
                getCommonMaster("egov-common-masters", "calendaryears", function(err, res) {
                    if (res) {
                        localStorage.setItem("years", JSON.stringify(res["CalendarYear"]));
                        cb(res["CalendarYear"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("years")));
            }
            break;

      case 'futureyears':
          if (!localStorage.getItem("futureyears") || localStorage.getItem("futureyears") == "undefined") {
              commonApiPost("egov-common-masters", "calendaryears", "_searchfutureyears", {tenantId:tenantId} , function(err, res) {
                  if (res) {
                      localStorage.setItem("futureyears", JSON.stringify(res["CalendarYear"]));
                      cb(res["Futureyears"]);
                  } else {
                      cb([]);
                  }
              })
          } else {
              cb(JSON.parse(localStorage.getItem("futureyears")));
          }
          break;
        case 'leaveTypes':
            if (!localStorage.getItem("leaveTypes") || localStorage.getItem("leaveTypes") == "undefined") {
                getCommonMaster("hr-leave", "leavetypes", function(err, res) {
                    if (res) {
                        localStorage.setItem("leaveTypes", JSON.stringify(res["LeaveType"]));
                        cb(res["LeaveType"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("leaveTypes")));
            }
            break;
        case 'accumulativeLeaveTypes':
            if (!localStorage.getItem("accleaveTypes") || localStorage.getItem("accleaveTypes") == "undefined") {
                commonApiPost("hr-leave", "leavetypes", "_search", {tenantId:tenantId,accumulative:true,pageSize:500}, function(err, res) {
                    if (res) {
                        localStorage.setItem("accleaveTypes", JSON.stringify(res["LeaveType"]));
                        cb(res["LeaveType"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("accleaveTypes")));
            }
            break;
        case 'gender':
            if (!localStorage.getItem("gender") || localStorage.getItem("gender") == "undefined") {
                getCommonMaster("egov-common-masters", "genders", function(err, res) {
                    if (res) {
                        localStorage.setItem("gender", JSON.stringify(res["Gender"]));
                        cb(res["Gender"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("gender")));
            }
            break;
        case 'relation':
            if (!localStorage.getItem("relation") || localStorage.getItem("relation") == "undefined") {
                getCommonMaster("egov-common-masters", "relationships", function(err, res) {
                    if (res) {
                        localStorage.setItem("relation", JSON.stringify(res["Relationship"]));
                        cb(res["Relationship"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("relation")));
            }
            break;
        case 'transferType':
              if (!localStorage.getItem('transferType') || localStorage.getItem('transferType') == 'undefined') {
                commonApiPost('hr-employee-movement', 'transfertypes', '_search',{tenantId:tenantId , pageSize:"500", typeOfMovement:"TRANSFER"} ,function (err, res) {
                  if (res) {
                    localStorage.setItem('transferType', JSON.stringify(res['TransferType']));
                    cb(res['TransferType']);
                  } else {
                    cb([]);
                  }
                })
              } else {
                cb(JSON.parse(localStorage.getItem('transferType')));
              }
              break;
        case 'transferReason':
              if (!localStorage.getItem('transferReason') || localStorage.getItem('transferReason') == 'undefined') {
                getCommonMaster('hr-employee-movement', 'transferreason', function (err, res) {
                  if (res) {
                    localStorage.setItem('transferReason', JSON.stringify(res['TransferReason']));
                    cb(res['TransferReason']);
                  } else {
                    cb([]);
                  }
                })
              } else {
                cb(JSON.parse(localStorage.getItem('transferReason')));
              }
              break;
        case 'promotionBasis':
              if (!localStorage.getItem('promotionBasis') || localStorage.getItem('promotionBasis') == 'undefined') {
                getCommonMaster('hr-employee-movement', 'promotionbasis', function (err, res) {
                  if (res) {
                    localStorage.setItem('promotionBasis', JSON.stringify(res['PromotionBasis']));
                    cb(res['PromotionBasis']);
                  } else {
                    cb([]);
                  }
                })
              } else {
                cb(JSON.parse(localStorage.getItem('promotionBasis')));
              }
              break;
        case 'districtList':
              if (!localStorage.getItem('districtList') || localStorage.getItem('districtList') == 'undefined') {
                getCommonMaster('tenant', 'v1/tenant', function (err, res) {
                  if (res) {
                    localStorage.setItem('districtList', JSON.stringify(res['tenant']));
                    cb(res['districtList']);
                  } else {
                    cb([]);
                  }
                })
              } else {
                cb(JSON.parse(localStorage.getItem('districtList')));
              }
              break;
        case 'holidayTypes':
              if (!localStorage.getItem('holidayTypes') || localStorage.getItem('holidayTypes') == 'undefined') {
                getCommonMaster('egov-common-masters', 'holidaytypes', function (err, res) {
                  if (res) {
                    localStorage.setItem('holidayTypes', JSON.stringify(res['HolidayType']));
                    cb(res['districtList']);
                  } else {
                    cb([]);
                  }
                })
              } else {
                cb(JSON.parse(localStorage.getItem('holidayTypes')));
              }
              break;
    }
}
function getTimestamp() {
    return new Date().getTime();
}
