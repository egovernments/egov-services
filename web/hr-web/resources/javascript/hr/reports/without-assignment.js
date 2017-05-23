

class WithoutAssignment extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
        "result": [],
        "searchSet": {
            "date": ""
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

  searchEmployee (e) {
    e.preventDefault();
    var result;
    try {
        result = commonApiPost("hr-employee", "employees", "_search", {...this.state.searchSet, tenantId}).responseJSON["Employee"] || [];
    } catch (e) {
        result = [];
        console.log(e);
    }
    this.setState({
        ...this.state,
        isSearchClicked: true,
        result: result
    })
  }

  closeWindow() {
      open(location, '_self').close();
  }

  render() {
    let {handleChange, searchEmployee, closeWindow} = this;
    let {result} = this.state;
    let {date} = this.state.searchSet;

    const renderTr = () => {
        return result.map((item, ind) => {
            return (
                <tr key={ind}>
                    <td>89 </td>
                    <td>Kumaresh</td>
                    <td><a href="#" assign>Assign </a></td>
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
                                <th>Action </th>
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
                                    <label for="">Date* </label>
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
            {showTable()}
        </div>
    );
  }
}






ReactDOM.render(
  <WithoutAssignment />,
  document.getElementById('root')
);
