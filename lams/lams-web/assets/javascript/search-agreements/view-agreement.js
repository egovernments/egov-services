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
$(document).ready(function() {

    $("#viewDcb").on("click", function() {
        //clear cookies and logout
        // $("#login").hide();
        // $("#dashboard").show();
        window.location = "./view-dcb.html"
    });

    try {
        var assetDetails = commonApiPost("asset-services", "assets", "_search", {
            assetCategory: getUrlVars()["assetId"]
        }).responseJSON["Assets"][0] || [];
        var agreementDetail = commonApiPost("lams-services", "agreements", "_search", {
            agreementNumber: getUrlVars()["agreementNumber"]
        }).responseJSON["Agreements"][0] || [];
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
                                $("[name='" + (isAsset ? "asset." : "") + key + "." + ckey + "']").text(getNameById(_obj, ckey));
                            }
                            else
                                $("[name='" + (isAsset ? "asset." : "") + key + "." + ckey + "']").text(values[key][ckey]);
                        }
                    }
                } else if(values[key]) {
                    $("[name='" + (isAsset ? "asset." : "") + key + "']").text(values[key]);
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

    agreementDetail = {};


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
    } else {
        $("#viewDcb").remove();
    }

    if (decodeURIComponent(getUrlVars()["type"]) == "land") {

        // remove all other Asset Details block from DOM except land asset related fields
        $("#shopAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateTwo,#agreementDetailsBlockTemplateThree").remove();

    } else if (decodeURIComponent(getUrlVars()["type"]) == "shop") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateThree").remove();

    } else if (decodeURIComponent(getUrlVars()["type"]) == "market") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    } else if (decodeURIComponent(getUrlVars()["type"]) == "kalyanamandapam") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    } else if (decodeURIComponent(getUrlVars()["type"]) == "parking_space") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();

    } else if (decodeURIComponent(getUrlVars()["type"]) == "slaughter_house") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();

    } else if (decodeURIComponent(getUrlVars()["type"]) == "usfructs") {


        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    } else if (decodeURIComponent(getUrlVars()["type"]) == "community") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    } else if (decodeURIComponent(getUrlVars()["type"]) == "Fish Tank") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    } else if (decodeURIComponent(getUrlVars()["type"]) == "park") {

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



    // Adding Jquery validation dynamically
    $("#renewAgreementForm").validate({
        rules: final_validatin_rules,
        submitHandler: function(form) {
            // form.submit();
            alert("submitted")
            // console.log(agreement);
            // $.post(`${baseUrl}agreements?tenant_id=kul.am`, {
            //     RequestInfo: requestInfo,
            //     Agreement: agreement
            // }, function(response) {
            //     alert("submit");
            //     console.log(response);
            // })
        }
    })
});
