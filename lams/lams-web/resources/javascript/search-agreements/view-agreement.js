// function getUrlVars() {
//     var vars = [],
//         hash;
//     var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
//     for (var i = 0; i < hashes.length; i++) {
//         hash = hashes[i].split('=');
//         vars.push(hash[0]);
//         vars[hash[0]] = hash[1];
//     }
//     return vars;
// }

function getValueByName(name,id) {
    for (var i = 0; i < assetCategories.length; i++) {
        if (assetCategories[i].id==id) {
            return assetCategories[i][name];
        }
    }
}

var _type;
$(document).ready(function() {

    $("#viewDcb").on("click", function() {
        //clear cookies and logout
        // $("#login").hide();
        // $("#dashboard").show();
        window.location = "app/search-agreement/view-dcb.html"
    });

    $("#close").on("click", function() {
        open(location, '_self').close();
    })

    try {
        if(getUrlVars()["agreementNumber"]) {
            var agreementDetail = commonApiPost("lams-services", "agreements", "_search", {
                agreementNumber: getUrlVars()["agreementNumber"]
            }).responseJSON["Agreements"][0] || {};
        } else {
             var agreementDetail = commonApiPost("lams-services", "agreements", "_search", {
                stateId: getUrlVars()["state"]
            }).responseJSON["Agreements"][0] || {};
        }


        var assetDetails = commonApiPost("asset-services", "assets", "_search", {
            id: (getUrlVars()["assetId"] || agreementDetail.asset.id)
        }).responseJSON["Assets"][0] || {};


        printValue("", agreementDetail);
        printValue("", assetDetails, true);
    } catch(e) {
        console.log(e);
    }

    function printValue(object = "", values, isAsset) {
        if (object != "") {

        } else {
            for (var key in values) {
                if (typeof values[key] === "object") {
                    for (ckey in values[key]) {
                        if(values[key][ckey]) {
                            //Get description
                            if(isAsset && key == "locationDetails" && ["locality", "electionWard", "street", "revenueWard", "revenueZone", "revenueBlock"].indexOf(ckey) > -1) {
                                var _obj;
                                switch(ckey) {
                                    case 'locality':
                                        _obj = locality;
                                        break;
                                    case 'electionWard':
                                        _obj = electionwards;
                                        break;
                                    case 'street':
                                        _obj = street;
                                        break;
                                    case 'revenueWard':
                                        _obj = revenueWard;
                                        break;
                                    case 'revenueZone':
                                        _obj = revenueZone;
                                        break;
                                    case 'revenueBlock':
                                        _obj = revenueZone;
                                        break;
                                }
                                $("[name='" + (isAsset ? "asset." : "") + key + "." + ckey + "']").text(getNameById(_obj, ckey) || "NA");
                            }
                            else
                                $("[name='" + (isAsset ? "asset." : "") + key + "." + ckey + "']").text(values[key][ckey] ? values[key][ckey] : "NA");
                        }
                    }
                } else if(values[key]) {
                    $("[name='" + (isAsset ? "asset." : "") + key + "']").text(values[key]);
                } else {
                    $("[name='" + (isAsset ? "asset." : "") + key + "']").text("NA");
                }

                if (key.search('date')>0) {
                        var d = new Date(values[key]);
                        $(`#${key}`).val(`${d.getDate()}-${d.getMonth()+1}-${d.getFullYear()}`);
                }
            }
        }
    }

    //base url for api_id
    // var baseUrl = "https://peaceful-headland-36194.herokuapp.com/v1/mSevaAndLA/";
    // //request info from cookies
    // var requestInfo = {
    //     "api_id": "string",
    //     "ver": "string",
    //     "ts": "2017-01-18T07:18:23.130Z",
    //     "action": "string",
    //     "did": "string",
    //     "key": "string",
    //     "msg_id": "string",
    //     "requester_id": "string",
    //     "auth_token": "aeiou"
    // };

    //agreementDetail = {};


    var renewAgreement = {};
    //Getting data for user input
    $("input").on("keyup", function() {
        // console.log(this.value);
        renewAgreement[this.id] = this.value;
    });

    //Getting data for user input
    $("select").on("change", function() {
        // console.log(this.value);
        renewAgreement[this.id] = this.value;

        if (($("#approver_department").val() != "" && $("#approver_designation").val() != "") && (this.id == "approver_department" || this.id == "approver_designation")) {
                employees = commonApiPost("hr-employee", "employees", "_search", {
                tenantId,
                departmentId: $("#approver_department").val(),
                designationId: $("#approver_designation").val()
            }).responseJSON["Employee"] || [];

            for (var i = 0; i < employees.length; i++) {
                $(`#approver_name`).append(`<option value='${employees[i]['id']}'>${employees[i]['name']}</option>`)
            }
        }
    });

    //file change handle for file upload
    $("input[type=file]").on("change", function(evt) {
        // console.log(this.value);
        // renewAgreement[this.id] = this.value;
        var file = evt.currentTarget.files[0];

        //call post api update and update that url in pur agrement object
    });

    var validation_rules = {};
    var final_validatin_rules = {};
    var commom_fields_rules = {

        renew_order_no: {
            required: true
        },
        renew_advance_payment: {
            required: true
        },
        renew_rent: {
            required: true
        },
        renew_reason: {
            required: true
        },
        renew_rent_increment_method: {
            required: true
        }
    };

    //remove renew part and related buttons from dom
     if (getUrlVars()["view"] == "new") {
        //removing renew section and renew button
        $("#renew,#workFlowDetails").remove();
    } else if(getUrlVars()["view"] == "inbox") {
        $("#historyTable").show();
        $("#renew").remove();
        $("#renewBtn").remove();
        //Fetch workFlow
        var workflow = commonApiPost("egov-common-workflows", "history", "", {
            tenantId: tenantId,
            workflowId: agreementDetail.stateId
        }).responseJSON["tasks"];

        var process = commonApiPost("egov-common-workflows", "process", "_search", {
            tenantId: tenantId,
            id: agreementDetail.stateId
        }).responseJSON["processInstance"];

        if(workflow && workflow.length) {
            workflow = workflow.sort();
            for(var i=0; i < workflow.length; i++) {
                $("#historyTable tbody").append(`<tr>
                    <td data-label="createdDate">${workflow[i].createdDate}</td>
                    <td data-label="updatedBy">${workflow[i].senderName}</td>
                    <td data-label="status">${workflow[i].status}</td>
                    <td data-label="comments">${workflow[i].comments}</td>
                    </tr>
                `);
            }
        }

        if(process && process.attributes && process.attributes.validActions && process.attributes.validActions.values && process.attributes.validActions.values.length) {
            for(var i=0;i<process.attributes.validActions.values.length;i++) {
                $("#footer-btn-grp").append($(`<button data-action=${process.attributes.validActions.values[i].key} id=${process.attributes.validActions.values[i].key} type="button" class="btn btn-submit">${process.attributes.validActions.values[i].name}<button/>`));
                if(process.attributes.validActions.values[i].key == "Approve") {
                    $("#workFlowDetails").remove();
                }
            }
        } else {
            $("#workFlowDetails").remove();
        }

        if(process) {
            getDesignations(process.status, function(designations) {
                for (var variable in designations) {
                    $(`#approver_designation`).append(`<option value='${designations[variable]["id"]}'>${designations[variable]["name"]}</option>`)
                }
            });
        }
    } else {
        $("#viewDcb").remove();
    }

    for (var variable in department) {
      $(`#approver_department`).append(`<option value='${department[variable]["id"]}'>${department[variable]["name"]}</option>`)
    }
   /* for (var variable in designation) {
      $(`#approver_designation`).append(`<option value='${designation[variable]["id"]}'>${designation[variable]["name"]}</option>`)
    }*/

    _type = getUrlVars()["type"] ? decodeURIComponent(getUrlVars()["type"]) : getValueByName("name",agreementDetail.asset.assetCategory.id);
    if (_type == "land") {

        // remove all other Asset Details block from DOM except land asset related fields
        $("#shopAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateTwo,#agreementDetailsBlockTemplateThree").remove();

    } else if (_type == "shop") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateThree").remove();

    } else if (_type == "market") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    } else if (_type == "kalyanamandapam") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    } else if (_type == "parking_space") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();

    } else if (_type == "slaughter_house") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();

    } else if (_type == "usfructs") {


        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    } else if (_type == "community") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    } else if (_type == "Fish Tank") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    } else if (_type == "park") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    }
    final_validatin_rules = Object.assign(validation_rules, commom_fields_rules);
    for (var key in final_validatin_rules) {
        if (final_validatin_rules[key].required) {
            $(`label[for=${key}]`).append(`<span> *</span>`);
        }
        // $(`#${key}`).attr("disabled",true);
    };



    $('body').on('click', 'button', function(e) {
        e.preventDefault();
        if(!e.target.id) return;
        var data = $("#" + e.target.id).data();
        if(data.action) {
            var _agrmntDet = Object.assign({}, agreementDetail);
            _agrmntDet.workflowDetails = {
                    "businessKey": process.businessKey,
                    "type":"Agreement",
                    "assignee": $("#approver_name") && $("#approver_name").val() ? $("#approver_name").val() : process.initiator,
                    "status": process.status,
                    "action": data.action
            };

            var response = $.ajax({
                    url: baseUrl + `/lams-services/agreements/_update/${agreementDetail.acknowledgementNumber}?tenantId=`+tenantId,
                    type: 'POST',
                    dataType: 'json',
                    data:JSON.stringify({
                        RequestInfo: requestInfo,
                        Agreement: _agrmntDet
                    }),
                    async: false,
                    headers: {
                            'auth-token': authToken
                        },
                    contentType: 'application/json'
                });
            if(response["status"] === 201) {
                open(location, '_self').close();
            } else {
                console.log("Handle error.");
            }
        }
    });

    // Adding Jquery validation dynamically
    $("#renewAgreementForm").validate({
        rules: final_validatin_rules,
        submitHandler: function(form) {

        }
    })
});
