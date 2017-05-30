
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

   try {
   var  year = !localStorage.getItem("year") || localStorage.getItem("year") == "undefined" ? (localStorage.setItem("year", JSON.stringify(getCommonMaster("egov-common-masters", "calendaryears", "CalendarYear").responseJSON["CalendarYear"] || [])), JSON.parse(localStorage.getItem("year"))) : JSON.parse(localStorage.getItem("year"));

   } catch (e) {
       console.log(e);
     var  year = [];
   }
     this.setState({
         ...this.state,
         departments: Object.assign([], assignments_department),
         designations: Object.assign([], assignments_designation),
         employeeTypes: Object.assign([], employeeType),
         calenderYears: Object.assign([], year)
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
   var result;
   try {
        result = commonApiPost("hr-employee", "employees", "_search", {...this.state.searchSet, tenantId,pageSize:500}).responseJSON["Employee"] || [];
   } catch(e) {
        result = [];
        console.log(e);
   }
   this.setState({
       ...this.state,
       isSearchClicked: true,
       result: result
   })
 }

 render () {
    let {handleChange, searchEmployee, closeWindow} = this;
    let {result, employeeTypes, departments, designations, months, calenderYears, error} = this.state;
    let {employeeCode, department, designation, employeeType, month, calenderYear} = this.state.searchSet;
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
                // <div>
                //     <br/>
                //     <br/>
                //     <div className="row">
                //         <div className="col-sm-6">
                //             <div className="row">
                //                 <div className="col-sm-6 label-text">
                //                 <label for="">Number Of Days In Month </label>
                //                 </div>
                //                 <div className="col-sm-6">
                //                 <input type="text" id="days" name="days" />
                //                 </div>
                //             </div>
                //         </div>
                //         <div className="col-sm-6">
                //             <div className="row">
                //                 <div className="col-sm-6 label-text">
                //                 <label for="">Number Of Working Days</label>
                //                 </div>
                //                 <div className="col-sm-6">
                //                 <input  type="text" id="Workingday" name="Workingday"/>
                //                 </div>
                //             </div>
                //         </div>
                //     </div>
                <div className="form-section" >
                    <h3 className="pull-left">Employee Details </h3>
                    <div className="clearfix"></div>

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
                                    <label for="">Month <span> * </span> </label>
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
                                    <label for="">Calender Year <span> *</span> </label>
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
