class EmployeeReport extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
        "result": [],
        "employeeCode":"",
        "employeeName":"",
        "departments": [],
        "designations": [],
    };

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


     this.setState({
         ...this.state,
         departments: Object.assign([], assignments_department),
         designations: Object.assign([], assignments_designation),
         assignments_department,
         assignments_designation

     });


       var _this = this;
       var id = getUrlVars()["id"];

           commonApiPost("hr-employee", "employees", "_search", {id, tenantId,pageSize:500}, function(err, res) {
             if(res && res.Employee && res.Employee[0].assignments) {
               console.log(res.Employee[0]);
               console.log(res.Employee[0].code +  res.Employee[0].name);
               _this.setState({
                 ..._this.state,
                   employeeCode:res.Employee[0].code,
                   employeeName:res.Employee[0].name,
                   result : res.Employee[0].assignments
               })
             } else {
               result = [];
               console.log(err);

             }
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


  closeWindow () {
      open(location, '_self').close();
  }


  render() {
    let { closeWindow} = this;
    let {result, employeeTypes, departments, designations,assignments_designation,assignments_department} = this.state;
    let {employeeCode,employeeName, employeeDepartment, employeeDesignation} = this.state;


    const renderTr = () => {
        return result.map((item, ind) => {
            return (
                <tr key={ind}>
                    <td>{employeeCode}</td>
                    <td>{employeeName}</td>
                    <td data-label="designation">{getNameById(assignments_designation,item.designation)}</td>
                    <td data-label="department">{getNameById(assignments_department,item.department)}</td>
                    <td>{item.fromDate}</td>
                    <td>{item.toDate}</td>
                </tr>
            )
        })
    }


    return (
        <div>

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
                                <th>From Date</th>
                                <th>To Date</th>
                            </tr>
                            </thead>
                            <tbody>
                                {renderTr()}
                            </tbody>
                        </table>
                    </div>
                </div>
                <br/>
                <div className="text-center">
                    <button type="button" className="btn btn-close" onClick={(e)=>{this.closeWindow()}}>Close</button>
                </div>

        </div>
    );
  }
}






ReactDOM.render(
  <EmployeeReport />,
  document.getElementById('root')
);
