class ShowLeaveMapping extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      leaveTypeList:[],leave:
      {designation:"",
      leaveType:"",
      noOfDay:""},

    }
    this.handleChange=this.handleChange.bind(this);

  }

  componentWillMount()
  {
    console.log(getUrlVars()["type"]);
    // console.log(getCommonMaster("hr-masters","grades","Grade").responseJSON["Grade"]);


  }



  componentDidMount()
  {
    this.setState({
      leaveTypeList:getCommonMaster("hr-leave","leaveallotments","LeaveAllotment").responseJSON["LeaveAllotment"]

    });
  }

  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.grades.length!=this.state.grades.length) {
          // $('#employeeTable').DataTable().draw();
          // alert(prevState.grades.length);
          // alert(this.state.grades.length);
          // alert('updated');
          $('#gradeTable').DataTable({
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
    console.log(this.state.leave);
    let {handleChange}=this;
    let {leaveTypeList}=this.state;
    let {designation,leaveType,noOfDay}=this.state.leave;


    const renderAction=function(type,leaveType){
      if (type==="update") {

              return (
                      <a href={`app/hr/leavemaster/leave-mapping.html?leaveType=${leaveType}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`app/hr/leavemaster/leave-mapping.html?leaveType=${leaveType}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
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
                    {renderAction(getUrlVars()["type"],item.leaveType)}
                    </td>
                </tr>
            );

      })
    }

      return (<div>
        <table id="employeeTable" className="table table-bordered">
            <thead>
                <tr>
                    <th>Leave Type</th>
                    <th>No of day</th>
                    <th>Action</th>

                </tr>
            </thead>

            <tbody id="employeeSearchResultTableBody">
                {
                    renderBody()
                }
            </tbody>

        </table>
      </div>
    );
  }
}


ReactDOM.render(
  <ShowLeaveMapping />,
  document.getElementById('root')
);
