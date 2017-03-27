class EmployeeSearch extends React.Component {
  constructor(props) {
    super(props);
    this.state={employees:[],searchSet:{
    name:"",
    code:"",
    department:"",
    designation:"",
    employeeType:"",
    month:"",calendarYear:"",},isSearchClicked:false,employeeTypeList:[],departmentList:[],designationList:[],year:[],monthList:[]}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
  }

  search(e)
  {
    let {
    name,
    code,
    department,
    designation,
    employeeType,
    calendarYear,month}=this.state.searchSet;
    e.preventDefault();
    //call api call
    var employees=[];
    //console.log(commonApiPost("hr-employee","employees","_search",{tenantId},this.state.searchSet).responseJSON["Employee"]);
   employees=commonApiPost("hr-employee","employees","_search",{tenantId,code},this.state.searchSet).responseJSON["Employee"];
    this.setState({
      isSearchClicked:true,
      employees
    })

    // $('#employeeTable').DataTable().draw();
    // console.log($('#employeeTable').length);

  }


  componentWillMount()
  {
    var date=new Date();
    var monthList=[{id:0,name:"January"},{id:1,name:"February"}, {id:2,name:"March"}, {id:3,name:"April"}, {id:4,name:"May"},
         {id:5,name:"June"}, {id:6,name:"July"}, {id:7,name:"August"}, {id:8,name:"September"}, {id:9,name:"October"},
         {id:10,name:"November"}, {id:11,name:"December"}]
    this.setState({
      departmentList:getCommonMaster("egov-common-masters","departments","Department").responseJSON["Department"] || [],
      designationList:getCommonMaster("hr-masters","designations","Designation").responseJSON["Designation"] || [],
      year:getCommonMaster("egov-common-masters","calendaryears","CalendarYear").responseJSON["CalendarYear"] || [],
      monthList,
      employeeTypeList:getCommonMaster("hr-masters","employeetypes","EmployeeType").responseJSON["EmployeeType"] || []
  })
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
          searchSet:{
              ...this.state.searchSet,
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
    console.log(this.state.searchSet);
    let {handleChange,search,updateTable}=this;
    let {isSearchClicked,employees}=this.state;
    let {name,
    code,
    department,
    designation,
    employeeType,
    calendarYear,month}=this.state.searchSet;
    const renderOption=function(list,listName="")
    {
        if(list)
        {
            if (listName==="year") {
              return list.map((item)=>
              {
                  return (<option key={item.id} value={item.name}>
                          {item.name}
                    </option>)
              })
            }
            else {
              return list.map((item)=>
              {
                  return (<option key={item.id} value={item.id}>
                          {item.name}
                    </option>)
              })
            }
        }
    }
    const showTable=function()
    {
      if(isSearchClicked)
      {
          return (
            <table id="employeeTable" className="table table-bordered">
                <thead>
                    <tr>
                        <th>Employee Code</th>
                        <th>Employee Name</th>
                        <th>Employee Designation</th>
                        <th>Employee Department</th>
                        <th>Employee Username</th>
                        <th>Action</th>

                    </tr>
                </thead>

                <tbody id="employeeSearchResultTableBody">
                    {
                        renderBody()
                    }
                </tbody>

            </table>

          )

          // updateTable();
          // $('#employeeTable').DataTable({
          //   dom: 'Bfrtip',
          //   buttons: [
          //            'copy', 'csv', 'excel', 'pdf', 'print'
          //    ],
          //    ordering: false
          // });
            // alert("hai");
      }

    }


    const renderAction=function(type,id){
      if (type==="update") {

              return (
                      <a href={`./../../../app/hr/employee/create.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );

    }else {
            return (
                    <a href={`./../../../app/hr/employee/create.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
        }
}



    const renderBody=function()
    {
      return employees.map((item,index)=>
      {
            return (<tr key={index}>
                    <td data-label="code">{item.code}</td>
                    <td data-label="name">{item.name}</td>
                    <td data-label="designation">{item.assignments[0].designation}</td>
                    <td data-label="department">{item.assignments[0].department}</td>
                    <td data-label="userName">{item.userName}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.id)}
                    </td>
                </tr>
            );

      })
    }
    const disbaled=function(type) {
        if (type==="view") {
              return "ture";
        } else {
            return "false";
        }
    }
    return (
      <div>
          <form onSubmit={(e)=>{search(e)}}>
          <fieldset>
      <div className="row">
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for=""> Month <span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                    <select id="month" name="month"  value= {month}
                        onChange={(e)=>{handleChange(e,"month")  }}required>
                    <option value="">Select Month</option>
                    {renderOption(this.state.monthList)}
                   </select>
                    </div>
                  </div>
              </div>
            </div>
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for=""> Calendar Year <span>*</span></label>
                    </div>
                    <div className="col-sm-6">
                      <div className="styled-select">
                      <select id="calendarYear" name="calendarYear"  value= {calendarYear}
                          onChange={(e)=>{handleChange(e,"calendarYear")}}required>
                      <option value="">Select Year</option>
                      {renderOption(this.state.year)}
                     </select>
                      </div>
                    </div>
                </div>
              </div>
          </div>
          <div className="row">
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="">Department  </label>
                    </div>
                    <div className="col-sm-6">
                    <div className="styled-select">
                        <select id="department" name="department" value={department} onChange={(e)=>{
                            handleChange(e,"department")
                        }}>
                          <option>Select Department</option>
                          {renderOption(this.state.departmentList)}
                       </select>
                    </div>
                    </div>
                </div>
              </div>
              <div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                        <label for="">Designation  </label>
                      </div>
                      <div className="col-sm-6">
                      <div className="styled-select">
                          <select id="designation" name="designation" value={designation} onChange={(e)=>{
                              handleChange(e,"designation")
                          }}>
                          <option>Select Designation</option>
                          {renderOption(this.state.designationList)}
                         </select>
                      </div>
                      </div>
                  </div>
                </div>
          </div>
          <div className="row">
            <div className="col-sm-6">
                <div className="row">
                    <div className="col-sm-6 label-text">
                      <label for="">Employee Code/Name  </label>
                    </div>
                    <div className="col-sm-6">
                        <input type="text" name="code" id="code" onChange={(e)=>{
                            handleChange(e,"code")
                        }} />
                    </div>
                </div>
              </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Employee Type  </label>
                          </div>
                          <div className="col-sm-6">
                          <div className="styled-select">
                              <select id="employeeType" name="employeeType" value={employeeType} onChange={(e)=>{
                                  handleChange(e,"employeeType")
                              }}>
                                  <option>Select Type</option>
                                  {renderOption(this.state.employeeTypeList)}
                             </select>
                          </div>
                          </div>
                      </div>
                    </div>
              </div>







            <div className="text-center">
              <button type="submit"  className="btn btn-submit">Search</button>&nbsp;&nbsp;
                <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>

            </div>
          </fieldset>
          </form>
          <br/>

          {showTable()}

      </div>
    );
  }
}






ReactDOM.render(
  <EmployeeSearch />,
  document.getElementById('root')
);
