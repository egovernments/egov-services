class ShowLeaveType extends React.Component {
  constructor(props) {
    super(props);
    this.state={employees:[],leaveType:{
      "id": "",
      "name": "",
      "description": "",
      "halfdayAllowed": "",
      "payEligible": "",
      "accumulative": "",
      "encashable": "",
      "active": "",
      "createdBy": "",
      "createdDate": "",
      "lastModifiedBy": "",
      "lastModifiedDate": "",
      "tenantId": tenantId
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
  console.log(getCommonMaster("hr-leave","leavetypes","LeaveType").responseJSON["LeaveType"]);
  this.setState({
  employees:getCommonMaster("hr-leave","leavetypes","LeaveType").responseJSON["LeaveType"]
    });
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
    let  {name}=this.state.leaveType;
    let {isSearchClicked,employees}=this.state;

    const renderBody=function()
    {

      return employees.map((item,index)=>
      {
            return (<tr key={index}>
                    <td>{index+1}</td>
                    <td data-label="name">{item.name}</td>
                    <td data-label="description">{item.description}</td>
                    <td data-label="action">
                              {renderAction(getUrlVars()["type"],item.id)}
                    </td>
                </tr>
            );

      })
    }
  const  renderAction=function(type,id)
    {
      if(type==="update")
      {

        return(
          <a href={`app/hr/leavemaster/leave-type.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>)
      }

      else {
            return(
                    <a href={`app/hr/leavemaster/leave-type.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
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
                          <th>Leave Name </th>
                          <th>Description</th>
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
