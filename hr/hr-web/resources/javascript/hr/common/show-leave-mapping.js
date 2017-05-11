class ShowLeaveMapping extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      leaveTypeList:[],leave:{
        "id": "",
        "designation": "",
        "leaveType": {
          "name": "",
        },
        "noOfDays": "",
        "tenantId": tenantId
      }
    }
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
      $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Leave Mapping");

    try {
        var _leaveAllotment = commonApiPost("hr-leave","leaveallotments","_search",{tenantId,pageSize:500}).responseJSON["LeaveAllotment"] || [];
    } catch(e) {
        var _leaveAllotment = [];
    }
    this.setState({
      leaveTypeList: _leaveAllotment

    });
  }

  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.leaveTypeList.length!=this.state.leaveTypeList.length) {
          // $('#employeeTable').DataTable().draw();
          // alert(prevState.grades.length);
          // alert(this.state.grades.length);
          // alert('updated');
          $('#LeaveTable').DataTable({
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
          leave:{
              ...this.state.leave,
              [name]:e.target.value
          }
      })

  }


  close(){
      // widow.close();
      open(location, '_self').close();
  }


  render() {
    let {handleChange}=this;
    let {leaveTypeList}=this.state;
    let {designation,leaveType,noOfDay}=this.state.leave;


    const renderAction=function(type,id){
      if (type==="update") {

              return (
                      <a href={`app/hr/leavemaster/leave-mapping.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`app/hr/leavemaster/leave-mapping.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
        }
}


    const renderBody=function()
    {
      return leaveTypeList.map((item,index)=>
      {
            return (<tr key={index}>
                    <td data-label="leaveType">{item.leaveType.name}</td>
                    <td data-label="noOfDay">{item.noOfDays}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.id)}
                    </td>
                </tr>
            );

      })
    }

      return (<div>
        <h3>{titleCase(getUrlVars()["type"])} Leave Mapping</h3>
        <table id="LeaveTable" className="table table-bordered">
            <thead>
                <tr>
                    <th>Leave Type</th>
                    <th>No of day</th>
                    <th>Action</th>

                </tr>
            </thead>

            <tbody id="leavesearchResultTableBody">
                {
                    renderBody()
                }
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
  <ShowLeaveMapping />,
  document.getElementById('root')
);
