class ShowLeaveType extends React.Component {
  constructor(props) {
    super(props);
    this.state={leaveList:[],leaveType:{
    name:"",
    payEligible:"",
    encashable:"",
    halfdayAllowed:"",
    accumulative:"",
    description:"",
    active:""
  }}
  this.handleChange=this.handleChange.bind(this);
}



componentDidMount()
{
  if(window.opener && window.opener.document) {
     var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
     if(logo_ele && logo_ele[0]) {
       document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
     }
   }
    $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Leave Type");
  try {
      var _leaveTypes = commonApiPost("hr-leave", "leavetypes","_search",{tenantId,pageSize:500}).responseJSON["LeaveType"] || [];
  } catch(e) {
      var _leaveTypes = [];
  }
  this.setState({
  leaveList:_leaveTypes
    });
}

close(){
    // widow.close();
    open(location, '_self').close();
}


componentDidUpdate(prevProps, prevState)
{
    if (prevState.leaveList.length!=this.state.leaveList.length) {
        // $('#leaveTable').DataTable().draw();
        // alert(prevState.leaveList.length);
        // alert(this.state.leaveList.length);
        // alert('updated');
        $('#leaveTable').DataTable({
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
    let  {name,description}=this.state.leaveType;
    let {leaveList}=this.state;

    const renderBody=function()
    {

      return leaveList.map((item,index)=>
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
          <h3>{titleCase(getUrlVars()["type"])} Leave Type</h3>
                <table id="leaveTable" className="table table-bordered">
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
              <div className="text-center">
                  <button type="button" className="btn btn-close"onClick={(e)=>{this.close()}}>Close</button>
              </div>
      </div>

    );
  }
}

ReactDOM.render(
  <ShowLeaveType />,
  document.getElementById('root')
);
