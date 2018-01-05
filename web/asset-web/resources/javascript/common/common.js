var authToken = localStorage.getItem("auth-token");
var baseUrl = window.location.origin;


//request info from cookies
var requestInfo = {
    "apiId": "org.egov.pgr",
    "ver": "1.0",
    "ts": "01-04-2017 01:01:01",
    "action": "asd",
    "did": "4354648646",
    "key": "xyz",
    "msgId": "654654",
    "requesterId": "61",
    "authToken": authToken
};

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

var employees = [];

function getCommonMaster(mainRoute, resource, cb) {
    $.ajax({
        url: baseUrl + "/" + mainRoute + "/" + resource + "/_search?tenantId=" + tenantId,
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify({ RequestInfo: requestInfo }),
        headers: {
            'auth-token': authToken
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
            'auth-token': authToken
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
            'auth-token': authToken
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
            'auth-token': authToken
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
        `<div id="error-alert-div" class="alert alert-danger alert-dismissible alert-toast" role="alert" style="display:none; z-index:100000">
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


function getDropdown(name, cb, params) {
    switch (name) {
        case 'assetCategories':
            if (!localStorage.getItem("assetCategories") || localStorage.getItem("assetCategories") == "undefined") {
                var queryString = { tenantId };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("asset-services", "assetCategories", "_search", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("assetCategories", JSON.stringify(res["AssetCategory"]));
                        cb(res["AssetCategory"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("assetCategories")));
            }
            break;
        case 'locality':
            if (!localStorage.getItem("locality") || localStorage.getItem("locality") == "undefined") {
                var queryString = { boundaryTypeName: "LOCALITY", hierarchyTypeName: "LOCATION", tenantId };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("locality", JSON.stringify(res["Boundary"]));
                        cb(res["Boundary"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("locality")));
            }
            break;
        case 'electionwards':
            if (!localStorage.getItem("electionwards") || localStorage.getItem("electionwards") == "undefined") {
                var queryString = { boundaryTypeName: "WARD", hierarchyTypeName: "ADMINISTRATION", tenantId };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("electionwards", JSON.stringify(res["Boundary"]));
                        cb(res["Boundary"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("electionwards")));
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
        case 'acquisitionList':
            if (!localStorage.getItem("acquisitionList") || localStorage.getItem("acquisitionList") == "undefined") {
                var queryString = { tenantId };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("asset-services", "", "GET_MODE_OF_ACQUISITION", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("acquisitionList", JSON.stringify(res.ModeOfAcquisition));
                        cb(res.ModeOfAcquisition);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("acquisitionList")));
            }
            break;
        case 'revenueZone':
            if (!localStorage.getItem("revenueZone") || localStorage.getItem("revenueZone") == "undefined") {
                var queryString = { boundaryTypeName: "ZONE", hierarchyTypeName: "ADMINISTRATION", tenantId };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("revenueZone", JSON.stringify(res["Boundary"]));
                        cb(res["Boundary"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("revenueZone")));
            }
            break;
        case 'street':
            if (!localStorage.getItem("street") || localStorage.getItem("street") == "undefined") {
                var queryString = { boundaryTypeName: "STREET", hierarchyTypeName: "LOCATION", tenantId };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("street", JSON.stringify(res["Boundary"]));
                        cb(res["Boundary"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("street")));
            }
            break;
        case 'revenueWard':
            if (!localStorage.getItem("revenueWard") || localStorage.getItem("revenueWard") == "undefined") {
                var queryString = { boundaryTypeName: "WARD", hierarchyTypeName: "REVENUE", tenantId };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("revenueWard", JSON.stringify(res["Boundary"]));
                        cb(res["Boundary"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("revenueWard")));
            }
            break;
        case 'revenueBlock':
            if (!localStorage.getItem("revenueBlock") || localStorage.getItem("revenueBlock") == "undefined") {
                var queryString = { boundaryTypeName: "BLOCK", hierarchyTypeName: "REVENUE", tenantId };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("revenueBlock", JSON.stringify(res["Boundary"]));
                        cb(res["Boundary"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("revenueBlock")));
            }
            break;
        case 'statusList':
            if (!localStorage.getItem("statusList") || localStorage.getItem("statusList") == "undefined") {
                var queryString = { tenantId };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("asset-services", "assetstatuses", "_search", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("statusList", JSON.stringify(res["AssetStatus"][0]["statusValues"]));
                        cb(res["AssetStatus"][0]["statusValues"]);
                    } else {
                        cb({});
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("statusList")));
            }
            break;
        case 'asset_category_type':
            if (!localStorage.getItem("asset_category_type") || localStorage.getItem("asset_category_type") == "undefined") {
                var queryString = { tenantId };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("asset-services", "", "GET_ASSET_CATEGORY_TYPE", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("asset_category_type", JSON.stringify(res.AssetCategoryType));
                        cb(res.AssetCategoryType);
                    } else {
                        cb({});
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("asset_category_type")));
            }
            break;
        case 'assignments_unitOfMeasurement':
            if (!localStorage.getItem("assignments_unitOfMeasurement") || localStorage.getItem("assignments_unitOfMeasurement") == "undefined") {
                var queryString = { tenantId };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("egov-common-masters", "uoms", "_search", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("assignments_unitOfMeasurement", JSON.stringify(res["UOM"]));
                        cb(res["UOM"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("assignments_unitOfMeasurement")));
            }
            break;
        case 'depreciationMethod':
            if (!localStorage.getItem("depreciationMethod") || localStorage.getItem("depreciationMethod") == "undefined") {
                var queryString = { tenantId };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("asset-services", "", "GET_DEPRECIATION_METHOD", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("depreciationMethod", JSON.stringify(res.DepreciationMethod));
                        cb(res.DepreciationMethod);
                    } else {
                        cb({});
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("depreciationMethod")));
            }
            break;
        case 'assetAccount':
            if (!localStorage.getItem("assetAccount") || localStorage.getItem("assetAccount") == "undefined") {
                var queryString = { tenantId, classification: 4 };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("egf-masters", "chartofaccounts", "_search", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("assetAccount", JSON.stringify(res["chartOfAccounts"]));
                        cb(res["chartOfAccounts"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("assetAccount")));
            }
            break;
        case 'accumulatedDepreciationAccount':
            if (!localStorage.getItem("accumulatedDepreciationAccount") || localStorage.getItem("accumulatedDepreciationAccount") == "undefined") {
                var queryString = { tenantId, classification: 4 };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("egf-masters", "chartofaccounts", "_search", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("accumulatedDepreciationAccount", JSON.stringify(res["chartOfAccounts"]));
                        cb(res["chartOfAccounts"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("accumulatedDepreciationAccount")));
            }
            break;
        case 'revaluationReserveAccount':
            if (!localStorage.getItem("revaluationReserveAccount") || localStorage.getItem("revaluationReserveAccount") == "undefined") {
                var queryString = { tenantId, classification: 4 };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("egf-masters", "chartofaccounts", "_search", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("revaluationReserveAccount", JSON.stringify(res["chartOfAccounts"]));
                        cb(res["chartOfAccounts"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("revaluationReserveAccount")));
            }
            break;
        case 'depreciationExpenseAccount':
            if (!localStorage.getItem("depreciationExpenseAccount") || localStorage.getItem("depreciationExpenseAccount") == "undefined") {
                var queryString = { tenantId, classification: 4 };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("egf-masters", "chartofaccounts", "_search", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("depreciationExpenseAccount", JSON.stringify(res["chartOfAccounts"]));
                        cb(res["chartOfAccounts"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("depreciationExpenseAccount")));
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
        case 'fixedAssetAccount':
            if (!localStorage.getItem("fixedAssetAccount") || localStorage.getItem("fixedAssetAccount") == "undefined") {
                var queryString = { tenantId, classification: 4 };
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("egf-masters", "chartofaccounts", "_search", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("fixedAssetAccount", JSON.stringify(res["chartOfAccounts"]));
                        cb(res["chartOfAccounts"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("fixedAssetAccount")));
            }
            break;
        case 'financialYears':
            if (!localStorage.getItem("financialYears") || localStorage.getItem("financialYears") == "undefined") {
                var queryString = {tenantId, active: true};
                if (params && typeof params == "object")
                    queryString = Object.assign(queryString, params);
                commonApiPost("egf-masters", "financialyears", "_search", queryString, function(err, res) {
                    if (res) {
                        localStorage.setItem("financialYears", JSON.stringify(res["financialYears"]));
                        cb(res["financialYears"]);
                    } else {
                        cb([]);
                    }
                })
            } else {
                cb(JSON.parse(localStorage.getItem("financialYears")));
            }
            break;
    }
}

function getTimestamp() {
    return new Date().getTime();
}
