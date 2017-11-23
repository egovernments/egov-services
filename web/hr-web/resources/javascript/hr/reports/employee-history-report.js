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
    this.setInitialState = this.setInitialState.bind(this);

  }

  setInitialState(initState) {
    this.setState(initState);
  }

  componentWillMount() {

    var _state = {}, _this = this, count = 2;
    const checkCountAndCall = function(key, res) {
      _state[key] = res;
      count--;
      if(count == 0)
        _this.setInitialState(_state);
    }


    getDropdown("assignments_department", function(res) {
      checkCountAndCall("departments", res);
    });
    getDropdown("assignments_designation", function(res) {
      checkCountAndCall("designations", res);
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
             ordering: false,
             language: {
               "emptyTable": "No Records"
             }
          });
      }
  }


  closeWindow () {
      open(location, '_self').close();
  }


  render() {
    let { closeWindow} = this;
    let {result, employeeTypes, departments, designations} = this.state;
    let {employeeCode,employeeName, employeeDepartment, employeeDesignation} = this.state;


    const renderTr = () => {
        return result.map((item, ind) => {
            return (
                <tr key={ind}>
                    <td>{employeeCode}</td>
                    <td>{employeeName}</td>
                    <td data-label="designation">{getNameById(designations,item.designation)}</td>
                    <td data-label="department">{getNameById(departments,item.department)}</td>
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
