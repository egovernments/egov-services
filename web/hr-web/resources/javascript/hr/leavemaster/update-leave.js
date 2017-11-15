class UpdateLeave extends React.Component {
  constructor(props) {
    super(props);
    this.state = {
        list: [],
        leaveSet: {
            "employee": "",
            "name": "",
            "code": "",
            "leaveType": {
                "id": ""
            },
            "fromDate": "",
            "toDate": "",
            "availableDays": "",
            "leaveDays": "",
            "reason": "",
            "status": "",
            "stateId": "",
            "tenantId": tenantId,
            "workflowDetails": {
                "department": "",
                "designation": "",
                "assignee": "",
                "action": "",
                "status": ""
            }
        },leaveNumber:"",employeeid:"",positionId:"",
        leaveList: [],
        buttons: []
    }
    this.handleChange = this.handleChange.bind(this);
    this.handleChangeThreeLevel = this.handleChangeThreeLevel.bind(this);
    this.getPrimaryAssigmentDep = this.getPrimaryAssigmentDep.bind(this);
    this.handleProcess = this.handleProcess.bind(this);
}


  componentDidMount() {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = window.location.origin + logo_ele[0].getAttribute("src");
       }
     }
    var type = getUrlVars()["type"], _this = this;
    var stateId = getUrlVars()["stateId"];
    var asOnDate = _this.state.leaveSet.toDate;
    var process = [], employee;
    var  _leaveSet = {};
    var  hrConfigurations = [], allHolidayList = [];
    $('#availableDays,#leaveDays,#name,#code').prop("disabled", true);

    commonApiPost("hr-masters", "hrconfigurations", "_search", {
          tenantId
    }, function(err, res) {
      if(res) {
        hrConfigurations = res;
      }
    })
    commonApiPost("egov-common-masters", "holidays", "_search", {
        tenantId
    }, function(err, res) {
      allHolidayList = res ? res.Holiday : [];
    });

    getCommonMaster("hr-leave", "leavetypes", function(err, res) {
      if(res) {
        _this.setState({
          leaveList: res.LeaveType
        })
      }
    })

    commonApiPost("hr-leave","leaveapplications", "_search", {tenantId, stateId}, function(err, res) {
      if(res && res.LeaveApplication && res.LeaveApplication[0]) {
        _leaveSet = res.LeaveApplication[0];
        commonApiPost("hr-masters", "hrstatuses","_search",{tenantId, id:_leaveSet.status},function(err, res2){
          if(res2 && res2.HRStatus && res2.HRStatus[0]){
            var _status = res2.HRStatus[0];
            if(_status.code!="REJECTED") {
              $("input,select,textarea").prop("disabled", true);
            }
          } else {
            showError("Something went wrong please contact Administrator");
          }
        });
        commonApiPost("hr-employee", "employees", "_search", {
            tenantId,
            id: _leaveSet.employee
        }, function(err, res1)  {
          if(res1 && res1.Employee && res1.Employee[0]) {
            employee = res1.Employee[0];
            _leaveSet.name = employee.name;
            _leaveSet.code = employee.code;
            _this.setState({
               leaveSet: _leaveSet,
               leaveNumber: _leaveSet.applicationNumber,
               departmentId: _this.getPrimaryAssigmentDep(employee,"department"),
               employeeid:employee.id
            })
          } else {
            showError("Something went wrong please contact Administrator");
          }
        });
      }
      else {
        showError("Something went wrong please contact Administrator");
      }
    });

        $('#fromDate, #toDate').datepicker({
            format: 'dd/mm/yyyy',
            autoclose:true

        });
        $('#fromDate, #toDate').on("change", function(e) {
          var _from = $('#fromDate').val();
          var _to = $('#toDate').val();
          var _triggerId = e.target.id;
          if(_from && _to) {
            var dateParts1 = _from.split("/");
            var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
            var date1 = new Date(newDateStr);
            var dateParts2 = _to.split("/");
            var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
            var date2 = new Date(newDateStr);
            if (date1 > date2) {
              showError("From date must be before End date.");
              $('#' + _triggerId).val("");
            } else {
              var holidayList = [], m1 = dateParts1[1], m2 = dateParts2[1], y1 = dateParts1[2], y2 = dateParts2[2];
              for(var i=0; i<allHolidayList.length;i++) {
                if(allHolidayList[i].applicableOn && +allHolidayList[i].applicableOn.split("/")[1] >= +m1 && +allHolidayList[i].applicableOn.split("/")[1] <= +m2 && +allHolidayList[i].applicableOn.split("/")[2] <= y1 && +allHolidayList[i].applicableOn.split("/")[2] >= y2) {
                  holidayList.push(new Date(allHolidayList[i].applicableOn.split("/")[2], allHolidayList[i].applicableOn.split("/")[1]-1, allHolidayList[i].applicableOn.split("/")[0]).getTime());
                }
              }
              //Calculate working days
              var _days = 0;
              var parts1 = $('#fromDate').val().split("/");
              var parts2 = $('#toDate').val().split("/");
              var startDate = new Date(parts1[2], (+parts1[1]-1), parts1[0]);
              var endDate = new Date(parts2[2], (+parts2[1]-1), parts2[0]);

              if(hrConfigurations["HRConfiguration"]["Weekly_holidays"][0] == "5-day week") {
                for (var d = startDate ; d <= endDate; d.setDate(d.getDate() + 1)) {
                    if(holidayList.indexOf(d.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0]!="Y" && !(d.getDay()===0||d.getDay()===6))
                        _days++;
                 }
              } else if (hrConfigurations["HRConfiguration"]["Weekly_holidays"][0] == "5-day week with 2nd Saturday holiday") {
                for (var d = startDate ; d <= endDate; d.setDate(d.getDate() + 1)) {
                    if(holidayList.indexOf(d.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0]!="Y" && !_this.isSecondSat(d) && d.getDay() != 0)
                        _days++;
                }
              } else if (hrConfigurations["HRConfiguration"]["Weekly_holidays"][0] == "5-day week with 2nd and 4th Saturday holiday"){
                for (var d = startDate ; d <= endDate; d.setDate(d.getDate() + 1)) {
                    if(holidayList.indexOf(d.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0]!="Y" && d.getDay() != 0 && !_this.isSecondSat(d) && !_this.isFourthSat(d))
                        _days++;
                }
              } else {
                for (var d = startDate ; d <= endDate; d.setDate(d.getDate() + 1)) {
                    if(holidayList.indexOf(d.getTime()) == -1 && hrConfigurations["HRConfiguration"]["Include_enclosed_holidays"][0]!="Y" && !(d.getDay()===0))
                      _days++;
                }
              }

              _this.setState({
                leaveSet: {
                    ..._this.state.leaveSet,
                    [_triggerId]: $("#" + _triggerId).val(),
                    leaveDays: _days
                }
              });


              setTimeout(function() {
                if (_this.state.leaveSet.toDate && _this.state.leaveSet.leaveType.id) {
                      var leaveType = _this.state.leaveSet.leaveType.id;
                      var asOnDate = _this.state.leaveSet.toDate;
                      var employeeid = getUrlVars()["id"] || _this.state.leaveSet.employee;;
                      commonApiPost("hr-leave","eligibleleaves","_search",{
                        leaveType,tenantId,asOnDate,employeeid
                      }, function(err, res) {
                        if(res) {
                          var _day =  res && res["EligibleLeave"] && res["EligibleLeave"][0] ? res["EligibleLeave"][0].noOfDays : "";
                          if( _day <=0 || _day =="") {

                            _this.setState({
                              leaveSet:{
                                ..._this.state.leaveSet,
                                availableDays: ""
                              }
                            });
                            return (showError("You do not have leave for this leave type."));
                          }
                          else{
                            _this.setState({
                              leaveSet:{
                                ..._this.state.leaveSet,
                                availableDays: _day
                              }
                            });
                          }
                        }
                      });
                }
              }, 200);
            }
          } else {
            _this.setState({
              leaveSet: {
                  ..._this.state.leaveSet,
                  [_triggerId]: $("#" + _triggerId).val()
              }
            });
          }
        });

        commonApiPost("egov-common-workflows", "process", "_search", {
          tenantId: tenantId,
          id: stateId
        },function(err,res){
          if(res){
            process = res["processInstance"];
            if (process && process.attributes && process.attributes.validActions && process.attributes.validActions.values && process.attributes.validActions.values.length) {
                var _btns = [];
                for (var i = 0; i < process.attributes.validActions.values.length; i++) {
                    if (process.attributes.validActions.values[i].key) {
                        _btns.push({
                          key: process.attributes.validActions.values[i].key,
                          name: process.attributes.validActions.values[i].name
                        });
                    }
                }

                _this.setState({
                  positionId:process.owner.id,
                  buttons: _btns.length ? _btns : []
                })
            }
          }
        })
    }


    getPrimaryAssigmentDep(obj,type)
    {
      for (var i = 0; i < obj.assignments.length; i++) {
        if(obj.assignments[i].isPrimary)
        {
          return obj.assignments[i][type];
        }
      }
    }


    handleChangeThreeLevel(e,pName,name) {
      var _this = this , val = e.target.value;
      if(pName=="leaveType" && _this.state.leaveSet.toDate){

          var leaveType = val;
          var asOnDate = _this.state.leaveSet.toDate;
          var employeeid = getUrlVars()["id"] || _this.state.leaveSet.employee;
          commonApiPost("hr-leave","eligibleleaves","_search",{leaveType,tenantId,asOnDate,employeeid}, function(err, res) {
            if(res) {
              var _day =  res && res["EligibleLeave"] && res["EligibleLeave"][0] ? res["EligibleLeave"][0].noOfDays : "";
                if(_day <=0 || _day ==""){
                  _this.setState({
                    leaveSet:{
                      ..._this.state.leaveSet,
                      availableDays:  "",
                      [pName]:{
                          ..._this.state.leaveSet[pName],
                          [name]:""
                    }
                  }
                });
                  return (showError("You do not have leave for this Leave Type."));
                }
                else {
                  _this.setState({
                  leaveSet:{
                    ..._this.state.leaveSet,
                    availableDays: _day,
                    [pName]:{
                        ..._this.state.leaveSet[pName],
                        [name]:val
                  }
                }
                });
              }
            }
          });

      } else {
        this.setState({
            leaveSet:{
                ...this.state.leaveSet,
                [pName]:{
                    ...this.state.leaveSet[pName],
                    [name]:e.target.value
                }
            }
        })
      }
    }

  handleChange(e,name) {
    this.setState({
        leaveSet:{
            ...this.state.leaveSet,
            [name]:e.target.value
        }
    })

}


close(){
    // widow.close();
    open(location, '_self').close();
}

handleProcess(e) {
    e.preventDefault();
    var ID = e.target.id,_this = this, employee= {}, owner;
    var stateId = getUrlVars()["stateId"];
    var _this = this;

    if( _this.state.availableDays<=0) {
      return (showError("You do not have leave for this leave type."));
    }

            if (ID === "Submit") {
                var employee;
                var today = new Date();
                var asOnDate = today.getDate() + "/" + (today.getMonth() + 1) + "/" + today.getFullYear();
                var departmentId = _this.state.departmentId;
                var leaveNumber = _this.state.leaveNumber;
                var tempInfo = Object.assign({}, _this.state.leaveSet),
                    type = getUrlVars()["type"];
                delete tempInfo.name;
                delete tempInfo.code;
                commonApiPost("hr-employee", "hod/employees", "_search", { tenantId, asOnDate, departmentId }, function(err, res2) {
                  if(res2 && res2["Employee"] && res2["Employee"][0]){
                    employee = res2["Employee"][0];
                    var hodname = employee.name;
                    if (!tempInfo.workflowDetails) {
                        tempInfo.workflowDetails = {
                            action: ID,
                            assignee: employee.assignments && employee.assignments[0] ? employee.assignments[0].position : ""
                        };
                    } else {
                      tempInfo.workflowDetails.action = ID,
                      tempInfo.workflowDetailsassignee= employee.assignments && employee.assignments[0] ? employee.assignments[0].position : ""
                      }
                    var body = {
                        "RequestInfo": requestInfo,
                        "LeaveApplication": tempInfo
                    };

                    $.ajax({
                        url: baseUrl + "/hr-leave/leaveapplications/" + _this.state.leaveSet.id + "/" + "_update?tenantId=" + tenantId,
                        type: 'POST',
                        dataType: 'json',
                        data: JSON.stringify(body),

                        contentType: 'application/json',
                        headers: {
                            'auth-token': authToken
                        },
                        success: function(res) {
                            window.location.href = `app/hr/leavemaster/ack-page.html?type=Submit&applicationNumber=${leaveNumber}&owner=${hodname}`;

                        },
                        error: function(err) {
                            showError(err);

                        }
                    });
                  }
                  else{
                    return  (showError("HOD does not exists for given date range Please assign the HOD."))
                  }

                });
            } else {
                var employee;
                var type;
                var asOnDate = _this.state.leaveSet.toDate;
                var departmentId = _this.state.departmentId;
                var leaveNumber = _this.state.leaveNumber;
                var tempInfo = Object.assign({}, _this.state.leaveSet);
                delete tempInfo.name;
                delete tempInfo.code;
                if (!tempInfo.workflowDetails) {

                    tempInfo.workflowDetails = { action: ID };
                } else {
                    tempInfo.workflowDetails.action = ID;
                }
                var body = {
                    "RequestInfo": requestInfo,
                    "LeaveApplication": tempInfo
                };

                $.ajax({
                    url: baseUrl + "/hr-leave/leaveapplications/" + _this.state.leaveSet.id + "/" + "_update?tenantId=" + tenantId,
                    type: 'POST',
                    dataType: 'json',
                    data: JSON.stringify(body),

                    contentType: 'application/json',
                    headers: {
                        'auth-token': authToken
                    },
                    success: function(res) {

                        if (ID == "Approve" || ID == "Cancel")
                            window.location.href = `app/hr/leavemaster/ack-page.html?type=${ID}&applicationNumber=${leaveNumber}`;
                        else
                        {
                          commonApiPost("egov-common-workflows", "process", "_search", {
                            tenantId: tenantId,
                            id: stateId
                          },function(err,res){
                            if(res){
                              var process = res["processInstance"];
                              if (process) {
                                  var positionId = process.owner.id;
                                  console.log(positionId);
                                  commonApiPost("hr-employee", "employees", "_search", {
                                      tenantId,
                                      positionId: positionId
                                  }, function(err, res) {
                                      if(res && res["Employee"] && res["Employee"][0]) {
                                          employee = res["Employee"][0];
                                          owner = employee.name;
                                          window.location.href = `app/hr/leavemaster/ack-page.html?type=Submit&applicationNumber=${leaveNumber}&owner=${owner}`;
                                    }
                                    else{
                                      return  (showError("Unable to fetch Employee details after forwarding."))
                                    }

                                  });

                              }
                            }
                          })

                        }
                    },
                    error: function(err) {
                        showError(err);

                    }
                });
            }
          }




  render() {
    let {handleChange, handleChangeThreeLevel, handleProcess}=this;
    let {leaveSet, buttons}=this.state;
    let {name,code,leaveDays,availableDays,fromDate,toDate,reason,leaveType}=leaveSet;
    let mode=getUrlVars()["type"];

    const renderProcesedBtns = function() {
      if(buttons.length) {
        return buttons.map(function(btn, ind) {
          return (
            <button key={ind} id={btn.key} type='button' className='btn btn-submit' onClick={(e)=>{handleProcess(e)}}>
              {btn.name}&nbsp;
            </button>
          )
        })
      }
    }

    const renderOption = function(list) {
      if(list) {
        return list.map((item)=>
        {
            return (<option key={item.id} value={item.id}>
                    {item.name}
              </option>)
        })
      }
    }


    return (
      <div>
        <form>
          <fieldset>
              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Employee Name</label>
                          </div>
                          <div className="col-sm-6">
                              <input type="text" id="name" name="name" value={name}
                              onChange={(e)=>{handleChange(e,"name")}}/>
                              </div>
                          </div>
                    </div>
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
                    </div>


                <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">From Date </label>
                          </div>
                          <div className="col-sm-6">
                          <div className="text-no-ui">
                          <span><i className="glyphicon glyphicon-calendar"></i></span>
                          <input type="text" id="fromDate" name="fromDate" value="fromDate" value={fromDate}
                          onChange={(e)=>{handleChange(e,"fromDate")}}required/>

                          </div>
                      </div>
                    </div>
                </div>
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="">To Date  </label>
                            </div>
                            <div className="col-sm-6">
                            <div className="text-no-ui">
                          <span><i className="glyphicon glyphicon-calendar"></i></span>
                          <input type="text"  id="toDate" name="toDate" value={toDate}
                          onChange={(e)=>{
                              handleChange(e,"toDate")}}required/>
                          </div>
                      </div>
                  </div>
                </div>
            </div>


            <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                            <label for="leaveType">Leave Type </label>
                        </div>
                        <div className="col-sm-6">
                            <div className="styled-select">
                            <select id="leaveType" name="leaveType" value={leaveType.id} required="true" onChange={(e)=>{
                                handleChangeThreeLevel(e,"leaveType","id")
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
                              <label for="Reason">Reason </label>
                          </div>
                          <div className="col-sm-6">
                          <textarea rows="4" cols="50" id="reason" name="reason" value={reason}
                          onChange={(e)=>{handleChange(e,"reason")}}required></textarea>
                          </div>
                      </div>
                  </div>
              </div>


              <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Working Days</label>
                          </div>
                          <div className="col-sm-6">

                              <input type="number" id="leaveDays" name="leaveDays" value={leaveDays}
                              onChange={(e)=>{handleChange(e,"leaveDays")}}/>
                          </div>
                      </div>
                  </div>
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="">Available Leave</label>
                            </div>
                            <div className="col-sm-6">
                                <input  type="number" id="availableDays" name="availableDays" value={availableDays}
                                onChange={(e)=>{handleChange(e,"availableDays")}}/>
                            </div>
                        </div>
                      </div>
                  </div>



            <div className="text-center">
            {renderProcesedBtns()}
            <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>

            </div>
          </fieldset>
          </form>
      </div> );

  }
}







ReactDOM.render(
  <UpdateLeave />,
  document.getElementById('root')
);
