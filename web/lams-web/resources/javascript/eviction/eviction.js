class EvictionAgreement extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            agreement: {
                id: "",
                tenantId: tenantId,
                agreementNumber: "",
                acknowledgementNumber: "",
                stateId: "",
                action: "Eviction",
                agreementDate: "",
                timePeriod: "",
                allottee: {
                    id: "",
                    name: "",
                    permanentAddress: "",
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
                cancellation: "",
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
            evictionReasons: ["Court Case", "Committee Order", "Others"],
            positionList: [],
            departmentList: [],
            designationList: [],
            userList: []

        }
        this.handleChangeTwoLevel = this.handleChangeTwoLevel.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.addOrUpdate = this.addOrUpdate.bind(this);
        this.setInitialState = this.setInitialState.bind(this);
        this.getUsersFun = this.getUsersFun.bind(this);
        this.handleBlur = this.handleBlur.bind(this);
    }

    setInitialState(initState) {
        this.setState(initState);
    }

    close() {
        open(location, '_self').close();
    }
    addOrUpdate(e) {

        e.preventDefault();
        var _this = this;
        var agreement = Object.assign({}, _this.state.agreement);
        agreement.action = "Eviction";
        var ID = "Forward";

        if(agreement.eviction.reasonForEviction == "Others" && !(agreement.remarks))
        return(showError("Please enter remarks when selected reason as others"));
    

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
                                url: baseUrl + "/lams-services/agreements/_eviction?tenantId=" + tenantId,
                                type: 'POST',
                                dataType: 'json',
                                data: JSON.stringify(body),
                                contentType: 'application/json',
                                headers: {
                                    'auth-token': authToken
                                },
                                success: function (res) {

                                    $.ajax({
                                        url: baseUrl + "/hr-employee/employees/_search?tenantId=" + tenantId + "&positionId=" + agreement.workflowDetails.assignee + "&asOnDate=" + asOnDate,
                                        type: 'POST',
                                        dataType: 'json',
                                        contentType: 'application/json',
                                        data: JSON.stringify({ RequestInfo: requestInfo }),
                                        headers: {
                                            'auth-token': authToken
                                        },
                                        success: function (res1) {
                                            if (window.opener)
                                                window.opener.location.reload();
                                            if (res1 && res1.Employee && res1.Employee[0].name)
                                                window.location.href = `app/acknowledgement/common-ack.html?wftype=Eviction&action=${ID}&name=${res1.Employee[0].code}::${res1.Employee[0].name}&ackNo=${res.Agreements[0].acknowledgementNumber}`;
                                            else
                                                window.location.href = `app/acknowledgement/common-ack.html?wftype=Eviction&action=${ID}&name=&ackNo=${res.Agreements[0].acknowledgementNumber}`;

                                        },
                                        error: function (err) {
                                            if (window.opener)
                                                window.opener.location.reload();
                                            window.location.href = `app/acknowledgement/common-ack.html?wftype=Eviction&action=${ID}&name=&ackNo=${res.Agreements[0].acknowledgementNumber}`;
                                        }
                                    })

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
                url: baseUrl + "/lams-services/agreements/_eviction?tenantId=" + tenantId,
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(body),
                contentType: 'application/json',
                headers: {
                    'auth-token': authToken
                },
                success: function (res) {

                    $.ajax({
                        url: baseUrl + "/hr-employee/employees/_search?tenantId=" + tenantId + "&positionId=" + agreement.workflowDetails.assignee + "&asOnDate=" + asOnDate,
                        type: 'POST',
                        dataType: 'json',
                        contentType: 'application/json',
                        data: JSON.stringify({ RequestInfo: requestInfo }),
                        headers: {
                            'auth-token': authToken
                        },
                        success: function (res1) {
                            if (window.opener)
                                window.opener.location.reload();
                            if (res1 && res1.Employee && res1.Employee[0].name)
                                window.location.href = `app/acknowledgement/common-ack.html?wftype=Eviction&action=${ID}&name=${res1.Employee[0].code}::${res1.Employee[0].name}&ackNo=${res.Agreements[0].acknowledgementNumber}`;
                            else
                                window.location.href = `app/acknowledgement/common-ack.html?wftype=Eviction&action=${ID}&name=&ackNo=${res.Agreements[0].acknowledgementNumber}`;

                        },
                        error: function (err) {
                            if (window.opener)
                                window.opener.location.reload();
                            window.location.href = `app/acknowledgement/common-ack.html?wftype=Eviction&action=${ID}&name=&ackNo=${res.Agreements[0].acknowledgementNumber}`;
                        }
                    })

                },
                error: function (err) {
                    if (err.responseJSON.Error && err.responseJSON.Error.message)
                        showError(err.responseJSON.Error.message);
                    else
                        showError("Something went wrong.");
                }

            })

        }

    }

    handleBlur(e) {
    var resolutionNo = this.agreement.councilNumber;
    // commonApiGet("restapi", "councilresolutions", "", { resolutionNo, tenantId }, function (err, res) {
    // if (res) {
    // alert(res);
    // }else{
    // alert("Invalid CR number");
    // this.agreement.councilNumber = "";
    // }
    // });
    if(resolutionNo){
        $.ajax({
        url: baseUrl + "/council/councilresolution?councilResolutionNo="+ resolutionNo,
        type:'GET',
        dataType: 'json',
        contentType: 'application/json;charset=utf-8',
        
        headers: {
        'auth-token': authToken
        },
        success: function (res1) { 
        $('#alert-box').fadeIn(function(){
            $("#alert-box-content").html("Preamble No : " + res1.preambleNumber +" Gist Of Preamble : "+ res1.gistOfPreamble);
        });
    },
    error: function (err) {
        $('#councilNumber').val("");
            $('#alert-box').fadeIn(function(){
                $("#alert-box-content").html("Invalid CR number");
                });
                }
            })
        }
    }

    handleChange(e, name) {

        console.log("Name :", name);

        var _this = this;

        if (name === "documents") {
            console.log(name, "Document  : ", e.target.value)
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
            console.log(name, "Name : ", e.target.value)
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
            data: JSON.stringify({
                RequestInfo: requestInfo
            }),
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
            error: function (err) { }

        })

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



    componentWillMount() {

        if (window.opener && window.opener.document) {
            var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
            if (logo_ele && logo_ele[0]) {
                document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
            }
        }
        $('#lams-title').text("Eviction Of Agreement");
        var _this = this;

        try {
            var departmentList = !localStorage.getItem("assignments_department") || localStorage.getItem("assignments_department") == "undefined" ? (localStorage.setItem("assignments_department", JSON.stringify(getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"] || [])), JSON.parse(localStorage.getItem("assignments_department"))) : JSON.parse(localStorage.getItem("assignments_department"));
        } catch (e) {
            console.log(e);
            var department = [];
        }

        getDesignations(null,null, function (designations) {
            _this.setState({
                ..._this.state,
                designationList: designations
            });

        }, "Eviction LeaseAgreement");


        var agreement = commonApiPost("lams-services",
            "agreements",
            "_search", {
                agreementNumber: getUrlVars()["agreementNumber"],
                tenantId
            }).responseJSON["Agreements"][0] || {};
        console.log(agreement);

        if (!agreement.eviction) {
            agreement.eviction = {};
        }
        if (!agreement.workflowDetails) {
            agreement.workflowDetails = {};
        }

        this.setState({
            ...this.state,
            agreement: agreement,
            departmentList: departmentList
        });

    }


    componentDidMount() {

        var _this = this;
        var ad = this.state.agreement.agreementDate;
        var startDate =  new Date(ad.split("/")[2], ad.split("/")[1] - 1, ad.split("/")[0]);

        $('#evictionProceedingDate').datepicker({
            format: 'dd/mm/yyyy',
            startDate: startDate,
            autoclose: true,
            defaultDate: ""
        });

        $('#evictionProceedingDate').on('change blur', function (e) {
            _this.setState({
                agreement: {
                    ..._this.state.agreement,
                    eviction: {
                        ..._this.state.agreement.eviction,
                        "evictionProceedingDate": $("#evictionProceedingDate").val()
                    }
                }
            });
        });

    }

    render() {
        var _this = this;
        let { handleChange, handleChangeTwoLevel, addOrUpdate,handleBlur } = this;
        let { agreement, evictionReasons } = this.state;
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

        const renederEvictionDetails = function () {
            return (
                <div className="form-section hide-sec" id="agreementEvictionDetails">
                    <h3 className="categoryType">Eviction Details </h3>
                    <div className="form-section-inner">
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="evictionProceedingNumber">Council/standing committee Resolution Number
                                       <span>*</span>
                                        </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <input type="text" name="evictionProceedingNumber" id="evictionProceedingNumber" value={eviction.evictionProceedingNumber}
                                            onChange={(e) => { handleChangeTwoLevel(e, "eviction", "evictionProceedingNumber") }} 
                                            onBlur={(e)=>handleBlur(e)} required />
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="evictionProceedingDate">Council/standing committee Resolution Date
                                       <span>*</span>
                                        </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="text-no-ui">
                                            <span className="glyphicon glyphicon-calendar"></span>
                                            <input type="text" className="datepicker" name="evictionProceedingDate" id="evictionProceedingDate" value={eviction.evictionProceedingDate}
                                                onChange={(e) => { handleChangeTwoLevel(e, "eviction", "evictionProceedingDate") }} required />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="row">
                            <div className="col-sm-6" id="courtR.ferenceNumber">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="courtReferenceNumber">Court Reference Number
                                      <span>*</span>
                                        </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <input type="text" name="courtReferenceNumber" id="courtReferenceNumber" value={eviction.courtReferenceNumber}
                                            onChange={(e) => { handleChangeTwoLevel(e, "eviction", "courtReferenceNumber") }} required />
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="reasonForEviction">Reason For Eviction
                                      <span>*</span>
                                        </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <select name="reasonForEviction" id="reasonForEviction" className="selectStyle" value={eviction.reasonForEviction}
                                            onChange={(e) => { handleChangeTwoLevel(e, "eviction", "reasonForEviction") }} required>
                                            <option value="">---Select Reason---</option>
                                            {renderOption(evictionReasons)}
                                        </select>
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
                                        <label for="remarks">Remarks </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <textarea rows="4" cols="50" id="remarks" name="remarks"
                                            onChange={(e) => { handleChange(e, "remarks") }} ></textarea>
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
                                    <label htmlFor="">Employee <span>*</span></label>
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
                <h3>Eviction Of Agreement </h3>
                <form onSubmit={(e) => { addOrUpdate(e) }} >
                    <fieldset>
                        {renderAssetDetails()}
                        {renderAllottee()}
                        {renderAgreementDetails()}
                        {renederEvictionDetails()}
                        {renderWorkFlowDetails()}

                        <br />
                        <div className="text-center">
                            <button id="sub" type="submit" className="btn btn-submit">Submit</button>&nbsp;&nbsp;
                    <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>
                        </div>

                    </fieldset>
                </form>
            </div>
        );
    }

}
ReactDOM.render(
    <EvictionAgreement />,
    document.getElementById('root')
);
