var flag = 0;

class EmployeeAttendence extends React.Component {

 constructor (props) {
    super(props);
    this.state = {
        "result": [],
        "noOfDaysInMonth": "",
        "noOfWorkingDays": "",
        "searchSet": {
            "code": "",
            "departmentId": "",
            "designationId": "",
            "employeeType": "",
            "month": "",
            "year": ""
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
            {name: "December", id: "12"}
        ],
        "calenderYears": [],
        "employeeList" : [],
        "isSearchClicked": false,
        "error": ""
    };
    this.handleChange = this.handleChange.bind(this);
    this.closeWindow = this.closeWindow.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.searchEmployeeAttendance = this.searchEmployeeAttendance.bind(this);
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
   getDropdown("years", function(res) {
     checkCountAndCall("calenderYears", res);
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

 searchEmployeeAttendance (e) {
   e.preventDefault();
   $('#employeeTable').dataTable().fnDestroy();
   var _this = this;
   try {
        flag = 1;
        commonApiPost("hr-attendance", "attendances", "_attendancereport", {..._this.state.searchSet, tenantId,pageSize:500},function(err, res) {
          if(res) {
            console.log(res);
            _this.setState({
              ..._this.state,
                isSearchClicked: true,
                noOfDaysInMonth:res.NoOfDaysInMonth,
                noOfWorkingDays:res.NoOfWorkingDays,
                result : res.Attendance
            })
          }else {
            _this.setState({
              ..._this.state,
                isSearchClicked: true
            })
          }
        });
   } catch(e) {
        console.log(e);
   }

 }



 render () {
    let {handleChange, searchEmployeeAttendance, closeWindow} = this;
    let {result, employeeTypes, departments, noOfDaysInMonth, noOfWorkingDays, designations, months, calenderYears, employeeList, error} = this.state;
    let {code, departmentId, designationId, employeeType, month, year} = this.state.searchSet;

    const renderOptions = function(list)
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

    const renderOptionsYear = function(list)
    {
        if(list)
        {
            return list.map((item)=>
            {
                return (<option key={item.id} value={item.name}>
                        {item.name}
                    </option>)
            })
        }
    }


    const renderTr = () => {
        return result.map((item, ind) => {
            return (
                <tr key={ind}>
                <td>{getNameById(employeeList, item.employee)}</td>
                <td>{item.presentDays}</td>
                <td>{item.absentDays}</td>
                <td>{item.leaveDays}</td>
                <td>{item.noOfOts}</td>
                </tr>
            )
        })
    }

    const showTable = () => {
        if(this.state.isSearchClicked) {
            return (
                <div>
                    <br/>
                    <br/>
                    <div className="form-section" >

                    <h3 className="pull-left">Employee Details </h3>
                    <div className="clearfix"></div>

                    <div className="row">
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                <label htmlFor="">Number Of Days In Month </label>
                                </div>
                                <div className="col-sm-6">
                                <input id="noOfDaysInMonth" type="text" value={noOfDaysInMonth} onChange={(e) => {handleChange(e, "noOfDaysInMonth")}} disabled/>
                                </div>
                            </div>
                        </div>
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                <label htmlFor="">Number Of Working Days</label>
                                </div>
                                <div className="col-sm-6">
                                <input id="noOfWorkingDays" type="text" value={noOfWorkingDays} onChange={(e) => {handleChange(e, "noOfWorkingDays")}} disabled/>
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
                </div>
            )
        }
    }


    return (
        <div>
            <form onSubmit={(e)=>
                    {searchEmployeeAttendance(e)}}>
                <fieldset>
                    <div className="row">
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                    <label htmlFor="">Month <span> * </span> </label>
                                </div>
                                <div className="col-sm-6">
                                <div className="styled-select">
                                    <select id="month" value={month} onChange={(e) => {handleChange(e, "month")}} required>
                                        <option value="">Select Month</option>
                                        {renderOptions(months)}
                                    </select>
                                </div>
                                </div>
                            </div>
                        </div>
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                    <label htmlFor="">Calender Year <span> *</span> </label>
                                </div>
                                <div className="col-sm-6">
                                <div className="styled-select">
                                    <select id="year" value={year} onChange={(e) => {handleChange(e, "year")}} required>
                                        <option value="">Select Year</option>
                                        {renderOptionsYear(calenderYears)}
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
                                            <option value="">Select designation</option>
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
                                        <option value="">Select Employee Type</option>
                                        {renderOptions(employeeTypes)}
                                    </select>
                                </div>
                                </div>
                            </div>
                        </div>
                    </div>
                    <p className="text-danger">{error}</p>
                    <div className="text-center">

                    <button type="submit" className="btn btn-submit">Search</button>
                    &nbsp; &nbsp;
                        <button type="button" className="btn btn-close" onClick={(e)=>{this.closeWindow()}}>Close</button>

                    </div>
                </fieldset>
            </form>
            {showTable()}
        </div>
    );
  }
}


ReactDOM.render(
  <EmployeeAttendence />,
  document.getElementById('root')
);
