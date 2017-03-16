
class EmployeeAttendence extends React.Component {
 
 constructor (props) {
    super(props);
    this.state = {
        "result": [],
        "searchSet": {
            "employeeCode": "",
            "department": "",
            "designation": "",
            "employeeType": "",
            "month": "",
            "calenderYear": ""
        },
        "employeeTypes": [],
        "departments": [],
        "designations": [],
        "months": [
            {name: "January", id: "1"},
            {name: "Febuary", id: "2"},
            {name: "March", id: "3"},
            {name: "April", id: "4"},
            {name: "May", id: "5"},
            {name: "June", id: "6"},
            {name: "July", id: "7"},
            {name: "August", id: "8"},
            {name: "September", id: "9"},
            {name: "October", id: "10"},
            {name: "November", id: "11"},
            {name: "Necember", id: "12"}
        ],
        "calenderYears": [],
        "isSearchClicked": false,
        "error": ""
    };
    this.handleChange = this.handleChange.bind(this);
    this.closeWindow = this.closeWindow.bind(this);
    this.searchEmployee = this.searchEmployee.bind(this);
 }

 componentWillMount() {
     this.setState({
         ...this.state,
         departments: commonApiPost("egov-common-masters", "departments", "_search", {tenantId}).responseJSON["Department"] || [],
         designations: commonApiPost("hr-masters", "designations", "_search", {tenantId}).responseJSON["Designation"] || [],
         employeeTypes: commonApiPost("hr-masters", "employeetypes", "_search", {tenantId}).responseJSON["EmployeeType"] || [],
         calenderYears: commonApiPost("egov-common-masters", "calendaryears", "_search", {tenantId}).responseJSON["CalendarYear"] || []
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

 handleChange (e, name) {
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
    e.preventDefault();

    if(!this.state.searchSet.department && !this.state.searchSet.designation && !this.state.searchSet.employeeCode && !this.state.searchSet.employeeType) {
        this.setState({
            ...this.state,
            error: "One of the above is mandatory"
        })
    } else {
        this.setState({
            ...this.state,
            error: ""
        });
        /*this.setState({
            ...this.state,
            isSearchClicked: true,
            result: commonApiPost("hr-employee", "employees", "_search", {...this.state.searchSet, tenantId}).responseJSON["Employee"] || []
        })*/
    }
 }

 render () {
    let {handleChange, searchEmployee, closeWindow} = this;
    let {result, employeeTypes, departments, designations, months, calenderYears, error} = this.state;
    let {employeeCode, department, designation, employeeType, month, calenderYear} = this.state.searchSet;
    const renderTr = () => {
        return result.map((item, ind) => {
            return (
                <tr key={ind}>
                    <td>89-Kumaresh </td>
                    <td>20</td>
                    <td>3</td>
                    <td>1</td>
                    <td>2</td>
                </tr>
            )
        })
    }

    const showTable = () => {
        if(this.props.isSearchClicked) {
            return (
                <div>
                    <br/>
                    <br/>
                    <div className="row">
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                <label for="">Number Of Days In Month </label>
                                </div>
                                <div className="col-sm-6">
                                <input type="text" id="days" name="days" />
                                </div>
                            </div>
                        </div>
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                <label for="">Number Of Working Days</label>
                                </div>
                                <div className="col-sm-6">
                                <input  type="text" id="Workingday" name="Workingday"/>
                                </div>
                            </div>
                        </div>
                    </div>
                    <div className="land-table">
                        <table id="employeeTable" className="table table-bordered">
                            <thead>
                                <tr>
                                <th>Employee Code-Name</th>
                                <th>Present Days</th>
                                <th>Absent Days</th>
                                <th>Leave Days</th>
                                <th>No of Ots</th>
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

    return (
        <div>
            <form onSubmit={(e)=>
                    {searchEmployee(e)}}>
                <fieldset>
                    <div className="row">
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                    <label for="">Month* </label>
                                </div>
                                <div className="col-sm-6">
                                    <select id="month" value={month} onChange={(e) => {handleChange(e, "month")}} required>
                                        <option value="" selected></option>
                                        {renderOptions(months)}
                                    </select>
                                </div>
                            </div>
                        </div>
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                    <label for="">Calender Year* </label>
                                </div>
                                <div className="col-sm-6">
                                    <select id="calenderYear" value={calenderYear} onChange={(e) => {handleChange(e, "calenderYear")}} required>
                                        <option value="" selected></option>
                                        {renderOptions(calenderYears)}
                                    </select>
                                </div>
                            </div>
                        </div>
                    </div>
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
                    <p className="text-danger">{error}</p>
                    <div className="text-center">
                        <button type="button" className="btn btn-submit" onClick={(e)=>{this.closeWindow()}}>Close</button>
                        &nbsp;&nbsp;
                        <button type="submit" className="btn btn-submit">Search</button>
                    </div>
                </fieldset>
            </form>
            
        </div>
    );
  }
}






ReactDOM.render(
  <EmployeeAttendence />,
  document.getElementById('root')
);
