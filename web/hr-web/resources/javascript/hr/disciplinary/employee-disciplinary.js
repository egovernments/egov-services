class CancellationAgreement extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            "disciplinarySet": {
                "employeeId": "",
                "gistCase": "",
                "disciplinaryAuthority": "",
                "orderNo": "",
                "orderDate": "",
                "memoNo": "",
                "memoDate": "",
                "memoServingDate": "",
                "dateOfReceiptMemoDate": "",
                "explanationAccepted": false,
                "chargeMemoNo": "",
                "chargeMemoDate": "",
                "dateOfReceiptToChargeMemoDate": "",
                "accepted": false,
                "dateOfAppointmentOfEnquiryOfficerDate": "",
                "enquiryOfficerName": "",
                "enquiryOfficerDesignation": "",
                "dateOfAppointmentOfPresentingOfficer": "",
                "presentingOfficerName": "",
                "presentingOfficerDesignation": "",
                "findingsOfEO": "",
                "enquiryReportSubmittedDate": "",
                "dateOfCommunicationOfER": "",
                "dateOfSubmissionOfExplanationByCO": "",
                "acceptanceOfExplanation": false,
                "proposedPunishmentByDA": "",
                "showCauseNoticeNo": "",
                "showCauseNoticeDate": "",
                "showCauseNoticeServingDate": "",
                "explanationToShowCauseNotice": "",
                "explanationToShowCauseNoticeAccepted": false,
                "punishmentAwarded": "",
                "proceedingsNumber": "",
                "proceedingsDate": "",
                "proceedingsServingDate": "",
                "courtCase": false,
                "courtOrderType": "",
                "courtOrderNo": "",
                "courtOrderDate": "",
                "gistOfDirectionIssuedByCourt": "",
                "tenantId": tenantId,
                "memoDocuments": "",
                "enquiryDocuments": "",
                "showCauseDocuments": "",
                "courtDocuments": ""
            },
            employee: {
                code: "",
                name: "",
                designation: ""
            },
            memo: false,
            enquiry: false,
            showcause: false,
            courtorder: false,
            courtOrderTypeList: []

        }
        this.handleChangeTwoLevel = this.handleChangeTwoLevel.bind(this);
        this.handleSectionChange = this.handleSectionChange.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.addOrUpdate = this.addOrUpdate.bind(this);
        this.setInitialState = this.setInitialState.bind(this);
        this.getUsersFun = this.getUsersFun.bind(this);
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
        var disciplinary = Object.assign({}, _this.state.disciplinarySet);

        var body = {
            "RequestInfo": requestInfo,
            "Disciplinary": disciplinary
        };

        console.log(baseUrl);
        $.ajax({
            url: baseUrl + "/hr-employee/disciplinary/_create?tenantId=" + tenantId,
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify(body),
            contentType: 'application/json',
            headers: {
                'auth-token': authToken
            },
            success: function (res) {
                showSuccess("Created successfully Created");
            },
            error: function (err) {
                console.log(err);
                if (err["responseJSON"] && err["responseJSON"].message)
                    showError(err["responseJSON"].message);
                // else if (err["responseJSON"].Movement[0]) {
                //   showError(err["responseJSON"].Movement[0].errorMsg)
                // }
                else {
                    showError("Something went wrong. Please contact Administrator");
                }
            }
        });

    }

    handleSectionChange(e, name) {

        var _this = this;

        if (name == "memo") {
            if (_this.state.memo) {
                _this.setState({
                    ..._this.state, memo: false, enquiry: false, showcause: false, courtorder: false
                });
                $('#enquiry, #showcause, #courtorder').prop("disabled", true);
                $('#enquiry, #showcause, #courtorder').prop("checked", false);

            } else {
                _this.setState({
                    ..._this.state, memo: true
                });
                $('#enquiry').prop("disabled", false);
            }

        } else if (name == "enquiry") {

            if (_this.state.enquiry) {
                _this.setState({
                    ..._this.state, memo: true, enquiry: false, showcause: false, courtorder: false
                });
                $('#showcause, #courtorder').prop("disabled", true);
                $('#showcause, #courtorder').prop("checked", false);
            } else {
                _this.setState({
                    ..._this.state, enquiry: true
                });
                $('#showcause').prop("disabled", false);
            }


        } else if (name == "showcause") {

            if (_this.state.showcause) {
                _this.setState({
                    ..._this.state, memo: true, enquiry: true, showcause: false, courtorder: false
                });
                $('#courtorder').prop("disabled", true);
                $('#courtorder').prop("checked", false);
            } else {
                _this.setState({
                    ..._this.state, showcause: true
                });
                $('#courtorder').prop("disabled", false);
            }
        } else if (name == "courtorder") {

            _this.setState({
                ..._this.state,
                courtorder: _this.state.courtorder ? false : true
            });

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
                disciplinarySet: {
                    ..._this.state.disciplinarySet,
                    [name]: e.target.value
                }
            })
        }
    }

    handleChangeTwoLevel(e, pName, name) {

        var _this = this;

        _this.setState({
            ..._this.state,
            [pName]: {
                ..._this.state[pName],
                [name]: e.target.value
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


    componentWillMount() {

        if (window.opener && window.opener.document) {
            var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
            if (logo_ele && logo_ele[0]) {
                document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
            }
        }
        $('#hp-citizen-title').text("Employee Disciplinary");
        let _this = this;
        let id = getUrlVars()["id"];

        getCommonMasterById("hr-employee", "employees", id, function (err, res) {
            if (res && res.Employee) {
                var obj = res.Employee[0];
                var ind = 0;
                if (obj.length > 0) {
                    obj.map((item, index) => {
                        for (var i = 0; i < item.assignments.length; i++) {
                            if ([true, "true"].indexOf(item.assignments[i].isPrimary) > -1) {
                                ind = i;
                                break;
                            }
                        }
                    });
                }
                _this.setState({
                    ..._this.state,
                    employee: {
                        name: obj.name,
                        code: obj.code,
                        designation: obj.assignments[ind].designation,
                    },
                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        employeeId: obj.id
                    }

                })
            }
        });

    }

    componentDidUpdate() {

        var _this = this;


        $('#orderDate').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#orderDate').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.orderDate != e.target.value)
                _this.setState({
                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "orderDate": $("#orderDate").val()
                    }
                });
        });



        $('#memoDate').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#memoDate').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.memoDate != e.target.value)
                _this.setState({
                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "memoDate": $("#memoDate").val()
                    }

                });
        });



        $('#memoServingDate').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#memoServingDate').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.memoServingDate != e.target.value)
                _this.setState({
                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "memoServingDate": $("#memoServingDate").val()
                    }

                });
        });



        $('#dateOfReceiptMemoDate').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#dateOfReceiptMemoDate').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.dateOfReceiptMemoDate != e.target.value)
                _this.setState({
                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "dateOfReceiptMemoDate": $("#dateOfReceiptMemoDate").val()
                    }
                });
        });



        $('#chargeMemoDate').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#chargeMemoDate').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.chargeMemoDate != e.target.value)
                _this.setState({
                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "chargeMemoDate": $("#chargeMemoDate").val()
                    }
                });
        });



        $('#dateOfReceiptToChargeMemoDate').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#dateOfReceiptToChargeMemoDate').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.dateOfReceiptToChargeMemoDate != e.target.value)
                _this.setState({
                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "dateOfReceiptToChargeMemoDate": $("#dateOfReceiptToChargeMemoDate").val()
                    }
                });
        });



        $('#dateOfAppointmentOfEnquiryOfficerDate').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#dateOfAppointmentOfEnquiryOfficerDate').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.dateOfAppointmentOfEnquiryOfficerDate != e.target.value)
                _this.setState({

                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "dateOfAppointmentOfEnquiryOfficerDate": $("#dateOfAppointmentOfEnquiryOfficerDate").val()

                    }
                });
        });



        $('#dateOfAppointmentOfPresentingOfficer').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#dateOfAppointmentOfPresentingOfficer').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.dateOfAppointmentOfPresentingOfficer != e.target.value)
                _this.setState({

                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "dateOfAppointmentOfPresentingOfficer": $("#dateOfAppointmentOfPresentingOfficer").val()

                    }
                });
        });



        $('#enquiryReportSubmittedDate').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#enquiryReportSubmittedDate').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.enquiryReportSubmittedDate != e.target.value)
                _this.setState({

                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "enquiryReportSubmittedDate": $("#enquiryReportSubmittedDate").val()

                    }
                });
        });



        $('#dateOfCommunicationOfER').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#dateOfCommunicationOfER').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.dateOfCommunicationOfER != e.target.value)
                _this.setState({

                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "dateOfCommunicationOfER": $("#dateOfCommunicationOfER").val()

                    }
                });
        });



        $('#dateOfSubmissionOfExplanationByCO').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#dateOfSubmissionOfExplanationByCO').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.dateOfSubmissionOfExplanationByCO != e.target.value)
                _this.setState({

                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "dateOfSubmissionOfExplanationByCO": $("#dateOfSubmissionOfExplanationByCO").val()

                    }
                });
        });



        $('#showCauseNoticeDate').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#showCauseNoticeDate').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.showCauseNoticeDate != e.target.value)
                _this.setState({

                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "showCauseNoticeDate": $("#showCauseNoticeDate").val()

                    }
                });
        });



        $('#showCauseNoticeServingDate').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#showCauseNoticeServingDate').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.showCauseNoticeServingDate != e.target.value)
                _this.setState({

                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "showCauseNoticeServingDate": $("#showCauseNoticeServingDate").val()

                    }
                });
        });



        $('#proceedingsDate').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#proceedingsDate').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.proceedingsDate != e.target.value)
                _this.setState({

                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "proceedingsDate": $("#proceedingsDate").val()

                    }
                });
        });


        $('#proceedingsServingDate').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#proceedingsServingDate').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.proceedingsServingDate != e.target.value)
                _this.setState({

                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "proceedingsServingDate": $("#proceedingsServingDate").val()
                    }

                });
        });



        $('#courtOrderDate').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#courtOrderDate').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.courtOrderDate != e.target.value)
                _this.setState({

                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "courtOrderDate": $("#courtOrderDate").val()

                    }
                });
        });

    }


    render() {
        var _this = this;
        let { handleChange, handleSectionChange, handleChangeTwoLevel, addOrUpdate } = this;
        let { disciplinarySet, memo, enquiry, showcause, courtorder, employee, courtOrderTypeList } = this.state;
        let {
            gistCase,
            disciplinaryAuthority,
            orderNo,
            orderDate,
            memoNo,
            memoDate,
            memoServingDate,
            dateOfReceiptMemoDate,
            explanationAccepted,
            chargeMemoNo,
            chargeMemoDate,
            dateOfReceiptToChargeMemoDate,
            accepted,
            dateOfAppointmentOfEnquiryOfficerDate,
            enquiryOfficerName,
            enquiryOfficerDesignation,
            dateOfAppointmentOfPresentingOfficer,
            presentingOfficerName,
            presentingOfficerDesignation,
            findingsOfEO,
            enquiryReportSubmittedDate,
            dateOfCommunicationOfER,
            dateOfSubmissionOfExplanationByCO,
            acceptanceOfExplanation,
            proposedPunishmentByDA,
            showCauseNoticeNo,
            showCauseNoticeDate,
            showCauseNoticeServingDate,
            explanationToShowCauseNotice,
            explanationToShowCauseNoticeAccepted,
            punishmentAwarded,
            proceedingsNumber,
            proceedingsDate,
            proceedingsServingDate,
            courtCase,
            courtOrderType,
            courtOrderNo,
            courtOrderDate,
            gistOfDirectionIssuedByCourt,
            memoDocuments,
            enquiryDocuments,
            showCauseDocuments,
            courtDocuments

        } = this.state.disciplinarySet;

        const renderOption = function (data) {
            if (data) {
                return data.map((item, ind) => {
                    return (<option key={ind} value={typeof item == "object" ? item.id : item}>
                        {typeof item == "object" ? item.name : item}
                    </option>)
                })
            }
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

        const renderSection = function () {
            return (
                <div className="form-section" id="allotteeDetailsBlock">
                    <h3>Memo Phase </h3>
                    <div className="form-section-inner">
                        <div className="row">
                            <div className="col-sm-3">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="allotteeName"> Memo </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="checkbox" name="memo" id="memo" value={""} maxLength="15"
                                            onChange={(e) => { handleSectionChange(e, "memo") }}  />
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-3">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="mobileNumber">Enquiry </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="checkbox" name="enquiry" id="enquiry" value={""} maxLength="15"
                                            onChange={(e) => { handleSectionChange(e, "enquiry") }}  disabled />
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-3">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="allotteeName"> Show Cause Notice </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="checkbox" name="showcause" id="showcause" value={""} maxLength="15"
                                            onChange={(e) => { handleSectionChange(e, "showcause") }}  disabled />
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-3">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="mobileNumber">Court Order </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="checkbox" name="courtorder" id="courtorder" value={""} maxLength="15"
                                            onChange={(e) => { handleSectionChange(e, "courtorder") }}  disabled />
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            );
        }

        const renderMemo = function () {
            if (memo) {
                return (
                    <div className="form-section" id="allotteeDetailsBlock">
                        <h3>Memo Phase </h3>
                        <div className="form-section-inner">
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="code"> Code </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="code" id="code" value={employee.code} maxLength="15"
                                                onChange={(e) => { handleChangeTwoLevel(e, "employee", "code") }} disabled />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="Name">Name </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="name" id="name" value={employee.name} maxLength="15"
                                                onChange={(e) => { handleChangeTwoLevel(e, "employee", "name") }}  disabled />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="designation">Designation </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="designation" id="designation" value={employee.designation} maxLength="15"
                                                onChange={(e) => { handleChangeTwoLevel(e, "employee", "designation") }} disabled />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="gistCase">Gist of the Case <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="gistCase" id="gistCase" value={gistCase} maxLength="15"
                                                onChange={(e) => { handleChange(e, "gistCase") }} required />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="disciplinaryAuthority">Disciplinary Authority <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="disciplinaryAuthority" id="disciplinaryAuthority" value={disciplinaryAuthority} maxLength="15"
                                                onChange={(e) => { handleChange(e, "disciplinaryAuthority") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="orderNo">Order No <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={orderNo} maxLength="15"
                                                onChange={(e) => { handleChange(e, "orderNo") }} required />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="orderDate">Order Date of Suspension <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderDate" id="orderDate" value={orderDate} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="memoNo">Memo No <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="memoNo" id="memoNo" value={memoNo} maxLength="15"
                                                onChange={(e) => { handleChange(e, "memoNo") }} required />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="memoDate">Memo Date <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="memoDate" id="memoDate" value={memoDate} required />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="memoServingDate">Memo serving Date</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="memoServingDate" id="memoServingDate" value={memoServingDate}  />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="dateOfReceiptMemoDate">Date of Receipt of Explanation to memo</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="dateOfReceiptMemoDate" id="dateOfReceiptMemoDate" value={dateOfReceiptMemoDate}
                                                 />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="explanationAccepted">Explanation Accepted Y/N</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="checkbox" name="explanationAccepted" id="explanationAccepted" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "explanationAccepted") }}  />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="chargeMemoNo">Charge Memo No</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="chargeMemoNo" id="chargeMemoNo" value={chargeMemoNo} maxLength="15"
                                                onChange={(e) => { handleChange(e, "chargeMemoNo") }}  />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="chargeMemoDate">Charge Memo Date</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="chargeMemoDate" id="chargeMemoDate" value={chargeMemoDate} maxLength="15"
                                                 />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="dateOfReceiptToChargeMemoDate">Date of Receipt of written statement to charge memo</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="dateOfReceiptToChargeMemoDate" id="dateOfReceiptToChargeMemoDate" value={dateOfReceiptToChargeMemoDate} maxLength="15"
                                                 />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="accepted">Accepted Y/N</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="checkbox" name="accepted" id="accepted" value={accepted} maxLength="15"
                                                onChange={(e) => { handleChange(e, "accepted") }}  />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="memoDocuments">Attachments</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="memoDocuments" id="memoDocuments" value={memoDocuments} maxLength="15"
                                                onChange={(e) => { handleChange(e, "memoDocuments") }}  />

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                );
            }
        }

        const renderEnquiry = function () {
            if (enquiry) {
                return (
                    <div className="form-section" id="agreementDetailsBlock">
                        <h3>Enquiry Phase </h3>
                        <div className="form-section-inner">
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="dateOfAppointmentOfEnquiryOfficerDate"> Date of Appointment of Enquiry officer <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="dateOfAppointmentOfEnquiryOfficerDate" id="dateOfAppointmentOfEnquiryOfficerDate" value={dateOfAppointmentOfEnquiryOfficerDate} maxLength="15" required />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="enquiryOfficerName">Enquiry officer Name <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="enquiryOfficerName" id="enquiryOfficerName" value={enquiryOfficerName} maxLength="15"
                                                onChange={(e) => { handleChange(e, "enquiryOfficerName") }} required />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="enquiryOfficerDesignation">Enquiry officer Designation <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="enquiryOfficerDesignation" id="enquiryOfficerDesignation" value={enquiryOfficerDesignation} maxLength="15"
                                                onChange={(e) => { handleChange(e, "enquiryOfficerDesignation") }} required />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="dateOfAppointmentOfPresentingOfficer">Date of Appointment of Presenting Officer </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="dateOfAppointmentOfPresentingOfficer" id="dateOfAppointmentOfPresentingOfficer" value={dateOfAppointmentOfPresentingOfficer} maxLength="15"
                                                 />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="presentingOfficerName">Presenting Officer Name </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="presentingOfficerName" id="presentingOfficerName" value={presentingOfficerName} maxLength="15"
                                                onChange={(e) => { handleChange(e, "presentingOfficerName") }}  />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="presentingOfficerDesignation">Presenting Officer Designation</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="presentingOfficerDesignation" id="presentingOfficerDesignation" value={presentingOfficerDesignation} maxLength="15"
                                                onChange={(e) => { handleChange(e, "presentingOfficerDesignation") }}  />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="findingsOfEO">Findings of EO</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="findingsOfEO" id="findingsOfEO" value={findingsOfEO} maxLength="15"
                                                onChange={(e) => { handleChange(e, "findingsOfEO") }}  />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="enquiryReportSubmittedDate">Enquiry Report submitted Date</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="enquiryReportSubmittedDate" id="enquiryReportSubmittedDate" value={enquiryReportSubmittedDate} maxLength="15"
                                                 />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="dateOfCommunicationOfER">Date of Communication of ER to delinquent officer</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="dateOfCommunicationOfER" id="dateOfCommunicationOfER" value={dateOfCommunicationOfER} maxLength="15"
                                                 />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="dateOfSubmissionOfExplanationByCO">Date of submission of explanation by CO</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="dateOfSubmissionOfExplanationByCO" id="dateOfSubmissionOfExplanationByCO" value={dateOfSubmissionOfExplanationByCO} maxLength="15"
                                                 />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="acceptanceOfExplanation">Acceptance of explanation Y/N</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="checkbox" name="acceptanceOfExplanation" id="acceptanceOfExplanation" value={acceptanceOfExplanation} maxLength="15"
                                                onChange={(e) => { handleChange(e, "acceptanceOfExplanation") }}  />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="enquiryDocuments">Attachments</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="enquiryDocuments" id="enquiryDocuments" value={enquiryDocuments} maxLength="15"
                                                onChange={(e) => { handleChange(e, "enquiryDocuments") }}  />

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                );
            }

        }

        const renederShowCause = function () {
            if (showcause) {
                return (
                    <div className="form-section hide-sec" id="agreementCancelDetails">
                        <h3 className="categoryType"> Show Cause notice </h3>
                        <div className="form-section-inner">
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="proposedPunishmentByDA">Proposed Punishment by DA <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="proposedPunishmentByDA" id="proposedPunishmentByDA" value={proposedPunishmentByDA} maxLength="15"
                                                onChange={(e) => { handleChange(e, "proposedPunishmentByDA") }} required />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="showCauseNoticeNo">Show cause notice No <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="showCauseNoticeNo" id="showCauseNoticeNo" value={showCauseNoticeNo} maxLength="15"
                                                onChange={(e) => { handleChange(e, "showCauseNoticeNo") }} required />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="showCauseNoticeDate">Show cause notice Date <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="showCauseNoticeDate" id="showCauseNoticeDate" value={showCauseNoticeDate} maxLength="15" required />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="showCauseNoticeServingDate">Show cause Notice serving Date <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="showCauseNoticeServingDate" id="showCauseNoticeServingDate" value={showCauseNoticeServingDate} maxLength="15" required />

                                        </div>
                                    </div>
                                </div>

                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="explanationToShowCauseNotice">Explanation to Show cause notice <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="explanationToShowCauseNotice" id="explanationToShowCauseNotice" value={explanationToShowCauseNotice} maxLength="15"
                                                onChange={(e) => { handleChange(e, "explanationToShowCauseNotice") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="explanationToShowCauseNoticeAccepted">Explanation to Show cause notice Accepted Y/N <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="checkbox" name="explanationToShowCauseNoticeAccepted" id="explanationToShowCauseNoticeAccepted" value={explanationToShowCauseNoticeAccepted} maxLength="15"
                                                onChange={(e) => { handleChange(e, "explanationToShowCauseNoticeAccepted") }} required />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="punishmentAwarded">Punishment Awarded <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="punishmentAwarded" id="punishmentAwarded" value={punishmentAwarded} maxLength="15"
                                                onChange={(e) => { handleChange(e, "punishmentAwarded") }} required  />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="proceedingsNumber">Proceedings Number </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="proceedingsNumber" id="proceedingsNumber" value={proceedingsNumber} maxLength="15"
                                                onChange={(e) => { handleChange(e, "proceedingsNumber") }}  />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="proceedingsDate">Proceedings Date </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="proceedingsDate" id="proceedingsDate" value={proceedingsDate} maxLength="15"
                                                 />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="proceedingsServingDate">Proceedings Serving Date </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="proceedingsServingDate" id="proceedingsServingDate" value={proceedingsServingDate} maxLength="15"
                                                 />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="showCauseDocuments">Attachments</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="showCauseDocuments" id="showCauseDocuments" value={showCauseDocuments} maxLength="15"
                                                onChange={(e) => { handleChange(e, "showCauseDocuments") }}  />

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                );
            }
        }

        const renderCourtOrder = function () {
            if (courtorder) {
                return (
                    <div className="form-section">
                        <div className="row">
                            <div className="col-md-8 col-sm-8">
                                <h3 className="categoryType">Court Order </h3>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="courtCase">Court case filed if any Y/N </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="checkbox" name="courtCase" id="courtCase" value={courtCase} maxLength="15"
                                            onChange={(e) => { handleChange(e, "courtCase") }}  />

                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="courtOrderType">Order Type </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <select type="select" name="courtOrderType" id="courtOrderType" value={courtOrderType} maxLength="15"
                                            onChange={(e) => { handleChange(e, "courtOrderType") }}  >
                                            <option value="">Select Court order type</option>
                                            {renderOption(_this.state.courtOrderTypeList)}
                                        </select>

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="courtOrderNo">Order No </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="text" name="courtOrderNo" id="courtOrderNo" value={courtOrderNo} maxLength="15"
                                            onChange={(e) => { handleChange(e, "courtOrderNo") }}  />

                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="courtOrderDate">Order Date </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="text" name="courtOrderDate" id="courtOrderDate" value={courtOrderDate} maxLength="15"
                                             />

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="gistOfDirectionIssuedByCourt">Gist of Direction Issued by Court </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="text" name="gistOfDirectionIssuedByCourt" id="gistOfDirectionIssuedByCourt" value={gistOfDirectionIssuedByCourt} maxLength="15"
                                            onChange={(e) => { handleChange(e, "gistOfDirectionIssuedByCourt") }}  />

                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="courtDocuments">Attachments</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="text" name="courtDocuments" id="courtDocuments" value={courtDocuments} maxLength="15"
                                            onChange={(e) => { handleChange(e, "courtDocuments") }}  />

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
                <h3>Employee Disciplinary </h3>
                <form onSubmit={(e) => { addOrUpdate(e) }} >
                    <fieldset>
                        {renderSection()}
                        <br />
                        {renderMemo()}
                        <br />
                        {renderEnquiry()}
                        <br />
                        {renederShowCause()}
                        <br />
                        {renderCourtOrder()}
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
    <CancellationAgreement />,
    document.getElementById('root')
);
