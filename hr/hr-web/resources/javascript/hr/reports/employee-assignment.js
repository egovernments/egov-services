// var EmployeeReportCommon=require("../common/employee-search-common.js");

// function getUrlVars() {
//     var vars = [],
//         hash;
//     var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
//     for (var i = 0; i < hashes.length; i++) {
//         hash = hashes[i].split('=');
//         vars.push(hash[0]);
//         vars[hash[0]] = hash[1];
//     }
//     return vars;
// }


class EmployeeReport extends React.Component {

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
            "date":"",
        },
        "employeeTypes": [],
        "departments": [],
        "designations": [],
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

  closeWindow ()
  {
      open(location, '_self').close();
  }

  searchEmployee (e)
  {
     console.log(commonApiPost("hr-employee", "employees", "_search", {...this.state.searchSet, tenantId}).responseJSON["Employee"]);
    e.preventDefault();
    this.setState({
        ...this.state,
        isSearchClicked: true,
        result: commonApiPost("hr-employee", "employees", "_search", {...this.state.searchSet, tenantId}).responseJSON["Employee"]
    })
  }

  render() {
    let {handleChange, searchEmployee, closeWindow} = this;
    let {result, employeeTypes, departments, designations} = this.state;
    let {employeeCode, department, designation, employeeType, employeeStatus,date} = this.state.searchSet;

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
                    <td>{item.code}</td>
                    <td>{item.name}</td>
                    <td>{item.employeeStatus}</td>
                    <td>{item.employeeType}</td>
                    <td><a href="#" Employee>Employee </a></td>
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
                                <th>From Date </th>
                                <th> TO Date</th>
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
                <h3 className="pull-left">Employee Assignment Search </h3>
                <div className="clearfix"></div>
                <form onSubmit={(e)=>
                    {searchEmployee(e)}}>
                    <fieldset>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="">Department <span> * </span></label>
                                    </div>
                                    <div className="col-sm-6">
                                        <select id="department" value={department} required="true"onChange={(e) => {handleChange(e, "department")}}>
                                            <option value="" selected></option>
                                            {renderOptions(departments)}
                                        </select>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="">Designation <span> * </span> </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <select id="designation" value={designation} required="true" onChange={(e) => {handleChange(e, "designation")}}>
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
                                        <label for="">Employee Code/Name <span> * </span> </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <input id="employeeCode" type="text" value={employeeCode} required="true"onChange={(e) => {handleChange(e, "employeeCode")}}/>
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
                                        <label for="">Status </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <input id="employeeStatus" type="text" value={employeeStatus} onChange={(e) => {handleChange(e, "employeeStatus")}}/>
                                    </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for=""> From Date* </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="text-no-ui">
                                            <span><i className="glyphicon glyphicon-calendar"></i></span>
                                            <input type="date" id="date" value={date} onChange={(e) => {handleChange(e, "date")}} required/>
                                        </div>
                                    </div>
                                </div>
                            </div>

                        </div>

                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for=""> To Date* </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="text-no-ui">
                                            <span><i className="glyphicon glyphicon-calendar"></i></span>
                                            <input type="date" id="date" value={date} onChange={(e) => {handleChange(e, "date")}} required/>
                                        </div>
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
