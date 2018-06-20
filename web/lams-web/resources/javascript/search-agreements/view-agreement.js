var CONST_API_GET_FILE = "/filestore/v1/files/id?tenantId=" + tenantId + "&fileStoreId=";
function getValueByName(name, id) {
    for (var i = 0; i < assetCategories.length; i++) {
        if (assetCategories[i].id == id) {
            return assetCategories[i][name];
        }
    }
}

function setRentPrefix(name) {
    $(`label[for=rent]`).text(name + " Rent (Rs)");
}

var rejectedSenderName, showFiles = 0;
try {
    var department = !localStorage.getItem("assignments_department") || localStorage.getItem("assignments_department") == "undefined" ? (localStorage.setItem("assignments_department", JSON.stringify(getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"] || [])), JSON.parse(localStorage.getItem("assignments_department"))) : JSON.parse(localStorage.getItem("assignments_department"));
} catch (e) {
    console.log(e);
    var department = [];
}

try { locality = !localStorage.getItem("locality") || localStorage.getItem("locality") == "undefined" ? (localStorage.setItem("locality", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "LOCALITY", hierarchyTypeName: "LOCATION", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("locality"))) : JSON.parse(localStorage.getItem("locality")); } catch (e) {
    console.log(e);
    locality = [];
}

try { assetCategories = !localStorage.getItem("assetCategories") || localStorage.getItem("assetCategories") == "undefined" ? (localStorage.setItem("assetCategories", JSON.stringify(commonApiPost("asset-services", "assetCategories", "_search", { tenantId }).responseJSON["AssetCategory"] || [])), JSON.parse(localStorage.getItem("assetCategories"))) : JSON.parse(localStorage.getItem("assetCategories")); } catch (e) {
    console.log(e);
    assetCategories = [];
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


var _type;
var nextUserDesignation;
$(document).ready(function() {
    if (window.opener && window.opener.document) {
        var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
        if (logo_ele && logo_ele[0]) {
            document.getElementsByClassName("homepage_logo")[0].src = logo_ele[0].getAttribute("src");
        }
    }
    $('.datepicker').datepicker({
        format: 'DD/MM/YYYY'
    });
    $('.datepicker').on("change", function(e) {
        fillValueToObject(e.target);
    });
    $("#viewDcb").on("click", function() {
        //clear cookies and logout
        // $("#login").hide();
        // $("#dashboard").show();
        window.location = "app/search-agreement/view-dcb.html"
    });

    $("#close").on("click", function() {
        open(location, '_self').close();
    })

    function createFileStore(noticeData, blob){
      var promiseObj = new Promise(function(resolve, reject){
        let formData = new FormData();
        formData.append("tenantId", tenantId);
        formData.append("module", "LAMS");
        formData.append("file", blob);
        $.ajax({
            url: baseUrl + "/filestore/v1/files?tenantId=" + tenantId,
            data: formData,
            cache: false,
            contentType: false,
            processData: false,
            type: 'POST',
            success: function (res) {
                let obj={
                  noticeData : noticeData,
                  fileStoreId : res.files[0].fileStoreId
                }
                resolve(obj);
            },
            error: function (jqXHR, exception) {
                reject(jqXHR.status);
            }
        });
      });
      return promiseObj;
    }

    function createNotice(obj){
      $.ajax({
          url: baseUrl + `/lams-services/agreement/notice/_create?tenantId=` + tenantId,
          type: 'POST',
          dataType: 'json',
          data: JSON.stringify({
              RequestInfo: requestInfo,
              Notice: {
                  tenantId,
                  agreementNumber: obj.noticeData.agreementNumber,
                  fileStore:obj.fileStoreId
              }
          }),
          headers: {
              'auth-token': authToken
          },
          contentType: 'application/json',
          success:function(res){
            // console.log('notice created');
            if(window.opener)
                window.opener.location.reload();
            open(location, '_self').close();
          },
          error:function(jqXHR, exception){
            console.log('error');
            showError('Error while creating notice');
          }
      });

    }

    function errorHandler(statusCode){
     console.log("failed with status", status);
     showError('Error');
    }

    function printNotice(agreement) {

         var commDesignation = commonApiPost("hr-masters", "designations", "_search", {name:"Commissioner", active:true,tenantId }).responseJSON["Designation"];
         var commDesignationId = commDesignation[0].id;
         var commissioners =  commonApiPost("hr-employee", "employees", "_search", {
                             tenantId,
                             designationId: commDesignationId,
                             active: true,
                             asOnDate: moment(new Date()).format("DD/MM/YYYY")
                             }).responseJSON["Employee"] || [];
        var commissionerName =commissioners[0].name;
        var LocalityData = commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "LOCALITY", hierarchyTypeName: "LOCATION", tenantId });
        var locality = getNameById(LocalityData["responseJSON"]["Boundary"], agreement.asset.locationDetails.locality);
        var cityGrade = !localStorage.getItem("city_grade") || localStorage.getItem("city_grade") == "undefined" ? (localStorage.setItem("city_grade", JSON.stringify(commonApiPost("tenant", "v1/tenant", "_search", { code: tenantId }).responseJSON["tenant"][0]["city"]["ulbGrade"] || {})), JSON.parse(localStorage.getItem("city_grade"))) : JSON.parse(localStorage.getItem("city_grade"));
        var ulbType = "Nagara Panchayat/Municipality";
        if (cityGrade.toLowerCase() === 'corp') {
            ulbType = "Municipal Corporation";
        }

        let commencementDate = agreement.commencementDate;
        let timePeriod = agreement.timePeriod;
        let endDate = commencementDate.split("/")[0]+"/"+commencementDate.split("/")[1]+"/"+ (Number(commencementDate.split("/")[2]) + Number(timePeriod))

        var columns1 = [
            { title: "Particulars", dataKey: "particulars" },
            { title: "Amount", dataKey: "amount" },
            { title: "Cheque/DD/Challan No and Date", dataKey: "leaseHolderName" },

        ];

        var rows1 = [
            { "particulars": "Goodwill", "amount": agreement.goodWillAmount, "leaseHolderName": "" },
            { "particulars": "3 Months Rental Deposits", "amount": agreement.securityDeposit, "leaseHolderName": "" },
            { "particulars": "Total", "amount": Number(agreement.goodWillAmount) + Number(agreement.securityDeposit), "leaseHolderName": "" }
        ];


        var autoTableOptions1 = {
            tableLineColor: [0, 0, 0],
            tableLineWidth: 0.2,
            styles: {
                lineColor: [0, 0, 0],
                lineWidth: 0.2,
            },
            headerStyles: {
                textColor: [0, 0, 0],
                fillColor: [255, 255, 255],
                overflow: 'linebreak',
                columnWidth: 'wrap'
            },
            bodyStyles: {
                fillColor: [255, 255, 255],
                textColor: [0, 0, 0],
                overflow: 'linebreak',
                columnWidth: 'wrap'
            },
            alternateRowStyles: {
                fillColor: [255, 255, 255]
            }, startY: 135
        };


        var columns2 = [
            { title: "Particulars", dataKey: "particulars" },
            { title: "Amount", dataKey: "amount" }
        ];

        var rows2 = [
            { "particulars": "Monthly Rental", "amount": agreement.rent },
            { "particulars": "GST", "amount": Number(agreement.sgst) + Number(agreement.cgst) },
        ];


        var autoTableOptions2 = {
            tableLineColor: [0, 0, 0],
            tableLineWidth: 0.2,
            styles: {
                lineColor: [0, 0, 0],
                lineWidth: 0.2,
            },
            headerStyles: {
                textColor: [0, 0, 0],
                fillColor: [255, 255, 255],
                overflow: 'linebreak',
                columnWidth: 'wrap'
            },
            bodyStyles: {
                fillColor: [255, 255, 255],
                textColor: [0, 0, 0],
                overflow: 'linebreak',
                columnWidth: 'wrap'
            },
            alternateRowStyles: {
                fillColor: [255, 255, 255]
            }, startY: 190
        };



        var doc = new jsPDF();

        doc.setFontType("bold");
        doc.setFontSize(13);
        doc.text(105, 20, "PROCEEDINGS OF THE COMMISSIONER, " + tenantId.split(".")[1].toUpperCase(), 'center');
        doc.text(105, 27, ulbType.toUpperCase(), 'center');
        doc.text(105, 34, "Present: "+ commissionerName, 'center');

        doc.setFontType("normal");
        doc.setFontSize(11);
        doc.text(15,50, 'Roc.No.');
        doc.setFontType("bold");
        doc.text(30,50, agreement.noticeNumber);
        doc.setFontType("normal");
        doc.text(140,50, 'Dt. ');
        doc.setFontType("bold");
        doc.text(146,50, agreement.agreementDate);

        doc.fromHTML("Sub: Leases – Revenue Section – Shop No <b>" + agreement.referenceNumber + "</b> in <b>" + agreement.asset.name + "</b> Complex, <b> " + locality + "</b> <br> - Allotment of lease – Orders  - Issued",15, 60);

        doc.fromHTML("Ref: 1. Open Auction Notice dt. <b>" + agreement.tenderDate +"</b> of this office",15,80);

        doc.fromHTML("2. Resolution No <b>" + agreement.councilNumber + "</b> dt <b>" + agreement.councilDate + "</b> of Municipal Council/Standing Committee",23,85);

        doc.text(105, 95, "><><><", 'center');

        doc.text(15, 105, "Orders:");
        doc.setLineWidth(0.5);
        doc.line(15, 106, 28, 106);

        doc.fromHTML("In the reference 1st cited, an Open Auction for leasing Shop No <b>" + agreement.referenceNumber + "</b> in <b>" + agreement.asset.name + "</b> <br>Shopping Complex was conducted and your bid for the highest amount (i.e. monthly rentals of Rs <b>" + agreement.rent + "/- </b> <br>and Goodwill amount of Rs <b>" + agreement.goodWillAmount + "/- </b> was accepted by the Municipal Council/Standing Committee vide <br>reference 2nd cited with the following deposit amounts as received by this office.",15, 115);

        doc.autoTable(columns1, rows1, autoTableOptions1);

        doc.fromHTML("In pursuance of the Municipal Council/Standing Committee resolution and vide GO MS No 56 (MA & UD <br> Department) dt. 05.02.2011, the said shop is allotted to you for the period <b>" + commencementDate + "</b> to <b>" + endDate + "</b> at <br> following rates of rentals and taxes thereon.",15, 175);

        doc.autoTable(columns2, rows2, autoTableOptions2);



        var paragraph3 = "The following terms and conditions are applicable for the renewal of lease."

            + "\n\t1. The leaseholder shall pay rent by 5th of the succeeding month"
            + "\n\t2. All the late payments of rentals will attract penalty and interest as applicable"
            + "\n\t3. The leaseholder shall not sub lease the premises in any case. If it is found that the premises are being sub let to any person, the lease shall stand cancelled without any prior notice."
            + "\n\t4. The D&O Trade License of the establishment shall be in the name of the leaseholder only."
            + "\n\t5. The leaseholder shall do business in the name of himself only."
            + "\n\t6. The leaseholder not to use the premises for any unlawful activities"
            + "\n\t7. The Goodwill and the Rental Deposits paid by the leaseholder shall be forfeited in the event of violation of terms and conditions in the agreement."

        var paragraph4 = "Hence you are requested to conclude an agreement duly registered with the SRO for the above mentioned lease within 15 days of receipt of this renewal letter without fail unless the renewal will stand cancelled without any further correspondence."

        var lines = doc.splitTextToSize(paragraph3, 180);
        doc.text(15, 220, lines);

        doc.addPage();
        var paragraph4 = "Hence you are requested to conclude an agreement duly registered with the SRO for the above mentioned lease within 15 days of receipt of this allotment letter without fail unless the allotment will stand cancelled without any further correspondence."
        var lines = doc.splitTextToSize(paragraph4, 180);
        doc.text(15, 30, lines);

        doc.text(120, 50, "Commissioner");
        doc.fromHTML("<b> "+tenantId.split(".")[1].charAt(0).toUpperCase() + tenantId.split(".")[1].slice(1) + " "+ulbType+"</b>",120, 55)

        doc.text(15, 60, "To");
        doc.text(15, 65, "The Leaseholder");
        doc.text(15, 70, "Copy to the concerned officials for necessary action");

        doc.save('Notice-' + agreement.agreementNumber + '.pdf');
        var blob = doc.output('blob');

        //  var text= noticeData.agreementPeriod * 12;
        //   doc.setFontType("normal");

        return text;
    }

    // function wordWrap(doc, paragraph, lMargin, rMargin, pdfInMM) {
    //     return doc.splitTextToSize(paragraph, (pdfInMM - lMargin - rMargin));
    // }

    try {
        if (getUrlVars()["agreementNumber"]) {
            var agreementDetail = commonApiPost("lams-services", "agreements", "_search", {
                agreementNumber: getUrlVars()["agreementNumber"],
                tenantId
            }).responseJSON["Agreements"][0] || {};
        } else if (getUrlVars()["acknowledgementNumber"]) {
            var agreementDetail = commonApiPost("lams-services", "agreements", "_search", {
                acknowledgementNumber: getUrlVars()["acknowledgementNumber"],
                tenantId
            }).responseJSON["Agreements"][0] || {};
        } else {
            var agreementDetail = commonApiPost("lams-services", "agreements", "_search", {
                stateId: getUrlVars()["state"],
                tenantId
            }).responseJSON["Agreements"][0] || {};
        }


        var assetDetails = commonApiPost("asset-services", "assets", "_search", {
            id: (getUrlVars()["assetId"] || agreementDetail.asset.id),
            tenantId
        }).responseJSON["Assets"][0] || {};

        //Attach photos
        if(agreementDetail && agreementDetail.documents) {
            showFiles = 1;
            for(var i=0; i<agreementDetail.documents.length; i++) {
                $("#fileTableTbody").append(`<tr>
                    <td>${i+1}</td>
                    <td>Document</td>
                    <td>
                        <a href=${window.location.origin + CONST_API_GET_FILE + agreementDetail.documents[i].fileStore} target="_blank">
                          Download
                        </a>
                    </td>
                </tr>`);
            }
        }

        printValue("", agreementDetail);
        printValue("", assetDetails, true);
    } catch (e) {
        console.log(e);
    }

    function printValue(object = "", values, isAsset) {
        if (object != "") {

        } else {
            for (var key in values) {
                if (typeof values[key] === "object") {
                    for (ckey in values[key]) {
                        if (typeof values[key][ckey] != "undefined") {
                            //Get description
                            if (isAsset && key == "locationDetails" && ["locality", "electionWard", "street", "revenueWard", "revenueZone", "revenueBlock"].indexOf(ckey) > -1) {
                                var _obj;
                                switch (ckey) {
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
                                        _obj = revenueWards;
                                        break;
                                    case 'revenueZone':
                                        _obj = revenueZone;
                                        break;
                                    case 'revenueBlock':
                                        _obj = revenueBlock;
                                        break;
                                }
                                $("[name='" + (isAsset ? "asset." : "") + key + "." + ckey + "']").text(getNameById(_obj, values[key][ckey]) || "NA");
                                $("[name='" + (isAsset ? "asset." : "") + key + "." + ckey + "']").val(getNameById(_obj, ckey) || "");
                            } else
                                $("[name='" + (isAsset ? "asset." : "") + key + "." + ckey + "']").text(values[key][ckey] ? values[key][ckey] : "NA");
                                $("[name='" + (isAsset ? "asset." : "") + key + "." + ckey + "']").val(values[key][ckey] || "");
                        }
                    }
                } else if (typeof values[key] != "undefined") {
                    $("[name='" + (isAsset ? "asset." : "") + key + "']").text(values[key]);
                    $("[name='" + (isAsset ? "asset." : "") + key + "']").val(values[key]);
                } else {
                    $("[name='" + (isAsset ? "asset." : "") + key + "']").text("NA");
                }

                if (key.search('date') > 0) {
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
    function fillValueToObject(currentState) {
        if (currentState.id.includes(".")) {
            if(currentState.id == "rentIncrementMethod.percentage") {
                agreementDetail["rentIncrementMethod"] = {
                    id: currentState.value
                };
            } else {
                var splitResult = currentState.id.split(".");
                if (agreement.hasOwnProperty(splitResult[0])) {
                    agreementDetail[splitResult[0]][splitResult[1]] = currentState.value;
                } else {
                    agreementDetail[splitResult[0]] = {};
                    agreementDetail[splitResult[0]][splitResult[1]] = currentState.value;
                }
            }
        } else {
            agreementDetail[currentState.id] = currentState.value;
        }
    }


    var renewAgreement = {};
    //Getting data for user input
    $("input").on("keyup", function() {
        // console.log(this.value);
      //  renewAgreement[this.id] = this.value;
        fillValueToObject(this);
    });

    $("textarea").on("keyup", function() {
        fillValueToObject(this);
    });

    //Getting data for user input
    $("select").on("change", function() {
        // console.log(this.value);
        renewAgreement[this.id] = this.value;
        if(this.id == "rentIncrementMethod.percentage")
            fillValueToObject(this);

        if(this.id =="approverDesignation") {
          nextUserDesignation =$('#approverDesignation option:selected').text();
        }
        if (($("#approverDepartment").val() != "" && $("#approverDesignation").val() != "") && (this.id == "approverDepartment" || this.id == "approverDesignation")) {
            employees = commonApiPost("hr-employee", "employees", "_search", {
                tenantId,
                departmentId: $("#approverDepartment").val(),
                designationId: $("#approverDesignation").val(),
                active: true,
                asOnDate: moment(new Date()).format("DD/MM/YYYY")
            }).responseJSON["Employee"] || [];



            for (var i = 0; i < employees.length; i++) {
                $(`#approverPositionId`).append(`<option value='${employees[i]['id']}'>${employees[i]['name']}</option>`)
            }
        }
    });

    //file change handle for file upload
    $("input[type=file]").on("change", function(evt) {
        // console.log(this.value);
        // renewAgreement[this.id] = this.value;
        renewAgreement[this.id] = evt.currentTarget.files;

        //call post api update and update that url in pur agrement object
    });

    var validation_rules = {};
    var final_validatin_rules = {};

    if (getUrlVars()["view"] == "cancel") {
        var commom_fields_rules = {
            cancel_reason: {
                required: true
            },
            cancel_termination_date: {
                required: true
            },
            cancel_order_no: {
                required: true
            },
            cancel_order_date: {
                required: true
            }
        };
    } else if (getUrlVars()["view"] == "eviction") {
        var commom_fields_rules = {
            evict_reason: {
                required: true
            },
            evict_proceed_date: {
                required: true
            },
            evict_proceed_no: {
                required: true
            }
        };
    } else {
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
            },
            date_of_expiry: {
                required: true
            },
            renew_order_date: {
                required: true
            },
            renew_period: {
                required: true
            }
        };
    }

    //remove renew part and related buttons from dom
    if (getUrlVars()["view"] == "new") {
        //removing renew section and renew button
        $("#renew,#workFlowDetails,#renewBtn,#cancel,#evict").remove();
        if(showFiles) $("#fileTable").show();
    } else if (getUrlVars()["view"] == "inbox") {
        $("#historyTable").show();
        $("#cancel,#evict,#renew,#renewBtn").remove();
        if(showFiles) $("#fileTable").show();
        //Fetch workFlow
        var workflow = commonApiPost("egov-common-workflows", "history", "", {
            tenantId: tenantId,
            workflowId: agreementDetail.stateId
        }).responseJSON["tasks"];

        var process = commonApiPost("egov-common-workflows", "process", "_search", {
            tenantId: tenantId,
            id: agreementDetail.stateId
        }).responseJSON["processInstance"];
        var currentUserDesignation = null;
         var ownerPosition = process.owner.id;
         var currOwnerPosition=null;

         var loggedInEmployee = commonApiPost("hr-employee", "employees", "_loggedinemployee", {asOnDate: moment(new Date()).format("DD/MM/YYYY"), tenantId }).responseJSON["Employee"];
            if(loggedInEmployee){
              var assignments = loggedInEmployee[0].assignments;
              var positions=[];
              if(assignments){
                for(var i=0;i<assignments.length;i++){
                    if(ownerPosition==assignments[i].position){
                      currentUserDesignation = process.owner.deptdesig.designation.name;
                      break;
                }else{
                   currOwnerPosition =assignments[0].designation;
                }
                }
              }

            }
            console.log(currentUserDesignation);
         if(currOwnerPosition && currentUserDesignation==null){

           var designation = getCommonMasterById("hr-masters", "designations", null,currOwnerPosition).responseJSON["Designation"];
           currentUserDesignation = designation[0].name;
         }

        if (workflow && workflow.length) {
            workflow = workflow.sort();
            workflow = workflow.reverse();
          for (var i = 0; i < workflow.length; i++) {
                if(workflow[i].status == "Assistant Approved")
                    rejectedSenderName = workflow[i].senderName;
                $("#historyTable tbody").append(`<tr>
                    <td data-label="createdDate">${workflow[i].lastupdatedSince}</td>
                    <td data-label="updatedBy">${workflow[i].senderName}</td>
                    <td data-label="status">${workflow[i].status}</td>
                    <td data-label="comments">${workflow[i].comments}</td>
                    </tr>
                `);
            }
        }

        if (process && process.attributes && process.attributes.validActions && process.attributes.validActions.values && process.attributes.validActions.values.length) {
            var flg = 0;
            for (var i = 0; i < process.attributes.validActions.values.length; i++) {
                if (process.attributes.validActions.values[i].key)
                $("#footer-btn-grp").append($(`<button data-action='${process.attributes.validActions.values[i].key}' id=${process.attributes.validActions.values[i].key} type="button" class="btn btn-submit">${process.attributes.validActions.values[i].name}</button>`))
                                  $("#footer-btn-grp").append($('<span>&nbsp;&nbsp</span>'));
                if (process.attributes.validActions.values[i].key.toLowerCase() == "print notice") {
                    $(".workFlowDetailsSection").remove();

                }
                if(process.attributes.validActions.values[i].key.toLowerCase() == "forward") {
                    flg = 1;
                }
            }

            if(flg == 0 && $(".workFlowDetails"))
                $(".workFlowDetails").remove();
        } else {
            $("#workFlowDetails").remove();
        }
        if(currentUserDesignation && currentUserDesignation==='Commissioner'){
          $("#workFlowDetails").remove();

        }
         $("#footer-btn-grp").append($('<button type="button" class="btn btn-close" id="close">Close</button>'));


         if(currentUserDesignation==='Junior Assistant' || currentUserDesignation==='Senior Assistant'){
           currentUserDesignation ="Assistant";
         }
        if (process) {
            getDesignations(process.status,currentUserDesignation, function(designations) {
                for (var variable in designations) {
                    if (!designations[variable]["id"]) {
                        var _res = commonApiPost("hr-masters", "designations", "_search", { tenantId, name: designations[variable]["name"] });
                        designations[variable]["id"] = _res && _res.responseJSON && _res.responseJSON["Designation"] && _res.responseJSON["Designation"][0] ? _res.responseJSON["Designation"][0].id : "";
                    }

                    $(`#approverDesignation`).append(`<option value='${designations[variable]["id"]}'>${designations[variable]["name"]}</option>`)
                }
            },process.businessKey);
        }
    } else if (getUrlVars()["view"] == "renew") {
        $("#cancel,#evict").remove();
        $(`label[for=renew_rent]`).text(getUrlVars()["type"] == "shop" ? "Shop Rent (Rs)" : "Land Rent (Rs)");
    } else if (["cancel", "eviction"].indexOf(getUrlVars()["view"]) > -1) {
        $("#renew").remove();
        //$("#renewBtn").text("Submit");
        $(".hide-sec").hide();
        $("#viewDetBtn").show();
        $("#minDetSec").show();
        if (getUrlVars()["view"] == "cancel") {
            $("#evict").remove();
        } else {
            $("#cancel").remove();
        }
    } else {
        $("#viewDcb,#cancel,#evict").remove();
    }

    for (var variable in department) {
        $(`#approverDepartment`).append(`<option value='${department[variable]["id"]}'>${department[variable]["name"]}</option>`)
    }
    /* for (var variable in designation) {
       $(`#approverDesignation`).append(`<option value='${designation[variable]["id"]}'>${designation[variable]["name"]}</option>`)
     }*/

    _type = getUrlVars()["type"] ? decodeURIComponent(getUrlVars()["type"]) : getValueByName("name", agreementDetail.asset.assetCategory.id);
    // console.log(_type);
    if (_type && _type.toLowerCase() == "land") {

        // remove all other Asset Details block from DOM except land asset related fields
        $(".shoppingComplexAssetDetailsBlock,.shopAssetDetailsBlock, .marketAssetDetailsBlock, .kalyanamandapamAssetDetailsBlock, .parkingSpaceAssetDetailsBlock, .slaughterHousesAssetDetailsBlock, .usfructsAssetDetailsBlock, .communityAssetDetailsBlock, .fishTankAssetDetailsBlock, .parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $(".agreementDetailsBlockTemplateTwo,.agreementDetailsBlockTemplateThree").remove();
        setRentPrefix("Land");
    } else if (_type && _type.toLowerCase() == "shopping complex") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $(".landAssetDetailsBlock,.shopAssetDetailsBlock, .marketAssetDetailsBlock, .kalyanamandapamAssetDetailsBlock, .parkingSpaceAssetDetailsBlock, .slaughterHousesAssetDetailsBlock, .usfructsAssetDetailsBlock, .communityAssetDetailsBlock, .fishTankAssetDetailsBlock, .parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $(".agreementDetailsBlockTemplateOne,.agreementDetailsBlockTemplateThree").remove();
        setRentPrefix("Shopping Complex");
    } else if (_type && _type.toLowerCase() == "shop") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $(".shoppingComplexAssetDetailsBlock,.landAssetDetailsBlock, .marketAssetDetailsBlock, .kalyanamandapamAssetDetailsBlock, .parkingSpaceAssetDetailsBlock, .slaughterHousesAssetDetailsBlock, .usfructsAssetDetailsBlock, .communityAssetDetailsBlock, .fishTankAssetDetailsBlock, .parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $(".agreementDetailsBlockTemplateOne,.agreementDetailsBlockTemplateThree").remove();
        setRentPrefix("Shop");
    } else if (_type && _type.toLowerCase() == "market") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $(".shoppingComplexAssetDetailsBlock,.rendCalculatedMethod,.shopAssetDetailsBlock, .landAssetDetailsBlock, .kalyanamandapamAssetDetailsBlock, .parkingSpaceAssetDetailsBlock, .slaughterHousesAssetDetailsBlock, .usfructsAssetDetailsBlock, .communityAssetDetailsBlock, .fishTankAssetDetailsBlock, .parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $(".agreementDetailsBlockTemplateOne,.agreementDetailsBlockTemplateTwo").remove();
        setRentPrefix("Market");
    } else if (_type && _type.toLowerCase() == "kalyana mandapam") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $(".shoppingComplexAssetDetailsBlock,.rendCalculatedMethod,.shopAssetDetailsBlock, .landAssetDetailsBlock, .marketAssetDetailsBlock, .parkingSpaceAssetDetailsBlock, .slaughterHousesAssetDetailsBlock, .usfructsAssetDetailsBlock, .communityAssetDetailsBlock, .fishTankAssetDetailsBlock, .parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $(".agreementDetailsBlockTemplateOne,.agreementDetailsBlockTemplateTwo").remove();
        setRentPrefix("Kalyana Mandapam");
    } else if (_type && _type.toLowerCase() == "parking space") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $(".shoppingComplexAssetDetailsBlock,.rendCalculatedMethod,.shopAssetDetailsBlock, .landAssetDetailsBlock, .marketAssetDetailsBlock, .kalyanamandapamAssetDetailsBlock, .slaughterHousesAssetDetailsBlock, .usfructsAssetDetailsBlock, .communityAssetDetailsBlock, .fishTankAssetDetailsBlock, .parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $(".agreementDetailsBlockTemplateOne,.agreementDetailsBlockTemplateTwo").remove();
        setRentPrefix("Parking Space");
    } else if (_type && _type.toLowerCase() == "slaughter house") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $(".shoppingComplexAssetDetailsBlock,.rendCalculatedMethod,.shopAssetDetailsBlock, .landAssetDetailsBlock, .marketAssetDetailsBlock, .kalyanamandapamAssetDetailsBlock, .parkingSpaceAssetDetailsBlock, .usfructsAssetDetailsBlock, .communityAssetDetailsBlock, .fishTankAssetDetailsBlock, .parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $(".agreementDetailsBlockTemplateOne,.agreementDetailsBlockTemplateTwo").remove();
        setRentPrefix("Slaughter House");
    } else if (_type && _type.toLowerCase() == "usufruct") {


        // remove all other Asset Details block from DOM except shop asset related fields
        $(".shoppingComplexAssetDetailsBlock,.rendCalculatedMethod,.shopAssetDetailsBlock, .landAssetDetailsBlock, .marketAssetDetailsBlock, .kalyanamandapamAssetDetailsBlock, .parkingSpaceAssetDetailsBlock, .slaughterHousesAssetDetailsBlock, .communityAssetDetailsBlock, .fishTankAssetDetailsBlock, .parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $(".agreementDetailsBlockTemplateOne,.agreementDetailsBlockTemplateTwo").remove();
        $("#rrReadingNo").remove();
        setRentPrefix("Usufruct");
    } else if (_type && _type.toLowerCase() == "community toilet complex") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $(".shoppingComplexAssetDetailsBlock,.rendCalculatedMethod,.shopAssetDetailsBlock, .landAssetDetailsBlock, .marketAssetDetailsBlock, .kalyanamandapamAssetDetailsBlock, .parkingSpaceAssetDetailsBlock, .slaughterHousesAssetDetailsBlock, .usfructsAssetDetailsBlock, .fishTankAssetDetailsBlock, .parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $(".agreementDetailsBlockTemplateOne,.agreementDetailsBlockTemplateTwo").remove();
        setRentPrefix("Community Toilet Complex");
    } else if (_type && _type.toLowerCase() == "fish tanks") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $(".shoppingComplexAssetDetailsBlock,.rendCalculatedMethod,.shopAssetDetailsBlock, .landAssetDetailsBlock, .marketAssetDetailsBlock, .kalyanamandapamAssetDetailsBlock, .parkingSpaceAssetDetailsBlock, .slaughterHousesAssetDetailsBlock, .usfructsAssetDetailsBlock, .communityAssetDetailsBlock, .parkAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $(".agreementDetailsBlockTemplateOne,.agreementDetailsBlockTemplateTwo").remove();
        setRentPrefix("Fish Tanks");
    } else if (_type && _type.toLowerCase() == "parks") {

        // remove all other Asset Details block from DOM except shop asset related fields
        $(".shoppingComplexAssetDetailsBlock,.rendCalculatedMethod,.shopAssetDetailsBlock, .landAssetDetailsBlock, .marketAssetDetailsBlock, .kalyanamandapamAssetDetailsBlock, .parkingSpaceAssetDetailsBlock, .slaughterHousesAssetDetailsBlock, .usfructsAssetDetailsBlock, .communityAssetDetailsBlock, .fishTankAssetDetailsBlock").remove();
        //remove agreement template two and three from screen
        $(".agreementDetailsBlockTemplateOne,.agreementDetailsBlockTemplateTwo").remove();
        setRentPrefix("Parks");
    }
    final_validatin_rules = Object.assign(validation_rules, commom_fields_rules);
    for (var key in final_validatin_rules) {
        if (final_validatin_rules[key].required) {
            $(`label[for=${key}]`).append(`<span> *</span>`);
        }
        // $(`#${key}`).attr("disabled",true);
    };



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


    $('body').on('click', 'button', function(e) {

        if(e.target.id==='close'){
          window.close();
        }else{
        e.preventDefault();
        if (!e.target.id) return;
        var data = $("#" + e.target.id).data();
        var _agrmntDet = Object.assign({}, agreementDetail);
        _agrmntDet.workflowDetails = {
            "businessKey": process.businessKey,
            "type": process.businessKey,
            "assignee": $("#approverPositionId") && $("#approverPositionId").val() && (!data.action || (data.action && data.action.toLowerCase() != "reject")) ? getPositionId($("#approverPositionId").val()) : process.initiatorPosition,
            "status": process.status,
            "designation":currentUserDesignation,
            "nextDesignation": nextUserDesignation,
            "action": data.action
        };

        if($("#wFremarks").val()) {
            _agrmntDet["workflowDetails"]["comments"] = $("#wFremarks").val();
        }

        if (data.action && data.action != "Print Notice") {
            if(data.action.toLowerCase() == "reject" && !$("#wFremarks").val()) {
                return showError("Comments is mandatory in case of 'Reject'");
            }

            if(data.action.toLowerCase() == "forward" && !$("#approverPositionId").val()) {
                return showError("Approver name is required.");
            }

            var response = $.ajax({
                url: baseUrl + `/lams-services/agreements/_update/${agreementDetail.acknowledgementNumber}?tenantId=` + tenantId,
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify({
                    RequestInfo: requestInfo,
                    Agreement: _agrmntDet
                }),
                async: false,
                headers: {
                    'auth-token': authToken
                },
                contentType: 'application/json'
            });


            if (response["status"] === 201) {
                if(window.opener)
                    window.opener.location.reload();
                window.location.href = "app/search-assets/create-agreement-ack.html?name=" + (data.action && data.action.toLowerCase() == "reject" ? (rejectedSenderName || "") : ($("#approverPositionId").val() ? getNameById(employees, $("#approverPositionId").val()) : "")) + "&ackNo=" + (data.action.toLowerCase() == "approve" ? response.responseJSON["Agreements"][0]["agreementNumber"] : response.responseJSON["Agreements"][0]["acknowledgementNumber"]) + "&action=" + data.action;
            } else {
                showError(response["statusText"]);
            }
        } else {
            delete _agrmntDet.workflowDetails.assignee;
            var response = $.ajax({
                url: baseUrl + `/lams-services/agreements/_update/${agreementDetail.acknowledgementNumber}?tenantId=` + tenantId,
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify({
                    RequestInfo: requestInfo,
                    Agreement: _agrmntDet
                }),
                async: false,
                headers: {
                    'auth-token': authToken
                },
                contentType: 'application/json'
            });

            if (response["status"] === 201) {
                workflow.forEach(function(item){
                    if(item.status === "")
                    agreement.commissionerName = item.senderName
                  });
              //call notice
              printNotice(agreementDetail);
            } else {
                showError(response["statusText"]);
            }
        }
      }
    });

    // Adding Jquery validation dynamically
    $("#renewAgreementForm").validate({
        rules: final_validatin_rules,
        submitHandler: function(form) {
          var response = $.ajax({
              url: baseUrl + `/lams-services/agreements/_renew/?tenantId=` + tenantId,
              type: 'POST',
              dataType: 'json',
              data: JSON.stringify({
                  RequestInfo: requestInfo,
                  Agreement: agreementDetails
              }),
              async: false,
              headers: {
                  'auth-token': authToken
              },
              contentType: 'application/json'
          });

        }
    })

    $("#cancelAgreementForm").validate({
        rules: final_validatin_rules,
        submitHandler: function(form) {
           var response = $.ajax({
               url: baseUrl + `/lams-services/agreements/_cancel/?tenantId=` + tenantId,
               type: 'POST',
               dataType: 'json',
               data: JSON.stringify({
                   RequestInfo: requestInfo,
                   Agreement: agreementDetails
               }),
               async: false,
               headers: {
                   'auth-token': authToken
               },
               contentType: 'application/json'
           });

           if (response["status"] === 201) {
               window.location.href = "app/search-assets/create-agreement-ack.html?name=" + ($("#approverPositionId").val() ? getNameById(employees, $("#approverPositionId").val()) : "") + "&ackNo=" + (data.action.toLowerCase() == "approve" ? response.responseJSON["Agreements"][0]["agreementNumber"] : response.responseJSON["Agreements"][0]["acknowledgementNumber"]) + "&action=" + data.action;
           } else {
               showError(response["statusText"]);
           }
        }
    })
});
