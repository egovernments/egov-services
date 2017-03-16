
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
  }

  search(e)
  {
    let {month,year,designationCode,departmentCode,code,employeeType}=this.state.searchSet;
    e.preventDefault();
    // console.log(e.target.value);
    window.location=`../../../app/hr/attendance/attendance.html?month=${month}&year=${year}&designationCode=${designationCode}&departmentCode=${departmentCode}&code=${code}&type=${employeeType}`;

    // console.log("fired");
  }

  componentWillMount()
  {
    var date=new Date();
    var month=[{id:0,name:"January"},{id:1,name:"February"}, {id:2,name:"March"}, {id:3,name:"April"}, {id:4,name:"May"},
         {id:5,name:"June"}, {id:6,name:"July"}, {id:7,name:"August"}, {id:8,name:"September"}, {id:9,name:"October"},
         {id:10,name:"November"}, {id:11,name:"December"}]
    this.setState({
      employeeType:getCommonMaster("hr-masters","employeetypes","EmployeeType").responseJSON["EmployeeType"] || [],
      departments:getCommonMaster("egov-common-masters","departments","Department").responseJSON["Department"] || [],
      designation:getCommonMaster("hr-masters","designations","Designation").responseJSON["Designation"] || [] ,
      month,
      year:getCommonMaster("egov-common-masters","calendaryears","CalendarYear").responseJSON["CalendarYear"] || []
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

  render() {
    console.log(this.state.searchSet);
    let {handleChange,search}=this;
    let {month,year,designationCode,departmentCode,code,employeeType}=this.state.searchSet;
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
    return (
      <div>
          <form onSubmit={(e)=>{search(e)}}>
          <fieldset>
            <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="">Year <span> *</span> </label>
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
                            <label for="">Month  <span> *</span></label>
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
                        <label for="">Department  </label>
                      </div>
                      <div className="col-sm-6">
                      <div className="styled-select">
                          <select id="department" name="department" onChange={(e)=>{
                              handleChange(e,"departmentCode")
                          }}>
                            <option>Select Department</option>
                            {renderOption(this.state.departments)}
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
                        <label for="">Employee Code/Name  </label>
                      </div>
                      <div className="col-sm-6">
                          <input type="text" name="code" onChange={(e)=>{
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
                            <select id="employeeType" name="employeeType" onChange={(e)=>{
                                handleChange(e,"employeeType")
                            }}>
                                <option>Select Type</option>
                                {renderOption(this.state.employeeType)}
                           </select>
                        </div>
                        </div>
                    </div>
                  </div>
            </div>



            <div className="text-center">
                <button type="button" className="btn btn-submit" onClick={(e)=>{this.close()}}>Close</button>
                <button type="submit"  className="btn btn-submit">Search</button>
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
