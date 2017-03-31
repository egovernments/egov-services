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

$('#close').on("click", function() {
    window.close();
})

$().ready(function() {

    //base url for api_id
    var baseUrl = "https://peaceful-headland-36194.herokuapp.com/v1/mSevaAndLA/";
    //request info from cookies
    var requestInfo = {
        "apiId": "string",
        "ver": "string",
        "ts": "2017-01-18T07:18:23.130Z",
        "action": "string",
        "did": "string",
        "key": "string",
        "msgId": "string",
        "requesterId": "string",
        "authToken": "aeiou"
    };

    // $.validator.addMethod(
    //     "regex",
    //     function(value, element, regexp) {
    //         var re = new RegExp(regexp);
    //         return this.optional(element) || re.test(value);
    //     },
    //     "Please check your input."
    // );


    // $("#contactNumber").rules("add", { regex: "^[0-9]{5,10}$" })

    var agreement = {};
    $(".disabled").attr("disabled", true);
    //Getting data for user input
    $("input").on("keyup", function() {
        agreement[this.id] = this.value;
        if(this.id == "rent") {
            $('#securityDeposit').val(this.value*3);
            agreement["securityDeposit"] = this.value*3;
        }
    });

    //Getting data for user input
    $("select").on("change", function() {
        // console.log(this.value);
        if (this.id=="natureOfAllotment") {
            if(this.value=="Direct")
            {
               $(".disabled").attr("disabled", false);

            }
            else {
              $(".disabled").attr("disabled", true);

            }
        }
        agreement[this.id] = this.value;
    });

    //file change handle for file upload
    $("input[type=file]").on("change", function(evt) {
        //evt.currentTarget.files[0];
        agreement["documents"] = evt.currentTarget.files;
    });


    $(".onlyNumber").on("keydown", function(e) {
        var key = e.keyCode ? e.keyCode : e.which;

        if (!([8, 9, 13, 27, 46, 110, 190].indexOf(key) !== -1 ||
                (key == 65 && (e.ctrlKey || e.metaKey)) ||
                (key >= 35 && key <= 40) ||
                (key >= 48 && key <= 57 && !(e.shiftKey || e.altKey)) ||
                (key >= 96 && key <= 105)
            )) {
            e.preventDefault();
        }
        if(this.value.length>11)
        {
          e.preventDefault();
        }
    });

    var validationRules = {};
    var finalValidatinRules = {};
    var commomFieldsRules = {
        name: {
            required: true
        },
        address: {
            required: true
        },
        natureOfAllotment: {
            required: true
        },
        aadhaarNumber: {
            required: false,
            aadhar: true
        },
      panNumber: {
            required: false,
          panNumber: true
        },
        emailid: {
            required: true,
            email: true
        },
        contactNumber: {
            required: true,
            phone: true
        },
        tenderNumber: {
            required: true
        },
        tenderDate: {
            required: true
        },
        tin: {
            required: false
        },
        tradeLicenseNumber: {
            required: false
        },
        caseNumber: {
            required: true
        },
        orderDetails: {
            required: true
        },
        rrReadingNumber: {
            required: getUrlVars()["type"] != "land" ? true : false
        },
        registrationFee: {
            required: true
        },
        councilNumber: {
            required: true
        },
        councilDate: {
            required: true
        },
        bankGuaranteeAmount: {
            required: true
        },
        bankGuaranteeDate: {
            required: true
        },
        agreementNumber: {
            required: true
        },
        agreementDate: {
            required: true
        },
        securityDepositDate: {
            required: true
        },
        securityDeposit: {
            required: true
        },
        commencementDate: {
            required: true
        },
        expiryDate: {
            required: true
        },
        rent: {
            required: true
        },
        paymentCycle: {
            required: true
        },
        rentIncrementMethod: {
            required: (getUrlVars()["type"] == "land" || getUrlVars()["type"] == "shop") ? true : false
        },
        remarks: {
            required: false
        },
        solvencyCertificateNo: {
            required: true
        },
        solvencyCertificateDate: {
            required: true
        }
    };
    if (getUrlVars()["type"] == "land") {
        // validation rules for land agreement
        validationRules = {
            landRegisterNumber: {
                required: true
            },
            particularsOfLand: {
                required: true
            },
            resurveyNumber: {
                required: true
            },
            landAddress: {
                required: true
            },
            townSurveyNumber: {
                required: true
            },
            assetCategory: {
                required: true
            },
            assetName: {
                required: true
            },
            assetCode: {
                required: true
            },
            assetArea: {
                required: true
            },
            assetLocality: {
                required: true
            },
            assetStreet: {
                required: true
            },
            assetRevenueZone: {
                required: true
            },
            assetRevenueWard: {
                required: true
            },
            assetRevenueBlock: {
                required: true
            },
            assetElectionWard: {
                required: true
            },
            assetAssetAddress: {
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

        //append category text
        $(".categoryType").prepend("Land ");
    } else if (getUrlVars()["type"] == "shop") {
        // validation rules for shop agreement
        validationRules = {
            shoppingComplexName: {
                required: true
            },
            shoppingComplexNo: {
                required: true
            },
            shoppingComplexShopNo: {
                required: true
            },
            shoppingComplexFloorNo: {
                required: true
            },
            shopArea: {
                required: true
            },
            shoppingComplexAddress: {
                required: true
            },
            assetCategory: {
                required: true
            },
            assetName: {
                required: true
            },
            assetCode: {
                required: true
            },
            assetLocality: {
                required: true
            },
            assetStreet: {
                required: true
            },
            assetRevenueZone: {
                required: true
            },
            assetRevenueWard: {
                required: true
            },
            assetRevenueBlock: {
                required: true
            },
            assetElectionWard: {
                required: true
            },
            assetAssetAddress: {
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
        //append category text
        $(".categoryType").prepend("Shop ");
    } else if (getUrlVars()["type"] == "market") {
        // validation rules for shop agreement
        validationRules = {
            assetCategory: {
                required: true
            },
            assetName: {
                required: true
            },
            assetCode: {
                required: true
            },
            assetArea: {
                required: true
            },
            assetLocality: {
                required: true
            },
            assetStreet: {
                required: true
            },
            assetRevenueZone: {
                required: true
            },
            assetRevenueWard: {
                required: true
            },
            assetRevenueBlock: {
                required: true
            },
            assetElectionWard: {
                required: true
            },
            assetAssetAddress: {
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
        //append category text
        $(".categoryType").prepend("Market ");
    } else if (getUrlVars()["type"] == "kalyanamandapam") {
        // validation rules for shop agreement
        validationRules = {
            kalyanamandapamName: {
                required: true
            },
            assetCategory: {
                required: true
            },
            assetName: {
                required: true
            },
            assetCode: {
                required: true
            },
            assetLocality: {
                required: true
            },
            assetStreet: {
                required: true
            },
            assetRevenueZone: {
                required: true
            },
            assetRevenueWard: {
                required: true
            },
            assetRevenueBlock: {
                required: true
            },
            assetElectionWard: {
                required: true
            },
            assetAssetAddress: {
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
        //append category text
        $(".categoryType").prepend("Kalyanamandapam ");
    } else if (getUrlVars()["type"] == "parking_space") {
        // validation rules for shop agreement
        validationRules = {
            parkingSpaceName: {
                required: true
            },
            assetCategory: {
                required: true
            },
            assetName: {
                required: true
            },
            assetCode: {
                required: true
            },
            assetArea: {
                required: true
            },
            assetLocality: {
                required: true
            },
            assetStreet: {
                required: true
            },
            assetRevenueZone: {
                required: true
            },
            assetRevenueWard: {
                required: true
            },
            assetRevenueBlock: {
                required: true
            },
            assetElectionWard: {
                required: true
            },
            assetAssetAddress: {
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

        //append category text
        $(".categoryType").prepend("Parking Space ");

    } else if (getUrlVars()["type"] == "slaughter_house") {
        // validation rules for shop agreement
        validationRules = {
            slaughterHouseName: {
                required: true
            },
            assetCategory: {
                required: true
            },
            assetName: {
                required: true
            },
            assetCode: {
                required: true
            },
            assetLocality: {
                required: true
            },
            assetStreet: {
                required: true
            },
            assetRevenueZone: {
                required: true
            },
            assetRevenueWard: {
                required: true
            },
            assetRevenueBlock: {
                required: true
            },
            assetElectionWard: {
                required: true
            },
            assetAssetAddress: {
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
        //append category text
        $(".categoryType").prepend("Slaughter House ");
    } else if (getUrlVars()["type"] == "usfructs") {
        // validation rules for shop agreement
        validationRules = {
            usfructName: {
                required: true
            },
            assetCategory: {
                required: true
            },
            assetName: {
                required: true
            },
            assetCode: {
                required: true
            },
            assetLocality: {
                required: true
            },
            assetStreet: {
                required: true
            },
            assetRevenueZone: {
                required: true
            },
            assetRevenueWard: {
                required: true
            },
            assetRevenueBlock: {
                required: true
            },
            assetElectionWard: {
                required: true
            },
            assetAssetAddress: {
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

        //append category text
        $(".categoryType").prepend("Usfructs ");
    } else if (getUrlVars()["type"] == "community") {
        // validation rules for shop agreement
        validationRules = {
            toiletComplexName: {
                required: true
            },
            assetCategory: {
                required: true
            },
            assetName: {
                required: true
            },
            assetCode: {
                required: true
            },
            assetLocality: {
                required: true
            },
            assetStreet: {
                required: true
            },
            assetRevenueZone: {
                required: true
            },
            assetRevenueWard: {
                required: true
            },
            assetRevenueBlock: {
                required: true
            },
            assetElectionWard: {
                required: true
            },
            assetAssetAddress: {
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
        //append category text
        $(".categoryType").prepend("Community ");
    } else if (getUrlVars()["type"] == "fish_tank") {
        // validation rules for shop agreement
        validationRules = {
            fishTankName: {
                required: true
            },
            assetCategory: {
                required: true
            },
            assetName: {
                required: true
            },
            assetCode: {
                required: true
            },
            assetLocality: {
                required: true
            },
            assetStreet: {
                required: true
            },
            assetRevenueZone: {
                required: true
            },
            assetRevenueWard: {
                required: true
            },
            assetRevenueBlock: {
                required: true
            },
            assetElectionWard: {
                required: true
            },
            assetAssetAddress: {
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
        //append category text
        $(".categoryType").prepend("Fish Tank ");
    } else if (getUrlVars()["type"] == "park") {
        // validation rules for shop agreement
        validationRules = {
            park_name: {
                required: true
            },
            assetCategory: {
                required: true
            },
            assetName: {
                required: true
            },
            assetCode: {
                required: true
            },
            assetLocality: {
                required: true
            },
            assetStreet: {
                required: true
            },
            assetRevenueZone: {
                required: true
            },
            assetRevenueWard: {
                required: true
            },
            assetRevenueBlock: {
                required: true
            },
            assetElectionWard: {
                required: true
            },
            assetAssetAddress: {
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
        //append category text
        $(".categoryType").prepend("Park ");
    }
    // finalValidatinRules = Object.assign(validationRules, commomFieldsRules);
    finalValidatinRules = Object.assign({}, commomFieldsRules);

    for (var key in finalValidatinRules) {
        if (finalValidatinRules[key].required) {
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

    finalValidatinRules["messages"] = {
        name: {
            required: "Enter Name of the Allottee/Lessee"
        },
        address: {
            required: "Enter Address of the Allottee/Lessee"
        },
        natureOfAllotment: {
            required: "Enter Nature of allotment of the agreement"
        },
        aadhaarNumber: {
            required: "Enter Aadhar no. of Allottee"
        },
      panNumber: {
            required: "Enter PAN no. of Allottee"
        },
        emailid: {
            required: "Enter Email ID of Allottee to get Notifications"
        },
        contactNumber: {
            required: "Enter Mobile number of the Allottee to get Notifications"
        },
        tenderNumber: {
            required: "Enter Tender/Auction no. of the agreement"
        },
        tenderDate: {
            required: "Enter Tender/Auction date of the agreement"
        },
        tin: {
            required: "Enter valid TIN number"
        },
        tradeLicenseNumber: {
            required: "Enter respective Trade license number"
        },
        // caseNumber: {
        //     required: true
        // },
        // orderDetails: {
        //     required: true
        // },
        rrReadingNumber: {
            required: getUrlVars()["type"] != "land" ? "Enter Electricity reading number" : ""
        },
        registrationFee: {
            required: "Enter Registration fee paid"
        },
        councilNumber: {
            required: "Enter Council/Standing committee resolution number"
        },
        councilDate: {
            required: "Enter Council/Standing committee resolution date"
        },
        bankGuaranteeAmount: {
            required: "Enter Bank guarantee amount"
        },
        bankGuaranteeDate: {
            required: "Enter Bank guarantee date"
        },
        agreementNumber: {
            required: "Enter Agreement Number"
        },
        agreementDate: {
            required: "Enter Agreement Date"
        },
        securityDepositDate: {
            required: "Enter security deposit received date by ULB"
        },
        securityDeposit: {
            required: "Enter Security deposit for Agreement"
        },
        commencementDate: {
            required: "Enter Date of commencement of asset"
        },
        // expiryDate: {
        //     required: true
        // },
        rent: {
            required: "Enter shop rent per month"
        },
        // paymentCycle: {
        //     required: true
        // },
        rentIncrementMethod: {
            required: getUrlVars()["type"] == "land" || getUrlVars()["type"] == "shop" ? "Select increase in monthly rent at the time of renewal" : ""
        },
        // remarks: {
        //     required: "Enter Remarks if any"
        // },
        solvencyCertificateNo: {
            required: "Enter Solvency certificate date"
        },
        solvencyCertificateDate: {
            required: "Enter Solvency certificate date"
        }


    }


    // Adding Jquery validation dynamically
    $("#createAgreementForm").validate({
        rules: finalValidatinRules,
        messages: finalValidatinRules["messages"],
        submitHandler: function(form) {
            // form.submit();
            uploadFiles(agreement, function(err, agreement) {
                if(err) {
                    //Handle error
                } else {
                    $.post(`${baseUrl}agreements?tenant_id=kul.am`, {
                        RequestInfo: requestInfo,
                        Agreement: agreement
                    }, function(response) {
                        window.open("../../../../app/search-assets/create-agreement-ack.html?&agreement_id=aeiou", "", "width=1200,height=800")
                    })
                }
            })
        }
    })

    function uploadFiles(agreement, cb) {
        if(agreement.documents.constructor == FileList) {
            let counter = agreement.documents.length, breakout = 0;
            for(let i=0; len = agreement.documents.length, i<len; i++) {
                makeAjaxUpload(agreement.documents[i], function(err, res) {
                    if(breakout == 1) 
                        return;
                    else if(err) {
                        cb(err);
                        breakout = 1;
                    } else {
                        counter--;
                        agreement.documents[i] = `/filestore/v1/files/id?fileStoreId=${res.files[0].fileStoreId}`;
                        if(counter == 0 && breakout == 0)
                            uploadFiles(agreement, cb);
                    }
                })
            }
        } else {
            cb(agreement);
        }
    }

    function makeAjaxUpload (file, cb) {
        let formData = new FormData();
        formData.append("jurisdictionId", "ap.public");
        formData.append("module", "PGR");
        formData.append("file", file);
        $.ajax({
            url: baseUrl + "/filestore/v1/files",
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            type: 'POST',
            success: function(res) {
                cb(null, res);
            },
            error: function(jqXHR, exception) {
                cb(jqXHR.responseText || jqXHR.statusText);
            }
        });   
    }
})
