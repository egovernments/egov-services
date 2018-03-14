var flag = 0;
class SearchDisciplinaryAction extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      employees: [], employ: {}, searchSet: {
        name: "",
        code: "",
        departmentId: "",
        designationId: ""
      },
      isSearchClicked: false,
      assignments_department: [],
      assignments_designation: [],
      modified: false
    }
    this.handleChange = this.handleChange.bind(this);
    this.search = this.search.bind(this);
    this.handleBlur = this.handleBlur.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    //this.handleClick = this.handleClick.bind(this);
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
    if (getUrlVars()["type"]) $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Disciplinary Action");

    var count = 2, _this = this, _state = {};
    var checkCountNCall = function (key, res) {
      count--;
      _state[key] = res;
      if (count == 0)
        _this.setInitialState(_state);
    }

    getDropdown("assignments_designation", function (res) {
      checkCountNCall("assignments_designation", res);
    });
    getDropdown("assignments_department", function (res) {
      checkCountNCall("assignments_department", res);
    });
  }


  search(e) {
    let {
    name,
      code,
      departmentId,
      designationId } = this.state.searchSet, _this = this;
    e.preventDefault();
    //call api call
    var employees = [];


    var today = new Date();
    var dd = today.getDate();
    var mm = today.getMonth() + 1; //January is 0!
    var yyyy = today.getFullYear();

    if (dd < 10) {
      dd = '0' + dd;
    }
    if (mm < 10) {
      mm = '0' + mm;
    }

    today = dd + '/' + mm + '/' + yyyy;


    commonApiPost("hr-employee", "employees", "_search", {
      ..._this.state.searchSet, tenantId,
      isPrimary: true,
      asOnDate: today,
      departmentId: departmentId || null,
      designationId: designationId || null,
      code: code || null,
      pageSize: 500
    }, function (err, res) {
      if (res) {
        employees = res["Employee"];
        flag = 1;
        _this.setState({
          isSearchClicked: true,
          employees,
          modified: true
        });
      }
      setTimeout(function () {
        _this.setState({
          modified: false
        })
      }, 1200);
    });
  }

  handleChange(e, name) {
    this.setState({
      searchSet: {
        ...this.state.searchSet,
        [name]: e.target.value
      }
    })
  }

  componentWillUpdate() {
    if (flag == 1) {
      flag = 0;
      $('#employeeTable').dataTable().fnDestroy();
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
      var code = e.target.value;
      commonApiPost("hr-employee", "employees", "_search", { code, tenantId }, function (err, res) {
        if (res) {
          _this.setState({
            searchSet: {
              ..._this.state.searchSet,
              name: res.Employee && res.Employee[0] ? res.Employee[0].name : ""
            }
          })
        }
      });
    } else {
      _this.setState({
        searchSet: {
          ..._this.state.searchSet,
          name: ""
        }
      })
    }
  }


  close() {
    // widow.close();
    open(location, '_self').close();
  }

  // handleClick(type, id) {
  //   if (type === "create") {
  //     window.open(`app/hr/leavemaster/apply-leave.html?id=${id}&type=${type}`, '_blank', 'location=yes, height=760, width=800, scrollbars=yes, status=yes');
  //   } else {
  //     window.open(`app/hr/leavemaster/view-apply.html?id=${id}&type=${type}`, '_blank', 'location=yes, height=760, width=800, scrollbars=yes, status=yes');
  //   }
  // }


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

  render() {
    let { handleChange, search, handleBlur } = this;
    let { isSearchClicked, employees, assignments_designation, assignments_department } = this.state;
    let { name,
      code,
      departmentId,
      designationId } = this.state.searchSet;
    const renderOption = function (list) {
      if (list) {
        return list.map((item) => {
          return (<option key={item.id} value={item.id}>
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
                <th>Employee Code</th>
                <th>Employee Name</th>
                <th>Employee Designation</th>
                <th>Employee Department</th>
                <th>Action</th>

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
        return employees.map((item, index, id) => {
          return (<tr key={index} >

            <td data-label="code">{item.code}</td>
            <td data-label="name">{item.name}</td>
            <td data-label="designation">{getNameById(assignments_designation, item.assignments[0].designation)}</td>
            <td data-label="department">{getNameById(assignments_department, item.assignments[0].department)}</td>
            <td data-label="action"><a href={`app/hr/disciplinary/employee-disciplinary.html?id=${item.id}&type=${getUrlVars()["type"]}`}>Initiate</a></td>
          </tr>
          );
        })
      }
    }
    return (
      <div>
        <h3>Search employee to {getUrlVars()["type"]} a Disciplinary Action</h3>
        <form onSubmit={(e) => { search(e) }}>
          <fieldset>
            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="">Designation  </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select id="designationId" name="designationId" value={designationId} onChange={(e) => {
                        handleChange(e, "designationId")
                      }}>
                        <option value="">Select Designation</option>
                        {renderOption(this.state.assignments_designation)}
                      </select>
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="">Department  </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select id="departmentId" name="departmentId" value={departmentId}
                        onChange={(e) => { handleChange(e, "departmentId") }}>
                        <option value="">Select Department</option>
                        {renderOption(this.state.assignments_department)}
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
                    <label for="">Employee Code</label>
                  </div>
                  <div className="col-sm-6">
                    <input type="text" id="code" name="code" value={code}
                      onChange={(e) => { handleChange(e, "code") }} onBlur={(e) => { handleBlur(e) }} />
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

            {/*<div className="row">
                      <div className="col-sm-6">
                          <div className="row">
                              <div className="col-sm-6 label-text">
                                <label for=""> Status</label>
                              </div>
                              <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="employeeStatusCode" name="employeeStatusCode" value={employeeStatusCode}
                                    onChange={(e)=>{ handleChange(e,"employeeStatusCode") }}>

                                      <option value="">Select Status</option>
                                      {renderOption(this.state.employeeStatus)}
                                 </select>
                              </div>
                              </div>
                          </div>
                        </div>
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                  <label for="">Functionary  </label>
                                </div>
                                <div className="col-sm-6">
                                <div className="styled-select">
                                    <select id="functionaryCode" name="functionaryCode" value={functionaryCode}
                                      onChange={(e)=>{  handleChange(e,"functionaryCode")}}>

                                    <option>Select Functionary</option>
                                    {renderOption(this.state.assignments_functionary)}
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
                            <label for="">Type  </label>
                          </div>
                          <div className="col-sm-6">
                            <div className="styled-select">
                              <select id="employeeTypeCode" name="employeeTypeCode" value={employeeTypeCode} onChange={(e)=>{
                                  handleChange(e,"employeeTypeCode")}}>
                              <option>Select Type</option>
                              {renderOption(this.state.employeeType)}
                             </select>
                          </div>
                          </div>
                      </div>
                    </div>
                </div>*/}


            <div className="text-center">
              <button id="sub" type="submit" className="btn btn-submit">Search</button> &nbsp;&nbsp;
                <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>
            </div>
          </fieldset>
        </form>
        <br />
        {showTable()}
      </div>
    );
  }
}






ReactDOM.render(
  <SearchDisciplinaryAction/>,
  document.getElementById('root')
);
