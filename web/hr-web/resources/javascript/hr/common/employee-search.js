var flag = 0;
class EmployeeSearch extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
        employees: [],
        searchSet: {
            code: "",
            departmentId: "",
            designationId: "",
            employeeType: "",
            asOnDate: "",
            name: "",
            mobileNumber: "",
            pan: "",
            aadhaarNumber: ""
        },
        isSearchClicked: false,
        employeeTypeList: [],
        assignments_department: [],
        assignments_designation: [],
        modified: false
    }
    this.handleChange = this.handleChange.bind(this);
    this.search = this.search.bind(this);
    this.handleBlur = this.handleBlur.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.handleClick=this.handleClick.bind(this);
  }

  setInitialState(initState) {
    this.setState(initState);
  }

  search(e) {
    let {
    code,
    name,
    departmentId,
    designationId,
    employeeType,
    asOnDate}=this.state.searchSet;
    var _this = this;
    e.preventDefault();
    //call api call
    var employees = [];
    commonApiPost("hr-employee","employees","_search", {
        tenantId, code, departmentId, designationId, name, pageSize:500
    }, function(err, res) {
        if(res) {
          employees = res.Employee;
        }

        flag = 1;
        _this.setState({
          isSearchClicked: true,
          employees,
          modified: true
        });

        setTimeout(function() {
          _this.setState({
            modified: false
          })
        }, 1200);
    });
  }
  handleBlur(e) {
    setTimeout(function(){
       if(document.activeElement.id == "sub") {
          $("#sub").click();
       }
    }, 100);
    var _this = this;

    if(e.target.value) {
       var code = e.target.value;
       //Make get employee call
       commonApiPost("hr-employee","employees","_search",{code,tenantId}, function(err, res) {
        if(res) {
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

  componentWillUpdate() {
    if(flag == 1) {
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

  componentDidMount() {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
    }
    $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Employee");

    var count = 3, _state = {}, _this = this;

    const checkCountAndCall = function(key, res) {
      _state[key] = res;
      count--;
      if(count == 0)
        _this.setInitialState(_state);
    }

    getDropdown("assignments_designation", function(res) {
      checkCountAndCall("assignments_designation", res);
    });
    getDropdown("assignments_department", function(res) {
      checkCountAndCall("assignments_department", res);
    });
    getDropdown("assignments_position", function(res) {
      checkCountAndCall("assignments_position", res);
    });

    var type = getUrlVars()["type"];
    var id = getUrlVars()["id"];
    $('#asOnDate').datepicker({
        format: 'DD/MM/YYYY',
        defaultDate: ""
    });

    $('#asOnDate').val("");
    $('#asOnDate').on("change", function(e) {
          _this.setState({
                searchSet: {
                    ..._this.state.searchSet,
                    "asOnDate":$("#asOnDate").val()
                }
          })
      });
  }

  handleChange(e,name) {
      this.setState({
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })
  }

  close(){
      // widow.close();
      open(location, '_self').close();
  }

  handleClick(type, id) {
    if (type === "update") {
      window.open(`app/hr/employee/create.html?id=${id}&type=${type}`, '_blank', 'height=760, width=800, scrollbars=yes, status=yes');
    }else {
      window.open(`app/hr/employee/create.html?id=${id}&type=${type}`, '_blank', 'height=760, width=800, scrollbars=yes, status=yes');
    }
  }

  render() {

    let {handleChange,search,handleBlur,handleClick}=this;
    let {isSearchClicked,employees, assignments_designation, assignments_department ,assignments_position}=this.state;
    let {
    code,
    departmentId,
    designationId,
    asOnDate,name,mobileNumber,pan,aadhaarNumber}=this.state.searchSet;
    const renderOption = function(list,listName="") {
        if(list)
        {
            if (listName==="year") {
              return list.map((item)=>
              {
                  return (<option key={item.id} value={item.name}>
                          {item.name}
                    </option>)
              })
            }
            else {
              return list.map((item)=>
              {
                  return (<option key={item.id} value={item.id}>
                          {item.name}
                    </option>)
              })
            }
        }
    }
    const showTable = function() {
      if(isSearchClicked)
      {
          return (
            <table id="employeeTable" className="table table-bordered">
                <thead>
                    <tr>
                        <th>Employee Code</th>
                        <th>Employee Name</th>
                        <th>Employee Designation</th>
                        <th>Employee Department</th>
                        <th>Employee Position</th>
                        <th>Date-Range</th>
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

    const renderBody = function() {
      if(employees.length>0){
      return employees.map((item,index)=> {
            var ind = 0, sInd = 0;
            for(var i=0; i<item.assignments.length; i++) {
              var _from = item.assignments[i].fromDate;;
              var _to = item.assignments[i].toDate;
              var today = new Date().getTime();
              var dateParts1 = _from.split("/");
              var newDateStr1 = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
              var date1 = new Date(newDateStr1).getTime();
              var dateParts2 = _to.split("/");
              var newDateStr2 = dateParts2[1] + "/" + dateParts2[0] + "/ " + dateParts2[2];
              var date2 = new Date(newDateStr2).getTime();
              if(date1<=today && today<=date2 && [true, "true"].indexOf(item.assignments[i].isPrimary) > -1) {
                ind = i;
                break;
              }
              sInd = [true, "true"].indexOf(item.assignments[i].isPrimary) > -1 ? i : (sInd || 0);
            }
            if(!ind)
              ind = sInd;
            return (<tr key={index} onClick={() => {handleClick(getUrlVars()["type"], item.id)}}>
                    <td data-label="code">{item.code}</td>
                    <td data-label="name">{item.name}</td>

                    <td data-label="designation">{getNameById(assignments_designation,item.assignments[ind].designation)}</td>
                    <td data-label="department">{getNameById(assignments_department,item.assignments[ind].department)}</td>
                    <td data-label="position">{getNameById(assignments_position,item.assignments[ind].position)}</td>
                    <td data-label="range">{item.assignments[ind].fromDate}-{item.assignments[ind].toDate}</td>
                </tr>
            );

      })
    }
}

    const disbaled=function(type) {
        if (type==="view") {
              return "ture";
        } else {
            return "false";
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
                      <label for="">Department  </label>
                    </div>
                    <div className="col-sm-6">
                    <div className="styled-select">
                        <select id="department" name="departmentId" value={departmentId} onChange={(e)=>{
                            handleChange(e,"departmentId")
                        }}>
                          <option value="">Select department</option>
                          {renderOption(this.state.assignments_department)}
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
                          <select id="designation" name="designationId" value={designationId} onChange={(e)=>{
                              handleChange(e,"designationId")
                          }}>
                          <option value="">Select Designation</option>
                          {renderOption(this.state.assignments_designation)}
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
                      <label for="">Employee Code  </label>
                    </div>
                    <div className="col-sm-6">
                        <input type="text" name="code" id="code" onChange={(e)=>{
                            handleChange(e,"code")
                        }} onBlur={(e)=>{handleBlur(e)}}/>
                    </div>
                </div>
              </div>
              {<div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                        <label for=""> Employee Name  </label>
                      </div>
                      <div className="col-sm-6">
                          <input type="text" name="name" id="name" value= {name} onChange={(e)=>{
                              handleChange(e,"name")
                          }} disabled/>
                      </div>
                  </div>
                </div>}
          </div>
        {/*<div className="row">
            <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="description">As On Date</label>
                        </div>
                        <div className="col-sm-6">
                            <input type="text" id="asOnDate" name="asOnDate" value= {asOnDate}
                              onChange={(e)=>{handleChange(e,"asOnDate")}} />
                        </div>
                      </div>
                  </div>
            <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="description">Mobile Number</label>
                        </div>
                        <div className="col-sm-6">
                            <input type="number" id="mobileNumber" name="mobileNumber" value= {mobileNumber}
                              onChange={(e)=>{handleChange(e,"mobileNumber")}}/>
                        </div>
                      </div>
                  </div>
        </div>
    <div>
        <div className="col-sm-6">
            <div className="row">
                <div className="col-sm-6 label-text">
                  <label for="">Pan  </label>
                </div>
                <div className="col-sm-6">
                    <input type="text" name="pan" id="pan" onChange={(e)=>{
                        handleChange(e,"pan")
                    }} />
                </div>
            </div>
          </div>
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="">Aadhar Code  </label>
                  </div>
                  <div className="col-sm-6">
                      <input type="text" name="aadhaarNumber" id="aadhaarNumber" onChange={(e)=>{
                          handleChange(e,"aadhaarNumber")
                      }} />
                  </div>
              </div>
            </div>
        </div>*/}
            <div className="text-center">
              <button id="sub" type="submit"  className="btn btn-submit">Search</button>&nbsp;&nbsp;
                <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>

            </div>
          </fieldset>
          </form>
          <br/>

          {showTable()}

      </div>
    );
  }
}






ReactDOM.render(
  <EmployeeSearch />,
  document.getElementById('root')
);
