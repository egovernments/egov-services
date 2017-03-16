


class LeaveReport extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
        "result": [],
        "searchSet": {
            "employeeCode": "",
            "department": "",
            "designation": "",
            "employeeType": "",
            "employeeStatus": "",
            "leaveType": "",
            "dateFrom": "",
            "dateTo": "",
            "leaveStatus": ""
        },
        "employeeTypes": [],
        "departments": [],
        "designations": [],
        "leaveStatuses": [],
        "leaveTypes": [],
        "employeeStatuses": [],
        "isSearchClicked": false
    };
    this.handleChange = this.handleChange.bind(this);
    this.searchEmployee = this.searchEmployee.bind(this);
    this.closeWindow = this.closeWindow.bind(this);
  }

  componentWillMount() {
     this.setState({
         ...this.state,
         departments: commonApiPost("egov-common-masters", "departments", "_search", {tenantId}).responseJSON["Department"] || [],
         designations: commonApiPost("hr-masters", "designations", "_search", {tenantId}).responseJSON["Designation"] || [],
         employeeTypes: commonApiPost("hr-masters", "employeetypes", "_search", {tenantId}).responseJSON["EmployeeType"] || []
     });
  }

  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.result.length!=this.state.result.length) {
          $('#employeeTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false
          });
      }
  }

  closeWindow(){
      open(location, '_self').close();
  }

  searchEmployee(e) {
    e.preventDefault();
    console.log(this.state.searchSet);
  }

  handleChange(e, name)
  {
      this.setState({
          ...this.state,
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })
  }

  render() {
    let {handleChange, searchEmployee, closeWindow} = this;
    let {result, employeeTypes, departments, designations, leaveStatuses, leaveTypes, employeeStatuses} = this.state;
    let {employeeCode, department, designation, employeeType, employeeStatus, leaveStatus, leaveType, dateFrom, dateTo} = this.state.searchSet;

    const renderOptions = function(list)
    {
        if(list && list.constructor == Array)
        {
            return list.map((item)=>
            {
                return (<option key={item.id} value={item.id}>
                        {item.name}
                    </option>)
            })
        }
    }

    const renderTr = () => {
        return result.map((item, ind) => {
            return (
                <tr key={ind}>
                    <td>89-Kumaresh </td>
                    <td>123</td>
                    <td>casual</td>
                    <td>2</td>
                    <td>ok</td>
                    <td>fine</td>
                </tr>
            )
        })
    }

    const showTable = () => {
        if(this.state.isSearchClicked) {
            return (
                <div>
                    <div className="land-table">
                        <table id="employeeTable" className="table table-bordered">
                            <thead>
                                <tr>
                                <th>Employee Code-Name</th>
                                <th>Application No</th>
                                <th>Leave Type</th>
                                <th>No Of. Days</th>
                                <th>Status</th>
                                <th>Comment </th>
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
                    <form onSubmit={(e)=>
                    {searchEmployee(e)}}>
                        <fieldset>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Department </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <select id="department" value={department} onChange={(e) => {handleChange(e, "department")}}>
                                                <option value="" selected></option>
                                                {renderOptions(departments)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Designation </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <select id="designation" value={designation} onChange={(e) => {handleChange(e, "designation")}}>
                                                <option value="" selected></option>
                                                {renderOptions(designations)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Employee Code/Name </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <input id="employeeCode" type="text" value={employeeCode} onChange={(e) => {handleChange(e, "employeeCode")}}/>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Employee Type </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <select id="employeeType" value={employeeType} onChange={(e) => {handleChange(e, "employeeType")}}>
                                                <option value="" selected></option>
                                                {renderOptions(employeeTypes)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Employee Status </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <select id="employeeStatus" value={employeeStatus} onChange={(e) => {handleChange(e, "employeeStatus")}}>
                                                <option value="" selected></option>
                                                {renderOptions(employeeStatuses)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Leave Type </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <select id="leaveType" type="text" value={leaveType} onChange={(e) => {handleChange(e, "leaveType")}}>
                                                <option value="" selected></option>
                                                {renderOptions(leaveTypes)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Date From </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="text-no-ui">
                                                <span><i className="glyphicon glyphicon-calendar"></i></span>
                                                <input type="date" id="dateFrom" value={dateFrom} onChange={(e) => {handleChange(e, "dateFrom")}}/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Date To </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="text-no-ui">
                                                <span><i className="glyphicon glyphicon-calendar"></i></span>
                                                <input type="date" id="dateTo" value={dateTo} onChange={(e) => {handleChange(e, "dateTo")}}/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label for="">Leave Status </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <select id="leaveStatus" type="text" value={leaveStatus} onChange={(e) => {handleChange(e, "leaveStatus")}}>
                                                <option value="" selected></option>
                                                {renderOptions(leaveStatuses)}
                                            </select>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="text-center">
                                <button type="button" className="btn btn-submit" onClick={(e)=>{this.closeWindow()}}>Close</button>
                                &nbsp;&nbsp;
                                <button type="submit" className="btn btn-submit">Search</button>
                            </div>
                        </fieldset>
                    </form>
                    {showTable()}
                </div>
    );
  }
}






ReactDOM.render(
  <LeaveReport />,
  document.getElementById('root')
);
