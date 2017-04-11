class PersonalInform extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
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
        departmentsList: [],
        designationList: [],
        leave: [],
        years: []
    } 
    this.handleChange = this.handleChange.bind(this);
    this.search = this.search.bind(this);
    this.addOrUpdate = this.addOrUpdate.bind(this);
    this.handleChangeSrchRslt = this.handleChangeSrchRslt.bind(this);
  }


  componentDidMount(){
    
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
    //call api call
    try {
        var _emps = commonApiPost("hr-employee","employees","_search",{
            tenantId,
            departmentId: department || null,
            designationId: designation || null,
            code: employee || null,
            employeeName: name || null
        }).responseJSON["Employee"] || [];
    } catch(e) {
        var _emps = [];
    }

    try {
        var leaveBal = commonApiPost("hr-leave", "leaveopeningbalances", "_search", {
            tenantId,
            pageSize: 500
        }).responseJSON["LeaveOpeningBalance"] || [];
    } catch(e) {
        var leaveBal = [];
    }
    var employees = [];
    var _noDays, _leaveId, _createdDate, _lastModifiedDate;
    for (var i = 0; i < _emps.length; i++) {
        for(var j = 0; j < leaveBal.length; j++) {
            _noDays = _leaveId = _createdDate = _lastModifiedDate = "";
            if(leaveBal[j].employee == _emps[i].id && leaveBal[j].calendarYear == this.state.searchSet.calendarYear && leaveBal[j].leaveType && leaveBal[j].leaveType.id == this.state.searchSet.leaveType) {
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
            code: _emps[i].code,
            leaveId: _leaveId || "",
            lastModifiedDate: _lastModifiedDate || "",
            createdDate: _createdDate
        })
    }
    this.setState({
        isSearchClicked: true,
        employees
    })  
  }

  addOrUpdate(e){
    e.preventDefault();
    var tempEmps = [];
    for(var i=0; i<this.state.employees.length; i++) {
        if(this.state.employees[i].noOfDays) {
            tempEmps.push({
                "id": this.state.employees[i].leaveId || null, 
                "employee": this.state.employees[i].employee,
                "calendarYear": this.state.searchSet.calendarYear,
                "leaveType": {
                    "id": this.state.searchSet.leaveType
                },
                "noOfDays": this.state.employees[i].noOfDays,
                "createdDate": this.state.employees[i].createdDate,
                "lastModifiedDate": this.state.employees[i].lastModifiedDate,
                tenantId
            })
        }
    }

    if(tempEmps.length) {
        if(getUrlVars()["type"] == "update") {
            var counter = tempEmps.length, breakOut = 0;
            for(let i=0; i < tempEmps.length; i++) {
                $.ajax({
                    url: baseUrl + "/hr-leave/leaveopeningbalances" + (tempEmps[i].id ? "/" + tempEmps[i].id + "/_update" : "/_create"),
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify({
                        RequestInfo: requestInfo,
                        LeaveOpeningBalance: [tempEmps[i]]
                    }),
                    async: false,
                    contentType: 'application/json',
                    headers: {
                        'auth-token': authToken
                    },
                    success: function(res) {
                        counter--;
                        if(counter == 0 && breakOut == 0) {
                            showSuccess("Updated successfully.");
                        }
                    },
                    error: function(err) {
                        showError(err);
                        breakOut = 1;
                    }
                });
            }
        } else {
            //Call and create leave
            $.ajax({
                url: baseUrl + "/hr-leave/leaveopeningbalances/_create",
                type: 'POST',
                dataType: 'json',
                data: JSON.stringify({
                    RequestInfo: requestInfo,
                    LeaveOpeningBalance: tempEmps
                }),
                async: false,
                contentType: 'application/json',
                headers: {
                    'auth-token': authToken
                },
                success: function(res) {
                    showSuccess("Added successfully.");
                },
                error: function(err) {
                    showError(err);
                }
            });
        }
    } else {
        showError("Nothing to update.");
    }
    
  }


  componentWillMount() {
    try {
        var _leaveTypes = getCommonMaster("hr-leave", "leavetypes", "LeaveType").responseJSON["LeaveType"] || [];
    } catch(e) {
        var _leaveTypes = [];
    }

    try {
        var _years = getCommonMaster("egov-common-masters", "calendaryears", "CalendarYear").responseJSON["CalendarYear"] || [];
    } catch(e) {
        var _years = [];
    }
    this.setState({
      departmentsList:assignments_department,
      designationList:assignments_designation,
      leave: _leaveTypes,
      years: _years
  })
  }

  handleChange(e, name) {
      this.setState({
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value,
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

  close(){
      // widow.close();
      open(location, '_self').close();
  }



  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.employees.length!=this.state.employees.length) {
          // $('#employeeTable').DataTable().draw();
          // alert(prevState.employees.length);
          // alert(this.state.employees.length);
          // alert('updated');
          $('#employeeTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false
          });
      }
  }

  render() {
    let {
        handleChange,
        search,
        updateTable,
        addOrUpdate,
        handleChangeSrchRslt
    } = this;
    let {
        isSearchClicked,
        employees
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

    const renderOption=function(list)
    {
        if(list)
        {
            return list.map((item)=>
            {
                return (<option key={item.id} value={item.id}>
                        {item.name}
                  </option>)
            })
        }
    }
    const showTable=function()
    {
      if(isSearchClicked)
      {
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
    const renderBody = function()
    {
      return employees.map((item, index)=>
      {
            return (<tr key={index}>
                    <td data-label="name">{item.name}</td>
                    <td data-label="code">{item.code}</td>
                    <td data-label="noOfDay">
                    <input type="number" id={item.id} name="noOfDays"  value={item.noOfDays}
                      onChange={(e)=>{handleChangeSrchRslt(e, "noOfDays", index)}}/>
                    </td>
                </tr>
            );

      })
    }
    const showActionButton = function() {
      if(((!mode) || mode==="update") && employees.length) {
        return (<button type="button" className="btn btn-submit" onClick={(e) => {addOrUpdate(e)}}>{mode?"Update":"Add"}</button>);
      }
    };

    const showCloseButton = function() {
        if(employees.length) {
            return (<button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>);
        }
    }

    return (
      <div>
          <form onSubmit={(e)=>{search(e)}}>
          <fieldset>
              <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="">Employee Code</label>
                        </div>
                        <div className="col-sm-6">
                            <input type="text" id="employee" name="employee" value={employee}
                              onChange={(e)=>{handleChange(e,"employee")}}/>
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
                                onChange={(e)=>{  handleChange(e,"name")}} />
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
                              onChange={(e)=>{ handleChange(e,"department")}}>
                                <option>Select Department</option>
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
                                <select id="designation" name="designation" value={designation} onChange={(e)=>{
                                    handleChange(e,"designation")}}>
                                <option>Select Designation</option>
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
                            <label for="">Leave Type  </label> <span className="text-danger">*</span>
                          </div>
                          <div className="col-sm-6">
                          <div className="styled-select">
                              <select id="leaveType" name="leaveType" value={leaveType} onChange={(e)=>{
                                  handleChange(e,"leaveType")
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
                                <label for=""> Calendar Year</label> <span className="text-danger">*</span>
                            </div>
                            <div className="col-sm-6">
                            <div className="text-no-ui">
                                <select id="calendarYear" name="calendarYear" value={calendarYear} onChange={(e)=>{
                                  handleChange(e,"calendarYear")
                                }} required>
                                    <option value="">Select Calendar Year</option>
                                    {renderOption(this.state.years)}
                                </select>
                            </div>
                            </div>
                        </div>
                    </div>
                </div>

            <div className="text-center">
                <button type="submit"  className="btn btn-submit">Search</button>
            </div>
          </fieldset>
          </form>
          <br/>
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

function showError(msg) {
    $('#error-alert-span').text(msg);
    $('#error-alert-div').show();
    setTimeout(function() {
        $('#error-alert-div').hide();
    }, 3000);
}

function showSuccess(msg) {
    $('#success-alert-span').text(msg);
    $('#success-alert-div').show();
    setTimeout(function() {
        $('#success-alert-div').hide();
    }, 3000);
}