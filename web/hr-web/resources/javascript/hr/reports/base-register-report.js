var flag = 0;
class EmployeeReport extends React.Component {

  constructor(props) {
    super(props);
    this.state = {
        "result": [],
        "searchSet": {
            "recruitmentType":"",
            "employeeType": "",
            "employeeStatus": ""
        },
        "employeeTypes": [],
        "recruitmentTypes": [],
        "employeeStatusTypes": [],
        "employeeGroups": [],
        "motherTongueTypes": [],
        "recruitmentModes": [],
        "recruitmentQuota": [],
        "bank":[],
        "branch":[],
        "isSearchClicked": false
    };
    this.handleChange = this.handleChange.bind(this);
    this.searchEmployee = this.searchEmployee.bind(this);
    this.setInitialState = this.setInitialState.bind(this);
    this.closeWindow = this.closeWindow.bind(this);
    this.formatDob = this.formatDob.bind(this);


  }

  setInitialState(initState) {
    this.setState(initState);
  }

  componentWillMount() {

    var _state = {}, _this = this, count = 9;
    const checkCountAndCall = function(key, res) {
      _state[key] = res;
      count--;
      if(count == 0)
        _this.setInitialState(_state);
    }

    getDropdown("employeeType", function(res) {
      checkCountAndCall("employeeTypes", res);
    });
    getDropdown("recruitmentType", function(res) {
      checkCountAndCall("recruitmentTypes", res);
    });
    getDropdown("employeeStatus", function(res) {
      checkCountAndCall("employeeStatusTypes", res);
    });
    getDropdown("group", function(res) {
      checkCountAndCall("employeeGroups", res);
    });
    getDropdown("motherTongue", function(res) {
      checkCountAndCall("motherTongueTypes", res);
    });
    getDropdown("recruitmentMode", function(res) {
      checkCountAndCall("recruitmentModes", res);
    });
    getDropdown("recruitmentQuota", function(res) {
      checkCountAndCall("recruitmentQuota", res);
    });
    getDropdown("bank", function(res) {
      checkCountAndCall("bank", res);
    });
    getDropdown("bankbranches", function(res) {
      checkCountAndCall("branch", res);
    });

  }

  componentDidUpdate(prevProps, prevState)
  {
      if (flag === 1) {
        flag = 0;
          $('#employeeTable').DataTable({
            dom: 'Bfrtip',
            buttons: [
                     'copy', 'csv', 'excel'
             ],
             ordering: false,
             language: {
               "emptyTable": "No Records"
             }
          });
      }
  }

  handleChange(e, name) {
      this.setState({
          ...this.state,
          searchSet:{
              ...this.state.searchSet,
              [name]:e.target.value
          }
      })
  }

  closeWindow () {
      open(location, '_self').close();
  }

  formatDob(date){
    if(date){
        var dateParts1 = date.split("-");
        var newDateStr = dateParts1[2] + "/" + dateParts1[1] + "/" + dateParts1[0];
        return newDateStr;
    }
  }

  searchEmployee (e) {
    e.preventDefault();
    $('#employeeTable').DataTable().destroy();
    var _this = this
    var result;
    try {
        flag = 1;
        result = commonApiPost("hr-employee", "employees", "_baseregisterreport", {..._this.state.searchSet, tenantId,pageSize:500},function(err, res) {
          if(res && res.Employee) {
            _this.setState({
              ..._this.state,
                isSearchClicked: true,
                result : res.Employee
            })
          }else {
            _this.setState({
              ..._this.state,
                isSearchClicked: true,
                result : []
            })
          }
        });
    } catch (e) {
        result = [];
        console.log(e);
    }
  }

  render() {
    let {handleChange, searchEmployee, formatDob, closeWindow} = this;
    let {result, employeeTypes,employeeStatusTypes,recruitmentTypes,employeeGroups,motherTongueTypes,recruitmentModes,recruitmentQuota,bank,branch} = this.state;
    let {recruitmentType, employeeStatus, employeeType} = this.state.searchSet;

    const renderOptions = function(list)
    {
        if(list && list.constructor == Array)
        {
            return list.map((item)=>
            {
                return (<option key={item.id} value={item.id}>
                        {item.name}
                    </option>)
            })
        }
    }
    const renderOptionsForDesc = function(list)
    {
        if(list && list.constructor == Array)
        {
            return list.map((item)=>
            {
                return (<option key={item.id} value={item.id}>
                        {item.code}
                    </option>)
            })
        }
    }

    const renderTr = () => {
        return result.map((item, ind) => {
            return (
                <tr key={ind}>
                      <td>{ind+1}</td>
                      <td>{item.code}</td>
                      <td>{item.salutation? item.salutation+" "+item.name : item.name}</td>
                      <td>{getNameById(employeeTypes,item.employeeType)}</td>
                      <td>{getNameById(employeeStatusTypes,item.employeeStatus,"code")}</td>
                      <td>{getNameById(employeeGroups,item.group)}</td>
                      <td>{formatDob(item.dob)}</td>
                      <td>{item.gender}</td>
                      <td>{item.maritalStatus}</td>
                      <td>{item.userName}</td>
                      <td>{item.active?"Yes":"No"}</td>
                      <td>{item.mobileNumber}</td>
                      <td>{item.emailId}</td>
                      <td>{item.guardian}</td>
                      <td>{item.placeOfBirth}</td>
                      <td>{item.bloodGroup}</td>
                      <td>{getNameById(motherTongueTypes,item.motherTongue)}</td>
                      <td>{item.identificationMark}</td>
                      <td>{item.pan}</td>
                      <td>{item.passportNo}</td>
                      <td>{item.gpfNo}</td>
                      <td>{item.aadhaarNumber}</td>
                      <td>{getNameById(bank,item.bank)}</td>
                      <td>{getNameById(branch,item.bankBranch)}</td>
                      <td>{item.bankAccount}</td>
                      <td>{item.altContactNumber}</td>
                      <td>{item.permanentPinCode}</td>
                      <td>{item.correspondencePinCode}</td>
                      <td>{item.languagesKnown.map((item) => getNameById(motherTongueTypes,item)).join()}</td>
                      <td>{getNameById(recruitmentModes,item.recruitmentMode)}</td>
                      <td>{getNameById(recruitmentTypes,item.recruitmentType)}</td>
                      <td>{getNameById(recruitmentQuota,item.recruitmentQuota)}</td>
                      <td>{item.dateOfAppointment}</td>
                      <td>{item.dateOfJoining}</td>
                      <td>{item.retirementAge}</td>
                      <td>{item.dateOfRetirement}</td>
                      <td>{item.dateOfTermination}</td>
                      <td>{item.dateOfResignation}</td>
                </tr>
            )
        })
    }

    const showTable = () => {
        if(this.state.isSearchClicked) {
            return (
                <div className="form-section" >
                    <h3 className="pull-left">Employee Details </h3>
                    <div className="clearfix"></div>
                    <div className="table-responsive">
                        <table id="employeeTable" className="table table-bordered">
                            <thead>
                            <tr>
                                <th>SlNo</th>
                                <th>Employee Code *</th>
                                <th>Employee Name *</th>
                                <th>Employee Type *</th>
                                <th>Status *</th>
                                <th>Employee Group</th>
                                <th>Date Of Birth *</th>
                                <th>Gender *</th>
                                <th>Marital Status *</th>
                                <th>User Name *</th>
                                <th>Is User Active? *</th>
                                <th>Mobile number *</th>
                                <th>Email ID</th>
                                <th>Father/Spouse Name</th>
                                <th>Native/Birth Place</th>
                                <th>Blood Group</th>
                                <th>Mother Tongue</th>
                                <th>Identification Mark</th>
                                <th>PAN</th>
                                <th>Passport Number</th>
                                <th>GPF Number/CPS Number</th>
                                <th>Aadhaar Number</th>
                                <th>Bank</th>
                                <th>Branch</th>
                                <th>Account number</th>
                                <th>Phone number (Emergency/Res)</th>
                                <th>Permanent Address City Pin code</th>
                                <th>Correspondence Address City Pin code</th>
                                <th>Languages Known</th>
                                <th>Recruitment Mode</th>
                                <th>Recruitment Types/Service Type</th>
                                <th>Recruitment Quota</th>
                                <th>Date of Appointment *</th>
                                <th>Date of Joining/Deputation</th>
                                <th>Retirement Age</th>
                                <th>Retirement Date</th>
                                <th>Termination Date</th>
                                <th>Resignation Date</th>
                            </tr>
                            </thead>
                            <tbody>
                                {renderTr()}
                            </tbody>
                        </table>
                    </div>
                </div>
            )
        }
    }

    return (
        <div>
            <div className="form-section">
                <h3 className="pull-left">Employee Search </h3>
                <div className="clearfix"></div>
                <form onSubmit={(e)=>
                    {searchEmployee(e)}}>
                    <fieldset>
                        <div className="row">
                            <div className="col-sm-6">
                                <div className="row">
                                <div className="col-sm-6 label-text">
                                    <label for="">Employee Status </label>
                                </div>
                                <div className="col-sm-6">
                                  <div className="styled-select">
                                    <select id="employeeStatus" value={employeeStatus} onChange={(e) => {handleChange(e, "employeeStatus")}}>
                                      <option value="">Select Employee Status</option>
                                        {renderOptionsForDesc(employeeStatusTypes)}
                                    </select>
                                </div>
                            </div>
                                </div>
                            </div>
                            <div className="col-sm-6">
                                <div className="row">
                                    <div className="col-sm-6 label-text">
                                        <label for="">Employee Type </label>
                                    </div>
                                    <div className="col-sm-6">
                                      <div className="styled-select">
                                        <select id="employeeType" value={employeeType} onChange={(e) => {handleChange(e, "employeeType")}}>
                                          <option value="">Select EmployeeType</option>
                                            {renderOptions(employeeTypes)}
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
                                    <label for="">Recruitment Type </label>
                                </div>
                                <div className="col-sm-6">
                                  <div className="styled-select">
                                    <select id="recruitmentType" value={recruitmentType} onChange={(e) => {handleChange(e, "recruitmentType")}}>
                                      <option value="">Select Recruitment Types</option>
                                        {renderOptions(recruitmentTypes)}
                                    </select>
                                </div>
                            </div>
                                </div>
                            </div>
                        </div>
                        <div className="text-center">


                            <button type="submit" className="btn btn-submit">Search</button>&nbsp;&nbsp;
                            <button type="button" className="btn btn-close" onClick={(e)=>{this.closeWindow()}}>Close</button>
                        </div>
                    </fieldset>
                </form>
            </div>
            <br/>
            {showTable()}
        </div>
    );
  }
}






ReactDOM.render(
  <EmployeeReport />,
  document.getElementById('root')
);
