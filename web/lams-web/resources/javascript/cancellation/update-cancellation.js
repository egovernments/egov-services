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
                    orderNumber: "",
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
            cancelReasons: ["Reason 1", "Reason 2", "Reason 3", "Reason 4"],
            positionList: [],
            departmentList: [],
            designationList: [],
            userList: [],
            buttons: []

        }
        this.handleChangeTwoLevel = this.handleChangeTwoLevel.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.handleProcess = this.handleProcess.bind(this);
        this.setInitialState = this.setInitialState.bind(this);
        this.setState = this.setState.bind(this);
        this.getUsersFun = this.getUsersFun.bind(this);
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
        var agreement = commonApiPost("lams-services",
            "agreements",
            "_search",
            {
                stateId: stateId,
                tenantId
            }).responseJSON["Agreements"][0] || {};

        var process = commonApiPost("egov-common-workflows", "process", "_search", {
            tenantId: tenantId,
            id: stateId
        }).responseJSON["processInstance"] || {};

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

        getDesignations(null, function (designations) {
            console.log(designations);
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
            //owner:process.owner.id,
            //status : process.status,
            buttons: _btns ? _btns : []
        });


        if (process.status && process.status != "Rejected") {

            $("#orderNumber").prop("disabled", true)
            $("#orderDate").prop("disabled", true)
            $("#terminationDate").prop("disabled", true)
            $("#reasonForCancellation").prop("disabled", true)
            $("#documents").prop("disabled", true)
            $("#remarks").prop("disabled", true)
        }


    }


    componentDidMount() {

        var _this = this;

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

        if ($('#update-promotion').valid()) { }
        var ID = e.target.id;
        var _this = this;
        var agreement = Object.assign({}, _this.state.agreement);

        agreement.action = "cancellation";
        agreement.workflowDetails.action = ID;

        console.log("Documents", agreement);

        if (agreement.documents && agreement.documents.constructor == FileList) {
            let counter = agreement.documents.length,
                breakout = 0,
                docs = [];
            for (let i = 0, len = agreement.documents.length; i < len; i++) {
                this.makeAjaxUpload(agreement.documents[i], function (err, res) {
                    if (breakout == 1) {
                        console.log("breakout", breakout);
                        return;
                    } else if (err) {
                        showError("Error uploding the files. Please contact Administrator");
                        breakout = 1;
                    } else {
                        counter--;
                        docs.push({ fileStore: res.files[0].fileStoreId });
                        console.log("docs", docs);
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

                                    showSuccess("Forwarded successfully");

                                },
                                error: function (err) {
                                    if (err.responseJSON.Error && err.responseJSON.Error.message)
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

                    showSuccess("Forwarded successfully");

                },
                error: function (err) {
                    if (err.responseJSON.Error && err.responseJSON.Error.message)
                        showError(err.responseJSON.Error.message);
                    else
                        showError("Something went wrong. Please contact Administrator");
                }

            })

        }


    }


    render() {
        var _this = this;
        let { handleChange, handleChangeTwoLevel, addOrUpdate, handleProcess } = this;
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
                                            {allottee.panNo ? allottee.panNo : "N/A"}   </label>
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
                                            {allottee.address ? allottee.address : "N/A"}
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
                                        <label htmlFor="orderNumber"> Order Number<span>*</span> </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <input type="text" name="orderNumber" id="orderNumber" value={cancellation.orderNumber}
                                            onChange={(e) => { handleChangeTwoLevel(e, "cancellation", "orderNumber") }} required />
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
                                                onChange={(e) => { handleChangeTwoLevel(e, "cancellation", "orderDate") }} required />
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
                                                onChange={(e) => { handleChangeTwoLevel(e, "cancellation", "terminationDate") }} required />
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
                                                onChange={(e) => { handleChangeTwoLevel(e, "cancellation", "reasonForCancellation") }} required>
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
                                            <input id="documents" name="documents" type="file" onChange={(e) => { handleChange(e, "documents") }} multiple />
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
                                            onChange={(e) => { handleChangeTwoLevel(e, "remarks") }} ></textarea>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            );
        }

        const renderWorkFlowDetails = function () {
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
                    </div>
                </div>
            );

        }


        return (
            <div>
                <h3>Cancellation Of Agreement </h3>
                <form onSubmit={(e) => { addOrUpdate(e) }} >
                    <fieldset>
                        {renderAssetDetails()}
                        {renderAllottee()}
                        {renderAgreementDetails()}
                        {renederCancelDetails()}
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
