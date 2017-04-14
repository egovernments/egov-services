class SearchLeaveApplication extends React.Component {
  constructor(props) {
    super(props);
    this.state={employees:[],employ:{},searchSet:{

    name:"",
    code:"",
    departmentId:"",
    designationId:"",
    employeeTypeCode:"",
    functionaryCode:"",
    employeeStatusCode:""},isSearchClicked:false,employeeType:[],assignments_department:[],assignments_designation:[],employeeStatus:[],assignments_functionary:[]}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);

  }




  search(e)
  {
      e.preventDefault();
    let {
    name,
    code,
    departmentId,
    designationId,
    employeeTypeCode,
    functionaryCode,
    employeeStatusCode}=this.state.searchSet;
    e.preventDefault();
    //call api call
    var employees=[];
    employees=commonApiPost("hr-employee","employees","_search",{tenantId,code,departmentId,designationId,pageSize:500},this.state.searchSet).responseJSON["Employee"] || [];
    this.setState({
      isSearchClicked:true,
      employees,searchSet:{

      name:"",
      code:"",
      departmentId:"",
      designation:"",
      employeeTypeCode:"",
      functionaryCode:"",
      employeeStatusCode:""}
    })
  }


  componentWillMount()
  {

    this.setState({
      assignments_department,
      assignments_designation,
      employeeType,
      employeeStatus,
      assignments_functionary
  })
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
    let {handleChange,search,updateTable}=this;
    let {isSearchClicked,employees}=this.state;
    let {name,
    code,
    departmentId,
    designationId,
    employeeTypeCode,
    functionaryCode,
    employeeStatusCode}=this.state.searchSet;
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
                        <th>Employee Code</th>
                        <th>Employee Name</th>
                        <th>Employee Designation</th>
                        <th>Employee Department</th>
                        <th>master</th>

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
      if(employees.length>0)
      {
      return employees.map((item,index,id)=>
      {
            return (<tr key={index}>

                    <td data-label="code">{item.code}</td>
                    <td data-label="name">{item.name}</td>
                    <td data-label="designation">{getNameById(assignments_designation,item.assignments[0].designation)}</td>
                    <td data-label="department">{getNameById(assignments_department,item.assignments[0].department)}</td>
                    <td data-label="action">
                      <a href={`app/hr/leavemaster/apply-leave.html?id=${item.id}`}>Create</a>
                    </td>

                </tr>
            );
      })
    }
    else {
      return (
          <tr>
              <td colSpan="6">No records</td>
          </tr>
      )
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
                          <label for="">Designation  </label>
                        </div>
                        <div className="col-sm-6">
                          <div className="styled-select">
                            <select id="designationId" name="designationId" value={designationId} onChange={(e)=>{
                                handleChange(e,"designationId")}}>
                            <option>Select Designation</option>
                            {renderOption(this.state.assignments_designation)}
                           </select>
                        </div>
                        </div>
                    </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Department  </label>
                          </div>
                          <div className="col-sm-6">
                          <div className="styled-select">
                              <select id="departmentId" name="departmentId" value={departmentId}
                              onChange={(e)=>{ handleChange(e,"departmentId")}}>
                                <option>Select Department</option>
                                {renderOption(this.state.assignments_department)}
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
                          <label for="">Employee Code</label>
                        </div>
                        <div className="col-sm-6">
                            <input type="text" id="code" name="code" value={code}
                              onChange={(e)=>{handleChange(e,"code")}}/>
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
                                <label for=""> Status</label>
                              </div>
                              <div className="col-sm-6">
                              <div className="styled-select">
                                  <select id="employeeStatusCode" name="employeeStatusCode" value={employeeStatusCode}
                                    onChange={(e)=>{ handleChange(e,"employeeStatusCode") }}>

                                      <option value="">Select Status</option>
                                      {renderOption(this.state.employeeStatus)}
                                 </select>
                              </div>
                              </div>
                          </div>
                        </div>
                        <div className="col-sm-6">
                            <div className="row">
                                <div className="col-sm-6 label-text">
                                  <label for="">Functionary  </label>
                                </div>
                                <div className="col-sm-6">
                                <div className="styled-select">
                                    <select id="functionaryCode" name="functionaryCode" value={functionaryCode}
                                      onChange={(e)=>{  handleChange(e,"functionaryCode")}}>

                                    <option>Select Functionary</option>
                                    {renderOption(this.state.assignments_functionary)}
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
                            <label for="">Type  </label>
                          </div>
                          <div className="col-sm-6">
                            <div className="styled-select">
                              <select id="employeeTypeCode" name="employeeTypeCode" value={employeeTypeCode} onChange={(e)=>{
                                  handleChange(e,"employeeTypeCode")}}>
                              <option>Select Type</option>
                              {renderOption(this.state.employeeType)}
                             </select>
                          </div>
                          </div>
                      </div>
                    </div>
                </div>


            <div className="text-center">
                <button type="submit"  className="btn btn-submit">Search</button> &nbsp;&nbsp;
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
  <SearchLeaveApplication />,
  document.getElementById('root')
);
