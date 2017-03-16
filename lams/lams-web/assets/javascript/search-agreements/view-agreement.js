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


$(document).ready(function() {

    $("#viewDcb").on("click", function() {
        //clear cookies and logout
        // $("#login").hide();
        // $("#dashboard").show();
        window.location = "./view-dcb.html"
    });
    //base url for api_id
    var baseUrl = "https://peaceful-headland-36194.herokuapp.com/v1/mSevaAndLA/";
    //request info from cookies
    var requestInfo = {
        "api_id": "string",
        "ver": "string",
        "ts": "2017-01-18T07:18:23.130Z",
        "action": "string",
        "did": "string",
        "key": "string",
        "msg_id": "string",
        "requester_id": "string",
        "auth_token": "aeiou"
    };

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
        renew_rent_increment_method:{
            required:true
        }
    };

    //remove renew part and related buttons from dom
    if (getUrlVars()["view"] == "new") {
        //removing renew section and renew button
        $("#renew,#workFlowDetails").remove();
    } else {
        $("#viewDcb").remove();
    }

    if (getUrlVars()["type"] == "land") {

        // remove all other Asset Details block from DOM except land asset related fields
        $("#shopAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateTwo,#agreementDetailsBlockTemplateThree").remove();

    } else if (getUrlVars()["type"] == "shop") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateThree").remove();

    } else if (getUrlVars()["type"] == "market") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    } else if (getUrlVars()["type"] == "kalyanamandapam") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    } else if (getUrlVars()["type"] == "parking_space") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();

    } else if (getUrlVars()["type"] == "slaughter_house") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();

    } else if (getUrlVars()["type"] == "usfructs") {


        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    } else if (getUrlVars()["type"] == "community") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    } else if (getUrlVars()["type"] == "fish_tank") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    } else if (getUrlVars()["type"] == "park") {

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


    $.post(`http://egov-micro-dev.egovernments.org/agreements_search?agreementNumber=${getUrlVars()["agreementNumber"]}`, {
        request_info: requestInfo
    }, function(response) {
        // console.log(response.Agreements[0]);
        for (var key in response.Agreements[0]) {
            if (typeof(response.Agreements[0][key]) === "object") {
                for (var keyInner in response.Agreements[0][key]) {
                    agreementDetail[keyInner] = response.Agreements[0][key][keyInner];
                    $(`#${keyInner}`).text(response.Agreements[0][key][keyInner]);
                }
            } else {
                agreementDetail[key] = response.Agreements[0][key];
                $(`#${key}`).text(response.Agreements[0][key]);
            }
        }

        for (var key in agreementDetail) {
            if (key.search('date')>0) {
                // console.log(key);
                    var d=new Date(agreementDetail[key]);
                    $(`#${key}`).text(`${d.getDate()}-${d.getMonth()+1}-${d.getFullYear()}`);
            }
        }

        $('#close').on("click",function() {
              window.close();
        })
        // console.log(agreementDetail);
    });


    // var dateFilterIds={
    //
    // }


    // if (Cookies.get('loginDetails')) {
    //   console.log(Cookies.get('loginDetails'));
    //   $("#dashboard").show();
    // } else {
    //   console.log("hi2");
    //   $("#login").show();
    // }
    // $("input").on("keyup", function() {
    //     console.log(this.value);
    //     user[this.id] = this.value;
    // });






});
