const month = [{
        id: 0,
        name: "January"
    }, {
        id: 1,
        name: "February"
    }, {
        id: 2,
        name: "March"
    }, {
        id: 3,
        name: "April"
    }, {
        id: 4,
        name: "May"
    }, {
        id: 5,
        name: "June"
    }, {
        id: 6,
        name: "July"
    }, {
        id: 7,
        name: "August"
    }, {
        id: 8,
        name: "September"
    }, {
        id: 9,
        name: "October"
    }, {
        id: 10,
        name: "November"
    }, {
        id: 11,
        name: "December"
    }
];

class EmployeeSearch extends React.Component {
  constructor(props) {
    super(props);
    this.state={searchSet:{month:"",
    year:"",
    code:"",
    departmentCode:"",
    designationCode:"",
    employeeType:""},employeeType:[],department:[],designation:[],month:[],year:[]}
    this.handleChange=this.handleChange.bind(this);
    this.search=this.search.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
  }

  setInitialState(initState) {
    this.setState(initState);
  }

  search(e) {
    let {month,year,designationCode,departmentCode,code,employeeType}=this.state.searchSet;
    e.preventDefault();

    if (!code && !departmentCode) {
        return showError("Either Employee Code or Department is mandatory");
    }

    window.location=`app/hr/attendance/attendance.html?month=${month}&year=${year}&designationCode=${designationCode}&departmentCode=${departmentCode}&code=${code}&type=${employeeType}`;
  }

  handleChange(e,name) {
      this.setState({
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })

      if(name == "year") {
          if(e.target.value == new Date().getFullYear()) {
                var currentMonthId = new Date().getMonth();
                var _months = month.filter(val => {
                    return (val.id <= currentMonthId);
                });
                this.setState({
                    //...this.state,
                    month: _months
                })
          } else {
                this.setState({
                    //...this.state,
                    month
                })
          }
      }
  }

  componentDidMount() {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
    }

    $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Employee");

    var count = 4, _state = {month}, _this = this;
    
    const checkCountAndCall = function(key, res) {
      _state[key] = res;
      count--;
      if(count == 0)
        _this.setInitialState(_state);
    }

    getDropdown("assignments_designation", function(res) {
      checkCountAndCall("designation", res);
    });
    getDropdown("assignments_department", function(res) {
      checkCountAndCall("department", res);
    });
    getDropdown("employeeType", function(res) {
      checkCountAndCall("employeeType", res);
    });
    getDropdown("years", function(res) {
      checkCountAndCall("year", res);
    });
  }

  close(){
      // widow.close();
      open(location, '_self').close();
  }

  render() {
    let {handleChange,search}=this;
    let {month,year,designationCode,departmentCode,code,employeeType}=this.state.searchSet;
    const renderOption=function(list,listName="")
    {
        if(list && list.constructor == Array)
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
    return (
      <div>
          <form onSubmit={(e)=>{search(e)}}>
          <fieldset>
            <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label htmlFor="">Year <span> *</span> </label>
                        </div>
                        <div className="col-sm-6">
                        <div className="styled-select">
                            <select id="year" name="year" onChange={(e)=>{
                                handleChange(e,"year")
                            }} required>
                            <option value="">Select Year</option>
                            {renderOption(this.state.year,"year")}
                           </select>
                        </div>
                        </div>
                    </div>
                  </div>

                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label htmlFor="">Month  <span> *</span></label>
                          </div>
                          <div className="col-sm-6">
                          <div className="styled-select">
                              <select id="month" name="month" onChange={(e)=>{
                                  handleChange(e,"month")
                              }} required>
                                  <option value="">Select Month</option>
                                  {renderOption(this.state.month)}
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
                        <label htmlFor="">Department  </label>
                      </div>
                      <div className="col-sm-6">
                      <div className="styled-select">
                          <select id="department" name="department" onChange={(e)=>{
                              handleChange(e,"departmentCode")
                          }}>
                            <option>Select Department</option>
                            {renderOption(this.state.department)}
                         </select>
                      </div>
                      </div>
                  </div>
                </div>
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label htmlFor="">Designation  </label>
                        </div>
                        <div className="col-sm-6">
                        <div className="styled-select">
                            <select id="designation" name="designation" onChange={(e)=>{
                                handleChange(e,"designationCode")
                            }}>
                            <option>Select Designation</option>
                            {renderOption(this.state.designation)}
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
                        <label htmlFor="">Employee Code/Name  </label>
                      </div>
                      <div className="col-sm-6">
                          <input type="text" name="code" onChange={(e)=>{
                              handleChange(e,"code")
                          }} />
                      </div>
                  </div>
                </div>

            </div>



            <div className="text-center">
                <button type="submit"  className="btn btn-submit">Search</button> &nbsp;&nbsp;
                <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>

            </div>
            <br />
            <div className="text-right text-danger">
                            Note: Either Employee Code or Department is mandatory.
            </div>
          </fieldset>
          </form>

      </div>
    );
  }
}






ReactDOM.render(
  <EmployeeSearch />,
  document.getElementById('root')
);


// <div className="col-sm-6">
//     <div className="row">
//         <div className="col-sm-6 label-text">
//           <label for="">Employee Type  </label>
//         </div>
//         <div className="col-sm-6">
//         <div className="styled-select">
//             <select id="employeeType" name="employeeType" onChange={(e)=>{
//                 handleChange(e,"employeeType")
//             }}>
//                 <option>Select Type</option>
//                 {renderOption(this.state.employeeType)}
//            </select>
//         </div>
//         </div>
//     </div>
//   </div>
