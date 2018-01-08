class RemissionAgreement extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            agreement: {
                id: "",
                tenantId: tenantId,
                agreementNumber: "",
                acknowledgementNumber: "",
                stateId: "",
                action: "Remission",
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
                remission: {
                    remissionOrder: "",
                    remissionDate: "",
                    remissionFromDate: "",
                    remissionToDate: "",
                    remissionRent: "",
                    remissionReason: ""
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
            remissionReasons: ["Natural Calamity", "Infrastructure Development"],
        }

        this.handleChange = this.handleChange.bind(this);
        this.handleChangeTwoLevel = this.handleChangeTwoLevel.bind(this);
        this.update = this.update.bind(this);
        this.setInitialState = this.setInitialState.bind(this);
    }

    setInitialState(initState) {
        this.setState(initState);
    }


    close() {

        open(location, '_self').close();
    }

    update(e) {

        e.preventDefault();
        var _this = this;
        var agreement = Object.assign({}, _this.state.agreement);
        agreement.action = "Remission";

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
                                url: baseUrl + "/lams-services/agreements/_remission?tenantId=" + tenantId,
                                type: 'POST',
                                dataType: 'json',
                                data: JSON.stringify(body),
                                contentType: 'application/json',
                                headers: {
                                    'auth-token': authToken
                                },
                                success: function (res) {

                                    showSuccess("Remission details updated!");

                                },
                                error: function (err) {
                                    if (err.responseJSON.Error && err.responseJSON.Error.message)
                                        showError(err.responseJSON.Error.message);
                                    else
                                        showError("Something went wrong");
                                }

                            })

                        }
                    }
                })
            }

        } else {

            var body = {
                "RequestInfo": requestInfo,
                "Agreement": agreement
            };

            $.ajax({
                url: baseUrl + "/lams-services/agreements/_remission?tenantId=ap.kurnool",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify(body),
                contentType: 'application/json',
                headers: {
                    'auth-token': authToken
                },
                success: function (res) {

                    showSuccess("Remission details updated!");
                    setTimeout(function () {
                        open(location, '_self').close();
                    }, 4000);

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

                _this.setState({
                    agreement: {
                        ..._this.state.agreement,
                        documents: e.currentTarget.files
                    }
                })
            } else {
                _this.setState({
                    agreement: {
                        ..._this.state.agreement,
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
        if (pName === "remission")
            _this.setState({
                ..._this.state,
                agreement: {
                    ..._this.state.agreement,
                    remission: {
                        ..._this.state.agreement.remission,
                        [name]: e.target.value
                    }
                }
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
        $('#remission-title').text("Remission Of Agreement");
        var _this = this;
        var agreement = commonApiPost("lams-services",
            "agreements",
            "_search", {
                agreementNumber: getUrlVars()["agreementNumber"],
                tenantId
            }).responseJSON["Agreements"][0] || {};
        console.log(agreement);

        if (!agreement.remission) {
            agreement.remission = {};
        }
        this.setState({
            ...this.state,
            agreement: agreement
        });
    }


    componentDidMount() {

        var _this = this;

        var ad = this.state.agreement.agreementDate;
        var startDate =  new Date(ad.split("/")[2], ad.split("/")[1] - 1, ad.split("/")[0]);

        $('#remissionDate').datepicker({
            format: 'dd/mm/yyyy',
            startDate: startDate,
            autoclose: true,
            defaultDate: ""
        });

        $('#remissionDate').on('changeDate', function (e) {
            _this.setState({
                agreement: {
                    ..._this.state.agreement,
                    remission: {
                        ..._this.state.agreement.remission,
                        "remissionDate": $("#remissionDate").val()
                    }
                }
            });
        });

        $('#remissionFromDate').datepicker({
            format: 'dd/mm/yyyy',
            startDate: startDate,
            autoclose: true,
            defaultDate: ""
        });

        $('#remissionFromDate').on('changeDate', function (e) {
            _this.setState({
                agreement: {
                    ..._this.state.agreement,
                    remission: {
                        ..._this.state.agreement.remission,
                        "remissionFromDate": $("#remissionFromDate").val()
                    }
                }
            });
        });

        $('#remissionToDate').datepicker({
            format: 'dd/mm/yyyy',
            startDate: startDate,
            autoclose: true,
            defaultDate: ""
        });

        $('#remissionToDate').on('changeDate', function (e) {
            _this.setState({
                agreement: {
                    ..._this.state.agreement,
                    remission: {
                        ..._this.state.agreement.remission,
                        "remissionToDate": $("#remissionToDate").val()
                    }
                }
            });
        });


        $('#remissionToDate, #remissionFromDate, #remissionDate').on('change', function (e) {

        if(_this.state.agreement.remission.remissionFromDate && _this.state.agreement.remission.remissionDate){

            var  _to= _this.state.agreement.remission.remissionFromDate;
            var _from = _this.state.agreement.remission.remissionDate;
            var _triggerId = e.target.id;
            if (_from && _to) {
                var dateParts1 = _from.split("/");
                var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
                var date1 = new Date(newDateStr);
                var dateParts2 = _to.split("/");
                var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
                var date2 = new Date(newDateStr);
                if(date2<date1){
                  return  (showError("Remission Order Date should be before Remission From Date"));
                  $('#' + _triggerId).val("");
                }
            }
          }


          if(_this.state.agreement.remission.remissionFromDate && _this.state.agreement.remission.remissionToDate){

            var  _to= _this.state.agreement.remission.remissionToDate;
            var _from = _this.state.agreement.remission.remissionFromDate;
            var _triggerId = e.target.id;
            if (_from && _to) {
                var dateParts1 = _from.split("/");
                var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
                var date1 = new Date(newDateStr);
                var dateParts2 = _to.split("/");
                var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
                var date2 = new Date(newDateStr);
                if(date2<date1){
                  return  (showError("Remission From Date should be before Remission To Date"));
                  $('#' + _triggerId).val("");
                }
            }
          }
        });

    }

    render() {
        var _this = this;
        let { handleChange, handleChangeTwoLevel, update } = this;
        let { agreement, remissionReasons } = this.state;
        let { allottee, asset, rentIncrementMethod, cancellation,
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

        const renederRemissionDetails = function () {
            return (
                <div className="form-section hide-sec" id="agreementCancelDetails">
                    <h3 className="categoryType">Remission Details </h3>
                    <div className="form-section-inner">
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="remissionOrder"> Order Number<span>*</span> </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <input type="text" name="remissionOrder" id="remissionOrder"
                                            onChange={(e) => { handleChangeTwoLevel(e, "remission", "remissionOrder") }} required />
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="remissionDate">Order Date<span>*</span> </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="text-no-ui">
                                            <span className="glyphicon glyphicon-calendar"></span>
                                            <input type="text" id="remissionDate" name="remissionDate"
                                                onChange={(e) => { handleChangeTwoLevel(e, "remission", "remissionDate") }} required />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="remissionFromDate">Remission From Date<span>*</span> </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="text-no-ui">
                                            <span className="glyphicon glyphicon-calendar"></span>
                                            <input type="text" className="datepicker" id="remissionFromDate" name="remissionFromDate"
                                                onChange={(e) => { handleChangeTwoLevel(e, "remission", "remissionFromDate") }} required />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="remissionToDate">Remission ToDate<span>*</span> </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="text-no-ui">
                                            <span className="glyphicon glyphicon-calendar"></span>
                                            <input type="text" className="datepicker" id="remissionToDate" name="remissionToDate"
                                                onChange={(e) => { handleChangeTwoLevel(e, "remission", "remissionToDate") }} required />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="remissionRent" className="categoryType">Rent<span>*</span></label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="text-no-ui">
                                            <span>₹</span>
                                            <input type="number" name="remissionRent" id="remissionRent"
                                                onChange={(e) => { handleChangeTwoLevel(e, "remission", "remissionRent") }} required />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="remissionReason">Remission Reason<span>*</span></label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="styled-select">
                                            <select name="remissionReason" id="remissionReason"
                                                onChange={(e) => { handleChangeTwoLevel(e, "remission", "remissionReason") }} required>
                                                <option value="">Select Reason</option>
                                                {renderOption(remissionReasons)}
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


        return (
            <div>
                <h3>Remission Of Agreement </h3>
                <form onSubmit={(e) => { update(e) }} >
                    <fieldset>
                        {renderAssetDetails()}
                        {renderAllottee()}
                        {renderAgreementDetails()}
                        {renederRemissionDetails()}
                        <br />
                        <div className="text-center">
                            <button id="sub" type="submit" className="btn btn-submit">Update </button>&nbsp;&nbsp;
                            <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>
                        </div>

                    </fieldset>
                </form>
            </div>
        );
    }

}
ReactDOM.render(
    <RemissionAgreement />,
    document.getElementById('root')
);
