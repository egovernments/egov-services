function getUrlVars() {
    var vars = [],
        hash;
    var hashes = window.location.href.slice(window.location.href.indexOf('?') + 1).split('&');
    for (var i = 0; i < hashes.length; i++) {
        hash = hashes[i].split('=');
        vars.push(hash[0]);
        vars[hash[0]] = hash[1];
    }
    return vars;
}

class ShowLeaveType extends React.Component {
  constructor(props) {
    super(props);
    this.state={employees:[],leaveType:{leaveName:"",
    pay_eligible:"",
    encashable:"",
    halfday_allowed:"",
    accumulative:""
  }}
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
  for(var i=1;i<=5;i++)
  {
      employees.push({
          slnumber:i,leaveName:"casual"
      })

  }
  // console.log(leavetypes);
  this.setState({employees});
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
        leaveType:{
            ...this.state.leaveType,
            [name]:e.target.value
        }
    })

}

  render() {
    console.log(this.state.leaveType);
    let  {leaveName}=this.state.leaveType;
    let {isSearchClicked,employees}=this.state;

    const renderBody=function()
    {

      return employees.map((item,index)=>
      {
            return (<tr key={index}>
                    <td data-label="slnumber">{item.slnumber}</td>
                    <td data-label="leaveName">{item.leaveName}</td>
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
          <a href={`../../../../app/hr/leavemaster/leave-type.html?leaveName=${leaveName}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>)
      }

      else {
            return(
                    <a href={`../../../../app/hr/leavemaster/leave-type.html?leaveName=${leaveName}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
                  )
                }
         }





     return (
      <div>
          <br/>
          <div className="land-table">
                <table id="employeeTable" className="table table-bordered">
                  <thead>
                      <tr>
                          <th>SL No. </th>
                          <th>Leave Type </th>
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
  <ShowLeaveType />,
  document.getElementById('root')
);
