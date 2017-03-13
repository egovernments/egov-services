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


class ShowCommunity extends React.Component {
  constructor(props) {
    super(props);
    this.state={employees:[],communitySet:{
        name:"",
      description:"",
        active:""}
      }
    this.handleChange=this.handleChange.bind(this);

  }

  componentWillMount()
  {console.log(getUrlVars()["type"]);}



  componentDidMount()
  {
    let {
      name,sdate,edate}=this.state.communitySet;
      // e.preventDefault();
      //call api call
      var employees=[];
      for(var i=1;i<=10;i++)
      {
          employees.push({
              name:"obc",description:"good"
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
          communitySet:{
              ...this.state.communitySet,
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
    console.log(this.state.communitySet);
    let {handleChange,search,updateTable}=this;
    let {isSearchClicked,employees}=this.state;
    let {name,description}=this.state.communitySet;
    const renderOption=function(list)
    {
        if(list)
        {
            return list.map((item)=>
            {
                return (<option key={item.id} value={item.id}>
                        {item.name}
                  </option>)
            })
        }
    }
    // const showTable=function()
    // {
    //   if(isSearchClicked)
    //   {
    //       return (
    //         <table id="employeeTable" className="table table-bordered">
    //             <thead>
    //                 <tr>
    //                     <th>Name</th>
    //                     <th>Age</th>
    //                     <th>Start date</th>
    //                     <th>End date</th>
    //                     <th>Action</th>
    //
    //                 </tr>
    //             </thead>
    //
    //             <tbody id="employeeSearchResultTableBody">
    //                 {
    //                     renderBody()
    //                 }
    //             </tbody>
    //
    //         </table>
    //
    //       )
    //
    //       // updateTable();
    //       // $('#employeeTable').DataTable({
    //       //   dom: 'Bfrtip',
    //       //   buttons: [
    //       //            'copy', 'csv', 'excel', 'pdf', 'print'
    //       //    ],
    //       //    ordering: false
    //       // });
    //         // alert("hai");
    //   }
    //
    // }
    const renderAction=function(type,name){
      if (type==="update") {

              return (
                      <a href={`../../../../app/hr/master/community.html?name=${name}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`../../../../app/hr/master/community.html?name=${name}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
        }
}


    const renderBody=function()
    {
      return employees.map((item,index)=>
      {
            return (<tr key={index}>
                    <td data-label="name">{item.name}</td>
                    <td data-label="description">{item.description}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.name)}
                    </td>
                </tr>
            );

      })
    }

      return (<div>
        <table id="employeeTable" className="table table-bordered">
            <thead>
                <tr>
                    <th>Community Name</th>
                    <th>Description</th>
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
  <ShowCommunity />,
  document.getElementById('root')
);
