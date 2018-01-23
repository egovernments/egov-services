var flag = 0;
class SearchLeaveApplication extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      compensatoryleaves:[],
      employ:{},
      searchSet:{
        name:"",
        code:"",
        departmentId:"",
        designationId:"",
        employeeTypeId:""
      },isSearchClicked:false,
        assignments_department:[],
        assignments_designation:[],
        employeeType:[],
        modified: false
    }
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
    this.setInitialState=this.setInitialState.bind(this);
    this.handleClick=this.handleClick.bind(this);
    this.handleBlur = this.handleBlur.bind(this);
  }


  setInitialState(initState) {
    this.setState(initState);
  }

  search(e) {
    var _this = this;
    let {
    name,
    code,
    departmentId,
    designationId}=_this.state.searchSet;
    e.preventDefault();
    var compensatoryleaves=[];
    commonApiPost("hr-leave","compensatoryleaves","_search",{...this.state.searchSet, tenantId,
      departmentId: departmentId || null,
      designationId: designationId || null,
      code: code || null,
      pageSize: 500},function(err,res){
        if(res){
          compensatoryleaves=res["CompensatoryLeave"];
          flag = 1;
          _this.setState({
            isSearchClicked:true,
            compensatoryleaves,
            modified: true
          });
        }
          setTimeout(function() {
            _this.setState({
              modified: false
            })
          }, 1200);
    });
  }

  componentDidMount(){
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
    }

  var count = 2, _this = this, _state = {};
  var checkCountNCall = function(key, res) {
  count--;
  _state[key] = res;
  if(count == 0)
    _this.setInitialState(_state);
  }

  getDropdown("assignments_designation", function(res) {
    checkCountNCall("assignments_designation", res);
  });
  getDropdown("assignments_department", function(res) {
    checkCountNCall("assignments_department", res);
  });
}


  componentWillUpdate() {
    if(flag == 1) {
      flag = 0;
      $('#employeeTable').dataTable().fnDestroy();
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

  handleClick(type, id, date) {
    if (type==="create") {
      window.open(`app/hr/leavemaster/compensetory-leave.html?id=${id}&type=${type}&workedOnDate=${date}`, '_self', 'resizable,location,scrollbars,status');
    }else {
      window.open(`app/hr/leavemaster/view-compensetory.html?id=${id}&type=${type}&workedOnDate=${date}`, '_self', 'resizable,location,scrollbars,status');
    }
  }

  handleBlur(e) {
    setTimeout(function(){
       if(document.activeElement.id == "sub") {
          $("#sub").click();
       }
    }, 100);
    var _this = this;

    if(e.target.value) {
       var code = e.target.value;
       //Make get employee call
       commonApiPost("hr-employee","employees","_search",{code,tenantId}, function(err, res) {
        if(res) {
          _this.setState({
            searchSet: {
              ..._this.state.searchSet,
              name: res.Employee && res.Employee[0] ? res.Employee[0].name : ""
            }
          })
        }
       });
    } else {
      _this.setState({
        searchSet: {
          ..._this.state.searchSet,
          name: ""
        }
      })
    }
  }


  componentDidUpdate(prevProps, prevState) {
      if (this.state.modified) {
          $('#employeeTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel', 'pdf', 'print'
             ],
             ordering: false,
             bDestroy: true,
             language: {
               "emptyTable": "No Records"
            }
          });
      }
  }

  render() {
    let {handleChange,search,assignments_designation,assignments_department,handleClick,handleBlur}=this;
    let {isSearchClicked,compensatoryleaves}=this.state;
    let {name,
    code,
    departmentId,
    designationId,employeeTypeId}=this.state.searchSet;
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
                        <th>Sl No</th>
                        <th>Worked Date</th>
                        <th>Employee Name</th>
                        <th>Employee Code</th>
                        <th>Status</th>
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
    const renderBody=function()
    {
      if(compensatoryleaves.length>0)
      {
      return compensatoryleaves.map((item,index,id)=>
      {
            return (<tr key={index} >
                    <td>{index+1}</td>
                    <td data-label="workedDate">{item.workedDate}</td>
                    <td data-label="name">{item.name}</td>
                    <td data-label="code">{item.code}</td>
                    <td data-label="status">{item.status}</td>
                    <td data-label="Apply" onClick={() => {handleClick(getUrlVars()["type"], item.employeeId, item.workedDate)}}><a>Apply</a></td>
                </tr>
            );
      })
    }
  }

    return (
      <div>
        <h3>Search employee to {getUrlVars()["type"]} a Compensetory application</h3>
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
                              <select id="departmentId" name="departmentId" value={departmentId}
                              onChange={(e)=>{ handleChange(e,"departmentId")}}>
                                <option  value= "">Select Department</option>
                                {renderOption(this.state.assignments_department)}
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
                                <select id="designationId" name="designationId" value={designationId} onChange={(e)=>{
                                    handleChange(e,"designationId")}}>
                                <option value= "">Select Designation</option>
                                {renderOption(this.state.assignments_designation)}
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
                              onChange={(e)=>{handleChange(e,"code")}} onBlur={(e)=>{handleBlur(e)}} />
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
                                onChange={(e)=>{  handleChange(e,"name")}} disabled/>
                          </div>
                      </div>
                    </div>
            </div>
            <div className="text-center">
                <button id="sub" type="submit"  className="btn btn-submit">Search</button> &nbsp;&nbsp;
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
