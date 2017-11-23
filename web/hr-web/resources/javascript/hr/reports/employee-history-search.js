var flag = 0;
class EmployeeReport extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
        "result": [],
        "searchSet": {
            "code": "",
            "departmentId": "",
            "designationId": "",
            "employeeType": "",
            "employeeStatus": ""
        },
        "employeeTypes": [],
        "departments": [],
        "designations": [],
        "status":[],
        "isSearchClicked": false
    };
    this.handleChange = this.handleChange.bind(this);
    this.searchEmployee = this.searchEmployee.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.closeWindow = this.closeWindow.bind(this);
  }
  
  setInitialState(initState) {
    this.setState(initState);
  }

  componentWillMount() {


    var _state = {}, _this = this, count = 4;
    const checkCountAndCall = function(key, res) {
      _state[key] = res;
      count--;
      if(count == 0)
        _this.setInitialState(_state);
    }

    getDropdown("employeeType", function(res) {
      checkCountAndCall("employeeTypes", res);
    });
    getDropdown("assignments_department", function(res) {
      checkCountAndCall("departments", res);
    });
    getDropdown("assignments_designation", function(res) {
      checkCountAndCall("designations", res);
    });
    getDropdown("employeeStatus", function(res) {
      checkCountAndCall("status", res);
    });


  }

  componentDidUpdate(prevProps, prevState)
  {
      if (flag === 1) {
          flag = 0;
          $('#employeeTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false,
             language: {
               "emptyTable": "No Records"
             }
          });
      }
  }

  handleChange(e, name) {
      this.setState({
          ...this.state,
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })
  }

  closeWindow () {
      open(location, '_self').close();
  }

  searchEmployee (e) {
    var _this = this;
    e.preventDefault();
    $('#employeeTable').DataTable().destroy();

    try {
        flag = 1;
        commonApiPost("hr-employee", "employees", "_search", {...this.state.searchSet, tenantId,pageSize:500}, function(err, res) {
          if(res && res.Employee) {
            _this.setState({
              ..._this.state,
                isSearchClicked: true,
                result : res.Employee
            })
          }else {
            _this.setState({
              ..._this.state,
                isSearchClicked: true,
                result : []
            })
          }
        });
    } catch (e) {
        result = [];
        console.log(e);
    }
  }

  render() {
    let {handleChange, searchEmployee, closeWindow} = this;
    let {result, employeeTypes, departments, status, employeeStatusList, designations} = this.state;
    let {code, departmentId, designationId, employeeType, employeeStatus} = this.state.searchSet;

    const renderOptions = function(list)
    {
        if(list && list.constructor == Array)
        {
            return list.map((item)=>
            {
                return (<option key={item.id} value={item.id}>
                        {item.name?item.name:item.code}
                    </option>)
            })
        }
    }

    const renderTr = () => {
        return result.map((item, ind) => {
            return (
                <tr key={ind}>
                    <td>{item.code}</td>
                    <td>{item.name}</td>
                    <td data-label="designation">{getNameById(designations,item.assignments[0].designation)}</td>
                    <td data-label="department">{getNameById(departments,item.assignments[0].department)}</td>
                    <td><a href={"app/hr/reports/employee-history-report.html?id=" + item.id} >Employee History </a></td>
                </tr>
            )
        })
    }

    const showTable = () => {
        if(this.state.isSearchClicked) {
            return (
                <div className="form-section" >
                    <h3 className="pull-left">Employee Details </h3>
                    <div className="clearfix"></div>
                    <div className="land-table">
                        <table id="employeeTable" className="table table-bordered">
                            <thead>
                            <tr>
                                <th>Employee Code</th>
                                <th>Employee Name</th>
                                <th>Employee Designation</th>
                                <th>Employee Department</th>
                                <th>Reports</th>
                            </tr>
                            </thead>
                            <tbody>
                                {renderTr()}
                            </tbody>
                        </table>
                    </div>
                </div>
            )
        }
    }

    return (
        <div>
            <div className="form-section">
                <h3 className="pull-left">Employee Search </h3>
                <div className="clearfix"></div>
                <form onSubmit={(e)=>
                    {searchEmployee(e)}}>
                    <fieldset>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label htmlFor="">Department </label>
                                    </div>
                                    <div className="col-sm-6">
                                      <div className="styled-select">
                                        <select id="departmentId" value={departmentId} onChange={(e) => {handleChange(e, "departmentId")}}>
                                            <option value="">Select department</option>
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
                                        <select id="designationId" value={designationId} onChange={(e) => {handleChange(e, "designationId")}}>
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
                                        <input id="code" type="text" value={code} onChange={(e) => {handleChange(e, "code")}}/>
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
                                        <select id="employeeType" value={employeeType} onChange={(e) => {handleChange(e, "employeeType")}}>
                                          <option value="">Select EmployeeType</option>
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
                                        <label htmlFor="">Status </label>
                                    </div>
                                    <div className="col-sm-6">
                                      <div className="styled-select">
                                        <select id="employeeStatus" value={employeeStatus} onChange={(e) => {handleChange(e, "employeeStatus")}}>
                                          <option value="">Select EmployeeStatus</option>
                                            {renderOptions(status)}
                                        </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div className="text-center">


                            <button type="submit" className="btn btn-submit">Search</button>&nbsp;&nbsp;
                            <button type="button" className="btn btn-close" onClick={(e)=>{this.closeWindow()}}>Close</button>
                        </div>
                    </fieldset>
                </form>
            </div>
            {showTable()}
        </div>
    );
  }
}






ReactDOM.render(
  <EmployeeReport />,
  document.getElementById('root')
);
