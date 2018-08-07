var flag = 0;
class LeaveReport extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      result: [],
      searchSet: {
        code: "",
        departmentId: "",
        designationId: "",
        employeeType: "",
        employeeStatus: "",
        leaveType: "",
        fromDate: "",
        toDate: "",
        leaveStatus: ""
      },
      employeeTypes: [],
      departments: [],
      designations: [],
      leaveStatuses: [],
      leaveTypes: [],
      employeeStatuses: [],
      employeeList: [],
      isSearchClicked: false
    };
    this.handleChange = this.handleChange.bind(this);
    this.searchEmployee = this.searchEmployee.bind(this);
    this.closeWindow = this.closeWindow.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
  }

  setInitialState(initState) {
    this.setState(initState);
  }

  componentWillMount() {
    var _state = {},
      _this = this,
      count = 6;
    const checkCountAndCall = function(key, res) {
      _state[key] = res;
      count--;
      if (count == 0) _this.setInitialState(_state);
    };

    getDropdown("assignments_designation", function(res) {
      checkCountAndCall("designations", res);
    });
    getDropdown("assignments_department", function(res) {
      checkCountAndCall("departments", res);
    });
    getDropdown("employeeType", function(res) {
      checkCountAndCall("employeeTypes", res);
    });
    getDropdown("leaveTypes", function(res) {
      checkCountAndCall("leaveTypes", res);
    });
    getDropdown("employeeStatus", function(res) {
      checkCountAndCall("employeeStatuses", res);
    });
    getDropdown("leaveStatus", function(res) {
      checkCountAndCall("leaveStatuses", res);
    });

    commonApiPost(
      "hr-employee",
      "employees",
      "_search",
      {
        tenantId,
        pageSize: 500
      },
      function(err, res) {
        if (res && res.Employee) {
          var employees = res["Employee"];
          if (res.Page && res.Page.totalPages > 1) {
            count = 0;

            try {
              var promises = [];
              for (var i = 2; i <= res.Page.totalPages; i++) {
                var queryParam = { tenantId, pageSize: 500 };
                queryParam.pageNumber = i;

                var employeePromise = new Promise(function(resolve, reject) {
                  commonApiPost(
                    "hr-employee",
                    "employees",
                    "_search",
                    queryParam,
                    function(err, res1) {
                      resolve(res1 ? res1["Employee"] : []);
                    }
                  );
                });

                promises.push(employeePromise);
              }
              Promise.all(promises).then(function(results) {
                for (var itm in results)
                  employees = employees.concat(results[itm]);
                employees.forEach(function(item, index, theArray) {
                  theArray[index] = {
                    id: item.id,
                    name: item.code + " - " + item.name
                  };
                });

                _this.setState({
                  ..._this.state,
                  employeeList: employees
                });
              });
            } catch (error) {
              console.log(error);
            }
          } else {
            res.Employee.forEach(function(item, index, theArray) {
              theArray[index] = {
                id: item.id,
                name: item.code + " - " + item.name
              };
            });

            _this.setState({
              ..._this.state,
              employeeList: res.Employee
            });
          }
        }
      }
    );
  }

  componentDidMount() {
    var _this = this;

    $("#fromDate").datepicker({
      format: "dd/mm/yyyy",
      autoclose: true,
      defaultDate: ""
    });

    $("#fromDate").on("changeDate", function(e) {
      _this.setState({
        searchSet: {
          ..._this.state.searchSet,
          fromDate: $("#fromDate").val()
        }
      });
    });

    $("#toDate").datepicker({
      format: "dd/mm/yyyy",
      autoclose: true,
      defaultDate: ""
    });

    $("#toDate").on("changeDate", function(e) {
      _this.setState({
        searchSet: {
          ..._this.state.searchSet,
          toDate: $("#toDate").val()
        }
      });
    });
  }

  componentDidUpdate(prevProps, prevState) {
    if (flag === 1) {
      flag = 0;
      $("#employeeTable").DataTable({
        dom: "Bfrtip",
        buttons: ["copy", "csv", "excel", "pdf", "print"],
        ordering: false,
        language: {
          emptyTable: "No Records"
        }
      });
    }
  }

  closeWindow() {
    open(location, "_self").close();
  }

  searchEmployee(e) {
    e.preventDefault();
    $("#employeeTable")
      .dataTable()
      .fnDestroy();
    var _this = this;
    if (this.state.searchSet.fromDate && !this.state.searchSet.toDate)
      return showError("Please enter To Date");
    if (!this.state.searchSet.fromDate && this.state.searchSet.toDate)
      return showError("Please enter From Date");
    try {
      flag = 1;
      commonApiPost(
        "hr-leave",
        "leaveapplications",
        "_leavereport",
        {
          ...this.state.searchSet,
          tenantId,
          pageSize: 500
        },
        function(err, res) {
          if (res && res.LeaveApplication) {
            _this.setState({
              ..._this.state,
              result: res.LeaveApplication,
              isSearchClicked: true
            });
          } else {
            _this.setState({
              ..._this.state,
              result: [],
              isSearchClicked: true
            });
          }
        }
      );
    } catch (e) {
      console.log(e);
    }
  }

  handleChange(e, name) {
    this.setState({
      ...this.state,
      searchSet: {
        ...this.state.searchSet,
        [name]: e.target.value
      }
    });
  }
  render() {
    let { handleChange, searchEmployee, closeWindow } = this;
    let {
      result,
      employeeTypes,
      departments,
      designations,
      leaveStatuses,
      leaveTypes,
      employeeStatuses,
      employeeList,
      isSearchClicked
    } = this.state;
    let {
      code,
      departmentId,
      designationId,
      employeeType,
      employeeStatus,
      leaveStatus,
      leaveType,
      fromDate,
      toDate
    } = this.state.searchSet;

    const renderOptions = function(list) {
      if (list && list.constructor == Array) {
        return list.map(item => {
          return (
            <option key={item.id} value={item.id}>
              {item.name ? item.name : item.code}
            </option>
          );
        });
      }
    };

    const renderTr = () => {
      if (isSearchClicked) {
        return result.map((item, ind) => {
          return (
            <tr key={ind}>
              <td>{getNameById(employeeList, item.employee)}</td>
              <td>{item.applicationNumber}</td>
              <td>{item.leaveType ? item.leaveType.name : ""}</td>
              <td>{item.fromDate + "-" + item.toDate}</td>
              <td>{item.leaveDays}</td>
              <td>{getNameById(leaveStatuses, item.status, "code")}</td>
              <td>{item.reason}</td>
            </tr>
          );
        });
      }
    };

    const showTable = () => {
      if (this.state.isSearchClicked) {
        return (
          <div>
            <div className="land-table">
              <table id="employeeTable" className="table table-bordered">
                <thead>
                  <tr>
                    <th>Employee Code-Name</th>
                    <th>Application Number </th>
                    <th>Leave Type</th>
                    <th>Date Range</th>
                    <th>Number Of Days</th>
                    <th>Status</th>
                    <th>Comments</th>
                  </tr>
                </thead>
                <tbody>{renderTr()}</tbody>
              </table>
            </div>
          </div>
        );
      }
    };

    return (
      <div>
        <form
          onSubmit={e => {
            searchEmployee(e);
          }}
        >
          <fieldset>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Department </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select
                        id="departmentId"
                        value={departmentId}
                        onChange={e => {
                          handleChange(e, "departmentId");
                        }}
                      >
                        <option value="">Select Department</option>
                        {renderOptions(departments)}
                      </select>
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Designation </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select
                        id="designationId"
                        value={designationId}
                        onChange={e => {
                          handleChange(e, "designationId");
                        }}
                      >
                        <option value="">Select Designation</option>
                        {renderOptions(designations)}
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
                    <label htmlFor="">Employee Code/Name </label>
                  </div>
                  <div className="col-sm-6">
                    <input
                      id="code"
                      type="text"
                      value={code}
                      onChange={e => {
                        handleChange(e, "code");
                      }}
                    />
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Employee Type </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select
                        id="employeeType"
                        value={employeeType}
                        onChange={e => {
                          handleChange(e, "employeeType");
                        }}
                      >
                        <option value="">search Employee Type</option>
                        {renderOptions(employeeTypes)}
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
                    <label htmlFor="">Employee Status </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select
                        id="employeeStatus"
                        value={employeeStatus}
                        onChange={e => {
                          handleChange(e, "employeeStatus");
                        }}
                      >
                        <option value="">search Employee Status</option>
                        {renderOptions(employeeStatuses)}
                      </select>
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Leave Type </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select
                        id="leaveType"
                        type="text"
                        value={leaveType}
                        onChange={e => {
                          handleChange(e, "leaveType");
                        }}
                      >
                        <option value="">Select Leave Type</option>
                        {renderOptions(leaveTypes)}
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
                    <label htmlFor="">Date From </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="text-no-ui">
                      <span>
                        <i className="glyphicon glyphicon-calendar" />
                      </span>
                      <input
                        type="text"
                        id="fromDate"
                        value={fromDate}
                        onChange={e => {
                          handleChange(e, "fromDate");
                        }}
                      />
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Date To </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="text-no-ui">
                      <span>
                        <i className="glyphicon glyphicon-calendar" />
                      </span>
                      <input
                        type="text"
                        id="toDate"
                        value={toDate}
                        onChange={e => {
                          handleChange(e, "toDate");
                        }}
                      />
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Leave Status </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select
                        id="leaveStatus"
                        type="text"
                        value={leaveStatus}
                        onChange={e => {
                          handleChange(e, "leaveStatus");
                        }}
                      >
                        <option value="">Select Leave Status</option>
                        {renderOptions(leaveStatuses)}
                      </select>
                    </div>
                  </div>
                </div>
              </div>
            </div>
            <div className="text-center">
              <button type="submit" className="btn btn-submit">
                Search
              </button>
              &nbsp;&nbsp;
              <button
                type="button"
                className="btn btn-close"
                onClick={e => {
                  this.closeWindow();
                }}
              >
                Close
              </button>
            </div>
          </fieldset>
        </form>
        {showTable()}
      </div>
    );
  }
}

ReactDOM.render(<LeaveReport />, document.getElementById("root"));
