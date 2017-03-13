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


class ShowLeaveMapping extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      employees:[],leave:
      {designation:"",
      leaveType:"",
      noOfDay:""},

    }
    this.handleChange=this.handleChange.bind(this);

  }

  componentWillMount()
  {console.log(getUrlVars()["type"]);}



  componentDidMount()
  {
    let {
      designation,leaveType,noOfDay}=this.state.leave;
      // e.preventDefault();
      //call api call
      var employees=[];
      for(var i=1;i<=10;i++)
      {
          employees.push({
              leaveType:"government",noOfDay:"2"
          })
      }
      this.setState({
        employees
      })

      // $('#employeeTable').DataTable().draw();
      // console.log($('#employeeTable').length);


    // $('#employeeTable').DataTable({
    //   dom: 'Bfrtip',
    //   buttons: [
    //            'copy', 'csv', 'excel', 'pdf', 'print'
    //    ],
    //    ordering: false
    // });

    // console.log($('#employeeTable').length);

  }

  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.employees.length!=this.state.employees.length) {
          // $('#employeeTable').DataTable().draw();
          // alert(prevState.employees.length);
          // alert(this.state.employees.length);
          // alert('updated');
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

  // updateTable()
  // {
  //   $('#employeeTable').DataTable({
  //     dom: 'Bfrtip',
  //     buttons: [
  //              'copy', 'csv', 'excel', 'pdf', 'print'
  //      ],
  //      ordering: false
  //   });
  //
  // }

  render() {
    console.log(this.state.leave);
    let {handleChange}=this;
    let {employees}=this.state;
    let {designation,leaveType,noOfDay}=this.state.leave;


    const renderAction=function(type,leaveType){
      if (type==="update") {

              return (
                      <a href={`../../../../app/hr/leavemaster/leave-mapping.html?leaveType=${leaveType}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`../../../../app/hr/leavemaster/leave-mapping.html?leaveType=${leaveType}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
        }
}


    const renderBody=function()
    {
      return employees.map((item,index)=>
      {
            return (<tr key={index}>
                    <td data-label="leaveType">{item.leaveType}</td>
                    <td data-label="noOfDay">{item.noOfDay}</td>
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
