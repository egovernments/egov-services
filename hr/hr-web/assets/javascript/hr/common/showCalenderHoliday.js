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


class ShowCalenderHoliday extends React.Component {
  constructor(props) {
    super(props);
    this.state={employees:[],holiday:
        {year:"",
        name:"",
        sdate:""
    }
  }
    this.handleChange=this.handleChange.bind(this);

  }


  componentWillMount(){
    console.log(getUrlVars()["type"]);
    var year=[{id:"2017",name:"2017"},{id:"2016",name:"2016"},{id:"2015",name:"2015"},{id:"2014",name:"2014"}];
    this.setState({year})
  }

  componentDidMount()
  {
    let {
      name,sdate,edate}=this.state.holiday;
      // e.preventDefault();
      //call api call
      var employees=[];
      for(var i=1;i<=10;i++)
      {
          employees.push({
          name:"Holi",  sdate:"20/07/2017"
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
          holiday:{
              ...this.state.holiday,
              [name]:e.target.value
          }
      })

  }


  close(){
      // widow.close();
      open(location, '_self').close();
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
    console.log(this.state.holiday);
    let {handleChange,search,updateTable}=this;
    let {isSearchClicked,employees}=this.state;
    let {name,year,sdate}=this.state.holiday;
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
                      <a href={`../../../../app/hr/master/calendar-holidays-setup.html?name=${name}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`../../../../app/hr/master/calendar-holidays-setup.html?name=${name}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
        }
}



    const renderBody=function()
    {
      return employees.map((item,index)=>
      {
            return (<tr key={index}>

                    <td data-label="sdate">{item.sdate}</td>
                      <td data-label="name">{item.name}</td>

                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.name)}
                    </td>
                </tr>
            );
      })
    }

      return (<div>
        <div className="row">
          <div className="col-sm-6 col-sm-offset-3">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="">Calendar Year <span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                    <select id="year" name="year" onChange={(e)=>{
                        handleChange(e,"year")
                    }}>
                    <option>Select Year</option>
                    {renderOption(this.state.year)}
                   </select>
                    </div>
                  </div>
              </div>
            </div>
        </div>

        <br/>
        <br/>
        <br/>
        <table id="employeeTable" className="table table-bordered">
            <thead>
                <tr>
                    <th>Holiday Date</th>
                    <th>Holiday Name</th>
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
  <ShowCalenderHoliday />,
  document.getElementById('root')
);
