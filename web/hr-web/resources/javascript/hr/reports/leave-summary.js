var flag= 0;

class LeaveSummary extends React.Component {

 constructor(props) {
    super(props);
    this.state = {
        "result": [],
        "searchSet": {
            "employeeCode": "",
            "departmentId": "",
            "designationId": "",
            "employeeType": "",
            "employeeStatus": "",
            "leaveType": "",
            "toDate": ""
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
    this.setInitialState = this.setInitialState.bind(this);
 }

 setInitialState(initState) {
   this.setState(initState);
 }

 componentWillMount() {


   var _state = {}, _this = this, count = 5;
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
   getDropdown("leaveTypes", function(res) {
     checkCountAndCall("leaveTypes", res);
   });
   getDropdown("employeeStatus", function(res) {
     checkCountAndCall("employeeStatuses", res);
   });

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

    $('#toDate').datepicker({
      format: 'dd/mm/yyyy',
      autoclose: true,
      defaultDate: ""
    });

    $('#toDate').on('changeDate', function(e) {
      _this.setState({
        searchSet: {
          ..._this.state.searchSet,
          "toDate": $("#toDate").val(),
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

  closeWindow() {
      open(location, '_self').close();
  }

  searchEmployee (e) {
    e.preventDefault();
    $('#employeeTable').dataTable().fnDestroy();
    var _this = this;
    try {
        flag = 1;
        commonApiPost("hr-leave", "leaveapplications", "_leavesummaryreport", {...this.state.searchSet, tenantId,pageSize:500},function(err, res) {
          if(res && res.LeaveApplication) {
            _this.setState({
              ..._this.state,
                isSearchClicked: true,
                result : res.LeaveApplication
            })
          }else {
            _this.setState({
              ..._this.state,
                isSearchClicked: true,
                result : []
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
    let {employeeCode, departmentId, designationId, employeeType, employeeStatus, toDate, leaveType} = this.state.searchSet;

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
                                        <select id="departmentId" value={departmentId} onChange={(e) => {handleChange(e, "departmentId")}}>
                                            <option value="" >Select Department</option>
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
                                            <option value="" >Select Designation</option>
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
                                            <option value="" >Select Employee Type</option>
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
                                            <option value="" >Select Employee Status</option>
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
                                            <option value="" >Select Leave Type</option>
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
                                        <label htmlFor="">As On Date <span>*</span></label>
                                    </div>
                                    <div className="col-sm-6">
                                        <div className="text-no-ui">
                                                <span><i className="glyphicon glyphicon-calendar"></i></span>
                                                <input type="text" id="toDate" value={toDate} onChange={(e) => {handleChange(e, "toDate")}} required/>
                                            </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <p className="text-danger">{error}</p>
                        <div className="text-center">
                            <button type="submit" className="btn btn-submit">Search</button>&nbsp;&nbsp;
                            <button type="button" className="btn btn-submit" onClick={(e)=>{this.closeWindow()}}>Close</button>
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
