class ShowPis extends React.Component {
  constructor(props) {
    super(props);
    this.state={employees:[],searchSet:{
    code:"",
    employeeStatusCode:"",
    employee:"",
    departmentCode:"",
    designationCode:"",
    employeeTypeCode:"",
    functionaryCode:"",
    leaveCode:"",
    noOfDay:"",
    calendarYear:""},isSearchClicked:false,mployeeType:[],department:[],designation:[],employeestatus:[],functionary:[]}
    this.handleChange=this.handleChange.bind(this);

  }


  componentWillMount()
  {
    //call employee api and get employees
    console.log(getUrlVars()["type"]);
  //  var queryParam=getUrlVars();
  }

  componentDidMount()
  {
      var employees=[];
    for(var i=1;i<=10;i++)
    {
        employees.push({
            code:i,employee:"murali"+1,employeeTypeCode:"Az30",noOfDay:"2"
        })
        this.setState({employees});
    }
  }



  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.employees.length!=this.state.employees.length) {
          // $('#employeeTable').DataTable().draw();
          // alert(prevState.employees.length);
          // alert(this.state.employees.length);
          // alert('updated');
          console.log(this.state.employees.length);
          $('#employeeTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false
          });
      }
  }


    handleChange(e,name)
    {

        this.setState({
            searchSet:{
                ...this.state.searchSet,
                [name]:e.target.value
            }
        })

    }



  render() {
    console.log(this.state.searchSet);
    let  {code,employee,employeeTypeCode,noOfDay}=this.state.searchSet;
    let {employees}=this.state;
    const renderBody=function()
    {
      return employees.map((item,index)=>
      {
            return (<tr key={index}>
                    <td data-label="employee">{item.employee}</td>
                    <td data-label="employeeTypeCode">{item.employeeTypeCode}</td>
                    <td data-label="noOfDay">{item.noOfDay}</td>
                    <td data-label="action">
                      {renderAction(getUrlVars()["type"])}
                    </td>
                </tr>
            );

      })
    }

    const  renderAction=function(type,leaveName)
      {
        console.log(type);
        if(type==="update")
        {
          return(
            <a href={`app/hr/leavemaster/pis.html?employeeTypeCode=${employeeTypeCode}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>)
        }

        else {
              return(
                      <a href={`app/hr/leavemaster/pis.html?employeeTypeCode=${employeeTypeCode}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
                    )
                  }
           }

    return(
      <div>
          <br/>
          <div className="land-table">
                <table id="employeeTable" className="table table-bordered">
                  <thead>
                      <tr>
                      <th>Employee Code</th>
                      <th>Employee Name</th>
                      <th>No. Of Day</th>
                      <th>Action</th>
                      </tr>
                  </thead>

                  <tbody>
                    {renderBody()}
                  </tbody>
              </table>
          </div>

      </div>
    );
  }
}

ReactDOM.render(
  <ShowPis />,
  document.getElementById('root')
);
