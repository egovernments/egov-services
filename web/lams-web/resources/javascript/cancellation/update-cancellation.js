class UpdateCancellation extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            agreement: {
                id: "",
                tenantId: tenantId,
                agreementNumber: "",
                acknowledgementNumber: "",
                stateId: "",
                action: "cancellation",
                agreementDate: "",
                timePeriod: "",
                allottee: {
                    id: "",
                    name: "",
                    pemovementrmanentAddress: "",
                    mobileNumber: "",
                    aadhaarNumber: "",
                    pan: "",
                    emailId: "",
                    userName: "",
                    password: "",
                    active: "",
                    type: "",
                    gender: "",
                    tenantId: tenantId,
                },
                asset: {
                    id: "",
                    assetCategory: {
                        id: "",
                        name: "",
                        code: ""
                    },
                    name: "",
                    code: "",
                    locationDetails: {
                        locality: "",
                        zone: "",
                        revenueWard: "",
                        block: "",
                        street: "",
                        electionWard: "",
                        doorNo: "",
                        pinCode: ""
                    }
                },
                tenderNumber: "",
                tenderDate: "",
                councilNumber: "",
                councilDate: "",
                bankGuaranteeAmount: "",
                bankGuaranteeDate: "",
                securityDeposit: "",
                collectedSecurityDeposit: "",
                securityDepositDate: "",
                status: "",
                natureOfAllotment: "",
                registrationFee: "",
                caseNo: "",
                commencementDate: "",
                expiryDate: "",
                orderDetails: "",
                rent: "",
                tradelicenseNumber: "",
                paymentCycle: "",
                rentIncrementMethod: {
                    id: "",
                    type: "",
                    assetCategory: "",
                    fromDate: "",
                    toDate: "",
                    percentage: "",
                    flatAmount: "",
                    tenantId: tenantId
                },
                orderNumber: "",
                orderDate: "",
                rrReadingNo: "",
                remarks: "",
                solvencyCertificateNo: "",
                solvencyCertificateDate: "",
                tinNumber: "",
                documents: {},
                demands: [],
                workflowDetails: {
                    department: "",
                    designation: "",
                    nextDesignation:"",
                    assignee: "",
                    action: "",
                    status: "",
                    initiatorPosition: "",
                    comments: ""
                },
                goodWillAmount: "",
                collectedGoodWillAmount: "",
                source: "",
                legacyDemands: "",
                cancellation: {
                    orderNo: "",
                    orderDate: "",
                    terminationDate: "",
                    reasonForCancellation: "",
                },
                rdivenewal: "",
                eviction: "",
                objection: "",
                judgement: "",
                remission: "",
                createdDate: "",
                createdBy: "",
                lastmodifiedDate: "",
                lastmodifiedBy: "",
                isAdvancePaid: "",
                adjustmentStartDate: ""
            },
            cancelReasons: ["Termination", "Cancellation", "Others"],
            positionList: [],
            departmentList: [],
            designationList: [],
            userList: [],
            buttons: [],
            wfStatus: "",
            workflow: [],
            currentUserDesignation:null

        }
        this.handleChangeTwoLevel = this.handleChangeTwoLevel.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleProcess = this.handleProcess.bind(this);
        this.setInitialState = this.setInitialState.bind(this);
        this.setState = this.setState.bind(this);
        this.getUsersFun = this.getUsersFun.bind(this);
        this.printNotice = this.printNotice.bind(this);

    }


    setInitialState(initState) {
        this.setState(initState);
    }


    handleChange(e, name) {

        var _this = this;

        if (name === "documents") {

            var fileTypes = ["application/msword", "application/vnd.openxmlformats-officedocument.wordprocessingml.document", "application/vnd.ms-excel", "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", "application/pdf", "image/png", "image/jpeg"];

            if (e.currentTarget.files.length != 0) {
                for (var i = 0; i < e.currentTarget.files.length; i++) {
                    //2097152 = 2mb
                    if (e.currentTarget.files[i].size > 2097152 && fileTypes.indexOf(e.currentTarget.files[i].type) == -1) {
                        $("#documents").val('');
                        return showError("Maximum file size allowed is 2 MB.\n Please upload only DOC, PDF, xls, xlsx, png, jpeg file.");
                    } else if (e.currentTarget.files[i].size > 2097152) {
                        $("#documents").val('');
                        return showError("Maximum file size allowed is 2 MB.");
                    } else if (fileTypes.indexOf(e.currentTarget.files[i].type) == -1) {
                        $("#documents").val('');
                        return showError("Please upload only DOC, PDF, xls, xlsx, png, jpeg file.");
                    }
                }

                this.setState({
                    agreement: {
                        ...this.state.agreement,
                        documents: e.currentTarget.files
                    }
                })
            } else {
                this.setState({
                    agreement: {
                        ...this.state.agreement,
                        documents: e.currentTarget.files
                    }
                })
            }


        } else {

            _this.setState({
                ..._this.state,
                agreement: {
                    ..._this.state.agreement,
                    [name]: e.target.value
                }
            })
        }
    }


    handleChangeTwoLevel(e, pName, name) {

        var _this = this;

        switch (name) {
            case "department":
                _this.state.agreement.workflowDetails.assignee = "";
                if (this.state.agreement.workflowDetails.designation) {
                    var _designation = this.state.agreement.workflowDetails.designation;
                    if (e.target.value != "" && e.target.value != null)
                        _this.getUsersFun(e.target.value, _designation);
                }
                break;
            case "designation":
                _this.state.agreement.workflowDetails.assignee = "";
                if (this.state.agreement.workflowDetails.department) {
                    var _department = this.state.agreement.workflowDetails.department;
                    if (e.target.value != "" && e.target.value != null)
                        _this.getUsersFun(_department, e.target.value);
                }
                break;

        }


        _this.setState({
            ..._this.state,
            agreement: {
                ..._this.state.agreement,
                [pName]: {
                    ..._this.state.agreement[pName],
                    [name]: e.target.value
                }
            }
        })

    }


    getUsersFun(departmentId, designationId) {
        var _this = this;
        var date = moment(new Date()).format("DD/MM/YYYY");
        $.ajax({
            url: baseUrl + "/hr-employee/employees/_search?tenantId=" + tenantId + "&departmentId=" + departmentId + "&designationId=" + designationId + "&active=true&asOnDate=" + date,
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify({ RequestInfo: requestInfo }),
            contentType: 'application/json',
            headers: {
                'auth-token': authToken
            },
            success: function (res) {

                _this.setState({
                    ..._this.state,
                    userList: res.Employee
                })

            },
            error: function (err) {
            }

        })

    }


    componentWillMount() {

        if (window.opener && window.opener.document) {
            var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
            if (logo_ele && logo_ele[0]) {
                document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
            }
        }
        $('#lams-title').text("Cancellation Of Agreement");
        var _this = this;

        try {
            var departmentList = !localStorage.getItem("assignments_department") || localStorage.getItem("assignments_department") == "undefined" ? (localStorage.setItem("assignments_department", JSON.stringify(getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"] || [])), JSON.parse(localStorage.getItem("assignments_department"))) : JSON.parse(localStorage.getItem("assignments_department"));
        } catch (e) {
            console.log(e);
            var department = [];
        }


        var stateId = getUrlVars()["state"];
        var process = commonApiPost("egov-common-workflows", "process", "_search", {
            tenantId: tenantId,
            id: stateId
        }).responseJSON["processInstance"] || {};

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
     if(currOwnerPosition && currentUserDesignation==null){

       var designation = getCommonMasterById("hr-masters", "designations", null,currOwnerPosition).responseJSON["Designation"];
       currentUserDesignation = designation[0].name;
     }
     console.log("loggedinuser",currentUserDesignation);
       var wfState = process.status;
        var currStatus = null;
        if (wfState.includes('Commissioner Approved')) {
            currStatus = 'INACTIVE';
        }

        var agreement = commonApiPost("lams-services",
            "agreements",
            "_search",
            {
                stateId: stateId,
                status: currStatus,
                action: "View",
                tenantId
            }).responseJSON["Agreements"][0] || {};

        var workflow = commonApiPost("egov-common-workflows", "history", "", {
            tenantId: tenantId,
            workflowId: stateId
        }).responseJSON["tasks"] || {};



        if (workflow) {
            workflow.sort((record1, record2) => record1.lastupdatedSince > record2.lastupdatedSince);
            workflow.forEach(function (item, index, theArray) {

                var employeeName = commonApiPost("hr-employee", "employees", "_search", {
                    tenantId: tenantId,
                    positionId: item.owner.id
                }).responseJSON["Employee"] || {};

                theArray[index].employeeName = employeeName[0] ? employeeName[0].code + " :: " + employeeName[0].name : "";
            });

        }

        if (process) {
            if (process && process.attributes && process.attributes.validActions && process.attributes.validActions.values && process.attributes.validActions.values.length) {
                var _btns = [];
                for (var i = 0; i < process.attributes.validActions.values.length; i++) {
                    if (process.attributes.validActions.values[i].key) {
                        _btns.push({
                            key: process.attributes.validActions.values[i].key,
                            name: process.attributes.validActions.values[i].name
                        });
                    }
                }
            }
        }
        if(currentUserDesignation==='Junior Assistant' || currentUserDesignation==='Senior Assistant'){
          currentUserDesignation ="Assistant";
        }
        console.log("loggedinuser",currentUserDesignation);
        getDesignations(process.status, currentUserDesignation, function (designations) {
            //console.log(designations);
            _this.setState({
                ..._this.state,
                designationList: designations
            });

        }, process.businessKey);



        if (!agreement.cancellation) {
            agreement.cancellation = {};
        }
        if (!agreement.workflowDetails) {
            agreement.workflowDetails = {};
        }


        this.setState({
            ...this.state,
            agreement: agreement,
            departmentList: departmentList,
            initiatorPosition: process.initiatorPosition,
            wfStatus: process.status,
            workflow: workflow,
            buttons: _btns ? _btns : [],
            currentUserDesignation:currentUserDesignation
        });

    }


    printNotice(agreement) {
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
        var municipalact = "AP Municipal Act 1965";
        if (cityGrade.toLowerCase() === 'corp') {
            ulbType = "Municipal Corporation";
            var municipalact = "AP Municipal Corporations Act 1994";
        }

        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1;
        var yyyy = today.getFullYear();
        if (dd < 10) {
            dd = '0' + dd;
        }
        if (mm < 10) {
            mm = '0' + mm;
        }
        var today = dd + '/' + mm + '/' + yyyy;

        var columns = [
            { title: "Reason for cancellation", dataKey: "reason" },
            { title: "Tick as applicable", dataKey: "tick" },
        ];
        var rows = [
            { "reason": "Nonpayment of rentals by due date", "tick": "" },
            { "reason": "Subletting premises", "tick": "" },
            { "reason": "Lease term expired and not renewed", "tick": "" },
            { "reason": "Altering premises without permission", "tick": "" },
            { "reason": "License for business is not in the name of leaseholder", "tick": "" },
            { "reason": "Using the premises for unlawful activities", "tick": "" },
            { "reason": "Others ", "tick": "" },

        ];

        var autoTableOptions = {
            tableLineColor: [0, 0, 0],
            tableLineWidth: 0.2,
            styles: {
                lineColor: [0, 0, 0],
                lineWidth: 0.2
            },
            headerStyles: {
                textColor: [0, 0, 0],
                fillColor: [255, 255, 255]
            },
            bodyStyles: {
                fillColor: [255, 255, 255],
                textColor: [0, 0, 0]
            },
            alternateRowStyles: {
                fillColor: [255, 255, 255]
            }, startY: 150
        };

        var doc = new jsPDF();

        doc.setFontType("bold");
        doc.setFontSize(13);
        doc.text(105, 20, "PROCEEDINGS OF THE COMMISSIONER, " + tenantId.split(".")[1].toUpperCase(), 'center');
        doc.text(105, 27, ulbType.toUpperCase(), 'center');
        doc.text(105, 34, "Present: " + commissionerName, 'center');

        doc.setFontType("normal");
        doc.setFontSize(11);
        doc.fromHTML( 'Roc.No. <b>' + agreement.noticeNumber + '</b>', 15, 50);
        doc.fromHTML('Dt. <b>' + agreement.agreementDate + '</b>', 140, 50);

        var paragraph = "Sub: Leases – Revenue Section – Shop No <b>" + agreement.referenceNumber + "</b> in <b>" + agreement.asset.name + "<b> Complex, <b>" + locality + "<b> - Lease cancellation – Orders  - Issued";
        var lines = doc.splitTextToSize(paragraph, 180);
        lines.forEach((element,index) => {
            doc.fromHTML(element.trim(),15, 60+(index*5));
         });

         doc.fromHTML("Ref: 1. Lease agreement No <b>" + agreement.agreementNumber + "</b> dt <b>" + agreement.agreementDate +"</b>", 15, 75);
         doc.fromHTML("2. Roc No <b>" + agreement.noticeNumber + "</b> dt <b>" + agreement.agreementDate + "</b> of this office", 24, 80);
         doc.fromHTML("3. Resolution No <b>" + agreement.cancellation.orderNo + "</b> dt <b>" + agreement.cancellation.orderDate + "</b> of Municipal Council/Standing Committee", 24, 85);
         doc.text(105, 100, "><><><", 'center');

         doc.text(15, 110, "Orders:");
         doc.setLineWidth(0.5);
         doc.line(15, 111, 30, 111);

         var paragraph1 = "In the reference 1st cited, an agreement was concluded with you for leasing Shop No <b>" + agreement.referenceNumber + "</b> in the <b>" + agreement.asset.name + "</b> Shopping Complex and the lease is subject to the terms and conditions prescribed therein. Due to non observance of the same, your lease for the said shop is being cancelled. (Reasons as cited below).";
         var lines = doc.splitTextToSize(paragraph1, 180);
         lines.forEach((element,index) => {
             doc.fromHTML(element,15, 120+(index*5));
         });
         //doc.text(15, 120, lines);

         doc.autoTable(columns, rows, autoTableOptions);

         var paragraph2 = "In pursuance of the same you are hereby instructed to clear all the dues and vacate the premises with immediate effect and handover the premises to the Municipal officials as it is, failing which civil and criminal action will be initiated under relevant provisions of <b>" + municipalact + "</b> and rules issued there upon. No correspondence will be entertained in this ";
         var lines = doc.splitTextToSize(paragraph2, 180);
         lines.forEach((element,index) => {
             doc.fromHTML(element,15, 220+(index*5));
         });

         doc.text(120, 250, "Commissioner");
         doc.text(120, 255, tenantId.split(".")[1].charAt(0).toUpperCase() + tenantId.split(".")[1].slice(1) + ",");
         doc.text(120, 260, ulbType);

         doc.text(15, 265, "To");
         doc.text(15, 270, "The Leaseholder");
         doc.text(15, 275, "Copy to the concerned officials for necessary action");

         doc.save('CancellationNotice-' + agreement.agreementNumber + '.pdf');
         var blob = doc.output('blob');

        this.createFileStore(agreement, blob).then(this.createNotice, this.errorHandler);
    }

    createFileStore(noticeData, blob) {
        // console.log('upload to filestore');
        var promiseObj = new Promise(function (resolve, reject) {
            let formData = new FormData();
            formData.append("jurisdictionId", tenantId);
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
                    let obj = {
                        noticeData: noticeData,
                        fileStoreId: res.files[0].fileStoreId
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

    createNotice(obj) {
        // console.log('notice create');
        $.ajax({
            url: baseUrl + `/lams-services/agreement/notice/_create?tenantId=` + tenantId,
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify({
                RequestInfo: requestInfo,
                Notice: {
                    tenantId,
                    agreementNumber: obj.noticeData.agreementNumber,
                    fileStore: obj.fileStoreId,
                    acknowledgementNumber: obj.noticeData.acknowledgementNumber,
                    status: "INACTIVE"
                }
            }),
            headers: {
                'auth-token': authToken
            },
            contentType: 'application/json',
            success: function (res) {
                // console.log('notice created');
                if (window.opener)
                    window.opener.location.reload();
                open(location, '_self').close();
            },
            error: function (jqXHR, exception) {
                console.log('error');
                showError('Error while creating notice');
            }
        });

    }

    errorHandler(statusCode) {
        console.log("failed with status", status);
        showError('Error');
    }

    componentDidMount() {

        var _this = this;

        if (this.state.wfStatus === "Rejected") {

            $("#orderNo").prop("disabled", false)
            $("#orderDate").prop("disabled", false)
            $("#terminationDate").prop("disabled", false)
            $("#reasonForCancellation").prop("disabled", false)
            $("#documents").prop("disabled", false)
            $("#remarks").prop("disabled", false)
        } else {
            $("#documentSection").remove();
        }

        if (this.state.wfStatus.includes("Commissioner Approved")) {
            $("#approvalCommentsSection").remove();
        }
        if(this.state.currentUserDesignation && this.state.currentUserDesignation==='Commissioner'){
          $("#approvalDetailsSection").remove();

        }

        $('#orderDate').datepicker({
            format: 'dd/mm/yyyy',
            autoclose: true,
            defaultDate: ""
        });

        $('#orderDate').on('change blur', function (e) {
            _this.setState({
                agreement: {
                    ..._this.state.agreement,
                    cancellation: {
                        ..._this.state.agreement.cancellation,
                        "orderDate": $("#orderDate").val()
                    }
                }
            });
        });


        $('#terminationDate').datepicker({
            format: 'dd/mm/yyyy',
            startDate: new Date(),
            autoclose: true,
            defaultDate: ""
        });

        $('#terminationDate').on('change blur', function (e) {
            _this.setState({
                agreement: {
                    ..._this.state.agreement,
                    cancellation: {
                        ..._this.state.agreement.cancellation,
                        "terminationDate": $("#terminationDate").val()
                    }
                }
            });
        });
    }

    makeAjaxUpload(file, cb) {
        if (file.constructor == File) {
            let formData = new FormData();
            formData.append("jurisdictionId", tenantId);
            formData.append("module", "LAMS");
            formData.append("file", file);
            $.ajax({
                url: baseUrl + "/filestore/v1/files?tenantId=" + tenantId,
                data: formData,
                cache: false,
                contentType: false,
                processData: false,
                type: 'POST',
                success: function (res) {
                    cb(null, res);
                },
                error: function (jqXHR, exception) {
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

    close() {
        // widow.close();
        open(location, '_self').close();
    }

    handleProcess(e) {

        e.preventDefault();

        if ($('#update-cancellation').valid()) {
            var buttonAction = e.target.id;
            var _this = this;
            var agreement = Object.assign({}, _this.state.agreement);

            if(agreement.cancellation.reasonForCancellation == "Others" && !(agreement.remarks))
            return(showError("Please enter remarks when selected reason as others"));        

            agreement.action = "cancellation";
            agreement.workflowDetails.action = buttonAction;
            agreement.workflowDetails.status = this.state.wfStatus;
            agreement.workflowDetails.nextDesignation = getNameById(this.state.designationList ,agreement.workflowDetails.designation);
            agreement.workflowDetails.designation = this.state.currentUserDesignation;

            var asOnDate = new Date();
            var dd = asOnDate.getDate();
            var mm = asOnDate.getMonth() + 1;
            var yyyy = asOnDate.getFullYear();

            if (dd < 10) {
                dd = '0' + dd
            }

            if (mm < 10) {
                mm = '0' + mm
            }

            asOnDate = dd + '/' + mm + '/' + yyyy;


            if (buttonAction === "Reject") {

                if (!agreement.workflowDetails.comments)
                    return showError("Please enter the Comments, If you are rejecting");

            }

            if (buttonAction === "Forward") {

                if (!agreement.workflowDetails.department)
                    return showError("Please Select the Department");
                else if (!agreement.workflowDetails.designation)
                    return showError("Please Select the Designation");
                else if (!agreement.workflowDetails.assignee)
                    return showError("Please Select the Employee");

            }

            if (!agreement.workflowDetails.assignee) {
                agreement.workflowDetails.assignee = this.state.initiatorPosition;
            }

            if(buttonAction.toLowerCase()==="approve" || buttonAction.toLowerCase()==="reject"){
              agreement.workflowDetails.assignee = this.state.initiatorPosition;
            }

            // console.log("Agreement", agreement);

            // With file upload
            if (agreement.documents && agreement.documents.constructor == FileList) {

                //console.log('update cancellation with file upload');

                let counter = agreement.documents.length,
                    breakout = 0,
                    docs = [];
                for (let i = 0, len = agreement.documents.length; i < len; i++) {
                    this.makeAjaxUpload(agreement.documents[i], function (err, res) {
                        if (breakout == 1) {
                            //console.log("breakout", breakout);
                            return;
                        } else if (err) {
                            showError("Error uploding the files. Please contact Administrator");
                            breakout = 1;
                        } else {
                            counter--;
                            docs.push({ fileStore: res.files[0].fileStoreId });
                            //console.log("docs", docs);
                            if (counter == 0 && breakout == 0) {
                                agreement.documents = docs;

                                var body = {
                                    "RequestInfo": requestInfo,
                                    "Agreement": agreement
                                };

                                $.ajax({
                                    url: baseUrl + "/lams-services/agreements/cancel/_update?tenantId=" + tenantId,
                                    type: 'POST',
                                    dataType: 'json',
                                    data: JSON.stringify(body),
                                    contentType: 'application/json',
                                    headers: {
                                        'auth-token': authToken
                                    },
                                    success: function (res) {

                                        agreement.acknowledgementNumber = res.Agreements[0].acknowledgementNumber;

                                        if (buttonAction === "Print Notice") {
                                            _this.state.workflow.forEach(function(item){
                                                if(item.status === "")
                                                agreement.commissionerName = item.senderName
                                              });
                                            _this.printNotice(agreement);
                                        } else {
                                            $.ajax({
                                                url: baseUrl + "/hr-employee/employees/_search?tenantId=" + tenantId + "&positionId=" + agreement.workflowDetails.assignee + "&asOnDate=" + asOnDate,
                                                type: 'POST',
                                                dataType: 'json',
                                                data: JSON.stringify({ RequestInfo: requestInfo }),
                                                contentType: 'application/json',
                                                headers: {
                                                    'auth-token': authToken
                                                },
                                                success: function (res1) {
                                                    console.log("res1", res1);
                                                    if (window.opener)
                                                        window.opener.location.reload();
                                                    if (buttonAction.toLowerCase() === 'cancel') {
                                                        window.location.href = `app/acknowledgement/common-ack.html?wftype=Cancel&action=${buttonAction}&ackNo=${res.Agreements[0].acknowledgementNumber}`;
                                                    } else {
                                                        if (res1 && res1.Employee.length > 0)
                                                            window.location.href = `app/acknowledgement/common-ack.html?wftype=Cancel&action=${buttonAction}&name=${res1.Employee[0].code}::${res1.Employee[0].name}&ackNo=${res.Agreements[0].acknowledgementNumber}`;
                                                        else
                                                            window.location.href = `app/acknowledgement/common-ack.html?wftype=Cancel&action=${buttonAction}&name=&ackNo=${res.Agreements[0].acknowledgementNumber}`;
                                                    }
                                                },
                                                error: function (err) {
                                                    if (window.opener)
                                                        window.opener.location.reload();
                                                    window.location.href = `app/acknowledgement/common-ack.html?wftype=Cancel&action=${buttonAction}&name=&ackNo=${res.Agreements[0].acknowledgementNumber}`;
                                                }
                                            })
                                        }
                                    },
                                    error: function (err) {
                                        if (err && err.responseJSON && err.responseJSON.Error && err.responseJSON.Error.message)
                                            showError(err.responseJSON.Error.message);
                                        else
                                            showError("Something went wrong. Please contact Administrator");
                                    }

                                })

                            }
                        }
                    })
                }
                // if (breakout == 1)
                //     return;
            } else {
                //No file upload
                var body = {
                    "RequestInfo": requestInfo,
                    "Agreement": agreement
                };

                // console.log('update cancellation without file upload');

                // console.log(ID);

                $.ajax({
                    url: baseUrl + "/lams-services/agreements/cancel/_update?tenantId=" + tenantId,
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify(body),
                    contentType: 'application/json',
                    headers: {
                        'auth-token': authToken
                    },
                    success: function (res) {

                        agreement.acknowledgementNumber = res.Agreements[0].acknowledgementNumber;

                        if (buttonAction === "Print Notice") {
                            _this.state.workflow.forEach(function(item){
                                if(item.status.trim() === "Commissioner Approved")
                                agreement.commissionerName = item.senderName
                              });
                            _this.printNotice(agreement);
                        } else {
                            $.ajax({
                                url: baseUrl + "/hr-employee/employees/_search?tenantId=" + tenantId + "&positionId=" + agreement.workflowDetails.assignee + "&asOnDate=" + asOnDate,
                                type: 'POST',
                                dataType: 'json',
                                data: JSON.stringify({ RequestInfo: requestInfo }),
                                contentType: 'application/json',
                                headers: {
                                    'auth-token': authToken
                                },
                                success: function (res1) {
                                    console.log("res1", res1);
                                    if (window.opener)
                                        window.opener.location.reload();
                                    if (buttonAction.toLowerCase() === 'cancel') {
                                        window.location.href = `app/acknowledgement/common-ack.html?wftype=Cancel&action=${buttonAction}&ackNo=${res.Agreements[0].acknowledgementNumber}`;
                                    } else {
                                        if (res1 && res1.Employee.length > 0)
                                            window.location.href = `app/acknowledgement/common-ack.html?wftype=Cancel&action=${buttonAction}&name=${res1.Employee[0].code}::${res1.Employee[0].name}&ackNo=${res.Agreements[0].acknowledgementNumber}`;
                                        else
                                            window.location.href = `app/acknowledgement/common-ack.html?wftype=Cancel&action=${buttonAction}&name=&ackNo=${res.Agreements[0].acknowledgementNumber}`;
                                    }
                                },
                                error: function (err) {
                                    if (window.opener)
                                        window.opener.location.reload();
                                    window.location.href = `app/acknowledgement/common-ack.html?wftype=Cancel&action=${buttonAction}&name=&ackNo=${res.Agreements[0].acknowledgementNumber}`;
                                }
                            })
                        }
                    },
                    error: function (err) {
                        if (err.responseJSON.Error && err.responseJSON.Error.message)
                            showError(err.responseJSON.Error.message);
                        else
                            showError("Something went wrong. Please contact Administrator");
                    }

                })

            }


        } else {
            showError("Please fill all required feilds");
        }


    }


    render() {
        var _this = this;
        let { handleChange, handleChangeTwoLevel, addOrUpdate, printNotice, handleProcess } = this;
        let { agreement, cancelReasons, buttons } = this.state;
        let { allottee, asset, rentIncrementMethod, workflowDetails, cancellation,
            renewal, eviction, objection, judgement, remission, remarks, documents } = this.state.agreement;
        let { assetCategory, locationDetails } = this.state.agreement.asset;

        const renderOption = function (data) {
            if (data) {
                return data.map((item, ind) => {
                    return (<option key={ind} value={typeof item == "object" ? item.id : item}>
                        {typeof item == "object" ? item.name : item}
                    </option>)
                })
            }
        }

        const renderProcesedBtns = function () {
            if (buttons.length) {
                return buttons.map(function (btn, ind) {
                    return (<span key={ind}> <button key={ind} id={btn.key} type='button' className='btn btn-submit' onClick={(e) => { handleProcess(e) }} >
                        {btn.name}
                    </button> &nbsp; </span>)
                })
            }
        }

        const renderAssetDetails = function () {
            return (
                <div className="form-section" id="assetDetailsBlock">
                    <h3>Asset Details </h3>
                    <div className="form-section-inner">
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="aName">Asset Name :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="code" name="code">
                                            {asset.name ? asset.name : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="code">Asset Code:</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="code" name="code">
                                            {asset.code ? asset.code : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="categoryType">Asset Category Type :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="assetCategoryType" name="assetCategoryType">
                                            {assetCategory.name ? assetCategory.name : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="assetArea">Asset Area :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="assetArea" name="assetArea" >
                                            {asset.totalArea ? asset.totalArea : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="locationDetails.locality">Locality :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="locationDetails.locality" name="locationDetails.locality">
                                            {locationDetails.locality ? locationDetails.locality : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="locationDetails.revenueWard">Revenue Ward :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="locationDetails.revenueWard" name="locationDetails.revenueWard">
                                            {locationDetails.revenueWard ? locationDetails.revenueWard : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="block">Block :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="Block" name="Block">
                                            {locationDetails.block ? locationDetails.block : "N/A"}
                                        </label>
                                    </div>

                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="locationDetails.zone">Revenue Zone :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="locationDetails.zone" name="locationDetails.zone">
                                            {locationDetails.zone ? locationDetails.zone : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>);
        }

        const renderOptionForUser = function (list) {
            if (list) {
                return list.map((item, ind) => {
                    return (<option key={ind} value={item.assignments[0].position}>
                        {item.name}
                    </option>)
                })
            }
        }

        const renderAllottee = function () {
            return (
                <div className="form-section" id="allotteeDetailsBlock">
                    <h3>Allottee Details </h3>
                    <div className="form-section-inner">
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="allotteeName"> Name :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="allotteeName" name="allotteeName">
                                            {allottee.name ? allottee.name : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="mobileNumber">Mobile Number:</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="mobileNumber" name="mobileNumber">
                                            {allottee.mobileNumber ? maskAlloteeDetails(allottee.mobileNumber) : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="aadhaarNumber">AadhaarNumber :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="aadhaarNumber" name="aadhaarNumber">
                                            {allottee.aadhaarNumber ? maskAlloteeDetails(allottee.aadhaarNumber) : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="panNo">PAN No:</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="panNo" name="panNo" >
                                            {allottee.pan ? allottee.pan : "N/A"}   </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="emailId">EmailId :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="emailId" name="emailId">
                                            {allottee.emailId ? allottee.emailId : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="address">Address :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="address" name="address">
                                            {allottee.permanentAddress ? allottee.permanentAddress : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            );
        }

        const renderAgreementDetails = function () {
            return (
                <div className="form-section" id="agreementDetailsBlock">
                    <h3>Agreement Details </h3>
                    <div className="form-section-inner">
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="agreementNumber"> Agreement Number :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="agreementNumber" name="agreementNumber">
                                            {agreement.agreementNumber ? agreement.agreementNumber : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="agreementDate">Agreement Date:</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="agreementDate" name="agreementDate">
                                            {agreement.agreementDate ? agreement.agreementDate : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="rent">Rent :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="rent" name="rent">
                                            {agreement.rent ? agreement.rent : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="securityDeposit">Advance Collection:</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="securityDeposit" name="securityDeposit">
                                            {agreement.securityDeposit ? agreement.securityDeposit : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="paymentCycle">PaymentCycle :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="paymentCycle" name="paymentCycle">
                                            {agreement.paymentCycle ? agreement.paymentCycle : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="natureOfAllotment">Allotment Type :</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <label id="natureOfAllotment" name="natureOfAllotment">
                                            {agreement.natureOfAllotment ? agreement.natureOfAllotment : "N/A"}
                                        </label>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            );

        }

        const renederCancelDetails = function () {
            return (
                <div className="form-section hide-sec" id="agreementCancelDetails">
                    <h3 className="categoryType">Cancellation Details </h3>
                    <div className="form-section-inner">
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="orderNo">Council/standing committee Resolution Number<span>*</span> </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <input type="text" name="orderNo" id="orderNo" value={cancellation.orderNo}
                                            onChange={(e) => { handleChangeTwoLevel(e, "cancellation", "orderNo") }} required disabled />
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="orderDate">Council/standing committee Resolution Date<span>*</span> </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="text-no-ui">
                                            <span className="glyphicon glyphicon-calendar"></span>
                                            <input type="text" id="orderDate" name="orderDate" value="orderDate" value={cancellation.orderDate}
                                                onChange={(e) => { handleChangeTwoLevel(e, "cancellation", "orderDate") }} required disabled />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="terminationDate">Termination Date<span>*</span> </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="text-no-ui">
                                            <span className="glyphicon glyphicon-calendar"></span>
                                            <input type="text" id="terminationDate" name="terminationDate" value="terminationDate" value={cancellation.terminationDate}
                                                onChange={(e) => { handleChangeTwoLevel(e, "cancellation", "terminationDate") }} required disabled />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="reasonForCancellation">Reason For Cancellation
                                    <span>*</span>
                                        </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="styled-select">
                                            <select name="reasonForCancellation" id="reasonForCancellation" value={cancellation.reasonForCancellation}
                                                onChange={(e) => { handleChangeTwoLevel(e, "cancellation", "reasonForCancellation") }} required disabled >
                                                <option value="">Select Reason</option>
                                                {renderOption(cancelReasons)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row" id="documentSection">
                                    <div className="col-sm-6 label-text">
                                        <label>Attach Document </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="styled-file">
                                            <input id="documents" name="documents" type="file" onChange={(e) => { handleChange(e, "documents") }} multiple disabled />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="remarks">Remarks </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <textarea rows="4" cols="50" id="remarks" name="remarks" value={remarks}
                                            onChange={(e) => { handleChange(e, "remarks") }} disabled ></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            );
        }
        const renderDocuments = function () {
            return (
                <div className="form-section" id="documentsBlock">
                    <h3 className="categoryType">Attached Documents </h3>
                    <div className="form-section-inner">

                        <table id="documentsTable" className="table table-bordered">
                            <thead>
                                <tr>
                                    <th>S.No</th>
                                    <th>Document Name</th>
                                    <th>Action</th>
                                </tr>
                            </thead>
                            <tbody id="documentsTableBody">
                                {
                                    renderDocumentsList()
                                }
                            </tbody>
                        </table>
                    </div>
                </div>
            )
        }
        const renderDocumentsList = function () {
            if (documents && documents.length > 0) {
                var CONST_API_GET_FILE = "/filestore/v1/files/id?tenantId=" + tenantId + "&fileStoreId=";
                return documents.map((item, index) => {
                    return (<tr key={index}>
                        <td>{index + 1}.</td>
                        <td>{item.fileName || 'N/A'}</td>
                        <td>  <a href={window.location.origin + CONST_API_GET_FILE + item.fileStore} target="_self">
                            Download
                                           </a>
                        </td>
                    </tr>
                    );

                })
            } else {
                return (<tr><td></td>
                    <td>No Documents</td>
                    <td></td>
                </tr>)
            }
        }

        const renderWorkflowHistory = function () {
            return (
                <div className="form-section" id="historyDetails">
                    <h3 className="categoryType">Workflow History </h3>
                    <div className="form-section-inner">
                        <table id="historyTable" className="table table-bordered">
                            <thead>
                                <tr>
                                    <th>Date</th>
                                    <th>Updated By</th>
                                    <th>Current Owner</th>
                                    <th>Status</th>
                                    <th>Comments</th>
                                </tr>
                            </thead>
                            <tbody id="historyTableBody">
                                {
                                    renderTr()
                                }
                            </tbody>
                        </table>
                    </div>
                </div>
            );
        }

        const renderTr = () => {
            return this.state.workflow.map((item, ind) => {

                return (
                    <tr key={ind}>
                        <td>{item.lastupdatedSince}</td>
                        <td>{item.senderName}</td>
                        <td>{item.employeeName}</td>
                        <td>{item.status}</td>
                        <td>{item.comments}</td>
                    </tr>
                )
            })
        }

        const renderWorkFlowDetails = function () {
            var flg = 0;
            let buttonsLowercase = [];

            buttons.forEach(function(button){
                buttonsLowercase.push(button.key.toLowerCase());
            })
            if(buttonsLowercase.indexOf("forward") < 0 && (buttonsLowercase.indexOf("approve") > -1 || buttonsLowercase.indexOf("print notice") > -1))
            flg = 1;

            if (flg === 0) {

                return (
                    <div className="form-section" id="approvalDetailsSection">
                        <div className="row">
                            <div className="col-md-8 col-sm-8">
                                <h3 className="categoryType">Approval Details </h3>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="">Department <span>*</span></label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="styled-select">
                                            <select id="department" name="department" value={workflowDetails.department}
                                                onChange={(e) => { handleChangeTwoLevel(e, "workflowDetails", "department") }} >
                                                <option value="">Select Department</option>
                                                {renderOption(_this.state.departmentList)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="">Designation <span>*</span></label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="styled-select">
                                            <select id="designation" name="designation" value={workflowDetails.designation}
                                                onChange={(e) => { handleChangeTwoLevel(e, "workflowDetails", "designation") }} >
                                                <option value="">Select Designation</option>
                                                {renderOption(_this.state.designationList)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="">Employee <span>*</span></label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="styled-select">
                                            <select id="assignee" name="assignee" value={workflowDetails.assignee}
                                                onChange={(e) => { handleChangeTwoLevel(e, "workflowDetails", "assignee") }} >
                                                <option value="">Select User</option>
                                                {renderOptionForUser(_this.state.userList)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="comments">Comments </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <textarea rows="4" cols="50" id="comments" name="comments" value={workflowDetails.comments}
                                            onChange={(e) => { handleChangeTwoLevel(e, "workflowDetails", "comments") }} ></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                );
            } else {
                return (
                    <div className="form-section" id="approvalCommentsSection">
                        <div className="row">
                            <div className="col-md-8 col-sm-8">
                                <h3 className="categoryType">Approval Details </h3>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="comments">Comments </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <textarea rows="4" cols="50" id="comments" name="comments" value={workflowDetails.comments}
                                            onChange={(e) => { handleChangeTwoLevel(e, "workflowDetails", "comments") }} ></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                );
            }

        }

        return (
            <div>
                <h3>Cancellation Of Agreement </h3>
                <form className="update-cancellation" id="update-cancellation" >
                    <fieldset>
                        {renderAssetDetails()}
                        {renderAllottee()}
                        {renderAgreementDetails()}
                        {renederCancelDetails()}
                        {renderDocuments()}
                        {renderWorkflowHistory()}
                        {renderWorkFlowDetails()}

                        <br />
                        <div className="text-center">
                            {renderProcesedBtns()}
                            <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>
                        </div>

                    </fieldset>
                </form>
            </div>
        );
    }
}


ReactDOM.render(
    <UpdateCancellation />,
    document.getElementById('root')
);
