class PersonalInform extends React.Component {
  constructor(props) {
    super(props);
    this.state={employees:[],searchSet:{
    name:"",
    employee:"",
    department:"",
    designation:"",
    leaveType:"",
    noOfDay:"",
    noOfLeave:"",
    calendarYear:""},isSearchClicked:false,departmentsList:[],designationList:[],leave:[]}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
    this.addOrUpdate=this.addOrUpdate.bind(this);
  }


  componentDidMount(){
    if(getUrlVars()["type"]==="view")
    {
      for (var variable in this.state.searchSet)
        document.getElementById(variable).disabled = true;
      }
  }

  search(e)
  {
    let {
    name,
    employee,
    department,
    designation,
    leaveType,
    noOfDay,calendarYear}=this.state.searchSet;
    e.preventDefault();
    //call api call
    var employees=[];
    //  employees=commonApiPost("hr-employee","employees","_search",{tenantId}).responseJSON || [];
    for(var i=1;i<=10;i++)
    {
        employees.push({
            name:"murali",employee:'xyz',noOfLeave:""
        })
    }
    this.setState({
      isSearchClicked:true,
      employees
    })


  }
  addOrUpdate(e,mode){
    e.preventDefault();
      console.log(this.state.searchSet);
      if (mode==="update") {
          console.log("update");

      } else {
        this.setState({searchSet:{
        name:"",
        employee:"",
        department:"",
        designation:"",
        leaveType:"",
        noOfDay:"",
        calendarYear:"",
        noOfLeave:""} })
      }

  }


  componentWillMount()
  {

    this.setState({
      departmentsList:assignments_department,
      designationList:assignments_designation,
      leave:[{
              id: 1,
              name: "Casual",
              description: "",
              orderno: "1",
              active: true
          },
          {
              id: 2,
              name: "Gazette",
              description: "",
              orderno: "1",
              active: true
          }]
  })
  }

  handleChange(e,noOfLeave,name)
  {

      this.setState({
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value,
              [noOfLeave]:e.target.value

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
          $('#employeeTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false
          });
      }
  }

  render() {
    console.log(this.state.searchSet);
    let {handleChange,search,updateTable,addOrUpdate}=this;
    let {isSearchClicked,employees}=this.state;
    let {name,
    employee,
    department,
    designation,
    employeeTypeCode,
    functionaryCode,
    leaveType,noOfDay,calendarYear}=this.state.searchSet;
    let mode=getUrlVars()["type"];
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
    const showTable=function()
    {
      if(isSearchClicked)
      {
          return (
            <table id="employeeTable" className="table table-bordered">
                <thead>
                    <tr>

                        <th>Employee Name</th>
                        <th>Employee Code</th>
                        <th>No. Of Leave Available</th>

                    </tr>
                </thead>

                <tbody id="employeeSearchResultTableBody">
                    {
                        renderBody()
                    }
                </tbody>

            </table>

          )


      }

    }
    const renderBody=function()
    {
      return employees.map((item,index)=>
      {
            return (<tr key={index}>
                    <td data-label="name">{item.name}</td>
                    <td data-label="employee">{item.employee}</td>
                    <td data-label="noOfDay">
                    <input type="number" id="noOfDay" name="noOfDay"  value={noOfDay}
                      onChange={(e)=>{handleChange(e,noOfLeave,"noOfDay")}}/>
                    </td>
                </tr>
            );

      })
    }
    const showActionButton=function() {
      if((!mode) ||mode==="update")
      {
        return (<button type="submit" className="btn btn-submit">{mode?"Update":"Add"}</button>);
      }
    };


    return (
      <div>
          <form onSubmit={(e)=>{search(e)}}>
          <fieldset>
              <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="">Employee Code</label>
                        </div>
                        <div className="col-sm-6">
                            <input type="text" id="employee" name="employee" value={employee}
                              onChange={(e)=>{handleChange(e,"employee")}}/>
                        </div>
                    </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Employee Name</label>
                          </div>
                          <div className="col-sm-6">
                              <input type="text" id="name" name="name" value={name}
                                onChange={(e)=>{  handleChange(e,"name")}} />
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
                              <select id="department" name="department" value={department}
                              onChange={(e)=>{ handleChange(e,"department")}}>
                                <option>Select Department</option>
                                {renderOption(this.state.departmentsList)}
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
                                    handleChange(e,"designation")}}>
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
                            <label for="">Leave Type  </label>
                          </div>
                          <div className="col-sm-6">
                          <div className="styled-select">
                              <select id="leaveType" name="leaveType" value={leaveType} onChange={(e)=>{
                                  handleChange(e,"leaveType")
                              }}>
                                <option>Select LeaveType</option>
                                {renderOption(this.state.leave)}
                             </select>
                          </div>
                          </div>
                      </div>
                    </div>

                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                                <label for=""> Calendar</label>
                            </div>
                            <div className="col-sm-6">
                            <div className="text-no-ui">
                                <span>
                                    <i className="glyphicon glyphicon-calendar"></i>
                                </span>
                                <input type="date" name="calendarYear" value={calendarYear} id="calendarYear"
                                onChange={(e)=>{handleChange(e,"calendarYear")}}/>

                            </div>
                            </div>
                        </div>
                    </div>
                </div>

            <div className="text-center">
                <button type="submit"  className="btn btn-submit">Search</button>
            </div>
          </fieldset>
          </form>
          <br/>
          {showTable()}
          <div className="text-center">
          {showActionButton()} &nbsp;&nbsp;
          <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
          </div>
      </div>
    );
  }
}






ReactDOM.render(
  <PersonalInform />,
  document.getElementById('root')
);
