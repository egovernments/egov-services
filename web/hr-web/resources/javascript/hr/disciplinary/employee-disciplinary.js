var CONST_API_GET_FILE = "/filestore/v1/files/id?tenantId=" + tenantId + "&fileStoreId=";

const makeAjaxUpload = function (file, cb) {
    if (file.constructor == File) {
        let formData = new FormData();
        formData.append("jurisdictionId", tenantId);
        formData.append("module", "ASSET");
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

const uploadFiles = function (body, cb) {

    var files = [];

    files = files.concat(body.Disciplinary.memoDocuments);
    files = files.concat(body.Disciplinary.courtDocuments);
    files = files.concat(body.Disciplinary.showCauseDocuments);
    files = files.concat(body.Disciplinary.enquiryDocuments);

    console.log(files);
    if (files.length) {
        console.log(files, body.Disciplinary.memoDocuments)

        var breakout = 0;
        var docs = [];
        let counter = files.length;
        for (let j = 0; j < files.length; j++) {
            if (files[j] instanceof File) {
                makeAjaxUpload(files[j], function (err, res) {
                    if (breakout == 1)
                        return;
                    else if (err) {
                        cb(err);
                        breakout = 1;
                    } else {
                        counter--;
                        docs.push({ fileStoreId: res.files[0].fileStoreId });
                        if (counter == 0) {
                            body.Disciplinary.disciplinaryDocuments = body.Disciplinary.disciplinaryDocuments.concat(docs);
                            delete body.Disciplinary.memoDocuments;
                            delete body.Disciplinary.courtDocuments;
                            delete body.Disciplinary.showCauseDocuments;
                            delete body.Disciplinary.enquiryDocuments;

                            cb(null, body);
                        }
                    }
                })
            } else {
                cb(new Error("Not a File"));
            }
        }
    } else {
        cb(null, body);
    }
}

class EmployeeDisciplinary extends React.Component {
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
                "punishmentImplemented": "",
                "endDateOfPunishment": "",
                "proceedingsNumber": "",
                "proceedingsDate": "",
                "proceedingsServingDate": "",
                "courtCase": false,
                "courtOrderType": "",
                "courtOrderNo": "",
                "courtOrderDate": "",
                "gistOfDirectionIssuedByCourt": "",
                "tenantId": tenantId,
                "memoDocuments": [],
                "enquiryDocuments": [],
                "showCauseDocuments": [],
                "courtDocuments": [],
                "disciplinaryDocuments": []
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
            courtOrderTypeList: [],
            disciplianryAuthorityList:[],
            removedFiles: []

        }
        this.handleChangeTwoLevel = this.handleChangeTwoLevel.bind(this);
        this.handleSectionChange = this.handleSectionChange.bind(this);
        this.handleChange = this.handleChange.bind(this);
        this.addOrUpdate = this.addOrUpdate.bind(this);
        this.setInitialState = this.setInitialState.bind(this);
        this.addToRemovedFiles = this.addToRemovedFiles.bind(this);
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
        let type = getUrlVars()["type"];

        if (disciplinary.courtCase) {
            if (!disciplinary.courtOrderType)
                return showError("Please fill Court Order Type");
            if (!disciplinary.courtOrderNo)
                return showError("Please fill Court Order Number");
            if (!disciplinary.courtOrderDate)
                return showError("Please fill Court Order Date");
        }

        let removedFiles = _this.state.removedFiles;

        if (removedFiles.length) {
            let docs = [];

            for (let i = 0; i < disciplinary.disciplinaryDocuments.length; i++) {
                if (removedFiles.indexOf(disciplinary.disciplinaryDocuments[i].fileStoreId) === -1)
                    docs.push(disciplinary.disciplinaryDocuments[i]);
            }

            disciplinary.disciplinaryDocuments = docs;
        }

        delete disciplinary.orderDate;
        delete disciplinary.orderNo;


        var body = {
            "RequestInfo": requestInfo,
            "Disciplinary": disciplinary
        };

        uploadFiles(body, function (err1, _body) {
            if (err1) {
                showError(err1);
            } else {
                delete disciplinary.memoDocuments;
                delete disciplinary.courtDocuments;
                delete disciplinary.showCauseDocuments;
                delete disciplinary.enquiryDocuments;
                $.ajax({
                    url: baseUrl + "/hr-employee/disciplinary/_create?tenantId=" + tenantId,
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify(_body),
                    contentType: 'application/json',
                    headers: {
                        'auth-token': authToken
                    },
                    success: function (res) {
                        showSuccess("Created successfully Created");
                        window.location.href = `app/hr/disciplinary/search-disciplinary.html?type=create`;
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
        })

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

        if (name === "memoDocuments" || name === "courtDocuments" || name === "showCauseDocuments" || name === "enquiryDocuments") {

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
                    disciplinarySet: {
                        ...this.state.disciplinarySet,
                        [name]: [e.currentTarget.files[0]]
                    }
                })
            } else {
                this.setState({
                    disciplinarySet: {
                        ...this.state.disciplinarySet,
                        [name]: []
                    }
                })
            }


        } else {

            _this.setState({
                ..._this.state,
                disciplinarySet: {
                    ..._this.state.disciplinarySet,
                    [name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
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
        let type = getUrlVars()["type"];

        getDropdown("assignments_designation", function (res) {
            _this.setState({
                ..._this.state,
                designationList: res
            })
        });

        getCommonMaster("hr-employee", "courtordertype", function (err, res) {
            if (res && res["CourtOrderType"]) {
                console.log(res);
                _this.setState({
                    ..._this.state,
                    courtOrderTypeList: res["CourtOrderType"]
                })
            }
        });

        
        getCommonMaster("hr-employee", "disciplinaryauthority", function (err, res) {
            if (res && res["DisciplinaryAuthority"]) {
                console.log(res);
                _this.setState({
                    ..._this.state,
                    disciplianryAuthorityList: res["DisciplinaryAuthority"]
                })
            }
        });

        if (type === "create") {
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
                            designation: getNameById(_this.state.designationList, obj.assignments[ind].designation),
                        },
                        disciplinarySet: {
                            ..._this.state.disciplinarySet,
                            employeeId: obj.id
                        },
                        memo : true
                    })
                }
            });
        } else {

            getCommonMasterById("hr-employee", "disciplinary", id, function (err, res1) {
                if (res1 && res1["Disciplinary"] && res1["Disciplinary"]["0"]) {
                    getCommonMasterById("hr-employee", "employees", res1["Disciplinary"]["0"].employeeId, function (err, res) {
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

                            let enquiry = false;
                            let showcause = false;
                            let courtorder = false;


                            if (res1["Disciplinary"]["0"]["enquiryOfficerName"]) {
                                enquiry = true
                                $('#enquiry, #showcause').prop("disabled", false);
                            }
                            if (res1["Disciplinary"]["0"]["showCauseNoticeNo"]) {
                                showcause = true
                                $('#showcause, #courtorder').prop("disabled", false);
                            }
                            if (res1["Disciplinary"]["0"]["courtOrderNo"]) {
                                courtorder = true
                                $('#courtorder').prop("disabled", false);
                            }

                            res1["Disciplinary"]["0"].memoDocuments = [];
                            res1["Disciplinary"]["0"].enquiryDocuments = [];
                            res1["Disciplinary"]["0"].showCauseDocuments = [];
                            res1["Disciplinary"]["0"].courtDocuments = [];

                            _this.setState({
                                ..._this.state,
                                employee: {
                                    name: obj.name,
                                    code: obj.code,
                                    designation: getNameById(_this.state.designationList, obj.assignments[ind].designation),
                                },
                                disciplinarySet: res1["Disciplinary"]["0"],
                                memo: true,
                                enquiry,
                                showcause,
                                courtorder
                            })
                        }
                    });

                }
            })
        }

    }

    componentDidUpdate() {

        var _this = this;
        let type = getUrlVars()["type"];

        if (type === "view") {
            $("input,select,textarea").prop("disabled", true);

        }

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



        $('#endDateOfPunishment').datepicker({
            format: 'dd/mm/yyyy',
            //startDate: new Date(),
            autoclose: true,

        });

        $('#endDateOfPunishment').on('changeDate', function (e) {
            if (_this.state.disciplinarySet.endDateOfPunishment != e.target.value)
                _this.setState({

                    disciplinarySet: {
                        ..._this.state.disciplinarySet,
                        "endDateOfPunishment": $("#endDateOfPunishment").val()

                    }
                });
        });


    }

    addToRemovedFiles(fileId, addBack) {
        var removedFiles = Object.assign([], this.state.removedFiles);
        if (addBack) {
            removedFiles.splice(removedFiles.indexOf(fileId), 1);
        } else {
            removedFiles.push(fileId);
        }
        this.setState({
            removedFiles: Object.assign([], removedFiles)
        })
    }


    render() {
        var _this = this;
        let mode = getUrlVars()["type"];
        let { handleChange, handleSectionChange, handleChangeTwoLevel, addOrUpdate, addToRemovedFiles } = this;
        let { disciplinarySet, memo, enquiry, showcause, courtorder, employee, courtOrderTypeList, removedFiles } = this.state;
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
            punishmentImplemented,
            endDateOfPunishment,
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
            courtDocuments,
            disciplinaryDocuments

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

        const renderFileDelBtn = function (fileId) {
            if (removedFiles.indexOf(fileId) === -1)
                return (
                    <button type="button" className="btn btn-close" style={{ "color": "#000000" }} onClick={() => addToRemovedFiles(fileId)}>Delete</button>
                )
            else
                return (
                    <button type="button" className="btn btn-close" style={{ "color": "#000000" }} onClick={() => addToRemovedFiles(fileId, true)}>Undo</button>
                )
        }

        const renderFileBody = function (fles) {
            return fles.map(function (file, ind) {
                return (
                    <tr key={ind}>
                        <td>{ind + 1}</td>
                        <td>{file.documentType}</td>
                        <td>
                            <a href={window.location.origin + CONST_API_GET_FILE + file.fileStoreId} target="_blank">
                                Download
                      </a>
                        </td>
                        <td>{getUrlVars()["type"] == "update" ? renderFileDelBtn(file.fileStoreId) : ""}</td>
                    </tr>
                )
            })
        }

        const showAttachedFiles = function () {
            if (disciplinaryDocuments.length) {
                return (
                    <table id="fileTable" className="table table-bordered">
                        <thead>
                            <tr>
                                <th>Sr. No.</th>
                                <th>Name</th>
                                <th>File</th>
                                <th>Action</th>
                            </tr>
                        </thead>
                        <tbody id="agreementSearchResultTableBody">
                            {
                                renderFileBody(disciplinaryDocuments)
                            }
                        </tbody>

                    </table>
                )
            }
        }

        const showActionButton = function () {
            if (mode === "create" || mode === "update") {
                return (<button type="submit" className="btn btn-submit">{mode === "update" ? "Update" : "Create"}</button>);
            }
        };

        const renderSection = function () {
            return (
                <div className="form-section" id="allotteeDetailsBlock">

                    <div className="form-section-inner">
                        <div className="row">
                            <div className="col-sm-3">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="allotteeName"> Memo </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="checkbox" name="memo" id="memo" checked={memo} maxLength="200"
                                            onChange={(e) => { handleSectionChange(e, "memo") }} />
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-3">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="mobileNumber">Enquiry </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="checkbox" name="enquiry" id="enquiry" checked={enquiry} maxLength="200"
                                            onChange={(e) => { handleSectionChange(e, "enquiry") }} />
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-3">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="allotteeName"> Show Cause Notice </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="checkbox" name="showcause" id="showcause" checked={showcause} maxLength="200"
                                            onChange={(e) => { handleSectionChange(e, "showcause") }} disabled />
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-3">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="mobileNumber">Court Order </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="checkbox" name="courtorder" id="courtorder" checked={courtorder} maxLength="200"
                                            onChange={(e) => { handleSectionChange(e, "courtorder") }} disabled />
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
                                            <input type="text" name="code" id="code" value={employee.code} maxLength="200"
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
                                            <input type="text" name="name" id="name" value={employee.name} maxLength="200"
                                                onChange={(e) => { handleChangeTwoLevel(e, "employee", "name") }} disabled />
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
                                            <input type="text" name="designation" id="designation" value={employee.designation} maxLength="200"
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
                                            <textarea type="text" name="gistCase" id="gistCase" value={gistCase} maxLength="1000"
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
                                            <select type="text" name="disciplinaryAuthority" id="disciplinaryAuthority" value={disciplinaryAuthority} maxLength="200"
                                                onChange={(e) => { handleChange(e, "disciplinaryAuthority") }} required >
                                                <option value="">Select Disciplinary Authority</option>
                                                {renderOption(_this.state.disciplianryAuthorityList)}
                                            </select>

                                        </div>
                                    </div>
                                </div>
                                {/* <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="orderNo">Order No <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="orderNo" id="orderNo" value={orderNo} maxLength="200"
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
                                            <input type="text" name="orderDate" id="orderDate" defaultValue={orderDate} required />

                                        </div>
                                    </div>
                                </div> */}
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="memoNo">Memo No <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="memoNo" id="memoNo" value={memoNo} maxLength="200"
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
                                            <input type="text" name="memoDate" id="memoDate" defaultValue={memoDate} required />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="memoServingDate">Memo serving Date</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="memoServingDate" id="memoServingDate" defaultValue={memoServingDate} />

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
                                            <input type="text" name="dateOfReceiptMemoDate" id="dateOfReceiptMemoDate" defaultValue={dateOfReceiptMemoDate} />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="explanationAccepted">Explanation Accepted Y/N</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="checkbox" name="explanationAccepted" id="explanationAccepted" checked={explanationAccepted} maxLength="200"
                                                onChange={(e) => { handleChange(e, "explanationAccepted") }} />

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
                                            <input type="text" name="chargeMemoNo" id="chargeMemoNo" value={chargeMemoNo} maxLength="200"
                                                onChange={(e) => { handleChange(e, "chargeMemoNo") }} />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="chargeMemoDate">Charge Memo Date</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="chargeMemoDate" id="chargeMemoDate" defaultValue={chargeMemoDate} />

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
                                            <input type="text" name="dateOfReceiptToChargeMemoDate" id="dateOfReceiptToChargeMemoDate" defaultValue={dateOfReceiptToChargeMemoDate} />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="accepted">Accepted Y/N</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="checkbox" name="accepted" id="accepted" checked={accepted} maxLength="200"
                                                onChange={(e) => { handleChange(e, "accepted") }} />

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
                                            <input type="file" name="memoDocuments" id="memoDocuments" maxLength="200"
                                                onChange={(e) => { handleChange(e, "memoDocuments") }} multiple />

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
                                            <input type="text" name="dateOfAppointmentOfEnquiryOfficerDate" id="dateOfAppointmentOfEnquiryOfficerDate" defaultValue={dateOfAppointmentOfEnquiryOfficerDate} required />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="enquiryOfficerName">Enquiry officer Name <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="enquiryOfficerName" id="enquiryOfficerName" value={enquiryOfficerName} maxLength="200"
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
                                            <input type="text" name="enquiryOfficerDesignation" id="enquiryOfficerDesignation" value={enquiryOfficerDesignation} maxLength="200"
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
                                            <input type="text" name="dateOfAppointmentOfPresentingOfficer" id="dateOfAppointmentOfPresentingOfficer" defaultValue={dateOfAppointmentOfPresentingOfficer} />

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
                                            <input type="text" name="presentingOfficerName" id="presentingOfficerName" value={presentingOfficerName} maxLength="200"
                                                onChange={(e) => { handleChange(e, "presentingOfficerName") }} />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="presentingOfficerDesignation">Presenting Officer Designation</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="presentingOfficerDesignation" id="presentingOfficerDesignation" value={presentingOfficerDesignation} maxLength="200"
                                                onChange={(e) => { handleChange(e, "presentingOfficerDesignation") }} />

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
                                            <textarea type="text" name="findingsOfEO" id="findingsOfEO" value={findingsOfEO} maxLength="1000"
                                                onChange={(e) => { handleChange(e, "findingsOfEO") }} />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="enquiryReportSubmittedDate">Enquiry Report submitted Date</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="enquiryReportSubmittedDate" id="enquiryReportSubmittedDate" defaultValue={enquiryReportSubmittedDate} />

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
                                            <input type="text" name="dateOfCommunicationOfER" id="dateOfCommunicationOfER" defaultValue={dateOfCommunicationOfER} />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="dateOfSubmissionOfExplanationByCO">Date of submission of explanation by CO</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="dateOfSubmissionOfExplanationByCO" id="dateOfSubmissionOfExplanationByCO" defaultValue={dateOfSubmissionOfExplanationByCO} />

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
                                            <input type="checkbox" name="acceptanceOfExplanation" id="acceptanceOfExplanation"  checked={acceptanceOfExplanation} maxLength="200"
                                                onChange={(e) => { handleChange(e, "acceptanceOfExplanation") }} />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="enquiryDocuments">Attachments</label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="file" name="enquiryDocuments" id="enquiryDocuments" maxLength="200"
                                                onChange={(e) => { handleChange(e, "enquiryDocuments") }} multiple />

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
                                            <textarea type="text" name="proposedPunishmentByDA" id="proposedPunishmentByDA" value={proposedPunishmentByDA} maxLength="1000" onChange={(e) => { handleChange(e, "proposedPunishmentByDA") }} required />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="showCauseNoticeNo">Show cause notice No <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="showCauseNoticeNo" id="showCauseNoticeNo" value={showCauseNoticeNo} maxLength="200"
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
                                            <input type="text" name="showCauseNoticeDate" id="showCauseNoticeDate" defaultValue={showCauseNoticeDate} required />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="showCauseNoticeServingDate">Show cause Notice serving Date <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="showCauseNoticeServingDate" id="showCauseNoticeServingDate" defaultValue={showCauseNoticeServingDate} required />

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
                                            <textarea type="text" name="explanationToShowCauseNotice" id="explanationToShowCauseNotice" value={explanationToShowCauseNotice} maxLength="1000"
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
                                            <input type="checkbox" name="explanationToShowCauseNoticeAccepted" id="explanationToShowCauseNoticeAccepted" checked={explanationToShowCauseNoticeAccepted} maxLength="200"
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
                                            <input type="text" name="punishmentAwarded" id="punishmentAwarded" value={punishmentAwarded} maxLength="200"
                                                onChange={(e) => { handleChange(e, "punishmentAwarded") }} required />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="proceedingsNumber">Proceedings Number </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="proceedingsNumber" id="proceedingsNumber" value={proceedingsNumber} maxLength="200"
                                                onChange={(e) => { handleChange(e, "proceedingsNumber") }} />

                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="punishmentImplemented">Punishment implemented Y/N </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="checkbox" name="punishmentImplemented" id="punishmentImplemented"  checked={punishmentImplemented}
                                                onChange={(e) => { handleChange(e, "punishmentImplemented") }}  />

                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="endDateOfPunishment">End date of Punishment operation period </label>
                                        </div>
                                        <div className="col-sm-6 label-view-text">
                                            <input type="text" name="endDateOfPunishment" id="endDateOfPunishment" defaultValue={endDateOfPunishment} maxLength="200"  />

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
                                            <input type="text" name="proceedingsDate" id="proceedingsDate" defaultValue={proceedingsDate}
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
                                            <input type="text" name="proceedingsServingDate" id="proceedingsServingDate" defaultValue={proceedingsServingDate} />

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
                                            <input type="file" name="showCauseDocuments" id="showCauseDocuments" maxLength="200"
                                                onChange={(e) => { handleChange(e, "showCauseDocuments") }} multiple />

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
                                        <input type="checkbox" name="courtCase" id="courtCase" checked={courtCase} maxLength="200"
                                            onChange={(e) => { handleChange(e, "courtCase") }} />

                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="courtOrderType">Order Type </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <select type="select" name="courtOrderType" id="courtOrderType" value={courtOrderType} maxLength="200"
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
                                        <input type="text" name="courtOrderNo" id="courtOrderNo" value={courtOrderNo} maxLength="200"
                                            onChange={(e) => { handleChange(e, "courtOrderNo") }} />

                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="courtOrderDate">Order Date </label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="text" name="courtOrderDate" id="courtOrderDate" defaultValue={courtOrderDate} />

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
                                        <textarea type="text" name="gistOfDirectionIssuedByCourt" id="gistOfDirectionIssuedByCourt" value={gistOfDirectionIssuedByCourt} maxLength="1000"
                                            onChange={(e) => { handleChange(e, "gistOfDirectionIssuedByCourt") }} />

                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="courtDocuments">Attachments</label>
                                    </div>
                                    <div className="col-sm-6 label-view-text">
                                        <input type="file" name="courtDocuments" id="courtDocuments" maxLength="200"
                                            onChange={(e) => { handleChange(e, "courtDocuments") }} multiple />

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
                        {showAttachedFiles()}
                        <br />
                        <div className="text-center">
                            {showActionButton()}&nbsp;&nbsp;
                            <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>
                        </div>

                    </fieldset>
                </form>
            </div>
        );
    }

}
ReactDOM.render(
    <EmployeeDisciplinary />,
    document.getElementById('root')
);
