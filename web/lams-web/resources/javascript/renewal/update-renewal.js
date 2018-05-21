class UpdateRenewal extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            agreement: {
                id: "",
                tenantId: tenantId,
                agreementNumber: "",
                acknowledgementNumber: "",
                stateId: "",
                action: "",
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
                renewal: "",
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
            renewalReasons: ["Reason 1", "Reason 2", "Reason 3", "Reason 4"],
            positionList: [],
            departmentList: [],
            designationList: [],
            userList: [],
            buttons: [],
            wfStatus: "",
            wfInitiator:"",
            rentInc: []

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
            url: baseUrl + "/hr-employee/employees/_search?tenantId=" + tenantId + "&departmentId=" + departmentId + "&designationId=" + designationId + "&active=true&asOnDate="+date,
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
        $('#lams-title').text("Renewal Of Agreement");
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
                action:"View",
                tenantId
            }).responseJSON["Agreements"][0] || {};

        var process = commonApiPost("egov-common-workflows", "process", "_search", {
            tenantId: tenantId,
            id: stateId
        }).responseJSON["processInstance"] || {};

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

        getDesignations(process.status, function (designations) {
            //console.log(designations);
            _this.setState({
                ..._this.state,
                designationList: designations
            });

        }, process.businessKey);


        if (!agreement.renewal) {
            agreement.renewal = {};
        }
        if (!agreement.workflowDetails) {
            agreement.workflowDetails = {};
        }

        var rentInc = commonApiPost("lams-services", "getrentincrements", "", {tenantId, basisOfAllotment:agreement.basisOfAllotment}).responseJSON;

        this.setState({
            ...this.state,
            agreement: agreement,
            departmentList: departmentList,
            //owner:process.owner.id,
            wfInitiator: process.initiatorPosition,
            wfStatus: process.status,
            workflow: workflow,
            buttons: _btns ? _btns : [],
            rentInc: rentInc,
            minRent: agreement.rent
        });

    }


    printNotice(agreement) {

        var commDesignation = commonApiPost("hr-masters", "designations", "_search", {name:"Commissioner", active:true,tenantId }).responseJSON["Designation"];
        var commDesignationId = commDesignation[0].id;
        var commissionerName =  commonApiPost("hr-employee", "employees", "_search", {
                          tenantId,
                          designationId: commDesignationId,
                          active: true,
                          asOnDate: moment(new Date()).format("DD/MM/YYYY")
                          }).responseJSON["Employee"] || [];
        var LocalityData = commonApiPost("egov-location/boundarys", "boundariesByBndryTypeNameAndHierarchyTypeName", "", { boundaryTypeName: "LOCALITY", hierarchyTypeName: "LOCATION", tenantId });
        var locality = getNameById(LocalityData["responseJSON"]["Boundary"], agreement.asset.locationDetails.locality);
        var cityGrade = !localStorage.getItem("city_grade") || localStorage.getItem("city_grade") == "undefined" ? (localStorage.setItem("city_grade", JSON.stringify(commonApiPost("tenant", "v1/tenant", "_search", { code: tenantId }).responseJSON["tenant"][0]["city"]["ulbGrade"] || {})), JSON.parse(localStorage.getItem("city_grade"))) : JSON.parse(localStorage.getItem("city_grade"));
        var ulbType = "Nagara Panchayat/Municipality";
        if (cityGrade.toLowerCase() === 'corp') {
            ulbType = "Municipal Corporation";
        }

        var renewalToDate = agreement.renewalDate.split("/")[0]+"/"+ agreement.renewalDate.split("/")[1]+"/" + (Number(agreement.renewalDate.split("/")[2]) + Number(agreement.timePeriod));

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
            { title: "S No", dataKey: "sNo" },
            { title: "Shop No", dataKey: "shopNo" },
            { title: "Name of the Leaseholder", dataKey: "leaseHolderName" },
            { title: "Current Rent", dataKey: "currentRent" },
            { title: "Shop Area", dataKey: "shopArea" },
            { title: "Land Area", dataKey: "landArea" },
            { title: "Rent per Sft", dataKey: "rentSft" },
            { title: "\"A\" Rent as per Regn Value  (1)", dataKey: "aRent" },
            { title: "\"B\" Rent of shops in the vicinity  (2)", dataKey: "bRent" },
            { title: "\"C\" 33% increase in current rent  (3)", dataKey: "cRent" },
            { title: "GST", dataKey: "gst" },
            { title: "Total Rent", dataKey: "totalRent" },
            { title: "Renewal Period", dataKey: "renewalPeriod" }
        ];

        var rows = [
            {
                "sNo": "1",
                "shopNo": agreement.referenceNumber,
                "leaseHolderName": agreement.allottee.name,
                "currentRent": agreement.rent,
                "shopArea": "",
                "landArea": "",
                "rentSft": "",
                "aRent": "",
                "bRent": "",
                "cRent": "",
                "gst": "",
                "totalRent": "",
                "renewalPeriod": agreement.timePeriod
            }
        ];

        var autoTableOptions = {
            tableLineColor: [0, 0, 0],
            tableLineWidth: 0.2,
            styles: {
                lineColor: [0, 0, 0],
                lineWidth: 0.2,
            },
            headerStyles: {
                textColor: [0, 0, 0],
                fillColor: [255, 255, 255],
                overflow: 'linebreak',
                columnWidth: 'wrap'
            },
            bodyStyles: {
                fillColor: [255, 255, 255],
                textColor: [0, 0, 0],
                overflow: 'linebreak',
                columnWidth: 'wrap'
            },
            alternateRowStyles: {
                fillColor: [255, 255, 255]
            }, startY: 135
        };

        var columns1 = [
            { title: "Particulars", dataKey: "particulars" },
            { title: "Amount", dataKey: "amount" },
            { title: "Cheque/DD/Challan No and Date", dataKey: "leaseHolderName" },

        ];

        var rows1 = [
            { "particulars": "Goodwill", "amount": agreement.goodWillAmount, "leaseHolderName": "" },
            { "particulars": "3 Months Rental Deposits", "amount": agreement.securityDeposit, "leaseHolderName": "" },
            { "particulars": "Total", "amount": "", "leaseHolderName": "" }
        ];

        var autoTableOptions1 = {
            tableLineColor: [0, 0, 0],
            tableLineWidth: 0.2,
            styles: {
                lineColor: [0, 0, 0],
                lineWidth: 0.2,
            },
            headerStyles: {
                textColor: [0, 0, 0],
                fillColor: [255, 255, 255],
                overflow: 'linebreak',
                columnWidth: 'wrap'
            },
            bodyStyles: {
                fillColor: [255, 255, 255],
                textColor: [0, 0, 0],
                overflow: 'linebreak',
                columnWidth: 'wrap'
            },
            alternateRowStyles: {
                fillColor: [255, 255, 255]
            }, startY: 180
        };

        var columns2 = [
            { title: "Particulars", dataKey: "particulars" },
            { title: "Amount", dataKey: "amount" }
        ];

        var rows2 = [
            { "particulars": "Monthly Rental", "amount": agreement.rent },
            { "particulars": "GST", "amount": "" },
        ];

        var autoTableOptions2 = {
            tableLineColor: [0, 0, 0],
            tableLineWidth: 0.2,
            styles: {
                lineColor: [0, 0, 0],
                lineWidth: 0.2,
            },
            headerStyles: {
                textColor: [0, 0, 0],
                fillColor: [255, 255, 255],
                overflow: 'linebreak',
                columnWidth: 'wrap'
            },
            bodyStyles: {
                fillColor: [255, 255, 255],
                textColor: [0, 0, 0],
                overflow: 'linebreak',
                columnWidth: 'wrap'
            },
            alternateRowStyles: {
                fillColor: [255, 255, 255]
            }, startY: 240
        };

        var doc = new jsPDF();

        doc.setFontType("bold");
        doc.setFontSize(13);
        doc.text(105, 20, "PROCEEDINGS OF THE COMMISSIONER, " + tenantId.split(".")[1].toUpperCase(), 'center');
        doc.text(105, 27, ulbType.toUpperCase(), 'center');
        doc.text(105, 34, "Present: " + commissionerName, 'center');

        doc.setFontType("normal");
        doc.setFontSize(11);
        doc.text(15, 50, 'Roc.No. ' + agreement.noticeNumber);
        doc.text(140, 50, 'Dt. ' + agreement.agreementDate);

        var paragraph = "Sub: Leases – Revenue Section – Shop No " + agreement.referenceNumber + " in " + agreement.asset.name + " Complex, " + locality + " - Remission of lease – Orders  - Issued";
        var lines = doc.splitTextToSize(paragraph, 180);
        doc.text(15, 65, lines);

        doc.text(15, 80, "Ref: 1. Request Letter by the leaseholder");
        doc.text(23, 85, "2. Resolution No …………… dt …………… of Municipal Council/Standing Committee");

        doc.text(105, 95, "><><><", 'center');

        doc.text(15, 105, "Orders:");
        doc.setLineWidth(0.5);
        doc.line(15, 106, 28, 106);

        var paragraph1 = "In the reference 1st cited, a request for renewal of existing lease Shop No " + agreement.referenceNumber + " in the " + agreement.asset.name + " Shopping Complex was received by this office and your application for renewal of lease was accepted by the Municipal Council/Standing Committee wide reference 2nd cited with the as per the rates mentioned below";
        var lines = doc.splitTextToSize(paragraph1, 180);
        doc.text(15, 115, lines);

        doc.autoTable(columns, rows, autoTableOptions);
        
        doc.text(15, 175, "Please fill (1)(2)(3) Manually");

        doc.autoTable(columns1, rows1, autoTableOptions1);

        var paragraph2 = "In pursuance of the Municipal Council/Standing Committee resolution and vide GO MS No 56 dt. 05.02.2011, existing lease of the said shop is being renewed for the period " + agreement.renewalDate + " to " + renewalToDate + " at following rates of rentals and taxes thereon.";
        var lines = doc.splitTextToSize(paragraph2, 180);
        doc.text(15, 220, lines);

        doc.autoTable(columns2, rows2, autoTableOptions2);

        doc.addPage();
        var paragraph3 = "The following terms and conditions are applicable for the renewal of lease."

            + "\n\t1. The leaseholder shall pay rent by 5th of the succeeding month"
            + "\n\t2. All the late payments of rentals will attract penalty and interest as applicable"
            + "\n\t3. The leaseholder shall not sub lease the premises in any case. If it is found that the premises are being sub let to any person, the lease shall stand cancelled without any prior notice."
            + "\n\t4. The D&O Trade License of the establishment shall be in the name of the leaseholder only."
            + "\n\t5. The leaseholder shall do business in the name of himself only."
            + "\n\t6. The leaseholder not to use the premises for any unlawful activities"
            + "\n\t7. The Goodwill and the Rental Deposits paid by the leaseholder shall be forfeited in the event of violation of terms and conditions in the agreement."

            + "\n\nHence you are requested to conclude an agreement duly registered with the SRO for the above mentioned lease within 15 days of receipt of this renewal letter without fail unless the renewal will stand cancelled without any further correspondence."

        var lines = doc.splitTextToSize(paragraph3, 180);
        doc.text(15, 30, lines);

        doc.text(120, 100, "Commissioner");
        doc.text(120, 105, tenantId.split(".")[1].charAt(0).toUpperCase() + tenantId.split(".")[1].slice(1) + ",");
        doc.text(120, 110, ulbType);

        doc.text(15, 115, "To");
        doc.text(15, 120, "The Leaseholder");
        doc.text(15, 125, "Copy to the concerned officials for necessary action");

        doc.save('RenewalNotice-' + agreement.agreementNumber + '.pdf');
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
                    acknowledgementNumber:obj.noticeData.acknowledgementNumber,
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

            $("#renewalOrderNumber").prop("disabled", false)
            $("#renewalOrderDate").prop("disabled", false)
            $("#renewalRent").prop("disabled", false)
            $("#timePeriod").prop("disabled", false)
            $("#securityDeposit").prop("disabled", false)
            $("#securityDepositDate").prop("disabled", false)
            $("#rentIncrementMethod").prop("disabled", false)
            $("#reasonForRenewal").prop("disabled", false)
            $("#documents").prop("disabled", false)
            $("#remarks").prop("disabled", false)
        }else{
          $("#documentSection").remove();
        }

        if (this.state.wfStatus === "Commissioner Approved") {
            $("#approvalDetailsSection").remove();
        }

        $('#renewalOrderDate').datepicker({
            format: 'dd/mm/yyyy',
            autoclose: true,
            defaultDate: ""
        });

        $('#renewalOrderDate').on('change blur', function (e) {
            _this.setState({
                agreement: {
                    ..._this.state.agreement,
                    renewal: {
                        ..._this.state.agreement.renewal,
                        "renewalOrderDate": $("#renewalOrderDate").val()
                    }
                }
            });
        });


        $('#securityDepositDate').datepicker({
            format: 'dd/mm/yyyy',
            autoclose: true,
            defaultDate: ""
        });

        $('#securityDepositDate').on('change blur', function (e) {
            _this.setState({
                agreement: {
                    ..._this.state.agreement,
                    "securityDepositDate": $("#securityDepositDate").val()
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

        if ($('#update-renewal').valid()) {
            var ID = e.target.id;
            var _this = this;
            var agreement = Object.assign({}, _this.state.agreement);

            agreement.action = "renewal";
            agreement.workflowDetails.action = ID;
            agreement.workflowDetails.status = this.state.wfStatus;

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


            if (ID === "Reject") {

                if (!agreement.workflowDetails.comments)
                    return showError("Please enter the Comments, If you are rejecting");

            }

            if (ID === "Forward") {

                if (!agreement.workflowDetails.department)
                    return showError("Please Select the Department");
                else if (!agreement.workflowDetails.designation)
                    return showError("Please Select the Designation");
                else if (!agreement.workflowDetails.assignee)
                    return showError("Please Select the Employee");

            }

            if (ID.toLowerCase() === "approve"){
              agreement.workflowDetails.assignee = _this.state.wfInitiator;
            }
            //console.log("Agreement", agreement);

            if (agreement.documents && agreement.documents.constructor == FileList) {
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
                                    url: baseUrl + "/lams-services/agreements/renewal/_update?tenantId=" + tenantId,
                                    type: 'POST',
                                    dataType: 'json',
                                    data: JSON.stringify(body),
                                    contentType: 'application/json',
                                    headers: {
                                        'auth-token': authToken
                                    },
                                    success: function (res) {
                                      var ackNo;

                                      if(ID==="Approve"){
                                        ackNo= res.Agreements[0].agreementNumber;
                                      }else {
                                        ackNo = res.Agreements[0].acknowledgementNumber;
                                      }
                                        if (ID === "Print Notice") {
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
                                                    if (window.opener)
                                                        window.opener.location.reload();
                                                    if (res1 && res1.Employee && res1.Employee[0].name)
                                                        window.location.href = `app/acknowledgement/common-ack.html?wftype=Renewal&action=${ID}&name=${res1.Employee[0].code}::${res1.Employee[0].name}&ackNo=${ackNo}`;
                                                    else
                                                        window.location.href = `app/acknowledgement/common-ack.html?wftype=Renewal&action=${ID}&name=&ackNo=${ackNo}`;

                                                },
                                                error: function (err) {
                                                    if (window.opener)
                                                        window.opener.location.reload();
                                                    window.location.href = `app/acknowledgement/common-ack.html?wftype=Renewal&action=${ID}&name=&ackNo=${ackNo}`;
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

                var body = {
                    "RequestInfo": requestInfo,
                    "Agreement": agreement
                };

                $.ajax({
                    url: baseUrl + "/lams-services/agreements/renewal/_update?tenantId=" + tenantId,
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify(body),
                    contentType: 'application/json',
                    headers: {
                        'auth-token': authToken
                    },
                    success: function (res) {
                      var ackNo;

                      if(ID==="Approve"){
                        ackNo= res.Agreements[0].agreementNumber;
                      }else {
                        ackNo = res.Agreements[0].acknowledgementNumber;
                      }
                        if (ID === "Print Notice") {
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
                                    if (window.opener)
                                        window.opener.location.reload();
                                    if (res1 && res1.Employee && res1.Employee[0].name){
                                        window.location.href = `app/acknowledgement/common-ack.html?wftype=Renewal&action=${ID}&name=${res1.Employee[0].code}::${res1.Employee[0].name}&ackNo=${ackNo}`;
                                    }else
                                        window.location.href = `app/acknowledgement/common-ack.html?wftype=Renewal&action=${ID}&name=&ackNo=${ackNo}`;

                                },
                                error: function (err) {
                                    if (window.opener)
                                        window.opener.location.reload();
                                    window.location.href = `app/acknowledgement/common-ack.html?wftype=Renewal&action=${ID}&name=&ackNo=${ackNo}`;
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
        let { agreement, renewalReasons, buttons, rentInc, minRent } = this.state;
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

        const renderOptionForRentInc = function (data) {
            if (data) {
                return data.map((item, ind) => {
                    return (<option key={ind} value={typeof item == "object" ? item.id : item}>
                        {typeof item == "object" ? item.percentage : item}
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

        const renederRenewalDetails = function () {
            return (
                <div className="form-section hide-sec" id="agreementRenewalDetails">
                    <h3 className="categoryType">Renewal Details </h3>
                    <div className="form-section-inner">

                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="renewalOrderNumber">Renewal Order Number
                                    <span>*</span>
                                        </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <input type="text" name="renewalOrderNumber" id="renewalOrderNumber" value={renewal.renewalOrderNumber}
                                            onChange={(e) => { handleChangeTwoLevel(e, "renewal", "renewalOrderNumber") }} required disabled />
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="renewalOrderDate">Renewal Order Date
                                    <span>*</span>
                                        </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="text-no-ui">
                                            <span className="glyphicon glyphicon-calendar"></span>
                                            <input type="text" className="datepicker" name="renewalOrderDate" id="renewalOrderDate" value={renewal.renewalOrderDate}
                                                onChange={(e) => { handleChangeTwoLevel(e, "renewal", "renewalOrderDate") }} required disabled />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">

                                        <label for="renewalRent" className="categoryType">Rent
                                    <span>*</span>
                                        </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="text-no-ui">
                                            <span>₹</span>
                                            <input type="number" min={minRent} name="renewalRent" id="renewalRent" value={agreement.rent}
                                                onChange={(e) => { handleChange(e, "rent") }} required disabled />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="timePeriod">Time Period
                                    <span>*</span>
                                        </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <select name="timePeriod" id="timePeriod" className="selectStyle" value={agreement.timePeriod} onChange={(e) => { handleChange(e, "timePeriod") }} required disabled >
                                            <option value="">Select </option>
                                            <option value="1">1</option>
                                            <option value="2">2</option>
                                            <option value="3">3 </option>
                                            <option value="4">4 </option>
                                            <option value="5">5 </option>
                                        </select>
                                    </div>
                                </div>
                            </div>

                        </div>

                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="securityDeposit">Security Deposit
                                  <span>*</span>
                                        </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <input type="number" min="0" id="securityDeposit" name="securityDeposit" onChange={(e) => { handleChange(e, "securityDeposit") }} value={agreement.securityDeposit} required disabled />
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="securityDepositDate">Security Deposit Received Date (DD/MM/YYYY)
                                    <span>*</span>
                                        </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="text-no-ui">
                                            <span className="glyphicon glyphicon-calendar"></span>
                                            <input type="text" className="datepicker" id="securityDepositDate" name="securityDepositDate" onChange={(e) => { handleChange(e, "securityDepositDate") }} value={agreement.securityDepositDate} required disabled />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="row">
                            <div className="col-sm-6" id="rentCalculatedMethod">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="rentIncrementMethod">Method by increase in rent calculated during Renewal
                                    <span>*</span>
                                        </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <select name="rentIncrementMethod" id="rentIncrementMethod" value={agreement.rentIncrementMethod.id} className="selectStyle" onChange={(e) => { handleChangeTwoLevel(e, "rentIncrementMethod", "id") }} required disabled >
                                            <option value="">Choose Percentage</option>
                                            {renderOptionForRentInc(rentInc)}
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="reasonForRenewal">Renewal Reason
                                    <span>*</span>
                                        </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <select name="reasonForRenewal" id="reasonForRenewal" className="selectStyle" value={renewal.reasonForRenewal} onChange={(e) => { handleChangeTwoLevel(e, "renewal", "reasonForRenewal") }} required disabled >
                                            <option value="" >Select</option>
                                            {renderOption(renewalReasons)}
                                        </select>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="row">

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
                                        <label for="remarks">Remarks </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <textarea name="remarks" id="remarks" value={remarks}
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
        const renderDocumentsList=function()
        {
          if (documents && documents.length>0) {
             var CONST_API_GET_FILE = "/filestore/v1/files/id?tenantId=" + tenantId + "&fileStoreId=";
            return documents.map((item,index)=>
            {                   return (<tr key={index}>
                                      <td>{index+1}.</td>
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
                                                onChange={(e) => { handleChangeTwoLevel(e, "workflowDetails", "department") }}  >
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
                                                onChange={(e) => { handleChangeTwoLevel(e, "workflowDetails", "designation") }}  >
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
                    <div className="form-section" id="approvalDetailsSection">
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
        return (
            <div>
                <h3>Renewal Of Agreement </h3>
                <form className="update-renewal" id="update-renewal" >
                    <fieldset>
                        {renderAssetDetails()}
                        {renderAllottee()}
                        {renderAgreementDetails()}
                        {renederRenewalDetails()}
                        {renderDocuments()}
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
    <UpdateRenewal />,
    document.getElementById('root')
);
