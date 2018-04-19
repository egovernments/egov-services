function today() {
  var today = new Date();
  var dd = today.getDate();
  var mm = today.getMonth() + 1; 
  var yyyy = today.getFullYear();
  if (dd < 10) {
    dd = '0' + dd;
  }
  if (mm < 10) {
    mm = '0' + mm;
  }
  var today = dd + '/' + mm + '/' + yyyy;

  return today;
}

class ApplyLeave extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
      list: [], leaveSet: {
        "employee": "",
        "name": "",
        "code": "",
        "leaveType": {
          "id": ""
        },
        "fromDate": "",
        "toDate": "",
        "availableDays": "",
        "leaveGround": "",
        "leaveDays": "",
        "reason": "",
        "status": "",
        "stateId": "",
        "tenantId": tenantId,
        "encashable": false,
        "totalWorkingDays": "",
        "workflowDetails": {
          "department": "",
          "designation": "",
          "assignee": "",
          "action": "",
          "status": ""
        }

      }, leaveList: [], stateId: "", owner: "", leaveNumber: "", perfixSuffix: "", encloseHoliday: ""
    }
    this.handleChange = this.handleChange.bind(this);
    this.addOrUpdate = this.addOrUpdate.bind(this);
    this.handleChangeThreeLevel = this.handleChangeThreeLevel.bind(this);
    this.getPrimaryAssigmentDep = this.getPrimaryAssigmentDep.bind(this);
    this.isSecondSat = this.isSecondSat.bind(this);
    this.isFourthSat = this.isFourthSat.bind(this);
    this.calculate = this.calculate.bind(this);

  }

  componentDidMount() {

    if (window.opener && window.opener.document) {
      var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
      if (logo_ele && logo_ele[0]) {
        document.getElementsByClassName("homepage_logo")[0].src = (logo_ele[0].getAttribute("src") && logo_ele[0].getAttribute("src").indexOf("http") > -1) ? logo_ele[0].getAttribute("src") : window.location.origin + logo_ele[0].getAttribute("src");
      }
    }

    $('#availableDays,#leaveDays,#name,#code').prop("disabled", true);

    if (getUrlVars()["type"]) $('#hp-citizen-title').text(titleCase(getUrlVars()["type"]) + " Leave Application");
    var type = getUrlVars()["type"], _this = this;
    var id = getUrlVars()["id"];
    var asOnDate = _this.state.leaveSet.toDate,
      _leaveSet = {}, employee;

    if (getUrlVars()["type"] === "view") {
      $("input,select,textarea").prop("disabled", true);
    }

    if (type === "view") {
      getCommonMasterById("hr-leave", "leaveapplications", id, function (err, res) {
        if (res && res.LeaveApplication && res.LeaveApplication[0]) {
          _leaveSet = res && res.LeaveApplication && res.LeaveApplication[0];
          commonApiPost("hr-employee", "employees", "_search", {
            tenantId,
            id: _leaveSet.employee
          }, function (err, res1) {
            if (res1 && res1.Employee && res1.Employee[0]) {
              employee = res1 && res1.Employee && res1.Employee[0];
              _leaveSet.name = employee.name;
              _leaveSet.code = employee.code;
              _this.setState({
                leaveSet: _leaveSet,
                leaveNumber: _leaveSet.applicationNumber
              })
            } else {
              showError("Something went wrong. Please contact Administrator.");
            }
          })
        } else {
          showError("Something went wrong. Please contact Administrator.");
        }
      });
    } else {

      var hrConfigurations = [], allHolidayList = [];
      commonApiPost("hr-masters", "hrconfigurations", "_search", {
        tenantId
      }, function (err, res) {
        if (res) {
          _this.setState({
            ..._this.state,
            hrConfigurations: res ? res : []
          });

        }
      });

      commonApiPost("egov-common-masters", "holidays", "_search", {
        tenantId
      }, function (err, res) {

        _this.setState({
          ..._this.state,
          allHolidayList: res ? res.Holiday : []
        });

      });


      if (!id) {
        commonApiPost("hr-employee", "employees", "_loggedinemployee", { tenantId }, function (err, res) {
          if (res && res.Employee && res.Employee[0]) {
            var obj = res.Employee[0];
            _this.setState({
              leaveSet: {
                ..._this.state.leaveSet,
                name: obj.name,
                code: obj.code,
                employee: obj.id
              },
              departmentId: _this.getPrimaryAssigmentDep(obj, "department")
            })
          } else {
            showError("Something went wrong. Please contact Administrator.");
          }
        });
      } else {
        getCommonMasterById("hr-employee", "employees", id, function (err, res) {
          if (res) {
            var obj = res.Employee[0];
            _this.setState({
              leaveSet: {
                ..._this.state.leaveSet,
                name: obj.name,
                code: obj.code,
                employee: obj.id
              },
              departmentId: _this.getPrimaryAssigmentDep(obj, "department")
            })
          }
        })
      }
    }

  }


  componentDidUpdate(){

    var type = getUrlVars()["type"], _this = this;
    var id = getUrlVars()["id"];

    $('#fromDate, #toDate').datepicker({
      format: 'dd/mm/yyyy',
      autoclose: true
    });

    $('#fromDate, #toDate').on("change", function (e) {

      if (!_this.state.leaveSet.leaveType.id) {
        showError("Please select Leave Type before entering from date and to date.");
        $('#' + e.target.id).val("");
      }

     if(_this.state.leaveSet[e.target.id] != e.target.value){

      var _from = $('#fromDate').val();
      var _to = $('#toDate').val();
      var _triggerId = e.target.id;

        _this.setState({
          leaveSet: {
            ..._this.state.leaveSet,
            [_triggerId]: $("#" + _triggerId).val()
          }
        });

        if (_from && _to) {

          let dateParts1 = _from.split("/");
          let newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
          let date1 = new Date(newDateStr);

          let dateParts2 = _to.split("/");
          let newDateStr1 = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
          let date2 = new Date(newDateStr1);


          if (date1 > date2) {
            showError("From date must be before End date.");
            $('#' + _triggerId).val("");
          }


          _this.calculate();
        }

    }
    });

  }

  getPrimaryAssigmentDep(obj, type) {
    for (var i = 0; i < obj.assignments.length; i++) {
      if (obj.assignments[i].isPrimary) {
        return obj.assignments[i][type];
      }
    }
  }

  componentWillMount() {
    var _this = this;
    getCommonMaster("hr-leave", "leavetypes", function (err, res) {
      if (res) {
        _this.setState({
          leaveList: res.LeaveType
        })
      }
    })
  }

  calculate() {

    let _this = this;
    let asOnDate = _this.state.leaveSet.toDate;
    let fromDate = _this.state.leaveSet.fromDate;
    let leaveDays = _this.state.leaveSet.leaveDays;
    let leaveType = _this.state.leaveSet.leaveType.id;
    let employeeid = getUrlVars()["id"] || _this.state.leaveSet.employee;
    let allHolidayList = _this.state.allHolidayList;
    let hrConfigurations = _this.state.hrConfigurations;
    let enclosingDays = 0;
    let prefixSuffixDays = 0;

    //Calling enclosing Holiday api
    let enclosingHoliday = getNameById(_this.state.leaveList, _this.state.leaveSet.leaveType.id, "encloseHoliday");
    if (enclosingHoliday || enclosingHoliday == "TRUE" || enclosingHoliday == "true") {
      commonApiPost("egov-common-masters", "holidays", "_search", { tenantId, fromDate, toDate: asOnDate }, function (err, res) {
        if (res) {
          console.log("enclosingDays", res.Holiday.length);
          enclosingDays = res.Holiday.length;
          _this.setState({
            encloseHoliday: res.Holiday
          });
        }
      });
    } else {
      _this.setState({
        encloseHoliday: ""
      });
    }

    //calling PrefixSuffix api
    let includePrefixSuffix = getNameById(_this.state.leaveList, _this.state.leaveSet.leaveType.id, "includePrefixSuffix");
    if (includePrefixSuffix || includePrefixSuffix == "TRUE" || includePrefixSuffix == "true") {
      commonApiPost("egov-common-masters", "holidays", "_searchprefixsuffix", { tenantId, fromDate, toDate: asOnDate }, function (err, res) {
        if (res) {
          console.log("prefixSuffixDays", res.Holiday[0].noOfDays);
          prefixSuffixDays = res.Holiday[0].noOfDays;
          _this.setState({
            perfixSuffix: res.Holiday[0]
          });
        }
      });
    } else {
      _this.setState({
        perfixSuffix: ""
      });
    }

    var holidayList = [], m1 = fromDate.split("/")[1], m2 = asOnDate.split("/")[1], y1 = fromDate.split("/")[2], y2 = asOnDate.split("/")[2];
    for (var i = 0; i < allHolidayList.length; i++) {
      if (allHolidayList[i].applicableOn && +allHolidayList[i].applicableOn.split("/")[1] >= +m1 && +allHolidayList[i].applicableOn.split("/")[1] <= +m2 && +allHolidayList[i].applicableOn.split("/")[2] <= y1 && +allHolidayList[i].applicableOn.split("/")[2] >= y2) {
        holidayList.push(new Date(allHolidayList[i].applicableOn.split("/")[2], allHolidayList[i].applicableOn.split("/")[1] - 1, allHolidayList[i].applicableOn.split("/")[0]).getTime());
      }
    }
    //Calculate working days
    var _days = 0;
    var parts1 = $('#fromDate').val().split("/");
    var parts2 = $('#toDate').val().split("/");
    var startDate = new Date(parts1[2], (+parts1[1] - 1), parts1[0]);
    var endDate = new Date(parts2[2], (+parts2[1] - 1), parts2[0]);

    if (hrConfigurations["HRConfiguration"]["Weekly_holidays"][0] == "5-day week") {
      for (var d = startDate; d <= endDate; d.setDate(d.getDate() + 1)) {
        if (holidayList.indexOf(d.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0] != "Y" && !(d.getDay() === 0 || d.getDay() === 6))
          _days++;
      }
    } else if (hrConfigurations["HRConfiguration"]["Weekly_holidays"][0] == "5-day week with 2nd Saturday holiday") {
      for (var d = startDate; d <= endDate; d.setDate(d.getDate() + 1)) {
        if (holidayList.indexOf(d.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0] != "Y" && !_this.isSecondSat(d) && d.getDay() != 0)
          _days++;
      }
    } else if (hrConfigurations["HRConfiguration"]["Weekly_holidays"][0] == "5-day week with 2nd and 4th Saturday holiday") {
      for (var d = startDate; d <= endDate; d.setDate(d.getDate() + 1)) {
        if (holidayList.indexOf(d.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0] != "Y" && d.getDay() != 0 && !_this.isSecondSat(d) && !_this.isFourthSat(d))
          _days++;
      }
    } else {
      for (var d = startDate; d <= endDate; d.setDate(d.getDate() + 1)) {
        if (holidayList.indexOf(d.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0] != "Y" && !(d.getDay() === 0))
          _days++;
      }
    }


    setTimeout(function () {
      console.log("prefixSuffixDays ", prefixSuffixDays, " ", enclosingDays);
      _this.setState({
        leaveSet: {
          ..._this.state.leaveSet,
          leaveDays: _days,
          totalWorkingDays: _days + prefixSuffixDays + enclosingDays
        }
      })
    }, 500);


    commonApiPost("hr-leave", "eligibleleaves", "_search", {
      leaveType, tenantId, asOnDate, employeeid
    }, function (err, res) {
      if (res) {
        var _day = res && res["EligibleLeave"] && res["EligibleLeave"][0] ? res["EligibleLeave"][0].noOfDays : "";
        if (_day <= 0 || _day == "") {
          _this.setState({
            leaveSet: {
              ..._this.state.leaveSet,
              availableDays: ""
            }
          });
          return (showError("You do not have leave for this leave type."));
        }
        else {
          _this.setState({
            leaveSet: {
              ..._this.state.leaveSet,
              availableDays: _day
            }
          });
        }
      }else{
        return (showError("You do not have leave for this leave type."));
      }
    });

  }

  handleChangeThreeLevel(e, pName, name) {
    var _this = this, val = e.target.value;
    if (pName == "leaveType") {

      this.setState({
        leaveSet: {
          ...this.state.leaveSet,
          encashable:false,
          [pName]: {
            ...this.state.leaveSet[pName],
            [name]: e.target.value
          }
        }
      });

      if (_this.state.leaveSet.fromDate && _this.state.leaveSet.toDate)
        _this.calculate();


    } else {

      this.setState({
        leaveSet: {
          ...this.state.leaveSet,
          [pName]: {
            ...this.state.leaveSet[pName],
            [name]: e.target.value
          }
        }
      });
    }
  }

  handleChange(e, name) {

    if (name === "encashable") {
      if (e.target.checked)
        $('#totalWorkingDays').prop("disabled", false);
      else
        $('#totalWorkingDays').prop("disabled", true);
      let _this = this
      
      var asOnDate = today();
      let leaveType = this.state.leaveSet.leaveType.id;
      let employeeid = getUrlVars()["id"];

      commonApiPost("hr-leave", "eligibleleaves", "_search", {
        leaveType, tenantId, asOnDate, employeeid
      }, function (err, res) {
        if (res) {
          var _day = res && res["EligibleLeave"] && res["EligibleLeave"][0] ? res["EligibleLeave"][0].noOfDays : "";
          if (_day <= 0 || _day == "") {
            _this.setState({
              leaveSet: {
                ..._this.state.leaveSet,
                availableDays: "",
                fromDate:"",
                toDate:""
              }
            });
            return (showError("You do not have leave for this leave type."));
          }
          else {
            _this.setState({
              leaveSet: {
                ..._this.state.leaveSet,
                availableDays: _day,
                fromDate:"",
                toDate:""
              }
            });
          }
        }else{

          _this.setState({
            leaveSet: {
              ..._this.state.leaveSet,
              fromDate:"",
              toDate:""
            }
          });

        }
      });
    }

    this.setState({
      leaveSet: {
        ...this.state.leaveSet,
        [name]: e.target.type === 'checkbox' ? e.target.checked : e.target.value
      }
    })

  }

  close() {
    // widow.close();
    open(location, '_self').close();
  }

  isSecondSat(d) {
    return (d.getDay() == 6 && Math.ceil(d.getDate() / 7) == 2);
  }

  isFourthSat(d) {
    return (d.getDay() == 6 && Math.ceil(d.getDate() / 7) == 4);
  }

  addOrUpdate(e, mode) {
    e.preventDefault();
    var _this = this;

    if (_this.state.leaveSet.availableDays <= 0 && _this.state.leaveSet.availableDays == "") {
      return (showError("You do not have leave for this leave type."));
    }

    let maxDays = getNameById(_this.state.leaveList, _this.state.leaveSet.leaveType.id, "maxDays");
    if (maxDays && maxDays < _this.state.leaveSet.leaveDays) {
      return (showError("Number of Leaves applied exceeds Maximum leaves permitted"));
    }


    var employee;
    var asOnDate = today();
    var departmentId = this.state.departmentId;
    var leaveNumber = this.state.leaveNumber;
    var owner = this.state.owner;
    var tempInfo = Object.assign({}, this.state.leaveSet), type = getUrlVars()["type"];
    delete tempInfo.name;
    delete tempInfo.code;

    if(tempInfo.encashable && !tempInfo.totalWorkingDays)
    return (showError("Total Leave Days cannot be empty or zero. Please enter Total Leave Days"));

    if (tempInfo.encashable && tempInfo.totalWorkingDays && tempInfo.totalWorkingDays < 1)
      return (showError("Total Leave Days cannot be negative or zero."));

    if(tempInfo.encashable && tempInfo.totalWorkingDays && tempInfo.totalWorkingDays > tempInfo.availableDays)
    return (showError("Total Leave Days cannot be greater than available days"));



    if(tempInfo.encashable){
      tempInfo.leaveDays = tempInfo.totalWorkingDays;
    }

    
    console.log(this.state.perfixSuffix, this.state.encloseHoliday);

    let holidays = [];
    if (this.state.encloseHoliday) {
      for (let i = 0; i < this.state.encloseHoliday.length; i++)
        holidays.push(this.state.encloseHoliday[i].applicableOn)
    }

    tempInfo.prefixDate = this.state.perfixSuffix ? this.state.perfixSuffix.prefixFromDate : "";
    tempInfo.suffixDate = this.state.perfixSuffix ? this.state.perfixSuffix.suffixToDate : "";
    tempInfo.holidays = holidays;

    commonApiPost("hr-employee", "hod/employees", "_search", { tenantId, asOnDate, departmentId, active:true }, function (err, res) {
      if (res && res["Employee"] && res["Employee"][0]) {
        employee = res["Employee"][0];
      }
      else {
        return (showError("HOD does not exists for given date range Please assign the HOD."))
      }
      var assignments_designation = [];
      getDropdown("assignments_designation", function (res) {
        assignments_designation = res;
      });
      var designation;
      employee.assignments.forEach(function (item) {
        if (item.isPrimary) {
          designation = item.designation;
        }
      });
      console.log(designation);
      var hodDesignation = getNameById(assignments_designation, designation);
      var hodDetails = employee.name + " - " + employee.code + " - " + hodDesignation;
      tempInfo.workflowDetails.assignee = employee.assignments && employee.assignments[0] ? employee.assignments[0].position : "";
      var body = {
        "RequestInfo": requestInfo,
        "LeaveApplication": [tempInfo]
      }, _this = this;
      $.ajax({
        url: baseUrl + "/hr-leave/leaveapplications/_create?tenantId=" + tenantId,
        type: 'POST',
        dataType: 'json',
        data: JSON.stringify(body),

        contentType: 'application/json',
        headers: {
          'auth-token': authToken
        },
        success: function (res) {
          var leaveNumber = res.LeaveApplication[0].applicationNumber;
          window.location.href = `app/hr/leavemaster/ack-page.html?type=Apply&applicationNumber=${leaveNumber}&owner=${hodDetails}`;
        },
        error: function (err) {
          if (err.responseJSON && err.responseJSON.LeaveApplication && err.responseJSON.LeaveApplication[0] && err.responseJSON.LeaveApplication[0].errorMsg) {
            showError(err.responseJSON.LeaveApplication[0].errorMsg);
          } else {
            showError("Something went wrong. Please contact Administrator.");
          }
        }
      });
    })

  }


  render() {
    let { handleChange, addOrUpdate, handleChangeThreeLevel } = this;
    let { leaveSet, perfixSuffix, encloseHoliday } = this.state;
    let { name, code, leaveDays, availableDays, fromDate, toDate, leaveGround, reason, leaveType, totalWorkingDays, encashable } = leaveSet;
    let mode = getUrlVars()["type"];

    const renderOption = function (list) {
      if (list) {
        return list.map((item) => {
          return (<option key={item.id} value={item.id}>
            {item.name}
          </option>)
        })
      }

    }

    const showActionButton = function () {
      if (mode === "create" || !(mode)) {
        return (<button type="submit" className="btn btn-submit">Apply</button>);
      }
    };

    const renderEnclosingHolidayTr = () => {
      if (this.state.encloseHoliday) {
        if (this.state.encloseHoliday.length != 0) {
          return encloseHoliday.map((item, ind) => {
            return (
              <tr key={ind}>
                <td>{item.name}</td>
                <td>{item.applicableOn}</td>
              </tr>
            )
          })
        } else {
          return (
            <tr>
              <td colSpan="2" className="text-center">No Enclosing Holidays</td>
            </tr>
          )
        }
      }
    }

    const showEnclosingHolidayTable = () => {
      if (this.state.encloseHoliday) {
        return (
          <div>
            <div className="land-table">
              <table id="employeeTable" className="table table-bordered">
                <thead>
                  <tr>
                    <th>Holiday Name</th>
                    <th>Holiday Date </th>
                  </tr>
                </thead>
                <tbody>
                  {renderEnclosingHolidayTr()}
                </tbody>
              </table>
            </div>
          </div>
        )
      }
    }

    const showPrefix = () => {
      if (this.state.perfixSuffix && this.state.perfixSuffix.prefixFromDate && this.state.perfixSuffix.prefixToDate && !(this.state.leaveSet.encashable)) {
        return (

          <div className="row">
            <div className="col-sm-6">
              <div className="row">
                <div className="col-sm-6 label-text">
                  <label htmlFor="">Prefix From Date</label>
                </div>
                <div className="col-sm-6">
                  <input type="text" id="perfixFromDate" name="perfixFromDate" value={perfixSuffix.prefixFromDate}
                    disabled />
                </div>
              </div>
            </div>
            <div className="col-sm-6">
              <div className="row">
                <div className="col-sm-6 label-text">
                  <label htmlFor="">Prefix To Date</label>
                </div>
                <div className="col-sm-6">
                  <input type="text" id="prefixToDate" name="prefixToDate" value={perfixSuffix.prefixToDate}
                    disabled />
                </div>
              </div>
            </div>
          </div>

        )
      }
    }

    const showSuffix = () => {
      if (this.state.perfixSuffix && this.state.perfixSuffix.suffixFromDate && this.state.perfixSuffix.suffixToDate && !(this.state.leaveSet.encashable)) {
        return (

          <div className="row">
            <div className="col-sm-6">
              <div className="row">
                <div className="col-sm-6 label-text">
                  <label htmlFor="">Suffix From Date</label>
                </div>
                <div className="col-sm-6">
                  <input type="text" id="suffixFromDate" name="suffixFromDate" value={perfixSuffix.suffixFromDate}
                    disabled />
                </div>
              </div>
            </div>
            <div className="col-sm-6">
              <div className="row">
                <div className="col-sm-6 label-text">
                  <label htmlFor="">Suffix To Date</label>
                </div>
                <div className="col-sm-6">
                  <input type="text" id="suffixToDate" name="suffixToDate" value={perfixSuffix.suffixToDate}
                    disabled />
                </div>
              </div>
            </div>
          </div>

        )
      }
    }

    const showEncashable = () => {

      if (this.state.leaveSet.leaveType.id) {
        let encashableLeaveType = getNameById(this.state.leaveList, this.state.leaveSet.leaveType.id, "encashable");
        if (encashableLeaveType || encashableLeaveType == "TRUE" || encashableLeaveType == "true") {

          return (

            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">En-cashable</label>
                  </div>
                  <div className="col-sm-6">
                    <input type="checkbox" id="encashable" name="encashable" checked={encashable}
                      onChange={(e) => { handleChange(e, "encashable") }} />
                  </div>
                </div>
              </div>
            </div>

          )
        }
      }
    }

    const showDateRange = () => {

      if (!this.state.leaveSet.encashable) {

        return (

          <div>

            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">From Date <span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <div className="text-no-ui">
                      <span><i className="glyphicon glyphicon-calendar"></i></span>
                      <input type="text" id="fromDate" name="fromDate" value="fromDate" value={fromDate}
                        onChange={(e) => { handleChange(e, "fromDate") }} required />
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">To Date <span>*</span> </label>
                  </div>
                  <div className="col-sm-6">
                    <div className="text-no-ui">
                      <span><i className="glyphicon glyphicon-calendar"></i></span>
                      <input type="text" id="toDate" name="toDate" value={toDate}
                        onChange={(e) => {
                          handleChange(e, "toDate")
                        }} required />
                    </div>
                  </div>
                </div>
              </div>
            </div>


            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Working Days</label>
                  </div>
                  <div className="col-sm-6">

                    <input type="number" id="leaveDays" name="leaveDays" value={leaveDays}
                      onChange={(e) => { handleChange(e, "leaveDays") }} disabled />
                  </div>
                </div>
              </div>

            </div>

          </div>

        )
      }

    }

    return (
      <div>
        <h3>{getUrlVars()["type"] ? titleCase(getUrlVars()["type"]) : "Apply"} Leave Application </h3>
        <form onSubmit={(e) => { addOrUpdate(e) }}>
          <fieldset>

            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Employee Name</label>
                  </div>
                  <div className="col-sm-6">
                    <input type="text" id="name" name="name" value={name}
                      onChange={(e) => { handleChange(e, "name") }} />
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Employee Code</label>
                  </div>
                  <div className="col-sm-6">
                    <input type="text" id="code" name="code" value={code}
                      onChange={(e) => { handleChange(e, "code") }} />
                  </div>
                </div>
              </div>
            </div>

            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="leaveType">Leave Type<span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <div className="styled-select">
                      <select id="leaveType" name="leaveType" value={leaveType.id} required="true" onChange={(e) => {
                        handleChangeThreeLevel(e, "leaveType", "id")
                      }}>
                        <option value=""> Select Leave Type</option>
                        {renderOption(this.state.leaveList)}
                      </select>
                    </div>
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="Reason">Reason <span>*</span></label>
                  </div>
                  <div className="col-sm-6">
                    <textarea rows="4" cols="50" id="reason" name="reason" value={reason}
                      onChange={(e) => { handleChange(e, "reason") }} required></textarea>
                  </div>
                </div>
              </div>
            </div>

            {showEncashable()}
            {showDateRange()}

            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Available Leave</label>
                  </div>
                  <div className="col-sm-6">
                    <input type="number" id="availableDays" name="availableDays" value={availableDays}
                      onChange={(e) => { handleChange(e, "availableDays") }} />
                  </div>
                </div>
              </div>
            </div>

            {showPrefix()}
            {showSuffix()}

            <div className="row">
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Total Leave Days</label>
                  </div>
                  <div className="col-sm-6">

                    <input type="number" id="totalWorkingDays" name="totalWorkingDays" value={totalWorkingDays}
                      onChange={(e) => { handleChange(e, "totalWorkingDays") }} disabled />
                  </div>
                </div>
              </div>
              <div className="col-sm-6">
                <div className="row">
                  <div className="col-sm-6 label-text">
                    <label htmlFor="">Leave Grounds <span>*</span></label>
                  </div>
                  <div className="col-sm-6">

                    <input type="text" id="leaveGround" name="leaveGround" value={leaveGround}
                      onChange={(e) => { handleChange(e, "leaveGround") }} required />
                  </div>
                </div>
              </div>

            </div>


            {showEnclosingHolidayTable()}


            <div className="text-center">
              {showActionButton()} &nbsp;&nbsp;
            <button type="button" className="btn btn-close" onClick={(e) => { this.close() }}>Close</button>

            </div>
          </fieldset>
        </form>
      </div>);

  }
}






ReactDOM.render(
  <ApplyLeave />,
  document.getElementById('root')
);
