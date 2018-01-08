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
                documents: "",
                demands: [],
                workflowDetails: {
                    department: "",
                    designation: "",
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
            cancelReasons: ["Termination", "Cancellation"],
            positionList: [],
            departmentList: [],
            designationList: [],
            userList: [],
            buttons: [],
            wfStatus: "",
            workflow: []

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
        $.ajax({
            url: baseUrl + "/hr-employee/employees/_search?tenantId=" + tenantId + "&departmentId=" + departmentId + "&designationId=" + designationId,
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

        var currStatus = null;
        if(process.status ==='Commissioner Approved'){
          currStatus='INACTIVE';
        }

        var agreement = commonApiPost("lams-services",
            "agreements",
            "_search",
            {
                stateId: stateId,
                status: currStatus,
                tenantId
            }).responseJSON["Agreements"][0] || {};

        var workflow = commonApiPost("egov-common-workflows", "history", "", {
            tenantId: tenantId,
            workflowId: stateId
        }).responseJSON["tasks"] || {};


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

        getDesignations(process.status, function (designations) {
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
            buttons: _btns ? _btns : []
        });

    }


    printNotice(noticeData) {
        var commencementDate = noticeData.commencementDate;
        var expiryDate = noticeData.expiryDate;
        // var rentPayableDate = noticeData.rentPayableDate;
        var doc = new jsPDF();

        doc.setFontSize(14);
        doc.setFontType("bold");
        doc.text(105, 20, tenantId.split(".")[1], 'center');
        doc.text(105, 27, tenantId.split(".")[1] + ' District', 'center');
        doc.text(105, 34, 'Asset Category Lease/Agreement Notice', 'center');
        doc.setLineWidth(0.5);
        doc.line(15, 38, 195, 38);
        doc.text(15, 47, 'Lease details: ');
        doc.text(110, 47, 'Agreement No: ' + noticeData.agreementNumber);
        doc.text(15, 57, 'Lease Name: ' + noticeData.allottee.name);
        doc.text(110, 57, 'Asset No: ' + noticeData.asset.code);
        doc.text(15, 67, (noticeData.allottee.mobileNumber ? noticeData.allottee.mobileNumber + ", " : "") + (noticeData.doorNo ? noticeData.doorNo + ", " : "") + (noticeData.allottee.permanentAddress ? noticeData.allottee.permanentAddress.replace(/(\r\n|\n|\r)/gm, "") + ", " : "") + tenantId.split(".")[1] + ".");

        doc.setFontType("normal");
        doc.text(15, 77, doc.splitTextToSize('1.    The period of lease shall be '));
        doc.setFontType("bold");
        doc.text(85, 77, doc.splitTextToSize(' ' + noticeData.timePeriod * 12 + ' '));
        doc.setFontType("normal");
        doc.text(93, 77, doc.splitTextToSize('months commencing from'));
        doc.setFontType("bold");
        doc.text(15, 83, doc.splitTextToSize(' ' + commencementDate + ' '));
        doc.setFontType("normal");
        doc.text(42, 83, doc.splitTextToSize('(dd/mm/yyyy) to'));
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
        doc.text(143, 143, doc.splitTextToSize(''));
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
        doc.text(15, 266, noticeData.commissionerName ? noticeData.commissionerName : "");
        doc.text(160, 266, 'LESSEE');
        doc.text(15, 274, 'Signature:   ');
        doc.text(160, 274, 'Signature:  ');
        doc.setFontType("bold");
        doc.text(15, 282, tenantId.split(".")[1]);
        doc.save('Notice-' + noticeData.agreementNumber + '.pdf');
        var blob = doc.output('blob');
        this.createFileStore(noticeData, blob).then(this.createNotice, this.errorHandler);
    }

    createFileStore(noticeData, blob) {
        // console.log('upload to filestore');
        var promiseObj = new Promise(function (resolve, reject) {
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
                    fileStore: obj.fileStoreId
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
        }

        $('#orderDate').datepicker({
            format: 'dd/mm/yyyy',
            autoclose: true,
            defaultDate: ""
        });

        $('#orderDate').on('changeDate', function (e) {
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

        $('#terminationDate').on('changeDate', function (e) {
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
            var ID = e.target.id;
            var _this = this;
            var agreement = Object.assign({}, _this.state.agreement);

            agreement.action = "cancellation";
            agreement.workflowDetails.action = ID;
            agreement.workflowDetails.status = this.state.wfStatus;

            // console.log(ID, agreement);


            if (ID === "Reject") {

                if (agreement.workflowDetails.comments || agreement.workflowDetails.comments === "")
                    return showError("Please enter the Comments, If you are rejecting");

            }

            if (!agreement.workflowDetails.assignee) {
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

                                        if (ID === "Print Notice") {
                                            _this.printNotice(agreement);
                                        } else {
                                            $.ajax({
                                                url: baseUrl + "/hr-employee/employees/_search?tenantId=" + tenantId + "&positionId=" + agreement.workflowDetails.assignee,
                                                type: 'POST',
                                                dataType: 'json',
                                                contentType: 'application/json',
                                                headers: {
                                                    'auth-token': authToken
                                                },
                                                success: function (res1) {
                                                    if (window.opener)
                                                        window.opener.location.reload();
                                                    if (res1 && res1.Employee && res1.Employee[0].name)
                                                        window.location.href = `app/acknowledgement/common-ack.html?wftype=Cancel&action=forward&name=${res1.Employee[0].name}&ackNo=${res.Agreements[0].acknowledgementNumber}`;
                                                    else
                                                        window.location.href = `app/acknowledgement/common-ack.html?wftype=Cancel&action=forward&name=&ackNo=${res.Agreements[0].acknowledgementNumber}`;

                                                },
                                                error: function (err) {
                                                    if (window.opener)
                                                        window.opener.location.reload();
                                                    window.location.href = `app/acknowledgement/common-ack.html?wftype=Cancel&action=forward&name=&ackNo=${res.Agreements[0].acknowledgementNumber}`;
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


                        if (ID === "Print Notice") {
                            _this.printNotice(agreement);
                        } else {
                            $.ajax({
                                url: baseUrl + "/hr-employee/employees/_search?tenantId=" + tenantId + "&positionId=" + agreement.workflowDetails.assignee,
                                type: 'POST',
                                dataType: 'json',
                                contentType: 'application/json',
                                headers: {
                                    'auth-token': authToken
                                },
                                success: function (res1) {
                                    if (window.opener)
                                        window.opener.location.reload();
                                    if (res1 && res1.Employee && res1.Employee[0].name)
                                        window.location.href = `app/acknowledgement/common-ack.html?wftype=Cancel&action=forward&name=${res1.Employee[0].name}&ackNo=${res.Agreements[0].acknowledgementNumber}`;
                                    else
                                        window.location.href = `app/acknowledgement/common-ack.html?wftype=Cancel&action=forward&name=&ackNo=${res.Agreements[0].acknowledgementNumber}`;

                                },
                                error: function (err) {
                                    if (window.opener)
                                        window.opener.location.reload();
                                    window.location.href = `app/acknowledgement/common-ack.html?wftype=Cancel&action=forward&name=&ackNo=${res.Agreements[0].acknowledgementNumber}`;
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
                    var positionId;
                    item.assignments.forEach(function (item) {
                        if (item.isPrimary) {
                            positionId = item.position;
                        }
                    });

                    return (<option key={ind} value={positionId}>
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
                                            {allottee.mobileNumber ? allottee.mobileNumber : "N/A"}
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
                                            {allottee.aadhaarNumber ? allottee.aadhaarNumber : "N/A"}
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
                                        <label htmlFor="securityDeposit">Advace Collection:</label>
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
                                        <label htmlFor="orderNo"> Order Number<span>*</span> </label>
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
                                        <label htmlFor="orderDate">Order Date<span>*</span> </label>
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
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label>Attach Document </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="styled-file">
                                            <input id="documents" name="documents" type="file" onChange={(e) => { handleChange(e, "documents") }} multiple disabled />
                                            {renderFile()}
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
                                        <textarea rows="4" cols="50" id="remarks" name="remarks"
                                            onChange={(e) => { handleChange(e, "remarks") }} disabled ></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            );
        }

        const renederWorkflowHistory = function () {
            return (
                <div className="form-section hide-sec" id="agreementCancelDetails">
                    <h3 className="categoryType">Workflow History </h3>
                    <div className="form-section-inner">

                        <div id="historyTable" className="land-table">
                            <table class="table table-bordered">
                                <thead>
                                    <tr>
                                        <th>Date</th>
                                        <th>Updated By</th>
                                        <th>Current Owner</th>
                                        <th>Status</th>
                                        <th>Comments </th>
                                    </tr>
                                </thead>
                                <tbody>
                                    {renderTr()}
                                </tbody>
                            </table>
                        </div>


                    </div>
                </div>
            );
        }

        const renderTr = () => {
            return this.state.workflow.map((item, ind) => {

                var employeeName = commonApiPost("hr-employee", "employees", "_search", {
                    tenantId: tenantId,
                    id: item.owner.id
                }).responseJSON["Employee"] || {};


                return (
                    <tr key={ind}>
                        <td>{item.createdDate}</td>
                        <td>{item.senderName}</td>
                        <td>{employeeName[0] ? employeeName[0].code + " :: " + employeeName[0].name : ""}</td>
                        <td>{item.status}</td>
                        <td>{item.comments}</td>
                    </tr>
                )
            })
        }

        const renderWorkFlowDetails = function () {

            var flg = 0;

            buttons.forEach(function (btn, ind) {
                if (btn.key.toLowerCase() === "approve" || btn.key.toLowerCase() === "print notice") {
                    flg = 1;
                }
            });

            if (flg === 0) {

                return (
                    <div className="form-section">
                        <div className="row">
                            <div className="col-md-8 col-sm-8">
                                <h3 className="categoryType">Workflow Details </h3>
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
                                                onChange={(e) => { handleChangeTwoLevel(e, "workflowDetails", "department") }} required >
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
                                                onChange={(e) => { handleChangeTwoLevel(e, "workflowDetails", "designation") }} required >
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
                                        <label htmlFor="">User Name <span>*</span></label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="styled-select">
                                            <select id="assignee" name="assignee" value={workflowDetails.assignee}
                                                onChange={(e) => { handleChangeTwoLevel(e, "workflowDetails", "assignee") }} required>
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
                    <div className="form-section">
                        <div className="row">
                            <div className="col-md-8 col-sm-8">
                                <h3 className="categoryType">Workflow Details </h3>
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

        const renderFileTr = function (status) {
            var CONST_API_GET_FILE = "/filestore/v1/files/id?tenantId=" + tenantId + "&fileStoreId=";

            for (var i = 0; i < _this.state.movement.documents.length; i++) {
                return (<tr>
                    <td>${i + 1}</td>
                    <td>Document</td>
                    <td>
                        <a href={window.location.origin + CONST_API_GET_FILE + _this.state.movement.documents[i]} target="_blank">
                            Download
                        </a>
                    </td>
                </tr>);
            }

        }

        const renderFile = function (status) {
            if (_this.state.movement && _this.state.movement.documents) {
                return (
                    <table className="table table-bordered" id="fileTable" style={{ "display": "none" }}>
                        <thead>
                            <tr>
                                <th>Sr. No.</th>
                                <th>Name</th>
                                <th>File</th>
                            </tr>
                        </thead>
                        <tbody>
                            {renderFileTr()}
                        </tbody>
                    </table>
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
                        {renederWorkflowHistory()}
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
