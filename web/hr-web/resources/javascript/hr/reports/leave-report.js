var flag = 0;
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
        "employeeList": [],
        "isSearchClicked": false
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
        var assignments_department = [];
      }

      try {
        var employeeType = !localStorage.getItem("employeeType") || localStorage.getItem("employeeType") == "undefined" ? (localStorage.setItem("employeeType", JSON.stringify(getCommonMaster("hr-masters", "employeetypes", "EmployeeType").responseJSON["EmployeeType"] || [])), JSON.parse(localStorage.getItem("employeeType"))) : JSON.parse(localStorage.getItem("employeeType"));
      } catch (e) {
        console.log(e);
        var employeeType = [];
      }

      var employeeStatusList;
      var leaveTypes;
      var leaveStatuses;

      getDropdown("employeeStatus", function(res) {
        employeeStatusList = res;
      });
      getDropdown("leaveTypes", function(res) {
        leaveTypes = res;
      });
      getDropdown("leaveStatus", function(res) {
        leaveStatuses = res;
      });


      this.setState({
        ...this.state,
        departments: Object.assign([], assignments_department),
        designations: Object.assign([], assignments_designation),
        employeeTypes: Object.assign([], employeeType),
        employeeStatuses: Object.assign([], employeeStatusList),
        leaveTypes: Object.assign([], leaveTypes),
        leaveStatuses: Object.assign([], leaveStatuses)
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

      $('#dateFrom').datepicker({
        format: 'dd/mm/yyyy',
        autoclose: true,
        defaultDate: ""
      });

      $('#dateFrom').on('changeDate', function(e) {
        _this.setState({
          searchSet: {
            ..._this.state.searchSet,
            "dateFrom": $("#dateFrom").val(),
          }
        });
      });


      $('#dateTo').datepicker({
        format: 'dd/mm/yyyy',
        autoclose: true,
        defaultDate: ""
      });

      $('#dateTo').on('changeDate', function(e) {
        _this.setState({
          searchSet: {
            ..._this.state.searchSet,
            "dateTo": $("#dateTo").val(),
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

    closeWindow() {
      open(location, '_self').close();
    }

    searchEmployee(e) {
      e.preventDefault();
      $('#employeeTable').dataTable().fnDestroy();
      var _this = this;
      if (this.state.searchSet.dateFrom && !this.state.searchSet.dateTo)
        return showError("Please enter To Date");
      if (!this.state.searchSet.dateFrom && this.state.searchSet.dateTo)
        return showError("Please enter From Date");
      try {
        flag = 1;
        commonApiPost("hr-leave", "leaveapplications", "_leavereport", { ...this.state.searchSet,
          tenantId
        }, function(err, res) {

          if (res && res.LeaveApplication) {

            _this.setState({
              ..._this.state,
              result: res.LeaveApplication,
              isSearchClicked: true
            })
          }else {
            _this.setState({
              ..._this.state,
              result: [],
              isSearchClicked: true
            })
          }
        });
      } catch (e) {
        console.log(e);
      }
    }

    handleChange(e, name) {
      this.setState({
        ...this.state,
        searchSet: {
          ...this.state.searchSet,
          [name]: e.target.value
        }
      })
    }
  render() {

    let {handleChange ,searchEmployee, closeWindow} = this;
    let {result, employeeTypes, departments, designations, leaveStatuses, leaveTypes, employeeStatuses, employeeList, isSearchClicked} = this.state;
    let {employeeCode, department, designation, employeeType, employeeStatus, leaveStatus, leaveType, dateFrom, dateTo} = this.state.searchSet;

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
      if(isSearchClicked){
        return result.map((item, ind) => {
            return (
                <tr key={ind}>
                <td>{getNameById(employeeList, item.employee)}</td>
                <td>{item.applicationNumber}</td>
                <td>{item.leaveType.name}</td>
                <td>{item.fromDate +"-"+ item.toDate}</td>
                <td>{item.leaveDays}</td>
                <td>{getNameById(leaveStatuses,item.status,"code")}</td>
                <td>{item.reason}</td>
                </tr>
            )
        })
      }
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
                                  <th>Application Number </th>
                                  <th>Leave Type</th>
                                  <th>Date Range</th>
                                  <th>Number Of Days</th>
                                  <th>Status</th>
                                  <th>Comments</th>


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
                                            <label htmlFor="">Department </label>
                                        </div>
                                        <div className="col-sm-6">
                                        <div className="styled-select">
                                            <select id="department" value={department} onChange={(e) => {handleChange(e, "department")}}>
                                                <option value="">Select Department</option>
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
                                                <option value="">search Employee Type</option>
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
                                                <option value="">search Employee Status</option>
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
                                            <select id="leaveType" type="text" value={leaveType} onChange={(e) => {handleChange(e, "leaveType")}}>
                                                <option value="">Select Leave Type</option>
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
                                            <label htmlFor="">Date From </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="text-no-ui">
                                                <span><i className="glyphicon glyphicon-calendar"></i></span>
                                                <input type="text" id="dateFrom" value={dateFrom} onChange={(e) => {handleChange(e, "dateFrom")}}/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="">Date To </label>
                                        </div>
                                        <div className="col-sm-6">
                                            <div className="text-no-ui">
                                                <span><i className="glyphicon glyphicon-calendar"></i></span>
                                                <input type="text" id="dateTo" value={dateTo} onChange={(e) => {handleChange(e, "dateTo")}}/>
                                            </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="row">
                                <div className="col-sm-6">
                                    <div className="row">
                                        <div className="col-sm-6 label-text">
                                            <label htmlFor="">Leave Status </label>
                                        </div>
                                        <div className="col-sm-6">
                                        <div className="styled-select">
                                            <select id="leaveStatus" type="text" value={leaveStatus} onChange={(e) => {handleChange(e, "leaveStatus")}}>
                                                <option value="">Select Leave Status</option>
                                                {renderOptions(leaveStatuses)}
                                            </select>
                                        </div>
                                        </div>
                                    </div>
                                </div>
                            </div>
                            <div className="text-center">
                            <button type="submit" className="btn btn-submit">Search</button>
                            &nbsp;&nbsp;

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
  <LeaveReport />,
  document.getElementById('root')
);
