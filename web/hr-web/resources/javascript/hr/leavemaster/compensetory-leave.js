class CompensetoryLeave extends React.Component {
  constructor(props) {
    super(props);
    this.state={
      compensetorySet: {
        "employee": "",
        "name":"",
        "code":"",
        // entered date
         "fromDate" : "",
         "toDate": "",
         //worked on date
         "compensatoryForDate" : "",
         "leaveDays":"",
         "tenantId" : tenantId,
         "workflowDetails": {
          "assignee": "",
          "action": "apply",
          }
        },
        "workedOnDays":[],
        "departmentId":""
      }

    this.handleChange=this.handleChange.bind(this);
    this.addOrUpdate=this.addOrUpdate.bind(this);

  }


  handleChange(e, name) {
    this.setState({
        compensetorySet:{
            ...this.state.compensetorySet,
            [name]:e.target.value
        }
    })
  }

  close() {
      open(location, '_self').close();
  }

addOrUpdate(e,mode) {
  e.preventDefault();

  if(this.state.compensetorySet.fromDate && this.state.compensetorySet.compensatoryForDate){

    var  _to= this.state.compensetorySet.fromDate;
    var _from = this.state.compensetorySet.compensatoryForDate;
    if (_from && _to) {
        var dateParts1 = _from.split("/");
        var newDateStr = dateParts1[1] + "/" + dateParts1[0] + "/ " + dateParts1[2];
        var date1 = new Date(newDateStr);
        var dateParts2 = _to.split("/");
        var newDateStr = dateParts2[1] + "/" + dateParts2[0] + "/" + dateParts2[2];
        var date2 = new Date(newDateStr);
        var daysDiff = Math.round((date2-date1)/(1000*60*60*24));
        if (daysDiff > 90) {
            return  (showError("Compensatory Leave Day should not be greater than Worked on Date by 90 days"));
        }else if(date2<date1){
          return  (showError("Compensatory Leave Day should not be before Worked on Date"));
        }
    }
  }

  var employee;
  var today = new Date();
  var asOnDate = today.getDate() + "/" + (today.getMonth() + 1) + "/" + today.getFullYear();
  var departmentId = this.state.departmentId;
  var leaveNumber = this.state.leaveNumber;
  var tempInfo=Object.assign({},this.state.compensetorySet);
  delete tempInfo.name;
  delete tempInfo.code;
  tempInfo.toDate = tempInfo.fromDate;
  tempInfo.leaveDays = 1;
  commonApiPost("hr-employee","hod/employees","_search",{tenantId,asOnDate,departmentId}, function(err, res) {
      if(res && res["Employee"] && res["Employee"][0]){
        employee = res["Employee"][0];
      }
      else{
        return  (showError("HOD does not exists for given date range Please assign the HOD."))
      }
      var assignments_designation=[];
      getDropdown("assignments_designation", function(res) {
        assignments_designation = res;
      });
      var designation;
      employee.assignments.forEach(function(item) {
                          if(item.isPrimary)
                          {
                            designation = item.designation;
                          }
                      });
      var hodDesignation = getNameById(assignments_designation,designation);
      var hodDetails = employee.name + " - " + employee.code + " - " + hodDesignation;
      tempInfo.workflowDetails.assignee = employee.assignments && employee.assignments[0] ? employee.assignments[0].position : "";
      var body={
        "RequestInfo":requestInfo,
        "LeaveApplication":[tempInfo]
      }, _this=this;
          $.ajax({
                url: baseUrl+"/hr-leave/leaveapplications/_create?tenantId=" + tenantId + "&encashable=false",
                type: 'POST',
                dataType: 'json',
                data:JSON.stringify(body),

                contentType: 'application/json',
                headers:{
                  'auth-token': authToken
                },
                success: function(res) {
                  var leaveNumber = res.LeaveApplication[0].applicationNumber;
                  window.location.href=`app/hr/leavemaster/ack-page.html?type=Apply&applicationNumber=${leaveNumber}&owner=${hodDetails}`;
                },
                error: function(err) {
                  if (err.responseJSON && err.responseJSON.LeaveApplication && err.responseJSON.LeaveApplication[0] && err.responseJSON.LeaveApplication[0].errorMsg) {
                    showError(err.responseJSON.LeaveApplication[0].errorMsg);
                  } else {
                    showError("Something went wrong. Please contact Administrator.");
                  }
                }
            });
  })

}


  componentDidMount() {
    if(window.opener && window.opener.document) {
       var logo_ele = window.opener.document.getElementsByClassName("homepage_logo");
       if(logo_ele && logo_ele[0]) {
         document.getElementsByClassName("homepage_logo")[0].src = (logo_ele[0].getAttribute("src") && logo_ele[0].getAttribute("src").indexOf("http") > -1) ? logo_ele[0].getAttribute("src") : window.location.origin + logo_ele[0].getAttribute("src");
     }
   }
    var type = getUrlVars()["type"], _this = this, id = getUrlVars()["id"];
    var workedOnDate = getUrlVars()["workedOnDate"];
    var code;
    $('#fromDate').datepicker({
        format: 'dd/mm/yyyy',
        autoclose: true

    });

    $('#fromDate').on("change", function(e) {
      _this.setState({
            compensetorySet: {
                ..._this.state.compensetorySet,
                "fromDate":$("#fromDate").val()
            }
      })
    });

    if (!id) {
      commonApiPost("hr-employee","employees","_loggedinemployee",{tenantId}, function(err, res) {
        if(res) {
          var obj = res.Employee[0];
          var departmentId;
          for (var i = 0; i < obj.assignments.length; i++) {
            if(obj.assignments[i].isPrimary)
              departmentId = obj.assignments[i].department;
          }

          commonApiPost("hr-leave","compensatoryleaves","_search",{tenantId, code:obj.code, pageSize: 500},function(err,res){
              if(res){
                  var workedOnDays = res["CompensatoryLeave"];
                  _this.setState({
                      workedOnDays: workedOnDays
                  })
               }
          });

          _this.setState({
            compensetorySet:{
                ..._this.state.compensetorySet,
                name:obj.name,
                code:obj.code,
                employee:obj.id,
              },
              departmentId:departmentId
          })
        }
      })
    } else {
      getCommonMasterById("hr-employee", "employees", id, function(err, res) {
        if(res) {
          var obj = res.Employee[0];
          var departmentId;
          for (var i = 0; i < obj.assignments.length; i++) {
            if(obj.assignments[i].isPrimary)
              departmentId = obj.assignments[i].department;
          }

          _this.setState({
            compensetorySet:{
                ..._this.state.compensetorySet,
                name:obj.name,
                code:obj.code,
                employee:obj.id
              },
              departmentId: departmentId
          })
        }
      });
    }

    if(workedOnDate){
      _this.setState({
        compensetorySet:{
            ..._this.state.compensetorySet,
            compensatoryForDate: workedOnDate
          },
          workedOnDays : [workedOnDate]
      })
    }

}

  render() {
      let {handleChange,addOrUpdate}=this;
      let {compensetorySet,workedOnDays}=this.state;
      let {code,name,fromDate,compensatoryForDate}=this.state.compensetorySet;
      let mode=getUrlVars()["type"];

      const showActionButton=function() {
        if((!mode) ||mode==="create")
        {
          return (<button type="submit" className="btn btn-submit">{mode?"forward":"Apply"}</button>);
        }
      };


      const renderOption=function(list) {
          if(list)
          {
              return list.map((item, ind)=>
              {
                  return (<option key={ind} value={typeof item == "object" ? item.workedDate : item}>
                          {typeof item == "object" ? item.workedDate : item}
                    </option>)
              })
          }
      }



    return (
      <div>
          <h3>{mode} Compensetory Leave</h3>
            <form onSubmit={(e)=>{addOrUpdate(e)}}>
          <fieldset>
              <div className="row">
                <div className="col-sm-6">
                    <div className="row">
                        <div className="col-sm-6 label-text">
                          <label for="">Employee Code </label>
                        </div>
                        <div className="col-sm-6">
                            <input type="text" id="code" name="code" value={code}
                              onChange={(e)=>{  handleChange(e,"code")}}/>
                        </div>
                    </div>
                  </div>
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Employee Name</label>
                          </div>
                          <div className="col-sm-6">
                              <input  type="text" id="name" name="name" value={name}
                              onChange={(e)=>{  handleChange(e,"name")}}/>

                            </div>
                          </div>
                        </div>
                    </div>
                <div className="row">
                  <div className="col-sm-6">
                      <div className="row">
                          <div className="col-sm-6 label-text">
                            <label for="">Worked On  <span>*</span></label>
                          </div>
                          <div className="col-sm-6">
                          <div className="styled-select">
                            <select name="compensatoryForDate" id="compensatoryForDate" value={compensatoryForDate} onChange={(e)=>{  handleChange(e,"compensatoryForDate")}} disabled = {getUrlVars()["workedOnDate"]?true:false} required>
                              <option value="">Select worked on date</option>
                              {renderOption(workedOnDays)}
                           </select>
                           </div>
                      </div>
                    </div>
                    </div>
                    <div className="col-sm-6">
                        <div className="row">
                            <div className="col-sm-6 label-text">
                              <label for="">Compensetory Leave On  <span>*</span></label>
                            </div>
                            <div className="col-sm-6">
                            <div className="text-no-ui">
                          <span><i className="glyphicon glyphicon-calendar"></i></span>
                          <input type="text" id="fromDate" name="fromDate" value={fromDate}
                          onChange={(e)=>{  handleChange(e,"fromDate")}} required/>

                          </div>
                          </div>
                        </div>
                      </div>
                </div>

            <div className="text-center">
                 <button type="submit"  className="btn btn-submit">Forward</button> &nbsp;&nbsp;
                <button type="button" className="btn btn-close" onClick={(e)=>{this.close()}}>Close</button>
            </div>
          </fieldset>
          </form>
      </div>
    );
  }
}




ReactDOM.render(
  <CompensetoryLeave />,
  document.getElementById('root')
);
