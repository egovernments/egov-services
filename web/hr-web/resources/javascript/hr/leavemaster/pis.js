var flag = 0;
const defaultState = {
    employees: [],
    searchSet: {
        name: "",
        employee: "",
        department: "",
        designation: "",
        leaveType: "",
        noOfDay: "",
        noOfLeave: "",
        calendarYear: new Date().getFullYear()
    },
    isSearchClicked: false,
    readonly: false
};
const callAjax = (type, empArr, cb) => {
    if (type == "update") {
        var counter = empArr.length, breakOut = 0;
        for (let i = 0; i < empArr.length; i++) {
            $.ajax({
                url: baseUrl + "/hr-leave/leaveopeningbalances" + (empArr[i].id ? "/" + empArr[i].id + "/_update" : "/_create") + "?tenantId=" + tenantId,
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify({
                    RequestInfo: requestInfo,
                    LeaveOpeningBalance: [empArr[i]]
                }),
                contentType: 'application/json',
                headers: {
                    'auth-token': authToken
                },
                success: function (res) {
                    counter--;
                    if (counter == 0 && breakOut == 0) {
                        cb();
                    }
                },
                error: function (err) {
                    if (breakOut == 0)
                        cb(err + "");
                    breakOut = 1;
                }
            });
        }
    } else {
        //Call and create leave
        $.ajax({
            url: baseUrl + "/hr-leave/leaveopeningbalances/_create?tenantId=" + tenantId,
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify({
                RequestInfo: requestInfo,
                LeaveOpeningBalance: empArr
            }),
            contentType: 'application/json',
            headers: {
                'auth-token': authToken
            },
            success: function (res) {
                cb();
            },
            error: function (err) {
                cb(err);
            }
        });
    }
}

class PersonalInform extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            ...defaultState,
            departmentsList: [],
            designationList: [],
            leave: [],
            years: [],
            modified: false
        }
        this.handleChange = this.handleChange.bind(this);
        this.search = this.search.bind(this);
        this.addOrUpdate = this.addOrUpdate.bind(this);
        this.handleChangeSrchRslt = this.handleChangeSrchRslt.bind(this);
        this.handleBlur = this.handleBlur.bind(this);
        this.close = this.close.bind(this);
        this.setInitialState = this.setInitialState.bind(this);
    }

    setInitialState(initState) {
        this.setState(initState);
    }

    componentDidMount() {
        if (window.opener && window.opener.document) {
            var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
            if (logo_ele && logo_ele[0]) {
                document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
            }
        }

        var count = 4, _this = this, _state = {};
        const checkCountAndCall = function (key, res) {
            _state[key] = res;
            count--;
            if (count == 0)
                _this.setInitialState(_state);
        }

        commonApiPost("hr-leave", "leavetypes", "_search", {
            tenantId,
            pageSize: 500,
            accumulative: true
        }, function (err, res) {
            checkCountAndCall("leave", res ? res.LeaveType : []);
        });

        getDropdown("years", function (res) {
            checkCountAndCall("years", res);
        })

        getDropdown("assignments_department", function (res) {
            checkCountAndCall("departmentsList", res);
        })

        getDropdown("assignments_designation", function (res) {
            checkCountAndCall("designationList", res);
        })

        if (getUrlVars()["type"]) $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Leave Opening Balance");

        if (getUrlVars()["type"] == "view") {
            this.setState({
                readonly: true
            })
        }
    }

    search(e) {
        let {
        name,
            employee,
            department,
            designation,
            leaveType,
            noOfDay,
            calendarYear
    } = this.state.searchSet;
        e.preventDefault();


        if (!employee && !department) {
            return showError("Either Employee Code or Department is mandatory");
        }


        var _emps = [], leaveBal = [], count = 2, _this = this;
        const calculate = function () {
            var employees = [];
            var _noDays, _leaveId, _createdDate, _lastModifiedDate;
            for (var i = 0; i < _emps.length; i++) {
                _noDays = _leaveId = _createdDate = _lastModifiedDate = "";
                for (var j = 0; j < leaveBal.length; j++) {
                    if (leaveBal[j].employee == _emps[i].id && leaveBal[j].calendarYear == _this.state.searchSet.calendarYear && leaveBal[j].leaveType && leaveBal[j].leaveType.id == _this.state.searchSet.leaveType) {
                        _noDays = leaveBal[j].noOfDays;
                        _leaveId = leaveBal[j].id;
                        _createdDate = leaveBal[j].createdDate
                        _lastModifiedDate = leaveBal[j].lastModifiedDate;
                        break;
                    }
                }

                employees.push({
                    name: _emps[i].name,
                    employee: _emps[i].id,
                    noOfDays: _noDays || "",
                    oldNoOfDays: _noDays || "",
                    code: _emps[i].code,
                    leaveId: _leaveId || "",
                    lastModifiedDate: _lastModifiedDate || "",
                    createdDate: _createdDate
                })
            }

            flag = 1;
            _this.setState({
                isSearchClicked: true,
                employees: Object.assign([], employees),
                modified: true
            });
            setTimeout(function () {
                _this.setState({
                    modified: false
                })
            }, 1200);
        }

        var today = new Date();
        var dd = today.getDate();
        var mm = today.getMonth() + 1; //January is 0!
        var yyyy = today.getFullYear();

        if (dd < 10) {
            dd = '0' + dd
        }

        if (mm < 10) {
            mm = '0' + mm
        }

        today = dd + '/' + mm + '/' + yyyy;

        //call api call
        commonApiPost("hr-employee", "employees", "_search", {
            tenantId,
            asOnDate: today,
            active: true,
            isPrimary: true,
            departmentId: department || null,
            designationId: designation || null,
            code: employee || null,
            pageSize: 500
        }, function (err, res) {
            if (res) {
                _emps = res.Employee;
            }
            count--;
            if (count == 0)
                calculate();
        });

        commonApiPost("hr-leave", "leaveopeningbalances", "_search", {
            tenantId,
            leaveType: leaveType || null,
            year: calendarYear || null,
            departmentId: department || null,
            code: employee || null,
            pageSize: 500
        }, function (err, res) {
            if (res) {
                leaveBal = res.LeaveOpeningBalance;
            }
            count--;
            if (count == 0)
                calculate();
        });
    }

    addOrUpdate(e) {
        e.preventDefault();
        var tempEmpsCreate = [], tempEmpsUpdate = [], _this = this;

        for (var i = 0; i < this.state.employees.length; i++) {
            if (_this.state.employees[i].noOfDays && _this.state.employees[i].noOfDays != _this.state.employees[i].oldNoOfDays) {
                var tmp = {
                    "id": _this.state.employees[i].leaveId || null,
                    "employee": _this.state.employees[i].employee,
                    "calendarYear": _this.state.searchSet.calendarYear,
                    "leaveType": {
                        "id": _this.state.searchSet.leaveType
                    },
                    "noOfDays": _this.state.employees[i].noOfDays,
                    "createdDate": _this.state.employees[i].createdDate,
                    "lastModifiedDate": _this.state.employees[i].lastModifiedDate,
                    tenantId
                };

                (!tmp.id) ? tempEmpsCreate.push(tmp) : tempEmpsUpdate.push(tmp);
            }
        }

        if (tempEmpsCreate.length == 0 && tempEmpsUpdate.length == 0) {
            getUrlVars()["type"] == "update" ? showError("Nothing to update.") : showError("Nothing to create.");
        } else if (tempEmpsCreate.length) {
            callAjax("create", tempEmpsCreate, function (err) {
                if (err) {
                    showError(err);
                } else if (tempEmpsUpdate.length) {
                    callAjax("update", tempEmpsUpdate, function (err) {
                        if (err) {
                            showError(err);
                        } else {
                            getUrlVars()["type"] == "update" ? showSuccess("Updated successfully.") : showSuccess("Added successfully.");
                        }
                    })
                } else {
                    getUrlVars()["type"] == "update" ? showSuccess("Updated successfully.") : showSuccess("Added successfully.");
                }
            })
        } else {
            callAjax("update", tempEmpsUpdate, function (err) {
                if (err) {
                    showError(err);
                } else {
                    getUrlVars()["type"] == "update" ? showSuccess("Updated successfully.") : showSuccess("Added successfully.");
                }
            })
        }
    }

    handleChange(e, name) {
        this.setState({
            searchSet: {
                ...this.state.searchSet,
                [name]: e.target.value
            }
        })
    }

    handleChangeSrchRslt(e, name, ind) {
        var _emps = Object.assign([], this.state.employees);
        _emps[ind][name] = e.target.value;
        this.setState({
            ...this.state,
            employees: _emps
        })
    }

    close() {
        // widow.close();
        open(location, '_self').close();
    }

    componentWillUpdate() {
        if (flag == 1) {
            flag = 0;
            $('#employeeTable').dataTable().fnDestroy();
        }
    }

    componentDidUpdate(prevProps, prevState) {
        if (this.state.modified) {
            $('#employeeTable').DataTable({
                dom: 'Bfrtip',
                buttons: [
                    'copy', 'csv', 'excel', 'pdf', 'print'
                ],
                ordering: false,
                bDestroy: true,
                language: {
                    "emptyTable": "No Records"
                }
            });
        }
    }

    handleBlur(e) {
        setTimeout(function () {
            if (document.activeElement.id == "sub") {
                $("#sub").click();
            }
        }, 100);
        var _this = this;

        if (e.target.value) {
            try {
                var code = e.target.value;
                //Make get employee call
                commonApiPost("hr-employee", "employees", "_search", { code, tenantId }, function (err, res) {
                    if (res) {
                        var obj = res.Employee[0];
                        _this.setState({
                            searchSet: {
                                ..._this.state.searchSet,
                                name: obj.name
                            }
                        })
                    }
                });
            } catch (e) {
                console.log(e);
            }
        } else {
            this.setState({
                searchSet: {
                    ...this.state.searchSet,
                    name: ""
                }
            })
        }
    }

    render() {
        let {
        handleChange,
            search,
            updateTable,
            addOrUpdate,
            handleChangeSrchRslt,
            handleBlur,
            close
    } = this;
        let {
        isSearchClicked,
            employees,
            readonly
    } = this.state;
        let {
        name,
            employee,
            department,
            designation,
            employeeTypeCode,
            functionaryCode,
            leaveType,
            noOfDay,
            calendarYear
    } = this.state.searchSet;
        let mode = getUrlVars()["type"];

        const renderOption = function (list, setNameBool) {
            if (list) {
                return list.map((item) => {
                    return (<option key={item.id} value={setNameBool ? item.name : item.id}>
                        {item.name}
                    </option>)
                })
            }
        }
        const showTable = function () {
            if (isSearchClicked) {
                return (
                    <table id="employeeTable" className="table table-bordered">
                        <thead>
                            <tr>
                                <th>Employee Name</th>
                                <th>Employee Code</th>
                                <th>No. Of Leave Available</th>
                            </tr>
                        </thead>
                        <tbody id="employeeSearchResultTableBody">
                            {
                                renderBody()
                            }
                        </tbody>
                    </table>

                )
            }

        }
        const renderBody = function () {
            if (employees.length > 0) {
                return employees.map((item, index) => {
                    return (<tr key={index}>
                        <td data-label="name">{item.name}</td>
                        <td data-label="code">{item.code}</td>
                        <td data-label="noOfDay">
                            <input type="number" id={item.id} name="noOfDays" value={item.noOfDays}
                                onChange={(e) => { handleChangeSrchRslt(e, "noOfDays", index) }} disabled={readonly} />
                        </td>
                    </tr>
                    );
                })
            }
        }

        const showActionButton = function () {
            if (((!mode) || mode === "update") && employees.length) {
                return (<button type="button" className="btn btn-submit" onClick={(e) => { addOrUpdate(e) }}>{mode ? "Update" : "Add"}</button>);
            }
        };

        const showCloseButton = function () {
            if (employees.length) {
                return (<button type="button" className="btn btn-close" onClick={(e) => { close() }}>Close</button>);
            }
        }

        return (
            <div>
                <form onSubmit={(e) => { search(e) }}>
                    <fieldset>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="">Employee Code</label>
                                    </div>
                                    <div className="col-sm-6">
                                        <input type="text" id="employee" name="employee" value={employee}
                                            onChange={(e) => { handleChange(e, "employee") }} onBlur={(e) => { handleBlur(e) }} />
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="">Employee Name</label>
                                    </div>
                                    <div className="col-sm-6">
                                        <input type="text" id="name" name="name" value={name}
                                            onChange={(e) => { handleChange(e, "name") }} disabled />
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="">Department  </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="styled-select">
                                            <select id="department" name="department" value={department}
                                                onChange={(e) => { handleChange(e, "department") }}>
                                                <option value="">Select Department</option>
                                                {renderOption(this.state.departmentsList)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="">Designation  </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="styled-select">
                                            <select id="designation" name="designation" value={designation} onChange={(e) => {
                                                handleChange(e, "designation")
                                            }}>
                                                <option value="">Select Designation</option>
                                                {renderOption(this.state.designationList)}
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
                                        <label for="">Leave Type <span>*</span> </label> 
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="styled-select">
                                            <select id="leaveType" name="leaveType" value={leaveType} onChange={(e) => {
                                                handleChange(e, "leaveType")
                                            }} required>
                                                <option value="">Select Leave Type</option>
                                                {renderOption(this.state.leave)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for=""> Calendar Year <span>*</span></label> 
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="styled-select">
                                            <select id="calendarYear" name="calendarYear" value={calendarYear} onChange={(e) => {
                                                handleChange(e, "calendarYear")
                                            }} required>
                                                <option value="">Select Calendar Year</option>
                                                {renderOption(this.state.years, true)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>

                        <div className="text-right text-danger">
                            Note: Either Employee Code or Department is mandatory.
                    </div>

                        <div className="text-center">
                            <button id="sub" type="submit" className="btn btn-submit">Search</button>&nbsp;&nbsp;
                <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>
                        </div>
                    </fieldset>
                </form>
                <br />
                {showTable()}
                <div className="text-center">
                    {showActionButton()} &nbsp;&nbsp;
          {showCloseButton()}
                </div>
            </div>
        );
    }
}

ReactDOM.render(
    <PersonalInform />,
    document.getElementById('root')
);
