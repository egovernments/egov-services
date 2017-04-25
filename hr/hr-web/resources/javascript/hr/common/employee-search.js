class EmployeeSearch extends React.Component {
  constructor(props) {
    super(props);
    this.state={employees:[],searchSet:{
    code:"",
    departmentId:"",
    designationId:"",
    employeeType:"",
    asOnDate:"",
    name:"",mobileNumber:"",pan:"",aadhaarNumber:""},isSearchClicked:false,employeeTypeList:[],departmentList:[],designationList:[]}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
    this.handleBlur=this.handleBlur.bind(this);
  }

  search(e) {
    let {
    code,
    name,
    departmentId,
    designationId,
    employeeType,
    asOnDate}=this.state.searchSet;
    e.preventDefault();
    //call api call
    var employees = [];
    try {
      employees = commonApiPost("hr-employee","employees","_search", {tenantId,code,departmentId,designationId,name,pageSize:500},this.state.searchSet).responseJSON["Employee"] || [];
    } catch (e) {
      employees = [];
      console.log(e);
    }

    this.setState({
      isSearchClicked:true,
      employees
    })

  }
  handleBlur(e) {
    var _this=this;

    if(e.target.value) {
      try{
        var code = e.target.value;
       //Make get employee call
       var obj = commonApiPost("hr-employee","employees","_search",{code,tenantId}).responseJSON["Employee"][0];
       _this.setState({
         searchSet:{
             ..._this.state.searchSet,
             name:obj.name
           }
     })
  }
      catch (e){
        console.log(e);
      }
  }
}



  componentWillMount() {
    this.setState({
      departmentList: Object.assign([], assignments_department),
      designationList: Object.assign([], assignments_designation)
    })
  }

  componentDidUpdate(prevProps, prevState)
  {
      if (prevState.employees.length!=this.state.employees.length) {
          $('#employeeTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false
          });
      }
  }
  componentDidMount() {
    var type = getUrlVars()["type"], _this = this;
    var id = getUrlVars()["id"];
    $('#asOnDate').datepicker({
        format: 'DD/MM/YYYY',
        // maxDate: new Date(),
        defaultDate: ""
    });
    $('#asOnDate').val("");
    $('#asOnDate').on("dp.change", function(e) {
          _this.setState({
                searchSet: {
                    ..._this.state.searchSet,
                    "asOnDate":$("#asOnDate").val()
                }
          })
      });
    }

  handleChange(e,name) {
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

  render() {
    console.log(this.state.searchSet);
    let {handleChange,search,handleBlur}=this;
    let {isSearchClicked,employees}=this.state;
    let {
    code,
    departmentId,
    designationId,
    asOnDate,name,mobileNumber,pan,aadhaarNumber}=this.state.searchSet;
    const renderOption = function(list,listName="") {
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
    const showTable = function() {
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
                        <th>Employee Position</th>
                        <th>Date-Range</th>
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
      }
    }


    const renderAction=function(type,id){
      if (type==="update") {
              return (
                      <a href={`app/hr/employee/create.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-pencil"></span></a>
              );
      } else {
            return (
                    <a href={`app/hr/employee/create.html?id=${id}&type=${type}`} className="btn btn-default btn-action"><span className="glyphicon glyphicon-modal-window"></span></a>
            );
      }
}

const getTodaysDate = function() {
        var now = new Date();
        var month = (now.getMonth() + 1);
        var day = now.getDate();
        if(month < 10)
            month = "0" + month;
        if(day < 10)
            day = "0" + day;
        return (now.getFullYear() + '-' + month + '-' + day);
    }

  //  <td data-label="designation">{getNameById(assignments_designation,item.assignments[0].designation)}</td>
    const renderBody = function() {
      if(employees.length>0)
      {
      return employees.map((item,index)=>
      {
            return (<tr key={index}>
                    <td data-label="code">{item.code}</td>
                    <td data-label="name">{item.name}</td>

                    <td data-label="designation">{getNameById(assignments_designation,item.assignments[0].designation)}</td>
                    <td data-label="department">{getNameById(assignments_department,item.assignments[0].department)}</td>
                    <td data-label="position">{getNameById(assignments_position,item.assignments[0].position)}</td>
                    <td data-label="range">{item.assignments[0].fromDate}-{item.assignments[0].toDate}</td>
                    <td data-label="action">
                    {renderAction(getUrlVars()["type"],item.id)}
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
                      <label for="">Department  </label>
                    </div>
                    <div className="col-sm-6">
                    <div className="styled-select">
                        <select id="department" name="departmentId" value={departmentId} onChange={(e)=>{
                            handleChange(e,"departmentId")
                        }}>
                          <option value="">Select department</option>
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
                          <select id="designation" name="designationId" value={designationId} onChange={(e)=>{
                              handleChange(e,"designationId")
                          }}>
                          <option value="">Select Designation</option>
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
                      <label for="">Employee Code  </label>
                    </div>
                    <div className="col-sm-6">
                        <input type="text" name="code" id="code" onChange={(e)=>{
                            handleChange(e,"code")
                        }} onBlur={(e)=>{handleBlur(e)}}/>
                    </div>
                </div>
              </div>
              {<div className="col-sm-6">
                  <div className="row">
                      <div className="col-sm-6 label-text">
                        <label for=""> Employee Name  </label>
                      </div>
                      <div className="col-sm-6">
                          <input type="text" name="name" id="name" value= {name} onChange={(e)=>{
                              handleChange(e,"name")
                          }} disabled/>
                      </div>
                  </div>
                </div>}
          </div>
        {/*<div className="row">
            <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="description">As On Date</label>
                        </div>
                        <div className="col-sm-6">
                            <input type="text" id="asOnDate" name="asOnDate" value= {asOnDate}
                              onChange={(e)=>{handleChange(e,"asOnDate")}} />
                        </div>
                      </div>
                  </div>
            <div className="col-sm-6">
                      <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="description">Mobile Number</label>
                        </div>
                        <div className="col-sm-6">
                            <input type="number" id="mobileNumber" name="mobileNumber" value= {mobileNumber}
                              onChange={(e)=>{handleChange(e,"mobileNumber")}}/>
                        </div>
                      </div>
                  </div>
        </div>
    <div>
        <div className="col-sm-6">
            <div className="row">
                <div className="col-sm-6 label-text">
                  <label for="">Pan  </label>
                </div>
                <div className="col-sm-6">
                    <input type="text" name="pan" id="pan" onChange={(e)=>{
                        handleChange(e,"pan")
                    }} />
                </div>
            </div>
          </div>
          <div className="col-sm-6">
              <div className="row">
                  <div className="col-sm-6 label-text">
                    <label for="">Aadhar Code  </label>
                  </div>
                  <div className="col-sm-6">
                      <input type="text" name="aadhaarNumber" id="aadhaarNumber" onChange={(e)=>{
                          handleChange(e,"aadhaarNumber")
                      }} />
                  </div>
              </div>
            </div>
        </div>*/}
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
