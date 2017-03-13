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

$('#close').on("click",function() {
      window.close();
})

$().ready(function() {

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

    // $.validator.addMethod(
    //     "regex",
    //     function(value, element, regexp) {
    //         var re = new RegExp(regexp);
    //         return this.optional(element) || re.test(value);
    //     },
    //     "Please check your input."
    // );


    // $("#contact_no").rules("add", { regex: "^[0-9]{5,10}$" })

    var agreement = {};
    //Getting data for user input
    $("input").on("keyup", function() {
        // console.log(this.value);
        agreement[this.id] = this.value;
    });

    //Getting data for user input
    $("select").on("change", function() {
        // console.log(this.value);
        agreement[this.id] = this.value;
    });

    //file change handle for file upload
    $("input[type=file]").on("change", function(evt) {
        // console.log(this.value);
        // agreement[this.id] = this.value;
        var file = evt.currentTarget.files[0];

        //call post api update and update that url in pur agrement object
    });

    var validation_rules = {};
    var final_validatin_rules = {};
    var commom_fields_rules = {
        name: {
            required: true
        },
        address: {
            required: true
        },
        nature_of_allotment: {
            required: true
        },
        aadhaar_no: {
            required: true,
            aadhar: true
        },
        pan_no: {
            required: true,
            pan_no: true
        },
        emailid: {
            required: true,
            email: true
        },
        contact_no: {
            required: false,
            phone: true
        },
        tin: {
            required: true
        },
        tradelicense_number: {
            required: true
        },
        case_no: {
            required: true
        },
        order_details: {
            required: true
        },
        registration_free: {
            required: true
        },
        council_number: {
            required: true
        },
        bank_guarantee_amount: {
            required: true
        },
        bank_guarantee_date: {
            required: true
        },
        agreement_number: {
            required: true
        },
        agreement_date: {
            required: true
        },
        security_deposit_date: {
            required: true
        },
        security_deposit: {
            required: true
        },
        commencement_date: {
            required: true
        },
        expiry_date: {
            required: true
        },
        rent: {
            required: true
        },
        payment_cycle: {
            required: true
        }
    };
    if (getUrlVars()["type"] == "land") {
        // validation rules for land agreement
        validation_rules = {
            land_register_number: {
                required: true
            },
            particulars_of_land: {
                required: true
            },
            resurvey_number: {
                required: true
            },
            land_address: {
                required: true
            },
            town_survey_no: {
                required: true
            },
            asset_category: {
                required: true
            },
            asset_name: {
                required: true
            },
            asset_code: {
                required: true
            },
            asset_area: {
                required: true
            },
            asset_locality: {
                required: true
            },
            asset_street: {
                required: true
            },
            asset_revenue_zone: {
                required: true
            },
            asset_revenue_ward: {
                required: true
            },
            asset_revenue_block: {
                required: true
            },
            asset_election_ward: {
                required: true
            },
            asset_asset_address: {
                required: true
            }
        }
        // remove all other Asset Details block from DOM except land asset related fields
        $("#shopAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateTwo,#agreementDetailsBlockTemplateThree").remove();
        //disabling input tag of asset details
        $("#landAssetDetailsBlock input").attr("disabled", true);
        //disabling text tag of asset details
        $("#landAssetDetailsBlock textarea").attr("disabled", true);
    } else if (getUrlVars()["type"] == "shop") {
        // validation rules for shop agreement
        validation_rules = {
            shopping_complex_name: {
                required: true
            },
            shopping_complex_no: {
                required: true
            },
            shopping_complex_shop_no: {
                required: true
            },
            shopping_complex_floor_no: {
                required: true
            },
            shop_area: {
                required: true
            },
            shopping_complex_address: {
                required: true
            },
            asset_category: {
                required: true
            },
            asset_name: {
                required: true
            },
            asset_code: {
                required: true
            },
            asset_locality: {
                required: true
            },
            asset_street: {
                required: true
            },
            asset_revenue_zone: {
                required: true
            },
            asset_revenue_ward: {
                required: true
            },
            asset_revenue_block: {
                required: true
            },
            asset_election_ward: {
                required: true
            },
            asset_asset_address: {
                required: true
            }
        }

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateThree").remove();
        //disabling input tag of asset details
        $("#shopAssetDetailsBlock input").attr("disabled", true);
        //disabling textarea tag of asset details
        $("#shopAssetDetailsBlock textarea").attr("disabled", true);
        //disabling select tag of asset details
        $("#shopAssetDetailsBlock select").attr("disabled", true);
    } else if (getUrlVars()["type"] == "market") {
        // validation rules for shop agreement
        validation_rules = {
            asset_category: {
                required: true
            },
            asset_name: {
                required: true
            },
            asset_code: {
                required: true
            },
            asset_area: {
                required: true
            },
            asset_locality: {
                required: true
            },
            asset_street: {
                required: true
            },
            asset_revenue_zone: {
                required: true
            },
            asset_revenue_ward: {
                required: true
            },
            asset_revenue_block: {
                required: true
            },
            asset_election_ward: {
                required: true
            },
            asset_asset_address: {
                required: true
            }
        }

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
        //disabling input tag of asset details
        $("#marketAssetDetailsBlock input").attr("disabled", true);
        $("#marketAssetDetailsBlock textarea").attr("disabled", true);
    } else if (getUrlVars()["type"] == "kalyanamandapam") {
        // validation rules for shop agreement
        validation_rules = {
            kalyanamandapam_name: {
                required: true
            },
            asset_category: {
                required: true
            },
            asset_name: {
                required: true
            },
            asset_code: {
                required: true
            },
            asset_locality: {
                required: true
            },
            asset_street: {
                required: true
            },
            asset_revenue_zone: {
                required: true
            },
            asset_revenue_ward: {
                required: true
            },
            asset_revenue_block: {
                required: true
            },
            asset_election_ward: {
                required: true
            },
            asset_asset_address: {
                required: true
            }
        }

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
        //disabling input tag of asset details
        $("#kalyanamandapamAssetDetailsBlock input").attr("disabled", true);
        $("#kalyanamandapamAssetDetailsBlock textarea").attr("disabled", true);
    } else if (getUrlVars()["type"] == "parking_space") {
        // validation rules for shop agreement
        validation_rules = {
            parking_space_name: {
                required: true
            },
            asset_category: {
                required: true
            },
            asset_name: {
                required: true
            },
            asset_code: {
                required: true
            },
            asset_area: {
                required: true
            },
            asset_locality: {
                required: true
            },
            asset_street: {
                required: true
            },
            asset_revenue_zone: {
                required: true
            },
            asset_revenue_ward: {
                required: true
            },
            asset_revenue_block: {
                required: true
            },
            asset_election_ward: {
                required: true
            },
            asset_asset_address: {
                required: true
            }
        }

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
        //disabling input tag of asset details
        $("#parkingSpaceAssetDetailsBlock input").attr("disabled", true);
        $("#parkingSpaceAssetDetailsBlock textarea").attr("disabled", true);

    } else if (getUrlVars()["type"] == "slaughter_house") {
        // validation rules for shop agreement
        validation_rules = {
            slaughter_house_name: {
                required: true
            },
            asset_category: {
                required: true
            },
            asset_name: {
                required: true
            },
            asset_code: {
                required: true
            },
            asset_locality: {
                required: true
            },
            asset_street: {
                required: true
            },
            asset_revenue_zone: {
                required: true
            },
            asset_revenue_ward: {
                required: true
            },
            asset_revenue_block: {
                required: true
            },
            asset_election_ward: {
                required: true
            },
            asset_asset_address: {
                required: true
            }
        }

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
        //disabling input tag of asset details
        $("#slaughterHousesAssetDetailsBlock input").attr("disabled", true);
        $("#slaughterHousesAssetDetailsBlock textarea").attr("disabled", true);
    } else if (getUrlVars()["type"] == "usfructs") {
        // validation rules for shop agreement
        validation_rules = {
            usfruct_name: {
                required: true
            },
            asset_category: {
                required: true
            },
            asset_name: {
                required: true
            },
            asset_code: {
                required: true
            },
            asset_locality: {
                required: true
            },
            asset_street: {
                required: true
            },
            asset_revenue_zone: {
                required: true
            },
            asset_revenue_ward: {
                required: true
            },
            asset_revenue_block: {
                required: true
            },
            asset_election_ward: {
                required: true
            },
            asset_asset_address: {
                required: true
            }
        }

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
        //disabling input tag of asset details
        $("#usfructsAssetDetailsBlock input").attr("disabled", true);
        $("#usfructsAssetDetailsBlock textarea").attr("disabled", true);
    } else if (getUrlVars()["type"] == "community") {
        // validation rules for shop agreement
        validation_rules = {
            toilet_complex_name: {
                required: true
            },
            asset_category: {
                required: true
            },
            asset_name: {
                required: true
            },
            asset_code: {
                required: true
            },
            asset_locality: {
                required: true
            },
            asset_street: {
                required: true
            },
            asset_revenue_zone: {
                required: true
            },
            asset_revenue_ward: {
                required: true
            },
            asset_revenue_block: {
                required: true
            },
            asset_election_ward: {
                required: true
            },
            asset_asset_address: {
                required: true
            }
        }

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
        //disabling input tag of asset details
        $("#communityAssetDetailsBlock input").attr("disabled", true);
        $("#communityAssetDetailsBlock textarea").attr("disabled", true);
    } else if (getUrlVars()["type"] == "fish_tank") {
        // validation rules for shop agreement
        validation_rules = {
            fish_tank_name: {
                required: true
            },
            asset_category: {
                required: true
            },
            asset_name: {
                required: true
            },
            asset_code: {
                required: true
            },
            asset_locality: {
                required: true
            },
            asset_street: {
                required: true
            },
            asset_revenue_zone: {
                required: true
            },
            asset_revenue_ward: {
                required: true
            },
            asset_revenue_block: {
                required: true
            },
            asset_election_ward: {
                required: true
            },
            asset_asset_address: {
                required: true
            }
        }

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
        //disabling input tag of asset details
        $("#fishTankAssetDetailsBlock input").attr("disabled", true);
        $("#fishTankAssetDetailsBlock textarea").attr("disabled", true);
    } else if (getUrlVars()["type"] == "park") {
        // validation rules for shop agreement
        validation_rules = {
            park_name: {
                required: true
            },
            asset_category: {
                required: true
            },
            asset_name: {
                required: true
            },
            asset_code: {
                required: true
            },
            asset_locality: {
                required: true
            },
            asset_street: {
                required: true
            },
            asset_revenue_zone: {
                required: true
            },
            asset_revenue_ward: {
                required: true
            },
            asset_revenue_block: {
                required: true
            },
            asset_election_ward: {
                required: true
            },
            asset_asset_address: {
                required: true
            }
        }

        // remove all other Asset Details block from DOM except shop asset related fields
        $("#rendCalculatedMethod,#shopAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
        //disabling input tag of asset details
        $("#parkingSpaceAssetDetailsBlock input").attr("disabled", true);
        $("#parkingSpaceAssetDetailsBlock textarea").attr("disabled", true);
    }
    final_validatin_rules = Object.assign(validation_rules, commom_fields_rules);
    for (var key in final_validatin_rules) {
        if (final_validatin_rules[key].required) {
            $(`label[for=${key}]`).append(`<span> *</span>`);
        }
        // $(`#${key}`).attr("disabled",true);
    };

    $.validator.addMethod('phone', function(value) {
        return /^[0-9]{10}$/.test(value);
    }, 'Please enter a valid phone number.');

    $.validator.addMethod('aadhar', function(value) {
        return /^[0-9]{12}$/.test(value);
    }, 'Please enter a valid aadhar.');

    $.validator.addMethod('pan_no', function(value) {
        return /^[0-9a-zA-Z]{10}$/.test(value);
    }, 'Please enter a valid pan.');


    // Adding Jquery validation dynamically
    $("#createAgreementForm").validate({
        rules: final_validatin_rules,
        submitHandler: function(form) {
            // form.submit();

            // console.log(agreement);
            $.post(`${baseUrl}agreements?tenant_id=kul.am`, {
                RequestInfo: requestInfo,
                Agreement: agreement
            }, function(response) {
                // alert("submit");
                window.open("../../../../app/search-assets/create-agreement-ack.html?&agreement_id=aeiou", "", "width=1200,height=800")
                console.log(response);
            })
        }
    })
})
