var flag= 0;

class LeaveSummary extends React.Component {

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
            "asOnDate": ""
        },
        "employeeTypes": [],
        "departments": [],
        "designations": [],
        "employeeStatuses": [],
        "leaveTypes": [],
        "isSearchClicked": false,
        "error": ""
    };
    this.handleChange = this.handleChange.bind(this);
    this.searchEmployee = this.searchEmployee.bind(this);
    this.closeWindow = this.closeWindow.bind(this);
  }

  componentWillMount() {
    try {
      var assignments_designation = !localStorage.getItem("assignments_designation") || localStorage.getItem("assignments_designation") == "undefined" ? (localStorage.setItem("assignments_designation", JSON.stringify(getCommonMaster("hr-masters", "designations", "Designation").responseJSON["Designation"] || [])), JSON.parse(localStorage.getItem("assignments_designation"))) : JSON.parse(localStorage.getItem("assignments_designation"));
    } catch (e) {
        console.log(e);
         var assignments_designation = [];
    }

    try {
      var assignments_department = !localStorage.getItem("assignments_department") || localStorage.getItem("assignments_department") == "undefined" ? (localStorage.setItem("assignments_department", JSON.stringify(getCommonMaster("egov-common-masters", "departments", "Department").responseJSON["Department"] || [])), JSON.parse(localStorage.getItem("assignments_department"))) : JSON.parse(localStorage.getItem("assignments_department"));
    } catch (e) {
        console.log(e);
      var  assignments_department = [];
    }

    try {
      var employeeType = !localStorage.getItem("employeeType") || localStorage.getItem("employeeType") == "undefined" ? (localStorage.setItem("employeeType", JSON.stringify(getCommonMaster("hr-masters", "employeetypes", "EmployeeType").responseJSON["EmployeeType"] || [])), JSON.parse(localStorage.getItem("employeeType"))) : JSON.parse(localStorage.getItem("employeeType"));
      }
      catch (e) {
        console.log(e);
      var employeeType = [];
    }



    var employeeStatusList;
    var leaveTypes;

    getDropdown("employeeStatus", function(res) {
      employeeStatusList = res;
    });
    getDropdown("leaveTypes", function(res) {
      leaveTypes = res;
    });

     this.setState({
         ...this.state,
         departments: Object.assign([], assignments_department),
         designations: Object.assign([], assignments_designation),
         employeeTypes: Object.assign([], employeeType),
         employeeStatuses: Object.assign([], employeeStatusList),
         leaveTypes: Object.assign([], leaveTypes),
     });


     var _this = this;

     commonApiPost("hr-employee", "employees", "_search", {
       tenantId,
       pageSize: 500
     }, function(err, res) {
       if (res && res.Employee) {
         res.Employee.forEach(function(item, index, theArray) {
           theArray[index] = {
             "id": item.id,
             "name": item.code + " - " + item.name
           };
         });

         _this.setState({
           ..._this.state,
           employeeList: res.Employee
         });

       }
     });




  }


  componentDidMount() {

    var _this = this;

    $('#asOnDate').datepicker({
      format: 'dd/mm/yyyy',
      autoclose: true,
      defaultDate: ""
    });

    $('#asOnDate').on('changeDate', function(e) {
      _this.setState({
        searchSet: {
          ..._this.state.searchSet,
          "asOnDate": $("#asOnDate").val(),
        }
      });
    });
  }

  componentDidUpdate(prevProps, prevState) {
      if (flag === 1) {
        flag = 0;
          $('#employeeTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false
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

  closeWindow() {
      open(location, '_self').close();
  }

  searchEmployee (e) {
    e.preventDefault();
    $('#employeeTable').dataTable().fnDestroy();
    var _this = this;
    try {
        commonApiPost("hr-employee", "employees", "_search", {...this.state.searchSet, tenantId},function(err, res) {
          if(res) {
            flag = 1;
            _this.setState({
              ..._this.state,
                isSearchClicked: true,
                result : res
            })
          }
        })
      } catch (e) {
        console.log(e);
    }
  }

  render() {
    let {handleChange, searchEmployee, closeWindow} = this;
    let {result, employeeTypes, departments, designations, employeeStatuses, leaveTypes, employeeList, error} = this.state;
    let {employeeCode, department, designation, employeeType, employeeStatus, asOnDate, leaveType} = this.state.searchSet;

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
                <td>{getNameById(employeeList, item.employee)}</td>
                <td>{item.name}</td>
                <td>{item.employeeStatus}</td>
                <td>{item.employeeType}</td>
                <td>{item.fromdate}</td>
                <td>{item.enddate}</td>

                </tr>
            )
        })
    }

    const showTable = () => {
        if(this.state.isSearchClicked) {
            return (
                <div className="land-table">
                    <table className="table table-bordered" id="employeeTable">
                        <thead>
                            <tr>
                                <th>Employee Code-Name</th>
                                <th>Leave Type</th>
                                <th>Opening Balance</th>
                                <th>Leave Elligible</th>
                                <th>Total Leave Elligible</th>
                                <th>Approved Leaves</th>
                                <th>Balance </th>
                            </tr>
                        </thead>
                        <tbody>
                            {renderTr()}
                        </tbody>
                    </table>
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
                                        <label htmlFor="">Department </label>
                                    </div>
                                    <div className="col-sm-6">
                                    <div className="styled-select">
                                        <select id="department" value={department} onChange={(e) => {handleChange(e, "department")}}>
                                            <option value="" ></option>
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
                                        <select id="designation" value={designation} onChange={(e) => {handleChange(e, "designation")}}>
                                            <option value="" ></option>
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
                                        <input id="employeeCode" type="text" value={employeeCode} onChange={(e) => {handleChange(e, "employeeCode")}}/>
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
                                            <option value="" ></option>
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
                                        <select id="employeeStatus" value={employeeStatus} onChange={(e) => {handleChange(e, "employeeStatus")}}>
                                            <option value="" ></option>
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
                                        <select id="leaveType" value={leaveType} onChange={(e) => {handleChange(e, "leaveType")}}>
                                            <option value="" ></option>
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
                                        <label htmlFor="">As On Date </label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="text-no-ui">
                                                <span><i className="glyphicon glyphicon-calendar"></i></span>
                                                <input type="text" id="asOnDate" value={asOnDate} onChange={(e) => {handleChange(e, "asOnDate")}}/>
                                            </div>
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
            {showTable()}
        </div>
    );
  }
}

ReactDOM.render(
  <LeaveSummary />,
  document.getElementById('root')
);
