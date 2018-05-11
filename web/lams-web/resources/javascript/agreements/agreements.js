try {
  var department = !localStorage.getItem("assignments_department") || localStorage.getItem("assignments_department") == "undefined" ? (localStorage.setItem("assignments_department", JSON.stringify(getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"] || [])), JSON.parse(localStorage.getItem("assignments_department"))) : JSON.parse(localStorage.getItem("assignments_department"));
} catch (e) {
    console.log(e);
    var department = [];
}

try {
  var natureOfAllotments = !localStorage.getItem("natureOfAllotments") || localStorage.getItem("natureOfAllotments") == "undefined" ? (localStorage.setItem("natureOfAllotments", JSON.stringify(commonApiPost("lams-services", "", "getnatureofallotment", {tenantId}).responseJSON || {})), JSON.parse(localStorage.getItem("natureOfAllotments"))) : JSON.parse(localStorage.getItem("natureOfAllotments"));
} catch (e) {
    console.log(e);
    var natureOfAllotments = {};
}
try {
  var paymentCycle = !localStorage.getItem("paymentCycle") || localStorage.getItem("paymentCycle") == "undefined" ? (localStorage.setItem("paymentCycle", JSON.stringify(commonApiPost("lams-services", "", "getpaymentcycle", {tenantId}).responseJSON || {})), JSON.parse(localStorage.getItem("paymentCycle"))) : JSON.parse(localStorage.getItem("paymentCycle"));
} catch (e) {
    console.log(e);
  var paymentCycle = {};
}

try { locality = !localStorage.getItem("locality") || localStorage.getItem("locality") == "undefined" ? (localStorage.setItem("locality", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "LOCALITY", hierarchyTypeName: "LOCATION", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("locality"))) : JSON.parse(localStorage.getItem("locality")); } catch (e) {
    console.log(e);
    locality = [];
}
try { electionwards = !localStorage.getItem("ward") || localStorage.getItem("ward") == "undefined" ? (localStorage.setItem("ward", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "ADMINISTRATION", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("ward"))) : JSON.parse(localStorage.getItem("ward")); } catch (e) {
    console.log(e);
    electionwards = [];
}
try { street = !localStorage.getItem("street") || localStorage.getItem("street") == "undefined" ? (localStorage.setItem("street", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "STREET", hierarchyTypeName: "LOCATION", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("street"))) : JSON.parse(localStorage.getItem("street")); } catch (e) {
    console.log(e);
    street = [];
}
try { revenueWards = !localStorage.getItem("revenueWard") || localStorage.getItem("revenueWard") == "undefined" ? (localStorage.setItem("revenueWard", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "WARD", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("revenueWard"))) : JSON.parse(localStorage.getItem("revenueWard")); } catch (e) {
    console.log(e);
    revenueWards = [];
}
try { revenueZone = !localStorage.getItem("revenueZone") || localStorage.getItem("revenueZone") == "undefined" ? (localStorage.setItem("revenueZone", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "ZONE", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("revenueZone"))) : JSON.parse(localStorage.getItem("revenueZone")); } catch (e) {
    console.log(e);
    revenueZone = [];
}
try { revenueBlock = !localStorage.getItem("revenueBlock") || localStorage.getItem("revenueBlock") == "undefined" ? (localStorage.setItem("revenueBlock", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "BLOCK", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("revenueBlock"))) : JSON.parse(localStorage.getItem("revenueBlock")); } catch (e) {
    console.log(e);
    revenueBlock = [];
}
try { assetCategories = !localStorage.getItem("assetCategories") || localStorage.getItem("assetCategories") == "undefined" ? (localStorage.setItem("assetCategories", JSON.stringify(commonApiPost("asset-services", "assetCategories", "_search", {tenantId}).responseJSON["AssetCategory"] || [])), JSON.parse(localStorage.getItem("assetCategories"))) : JSON.parse(localStorage.getItem("assetCategories")); } catch (e) {
    console.log(e);
    assetCategories = [];
}
try {
cityGrade = !localStorage.getItem("city_grade") || localStorage.getItem("city_grade") == "undefined" ? (localStorage.setItem("city_grade", JSON.stringify(commonApiPost("tenant", "v1/tenant", "_search", { code: tenantId }).responseJSON["tenant"][0]["city"]["ulbGrade"] || {})), JSON.parse(localStorage.getItem("city_grade"))) : JSON.parse(localStorage.getItem("city_grade"));} catch (e) {
console.log(e);
cityGrade = null;
}

$('#close').on("click", function() {
    window.close();
})

var CONST_API_GET_FILE = "/filestore/v1/files/id?fileStoreId=";
var agreement = {};
var employees = [];
var fileTypes = ["application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/pdf", "image/png", "image/jpeg"];
var shopsMap = {};

$(document).ready(function(){

  $('#municipalOrder, #governmentOrder').hide();
  if (decodeURIComponent(getUrlVars()["type"]) != "Shopping Complex"){
    $("#natureOfAllotment option[value='DIRECT']").remove();

  }

  if(window.opener && window.opener.document) {
     var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
     if(logo_ele && logo_ele[0]) {
       document.getElementsByClassName("homepage_logo")[0].src = logo_ele[0].getAttribute("src");
     }
   }

   $('#timePeriod').append($("<option/>").val('').text('Select Time Period'));
   if (decodeURIComponent(getUrlVars()["type"]) == "Shopping Complex"){
   for(var i=0;i<25;i++){
     $('#timePeriod').append($("<option/>").val(i+1).text(i+1));
   }
 } else{
   $('#timePeriod').append($("<option/>").val(1).text("1"));
 }

   //basis of allotment API call
   var basisOfAllotment = [{label:'Goodwill Auction Basis',value:'Goodwill Auction Basis'},{label:'Normal Basis',value:'Normal Basis'}];
   $.each(basisOfAllotment, function (idx, basis){
     $("#basisOfAllotment").append($("<option />").val(basis.value).text(basis.label));
   });

   //category API call
   var category = commonApiPost("lams-services", "getreservations", "", {tenantId}).responseJSON;
   $.each(category, function (idx, cat){
     $("#reservationCategory").append($("<option />").val(cat.id).text(cat.name));
   });

   $('#basisOfAllotment').on('change',function(){

     // dependent fields show & hide
     if(this.value && $('#timePeriod').val()){
       dependentonBasisTime(this.value, $('#timePeriod').val());
     }

     // update natureOfAllotment dropdown
     $('#natureOfAllotment').find('option').remove().end().append('<option value="">Select</option>');
     fillValueToObject({id:'natureOfAllotment',name:'natureOfAllotment',value:''});
     fillValueToObject({id:'rentIncrementMethod',name:'rentIncrementMethod',value:''});
     if(this.value === 'Goodwill Auction Basis'){
       $('#natureOfAllotment').append(`<option value='AUCTION'>AUCTION</option>`);
     }else if(this.value === 'Normal Basis'){
       Object.keys(natureOfAllotments).map((k, index)=>{
           $('#natureOfAllotment').append('<option value='+k+'>'+natureOfAllotments[k]+'</option>')
       });
     }

     $('#rentIncrementMethod').find('option').remove().end().append('<option value="">Choose Percentage</option>');

     loadRenewalRent(this.value);

   });

   $('#timePeriod').on('change',function(){
     if($('#basisOfAllotment').val() && this.value){
       dependentonBasisTime($('#basisOfAllotment').val(), this.value);
     }
   })

});

$(".disabled").attr("disabled", true);
$("textarea").on("keyup", function() {
    fillValueToObject(this);
});

//Getting data for user input
$("input").on("keyup", function() {
    fillValueToObject(this);
    if (this.id == "rent") {
        $('#securityDeposit').val(this.value * 3);
        $("#securityDeposit").attr({
            "min": this.value * 3
        });
        agreement["securityDeposit"] = this.value * 3;
    }
});

$(document).on("keyup","input", function() {
    //fillValueToObject(this);
    if (this.id === "auctionAmount") {
        if($('#commencementDate').val()!=''){
            var fromDate = $('#commencementDate').val();
            fromDate = moment(fromDate,"DD/MM/YYYY");
          adjustSecurityDeposit(fromDate,this.val);

    }else{
      $('#securityDeposit').val(this.value * 0.25);
      $("#securityDeposit").attr({
          "min": this.value * 0.25
      });
      agreement["securityDeposit"] = Math.round(this.value * 0.25);
    }
  }
});
$(document).on("blur",".datepicker", function() {
    fillValueToObject(this);
});

//Getting data for user input
$("select").on("change", function() {
    // console.log(this.value);
    if (this.id == "natureOfAllotment") {
        if (this.value == "DIRECT") {
            $(".disabled").attr("disabled", false);
        } else {
            $(".disabled").attr("disabled", true);
        }
    } else if(this.id == "floorNumber" && this.value){
      validateAgreementsForFloor(this.value);
    }

    if (($("#approverDepartment").val() != "" && $("#approverDesignation").val() != "") && (this.id == "approverDepartment" || this.id == "approverDesignation")) {
        employees = commonApiPost("hr-employee", "employees", "_search", {
            tenantId,
            departmentId: $("#approverDepartment").val(),
            designationId: $("#approverDesignation").val()
        }).responseJSON["Employee"] || [];
        $(`#approverPositionId`).html(`<option value=''>Select</option>`)

        for (var i = 0; i < employees.length; i++) {
            $(`#approverPositionId`).append(`<option value='${employees[i]['id']}'>${employees[i]['name']}</option>`)
        }
    }

    // agreement[this.id] = this.value;
    fillValueToObject(this);

});

function validateAgreementsForFloor(floorNumber) {
   var noOfAgreements = commonApiPost("lams-services", "agreements", "_search", {tenantId, floorNumber:floorNumber,asset:getUrlVars()["assetId"]}).responseJSON["Agreements"];
   var noOfShops = shopsMap[floorNumber];
 if(noOfAgreements.length === noOfShops){
   showError("Agreements should not exceed number of shops in the floor ");
   $("#createAgreement").attr("disabled",true);
   return false;
 } else{
   $("#createAgreement").attr("disabled",false);
 }
}

//file change handle for file upload
$("input[type=file]").on("change", function(evt) {
    //2097152 = 2mb
    if(evt.currentTarget.files[0].size > 2097152 && fileTypes.indexOf(evt.currentTarget.files[0].type) == -1) {
        $("#documents").val('');
        return showError("Maximum file size allowed is 2 MB.\n Please upload only DOC, PDF, xls, xlsx, png, jpeg file.");
    } else if(evt.currentTarget.files[0].size > 2097152) {
        $("#documents").val('');
        return showError("Maximum file size allowed is 2 MB.");
    } else if(fileTypes.indexOf(evt.currentTarget.files[0].type) == -1) {
        $("#documents").val('');
        return showError("Please upload only DOC, PDF, xls, xlsx, png, jpeg file.");
    }

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
    /*if(this.value.length > 11 && [8, 46, 37, 39].indexOf(key) == -1) {
      e.preventDefault();
    }*/
});

$(".onlyAlphaNum").on("keydown", function(e) {
  var key = e.keyCode ? e.keyCode : e.which;
  if (!([8, 9, 13, 27, 46, 110, 190].indexOf(key) !== -1 ||
          (key == 65 && (e.ctrlKey || e.metaKey)) ||
          (key >= 35 && key <= 40) ||
          (key >= 48 && key <= 57 && !(e.shiftKey || e.altKey)) ||
          (key >= 96 && key <= 105)
      )) {
      e.preventDefault();
  }
});

//it will split object string where it has .
function fillValueToObject(currentState) {
    if (currentState.id.includes(".")) {
        var splitResult = currentState.id.split(".");
        if (agreement.hasOwnProperty(splitResult[0])) {
            agreement[splitResult[0]][splitResult[1]] = currentState.value;
        } else {
            agreement[splitResult[0]] = {};
            agreement[splitResult[0]][splitResult[1]] = currentState.value;
        }



    } else {

        agreement[currentState.id] = currentState.value;

    }

    console.log(agreement);
}


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
        required: false
    },
    "allottee.aadhaarNumber": {
        required: false,
        aadhar: true
    },
    "allottee.pan": {
        required: false,
        panNo: true
    },
    "allottee.name": {
        required: true,
        alloName: true
    },
    "allottee.emailId": {
        required: false,
        email: true
    },
    "allottee.mobileNumber": {
        required: true,
        phone: true
    },
    "allottee.permanentAddress": {
       required: true,

   },
    tenderNumber: {
        required: false,
        alphaNumer: true

    },
    tenderDate: {
        required: false

    },
    tin: {
        required: false

    },
    tradelicenseNumber: {
        required: false,
        alphaNumer: true
    },
    caseNo: {
        required: false
    },
    orderDetails: {
        required: false
    },
    rrReadingNumber: {
        required: decodeURIComponent(getUrlVars()["type"]) != "land" ? true : false
    },
    registrationFee: {
        required: false
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
        required: true,
        integerOnly:true
    },
    commencementDate: {
        required: true
    },
    expiryDate: {
        required: true
    },
    rent: {
        required: true,
        integerOnly:true
    },
    goodWillAmount:{
      integerOnly:false
    },
    paymentCycle: {
        required: true
    },
    approverPositionId: {
        required: true
    },
    // rentIncrementMethod: {
    //     required: (decodeURIComponent(getUrlVars()["type"]).toLowerCase() == "land" || decodeURIComponent(getUrlVars()["type"]).toLowerCase() == "shop") ? true : false
    // },
    remarks: {
        required: false
    },
    solvencyCertificateNo: {
        required: true,
        alphaNumer: true
    },
    solvencyCertificateDate: {
        required: true
    },
    timePeriod: {
        required: true
    },
    basisOfAllotment: {
        required: true
    },
    "rentIncrementMethod": {
        required: true
    },
    reservationCategory: {
        required: true
    },
    gstin: {
        required: false,
        alphaNumer: true
    },
    municipalOrderNumber:{
      required :false,
      alphaNumersh: true
    },
    governmentOrderNumber :{
      required :false,
      alphaNumersh: true
    },
    referenceNumber:{
      required: decodeURIComponent(getUrlVars()["type"]).toLowerCase() == "shopping complex"  ? true : false,
      alphaNumersh: true
    },
    floorNumber :{
      required :true
    },
    auctionAmount:{
      required :true
    }
};
if (decodeURIComponent(getUrlVars()["type"]) == "Land") {
    // validation rules for land agreement
    validationRules = {
            // landRegisterNumber: {
            //     required: true
            // },
            // particularsOfLand: {
            //     required: true
            // },
            // resurveyNumber: {
            //     required: true
            // },
            // landAddress: {
            //     required: true
            // },
            // townSurveyNumber: {
            //     required: true
            // }
            // ,
            // assetCategory: {
            //     required: true
            // },
            // assetName: {
            //     required: true
            // },
            // assetCode: {
            //     required: true
            // },
            // assetArea: {
            //     required: true
            // },
            // assetLocality: {
            //     required: true
            // },
            // assetStreet: {
            //     required: true
            // },
            // assetRevenueZone: {
            //     required: true
            // },
            // assetrevenueWards: {
            //     required: true
            // },
            // assetRevenueBlock: {
            //     required: true
            // },
            // assetElectionWard: {
            //     required: true
            // },
            // assetAssetAddress: {
            //     required: true
            // }
        }
        // remove all other Asset Details block from DOM except land asset related fields
    $("#shopAssetDetailsBlock,#shoppingComplexAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
    //remove agreement template two and three from screen
    $("#agreementDetailsBlockTemplateTwo,#agreementDetailsBlockTemplateThree,#agreementDetailsBlockForAuctionAssets").remove();
    $("#allotteeDetailsBlock").remove();
    //disabling input tag of asset details
    $("#landAssetDetailsBlock input").attr("disabled", true);
    //disabling text tag of asset details
    $("#landAssetDetailsBlock textarea").attr("disabled", true);

    //append category text
    $(".categoryType").prepend("Land ");
} else if (decodeURIComponent(getUrlVars()["type"]) == "Shop") {
    // validation rules for shop agreement
    validationRules = {
        // shoppingComplexName: {
        //     required: true
        // },
        // shoppingComplexNo: {
        //     required: true
        // },
        // shoppingComplexShopNo: {
        //     required: true
        // },
        // shoppingComplexFloorNo: {
        //     required: true
        // },
        // shopArea: {
        //     required: true
        // },
        // shoppingComplexAddress: {
        //     required: true
        // }
        // ,
        // assetCategory: {
        //     required: true
        // },
        // assetName: {
        //     required: true
        // },
        // assetCode: {
        //     required: true
        // },
        // assetLocality: {
        //     required: true
        // },
        // assetStreet: {
        //     required: true
        // },
        // assetRevenueZone: {
        //     required: true
        // },
        // assetrevenueWards: {
        //     required: true
        // },
        // assetRevenueBlock: {
        //     required: true
        // },
        // assetElectionWard: {
        //     required: true
        // },
        // assetAssetAddress: {
        //     required: true
        // }
    }

    // remove all other Asset Details block from DOM except shop asset related fields
    $("#shoppingComplexAssetDetailsBlock,#landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
    //remove agreement template two and three from screen
    $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateThree,#agreementDetailsBlockForAuctionAssets").remove();
    //remove allotee details block(old details)
     $("#allotteeDetailsBlockTwo").remove();
    //disabling input tag of asset details
    $("#shopAssetDetailsBlock input").attr("disabled", true);
    //disabling textarea tag of asset details
    $("#shopAssetDetailsBlock textarea").attr("disabled", true);
    //disabling select tag of asset details
    $("#shopAssetDetailsBlock select").attr("disabled", true);
    //append category text
    $(".categoryType").prepend("Shop ");
} else if (decodeURIComponent(getUrlVars()["type"]) == "Shopping Complex") {
    // validation rules for shop agreement
    // remove all other Asset Details block from DOM except shop asset related fields
    $("#shopAssetDetailsBlock,#landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
    //remove agreement template two and three from screen
    $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateThree,#agreementDetailsBlockForAuctionAssets").remove();
    //remove allotee details block(old details)
     $("#allotteeDetailsBlockTwo").remove();
    //disabling input tag of asset details
    $("#shoppingComplexAssetDetailsBlock input").attr("disabled", true);
    //disabling textarea tag of asset details
    $("#shoppingComplexAssetDetailsBlock textarea").attr("disabled", true);
    //disabling select tag of asset details
    $("#shoppingComplexAssetDetailsBlock select").attr("disabled", true);
    $("#referenceNumber").attr("disabled",false);
    $("#floorNumber").attr("disabled",false);
    $("#referenceNumberSection").remove();
    //append category text
    $(".categoryType").prepend("Shopping Complex ");
}
else if (decodeURIComponent(getUrlVars()["type"]) == "Market") {
    // validation rules for shop agreement
    validationRules = {
        // assetCategory: {
        //     required: true
        // },
        // assetName: {
        //     required: true
        // },
        // assetCode: {
        //     required: true
        // },
        // assetArea: {
        //     required: true
        // },
        // assetLocality: {
        //     required: true
        // },
        // assetStreet: {
        //     required: true
        // },
        // assetRevenueZone: {
        //     required: true
        // },
        // assetrevenueWards: {
        //     required: true
        // },
        // assetRevenueBlock: {
        //     required: true
        // },
        // assetElectionWard: {
        //     required: true
        // },
        // assetAssetAddress: {
        //     required: true
        // }
    }

    // remove all other Asset Details block from DOM except shop asset related fields
    $("#rendCalculatedMethod,#shopAssetDetailsBlock,#shoppingComplexAssetDetailsBlock, #landAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
    //remove agreement template two and three from screen
    $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo,#agreementDetailsBlockTemplateThree").remove();
    $("#allotteeDetailsBlock").remove();
    //disabling input tag of asset details
    $("#marketAssetDetailsBlock input").attr("disabled", true);
    $("#marketAssetDetailsBlock textarea").attr("disabled", true);
    //append category text
    $(".categoryType").prepend("Market ");
} else if (decodeURIComponent(getUrlVars()["type"]) == "Community Hall") {

    // remove all other Asset Details block from DOM except shop asset related fields
    $("#rendCalculatedMethod,#shopAssetDetailsBlock,#shoppingComplexAssetDetailsBlock, #landAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
    //remove agreement template two and three from screen
    $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo,#agreementDetailsBlockTemplateThree").remove();
    //removing allotee details block
     $("#allotteeDetailsBlock").remove();
    //disabling input tag of asset details
    $("#historyDetails,#subesquentrenewals").remove();
    $("#marketAssetDetailsBlock input").attr("disabled", true);
    $("#marketAssetDetailsBlock textarea").attr("disabled", true);
    //append category text
    $('#assetDetails-title').text("Community Hall Asset Details");
    $(".categoryType").prepend("Community Hall ");
} else if (decodeURIComponent(getUrlVars()["type"]) == "Kalyana Mandapam") {
    // validation rules for shop agreement
    validationRules = {
        // kalyanamandapamName: {
        //     required: true
        // },
        // assetCategory: {
        //     required: true
        // },
        // assetName: {
        //     required: true
        // },
        // assetCode: {
        //     required: true
        // },
        // assetLocality: {
        //     required: true
        // },
        // assetStreet: {
        //     required: true
        // },
        // assetRevenueZone: {
        //     required: true
        // },
        // assetrevenueWards: {
        //     required: true
        // },
        // assetRevenueBlock: {
        //     required: true
        // },
        // assetElectionWard: {
        //     required: true
        // },
        // assetAssetAddress: {
        //     required: true
        // }
    }

    // remove all other Asset Details block from DOM except shop asset related fields
    $("#rendCalculatedMethod,#shopAssetDetailsBlock,#shoppingComplexAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
    //remove agreement template two and three from screen
    $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo,#agreementDetailsBlockTemplateThree").remove();
    //remove allotee details block(old details)
     $("#allotteeDetailsBlock").remove();
    //disabling input tag of asset details
    $("#kalyanamandapamAssetDetailsBlock input").attr("disabled", true);
    $("#kalyanamandapamAssetDetailsBlock textarea").attr("disabled", true);
    //append category text
    $(".categoryType").prepend("Kalyanamandapam ");
} else if (decodeURIComponent(getUrlVars()["type"]) == "Parking Space") {
    // validation rules for shop agreement
    validationRules = {
        // parkingSpaceName: {
        //     required: true
        // },
        // assetCategory: {
        //     required: true
        // },
        // assetName: {
        //     required: true
        // },
        // assetCode: {
        //     required: true
        // },
        // assetArea: {
        //     required: true
        // },
        // assetLocality: {
        //     required: true
        // },
        // assetStreet: {
        //     required: true
        // },
        // assetRevenueZone: {
        //     required: true
        // },
        // assetrevenueWards: {
        //     required: true
        // },
        // assetRevenueBlock: {
        //     required: true
        // },
        // assetElectionWard: {
        //     required: true
        // },
        // assetAssetAddress: {
        //     required: true
        // }
    }

    // remove all other Asset Details block from DOM except shop asset related fields
    $("#rendCalculatedMethod,#shopAssetDetailsBlock,#shoppingComplexAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
    //remove agreement template two and three from screen
    $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo,#agreementDetailsBlockForAuctionAssets").remove();
    //remove allotee details block(old details)
     $("#allotteeDetailsBlockTwo").remove();
    //disabling input tag of asset details
    $("#parkingSpaceAssetDetailsBlock input").attr("disabled", true);
    $("#parkingSpaceAssetDetailsBlock textarea").attr("disabled", true);

    //append category text
    $(".categoryType").prepend("Parking Space ");

} else if (decodeURIComponent(getUrlVars()["type"]) == "Slaughter House") {
    // validation rules for shop agreement
    validationRules = {
        // slaughterHouseName: {
        //     required: true
        // },
        // assetCategory: {
        //     required: true
        // },
        // assetName: {
        //     required: true
        // },
        // assetCode: {
        //     required: true
        // },
        // assetLocality: {
        //     required: true
        // },
        // assetStreet: {
        //     required: true
        // },
        // assetRevenueZone: {
        //     required: true
        // },
        // assetrevenueWards: {
        //     required: true
        // },
        // assetRevenueBlock: {
        //     required: true
        // },
        // assetElectionWard: {
        //     required: true
        // },
        // assetAssetAddress: {
        //     required: true
        // }
    }

    // remove all other Asset Details block from DOM except shop asset related fields
    $("#rendCalculatedMethod,#shopAssetDetailsBlock, #shoppingComplexAssetDetailsBlock,#landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
    //remove agreement template two and three from screen
    $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo,#agreementDetailsBlockTemplateThree").remove();
    $("#allotteeDetailsBlock").remove();
    //disabling input tag of asset details
    $("#slaughterHousesAssetDetailsBlock input").attr("disabled", true);
    $("#slaughterHousesAssetDetailsBlock textarea").attr("disabled", true);
    //append category text
    $(".categoryType").prepend("Slaughter House ");
} else if (decodeURIComponent(getUrlVars()["type"]) == "Usufruct") {
    // validation rules for shop agreement
    validationRules = {
        // usfructName: {
        //     required: true
        // },
        // assetCategory: {
        //     required: true
        // },
        // assetName: {
        //     required: true
        // },
        // assetCode: {
        //     required: true
        // },
        // assetLocality: {
        //     required: true
        // },
        // assetStreet: {
        //     required: true
        // },
        // assetRevenueZone: {
        //     required: true
        // },
        // assetrevenueWards: {
        //     required: true
        // },
        // assetRevenueBlock: {
        //     required: true
        // },
        // assetElectionWard: {
        //     required: true
        // },
        // assetAssetAddress: {
        //     required: true
        // }
    }

    // remove all other Asset Details block from DOM except shop asset related fields
    $("#rendCalculatedMethod,#shopAssetDetailsBlock,#shoppingComplexAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
    //remove agreement template two and three from screen
    $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo").remove();
    //remove allotee details block(old details)
     $("#allotteeDetailsBlockTwo").remove();
    //disabling input tag of asset details
    $("#usfructsAssetDetailsBlock input").attr("disabled", true);
    $("#usfructsAssetDetailsBlock textarea").attr("disabled", true);

    //append category text
    $(".categoryType").prepend("Usfructs ");
} else if (decodeURIComponent(getUrlVars()["type"]) == "Community Toilet Complex") {
    // validation rules for shop agreement
    validationRules = {
        // toiletComplexName: {
        //     required: true
        // },
        // assetCategory: {
        //     required: true
        // },
        // assetName: {
        //     required: true
        // },
        // assetCode: {
        //     required: true
        // },
        // assetLocality: {
        //     required: true
        // },
        // assetStreet: {
        //     required: true
        // },
        // assetRevenueZone: {
        //     required: true
        // },
        // assetrevenueWards: {
        //     required: true
        // },
        // assetRevenueBlock: {
        //     required: true
        // },
        // assetElectionWard: {
        //     required: true
        // },
        // assetAssetAddress: {
        //     required: true
        // }
    }

    // remove all other Asset Details block from DOM except shop asset related fields
    $("#rendCalculatedMethod,#shopAssetDetailsBlock,#shoppingComplexAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
    //remove agreement template two and three from screen
    $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo,#agreementDetailsBlockTemplateThree").remove();
    $("#allotteeDetailsBlock").remove();
    //disabling input tag of asset details
    $("#communityAssetDetailsBlock input").attr("disabled", true);
    $("#communityAssetDetailsBlock textarea").attr("disabled", true);
    //append category text
    $(".categoryType").prepend("Community Toilet ");
} else if (decodeURIComponent(getUrlVars()["type"]) == "Fish Tanks") {
    // validation rules for shop agreement
    validationRules = {
        // fishTankName: {
        //     required: true
        // },
        // assetCategory: {
        //     required: true
        // },
        // assetName: {
        //     required: true
        // },
        // assetCode: {
        //     required: true
        // },
        // assetLocality: {
        //     required: true
        // },
        // assetStreet: {
        //     required: true
        // },
        // assetRevenueZone: {
        //     required: true
        // },
        // assetrevenueWards: {
        //     required: true
        // },
        // assetRevenueBlock: {
        //     required: true
        // },
        // assetElectionWard: {
        //     required: true
        // },
        // assetAssetAddress: {
        //     required: true
        // }
    }

    // remove all other Asset Details block from DOM except shop asset related fields
    $("#rendCalculatedMethod,#shopAssetDetailsBlock,#shoppingComplexAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #parkAssetDetailsBlock").remove();
    //remove agreement template two and three from screen
    $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo,#agreementDetailsBlockTemplateThree").remove();
    $("#allotteeDetailsBlock").remove();
    //disabling input tag of asset details
    $("#fishTankAssetDetailsBlock input").attr("disabled", true);
    $("#fishTankAssetDetailsBlock textarea").attr("disabled", true);
    //append category text
    $(".categoryType").prepend("Fish Tank ");
} else if (decodeURIComponent(getUrlVars()["type"]) == "Parks") {
    // validation rules for shop agreement
    validationRules = {
        // park_name: {
        //     required: true
        // },
        // assetCategory: {
        //     required: true
        // },
        // assetName: {
        //     required: true
        // },
        // assetCode: {
        //     required: true
        // },
        // assetLocality: {
        //     required: true
        // },
        // assetStreet: {
        //     required: true
        // },
        // assetRevenueZone: {
        //     required: true
        // },
        // assetrevenueWards: {
        //     required: true
        // },
        // assetRevenueBlock: {
        //     required: true
        // },
        // assetElectionWard: {
        //     required: true
        // },
        // assetAssetAddress: {
        //     required: true
        // }
    }

    // remove all other Asset Details block from DOM except shop asset related fields
    $("#rendCalculatedMethod,#shopAssetDetailsBlock,#shoppingComplexAssetDetailsBlock, #landAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock").remove();
    //remove agreement template two and three from screen
    $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo,#agreementDetailsBlockForAuctionAssets").remove();
    //remove allotee details block(old details)
     $("#allotteeDetailsBlockTwo").remove();
    //disabling input tag of asset details
    $("#parkAssetDetailsBlock input").attr("disabled", true);
    $("#parkAssetDetailsBlock textarea").attr("disabled", true);
    //append category text
    $(".categoryType").prepend("Park ");
} else {
    // remove all other Asset Details block from DOM except land asset related fields
    $("#landAssetDetailsBlock,#shopAssetDetailsBlock,#shoppingComplexAssetDetailsBlock, #marketAssetDetailsBlock, #kalyanamandapamAssetDetailsBlock, #parkingSpaceAssetDetailsBlock, #slaughterHousesAssetDetailsBlock, #usfructsAssetDetailsBlock, #communityAssetDetailsBlock, #fishTankAssetDetailsBlock, #parkAssetDetailsBlock").remove();
    //remove agreement template two and three from screen
    $("#agreementDetailsBlockTemplateOne,#agreementDetailsBlockTemplateTwo,#agreementDetailsBlockTemplateThree").remove();
    alert("Agreement is not applicable for selected category");
    window.open(location, '_self').close();
}

// finalValidatinRules = Object.assign(validationRules, commomFieldsRules);
finalValidatinRules = Object.assign({}, commomFieldsRules);

for (var key in finalValidatinRules) {
    if (finalValidatinRules[key].required) {
        if (key.split(".").length == "2") {
            $(`label[for=${key.split(".")[0]}\\.${key.split(".")[1]}]`).append(`<span> *</span>`);
        } else {
            $(`label[for=${key}]`).append(`<span> *</span>`);
        }
    }
    // $(`#${key}`).attr("disabled",true);
};



$.validator.addMethod('phone', function(value) {
    return /^[0-9]{10}$/.test(value);
}, 'Please enter a valid phone number.');

$.validator.addMethod('aadhar', function(value) {
    return value ? /^[0-9]{12}$/.test(value) : true;
}, 'Please enter a valid aadhar.');

$.validator.addMethod('panNo', function(value) {
  return value.length > 0 ? /^([a-zA-Z]){5}([0-9]){4}([a-zA-Z]){1}?$/i.test(value) && value.length === 10 : true;
}, 'Please enter a valid pan.');

$.validator.addMethod('alloName', function(value) {
    return /^[a-zA-Z ]*$/.test(value);
}, 'Please enter a valid name.');

$.validator.addMethod('alphaNumer', function(value) {
  return value ? /^[a-z0-9]+$/i.test(value) : true;
}, 'Please enter only Alpha/Numeric Value');

$.validator.addMethod('alphaNumersh', function(value) {
  return value ? /^[a-z0-9./-]+$/i.test(value) : true;
}, 'Alhpabets, Numbers and / - are only allowed');

$.validator.addMethod('integerOnly',function(value){
  return /^[0-9]*$/.test(value);
},'please check the value/enter integer numbers only.');


finalValidatinRules["messages"] = {
    "allottee.name": {
        required: "Enter Name of the Allottee/Lessee"
    },
    "allottee.permanentAddress": {
        required: "Enter Address of the Allottee/Lessee"
    },
    natureOfAllotment: {
        required: "Enter Nature of allotment of the agreement"
    },
    "allottee.aadhaarNumber": {
        required: "Enter Aadhar no. of Allottee"
    },
    "allottee.pan": {
        required: "Enter PAN no. of Allottee"
    },
    "allottee.emailId": {
        required: "Enter Email ID of Allottee to get Notifications"
    },
    "allottee.mobileNumber": {
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
    tradelicenseNumber: {
        required: "Enter respective Trade license number"
    },
    // caseNumber: {
    //     required: true
    // },
    // orderDetails: {
    //     required: true
    // },
    rrReadingNumber: {
        required: decodeURIComponent(getUrlVars()["type"]) != "land" ? "Enter Electricity reading number" : ""
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
        required: decodeURIComponent(getUrlVars()["type"]) == "land" || decodeURIComponent(getUrlVars()["type"]) == "shop" ? "Select increase in monthly rent at the time of renewal" : ""
    },
    // remarks: {
    //     required: "Enter Remarks if any"
    // },
    solvencyCertificateNo: {
        required: "Enter Solvency certificate number"
    },
    solvencyCertificateDate: {
        required: "Enter Solvency certificate date"
    },
    approverPositionId: {
        required: "Select approver name"
    }
}

// $("#"+name).val("murali");

var assetDetails = commonApiPost("asset-services", "assets", "_search", { id: getUrlVars()["assetId"], tenantId }).responseJSON["Assets"][0] || {};
// var otments=commonApiPost("lams-services","","getnatureofallotment",{}).responseJSON ||{};
// var designation= getCommonMaster("hr-masters", "designations", "Designation").responseJSON["Designation"] || [];
// var department= getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"] || [];

for (var variable in natureOfAllotments) {
    if (natureOfAllotments.hasOwnProperty(variable)) {
        $(`#natureOfAllotment`).append(`<option value='${variable}'>${natureOfAllotments[variable]}</option>`)

    }
}

for (var variable in paymentCycle) {
    if (paymentCycle.hasOwnProperty(variable)) {
        $(`#paymentCycle`).append(`<option value='${variable}'>${paymentCycle[variable]}</option>`)

    }
}

$(`#approverDepartment`).html(`<option value=''>Select</option>`);
for (var variable in department) {
    $(`#approverDepartment`).append(`<option value='${department[variable]["id"]}'>${department[variable]["name"]}</option>`)

}
$(`#approverDesignation`).html(`<option value=''>Select</option>`)

/*for (var variable in designation) {


    $(`#approverDesignation`).append(`<option value='${designation[variable]["id"]}'>${designation[variable]["name"]}</option>`)


}*/

getDesignations(null,null,function(designations) {
    for (let variable in designations) {
        if (!designations[variable]["id"]) {
            var _res = commonApiPost("hr-masters", "designations", "_search", { tenantId, name: designations[variable]["name"] });
            designations[variable]["id"] = _res && _res.responseJSON && _res.responseJSON["Designation"] && _res.responseJSON["Designation"][0] ? _res.responseJSON["Designation"][0].id : "";
        }

        $(`#approverDesignation`).append(`<option value='${designations[variable]["id"]}'>${designations[variable]["name"]}</option>`);
    }
},"New LeaseAgreement");

// var locality=commonApiPost("v1/location/boundarys","boundariesByBndryTypeNameAndHierarchyTypeName","",{boundaryTypeName:"LOCALITY",hierarchyTypeName:"LOCATION"}).responseJSON["Boundary"] || [],
// var electionwards=commonApiPost("v1/location/boundarys","boundariesByBndryTypeNameAndHierarchyTypeName","",{boundaryTypeName:"WARD",hierarchyTypeName:"ADMINISTRATION"}).responseJSON["Boundary"] || [],
// var revenueWardss=commonApiPost("v1/location/boundarys","boundariesByBndryTypeNameAndHierarchyTypeName","",{boundaryTypeName:"WARD",hierarchyTypeName:"REVENUE"}).responseJSON["Boundary"] || []
if (assetDetails && Object.keys(assetDetails).length) {
    $("#assetCategory\\.name").val(assetDetails["assetCategory"]["name"]);

    $("#aName").val(assetDetails["name"]);

    $("#totalArea").val(assetDetails["totalArea"]);

    $("#code").val(assetDetails["code"]);

    $("#shoppingComplexName").val(assetDetails["name"]);

    $("#locationDetails\\.locality").val(getNameById(locality, assetDetails["locationDetails"]["locality"]));

    $("#locationDetails\\.street").val(getNameById(street, assetDetails["locationDetails"]["street"]));

    $("#locationDetails\\.zone").val(getNameById(revenueZone, assetDetails["locationDetails"]["zone"]));

    $("#locationDetails\\.revenueWard").val(getNameById(revenueWards, assetDetails["locationDetails"]["revenueWard"]));

    $("#locationDetails\\.block").val(getNameById(revenueBlock, assetDetails["locationDetails"]["block"]));

    $("#locationDetails\\.electionWard").val(getNameById(electionwards, assetDetails["locationDetails"]["electionWard"]));

    if(assetDetails.assetAttributes) {
        var attrs = assetDetails.assetAttributes;
        for(var i=0, len = attrs.length; i<len; i++) {
            switch (attrs[i].key) {
                case 'Land Register Number':
                    $("#landRegisterNumber").val(attrs[i].value);
                    break;
                case 'Particulars of Land':
                    $("#particularsOfLand").val(attrs[i].value);
                    break;
                case 'Re-survey Number':
                    $("#resurveyNumber").val(attrs[i].value);
                    break;
                case 'Land Address':
                    $("#landAddress").val(attrs[i].value);
                    break;
                case 'Land Survey Number':
                    $("#townSurveyNo").val(attrs[i].value);
                    break;
                case 'Usage Reference Number':
                    $("#usageReferenceNumber").val(attrs[i].value);
                    break;
                case 'Shopping Complex Name':
                    $("#shoppingComplexName").val(attrs[i].value);
                    break;
                case 'Shopping Complex No.':
                    $("#shoppingComplexNo").val(attrs[i].value);
                    break;
                case 'Total No. of Shops':
                            $("#shoppingComplexNoShops").val(attrs[i].value);
                            break;
               case 'No. of Floors':
                            $("#shoppingComplexNoFloors").val(attrs[i].value);
                            break;
                case 'Shop No':
                    $("#shoppingComplexShopNo").val(attrs[i].value);
                    break;
                case 'Floor No.':
                    $("#shoppingComplexFloorNo").val(attrs[i].value);
                    break;
                case 'Shop Area':
                    $("#shopArea").val(attrs[i].value);
                    break;
                case 'Shopping Complex Address':
                    $("#shoppingComplexAddress").val(attrs[i].value);
                    break;
                    case 'Floor Details':
                          var floorDetails = attrs[i].value;
                        for (let i in floorDetails){
                         $(`#floorNumber`).append(`<option value='${floorDetails[i]["Floor No."]}'>${floorDetails[i]["Floor No."]}</option>`);
                           shopsMap[floorDetails[i]["Floor No."]]=parseInt(floorDetails[i]["No. of Shops"]);
                           }
                           break;

            }
        }
    }
}

$('#commencementDate').datepicker({
                    format: 'dd/mm/yyyy',
                    autoclose:true

                });

$('.datepicker').datepicker({
      format: 'dd/mm/yyyy',
      endDate: new Date(),
      autoclose:true

});

$(".datepicker").on("changeDate", function() {
    if(this.id ==='commencementDate'){
      var auctionAmt = $('#auctionAmount').val();
        if(auctionAmt){
         var fromDate = moment(this.value,"DD/MM/YYYY");
         adjustSecurityDeposit(fromDate,auctionAmt);
    }
  }
    fillValueToObject(this);

});

function adjustSecurityDeposit(fromDate,auctionAmount){
  var year = Number(fromDate.format('YYYY'));
  var month = Number(fromDate.format('MM'));
  if(month > 3){
     year = year + 1;
  }
  var fisYear = new Date(year,2,31); //month number starts from 0(Jan) to 11(Dec)
  fisYear = moment(fisYear).format("DD/MM/YYYY");
  //var month = fromDate.getMonth();
  let totalMonths = getAbsoulteMonths(moment(fisYear,"DD/MM/YYYY"))-getAbsoulteMonths(moment(fromDate),"DD/MM/YYYY");
  totalMonths = totalMonths + 1;
  var auctionAmt = $('#auctionAmount').val();
  var monthlyRent = auctionAmt/totalMonths ;
  var securityDeposit = monthlyRent;
  if(totalMonths >3){
    securityDeposit = Math.round(monthlyRent * 3);
  }else {
    securityDeposit = Math.round(monthlyRent);
  }

  $('#securityDeposit').val(securityDeposit);
  var paymentCycle = $('#paymentCycle').val();
  if(paymentCycle ==='ANNUAL'){
    agreement["rent"] = auctionAmt;
  }else if(paymentCycle ==='QUARTER'){
    agreement["rent"] = Math.round(auctionAmt/4);
  }else if(paymentCycle ==='HALFYEAR'){
    agreement["rent"] = Math.round(auctionAmt/2);
 }else{
    agreement["rent"] = Math.round(monthlyRent);
 }
    agreement["securityDeposit"] = Math.round(securityDeposit);

}

// printValue("",assetDetails)
//
//
// function printValue(object="",values) {
//   if (object != "") {
//
//   }
//   else {
//     for (var key in values)
//     {
//             if (typeof(values[key])=="object") {
//                 for (var variable in values[key]) {
//                     $("#"+key+"\\."+variable).val(values[key][variable]);
//                 }
//             }
//             else {
//                   if(key=="name")
//                   {
//                     $("#aName").val(values[key]);
//
//                   }
//                   else {
//                     $("#"+key).val(values[key]);
//
//                   }
//             }
//
//     }
//     // for (var key in values) {
//     //     if (key.search('date')>0) {
//     //         // console.log(key);
//     //             var d=new Date(values[key]);
//     //             $(`#${key}`).val(`${d.getDate()}-${d.getMonth()+1}-${d.getFullYear()}`);
//     //     }
//     // }
//
//   }
// }

$("#createAgreement").on("click", function(e) {
    e.preventDefault();
    $("#createAgreementForm").submit();
    // switchValidation("final_validatin_rules");
})


function getPositionId(id) {
    var tempEmploye = {};
    for (var i = 0; i < employees.length; i++) {
        if (employees[i].id == id) {
            tempEmploye = employees[i];
        }
    }

    if(tempEmploye && tempEmploye.assignments) {
        return tempEmploye.assignments[0].position;
    } else {
        return "";
    }
}

// Adding Jquery validation dynamically
$("#createAgreementForm").validate({
    rules: finalValidatinRules,
    messages: finalValidatinRules["messages"],
    submitHandler: function(form) {
         var fromDate = $('#commencementDate').val();
         fromDate = moment(fromDate,"DD/MM/YYYY");
         var auctionAmt = $('#auctionAmount').val();
         if(auctionAmt){
          adjustSecurityDeposit(fromDate,auctionAmount);
        }
        agreement["workflowDetails"] = {};
        agreement["workflowDetails"]["assignee"] = getPositionId(agreement["approverPositionId"]);
        if(agreement.remarks) {
            agreement["workflowDetails"]["comments"] = agreement.remarks;
        }
        agreement["asset"] = {};
        agreement["asset"]["id"] = getUrlVars()["assetId"];
        agreement["asset"]["name"] = assetDetails["name"];
        agreement["asset"]["code"] = assetDetails["code"];
        agreement["asset"]["assetCategory"] = {};
        agreement["asset"]["assetCategory"]["id"] = assetDetails["assetCategory"]["id"];
        agreement["asset"]["assetCategory"]["code"] = assetDetails["assetCategory"]["code"];
        agreement["asset"]["assetCategory"]["name"] = assetDetails["assetCategory"]["name"];
        if($("#rentIncrementMethod").val()) {
            agreement["rentIncrementMethod"] = {};
            agreement["rentIncrementMethod"]["id"] = $("#rentIncrementMethod").val();
        }

        agreement["tenantId"] = tenantId;
        agreement["source"] = "SYSTEM";
        agreement["action"] = "CREATE";//Different in case of cancel/evict. Please remove
        agreement["isHistory"] = false;
        uploadFiles(agreement, function(err, _agreement) {
            if (err) {
                //Handle error
            } else {
                // $.post(`${baseUrl}agreements?tenant_id=kul.am`, {
                //     RequestInfo: requestInfo,
                //     Agreement: agreement
                // }, function(response) {
                //     // alert("submit");
                //     // window.open("../../../../app/search-assets/create-agreement-ack.html?&agreement_id=aeiou", "", "width=1200,height=800")
                //     // console.log(response);
                // })

                var response = $.ajax({
                    url: baseUrl + "/lams-services/agreements/_create?tenantId=" + tenantId,
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify({
                        RequestInfo: requestInfo,
                        Agreement: _agreement
                    }),
                    async: false,
                    headers: {
                        'auth-token': authToken
                    },
                    contentType: 'application/json'
                });

                if (response["status"] === 201) {
                    if (typeof(response["responseJSON"]["Error"]) != "undefined") {
                        showError(response["responseJSON"]["Error"]["message"]);
                    } else {
                        if(window.opener)
                            window.opener.location.reload();
                        window.location.href = "app/search-assets/create-agreement-ack.html?name=" + getNameById(employees, agreement["approverPositionId"]) +"&asset=" + response.responseJSON["Agreements"][0]["asset"]["name"]+ "&ackNo=" + response.responseJSON["Agreements"][0]["acknowledgementNumber"];
                    }

                } else if(response["responseJSON"] && response["responseJSON"].Error) {
                    var err = response["responseJSON"].Error.message || "";
                    if(response["responseJSON"].Error.fields && Object.keys(response["responseJSON"].Error.fields).length) {
                      for(var key in response["responseJSON"].Error.fields) {
                        var _key = "";
                        if(key.indexOf(".") > -1) {
                          _key = key.split(".");
                          _key.shift();
                          _key = _key.join(".");
                        }
                        err += "\n " + _key + "- " + response["responseJSON"].Error.fields[key] + " "; //HERE
                      }
                      showError(err);
                    } else {
                      showError(response["statusText"]);
                    }
                } else {
                    showError(err);
                }
            }
        })
    }
})

function uploadFiles(agreement, cb) {
    if (agreement.documents && agreement.documents.constructor == FileList) {
        let counter = agreement.documents.length,
            breakout = 0, docs = [];
        for (let i = 0, len = agreement.documents.length; i < len; i++) {
            makeAjaxUpload(agreement.documents[i], function(err, res) {
                if (breakout == 1)
                    return;
                else if (err) {
                    cb(err);
                    breakout = 1;
                } else {
                    counter--;
                    docs.push({fileStore: res.files[0].fileStoreId});
                    if (counter == 0 && breakout == 0) {
                        agreement.documents = docs;
                        cb(null, agreement);
                    }
                }
            })
        }
    } else {
        agreement.documents = null;
        cb(null, agreement);
    }
}

function makeAjaxUpload(file, cb) {
    if(file.constructor == File) {
        let formData = new FormData();
        formData.append("jurisdictionId", "ap.public");
        formData.append("module", "PGR");
        formData.append("file", file);
        $.ajax({
            url: baseUrl + "/filestore/v1/files?tenantId=" + tenantId,
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
    } else {
        cb(null, {
              files: [{
                fileStoreId: file
              }]
            });
    }
}

async function loadRenewalRent(basisOfAllotment){
  var renewalOfRent = commonApiPost("lams-services", "getrentincrements", "", {tenantId, basisOfAllotment:basisOfAllotment}).responseJSON;
 $.each(renewalOfRent, function (idx, rent){
    $("#rentIncrementMethod").append($("<option />").val(rent.id).text(rent.description));
  });
}

function dependentonBasisTime(basis, time){
  if(basis === 'Goodwill Auction Basis'){
    if(time <= 5){
      $('#governmentOrder').hide();
      clearGovernment();
      $('#municipalOrder').show();
    }else{
      $('#municipalOrder').hide();
      clearMunicipal();
      $('#governmentOrder').show();
    }
  }else if(basis === 'Normal Basis'){
    if(time <= 3){
      $('#governmentOrder').hide();
      clearGovernment();
      $('#municipalOrder').show();
    }else{
      $('#municipalOrder').hide();
      clearMunicipal();
      $('#governmentOrder').show();
    }
  }
}

function clearGovernment(){
  fillValueToObject({id:'governmentOrderNumber',name:'governmentOrderNumber',value:''});
  fillValueToObject({id:'governmentOrderDate',name:'governmentOrderDate',value:''});
  $('#governmentOrderNumber, #governmentOrderDate').val('');
}

function clearMunicipal(){
  fillValueToObject({id:'municipalOrderNumber',name:'municipalOrderNumber',value:''});
  fillValueToObject({id:'municipalOrderDate',name:'municipalOrderDate',value:''});
  $('#municipalOrderNumber, #municipalOrderDate').val('');
}
