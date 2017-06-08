class ShowLeaveType extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      leaveList:[]
    }
  }



componentDidMount() {
  if(window.opener && window.opener.document) {
     var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
     if(logo_ele && logo_ele[0]) {
       document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
     }
  }
  $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Leave Type");
  var _this = this;
  commonApiPost("hr-leave", "leavetypes","_search",{tenantId,pageSize:500}, function(err, res) {
    _this.setState({
      leaveList: res.LeaveType
    })
  })
}

close(){
    // widow.close();
    open(location, '_self').close();
}


componentDidUpdate(prevProps, prevState) {
    if (prevState.leaveList.length!=this.state.leaveList.length) {
        $('#leaveTable').DataTable({
          dom: 'Bfrtip',
          buttons: [
                   'copy', 'csv', 'excel', 'pdf', 'print'
           ],
           ordering: false
        });
    }
}


render() {
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
