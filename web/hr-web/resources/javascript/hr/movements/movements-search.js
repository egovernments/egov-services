var flag = 0;
class EmployeeSearch extends React.Component {
  constructor(props) {
    super(props);
    this.state={employees:[],searchSet:{
    code:"",
    departmentId:"",
    designationId:"",
    employeeType:"",
    name:"",
    employeeTypeCode:"",
    employeeStatus:""},
    isSearchClicked:false,employeeTypeList:[],departmentList:[],designationList:[],employeeTypeList:[],employeeStatusList:[],
    modified: false}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
    this.handleBlur=this.handleBlur.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
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
    employeeTypeCode,
    employeeStatus}=this.state.searchSet;
    var _this = this;
    e.preventDefault();
    //call api call
    var employees = [];
    if(departmentId ||designationId||code||employeeType) {
      commonApiPost("hr-employee","employees","_search", {
          tenantId, code, departmentId, designationId, name, employeeType, employeeStatus:7,pageSize:500
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
    else {
      return(showError("Any one of the search criteria is mandatory."));

    }

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
    $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + "Search Employee for " + titleCase(getUrlVars()["value"]));
    var type = getUrlVars()["type"];
    var id = getUrlVars()["id"];
    var count = 5, _state = {}, _this = this;

    const checkCountAndCall = function(key, res) {
      _state[key] = res;
      count--;
      if(count == 0)
        _this.setInitialState(_state);
    }

    getDropdown("assignments_designation", function(res) {
      checkCountAndCall("designationList", res);
    });
    getDropdown("assignments_department", function(res) {
      checkCountAndCall("departmentList", res);
    });
    getDropdown("assignments_position", function(res) {
      checkCountAndCall("assignments_position", res);
    });
    getDropdown("employeeStatus", function(res) {
      checkCountAndCall("employeeStatusList", res);
    });
    getDropdown("employeeType", function(res) {
      checkCountAndCall("employeeTypeList", res);
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

  render() {

    let {handleChange,search,handleBlur}=this;
    let {isSearchClicked,employees, designationList, departmentList ,assignments_position,employeeTypeList,employeeStatusList}=this.state;
    let {
    code,
    departmentId,
    designationId,
    employeeTypeCode,employeeStatus,name}=this.state.searchSet;
    const renderOption=function(list) {
        if(list)
        {
            return list.map((item, ind)=>
            {
                return (<option key={ind} value={typeof item == "object" ? item.id : item}>
                        {typeof item == "object" ? (item.name ? item.name :item.code ) : item}
                  </option>)
            })
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
                        <th>Employee Status</th>
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


    const renderAction=function(type,value,id) {
      if(value==="transfer"){
        if (type==="update") {
                return (
                        <a href={`app/hr/movements/transfer/transfer.html?id=${id}&type=${type}`}>Initiate</a>
                );
        } else if (type==="view") {
              return (
                      <a href={`app/hr/movements/transfer/transfer.html?id=${id}&type=${type}`}>Initiate</a>
              );
        } else {
            return (
                    <a href={`app/hr/movements/transfer/transfer.html?id=${id}`} >Initiate</a>
            );
        }
      } else {
        if (type==="update") {
                return (
                        <a href={`app/hr/movements/promotion/promotion.html?id=${id}&type=${type}`}>Initiate</a>
                );
        } else if (type==="view") {
              return (
                      <a href={`app/hr/movements/promotion/promotion.html?id=${id}&type=${type}`}>Initiate</a>
              );
        } else {
            return (
                    <a href={`app/hr/movements/promotion/promotion.html?id=${id}`} >Initiate</a>
            );
        }
      }

  }

    const renderBody = function() {
      if(employees.length>0)
      {
      return employees.map((item,index)=>
      {
            var ind = 0;
            for(var i=0; i<item.assignments.length; i++) {
              if([true, "true"].indexOf(item.assignments[i].isPrimary) > -1) {
                ind = i;
                break;
              }
            }

            return (<tr key={index}>
                    <td data-label="code">{item.code}</td>
                    <td data-label="name">{item.name}</td>
                    <td data-label="designation">{getNameById(designationList,item.assignments[ind].designation)}</td>
                    <td data-label="department">{getNameById(departmentList,item.assignments[ind].department)}</td>
                    <td data-label="position">{getNameById(employeeStatusList,item.employeeStatus,"code")}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],getUrlVars()["value"],item.id)}
                    </td>
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
        <h3>Search Employee for {titleCase(getUrlVars()["value"])}</h3>
          <form onSubmit={(e)=>{search(e)}}>
          <fieldset>
          <div className="row">
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">Department  </label>
                    </div>
                    <div className="col-sm-6">
                    <div className="styled-select">
                        <select id="department" name="departmentId" value={departmentId} onChange={(e)=>{
                            handleChange(e,"departmentId")
                        }}>
                          <option value="">Select department</option>
                          {renderOption(this.state.departmentList)}
                       </select>
                    </div>
                    </div>
                </div>
              </div>
              <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                        <label htmlFor="">Designation  </label>
                      </div>
                      <div className="col-sm-6">
                      <div className="styled-select">
                          <select id="designation" name="designationId" value={designationId} onChange={(e)=>{
                              handleChange(e,"designationId")
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
                      <label htmlFor="">Employee Code  </label>
                    </div>
                    <div className="col-sm-6">
                        <input type="text" name="code" id="code" onChange={(e)=>{
                            handleChange(e,"code")
                        }} onBlur={(e)=>{handleBlur(e)}}/>
                    </div>
                </div>
              </div>
              <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                        <label htmlFor=""> Employee Name  </label>
                      </div>
                      <div className="col-sm-6">
                          <input type="text" name="name" id="name" value= {name} onChange={(e)=>{
                              handleChange(e,"name")
                          }} disabled/>
                      </div>
                  </div>
                </div>
            </div>
        {/*<div className="row">
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                      <label htmlFor="">Type  </label>
                    </div>
                    <div className="col-sm-6">
                      <div className="styled-select">
                        <select id="employeeTypeCode" name="employeeTypeCode" value={employeeTypeCode} onChange={(e)=>{
                            handleChange(e,"employeeTypeCode")}}>
                        <option>Select Type</option>
                        {renderOption(this.state.employeeTypeList)}
                       </select>
                    </div>
                    </div>
                </div>
              </div>
              <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                        <label htmlFor=""> Status</label>
                      </div>
                      <div className="col-sm-6">
                      <div className="styled-select">
                          <select id="employeeStatus" name="employeeStatus" value={employeeStatus}
                            onChange={(e)=>{ handleChange(e,"employeeStatus") }}>

                              <option value="">Select Status</option>
                              {renderOption(this.state.employeeStatusList)}
                         </select>
                      </div>
                      </div>
                  </div>
                </div>
          </div>*/}
          <div className="text-right text-danger">
                          Note: Any one of the search criteria is mandatory.
                    </div>
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
