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

// try {
//     rentInc = commonApiPost("lams-services", "getrentincrements", "", {
//         tenantId
//     }).responseJSON;
//
//     if (rentInc && rentInc.constructor == Array) {
//         for (var i = 0; i < rentInc.length; i++) {
//             // console.log(rentInc[i]['id']);
//             $(`#rentIncrementMethod.percentage`).append(`<option value='${rentInc[i]['id']}'>${rentInc[i]['percentage']}</option>`);
//         }
//     }
// } catch (e) {
//     console.log(e);
// }



try { reasonForCancellation = !localStorage.getItem("reasonForCancellation") || localStorage.getItem("reasonForCancellation") == "undefined" ? (localStorage.setItem("reasonForCancellation", JSON.stringify(commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "BLOCK", hierarchyTypeName: "REVENUE", tenantId }).responseJSON["Boundary"] || [])), JSON.parse(localStorage.getItem("reasonForCancellation"))) : JSON.parse(localStorage.getItem("reasonForCancellation")); } catch (e) {
    console.log(e);
    reasonForCancellation = [];
}



var _type;
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

    function printNotice(noticeData) {
        var commencementDate=noticeData.commencementDate;
        var expiryDate= noticeData.expiryDate;
        // var rentPayableDate = noticeData.rentPayableDate;

        var doc = new jsPDF();
        doc.setFontType("bold");
        doc.setFontSize(14);
        doc.text(105, 20, tenantId.split(".")[1] , 'center');
        doc.text(105, 27, tenantId.split(".")[1] + ' District', 'center');
        doc.text(105, 34, 'Asset Category Lease/Agreement Notice', 'center');
        doc.setLineWidth(0.5);
        doc.line(15, 38, 195, 38);
        doc.text(15, 47, 'Lease details: ');
        doc.text(110, 47, 'Agreement No: ' + noticeData.agreementNumber);
        doc.text(15, 57, 'Lease Name: ' + noticeData.allottee.name);
        doc.text(110, 57, 'Asset No: ' + noticeData.asset.code);
        doc.text(15, 67, (noticeData.allottee.mobileNumber ? noticeData.allottee.mobileNumber + ", " : "") + (noticeData.doorNo ? noticeData.doorNo + ", " : "") + (noticeData.allottee.permanentAddress ? noticeData.allottee.permanentAddress + ", " : "") + tenantId.split(".")[1] + ".");


        doc.setFontType("normal");
        doc.text(15, 77, doc.splitTextToSize('1.    The period of lease shall be ' ));
        doc.setFontType("bold");
        doc.text(85, 77, doc.splitTextToSize(' ' + noticeData.timePeriod * 12 + ' '));
        doc.setFontType("normal");
        doc.text(93, 77, doc.splitTextToSize('months commencing from'));
        doc.setFontType("bold");
        doc.text(15, 83, doc.splitTextToSize(' ' + commencementDate + ' '));
        doc.setFontType("normal");
        doc.text(42, 83, doc.splitTextToSize('(dd/mm/yyyy) to' ));
        doc.setFontType("bold");
        doc.text(77, 83, doc.splitTextToSize(' ' + expiryDate + ' '));
        doc.setFontType("normal");
        doc.text(104, 83, doc.splitTextToSize('(dd/mm/yyyy).', (210 - 15 - 15)));
        doc.text(15, 91, doc.splitTextToSize('2.    The property leased is shop No'));
        doc.setFontType("bold");
        doc.text(93, 91, doc.splitTextToSize(' ' + noticeData.asset.code + ' '));
        doc.setFontType("normal");
        doc.text(112, 91, doc.splitTextToSize('and shall be leased for a sum of '));
        doc.setFontType("bold");
        doc.text(15, 97, doc.splitTextToSize('Rs.' + noticeData.rent + '/- '));
        doc.setFontType("normal");
        doc.text(111, 97, doc.splitTextToSize('per month exclusive of the payment'));
        doc.text(15, 103, doc.splitTextToSize('of electricity and other charges.', (210 - 15 - 15)));
        doc.text(15, 112, doc.splitTextToSize('3.   The lessee has paid a sum of '));
        doc.setFontType("bold");
        doc.text(90, 112, doc.splitTextToSize('Rs.' + noticeData.securityDeposit + '/- '));
        doc.setFontType("normal");
        doc.text(15, 118, doc.splitTextToSize('as security deposit for the tenancy and the said sum is repayable or adjusted only at the end of the tenancy on the lease delivery vacant possession of the shop let out, subject to deductions, if any, lawfully and legally payable by the lessee under the terms of this lease deed and in law.', (210 - 15 - 15)));
        doc.text(15, 143, doc.splitTextToSize('4.   The rent for every month shall be payable on or before'));
        doc.setFontType("bold");
        doc.text(143, 143, doc.splitTextToSize(' '));
        doc.setFontType("normal");
        doc.text(169, 143, doc.splitTextToSize('of the'));
        doc.text(15, 149, doc.splitTextToSize('succeeding month.', (210 - 15 - 15)));
        doc.text(15, 158, doc.splitTextToSize('5.   The lessee shall pay electricity charges to the Electricity Board every month without fail.', (210 - 15 - 15)));
        doc.text(15, 172, doc.splitTextToSize('6.   The lessor or his agent shall have a right to inspect the shop at any hour during the day time.', (210 - 15 - 15)));
        doc.text(15, 187, doc.splitTextToSize('7.   The Lessee shall use the shop let out duly for the business of General Merchandise and not use the same for any other purpose.  (The lessee shall not enter into partnership) and conduct the business in the premises in the name of the firm.  The lessee can only use the premises for his own business.', (210 - 15 - 15)));
        doc.text(15, 214, doc.splitTextToSize('8.    The lessee shall not have any right to assign, sub-let, re-let, under-let or transfer the tenancy or any portion thereof.', (210 - 15 - 15)));
        doc.text(15, 229, doc.splitTextToSize('9.    The lessee shall not carry out any addition or alteration to the shop without the previous consent and approval in writing of the lessor.', (210 - 15 - 15)));
        doc.text(15, 244, doc.splitTextToSize('10.   The lessee on the expiry of the lease period of'));
        doc.setFontType("bold");
        doc.text(128, 244, doc.splitTextToSize(' ' + expiryDate + ' '));
        doc.setFontType("normal");
        doc.text(156, 244, doc.splitTextToSize('months'));
        doc.text(15, 250, doc.splitTextToSize('shall hand over vacant possession of the ceased shop peacefully or the lease agreement can be renewed for a further period on mutually agreed terms.', (210 - 15 - 15)));
       doc.text(15, 266, noticeData.commissionerName?noticeData.commissionerName:"");
       doc.text(160, 266, 'LESSEE');
       doc.text(15, 274, 'Signature:   ');
        doc.text(160, 274, 'Signature:  ');
        doc.setFontType("bold");
        doc.text(15, 282, tenantId.split(".")[1]);

        doc.save('Notice-' + noticeData.agreementNumber + '.pdf');
        var blob = doc.output('blob');
        createFileStore(noticeData, blob).then(createNotice, errorHandler);
    }

    function setFonttype(doc, text){
         doc.setFontType("bold");
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

        if (workflow && workflow.length) {
            workflow = workflow.sort();
            for (var i = 0; i < workflow.length; i++) {
                if(workflow[i].status == "Assistant Approved")
                    rejectedSenderName = workflow[i].senderName;
                $("#historyTable tbody").append(`<tr>
                    <td data-label="createdDate">${workflow[i].createdDate}</td>
                    <td data-label="updatedBy">${workflow[i].senderName}</td>
                    <td data-label="status">${workflow[i].status}</td>
                    <td data-label="comments">${workflow[i].comments}</td>
                    </tr>
                `);
            }
        }

        if(process && process.status && process.status.toUpperCase() == "REJECTED") {
          $('.details-label').remove();
          $('.details-input').show();
          if (rentInc && rentInc.constructor == Array)
              for(var i=0; i<rentInc.length; i++)
                $(`#rentIncrementMethod\\.percentage`).append(`<option value='${rentInc[i]['id']}'>${rentInc[i]['percentage']}</option>`);
        } else {
          $('.details-input').remove();
        }

        if (process && process.attributes && process.attributes.validActions && process.attributes.validActions.values && process.attributes.validActions.values.length) {
            var flg = 0;
            for (var i = 0; i < process.attributes.validActions.values.length; i++) {
                if (process.attributes.validActions.values[i].key)
                    $("#footer-btn-grp").append($(`<button data-action='${process.attributes.validActions.values[i].key}' id=${process.attributes.validActions.values[i].key} type="button" class="btn btn-submit">${process.attributes.validActions.values[i].name}<button/>`));
                if (process.attributes.validActions.values[i].key.toLowerCase() == "print notice") {
                    $(".workFlowDetails").remove();
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

        if (process) {
            getDesignations(process.status, function(designations) {
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
        $(".landAssetDetailsBlock, .marketAssetDetailsBlock, .kalyanamandapamAssetDetailsBlock, .parkingSpaceAssetDetailsBlock, .slaughterHousesAssetDetailsBlock, .usfructsAssetDetailsBlock, .communityAssetDetailsBlock, .fishTankAssetDetailsBlock, .parkAssetDetailsBlock").remove();
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
        e.preventDefault();
        if (!e.target.id) return;
        var data = $("#" + e.target.id).data();
        var _agrmntDet = Object.assign({}, agreementDetail);
        _agrmntDet.workflowDetails = {
            "businessKey": process.businessKey,
            "type": process.businessKey,
            "assignee": $("#approverPositionId") && $("#approverPositionId").val() && (!data.action || (data.action && data.action.toLowerCase() != "reject")) ? getPositionId($("#approverPositionId").val()) : process.initiatorPosition,
            "status": process.status,
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
              //call notice
              printNotice(agreementDetail);
            } else {
                showError(response["statusText"]);
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
