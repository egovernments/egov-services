var flag = 0;

class WithoutAssignment extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
        "result": [],
        "searchSet": {
            "asOnDate": ""
        },
        "isSearchClicked": false
    };
    this.handleChange = this.handleChange.bind(this);
    this.searchEmployee = this.searchEmployee.bind(this);
    this.closeWindow = this.closeWindow.bind(this);
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

  searchEmployee (e) {
    e.preventDefault();
    var _this = this
    $('#employeeTable').dataTable().fnDestroy();
    try {
        flag = 1;
        commonApiPost("hr-employee", "employees", "_employeewithoutassignmentreport", {...this.state.searchSet, tenantId},function(err,res){
          if(res && res.Employee){
          _this.setState({
            ..._this.state,
            isSearchClicked: true,
            result : res.Employee
          });
         }else {
           _this.setState({
             ..._this.state,
               isSearchClicked: true
           })
         }
        });
    } catch (e) {
        console.log(e);
    }
  }

  closeWindow() {
      open(location, '_self').close();
  }

  render() {
    let {handleChange, searchEmployee, closeWindow} = this;
    let {result} = this.state;
    let {asOnDate} = this.state.searchSet;

    const renderTr = () => {
      console.log(result);
        return result.map((item, ind) => {
            return (
                <tr key={ind}>
                    <td>{item.code}</td>
                    <td>{item.name}</td>
                </tr>
            )
        })
    }

    const showTable = () => {
        if(this.state.isSearchClicked) {
            return (
                <div className="land-table">
                    <table id="employeeTable" className="table table-bordered">
                        <thead>
                            <tr>
                                <th>Employee Code</th>
                                <th>Employee Name</th>
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
    return(
        <div>
            <form onSubmit={(e)=>
                    {searchEmployee(e)}}>
                <fieldset>
                    <div className="row">
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                    <label htmlFor="">Date<span>*</span> </label>
                                </div>
                                <div className="col-sm-6">
                                    <div className="text-no-ui">
                                        <span><i className="glyphicon glyphicon-calendar"></i></span>
                                        <input type="text" id="asOnDate" value={asOnDate} onChange={(e) => {handleChange(e, "asOnDate")}} required/>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
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
  <WithoutAssignment />,
  document.getElementById('root')
);
