let months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];

class EmployeePayscale extends React.Component {
    constructor(props) {
        super(props);
        this.state = {
            employeePayscale: [{
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
            }],
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
        this.addPayScaleDetails = this.addPayScaleDetails.bind(this);
        this.deletePayscaleDetails = this.deletePayscaleDetails.bind(this);
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

        commonApiPost("hr-employee", "employeepayscale", "_search", { tenantId }, function (err, res) {
            if (res) {
                console.log(res);
            }
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
                        // }, employeePayscale: {
                        //     ..._this.state.employeePayscale,
                        //     employee: { id: obj.id }
                    }
                })
            }
        });

    }

    componentDidUpdate(prevProps, prevState) {
        let _this = this;
        let details = Object.assign([], _this.state.employeePayscale);

        $('#effectiveFrom_0').datepicker({
            format: 'dd/mm/yyyy',
            autoclose: true
        });
        $('#effectiveFrom_0').on("change", function (e) {
            details[e.target.name].effectiveFrom = e.target.value;
            _this.setState({
                employeePayscale: details
            })
        });

        if (prevState.employeePayscale !== _this.state.employeePayscale) {

            
            let names = "";
            details.forEach(function (item, index) {
                if (index == 0)
                    names = names + "#effectiveFrom_" + index;
                else
                    names = names + ",#effectiveFrom_" + index;
            })

            $(names).datepicker({
                format: 'dd/mm/yyyy',
                autoclose: true
            });
            $(names).on("change", function (e) {
                details[e.target.name].effectiveFrom = e.target.value;
                _this.setState({
                    employeePayscale: details
                })
            });
        }
    }

    handleChange(e, ind, name) {

        let details = Object.assign([], this.state.employeePayscale);

        if (name === "payscaleHeader")
            details[ind][name]["id"] = e.target.value;
        else
            details[ind][name] = e.target.value;

        this.setState({
            employeePayscale: details
        })
    }

    deletePayscaleDetails(ind) {
        let details = Object.assign([], this.state.employeePayscale);
        if (details.length > 1) {
            details.splice(ind, 1);

            this.setState({
                employeePayscale: details
            })
        } else {
            alert("Cant not Delete all payscale");
        }
    }

    addPayScaleDetails(index) {

        let details = Object.assign([], this.state.employeePayscale);


        let newDetails = {
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
        }

        details.push(newDetails);

        this.setState({
            employeePayscale: details
        })

    }


    close() {
        // widow.close();
        open(location, '_self').close();
    }

    addOrUpdate(e, mode) {
        e.preventDefault();
        var _this = this;

        let tempInfo = Object.assign([], _this.state.employeePayscale);

        tempInfo.forEach(function (item) {
            item.employee.id = getUrlVars()["id"];
        })

        let type = getUrlVars()["type"];
        let body = {
            "RequestInfo": requestInfo,
            "EmployeePayscale": tempInfo
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
        let { handleChange, addOrUpdate, deletePayscaleDetails, addPayScaleDetails } = this;
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

        const renderPlusMinus = function (ind) {
            if (mode !== "view")
                return (
                    <td>
                        <button type="button" className="btn btn-default btn-action" onClick={() => addPayScaleDetails(employeePayscale.length)}><span className="glyphicon glyphicon-plus" aria-hidden="true"></span></button>
                        &nbsp;<button type="button" className="btn btn-default btn-action" onClick={() => deletePayscaleDetails(ind)}><span className="glyphicon glyphicon-minus" aria-hidden="true"></span></button>
                    </td>
                )
        }
        const renderPayScaleDetails = function () {
            if (employeePayscale && employeePayscale.length) {

                return employeePayscale.map(function (payscaleDetail, ind) {
                    return (

                        <tr key={ind} id={ind} >
                            <td>
                                <input type="text" id={"effectiveFrom_" + ind} key={"payscale" + ind} name={ind} value={payscaleDetail["effectiveFrom"]} required />
                            </td>
                            <td>
                                <select id="payscaleHeader" name="payscaleHeader" value={payscaleDetail["payscaleHeader"].id}
                                    onChange={(e) => { handleChange(e, ind, "payscaleHeader") }} required >
                                    <option value=""> Select Payscale</option>
                                    {renderOption(payscaleList)}
                                </select>
                            </td>
                            <td>
                                <input type="number" id="basicAmount" name="basicAmount" value={payscaleDetail["basicAmount"]}
                                    onChange={(e) => { handleChange(e, ind, "basicAmount") }} required />

                            </td>
                            <td>
                                <select id="incrementMonth" name="incrementMonth" value={payscaleDetail["incrementMonth"]}
                                    onChange={(e) => { handleChange(e, ind, "incrementMonth") }} required>
                                    <option value=""> Select month</option>
                                    {renderMonths(months)}
                                </select>
                            </td>
                            <td>
                                <textarea rows="4" cols="50" id="reason" name="reason" value={payscaleDetail["reason"]}
                                    onChange={(e) => { handleChange(e, ind, "reason") }} required></textarea>
                            </td>
                            {renderPlusMinus(ind)}

                        </tr>

                    );
                })
            }
        }


        const showActionButton = function () {
            if (mode === "create" || !(mode)) {
                return (<button type="submit" className="btn btn-submit">Create</button>);
            } else if (mode === "update") {
                return (<button type="submit" className="btn btn-submit">Update</button>);
            }
        };

        const renderActionButton = function () {
            if (mode !== "view")
                return (<th>Action</th>)
        }

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
                                            <input type="text" id="name" name="name" value={currentEmployee.name} disabled />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="">Employee Code</label>
                                        </div>
                                        <div className="col-sm-6">
                                            <input type="text" id="code" name="code" value={currentEmployee.code} disabled />
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
                                            <input type="text" id="department" name="department" value={currentEmployee.department} disabled />
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="">Employee Designation</label>
                                        </div>
                                        <div className="col-sm-6">
                                            <input type="text" id="designation" name="designation" value={currentEmployee.designation} disabled />
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>
                        <br />
                        <div className="form-section">

                            <div className="form-section-inner">

                                <table id="fileTable" className="table table-bordered">
                                    <thead>
                                        <tr>
                                            <th>Effective From Date<span>* </span></th>
                                            <th>Pay scale<span>* </span></th>
                                            <th>Basic Amount<span>* </span></th>
                                            <th>Increment Month<span>* </span></th>
                                            <th>Increment Reason<span>* </span></th>
                                            {renderActionButton()}
                                        </tr>
                                    </thead>
                                    <tbody id="tableBody">
                                        {
                                            renderPayScaleDetails()
                                        }
                                    </tbody>
                                </table>
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