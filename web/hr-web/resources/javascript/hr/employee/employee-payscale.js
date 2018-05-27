let months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

class EmployeePayscale extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            employeePayscale: {
                id: "",
                employee: {
                    "id": "",
                    "code": ""
                },
                payscaleHeader: {
                    "id": ""
                },
                "effectiveFrom": "",
                "basicAmount": "",
                "incrementMonth": "",
                "reason": "",
                "createdBy": "",
                "lastModifiedBy": "",
                "createdDate": "",
                "lastModifiedDate": "",
                "tenantId": tenantId
            },
            currentEmployee: {
                code: "",
                name: "",
                department: "",
                designation: ""
            }
        }
        this.handleChange = this.handleChange.bind(this);
        this.addOrUpdate = this.addOrUpdate.bind(this);
        this.setInitialState = this.setInitialState.bind(this);
        this.handleChangeThreeLevel = this.handleChangeThreeLevel.bind(this);
    }

    setInitialState(initState) {
        this.setState(initState);
    }

    componentDidMount() {
       
        if (window.opener && window.opener.document) {
            var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
            if (logo_ele && logo_ele[0]) {
                document.getElementsByClassName("homepage_logo")[0].src = (logo_ele[0].getAttribute("src") && logo_ele[0].getAttribute("src").indexOf("http") > -1) ? logo_ele[0].getAttribute("src") : window.location.origin + logo_ele[0].getAttribute("src");
            }
        }

        let count = 3, _state = {}, _this = this;

        const checkCountAndCall = function (key, res) {
            _state[key] = res;
            count--;
            if (count == 0)
                _this.setInitialState(_state);
        }

        getDropdown("assignments_designation", function (res) {
            checkCountAndCall("assignments_designation", res);
        });
        getDropdown("assignments_department", function (res) {
            checkCountAndCall("assignments_department", res);
        });
        getDropdown("payscale", function (res) {
            checkCountAndCall("payscaleList", res);
        });

        getCommonMasterById("hr-employee", "employees", getUrlVars()["id"], function (err, res) {
            if (res) {
                var obj = res.Employee[0];

                for (var i = 0; i < obj.assignments.length; i++) {
                    if (obj.assignments[i].isPrimary) {
                        var department = getNameById(_this.state.assignments_department, obj.assignments[i]["department"]);
                        var designation = getNameById(_this.state.assignments_designation, obj.assignments[i]["designation"]);
                    }
                }

                _this.setState({
                    currentEmployee: {
                        name: obj.name,
                        code: obj.code,
                        department: department,
                        designation: designation
                    }, employeePayscale: {
                        ..._this.state.employeePayscale,
                        employee: { id: obj.id }
                    }
                })
            }
        });

        $('#effectiveFrom').datepicker({
            format: 'dd/mm/yyyy',
            autoclose: true
        });
        $('#effectiveFrom').on("change", function (e) {
            _this.setState({
                employeePayscale: {
                    ..._this.state.employeePayscale,
                    effectiveFrom: e.target.value
                }
            })
        });

    }

    handleChangeThreeLevel(e, pName, name) {
        var _this = this;
        this.setState({
            employeePayscale: {
                ...this.state.employeePayscale,
                [pName]: {
                    ...this.state.employeePayscale[pName],
                    [name]: e.target.value
                }
            }
        });
    }

    handleChange(e, name) {
        this.setState({
            employeePayscale: {
                ...this.state.employeePayscale,
                [name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
            }
        })
    }

    close() {
        // widow.close();
        open(location, '_self').close();
    }

    addOrUpdate(e, mode) {
        e.preventDefault();
        var _this = this;

        let tempInfo = Object.assign({}, _this.state.employeePayscale);
        let type = getUrlVars()["type"];
        let body = {
            "RequestInfo": requestInfo,
            "EmployeePayscale": [tempInfo]
          }

        $.ajax({
            url: baseUrl + "/hr-employee/employeepayscale/_create?tenantId=" + tenantId,
            type: 'POST',
            dataType: 'json',
            data: JSON.stringify(body),
            contentType: 'application/json',
            headers: {
                'auth-token': authToken
            },
            success: function (res) {
                window.location.href = `app/hr/employee/employee-search-payscale.html`;
            },
            error: function (err) {
                if (err.responseJSON && err.responseJSON.LeaveApplication && err.responseJSON.LeaveApplication[0] && err.responseJSON.LeaveApplication[0].errorMsg) {
                    showError(err.responseJSON.LeaveApplication[0].errorMsg);
                } else {
                    showError("Something went wrong. Please contact Administrator.");
                }
            }
        });

    }

    render() {
        let { handleChange, addOrUpdate, handleChangeThreeLevel } = this;
        let { currentEmployee, employeePayscale, payscaleList } = this.state;
        let { employee, payscaleHeader, effectiveFrom, basicAmount, incrementMonth, reason } = this.state.employeePayscale;
        let mode = getUrlVars()["type"];

        const renderOption = function (list) {
            if (list) {
                return list.map((item) => {
                    return (<option key={item.payscale} value={item.id}>
                        {item.payscale + "-" + item.paycommission}
                    </option>)
                })
            }
        }

        const renderMonths = function (list) {
            if (list) {
                return list.map((item) => {
                    return (<option key={item} value={item}>
                        {item}
                    </option>)
                })
            }

        }

        const showActionButton = function () {
            if (mode === "create" || !(mode)) {
                return (<button type="submit" className="btn btn-submit">Create</button>);
            } else if(mode === "update"){
                return (<button type="submit" className="btn btn-submit">Update</button>);
            }
        };

        return (
            <div>

                <h3>{getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) : "Create"} Employee Payscale </h3>
                <form onSubmit={(e) => { addOrUpdate(e) }}>
                    <fieldset>

                        <div className="form-section">
                        
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="">Employee Name</label>
                                        </div>
                                        <div className="col-sm-6">
                                            <input type="text" id="name" name="name"  value={currentEmployee.name} disabled />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="">Employee Code</label>
                                        </div>
                                        <div className="col-sm-6">
                                            <input type="text" id="code" name="code"  value={currentEmployee.code} disabled />
                                        </div>
                                    </div>
                                </div>
                            </div>
                            
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="department">Employee Department</label>
                                        </div>
                                        <div className="col-sm-6">
                                            <input type="text" id="department" name="department"  value={currentEmployee.department} disabled />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="">Employee Designation</label>
                                        </div>
                                        <div className="col-sm-6">
                                            <input type="text" id="designation" name="designation"  value={currentEmployee.designation} disabled />
                                        </div>
                                    </div>
                                </div>
                            </div>
                        
                        </div>

                        <br />
                        
                        <div className="form-section">

                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="effectiveFrom">Effective From Date<span>*</span></label>
                                        </div>
                                        <div className="col-sm-6">
                                            <input type="text" id="effectiveFrom" name="effectiveFrom" value={effectiveFrom} required />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="payscaleHeader">Pay scale <span>*</span></label>
                                        </div>
                                        <div className="col-sm-6">
                                            <select id="payscaleHeader" name="payscaleHeader" value={payscaleHeader.id}
                                             onChange={(e) => {handleChangeThreeLevel(e, "payscaleHeader", "id") }} required >
                                                <option value=""> Select Payscale</option>
                                                {renderOption(payscaleList)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="basicAmount">Basic Amount<span>*</span></label>
                                        </div>
                                        <div className="col-sm-6">
                                            <input type="number" id="basicAmount" name="basicAmount" value={basicAmount}
                                                onChange={(e) => { handleChange(e, "basicAmount") }} required />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="incrementMonth">Increment Month<span>*</span></label>
                                        </div>
                                        <div className="col-sm-6">
                                            <select id="incrementMonth" name="incrementMonth" value={incrementMonth} 
                                            onChange={(e) => {handleChange(e, "incrementMonth") }} required>
                                                <option value=""> Select month</option>
                                                {renderMonths(months)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>

                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="reason">Increment Reason<span>*</span></label>
                                        </div>
                                        <div className="col-sm-6">
                                            <textarea rows="4" cols="50" id="reason" name="reason" value={reason}
                                                onChange={(e) => { handleChange(e, "reason") }} required></textarea>

                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <br />

                        <div className="text-center">
                            {showActionButton()} &nbsp;&nbsp;
                            <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>
                        </div>
                    </fieldset>
                </form>
            </div>);

    }
}


ReactDOM.render(
    <EmployeePayscale />,
    document.getElementById('root')
);