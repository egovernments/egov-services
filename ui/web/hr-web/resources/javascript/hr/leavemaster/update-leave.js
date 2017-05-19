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
    this.getPrimaryAssigmentDep=this.getPrimaryAssigmentDep.bind(this);
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


      try {
        var _leaveSet = commonApiPost("hr-leave","leaveapplications","_search",{tenantId,stateId}).responseJSON["LeaveApplication"][0];
        var employee = commonApiPost("hr-employee", "employees", "_search", {
            tenantId,
            id: _leaveSet.employee
        }).responseJSON["Employee"][0];
        _leaveSet.name = employee.name;
        _leaveSet.code = employee.code;
      } catch(e) {
        console.log(e);
      }
        _this.setState({
           leaveSet: _leaveSet,
           departmentId:this.getPrimaryAssigmentDep(employee,"department"),
           leaveNumber:_leaveSet.applicationNumber,
           employeeid:employee.id
        })

      if(_leaveSet.status!="REJECTED"){
          $("input,select,textarea").prop("disabled", true);
        }

        $('#fromDate').datepicker({
            format: 'dd/mm/yyyy',
            autoclose:true

        });
        $('#fromDate').on("change", function(e) {
          var from = $('#fromDate').val();
          var to = $('#toDate').val();
          var dateParts = from.split("/");
          var newDateStr = dateParts[1] + "/" + dateParts[0] + "/ " + dateParts[2];
          var date1 = new Date(newDateStr);

          var dateParts = to.split("/");
          var newDateStr = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2];
          var date2 = new Date(newDateStr);
          if (date1 > date2) {
              showError("From date must be before of End date");
              $('#fromDate').val("");
          }
          _this.setState({
                leaveSet: {
                    ..._this.state.leaveSet,
                    "fromDate":$("#fromDate").val()
                }
          })

          });

          $('#toDate').datepicker({
              format: 'dd/mm/yyyy',
              autoclose:true

          });
          $('#toDate').on("change", function(e) {
            var from = $('#fromDate').val();
            var to = $('#toDate').val();
            var dateParts = from.split("/");
            var newDateStr = dateParts[1] + "/" + dateParts[0] + "/ " + dateParts[2];
            var date1 = new Date(newDateStr);

            var dateParts = to.split("/");
            var newDateStr = dateParts[1] + "/" + dateParts[0] + "/" + dateParts[2];
            var date2 = new Date(newDateStr);
            if (date1 > date2) {
                showError("End date must be after of From date");
                $('#toDate').val("");
            }

            _this.setState({
                  leaveSet: {
                      ..._this.state.leaveSet,
                      "toDate":$("#toDate").val()
                  }

            })
            var start = $('#fromDate').datepicker('getDate');
            var end   = $('#toDate').datepicker('getDate');
            var timeDiff = 1 + Math.round(end.getTime() - start.getTime());
                 var days = Math.ceil(timeDiff / (1000 * 3600 * 24));

                 if(days){
                       // Subtract two weekend days for every week in between
                       var weeks = Math.floor(days / 7);
                       days = days - (weeks * 2);

                       // Handle special cases
                       var startDay = start.getDay();
                       var endDay = end.getDay();

                       // Remove weekend not previously removed.
                       if (startDay - endDay > 1)
                           days = days - 2;

                       // Remove start day if span starts on Sunday but ends before Saturday
                       if (startDay == 0 && endDay != 6)
                           days = days - 1;

                       // Remove end day if span ends on Saturday but starts after Sunday
                       if (endDay == 6 && startDay != 0)
                           days = days - 1;
                   }

                   if (_this.state.leaveSet.toDate && _this.state.leaveSet.leaveType.id) {
                     try{
                        var leaveType = _this.state.leaveSet.leaveType.id;
                        var asOnDate = _this.state.leaveSet.toDate;
                        var employeeid = _this.state.employeeid;
                        var object =  commonApiPost("hr-leave","eligibleleaves","_search",{leaveType,tenantId,asOnDate,employeeid}).responseJSON["EligibleLeave"][0];
                         _this.setState({
                             leaveSet:{
                                 ..._this.state.leaveSet,
                                 availableDays: object.noOfDays,
                                 leaveDays:days
                             }
                         })

                         }
                         catch (e){
                           console.log(e);
                         }
                    } else{
                      _this.setState({
                        leaveSet:{
                            ..._this.state.leaveSet,
                            leaveDays:days
                          }
                      })
                  }
            });


        try{
          var process = commonApiPost("egov-common-workflows", "process", "_search", {
          tenantId: tenantId,
          id: stateId
          }).responseJSON["processInstance"];
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
      } catch(e){
        console.log(e);
      }
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

    componentWillMount()
    {
      try {
          var _leaveTypes = getCommonMaster("hr-leave", "leavetypes", "LeaveType").responseJSON["LeaveType"] || [];
      } catch(e) {
          var _leaveTypes = [];
      }
      this.setState({
        leaveList: _leaveTypes

    })
    }


  handleChangeThreeLevel(e,pName,name) {
    var _this=this;
    if(pName=="leaveType"&&_this.state.leaveSet.toDate){

      try{
      var leaveType = e.target.value;
      var asOnDate = _this.state.leaveSet.toDate;
      var employeeid = getUrlVars()["id"] || _this.state.leaveSet.employee;
        var object =  commonApiPost("hr-leave","eligibleleaves","_search",{leaveType,tenantId,asOnDate,employeeid}).responseJSON["EligibleLeave"][0];
          this.setState({
            leaveSet:{
              ...this.state.leaveSet,
              availableDays: object.noOfDays,
              [pName]:{
                  ...this.state.leaveSet[pName],
                  [name]:e.target.value
              }

            }
          })
        }
        catch (e) {
          console.log(e);
        }
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
  var ID = e.target.id,  _this=this ;
  //Make your server calls here for these actions/buttons
  //Please test it, I have only wrote the code, not tested - Sourabh
  try{
    var employee = commonApiPost("hr-employee", "employees", "_search", {
        tenantId,
        positionId: _this.state.positionId
    }).responseJSON["Employee"][0];
  }catch(e){
    console.log(e);
  }

    var owner = employee.name;

  if(ID==="Submit"){
    var employee;
    var asOnDate = _this.state.leaveSet.toDate;
    var departmentId = _this.state.departmentId;
    var leaveNumber = _this.state.leaveNumber;
    var tempInfo=Object.assign({},_this.state.leaveSet) , type = getUrlVars()["type"];
    delete  tempInfo.name;
    delete tempInfo.code;
    var res = commonApiPost("hr-employee","hod/employees","_search",{tenantId,asOnDate,departmentId})
      if(res && res.responseJSON && res.responseJSON["Employee"] && res.responseJSON["Employee"][0]){
        employee = res.responseJSON["Employee"][0]
      }
      else{
        employee={};
      }

    if(!tempInfo.workflowDetails){
      tempInfo.workflowDetails = {action : ID,
                                  assignee : employee.assignments && employee.assignments[0] ? employee.assignments[0].position : ""};
    }
    var body={
        "RequestInfo":requestInfo,
        "LeaveApplication":tempInfo
      };

          $.ajax({
                url:baseUrl+"/hr-leave/leaveapplications/" + _this.state.leaveSet.id + "/" + "_update?tenantId=" + tenantId,
                type: 'POST',
                dataType: 'json',
                data:JSON.stringify(body),

                contentType: 'application/json',
                headers:{
                  'auth-token': authToken
                },
                success: function(res) {
                    window.location.href=`app/hr/leavemaster/ack-page.html?type=Submit&applicationNumber=${leaveNumber}&owner=${owner}`;

                },
                error: function(err) {
                    showError(err);

                }
            });
  }else{
      var employee;
      var type;
      var asOnDate = _this.state.leaveSet.toDate;
      var departmentId = _this.state.departmentId;
      var leaveNumber = _this.state.leaveNumber;
      var tempInfo=Object.assign({},_this.state.leaveSet);
      delete  tempInfo.name;
      delete tempInfo.code;
      if(!tempInfo.workflowDetails){

        tempInfo.workflowDetails = {action : ID};
      }
      var body={
          "RequestInfo":requestInfo,
          "LeaveApplication":tempInfo
        };

            $.ajax({
                  url:baseUrl+"/hr-leave/leaveapplications/" + _this.state.leaveSet.id + "/" + "_update?tenantId=" + tenantId,
                  type: 'POST',
                  dataType: 'json',
                  data:JSON.stringify(body),

                  contentType: 'application/json',
                  headers:{
                    'auth-token': authToken
                  },
                  success: function(res) {

                    if(ID=="Approve"|| ID== "Cancel")
                      window.location.href=`app/hr/leavemaster/ack-page.html?type=${ID}&applicationNumber=${leaveNumber}`;
                    else
                      window.location.href=`app/hr/leavemaster/ack-page.html?type=${ID}&applicationNumber=${leaveNumber}&owner=${owner}`;
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
                            <label for="">From Date <span>*</span></label>
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
                              <label for="">To Date <span>*</span> </label>
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
                            <label for="leaveType">Leave Type<span>*</span></label>
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
                              <label for="Reason">Reason <span>*</span></label>
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
