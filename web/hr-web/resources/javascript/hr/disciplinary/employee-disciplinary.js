class CancellationAgreement extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            disciplinarySet: {
                memoSet: {},
                enquirySet: {},
                showcauseSet: {},
                courtorderSet: {}

            },
            memo: false,
            enquiry: false,
            showcause: false,
            courtorder: false,
            designationList: [],
            userList: []

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
        // widow.close();
        open(location, '_self').close();
    }

    addOrUpdate(e) { }

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
                courtorder: _this.state.courtorder ? false :true
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
        var _this = this;


    }

    componentDidMount() {

        var _this = this;
        console.log(_this.state.memo, " ", _this.state.enquiry, " ", _this.state.showcause, " ", _this.state.courtorder);
        if (_this.state.memo && _this.state.enquiry && _this.state.showcause) {
            $('#memo, #enquiry, #showcause,  #courtorder').prop("disabled", true);
        } else if (_this.state.memo && _this.state.enquiry) {
            $('#memo, #enquiry, #showcause').prop("disabled", false);
            $('#courtorder').prop("disabled", true);
        } else if (_this.state.memo) {
            $('#memo, #enquiry').prop("disabled", false);
            $('#courtorder, #showcause').prop("disabled", true);
        } else {
            $('#memo').prop("disabled", false);
            $('#courtorder, #showcause, #enquiry').prop("disabled", true);
        }

        var ad = this.state.agreement.agreementDate;

        $('#orderDate').datepicker({
            format: 'dd/mm/yyyy',
            startDate: new Date(ad.split("/")[2], ad.split("/")[1] - 1, ad.split("/")[0]),
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


    render() {
        var _this = this;
        let { handleChange, handleSectionChange, handleChangeTwoLevel, addOrUpdate } = this;
        let { disciplinarySet, memo, enquiry, showcause, courtorder } = this.state;
        let { memoSet } = this.state.disciplinarySet;

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
                                            onChange={(e) => { handleSectionChange(e, "memo") }} required />
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
                                            onChange={(e) => { handleSectionChange(e, "enquiry") }} required disabled />
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
                                            onChange={(e) => { handleSectionChange(e, "showcause") }} required disabled />
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
                                            onChange={(e) => { handleSectionChange(e, "courtorder") }} required disabled />
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
                                            <label htmlFor="allotteeName"> Code </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="mobileNumber">Name </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="aadhaarNumber">Designation </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="panNo">Gist of the Case</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="emailId">Disciplinary Authority</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="address">Order No and Date of Suspension (If Any)</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="allotteeName">Memo No and Date</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="mobileNumber">Memo serving Date</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="aadhaarNumber">Date of Receipt of Explanation to memo</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="panNo">Explanation Accepted Y/N</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="emailId">Charge Memo No and Date</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="address">Date of Receipt of written statement to charge memo</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="emailId">Accepted Y/N</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="address">Attachments</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

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
                                            <label htmlFor="allotteeName"> Date of Appointment of Enquiry officer </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="mobileNumber">Enquiry officer Name & Designation </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="aadhaarNumber">Date of Appointment of Presenting Officer </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="panNo">Presenting Officer Name & Designation</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="emailId">Findings of EO</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="address">Enquiry Report submitted Date</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="allotteeName">Date of Communication of ER to delinquent officer</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="mobileNumber">Date of submission of explanation by CO</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="aadhaarNumber">Acceptance of explanation Y/N</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="panNo">Attachments</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

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
                                            <label htmlFor="allotteeName">Proposed Punishment by DA </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="mobileNumber">Show cause notice No and Date </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="aadhaarNumber">Show cause Notice serving Date </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="panNo">Explanation to Show cause notice </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="emailId">Explanation to Show cause notice Accepted Y/N </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="address">Punishment Awarded </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="allotteeName">Proceedings Number and Date </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="mobileNumber">Proceedings Serving Date </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="panNo">Attachments</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                                onChange={(e) => { handleChange(e, "cancellation") }} required />

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
                                        <label htmlFor="emailId">Court case filed if any Y/N </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                            onChange={(e) => { handleChange(e, "cancellation") }} required />

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="allotteeName">Order Type </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                            onChange={(e) => { handleChange(e, "cancellation") }} required />

                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="mobileNumber">Order No & Date </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                            onChange={(e) => { handleChange(e, "cancellation") }} required />

                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="allotteeName">Gist of Direction Issued by Court </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                            onChange={(e) => { handleChange(e, "cancellation") }} required />

                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="panNo">Attachments</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="text" name="orderNo" id="orderNo" value={""} maxLength="15"
                                            onChange={(e) => { handleChange(e, "cancellation") }} required />

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
