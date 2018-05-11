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
        this.printNotice = this.printNotice.bind(this);

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
                                    _this.printNotice(agreement);
                                    showSuccess("Remission details updated!");
                                    setTimeout(function () {
                                        open(location, '_self').close();
                                    }, 4000);
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
                    
                    _this.printNotice(agreement);

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

        $('#remissionDate').on('change blur', function (e) {
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

        $('#remissionFromDate').on('change blur', function (e) {
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

        $('#remissionToDate').on('change blur', function (e) {
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

    }

    printNotice(agreement) {

        var LocalityData = commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "LOCALITY", hierarchyTypeName: "LOCATION", tenantId });
        var locality = getNameById(LocalityData["responseJSON"]["Boundary"], agreement.asset.locationDetails.locality);
        var cityGrade = !localStorage.getItem("city_grade") || localStorage.getItem("city_grade") == "undefined" ? (localStorage.setItem("city_grade", JSON.stringify(commonApiPost("tenant", "v1/tenant", "_search", { code: tenantId }).responseJSON["tenant"][0]["city"]["ulbGrade"] || {})), JSON.parse(localStorage.getItem("city_grade"))) : JSON.parse(localStorage.getItem("city_grade"));
        var ulbType = "Nagara Panchayat/Municipality";
        if (cityGrade.toLowerCase() === 'corp') {
            ulbType = "Municipal Corporation";
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
            { title: "Particulars", dataKey: "p" },
            { title: "Existing Amount (per month)", dataKey: "ea" },
            { title: "Dues", dataKey: "d" },
            { title: "Remission sanctioned", dataKey: "rs" },
            { title: "Net Amount Due", dataKey: "nad" },
        ];
        var rows = [
            { "p": "Lease Rentals", "ea": "100", "d": "200", "rs": "100", "nad": "100" },
            { "p": "Total", "ea": "100", "d": "200", "rs": "100", "nad": "100" }
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
            }, startY: 135
        };

        var doc = new jsPDF();

        doc.setFontType("bold");
        doc.setFontSize(13);
        doc.text(105, 20, "PROCEEDINGS OF THE COMMISSIONER, " + tenantId.split(".")[1].toUpperCase(), 'center');
        doc.text(105, 27, "NAGAR PANCHAYATHY/MUNICIPALITY/MUNICIPAL CORPORATION TODO", 'center');
        doc.text(105, 34, "Present: S Ravindra Babu", 'center');

        doc.setFontType("normal");
        doc.setFontSize(11);
        doc.text(15, 50, 'Roc.No. ' + agreement.agreementNumber);
        doc.text(140, 50, 'Dt. ' + today);

        var paragraph = "Sub: Leases – Revenue Section – Shop No " + agreement.referenceNumber + " in " +agreement.asset.name + " Complex, " + locality + " - Remission of lease – Orders  - Issued";
        var lines = doc.splitTextToSize(paragraph, 180);
        doc.text(15, 65, lines);

        doc.text(15, 80, "Ref: 1. Request Letter by the leaseholder");
        doc.text(23, 85, "2. Resolution No …………… dt …………… of Municipal Council/Standing Committee");
       
        doc.text(105, 95, "><><><", 'center');

        doc.text(15, 105, "Orders:");
        doc.setLineWidth(0.5);
        doc.line(15, 106, 28, 106);


        var paragraph1 =  "In the reference 1st cited, a request for remission of lease amount of existing lease for Shop No " + agreement.referenceNumber + " in the " + agreement.asset.name + " Shopping Complex was received by this office and your application for remission of lease amount was accepted by the Municipal Council/Standing Committee vide reference 2nd cited with the as per the rates mentioned below";
        var lines = doc.splitTextToSize(paragraph1, 180);
        doc.text(15, 115, lines);

        doc.autoTable(columns, rows, autoTableOptions);

        var paragraph2 = "In pursuance of the Municipal Council/Standing Committee resolution and vide GO MS No 56 dt. 05.02.2011, you are requested to pay all the dues for the said lease mentioned above immediately failing which action will be initiated as per the agreement conditions.";
        var lines = doc.splitTextToSize(paragraph2, 180);
        doc.text(15, 170, lines);


        doc.text(120, 190, "Commissioner");
        doc.text(120, 195, tenantId.split(".")[1].charAt(0).toUpperCase() + tenantId.split(".")[1].slice(1) + ",");
        doc.text(120, 200, ulbType);


        doc.text(15, 205, "To");
        doc.text(15, 210, "The Leaseholder");
        doc.text(15, 215, "Copy to the concerned officials for necessary action");

        doc.save('Notice-' + agreement.agreementNumber + '.pdf');
        var blob = doc.output('blob');

        this.createFileStore(agreement, blob).then(this.createNotice, this.errorHandler);
    }

    render() {
        var _this = this;
        let { handleChange, handleChangeTwoLevel, update, printNotice } = this;
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
                                            <input type="number" name="remissionRent" id="remissionRent" min="0"
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
